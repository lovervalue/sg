package com.sofn.bigdata.web;

import com.sofn.bigdata.service.TableRelationService;
import com.sofn.bigdata.vo.*;
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

import java.util.HashMap;
import java.util.List;

/**
 * 数据源管理-表关系设置
 */
@Slf4j
@Api(tags = "数据管理-数据源-表关系设置")
@RestController
@RequestMapping("/tableRelation")
public class TableRelationController extends BaseController {

    @Autowired
    private TableRelationService tableRelationService;

    @SofnLog("保存表关系")
    @ApiOperation(value = "保存表关系",notes = "权限码：（bigdata:TableRelation:saveTableRelation）")
//    @RequiresPermissions("bigdata:TableRelation:saveTableRelation")
    @PostMapping("/saveTableRelation")
    public Result<String> saveTableRelation(@Validated @ApiParam(name = "保存表关系信息", value = "传入json格式", required = true) @RequestBody TableRelationVo tableRelationVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            tableRelationService.saveTableRelation(tableRelationVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("修改表关系")
    @ApiOperation(value = "修改表关系",notes = "权限码：（bigdata:TableRelation:updateTableRelation）")
//    @RequiresPermissions("bigdata:TableRelation:updateTableRelation")
    @PostMapping("/updateTableRelation")
    public Result<String> updateTableRelation(@Validated @ApiParam(name = "修改表关系信息", value = "传入json格式", required = true) @RequestBody TableRelationVo tableRelationVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            tableRelationService.updateTableRelation(tableRelationVo);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }


    @SofnLog("查询")
    @ApiOperation(value = "查询",notes = "权限码：（bigdata:TableRelation:getTableRelationList）")
//    @RequiresPermissions("bigdata:TableRelation:getTableRelationList")
    @GetMapping("/getTableRelationList")
    public Result<PageUtils<TableRelationVo>> getTableRelationList(@RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                                              @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize){
        PageUtils<TableRelationVo> dbSourcePageUtils = tableRelationService.getTableRelationList(pageNo,pageSize);
        return  Result.ok(dbSourcePageUtils);
    }

    @SofnLog("根据数据库ID查询所有的表")
    @ApiOperation(value = "根据数据库ID查询所有的表",notes = "权限码：（bigdata:TableRelation:getAllTablesBySourceId）")
    @GetMapping("/getAllTablesBySourceId")
    public Result<List<TableVo>> getAllTablesBySourceId(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid){
        List<TableVo> resList = tableRelationService.getAllTablesBySourceId(dbid);
        return  Result.ok(resList);
    }

    @SofnLog("根据数据库ID、表名称查询所有的表字段")
    @ApiOperation(value = "根据数据库ID、表名称查询所有的表字段",notes = "权限码：（bigdata:TableRelation:getAllColumnsByIdAndTbName）")
    @GetMapping("/getAllColumnsByIdAndTbName")
    public Result<List<ColumnVo>> getAllColumnsByIdAndTbName(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid,
                                                             @ApiParam(required = true, value = "数据表名称") @RequestParam(value = "tbname") String tbname){
        List<ColumnVo> resList = tableRelationService.getAllColumnsByIdAndTbName(dbid,tbname);
        return  Result.ok(resList);
    }

    @SofnLog("查询连接方式")
    @ApiOperation(value = "查询连接方式",notes = "权限码：（bigdata:TableRelation:getUnionType）")
//    @RequiresPermissions("bigdata:TableRelation:getUnionType")
    @GetMapping("/getUnionType")
    public Result<List<DataEnumVo>> getUnionType(){
        List<DataEnumVo> list = tableRelationService.getUnionType();
        return  Result.ok(list);
    }

    @SofnLog("删除")
    @ApiOperation(value = "删除",notes = "权限码：（bigdata:TableRelation:delTableRelation）")
//    @RequiresPermissions("bigdata:TableRelation:delTableRelation")
    @PostMapping("/delTableRelation")
    public Result<String> delTableRelation(@ApiParam(required = true, value = "表关系设置ID") @RequestParam(value = "tbid") String tbid){
        try {
            tableRelationService.delTableRelation(tbid);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

}
