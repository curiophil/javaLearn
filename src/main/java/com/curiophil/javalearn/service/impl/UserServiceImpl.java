package com.curiophil.javalearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curiophil.javalearn.mapper.mysql.UserMapper;
import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

    /**
     * 结论：
     * in智能用于单个字段，不能用于多个字段in
     * 多个字段用inSql
     * @return
     */
    @Override
    public Object testIn() {

        ArrayList<User> list = new ArrayList<User>() {{
            add(User.builder().id(15).name("xxx").age(18).email("xxx").build());
            add(User.builder().id(18).name("bbb").age(32).email("bbb").build());
            add(User.builder().id(18).name("bbb").age(32).email("bbb").build());
        }};

//        List<String> collect = list.stream().map(o -> "(" + o.getAge() + ",'" + o.getEmail() + "')").collect(Collectors.toList()); //字符串相当于一列


        List<Map<String, Object>> maps = userMapper.selectMaps(new QueryWrapper<User>()
                .inSql("(age, email)", list.stream()
                        .map(o -> "(" + o.getAge() + ",'" + o.getEmail() + "')")
                        .collect(Collectors.joining(","))));
        return maps;
    }
}
