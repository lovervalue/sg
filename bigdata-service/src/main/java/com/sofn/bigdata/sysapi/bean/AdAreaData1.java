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
@ApiModel(value = "区域氮磷钾统计对象")
@Data
public class AdAreaData1 implements Serializable {
  /**
   * 行政区域名称
   */
  @ApiModelProperty("行政区域名称")
  private String adRegionName;
  @ApiModelProperty("地图当前行政级别")
  private String adLevel;

  @ApiModelProperty("行政区域代码")
  private String adRegionCode;
  @ApiModelProperty("n含量")
  private String n;
  @ApiModelProperty("p205含量")
  private String p2o5;
  @ApiModelProperty("k20含量")
  private String k2o;

}
