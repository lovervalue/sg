package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="业务数据分类Vo对象")
public class BusiDataVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "数据源id")
    @Length(max = 255,message = "数据源id超长")
    private String sourceid;

    @ApiModelProperty(value = "父id")
    @Length(max = 32,message = "父id超长")
    private String pid;

    @ApiModelProperty(value = "字段类型")
    @Length(max = 255,message = "字段类型超长")
    private String columnType;

    @ApiModelProperty(value = "名称")
    @Length(max = 255,message = "名称超长")
    private String dataname;

    @ApiModelProperty(value = "字段别名")
    @Length(max = 255,message = "字段别名超长")
    private String dataAlias;

    @ApiModelProperty(value = "描述")
    @Length(max = 255,message = "描述超长")
    private String dataComment;

    @ApiModelProperty(value = "数据类型")
    @Length(max = 255,message = "数据类型超长")
    private String dataType;

    @ApiModelProperty(value = "数据格式")
    @Length(max = 255,message = "数据格式超长")
    private String dataForm;
    
}
