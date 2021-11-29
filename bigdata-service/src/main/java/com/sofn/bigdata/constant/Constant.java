package com.sofn.bigdata.constant;

import org.pentaho.di.core.logging.LogLevel;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author:袁彬锋
 * @date:2020/4/27 16:20
 * @company:曙光集团绿康元公司
 * @description:常量类，返回结果常量系统常量等
 */
public class Constant implements Serializable {
    //分页查询常量
    public static final Long PAGE_SIZE = 15L;

    //模板的id
    public static final Long WATER_QUALITY_TEMPLATE_ID = 739870632888938496L;//国家地表水 水质自动监测实时数据模板

    //模板状态
    public static final String DISABLED = "disabled";//不可用
    public static final String UP = "up";//启动
    public static final String OFF = "off";//关闭/禁止

    //保存测试数据
    public static final String SAVE_SPIDER_SUCCESS = "保存更新成功";
    public static final String CREATE_TEMPLATE_SUCCESS = "创建模板成功";
    public static final String DELETE_TEMPLATE_SUCCESS = "删除模板成功";
    public static final String PAGE_QUERY_SUCCESS = "分页查询成功";

    //水质数据爬任务、触发器名称与组名
    public static final String WATER_QUALITY_JOB_NAME = "http://123.127.175.45:8082";//水质数据爬取任务名称
    public static final String WATER_QUALITY_TRIGGER_NAME = "http://123.127.175.45:8082";//水质数据爬取触发器名称

    //ETL--kettle相关的
    /** Public */
    public static final String VERSION = "8.0.0.0-25";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String SESSION_ID = "SESSION_ID";

    /** Kettle */
    public static final String TYPE_JOB = "job";
    public static final String TYPE_TRANS = "transformation";

    /** quartz **/

    public static final String JOB_PREFIX = "JOB";
    public static final String JOB_GROUP_PREFIX = "JOB_GROUP";
    public static final String TRIGGER_PREFIX = "TRIGGER";
    public static final String TRIGGER_GROUP_PREFIX = "TRIGGER_GROUP";
    public static final String QUARTZ_SEPARATE = "@";

    /** quartz parameter **/
    public static final String REPOSITORYOBJECT = "REPOSITORYOBJECT";
    public static final String DBCONNECTIONOBJECT = "DBCONNECTIONOBJECT";
    public static final String JOBID = "JOBID";
    public static final String TRANSID= "TRANSID";
    public static final String JOBTYPE="JOBTYPE";
    public static final String JOBPATH = "JOBPATH";
    public static final String TRANSPATH = "TRANSPATH";
    public static final String JOBNAME = "JOBNAME";
    public static final String TRANSNAME = "TRANSNAME";
    public static final String USERID = "USERID";
    public static final String LOGLEVEL = "LOGLEVEL";
    public static final String LOGFILEPATH = "LOGFILEPATH";

    public static final String RUNSTATUS_SEPARATE = "-";

    public static String KETTLE_HOME;
    public static String KETTLE_PLUGIN;
    public static String KETTLE_SCRIPT;
    public static LogLevel KETTLE_LOGLEVEL;

    public static String DATASOURCE_TYPE;

    public static Properties props;
    public static LogLevel logger(String level) {
        LogLevel logLevel = null;
        if ("basic".equals(level)) {
            logLevel = LogLevel.BASIC;
        } else if ("detail".equals(level)) {
            logLevel = LogLevel.DETAILED;
        } else if ("error".equals(level)) {
            logLevel = LogLevel.ERROR;
        } else if ("debug".equals(level)) {
            logLevel = LogLevel.DEBUG;
        } else if ("minimal".equals(level)) {
            logLevel = LogLevel.MINIMAL;
        } else if ("rowlevel".equals(level)) {
            logLevel = LogLevel.ROWLEVEL;
        } else if ("Nothing".endsWith(level)){
            logLevel = LogLevel.NOTHING;
        }else {
            logLevel = KETTLE_LOGLEVEL;
        }
        return logLevel;
    }
}
