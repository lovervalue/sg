package com.sofn.bigdata.web;


import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.model.TableStructure;
import com.sofn.bigdata.model.TablesInfo;
import com.sofn.bigdata.service.TablesInfoService;
import com.sofn.bigdata.utils.UserUtils;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 袁彬峰
 * @since 2020-06-24
 */
@RestController
@RequestMapping("/tablesInfo")
@Api(tags = "数据表管理", value = "数据表管理")
public class TablesInfoController {

    @Autowired
    private TablesInfoService tablesInfoService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/insert")
    @ApiOperation(value = "添加表数据信息")
    public Result insert(@RequestBody TablesInfo tableInfo){
        tableInfo.setId(IdUtil.getUUId());
        String creator = UserUtils.getUserName();
        tableInfo.setCreator(creator);
        tableInfo.setCreateTime(new Date());
        tableInfo.setUpdateTime(new Date());
        tablesInfoService.save(tableInfo);
        return Result.ok("插入成功");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除表数据信息")
    public Result  delete(@PathVariable("id")String id){
        tablesInfoService.removeById(id);
        return Result.ok("删除成功");
    }

    @GetMapping("/findById/{id}")
    @ApiOperation(value = "查询详情接口")
    public Result<TablesInfo> findById(@PathVariable("id") String id){
        TablesInfo tablesInfo = tablesInfoService.getById(id);
        String sql = "select * from information_schema.columns where table_schema = 'bigdata' and table_name = ?";
        String tableName = tablesInfo.getTableName();
        List<TableStructure> tableStructureList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TableStructure.class), tableName);
        tablesInfo.setTableStructureList(tableStructureList);
        return Result.ok(tablesInfo);
    }

    @GetMapping("/pageQuery")
    @ApiOperation(value = "查询接口")
    public Result pageQuery(@ApiParam(name = "pageNum", value = "当前页码", required = true)@RequestParam(value = "pageNum", required = true)Integer pageNum,
                            @ApiParam(name = "pageSize", value = "每页显示内容数量", required = false)@RequestParam(value = "pageSize", required = false)Integer pageSize,
                            @ApiParam(name = "tableName", value = "表名称", required = false)@RequestParam(value = "tableName", required = false)String tableName,
                            @ApiParam(name = "origin", value = "数据来源", required = false)@RequestParam(value = "origin", required = false)String origin){
        PageUtils<TablesInfo> pageUtils = tablesInfoService.pageQuery(pageNum, pageSize, tableName, origin);
        return Result.ok(pageUtils);
    }
}