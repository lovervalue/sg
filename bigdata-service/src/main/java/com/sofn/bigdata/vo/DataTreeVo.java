package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="业务数据树Vo对象")
public class DataTreeVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "子对象")
    private DataTreeVo dataTreeVo;

    @ApiModelProperty(value = "深度")
    private String datalevel;

}
