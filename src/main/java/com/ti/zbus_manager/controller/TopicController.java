package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.service.TopicService;
import com.ti.zbus_manager.util.ZbusUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TopicController {

    @Autowired
    private TopicService service;

    @GetMapping("/add")
    @ApiOperation(value = "添加主题", notes = "添加主题")
    public void addTopic(Topic topic, HttpServletRequest request) {
        String ipAddress = ZbusUtils.getIpAddress(request);
        topic.setIp(ipAddress);
        service.addTopic(topic);

    }

    @GetMapping("find")
    @ApiOperation(value = "查找主题", notes = "根据主题名字查找")
    public Topic findTopic(Topic topic) {
        return service.findTopic(topic);
    }
}
