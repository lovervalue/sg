package com.sofn.bigdata.vo.pieChart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: pcnpl-service
 * @author: zhaopeng
 * @create: 2020-07-29 15:11
 **/
@Data
@ApiModel(value="肥料使用指标需要用的实体")
public class Flsy {
    @ApiModelProperty(value = "n")
    private Double plantingSize;
    @ApiModelProperty(value = "syAmount")
    private Double syAmount;
    @ApiModelProperty(value = "n")
    private Double n;
    @ApiModelProperty(value = "p2o5")
    private Double p2o5;
    @ApiModelProperty(value = "k2o")
    private Double k2o;
    @ApiModelProperty(value = "reginName")
    private String reginName;




}
