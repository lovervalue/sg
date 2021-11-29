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
@ApiModel(value="果园面积")
public class GySize {
    @ApiModelProperty(value = "苹果面积")
    private String gyPgSize;
    @ApiModelProperty(value = "梨面积")
    private String gyLSize;
    @ApiModelProperty(value = "葡萄面积")
    private String gyPtSize;
    @ApiModelProperty(value = "桃面积 ")
    private String gyTSize;
    @ApiModelProperty(value = "桔柑园面积")
    private String gyJgSize;
    @ApiModelProperty(value = "香蕉面积")
    private String gyXjSize;
    @ApiModelProperty(value = "菠萝面积")
    private String gyBlSize;
    @ApiModelProperty(value = "其它面积 ")
    private String gyOtherSize;


}
