package com.sofn.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sofn.common.exception.SofnException;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.RedisUtils;
import com.sofn.common.utils.UserUtil;
import com.sofn.sys.enums.SysManageEnum;
import com.sofn.sys.mapper.SysResourceMapper;
import com.sofn.sys.mapper.SysSubsystemMapper;
import com.sofn.sys.mapper.SysSystemOrgMapper;
import com.sofn.sys.model.SysSubsystem;
import com.sofn.sys.model.SysSystemOrg;
import com.sofn.sys.service.SysSubsystemService;
import com.sofn.sys.util.SysCacheUtils;
import com.sofn.sys.util.UserHaveAppIdUtil;
import com.sofn.sys.vo.SysResourceForm;
import com.sofn.sys.vo.SysSubsystemForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sofn
 */
@SuppressWarnings({"ALL"})
@Service
@Slf4j
public class SysSubsystemServiceImpl extends ServiceImpl<SysSubsystemMapper, SysSubsystem>
        implements SysSubsystemService {

    @Autowired
    private SysSubsystemMapper sysSubsystemMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysSystemOrgMapper sysSystemOrgMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @RedisDel(key = {PermissionConstant.PERMISSION_SUBSYSTEM_KEY, PermissionConstant.PERMISSION_ORG_KEY})
    public void addSysSubsystem(SysSubsystem sysSubsystem) {
        // 1. ????????????????????????
        checkSysSubsystemIsExists(sysSubsystem.getSubsystemName(), sysSubsystem.getAppId(), null);
        // 2. ???????????????
        sysSubsystem.setId(IdUtil.getUUId());
        sysSubsystem.setDelFlag(SysManageEnum.DEL_FLAG_N.getCode());
        // ?????????ID?????? ???????????????????????? ?????????????????????
        if (sysSubsystem.getParentId().equals(SysManageEnum.SUBSYSTEM_ROOT.getCode()) || "".equals(sysSubsystem.getParentId())) {
            sysSubsystem.setParentIds(SysManageEnum.SUBSYSTEM_ROOT.getCode() + "/");
        } else {
            SysSubsystem subsystem = sysSubsystemMapper.selectById(sysSubsystem.getParentId());
            sysSubsystem.setParentIds(subsystem.makeSelfAsParentIds());
        }
        sysSubsystem.setCreateTime(new Date());
        sysSubsystem.setCreateUserId(UserUtil.getLoginUserId());

        // 3. ??????
        sysSubsystemMapper.insert(sysSubsystem);

        // 4. ??????????????????????????????  ROOT_ORG
        List<SysSystemOrg> sysSystemOrgs = Lists.newArrayList(new SysSystemOrg(IdUtil.getUUId(), sysSubsystem.getAppId(), SysManageEnum.ROOT_ORG.getCode()));
        sysSystemOrgMapper.saveBatch(sysSystemOrgs);
        // ????????????
        deleteRedis(sysSubsystem.getParentId());
        SysCacheUtils.delSubsystemList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @RedisDel(key = {PermissionConstant.PERMISSION_SUBSYSTEM_KEY, PermissionConstant.PERMISSION_ORG_KEY})
    public void updateSysSubsystem(SysSubsystem sysSubsystem) {
        // 1. ????????????????????????
        SysSubsystem selectSysSubsystem = sysSubsystemMapper.selectById(sysSubsystem.getId());
        if (selectSysSubsystem == null || SysManageEnum.DEL_FLAG_Y.getCode().equals(selectSysSubsystem.getDelFlag())) {
            throw new SofnException("????????????????????????");
        }
        if (SysManageEnum.SUBSYSTEM_ROOT.getCode().equals(sysSubsystem.getId())) {
            throw new SofnException("???????????????????????????");
        }

        // ???ID???????????????
        if (sysSubsystem.getId().equals(sysSubsystem.getParentId())) {
            throw new SofnException("????????????????????????");
        }

        SysSubsystem parentSysSubsystem = sysSubsystemMapper.selectById(sysSubsystem.getParentId());
        if(parentSysSubsystem == null){
            throw new SofnException("?????????????????????");
        }

        // ???????????????????????????????????????????????????
        if(!StringUtils.isBlank(selectSysSubsystem.getParentId())){
            if(!selectSysSubsystem.getParentId().equals(sysSubsystem.getParentId())){
                List<SysSubsystem> sysSubsystems = sysSubsystemMapper.selectList(new QueryWrapper<SysSubsystem>()
                        .eq("parent_id", sysSubsystem.getId())
                        .eq("DEL_FLAG", SysManageEnum.DEL_FLAG_N.getCode()));
                if(!CollectionUtils.isEmpty(sysSubsystems)){
                    String oldParentIdsPrefix = selectSysSubsystem.getParentIds() + selectSysSubsystem.getId() + SysManageEnum.TREE_NODE_SPLIT_STR.getCode();
                    String newParentIdsPrefix = parentSysSubsystem.getParentIds() + parentSysSubsystem.getId() +
                            SysManageEnum.TREE_NODE_SPLIT_STR.getCode() +sysSubsystem.getId() +  SysManageEnum.TREE_NODE_SPLIT_STR.getCode()  ;
                    List<String> ids = sysSubsystems.stream().map(SysSubsystem::getId).collect(Collectors.toList());
                    this.updateParentIdsByIds(ids,newParentIdsPrefix,oldParentIdsPrefix);
                }
            }
        }

        // 2. ????????????????????????
        checkSysSubsystemIsExists(sysSubsystem.getSubsystemName(), sysSubsystem.getAppId(), sysSubsystem.getId());
        //????????????AppID,??????????????????????????????????????????appid?????????.
        String appId = sysSubsystem.getAppId();
        if (!appId.equals(selectSysSubsystem.getAppId())) {
           // TODO  :  ??????AppId??????AppId

        }
        // ??????parentIds
        if(SysManageEnum.SUBSYSTEM_ROOT.getCode().equals(sysSubsystem.getParentId())){
            selectSysSubsystem.setParentIds(SysManageEnum.SUBSYSTEM_ROOT.getCode()+ SysManageEnum.TREE_NODE_SPLIT_STR.getCode() );
        }else{
            selectSysSubsystem.setParentIds(parentSysSubsystem.getParentIds() + parentSysSubsystem.getId()+ SysManageEnum.TREE_NODE_SPLIT_STR.getCode() );
        }


        // 3. ????????????
        BeanUtils.copyProperties(sysSubsystem, selectSysSubsystem);
        selectSysSubsystem.setUpdateTime(new Date());
        selectSysSubsystem.setUpdateUserId(UserUtil.getLoginUserId());
        sysSubsystemMapper.updateById(selectSysSubsystem);

        // 4. ??????appId????????????,??????AppId???????????????????????????????????????????????????
        if (!selectSysSubsystem.getAppId().equals(sysSubsystem.getAppId())) {
            List<SysSystemOrg> sysSystemOrgs = Lists.newArrayList(new SysSystemOrg(IdUtil.getUUId(), sysSubsystem.getAppId(), SysManageEnum.ROOT_ORG.getCode()));
            sysSystemOrgMapper.saveBatch(sysSystemOrgs);
        }
        // ????????????
        deleteRedis(selectSysSubsystem.getParentId());
        SysCacheUtils.delSubsystemList();
    }

    @Override
    public void deleteSysSubsystem(String id) {
        SysSubsystem selectSysSubsystem = sysSubsystemMapper.selectById(id);
        // 2. ???????????????????????????????????????
        Map<String, Object> params = Maps.newHashMap();
        params.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());
        params.put("parentId", selectSysSubsystem.getId());
        List<SysSubsystem> sysSubsystemList = sysSubsystemMapper.getSysSubsystemByCondition(params);
        if (sysSubsystemList != null && sysSubsystemList.size() > 0) {
            throw new SofnException("??????????????????????????????????????????");
        }

        //???????????????????????????????????????
        Map<String, Object> params2 = Maps.newHashMap();
        params2.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());
        params2.put("appId", selectSysSubsystem.getAppId());
        List<SysResourceForm> sysResourceList = sysResourceMapper.getSysResourceByCondition(params2);
        if (sysResourceList != null && sysResourceList.size() > 0) {
            throw new SofnException("?????????????????????????????????????????????");
        }
        // ???????????????
        selectSysSubsystem.setDelFlag(SysManageEnum.DEL_FLAG_Y.getCode());
        updateSysSubsystem(selectSysSubsystem);
        SysCacheUtils.delSubsystemList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRedis(String parentId) {
        try {
            // ????????????
            redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_TREE_KEY.getCode());
            redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_KEY.getCode());
            SysCacheUtils.delSubsystemTreeCache();
            // ?????????ID?????????
            redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_KEY.getCode() + "_" + parentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SofnException("Redis??????");
        }
    }

    @Override
    public boolean checkSysSubsystemIsExists(String name, String appId, String id) {
        //??????????????????
        if (id != null) {
            // ??????
            // ????????????????????????
            Integer number = sysSubsystemMapper.getSysSubsystemByNameOrAppId(name, null, id);
            if (number > 0) {
                throw new SofnException("??????????????????");
            }
            // ??????Code????????????
            number = sysSubsystemMapper.getSysSubsystemByNameOrAppId(null, appId, id);
            if (number > 0) {
                throw new SofnException("??????APPID??????");
            }
        } else {
            // ??????
            // ????????????????????????
            Integer number = sysSubsystemMapper.getSysSubsystemByNameOrAppId(name, null, null);
            if (number > 0) {
                throw new SofnException("??????????????????");
            }
            // ??????Code????????????
            number = sysSubsystemMapper.getSysSubsystemByNameOrAppId(null, appId, null);
            if (number > 0) {
                throw new SofnException("??????APPID??????");
            }
        }
        return false;
    }

    @Override
    public PageUtils<SysSubsystemForm> getSysSubsystemByContion(Map<String, Object> paramas, Integer pageNo, Integer pageSize) {
        PageHelper.offsetPage(pageNo, pageSize);
        List<SysSubsystem> sysSubsystemList = sysSubsystemMapper.getSysSubsystemByCondition(paramas);
        List<SysSubsystemForm> subsystemFormList = Lists.newArrayList();
        getSysSubsystemFormList(sysSubsystemList, subsystemFormList, sysSubsystemMapper);
        PageInfo<SysSubsystemForm> pageInfo = new PageInfo<>(subsystemFormList);
        return PageUtils.getPageUtils(pageInfo);
    }

    static void getSysSubsystemFormList(List<SysSubsystem> sysSubsystemList, List<SysSubsystemForm> subsystemFormList, SysSubsystemMapper sysSubsystemMapper) {
        for (SysSubsystem sysSubsystem : sysSubsystemList) {
            //??????????????????
            SysSubsystemForm sysSubsystemForm = new SysSubsystemForm(sysSubsystem);
            SysSubsystem sysSubsystemParent = sysSubsystemMapper.selectById(sysSubsystem.getParentId());
            if (sysSubsystem.getParentId().equals(SysManageEnum.SUBSYSTEM_ROOT.getCode())) {
                sysSubsystemForm.setParentName(SysManageEnum.SUBSYSTEM_ROOT.getDescription());
            } else {
                sysSubsystemForm.setParentName(sysSubsystemParent.getSubsystemName());
            }
            subsystemFormList.add(sysSubsystemForm);
        }
    }

    @Override
    public SysSubsystemForm getSysSubsystemTree(String name) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());
        params.put("subsystemName", name);
        List<SysSubsystem> sysSubsystemList = sysSubsystemMapper.getSysSubsystemByCondition(params);
        if (CollectionUtils.isEmpty(sysSubsystemList)) {
            return null;
        }
        // ???????????????????????????????????????????????????
        List<SysSubsystemForm> treeSysSubsystemList = tree(sysSubsystemList, SysManageEnum.SUBSYSTEM_ROOT.getCode());
        SysSubsystem systemRoot = new SysSubsystem();
        systemRoot.setId(SysManageEnum.SUBSYSTEM_ROOT.getCode());
        systemRoot.setSubsystemName(SysManageEnum.SUBSYSTEM_ROOT.getDescription());
        SysSubsystemForm systemRootForm = new SysSubsystemForm(systemRoot, UserHaveAppIdUtil.getValue());
        systemRootForm.setChildList(treeSysSubsystemList);
        SysCacheUtils.cacheSubsystemTree(treeSysSubsystemList);
        return systemRootForm;
    }

    private List<SysSubsystemForm> getSubsystemTree(List<SysSubsystem> sysSubsystemList) {
        Set<SysSubsystem> ssfSet = new HashSet<>();
        for (SysSubsystem ss : sysSubsystemList) {
            if (ss == null || StringUtils.isBlank(ss.getParentIds())) {
                continue;
            }
            // ???????????????
            String[] ids = ss.getParentIds().split("/");
            List<SysSubsystem> ssfList = new ArrayList<>();
            for (String id : ids) {
                if (!id.equals(SysManageEnum.SUBSYSTEM_ROOT.getCode())) {
                    SysSubsystem ssys = sysSubsystemMapper.selectById(id);
                    ssfList.add(ssys);
                }
            }
            ssfList.add(ss);
            ssfSet.addAll(ssfList);
        }

        List<SysSubsystem> ssfList2 = new ArrayList<>(ssfSet);
        return tree(ssfList2, SysManageEnum.SUBSYSTEM_ROOT.getCode());
    }

    @Override
    public SysSubsystemForm getSysSubsystemTreeByUser() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());
        List<String> roleIdList = UserUtil.getLoginUserRoleIdList();
        //??????????????????????????????
        List<SysSubsystem> allSubsystemList;
        if (CollectionUtils.isEmpty(roleIdList)) {
            throw new SofnException("???????????????????????????");
        }
        if (roleIdList.contains(SysManageEnum.DEVELOPER_ROLE_ID.getCode())) {
            System.out.println("you are admin,have all permission");
            allSubsystemList = sysSubsystemMapper.getSysSubsystemByCondition(params);
        } else {
            allSubsystemList = sysSubsystemMapper.getSysSubsystemByRoleId(roleIdList);
        }
        List<SysSubsystemForm> treeSysSubsystemList = getSubsystemTree(allSubsystemList);
        SysSubsystem systemRooot = new SysSubsystem();
        systemRooot.setId(SysManageEnum.SUBSYSTEM_ROOT.getCode());
        systemRooot.setSubsystemName(SysManageEnum.SUBSYSTEM_ROOT.getDescription());
        SysSubsystemForm systemRoootForm = new SysSubsystemForm(systemRooot);
        systemRoootForm.setChildList(treeSysSubsystemList);
        return systemRoootForm;
    }

    private List<SysSubsystemForm> tree(List<SysSubsystem> allList, String treeRoot) {
        // ???????????????
        List<SysSubsystemForm> resultList = new ArrayList<>();
        for (SysSubsystem sysSubsystem : allList) {
            if (sysSubsystem != null) {
                SysSubsystemForm sysSubsystemForm = new SysSubsystemForm(sysSubsystem, UserHaveAppIdUtil.getValue());
                if (sysSubsystem.getParentId().equals(treeRoot)) {
                    resultList.add(sysSubsystemForm);
                }
            }

        }
        // ????????????
        Optional<SysSubsystem> first = allList.stream().filter(item -> item.getId().equals(SysManageEnum.SUBSYSTEM_ROOT.getCode())).findFirst();
        SysSubsystemForm sysSubsystemForm = null;
        if (first.isPresent()) {
            sysSubsystemForm = new SysSubsystemForm(first.get());
        }
        if (sysSubsystemForm == null) {
            throw new SofnException("????????????????????????");
        }
        for (SysSubsystemForm sysSubsystemForm2 : resultList) {
            if (sysSubsystemForm2.getId().equals(treeRoot)) {
                continue;
            }
            getSublist(sysSubsystemForm2, allList);
        }

        sysSubsystemForm.setChildList(resultList);
        return Lists.newArrayList(sysSubsystemForm);
    }

    private void getSublist(SysSubsystemForm sysSubsystemForm, List<SysSubsystem> allList) {
        // ????????????

        List<SysSubsystemForm> sonSubsystems = allList.stream()
                .filter(item -> item.getParentId().equals(sysSubsystemForm.getId()))
                .map(item -> new SysSubsystemForm(item, UserHaveAppIdUtil.getValue()))
                .collect(Collectors.toList());


        if (!CollectionUtils.isEmpty(sonSubsystems)) {
            for (SysSubsystemForm child : sonSubsystems) {
                if (child != null) {
                    getSublist(child, allList);
                }
            }
        }
        sysSubsystemForm.setChildList(sonSubsystems);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @RedisDel(key = {PermissionConstant.PERMISSION_SUBSYSTEM_KEY})
    public void batchDeleteSubsystem(List<String> ids) {
        String msg;
        if (!CollectionUtils.isEmpty(ids)) {
            // ???ID ??????????????????
            Set<String> parentIds = Sets.newHashSet();
            List<String> subsystemNames = new ArrayList<>();
            // ??????????????????????????????
            ids.forEach((id) -> {
                Map<String, Object> params = Maps.newHashMap();
                params.put("parentId", id);
                params.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());
                SysSubsystem sysSubsystem = sysSubsystemMapper.selectById(id);
                List<SysSubsystem> sysSubsystemList = sysSubsystemMapper.getSysSubsystemByCondition(params);
                if (sysSubsystemList != null && sysSubsystemList.size() > 0) {
                    subsystemNames.add(sysSubsystem.getSubsystemName());
                }
                if (sysSubsystem != null) {
                    parentIds.add(sysSubsystem.getParentId());
                }
            });
            if (subsystemNames.size() > 0) {
                msg = "??????[" + String.join(",", subsystemNames) + "]??????????????????????????????";
                throw new SofnException(msg);
            } else {
                sysSubsystemMapper.batchDelete(ids, UserUtil.getLoginUserId(), new Date());
                try {
                    redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_KEY.getCode());
                    // ?????????ID?????????
                    if (!CollectionUtils.isEmpty(parentIds)) {
                        parentIds.forEach((str) -> {
                            redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_KEY.getCode() + "_" + str);
                            redisUtils.delete(SysManageEnum.SYS_SUBSYSTEM_CACHE_TREE_KEY.getCode());
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SofnException("??????????????????????????????????????????");
                }
            }

            SysCacheUtils.delSubsystemList();
        }
    }

    @Override
    public List<SysSubsystem> getSubSystemByIds(String[] ids) {
        return sysSubsystemMapper.selectSubsystemByIds(ids);
    }

    @Override
    public List<String> getAllAppidList() {
        return sysSubsystemMapper.selectAllAppIdList();
    }

    @Override
    public List<SysSubsystem> getSysSubsystemTreeById(String parentId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentId", parentId);
        params.put("delFlag", SysManageEnum.DEL_FLAG_N.getCode());

        return sysSubsystemMapper.getSysSubsystemByCondition(params);
    }

    @Override
    public SysSubsystem getSysSubsystemAppId(String appId) {

        return sysSubsystemMapper.getSysSubsystemAppId(appId);
    }

    @Override
    public SysSubsystem getSubsystemByIdOrAppId(String idOrAppId) {

        if (!StringUtils.isEmpty(idOrAppId)) {
            List<SysSubsystem> sysSubsystems = sysSubsystemMapper.selectList(new QueryWrapper<SysSubsystem>().eq("ID", idOrAppId).or().eq("APP_ID", idOrAppId));
            if (!CollectionUtils.isEmpty(sysSubsystems)) {
                return sysSubsystems.get(0);
            }
        }
        return null;
    }

    @Override
    public List<SysSubsystemForm> getSubsystemByType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new SofnException("??????????????????");
        }
        List<SysSubsystem> subsystems = sysSubsystemMapper.selectList(new QueryWrapper<SysSubsystem>().eq("SYSTEM_TYPE", type));
        return getSysSubsystemForms(subsystems);
    }

    @Override
    public void updateParentIdsByIds(List<String> ids, String newParentIdPrefix, String oldParentIdPrefix) {
        if(CollectionUtils.isEmpty(ids)){
            return;
        }

        if(StringUtils.isBlank(newParentIdPrefix) || StringUtils.isBlank(oldParentIdPrefix)){
            return;
        }
        sysSubsystemMapper.updateParentIdsByIds(ids,newParentIdPrefix,oldParentIdPrefix);
        SysCacheUtils.delSubsystemList();
    }

    @Override
    public List<SysSubsystem> getSubsystemListByIds(Set<String> ids) {
        Map<String,SysSubsystem> map = SysCacheUtils.getSubSubsystemList(ids);
        if (CollectionUtils.isEmpty(map)){
            getAllSubsystemList();
            map = SysCacheUtils.getSubSubsystemList(ids);
        }

        List<SysSubsystem> subsystemList = null;
        if (CollectionUtils.isEmpty(map)){
            subsystemList = map.values().stream().collect(Collectors.toList());
        }

        return subsystemList;
    }

    @Override
    public List<SysSubsystem> getAllSubsystemList() {
        Map<String,SysSubsystem> map = SysCacheUtils.getSubsystemList();
        List<SysSubsystem> list = null;
        if (CollectionUtils.isEmpty(map)) {
            list = sysSubsystemMapper.selectList(new QueryWrapper<SysSubsystem>()
                .eq("DEL_FLAG", SysManageEnum.DEL_FLAG_N.getCode()));
            SysCacheUtils.cacheSubsystemListById(list);
        }else {
            list = map.values().stream().collect(Collectors.toList());
        }

        return list;
    }

    @Nullable
    public static List<SysSubsystemForm> getSysSubsystemForms(List<SysSubsystem> subsystems) {
        if (!CollectionUtils.isEmpty(subsystems)) {
            List<SysSubsystem> dontDeleteInfo = subsystems.stream()
                    .filter(item -> !SysManageEnum.DEL_FLAG_Y.getCode().equals(item.getDelFlag()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(dontDeleteInfo)) {
                return dontDeleteInfo.stream().map(SysSubsystemForm::new).collect(Collectors.toList());
            }
        }
        return null;
    }

}
