package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Acthor Tao.Lee @date 2021/2/25 10:57
 * @Description
 */
@TableName("warning_disposal")
public class WarningDisposal {

    @TableId(value = "warning_id",type= IdType.UUID)
    private String warningId;
    //业务数据名称
    private String dataName;

    @ApiModelProperty("预警等级")
    private String warningLevel;

    @ApiModelProperty("预警名称")
    private String warningName;

    @ApiModelProperty("触发次数")
    private String triggersNumber;

    @ApiModelProperty("处置方式")
    private String solvingMethod;

    @ApiModelProperty("备注")
    private String remarks;

    @Override
    public String toString() {
        return "WarningDisposal{" +
                "warningId='" + warningId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", warningLevel='" + warningLevel + '\'' +
                ", warningName='" + warningName + '\'' +
                ", triggersNumber='" + triggersNumber + '\'' +
                ", solvingMethod='" + solvingMethod + '\'' +
                ", remarks='" + remarks + '\'' +
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

    public String getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(String warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public String getTriggersNumber() {
        return triggersNumber;
    }

    public void setTriggersNumber(String triggersNumber) {
        this.triggersNumber = triggersNumber;
    }

    public String getSolvingMethod() {
        return solvingMethod;
    }

    public void setSolvingMethod(String solvingMethod) {
        this.solvingMethod = solvingMethod;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
