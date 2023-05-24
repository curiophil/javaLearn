package com.curiophil.javalearn.controller;

import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.DateTimeService;
import com.curiophil.javalearn.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/datetime")
public class DateTimeController {

    @Resource
    private DateTimeService dateTimeService;

    @RequestMapping("/getTimeSlots")
    public List<String> getUserList() {
        return dateTimeService.getTimeSlots();
    }
}
