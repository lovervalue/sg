package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 *@Description 行政区域描点视图数据对象
 *@Author quzhijie
 *@Date 2020/7/24 18:09
 *@Version 1.0
 **/
@ApiModel(value = "行政区域描点视图数据对象")
@Data
public class AdPointData implements Serializable {

  /**
   * 区域名称
   */
  @ApiModelProperty("区域名称")
  private String adRegionName;
  /**
   * 行政区域代码
   */
  @ApiModelProperty("行政区域代码")
  private String adRegionCode;
  /**
   * 地块id
   */
  @ApiModelProperty("地块id")
  private String dkId;
  /**
   * 地块地址
   */
  @ApiModelProperty("地块编号")
  private String dkCode;
  /**
   * 经度
   */
  @ApiModelProperty("经度")
  private String longitude;
  /**
   * 纬度
   */
  @ApiModelProperty("纬度")
  private String latitude;

  @ApiModelProperty("地图图表类型，柱状图：columnar，饼状图：pancake")
  private String chartType;
  /**
   * 行政区域指标数据值
   */
  @ApiModelProperty("行政区域指标数据值")
  private String indexValue;

  /**
   * 行政区域指标数据值级别，前端可通过该级别来确定描点覆盖物
   */
  @ApiModelProperty("行政区域指标数据值级别，前端可通过该级别来确定描点覆盖物")
  private String indexValueLevel;

  /**
   * 行政区域指标详细数据信息，前端可用来显示toast
   */
  @ApiModelProperty("行政区域指标详细数据信息，前端可用来显示toast")
  private Map<String,Object> indexInfo;

  /**
   * 指标级别标签列表,key为indexValueLevel，value为指数级别名称，前端用来显示提示信息
   */
  @ApiModelProperty("指标级别标签列表,key为indexValueLevel，value为指数级别名称，前端用来显示提示信息")
  private Map<String,String> indexLabelList;
}
