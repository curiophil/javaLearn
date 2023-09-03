package com.curiophil.javalearn.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private Integer id;
    private String name;
    private Integer age;
    private String email;
}
