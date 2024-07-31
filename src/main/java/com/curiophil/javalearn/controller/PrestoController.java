package com.curiophil.javalearn.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/presto")
public class PrestoController {

    @Resource(name="prestoTemplate")
    private JdbcTemplate prestoTemplate;

    @GetMapping("/query")
    public Object executeQuery(@RequestParam String sql) {
        List<Map<String, Object>> maps = prestoTemplate.queryForList(sql);
        return maps;
    }

}
