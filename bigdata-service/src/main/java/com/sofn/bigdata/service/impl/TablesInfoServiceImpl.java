package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.sofn.bigdata.model.TablesInfo;
import com.sofn.bigdata.mapper.TablesInfoMapper;
import com.sofn.bigdata.service.TablesInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sofn.common.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 袁彬峰
 * @since 2020-06-24
 */
@Service
public class TablesInfoServiceImpl extends ServiceImpl<TablesInfoMapper, TablesInfo> implements TablesInfoService {

    @Autowired
    private TablesInfoMapper tablesInfoMapper;

    @Override
    public PageUtils<TablesInfo> pageQuery(Integer pageNum, Integer pageSize, String tableName, String origin) {
        if (pageSize == null){
            pageSize = 1;
        }

        QueryWrapper<TablesInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tableName)){
            queryWrapper.and(wrapper -> wrapper.like("table_name", tableName).or().like("table_general_name", tableName));
        }
        if (StringUtils.isNotEmpty(origin)){
            queryWrapper.eq("origin", origin);
        }
        queryWrapper.orderByDesc("create_time");
//        PageHelper.startPage(pageNum, pageSize);
//        PageHelper.orderBy("update_time desc");//默认按照时间降序
        IPage<TablesInfo> page = tablesInfoMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        return new PageUtils<TablesInfo>(page.getRecords(), (int)page.getTotal(), pageSize, pageNum);
    }
}
