package com.ti.zbus_manager.mapper;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.parameter.AnalyzeQueryParameter;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.Date;

public interface TopicAnalyzeMapper {

    ArrayList<TopicAnalyze> getAllInfo();

    void addInfo(TopicAnalyze topicAnalyze);

    TopicAnalyze findByNameAndTime(String topicName,Date connectTime);

    void addConnectCount(TopicAnalyze topicAnalyze);

    int getConnectCountByDateRange(AnalyzeQueryParameter parameter);

    @Cacheable("consumer")
    ArrayList<TopicAnalyze> getAnalyzeInfoByDateRange(AnalyzeQueryParameter parameter);
}
