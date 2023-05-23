package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getUserList")
    public List<User> getUserList() {
        return userService.getUserList();
    }
}
