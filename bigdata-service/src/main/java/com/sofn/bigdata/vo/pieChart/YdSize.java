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
@ApiModel(value="园地面积")
public class YdSize {
    @ApiModelProperty(value = "果园面积")
    private String ydGySize;
    @ApiModelProperty(value = "茶园面积")
    private String ydCySize;
    @ApiModelProperty(value = "桑园面积")
    private String ydSySize;
    @ApiModelProperty(value = "其它面积 ")
    private String ydOtherSize;


}
