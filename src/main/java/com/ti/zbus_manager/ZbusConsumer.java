package com.ti.zbus_manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.mq.Consumer;
import org.zbus.mq.MqConfig;



public class ZbusConsumer {
    private static Logger logger = LoggerFactory.getLogger(ZbusConsumer.class);

    public static void main(String[] args) {
        consumerMessage();
    }

    public static void consumerMessage() {
        try {
            BrokerConfig brokerConfig = new BrokerConfig();
            brokerConfig.setServerAddress("127.0.0.1:15555");
            SingleBroker singleBroker = new SingleBroker(brokerConfig);
            MqConfig mqConfig = new MqConfig();
            mqConfig.setBroker(singleBroker);
            mqConfig.setMq("mq");
            //mqConfig.setTopic("mytopic");
            //mqConfig.setMode(Protocol.MqMode.PubSub);
            Consumer consumer = new Consumer(mqConfig);
            //consumer.setTopic("mytopic");
            consumer.onMessage((msg,cons)->{
                System.out.println("###############:"+new String(msg.getBody()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
           consumer.start();
        } catch (Exception e) {
            logger.error("zbus消费队列出错:{}", e);
        }

    }
}
