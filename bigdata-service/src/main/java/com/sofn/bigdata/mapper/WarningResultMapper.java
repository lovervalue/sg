package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.WarningResult;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/25 11:02
 * @Description
 */
public interface WarningResultMapper extends BaseMapper<WarningResult> {

    List selectsum(@Param(value = "dataName")String dataName,
                   @Param(value = "startdates") Date startdates,
                   @Param(value = "stopdatestf")Date stopdatestf);

}
