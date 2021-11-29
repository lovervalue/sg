package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="表属性VO对象")
public class ColumnVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "字段名称")
    private String columnName;

    @ApiModelProperty(value = "字段别名")
    private String columnAlias;

    @ApiModelProperty(value = "数据类型")
    private String columnType;

    @ApiModelProperty(value = "数据格式")
    private String columnForm;

}
