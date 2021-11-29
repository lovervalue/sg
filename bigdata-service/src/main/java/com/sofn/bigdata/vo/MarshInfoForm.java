package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(value = "沼气数据的录入对象(含站点，采集频率)")
@NoArgsConstructor
@AllArgsConstructor
public class MarshInfoForm {
    /**
     * 站点编码
     */
    @ApiModelProperty("站点编码")
    @NotEmpty(message = "站点编码必填")
    private String siteId;

    /**
     * 采集频率
     */
    @ApiModelProperty("采集频率")
    @NotEmpty(message = "采集频率必填")
    private String collectFrequency;

    @ApiModelProperty("某站点在一个采集周期内的沼气信息集合")
    @NotEmpty(message = "某站点在一个采集周期内的沼气信息集合")
    private List<MarshInfoVo> marshInfoList;
}
