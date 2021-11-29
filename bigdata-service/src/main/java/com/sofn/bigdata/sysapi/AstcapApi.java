package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.sysapi.bean.*;
import com.sofn.bigdata.vo.SiteForm;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhang Yi
 * @Date 2020/8/20 9:51
 * @Version 1.0
 * 可再生能源处：ZhouQingChun
 */
@FeignClient(
		value = "astcap-service",
		configuration = FeignConfiguration.class
)
public interface AstcapApi {

	//查询工段参数详情

	@GetMapping("/astcapMonitorAlarm/getAlarmConfigDetail")
	public Result<AstcapMonitorAlarmSiteVo> getAlarmConfigDetail(@RequestParam(name="siteid") @ApiParam(value = "站点ID",required = true) String siteid,
	                                                             @RequestParam(name="alarmdate",required = false) @ApiParam(value = "监测时间：xxxx-xx-xx") String alarmdate);




	//("按小时查询工段参数累计值（产气量、CNG流量）,有siteid为站点的，没有siteid时候根据区域码查询")

	@GetMapping("/astcapMonitorAlarm/getAlarmTotalByHours")
	public Result<List<AstcapMonitorAlarmTotalHoursVo>> getAlarmTotalByHours(@RequestParam(name="siteid",required = false) @ApiParam(value = "站点ID") String siteid,
	                                                                         @RequestParam(name="areaCode",required = false) @ApiParam(value = "区域代码：全国(100000)") String areaCode,
	                                                                         @RequestParam(name="alarmdate",required = false) @ApiParam(value = "监测时间：xxxx-xx-xx") String alarmdate);








	//按条件获取站点信息集合
	@PostMapping(value = "/site/getSiteListByCondition")

	public Result<List<SiteForm>> getSiteListByCondition(

			@RequestParam(value = "companyId", required = false)  @ApiParam(name = "companyId",value = "公司ID")      String companyId,

			@RequestParam(value = "prjType", required = false) @ApiParam(name = "prjType",value = "项目类型") String prjType,

			@RequestParam(value = "status", required = false) @ApiParam(name = "status",value = "状态:1运行, 2暂停") String status);





/**
 * 4.农业资源保护环境：ZhouQingChun
 */
//查询人员数量合计table
@GetMapping(value = "/astiiRepaiRecord/getPersonTotal")
public Result<List<Map<String, Object>>> getPersonTotal(@RequestParam(name = "regionCode")
                                                        @ApiParam(value = "区域编码", required = true) String regionCode,
                                                        @RequestParam(name = "year")
                                                        @ApiParam(value = "年份", required = true) String year);


	//农业资源保护环境("查询人员数量地图")
	//@ApiOperation(value = "查询人员数量地图", notes = "权限码：（astcap:astiiRepaiRecord:getPersonMap）")
	@GetMapping(value = "/astiiRepaiRecord/getPersonMap")
	public Result<List<Map<String, Object>>> getPersonMap( @RequestParam(name = "regionCode") @ApiParam(value = "区域编码", required = true) String regionCode,
	                                                       @RequestParam(name = "year") @ApiParam(value = "年份", required = true) String year);





}





