package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.BigDataSource;
import com.sofn.bigdata.vo.*;
import com.sofn.common.utils.PageUtils;

import java.util.HashMap;
import java.util.List;

public interface BigDataSourceService extends IService<BigDataSource> {

    void saveBigDataSource(BigDataSourceVo bigDataSourceVo);

    boolean testConBigDataSource(BigDataSourceVo bigDataSourceVo) throws Exception;

    void executeSql(String sqlValue, String dbid);

    PageUtils<DbSourceVo> getBigDataSourceList(String dbname, String dbalias, Integer pageNo, Integer pageSize);

    void delBigDataSource(String dbid);

    List<DataEnumVo> getCharSet();

    List<DataEnumVo> getDriverList();

    List<CheckSqlVo> getCheckSqlList();

    List<DataEnumVo> getIsolationLevelList();

    HashMap<String, Object> getDbSourceDetail(String dbid, String level);

    List<SourceTableInfoVo> getTableListBySourceId(String dbid);

    void saveTables(String tbids);

    List<SourceTableInfoVo> getTablesBySourceId(String dbid);

    List<DbSourceVo> getAllSourceList();

    HashMap<String, Object> getColunmDetailBySourceId(String dbid, String tbname, String datatype);

    void renameAliasDb(String dbid, String alias);
}
