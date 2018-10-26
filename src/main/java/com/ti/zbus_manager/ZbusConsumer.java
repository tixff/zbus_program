package com.ti.zbus_manager;

import io.zbus.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ZbusConsumer {
    private static Logger logger = LoggerFactory.getLogger(ZbusConsumer.class);

    public static void main(String[] args) {
        consumerMessage();
    }

    public static void consumerMessage() {
        try {
            Broker broker = new Broker("localhost:15555");
            ConsumerConfig config = new ConsumerConfig(broker);
            config.setTopic("xff_topic");  //指定消息队列主题，同时可以指定分组通道
            config.setMessageHandler(new MessageHandler() {
                @Override
                public void handle(Message msg, MqClient client) throws IOException {
                    System.out.println(msg); //消费处理
                }
            });

            Consumer consumer = new Consumer(config);
            consumer.start();

        } catch (Exception e) {
            logger.error("zbus消费队列出错:{}", e);
        }

    }
}
