package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "沼气数据对象")
@NoArgsConstructor
@AllArgsConstructor
public class MarshInfoVo {

    /**
     * 工段
     */
    @ApiModelProperty("工段信息【必填】")
    @NotEmpty(message = "工段信息必填")
    private String workStage;

    /**
     * 目标参数
     */
    @ApiModelProperty("参数【必填】")
    @NotEmpty(message = "参数必填")
    private String targetParam;

    /**
     * 目标参数的单位，如流量(m3/h)、压力(kPa)等
     */
    @ApiModelProperty("目标参数的单位，如流量(m3/h)、压力(kPa)【必填】")
    @NotEmpty(message = "参数单位必填")
    private String paramUnit;

    /**
     * 监测时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("监测时间 yyyy-MM-dd HH:mm:ss【必填】")
    @NotEmpty(message = "监测时间必填")
    private String monitorTime;

    /**
     * 当前值
     */
    @ApiModelProperty("当前值【必填】")
    @NotEmpty(message = "当前值必填")
    private String currentValue;

    @ApiModelProperty("采集设备唯一编号")
    private String identifier;

    @ApiModelProperty("设施唯一编号")
    private String facilityno;

    @ApiModelProperty("监测设备唯一编号")
    private String mdevno;

}
