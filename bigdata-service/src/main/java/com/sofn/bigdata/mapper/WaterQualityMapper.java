package com.sofn.bigdata.mapper;

import com.sofn.bigdata.model.WaterQuality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 袁彬峰
 * @since 2020-05-21
 */
public interface WaterQualityMapper extends BaseMapper<WaterQuality> {

    //通过爬取日期查询数据库存在数据
    List<WaterQuality> selectListByCrawlingDate(String crawlingDate);

    //批量插入水质数据
    void batchInsert(@Param("param") List<WaterQuality> waterQualityList);

}
