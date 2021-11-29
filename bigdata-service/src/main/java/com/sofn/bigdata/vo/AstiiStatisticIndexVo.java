package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 统计分析的通用指标对象包装
 * @author: tc
 * @date: 2020/8/5 14:36
 */
@Data
@ApiModel(value="统计分析的通用指标对象包装")
public class AstiiStatisticIndexVo {

    @ApiModelProperty(value = "指标名称")
    private String indexName;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "地图类型")
    private String mapType;

    @ApiModelProperty(value = "识别码")
    private String code;

    @ApiModelProperty(value = "指标集合")
    private List<AstiiStatisticIndexVo> list;
    
}
