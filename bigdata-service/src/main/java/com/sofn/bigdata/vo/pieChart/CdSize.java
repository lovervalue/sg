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
@ApiModel(value="菜地面积")
public class CdSize {
    @ApiModelProperty(value = "露地面积")
    private String cdLdSize;
    @ApiModelProperty(value = "保护地面积")
    private String cdBhdSize;


}
