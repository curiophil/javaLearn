package com.curiophil.javalearn.service.impl;

import com.curiophil.javalearn.mapper.mysql.MyBaseMapper;
import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.BaseService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class BaseServiceImpl implements BaseService {

    @Resource
    private MyBaseMapper myBaseMapper;


    @Override
    public Boolean test() {
        ArrayList<User> list = new ArrayList<User>() {{
            add(User.builder().id(15l).name("xxx").age(18).email("xxx").build());
            add(User.builder().id(18l).name("bbb").age(32).email("bbb").build());
        }};

//        ArrayList<String> list = new ArrayList<String>() {{
//            add("123");
//            add("456");
//        }};
        Boolean test = myBaseMapper.test(list);
        System.out.println(test);

        return test;
    }
}
