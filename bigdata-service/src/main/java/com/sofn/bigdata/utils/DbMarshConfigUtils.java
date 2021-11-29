package com.sofn.bigdata.utils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

@Component
public class DbMarshConfigUtils {
    @Resource
    private Environment environment;
    private static Environment env;

    @PostConstruct
    private void init() {
        env = environment;
    }

    public static Connection getConnection(){
        String url = env.getProperty("url");
        String username = env.getProperty("username1");
        String password = env.getProperty("password1");
        try {
            return DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection connection, ResultSet resultSet){
        try {
            if(connection!=null){
                connection.close();
            }
            if(resultSet!=null){
                resultSet.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}