package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.service.TopicService;
import com.ti.zbus_manager.util.ZbusUtils;
import com.ti.zbus_manager.vo.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class TopicController {

    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
    @Autowired
    private TopicService service;

    @PostMapping("/add")
    @ApiOperation(value = "添加主题", notes = "添加主题")
    public ResultMessage addTopic(Topic topic, HttpServletRequest request) {
        String ipAddress = ZbusUtils.getIpAddress(request);
        topic.setIp(ipAddress);
        try {
            service.addTopic(topic);
            return new ResultMessage("添加主题成功");
        }catch (Exception e){
            logger.error("添加主题失败");
            return new ResultMessage("添加主题失败");
        }
    }

    @GetMapping("find")
    @ApiOperation(value = "查找主题", notes = "根据主题名字查找")
    public Topic findTopic(Topic topic) {
        return service.findTopic(topic);
    }

    @PostMapping("getAllTopic")
    public ArrayList<Topic> getAllTopic(){
        ArrayList<Topic> allTopic = service.getAllTopic();
        return allTopic;
    }
}
