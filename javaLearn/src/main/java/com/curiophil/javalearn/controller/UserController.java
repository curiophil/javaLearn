package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.pojo.JavaLearnThread;
import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getUserList")
    public List<User> getUserList() {
        CompletableFuture.runAsync(new JavaLearnThread() {
            @Override
            public void run0() {
                log.info("测试requestId是否携带");
            }
        });
        return userService.getUserList();
    }
}
