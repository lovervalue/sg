package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.DataEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 驱动程序集合
 */
@Getter
public enum DriverSetEnum {

    DRIVER_DEFAULT("1","[driver]"),
    DRIVER_ALIYUN_ANALYTICDB("2","aliyun AnalyticDB"),
    DRIVER_ALIYUN_MAXCOMPUTE("3","aliyun MaxCompute"),
    DRIVER_CLICKHOUSE("4","ClickHouse"),
    DRIVER_DB2_V9("5","DB2 V9"),
    DRIVER_GBASE_8S_V8_4("6","Gbase 8S V8.4"),
    DRIVER_GBASE_8S_V8_8("7","Gbase 8S V8.8"),
    DRIVER_GBASE_8A("8","Gbase 8A"),
    DRIVER_GREENPLUM("9","Greenplum"),
    DRIVER_HANA("10","HANA"),
    DRIVER_HADOOP_HIVE("11","Hadoop_Hive"),
    DRIVER_HUAWEI_FUSIONINSIGHT_HD("12","Huawei FusionInsight HD"),
    DRIVER_IMPALA("13","IMPALA"),
    DRIVER_INFOBRIGHT("14","Infobright"),
    DRIVER_INFORMIX("15","Informix"),
    DRIVER_KINGBASE("16","Kingbase"),
    DRIVER_KINGBASE_ANALYTICS("17","KingbaseAnalytics"),
    DRIVER_KYLIN("18","Kylin"),
    DRIVER_MS_SQL_SERVER("19","MS SQL Server"),
    DRIVER_MYSQL("20","MySQL"),
    DRIVER_ORACLE("21","Oracle"),
    DRIVER_ORACLE_TIMESTEN("22","Oracle TimesTen"),
    DRIVER_POSTGRESQL("23","PostgreSQL"),
    DRIVER_PRESTO("24","Presto"),
    DRIVER_SHENTONG("25","ShenTong"),
    DRIVER_SMARTBI_JDBC_FOR_EXCEL("26","Smartbi JDBC for Excel"),
    DRIVER_SPARKSQL("27","Spark SQL"),
    DRIVER_SYBASE("28","Sybase"),
    DRIVER_TERADATA_V12("29","Teradata_v12"),
    DRIVER_TERADATA_V13_UP("30","Teradata_v13及其以上"),
    DRIVER_VERTICA("31","Vertica"),
    DRIVER_XINGHUAN("32","星环"),
    DRIVER_DARMENG_7("33","达梦7"),
    DRIVER_DAMENG_6("34","达梦6"),
    DRIVER_OTHER("35","other");

    private String code;
    private String desc;

    DriverSetEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获得所有的枚举值和描述信息
     * @return
     */
    public static List<DataEnumVo> getEnumInfo(){
        List<DataEnumVo> list = new ArrayList<>();
        for(DriverSetEnum e: DriverSetEnum.values()){
            DataEnumVo obj = new DataEnumVo();
            obj.setCode(e.getCode());
            obj.setDesc(e.getDesc());
            list.add(obj);
        }
        return list;
    }

    /**
     * 根据code获得描述信息
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        for(DriverSetEnum e: DriverSetEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
