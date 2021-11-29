package com.sofn.bigdata.service;

import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.vo.SysFileManageForm;
import com.sofn.bigdata.vo.SysFileVo;
import com.sofn.common.config.FeignConfiguration;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author gouliangcheng
 * @Description 上传附件调用支撑平台接口
 * @Date 16:49 2020/8/4
 * @Param
 * @return
 **/
@FeignClient(value = "sys-service", configuration = FeignConfiguration.class)
public interface KnowledgeFileService {
    /**
     * @Author gouliangcheng
     * @Description 上传文件
     * @Date 14:40 2020/8/10
     * @Param [file]
     * @return com.sofn.bigdata.model.Result<com.sofn.bigdata.vo.SysFileVo>
     **/
    @PostMapping(value = "/fileManage/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<SysFileVo> uploadFile(@ApiParam(value = "文件") @RequestPart(value = "file") MultipartFile file);

    /**
     * @Author gouliangcheng
     * @Description 激活文件
     * @Date 14:40 2020/8/10
     * @Param [sysFileManageForm]
     * @return void
     **/
    @PostMapping(value = "/fileManage/activationFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    void activationFile(@RequestBody @ApiParam(name = "添加文件对象", value = "传入json格式", required = true) SysFileManageForm sysFileManageForm);
}
