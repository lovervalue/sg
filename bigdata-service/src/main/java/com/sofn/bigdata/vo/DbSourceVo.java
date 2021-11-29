package com.sofn.bigdata.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@ApiModel(value="数据源列表Vo对象")
public class DbSourceVo {

    @ApiModelProperty(value = "数据源ID")
    @Length(max = 32,message = "数据源ID超长")
    private String id;

    @ApiModelProperty(value = "数据源名称")
    @Length(max = 255,message = "数据源名称超长")
    private String name;

    @ApiModelProperty(value = "数据源别名")
    @Length(max = 255,message = "数据源别名超长")
    private String alias;

    @ApiModelProperty("操作者")
    @Length(max = 32,message = "操作者超长")
    private String operator;

    @ApiModelProperty("操作时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operatime;

}
