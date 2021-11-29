package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.enums.DataShareStatusEnum;
import com.sofn.bigdata.mapper.DataShareMapper;
import com.sofn.bigdata.model.DataShare;
import com.sofn.bigdata.model.SysInterfaceManage;
import com.sofn.bigdata.schedule.DataShareSchedule;
import com.sofn.bigdata.service.DataShareService;
import com.sofn.bigdata.sysapi.SysInterfaceManageApi;
import com.sofn.bigdata.utils.CacheUtils;
import com.sofn.bigdata.utils.HttpUtils;
import com.sofn.bigdata.vo.DataShareVo;
import com.sofn.bigdata.vo.SysInterfaceManageVo;
import com.sofn.common.exception.SofnException;
import com.sofn.common.model.Result;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataShareServiceImpl extends BaseService<DataShareMapper, DataShare> implements DataShareService {

    @Autowired
    private DataShareMapper dataShareDao;
    @Autowired
    private SysInterfaceManageApi sysInterfaceManageApi;
    @Autowired
    private DataShareSchedule dataShareSchedule;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDataShare(DataShareVo dataShareVo) {

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(dataShareVo));

        String id = dataShareVo.getId();
        if(null == id || "".equals(id)){
            id = IdUtil.getUUId();
        }

        params.put("id",id);
        params.put("delFlag","N");
        getJMap(params,"operatime",new Date());
        params.put("createTime",new Date());
        /*params.put("createUserId", UserUtil.getLoginUserId());*/
        getJMap(params,"interStatus", DataShareStatusEnum.NOTSTARTED.getCode());

        String flag = isExist(dataShareVo.getInterId());
        if(null == flag){
            dataShareDao.saveDataShare(params);
        }
    }

    private String isExist(String interId) {
        List<DataShare> res = dataShareDao.getDataShareListByInterid(interId);
        if(null != res && res.size() > 0 && null != res.get(0)){
            return res.get(0).getId();
        }else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataShare(String id) {
        try {
            dataShareDao.deleteDataShare(id);
        }catch (Exception e){
            throw new SofnException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataShare(DataShareVo dataShareVo) {

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(dataShareVo));
        params.put("id",dataShareVo.getId());
        params.put("delFlag","N");
        params.put("updateTime",new Date());
        /*params.put("updateUserId",UserUtil.getLoginUserId());*/
        getJMap(params,"operatime",new Date());
        getJMap(params,"interStatus", DataShareStatusEnum.NOTSTARTED.getCode());

        String flag = isExist(dataShareVo.getInterId());
        if(dataShareVo.getId().equals(flag)){
            dataShareDao.updateDataShare(params);
        }

    }

    @Override
    public PageUtils getDataShareList(String interName, String interStatus, Integer pageNo, Integer pageSize) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }

        List<DataShare> res = dataShareDao.getDataShareList(interName,interStatus);
        PageInfo<DataShare> resPage = new PageInfo<>(res);
        List<DataShare> list = resPage.getList();

        List<DataShareVo> detailVoList = resListByRes(list);
        PageInfo<DataShareVo> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(resPage,pageInfo);
        pageInfo.setList(detailVoList);
        return PageUtils.getPageUtils(pageInfo);
    }

    @Override
    public void startDataShare(String id) {
        try {
            DataShare dataShare = dataShareDao.selectById(id);
            dataShare.setInterStatus(DataShareStatusEnum.STARTED.getCode());
            dataShareDao.insert(dataShare);

            String interReg = dataShare.getInterReg();
            String interId = dataShare.getInterId();

            Result<SysInterfaceManageVo> interfaceManageResult = sysInterfaceManageApi.getOne(interId);
            SysInterfaceManageVo manageVo = interfaceManageResult.getData();
            CacheUtils.cacheAszdqqInter(manageVo);

            dataShareSchedule.startDataShare(interReg);
        }catch (Exception e){
            throw new SofnException("启动失败："+ e.getMessage());
        }
    }

    @Override
    public void notStartDataShare(String id) {
        try {
            DataShare dataShare = dataShareDao.selectById(id);
            dataShare.setInterStatus(DataShareStatusEnum.NOTSTARTED.getCode());
            dataShareDao.insert(dataShare);
        }catch (Exception e){
            throw new SofnException("禁止失败："+ e.getMessage());
        }
    }

    private List<DataShareVo> resListByRes(List<DataShare> res) {
        List<DataShareVo> detailVoList = new ArrayList<>();
        res.forEach(dataShare -> {
            DataShareVo detailVo = new DataShareVo();
            BeanUtils.copyProperties(dataShare,detailVo);
            detailVoList.add(detailVo);
        });
        return detailVoList;
    }

    private void getJMap(JSONObject praMap,String key,Object val){
        boolean flay = praMap.containsKey(key);
        if(flay){
            praMap.replace(key,val);
        }else {
            praMap.put(key,val);
        }
    }

}
