package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel(value="数据分析VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class DataAnalyzeVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "分析名称")
    private String analyzeName;

    @ApiModelProperty(value = "业务数据源ID")
    private String busiId;

    @ApiModelProperty(value = "描述")
    private String dataComment;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    private Date operatDate;

    @ApiModelProperty(value = "图表类型：1柱图，2线图，3饼图，4油量图，5地图，6其他")
    private String gramType;

    @ApiModelProperty(value = "图表数据json")
    private String dataDetail;

}
