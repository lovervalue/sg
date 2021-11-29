package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.sysapi.bean.AreaDataVo;
import com.sofn.bigdata.sysapi.bean.SummaryQueryVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/8/20 9:22
 * @Version 1.0
 * 农产品产地环境评价中心：XieZhi
 */
@FeignClient(
		value = "pmlcsc-service",
		configuration = FeignConfiguration.class
)
public interface PmlcscApi {
	@PostMapping(value = "/seoulHas/getStatisticalData", consumes = MediaType.APPLICATION_JSON_VALUE)
	Result<MapViewData> getStatisticalData(@RequestBody @ApiParam(value = "查询参数", required = true) SummaryQueryVo queryVo);


	@PostMapping("/seoulHas/getData")
	public Result<List<AreaDataVo>> getData(@RequestParam(value = "adLevel") @ApiParam(value = "地图当前行政级别(ad_country:国家级,ad_province:省级,ad_city:市级)", required = true) String adLevel,
	                                        @RequestParam(value = "adRegionCode") @ApiParam(value = "行政区域代码", required = true) String adRegionCode,
	                                        @RequestParam(value = "year", required = false) @ApiParam(value = "年份，默认为当前年份") String year,
	                                        @RequestParam(value = "taskType", required = false) @ApiParam(value = "任务类型,数字:1-土壤，2-农产品(默认查询土壤)") String taskType
	);



}
