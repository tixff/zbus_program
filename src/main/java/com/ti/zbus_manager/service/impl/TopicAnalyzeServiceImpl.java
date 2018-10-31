package com.ti.zbus_manager.service.impl;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.mapper.TopicAnalyzeMapper;
import com.ti.zbus_manager.mapper.TopicMapper;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import com.ti.zbus_manager.util.ZbusUtils;
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

    @Autowired
    private TopicAnalyzeMapper mapper;
    @Autowired
    private TopicMapper topicMapper;

    @Override
    public ArrayList<TopicAnalyze> getAllInfo() {
        ArrayList<TopicAnalyze> allInfo = mapper.getAllInfo();
        return allInfo;
    }

    @Override
    public void addCondumer(String topicName) {
        TopicAnalyze topicAnalyze = new TopicAnalyze();
        Topic topic = topicMapper.findTopicByName(topicName);
        if (topic == null) {
            return;
        }
        try {
            Broker broker = new Broker("localhost:15555");
            ConsumerConfig config = new ConsumerConfig(broker);
            int connectCount = config.getConnectionCount();
            config.setTopic(topicName);
            topicAnalyze.setTopicName(topicName);
            topicAnalyze.setConnectCount(connectCount);
            topicAnalyze.setConnectTime(new Date());
            Consumer consumer = new Consumer(config);
            consumer.start((message, mqClient) -> {
                if (message != null) {
                    topicAnalyze.setReceivedTime(new Date());
                    String bodyString = message.getBodyString("utf-8");
                    topicAnalyze.setReceivedDesc(bodyString);
                    mapper.addInfo(topicAnalyze);
                }
            });
        } catch (Exception e) {
            logger.error("添加消费者出错");
        }
    }

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
}
