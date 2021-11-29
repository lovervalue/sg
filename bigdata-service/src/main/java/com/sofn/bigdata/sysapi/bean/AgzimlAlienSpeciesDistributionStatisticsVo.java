package com.sofn.bigdata.sysapi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 国家重点管理外来入侵物种分布统计数据VO
 *
 * @author John
 * @version 1.0
 */
@Data
@ApiModel
public class AgzimlAlienSpeciesDistributionStatisticsVo implements Serializable {
    private static final long serialVersionUID = -5829907260115001515L;
    // 省级区划名称
    @ApiModelProperty(value = "省级区划名称", position = 0)
    @JSONField(ordinal = 0)
    private String provinceName;
    // 省级区划编码
    @ApiModelProperty(value = "省级区划编码", position = 1)
    @JSONField(ordinal = 1)
    private String provinceCode;
    // 物种总数
    @ApiModelProperty(value = "物种总数", position = 2)
    @JSONField(ordinal = 2)
    private Integer speciesTotal = 0;
    // 植物物种总数
    @ApiModelProperty(value = "植物物种总数", position = 3)
    @JSONField(ordinal = 3)
    private Integer plantSpeciesTotal = 0;
    // 动物物种总数
    @ApiModelProperty(value = "动物物种总数", position = 4)
    @JSONField(ordinal = 4)
    private Integer animalSpeciesTotal = 0;
    // 微生物物种总数
    @ApiModelProperty(value = "微生物物种总数", position = 5)
    @JSONField(ordinal = 5)
    private Integer microbeSpeciesTotal = 0;
    // 植物物种列表
    @ApiModelProperty(value = "植物物种列表", position = 6)
    @JSONField(ordinal = 6)
    private List<AgzimlAlienSpeciesGroupByBiotypeVo> plantSpeciesList = new ArrayList<>();
    // 动物物种列表
    @ApiModelProperty(value = "动物物种列表", position = 7)
    @JSONField(ordinal = 7)
    private List<AgzimlAlienSpeciesGroupByBiotypeVo> animalSpeciesList = new ArrayList<>();
    // 微生物物种列表
    @ApiModelProperty(value = "微生物物种列表", position = 8)
    @JSONField(ordinal = 8)
    private List<AgzimlAlienSpeciesGroupByBiotypeVo> microbeSpeciesList = new ArrayList<>();
}
