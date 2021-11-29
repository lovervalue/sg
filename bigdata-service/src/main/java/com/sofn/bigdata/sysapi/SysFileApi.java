package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.vo.SysFileManageForm;
import com.sofn.bigdata.vo.SysFileManageVo;
import com.sofn.bigdata.vo.SysFileVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@FeignClient(
        value = "sys-service",
        configuration = FeignConfiguration.class
)
public interface SysFileApi {

    @GetMapping("/fileManage/replaceFile")
    Result replaceFile(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "remark") String remark,
            @RequestParam(value = "newFileId") String newFileId
    );

    /**
     * 激活文件
     */
    @PostMapping(value =  "/fileManage/activationFile", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<SysFileVo>> activationFile(@RequestBody SysFileManageForm sysFileManageForm);

    /**
     * 根据ID删除文件
     * @param id 文件id
     * @return Result<String>
     */
    @PostMapping("/fileManage/deleteFile")
    @ResponseBody
    Result<String> delFile(@RequestParam("id") String id);
    
    /**
     * 通过ID获取文件
     * @param id
     * @return
     */
    @GetMapping("/fileManage/getOne/{id}")
    Result<SysFileManageVo> getOne(@PathVariable("id") String id);

    /**
     * 文件下载
     * @param id
     * @param response
     */
    @GetMapping(value = "/fileManage/downloadFile/{id}", produces = "application/octet-stream")
    void downloadFile(@RequestParam("id") String id, HttpServletResponse response);

    /**
     * 查询文件列表
     * @param pageNo
     * @param pageSize
     * @param fileName
     * @param systemId
     * @param operatorName
     * @return
     */
    @GetMapping("/fileManage/list")
    PageUtils<SysFileManageVo> getSysFileByPage(@RequestParam(value = "pageNo") Integer pageNo,
                                                @RequestParam(value = "pageSize") Integer pageSize,
                                                @RequestParam(value = "fileName", required = false) String fileName,
                                                @RequestParam(value = "systemId", required = false) String systemId,
                                                @RequestParam(value = "operatorName", required = false) String operatorName);

    /**
     * 重命名文件
     * @param id
     * @param newFileName
     * @return
     */
    @GetMapping(value = "/fileManage/updateFileName")
    Result<String> updateFileName(@RequestParam("id") String id,
                                  @RequestParam("newFileName") String newFileName);

    /**
     * 上传文件
     * @param file 文件
     * @return Result<SysFileVo>
     */
    @PostMapping(value = "/fileManage/uploadFile", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<SysFileVo> uploadFile(@ApiParam(value = "文件") @RequestPart(value = "file") MultipartFile file);

}
