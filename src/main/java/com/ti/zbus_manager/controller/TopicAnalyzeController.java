package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TopicAnalyzeController {

    @Autowired
    private TopicAnalyzeService service;
    @GetMapping("getAll")
    public void getAllInfo(){
        ArrayList<TopicAnalyze> allInfo = service.getAllInfo();
        allInfo.forEach(o->{
            System.out.println(o);
        });
    }

    @GetMapping("addConsumer")
    @ApiOperation(value = "topic的名字",notes = "topic的名字")
    public void addConsumer(String topicName){
        service.addCondumer(topicName);
    }

    @GetMapping("produce")
    @ApiOperation(value = "topic的名字",notes = "topic的名字")
    public void produceMessage(String topicName,String message){
        service.produceMessage(topicName,message);
    }


}
