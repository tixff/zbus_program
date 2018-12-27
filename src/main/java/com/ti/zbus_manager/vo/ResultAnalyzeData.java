package com.ti.zbus_manager.vo;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.entities.TopicProduce;
import com.ti.zbus_manager.util.DateUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ti
 * @date 2018/12/25
 */

@Data
public class ResultAnalyzeData {
    private Date date;
    private ArrayList<TopicAnalyze> list;
    private static final int COUNT = 100;
    private int dateRange;
    private String topicName;
    private static List<String> topicNames;
    private ArrayList<TopicProduce> produceList;

    public static List<String> getTopicNames() {
        return topicNames;
    }

    public static void setTopicNames(List<String> topicNames) {
        ResultAnalyzeData.topicNames = topicNames;
    }

    /**
     * 前端所需数据格式转换(highchart)
     *
     * @return
     */
    public HashMap<String, ArrayList> toAnalyzeData() {
        HashMap<String, ArrayList> resultMap = new HashMap<>();

        //添加30分钟的时间范围
        ArrayList<String> dateList = new ArrayList<>();
        for (int i = 0; i < dateRange; i++) {
            Date minuteAgoTime = DateUtils.getMinuteAgoTime(date, dateRange - i);
            minuteAgoTime = DateUtils.getNoSecondDate(minuteAgoTime);
            String minuteAgoTimeStr = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm).format(minuteAgoTime);
            dateList.add(minuteAgoTimeStr);
        }
        resultMap.put("dateRange", dateList);
        if (list == null || list.size() < 1) {
            return resultMap;
        }
        //获取名称list
        ArrayList<String> topicNames = new ArrayList<>();
        list.forEach(o -> {
            boolean contains = topicNames.contains(o.getTopicName());
            if (!contains) {
                topicNames.add(o.getTopicName());
            }
        });

        //添加serie数据
        ArrayList<Serie> series = new ArrayList();
        topicNames.forEach(o -> {
            Serie serie = new Serie();
            serie.setName(o);
            ArrayList<Integer> data = new ArrayList<>();
            dateList.forEach(time -> {
                TopicAnalyze t = findTopicAnalyzeByNameAndTime(list, o, time);
                if (t == null) {
                    data.add(0);
                } else {
                    data.add(t.getReceivedCount());
                }
            });
            serie.setData(data);
            series.add(serie);
        });

        resultMap.put("series", series);
        return resultMap;
    }

    /**
     * 获取某一主题某一分钟的接收情况
     *
     * @param list
     * @param topicName
     * @param time
     * @return
     */
    private TopicAnalyze findTopicAnalyzeByNameAndTime(ArrayList<TopicAnalyze> list, String topicName, String time) {
        for (TopicAnalyze l : list) {
            String name = l.getTopicName();
            Date receivedTime = l.getReceivedTime();
            String receivedTimeStr = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm)
                    .format(DateUtils.getNoSecondDate(receivedTime));
            if (name.equals(topicName) && receivedTimeStr.equals(time)) {
                return l;
            }
        }
        return null;
    }

    /**
     * 心电图格式消费数据转化
     *
     * @return
     */
    public HashMap<String, ArrayList<String[]>> toDynamicData() {
        HashMap<String, ArrayList<String[]>> resultMap = new HashMap<>();
        if (list == null || list.size() < 1) {
            return null;
        }

        if (topicNames == null || topicNames.size() < 1) {
            return null;
        }

        //获取所有topic消费情况
        for (int i = 0; i < topicNames.size(); i++) {
            ArrayList<String[]> resultData = new ArrayList<>();
            //添加时间范围
            String tname = topicNames.get(i);
            resultData = setTimeRange(resultData);
            //添加接收数目
            for (int k = resultData.size() - COUNT; k < resultData.size(); k++) {
                String[] strs = resultData.get(k);
                String time = strs[0];
                TopicAnalyze topicAnalyzeByNameAndTime = findTopicAnalyzeByNameAndTime(list, tname, time);
                if (topicAnalyzeByNameAndTime == null) {
                    strs[1] = "0";
                } else {
                    int receivedCount = topicAnalyzeByNameAndTime.getReceivedCount();
                    strs[1] = String.valueOf(receivedCount);
                }
            }

            resultMap.put(tname, resultData);
        }

        return resultMap;
    }

    /**
     * 心电图格式生产者数据转化
     * @return
     */
    public HashMap<String, ArrayList<String[]>> toDynamicProduce() {
        HashMap<String, ArrayList<String[]>> resultMap = new HashMap<>();
        if (produceList == null || produceList.size() < 1) {
            return null;
        }
        //获取生产者信息
        for (int i = 0; i < topicNames.size(); i++) {
            ArrayList<String[]> resultData = new ArrayList<>();
            String tname = topicNames.get(i);
            //添加时间段
            resultData = setTimeRange(resultData);
            //添加给定topic给定时间生产者发送的数目
            for (int k = resultData.size() - COUNT; k < resultData.size(); k++) {
                String[] strs = resultData.get(k);
                String time = strs[0];
                int produceCount = findTopicProduceByNameAndTime(tname, time);
                strs[1] = String.valueOf(produceCount);

            }
            resultMap.put(tname, resultData);
        }
        return resultMap;
    }

    /**
     * 添加时间段
     * @param resultData
     * @return
     */
    private ArrayList<String[]> setTimeRange(ArrayList<String[]> resultData) {
        for (int j = 0; j < COUNT; j++) {
            Date minuteAgoTime = DateUtils.getMinuteAgoTime(date, COUNT - j);
            minuteAgoTime = DateUtils.getNoSecondDate(minuteAgoTime);
            String minuteAgoTimeStr = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_HH_mm).format(minuteAgoTime);
            String str[] = new String[2];
            str[0] = minuteAgoTimeStr;
            resultData.add(str);
        }
        return resultData;
    }

    /**
     * 获取给定时间和主题的接收数目
     * @param topicName
     * @param time
     * @return
     */
    private int findTopicProduceByNameAndTime(String topicName, String time) {
        int count = 0;
        if (topicName == null || time == null) {
            return count;
        }
        for (int i = 0; i < produceList.size(); i++) {
            TopicProduce produce = produceList.get(i);
            if (topicName.equals(produce.getTopicName())) {
                String produceTimeStr = produce.getProduceTimeStr();
                if (produceTimeStr.equals(time)) {
                    count++;
                }
            }
        }
        return count;
    }
}
