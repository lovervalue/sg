package com.sofn.bigdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.sofn.bigdata.enums.DecisionStatusEnum;
import com.sofn.bigdata.mapper.DecisionProblemMapper;
import com.sofn.bigdata.mapper.ProposalMapper;
import com.sofn.bigdata.model.DecisionProblem;
import com.sofn.bigdata.model.Proposal;
import com.sofn.bigdata.service.DecisionProblemService;
import com.sofn.common.exception.SofnException;
import com.sofn.common.model.User;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.UserUtil;
import org.apache.avro.ipc.specific.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class DecisionProblemServiceImpl implements DecisionProblemService {

    @Autowired
    private DecisionProblemMapper decisionProblemMapper;
    @Autowired
    private ProposalMapper proposalMapper;

    @Override
    public PageInfo<DecisionProblem> selectListByQuery(Integer pageNo, Integer pageSize, String name, String status) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("name",name);
        params.put("status",status);
        params.put("creatorId",UserUtil.getLoginUserId());
        List<DecisionProblem> list = decisionProblemMapper.selectListByQuery(params);
        list.forEach(decisionProblem -> {
            List<Proposal> proposals = proposalMapper.selectByDecisionProblemId(decisionProblem.getId());
            if (proposals==null||proposals.size()==0){
                decisionProblem.setProposalName("无");
                decisionProblem.setProposalNamePerson("无");
            }else {
                decisionProblem.setProposalName(proposals.get(0).getName());
                decisionProblem.setProposalNamePerson(proposals.get(0).getCreator());
            }
        });
        List<DecisionProblem> newlist = new ArrayList<>();
        if(pageNo*pageSize>list.size()){
            newlist=list.subList((pageNo-1)*pageSize, list.size());
        }else{
            newlist=list.subList((pageNo-1)*pageSize, pageNo*pageSize);
        }
        PageInfo<DecisionProblem> pageInfo = new PageInfo<>(newlist);
        pageInfo.setTotal(list.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNo);
        return pageInfo;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void insertDecisionProblem(DecisionProblem decisionProblem) {
        User loginUser = UserUtil.getLoginUser();
        decisionProblem.setId(IdUtil.getUUId());
        decisionProblem.setCreatorId(loginUser.getId());
        decisionProblem.setCreateTime(new Date());
        decisionProblem.setCreator(loginUser.getUsername());
        decisionProblem.setStatus(DecisionStatusEnum.ASKING.getCode());
        String isRelated = decisionProblem.getIsRelated();
        if ("Y".equals(isRelated)){
            //如果有关联其他问题则判断是从属性还是关联性
            String problemAffiliation = decisionProblem.getProblemAffiliation();
            if ("从属性".equals(problemAffiliation)){
                //说明单向绑定不必操作,直接添加
                decisionProblemMapper.insert(decisionProblem);
            }else {
                //说明是关联性  需要双向绑定  在关联的问题中增加其关联问题数量
                //判断字段中是否存在",",存在则是多个问题，不存在则是单个
                if (decisionProblem.getRelatedProblemIds().contains(",")){
                    //包含则转为集合
                    List<String> idList = Arrays.asList(decisionProblem.getRelatedProblemIds().split(","));
                    for (String s : idList) {
                        DecisionProblem view = decisionProblemMapper.view(s);
                        //判断该问题是否当前有绑定问题
                        if (view.getIsRelated().equals("Y")){
                            //所绑定问题绑定的问题新增当前问题
                            view.setRelatedProblemIds(view.getRelatedProblemIds()+","+decisionProblem.getId());
                            view.setRelatedProblemNames(view.getRelatedProblemNames()+","+decisionProblem.getName());
                            decisionProblemMapper.update(view);
                        }else {
                            //说明之前所绑定的问题并未绑定其他问题
                            view.setIsRelated("Y");
                            view.setRelatedProblemIds(decisionProblem.getId());
                            view.setRelatedProblemNames(decisionProblem.getName());
                            decisionProblemMapper.update(view);
                        }
                    }
                    decisionProblemMapper.insert(decisionProblem);
                }else {
                    //不包含则直接去修改所关联问题
                    DecisionProblem view = decisionProblemMapper.view(decisionProblem.getRelatedProblemIds());
                    //判断该问题是否当前有绑定问题
                    if (view.getIsRelated().equals("Y")){
                        //有的话则添加新的
                        //所绑定问题绑定的问题新增当前问题
                        view.setRelatedProblemIds(view.getRelatedProblemIds()+","+decisionProblem.getId());
                        view.setRelatedProblemNames(view.getRelatedProblemNames()+","+decisionProblem.getName());
                        decisionProblemMapper.update(view);
                        decisionProblemMapper.insert(decisionProblem);
                    }else {
                        //说明之前所绑定的问题并未绑定其他问题
                        view.setIsRelated("Y");
                        view.setRelatedProblemIds(decisionProblem.getId());
                        view.setRelatedProblemNames(decisionProblem.getName());
                        decisionProblemMapper.update(view);
                        decisionProblemMapper.insert(decisionProblem);
                    }
                }
            }
        }else {
            //未关联其他问题就不必操作,直接添加
            decisionProblemMapper.insert(decisionProblem);
        }
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void remove(String id) {
        DecisionProblem decisionProblemOld = decisionProblemMapper.view(id);
        if (decisionProblemOld.getIsRelated()!=null&&decisionProblemOld.getIsRelated().equals("Y")){
            //判断以前是否为从属性，是则不作操作，否则获取所关联的问题，在关联的问题所绑定的问题中去掉当前问题
            if (decisionProblemOld.getProblemAffiliation()!=null&&decisionProblemOld.getProblemAffiliation().equals("关联性")){
                //以前为关联性,判断绑定1个还是多个问题
                if (decisionProblemOld.getRelatedProblemIds().contains(",")){
                    List<String> idOldList = Arrays.asList(decisionProblemOld.getRelatedProblemIds().split(","));
                    for (String s : idOldList) {
                        DecisionProblem view = decisionProblemMapper.view(s);
                        if (view.getRelatedProblemIds().contains(",")){
                            List<String> idList = Arrays.asList(view.getRelatedProblemIds().split(","));
                            idList.remove(decisionProblemOld.getId());
                            List<String> nameList = Arrays.asList(view.getRelatedProblemNames().split(","));
                            nameList.remove(decisionProblemOld.getName());
                            String ids = String.join(",", idList);
                            String names = String.join(",", nameList);
                            view.setRelatedProblemIds(ids);
                            view.setRelatedProblemNames(names);
                            decisionProblemMapper.update(view);
                        }else {
                            //该问题只绑定了当前问题那么直接清空
                            view.setIsRelated("N");
                            view.setRelatedProblemIds(null);
                            view.setRelatedProblemNames(null);
                            decisionProblemMapper.update(view);
                        }
                    }
                }else {
                    //绑定一个问题则去删除当前问题
                    DecisionProblem view = decisionProblemMapper.view(decisionProblemOld.getRelatedProblemIds());
                    if (view.getRelatedProblemIds().contains(",")){
                        List<String> idList = Arrays.asList(view.getRelatedProblemIds().split(","));
                        idList.remove(decisionProblemOld.getId());
                        List<String> nameList = Arrays.asList(view.getRelatedProblemNames().split(","));
                        nameList.remove(decisionProblemOld.getName());
                        String ids = String.join(",", idList);
                        String names = String.join(",", nameList);
                        view.setRelatedProblemIds(ids);
                        view.setRelatedProblemNames(names);
                        decisionProblemMapper.update(view);
                    }else {
                        //该问题只绑定了当前问题那么直接清空
                        view.setIsRelated("N");
                        view.setRelatedProblemIds(null);
                        view.setRelatedProblemNames(null);
                        decisionProblemMapper.update(view);
                    }
                }
            }
        }
        //删除问题时同时删除所对应的提案
        List<Proposal> proposals = proposalMapper.selectByDecisionProblemId(id);
        if (proposals!=null&&proposals.size()!=0){
            for (Proposal proposal : proposals) {
                proposalMapper.removeById(proposal.getId());
            }
        }
        decisionProblemMapper.removeById(id);
    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void update(DecisionProblem decisionProblem) {
        DecisionProblem decisionProblemOld = decisionProblemMapper.view(decisionProblem.getId());
        //判断以前是否为关联性，是否有绑定的问题，有则删除以前绑定问题重新添加
        if (decisionProblemOld.getIsRelated().equals("Y")){
            //判断以前是否为从属性，是则不作操作，否则获取所关联的问题，在关联的问题所绑定的问题中去掉当前问题
            if (decisionProblemOld.getProblemAffiliation().equals("关联性")){
                //以前为关联性,判断绑定1个还是多个问题
                if (decisionProblemOld.getRelatedProblemIds().contains(",")){
                    List<String> idOldList = Arrays.asList(decisionProblemOld.getRelatedProblemIds().split(","));
                    for (String s : idOldList) {
                        DecisionProblem view = decisionProblemMapper.view(s);
                        if (view.getRelatedProblemIds().contains(",")){
                            List<String> idList = Arrays.asList(view.getRelatedProblemIds().split(","));
                            idList.remove(decisionProblem.getId());
                            List<String> nameList = Arrays.asList(view.getRelatedProblemNames().split(","));
                            nameList.remove(decisionProblem.getName());
                            //判断被关联问题所关联的问题是关联性还是从属从而确定问题从属关系的字段
                            for (String s1 : idList) {
                                DecisionProblem view1 = decisionProblemMapper.view(s1);
                                if (view1.getRelatedProblemIds()!=null&&view1.getRelatedProblemIds().contains(s1)){
                                    //说明是关联性 只有存在至少一个问题有关联该问题就为关联性
                                    view.setProblemAffiliation("关联性");
                                    break;
                                }else {
                                    //说明是从属性
                                    view.setProblemAffiliation("从属性");
                                }
                            }
                            String ids = String.join(",", idList);
                            String names = String.join(",", nameList);
                            view.setRelatedProblemIds(ids);
                            view.setRelatedProblemNames(names);

                            decisionProblemMapper.update(view);
                        }else {
                            //该问题只绑定了当前问题那么直接清空
                            view.setIsRelated("N");
                            view.setRelatedProblemIds(null);
                            view.setRelatedProblemNames(null);
                            view.setProblemAffiliation(null);
                            decisionProblemMapper.update(view);
                        }
                    }
                }else {
                    //绑定一个问题则去删除当前问题
                    DecisionProblem view = decisionProblemMapper.view(decisionProblemOld.getRelatedProblemIds());
                    if (view.getRelatedProblemIds().contains(",")){
                        List<String> idList = Arrays.asList(view.getRelatedProblemIds().split(","));
                        idList.remove(decisionProblem.getId());
                        List<String> nameList = Arrays.asList(view.getRelatedProblemNames().split(","));
                        nameList.remove(decisionProblem.getName());
                        //判断被关联问题所关联的问题是关联性还是从属从而确定问题从属关系的字段
                        for (String s1 : idList) {
                            DecisionProblem view1 = decisionProblemMapper.view(s1);
                            if (view1.getRelatedProblemIds()!=null&&view1.getRelatedProblemIds().contains(s1)){
                                //说明是关联性 只有存在至少一个问题有关联该问题就为关联性
                                view.setProblemAffiliation("关联性");
                                break;
                            }else {
                                //说明是从属性
                                view.setProblemAffiliation("从属性");
                            }
                        }
                        String ids = String.join(",", idList);
                        String names = String.join(",", nameList);
                        view.setRelatedProblemIds(ids);
                        view.setRelatedProblemNames(names);
                        decisionProblemMapper.update(view);
                    }else {
                        //该问题只绑定了当前问题那么直接清空
                        view.setIsRelated("N");
                        view.setRelatedProblemIds(null);
                        view.setRelatedProblemNames(null);
                        view.setProblemAffiliation(null);
                        decisionProblemMapper.update(view);
                    }
                }
            }
        }
        //添加新的
        if ("Y".equals(decisionProblem.getIsRelated())){
            //如果有关联其他问题则判断是从属性还是关联性
            String problemAffiliation = decisionProblem.getProblemAffiliation();
            if ("从属性".equals(problemAffiliation)){
                //说明单向绑定不必操作,直接添加
                decisionProblemMapper.update(decisionProblem);
            }else {
                //说明是关联性  需要双向绑定  在关联的问题中增加其关联问题数量
                //判断字段中是否存在",",存在则是多个问题，不存在则是单个
                if (decisionProblem.getRelatedProblemIds().contains(",")){
                    //包含则转为集合
                    List<String> idList = Arrays.asList(decisionProblem.getRelatedProblemIds().split(","));
                    for (String s : idList) {
                        DecisionProblem view = decisionProblemMapper.view(s);
                        //判断该问题是否当前有绑定问题
                        if (view.getIsRelated().equals("Y")){
                            //所绑定问题绑定的问题新增当前问题
                            view.setRelatedProblemIds(view.getRelatedProblemIds()+","+decisionProblem.getId());
                            view.setRelatedProblemNames(view.getRelatedProblemNames()+","+decisionProblem.getName());
                            decisionProblemMapper.update(view);
                        }else {
                            //说明之前所绑定的问题并未绑定其他问题
                            view.setIsRelated("Y");
                            view.setRelatedProblemIds(decisionProblem.getId());
                            view.setRelatedProblemNames(decisionProblem.getName());
                            decisionProblemMapper.update(view);
                        }
                    }
                    decisionProblemMapper.update(decisionProblem);
                }else {
                    //不包含则直接去修改所关联问题
                    DecisionProblem view = decisionProblemMapper.view(decisionProblem.getRelatedProblemIds());
                    //判断该问题是否当前有绑定问题
                    if (view.getIsRelated().equals("Y")){
                        //有的话则添加新的
                        //所绑定问题绑定的问题新增当前问题
                        view.setRelatedProblemIds(view.getRelatedProblemIds()+","+decisionProblem.getId());
                        view.setRelatedProblemNames(view.getRelatedProblemNames()+","+decisionProblem.getName());
                        decisionProblemMapper.update(view);
                        decisionProblemMapper.update(decisionProblem);
                    }else {
                        //说明之前所绑定的问题并未绑定其他问题
                        view.setIsRelated("Y");
                        view.setRelatedProblemIds(decisionProblem.getId());
                        view.setRelatedProblemNames(decisionProblem.getName());
                        decisionProblemMapper.update(view);
                        decisionProblemMapper.update(decisionProblem);
                    }
                }
            }
        }else {
            //未关联其他问题就不必操作,直接添加
            decisionProblemMapper.update(decisionProblem);
        }

        List<Proposal> proposals = proposalMapper.selectByDecisionProblemId(decisionProblem.getId());

        if (proposals!=null&&proposals.size()!=0){
            proposals.forEach(proposal -> {
                proposal.setDecisionProblemName(decisionProblem.getName());
                proposalMapper.update(proposal);
            });
        }

    }


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public DecisionProblem view(String id) {
        //获取决策问题详情
        DecisionProblem decisionProblem = decisionProblemMapper.view(id);
        //获取所有决策提案
        List<Proposal> proposals = proposalMapper.selectListByQuery(null);
        //如果暂无该问题的提案直接返回空
        if(proposals==null||proposals.size()==0){
            return decisionProblem;
        }
        List<String> ids = new ArrayList<>();
        for (Proposal proposal1 : proposals) {
            ids.add(proposal1.getId());
        }
        //id去重
        ids=ids.stream()
                .distinct()
                .collect(Collectors.toList());
        //通过id获取每条最新数据放入集合中
        List<Proposal> newLists = new ArrayList<>();
        for (String s : ids) {
            List<Proposal> view = proposalMapper.view(s);
            //将决策提案中状态为1--未入选的提案和与当前问题对应的提案添加到集合中
            if (view.get(0).getStatus().equals(DecisionStatusEnum.BE_NOT_SELECTED.getCode())&&decisionProblem.getName().equals(view.get(0).getDecisionProblemName())){
                newLists.add(view.get(0));
            }else if(view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())&&decisionProblem.getName().equals(view.get(0).getDecisionProblemName())){
                //为空说明决策问题未选择提案;不为空则将决策问题所选定的提案添加到实体类中
                decisionProblem.setProposal(view.get(0));
            }else if(view.get(0).getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ED.getCode())&&decisionProblem.getName().equals(view.get(0).getDecisionProblemName())){
                //为空说明决策问题未选择提案;不为空则将决策问题所选定的提案添加到实体类中
                decisionProblem.setProposal(view.get(0));
            }
        }
        //将所有未有决策问题选定的决策提案添加到实体类;作当前决策问题的备份。
        decisionProblem.setProposals(newLists);
        return decisionProblem;
    }

    @Override
    public void designate(String id,String proposalId) {
        //获取当前问题
        DecisionProblem decisionProblem = decisionProblemMapper.view(id);
        if (decisionProblem.getStatus().equals(DecisionStatusEnum.Decision_PROPOSAL_ED.getCode())){
            throw  new SofnException("该问题已完成提问,不可更改提案");
        }
        //判断当前问题状态是否为提问中(即未选定提案),是则改为提案进行中
        if (decisionProblem.getStatus().equals(DecisionStatusEnum.ASKING.getCode())){
            decisionProblem.setStatus(DecisionStatusEnum.Decision_PROPOSAL_ING.getCode());
            decisionProblemMapper.update(decisionProblem);
        }

        //判断当前问题是否已选定提案,已选定则修改旧提案状态
        List<Proposal> proposals = proposalMapper.selectByDecisionProblemId(id);
        if (proposals!=null&&proposals.size()!=0){
            for (Proposal proposal : proposals) {
                if (proposal.getDecisionProblemId().equals(id) && proposal.getStatus().equals(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode())) {
                    proposal.setStatus(DecisionStatusEnum.BE_NOT_SELECTED.getCode());
                    proposalMapper.update(proposal);
                }
            }
        }
        List<Proposal> view = proposalMapper.view(proposalId);
        Proposal proposal = view.get(0);
        proposal.setDecisionProblemId(id);
        proposal.setDecisionProblemName(decisionProblem.getName());
        proposal.setStatus(DecisionStatusEnum.PROPOSAL_PROPOSAL_ING.getCode());
        proposalMapper.update(proposal);

    }
}