package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel(value="远程在线实时预警监测参数VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class AstcapMonitorAlarmGdConfigMapVo {

    @ApiModelProperty("区域码")
    private String areaCode;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("参数集合")
    private List<AstcapMonitorAlarmGdConfigVo> configVos;
}
