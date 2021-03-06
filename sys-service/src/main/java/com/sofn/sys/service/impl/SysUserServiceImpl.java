package com.sofn.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sofn.common.constants.Constants;
import com.sofn.common.exception.ExceptionType;
import com.sofn.common.exception.SofnException;
import com.sofn.common.utils.*;
import com.sofn.common.utils.shiro.ShiroUtils;
import com.sofn.sys.enums.SysManageEnum;
import com.sofn.sys.mapper.*;
import com.sofn.sys.model.*;
import com.sofn.sys.service.*;
import com.sofn.sys.service.permission.PermissionRoleService;
import com.sofn.sys.service.permission.PermissionSubSystemService;
import com.sofn.sys.service.permission.PermissionSuperAdminService;
import com.sofn.sys.util.SysCacheUtils;
import com.sofn.sys.vo.*;
import com.sofn.sys.vo.Excel.ImportSysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author quzj
 */
@SuppressWarnings("ALL")
@Slf4j
@Service(value = "sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;
    @Autowired
    private SysUserSystemMapper userSystemMapper;

    @Autowired
    private SysRoleGroupMapper sysRoleGroupMapper;

    @Autowired
    private SysRoleRoleGroupMapper sysRoleRoleGroupMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PermissionSubSystemService permissionSubSystemService;

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private SysOrgAgentService sysOrgAgentService;

    @Autowired
    private SysRegionService sysRegionService;

    @Autowired
    private SysOrgService sysOrgService;

    @Autowired
    private PermissionRoleService permissionRoleService;

    @Autowired
    private PermissionSuperAdminService permissionSuperAdminService;

    @Autowired
    private SysSubsystemService sysSubsystemService;

  /**
   * ????????????????????????
   * @param username ?????????
   * @return SysUser
   */
    @Override
    public SysUser findByUserName(String username) {
        List<SysUser> sysUsers = this.baseMapper.selectList(new QueryWrapper<SysUser>().eq("username", username));
        if(CollectionUtils.isEmpty(sysUsers)){
           return null;
        }

        List<SysUser> collect = sysUsers.stream().filter(item -> !BoolUtils.Y.equals(item.getDelFlag())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)){
            return null;
        }

        return collect.get(0);
    }

    @Override
    public Auth execuLogin(LoginVo loginVo,boolean isApp,String type) {
        // ????????????
        // 1.?????????????????? ????????????app???????????????????????????
        if(!isApp){
            if (StringUtils.isBlank(loginVo.getCaptchaId()) || StringUtils.isBlank(loginVo.getCaptcha())) {
                throw new SofnException(ExceptionType.CAPTCHA_CORRECT);
            }

            // ??????redis?????????????????????
            String cacheCaptcha = SysCacheUtils.getCaptchaById(loginVo.getCaptchaId());

            // ???????????????????????????
            if (!StringUtils.equalsIgnoreCase(loginVo.getCaptcha(), cacheCaptcha)) {
                // TODO ?????????sofn ????????????????????????????????? ???????????????
                cacheCaptcha = "sofn";
                if (!StringUtils.equalsIgnoreCase(loginVo.getCaptcha(), cacheCaptcha)) {
                    throw new SofnException(ExceptionType.CAPTCHA_CORRECT);
                }
            }
        }

        // 2. ????????????
        SysUser user = this.findByUserName(loginVo.getUsername());
        if (user == null) {
            throw new SofnException(ExceptionType.USER_NOT_EXIST);
        }

        if(!SysManageEnum.STATUS_1.getCode().equals(user.getStatus())){
            throw new SofnException("??????????????????(???????????????" + SysManageEnum.getEnumDes("STATUS",user.getStatus()) + ")");
        }

        // 3. ????????????
        String saltPassword = ShiroUtils.sha256(loginVo.getPassword(),user.getSalt());
        if (!StringUtils.equals(saltPassword, user.getPassword())){
            throw new SofnException(ExceptionType.PASSWORD_ERROR);
        }

        // 4. ??????????????????????????????
        SysUserForm sysUserForm = new SysUserForm();
        SysOrganization org = null;
        if (StringUtils.isNotBlank(user.getOrganizationId())){
          SysOrg sysOrg = sysOrgService.getOrgById(user.getOrganizationId());
          if(sysOrg == null){
            throw new SofnException("???????????????????????????????????????");
          }

          sysUserForm.setRegionCode(sysOrg.getRegionLastCode());
          org = new SysOrganization();
          sysUserForm.setRegionCode(sysOrg.isThirdOrganization()?sysOrg.getAddressLastCode():sysOrg.getRegionLastCode());
          List<String> regionList = sysRegionService.getFormatIdsOrNamesByCode(sysUserForm.getRegionCode(), "NAME");
          if (!CollectionUtils.isEmpty(regionList)){
            sysUserForm.setRegionNames(StringUtils.join(regionList.toArray()));
          }
          BeanUtils.copyProperties(sysOrg,org);
        }else {
          throw new SofnException("?????????????????????????????????");
        }

        BeanUtils.copyProperties(user,sysUserForm);

        // 5. ??????????????????????????????
        Set<String> roleList = new HashSet<>();
        Set<String> roleCodeList = new HashSet<>();
        Set<String> subsystemIds = Sets.newHashSet();

        List<SysRole> roles =loadRolesByUserId(user.getId());
        if(CollectionUtils.isEmpty(roles)){
            throw new SofnException("??????????????????????????????");
        }
        roles.forEach(sysRole -> {
            if (StringUtils.isNotBlank(sysRole.getRoleName())){
                roleList.add(sysRole.getRoleName());
            }

            if (StringUtils.isNotBlank(sysRole.getRoleCode())){
                roleCodeList.add(sysRole.getRoleCode());
            }

            if(StringUtils.isNotBlank(sysRole.getSubsystemId())){
                subsystemIds.add(sysRole.getSubsystemId());
            }
        });

        sysUserForm.setOrganizationName(org.getOrganizationName());
        sysUserForm.setRoleNames(StringUtils.join(roleList,","));
        sysUserForm.setRoleCodes(roleCodeList);

        // ????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????
        List<SysSubsystem> list;
        if(roleCodeList.contains(SysManageEnum.DEVELOPER_ROLE_CODE.getCode())){
            list = sysSubsystemService.getAllSubsystemList();
        }else{
            list = sysSubsystemService.getSubsystemListByIds(subsystemIds);
        }

        Set<String> realCanShowSubsystemId = Sets.newHashSet();
        Set<String> appIds = Sets.newHashSet();
        if(!CollectionUtils.isEmpty(subsystemIds)){
            if(!CollectionUtils.isEmpty(list)){
                list.forEach(item->{
                    realCanShowSubsystemId.add(item.getId());
                    appIds.add(item.getAppId());
                });
            }
        }

        // ??????token
        String token = "";
        if(isApp){
            token = JwtUtils.generateToken(loginVo.getUsername());
        }else{
            token = JwtUtils.generateToken(loginVo.getUsername(),type);
        }

        // ??????????????????
        SysCacheUtils.cacheUserInfo(token, sysUserForm, roles, loginVo, org);
        SysCacheUtils.cacheUserCanShowSystem(token,realCanShowSubsystemId, appIds, sysUserForm.getUsername() );
        return new Auth(token, sysUserForm);
    }


    @Override
    public PageUtils<SysUserForm> findAllUserList(Map<String, Object> params, Integer pageNo, Integer pageSize) {

        PageHelper.offsetPage(pageNo, pageSize);
        List<SysUser> sysUsers = sysUserMapper.getSysUserByCondition(params);
        PageInfo<SysUser> sysUserPageInfo = new PageInfo<SysUser>(sysUsers);
        PageInfo<SysUserForm> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(sysUserPageInfo, pageInfo);
        List<SysUserForm> userForm = getUserForm(sysUsers, pageInfo);
        pageInfo.setList(userForm);


        return PageUtils.getPageUtils(pageInfo);
    }

    /**
     * ???SysUsers??????SysUserForms
     * @param sysUsers
     * @param pageInfo
     * @return
     */
    private List<SysUserForm> getUserForm(List<SysUser> sysUsers, PageInfo<SysUserForm> pageInfo) {
        if (!CollectionUtils.isEmpty(sysUsers)) {
            log.info("?????????????????????????????????");
            List<String> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            List<String> orgIds = sysUsers.stream().map(SysUser::getOrganizationId).collect(Collectors.toList());
            // ??????????????????
            Map<String,Object> queryParams = Maps.newHashMap();
            queryParams.put("ids",orgIds);
            List<SysOrg> sysOrgs = sysOrgService.getInfoByCondition(queryParams);
            if (CollectionUtils.isEmpty(sysOrgs)) {
                log.warn("??????????????????????????????????????????????????????{}???", sysUsers);
            } else {
                sysOrgs = sysOrgs.stream().filter(sysOrganization -> !"Y".equals(sysOrganization.getDelFlag())).collect(Collectors.toList());
            }
            // ??????????????????????????????
            List<SysUserRole> sysUserHasRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().in("USER_ID", userIds));
            List<SysUserGroup> sysUserHasGroups = sysUserGroupMapper.selectList(new QueryWrapper<SysUserGroup>().in("USER_ID", userIds));
            // ????????????????????????????????????
            List<SysRole> sysRoles = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(sysUserHasRoleList)) {
                List<String> roleIds = sysUserHasRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                sysRoles = sysRoleMapper.selectList(
                        new QueryWrapper<SysRole>().in("ID", roleIds)
                        .ne("DEL_FLAG", "Y")
                        .eq("status",SysManageEnum.STATUS_1.getCode())
                );
            }
            List<SysRoleGroup> sysRoleGroups = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(sysUserHasGroups)) {
                List<String> groupIds = sysUserHasGroups.stream().map(SysUserGroup::getGroupId).collect(Collectors.toList());
                sysRoleGroups = sysRoleGroupMapper.selectList(new QueryWrapper<SysRoleGroup>().in("ID", groupIds).ne("DEL_FLAG", "Y"));
            }

            // ????????????
            List<SysUserForm> sysUserFormList = new ArrayList<>();
            List<SysRole> finalSysRoles = sysRoles;
            List<SysRoleGroup> finalSysRoleGroups = sysRoleGroups;
            List<SysOrgVo> finalSysOrganizations = sysOrgService.getVoList(sysOrgs);
            sysUsers.forEach(sysUser -> {
                if (sysUser != null) {
                    SysUserForm sysUserForm = new SysUserForm();
                    BeanUtils.copyProperties(sysUser, sysUserForm);
                    // roleIds    groupIds   roleNames   roleCodes    resourceList
                    // ?????????????????????????????????
                    String orgId = sysUser.getOrganizationId();
                    if (StringUtils.isBlank(orgId)) {
                        log.error("???????????????????????????????????????{}???", sysUser);
                        throw new SofnException(String.format("??????%d??????????????????", sysUser.getId()));
                    }
                    if (CollectionUtils.isEmpty(finalSysOrganizations)) {
                        sysUserForm.setOrganizationName("");
                    } else {
                        Optional<SysOrgVo> first = finalSysOrganizations.stream().filter(sysOrganization -> sysOrganization.getId().equals(orgId)).findFirst();
                        if (first.isPresent()) {
                            sysUserForm.setOrganizationName(first.get().getOrganizationName());
                            // ??????????????????
                            first.get().getRegionFullCode();
                            sysUserForm.setRegionCode(first.get().getRegionFullCode()  == null ? "" : IdUtil.getStrIdsByList(first.get().getRegionFullCode()));
                            sysUserForm.setRegionNames(first.get().getRegionFullName() == null ? "" : IdUtil.getStrIdsByList(first.get().getRegionFullName()));
                        } else {
                            log.warn("?????????{}????????????????????????????????????ID??????{}???",sysUser.getUsername(), sysUser.getOrganizationId());
                            sysUserForm.setOrganizationName("");
                        }
                    }
                    // ??????????????????
                    List<String> roleIds = sysUserHasRoleList.stream().filter(sysUserRole -> sysUserRole.getUserId().equals(sysUser.getId()))
                            .collect(Collectors.toList())
                            .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                    List<SysRole> sysRoles1 = finalSysRoles.stream().filter(sysRole -> roleIds.contains(sysRole.getId())).collect(Collectors.toList());
                    List<String> setRoleIds = Lists.newArrayList();
                    List<String> setRoleNames = Lists.newArrayList();
                    List<String> setRoleCodes = Lists.newArrayList();
                    if (!CollectionUtils.isEmpty(sysRoles1)) {
                        sysRoles1.forEach(sysRolesBean -> {
                            setRoleIds.add(sysRolesBean.getId());
                            setRoleNames.add(sysRolesBean.getRoleName());
                            setRoleCodes.add(sysRolesBean.getRoleCode());
                        });
                    }
                    sysUserForm.setRoleIds(IdUtil.getStrIdsByList(setRoleIds));
                    sysUserForm.setRoleNames(IdUtil.getStrIdsByList(setRoleNames));
                    // ?????????????????????
                    if (!CollectionUtils.isEmpty(sysUserHasGroups)) {
                        List<String> groupIds = sysUserHasGroups.stream()
                                .filter(sysUserGroup -> sysUserGroup.getUserId().equals(sysUser.getId()))
                                .collect(Collectors.toList())
                                .stream().map(SysUserGroup::getGroupId)
                                .collect(Collectors.toList());
                        List<SysRoleGroup> sysRoleGroupList = finalSysRoleGroups.stream().filter(sysRoleGroup -> groupIds.contains(sysRoleGroup.getId())).collect(Collectors.toList());
                        List<String> roleGroupList = sysRoleGroupList.stream().map(SysRoleGroup::getId).collect(Collectors.toList());
                        sysUserForm.setGroupIds(IdUtil.getStrIdsByList(roleGroupList));
                    }
                    sysUserFormList.add(sysUserForm);
                }
            });
            log.info("?????????????????????????????????");
            return sysUserFormList;
        }
        return Lists.newArrayList();
    }

    /**
     * ????????????: <br>
     * ???????????????ID??????????????????????????????ID??????>
     *
     * @Param: [sysUserForm]
     * @Return: void
     * @Author: luoyh
     * @Date: 2019/10/10/010 16:40
     */
    public void getRoleForm(SysUserForm sysUserForm) {
        //??????userRole?????????roleId??????
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.getUserRoleByUserId(sysUserForm.getId());
        List<String> roleIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sysUserRoles)) {
            sysUserRoles.forEach((sysUserRole -> {
                if (sysUserRole != null) {
                    roleIds.add(sysUserRole.getRoleId());
                }
            }));
        }
        //??????UserGroup?????????roleId??????
        List<SysUserGroup> ugList = sysUserGroupMapper.getUserGroupsByUserId(sysUserForm.getId());
        List<String> idsList = new ArrayList<>();
        if (ugList.size() > 0) {
            for (SysUserGroup sug : ugList) {
                List<SysRoleRoleGroup> rrpList = sysRoleRoleGroupMapper.getListByGroupId(sug.getGroupId());
                if (rrpList.size() > 0) {
                    for (SysRoleRoleGroup sysRoleRoleGroup : rrpList) {
                        idsList.add(sysRoleRoleGroup.getRoleId());
                    }
                }
            }
        }
        roleIds.remove(idsList);
        roleIds.addAll(idsList);

        // ??????
        Set<String> roleIdSets = Sets.newHashSet(roleIds);

        List<String> roleNameList = new ArrayList<>();
        List<String> roleIdList = new ArrayList<>();
        if (roleIdSets != null && roleIdSets.size() > 0) {
            for (String roleId : roleIdSets) {
                SysRole role = sysRoleMapper.selectById(roleId);
                if (role != null) {
                    roleNameList.add(role.getRoleName());
                    roleIdList.add(roleId);
                }
            }
            sysUserForm.setRoleIds(StringUtils.join(roleIdList, ","));
            sysUserForm.setRoleNames(StringUtils.join(roleNameList, ","));
        }
    }

  public void getUserSystemList(String userId) {
    //??????userRole?????????roleId??????
    List<SysUserRole> sysUserRoles = sysUserRoleMapper.getUserRoleByUserId(userId);
    List<String> roleIds = new ArrayList<>();
    if (!CollectionUtils.isEmpty(sysUserRoles)) {
      sysUserRoles.forEach((sysUserRole -> {
        if (sysUserRole != null) {
          roleIds.add(sysUserRole.getRoleId());
        }
      }));
    }

    //??????UserGroup?????????roleId??????
    List<SysUserGroup> ugList = sysUserGroupMapper.getUserGroupsByUserId(userId);
    List<String> idsList = new ArrayList<>();
    if (ugList.size() > 0) {
      for (SysUserGroup sug : ugList) {
        List<SysRoleRoleGroup> rrpList = sysRoleRoleGroupMapper.getListByGroupId(sug.getGroupId());
        if (rrpList.size() > 0) {
          for (SysRoleRoleGroup sysRoleRoleGroup : rrpList) {
            idsList.add(sysRoleRoleGroup.getRoleId());
          }
        }
      }
    }
    roleIds.remove(idsList);
    roleIds.addAll(idsList);

    // ??????
    Set<String> roleIdSets = Sets.newHashSet(roleIds);

    List<String> roleNameList = new ArrayList<>();
    List<String> roleIdList = new ArrayList<>();
    if (roleIdSets != null && roleIdSets.size() > 0) {
      for (String roleId : roleIdSets) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role != null) {
          roleNameList.add(role.getRoleName());
          roleIdList.add(roleId);
        }
      }
    }
  }

    @Override
    public PageUtils<SysUserForm> findUserListBySubsystem(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        PageHelper.offsetPage(pageNo, pageSize);
        // ??????????????????????????????????????????????????????????????????????????????
        String proxyUser = (String) params.get("proxyUser");
        if (BoolUtils.Y.equals(proxyUser)) {
            String organizationLevel = (String) params.get("organizationLevel");
            Map<String,Object> queryParams = Maps.newHashMap();
            queryParams.put("thirdOrg",organizationLevel);
            List<SysOrg> orgInfoByOrgLevel = sysOrgService.getInfoByCondition(queryParams);
            List<String> orgIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(orgInfoByOrgLevel)) {
                orgIds = orgInfoByOrgLevel.stream().map(SysOrg::getId).collect(Collectors.toList());
            }
            // ??????????????????
            String subsystemId = (String) params.get("subsystemId");
            if (StringUtils.isBlank(subsystemId)) {
                // ?????????????????????????????????????????????
                subsystemId = SysManageEnum.SUBSYSTEM_ROOT.getCode();
            }
            // ???????????????????????????ID
            List<String> proxyOrgIds = sysOrgAgentService.selectOrgAgentByOrgId(Lists.newArrayList(subsystemId), orgIds, "");
            params.put("proxyOrgs", proxyOrgIds);
        }
        List<SysUser> sysUsers = sysUserMapper.getUserListBySubsystem(params);
        PageInfo<SysUser> sysUserPageInfo = new PageInfo<SysUser>(sysUsers);
        List<SysUserForm> sysUserFormList = new ArrayList<>();
        PageInfo<SysUserForm> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(sysUserPageInfo, pageInfo);
        for (SysUser sysUser : sysUsers) {
            SysUserForm sysUserForm = new SysUserForm();
            sysUserForm.setSex(sysUser.getSex());
            BeanUtils.copyProperties(sysUser, sysUserForm);
            getRoleForm(sysUserForm);
            // ????????????
            Map<String,Object> queryParams = Maps.newHashMap();
            queryParams.put("id",sysUser.getOrganizationId());
            List<SysOrg> orgInfoByOrgLevel = sysOrgService.getInfoByCondition(queryParams);
            if(!CollectionUtils.isEmpty(orgInfoByOrgLevel)){
                SysOrg sysOrganization = orgInfoByOrgLevel.get(0);
                if (sysOrganization != null && StringUtils.isNotBlank(sysOrganization.getOrganizationName())) {
                    sysUserForm.setOrganizationName(sysOrganization.getOrganizationName());
                }
            }



            sysUserFormList.add(sysUserForm);
        }
        pageInfo.setList(sysUserFormList);

        return PageUtils.getPageUtils(pageInfo);
    }

    private String getRoleNames(List<SysRole> roleList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SysRole sysRole : roleList) {
            stringBuilder.append(sysRole.getRoleName() + ",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveSysUser(SysUser sysUser) {
        // ????????????
        //1.????????????????????????
        checkExists(sysUser);

        //2. ???????????????
        sysUser.setId(IdUtil.getUUId());
        sysUser.setCreateTime(new Date());
        sysUser.setCreateUserId(UserUtil.getLoginUserId());
        sysUser.setStatus(SysManageEnum.STATUS_1.getCode());
        sysUser.setDelFlag(SysManageEnum.DEL_FLAG_N.getCode());

        // ?????????????????????
        String initPassword = sysUser.getInitPassword();
        if (StringUtils.isEmpty(initPassword)) {
            initPassword = IdUtil.getSixNumCode();
        }
        String salt = IdUtil.getUUId();
        String pdMd5 = DigestUtils.md5DigestAsHex(initPassword.getBytes());
        String password = ShiroUtils.sha256(pdMd5, salt);
        sysUser.setPassword(password);
        sysUser.setSalt(salt);

        this.save(sysUser);
        //3.???????????????????????????
        List<SysUserRole> sysUserRoles = getSysUserRoleRelations(sysUser);
        if (null != sysUserRoles && sysUserRoles.size() > 0) {
            sysUserRoleMapper.batchSaveSysUserRole(sysUserRoles);
        }

        //4.??????????????????????????????
        List<SysUserGroup> sysUserGroups = getSysUserGroupRelations(sysUser);
        if (null != sysUserGroups && sysUserGroups.size() > 0) {
            sysUserGroupMapper.batchSaveSysUserGroup(sysUserGroups);
        }

        return sysUser.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysUser(SysUser sysUser) {
        // ????????????
        SysUser sysUser1 = this.baseMapper.selectById(sysUser.getId());
        if (sysUser1 == null) {
            throw new SofnException("????????????????????????");
        }
        //???????????????????????????
        checkExists(sysUser);

        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUserId(UserUtil.getLoginUserId());
        this.baseMapper.updateById(sysUser);

        //?????????????????????????????????,
        sysUserRoleMapper.dropUserRolesByUserId(sysUser.getId());
        sysUserGroupMapper.dropUserGroupsByUserId(sysUser.getId());
        //?????????????????????????????????
        SysCacheUtils.increaseUserCacheVersion(sysUser.getId());
        List<SysUserRole> sysUserRoles = getSysUserRoleRelations(sysUser);
        if (null != sysUserRoles && sysUserRoles.size() > 0) {
            sysUserRoleMapper.batchSaveSysUserRole(sysUserRoles);
        }
        //4.??????????????????????????????
        List<SysUserGroup> sysUserGroups = getSysUserGroupRelations(sysUser);
        if (null != sysUserGroups && sysUserGroups.size() > 0) {
            sysUserGroupMapper.batchSaveSysUserGroup(sysUserGroups);
        }
        // ????????????????????????
        if (sysUser.getId().equals(UserUtil.getLoginUserId())) {
            // ???????????? ??????????????????
            String token = UserUtil.getLoginToken();
            if (!StringUtils.isBlank(token)) {
                // ???????????? userName
                redisHelper.hset(Constants.UserSession.userNameToken, sysUser.getUsername(), token);
                // ??????????????????
                redisHelper.hset(token, Constants.UserSession.user, JsonUtils.obj2json(sysUser));
                // ????????????username
                redisHelper.hset(token, Constants.UserSession.userName, sysUser.getUsername());
                // ??????????????????
                redisHelper.hset(token, Constants.UserSession.userOrganizationId, sysUser.getOrganizationId());
                // ??????????????????
                List<SysRole> roles = loadRolesByUserId(sysUser.getId());
                redisHelper.hset(token, Constants.UserSession.userRoles, JsonUtils.obj2json(roles));
            }
        } else {
            // ??????????????????????????????token ?????????????????????   hyj  20200407  ??????????????????Common???
            UserUtil.delUserCache(Lists.newArrayList(sysUser.getUsername()));
        }

    }

    /**
     * ??????????????????
     *
     * @param sysUserForm ???????????????
     * @return false ????????????
     */
    private void checkExists(SysUser sysUser) {
        if (!RegexUtils.isUsername(sysUser.getUsername())){
            throw new SofnException("?????????????????????");
        }

        SysUser sysUser2 = this.findByUserName(sysUser.getUsername());
        if (StringUtils.isBlank(sysUser.getId())) {
            // ??????
            if (sysUser2 != null) {
                throw new SofnException("????????????");
            }
        } else {
            // ??????
            if (sysUser2 != null && !sysUser2.getId().equals(sysUser.getId())) {
                throw new SofnException("????????????");
            }
        }

        if (StringUtils.isBlank(sysUser.getOrganizationId())){
            throw new SofnException("?????????????????????");
        }
        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("id",sysUser.getOrganizationId());
        List<SysOrg> orgInfoByOrgLevel = sysOrgService.getInfoByCondition(queryParams);
        if(!CollectionUtils.isEmpty(orgInfoByOrgLevel)){
            SysOrg so =orgInfoByOrgLevel.get(0);
            if (so == null) {
                throw new SofnException("?????????????????????");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<SysUser> sysUsers) {
        if (!CollectionUtils.isEmpty(sysUsers)) {
            int addNum = 0;
            List<SysUser> sysUserList = Lists.newArrayList();
            // ???100???????????????
            for (SysUser sysUser : sysUsers) {
                if (sysUser != null) {
                    sysUserList.add(sysUser);
                    addNum++;
                    if (addNum % 100 == 0) {
                        sysUserMapper.batchSave(sysUserList);
                        sysUserList.clear();
                        addNum = 0;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(sysUserList)) {
                sysUserMapper.batchSave(sysUserList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysUser(String id) {
        // 1. ??????????????????????????????
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new SofnException("????????????????????????");
        }

        SysCacheUtils.increaseUserCacheVersion(sysUser.getId());
        sysUser.preUpdate();
        sysUser.setDelFlag(SysManageEnum.DEL_FLAG_Y.getCode());
        sysUserMapper.deleteById(sysUser);
        sysUserRoleMapper.dropUserRolesByUserId(sysUser.getId());
    }

    @Override
    public List<SysUserForm> getUserByCondition(Map<String, Object> params) {
        List<SysUser> userList = sysUserMapper.getSysUserByCondition(params);
        List<SysUserForm> userFormList = new ArrayList<>();
        for (SysUser user : userList) {
            SysUserForm sysUserForm = new SysUserForm();
            BeanUtils.copyProperties(user, sysUserForm);
            getRoleForm(sysUserForm);
            userFormList.add(sysUserForm);
        }
        return userFormList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach((id) -> {
                deleteSysUser(id);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String id, String oldPassword, String newPassword) {
        //1 ????????????????????????
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new SofnException("????????????????????????");
        }
        //2 ????????????????????????
        String checkPassword = ShiroUtils.sha256(oldPassword, sysUser.getSalt());
        if (!checkPassword.equals(sysUser.getPassword())) {
            throw new SofnException("?????????????????????????????????");
        }
        //3 ????????????
        String salt = IdUtil.getUUId();
        sysUser.setPassword(ShiroUtils.sha256(newPassword, salt));
        sysUser.setSalt(salt);
        sysUser.preUpdate();
        updateById(sysUser);

        // ??????????????????????????????token ?????????????????????
        String token = redisHelper.hget(Constants.UserSession.userNameToken, sysUser.getUsername()) == null
          ? "" : (String) redisHelper.hget(Constants.UserSession.userNameToken, sysUser.getUsername());
        redisHelper.del(token);
        redisHelper.hdel(Constants.UserSession.userNameToken, sysUser.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String id) {
        //1 ????????????????????????
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new SofnException("????????????????????????");
        }
        //3 ????????????
        String salt = IdUtil.getUUId();
        String initPassword;
        if (sysUser.getInitPassword().isEmpty()) {
            initPassword = IdUtil.getSixNumCode();
        } else {
            initPassword = sysUser.getInitPassword();
        }
        String pdMd5 = DigestUtils.md5DigestAsHex(initPassword.getBytes());
        sysUser.setPassword(ShiroUtils.sha256(pdMd5, salt));
        sysUser.setSalt(salt);
        sysUserMapper.updateById(sysUser);
//        this.baseMapper.updateById(sysUser);
    }

  /**
   * ????????????ID??????????????????
   * @param userId ??????id
   * @return ????????????
   */
    @Override
    public List<SysRole> loadRolesByUserId(String userId) {
        // ???????????????????????????
        List<SysRole> roles = SysCacheUtils.getRoleListByUserId(userId);
        if (CollectionUtils.isEmpty(roles)){
            List<SysRoleVo> loginUserHaveRole = permissionRoleService.getLoginUserHaveRoleNotIncludeUserCreate(userId,false);
            if(CollectionUtils.isEmpty(loginUserHaveRole)){
                throw new SofnException("????????????????????????");
            }

            roles = loginUserHaveRole.stream().map(item -> {
                SysRole sysRole = new SysRole();
                BeanUtils.copyProperties(item, sysRole);
                sysRole.setSubsystemId(item.getSubsystemId());
                return sysRole;
            }).collect(Collectors.toList());

            // ??????????????????
            SysCacheUtils.cacheRoleListByUserId(userId, roles);
        }

        return roles;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<ImportSysUserVo> userList) {
        if (!CollectionUtils.isEmpty(userList)) {
            // ?????????????????????
            Set<String> orgName = Sets.newHashSet();
            // ??????????????????
            Set<String> roleNames = Sets.newHashSet();
            // ?????????????????????
            Set<String> roleGroupNames = Sets.newHashSet();
            // ??????????????????
            Set<String> userName = Sets.newHashSet();
            userList.forEach((user) -> {
                if (user != null) {
                    if (StringUtils.isBlank(user.getUserName())) {
                        throw new SofnException("???????????????");
                    }
                    userName.add(user.getUserName());

                    if (StringUtils.isBlank(user.getOrganizationName())) {
                        throw new SofnException(user.getUserName() + "????????????");
                    }
                    orgName.add(user.getOrganizationName());

                    if (StringUtils.isBlank(user.getRoleNames())) {
                        throw new SofnException(user.getUserName() + "????????????");
                    }
                    roleNames.addAll(IdUtil.getIdsByStr(user.getRoleNames()));

                    if (!StringUtils.isBlank(user.getGroupNames())) {
                        roleGroupNames.addAll(IdUtil.getIdsByStr(user.getGroupNames()));
                    }

                }
            });
            // ?????????????????????????????????
            if (userName.size() != userList.size()) {
                throw new SofnException("??????????????????????????????");
            }
            // ?????????????????????
            Map<String, List> data = checkImportData(orgName, roleNames, roleGroupNames, userName);
            // ??????  SysUser??????
            List<SysUser> sysUserList = Lists.newArrayList();
            List<SysUserRole> sysUserRoles = Lists.newArrayList();
            List<SysUserGroup> sysUserGroups = Lists.newArrayList();
            setData(sysUserList, sysUserRoles, sysUserGroups, data, userList);
            // ????????????
            sysUserMapper.batchSave(sysUserList);
            sysUserRoleMapper.batchSaveSysUserRole(sysUserRoles);
            if (!CollectionUtils.isEmpty(sysUserGroups)) {
                sysUserGroupMapper.batchSaveSysUserGroup(sysUserGroups);
            }
        }
    }


    /**
     * ?????????
     *
     * @param sysUserList   ???????????????????????????
     * @param sysUserRoles  ?????????????????????????????????
     * @param sysUserGroups ????????????????????????????????????
     * @param data          ?????????????????????????????????
     * @param userList      Excel?????????
     */
    public void setData(List<SysUser> sysUserList, List<SysUserRole> sysUserRoles, List<SysUserGroup> sysUserGroups, Map<String, List> data, List<ImportSysUserVo> userList) {
        if (!CollectionUtils.isEmpty(userList) && !CollectionUtils.isEmpty(data)) {
            // ???????????????????????????????????????????????????????????????????????????????????????????????????
            List<SysRole> sysRoles = data.get("sysRoleInfo");
            List<SysOrg> sysOrganizations = data.get("sysOrgInfo");
            List<SysRoleGroup> sysRoleGroups = data.get("sysRoleGroupInfo");
            userList.forEach(userInfo -> {
                SysUser sysUser = new SysUser();
                sysUser.setId(IdUtil.getUUId());
                String sex = userInfo.getSex();
                sysUser.setSex(Integer.parseInt(SysManageEnum.getEnumCode("SEX_", sex)));
                sysUser.setStatus(SysManageEnum.getEnumCode("STATUS_", userInfo.getStatus()));
                sysUser.setMobile(userInfo.getMobile());
                sysUser.setInitPassword(userInfo.getInitPassword());
                sysUser.setNickname(userInfo.getNickname());
                sysUser.setUsername(userInfo.getUserName());
                String email = userInfo.getEmail();
                sysUser.setEmail(email);
                sysUser.setRemark(UserUtil.getLoginUserId() + "??????????????????");
                sysUser.preInsert();
                // ????????????ID
                String organizationName = userInfo.getOrganizationName();
                Optional<SysOrg> first = sysOrganizations.stream().filter((org) -> org.getOrganizationName().equals(organizationName)).findFirst();
                if (first.isPresent()) {
                    sysUser.setOrganizationId(first.get().getId());
                } else {
                    throw new SofnException("??????" + organizationName + "?????????");
                }
                sysUserList.add(sysUser);
                // ????????????????????????
                String roleNames = userInfo.getRoleNames();
                List<String> excelRoleNames = IdUtil.getIdsByStr(roleNames);
                List<String> roleIds = Lists.newArrayList();
                excelRoleNames.forEach((roleName) -> {
                    Optional<SysRole> first1 = sysRoles.stream().filter(sysRole -> sysRole.getRoleName().equals(roleName)).findFirst();
                    if (first1.isPresent()) {
                        roleIds.add(first1.get().getId());
                    } else {
                        throw new SofnException("??????" + roleName + "?????????");
                    }
                });
                roleIds.forEach((roleId) -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setId(IdUtil.getUUId());
                    sysUserRole.setRoleId(roleId);
                    sysUserRole.setUserId(sysUser.getId());
                    sysUserRoles.add(sysUserRole);
                });
                // ???????????????????????????
                String roleGroupNames = userInfo.getGroupNames();
                if (!StringUtils.isBlank(roleGroupNames)) {
                    List<String> roleGroupIds = Lists.newArrayList();
                    List<String> excelRoleGroupNames = IdUtil.getIdsByStr(roleGroupNames);
                    excelRoleGroupNames.forEach((e) -> {
                        Optional<SysRoleGroup> first1 = sysRoleGroups.stream().filter(sysRoleGroup -> sysRoleGroup.getGroupName().equals(e)).findFirst();
                        if (first1.isPresent()) {
                            roleGroupIds.add(first1.get().getId());
                        } else {
                            throw new SofnException("?????????" + e + "?????????");
                        }
                    });
                    roleGroupIds.forEach((groupId) -> {
                        SysUserGroup sysUserGroup = new SysUserGroup();
                        sysUserGroup.setId(IdUtil.getUUId());
                        sysUserGroup.setUserId(sysUser.getId());
                        sysUserGroup.setGroupId(groupId);
                        sysUserGroups.add(sysUserGroup);
                    });
                }
            });
        }
    }


    /**
     * ?????????????????????
     *
     * @param orgName        ????????????
     * @param roleNames      ????????????
     * @param roleGroupNames ???????????????
     * @param userName       ????????????
     */
    private Map<String, List> checkImportData(Set<String> orgName, Set<String> roleNames, Set<String> roleGroupNames, Set<String> userName) {
        Map<String, List> result = Maps.newHashMap();
        // 1. ?????????????????????
        if (!CollectionUtils.isEmpty(userName)) {
            List<SysUser> sysUserList = sysUserMapper.selectList(new QueryWrapper<SysUser>().in("USERNAME", userName).eq("DEL_FLAG", SysManageEnum.DEL_FLAG_N.getCode()));
            if (!CollectionUtils.isEmpty(sysUserList)) {
                throw new SofnException("?????????????????????????????????");
            }
            // 2. ??????????????????????????????
            if (!CollectionUtils.isEmpty(roleNames)) {
                List<SysRole> sysRoles = sysRoleMapper.selectList(new QueryWrapper<SysRole>().in("ROLE_NAME", roleNames).eq("DEL_FLAG", SysManageEnum.DEL_FLAG_N.getCode()));
                if (CollectionUtils.isEmpty(sysRoles)) {
                    throw new SofnException("????????????????????????");
                } else if (sysRoles.size() != roleNames.size()) {
                    List<String> sysRoleNames = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
                    List<String> oldRoleNames = Lists.newArrayList(roleNames);
                    oldRoleNames.removeAll(sysRoleNames);
                    throw new SofnException("????????????????????????" + IdUtil.getStrIdsByList(oldRoleNames) + "???");
                }
                result.put("sysRoleInfo", sysRoles);

            } else {
                throw new SofnException("????????????????????????");
            }
            // 3. ??????????????????????????????
            if (!CollectionUtils.isEmpty(orgName)) {

                Map<String,Object> queryParams = Maps.newHashMap();
                queryParams.put("orgNames",Lists.newArrayList(orgName));
                List<SysOrg> sysOrganizations = sysOrgService.getInfoByCondition(queryParams);

                if (CollectionUtils.isEmpty(sysOrganizations)) {
                    throw new SofnException("??????????????????????????????");
                }
                if (sysOrganizations.size() != orgName.size()) {
                    List<String> oldOrgNames = Lists.newArrayList(orgName);
                    List<String> sysOrgNames = sysOrganizations.stream().map(SysOrg::getOrganizationName).collect(Collectors.toList());
                    oldOrgNames.removeAll(sysOrgNames);
                    throw new SofnException("????????????????????????" + IdUtil.getStrIdsByList(oldOrgNames) + "???");
                }
                result.put("sysOrgInfo", sysOrganizations);
            } else {
                throw new SofnException("????????????????????????????????????");
            }
            // 4. ???????????????????????????
            if (!CollectionUtils.isEmpty(roleGroupNames)) {
                List<SysRoleGroup> sysRoleGroups =
                        sysRoleGroupMapper.selectList(new QueryWrapper<SysRoleGroup>().in("GROUP_NAME", roleGroupNames).eq("DEL_FLAG", SysManageEnum.DEL_FLAG_N.getCode()));
                if (CollectionUtils.isEmpty(sysRoleGroups)) {
                    throw new SofnException("???????????????????????????????????????");
                }
                if (sysRoleGroups.size() != roleGroupNames.size()) {
                    List<String> oldRoleGroups = Lists.newArrayList(roleGroupNames);
                    oldRoleGroups.removeAll(sysRoleGroups.stream().map(SysRoleGroup::getGroupName).collect(Collectors.toSet()));
                    throw new SofnException("???????????????????????????" + IdUtil.getStrIdsByList(oldRoleGroups) + "???");
                }
                result.put("sysRoleGroupInfo", sysRoleGroups);
            }
        } else {
            throw new SofnException("????????????????????????");
        }
        return result;
    }


    @Override
    public List<SysUser> getUserListByOrgId(String orgId, String proxyUser) {
        // ??????????????????????????????
        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("id",orgId);
        List<SysOrg> sysOrganizations = sysOrgService.getInfoByCondition(queryParams);
        SysOrg sysOrg = null;
        if(!CollectionUtils.isEmpty(sysOrganizations)){
            sysOrg = sysOrganizations.get(0);
        }


        Set<SysOrg> orgList = Sets.newHashSet();
        if(sysOrg != null){
            String parengIds = sysOrg.getParentIds() + "/" + sysOrg.getId();
            queryParams.clear();
            queryParams.put("parentIdStrs",parengIds);
            List<SysOrg> sysOrgs = sysOrgService.getInfoByCondition(queryParams);
            if(!CollectionUtils.isEmpty(sysOrgs)){
                orgList.addAll(sysOrgs);
            }
        }

        Set<String> orgIdSet = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(orgList)) {
            orgIdSet = orgList.stream().map(SysOrg::getId).collect(Collectors.toSet());
        }
        orgIdSet.add(sysOrg.getId());
        //  ???????????????????????????
        if (BoolUtils.Y.equals(proxyUser)) {
            List<String> userHasSubsystemIds = permissionSubSystemService.getUserHasSubsystemIds();
            if (!CollectionUtils.isEmpty(userHasSubsystemIds)) {
                List<String> proxyOrgIds = sysOrgAgentService.selectOrgAgentByOrgId(userHasSubsystemIds, Lists.newArrayList(orgId), "");
                if (!CollectionUtils.isEmpty(proxyOrgIds)) {
                    orgIdSet.addAll(proxyOrgIds);
                }
            }
        }
        List<SysUser> sysUsers = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(orgIdSet)) {
            sysUsers = sysUserMapper.selectList(new QueryWrapper<SysUser>()
                    .ne("DEL_FLAG", "Y")
                    .eq("STATUS","1")
                    .in("ORGANIZATION_ID", orgIdSet)
            );
        }
        return sysUsers;
    }

    @Override
    public List<SysUserForm> getUserInfoByIds(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<SysUserForm> userInfoByIds = sysUserMapper.getUserInfoByIds(ids);
            if (CollectionUtils.isEmpty(userInfoByIds)) {
                return Lists.newArrayList();
            }
            // ??????????????????
            userInfoByIds.forEach((e) -> {
                List<String> roleIds = sysUserMapper.getRoleIdsByUserId(e.getId());
                List<String> roleIds2 = Lists.newArrayList();
                List<String> roleNames = Lists.newArrayList();
                Set<String> roleCodes = Sets.newHashSet();
                Set<String> permissions = Sets.newHashSet();
                if (!CollectionUtils.isEmpty(roleIds)) {
                    // ???????????????????????????
                    List<Map<String, String>> roleInfoByRoleIds = sysUserMapper.getRoleInfoByRoleIds(roleIds);
                    if (!CollectionUtils.isEmpty(roleInfoByRoleIds)) {
                        roleInfoByRoleIds.forEach(mapInfo -> {
                            String roleId = mapInfo.get("roleid");
                            if (!roleIds2.contains(roleId)) {
                                roleIds2.add(roleId);
                                roleNames.add(mapInfo.get("rolename"));
                                roleCodes.add(mapInfo.get("rolecode") == null ? "" : mapInfo.get("rolecode"));
                            }
                            String permission = mapInfo.get("permission");
                            if (!StringUtils.isBlank(permission)) {
                                permissions.add(permission);
                            }
                        });
                    }
                }
                e.setRoleCodes(roleCodes);
                // ????????????
                e.setRoleIds(IdUtil.getStrIdsByList(roleIds2));
                e.setRoleNames(IdUtil.getStrIdsByList(roleNames));
                e.setResourceList(permissions);
            });
            return userInfoByIds;
        }
        return Lists.newArrayList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UpdateUserInfoVo updateUserInfoVo) {
        if (updateUserInfoVo != null) {
            String id = updateUserInfoVo.getId();
            SysUser sysUser = sysUserMapper.selectById(id);
            if (sysUser == null) {
                throw new SofnException("????????????????????????");
            }
            if (SysManageEnum.DEL_FLAG_Y.getCode().equals(sysUser.getDelFlag())) {
                throw new SofnException("???????????????");
            }

            if(StringUtils.isNotBlank(updateUserInfoVo.getEmail())){
                sysUser.setEmail(updateUserInfoVo.getEmail());
            }
            if(StringUtils.isNotBlank(updateUserInfoVo.getMobile())){
                sysUser.setMobile(updateUserInfoVo.getMobile());
            }
            if(StringUtils.isNotBlank(updateUserInfoVo.getStatus())){
                sysUser.setStatus(updateUserInfoVo.getStatus());
            }
            if(updateUserInfoVo.getSex() != null && updateUserInfoVo.getSex() != 0 ){
                sysUser.setSex(updateUserInfoVo.getSex());
            }
            sysUser.setRemark(updateUserInfoVo.getRemark());
            if(StringUtils.isNotBlank(updateUserInfoVo.getNickname())){
                sysUser.setNickname(updateUserInfoVo.getNickname());
            }
            sysUser.preUpdate();
            sysUserMapper.updateById(sysUser);
        }
    }

    @Override
    public PageUtils<BasicUserInfo> getBasicUserInfo(SelectUserConditionVo selectUserConditionVo, Integer pageNo,
                                                     Integer pageSize) {
        if (selectUserConditionVo != null) {
            if (!StringUtils.isBlank(selectUserConditionVo.getStrIds())) {
                selectUserConditionVo.setIds(IdUtil.getIdsByStr(selectUserConditionVo.getStrIds()));
            }
            if (!StringUtils.isBlank(selectUserConditionVo.getRoleIdStrs())) {
                selectUserConditionVo.setRoleIds(IdUtil.getIdsByStr(selectUserConditionVo.getRoleIdStrs()));
            }
            if (pageNo != null && pageSize != null && pageSize != 0) {
                PageHelper.offsetPage(pageNo, pageSize);
            }
            List<BasicUserInfo> basicUserInfo = sysUserMapper.getBasicUserInfo(selectUserConditionVo);
            PageInfo<BasicUserInfo> pageInfo = new PageInfo<>(basicUserInfo);
            return PageUtils.getPageUtils(pageInfo);
        }
        return null;

    }

    @Override
    public PageUtils<BasicUserInfo> getBasicUserInfoNew(SelectUserConditionVo selectUserConditionVo, Integer pageNo, Integer pageSize) {
        if (selectUserConditionVo != null && selectUserConditionVo.getRegioncodes().size() > 0) {
            if (!StringUtils.isBlank(selectUserConditionVo.getStrIds())) {
                selectUserConditionVo.setIds(IdUtil.getIdsByStr(selectUserConditionVo.getStrIds()));
            }
            if (!StringUtils.isBlank(selectUserConditionVo.getRoleIdStrs())) {
                selectUserConditionVo.setRoleIds(IdUtil.getIdsByStr(selectUserConditionVo.getRoleIdStrs()));
            }
            if (pageNo != null && pageSize != null && pageSize != 0) {
                PageHelper.offsetPage(pageNo, pageSize);
            }
            List<BasicUserInfo> basicUserInfo = sysUserMapper.getBasicUserInfoNew(selectUserConditionVo);
            PageInfo<BasicUserInfo> pageInfo = new PageInfo<>(basicUserInfo);
            return PageUtils.getPageUtils(pageInfo);
        }
        return null;

    }

    @Override
    public boolean checkUsernameExist(String id, String username) {
        List<SysUser> userList = sysUserMapper.selectList(new QueryWrapper<SysUser>()
                .eq("USERNAME", username)
                .ne(StringUtils.isNotBlank(id), "ID", id));
        return !CollectionUtils.isEmpty(userList);
    }

    @Override
    public List<ImportSysUserVo> getExportUserInfo(Map<String, String> params) {
        if (!CollectionUtils.isEmpty(params)) {
            return sysUserMapper.getExportUserInfo(params);
        }
        return null;
    }

    @Override
    public PageUtils<List<BasicUserInfo>> getRoleHaveUserInfo(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        if (CollectionUtils.isEmpty(params) ||params.get("roleId") == null ||StringUtils.isBlank(params.get("roleId").toString())) {
            return null;
        }
        if(pageSize >0){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        // ??????????????????????????????
        List<BasicUserInfo> userByRoleIdAndSubsystemId;
        if(permissionSuperAdminService.isSuperman()){
            userByRoleIdAndSubsystemId = sysUserMapper.getUserByRoleIdAndSubsystemId(params);
        }else{
            List<String> userAppIds = permissionSubSystemService.getUserAppIds();
            params.put("orgId", UserUtil.getLoginUserOrganizationId());
            params.put("appIds", userAppIds);
            params.put("limitOrg", BoolUtils.Y);
            params.put("carryChildren", BoolUtils.Y);
            params.put("carryProxyOrg", BoolUtils.Y);
            // ????????????????????????
            params.put("userId", UserUtil.getLoginUserId());

            userByRoleIdAndSubsystemId =  sysUserMapper.getUserByRoleIdAndSubsystemId(params);
        }

        PageInfo<BasicUserInfo> pageInfo = new PageInfo<>(userByRoleIdAndSubsystemId);
        return PageUtils.getPageUtils(pageInfo);
    }

    @Override
    public PageUtils<List<BasicUserInfo>> getRoleDontHaveUserInfo(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        throw new SofnException("????????????????????????????????????????????????orgIds??????????????????");
//        if (CollectionUtils.isEmpty(params) ||params.get("roleId") == null ||StringUtils.isBlank(params.get("roleId").toString())) {
//            return null;
//        }
//        if(pageSize > 0){
//            PageHelper.offsetPage(pageNo, pageSize);
//        }
//
//        // ??????????????????????????????????????????
//
//        List<String> allOrgListAndLoginUserId = sysOrgService.getUserHaveOrgIds(null, true, true);
//        if(CollectionUtils.isEmpty(allOrgListAndLoginUserId)){
//            throw new SofnException("????????????????????????????????????");
//        }
//        params.put("orgIds", allOrgListAndLoginUserId);
//        List<BasicUserInfo> userByNotExistsRoleId = sysUserMapper.getUserByNotExistsRoleId(params);
//        PageInfo<BasicUserInfo> pageInfo = new PageInfo<>(userByNotExistsRoleId);
//        return PageUtils.getPageUtils(pageInfo);
    }

    @Override
    public List<BasicUserInfo> getAllUser() {
        log.info("????????????");
        List<String> userHaveOrgIds = sysOrgService.getUserHaveOrgIds(null, true, true);
        log.info("??????????????????");
        if(CollectionUtils.isEmpty(userHaveOrgIds)){
            log.warn("???????????????{}????????????????????????????????????",UserUtil.getLoginUserName());
            return Lists.newArrayList();
        }
        List<SysUser> sysUserList;
        log.info("????????????????????????");
        if(permissionSuperAdminService.isSuperman()){
            sysUserList = sysUserMapper.selectList(
                    new QueryWrapper<SysUser>()
                            .eq("DEL_FLAG",SysManageEnum.DEL_FLAG_N.getCode())
                            .eq("STATUS", SysManageEnum.STATUS_1.getCode())
            );
        }else{
            GetAllInfoByPageUtil<SysUser> getAllInfoByPageUtil = new GetAllInfoByPageUtil<>();
            sysUserList = getAllInfoByPageUtil.getAllInfo((param)->{
                return sysUserMapper.selectList(
                        new QueryWrapper<SysUser>().in("ORGANIZATION_ID",(List)param[0])
                                .eq("DEL_FLAG",SysManageEnum.DEL_FLAG_N.getCode())
                                .eq("STATUS", SysManageEnum.STATUS_1.getCode())

                );
            },0,false,"",userHaveOrgIds);
        }
        log.info("??????????????????????????????");

        if (!CollectionUtils.isEmpty(sysUserList)) {
            List<BasicUserInfo> basicUserInfos = sysUserList.stream().map(item -> {
                BasicUserInfo basicUserInfo = new BasicUserInfo();
                if (item != null) {
                    basicUserInfo.setId(item.getId());
                    basicUserInfo.setNickname(item.getNickname());
                    basicUserInfo.setUsername(item.getUsername());
                }
                return basicUserInfo;
            }).collect(Collectors.toList());
            return basicUserInfos;
        }
        return null;
    }

    @Override
    public List<String> getLoginUserBelongSystem() {

        return null;
    }

    @Override
    public SysUserForm getUserInfoByUsername(String username) {
        List<SysUser> sysUserList = sysUserMapper.selectList(new QueryWrapper<SysUser>().eq("USERNAME", username));
        if(CollectionUtils.isEmpty(sysUserList)){
            return null;
        }
        if(sysUserList.size() > 1){
            log.error("?????????:{}??????",username);
            return null;
        }
        SysUser sysUser = sysUserList.get(0);
        if(BoolUtils.Y.equals(sysUser.getDelFlag())){
            log.error("??????:{}?????????",username);
            return null;
        }

        String organizationId = sysUser.getOrganizationId();
        if(StringUtils.isEmpty(organizationId)){
            log.error("??????:{}???????????????",username);
            return null;
        }
        Map<String,Object> queryParams = Maps.newHashMap();
        queryParams.put("id",organizationId);
        List<SysOrg> sysOrganizations = sysOrgService.getInfoByCondition(queryParams);
        SysOrg sysOrg = null;
        if(!CollectionUtils.isEmpty(sysOrganizations)){
            sysOrg = sysOrganizations.get(0);
        }

        if(sysOrg == null|| BoolUtils.Y.equals(sysOrg.getDelFlag())){
            log.error("??????:{}???????????????",username);
            return null;
        }
        // ??????????????????
        SysUserForm sysUserForm = new SysUserForm(sysUser);
        sysUserForm.setOrganizationName(sysOrg.getOrganizationName());
        sysUserForm.setOrganizationId(organizationId);
        return sysUserForm;
    }

    @Override
    public List<SysUserForm> getUserInfoByRegionCodeAndAppId(String regionCode, String appId) {
        return getUserByOrgInfoAndAppId(Lists.newArrayList(regionCode), appId, null, true);
    }

    @Override
    public List<SysUserForm> getUserByOrgInfoAndAppId(List<String> regionCodes, String appId, String level, boolean includeRoleAndOrg) {
        List<SysUserForm> sysUserForms = Lists.newArrayList();

        // 1. ???????????????????????????????????????
        List<SysOrg> orgList = sysOrgService.getOrgIdByRegionCodeAndAppId(appId, regionCodes, level);
        if(CollectionUtils.isEmpty(orgList)){
            return sysUserForms;
        }

        // 2. ??????????????????????????????
        List<String> collect = orgList.stream().map(SysOrg::getId).collect(Collectors.toList());
        GetAllInfoByPageUtil<SysUser> getAllInfoByPageUtil = new GetAllInfoByPageUtil<>();

        List<SysUser> sysUserList = getAllInfoByPageUtil.getAllInfo((param)->{
            return sysUserMapper.selectList(
                    new QueryWrapper<SysUser>().in("ORGANIZATION_ID",(List)param[0])
                    .eq("DEL_FLAG",SysManageEnum.DEL_FLAG_N.getCode())
            );
        },0,false,"",collect);

        if(CollectionUtils.isEmpty(sysUserList)){
            return null;
        }

        if (includeRoleAndOrg){
            List<String> userIds = sysUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // 3. ????????????????????????
            GetAllInfoByPageUtil<UserHaveRoleVo> getAllInfoByPageUtil2 = new GetAllInfoByPageUtil<>();
            List<UserHaveRoleVo> userHaveRoleVos = getAllInfoByPageUtil2.getAllInfo((param)->{
                return sysRoleMapper.getUserHaveRole((List)param[0]);
            },0,false,"",userIds);

            Map<String,SysOrg> orgInfos = Maps.newHashMap();
            orgList.forEach(item->{
                orgInfos.put(item.getId(),item);
            });

            sysUserForms = sysUserList.stream().map(item->{
                SysUserForm sysUserForm = new SysUserForm(item);
                // ?????????????????????
                SysOrg sysOrg = orgInfos.get(item.getOrganizationId());
                if(sysOrg != null){
                    sysUserForm.setRegionCode(sysOrg.getRegionLastCode());
                    sysUserForm.setRegionNames(sysOrg.getRegioncode());
                }
                // ??????????????????
                Optional<UserHaveRoleVo> first = userHaveRoleVos.stream().filter(userHaveRoleVo -> userHaveRoleVo.getUserId().equals(item.getId())).findFirst();
                if(first.isPresent()){
                    UserHaveRoleVo userHaveRoleVo = first.get();
                    List<SysRole> sysRoles = userHaveRoleVo.getSysRoles();
                    if(!CollectionUtils.isEmpty(sysRoles)){
                        List<String> roleCodes = sysRoles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
                        sysUserForm.setRoleCodes(Sets.newHashSet(roleCodes));
                        List<String> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toList());
                        sysUserForm.setRoleIds(IdUtil.getStrIdsByList(roleIds));
                    }
                }
                return sysUserForm;
            }).collect(Collectors.toList());
        }else {
            sysUserForms = sysUserList.stream().map(item->{
                SysUserForm sysUserForm = new SysUserForm(item);
                return sysUserForm;
            }).collect(Collectors.toList());
        }

        return sysUserForms;
    }

    @Override
  public boolean hasUserByOrgId(String orgId) {
    Integer count = sysUserMapper.selectCount(new QueryWrapper<SysUser>()
      .ne("DEL_FLAG", SysManageEnum.DEL_FLAG_Y.getCode())
      .eq("ORGANIZATION_ID", orgId)
    );

    return count>0;
  }

  private List<SysUserRole> getSysUserRoleRelations(SysUser sysUser) {
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        if (sysUser.getRoleList() != null && sysUser.getRoleList().size() > 0) {
            sysUser.getRoleList().forEach(role -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(IdUtil.getUUId());
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(role.getId());
                sysUserRoles.add(sysUserRole);
            });
        }
        return sysUserRoles;
    }

    private List<SysUserGroup> getSysUserGroupRelations(SysUser sysUser) {
        List<SysUserGroup> sysUserGroups = new ArrayList<>();
        if (sysUser.getGroupList() != null && sysUser.getGroupList().size() > 0) {
            sysUser.getGroupList().forEach(roleGroup -> {
                SysUserGroup sysUserGroup = new SysUserGroup();
                sysUserGroup.setId(IdUtil.getUUId());
                sysUserGroup.setUserId(sysUser.getId());
                sysUserGroup.setGroupId(roleGroup.getId());
                sysUserGroups.add(sysUserGroup);
            });
        }
        return sysUserGroups;
    }

    @Override
    public List<SysUserSystem> getUserUseSystemList(String userId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("userId",userId);
        List<SysUserSystem> userSystemList=userSystemMapper.getListByConditions(params);
        return userSystemList;
    }
    @Override
    public String addUserSystem(String userId,String systemId){
        String returnString="";
        //?????????????????????????????????????????????????????????
        Map<String,Object> params=Maps.newHashMap();
        params.put("userId",userId);
        params.put("systemId",systemId);
        List<SysUserSystem> sysUserSystem=userSystemMapper.getListByConditions(params);
        if(sysUserSystem.size()>0){//?????????????????????
            sysUserSystem.get(0).setUpdateTime(new Date());
            userSystemMapper.updateById(sysUserSystem.get(0));
        }else{//??????????????????
            SysUserSystem userSystem=new SysUserSystem();
            userSystem.setId(IdUtil.getUUId());
            SysSubsystem subSystem=sysSubsystemService.getSubsystemByIdOrAppId(systemId);
            userSystem.setSystemId(systemId);
            userSystem.setUserId(userId);
            userSystem.setAppId(subSystem.getAppId());
            userSystem.setSystemName(subSystem.getSubsystemName());
            userSystem.setUpdateTime(new Date());
            userSystem.setViewUrl(subSystem.getViewUrl());

            userSystemMapper.insert(userSystem);
            //????????????????????????????????????????????????10???,??????10??????????????????????????????????????????
            params.remove("systemId");
            List<SysUserSystem> userSystemList=userSystemMapper.getListByConditions(params);
            if(userSystemList.size()>10){
                userSystemMapper.deleteById(userSystemList.get(10).getId());
            }
        }
        //redis?????????????????????????????????
        params.remove("systemId");
        List<SysUserSystem> userSystemList=userSystemMapper.getListByConditions(params);
        SysCacheUtils.cacheUserUseSystem(userId,userSystemList);
        return returnString;
    }
}
