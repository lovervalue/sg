package com.sofn.bigdata.sysapi.map;


import com.sofn.bigdata.vo.AstiiStatisticIndexVo;
import com.sofn.bigdata.vo.AstiiStatisticQueryVo;
import com.sofn.bigdata.vo.SiteForm;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 沼气系统+农业体系地图接口
 */
@FeignClient(
        value = "astcap-service",
        configuration = FeignConfiguration.class
)
public interface AstcapMapApi {

    /**
     * 查询所有的工段参数结构树
     * @return
     */
    @GetMapping("/astcapMonitorAlarm/getGdConfigTree")
    Result<List<HashMap<String,Object>>> getGdConfigTree();


    /**
     * 获取站点信息集合
     * @param companyId
     * @param prjType
     * @param status
     * @return
     */
    @PostMapping(value = "/site/getSiteListByCondition")
    Result<List<SiteForm>> getSiteListByCondition(
            @ApiParam(name = "companyId",value = "公司ID")
            @RequestParam(value = "companyId", required = false) String companyId,
            @ApiParam(name = "prjType",value = "项目类型")
            @RequestParam(value = "prjType", required = false) String prjType,
            @ApiParam(name = "status",value = "状态:1运行, 2暂停")
            @RequestParam(value = "status", required = false) String status);

    /**
     * 查询全国农村可再生能源统计上报与审核统计分析的类型
     * @return
     */
    @GetMapping("/mainStatistic/getAllIndexList")
    Result<List<AstiiStatisticIndexVo>> getAllIndexListMain();

    /**
     * 查询全国农村可再生能源统计上报与审核统计分析的数据表
     * @param vo
     * @return
     */
    @PostMapping(value = "/mainStatistic/getDataTable",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<Map<String, Object>>> getDataTableMain(@Validated @ApiParam(value = "统计查询对象",required = true) @RequestBody AstiiStatisticQueryVo vo);

    /**
     * 查询全国农业资源环境信息统计分析的所有指标
     * @return
     */
    @GetMapping("/astii/astiiNareiStatistic/getAllIndexList")
    Result<List<AstiiStatisticIndexVo>> getAllIndexListAstii();

    /**
     * 查询全国农业资源环境信息统计分析的数据表
     * @param vo
     * @return
     */
    @PostMapping(value = "/astii/astiiNareiStatistic/getDataTable",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<Map<String, Object>>> getDataTableAstii(@Validated @ApiParam(value = "统计查询对象",required = true) @RequestBody AstiiStatisticQueryVo vo);

    /**
     * 查询农业资源环境保护体系建设统计分析的类型
     * @return
     */
    @GetMapping("/repaiStatistic/getAllIndexList")
    Result<List<AstiiStatisticIndexVo>> getAllIndexListRepa();


    /**
     * 查询农业资源环境保护体系建设统计分析的数据表
     * @param vo
     * @return
     */
    @PostMapping(value = "/repaiStatistic/getDataTable",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<Map<String, Object>>> getDataTableRepa(@Validated @ApiParam(value = "统计查询对象",required = true) @RequestBody AstiiStatisticQueryVo vo);

    /**
     * 查询农村可再生能源体系建设统计分析的所有指标
     * @return
     */
    @GetMapping("/astii/rrempStatistic/getAllIndexList")
    Result<List<AstiiStatisticIndexVo>> getAllIndexListRrem();


    /**
     * 查询农村可再生能源体系建设统计分析的数据表
     * @param vo
     * @return
     */
    @PostMapping(value = "/astii/rrempStatistic/getDataTable",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<Map<String, Object>>> getDataTableRrem(@Validated @ApiParam(value = "统计查询对象",required = true) @RequestBody AstiiStatisticQueryVo vo);
}
