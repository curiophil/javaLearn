package com.curiophil.javalearn.config;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.curiophil.javalearn.mapper.clickhouse", sqlSessionFactoryRef = "clickHouseSessionFactory")
public class ClickHouseDataSourceConfiguration {

    @Value("${mybatis-plus.mapper-locations-clickhouse}")
    private String clickHouseXmlLocation;

    @Bean("clickHouseDataSource")
    @ConfigurationProperties(prefix="spring.datasource.clickhouse")
    public DataSource prestoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("clickHouseSessionFactory")
    public SqlSessionFactory prestoSessionFactory(@Qualifier("clickHouseDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(clickHouseXmlLocation));
        return bean.getObject();// 设置mybatis的xml所在位置
    }

}
