package com.sofn.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sofn.common.excel.ExcelExportUtil;
import com.sofn.common.excel.test.testimport.ImportBaseDataBean;
import com.sofn.common.exception.SofnException;
import com.sofn.common.model.BaseData;
import com.sofn.common.utils.*;
import com.sofn.sys.mapper.SysBaseDataMapper;
import com.sofn.sys.mapper.SysBaseDataTypeMapper;
import com.sofn.sys.mapper.SysSubSystemBaseDataOwnMapper;
import com.sofn.sys.model.SysBaseData;
import com.sofn.sys.model.SysBaseDataType;
import com.sofn.sys.service.SysBaseDataService;
import com.sofn.sys.vo.Excel.ImportBaseDataVo;
import com.sofn.sys.vo.SysBaseDataAndTypeVo;
import com.sofn.sys.vo.SysBaseDataVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zqc
 */
@Service(value = "sysBaseDataService")
@Slf4j
public class SysBaseDataServiceImpl extends ServiceImpl<SysBaseDataMapper, SysBaseData> implements SysBaseDataService {
    @Autowired
    private SysBaseDataMapper sysBaseDataDao;
    @Autowired
    private SysBaseDataTypeMapper sysBaseDataTypeDao;
    @Autowired
    private SysSubSystemBaseDataOwnMapper sysSubSystemBaseDataOwnMapper;


    @Override
    public PageUtils<HashMap<String, Object>> getBaseDataInfo(String typeKeyName, Integer pageNo, Integer pageSize) {

        if (pageNo != null && pageSize != null) {
            PageHelper.offsetPage(pageNo, pageSize);
        }

        if (typeKeyName == null) {
            typeKeyName = "";
        }

        List<HashMap<String, Object>> baseDataListBaseData = Lists.newArrayList();
        /* ????????????????????????????????? */
        List<SysBaseData> sysBaseDatas = sysBaseDataDao.getBaseDataByName(typeKeyName);
        /* ???????????????????????? */
        List<SysBaseDataType> baseDataTypes = new ArrayList<>();
        for (SysBaseData sysBaseData : sysBaseDatas) {
            SysBaseDataType sysBaseDataType = sysBaseData.getSysBaseDataType();
            if (!baseDataTypes.contains(sysBaseDataType)) {
                baseDataTypes.add(sysBaseDataType);
            }
        }
        for (SysBaseDataType sysBaseDataType : baseDataTypes) {
            HashMap<String, Object> res = new HashMap<>();
            List<SysBaseData> subList = new ArrayList<>();
            String BaseDataTypeID = sysBaseDataType.getId();

            for (SysBaseData sysBaseData : sysBaseDatas) {
                SysBaseDataType sysBaseDataType1 = sysBaseData.getSysBaseDataType();
                String BaseDataTypeID1 = sysBaseDataType1.getId();
                if (BaseDataTypeID.equals(BaseDataTypeID1)) {
                    subList.add(sysBaseData);
                }
            }

            List<SysBaseDataType> types = sysBaseDataTypeDao.getBaseDataTypeById(BaseDataTypeID);
            if (types == null || types.size() == 0) {
                throw new SofnException("?????????????????????ID???" + BaseDataTypeID + "?????????????????????????????????");
            }

            SysBaseDataType sysBaseDataType1 = types.get(0);
            String BaseDataTypeId = sysBaseDataType1.getId();
            String BaseDataTypeName = sysBaseDataType1.getTypename();
            String BaseDataTypeValue = sysBaseDataType1.getTypevalue();

            res.put("id", BaseDataTypeId);
            res.put("typename", BaseDataTypeName);
            res.put("typevalue", BaseDataTypeValue);
            res.put("data", subList);
            baseDataListBaseData.add(res);
        }


        //1?????????????????????????????????2???????????????????????????????????????3??????????????????

//        List<SysBaseData> BaseDataGroup = sysBaseDataDao.getBaseDataType(BaseDatatypeid);
        List<SysBaseDataType> BaseDataTypeGroup = sysBaseDataTypeDao.getBaseDataTypeByName(typeKeyName, "");
        PageInfo<SysBaseDataType> pageInfo = new PageInfo<SysBaseDataType>(BaseDataTypeGroup);
        PageInfo<HashMap<String, Object>> pageInfo2 = new PageInfo<HashMap<String, Object>>();
        BeanUtils.copyProperties(pageInfo, pageInfo2);
        List<SysBaseDataType> list = pageInfo.getList();
        List<HashMap<String, Object>> BaseDataList = Lists.newArrayList();

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> res = new HashMap<>();
            SysBaseDataType sysBaseDataType = BaseDataTypeGroup.get(i);
            String BaseDataTypeId = sysBaseDataType.getId();
            String BaseDataTypeName = sysBaseDataType.getTypename();
            String BaseDataTypeValue = sysBaseDataType.getTypevalue();
            List<SysBaseData> subBaseData = sysBaseDataDao.getBaseDataByValueAndType(BaseDataTypeId, "", "", null);
            res.put("id", BaseDataTypeId);
            res.put("typename", BaseDataTypeName);
            res.put("typevalue", BaseDataTypeValue);
            res.put("data", subBaseData);
            BaseDataList.add(res);
        }

