package com.curiophil.javalearn.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private LocalDateTime createTime;
//    @TableField(value = "deleted")
    // boolean类型Lombok生成的getter为isxxx Boolean类型Lombok生成的getter正常
//    private Boolean isDeleted;
}
