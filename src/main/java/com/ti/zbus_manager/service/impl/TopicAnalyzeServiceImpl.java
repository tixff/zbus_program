package com.ti.zbus_manager.service.impl;

import com.ti.zbus_manager.config.ZbusConfig;
import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.entities.TopicProduce;
import com.ti.zbus_manager.mapper.TopicAnalyzeMapper;
import com.ti.zbus_manager.mapper.TopicMapper;
import com.ti.zbus_manager.mapper.TopicProduceMapper;
import com.ti.zbus_manager.parameter.AnalyzeQueryParameter;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import com.ti.zbus_manager.util.DateUtils;
import com.ti.zbus_manager.vo.ConsumerVo;
import com.ti.zbus_manager.vo.ResultAnalyzeData;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class TopicAnalyzeServiceImpl implements TopicAnalyzeService {

    private static Logger logger = LoggerFactory.getLogger(TopicAnalyzeServiceImpl.class);
    private static final int MINUTE = 100;

    @Autowired
    private ZbusConfig zbusConfig;
    @Autowired
    private TopicAnalyzeMapper mapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicProduceMapper topicProduceMapper;

    /**
     * 获取所有主题消费信息
     *
     * @return
     */
    @Override
    public ArrayList<TopicAnalyze> getAllInfo() {
        ArrayList<TopicAnalyze> allInfo = mapper.getAllInfo();
        return allInfo;
    }

    /**
     * 添加消费者
     *
     * @param topicName
     */
    @Override
    public void addConsumer(String topicName) {
        TopicAnalyze topicAnalyze = new TopicAnalyze();
        Topic topic = topicMapper.findTopicByName(topicName);
        if (topic == null) {
            return;
        }
        try {
            Broker broker = new Broker(zbusConfig.getHost());
            ConsumerConfig config = new ConsumerConfig(broker);
            int connectCount = config.getConnectionCount();
            config.setTopic(topicName);
            topicAnalyze.setTopicName(topicName);
            topicAnalyze.setConnectCount(connectCount);
            topicAnalyze.setConnectTime(new Date());
            Consumer consumer = new Consumer(config);
            consumer.start((message, mqClient) -> {
                if (message != null) {
                    Date date = new Date();
                    int dateMinute = DateUtils.getDateMinute(date);
                    topicAnalyze.setReceivedTime(date);
                    topicAnalyze.setMinute(dateMinute);
                    String bodyString = message.getBodyString("utf-8");
                    topicAnalyze.setReceivedDesc(bodyString);
                    mapper.addInfo(topicAnalyze);
                }
            });
        } catch (Exception e) {
            logger.error("添加消费者出错");
        }
    }

    /**
     * 发送消息到给定主题并记录发送者信息
     *
     * @param topicName
     * @param message
     */
    @Override
    public void produceMessage(String topicName, String message,String ip) {
        Topic topic = topicMapper.findTopicByName(topicName);
        if (topic == null) {
            return;
        }
        Broker broker = new Broker(zbusConfig.getHost());
        Producer p = new Producer(broker);
        //创建topic
        //p.declareTopic("xff_topic");    //当确定队列不存在需创建
        Message msg = new Message();
        //设置topic
        msg.setTopic(topicName);       //设置消息主题
        msg.setBody(message);
        System.out.println(message);
        try {
            Message res = p.publish(msg);
            //记录发送者信息
            TopicProduce topicProduce = new TopicProduce();
            topicProduce.setProduceIp(ip);
            topicProduce.setProduceTime(new Date());
            topicProduce.setTopicName(topicName);
            topicProduceMapper.addTopicProduce(topicProduce);
            broker.close();
        } catch (Exception e) {
            logger.error("发送消息失败");
        }

    }

    /**
     * 获取给定时间前100分钟的消费信息
     *
     * @param date
     * @return
     */
    @Override
    public ResultAnalyzeData getConsumerAnalyzeData(Date date, String topicName) {
        ResultAnalyzeData resultData = new ResultAnalyzeData();
        ArrayList<Topic> aLlTopic = topicMapper.getALlTopic();
        ArrayList<String> topicNames = new ArrayList<>();
        aLlTopic.forEach(t ->
                topicNames.add(t.getName())
        );
        resultData.setTopicNames(topicNames);
        AnalyzeQueryParameter parameter = new AnalyzeQueryParameter();
        Date startTime = DateUtils.getMinuteAgoTime(date, MINUTE);
        parameter.setEndTime(date);
        parameter.setStartTime(startTime);
        ArrayList<TopicAnalyze> list = mapper.getAnalyzeInfoByDateRange(parameter);
        resultData.setList(list);
        resultData.setDate(date);
        resultData.setDateRange(MINUTE);
        resultData.setTopicName(topicName);
        //HashMap<String, ArrayList> resultMap = toAnalyzeData(list, date);
        return resultData;
    }

    @Override
    public ResultAnalyzeData getProduceAnalyzeData() {
        ResultAnalyzeData resultData = new ResultAnalyzeData();
        Date endDate = new Date();
        Date startTime = DateUtils.getMinuteAgoTime(endDate, MINUTE);

        AnalyzeQueryParameter parameter = new AnalyzeQueryParameter();
        parameter.setStartTime(startTime);
        parameter.setEndTime(endDate);
        ArrayList<TopicProduce> produceList = topicProduceMapper.findProduceByDateRange(parameter);
        resultData.setProduceList(produceList);
        resultData.setDate(endDate);
        return resultData;
    }

    /**
     * 对象转换
     *
     * @param topicAnalyze
     * @return
     */
    private ConsumerVo formatData(TopicAnalyze topicAnalyze) {
        ConsumerVo vo = new ConsumerVo();
        vo.setCount(topicAnalyze.getReceivedCount());
        vo.setDate(topicAnalyze.getReceivedTime());
        vo.setTopicName(topicAnalyze.getTopicName());
        return vo;
    }


}
