package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.Knowledge;
import com.sofn.bigdata.model.MyKnowledge;
import com.sofn.bigdata.model.RelatedKnowledge;
import com.sofn.bigdata.model.RelatedKnowledges;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author gouliangcheng
 * @Description 知识整合模块mapper
 * @Date 15:07 2020/8/10
 * @Param
 * @return
 **/
public interface KnowledgeMapper extends BaseMapper<Knowledge> {

    /*
     * @Author gouliangcheng
     * @Description 模糊搜索知识点信息
     * @Date 15:07 2020/8/10
     * @Param [knowledgeName] 知识点名称
     * @return java.util.List<com.sofn.bigdata.model.MyKnowledge> 知识点返回实体类的列表
     **/
    List<MyKnowledge> fuzzySearchKnowledge(@Param(value = "knowledgeName") String knowledgeName);

    /*
     * @Author gouliangcheng
     * @Description 模糊搜索已收藏知识点信息
     * @Date 15:07 2020/8/10
     * @Param [paramas] 用户id，知识点id
     * @return java.util.List<com.sofn.bigdata.model.MyKnowledge> 知识点返回实体类的列表
     **/
    List<MyKnowledge> fuzzySearchCollectKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 收藏知识点
     * @Date 15:07 2020/8/10
     * @Param [paramas] 用户id，知识点id
     * @return void
     **/
    void collectKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 取消收藏知识点
     * @Date 15:10 2020/8/10
     * @Param [paramas] 用户id，知识点id
     * @return void
     **/
    void cancelCollectKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 插入知识点信息
     * @Date 15:14 2020/8/10
     * @Param [paramas]
     * @return void
     **/
    void insertKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 插入知识点的附件信息
     * @Date 15:15 2020/8/10
     * @Param [paramas] 知识点id，附件名称，附件地址
     * @return void
     **/
    void insertAttachments(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 获取知识点的关联信息
     * @Date 15:15 2020/8/10
     * @Param [paramas] 知识点id，关联层级
     * @return java.util.List<com.sofn.bigdata.model.RelatedKnowledge> 知识点关联实体类的列表
     **/
    List<RelatedKnowledge> getShow(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 获取知识点基本信息
     * @Date 15:17 2020/8/10
     * @Param [paramas] 知识点id
     * @return com.sofn.bigdata.model.Knowledge 知识点返回实体类
     **/
    Knowledge getKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 获取知识点关联列表(带分页)
     * @Date 15:17 2020/8/10
     * @Param [paramas] 知识点id，起始页码，分页大小，层级信息
     * @return java.util.List<com.sofn.bigdata.model.RelatedKnowledge> 知识点关系实体类的列表
     **/
    List<RelatedKnowledges> getKnowledgeList(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 关联知识点信息
     * @Date 15:19 2020/8/10
     * @Param [paramas] 主知识点id，关联知识点id，关联层级信息
     * @return void
     **/
    void associatedKnowledge(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 查看是否已经收藏
     * @Date 15:20 2020/8/10
     * @Param [paramas] 知识点id，用户id
     * @return java.lang.Integer 1/0，代表是/否
     **/
    Integer getKnowledgeCol(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 获取该知识点的收藏总数
     * @Date 15:22 2020/8/10
     * @Param [paramas] 知识点id
     * @return java.lang.Integer 该知识点已经收藏的数量
     **/
    Integer getKnowledgeColCount(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 可关联知识点查询
     * @Date 15:57 2020/8/26
     * @Param 知识点等级
     * @return java.util.List<RelatedKnowledge>
     **/
    List<MyKnowledge> getAbleKnowledgeList(Map<String, Object> paramas);

    /*
     * @Author gouliangcheng
     * @Description 通过id查询等级
     * @Date 16:39 2020/8/26
     * @Param [knowledgeId]
     * @return java.lang.Integer
     **/
    Integer getKnowledgeLevel(@Param(value = "knowledgeId") String knowledgeId);

    /*
     * @Author gouliangcheng
     * @Description 同名检测
     * @Date 14:29 2020/8/27
     * @Param [knowledgeName]
     * @return Integer
     **/
    Integer isSameName(@Param(value = "knowledgeName") String knowledgeName);

    /**
     * 分页总条数查询
     * @param paramas
     * @return
     */
    Integer getKnowledgeLimitCount(HashMap<String, Object> paramas);
	
	/**
     * 分页总条数查询分类
     * @param paramas
     * @return
     */
	Integer getKnowledgeLimitCounts(HashMap<String, Object> paramas);

    /**
     * 收藏知识模糊总条数查询
     * @param paramas
     * @return
     */
    Integer fuzzySearchCollectKnowledgeCount(HashMap<String, Object> paramas);
}
