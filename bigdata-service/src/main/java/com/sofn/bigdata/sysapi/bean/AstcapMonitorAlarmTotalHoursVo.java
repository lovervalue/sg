package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="远程在线实时预警顶层参数统计值VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class AstcapMonitorAlarmTotalHoursVo {

    @ApiModelProperty("区域ID")
    private String area;

    @ApiModelProperty("产气量")
    @Length(max = 32,message = "产气量超长")
    private String cql = "0";

    @ApiModelProperty("CNG流量")
    @Length(max = 32,message = "CNG流量超长")
    private String cng = "0";

    @ApiModelProperty("小时")
    private String hours = "00";

}
