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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.curiophil.javalearn.mapper.es", sqlSessionFactoryRef = "esSessionFactory")
public class EsDataSourceConfiguration {

    @Value("${mybatis-plus.mapper-locations3}")
    private String esXmlLocation;


    @Bean("esDataSource")
    @ConfigurationProperties(prefix="spring.datasource.es")
    public DataSource esDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("esSessionFactory")
    public SqlSessionFactory esSessionFactory(@Qualifier("esDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        bean.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources(esXmlLocation));
        return bean.getObject();// 设置mybatis的xml所在位置
    }


    @Bean("esTemplate")
    public JdbcTemplate esJdbcTemplate(@Qualifier("esDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
