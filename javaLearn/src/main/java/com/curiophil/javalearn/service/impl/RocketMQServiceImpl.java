package com.curiophil.javalearn.service.impl;


import com.curiophil.javalearn.service.RocketMQService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RocketMQServiceImpl implements RocketMQService {


    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessage() {
        rocketMQTemplate.syncSend("testTopic","hello rocketmq!");
    }

}
