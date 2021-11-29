package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.SourceTbInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SourceTbInfoMapper extends BaseMapper<SourceTbInfo> {

    void saveTables(@Param("tbids") List<String> tbids);

    List<SourceTbInfo> getTablesBySourceId(@Param("dbid") String dbid);
}
