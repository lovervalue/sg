package com.sofn.bigdata.vo;

import com.sofn.bigdata.enums.SamplingTaskEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 统计查询对象vo
 * @author: tc
 * @date: 2020/8/6 16:11
 */
@Data
@ApiModel(value="统计数据表查询对象vo")
public class AstiiStatisticQueryVo {
    
    @ApiModelProperty(value = "年份(氮磷系统必填,其他系统不填则默认当前年份,野生植物保护名录系统无该字段)")
    private String year;
    
    @ApiModelProperty(value = "行政代码(地膜系统只填写省份code,农作物秸秆资源台账子系统必填)")
    private String regionCode;
    
    @ApiModelProperty(value = "地图当前行政级别(ad_country:国家级,ad_province:省级,ad_city:市级,ad_county:区县级)(国控土壤系统和地膜和野生植物保护名录系统无区县级)(野生植物保护名录系统格式:行政区域代码,510001-510100-510102)(必填)")
    private String regionLevel;

    @ApiModelProperty(value = "指标(氮磷,地膜,农作物秸秆资源台账子系统必填,国控土壤系统不填)")
    private String index;

    @ApiModelProperty(value = "系统ID",required = true)
    private String sysCode;

    @ApiModelProperty(value = "一二级指标集合(农业体系选填)")
    List<AstiiStatisticQueryIndexVo> list;

//    @ApiModelProperty(name = "siteCode", value = "国控点（支持模糊查询）(国控土壤系统)")
//    private String siteCode;

    @ApiModelProperty(name = "areaCentreId", value = "区域中心ID(国控土壤系统)")
    private String areaCentreId;

    @ApiModelProperty(name = "taskCategory", value = "任务期数,中文:任务一,任务二(国控土壤系统)")
    private String taskCategory;

    @ApiModelProperty(name = "taskType", value = "任务类型,数字:1-土壤，2-农产品(默认查询土壤)-国控土壤系统")
    private String taskType = SamplingTaskEnum.TASK_TYPE_1.getKey();

    @ApiModelProperty(name = "plantId", value = "植物id(农业野生植物保护名录管理子系统使用 选填)")
    private String plantId;

}
