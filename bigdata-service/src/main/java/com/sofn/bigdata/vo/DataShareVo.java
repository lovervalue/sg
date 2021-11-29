package com.sofn.bigdata.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@ApiModel(value="数据共享VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class DataShareVo {

    @ApiModelProperty("ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty("接口ID")
    @Length(max = 32,message = "接口ID超长")
    private String interId;

    @ApiModelProperty("接口名称")
    @Length(max = 255,message = "接口名称超长")
    private String interName;

    @ApiModelProperty("接口描述")
    @Length(max = 255,message = "接口描述超长")
    private String interComment;

    @ApiModelProperty("接口URL")
    @Length(max = 255,message = "接口URL超长")
    private String interUrl;

    @ApiModelProperty("请求方式")
    @Length(max = 32,message = "请求方式超长")
    private String interRequest;

    @ApiModelProperty("频率")
    @Length(max = 32,message = "频率超长")
    private String interReg;

    @ApiModelProperty("参数")
    @Length(max = 2000,message = "参数超长")
    private String interParams;

    @ApiModelProperty("状态:0禁止，1启动")
    @Length(max = 32,message = "状态")
    private String interStatus;

    @ApiModelProperty("操作者")
    @Length(max = 32,message = "操作者超长")
    private String operator;

    @ApiModelProperty("操作时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operatime;

}
