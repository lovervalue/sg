package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/8 14:41
 * @Description
 */
//新增阈值模型
@TableName("threshold_model")
public class Thresholdmodel {

    @TableId(value = "threshold_id",type= IdType.UUID)
    @ApiModelProperty("模型id不传")
    private String thresholdId;
    //预警级别一级到五级到其他
    @ApiModelProperty("预警级别一级到五级到其他")
    private String warningLevel;
    //级别名称
    @ApiModelProperty("级别名称")
    private String levelName;
    //业务数据名称
    @ApiModelProperty("业务数据名称")
    private String dataName;
    //产生量Yield
    //@ApiModelProperty("产生量Yield")
    //private Integer yield;
    //可收集量Collectableamount
    @ApiModelProperty("动态字段")
    private String dynamidField;
    //秸秆利用量Straw utilization
    @ApiModelProperty("秸秆利用量")
    private Double strawUtilization;
    @ApiModelProperty("综合利用率")
    //综合利用率Comprehensive utilization
    private Double comprehensiveUtilization;
    //操作1Operation 1
    @ApiModelProperty("操作1")
    private String operationOne;
    //数值1Value 1
    @ApiModelProperty("数值1")
    private Integer valueOne;
    //字体颜色font color1
    @ApiModelProperty("字体颜色1")
    private String fontColorOne;
    //背景颜色background color 1
    @ApiModelProperty("背景颜色1")
    private String backgroundColorOne;
    //操作1Operation 2
    @ApiModelProperty("操作2")
    private String operationTwo;
    //数值1Value 2
    @ApiModelProperty("数值2")
    private Integer valueTwo;
    //字体颜色font color2
    @ApiModelProperty("字体颜色2")
    private String fontColorTwo;
    @ApiModelProperty("背景颜色2")
    //背景颜色background color 2
    private String backgroundColorTwo;
    //创建时间
    @ApiModelProperty("创建时间，不传")
    private Date createTime;


    public String getThresholdId() {
        return thresholdId;
    }

    public void setThresholdId(String thresholdId) {
        this.thresholdId = thresholdId;
    }

    public String getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(String warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDynamidField() {
        return dynamidField;
    }

    public void setDynamidField(String dynamidField) {
        this.dynamidField = dynamidField;
    }

    public Double getStrawUtilization() {
        return strawUtilization;
    }

    public void setStrawUtilization(Double strawUtilization) {
        this.strawUtilization = strawUtilization;
    }

    public Double getComprehensiveUtilization() {
        return comprehensiveUtilization;
    }

    public void setComprehensiveUtilization(Double comprehensiveUtilization) {
        this.comprehensiveUtilization = comprehensiveUtilization;
    }

    public String getOperationOne() {
        return operationOne;
    }

    public void setOperationOne(String operationOne) {
        this.operationOne = operationOne;
    }

    public Integer getValueOne() {
        return valueOne;
    }

    public void setValueOne(Integer valueOne) {
        this.valueOne = valueOne;
    }

    public String getFontColorOne() {
        return fontColorOne;
    }

    public void setFontColorOne(String fontColorOne) {
        this.fontColorOne = fontColorOne;
    }

    public String getBackgroundColorOne() {
        return backgroundColorOne;
    }

    public void setBackgroundColorOne(String backgroundColorOne) {
        this.backgroundColorOne = backgroundColorOne;
    }

    public String getOperationTwo() {
        return operationTwo;
    }

    public void setOperationTwo(String operationTwo) {
        this.operationTwo = operationTwo;
    }

    public Integer getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(Integer valueTwo) {
        this.valueTwo = valueTwo;
    }

    public String getFontColorTwo() {
        return fontColorTwo;
    }

    public void setFontColorTwo(String fontColorTwo) {
        this.fontColorTwo = fontColorTwo;
    }

    public String getBackgroundColorTwo() {
        return backgroundColorTwo;
    }

    public void setBackgroundColorTwo(String backgroundColorTwo) {
        this.backgroundColorTwo = backgroundColorTwo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
