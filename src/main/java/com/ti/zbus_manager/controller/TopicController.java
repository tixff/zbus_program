package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {

    @Autowired
    private TopicService service;

    @GetMapping("/add")
    @ApiOperation(value = "添加topic", notes = "添加topic")
    public void addTopic(Topic topic) {
        service.addTopic(topic);
    }

    @GetMapping("find")
    @ApiOperation(value = "查找Topic", notes = "根据名字查找")
    public Topic findTopic(Topic topic) {
        return service.findTopic(topic);
    }
}
