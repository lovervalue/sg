package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.DataItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author:袁彬锋
 * @date:2020/4/29 11:42
 * @company:曙光集团绿康元公司
 * @description:
 */
public interface DataItemMapper extends BaseMapper<DataItem> {
    //批量插入数据项
    void batchInsert(@Param("params") List<DataItem> dataItemList);
}
