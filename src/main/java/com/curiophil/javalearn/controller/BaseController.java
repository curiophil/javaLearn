package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.service.BaseService;
import com.curiophil.javalearn.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@ResponseBody
@RequestMapping("/base")
public class BaseController {


    @Resource
    private BaseService baseService;

    @Resource
    private UserService userService;


    @GetMapping("test")
    public Object test() {
//        return baseService.test();
        return userService.testIn();
    }
}
