package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.TableRelation;

import java.util.List;
import java.util.Map;

public interface TableRelationMapper extends BaseMapper<TableRelation> {
    
    void saveTableRelation(Map<String, Object> praMap);

    List<TableRelation> getTableRelationList();

    void updateTableRelation(Map<String, Object> praMap);

    void deleteTableRelation(String id);

}
