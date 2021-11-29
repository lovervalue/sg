package com.sofn.bigdata.service;

import com.sofn.bigdata.model.PageResult;
import com.sofn.bigdata.model.Template;

public interface TemplateService {
    //保存模板
    void insert(Template template);

    //根据id删除
    void deleteById(Long id);

    //通过id获取
    Template findById(Long id);

    //根据id更新
    void updateById(Template template);

    //多条件分页查询
    PageResult pageQuery(Long pageNum, Long pageSize, String name, String status);
}
