package com.sofn.bigdata.sysapi.map;


import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.log.SofnLog;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * 农作物秸秆资源台账子系统地图接口
 */
@FeignClient(
        value = "ducss-service",
        configuration = FeignConfiguration.class
)
public interface DucssMapApi {


    /**
     * 图形分析
     * @param index
     * @param adLevel
     * @param adCode
     * @param year
     * @return
     */
    @GetMapping("/graphicAnalysis/getMapViewData")
    Result<MapViewData> getMapViewData(@RequestParam(value = "index") @ApiParam(name="index", value="指标,左边固定值中的指标值,比如说产生量传OUTPUT",
            allowableValues = "OUTPUT,UTILIZATION,DIRECTRETURN,CALLIN,FIVEMATERIALS,OEE,COMPREHENSIVEUTILIZATION,CUCI,IUCI", required = true) String index,
                                              @RequestParam(value = "adLevel") @ApiParam(name="adLevel", value="行政级别,传值ad_county：国家 ad_province:省级；ad_city:市级；",allowableValues = "ad_county,ad_province,ad_city", required = true) String adLevel,
                                              @RequestParam(value = "adCode") @ApiParam(name="adCode", value="行政区域代码或行政区域名称；adLevel为国家时传100000；adLevel为省市级时候传相应省市的6位行政编码", required = true) String adCode,
                                              @RequestParam(value = "year",required = false) @ApiParam(name="year", value="年度查询条件,比如：2020;不传默认当前年") String year);
}
