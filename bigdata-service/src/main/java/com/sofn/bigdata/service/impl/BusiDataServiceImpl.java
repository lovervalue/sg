package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sofn.bigdata.mapper.BusiDataMapper;
import com.sofn.bigdata.model.BusiData;
import com.sofn.bigdata.service.BusiDataService;
import com.sofn.bigdata.vo.BusiDataVo;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BusiDataServiceImpl extends BaseService<BusiDataMapper, BusiData> implements BusiDataService {

    @Autowired
    private BusiDataMapper busiDataDao;

    @Override
    public void saveBusiData(BusiDataVo busiDataVo) {

        BusiData busiData = new BusiData();
        BeanUtils.copyProperties(busiDataVo,busiData);
        busiData.setId(IdUtil.getUUId());
        busiData.setDelFlag("N");
        busiData.setCreateTime(new Date());
        busiData.setCreateUserId(UserUtil.getLoginUserId());

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(busiData));

        busiDataDao.saveBusiData(params);
    }
}
