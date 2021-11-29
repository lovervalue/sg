package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/25 10:41
 * @Description
 */
@TableName("warning_result")
public class WarningResult {

    @TableId(value = "warning_id",type= IdType.UUID)
    private String warningId;
    //业务数据名称
    private String dataName;
    //预警名称

    @ApiModelProperty("预警名称")
    private String warningName;

    @ApiModelProperty("触发次数")
    private Integer triggersNumber;

    @ApiModelProperty("预警时间")
    private String warningTime;

    @ApiModelProperty("状态，1未处置 2处置中 3处置完成")
    private Integer status;

    @ApiModelProperty("操作")
    private String operating;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("操作时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date operatingTime;

    @ApiModelProperty("操作人员")
    private String operatingName;

    @Override
    public String toString() {
        return "WarningResult{" +
                "warningId='" + warningId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", warningName='" + warningName + '\'' +
                ", triggersNumber=" + triggersNumber +
                ", warningTime='" + warningTime + '\'' +
                ", status=" + status +
                ", operating='" + operating + '\'' +
                ", remarks='" + remarks + '\'' +
                ", operatingTime=" + operatingTime +
                ", operatingName='" + operatingName + '\'' +
                '}';
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

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public Integer getTriggersNumber() {
        return triggersNumber;
    }

    public void setTriggersNumber(Integer triggersNumber) {
        this.triggersNumber = triggersNumber;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperating() {
        return operating;
    }

    public void setOperating(String operating) {
        this.operating = operating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getOperatingName() {
        return operatingName;
    }

    public void setOperatingName(String operatingName) {
        this.operatingName = operatingName;
    }
}
