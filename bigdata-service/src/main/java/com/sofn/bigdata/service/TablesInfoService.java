package com.sofn.bigdata.service;

import com.sofn.bigdata.model.TablesInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.common.utils.PageUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 袁彬峰
 * @since 2020-06-24
 */
public interface TablesInfoService extends IService<TablesInfo> {

    //条件组合分页查询
    PageUtils<TablesInfo> pageQuery(Integer pageNum, Integer pageSize, String tableName, String origin);
}
