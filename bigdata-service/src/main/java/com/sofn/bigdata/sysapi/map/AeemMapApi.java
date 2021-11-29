package com.sofn.bigdata.sysapi.map;


import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 地膜系统地图接口
 */
@FeignClient(
        value = "aeem-service",
        configuration = FeignConfiguration.class
)
public interface AeemMapApi {


    /**
     * 获取地图展示数据
     * @param index
     * @param adLevel
     * @param provinceId
     * @param belongYear
     * @return
     */
    @GetMapping("/dataDisplay/getDateMapDisplay")
    Result<MapViewData> getDateMapDisplay(
            @RequestParam("index") @ApiParam(name = "index", value = "index", required = true) String index,
            @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel,
            @ApiParam(name = "provinceId",value = "省份id")@RequestParam(value = "provinceId",required = false)String provinceId,
            @ApiParam(name = "belongYear",value = "所属年份")@RequestParam(value = "belongYear",required = false)String belongYear);


    @GetMapping("/bigDataDisplay/getBigDateMapDisplay")
         Result<MapViewData>  getDateMapDisplay(
            @RequestParam("index") @ApiParam(name = "index",
                    value = "指标:CoatRate-覆膜率,RecycleRate-回收率, ResidualAmount-残留量", required = true) String index,
            @RequestParam("adLevel") @ApiParam(name = "adLevel",
                    value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true)
                    String adLevel,
            @RequestParam(value = "regionCode",required = false) @ApiParam(name = "regionCode",
            value = "行政代码") String regionCode
    );


   // @ApiOperation(value="获取图表展示数据")

    @GetMapping("/bigDataDisplay/getBigDateTableDisplay")
    public Result<List<Map<String, Object>>>  getBigDateTableDisplay();



}


