package com.ti.zbus_manager.controller;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.service.TopicAnalyzeService;
import com.ti.zbus_manager.util.DateUtils;
import com.ti.zbus_manager.util.ZbusUtils;
import com.ti.zbus_manager.vo.ResultAnalyzeData;
import com.ti.zbus_manager.vo.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@EnableScheduling
public class TopicAnalyzeController {
    private static Logger logger = LoggerFactory.getLogger(TopicAnalyzeController.class);
    private static final ThreadLocal<Boolean> fistTime = new ThreadLocal<>();

    static {
        fistTime.set(true);
    }

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

    @PostMapping("addConsumer")
    @ApiOperation(value = "添加消费者", notes = "添加消费者")
    public ResultMessage addConsumer(
            // @ApiParam(name = "topicName", value = "主题名称")
            String topicName) {
        try {
            service.addConsumer(topicName);
            return new ResultMessage("添加消费者成功");
        } catch (Exception e) {
            logger.error("添加消费者失败");
            return new ResultMessage("添加消费者失败");
        }
    }

    @PostMapping("produce")
    @ApiOperation(value = "根据主题发送消息", notes = "根据主题发送消息")
    public ResultMessage produceMessage(
            //@ApiParam(name = "topicName", value = "主题名字")
            String topicName,
            //@ApiParam(name = "message", value = "消息内容")
            String message, HttpServletRequest request) {
        try {
            String ip = ZbusUtils.getIpAddress(request);
            service.produceMessage(topicName, message, ip);
            return new ResultMessage("发送消息成功");
        } catch (Exception e) {
            logger.error("发送消息失败");
            return new ResultMessage("发送消息失败");
        }
    }

    /**
     * 给定时间查询（highchart数据）
     *
     * @param time
     * @return
     */
    @GetMapping("analyzeData")
    @ApiOperation(value = "获取消费信息", notes = "获取消费信息")
    public HashMap<String, ArrayList> getAnalyzeData(
            //@ApiParam(name = "time",format ="yyyy-MM-dd hh:mm",value = "截止时间")
            String time) {
        Date date = null;
        try {
            date = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间参数格式错误");
        }
        ResultAnalyzeData analyzeData = service.getConsumerAnalyzeData(date, null);
        HashMap<String, ArrayList> result = analyzeData.toAnalyzeData();
        return result;
    }

    /**
     * 处理前端webSocket请求（echart数据）
     *
     * @param time
     * @return
     */
    @MessageMapping("/echartData")
    @SendTo("/topic/echart")
    public HashMap<String, ArrayList<String[]>> getEcharData(String time, @Header("atytopic") String topic) {
        Date date = null;
        try {
            date = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间参数格式错误");
        }

        ResultAnalyzeData analyzeData = service.getConsumerAnalyzeData(date, topic);
        HashMap<String, ArrayList<String[]>> resultMap = analyzeData.toDynamicData();
        return resultMap;
    }

    /**
     * 处理前端webSocket请求(highchart)
     *
     * @param time
     * @return
     * @throws Exception
     */
    @MessageMapping("/getAnalyze")
    @SendTo("/topic/topicAnalyze")
    public HashMap<String, ArrayList> sendInfo(String time, String topicName) throws Exception {
        logger.info("前端给定查询时间：{}", time);
        Date date = null;
        try {
            date = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间参数格式错误");
        }
        ResultAnalyzeData analyzeData = service.getConsumerAnalyzeData(date, null);
        HashMap<String, ArrayList> result = analyzeData.toAnalyzeData();
        return result;
    }


    /**
     * 定时发送服务（highchart数据）
     *
     * @return
     */
    @Scheduled(fixedRate = 1000 * 5)
    public Object sendInfoRegular() {
        logger.info("正在定时向前端发送消息(higthchart)......");
        ResultAnalyzeData analyzeData = service.getConsumerAnalyzeData(new Date(), null);
        HashMap<String, ArrayList> result = analyzeData.toAnalyzeData();
        messagingTemplate.convertAndSend("/topic/dynamic", result);
        return "topicAnalyze";
    }

    /**
     * 定时发送服务（echart数据）
     *
     * @return
     */
    @Scheduled(fixedRate = 1000 * 3)
    public Object sendAnalyzeData() {
        logger.info("正在定时发送分析数据(echart)......");
        ResultAnalyzeData consumerAnalyzeData = service.getConsumerAnalyzeData(new Date(), null);
        ResultAnalyzeData produceAnalyzeData = service.getProduceAnalyzeData();

        HashMap<String, ArrayList<String[]>> consumerMap = consumerAnalyzeData.toDynamicData();
        HashMap<String, ArrayList<String[]>> produceMap = produceAnalyzeData.toDynamicProduce();

        if (consumerMap != null) {
            messagingTemplate.convertAndSend("/topic/echartdynamic", consumerMap);
        }

        if (produceMap != null) {
            messagingTemplate.convertAndSend("/topic/producedynamic", produceMap);
        }
        return "echartAnayze";
    }

    @GetMapping("/setTopicName")
    public void setTopicName(String name, HttpServletRequest request) {
        request.getSession().setAttribute("topicName", name);
    }
}
