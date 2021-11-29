package com.sofn.bigdata.vo;

import com.sofn.bigdata.sysapi.bean.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 15:06
 * @Version 1.0
 * 可再生能源处-沼气返回对象
 */
@Data
@ApiModel("可再生能源处-沼气返回对象")
public class AstcapDataVo {

	@ApiModelProperty("远程在线实时预警站点中工段参数详情VO对象")
	private AstcapMonitorAlarmSiteVo astcapMonitorAlarmSiteVo;


	@ApiModelProperty("站点VO对象")
	private List<SiteForm> siteFormList ;



}
