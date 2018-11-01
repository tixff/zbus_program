package com.ti.zbus_manager.service.impl;

import com.ti.zbus_manager.config.ZbusConfig;
import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.mapper.TopicAnalyzeMapper;
import com.ti.zbus_manager.mapper.TopicMapper;
import com.ti.zbus_manager.parameter.AnalyzeQueryParameter;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import com.ti.zbus_manager.util.DateUtils;
import com.ti.zbus_manager.vo.ConsumerVo;
import com.ti.zbus_manager.vo.Serie;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class TopicAnalyzeServiceImpl implements TopicAnalyzeService {

    private static Logger logger = LoggerFactory.getLogger(TopicAnalyzeServiceImpl.class);
    private static final int THIRTY_MINUTE = 30;
    private boolean flag = false;

    @Autowired
    private ZbusConfig zbusConfig;
    @Autowired
    private TopicAnalyzeMapper mapper;
    @Autowired
    private TopicMapper topicMapper;

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
     * 发送消息到给定主题
     *
     * @param topicName
     * @param message
     */
    @Override
    public void produceMessage(String topicName, String message) {
        Topic topic = topicMapper.findTopicByName(topicName);
        if (topic == null) {
            return;
        }
        Broker broker = new Broker("localhost:15555");
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
            broker.close();
        } catch (Exception e) {
            logger.error("发送消息失败");
        }

    }

    /**
     * 获取给定时间前30分钟的消费信息
     *
     * @param date
     * @return
     */
    @Override
    public HashMap<String, ArrayList> getAnalyzeData(Date date) {
        AnalyzeQueryParameter parameter = new AnalyzeQueryParameter();
        Date startTime = DateUtils.getMinuteAgoTime(date, THIRTY_MINUTE);
        parameter.setEndTime(date);
        parameter.setStartTime(startTime);
        ArrayList<TopicAnalyze> list = mapper.getAnalyzeInfoByDateRange(parameter);
        HashMap<String, ArrayList> resultMap = toAnalyzeData(list, date);
        return resultMap;
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

    /**
     * 前端所需数据格式转换
     *
     * @param list
     * @param date
     * @return
     */
    private HashMap<String, ArrayList> toAnalyzeData(ArrayList<TopicAnalyze> list, Date date) {
        HashMap<String, ArrayList> resultMap = new HashMap<>();

        //添加30分钟的时间范围
        ArrayList<String> dateList = new ArrayList<>();
        for (int i = 0; i < THIRTY_MINUTE; i++) {
            Date minuteAgoTime = DateUtils.getMinuteAgoTime(date, THIRTY_MINUTE - i);
            minuteAgoTime = DateUtils.getNoSecondDate(minuteAgoTime);
            String minuteAgoTimeStr = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_hh_mm).format(minuteAgoTime);
            dateList.add(minuteAgoTimeStr);
        }
        resultMap.put("dateRange", dateList);

        //获取名称list
        ArrayList<String> topicNames = new ArrayList<>();
        list.forEach(o -> {
            boolean contains = topicNames.contains(o.getTopicName());
            if (!contains) {
                topicNames.add(o.getTopicName());
            }
        });

        //添加serie数据
        ArrayList<Serie> series = new ArrayList();
        topicNames.forEach(o -> {
            Serie serie = new Serie();
            serie.setName(o);
            ArrayList<Integer> data = new ArrayList<>();
            dateList.forEach(time -> {
                TopicAnalyze t = findTopicAnalyzeByNameAndTime(list, o, time);
                if (t == null) {
                    data.add(0);
                } else {
                    data.add(t.getReceivedCount());
                }
            });
            serie.setData(data);
            series.add(serie);
        });

        resultMap.put("series", series);
        return resultMap;
    }

    private TopicAnalyze findTopicAnalyzeByNameAndTime(ArrayList<TopicAnalyze> list, String topicName, String time) {
        for (TopicAnalyze l : list) {
            String name = l.getTopicName();
            Date receivedTime = l.getReceivedTime();
            String receivedTimeStr = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_hh_mm)
                    .format(DateUtils.getNoSecondDate(receivedTime));
            if (name.equals(topicName) && receivedTimeStr.equals(time)) {
                return l;
            }
        }
        return null;
    }
}
