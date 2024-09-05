package com.curiophil.javalearn.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curiophil.javalearn.mapper.mysql.UserMapper;
import com.curiophil.javalearn.pojo.User;
import com.curiophil.javalearn.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        List<User> users = userMapper.selectList(null);
        System.out.println(JSON.toJSONString(users));
        return users;
    }

    /**
     * 结论：
     * in智能用于单个字段，不能用于多个字段in
     * 多个字段用inSql
     * @return
     */
    @Override
    public Object testIn() {

//        ArrayList<User> list = new ArrayList<User>() {{
//            add(User.builder().id(15).name("xxx").age(18).email("xxx").build());
//            add(User.builder().id(18).name("bbb").age(32).email("bbb").build());
//            add(User.builder().id(18).name("bbb").age(32).email("bbb").build());
//        }};

//        List<String> collect = list.stream().map(o -> "(" + o.getAge() + ",'" + o.getEmail() + "')").collect(Collectors.toList()); //字符串相当于一列


//        List<Map<String, Object>> maps = userMapper.selectMaps(new QueryWrapper<User>()
//                .inSql("(age, email)", list.stream()
//                        .map(o -> "(" + o.getAge() + ",'" + o.getEmail() + "')")
//                        .collect(Collectors.joining(","))));

/*        maps = userMapper.selectMaps(new QueryWrapper<User>().nested(
                queryWrapper -> {
                    for (User user : list) {
                        queryWrapper.or().eq("age", user.getAge());
                        queryWrapper.or().eq("email", user.getEmail());
                    }
                }
        ));*/
        ArrayList<User> list = new ArrayList<User>() {{
            add(User.builder().name("xxx").age(18).email("xxx").build());
            add(User.builder().name("bbb").age(32).email("bbb").build());
            add(User.builder().name("bbb").age(32).email("bbb").build());
        }};
        this.saveBatch(list);
        return null;
    }
}
