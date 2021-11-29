package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="数据源表信息Vo对象")
public class SourceTableInfoVo {

    @ApiModelProperty(value = "表ID")
    @Length(max = 32,message = "表ID超长")
    private String tbid;

    @ApiModelProperty(value = "数据库ID")
    @Length(max = 32,message = "数据库ID超长")
    private String sourceid;

    @ApiModelProperty(value = "表名称")
    @Length(max = 255,message = "表名称超长")
    private String tbName;

    @ApiModelProperty(value = "表别名")
    @Length(max = 255,message = "表别名超长")
    private String tbAlias;

    @ApiModelProperty(value = "表被选中状态:1未选择，2选择")
    @Length(max = 32,message = "表被选中状态超长")
    private String tbStatus;
}
