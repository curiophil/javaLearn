package com.curiophil.javalearn.service.impl;


import com.curiophil.javalearn.mapper.DateTimeMapper;
import com.curiophil.javalearn.service.DateTimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Resource
    private DateTimeMapper dateTimeMapper;


    @Override
    public List<String> getTimeSlots() {
        return dateTimeMapper.getTimeSlots();
    }
}
