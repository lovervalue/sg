package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
@ApiModel(description = "区域数据统计VO对象", value = "AreaDataVo")
public class AreaDataVo implements Serializable {

    @ApiModelProperty("地图视图类型，描点：point，区域：area")
    private String viewType;

    /**
     * 区域信息
     */
    @ApiModelProperty(name = "adRegionName", value = "行政区域名称")
    private String adRegionName;

    @ApiModelProperty(name = "adRegionCode", value = "行政区域代码")
    private String adRegionCode;

    @ApiModelProperty(name = "sampleSum", value = "样品总数")
    private Integer sampleSum;

    @ApiModelProperty(name = "sampleFinishSum", value = "采样完成")
    private Integer sampleFinishSum;

    @ApiModelProperty(name = "preparationUnderway", value = "样品制备中")
    private Integer preparationUnderway;

    @ApiModelProperty(name = "preparationFinish", value = "制备完成")
    private Integer preparationFinish;

    @ApiModelProperty(name = "finishInspect", value = "已送检")
    private Integer finishInspect;

    /**
     * 描点信息
     */
    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("点位详情信息，前端可用来显示toast")
    private Map<String, Object> indexInfo;
}
