package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.BigDataSource;
import com.sofn.bigdata.model.BigDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BigDataSourceMapper extends BaseMapper<BigDataSource> {

    void saveBigDataSource(Map<String, Object> praMap);

    List<BigDataSource> getBigDataSourceList(@Param("name") String name, @Param("alias") String alias);

    void updateBigDataSource(Map<String, Object> praMap);

    void deleteBigDataSource(String id);

    void renameAliasDb(@Param("dbid") String dbid,@Param("alias") String alias);
}
