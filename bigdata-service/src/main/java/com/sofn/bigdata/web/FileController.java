package com.sofn.bigdata.web;

import com.sofn.bigdata.feign.SysFeign;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.vo.SysFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:袁彬锋
 * @date:2020/4/27 14:28
 * @company:曙光集团绿康元公司
 * @description:文件操作接口，上传/下载等
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private SysFeign sysFeign;

    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam MultipartFile file){
        //to/do 将文件上传文件库
        Result<SysFileVo> uploadFileResult = sysFeign.uploadFile(file);
        if (uploadFileResult.getCode() != 200 || uploadFileResult.getData() == null){
            throw new RuntimeException("文件上传失败");
        }
        return Result.ok();

    }
}

