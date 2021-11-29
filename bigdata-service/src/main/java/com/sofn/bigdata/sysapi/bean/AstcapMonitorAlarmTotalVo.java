package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.HashMap;

@Data
@ApiModel(value="远程在线实时预警顶层参数统计值VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class AstcapMonitorAlarmTotalVo {

    @ApiModelProperty("秸秆")
    @Length(max = 32,message = "秸秆超长")
    private String jg = "0";

    @ApiModelProperty("粪便")
    @Length(max = 32,message = "粪便超长")
    private String fb = "0";

    @ApiModelProperty("产气量")
    @Length(max = 32,message = "产气量超长")
    private String cql = "0";

    @ApiModelProperty("CNG流量")
    @Length(max = 32,message = "CNG流量超长")
    private String cng = "0";

    @ApiModelProperty("发电量")
    @Length(max = 32,message = "发电量超长")
    private String fdl = "0";

    @ApiModelProperty("产气量月份统计值")
    private HashMap<String,String> cqlMonthData;
}
