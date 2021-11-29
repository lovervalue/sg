package com.sofn.bigdata.web;

import com.sofn.bigdata.service.BusinessSourceService;
import com.sofn.bigdata.vo.BusinessSourceVo;
import com.sofn.bigdata.vo.DbSourceVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据管理-业务数据
 */
@Slf4j
@Api(tags = "数据管理-业务数据")
@RestController
@RequestMapping("/businessSource")
public class BusinessSourceController extends BaseController {

    @Autowired
    private BusinessSourceService businessSourceService;

    @SofnLog("保存数据源信息")
    @ApiOperation(value = "保存数据源信息",notes = "权限码：（bigdata:BusinessSource:saveBusinessSource）")
//    @RequiresPermissions("bigdata:BusinessSource:saveBusinessSource")
    @PostMapping("/saveBusinessSource")
    public Result<String> saveBusinessSource(@Validated @ApiParam(name = "保存数据源信息", value = "传入json格式", required = true) @RequestBody BusinessSourceVo businessSourceVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            businessSourceService.saveBusinessSource(businessSourceVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("查询")
    @ApiOperation(value = "查询",notes = "权限码：（bigdata:BusinessSource:getBusinessSourceList）")
//    @RequiresPermissions("bigdata:BusinessSource:getBusinessSourceList")
    @GetMapping("/getBusinessSourceList")
    public Result<PageUtils<BusinessSourceVo>> getBusinessSourceList(@ApiParam(value = "业务数据名称") @RequestParam(required = false, value = "businame") String businame,
                                                              @RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                                              @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize){
        PageUtils<BusinessSourceVo> dbSourcePageUtils = businessSourceService.getBusinessSourceList(businame,pageNo,pageSize);
        return  Result.ok(dbSourcePageUtils);
    }

    @SofnLog("查询所有业务数据列表")
    @ApiOperation(value = "查询所有业务数据列表",notes = "权限码：（bigdata:BusinessSource:getAllBusinessSourceList）")
//    @RequiresPermissions("bigdata:BusinessSource:getAllBusinessSourceList")
    @GetMapping("/getAllBusinessSourceList")
    public Result<List<BusinessSourceVo>> getAllBusinessSourceList(){
        List<BusinessSourceVo> dbSourcePageUtils = businessSourceService.getAllBusinessSourceList();
        return  Result.ok(dbSourcePageUtils);
    }

    @SofnLog("删除")
    @ApiOperation(value = "删除",notes = "权限码：（bigdata:BusinessSource:delBusinessSource）")
//    @RequiresPermissions("bigdata:BusinessSource:delBusinessSource")
    @PostMapping("/delBusinessSource")
    public Result<String> delBusinessSource(@ApiParam(required = true, value = "数据源信息ID") @RequestParam(value = "dbid") String dbid){
        try {
            businessSourceService.delBusinessSource(dbid);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

}
