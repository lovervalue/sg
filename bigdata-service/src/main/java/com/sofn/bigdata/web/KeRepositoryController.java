package com.sofn.bigdata.web;

import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.model.KRepository;
import com.sofn.bigdata.model.RepositoryTree;
import com.sofn.bigdata.service.KeRepositoryService;
import com.sofn.bigdata.utils.JsonUtils;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/2 16:20
 * @Description
 */
@Slf4j
@Api(tags = "Kettle数据库资源库接口")
@RestController
@RequestMapping("/repository/")
public class KeRepositoryController {

    @Autowired
    private KeRepositoryService keRepositoryService;

    @PostMapping("database/ckeck")
    @ApiOperation(value = "检查连接")
    public Result<String> check(KRepository kRepository){
        try {
            keRepositoryService.check(kRepository);
            System.out.println("===连接成功===");
            return Result.ok("连接成功");
        }catch (KettleException e){
            e.printStackTrace();
            System.out.println("===连接失败===");
            return Result.ok("连接失败");
        }
    }
    @PostMapping("database/insert")
    @ApiOperation(value = "新增连接")
    public Result<String> insert(KRepository kRepository){
        try {
            keRepositoryService.check(kRepository);
            System.out.println("===插入成功===");
            keRepositoryService.insert(kRepository);
            return Result.ok("插入成功");
        }catch (KettleException e){
            e.printStackTrace();
            System.out.println("===插入失败===");
            return Result.ok("插入失败");
        }
    }
    @PostMapping("database/delete")
    @ApiOperation(value = "删除连接")
    public Result<String> delete(Integer repositoryId){
        try{
            keRepositoryService.delete(repositoryId);
            return Result.ok("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("===删除失败===");
            return Result.ok("删除失败");
        }
    }
    @GetMapping("database/getList")
    @ApiOperation(value = "获取列表")
    public Result<String> getList(){
        List list=keRepositoryService.getList();
        return Result.ok(list);
    }

    /**
     * 获取资源库列表
     * @param repositoryId
     * @return
     */
    @GetMapping("database/getJobTree")
    @ApiOperation(value = "获取资源库内作业列表")
    public String getJobTree(Integer repositoryId){
        try {
            List<RepositoryTree> repositoryTreeList = keRepositoryService.getTreeList(repositoryId);
            List<RepositoryTree> newRepositoryTreeList = new ArrayList<RepositoryTree>();
            for(RepositoryTree repositoryTree : repositoryTreeList){
                if ("0".equals(repositoryTree.getParent())){
                    repositoryTree.setParent("#");
                }
                if (repositoryTree.getId().indexOf("@") > 0){
                    repositoryTree.setIcon("none");
                }
                if (Constant.TYPE_TRANS.equals(repositoryTree.getType())){
                    Map<String,String> stateMap = new HashMap<String, String>();
                    stateMap.put("disabled", "false");
                    repositoryTree.setState(stateMap);
                }
                newRepositoryTreeList.add(repositoryTree);
            }
            return JsonUtils.objectToJson(newRepositoryTreeList);
        } catch (KettleException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("database/getTransTree")
    @ApiOperation(value = "获取资源库内转换列表")
    public String getTransTree(Integer repositoryId){
        try {
            List<RepositoryTree> repositoryTreeList = keRepositoryService.getTreeList(repositoryId);
            List<RepositoryTree> newRepositoryTreeList = new ArrayList<RepositoryTree>();
            for(RepositoryTree repositoryTree : repositoryTreeList){
                if ("0".equals(repositoryTree.getParent())){
                    repositoryTree.setParent("#");
                }
                if (repositoryTree.getId().indexOf("@") > 0){
                    repositoryTree.setIcon("none");
                }
                if (Constant.TYPE_JOB.equals(repositoryTree.getType())){
                    Map<String,String> stateMap = new HashMap<String, String>();
                    stateMap.put("disabled", "false");
                    repositoryTree.setState(stateMap);
                }
                newRepositoryTreeList.add(repositoryTree);
            }
            return JsonUtils.objectToJson(newRepositoryTreeList);
        } catch (KettleException e) {
            e.printStackTrace();
        }
        return null;
    }

}
