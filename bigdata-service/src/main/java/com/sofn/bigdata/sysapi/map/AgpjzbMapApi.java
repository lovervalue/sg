package com.sofn.bigdata.sysapi.map;


import com.sofn.bigdata.vo.DropDownVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.log.SofnLog;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 农业野生植物保护名录管理子系统地图接口
 */
@FeignClient(
        value = "agpjzb-service",
        configuration = FeignConfiguration.class
)
public interface AgpjzbMapApi {

    /**
     * 获取国家重点保护野生植物名录管理
     * @return
     */
    @GetMapping("/countryPlants/listForSelect")
    Result<List<DropDownVo>> listForSelect();

    /**
     * 获取国家重点保护野生植物地图数据
     * @param index
     * @param adLevel
     * @param adCode
     * @param plantId
     * @return
     */
    @GetMapping("/countryPlants/getMapViewData")
    Result<MapViewData> getMapViewData(
            @RequestParam("index") @ApiParam(name = "index", value = "指标:1:植物分布", required = true) String index
            , @RequestParam("adLevel") @ApiParam(name = "adLevel", value = "行政级别:国家级:ad_country;省级:ad_province;市级:ad_city", required = true) String adLevel
            , @RequestParam(value = "adCode", required = false) @ApiParam(name = "adCode", value = "行政区域代码,510001-510100-510102") String adCode
            , @RequestParam(value = "plantId", required = false) @ApiParam(name = "plantId", value = "植物id") String plantId);

}
