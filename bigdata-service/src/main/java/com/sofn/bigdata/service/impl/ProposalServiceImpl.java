package com.sofn.bigdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Maps;
import com.sofn.bigdata.enums.DecisionStatusEnum;
import com.sofn.bigdata.mapper.DataAnalyzeMapper;
import com.sofn.bigdata.mapper.DecisionProblemMapper;
import com.sofn.bigdata.mapper.ProposalMapper;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.model.DecisionProblem;
import com.sofn.bigdata.model.Proposal;
import com.sofn.bigdata.service.ProposalService;
import com.sofn.bigdata.sysapi.SysRegionApi;
import com.sofn.bigdata.vo.DataAnalyzeVo;
import com.sofn.bigdata.vo.SysFileManageForm;
import com.sofn.bigdata.vo.SysFileVo;
import com.sofn.common.exception.SofnException;
import com.sofn.common.model.Result;
import com.sofn.common.model.User;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.UserUtil;
import com.xiaoleilu.hutool.date.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private SysRegionApi sysRegionApi;
    @Autowired
    private DecisionProblemMapper decisionProblemMapper;
    @Autowired
    private DataAnalyzeMapper dataAnalyzeMapper;

    @Override
    public PageInfo<Proposal> selectListByQuery(Integer pageNo, Integer pageSize, String name, String status) {
        User loginUser = UserUtil.getLoginUser();
        String creator = loginUser.getUsername();
        Map<String,Object> params = Maps.newHashMap();
        params.put("name",name);
        params.put("status",status);
        params.put("creator",creator);
        List<Proposal> proposals = proposalMapper.selectListByQuery(params);
        List<String> ids = new ArrayList<>();
        for (Proposal proposal1 : proposals) {
            ids.add(proposal1.getId());
        }
        //id去重
        ids=ids.stream()
                .distinct()
                .collect(Collectors.toList());
        List<Proposal> list = new ArrayList<>();
        for (String s : ids) {
            List<Proposal> view = proposalMapper.view(s);
            list.add(view.get(0));
        }
        List<Proposal> newlist = new ArrayList<>();
        if(pageNo*pageSize>list.size()){
            newlist=list.subList((pageNo-1)*pageSize, list.size());
        }else{
            newlist=list.subList((pageNo-1)*pageSize, pageNo*pageSize);
        }
        PageInfo<Proposal> pageInfo = new PageInfo<>(newlist);
        pageInfo.setTotal(list.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNo);
        return pageInfo;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void insertProposal(Proposal proposal) {
        User loginUser = UserUtil.getLoginUser();
        if (proposal.getId()==null||proposal.getId()==""){
            //id为空说明新增
            proposal.setId(IdUtil.getUUId());
            proposal.setCreateTime(new Date());
            proposal.setCreator(loginUser.getUsername());
            proposal.setCreatorId(loginUser.getId());
            proposal.setStatus(DecisionStatusEnum.BE_NOT_SELECTED.getCode());
            if (proposal.getFileId()==null|| StringUtil.isEmpty(proposal.getFileId())){
                throw new SofnException("上传文件ID不可为空！");
            }
            //激活新文件信息
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("激活文件失败");
            }
            proposalMapper.insert(proposal);
        }else {
            List<Proposal> view = proposalMapper.view(proposal.getId());
            Proposal proposalOld = view.get(0);
            if (proposalOld.equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
                throw new SofnException("已完成的提案不允许再作更改！");
            }
            proposal.setCreateTime(new Date());
            proposal.setCreator(loginUser.getUsername());
            proposal.setCreatorId(loginUser.getId());
            proposal.setStatus(proposalOld.getStatus());
            //已被问题选定的提案进行编辑时,不可更换所提问的问题
            if (!proposal.getDecisionProblemId().equals(proposalOld.getDecisionProblemId()) && proposalOld.getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())) {
                throw new SofnException("该提案处于进行中时,不可更换所提问的问题");
            }
            if (proposal.getFileId()==null|| StringUtil.isEmpty(proposal.getFileId())){
                throw new SofnException("上传文件ID不可为空！");
            }
            //激活新文件信息
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("激活文件失败");
            }
            proposalMapper.insert(proposal);
        }
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void remove(String id) {
        List<Proposal> view = proposalMapper.view(id);
        for (Proposal proposal : view) {
            //先删除已存远程文件信息
            sysRegionApi.delFile(proposal.getFileId());
        }
        //再删除该提案
        proposalMapper.removeById(id);
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void update(Proposal proposal) {
        //获取所更新提案信息
        List<Proposal> view = proposalMapper.view(proposal.getId());
        if (view.get(0).getFileId()==null){
            throw new SofnException("未查询到该提案中文件信息");
        }
        if (view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
            throw new SofnException("已完成的提案不允许再作更改！");
        }
        //判断是否更新了文件信息,相同则更新其他字段,不同则删除已存文件信息,激活新文件信息
        if (view.get(0).getFileId().equals(proposal.getFileId())){
            proposalMapper.update(proposal);
        }else {
            //先删除已存远程文件信息
            sysRegionApi.delFile(view.get(0).getFileId());
            //激活新文件信息
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("激活文件失败");
            }
            proposalMapper.update(proposal);
        }
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public List<Proposal> view(String id) {
        List<Proposal> view = proposalMapper.view(id);
        List<Proposal> newlist = new ArrayList<>();
        if(3>view.size()){
            newlist=view.subList(0, view.size());
        }else{
            newlist=view.subList(0, 3);
        }
        return newlist;
    }

    @Override
    public void approver(String id) {
        //获取所有该数据
        List<Proposal> view = proposalMapper.view(id);
        Proposal proposal = view.get(0);
        if (!proposal.getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())){
            throw new SofnException("未被选定的提案无法批准");
        }
        //获取最新数据
        proposal.setStatus(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode());
        proposal.setApprover(UserUtil.getLoginUser().getUsername());
        DecisionProblem view1 = decisionProblemMapper.view(proposal.getDecisionProblemId());
        if (view1.getStatus().equals(DecisionStatusEnum.Decision_PROPOSAL_ED.getCode())){
            throw new SofnException("已被批准的问题请勿重复批准");
        }
        view1.setStatus(DecisionStatusEnum.Decision_PROPOSAL_ED.getCode());
        decisionProblemMapper.update(view1);
        proposalMapper.update(proposal);
    }

    @Override
    public void insertAnnotation(String id, String annotation) {
        //获取所有该数据
        List<Proposal> view = proposalMapper.view(id);
        //获取最新数据
        if (!view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
            throw new SofnException("未批准的提案不可进行批注！");
        }
        view.get(0).setAnnotation(annotation);
        view.get(0).setAnnotationPerson(UserUtil.getLoginUser().getUsername());
        view.get(0).setAnnotationTime(new Date());
        proposalMapper.update(view.get(0));
    }

    @Override
    public List<DataAnalyze> getDataAnalyze() {
        return dataAnalyzeMapper.getDataAnalyzeList(null);
    }
}