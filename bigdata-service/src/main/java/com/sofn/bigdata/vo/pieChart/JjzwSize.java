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
@ApiModel(value="经济作物面积")
public class JjzwSize {
    @ApiModelProperty(value = "油料作物面积")
    private String jjzwYlzwSize;
    @ApiModelProperty(value = "棉麻面积")
    private String jjzwMmSize;
    @ApiModelProperty(value = "糖料面积")
    private String jjzwTlSize;
    @ApiModelProperty(value = "烟叶面积 ")
    private String jjzwYySize;




}
