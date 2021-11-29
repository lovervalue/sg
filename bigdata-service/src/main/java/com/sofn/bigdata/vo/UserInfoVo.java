package com.sofn.bigdata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: UserInfoVo
 * @author: Chenlf
 * @date: 2019/9/22 9:51
 */
@Data
@ApiModel(value="用户信息VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
    @ApiModelProperty(value = "用户信息是否完善：0-未完善；1-已完善")
    private String flag;

    @ApiModelProperty(value = "用户类型：-1-地膜系统管理员；0-总站；1-省站；2-质控；3-监测；4-县级填报员")
    private String type;

}
