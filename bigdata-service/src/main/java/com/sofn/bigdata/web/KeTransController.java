package com.sofn.bigdata.web;

import com.baomidou.mybatisplus.extension.api.R;
import com.sofn.bigdata.model.KQuartz;
import com.sofn.bigdata.model.KTrans;
import com.sofn.bigdata.service.KeTransService;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/3 10:18
 * @Description
 */
@Slf4j
@Api(tags = "Kettle资源库转换作业接口")
@RestController
@RequestMapping("/trans/")
public class KeTransController {

    @Autowired
    private KeTransService transService;

    @GetMapping("getList")
    @ApiOperation(value = "获取转换列表")
    public List<KTrans> getList(){
        return transService.getList();
    }

    @PostMapping("insert")
    @ApiOperation(value = "新建转换")
    public Result<String> insert(KTrans kTrans,String customerQuarz){
        if(transService.check(kTrans.getTransRepositoryId(),kTrans.getTransPath())){
            try{
                transService.inset(kTrans,customerQuarz);
                return Result.ok("添加成功");
            }catch (SQLException e){
                e.printStackTrace();
                return Result.ok("添加失败");
            }
        }else{
            return Result.ok("该作业已经添加过了");
        }
    }

    @PostMapping("getTrans")
    @ApiOperation(value = "根据转换id换取信息")
    public KTrans getTrans (Integer transId){
        return transService.getTrans(transId);
    }

    @PostMapping("start")
    @ApiOperation(value = "启动转换")
    public Result<String> start(Integer transId){
        transService.start(transId);
        return Result.ok("启动成功");
    }

    @PostMapping("stop")
    @ApiOperation(value = "停止转换")
    public Result<String> stop(Integer transId){
        transService.stop(transId);
        return Result.ok("关闭成功");
    }
    @PostMapping("detele")
    @ApiOperation(value = "删除转换")
    public Result<String> detele(Integer transId){
        transService.delete(transId);
        return Result.ok("删除成功");
    }
    @PostMapping("getquartzlist")
    @ApiOperation(value = "获取定时策略列表")
    public List<KQuartz> getquartzlist(){
        return transService.getquartzlist();
    }

}
