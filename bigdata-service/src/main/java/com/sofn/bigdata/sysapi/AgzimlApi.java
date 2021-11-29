package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.sysapi.bean.AgzimlKmAlienSpeciesDistributionParam;
import com.sofn.bigdata.sysapi.bean.AgzimlKmAlienSpeciesDistributionSearchResult;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.SortedMap;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 9:17
 * @Version 1.0
 * 外来入侵接口-LiJiang
 */
@FeignClient(
		value = "agziml-service",
		configuration = FeignConfiguration.class
)
public interface AgzimlApi {
//入侵物种分布图查询", "按查询条件统计国家重点管理外来入侵物种"

	@GetMapping(value = "/agzimlKmAlienSpecies/statisticsQuery")
	public Result<AgzimlKmAlienSpeciesDistributionSearchResult> statisticsQuery(@RequestParam(name="searchStr",required = false) @ApiParam(value = "模糊查询(中文名称)") String searchStr,
	                                                                            @RequestParam(name="biotype",required = true) @ApiParam(value = "生物类型(1:植物 2:动物 3:微生物)", allowableValues = "1,2,3") String biotype );


////返回结果包含本级区划的物种数量和下一级区划的物种数量，区划名称为空查询全国物种数量和省级区划的物种数量
//
//	@GetMapping(value = "/agzimlKmAlienSpecies/querySpeciesCount")
//	public Result<SortedMap<String, Object>> querySpeciesCount( @RequestParam(value = "regionName")  String regionName);

	//返回结果包含本级区划的物种数量和下一级区划的物种数量，区划编码为空或者100000，查询全国物种数量和省级区划的物种数量"

	@RequestMapping(value = "/agzimlKmAlienSpecies/querySpeciesCount")
	public Result<SortedMap<String, Object>> querySpeciesCount( @RequestParam(value = "regionCode")String regionCode) ;





}
