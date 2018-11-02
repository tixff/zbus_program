package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import com.ti.zbus_manager.util.DateUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@EnableScheduling
public class TopicAnalyzeController {
    private static Logger logger = LoggerFactory.getLogger(TopicAnalyzeController.class);
    @Autowired
    private TopicAnalyzeService service;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("getAll")
    @ApiOperation(value = "获取所有消费者信息", notes = "获取所有消费者信息")
    public void getAllInfo() {
        ArrayList<TopicAnalyze> allInfo = service.getAllInfo();
        allInfo.forEach(o -> {
            System.out.println(o);
        });
    }

    @GetMapping("addConsumer")
    @ApiOperation(value = "添加消费者", notes = "添加消费者")
    public void addConsumer(
            // @ApiParam(name = "topicName", value = "主题名称")
            String topicName) {
        service.addConsumer(topicName);
    }

    @GetMapping("produce")
    @ApiOperation(value = "根据主题发送消息", notes = "根据主题发送消息")
    public void produceMessage(
            //@ApiParam(name = "topicName", value = "主题名字")
            String topicName,
            //@ApiParam(name = "message", value = "消息内容")
            String message) {
        service.produceMessage(topicName, message);
    }

    @GetMapping("analyzeData")
    @ApiOperation(value = "获取消费信息", notes = "获取消费信息")
    public HashMap<String, ArrayList> getAnalyzeData(
            //@ApiParam(name = "time",format ="yyyy-MM-dd hh:mm",value = "截止时间")
            String time) {
        Date date = null;
        try {
            date = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_hh_mm).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间参数格式错误");
        }
        HashMap<String, ArrayList> resultData = service.getAnalyzeData(date);
        return resultData;
    }

    @MessageMapping("/getAnalyze")
    @SendTo("/topic/topicAnalyze")
    public HashMap<String, ArrayList> sendInfo(String time) throws Exception {
        System.out.println("前端信息：" + time);
        Date date = null;
        try {
            date = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_hh_mm).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间参数格式错误");
        }
        HashMap<String, ArrayList> resultData = service.getAnalyzeData(date);
        return resultData;
    }

    @Scheduled(fixedRate = 1000*5)
    public Object sendInfoRegular(){
        System.out.println("正在定时发送消息......");
        HashMap<String, ArrayList> resultData = service.getAnalyzeData(new Date());
        messagingTemplate.convertAndSend("/topic/topicAnalyze",resultData);
        return "topicAnalyze";
    }
}
