package com.sofn.bigdata.service;

import com.sofn.bigdata.sysapi.bean.MapData;
import com.sofn.bigdata.sysapi.bean.MapSysConditions;
import com.sofn.bigdata.vo.AstiiStatisticQueryVo;

import java.util.List;

public interface MapService{

    List<MapData> getMapData(List<AstiiStatisticQueryVo> vos);

    List<MapSysConditions> getIndex();
}
