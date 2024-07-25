package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.consistcy.NetworkApi;
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

    @Resource
    private NetworkApi networkApi;


    @GetMapping("test")
    public Object test() {
        networkApi.execute(1, "");
        networkApi.execute2();
        return networkApi.execute(1, "");
    }
}
