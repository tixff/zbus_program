package com.ti.zbus_manager;

import io.zbus.mq.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;


public class SpringBeanProcess implements BeanNameAware,BeanFactoryAware,InitializingBean,DisposableBean,BeanPostProcessor,ApplicationContextAware {
    public void init(){
        System.out.println("----:init()");
    }
    public  void dest(){
        System.out.println("----:dest()");
    }
    public SpringBeanProcess(){
        System.out.println("----:createBean");
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("----:setBeanFactory()");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("----:setBeanName()");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("----:destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("----:afterPropertiesSet()");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("----:postProcessAfterInitialization()");
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("----:postProcessBeforeInitialization()");
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("----:setApplicationContext()");

    }

    public static void main(String[] args) {
        try {
            Broker broker = new Broker("localhost:15555");
            ConsumerConfig config = new ConsumerConfig(broker);
            config.setConsumeTimeout(1000*3);
            System.out.println("###############连接数："+config.getConnectionCount());
            config.setTopic("xff_topic");  //指定消息队列主题，同时可以指定分组通道
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
            e.printStackTrace();
        }
    }

}
