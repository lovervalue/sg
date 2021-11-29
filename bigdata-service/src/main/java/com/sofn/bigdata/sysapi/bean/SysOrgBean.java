package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysOrgBean {

    @ApiModelProperty(value = "省代码")
    private String province;

    @ApiModelProperty(value = "市代码")
    private String city;

    @ApiModelProperty(value = "区代码")
    private String area;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "区名称")
    private String areaName;
}
