package com.curiophil.javalearn.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile("1\\.(?<MAJOR>[0-9]+)(\\.(?<MINOR>(0|[1-9][0-9]*)))?(_(?<UPDATE>[1-9][0-9]*))?" + "(?:-(?:[-a-zA-Z0-9.]+))?").matcher("1.8.0-332");

        if (matcher.matches()) {
            int major = Integer.parseInt(matcher.group("MAJOR"));
            int minor = Optional.ofNullable(matcher.group("MINOR"))
                    .map(Integer::parseInt)
                    .orElse(0);

            String update = matcher.group("UPDATE");
            System.out.println(major);
            System.out.println(minor);
            System.out.println(update);

        }
    }
}
