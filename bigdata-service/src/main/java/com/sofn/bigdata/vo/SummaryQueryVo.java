package com.sofn.bigdata.vo;

import com.sofn.bigdata.enums.SamplingTaskEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang xin
 * @date 2020/6/16 10:49
 */
@Data
@ApiModel(value = "首页查询VO对象")
public class SummaryQueryVo implements Serializable {

    @ApiModelProperty(name = "adLevel", value = "地图当前行政级别(ad_country:国家级,ad_province:省级,ad_city:市级)")
    private String adLevel = "ad_country";

    @ApiModelProperty(name = "adCode", value = "行政区域code")
    private String adCode;

    @ApiModelProperty(name = "year", value = "年份,默认为当前年")
    private String year = String.valueOf(LocalDateTime.now().getYear());

    @ApiModelProperty(name = "siteCode", value = "国控点（支持模糊查询）")
    private String siteCode;

    @ApiModelProperty(name = "areaCentreId", value = "区域中心ID")
    private String areaCentreId;

    @ApiModelProperty(name = "taskCategory", value = "任务期数,中文:任务一,任务二")
    private String taskCategory;

    @ApiModelProperty(name = "taskType", value = "任务类型,数字:1-土壤，2-农产品(默认查询土壤)")
    private String taskType = SamplingTaskEnum.TASK_TYPE_1.getKey();

}
