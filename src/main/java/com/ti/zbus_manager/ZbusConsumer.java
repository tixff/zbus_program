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
            config.setConsumeTimeout(1000*3);
            config.setTopic("xff_topic");  //指定消息队列主题，同时可以指定分组通道
            System.out.println("################连接数："+config.getConnectionCount());
            Consumer consumer = new Consumer(config);
            consumer.start(new MessageHandler() {
                @Override
                public void handle(Message message, MqClient mqClient) throws IOException {
                    if (message != null) {
                        String remoteAddr = message.getRemoteAddr();
                        System.out.println("remoteAddr:" + remoteAddr);
                        System.out.println(message.getBodyString());

                    }
                }
            });
        } catch (Exception e) {
            logger.error("zbus消费队列出错:{}", e);
        }

    }
}
