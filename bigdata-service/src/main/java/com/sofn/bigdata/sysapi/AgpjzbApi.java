package com.sofn.bigdata.sysapi;

import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 17:44
 * @Version 1.0
 * 资源保护处 WuXiuYun
 *
 */
@FeignClient(
		value = "agpjzb-service",
		configuration = FeignConfiguration.class
)
public interface AgpjzbApi {
	//获取保护野生植物名录子系统

	@GetMapping("/agricultureSpecies/getMapViewData")
	 Result<MapViewData> getMapViewDataY(
			@RequestParam("index") @ApiParam(name = "index", value = "指标:1:物种分布", required = true) String index
			, @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码,510001-510100-510102") String adCode
			, @RequestParam(value = "speciesId", required = false) @ApiParam(name = "speciesId", value = "物种id") String speciesId);



	// "获取国家重点保护野生植物地图数据"

	@GetMapping("/countryPlants/getMapViewData")
	public Result<MapViewData> getMapViewDataZ(
			@RequestParam("index") @ApiParam(name = "index", value = "指标:1:植物分布", required = true) String index
			, @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码,510001-510100-510102") String adCode
			, @RequestParam(value = "plantId", required = false) @ApiParam(name = "plantId", value = "植物id") String plantId);


}
