package com.sofn.bigdata.enums;

import lombok.Getter;

/**
 * redis缓存键
 */
@Getter
public enum  DataCacheKeyEnum {

    /**
     * code值对应的mysql数据库中结果集的表名，desc对应接口名称
     */
    ASZDQQ_INTER_KEY("aszdqq_inter_table","农村清洁工程建设状况调查子系统共享接口");

    private String code;
    private String desc;

    DataCacheKeyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获得描述信息
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        for(DataCacheKeyEnum e: DataCacheKeyEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
