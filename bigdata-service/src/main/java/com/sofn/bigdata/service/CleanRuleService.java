package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.CleanRule;
import com.sofn.bigdata.vo.CleanRuleVo;

import java.util.List;

public interface CleanRuleService extends IService<CleanRule> {
    void save(CleanRuleVo cleanRuleVo);
    void delete(String id);
    void update(CleanRuleVo cleanRuleVo);
    List<CleanRule> getByTableId(String tableId);
}
