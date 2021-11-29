package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@ApiModel(value="远程在线实时预警监测参数每月值VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class AstcapMonitorAlarmGdConfigVo {

    @ApiModelProperty("工段id|参数id:'xxx|xxx'")
    private String gdconfigid;

    @ApiModelProperty("工段名称+参数名称")
    private String gdconfigname;

    @ApiModelProperty("月份统计值")
    private HashMap<String,String> monthData;

    @ApiModelProperty("区域码")
    private String areaCode;

    @ApiModelProperty("累计值")
    private String totalValue;
}
