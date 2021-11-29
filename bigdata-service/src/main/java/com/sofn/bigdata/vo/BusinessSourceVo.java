package com.sofn.bigdata.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@ApiModel(value="业务数据源Vo对象")
public class BusinessSourceVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "数据源ID")
    @Length(max = 32,message = "数据源ID超长")
    private String sourceid;

    @ApiModelProperty(value = "数据源名称")
    @Length(max = 255,message = "数据源名称超长")
    private String busiName;

    @ApiModelProperty(value = "数据源别名")
    @Length(max = 255,message = "数据源别名超长")
    private String busiAlias;

    @ApiModelProperty(value = "业务结构树json")
    private String dataJson;

    @ApiModelProperty(value = "数据源备注")
    @Length(max = 255,message = "数据源备注超长")
    private String busiComment;

    @ApiModelProperty(value = "操作人")
    @Length(max = 32,message = "操作人超长")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operatDate;

}
