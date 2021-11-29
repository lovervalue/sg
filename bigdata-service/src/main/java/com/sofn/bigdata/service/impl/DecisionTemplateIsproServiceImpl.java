package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.mapper.TemplateIsproMapper;
import com.sofn.bigdata.model.TemplateIspro;
import com.sofn.bigdata.service.DecisionTemplateIsproService;
import com.sofn.bigdata.vo.TemplateIsproVo;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DecisionTemplateIsproServiceImpl extends BaseService<TemplateIsproMapper, TemplateIspro> implements DecisionTemplateIsproService {

    @Autowired
    private TemplateIsproMapper templateIsproDao;

    @Override
    public void saveTemplateIspro(TemplateIsproVo templateIsproVo) {
        TemplateIspro ispro = new TemplateIspro();
        BeanUtils.copyProperties(templateIsproVo,ispro);
        ispro.setId(IdUtil.getUUId());
        ispro.setCreateTime(new Date());
        ispro.setCreateUserId(UserUtil.getLoginUserId());
        ispro.setDelFlag("N");
        templateIsproDao.insert(ispro);
    }

    @Override
    public void delTemplateIspro(String id) {
        templateIsproDao.delTemplateIspro(id);
    }

    @Override
    public void updateTemplateIspro(TemplateIsproVo templateIsproVo) {
        TemplateIspro ispro = new TemplateIspro();
        BeanUtils.copyProperties(templateIsproVo,ispro);
        ispro.setUpdateTime(new Date());
        ispro.setUpdateUserId(UserUtil.getLoginUserId());
        ispro.setDelFlag("N");

        templateIsproDao.updateTemplateIspro(ispro);
    }

    @Override
    public PageUtils<TemplateIsproVo> getTemplateIsproList(String fieldname, String fieldtype, String ispro, Integer pageNo, Integer pageSize) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<TemplateIspro> isproList = templateIsproDao.getTemplateIsproList(fieldname,fieldtype,ispro);
        PageInfo<TemplateIspro> pageInfo = new PageInfo<>(isproList);
        List<TemplateIspro> resList = pageInfo.getList();
        List<TemplateIsproVo> isproVoList = new ArrayList<>();
        for(TemplateIspro templateIspro : resList){
            TemplateIsproVo isproVo = new TemplateIsproVo();
            BeanUtils.copyProperties(templateIspro,isproVo);
            isproVoList.add(isproVo);
        }

        PageInfo<TemplateIsproVo> templateIsproVoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,templateIsproVoPageInfo);
        templateIsproVoPageInfo.setList(isproVoList);

        return PageUtils.getPageUtils(templateIsproVoPageInfo);
    }

    @Override
    public List<TemplateIsproVo> getAllTemplateIsproList(String ispro) {
        List<TemplateIspro> isproList = templateIsproDao.getTemplateIsproList("","",ispro);
        List<TemplateIsproVo> isproVoList = new ArrayList<>();
        for(TemplateIspro templateIspro : isproList){
            TemplateIsproVo isproVo = new TemplateIsproVo();
            BeanUtils.copyProperties(templateIspro,isproVo);
            isproVoList.add(isproVo);
        }
        return isproVoList;
    }

    @Override
    public void startTemplateIspro(String id) {
        templateIsproDao.startTemplateIspro(id);
    }

    @Override
    public void stopTemplateIspro(String id) {
        templateIsproDao.stopTemplateIspro(id);
    }
}
