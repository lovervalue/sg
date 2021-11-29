package com.sofn.bigdata.service;


import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.model.Proposal;
import com.sofn.bigdata.vo.DataAnalyzeVo;

import java.util.Date;
import java.util.List;


public interface ProposalService {
    /**
     * 所有决策提案列表分页及条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param name 决策提案名称
     * @param status  决策提案状态
     * @return
     */
    PageInfo<Proposal> selectListByQuery(Integer pageNo, Integer pageSize, String name, String status);

    /**
     * 添加决策提案
     *
     * @param proposal
     */
    void insertProposal(Proposal proposal);

    /**
     * 删除
     *
     * @param id
     */
    void remove(String id);

    /**
     * 更新
     *
     * @param proposal
     */
    void update(Proposal proposal);

    /**
     * 详情
     *
     * @param id
     */
    List<Proposal> view(String id);

    /**
     * 批准
     * @param id
     */
    void approver(String id);

    /**
     * 批注
     * @param id
     * @param annotation
     */
    void insertAnnotation(String id, String annotation);

    /**
     * 获取多维分析数据所有数据
     * @return
     */
    List<DataAnalyze> getDataAnalyze();
}
