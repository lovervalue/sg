package com.sofn.bigdata.mapper;

import com.sofn.bigdata.model.DecisionProblem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DecisionProblemMapper {

    /**
     * 通过id删除该决策问题
     *
     * @param id
     */
    void removeById(String id);

    /**
     * 更新决策问题
     *
     * @param decisionProblem
     */
    void update(DecisionProblem decisionProblem);

    /**
     * 添加决策问题
     * @param decisionProblem
     */
    void insert(DecisionProblem decisionProblem);

    /**
     * 详情
     *
     * @param id
     */
    DecisionProblem view(String id);


    /**
     * 分页查询+条件查询
     *
     * @param params
     * @return
     */
    List<DecisionProblem> selectListByQuery(Map params);





}
