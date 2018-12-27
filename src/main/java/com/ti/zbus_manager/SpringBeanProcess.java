package com.ti.zbus_manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringBeanProcess implements BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean, BeanPostProcessor, ApplicationContextAware {
    public void init() {
        System.out.println("----:init()");
    }

    public void dest() {
        System.out.println("----:dest()");
    }

    public SpringBeanProcess() {
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
        int i1  = 22;
        Integer i2 = new Integer(22);
        Integer i3  = Integer.valueOf(i2);
        System.out.println(i2==i1);
        System.out.println(i2==i3);
    }

}
