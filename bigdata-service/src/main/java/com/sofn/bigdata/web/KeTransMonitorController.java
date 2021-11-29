package com.sofn.bigdata.web;

import com.sofn.bigdata.model.KTransMonitor;
import com.sofn.bigdata.model.KTransRecord;
import com.sofn.bigdata.service.KeTransMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/19 11:14
 * @Description
 */
@Slf4j
@Api(tags = "Kettle转换监控接口")
@RestController
@RequestMapping("/trans/monitor/")
public class KeTransMonitorController {

    @Autowired
    private KeTransMonitorService keTransMonitorService;

    @GetMapping("alltranscount")
    @ApiOperation(value = "总转换任务数")
    public Integer alltranscount(){
        return keTransMonitorService.alltranscount();
    }

    @GetMapping("alltranssuccesscount")
    @ApiOperation(value = "总成功数")
    public Integer alltranssuccesscount(){
        return keTransMonitorService.alltranssuccesscount();
    }

    @GetMapping("alltransfailcount")
    @ApiOperation(value = "总失败数")
    public Integer alltransfailcount(){
        return keTransMonitorService.alltransfailcount();
    }

    @GetMapping("alltrans")
    @ApiOperation(value = "三个数字的集合")
    public Map<String,Integer> alltrans(){
        // map实例化
        Map<String, Integer> maps = new HashMap<>();
        maps.put("总转换任务数",keTransMonitorService.alltranscount());
        maps.put("总成功数",keTransMonitorService.alltranssuccesscount());
        maps.put("总失败数",keTransMonitorService.alltransfailcount());
        return maps;
    }

    @GetMapping("seetrans")
    @ApiOperation(value = "查看详情")
    public List<KTransRecord> seetrans(Integer recordTrans){
        return keTransMonitorService.seetrans(recordTrans);
    }

    @GetMapping("getTransList")
    @ApiOperation(value = "总转换列表")
    public List<KTransMonitor> getTransList(){
        return keTransMonitorService.getTransList();
    }

    @GetMapping("getTransCount")
    @ApiOperation(value = "获取转换的总次数")
    public Integer getTransCount(Integer recordTrans){
        return keTransMonitorService.seetransconut(recordTrans);
    }

}





