package com.sofn.bigdata.sysapi.bean;

import com.sofn.bigdata.vo.MapViewDataA;
import com.sofn.common.map.MapViewData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class MapData {

    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String name;
    /**
     * 系统AppID
     */
    @ApiModelProperty(value = "系统AppID")
    private String code;
    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private MapViewData mapViewData;

    @ApiModelProperty(value = "数据3")
    private MapViewDataA mapViewData2;

    @ApiModelProperty(value = "数据2")
    private List datas;
    /**
     * 备注
     */
    @ApiModelProperty(value = "信息")
    private String msg;
}
