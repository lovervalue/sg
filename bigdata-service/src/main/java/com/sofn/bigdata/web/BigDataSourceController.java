package com.sofn.bigdata.web;

import com.sofn.bigdata.service.BigDataSourceService;
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
 * 数据管理-数据源
 */
@Slf4j
@Api(tags = "数据管理-数据源")
@RestController
@RequestMapping("/bigDataSource")
public class BigDataSourceController extends BaseController {

    @Autowired
    private BigDataSourceService bigDataSourceService;

    @SofnLog("保存数据源信息")
    @ApiOperation(value = "保存数据源信息",notes = "权限码：（bigdata:bigDataSource:saveBigDataSource）")
//    @RequiresPermissions("bigdata:bigDataSource:saveBigDataSource")
    @PostMapping("/saveBigDataSource")
    public Result<String> saveBigDataSource(@Validated @ApiParam(name = "保存数据源信息", value = "传入json格式", required = true) @RequestBody BigDataSourceVo bigDataSourceVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            bigDataSourceService.saveBigDataSource(bigDataSourceVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("数据库设置中-重命名")
    @ApiOperation(value = "数据库设置中-重命名",notes = "权限码：（bigdata:bigDataSource:renameAliasDb）")
    @PostMapping("/renameAliasDb")
    public Result<String> renameAliasDb(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid,
                                        @ApiParam(required = true, value = "数据源别名") @RequestParam(value = "alias") String alias){
        try {
            bigDataSourceService.renameAliasDb(dbid,alias);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }


    @SofnLog("测试是否连接")
    @ApiOperation(value = "测试是否连接",notes = "权限码：（bigdata:bigDataSource:testConBigDataSource）")
//    @RequiresPermissions("bigdata:bigDataSource:testConBigDataSource")
    @PostMapping("/testConBigDataSource")
    public Result<String> testConBigDataSource(@Validated @ApiParam(name = "测试是否连接", value = "传入json格式", required = true) @RequestBody BigDataSourceVo bigDataSourceVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            boolean flag = bigDataSourceService.testConBigDataSource(bigDataSourceVo);
            if(flag){
                return Result.ok("测试已连通");
            }else {
                return Result.error("测试连通失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("测试连通失败：" + e.getMessage());
        }
    }

    @SofnLog("执行")
    @ApiOperation(value = "执行",notes = "权限码：（bigdata:bigDataSource:executeSql）")
//    @RequiresPermissions("bigdata:bigDataSource:executeSql")
    @PostMapping("/executeSql")
    public Result<String> executeSql(@ApiParam(required = true, value = "执行sql语句") @RequestParam(value = "sqlValue") String sqlValue,
                                     @ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid){
        try {
            bigDataSourceService.executeSql(sqlValue,dbid);
            return  Result.ok("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("执行失败：" + e.getMessage());
        }
    }

    @SofnLog("查询")
    @ApiOperation(value = "查询",notes = "权限码：（bigdata:bigDataSource:getBigDataSourceList）")
//    @RequiresPermissions("bigdata:bigDataSource:getBigDataSourceList")
    @GetMapping("/getBigDataSourceList")
    public Result<PageUtils<DbSourceVo>> getBigDataSourceList(@ApiParam(value = "数据源名称") @RequestParam(required = false, value = "dbname") String dbname,
                                          @ApiParam(value = "数据源别名") @RequestParam(required = false, value = "dbalias") String dbalias,
                                          @RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                          @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize){
        PageUtils<DbSourceVo> dbSourcePageUtils = bigDataSourceService.getBigDataSourceList(dbname,dbalias,pageNo,pageSize);
        return  Result.ok(dbSourcePageUtils);
    }

    @SofnLog("查询所有数据源列表")
    @ApiOperation(value = "查询所有数据源列表",notes = "权限码：（bigdata:bigDataSource:getAllSourceList）")
//    @RequiresPermissions("bigdata:bigDataSource:getAllSourceList")
    @GetMapping("/getAllSourceList")
    public Result<List<DbSourceVo>> getAllSourceList(){
        List<DbSourceVo> sourceList = bigDataSourceService.getAllSourceList();
        return  Result.ok(sourceList);
    }

    @SofnLog("查询详情")
    @ApiOperation(value = "查询详情",notes = "权限码：（bigdata:bigDataSource:getDbSourceDetail）")
//    @RequiresPermissions("bigdata:bigDataSource:getDbSourceDetail")
    @GetMapping("/getDbSourceDetail")
    public Result<HashMap<String,Object>> getDbSourceDetail(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid,
                                                            @ApiParam(value = "深度：1为表级别，2为字段级别，默认为1表级别") @RequestParam(value = "level",required = false) String level){
        HashMap<String,Object> resList = bigDataSourceService.getDbSourceDetail(dbid,level);
        return  Result.ok(resList);
    }

    @SofnLog("查询表属性或数据预览")
    @ApiOperation(value = "查询表属性或数据预览",notes = "权限码：（bigdata:bigDataSource:getColunmDetailBySourceId）")
    @GetMapping("/getColunmDetailBySourceId")
    public Result<HashMap<String,Object>> getColunmDetailBySourceId(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid,
                                                                    @ApiParam(required = true, value = "表名称(英文名)") @RequestParam(value = "tbname") String tbname,
                                                                    @ApiParam(required = true, value = "表属性或数据预览标识：1为表属性，2为数据预览") @RequestParam(value = "datatype") String datatype){
        HashMap<String,Object> resList = bigDataSourceService.getColunmDetailBySourceId(dbid,tbname,datatype);
        return  Result.ok(resList);
    }

    @SofnLog("保存数据表列表")
    @ApiOperation(value = "保存数据表列表",notes = "权限码：（bigdata:bigDataSource:saveTables）")
//    @RequiresPermissions("bigdata:bigDataSource:saveTables")
    @PostMapping("/saveTables")
    public Result<String> saveTables(@ApiParam(required = true, value = "数据表ID:'xx,xx,xx'") @RequestParam(value = "tbids") String tbids){
        try {
            bigDataSourceService.saveTables(tbids);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("根据数据源ID查询数据表(数据库设置中)")
    @ApiOperation(value = "根据数据源ID查询数据表(数据库设置中)",notes = "权限码：（bigdata:bigDataSource:getTablesBySourceId）")
//    @RequiresPermissions("bigdata:bigDataSource:getTablesBySourceId")
    @GetMapping("/getTablesBySourceId")
    public Result<List<SourceTableInfoVo>> getTablesBySourceId(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid){
        List<SourceTableInfoVo> resList = bigDataSourceService.getTablesBySourceId(dbid);
        return  Result.ok(resList);
    }

    @Deprecated
    @SofnLog("根据数据源ID查询数据表(从原始数据库中查)")
    @ApiOperation(value = "根据数据源ID查询数据表(从原始数据库中查)",notes = "权限码：（bigdata:bigDataSource:getTableListBySourceId）")
//    @RequiresPermissions("bigdata:bigDataSource:getTableListBySourceId")
    @GetMapping("/getTableListBySourceId")
    public Result<List<SourceTableInfoVo>> getTableListBySourceId(@ApiParam(required = true, value = "数据源ID") @RequestParam(value = "dbid") String dbid){
        List<SourceTableInfoVo> resList = bigDataSourceService.getTableListBySourceId(dbid);
        return  Result.ok(resList);
    }

    @SofnLog("查询字符集列表")
    @ApiOperation(value = "查询字符集列表",notes = "权限码：（bigdata:bigDataSource:getCharSet）")
//    @RequiresPermissions("bigdata:bigDataSource:getCharSet")
    @GetMapping("/getCharSet")
    public Result<List<DataEnumVo>> getCharSet(){
        List<DataEnumVo> resList = bigDataSourceService.getCharSet();
        return  Result.ok(resList);
    }

    @SofnLog("查询驱动程序类型")
    @ApiOperation(value = "查询驱动程序类型",notes = "权限码：（bigdata:bigDataSource:getDriverList）")
//    @RequiresPermissions("bigdata:bigDataSource:getDriverList")
    @GetMapping("/getDriverList")
    public Result<List<DataEnumVo>> getDriverList(){
        List<DataEnumVo> resList = bigDataSourceService.getDriverList();
        return  Result.ok(resList);
    }

    @SofnLog("查询校验语句列表")
    @ApiOperation(value = "查询校验语句列表",notes = "权限码：（bigdata:bigDataSource:getCheckSqlList）")
//    @RequiresPermissions("bigdata:bigDataSource:getCheckSqlList")
    @GetMapping("/getCheckSqlList")
    public Result<List<CheckSqlVo>> getCheckSqlList(){
        List<CheckSqlVo> resList = bigDataSourceService.getCheckSqlList();
        return  Result.ok(resList);
    }

    @SofnLog("查询隔离级别列表")
    @ApiOperation(value = "查询隔离级别列表",notes = "权限码：（bigdata:bigDataSource:getIsolationLevelList）")
//    @RequiresPermissions("bigdata:bigDataSource:getIsolationLevelList")
    @GetMapping("/getIsolationLevelList")
    public Result<List<DataEnumVo>> getIsolationLevelList(){
        List<DataEnumVo> resList = bigDataSourceService.getIsolationLevelList();
        return  Result.ok(resList);
    }

    @SofnLog("删除")
    @ApiOperation(value = "删除",notes = "权限码：（bigdata:bigDataSource:delBigDataSource）")
//    @RequiresPermissions("bigdata:bigDataSource:delBigDataSource")
    @PostMapping("/delBigDataSource")
    public Result<String> delBigDataSource(@ApiParam(required = true, value = "数据源信息ID") @RequestParam(value = "dbid") String dbid){
        try {
            bigDataSourceService.delBigDataSource(dbid);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

}
