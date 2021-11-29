package com.sofn.bigdata.web;

import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import com.sofn.bigdata.model.MapViewData2;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.sysapi.*;
import com.sofn.bigdata.sysapi.bean.*;
import com.sofn.bigdata.sysapi.map.AeemMapApi;
import com.sofn.bigdata.vo.AstcapDataVo;
import com.sofn.bigdata.vo.AstcapVo;
import com.sofn.bigdata.vo.DucssVo;
import com.sofn.bigdata.vo.SiteForm;
import com.sofn.common.map.*;
import com.sofn.common.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author Zhang Yi
 * @Date 2020/8/17 15:38
 * @Version 1.0
 * 大数据展示
 */
@Slf4j
@Api(tags = "7个系统大数据展示")
@RestController
@RequestMapping("/Show")
public class BigDataShowController {
	//注入各个代理接口

	@Autowired
	private AstcapApi astcapApi;

	@Autowired
	private PcnplApi pcnplApi;

	@Autowired
	private PmlcscApi pmlcscApi;

	@Autowired
	private AgzimlApi agzimlApi;

	@Autowired
	private DucssApi ducssApi;

	@Autowired
	private AgpjzbApi agpjzbApi;


	@Autowired
	private AeemMapApi aeemMapApi;

	@Autowired
	private SysRegionApi sysRegionApi;

	@Autowired
	private AgziwjApi agziwjApi;

	//拆分成7个接口。

	@GetMapping("/pmlcsc")
	@ApiOperation(value = "农产品产地环境评价中心")
	public Result<List<AreaDataVo>> getData(@RequestParam @ApiParam(value = "地图当前行政级别(ad_country:国家级,ad_province:省级,ad_city:市级)", required = true) String adLevel,
	                                        @RequestParam @ApiParam(value = "行政区域代码", required = true) String adRegionCode,
	                                        @RequestParam(value = "year", required = false) @ApiParam(value = "年份，默认为当前年份") String year,
	                                        @RequestParam(value = "taskType", required = false) @ApiParam(value = "任务类型,数字:1-土壤，2-农产品(默认查询土壤)") String taskType
	) {
		List<AreaDataVo> pmlcscData = pmlcscApi.getData(adLevel, adRegionCode, year, taskType).getData();

		if (pmlcscData==null){
			return Result.ok(0);
		}

		return Result.ok(pmlcscData);

	}

	//2.环境切面面源

	@GetMapping("/pcnpl")
	@ApiOperation(value = "环境保护处：环境切面面源-氮磷使用量Table")
	public Result<List<BigDataVo>> getpcnplData(@RequestParam(value = "adLevel",required = true) @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city;县级:ad_county") String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码") String adCode
	) {
		List<BigDataVo> pcnplData = pcnplApi.getMapViewDataForBigData(adLevel, adCode).getData();

		if (pcnplData==null){
			return Result.ok(0);
		}

		return Result.ok(pcnplData);


	}

//2.环境切面面源
@GetMapping("/pcnplMap")
@ApiOperation(value = "环境保护处：环境切面面源-氮磷使用量Map")

