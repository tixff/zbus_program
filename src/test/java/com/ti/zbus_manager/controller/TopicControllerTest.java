package com.ti.zbus_manager.controller;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TopicControllerTest extends AbstractControllerTest {

    //@Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        //获取mockmvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DatabaseSetup(value = "classpath:data/topic.xml", type = DatabaseOperation.CLEAN_INSERT)
    @Transactional
    @Rollback
    public void addTopic() throws Exception {
        mockMvc.perform(get("/add")
                .param("name", "tixff_topic")
                .param("ip", "192.168.5.9").param("create_time", "2018-10-19"));
        mockMvc.perform(get("/find").param("name", "tixff_topic")).andDo(print());
    }

    @Test
    @DatabaseSetup(value = "classpath:data/topic.xml", type = DatabaseOperation.CLEAN_INSERT)
    @Transactional
    @Rollback
    public void findTopic() throws Exception {
        mockMvc.perform(get("/find").param("name", "luzhou_topic"));
    }
}