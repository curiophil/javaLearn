package com.curiophil.javalearn.consistcy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NetworkApi implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Execute(value = "1")
    public Map execute(Integer a, String c) {
        return new HashMap() {{
            put("s", "1111111111");
            put("i", 123);
            put("l", Arrays.asList(1, 2, 3));
        }};
    }

    @Execute(value = "2")
    public Map execute2() {
        if (true) {
            throw new RuntimeException("null");
        }
        return new HashMap() {{
            put("s", "1111111111");
            put("ll", 123);
            put("l", Arrays.asList(1, 2, 3));
        }};
    }


    @Rollback(value = "1")
    public void rollbacks(String s, Integer i) {
        System.out.println("AAA");
    }

    @Rollback(value = "2")
    public void rollback(String s, List<Integer> l) {
        System.out.println("BBB");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
