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
        //id??????
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
            //id??????????????????
            proposal.setId(IdUtil.getUUId());
            proposal.setCreateTime(new Date());
            proposal.setCreator(loginUser.getUsername());
            proposal.setCreatorId(loginUser.getId());
            proposal.setStatus(DecisionStatusEnum.BE_NOT_SELECTED.getCode());
            if (proposal.getFileId()==null|| StringUtil.isEmpty(proposal.getFileId())){
                throw new SofnException("????????????ID???????????????");
            }
            //?????????????????????
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("??????????????????");
            }
            proposalMapper.insert(proposal);
        }else {
            List<Proposal> view = proposalMapper.view(proposal.getId());
            Proposal proposalOld = view.get(0);
            if (proposalOld.equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
                throw new SofnException("??????????????????????????????????????????");
            }
            proposal.setCreateTime(new Date());
            proposal.setCreator(loginUser.getUsername());
            proposal.setCreatorId(loginUser.getId());
            proposal.setStatus(proposalOld.getStatus());
            //??????????????????????????????????????????,??????????????????????????????
            if (!proposal.getDecisionProblemId().equals(proposalOld.getDecisionProblemId()) && proposalOld.getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())) {
                throw new SofnException("???????????????????????????,??????????????????????????????");
            }
            if (proposal.getFileId()==null|| StringUtil.isEmpty(proposal.getFileId())){
                throw new SofnException("????????????ID???????????????");
            }
            //?????????????????????
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("??????????????????");
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
            //?????????????????????????????????
            sysRegionApi.delFile(proposal.getFileId());
        }
        //??????????????????
        proposalMapper.removeById(id);
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void update(Proposal proposal) {
        //???????????????????????????
        List<Proposal> view = proposalMapper.view(proposal.getId());
        if (view.get(0).getFileId()==null){
            throw new SofnException("????????????????????????????????????");
        }
        if (view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
            throw new SofnException("??????????????????????????????????????????");
        }
        //?????????????????????????????????,???????????????????????????,?????????????????????????????????,?????????????????????
        if (view.get(0).getFileId().equals(proposal.getFileId())){
            proposalMapper.update(proposal);
        }else {
            //?????????????????????????????????
            sysRegionApi.delFile(view.get(0).getFileId());
            //?????????????????????
            SysFileManageForm sysFileManageForm = new SysFileManageForm();
            sysFileManageForm.setSystemId(DecisionStatusEnum.ASTCAP_ID.getCode());
            sysFileManageForm.setIds(proposal.getFileId());
            Result<List<SysFileVo>> listResult = sysRegionApi.activationFile(sysFileManageForm);
            if(listResult.getData() == null || listResult.getData().size() == 0){
                throw new SofnException("??????????????????");
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
        //?????????????????????
        List<Proposal> view = proposalMapper.view(id);
        Proposal proposal = view.get(0);
        if (!proposal.getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())){
            throw new SofnException("?????????????????????????????????");
        }
        //??????????????????
        proposal.setStatus(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode());
        proposal.setApprover(UserUtil.getLoginUser().getUsername());
        DecisionProblem view1 = decisionProblemMapper.view(proposal.getDecisionProblemId());
        if (view1.getStatus().equals(DecisionStatusEnum.Decision_PROPOSAL_ED.getCode())){
            throw new SofnException("???????????????????????????????????????");
        }
        view1.setStatus(DecisionStatusEnum.Decision_PROPOSAL_ED.getCode());
        decisionProblemMapper.update(view1);
        proposalMapper.update(proposal);
    }

    @Override
    public void insertAnnotation(String id, String annotation) {
        //?????????????????????
        List<Proposal> view = proposalMapper.view(id);
        //??????????????????
        if (!view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())){
            throw new SofnException("???????????????????????????????????????");
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