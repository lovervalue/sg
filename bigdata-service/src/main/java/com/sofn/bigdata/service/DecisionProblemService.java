package com.sofn.bigdata.service;


import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.DecisionProblem;


public interface DecisionProblemService {
    /**
     * 所有决策问题列表分页及条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param name 决策问题名称
     * @param status  决策问题状态
     * @return
     */
    PageInfo<DecisionProblem> selectListByQuery(Integer pageNo, Integer pageSize, String name, String status);

    /**
     * 添加决策问题
     *
     * @param decisionProblem
     */
    void insertDecisionProblem(DecisionProblem decisionProblem);

    /**
     * 删除
     *
     * @param id
     */
    void remove(String id);

    /**
     * 更新
     *
     * @param decisionProblem
     */
    void update(DecisionProblem decisionProblem);

    /**
     * 详情
     *
     * @param id
     */
    DecisionProblem view(String id);

    /**
     * 问题选定提案
     * @param id
     * @param proposalId
     */
    void designate(String id,String proposalId);
}
