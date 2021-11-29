package com.sofn.bigdata.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 区域中心Bean
 * @Author: mcc
 * @Date: 2019\11\25 0025
 */
@Data
public class AreaCentre extends BaseModel<AreaCentre> {

    ///**
    // * 区域类型
    // */
    //@ApiModelProperty(name = "areaName", value = "区域名称")
    //private String areaName;

    /**
     * 区域中心名称
     */
    @ApiModelProperty(name = "name", value = "区域中心名称")
    private String name;


}
