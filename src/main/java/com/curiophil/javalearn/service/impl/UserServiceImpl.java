package com.curiophil.javalearn.service.impl;

import com.curiophil.javalearn.mapper.UserMapper;
import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }
}
