package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/7 11:04
 * @Description
 */
@TableName("warning_analysis")
public class WarningAnalysis {

    @TableId(value = "warning_id",type= IdType.UUID)
    @ApiModelProperty("预警id，不传，自动创建")
    private String warningId;
    //业务数据名称
    @ApiModelProperty("业务数据名称")
    private String dataName;
    @ApiModelProperty("创建人员姓名")
    private String createName;
    @ApiModelProperty("创建时间，不传")
    private Date createTime;
    //1是启用，0是停用
    @ApiModelProperty("1是启用，0是停用")
    private Integer warningStatus;
    //说明
    @ApiModelProperty("说明")
    private String warningOperating;
    @ApiModelProperty("数据库/集")
    private String databaseName;
    @ApiModelProperty("别名")
    private String businessOthername;


    @Override
    public String toString() {
        return "WarningAnalysis{" +
                "warningId='" + warningId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", createName='" + createName + '\'' +
                ", createTime=" + createTime +
                ", warninStatus=" + warningStatus +
                ", warningOperating='" + warningOperating + '\'' +
                '}';
    }

    public Integer getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(Integer warningStatus) {
        this.warningStatus = warningStatus;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getBusinessOthername() {
        return businessOthername;
    }

    public void setBusinessOthername(String businessOthername) {
        this.businessOthername = businessOthername;
    }

    public String getWarningId() {
        return warningId;
    }

    public void setWarningId(String warningId) {
        this.warningId = warningId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getWarninStatus() {
        return warningStatus;
    }

    public void setWarninStatus(Integer warninStatus) {
        this.warningStatus = warninStatus;
    }

    public String getWarningOperating() {
        return warningOperating;
    }

    public void setWarningOperating(String warningOperating) {
        this.warningOperating = warningOperating;
    }
}




















