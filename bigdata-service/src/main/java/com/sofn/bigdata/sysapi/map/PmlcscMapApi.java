package com.sofn.bigdata.sysapi.map;


import com.sofn.bigdata.vo.AreaCentre;
import com.sofn.bigdata.vo.SummaryQueryVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 国控点土壤信息采集子系统地图接口
 */
@FeignClient(
        value = "pmlcsc-service",
        configuration = FeignConfiguration.class
)
public interface PmlcscMapApi {


    /**
     * 全国地图数据统计-展示所有省份进展情况
     * @param queryVo
     * @return
     */
    @PostMapping(value = "/seoulHas/getStatisticalData",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<MapViewData> getStatisticalData(@RequestBody @ApiParam(value = "查询参数", required = true) SummaryQueryVo queryVo);

    /**
     * 获取区域中心列表
     * @return
     */
    @GetMapping("/baseData/areaCentre/getList")
    Result<List<AreaCentre>> getList();
}
