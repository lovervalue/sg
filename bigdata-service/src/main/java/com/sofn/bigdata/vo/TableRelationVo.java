package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="表关系设置Vo对象")
public class TableRelationVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "左表名称")
    @Length(max = 255,message = "左表名称超长")
    private String leftTbName;

    @ApiModelProperty(value = "左列名称")
    @Length(max = 255,message = "左列名称超长")
    private String leftTbColumn;

    @ApiModelProperty(value = "表关系连接方式")
    @Length(max = 32,message = "表关系连接方式超长")
    private String unionType;

    @ApiModelProperty(value = "右表")
    @Length(max = 255,message = "右表超长")
    private String rightTbName;

    @ApiModelProperty(value = "右列")
    @Length(max = 255,message = "右列超长")
    private String rightTbColumn;

    @ApiModelProperty(value = "备注")
    @Length(max = 255,message = "备注超长")
    private String tbComment;

    @ApiModelProperty(value = "模块ID")
    @Length(max = 32,message = "模块ID超长")
    private String moduleid;

}
