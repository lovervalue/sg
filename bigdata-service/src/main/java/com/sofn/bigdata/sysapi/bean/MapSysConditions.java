package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MapSysConditions {
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String indexName;
    /**
     * 系统AppID
     */
    @ApiModelProperty(value = "系统AppID")
    private String fieldName;
    /**
     * 指标
     */
    @ApiModelProperty(value = "指标")
    private List list;

    /**
     * 系统名称(沼气专用)
     */
    @ApiModelProperty(value = "系统名称(沼气专用)")
    private String configname;
    /**
     * 系统AppID(沼气专用)
     */
    @ApiModelProperty(value = "系统AppID(沼气专用)")
    private String configid;
    /**
     * 指标(沼气专用)
     */
    @ApiModelProperty(value = "指标(沼气专用)")
    private List configList;

    @ApiModelProperty(value = "识别码")
    private String code;
}
