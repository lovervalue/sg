package com.sofn.bigdata.sysapi.map;


import com.sofn.bigdata.vo.MapViewDataA;
import com.sofn.bigdata.vo.ShowDataVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.map.MapConditions;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 氮磷系统地图接口
 */
@FeignClient(
        value = "pcnpl-service",
        configuration = FeignConfiguration.class
)
public interface PcnplMapApi {

    /**
     * 指标返回
     * @return
     */
    @GetMapping(value ="/map/returnIndex",consumes = MediaType.APPLICATION_JSON_VALUE )
    Result<List<MapConditions>> returnIndex();

    /**
     * 数据返回
     * @return
     */
    @GetMapping("/map/showData")
    Result<List<ShowDataVo>> showData(@RequestParam(required = true) @ApiParam(value = "年份", required = true) String years,
                                      @RequestParam(required = true) @ApiParam(value = "区域code", required = true) String areaCode);


    /**
     * 典型地块点位分布数据返回
     * @param adCode
     * @param years
     * @return
     */
    @GetMapping("/map/getMapViewData")
    Result<MapViewDataA> getDwfbData(@RequestParam(value = "adCode") @ApiParam(name = "adCode", value = "行政区域代码,看全国时就传'100000'", required = true) String adCode,
                                    @RequestParam(value = "years") @ApiParam(name = "years", value = "年度", required = true) String years);
}
