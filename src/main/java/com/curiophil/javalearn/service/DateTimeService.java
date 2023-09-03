package com.curiophil.javalearn.service;


import com.curiophil.javalearn.pojo.User;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public interface DateTimeService {

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
        Date utc = Date.from(LocalDateTime.parse("2023-08-25T12:33:49.666000810Z", formatter).atZone(ZoneId.of("UTC")).toInstant());
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(utc);
        System.out.println(format);
    }


    List<String> getTimeSlots();

    void sendMessage();
}
