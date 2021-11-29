package com.sofn.bigdata.service;

import com.sofn.bigdata.model.Product;

import com.sofn.bigdata.utils.PageQuery;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/3/8 13:45
 * @Description
 */
public interface LuceneService {
    /**
     * 增加索引
     * @param list
     * @throws IOException
     */
    public void createProductIndex(List<Product> list) throws IOException;

    /**
     * 查询
     * @param pageQuery
     * @return
     * @throws Exception
     * @throws ParseException
     */
    public PageQuery searchProduct(PageQuery pageQuery) throws Exception, ParseException;

    /**
     *删除
     * @param id
     * @throws IOException
     */
    public void deleteProductIndexById(String id) throws IOException;
}
