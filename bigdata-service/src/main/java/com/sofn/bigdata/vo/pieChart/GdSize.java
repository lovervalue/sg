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
@ApiModel(value="耕地面积")
public class GdSize {
    @ApiModelProperty(value = "旱地面积")
    private String gdHdSize;
    @ApiModelProperty(value = "水田面积")
    private String gdStSize;


}
