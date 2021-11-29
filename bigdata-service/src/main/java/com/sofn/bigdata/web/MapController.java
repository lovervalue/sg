package com.sofn.bigdata.web;

import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.Proposal;
import com.sofn.bigdata.service.MapService;
import com.sofn.bigdata.sysapi.bean.MapData;
import com.sofn.bigdata.sysapi.bean.MapSysConditions;
import com.sofn.bigdata.vo.AstiiStatisticQueryVo;
import com.sofn.common.map.MapConditions;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Api(tags = "地图相关接口-多个系统")
@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @PostMapping("/getData")
    @ApiOperation(value = "根据一个或多个条件获取对应系统地图数据")
    public Result<List<MapData>> getMapData(@Validated @ApiParam(value = "统计查询对象",required = true) @RequestBody List<AstiiStatisticQueryVo> vos){

        List<MapData> mapDataList = mapService.getMapData(vos);
        return Result.ok(mapDataList);
    }

    @GetMapping("/getIndex")
    @ApiOperation(value = "获取各系统地图指标")
    public Result<List<MapSysConditions>> getIndex(){
        List<MapSysConditions> mapSysConditions = mapService.getIndex();
        return Result.ok(mapSysConditions);
    }
}