public Result<MapViewData2> getPcnplMap(
		@RequestParam("index") @ApiParam(name = "index", required = true) String index
		, @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city;县级:ad_county", required = true) String adLevel
		, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码") String adCode
		, @RequestParam(value = "year") @ApiParam(name = "year", value = "年度", required = true) String year) {

	MapViewData2 mapViewData = pcnplApi.getMapViewData(index, adLevel, adCode, year).getData();

	if (mapViewData==null){
		return Result.ok(0);
	}

	return Result.ok(mapViewData);

}






	//3.可再生能源处-沼气
	@PostMapping("/astcapMap")
	@ApiOperation(value = "可再生能源处-沼气-获取地图站点数据")
	public Result<List<SiteForm>> getAstcapdata() {

		List<SiteForm> siteFormList = astcapApi.getSiteListByCondition(null,null,null).getData();
		if (siteFormList==null){
			return Result.ok(0);
		}

		return Result.ok(siteFormList);

	}

	//3.可再生能源处-沼气
	@GetMapping("/astcapTable")
	@ApiOperation(value = "可再生能源处-沼气-获取图表数据")
	public Result<List<AstcapMonitorAlarmTotalHoursVo>> getAlarmTotalByHours(@RequestParam(name = "siteid", required = false) @ApiParam(value = "站点ID") String siteid,
	                                                                         @RequestParam(name = "areaCode", required = false) @ApiParam(value = "区域代码：全国(100000)") String areaCode,
	                                                                         @RequestParam(name = "alarmdate", required = false) @ApiParam(value = "监测时间：xxxx-xx-xx") String alarmdate) {
		List<AstcapMonitorAlarmTotalHoursVo> data = astcapApi.getAlarmTotalByHours(siteid, areaCode, alarmdate).getData();
		if (data==null){
			return Result.ok(0);
		}


		return Result.ok(data);

	}

	//3.可再生能源处-沼气
	@GetMapping("/astcapTips")
	@ApiOperation(value = "可再生能源处-沼气-获取地图Tips详情提示")
	public Result<AstcapMonitorAlarmSiteVo> getAlarmConfigDetail(@RequestParam(name="siteid") @ApiParam(value = "站点ID",required = true) String siteid,
	                                                             @RequestParam(name="alarmdate",required = false) @ApiParam(value = "监测时间：xxxx-xx-xx") String alarmdate){


		AstcapMonitorAlarmSiteVo data = astcapApi.getAlarmConfigDetail(siteid, alarmdate).getData();
		if (data==null){
			return Result.ok(0);
		}


		return Result.ok(data);

	}





	//4.信息与培训处-农业体系接口
	@GetMapping("/astcapMessageTable")
	@ApiOperation("信息与培训处-农业体系接口Table")
	public Result<List<Map<String, Object>>> astcapMessageTable(@RequestParam(name = "regionCode") @ApiParam(value = "区域编码", required = true) String regionCode,
	                                                          @RequestParam(name = "year") @ApiParam(value = "年份", required = true) String year) {
		List<Map<String, Object>> data = astcapApi.getPersonTotal(regionCode, year).getData();
		if (data==null){
			return Result.ok(0);
		}

		return Result.ok(data);
	}


	//农业资源保护环境("查询人员数量地图")
	@ApiOperation("信息与培训处-查询人员数量地图Map")
	@GetMapping(value = "/astcapMessageMap")
	public Result<List<Map<String, Object>>> astcapMessageMap( @RequestParam(name = "regionCode") @ApiParam(value = "区域编码", required = true) String regionCode,
	                                                       @RequestParam(name = "year") @ApiParam(value = "年份", required = true) String year){

		List<Map<String, Object>> data = astcapApi.getPersonMap(regionCode, year).getData();

		if (data==null){
			return Result.ok(0);
		}

		return Result.ok(data);


	}





	//5.资源保护处-外来入侵

	@GetMapping("/agzim")
	@ApiOperation("/资源保护处-国家重点管理外来入侵物种名录子系统/外來入侵")
	public Result<SortedMap<String, Object>> querySpeciesCount(
			@ApiParam(value = "行政区划代码：510000")  @RequestParam(name = "adCode",required = false) String adCode,
			@ApiParam(value = "级别：ad_country-全国，ad_province-省级，ad_city-市级",required = true) @RequestParam(name = "adLevel") String adLevel){


		List<SpeciesTotalVo> voList = agziwjApi.getTotal(adCode, adLevel).getData();

		SortedMap<String, Object> dataG = agzimlApi.querySpeciesCount(adCode).getData();

		if (dataG==null){
			return Result.ok("国家重点管理外来入侵物种名录数据为空");
		}

		if (  voList==null||voList.size()<=0){

			List<Map<String, Object>> subRegionSpeciesCountList =(List<Map<String, Object>>) dataG.get("subRegionSpeciesCountList");

			for (Map<String, Object> stringObjectSortedMap : subRegionSpeciesCountList) {
				stringObjectSortedMap.put("speciesTotalOrdinary", 0 );
			}

		} else {
			List<Map<String, Object>> subRegionSpeciesCountList =(List<Map<String, Object>>) dataG.get("subRegionSpeciesCountList");

			for (Map<String, Object> stringObjectSortedMap : subRegionSpeciesCountList) {
				String regionNameG =  (String) stringObjectSortedMap.get("regionName");

				for (int i = 0; i < voList.size(); i++) {
					stringObjectSortedMap.put("speciesTotalOrdinary", 0);
					String regionName1 = voList.get(i).getRegionName();
					if (regionNameG.equals(regionName1)){
						stringObjectSortedMap.put("speciesTotalOrdinary", voList.get(i).getSpeciesTotal() );
						voList.remove(voList.get(i));
						break;
					}

				}

			}
		}


		return Result.ok(dataG);

	}


	//5.1资源保护处-国家重点保护野生植物名录子系统

	@GetMapping("/agpjzb")
	@ApiOperation("资源保护处-国家重点保护野生植物名录子系统/农业野生植物物种数据")
	public Result<MapViewData> getMapViewData(
			 @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码,510001-510100-510107") String adCode
	) {

		MapViewData mapViewDataZ = agpjzbApi.getMapViewDataZ("1", adLevel, adCode, null).getData();
		MapViewData mapViewDataY = agpjzbApi.getMapViewDataY("1", adLevel, adCode, null).getData();

		if (mapViewDataZ==null){
			return Result.ok(0);
		}
		if (mapViewDataY==null){
			return Result.ok(0);
		}

		// 遍历往 重点野生里面加一个农业物种数

		Map<String, List<AdPointData>> mapZ = mapViewDataZ.getAdPointDataList();
		Map<String, List<AdPointData>> mapY = mapViewDataY.getAdPointDataList();
		if (mapZ==null){
			return Result.ok(0);
		}
		if (mapY==null){
			return  Result.ok(0);
		}
		List<AdPointData> plantList =mapZ.get("plantList");
		if (plantList==null||plantList.size()<=0){
			return Result.ok(0);
		}

		List<AdPointData> speciesList =mapY.get("speciesList");
		if (speciesList==null||speciesList.size()<=0){
			return Result.ok(0);
		}
		for (AdPointData adPointData : plantList) {
			Map<String, Object> indexInfo = adPointData.getIndexInfo();
			String regionNameZ = (String) indexInfo.get("regionName");

			for (int i = 0; i < speciesList.size(); i++) {
				Map<String, Object> indexInfoY = speciesList.get(i).getIndexInfo();
				String regionNameY = (String) indexInfoY.get("regionName");
				indexInfo.put("indexValueY", "0");
				if (regionNameZ.equals(regionNameY)){
					indexInfo.put("indexValueY", speciesList.get(i).getIndexValue());
					speciesList.remove(speciesList.get(i));
					break;
			}

				}
			}

		return Result.ok(mapViewDataZ);

	}



	//6.生态农业处-秸秆
	@GetMapping(value = "/getDucssDataTabel")
	@ApiOperation("生态农业处-秸秆-获取柱状图数据table")
	public Result<List<ColumnLineVo>> getDucssDataTabel( @RequestParam(value = "year",required = true) @ApiParam(name = "year",value = "年份") String year,
	                                                @RequestParam(value = "type",required = true) @ApiParam(name = "type",value = "1:代表近5年，2代表近10年") String type,
	                                                @RequestParam(value = "administrativeLevel",required = true)@ApiParam(name = "administrativeLevel",value = "行政等級：1:代表:县，2：市，3：省，4：国家") String administrativeLevel,
	                                                @RequestParam(value = "areaCode",required = true) @ApiParam(name = "areaCode",value = "区域Code") String areaCode){

		List<ColumnLineVo> lineVos = ducssApi.generateMonitorCode(year, type, administrativeLevel, areaCode).getData();
		if (lineVos==null){
			return Result.ok(0);
		}


		return  Result.ok(lineVos);


	}


	//6.生态农业处-秸秆
	@GetMapping(value = "/getDucssMap")
	@ApiOperation("生态农业处-秸秆-获取地图数据Map")
	public Result<MapViewData> getDucssMap(@RequestParam(value = "year",required = true) @ApiParam(name = "year",value = "年份") String year,
	                                       @RequestParam(value = "administrativeLevel",required = true)@ApiParam(name = "administrativeLevel",value = "行政等級：1:代表:县，2：市，3：省，4：国家") String administrativeLevel,
	                                       @RequestParam(value = "areaCode",required = true) @ApiParam(name = "areaCode",value = "区域Code") String areaCode){

		MapViewData data = ducssApi.getMapViewData(year,administrativeLevel,areaCode).getData();
		if (data==null){
			return Result.ok(0);
		}

		return  Result.ok(data);

	}



		//7.环境保护处--地膜

		@GetMapping("/aeemMap")
		@ApiOperation("环境保护处--地膜地图展示")
		public Result<MapViewData> getDateMapDisplay (
				@RequestParam("index") @ApiParam(name = "index",
						value = "指标:CoatRate-覆膜率,RecycleRate-回收率, ResidualAmount-残留量", required = true) String index,
				@RequestParam("adLevel") @ApiParam(name = "adLevel",
						value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true)
						String adLevel,
				@RequestParam(value = "regionCode",required = false) @ApiParam(name = "regionCode",
						value = "行政代码") String regionCode
				)
		{
			MapViewData data = aeemMapApi.getDateMapDisplay(index, adLevel,regionCode).getData();
			if (data==null){
				return Result.ok(0);
			}

			return Result.ok(data);
		}
		//7.环境保护处--地膜

		@GetMapping("/aeemTable")
		@ApiOperation("环境保护处-地膜统计图展示")
		public Result<List<Map<String, Object>>> getBigDateTableDisplay () {
			List<Map<String, Object>> data = aeemMapApi.getBigDateTableDisplay().getData();
			if (data==null){
				return Result.ok(0);
			}

			return Result.ok(data);
		}

		//四合一接口

//	@PostMapping("/FourInOne")
//	@ApiOperation("地图展示外来和野生系统数据(普通和国家重点)")
//	public  Result<MapViewDataA> FourInOne(
//			 @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel
//			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码,510001-510100-510107") String adCode
//	){
//		 String  replaceRegionName =null;
//		//重点外来的区域，我要将区域代码变成中文。
//		if (StringUtils.isNotBlank(adCode)){
//			String regionName = sysRegionApi.getRegionNamesByCodes(adCode).getData();
//			replaceRegionName = regionName.replace(",", "-");
//		}
//
//
//		SortedMap<String, Object> zWl = agzimlApi.querySpeciesCount(replaceRegionName).getData();
//
//
//		MapViewDataA ys = agpjzbApi.getMapViewDataY("1", adLevel, adCode, null).getData();
//		MapViewDataA zYs = agpjzbApi.getMapViewDataZ("1", adLevel, adCode, null).getData();
//
//		Map<String, List<Map<String, Object>>> dataMap = zYs.getAdStatisticsData().getDataMap();
//		List<Map<String, Object>> list = dataMap.get("speciesList");
//		for (Map<String, Object> stringObjectMap : list) {
//
//		}
//
//
//		return Result.ok(zYs);
//
//
//	}





	}

