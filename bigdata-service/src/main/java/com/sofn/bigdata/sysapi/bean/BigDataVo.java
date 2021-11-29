package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@Description 行政区域视图数据对象
 *@Author quzhijie
 *@Date 2020/7/24 18:09
 *@Version 1.0
 **/
@ApiModel(value = "大数据氮磷钾数据返回vo")
@Data
public class BigDataVo implements Serializable {

  @ApiModelProperty("年份")
  private String years;

  @ApiModelProperty("数据对象")
  private AdAreaData1 adAreaData1;


}
