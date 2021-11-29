package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSON;
import com.sofn.bigdata.enums.MapSysCodeEnum;
import com.sofn.bigdata.service.MapService;
import com.sofn.bigdata.sysapi.bean.MapData;
import com.sofn.bigdata.sysapi.bean.MapSysConditions;
import com.sofn.bigdata.sysapi.map.*;
import com.sofn.bigdata.vo.*;
import com.sofn.bigdata.vo.pieChart.*;
import com.sofn.common.exception.SofnException;
import com.sofn.common.map.MapViewData;
import com.sofn.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class MapServiceImpl  implements MapService {

    @Autowired
    private PcnplMapApi pcnplMapApi;
    @Autowired
    private AstcapMapApi astcapMapApi;
    @Autowired
    private PmlcscMapApi pmlcscMapApi;
    @Autowired
    private AeemMapApi aeemMapApi;
    @Autowired
    private DucssMapApi ducssMapApi;
    @Autowired
    private AgpjzbMapApi agpjzbMapApi;

    @Override
    public List<MapData> getMapData(List<AstiiStatisticQueryVo> vos) {
        List<MapData> mapDataList = new ArrayList<>();
        for (AstiiStatisticQueryVo vo : vos) {
            if (vo.getSysCode().equals(MapSysCodeEnum.PCNPL.getCode())){
                //从氮磷系统获取地图数据
                MapData pcnplMapData = new MapData();
                if (vo.getIndex()==null||vo.getIndex()==""){
                    mapDataList.add(null);
                }else {
                    if (vo.getRegionLevel().equals("ad_country")){
                        vo.setRegionCode(null);
                    }
                    if (vo.getYear()==null||vo.getYear()==""){
                        vo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                    }
                    Result<MapViewDataA> mapViewData = null;
                    Result<List<ShowDataVo>> listResult = null;
                    try {
                         mapViewData = pcnplMapApi.getDwfbData(vo.getRegionCode(), vo.getYear());
                         listResult = pcnplMapApi.showData(vo.getYear(), vo.getRegionCode());
                    } catch (Exception e) {
                        throw new SofnException("氮磷系统服务器报错："+e);
                    }
                    mapViewData = pcnplMap(mapViewData,listResult,vo.getIndex());
                    pcnplMapData.setMapViewData2(mapViewData.getData());
                    pcnplMapData.setCode(MapSysCodeEnum.PCNPL.getCode());
                    pcnplMapData.setName(MapSysCodeEnum.PCNPL.getDesc());
                    pcnplMapData.setMsg(mapViewData.getCode()+"----"+mapViewData.getMsg());
                    mapDataList.add(pcnplMapData);
                }
            }else if(vo.getSysCode().equals(MapSysCodeEnum.ASTCAP.getCode())){
                //从沼气系统获取地图数据
                MapData astcapMapData = new MapData();
                Result<List<SiteForm>> siteListByCondition = null;
                try {
                    siteListByCondition = astcapMapApi.getSiteListByCondition(null, null, null);
                } catch (Exception e) {
                    throw new SofnException("沼气系统服务器报错："+e);
                }
                List<SiteForm> datas = siteListByCondition.getData();
                astcapMapData.setCode(MapSysCodeEnum.ASTCAP.getCode());
                astcapMapData.setName(MapSysCodeEnum.ASTCAP.getDesc());
                astcapMapData.setDatas(datas);
                astcapMapData.setMsg(siteListByCondition.getCode()+"----"+siteListByCondition.getMsg());
                mapDataList.add(astcapMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.ASTII_MAIN.getCode())){
                //从农业体系-全国可再生能源获取地图数据
                switch (vo.getRegionLevel()){
                    case "ad_country":
                        vo.setRegionLevel("1");
                        break;
                    case "ad_province":
                        vo.setRegionLevel("2");
                        break;
                    case "ad_city":
                        vo.setRegionLevel("3");
                        break;
                    case "ad_county":
                        vo.setRegionLevel("4");
                        break;
                    default:
                        throw new SofnException("行政级别参数有误");
                }
                MapData astiiMainMapData = new MapData();
                if (vo.getYear()==null||vo.getYear()==""){
                    vo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                }
                Result<List<Map<String, Object>>> main = null;
                try {
                    main = astcapMapApi.getDataTableMain(vo);
                } catch (Exception e) {
                    throw new SofnException("农业体系服务器报错："+e);
                }
                astiiMainMapData.setDatas(main.getData());
                astiiMainMapData.setMsg(main.getCode()+"----"+main.getMsg());
                astiiMainMapData.setName(MapSysCodeEnum.ASTII_MAIN.getDesc());
                astiiMainMapData.setCode(MapSysCodeEnum.ASTII_MAIN.getCode());
                mapDataList.add(astiiMainMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.ASTII_RREM.getCode())){
                //从农业体系-可再生能源获取地图数据
                switch (vo.getRegionLevel()){
                    case "ad_country":
                        vo.setRegionLevel("1");
                        break;
                    case "ad_province":
                        vo.setRegionLevel("2");
                        break;
                    case "ad_city":
                        vo.setRegionLevel("3");
                        break;
                    case "ad_county":
                        vo.setRegionLevel("4");
                        break;
                    default:
                        throw new SofnException("行政级别参数有误");
                }
                MapData astiiRremMapData = new MapData();
                if (vo.getYear()==null||vo.getYear()==""){
                    vo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                }
                Result<List<Map<String, Object>>> main = null;
                try {
                    main = astcapMapApi.getDataTableRrem(vo);
                } catch (Exception e) {
                    throw new SofnException("农业体系服务器报错："+e);
                }
                astiiRremMapData.setDatas(main.getData());
                astiiRremMapData.setMsg(main.getCode()+"----"+main.getMsg());
                astiiRremMapData.setName(MapSysCodeEnum.ASTII_RREM.getDesc());
                astiiRremMapData.setCode(MapSysCodeEnum.ASTII_RREM.getCode());
                mapDataList.add(astiiRremMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.ASTII.getCode())){
                //从农业体系-全国资源环境获取地图数据
                switch (vo.getRegionLevel()){
                    case "ad_country":
                        vo.setRegionLevel("1");
                        break;
                    case "ad_province":
                        vo.setRegionLevel("2");
                        break;
                    case "ad_city":
                        vo.setRegionLevel("3");
                        break;
                    case "ad_county":
                        vo.setRegionLevel("4");
                        break;
                    default:
                        throw new SofnException("行政级别参数有误");
                }
                MapData astiiMapData = new MapData();
                if (vo.getYear()==null||vo.getYear()==""){
                    vo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                }
                Result<List<Map<String, Object>>> main = null;
                try {
                    main = astcapMapApi.getDataTableAstii(vo);
                } catch (Exception e) {
                    throw new SofnException("农业体系服务器报错："+e);
                }
                astiiMapData.setDatas(main.getData());
                astiiMapData.setMsg(main.getCode()+"----"+main.getMsg());
                astiiMapData.setName(MapSysCodeEnum.ASTII.getDesc());
                astiiMapData.setCode(MapSysCodeEnum.ASTII.getCode());
                mapDataList.add(astiiMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.ASTII_REPA.getCode())){
                //从农业体系-资源环境获取地图数据
                switch (vo.getRegionLevel()){
                    case "ad_country":
                        vo.setRegionLevel("1");
                        break;
                    case "ad_province":
                        vo.setRegionLevel("2");
                        break;
                    case "ad_city":
                        vo.setRegionLevel("3");
                        break;
                    case "ad_county":
                        vo.setRegionLevel("4");
                        break;
                    default:
                        throw new SofnException("行政级别参数有误");
                }
                MapData astiiRepaMapData = new MapData();
                if (vo.getYear()==null||vo.getYear()==""){
                    vo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                }
                Result<List<Map<String, Object>>> main = null;
                try {
                    main = astcapMapApi.getDataTableRepa(vo);
                } catch (Exception e) {
                    throw new SofnException("农业体系服务器报错："+e);
                }
                astiiRepaMapData.setDatas(main.getData());
                astiiRepaMapData.setMsg(main.getCode()+"----"+main.getMsg());
                astiiRepaMapData.setName(MapSysCodeEnum.ASTII_REPA.getDesc());
                astiiRepaMapData.setCode(MapSysCodeEnum.ASTII_REPA.getCode());
                mapDataList.add(astiiRepaMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.PMLCSC.getCode())){
                //从国控点土壤信息采集子系统获取地图数据
                if (vo.getRegionLevel()==null){
                    vo.setRegionLevel("ad_country");
                }
                MapData pmlcscMapData = new MapData();
                SummaryQueryVo summaryQueryVo = new SummaryQueryVo();
                summaryQueryVo.setAdCode(vo.getRegionCode());
                summaryQueryVo.setAdLevel(vo.getRegionLevel());
                summaryQueryVo.setAreaCentreId(vo.getAreaCentreId());
                summaryQueryVo.setTaskCategory(vo.getTaskCategory());
                summaryQueryVo.setTaskType(vo.getTaskType());
                if (vo.getYear()==null||vo.getYear()==""){
                    summaryQueryVo.setYear(String.valueOf(LocalDateTime.now().getYear()));
                }else {
                    summaryQueryVo.setYear(vo.getYear());
                }
                Result<MapViewData> statisticalData = null;
                try {
                    statisticalData = pmlcscMapApi.getStatisticalData(summaryQueryVo);
                } catch (Exception e) {
                    throw new SofnException("国控点土壤信息采集子系统服务器报错："+e);
                }
                pmlcscMapData.setMsg(statisticalData.getCode()+"----"+statisticalData.getMsg());
                pmlcscMapData.setName(MapSysCodeEnum.PMLCSC.getDesc());
                pmlcscMapData.setCode(MapSysCodeEnum.PMLCSC.getCode());
                pmlcscMapData.setMapViewData(statisticalData.getData());
                mapDataList.add(pmlcscMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.AEEM.getCode())){
                //从地膜系统获取地图数据
                MapData aeemMapData = new MapData();
                Result<MapViewData> statisticalData = null;
                try {
                    statisticalData = aeemMapApi.getDateMapDisplay(vo.getIndex(), vo.getRegionLevel(), vo.getRegionCode(), vo.getYear());
                } catch (Exception e) {
                    throw new SofnException("地膜系统服务器报错："+e);
                }
                aeemMapData.setMsg(statisticalData.getCode()+"----"+statisticalData.getMsg());
                aeemMapData.setName(MapSysCodeEnum.AEEM.getDesc());
                aeemMapData.setCode(MapSysCodeEnum.AEEM.getCode());
                aeemMapData.setMapViewData(statisticalData.getData());
                mapDataList.add(aeemMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.DUCSS.getCode())){
                //从农作物秸秆资源台账子系统获取地图数据
                MapData ducssMapData = new MapData();
                Result<MapViewData> ducssData = null;
                if (vo.getRegionLevel().equals("ad_county")||vo.getRegionLevel()==null){
                    vo.setRegionLevel("ad_country");
                }
                try {
                    ducssData = ducssMapApi.getMapViewData(vo.getIndex(), vo.getRegionLevel(), vo.getRegionCode(),vo.getYear());
                } catch (Exception e) {
                    throw new SofnException("农作物秸秆资源台账子系统服务器报错："+e);
                }
                ducssMapData.setMsg(ducssData.getCode()+"----"+ducssData.getMsg());
                ducssMapData.setName(MapSysCodeEnum.DUCSS.getDesc());
                ducssMapData.setCode(MapSysCodeEnum.DUCSS.getCode());
                ducssMapData.setMapViewData(ducssData.getData());
                mapDataList.add(ducssMapData);
            }else if(vo.getSysCode().equals(MapSysCodeEnum.AGPJZB.getCode())){
                //从农业野生植物保护名录管理子系统获取地图数据
                MapData agpjzbMapData = new MapData();
                Result<MapViewData> agpjzbData = null;
                try {
                    agpjzbData = agpjzbMapApi.getMapViewData(vo.getIndex(), vo.getRegionLevel(), vo.getRegionCode(),vo.getPlantId());
                } catch (Exception e) {
                    throw new SofnException("农业野生植物保护名录管理子系统服务器报错："+e);
                }
                agpjzbMapData.setMsg(agpjzbData.getCode()+"----"+agpjzbData.getMsg());
                agpjzbMapData.setName(MapSysCodeEnum.AGPJZB.getDesc());
                agpjzbMapData.setCode(MapSysCodeEnum.AGPJZB.getCode());
                agpjzbMapData.setMapViewData(agpjzbData.getData());
                mapDataList.add(agpjzbMapData);
            }else {
                throw new SofnException("未查询到该系统或系统输入有误");
            }
        }

        return mapDataList;
    }

    //将指标进行过滤封装数据到统一返回对象中
    private Result<MapViewDataA> pcnplMap(Result<MapViewDataA> mapViewData, Result<List<ShowDataVo>> listResult,String index) {
        Map<String,List<AdAreaData>> adAreaDataList = new HashMap<>();
        List<AdAreaData> areaDataList = new ArrayList<>();
        for (ShowDataVo datum : listResult.getData()) {
            AdAreaData adAreaData = new AdAreaData();
            adAreaData.setAdRegionCode(datum.getAdRegionCode());
            adAreaData.setAdRegionName(datum.getAdRegionName());
            switch (index){
                //规模种植主体数量
                case "gmzz_size":
                    adAreaData.setIndexValue(datum.getGmzzztqkSl());
                    break;
                //耕地和园地总面积
                case "gd_yd_size":
                    adAreaData.setIndexValue(datum.getJcxxGdhydzmj());
                    break;
                //农村劳动力数量
                case "farm_people":
                    adAreaData.setIndexValue(datum.getJcxxNcldlsl());
                    break;
                //农户总数
                case "farm_family":
                    adAreaData.setIndexValue(datum.getJcxxNhzs());
                    break;
                //不同坡度耕地和园地面积
                case "btpdgyd":
                    Btpdgdyd btpdgdyd = new Btpdgdyd();
                    btpdgdyd.setGdYdSizeDpd(datum.getBtpdgdhydmjDpd());
                    btpdgdyd.setGdYdSizeHpd(datum.getBtpdgdhydmjHpd());
                    btpdgdyd.setGdYdSizePd(datum.getBtpdgdhydmjPd());
                    adAreaData.setBtpdgdyd(btpdgdyd);
                    break;
                //耕地面积
                case "gd_size":
                    GdSize gdSize = new GdSize();
                    gdSize.setGdHdSize(datum.getGdmjHd());
                    gdSize.setGdStSize(datum.getGdmjSt());
                    adAreaData.setGdSize(gdSize);
                    break;
                //菜地面积
                case "cd_size":
                    CdSize cdSize = new CdSize();
                    cdSize.setCdBhdSize(datum.getCdmjBhd());
                    cdSize.setCdLdSize(datum.getCdmjLd());
                    adAreaData.setCdSize(cdSize);
                    break;
                //园地面积
                case "yd_size":
                    YdSize ydSize = new YdSize();
                    ydSize.setYdCySize(datum.getYdmjCy());
                    ydSize.setYdGySize(datum.getYdmjGy());
                    ydSize.setYdOtherSize(datum.getYdmjQt());
                    ydSize.setYdSySize(datum.getYdmjSy());
                    adAreaData.setYdSize(ydSize);
                    break;
                //果园面积
                case "gy_size":
                    GySize gySize = new GySize();
                    gySize.setGyOtherSize(datum.getGymjQtgs());
                    gySize.setGyXjSize(datum.getGymjXj());
                    gySize.setGyTSize(datum.getGymjT());
                    gySize.setGyPtSize(datum.getGymjPt());
                    gySize.setGyPgSize(datum.getGymjPg());
                    gySize.setGyLSize(datum.getGymjL());
                    gySize.setGyJgSize(datum.getYdmjGy());
                    gySize.setGyBlSize(datum.getGymjBl());
                    adAreaData.setGySize(gySize);
                    break;
                //经济作物播种面积
                case "jjzw_size":
                    JjzwSize jjzwSize = new JjzwSize();
                    jjzwSize.setJjzwMmSize(datum.getJjzwbzmjMm());
                    jjzwSize.setJjzwYySize(datum.getJjzwbzmjYy());
                    jjzwSize.setJjzwYlzwSize(datum.getJjzwbzmjYlzw());
                    jjzwSize.setJjzwTlSize(datum.getJjzwbzmjTl());
                    adAreaData.setJjzwSize(jjzwSize);
                    break;
                //粮食作物播种面积
                case "lszw_size":
                    LszwSize lszwSize = new LszwSize();
                    lszwSize.setLszwDouSize(datum.getLszwbzmjDl());
                    lszwSize.setLszwYmSize(datum.getLszwbzmjYm());
                    lszwSize.setLszwXmSize(datum.getLszwbzmjXm());
                    lszwSize.setLszwSdSize(datum.getLszwbzmjSd());
                    lszwSize.setLszwOtherLszwSize(datum.getLszwbzmjQtlszw());
                    lszwSize.setLszwMlsSize(datum.getLszwbzmjMls());
                    adAreaData.setLszwSize(lszwSize);
                    break;
                //蔬菜瓜果播种面积
                case "scgg_size":
                    GgscSize ggscSize = new GgscSize();
                    ggscSize.setScggGgSize(datum.getScggbzmjGg());
                    ggscSize.setScggScSize(datum.getScggbzmjSc());
                    adAreaData.setGgscSize(ggscSize);
                    break;
                default:
                    throw  new SofnException("index指标错误");
            }
            areaDataList.add(adAreaData);
        }
        adAreaDataList.put("areaDataList",areaDataList);
        mapViewData.getData().setAdAreaDataList(adAreaDataList);
        return mapViewData;
    }

    @Override
    public List<MapSysConditions> getIndex() {
        List<MapSysConditions> mapSysConditions = new ArrayList<>();
        //从氮磷系统获取指标数据
        MapSysConditions mapPcnplCondition = new MapSysConditions();
        List listResult = getPcnplIndex();
        mapPcnplCondition.setList(listResult);
        mapPcnplCondition.setFieldName(MapSysCodeEnum.PCNPL.getCode());
        mapPcnplCondition.setIndexName(MapSysCodeEnum.PCNPL.getDesc());
        mapPcnplCondition.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(mapPcnplCondition.getFieldName()));
        mapSysConditions.add(mapPcnplCondition);
        //从沼气系统获取指标数据
        MapSysConditions mapAstcapCondition = new MapSysConditions();
        Result<List<HashMap<String, Object>>> gdConfigTree = null;
        try {
            gdConfigTree = astcapMapApi.getGdConfigTree();
        } catch (Exception e) {
            throw new SofnException("沼气系统服务器报错："+e);
        }
        mapAstcapCondition.setList(getAstcapIndex(gdConfigTree.getData()));
        mapAstcapCondition.setConfigid(MapSysCodeEnum.ASTCAP.getCode());
        mapAstcapCondition.setConfigname(MapSysCodeEnum.ASTCAP.getDesc());
        mapAstcapCondition.setCode(MapSysCodeEnum.ASTCAP.getCode()+"_"+getLetter(mapAstcapCondition.getConfigid()));
        mapSysConditions.add(mapAstcapCondition);
        //从农业体系获取农业资源环境指标数据
        MapSysConditions mapAstiiRepaCondition = new MapSysConditions();
        Result<List<AstiiStatisticIndexVo>> allIndexListRepa = null;
        try {
            allIndexListRepa = astcapMapApi.getAllIndexListRepa();
        } catch (Exception e) {
            throw new SofnException("农业体系服务器报错："+e);
        }
        mapAstiiRepaCondition.setList(getAstiiRepaIndex(allIndexListRepa.getData()));
        mapAstiiRepaCondition.setFieldName(MapSysCodeEnum.ASTII_REPA.getCode());
        mapAstiiRepaCondition.setIndexName(MapSysCodeEnum.ASTII_REPA.getDesc());
        mapAstiiRepaCondition.setCode(MapSysCodeEnum.ASTII_REPA.getCode()+"_"+getLetter(mapAstiiRepaCondition.getFieldName()));
        mapSysConditions.add(mapAstiiRepaCondition);
        //从农业体系获取农村可再生能源指标数据
        MapSysConditions mapAstiiRremCondition = new MapSysConditions();
        Result<List<AstiiStatisticIndexVo>> allIndexListRrem = null;
        try {
            allIndexListRrem = astcapMapApi.getAllIndexListRrem();
        } catch (Exception e) {
            throw new SofnException("农业体系报错："+e);
        }
        mapAstiiRremCondition.setList(getAstiiRremIndex(allIndexListRrem.getData()));
        mapAstiiRremCondition.setFieldName(MapSysCodeEnum.ASTII_RREM.getCode());
        mapAstiiRremCondition.setIndexName(MapSysCodeEnum.ASTII_RREM.getDesc());
        mapAstiiRremCondition.setCode(MapSysCodeEnum.ASTII_RREM.getCode()+"_"+getLetter(mapAstiiRremCondition.getFieldName()));
        mapSysConditions.add(mapAstiiRremCondition);
        //从农业体系获取全国农业资源环境指标数据
        MapSysConditions mapAstiiCondition = new MapSysConditions();
        Result<List<AstiiStatisticIndexVo>> allIndexListAstii = null;
        try {
            allIndexListAstii = astcapMapApi.getAllIndexListAstii();
        } catch (Exception e) {
            throw new SofnException("农业体系报错："+e);
        }
        mapAstiiCondition.setList(getAstiiIndex(allIndexListAstii.getData()));
        mapAstiiCondition.setFieldName(MapSysCodeEnum.ASTII.getCode());
        mapAstiiCondition.setIndexName(MapSysCodeEnum.ASTII.getDesc());
        mapAstiiCondition.setCode(MapSysCodeEnum.ASTII.getCode()+"_"+getLetter(mapAstiiCondition.getFieldName()));
        mapSysConditions.add(mapAstiiCondition);
        //从农业体系获取全国农村可再生能源指标数据
        MapSysConditions mapAstiiMainCondition = new MapSysConditions();
        Result<List<AstiiStatisticIndexVo>> allIndexListMain = null;
        try {
            allIndexListMain = astcapMapApi.getAllIndexListMain();
        } catch (Exception e) {
            throw new SofnException("农业体系服务器报错："+e);
        }
        mapAstiiMainCondition.setList(getAstiiMainIndex(allIndexListMain.getData()));
        mapAstiiMainCondition.setFieldName(MapSysCodeEnum.ASTII_MAIN.getCode());
        mapAstiiMainCondition.setIndexName(MapSysCodeEnum.ASTII_MAIN.getDesc());
        mapAstiiMainCondition.setCode(MapSysCodeEnum.ASTII_MAIN.getCode()+"_"+getLetter(mapAstiiMainCondition.getFieldName()));
        mapSysConditions.add(mapAstiiMainCondition);
        //从地膜系统指标数据
        MapSysConditions mapAeemCondition = new MapSysConditions();
        List aeemIndex = getAeemIndex();
        mapAeemCondition.setList(aeemIndex);
        mapAeemCondition.setFieldName(MapSysCodeEnum.AEEM.getCode());
        mapAeemCondition.setIndexName(MapSysCodeEnum.AEEM.getDesc());
        mapAeemCondition.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(mapAeemCondition.getFieldName()));
        mapSysConditions.add(mapAeemCondition);
        //从农作物秸秆资源台账子系统指标数据
        MapSysConditions mapDucssCondition = new MapSysConditions();
        List ducssIndex = getDucssIndex();
        mapDucssCondition.setList(ducssIndex);
        mapDucssCondition.setFieldName(MapSysCodeEnum.DUCSS.getCode());
        mapDucssCondition.setIndexName(MapSysCodeEnum.DUCSS.getDesc());
        mapDucssCondition.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(mapDucssCondition.getFieldName()));
        mapSysConditions.add(mapDucssCondition);
        //从国控点土壤信息采集子系统指标数据
        MapSysConditions pmlcscCondition = new MapSysConditions();
        Result<List<AreaCentre>> list = null;
        try {
            list = pmlcscMapApi.getList();
        } catch (Exception e) {
            throw new SofnException("国控点土壤信息采集子系统服务器报错："+e);
        }
        pmlcscCondition.setList(getPmlcscIndex(list.getData()));
        pmlcscCondition.setFieldName(MapSysCodeEnum.PMLCSC.getCode());
        pmlcscCondition.setIndexName(MapSysCodeEnum.PMLCSC.getDesc());
        pmlcscCondition.setCode(MapSysCodeEnum.PMLCSC.getCode()+"_"+getLetter(pmlcscCondition.getFieldName()));
        mapSysConditions.add(pmlcscCondition);
        //从农业野生植物保护名录管理子系统指标数据
        MapSysConditions mapAgpjzbCondition = new MapSysConditions();
        Result<List<DropDownVo>> agpjzbIndex = null;
        try {
            agpjzbIndex = agpjzbMapApi.listForSelect();
        } catch (Exception e) {
            throw new SofnException("农业野生植物保护名录管理子系统服务器报错："+e);
        }
        mapAgpjzbCondition.setList(getAgpjzbIndex(agpjzbIndex.getData()));
        mapAgpjzbCondition.setFieldName(MapSysCodeEnum.AGPJZB.getCode());
        mapAgpjzbCondition.setIndexName(MapSysCodeEnum.AGPJZB.getDesc());
        mapAgpjzbCondition.setCode(MapSysCodeEnum.AGPJZB.getCode()+"_"+getLetter(mapAgpjzbCondition.getFieldName()));
        mapSysConditions.add(mapAgpjzbCondition);
        return mapSysConditions;
    }


    /**
     * 地膜系统指标数据封装
     * @return
     */
    private static List getAeemIndex() {

        AstiiStatisticIndexVo voMaster = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> astiiStatisticIndexVosMaster = new ArrayList<>();
        AstiiStatisticIndexVo vo = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> astiiStatisticIndexVos = new ArrayList<>();
        AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
        vo1.setMapType("三级指标");
        vo1.setIndexName("残膜回收方式");
        vo1.setFieldName("monitorResidualMode");
        vo1.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo1.getFieldName()));
        vo1.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo1);
        AstiiStatisticIndexVo vo2 = new AstiiStatisticIndexVo();
        vo2.setMapType("三级指标");
        vo2.setIndexName("残膜回收量");
        vo2.setFieldName("monitorResidualAmount");
        vo2.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo2.getFieldName()));
        vo2.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo2);
        AstiiStatisticIndexVo vo3 = new AstiiStatisticIndexVo();
        vo3.setMapType("三级指标");
        vo3.setIndexName("残膜去向");
        vo3.setFieldName("monitorGoWays");
        vo3.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo3.getFieldName()));
        vo3.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo3);
        AstiiStatisticIndexVo vo4 = new AstiiStatisticIndexVo();
        vo4.setMapType("三级指标");
        vo4.setIndexName("点位分布");
        vo4.setFieldName("monitorPointsDistribution");
        vo4.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo4.getFieldName()));
        vo4.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo4);
        vo.setFieldName("监测点");
        vo.setCode(MapSysCodeEnum.AEEM.getCode()+"_jzd");
        vo.setIndexName("监测点");
        vo.setMapType("二级指标");
        vo.setList(astiiStatisticIndexVos);
        astiiStatisticIndexVosMaster.add(vo);

        AstiiStatisticIndexVo vot = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> astiiStatisticIndexVos1 = new ArrayList<>();
        AstiiStatisticIndexVo vo10 = new AstiiStatisticIndexVo();
        vo10.setMapType("三级指标");
        vo10.setIndexName("残膜回收方式");
        vo10.setFieldName("surveyResidualMode");
        vo10.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo10.getFieldName()));
        vo10.setList(new ArrayList<>());
        astiiStatisticIndexVos1.add(vo10);
        AstiiStatisticIndexVo vo9 = new AstiiStatisticIndexVo();
        vo9.setMapType("三级指标");
        vo9.setIndexName("残膜回收量");
        vo9.setFieldName("surveyResidualAmount");
        vo9.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo9.getFieldName()));
        vo9.setList(new ArrayList<>());
        astiiStatisticIndexVos1.add(vo9);
        AstiiStatisticIndexVo vo8 = new AstiiStatisticIndexVo();
        vo8.setMapType("三级指标");
        vo8.setIndexName("残膜去向");
        vo8.setFieldName("surveyGoWays");
        vo8.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo8.getFieldName()));
        vo8.setList(new ArrayList<>());
        astiiStatisticIndexVos1.add(vo8);
        AstiiStatisticIndexVo vo6 = new AstiiStatisticIndexVo();
        vo6.setMapType("三级指标");
        vo6.setIndexName("点位分布");
        vo6.setFieldName("surveyPointsDistribution");
        vo6.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(vo6.getFieldName()));
        vo6.setList(new ArrayList<>());
        astiiStatisticIndexVos1.add(vo6);
        vot.setFieldName("调查点");
        vot.setIndexName("调查点");
        vot.setMapType("二级指标");
        vot.setCode(MapSysCodeEnum.AEEM.getCode()+"_jcd");
        vot.setList(astiiStatisticIndexVos1);
        astiiStatisticIndexVosMaster.add(vot);
        voMaster.setFieldName("地膜残留监测");
        voMaster.setIndexName("地膜残留监测");
        voMaster.setCode(MapSysCodeEnum.AEEM.getCode()+"_dmcljc");
        voMaster.setMapType("一级指标");
        voMaster.setList(astiiStatisticIndexVosMaster);

        AstiiStatisticIndexVo masterObj = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> listMaster = new ArrayList<>();
        AstiiStatisticIndexVo obj = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> list = new ArrayList<>();
        AstiiStatisticIndexVo obj1 = new AstiiStatisticIndexVo();
        obj1.setIndexName("残膜回收方式");
        obj1.setMapType("三级指标");
        obj1.setFieldName("recycleMonitorResidualMode");
        obj1.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(obj1.getFieldName()));
        obj1.setList(new ArrayList<>());
        list.add(obj1);
        AstiiStatisticIndexVo obj2 = new AstiiStatisticIndexVo();
        obj2.setIndexName("残膜回收量");
        obj2.setMapType("三级指标");
        obj2.setFieldName("recycleMonitorResidualAmount");
        obj2.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(obj2.getFieldName()));
        obj2.setList(new ArrayList<>());
        list.add(obj2);
        AstiiStatisticIndexVo obj3 = new AstiiStatisticIndexVo();
        obj3.setIndexName("残膜去向");
        obj3.setMapType("三级指标");
        obj3.setFieldName("recycleMonitorGoWays");
        obj3.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(obj3.getFieldName()));
        obj3.setList(new ArrayList<>());
        list.add(obj3);
        AstiiStatisticIndexVo obj4 = new AstiiStatisticIndexVo();
        obj4.setIndexName("点位分布");
        obj4.setMapType("三级指标");
        obj4.setFieldName("recycleMonitorPointsDistribution");
        obj4.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(obj4.getFieldName()));
        obj4.setList(new ArrayList<>());
        list.add(obj4);
        obj.setFieldName("监测点");
        obj.setIndexName("监测点");
        obj.setMapType("二级指标");
        obj.setCode(MapSysCodeEnum.AEEM.getCode()+"_dsds");
        obj.setList(list);
        listMaster.add(obj);

        AstiiStatisticIndexVo aa = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb = new ArrayList<>();
        AstiiStatisticIndexVo a1 = new AstiiStatisticIndexVo();
        a1.setIndexName("农用地膜");
        a1.setMapType("三级指标");
        a1.setFieldName("utilizationRecyclingRateLand");
        a1.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a1.getFieldName()));
        a1.setList(new ArrayList<>());
        bb.add(a1);
        AstiiStatisticIndexVo a2 = new AstiiStatisticIndexVo();
        a2.setIndexName("农用棚膜");
        a2.setMapType("三级指标");
        a2.setFieldName("utilizationRecyclingRateShed");
        a2.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a2.getFieldName()));
        a2.setList(new ArrayList<>());
        bb.add(a2);
        aa.setFieldName("农膜使用及回收利用");
        aa.setIndexName("农膜使用及回收利用");
        aa.setCode(MapSysCodeEnum.AEEM.getCode()+"_sss");
        aa.setMapType("二级指标");
        aa.setList(bb);
        listMaster.add(aa);

        AstiiStatisticIndexVo aa1 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb1 = new ArrayList<>();
        AstiiStatisticIndexVo a3 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bbb1 = new ArrayList<>();
        AstiiStatisticIndexVo a31 = new AstiiStatisticIndexVo();
        a31.setIndexName("回收率");
        a31.setMapType("四级指标");
        a31.setFieldName("cropMulchGrainRecycleRate");
        a31.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a31.getFieldName()));
        a31.setList(new ArrayList<>());
        bbb1.add(a31);
        AstiiStatisticIndexVo a32 = new AstiiStatisticIndexVo();
        a32.setIndexName("覆膜率");
        a32.setMapType("四级指标");
        a32.setFieldName("cropMulchGrainCoatRate");
        a32.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a32.getFieldName()));
        a32.setList(new ArrayList<>());
        bbb1.add(a32);
        a3.setFieldName("粮食作物");
        a3.setMapType("三级指标");
        a3.setIndexName("粮食作物");
        a3.setCode(MapSysCodeEnum.AEEM.getCode()+"_vad");
        a3.setList(bbb1);
        bb1.add(a3);
        AstiiStatisticIndexVo a4 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bbb2 = new ArrayList<>();
        AstiiStatisticIndexVo a41 = new AstiiStatisticIndexVo();
        a41.setIndexName("回收率");
        a41.setMapType("四级指标");
        a41.setFieldName("cropMulchEconomyRecycleRate");
        a41.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a41.getFieldName()));
        a41.setList(new ArrayList<>());
        bbb2.add(a41);
        AstiiStatisticIndexVo a42 = new AstiiStatisticIndexVo();
        a42.setIndexName("覆膜率");
        a42.setMapType("四级指标");
        a42.setFieldName("cropMulchEconomyCoatRate");
        a42.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a42.getFieldName()));
        a42.setList(new ArrayList<>());
        bbb2.add(a42);
        a4.setFieldName("经济作物");
        a4.setMapType("三级指标");
        a4.setIndexName("经济作物");
        a4.setCode(MapSysCodeEnum.AEEM.getCode()+"_hafasd");
        a4.setList(bbb2);
        bb1.add(a4);
        aa1.setFieldName("主要作物地膜使用回收");
        aa1.setIndexName("主要作物地膜使用回收");
        aa1.setCode(MapSysCodeEnum.AEEM.getCode()+"_gafds");
        aa1.setMapType("二级指标");
        aa1.setList(bb1);
        listMaster.add(aa1);

        AstiiStatisticIndexVo aa3 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb3 = new ArrayList<>();
        AstiiStatisticIndexVo a11 = new AstiiStatisticIndexVo();
        a11.setIndexName("企业个数");
        a11.setMapType("三级指标");
        a11.setFieldName("mulchManufacturer");
        a11.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a11.getFieldName()));
        a11.setList(new ArrayList<>());
        bb3.add(a11);
        aa3.setFieldName("地膜生产企业");
        aa3.setIndexName("地膜生产企业");
        aa3.setCode(MapSysCodeEnum.AEEM.getCode()+"_eeeew");
        aa3.setMapType("二级指标");
        aa3.setList(bb3);
        listMaster.add(aa3);
        AstiiStatisticIndexVo aa4 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb4 = new ArrayList<>();
        AstiiStatisticIndexVo a111 = new AstiiStatisticIndexVo();
        a111.setIndexName("企业个数");
        a111.setMapType("三级指标");
        a111.setFieldName("mulchSalesNetworks");
        a111.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a111.getFieldName()));
        a111.setList(new ArrayList<>());
        bb4.add(a111);
        aa4.setFieldName("地膜销售网点");
        aa4.setIndexName("地膜销售网点");
        aa4.setCode(MapSysCodeEnum.AEEM.getCode()+"_gadcc");
        aa4.setMapType("二级指标");
        aa4.setList(bb4);
        listMaster.add(aa4);
        AstiiStatisticIndexVo aa5 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb5 = new ArrayList<>();
        AstiiStatisticIndexVo a1111 = new AstiiStatisticIndexVo();
        a1111.setIndexName("企业个数");
        a1111.setMapType("三级指标");
        a1111.setFieldName("recycleCompany");
        a1111.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a1111.getFieldName()));
        a1111.setList(new ArrayList<>());
        bb5.add(a1111);
        aa5.setFieldName("回收企业");
        aa5.setIndexName("回收企业");
        aa5.setCode(MapSysCodeEnum.AEEM.getCode()+"_hads");
        aa5.setMapType("二级指标");
        aa5.setList(bb5);
        listMaster.add(aa5);
        AstiiStatisticIndexVo aa6 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb6 = new ArrayList<>();
        AstiiStatisticIndexVo a11111 = new AstiiStatisticIndexVo();
        a11111.setIndexName("企业个数");
        a11111.setMapType("三级指标");
        a11111.setList(new ArrayList<>());
        a11111.setFieldName("recycleNetworksDisplay");
        a11111.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a11111.getFieldName()));
        bb6.add(a11111);
        aa6.setFieldName("回收网点");
        aa6.setIndexName("回收网点");
        aa6.setCode(MapSysCodeEnum.AEEM.getCode()+"_dsgagg");
        aa6.setMapType("二级指标");
        aa6.setList(bb6);
        listMaster.add(aa6);
        AstiiStatisticIndexVo aa7 = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> bb7 = new ArrayList<>();
        AstiiStatisticIndexVo a111111 = new AstiiStatisticIndexVo();
        a111111.setIndexName("企业个数");
        a111111.setMapType("三级指标");
        a111111.setList(new ArrayList<>());
        a111111.setFieldName("reuseCompanyDisplay");
        a111111.setCode(MapSysCodeEnum.AEEM.getCode()+"_"+getLetter(a111111.getFieldName()));
        bb7.add(a111111);
        aa7.setFieldName("再利用企业");
        aa7.setIndexName("再利用企业");
        aa7.setCode(MapSysCodeEnum.AEEM.getCode()+"_caefe");
        aa7.setMapType("二级指标");
        aa7.setList(bb7);
        listMaster.add(aa7);
        masterObj.setFieldName("地膜回收行动");
        masterObj.setIndexName("地膜回收行动");
        masterObj.setCode(MapSysCodeEnum.AEEM.getCode()+"_dsdsd");
        masterObj.setMapType("一级指标");
        masterObj.setList(listMaster);
        List index = new ArrayList<>();
        index.add(masterObj);
        index.add(voMaster);
        return index;
    }

    /**
     * 农作物秸秆资源台账子系统指标数据封装
     * @return
     */
    private static List getDucssIndex(){
        AstiiStatisticIndexVo vo = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> astiiStatisticIndexVos = new ArrayList<>();
        AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
        vo1.setMapType("二级指标");
        vo1.setIndexName("调出量");
        vo1.setFieldName("DCL");
        vo1.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo1.getFieldName()));
        vo1.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo1);
        AstiiStatisticIndexVo vo4 = new AstiiStatisticIndexVo();
        vo4.setMapType("二级指标");
        vo4.setIndexName("可收集量");
        vo4.setFieldName("KSJL");
        vo4.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo4.getFieldName()));
        vo4.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo4);
        AstiiStatisticIndexVo vo5 = new AstiiStatisticIndexVo();
        vo5.setMapType("二级指标");
        vo5.setIndexName("粮食产量");
        vo5.setFieldName("LSCL");
        vo5.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo5.getFieldName()));
        vo5.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo5);
        AstiiStatisticIndexVo vo6 = new AstiiStatisticIndexVo();
        vo6.setMapType("二级指标");
        vo6.setIndexName("产生量");
        vo6.setFieldName("CSL");
        vo6.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo6.getFieldName()));
        vo6.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo6);
        vo.setFieldName("OUTPUT");
        vo.setIndexName("产生量");
        vo.setMapType("一级指标");
        vo.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo.getFieldName()));
        vo.setList(astiiStatisticIndexVos);

        AstiiStatisticIndexVo vot = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> list1 = new ArrayList<>();
        AstiiStatisticIndexVo vo2 = new AstiiStatisticIndexVo();
        vo2.setMapType("二级指标");
        vo2.setIndexName("分散利用量");
        vo2.setFieldName("FSLYL");
        vo2.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo2.getFieldName()));
        vo2.setList(new ArrayList<>());
        list1.add(vo2);
        AstiiStatisticIndexVo vo3 = new AstiiStatisticIndexVo();
        vo3.setMapType("二级指标");
        vo3.setIndexName("市场主体利用量");
        vo3.setFieldName("SCZTLYL");
        vo3.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vo3.getFieldName()));
        vo3.setList(new ArrayList<>());
        list1.add(vo3);
        vot.setFieldName("UTILIZATION");
        vot.setIndexName("利用量");
        vot.setMapType("一级指标");
        vot.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(vot.getFieldName()));
        vot.setList(list1);

        AstiiStatisticIndexVo a = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> b = new ArrayList<>();
        AstiiStatisticIndexVo a1 = new AstiiStatisticIndexVo();
        a1.setMapType("二级指标");
        a1.setIndexName("燃料化利用量");
        a1.setFieldName("RLHLYL");
        a1.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a1.getFieldName()));
        a1.setList(new ArrayList<>());
        b.add(a1);
        AstiiStatisticIndexVo a2 = new AstiiStatisticIndexVo();
        a2.setMapType("二级指标");
        a2.setIndexName("基料化利用量");
        a2.setFieldName("JLHLYL");
        a2.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a2.getFieldName()));
        a2.setList(new ArrayList<>());
        b.add(a2);
        AstiiStatisticIndexVo a3 = new AstiiStatisticIndexVo();
        a3.setMapType("二级指标");
        a3.setIndexName("原料化利用量");
        a3.setFieldName("YLHLYL");
        a3.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a3.getFieldName()));
        a3.setList(new ArrayList<>());
        b.add(a3);
        AstiiStatisticIndexVo a4 = new AstiiStatisticIndexVo();
        a4.setMapType("二级指标");
        a4.setIndexName("饲料化利用量");
        a4.setFieldName("SLHLYL");
        a4.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a4.getFieldName()));
        a4.setList(new ArrayList<>());
        b.add(a4);
        AstiiStatisticIndexVo a5 = new AstiiStatisticIndexVo();
        a5.setMapType("二级指标");
        a5.setIndexName("肥料化利用量");
        a5.setFieldName("FLHLYL");
        a5.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a5.getFieldName()));
        a5.setList(new ArrayList<>());
        b.add(a5);
        a.setFieldName("FIVEMATERIALS");
        a.setIndexName("五料化利用量");
        a.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(a.getFieldName()));
        a.setMapType("一级指标");
        a.setList(b);

        AstiiStatisticIndexVo aa = new AstiiStatisticIndexVo();
        aa.setMapType("一级指标");
        aa.setIndexName("产业化利用能力指数");
        aa.setFieldName("IUCI");
        aa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aa.getFieldName()));
        aa.setList(new ArrayList<>());
        AstiiStatisticIndexVo aaa = new AstiiStatisticIndexVo();
        aaa.setMapType("一级指标");
        aaa.setIndexName("综合利用能力指数");
        aaa.setFieldName("CUCI");
        aaa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aaa.getFieldName()));
        aaa.setList(new ArrayList<>());
        AstiiStatisticIndexVo aaaa = new AstiiStatisticIndexVo();
        aaaa.setMapType("一级指标");
        aaaa.setIndexName("综合利用量");
        aaaa.setFieldName("COMPREHENSIVEUTILIZATION");
        aaaa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aaaa.getFieldName()));
        aaaa.setList(new ArrayList<>());
        AstiiStatisticIndexVo aaaaa = new AstiiStatisticIndexVo();
        aaaaa.setMapType("一级指标");
        aaaaa.setIndexName("综合利用率");
        aaaaa.setList(new ArrayList<>());
        aaaaa.setFieldName("OEE");
        aaaaa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aaaaa.getFieldName()));
        AstiiStatisticIndexVo aaaaaa = new AstiiStatisticIndexVo();
        aaaaaa.setMapType("一级指标");
        aaaaaa.setIndexName("市场主体调入量");
        aaaaaa.setFieldName("CALLIN");
        aaaaaa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aaaaaa.getFieldName()));
        aaaaaa.setList(new ArrayList<>());
        AstiiStatisticIndexVo aaaaaaa = new AstiiStatisticIndexVo();
        aaaaaaa.setMapType("一级指标");
        aaaaaaa.setIndexName("直接还田量");
        aaaaaaa.setFieldName("DIRECTRETURN");
        aaaaaaa.setCode(MapSysCodeEnum.DUCSS.getCode()+"_"+getLetter(aaaaaaa.getFieldName()));
        aaaaaaa.setList(new ArrayList<>());
        List index = new ArrayList<>();
        index.add(vo);
        index.add(vot);
        index.add(a);
        index.add(aa);
        index.add(aaaa);
        index.add(aaa);
        index.add(aaaaa);
        index.add(aaaaaa);
        index.add(aaaaaaa);
        return index;
    }

    /**
     * 氮磷系统指标数据封装
     * @return
     */
    private static List getPcnplIndex(){
        AstiiStatisticIndexVo vo = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> astiiStatisticIndexVos = new ArrayList<>();
        AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
        vo1.setMapType("二级指标");
        vo1.setIndexName("规模种植主体数量");
        vo1.setFieldName("gmzz_size");
        vo1.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo1.getFieldName()));
        vo1.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo1);
        AstiiStatisticIndexVo vo4 = new AstiiStatisticIndexVo();
        vo4.setMapType("二级指标");
        vo4.setIndexName("耕地和园地总面积");
        vo4.setFieldName("gd_yd_size");
        vo4.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo4.getFieldName()));
        vo4.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo4);
        AstiiStatisticIndexVo vo5 = new AstiiStatisticIndexVo();
        vo5.setMapType("二级指标");
        vo5.setIndexName("农村劳动力数量");
        vo5.setList(new ArrayList<>());
        vo5.setFieldName("farm_people");
        vo5.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo5.getFieldName()));
        astiiStatisticIndexVos.add(vo5);
        AstiiStatisticIndexVo vo6 = new AstiiStatisticIndexVo();
        vo6.setMapType("二级指标");
        vo6.setList(new ArrayList<>());
        vo6.setIndexName("农户总数");
        vo6.setFieldName("farm_family");
        vo6.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo6.getFieldName()));
        astiiStatisticIndexVos.add(vo6);
        AstiiStatisticIndexVo vo7 = new AstiiStatisticIndexVo();
        vo7.setMapType("二级指标");
        vo7.setIndexName("不同坡度耕地和园地面积");
        vo7.setList(new ArrayList<>());
        vo7.setFieldName("btpdgyd");
        vo7.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo7.getFieldName()));
        astiiStatisticIndexVos.add(vo7);
        AstiiStatisticIndexVo vo8 = new AstiiStatisticIndexVo();
        vo8.setMapType("二级指标");
        vo8.setIndexName("耕地面积");
        vo8.setFieldName("gd_size");
        vo8.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo8.getFieldName()));
        vo8.setList(new ArrayList<>());
        astiiStatisticIndexVos.add(vo8);
        AstiiStatisticIndexVo vo9 = new AstiiStatisticIndexVo();
        vo9.setMapType("二级指标");
        vo9.setIndexName("菜地面积");
        vo9.setList(new ArrayList<>());
        vo9.setFieldName("cd_size");
        vo9.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo9.getFieldName()));
        astiiStatisticIndexVos.add(vo9);
        AstiiStatisticIndexVo vo10 = new AstiiStatisticIndexVo();
        vo10.setMapType("二级指标");
        vo10.setIndexName("园地面积");
        vo10.setList(new ArrayList<>());
        vo10.setFieldName("yd_size");
        vo10.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo10.getFieldName()));
        astiiStatisticIndexVos.add(vo10);
        AstiiStatisticIndexVo vo11 = new AstiiStatisticIndexVo();
        vo11.setMapType("二级指标");
        vo11.setList(new ArrayList<>());
        vo11.setIndexName("果园面积");
        vo11.setFieldName("gy_size");
        vo11.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo11.getFieldName()));
        astiiStatisticIndexVos.add(vo11);
        AstiiStatisticIndexVo vo12 = new AstiiStatisticIndexVo();
        vo12.setMapType("二级指标");
        vo12.setList(new ArrayList<>());
        vo12.setIndexName("粮食作物播种面积");
        vo12.setFieldName("lszw_size");
        vo12.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo12.getFieldName()));
        astiiStatisticIndexVos.add(vo12);
        AstiiStatisticIndexVo vo13 = new AstiiStatisticIndexVo();
        vo13.setMapType("二级指标");
        vo13.setIndexName("经济作物播种面积");
        vo13.setList(new ArrayList<>());
        vo13.setFieldName("jjzw_size");
        vo13.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo13.getFieldName()));
        astiiStatisticIndexVos.add(vo13);
        AstiiStatisticIndexVo vo14 = new AstiiStatisticIndexVo();
        vo14.setMapType("二级指标");
        vo14.setIndexName("蔬菜瓜果播种面积");
        vo14.setList(new ArrayList<>());
        vo14.setFieldName("scgg_size");
        vo14.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo14.getFieldName()));
        astiiStatisticIndexVos.add(vo14);
        vo.setFieldName("plantIndex");
        vo.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo.getFieldName()));
        vo.setIndexName("种植业基本情况");
        vo.setMapType("一级指标");
        vo.setList(astiiStatisticIndexVos);

        AstiiStatisticIndexVo vot = new AstiiStatisticIndexVo();
        List<AstiiStatisticIndexVo> as = new ArrayList<>();
        AstiiStatisticIndexVo vo2 = new AstiiStatisticIndexVo();
        vo2.setMapType("二级指标");
        vo2.setIndexName("肥料施用含量");
        vo2.setList(new ArrayList<>());
        vo2.setFieldName("flsy");
        vo2.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo2.getFieldName()));
        as.add(vo2);
        AstiiStatisticIndexVo vo3 = new AstiiStatisticIndexVo();
        vo3.setMapType("二级指标");
        vo3.setList(new ArrayList<>());
        vo3.setIndexName("点位分布");
        vo3.setFieldName("dwfb");
        vo3.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vo3.getFieldName()));
        as.add(vo3);
        vot.setFieldName("dikuaiIndex");
        vot.setIndexName("典型地块情况");
        vot.setCode(MapSysCodeEnum.PCNPL.getCode()+"_"+getLetter(vot.getFieldName()));
        vot.setMapType("一级指标");
        vot.setList(as);

        List index = new ArrayList<>();
        index.add(vo);
        index.add(vot);
        return index;
    }

    /**
     * 农业野生植物保护名录管理子系统指标数据封装
     * @param vos
     * @return
     */
    private static List getAgpjzbIndex(List<DropDownVo> vos){

        if(vos==null||vos.size()==0) {
            return new ArrayList();
        }

        List index = new ArrayList<>();
        for (DropDownVo vo : vos) {
            AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
            vo1.setMapType("一级指标");
            vo1.setIndexName(vo.getName());
            vo1.setFieldName(vo.getId());
            vo1.setCode(MapSysCodeEnum.AGPJZB.getCode()+"_"+getLetter(vo1.getFieldName()));
            vo1.setList(new ArrayList<>());
            index.add(vo1);
        }
        return index;
    }

    /**
     * 国控点土壤信息采集子系统指标数据封装
     * @return
     */
    private static List getPmlcscIndex(List<AreaCentre> list){
        if(list==null||list.size()==0) {
            return new ArrayList();
        }
        List index = new ArrayList<>();
        for (AreaCentre vo : list) {
            AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
            vo1.setMapType("区域中心id和地区名获取");
            vo1.setIndexName(vo.getName());
            vo1.setFieldName(vo.getId());
            vo1.setCode(MapSysCodeEnum.PMLCSC.getCode()+"_"+getLetter(vo1.getFieldName()));
            vo1.setList(new ArrayList<>());
            index.add(vo1);
        }
        return index;
    }

    /**
     * 农业体系获取农业资源环境指标数据封装
     * @param list
     * @return
     */
    private static List getAstiiRepaIndex(List<AstiiStatisticIndexVo> list){
         if(list==null||list.size()==0) {
            return new ArrayList();
        }
        return getList(list, 0, 1).getList();
    }
    /**
     * 农业体系获取农村可再生能源指标数据封装
     * @param list
     * @return
     */
    private static List getAstiiRremIndex(List<AstiiStatisticIndexVo> list){
         if(list==null||list.size()==0) {
            return new ArrayList();
        }
        return getList(list, 0, 2).getList();
    }
    /**
     * 农业体系获取全国农业资源环境指标数据封装
     * @param list
     * @return
     */
    private static List getAstiiIndex(List<AstiiStatisticIndexVo> list){
         if(list==null||list.size()==0) {
            return new ArrayList();
        }
        return getList(list, 0, 3).getList();
    }
    /**
     * 农业体系获取全国农村可再生能源指标数据封装
     * @param list
     * @return
     */
    private static List getAstiiMainIndex(List<AstiiStatisticIndexVo> list){
         if(list==null||list.size()==0) {
            return new ArrayList();
        }
        return getList(list, 0, 4).getList();
    }

    /**
     * 沼气系统指标数据封装
     * @return
     */
    private static List getAstcapIndex(List<HashMap<String, Object>> list){
//        List index = new ArrayList<>();
//        for (HashMap<String, Object> vo : list) {
//            AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
//            vo1.setMapType("区域中心id和地区名获取");
//            vo1.setIndexName(vo.get("gdname").toString());
//            vo1.setFieldName(vo.get("gdid").toString());
//            vo1.setCode(MapSysCodeEnum.ASTCAP.getCode()+"_"+getLetter(vo1.getFieldName()));
//            Object configList = vo.get("configList");
//            vo1.setList(new ArrayList<>());
//            index.add(vo1);
//        }
        return list;
    }

    /**
     * 获取字符串中所有字母
     * @return
     */
    private static String getLetter(String a) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for(int i = 0;i<a.length();i++){
            char c = a.charAt(i);
            if((c<='z'&&c>='a')||(c<='Z'&&c>='A')){
                sb.append(c);
            }
        }
