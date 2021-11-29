package com.sofn.bigdata.feign;

import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.vo.SysFileVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:袁彬锋
 * @date:2020/4/27 14:28
 * @company:曙光集团绿康元公司
 * @description:支撑平台的feign接口
 */

@FeignClient("sys-service")
public interface SysFeign {

    //文件上传接口
    @PostMapping(value = "/fileManage/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<SysFileVo> uploadFile(@ApiParam(value = "文件") @RequestPart(value = "file") MultipartFile file);
}
