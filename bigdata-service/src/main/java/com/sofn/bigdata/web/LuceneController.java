package com.sofn.bigdata.web;

/**
 * @Acthor Tao.Lee @date 2021/3/8 14:13
 * @Description LuceneController
 */
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.Product;
import com.sofn.bigdata.service.LuceneService;
import com.sofn.bigdata.utils.PageQuery;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Api( tags = "Lucene的相关的,全局搜索")
@RequestMapping("/lucene/")
@RestController
public class LuceneController {
    @Autowired
    private LuceneService luceneService;

    @PostMapping("creat")
    @ApiOperation(value = "创建索引")
    public Result<String> create(String skuName) throws Exception{
        List<Product> list = new ArrayList<Product>();
        Product product = new Product();
        product.setId(1);
        product.setBindType(10);
        product.setBrandCode("001");
        product.setBrandName("歌曲");
        product.setCid1(10000L);
        product.setCid1Name("文字");
        product.setClickurl("www.baidu.com");
        product.setComments(100L);
        product.setCommission(10.0);
        product.setCommissionShare(20.0);
        product.setCreateTime(new Date());
        product.setDesc("desc");
        product.setDiscount(10.0);
        product.setDiscountRate(30.0);
        product.setSkuId(635373L);
        product.setPrice(999.01);
        product.setSkuName(skuName);
        list.add(product);
        luceneService.createProductIndex(list);
        System.out.println("===============成功创建");
        return Result.ok("成功创建") ;
    }

    /**
     *
     * @param {
     *         "page":1,
     *         " imit":10,
     *         "skuName":""
     *          }
     * @return  结果列表
     * @throws Exception
     */
    @PostMapping("find")
    @ApiOperation(value = "搜索")
    public PageInfo<Product> find(@RequestBody Product pro) throws Exception{

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pro.getPage());
        pageInfo.setPageSize(pro.getLimit());
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageInfo(pageInfo);
        pageQuery.setParams(pro);
        PageQuery result  = luceneService.searchProduct(pageQuery);

        PageInfo<Product> pageResult = new PageInfo<Product>();
        pageResult.setPageSize(pro.getLimit());
        pageResult.setPageNum(pro.getPage());
        pageResult.setTotal(result.getPageInfo().getTotal());
        pageResult.setList(result.getResults());
        System.out.println("===="+pageResult+"====");
        return pageResult;
    }


    @PostMapping("findbeizhu")
    @ApiOperation(value = "搜索字段备注")
    public Result<String> findbeizhu(
            @RequestParam(name="cid1Name") @ApiParam(value = "标题",required = true) String cid1Name,
            @RequestParam(name="clickurl") @ApiParam(value = "来源",required = true) String clickurl,
            @RequestParam(name="skuName") @ApiParam(value = "文件路径",required = true) String skuName,
            @RequestParam(name="page") @ApiParam(value = "一页显示几个",required = true) Integer page,
            @RequestParam(name="limit") @ApiParam(value = "显示几页",required = true) Integer limit
    ){
        return Result.ok("成功无东西");
    }


}














