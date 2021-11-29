package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.Template;

/**
 * @author:袁彬锋
 * @date:2020/4/29 11:42
 * @company:曙光集团绿康元公司
 * @description:
 */
public interface TemplateMapper extends BaseMapper<Template> {
    void updateByTemplate(Template template);
}
