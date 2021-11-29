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
@ApiModel(value="蔬菜瓜果面积")
public class GgscSize {
    @ApiModelProperty(value = "蔬菜面积")
    private String scggScSize;
    @ApiModelProperty(value = "瓜果面积")
    private String scggGgSize;


}
