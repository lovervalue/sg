package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.KTransMonitor;
import com.sofn.bigdata.model.KTransRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/4 10:45
 * @Description
 */
public interface KeTransMonitorMapper extends BaseMapper<KTransMonitor> {

    Integer selectCountsuccess();

    Integer selectCountfail();

    List<KTransRecord> seetrans(@Param(value = "recordTrans") Integer recordTrans);

    Integer seetransconut(@Param(value = "recordTrans") Integer recordTrans);
}
