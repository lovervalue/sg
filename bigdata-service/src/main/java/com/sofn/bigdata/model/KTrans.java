package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/3 10:08
 * @Description
 */
@TableName("k_trans")
public class KTrans {
//    @TableId(value = "trans_id",type= IdType.UUID)
    @ApiModelProperty("转换id,必传")
    private Integer transId;

    private Integer categoryId;

    //是否删除（1：存在；0：删除）
    private Integer delFlag;
    //定时策略（外键ID）
    @ApiModelProperty("定时策略id,必传，也是先调列表/trans/getquartzlist")
    private Integer transQuartz;
    //转换执行记录（外键ID）
    private Integer transRecord;
    //转换的资源库ID
    @ApiModelProperty("资源库id,必传,调资源库列表")
    private Integer transRepositoryId;
    //状态（1：正在运行；2：已停止）
    private Integer transStatus;
    //1:数据库资源库；2:上传的文件
    @ApiModelProperty("1:数据库资源库；2:上传的文件")
    private Integer transType;
    //转换描述
    @ApiModelProperty("转换描述")
    private String transDescription;
    @ApiModelProperty("日志级别(basic，detail，error，debug，minimal，rowlevel）")
    //日志级别(basic，detail，error，debug，minimal，rowlevel）
    private String transLogLevel;
    //转换名称
    @ApiModelProperty("转换名称")
    private String transName;
    //转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
    @ApiModelProperty("转换保存路径资源库调repository/database/getTransTree?repositoryId=transRepositoryId，本地是本地环境")
    private String transPath;
    //添加时间
    private Date addTime;


    public KTrans() {
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getTransQuartz() {
        return transQuartz;
    }

    public void setTransQuartz(Integer transQuartz) {
        this.transQuartz = transQuartz;
    }

    public Integer getTransRecord() {
        return transRecord;
    }

    public void setTransRecord(Integer transRecord) {
        this.transRecord = transRecord;
    }

    public Integer getTransRepositoryId() {
        return transRepositoryId;
    }

    public void setTransRepositoryId(Integer transRepositoryId) {
        this.transRepositoryId = transRepositoryId;
    }

    public Integer getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Integer transStatus) {
        this.transStatus = transStatus;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public String getTransLogLevel() {
        return transLogLevel;
    }

    public void setTransLogLevel(String transLogLevel) {
        this.transLogLevel = transLogLevel;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransPath() {
        return transPath;
    }

    public void setTransPath(String transPath) {
        this.transPath = transPath;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
