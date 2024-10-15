package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.service.RocketMQService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rocket")
public class RocketMQController {

    @Resource
    private RocketMQService rocketMQService;

    @RequestMapping("/sendMessage")
    public void sendMessage() {
        rocketMQService.sendMessage();
    }
}
