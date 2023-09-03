package com.curiophil.javalearn.config;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.curiophil.javalearn.mapper.presto", sqlSessionFactoryRef = "prestoSessionFactory")
public class PrestoDataSourceConfiguration {

    @Value("${mybatis-plus.mapper-locations2}")
    private String prestoXmlLocation;


    @Bean("prestoDataSource")
    @ConfigurationProperties(prefix="spring.datasource.presto")
    public DataSource prestoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("prestoSessionFactory")
    public SqlSessionFactory prestoSessionFactory(@Qualifier("prestoDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        bean.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources(prestoXmlLocation));
        return bean.getObject();// 设置mybatis的xml所在位置
    }


    @Bean("prestoTemplate")
    public JdbcTemplate prestoJdbcTemplate(@Qualifier("prestoDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