        /*BaseDataList.removeAll(BaseDataListBaseData);
        BaseDataList.addAll(BaseDataListBaseData);*/
        for (HashMap<String, Object> resMap : baseDataListBaseData) {
            if (!BaseDataList.contains(resMap)) {
                BaseDataList.add(resMap);
            }
        }

        pageInfo2.setList(BaseDataList);
//        pageInfo.setTotal(BaseDataTypeGroup.size());
        return PageUtils.getPageUtils(pageInfo2);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveBaseDataInfo(SysBaseDataVo sysBaseDataVo) {

        SysBaseData sysBaseData = SysBaseDataVo.getSysBaseData(sysBaseDataVo);
        sysBaseData.preInsert();

        sysBaseDataDao.saveBaseDataInfo(sysBaseData);
        // ????????????
        if (sysBaseData.getSysBaseDataType() != null) {
            SysBaseDataType sysBaseDataType = sysBaseDataTypeDao.selectById(sysBaseData.getSysBaseDataType().getId());
            if (sysBaseDataType != null) {
                BaseDataUtils.deleteCacheByType(sysBaseDataType.getTypevalue());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateBaseDataInfo(String id, String enable) {
        Date updateTime = new Date();
        String updateUserId = UserUtil.getLoginUserId();
        sysBaseDataDao.updateBaseDataInfo(id, enable, updateTime, updateUserId);

        List<SysBaseData> sysBaseDataList = sysBaseDataDao.getBaseDataById(Lists.newArrayList(id));
        if (sysBaseDataList.size() > 0) {
            SysBaseData sysBaseData = sysBaseDataList.get(0);
            SysBaseDataType sysBaseDataType = sysBaseData.getSysBaseDataType();
            // ????????????
            if (sysBaseDataType != null) {
                BaseDataUtils.deleteCacheByType(sysBaseDataType.getTypevalue());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBaseDataInfo(String id) {

        // id,id
        List<String> ids = IdUtil.getIdsByStr(id);
        if (CollectionUtils.isEmpty(ids)) {
            throw new SofnException("???????????????????????????");
        }
        List<SysBaseData> sysBaseDataList = sysBaseDataDao.getBaseDataById(ids);
        if (sysBaseDataList.size() > 0) {
            sysBaseDataList.forEach(item -> {
                // ??????????????? ?????????????????? ???????????????????????????????????????
                SysBaseDataType sysBaseDataType = item.getSysBaseDataType();
                if (sysBaseDataType != null) {
                    SysBaseDataType baseDataType = sysBaseDataTypeDao.selectById(sysBaseDataType.getId());
                    // ????????????
                    if (baseDataType != null) {
                        BaseDataUtils.deleteCacheByType(baseDataType.getTypevalue());
                    }
                }
            });
        }
        sysBaseDataDao.deleteBaseDataInfo(ids);
        // ???????????????????????????????????????
        sysSubSystemBaseDataOwnMapper.deleteByBaseDataIds(ids);
      //?????????????????????????????????
      BaseDataUtils.clearAllCache();
    }

    @Override
    public List<SysBaseData> getBaseDataByName(String baseDataName) {
        List<SysBaseData> res = sysBaseDataDao.getBaseDataByName(baseDataName);
        return res;
    }

    @Override
    public List<SysBaseData> getBaseDataByValueAndType(String baseDataTypeId, String baseDataName, String baseDataCode, String id) {
        List<SysBaseData> res = sysBaseDataDao.getBaseDataByValueAndType(baseDataTypeId, baseDataName, baseDataCode, id);
        return res;
    }

    @Override
    public void exportBaseData(String filePath) {
        List<SysBaseData> res = sysBaseDataDao.getBaseDataByValueAndType("", "", "", null);
        if (!CollectionUtils.isEmpty(res)) {
            List<SysBaseDataVo> sysBaseDataVos = Lists.newArrayList();
            res.forEach((sysBaseData -> {
                SysBaseDataVo sysBaseDataVo = SysBaseDataVo.getSysBaseDataVo(sysBaseData);
                sysBaseDataVos.add(sysBaseDataVo);
            }));
            System.out.println(res.size());
            log.info("??????????????????");
            ExcelExportUtil.createExcel(filePath, ImportBaseDataBean.class, sysBaseDataVos);
            log.info("??????????????????");
        }

    }

    @Override
    public List<SysBaseData> getBaseDataById(String id) {
        return sysBaseDataDao.getBaseDataById(Lists.newArrayList(id));
    }

    @Override
    public List<SysBaseData> getBaseDataNameByValueAndType(String typename, String baseDataCode) {
        return sysBaseDataDao.getBaseDataNameByValueAndType(typename, baseDataCode);
    }

    @Override
    public List<SysBaseData> getBaseDataListByType(String typeValue) {
        return sysBaseDataDao.getBaseDataListByType(typeValue);
    }

    /**
     * ??????appid???type???key??????????????????
     *
     * @param type ??????????????????
     * @param key  ??????????????????
     * @return ????????????
     */
    @Override
    public BaseData getByAppIdAndTypeAndKey(String appId, String type, String key) {
        List<BaseData> list = getByAppIdAndType(appId, type);
        if (list != null && list.size() > 0) {
            for (BaseData baseData : list) {
                if (baseData == null) {
                    continue;
                }

                if (key.equals(baseData.getBaseDataCode())) {
                    return baseData;
                }
            }
        }

        return null;
    }

    /**
     * ??????appid???type???value??????????????????
     *
     * @param type  ??????????????????
     * @param value ???????????????
     * @return ????????????
     */
    @Override
    public BaseData getByAppIdAndTypeAndValue(String appId, String type, String value) {
        List<BaseData> list = getByAppIdAndType(appId, type);
        if (list != null && list.size() > 0) {
            for (BaseData baseData : list) {
                if (baseData == null) {
                    continue;
                }

                if (value.equals(baseData.getBaseDataName())) {
                    return baseData;
                }
            }
        }

        return null;
    }

    /**
     * ??????appid???type??????????????????
     *
     * @param appId ??????appID
     * @param type  ??????????????????
     * @return ??????????????????
     */
    @Override
    public List<BaseData> getByAppIdAndType(String appId, String type) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(appId)) {
            return null;
        }

        List<BaseData> list = BaseDataUtils.getByType(appId, type);
        return list;
    }

    @Override
    public List<BaseData> getBaseDataCodeByNames(String appId, String baseDataTypeCode, String baseDataNames) {
        if (StringUtils.isBlank(appId)) {
            throw new SofnException("??????????????????");
        }
        if (StringUtils.isBlank(baseDataTypeCode)) {
            throw new SofnException("??????????????????????????????");
        }
        if (StringUtils.isBlank(baseDataNames)) {
            return Lists.newArrayList();
        }
        List<String> names = IdUtil.getIdsByStr(baseDataNames);
        if (CollectionUtils.isEmpty(names)) {
            return Lists.newArrayList();
        }

        return sysBaseDataDao.getBaseDataCodeByNames(appId, baseDataTypeCode, names);
    }

    @Override
    public PageUtils<SysBaseData> getSysBaseDataByPage(String keyword, String baseDataTypeCode, Integer pageNo, Integer pageSize) {
        PageHelper.offsetPage(pageNo, pageSize);
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyword", keyword);
        params.put("baseDataTypeCode", baseDataTypeCode);
        List<SysBaseData> baseDataCodeByParams = sysBaseDataDao.getBaseDataCodeByParams(params);
        PageInfo<SysBaseData> pageInfo = new PageInfo<>(baseDataCodeByParams);
        return PageUtils.getPageUtils(pageInfo);
    }

    @Override
    public void updateBaseDataInfoById(SysBaseData sysBaseData) {
        sysBaseDataDao.updateBaseDataInfoById(sysBaseData);
      //?????????????????????????????????
      BaseDataUtils.clearAllCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBaseData(List<ImportBaseDataVo> importBaseDataVos) {
        if (CollectionUtils.isEmpty(importBaseDataVos)) {
            return;
        }
        // ???????????????????????????????????????type??? ????????????????????????
        Set<String> allTypes = importBaseDataVos.stream().map(item-> item.getTypeCode().trim()).collect(Collectors.toSet());
        if (allTypes.size() > 1) {
            throw new SofnException("???????????????????????????????????????");
        }
        List<SysBaseDataType> baseDataTypeByCodes = sysBaseDataTypeDao.getBaseDataTypeByCodes(Lists.newArrayList(allTypes));
        if (CollectionUtils.isEmpty(baseDataTypeByCodes)) {
            throw new SofnException("?????????????????????????????????");
        }

        // ??????????????????    ??????????????????????????????
        // ???????????????????????????????????????
        Set<String> importNames = importBaseDataVos.stream().map(ImportBaseDataVo::getDataName).collect(Collectors.toSet());
        Set<String> importCodes = importBaseDataVos.stream().map(ImportBaseDataVo::getDataCode).collect(Collectors.toSet());
        if (importCodes.size() != importBaseDataVos.size()) {
            List<String> repeatCode = getRepeteElement(importBaseDataVos, "");
            if (!CollectionUtils.isEmpty(repeatCode)) {
                throw new SofnException("?????????????????????????????????????????????" + repeatCode);
            }
        }
        if (importNames.size() != importBaseDataVos.size()) {
            List<String> repeatCode = getRepeteElement(importBaseDataVos, "NAME");
            if (!CollectionUtils.isEmpty(repeatCode)) {
                throw new SofnException("?????????????????????????????????????????????" + repeatCode);
            }
        }

        // ???????????????????????????
        String typeId = baseDataTypeByCodes.get(0).getId();
        List<SysBaseData> baseDataByBaseTypeIds = sysBaseDataDao.getBaseDataByBaseTypeIds(Lists.newArrayList(typeId));
        if (!CollectionUtils.isEmpty(baseDataByBaseTypeIds)) {
            List<String> baseDataCodes = baseDataByBaseTypeIds.stream().map(SysBaseData::getBaseDatacode).collect(Collectors.toList());
            List<String> baseDataNames = baseDataByBaseTypeIds.stream().map(SysBaseData::getBaseDataname).collect(Collectors.toList());
            // ???????????????
            List<String> repeatNames = importNames.stream().filter(baseDataNames::contains).collect(Collectors.toList());
            List<String> repeatCodes = importCodes.stream().filter(baseDataCodes::contains).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(repeatNames) || !CollectionUtils.isEmpty(repeatCodes)) {
                throw new SofnException("????????????????????????????????????:" + repeatNames + "??? ?????????????????????:" + repeatCodes);
            }
        }

        // ??????????????????????????????????????????
        List<SysBaseData> collect1 = importBaseDataVos.stream().map(item -> {
            SysBaseData sysBaseData = new SysBaseData();
            sysBaseData.preInsert();
            sysBaseData.setId(IdUtil.getUUId());
            sysBaseData.setEnable("??????".equals(item.getEnable()) ? BoolUtils.Y : BoolUtils.N);
            sysBaseData.setRemark(item.getRemark());
            sysBaseData.setBasedatatypeid(typeId);
            sysBaseData.setBaseDataname(item.getDataName());
            sysBaseData.setBaseDatacode(item.getDataCode());
            return sysBaseData;
        }).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(collect1)){
            // ????????????
            sysBaseDataDao.batchSaveBaseDataInfo(collect1);
        }

    }

    @Override
    public PageUtils<SysBaseDataAndTypeVo> getSysBaseDataAndType(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        PageHelper.offsetPage(pageNo,pageSize);
        List<SysBaseDataAndTypeVo> sysBaseDataAndType = sysBaseDataDao.getSysBaseDataAndType(params);
        PageInfo<SysBaseDataAndTypeVo> pageInfo = new PageInfo<>(sysBaseDataAndType);
        return PageUtils.getPageUtils(pageInfo);
    }


    /**
     * ?????????????????????
     *
     * @param importBaseDataVos ???????????????
     * @param type              NAME
     * @return List<String>
     */
    private List<String> getRepeteElement(List<ImportBaseDataVo> importBaseDataVos, String type) {

        Map<String, Integer> maps = Maps.newHashMap();
        importBaseDataVos.forEach(item -> {
            Integer num;
            if ("NAME".equals(type)) {
                num = maps.get(item.getDataName());
            } else {
                num = maps.get(item.getDataCode());
            }
            if (num == null) {
                num = 1;
            } else {
                num += 1;
            }
            if ("NAME".equals(type)) {
                maps.put(item.getDataName(), num);
            } else {
                maps.put(item.getDataCode(), num);
            }
        });
        List<String> repeatCode = Lists.newArrayList();
        maps.forEach((k, v) -> {
            if (v > 1) {
                repeatCode.add(k);
            }
        });
        return repeatCode;
    }


}
