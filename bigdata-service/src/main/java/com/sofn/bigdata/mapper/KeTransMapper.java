package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.KTrans;
import org.apache.ibatis.annotations.Param;

/**
 * @Acthor Tao.Lee @date 2021/2/3 10:25
 * @Description
 */
public interface KeTransMapper extends BaseMapper<KTrans> {

    void updatestatus(@Param(value = "transId") Integer transId);

    void updatestatustrue(@Param(value = "transId") Integer transId);
}
