package com.sofn.bigdata.utils;

/**
 * @Acthor Tao.Lee @date 2021/3/8 13:49
 * @Description 这个是lucene的分页
 */
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.Product;
import lombok.Data;
import java.util.List;
import java.util.Map;


@Data
public class PageQuery {
    private PageInfo pageInfo;
    /**
     * 排序字段
     */
    private Sort sort;
    /**
     * 查询参数类
     */
    private Product params;
    /**
     * 返回结果集
     */
    private List<Product> results;
    /**
     * 不在T类中的参数
     */
    private Map<String, String> queryParam;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Product getParams() {
        return params;
    }

    public void setParams(Product params) {
        this.params = params;
    }

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }

    public Map<String, String> getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(Map<String, String> queryParam) {
        this.queryParam = queryParam;
    }
}