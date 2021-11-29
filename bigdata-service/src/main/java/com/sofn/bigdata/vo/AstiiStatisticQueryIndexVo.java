package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 查询时一二级指标对象
 * @author: tc
 * @date: 2020/8/6 16:04
 */
@Data
@ApiModel(value="查询时一二级指标对象")
public class AstiiStatisticQueryIndexVo {
    
    @ApiModelProperty(value = "一级指标名称")
    private String firstIndexName;
    
    @ApiModelProperty(value = "二级指标名称")
    private String secondIndexName;
}
