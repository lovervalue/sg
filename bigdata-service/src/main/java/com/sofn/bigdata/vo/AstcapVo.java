package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 11:00
 * @Version 1.0
 * 可再生能源处-查询对象
 */
@ApiModel("可再生能源处-查询对象")
@Data
public class AstcapVo {
	@ApiModelProperty("可再生能源处-站点Id")
	private String siteId;

	@ApiModelProperty("可再生能源处-区域Id")
	private String areaCode;

	@ApiModelProperty("可再生能源处-工段参数ID")
	private  String gdconfigid;

	@ApiModelProperty("可再生能源处-公司Id")
	private  String companyId;

	@ApiModelProperty("可再生能源处-项目类型")
	private String prjType;

	@ApiModelProperty("可再生能源处-状态:1运行, 2暂停")
	private String status;

	@ApiModelProperty("监测时间：xxxx-xx-xx")
	private String alarmdate;


}
