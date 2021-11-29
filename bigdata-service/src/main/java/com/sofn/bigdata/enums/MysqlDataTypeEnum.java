package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.DataEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符集集合
 */
@Getter
public enum MysqlDataTypeEnum {

    TINYINT("tinyint","小整数值"),
    SMALLINT("smallint","大整数值"),
    MEDIUMINT("mediumint","大整数值"),
    INT("int","大整数值"),
    INTEGER("integer","大整数值"),
    BIGINT("bigint","极大整数值"),
    FLOAT("float","浮点数值"),
    DOUBLE("double","浮点数值"),
    DECIMAL("decimal","小数值"),
    DATE("date","日期值"),
    TIME("time","时间值或持续时间"),
    YEAR("year","年份值"),
    DATETIME("datetime","混合日期和时间值"),
    TIMESTAMP("timestamp","时间戳"),
    CHAR("char","定长字符串"),
    VARCHAR("varchar","变长字符串"),
    TINYBLOB("tinyblob","二进制字符串"),
    TINYTEXT("tinytext","短文本字符串"),
    BLOB("blob","二进制形式的长文本数据"),
    TEXT("text","长文本数据"),
    MEDIUMBLOB("mediumblob","二进制形式的中等长度文本数据"),
    MEDIUMTEXT("mediumtext","中等长度文本数据"),
    LONGBLOB("longblob","二进制形式的极大文本数据"),
    LONGTEXT("longtext","极大文本数据");

    private String code;
    private String desc;

    MysqlDataTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获得所有的枚举值和描述信息
     * @return
     */
    public static List<DataEnumVo> getEnumInfo(){
        List<DataEnumVo> list = new ArrayList<>();
        for(MysqlDataTypeEnum e: MysqlDataTypeEnum.values()){
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
        for(MysqlDataTypeEnum e: MysqlDataTypeEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
