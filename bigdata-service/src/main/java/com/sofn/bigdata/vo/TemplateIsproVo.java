package com.sofn.bigdata.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="模板字段VO对象")
public class TemplateIsproVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "字段名称")
    private String fieldname;

    @ApiModelProperty(value = "字段编码（字段英文名称）")
    private String fieldcode;

    @ApiModelProperty(value = "字段类型：文本（暂时默认）")
    private String fieldtype;

    @ApiModelProperty(value = "状态：0停用，1启用")
    private String state;

    @ApiModelProperty(value = "是否是决策问题：0是决策问题字段，1是决策提案字段")
    private String ispro;

}
