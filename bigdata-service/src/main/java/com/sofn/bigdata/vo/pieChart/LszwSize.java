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
@ApiModel(value="粮食作物面积")
public class LszwSize {
    @ApiModelProperty(value = "小麦面积")
    private String lszwXmSize;
    @ApiModelProperty(value = "玉米面积")
    private String lszwYmSize;
    @ApiModelProperty(value = "水稻面积")
    private String lszwSdSize;
    @ApiModelProperty(value = "马铃薯面积 ")
    private String lszwMlsSize;
    @ApiModelProperty(value = "豆类面积")
    private String lszwDouSize;
    @ApiModelProperty(value = "其它面积")
    private String lszwOtherLszwSize;



}
