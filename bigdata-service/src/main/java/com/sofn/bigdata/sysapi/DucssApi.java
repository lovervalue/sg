package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.sysapi.bean.ColumnLineVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 11:33
 * @Version 1.0
 * 生态农业处-秸秆
 */

@FeignClient(
		value = "ducss-service",
		configuration = FeignConfiguration.class
)
public interface DucssApi {

	//获取柱线图数据

	@GetMapping(value = "/dataShow/generateMonitorCode/{year}/{type}/{administrativeLevel}/{areaCode}")
	public Result<List<ColumnLineVo> > generateMonitorCode(@PathVariable("year") String year, @PathVariable("type") String type, @PathVariable("administrativeLevel") String administrativeLevel, @PathVariable("areaCode") String areaCode);

	//获取地图数据接口

	@GetMapping(value = "/dataShow/getMapViewData/{year}/{administrativeLevel}/{areaCode}")
	public Result<MapViewData> getMapViewData(@PathVariable("year") String year, @PathVariable("administrativeLevel") String administrativeLevel, @PathVariable("areaCode") String areaCode);


}
