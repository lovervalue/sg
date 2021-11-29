package com.sofn.bigdata.vo;

import com.sofn.bigdata.vo.pieChart.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 *@Description 行政区域视图数据对象
 *@Author quzhijie
 *@Date 2020/7/24 18:09
 *@Version 1.0
 **/
@ApiModel(value = "行政区域视图数据对象")
@Data
public class AdAreaData implements Serializable {
  /**
   * 行政区域名称
   */
  @ApiModelProperty("行政区域名称")
  private String adRegionName;

  /**
   * 行政区域代码
   */
  @ApiModelProperty("行政区域代码")
  private String adRegionCode;

  /**
   * 行政区域指标数据值
   */
  @ApiModelProperty("行政区域指标数据值")
  private String indexValue;
  @ApiModelProperty("n含量")
  private String n;
  @ApiModelProperty("p205含量")
  private String p2o5;
  @ApiModelProperty("k20含量")
  private String k2o;

  @ApiModelProperty("不同坡度耕地和园地面积小饼图")
  private Btpdgdyd btpdgdyd;

  @ApiModelProperty("耕地面积")
  private GdSize gdSize;
  @ApiModelProperty("菜地面积")
  private CdSize cdSize;
  @ApiModelProperty("园地面积")
  private YdSize ydSize;
  @ApiModelProperty("果园面积")
  private GySize gySize;
  @ApiModelProperty("粮食作物面积")
  private LszwSize lszwSize;
  @ApiModelProperty("经济作物面积")
  private JjzwSize jjzwSize;
  @ApiModelProperty("蔬菜瓜果面积")
  private GgscSize ggscSize;
  /**
   * 行政区域指标数据值级别，前端可通过该级别来确定该区域的高亮颜色
   */
  @ApiModelProperty("行政区域指标数据值级别，前端可通过该级别来确定该区域的高亮颜色")
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
