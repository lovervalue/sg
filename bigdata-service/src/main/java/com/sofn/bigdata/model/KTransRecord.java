package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/3 15:12
 * @Description
 */
public class KTransRecord {
    //转换记录ID
    @TableId(value = "record_id",type= IdType.UUID)
    private String recordId ;
    //任务执行结果（1：成功；2：失败）
    private Integer recordStatus ;
    //转换ID
    private Integer recordTrans ;
    //添加人
    private Integer addUser;

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    //转换日志记录文件保存位置
    private String logFilePath ;
    //启动时间
    private Date startTime ;
    //停止时间
    private Date stopTime ;

    public KTransRecord() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getRecordTrans() {
        return recordTrans;
    }

    public void setRecordTrans(Integer recordTrans) {
        this.recordTrans = recordTrans;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
}
