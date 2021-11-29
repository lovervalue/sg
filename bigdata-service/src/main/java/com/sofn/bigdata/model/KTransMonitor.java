package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/4 10:33
 * @Description
 */
public  class KTransMonitor {
    @TableId(value = "monitor_id",type= IdType.UUID)
    private String monitorId;
    //失败次数
    @ApiModelProperty("失败次数")
    private Integer monitorFail;
    //监控状态（是否启动，1:启动；2:停止）
    @ApiModelProperty("监控状态（是否启动，1:启动；2:停止）")
    private Integer monitorStatus;
    //成功次数
    @ApiModelProperty("成功次数")
    private Integer monitorSuccess;
    //监控的转换的ID
    private Integer monitorTrans;
    //运行状态（起始时间-结束时间,起始时间-结束时间……）
    private String runStatus;
    //上次执行时间
    private Date lastExecuteTime;
    //下次执行时间
    private Date nextExecuteTime;
    private String categoryName;
//    private String transName;

    public KTransMonitor() {
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public Integer getMonitorFail() {
        return monitorFail;
    }

    public void setMonitorFail(Integer monitorFail) {
        this.monitorFail = monitorFail;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public Integer getMonitorSuccess() {
        return monitorSuccess;
    }

    public void setMonitorSuccess(Integer monitorSuccess) {
        this.monitorSuccess = monitorSuccess;
    }

    public Integer getMonitorTrans() {
        return monitorTrans;
    }

    public void setMonitorTrans(Integer monitorTrans) {
        this.monitorTrans = monitorTrans;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Date getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Date nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
