package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="枚举对象")
@NoArgsConstructor
@AllArgsConstructor
public class DataEnumVo {
    /**
     *状态值
     */
    @ApiModelProperty("状态值")
    private String code;

    /**
     *状态描述
     */
    @ApiModelProperty("状态描述")
    @Length(max = 32,message = "状态描述【字段超长】")
    private String desc;

}
