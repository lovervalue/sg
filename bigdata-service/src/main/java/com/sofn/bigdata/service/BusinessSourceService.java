package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.BusinessSource;
import com.sofn.bigdata.vo.BusinessSourceVo;
import com.sofn.common.utils.PageUtils;

import java.util.List;

public interface BusinessSourceService extends IService<BusinessSource> {

    void saveBusinessSource(BusinessSourceVo businessSourceVo);

    void delBusinessSource(String dbid);

    PageUtils<BusinessSourceVo> getBusinessSourceList(String businame, Integer pageNo, Integer pageSize);

    List<BusinessSourceVo> getAllBusinessSourceList();
}
