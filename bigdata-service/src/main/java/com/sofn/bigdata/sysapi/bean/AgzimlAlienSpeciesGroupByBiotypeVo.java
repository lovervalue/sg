/*
 * Copyright (c) 1998-2020 SOFN Corporation
 * All rights reserved.
 *
 *
 */
package com.sofn.bigdata.sysapi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家重点管理外来入侵物种分组查询VO
 *
 * @author John
 * @version 1.0
 **/
@Data
@ApiModel
public class AgzimlAlienSpeciesGroupByBiotypeVo implements Serializable {
    private static final long serialVersionUID = -3788851864604209369L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "物种名称对应的主键ID", position = 0)
    @JSONField(ordinal = 0)
    private String id;
    /**
     * 中文名称
     */
    @ApiModelProperty(value = "物种中文名称", position = 1)
    @JSONField(ordinal = 1)
    private String chineseName;

    /**
     * 国内分布(逗号分隔)
     */
    @ApiModelProperty(value = "国内分布(逗号分隔),只返回本省的区划名称", position = 2)
    @JSONField(ordinal = 2)
    private String domesticDistribute;
}
