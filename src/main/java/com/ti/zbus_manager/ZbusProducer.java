package com.ti.zbus_manager;


import io.zbus.mq.Broker;
import io.zbus.mq.Message;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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


        Broker broker = new Broker("localhost:15555");

        Producer p = new Producer(broker);
        //创建topic
        //p.declareTopic("xff_topic");    //当确定队列不存在需创建

        Message msg = new Message();
        //设置topic
        msg.setTopic("xff_topic");       //设置消息主题
        //msg.setTag("oo.account.pp"); //可以设置消息标签
        msg.setBody(data);

        Message res = p.publish(msg);
        System.out.println(res);

        broker.close();

    }
}
