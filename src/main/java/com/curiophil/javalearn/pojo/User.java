package com.curiophil.javalearn.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {

    @TableId
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Date createTime;
//    @TableField(value = "deleted")
    // boolean类型Lombok生成的getter为isxxx Boolean类型Lombok生成的getter正常
//    private Boolean isDeleted;
}
