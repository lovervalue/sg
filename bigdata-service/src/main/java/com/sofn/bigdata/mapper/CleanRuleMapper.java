package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.CleanRule;
import com.sofn.bigdata.model.Template;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CleanRuleMapper extends BaseMapper<CleanRule> {
    List<CleanRule> getByTableId(@Param(value = "tableId") String tableId);
    int deleteByPrimaryKey(String id);
}
