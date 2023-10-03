package com.curiophil.javalearn.service;


import com.curiophil.javalearn.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface DateTimeService {

    public static void main(String[] args) throws JsonProcessingException {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("123", "456", "789"));
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("list", strings);
            put("str", "101");
            put("obj", new HashMap<String, Object>() {{
                put("name", "jackson");
            }});
        }};
        String s = new ObjectMapper().writeValueAsString(map);
        System.out.println(s);
    }


    List<String> getTimeSlots();

    void sendMessage();
}
