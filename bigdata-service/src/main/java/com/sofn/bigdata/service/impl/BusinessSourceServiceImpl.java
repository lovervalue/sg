package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.mapper.BusinessSourceMapper;
import com.sofn.bigdata.model.BusinessSource;
import com.sofn.bigdata.service.BusinessSourceService;
import com.sofn.bigdata.vo.BusinessSourceVo;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BusinessSourceServiceImpl extends BaseService<BusinessSourceMapper, BusinessSource> implements BusinessSourceService {

    @Autowired
    private BusinessSourceMapper businessSourceDao;

    @Override
    public void saveBusinessSource(BusinessSourceVo businessSourceVo) {

        BusinessSource businessSource = new BusinessSource();
        BeanUtils.copyProperties(businessSourceVo,businessSource);
        businessSource.setId(IdUtil.getUUId());
        businessSource.setOperator(UserUtil.getLoginUserId());
        businessSource.setOperatDate(new Date());
        businessSource.setCreateUserId(UserUtil.getLoginUserId());
        businessSource.setCreateTime(new Date());
        businessSource.setDelFlag("N");

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(businessSource));

        businessSourceDao.saveBusinessSource(params);
    }

    @Override
    public void delBusinessSource(String dbid) {
        businessSourceDao.deleteBusinessSource(dbid);
    }

    @Override
    public PageUtils<BusinessSourceVo> getBusinessSourceList(String businame, Integer pageNo, Integer pageSize) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<BusinessSource> res = businessSourceDao.getBusinessSourceList(businame);

        PageInfo<BusinessSource> pageInfo = new PageInfo<>(res);
        List<BusinessSource> resList = pageInfo.getList();

        List<BusinessSourceVo> resInfo = getResByResList(resList);
        PageInfo<BusinessSourceVo> sourceVoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,sourceVoPageInfo);
        sourceVoPageInfo.setList(resInfo);

        return PageUtils.getPageUtils(sourceVoPageInfo);
    }

    @Override
    public List<BusinessSourceVo> getAllBusinessSourceList() {
        List<BusinessSource> res = businessSourceDao.getBusinessSourceList("");
        return getResByResList(res);
    }

    @NotNull
    private List<BusinessSourceVo> getResByResList(List<BusinessSource> resList) {
        List<BusinessSourceVo> voList = new ArrayList<>();
        for(BusinessSource source : resList){
            BusinessSourceVo vo = new BusinessSourceVo();
            BeanUtils.copyProperties(source,vo);
            voList.add(vo);
        }
        return voList;
    }
}
