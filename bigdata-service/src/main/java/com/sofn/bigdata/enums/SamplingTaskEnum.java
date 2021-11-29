package com.sofn.bigdata.enums;

import lombok.Getter;

/**
 * @program: sofn-eep
 * @description: 任务状态值
 * @author: Mr.YangXin
 * @create: 2019-12-04 10:06
 * @return
 **/

@Getter
public enum SamplingTaskEnum {

    ALLOT_STATE_1("1", "未分配"),
    ALLOT_STATE_2("2", "已分配"),

    TASK_TYPE_1("1", "土壤"),
    TASK_TYPE_2("2", "农产品"),

    STATE_1("1", "未采集"),
    STATE_2("2", "待审核"),
    STATE_3("3", "审核通过"),

    SITE_STATE_1("1", "启用"),
    SITE_STATE_2("2", "停用");


    private String key;
    private String val;

    SamplingTaskEnum(String key, String val) {
        this.key = key;
        this.val = val;
    }
}
