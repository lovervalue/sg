package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.model.MapViewData2;
import com.sofn.bigdata.sysapi.bean.BigDataVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/8/20 9:41
 * @Version 1.0
 * 环境保护处-面源-ZhaoPeng
 */
@FeignClient(
		value = "pcnpl-service",
		configuration = FeignConfiguration.class
)
public interface PcnplApi {

	@GetMapping("/map/getMapViewDataForBigData")
	public Result<List<BigDataVo>> getMapViewDataForBigData(
			@RequestParam(value = "adLevel", required = true) @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city;县级:ad_county") String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码") String adCode
	);


	//  氮、磷使用量情况   "数据返回"
	@GetMapping("/map/getMapViewData")
	public Result<MapViewData2> getMapViewData(
			@RequestParam("index") @ApiParam(name = "index", required = true) String index
			, @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city;县级:ad_county", required = true) String adLevel
			, @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码") String adCode
			, @RequestParam(value = "year") @ApiParam(name = "year", value = "年度", required = true) String year) ;



}
