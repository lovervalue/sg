package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.vo.SysInterfaceManageVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "sys-service",
        configuration = FeignConfiguration.class
)
public interface SysInterfaceManageApi {

    @PostMapping("/getOne")
    Result<SysInterfaceManageVo> getOne(@ApiParam(name = "id", value = "接口管理参数ID", required = true) @RequestParam("id") String id);

}
