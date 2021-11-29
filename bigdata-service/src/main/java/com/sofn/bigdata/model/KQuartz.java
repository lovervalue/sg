package com.sofn.bigdata.model;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/3 13:50
 * @Description
 */
public class KQuartz {

    private Integer quartzId ;
    //是否删除（1：存在；0：删除）
    private Integer delFlag ;
    //定时策略
    private String quartzCron ;
    //任务描述
    private String quartzDescription ;
    //添加时间
    private Date addTime ;

    public KQuartz() {
    }

    public Integer getQuartzId() {
        return quartzId;
    }

    public void setQuartzId(Integer quartzId) {
        this.quartzId = quartzId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getQuartzCron() {
        return quartzCron;
    }

    public void setQuartzCron(String quartzCron) {
        this.quartzCron = quartzCron;
    }

    public String getQuartzDescription() {
        return quartzDescription;
    }

    public void setQuartzDescription(String quartzDescription) {
        this.quartzDescription = quartzDescription;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
