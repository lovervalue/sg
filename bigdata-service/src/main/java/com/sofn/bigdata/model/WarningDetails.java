package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Acthor Tao.Lee @date 2021/2/25 10:50
 * @Description 实体类
 */
@TableName("warning_details")
public class WarningDetails {

    @TableId(value = "warning_id",type= IdType.UUID)
    private String warningId;
    //业务数据名称
    private String dataName;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("地区")
    private String area;
    //产生量Yield
    @ApiModelProperty("产生量Yield")
    private Integer yield;
    //可收集量Collectableamount
    @ApiModelProperty("可收集量Collectableamount")
    private Integer collectableAmount;
    //秸秆利用量Straw utilization
    @ApiModelProperty("秸秆利用量Straw utilization")
    private Double strawUtilization;
    //综合利用率Comprehensive utilization
    @ApiModelProperty("秸秆利用量Straw utilization")
    private Double comprehensiveUtilization;

    @Override
    public String toString() {
        return "WarningDetailsMapper{" +
                "warningId='" + warningId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", year='" + year + '\'' +
                ", area='" + area + '\'' +
                ", yield=" + yield +
                ", collectableAmount=" + collectableAmount +
                ", strawUtilization=" + strawUtilization +
                ", comprehensiveUtilization=" + comprehensiveUtilization +
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getYield() {
        return yield;
    }

    public void setYield(Integer yield) {
        this.yield = yield;
    }

    public Integer getCollectableAmount() {
        return collectableAmount;
    }

    public void setCollectableAmount(Integer collectableAmount) {
        this.collectableAmount = collectableAmount;
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
}