//        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * obj转为list
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 返回字母
     * @param a
     * @return
     */
    private static char getWord(int a) {
        char arr[]=new char[52];
        int i;
        for(i=0;i<52;i++){
            if(i<26) {
                arr[i] = (char) ('A' + i);
            }else{
                arr[i]=(char)('a'+(i-26));
            }
        }
        return arr[a];
    }

    /**
     * 判断字符串是否存在字母工具
     * @param str
     * @return
     */
    private static boolean judgeContainsStr(String str) {
        String regex=".*[a-zA-Z]+.*";
        Matcher m= Pattern.compile(regex).matcher(str);
        return m.matches();
    }

    /**
     * 农业体系获取指标数据处理
     * @param list
     * @param a
     * @return
     */
    private static MapClassVo getList(List<AstiiStatisticIndexVo> list,int a,int sysCode){
        List index = new ArrayList<>();
        for (AstiiStatisticIndexVo vo : list) {
            AstiiStatisticIndexVo vo1 = new AstiiStatisticIndexVo();
            vo1.setMapType(vo.getMapType());
            vo1.setIndexName(vo.getIndexName());
            vo1.setFieldName(vo.getFieldName());
            switch (sysCode){
                case 1:
                    if (judgeContainsStr(vo.getFieldName())) {
                        vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_" + getLetter(vo1.getFieldName()));
                    } else {
                        if (a < 52) {
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_" + getWord(a++));
                        } else if (a>=52&&a<104){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_z" + getWord(a++ - 52));
                        } else if (a>=104&&a<156){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_x" + getWord(a++ - 104));
                        } else if (a>=156&&a<208){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_y" + getWord(a++ - 156));
                        }else if (a>=208&&a<264){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_v" + getWord(a++ - 208));
                        }else if (a>=264&&a<320){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_v" + getWord(a++ - 264));
                        }else if (a>=320&&a<357){
                            vo1.setCode(MapSysCodeEnum.ASTII_REPA.getCode() + "_v" + getWord(a++ - 320));
                        } else {
                            throw new SofnException("指标数据过多");
                        }
                    }
                    if (vo.getList() == null || vo.getList().size() == 0) {
                        vo1.setList(new ArrayList<>());
                    } else {
                        MapClassVo mapClassVo = getList(vo.getList(), a, sysCode);
                        vo1.setList(mapClassVo.getList());
                        a=mapClassVo.getCode();
                    }
                    break;

                case 2:
                    if (judgeContainsStr(vo.getFieldName())) {
                        vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_" + getLetter(vo1.getFieldName()));
                    } else {
                        if (a < 52) {
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_" + getWord(a++));
                        } else if (a>=52&&a<104){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_odj" + getWord(a++ - 52));
                        } else if (a>=104&&a<156){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_hj" + getWord(a++ - 104));
                        } else if (a>=156&&a<208){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_kkew" + getWord(a++ - 156));
                        }else if (a>=208&&a<264){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_va" + getWord(a++ - 208));
                        }else if (a>=264&&a<320){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_vwe" + getWord(a++ - 264));
                        }else if (a>=320&&a<357){
                            vo1.setCode(MapSysCodeEnum.ASTII_RREM.getCode() + "_dsv" + getWord(a++ - 320));
                        } else {
                            throw new SofnException("指标数据过多");
                        }
                    }
                    if (vo.getList() == null || vo.getList().size() == 0) {
                        vo1.setList(new ArrayList<>());
                    } else {
                        MapClassVo mapClassVo = getList(vo.getList(), a, sysCode);
                        vo1.setList(mapClassVo.getList());
                        a=mapClassVo.getCode();
                    }
                    break;
                case 3:
                    if (judgeContainsStr(vo.getFieldName())) {
                        vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_" + getLetter(vo1.getFieldName()));
                    } else {
                        if (a < 52) {
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_" + getWord(a++));
                        } else if (a>=52&&a<104){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_z" + getWord(a++ - 52));
                        } else if (a>=104&&a<156){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_x" + getWord(a++ - 104));
                        } else if (a>=156&&a<208){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_ywe" + getWord(a++ - 156));
                        } else if (a>=208&&a<264){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_vf" + getWord(a++ - 208));
                        }else if (a>=264&&a<320){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_va" + getWord(a++ - 264));
                        }else if (a>=320&&a<357){
                            vo1.setCode(MapSysCodeEnum.ASTII.getCode() + "_vds" + getWord(a++ - 320));
                        }else {
                            throw new SofnException("指标数据过多");
                        }
                    }
                    if (vo.getList() == null || vo.getList().size() == 0) {
                        vo1.setList(new ArrayList<>());
                    } else {
                        MapClassVo mapClassVo = getList(vo.getList(), a, sysCode);
                        vo1.setList(mapClassVo.getList());
                        a=mapClassVo.getCode();
                    }
                    break;
                case 4:
                    if (judgeContainsStr(vo.getFieldName())) {
                        vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_" + getLetter(vo1.getFieldName()));
                    } else {
                        if (a < 52) {
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_" + getWord(a++));
                        } else if (a>=52&&a<104){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_z" + getWord(a++ - 52));
                        } else if (a>=104&&a<156){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_x" + getWord(a++ - 104));
                        } else if (a>=156&&a<208){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_y" + getWord(a++ - 156));
                        }else if (a>=208&&a<264){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_vdsd" + getWord(a++ - 208));
                        }else if (a>=264&&a<320){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_vdssd" + getWord(a++ - 264));
                        }else if (a>=320&&a<357){
                            vo1.setCode(MapSysCodeEnum.ASTII_MAIN.getCode() + "_vs" + getWord(a++ - 320));
                        } else {
                            throw new SofnException("指标数据过多");
                        }
                    }
                    if (vo.getList() == null || vo.getList().size() == 0) {
                        vo1.setList(new ArrayList<>());
                    } else {
                        MapClassVo mapClassVo = getList(vo.getList(), a, sysCode);
                        vo1.setList(mapClassVo.getList());
                        a=mapClassVo.getCode();
                    }
                    break;
                default:
                    throw new SofnException("传参错误");
            }
            index.add(vo1);
        }
        MapClassVo mapClassVo = new MapClassVo();
        mapClassVo.setCode(a);
        mapClassVo.setList(index);
        return mapClassVo;
    }
}
