package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="枚举对象的通过返回模版")
@NoArgsConstructor
@AllArgsConstructor
public class EnumVo {
    /**
     *状态值
     */
    @ApiModelProperty("code值")
    private String code;

    /**
     *状态描述
     */
    @ApiModelProperty("描述信息")
    @Length(max = 32,message = "描述信息【字段超长】")
    private String desc;

    @ApiModelProperty("补充信息")
    @Length(max = 32,message = "补充信息【字段超长】")
    private String supply;
}

