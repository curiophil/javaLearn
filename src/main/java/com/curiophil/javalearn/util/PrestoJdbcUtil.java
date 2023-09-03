package com.curiophil.javalearn.util;

import com.facebook.presto.jdbc.PrestoConnection;
import com.facebook.presto.jdbc.PrestoPreparedStatement;
import com.facebook.presto.jdbc.PrestoStatement;
import com.facebook.presto.jdbc.QueryStats;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PrestoJdbcUtil {


    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            synchronized (PrestoJdbcUtil.class) {
                if (dataSource == null) {
                    dataSource = getDataSource();
                }
            }
        }
        return dataSource.getConnection();
    }

    private static DataSource getDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.facebook.presto.jdbc.PrestoDriver");
        hikariDataSource.setJdbcUrl("jdbc:presto://10.253.76.39:9000/es/default");
        hikariDataSource.setUsername("1");
        hikariDataSource.setConnectionTimeout(30000);
        return hikariDataSource;
    }


    private static Cache<String, QueryStats> queryStatsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10L, TimeUnit.SECONDS)
            .build();

    private static Cache<String, Statement> jobMapCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10L, TimeUnit.SECONDS)
            .build();


    public static void main(String[] args) throws SQLException {
        new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("queryStatsMap: ");
                queryStatsCache.asMap().forEach((k, v) -> {
                    System.out.println(k + "----------" + v.getState());
                });

                System.out.println("jobMap: " + jobMapCache.asMap());
            }
        })
        .start();

        PrestoConnection connection = null;
        PrestoStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = PrestoJdbcUtil.getConnection().unwrap(PrestoConnection.class);
//            Connection connection = DriverManager.getConnection("jdbc:presto://10.253.76.39:9000/es/default", "test", null);
            connection.setCatalog("es");
            connection.setSchema("default");
            statement = connection.createStatement().unwrap(PrestoStatement.class);
            PrestoStatement finalStatement = statement;
            statement.setProgressMonitor(queryStats -> {
                String queryId = queryStats.getQueryId();
                PrestoJdbcUtil.queryStatsCache.put(queryId, queryStats);
                if (!PrestoJdbcUtil.jobMapCache.asMap().containsKey(queryId)) {
                    PrestoJdbcUtil.jobMapCache.put(queryId, finalStatement);
                }
            });
            statement.setQueryTimeout(10);

            resultSet = statement.executeQuery("select \"@k8s_pod_name\", count(1) from \"326_fn-web-env-stdout_deflector\"  group by \"@k8s_pod_name\" order by \"@k8s_pod_name\" desc");

            ArrayList<Map<String, Object>> resultList = new ArrayList<>();

            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                HashMap<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    Object object = resultSet.getObject(i);
                    rowMap.put(columnName, object);
                }
                resultList.add(rowMap);
            }

            resultList.forEach(map -> System.out.println(map));

        } catch (Exception e) {
            System.out.println(System.currentTimeMillis());
            throw e;
        } finally {
            PrestoJdbcUtil.close(statement, resultSet);
        }

    }

    private static void close(Statement statement, ResultSet resultSet) {
        close(null, statement, resultSet);
    }

    private static void close(Connection connection, Statement statement, ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("关闭resultset");
        }


        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("关闭statement");
        }


        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("关闭connection");
        }


    }
}
