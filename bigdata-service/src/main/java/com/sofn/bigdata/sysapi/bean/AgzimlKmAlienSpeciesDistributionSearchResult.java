/*
 * Copyright (c) 1998-2020 SOFN Corporation
 * All rights reserved.
 *
 *
 */
package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 国家重点管理外来入侵物种分布搜索结果VO
 *
 * @author John
 * @version 1.0
 **/
@ApiModel
@Data
public class AgzimlKmAlienSpeciesDistributionSearchResult implements Serializable {
    private static final long serialVersionUID = 646549859725252551L;
    /**
     * 外来入侵物种分布搜索结果列表
     */
    @ApiModelProperty(value = "外来入侵物种分布搜索结果列表: 可以用于在地图上标记物种分布的区划")
    private List<AgzimlKmAlienSpeciesDistributionVo> distributionVoList;
    /**
     * 外来入侵物种统计结果列表
     */
    @ApiModelProperty(value = "外来入侵物种分布统计结果列表：用于在地图上标记对应区划的物种总数")
    private List<AgzimlAlienSpeciesDistributionStatisticsVo> statisticsVoList;
}
