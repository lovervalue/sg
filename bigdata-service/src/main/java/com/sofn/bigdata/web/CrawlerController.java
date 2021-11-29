package com.sofn.bigdata.web;

import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.service.CrawlerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:袁彬锋
 * @date:2020/5/21 14:19
 * @company:曙光集团绿康元公司
 * @description:
 */
@RestController
@RequestMapping("/crawler")
@Api(value = "爬虫模板启动关闭统一调用", description = "爬虫模板启动关闭统一调用")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping("/{id}")
    @ApiOperation(value = "传入模板的id", tags = "爬虫模板启动关闭统一调用,传入模板id")
    public Result crawling(@PathVariable("id") Long id, @ApiParam(name = "isOpen", value = "开启或关闭,传入值为Y or N", required = true) @RequestParam(value = "isOpen") String isOpen) {
        crawlerService.crawling(id, isOpen);
        return Result.ok();
    }
}
