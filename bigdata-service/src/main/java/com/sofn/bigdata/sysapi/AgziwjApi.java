package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.sysapi.bean.SpeciesTotalVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/9/8 10:10
 * @Version 1.0
 * 外来入侵-普通
 */
@FeignClient(
		value = "agziwj-service",
		configuration = FeignConfiguration.class
)
public interface AgziwjApi {
	// "获取外来入侵物种个数"

	@GetMapping("/newMap/getSum")
	public Result<List<SpeciesTotalVo>> getTotal(
			@ApiParam(value = "行政区划代码：510000")  @RequestParam(name = "adCode",required = false) String adCode,
	       @ApiParam(value = "级别：ad_country-全国，ad_province-省级，ad_city-市级") @RequestParam(name = "adLevel",required = true) String adLevel);


}
