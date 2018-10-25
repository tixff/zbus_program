package com.ti.zbus_manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.mq.MqConfig;
import org.zbus.mq.Producer;
import org.zbus.mq.Protocol;
import org.zbus.mq.server.MqServer;
import org.zbus.net.http.Message;


public class ZbusProducer {

    private static Logger logger = LoggerFactory.getLogger(ZbusProducer.class);

    public static void main(String[] args) {
        try {
            sendMsg("###############################:hello its a first zbus message");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送失败");
        }
    }

    public static void sendMsg(String data) throws Exception {


        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setServerAddress("127.0.0.1:15555");
        SingleBroker singleBroker = new SingleBroker(brokerConfig);

        MqConfig mqConfig = new MqConfig();
        mqConfig.setMq("mq");
        mqConfig.setBroker(singleBroker);
        //mqConfig.setMode(Protocol.MqMode.PubSub);
        Producer producer = new Producer(mqConfig);
        producer.createMQ();
        Message message = new Message();
        message.setBody(data);
        //message.setTopic("mytopic");
        producer.sendAsync(message);
        //producer.sendSync(message);
        System.out.println(message);


    }
}
