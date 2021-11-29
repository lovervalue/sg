package com.sofn.bigdata.vo;

import com.sofn.bigdata.sysapi.bean.AgzimlKmAlienSpeciesDistributionParam;
import com.sofn.bigdata.sysapi.bean.SummaryQueryVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Zhang Yi
 * @Date 2020/8/24 9:44
 * @Version 1.0
 * 7个系统入参、参数
 */
@Data
@ApiModel("7个系统大数据展示入参、参数")
public class BigDataShowVo {

	@ApiModelProperty("农产品产地环境评价中心-首页查询VO对象")
	private SummaryQueryVo summaryQueryVo;

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

	@ApiModelProperty("信息与培训处-区域编码")
	private String regionCode;

	@ApiModelProperty("信息与培训处-年份")
	private String year;

	@ApiModelProperty("资源保护处-外来入侵")
	private AgzimlKmAlienSpeciesDistributionParam agzimlKmAlienSpeciesDistributionParam;





}
