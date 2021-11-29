package com.sofn.bigdata.service;

import com.sofn.bigdata.model.Knowledge;
import com.sofn.bigdata.model.MyKnowledge;
import com.sofn.bigdata.model.RelatedKnowledge;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.vo.KnowledgeVo;
import com.sofn.bigdata.vo.RelatedKnowledgeVo;

import java.util.HashMap;
import java.util.List;

/*
 * @Author gouliangcheng
 * @Description 知识整合模块服务层
 * @Date 13:52 2020/8/3
 **/
public interface KnowledgeService {
    String collectKnowledge(String loginUserId, String knowledgeId);

    String cancelCollectKnowledge(String loginUserId, String knowledgeId);

    String insertAttachments(String loginUserId, String knowledgeId, String fileName, String id);

    HashMap<String, Object> insertKnowledge(String loginUserId, KnowledgeVo knowledgeVo);

    Result<List<RelatedKnowledge>> getShow(String knowledgeId);

    Result<HashMap<String, Object>> getKnowledgeList(int pageNo, int pageSize, String knowledgeId);

    Result<List<MyKnowledge>> fuzzySearchKnowledge(String knowledgeName);

    Result<HashMap<String, Object>> fuzzySearchCollectKnowledge(String loginUserId, int pageNo, int pageSize, String knowledgeName);

    String associatedKnowledge(String loginUserId, RelatedKnowledgeVo relatedKnowledgeVo);

    Result<Knowledge> getKnowledge(String loginUserId, String knowledgeId);

    Result<List<MyKnowledge>> getAbleKnowledgeList(Integer level, String flag);

    Integer isSameName(String knowledgeName);

    String isCollectKnowledge(String loginUserId, String knowledgeId);
}
