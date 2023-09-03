package com.curiophil.javalearn.mapper.mysql;

import org.apache.ibatis.annotations.Param;

public interface MyBaseMapper {


    Boolean test(@Param("list") Object list);
}
