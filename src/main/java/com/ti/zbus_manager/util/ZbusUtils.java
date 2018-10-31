package com.ti.zbus_manager.util;

import com.ti.zbus_manager.ZbusProducer;
import com.ti.zbus_manager.entities.Topic;
import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ZbusUtils {

    private static Logger logger = LoggerFactory.getLogger(ZbusUtils.class);

    public static Broker connectZbus(String inetAddress) {
        Broker broker = new Broker(inetAddress);
        return broker;
    }

    public static void disConnectZbus(Broker broker) throws Exception {
        broker.close();
    }

    public static void addTopic(Topic topic, String inetAddress) throws Exception {
        Broker broker = connectZbus(inetAddress);
        Producer p = new Producer(broker);
        //创建topic
        p.declareTopic(topic.getName());
        disConnectZbus(broker);
    }

    public static void addConsumerByTopicName(String topicName) throws Exception {
        Broker broker = new Broker("localhost:15555");
        ConsumerConfig config = new ConsumerConfig(broker);
        int connectionCount = config.getConnectionCount();
        config.setTopic(topicName);  //指定消息队列主题，同时可以指定分组通道
        Consumer consumer = new Consumer(config);
        consumer.start((message, mqClient) -> {

            String bodyString = message.getBodyString("utf-8");
            System.out.println(message);
            System.out.println(bodyString);

        });
        consumer.close();
    }

    public static void sendMessage(Producer producer, String topicName, String data) throws Exception {
        Message msg = new Message();
        //设置topic
        msg.setTopic(topicName);       //设置消息主题
        //msg.setTag("oo.account.pp"); //可以设置消息标签
        msg.setBody(data);
        System.out.println(data);
        Message res = producer.publish(msg);
        System.out.println(res);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
