package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.HashMap;
import java.util.List;

@Data
@ApiModel(value="远程在线实时预警站点中工段参数详情VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class AstcapMonitorAlarmSiteVo {

    @ApiModelProperty("站点ID")
    @Length(max = 32,message = "站点ID超长")
    private String siteid;

    @ApiModelProperty("站点名称")
    @Length(max = 32,message = "站点名称超长")
    private String sitename;

    @ApiModelProperty("经度")
//    @Length(max = 32,message = "经度超长")
    private String longitude;

    @ApiModelProperty("纬度")
//    @Length(max = 32,message = "纬度超长")
    private String latitude;

    @ApiModelProperty("参数详情")
    private List<HashMap<String, Object>> configdetail;
}
