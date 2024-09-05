package com.curiophil.javalearn.service.impl;


import com.curiophil.javalearn.mapper.mysql.DateTimeMapper;
import com.curiophil.javalearn.service.DateTimeService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Resource
    private DateTimeMapper dateTimeMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public List<String> getTimeSlots() {
        return dateTimeMapper.getTimeSlots();
    }

    @Override
    public void sendMessage() {
        rocketMQTemplate.syncSend("testTopic","hello rocketmq!");
    }


}
