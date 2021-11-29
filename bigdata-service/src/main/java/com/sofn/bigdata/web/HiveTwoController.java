package com.sofn.bigdata.web;

import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/3/1 16:36
 * @Description hive 的相关接口，获取表
 */
@Slf4j
@Api(value = "hive的相关的,帆软表名", tags = "hive的相关的,帆软表名")
@RequestMapping("/hive/")
@RestController
public class HiveTwoController {

    //获取配置文件application.yml里面的hive参数信息
    @Value("${hive.driver-class-name}")
    private  String driverName;// = "org.apache.hive.jdbc.HiveDriver";
    @Value("${hive.url}")
    private  String url ;//= "jdbc:hive2://192.168.21.101:10000/oracle";
    @Value("${hive.user}")
    private String user;
    @Value("${hive.password}")
    private String password;

    private static Connection con = null;
    private static Statement state = null;
    private static ResultSet res = null;

    //加载驱动,创建连接
    public void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        con = DriverManager.getConnection(url, user, password);
        state = con.createStatement();
    }

/**
 * 列举当前Hive库中的所有数据表
 */

    @SofnLog("总获取hive表名")
    @ApiOperation(value = "获取hive表名")
    @PostMapping("table/list")
    public Result<String> listAllTables() throws SQLException, ClassNotFoundException {
        List<String> list = new ArrayList<String>();
        String sqlone="show databases";
        String sqltwo="use oracle";
        String sql = "show tables";
        init();
        res=state.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
            list.add(res.getString(1));
        }
        System.out.println("======"+list);
        return Result.ok(list);
    }

    @ApiOperation(value = "获取表里的字段")
    @PostMapping("table/see")
    public List<String> listtablesee(String tablename) throws SQLException, ClassNotFoundException {
        List<String> list = new ArrayList<String>();
        String sql="desc "+tablename;
        res=state.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
            list.add(res.getString(1));
        }
        System.out.println("======"+list);
        return list;
    }

    @ApiOperation(value = "获取表里的内容")
    @PostMapping("table/select")
    public List<String> listtableslect(String tablename) throws SQLException, ClassNotFoundException {
        List<String> list = new ArrayList<String>();
        String sql="select * from "+tablename +" limit 10";
        res=state.executeQuery(sql);
        while (res.next()) {
            System.out.println(String.valueOf(res.getString(1)) +
                    "\t" + res.getString(2) +
                    "\t" + res.getString(3) +
                    "\t" + res.getString(4) +
                    "\t" + res.getString(5) +
                    "\t" + res.getString(6) +
                    "\t" + res.getString(7));

            list.add(String.valueOf(res.getString(1)) +
                    "  " + res.getString(4) +
                    "  " + res.getString(5) +
                    "  " + res.getString(6) +
                    "  " + res.getString(7) +
                    "  " + res.getString(8) +
                    "  " + res.getString(9));
        }
      //  System.out.println("======"+list);
        return list;
    }
}
