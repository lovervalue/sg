package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.BusiData;
import com.sofn.bigdata.vo.BusiDataVo;

public interface BusiDataService extends IService<BusiData> {
    void saveBusiData(BusiDataVo busiDataVo);
}
