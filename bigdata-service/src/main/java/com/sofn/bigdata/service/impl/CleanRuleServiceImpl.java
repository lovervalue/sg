package com.sofn.bigdata.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.kafka.MyKafkaProducer;
import com.sofn.bigdata.kafka.ProducerManager;
import com.sofn.bigdata.mapper.CleanRuleMapper;
import com.sofn.bigdata.model.CleanRule;
import com.sofn.bigdata.service.CleanRuleService;
import com.sofn.bigdata.service.MarshInfoService;
import com.sofn.bigdata.vo.CleanRuleVo;
import com.sofn.common.exception.SofnException;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "cleanRuleService")
public class CleanRuleServiceImpl extends BaseService<CleanRuleMapper, CleanRule> implements CleanRuleService {
    @Autowired
    private CleanRuleMapper cleanRuleMapper;

    @Override
    public List<CleanRule> getByTableId(String tableId) {
        List<CleanRule> list = cleanRuleMapper.getByTableId(tableId);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CleanRuleVo cleanRuleVo) {
        CleanRule cleanRule = CleanRuleVo.getCleanRule(cleanRuleVo);
        if(!(cleanRule.getFieldType().equals("String")
                ||cleanRule.getFieldType().equals("Number")
                ||cleanRule.getFieldType().equals("Date"))){
            throw new SofnException("当前仅支持定义String、Number、Date类型的字段规则");
        }
        cleanRule.setCreateTime(new Date());
        cleanRule.setCreateUserId(UserUtil.getLoginUserId());
        cleanRuleMapper.insert(cleanRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        cleanRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CleanRuleVo cleanRuleVo) {
        CleanRule cleanRule = CleanRuleVo.getCleanRule(cleanRuleVo);
        if(!(cleanRule.getFieldType().equals("String")
                ||cleanRule.getFieldType().equals("Number")
                ||cleanRule.getFieldType().equals("Date"))){
            throw new SofnException("当前仅支持定义String、Number、Date类型的字段规则");
        }
        cleanRule.setUpdateTime(new Date());
        cleanRule.setUpdateUserId(UserUtil.getLoginUserId());
        cleanRuleMapper.updateById(cleanRule);
    }

}
