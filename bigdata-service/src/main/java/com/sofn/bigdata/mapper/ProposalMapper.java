package com.sofn.bigdata.mapper;

import com.sofn.bigdata.model.Proposal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProposalMapper {

    /**
     * 通过id删除该决策提案
     *
     * @param id
     */
    void removeById(String id);

    /**
     * 更新决策提案
     *
     * @param proposal
     */
    void update(Proposal proposal);

    /**
     * 添加决策提案
     * @param proposal
     */
    void insert(Proposal proposal);

    /**
     * 详情
     *
     * @param id
     */
    List<Proposal> view(String id);


    /**
     * 分页查询+条件查询
     *
     * @param params
     * @return
     */
    List<Proposal> selectListByQuery(Map params);


    /**
     * 通过决策问题id查找提案信息
     * @param decisionProblemId
     * @return
     */
    List<Proposal>  selectByDecisionProblemId(String decisionProblemId);
}
