package com.curiophil.javalearn.mapper.clickhouse;

import java.util.List;
import java.util.Map;

public interface CommonMapper {

    List<Map<String, Object>> showTables();
}
