package com.sofn.bigdata.service.impl;

import com.sofn.bigdata.mapper.KnowledgeMapper;
import com.sofn.bigdata.model.*;
import com.sofn.bigdata.service.KnowledgeService;
import com.sofn.bigdata.vo.KnowledgeVo;
import com.sofn.bigdata.vo.RelatedKnowledgeVo;
import com.sofn.common.utils.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName KnowledgeServiceImpl
 * @Description 知识整合实现层
 * @Author gouliangcheng
 * @Date 2020/8/4 10:56
 * @Version 1.0
 **/
@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    /*
     * @return java.lang.String
     * @Author gouliangcheng
     * @Description 收藏知识点
     * @Date 14:51 2020/8/10
     * @Param [loginUserId, knowledgeId]
     **/
    @Override
    public String collectKnowledge(String loginUserId, String knowledgeId) {
        if (StringUtils.isNotEmpty(loginUserId) && StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("id", IdUtil.getUUId());
            paramas.put("loginUser_id", loginUserId);
            paramas.put("knowledge_id", knowledgeId);
            paramas.put("create_time", new Date());
            Integer colFlag = knowledgeMapper.getKnowledgeCol(paramas);
            // 判断是否已经收藏
            if (colFlag == 0) {
                // 未收藏，添加
                knowledgeMapper.collectKnowledge(paramas);
                return String.valueOf(1);
            }
        }
        return null;
    }

    /*
     * @Author gouliangcheng
     * @Description 取消收藏
     * @Date 14:52 2020/8/10
     * @Param [loginUserId, knowledgeId]
     * @return java.lang.String
     **/
    @Override
    public String cancelCollectKnowledge(String loginUserId, String knowledgeId) {
        if (StringUtils.isNotEmpty(loginUserId) && StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("loginUserId", loginUserId);
            paramas.put("knowledgeId", knowledgeId);
            knowledgeMapper.cancelCollectKnowledge(paramas);
            return String.valueOf(1);
        }
        return null;
    }

    /*
     * @Author gouliangcheng
     * @Description 插入附件信息至对应知识点
     * @Date 14:53 2020/8/10
     * @Param [loginUserId, knowledgeId, fileName, id]
     * @return java.lang.String
     **/
    @Override
    public String insertAttachments(String loginUserId, String knowledgeId, String fileName, String id) {
        if (StringUtils.isNotEmpty(loginUserId) && StringUtils.isNotEmpty(knowledgeId) && StringUtils.isNotEmpty(id)) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("knowledgeId", knowledgeId);
            paramas.put("attachment_name", fileName);
            paramas.put("attachment_address", id);
            paramas.put("update_user_id", loginUserId);
            paramas.put("update_time", new Date());
            knowledgeMapper.insertAttachments(paramas);
            return String.valueOf(1);
        }
        return null;
    }

    /*
     * @Author gouliangcheng
     * @Description 新增知识点
     * @Date 14:54 2020/8/10
     * @Param [loginUserId, knowledgeVo]
     * @return java.lang.String
     **/
    @Override
    public HashMap<String, Object> insertKnowledge(String loginUserId, KnowledgeVo knowledgeVo) {
        if (knowledgeVo != null) {
            HashMap<String, Object> paramas = new HashMap<>();
            HashMap<String, Object> paramas1 = new HashMap<>();
            String uuId = IdUtil.getUUId();
            paramas.put("id", uuId);
            paramas.put("knowledge_name", knowledgeVo.getKnowledgeName());
            paramas.put("author", knowledgeVo.getAuthor());
            paramas.put("abstracts", knowledgeVo.getAbstracts());
            paramas.put("keyword", knowledgeVo.getKeyword());
            paramas.put("text", knowledgeVo.getText());
            paramas.put("level", knowledgeVo.getLevel());
            paramas.put("create_time", new Date());
            paramas.put("create_user_id", loginUserId);
            paramas.put("create_time_sys", knowledgeVo.getCreateTime());
            paramas.put("create_user", knowledgeVo.getCreateUser());
            knowledgeMapper.insertKnowledge(paramas);
            paramas1.put("id", uuId);
            paramas1.put("level", knowledgeVo.getLevel());
            return paramas1;
        }
        return null;
    }

    /**
     * getshow专用调换位置方法
     *
     * @return
     */
    private List<RelatedKnowledge> transposition(List<RelatedKnowledge> relatedKnowledges, String knowledgeId) {
        for (RelatedKnowledge relatedKnowledge : relatedKnowledges) {
            if (relatedKnowledge.getRelatedknowledgeId().equals(knowledgeId)) {
                relatedKnowledge.setAssociatedFlag("L");
                String relatedknowledgeId = relatedKnowledge.getKnowledgeId();
                String relatedknowledgeName = relatedKnowledge.getKnowledgeName();
                relatedKnowledge.setKnowledgeId(relatedKnowledge.getRelatedknowledgeId());
                relatedKnowledge.setKnowledgeName(relatedKnowledge.getRelatedknowledgeName());
                relatedKnowledge.setRelatedknowledgeId(relatedknowledgeId);
                relatedKnowledge.setRelatedknowledgeName(relatedknowledgeName);
            }
        }
        return relatedKnowledges;
    }

    /*
     * @Author gouliangcheng
     * @Description 通过知识点id获取知识网络，最多返回三层信息
     * @Date 14:55 2020/8/10
     * @Param [knowledgeId]
     * @return com.sofn.bigdata.model.Result<java.util.List<com.sofn.bigdata.model.RelatedKnowledge>>
     **/
    @Override
    public Result<List<RelatedKnowledge>> getShow(String knowledgeId) {
        // S 上下级,P 平级,L 下上级
        if (StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> sParamas = new HashMap<>();
            HashMap<String, Object> lParamas = new HashMap<>();
            HashMap<String, Object> pParamas = new HashMap<>();
            sParamas.put("associatedFlag", "S");
            lParamas.put("associatedFlag", "L");
            pParamas.put("associatedFlag", "P");
            sParamas.put("knowledgeId", knowledgeId);
            lParamas.put("knowledgeId", knowledgeId);
            pParamas.put("knowledgeId", knowledgeId);
            List<RelatedKnowledge> sShowList = knowledgeMapper.getShow(sParamas);
            List<RelatedKnowledge> lShowList = knowledgeMapper.getShow(lParamas);
            List<RelatedKnowledge> pShowList = knowledgeMapper.getShow(pParamas);
            // 初始返回数组
            ArrayList<RelatedKnowledge> relatedKnowledges = new ArrayList<>(transposition(pShowList, knowledgeId));
            if (sShowList.size() != 0) {
                // 有下级，找上级
                relatedKnowledges.addAll(transposition(sShowList, knowledgeId));
                if (lShowList.size() != 0) {
                    // 有上级
                    relatedKnowledges.addAll(transposition(lShowList, knowledgeId));
                } else {
                    // 无上级，找下下级
                    HashSet<String> sStrings = new HashSet<>();
                    for (RelatedKnowledge rk : sShowList) {
                        sStrings.add(rk.getKnowledgeId());
                        sStrings.add(rk.getRelatedknowledgeId());
                    }
                    // 去重
                    sStrings.remove(knowledgeId);
                    for (String rk : sStrings) {
                        HashMap<String, Object> ssParamas = new HashMap<>();
                        ssParamas.put("associatedFlag", "S");
                        ssParamas.put("knowledgeId", rk);
                        List<RelatedKnowledge> ssShowList = knowledgeMapper.getShow(ssParamas);
                        if (ssShowList.size() != 0)
                            relatedKnowledges.addAll(transposition(ssShowList, rk));
                    }
                }
            } else {
                // 无下级，找上级
                if (lShowList.size() != 0) {
                    // 有上级，找上上级
                    relatedKnowledges.addAll(transposition(lShowList, knowledgeId));
                    HashSet<String> lStrings = new HashSet<>();
                    for (RelatedKnowledge rk : lShowList) {
                        lStrings.add(rk.getKnowledgeId());
                        lStrings.add(rk.getRelatedknowledgeId());
                    }
                    // 去重
                    lStrings.remove(knowledgeId);
                    for (String rk : lStrings) {
                        HashMap<String, Object> llParamas = new HashMap<>();
                        llParamas.put("associatedFlag", "L");
                        llParamas.put("knowledgeId", rk);
                        List<RelatedKnowledge> llShowList = knowledgeMapper.getShow(llParamas);
                        if (llShowList.size() != 0)
                            relatedKnowledges.addAll(transposition(llShowList, rk));
                    }
                } else {
                    // 无上级，找平级的上下级
                    HashSet<String> pStrings = new HashSet<>();
                    for (RelatedKnowledge rk : pShowList) {
                        pStrings.add(rk.getKnowledgeId());
                        pStrings.add(rk.getRelatedknowledgeId());
                    }
                    // 去重
                    pStrings.remove(knowledgeId);
                    for (String rk : pStrings) {
                        HashMap<String, Object> plParamas = new HashMap<>();
                        plParamas.put("associatedFlag", "L");
                        plParamas.put("knowledgeId", rk);
                        List<RelatedKnowledge> plShowList = knowledgeMapper.getShow(plParamas);
                        if (plShowList.size() != 0)
                            relatedKnowledges.addAll(transposition(plShowList, rk));
                        HashMap<String, Object> psParamas = new HashMap<>();
                        psParamas.put("associatedFlag", "S");
                        psParamas.put("knowledgeId", rk);
                        List<RelatedKnowledge> psShowList = knowledgeMapper.getShow(psParamas);
                        if (psShowList.size() != 0)
                            relatedKnowledges.addAll(transposition(psShowList, rk));
                    }
                }
            }
            return new Result<>(200, relatedKnowledges);
        }
        return new Result<>(500, null);
    }

    /**
     * 查询不同级信息
     * 暂时不用
     *
     * @return
     */
//    private HashMap<String, Object> queryRatingInformation(int pageNo, int pageSize, String knowledgeId, String flag) {
//        HashMap<String, Object> returnParamas = new HashMap<>();
//        HashMap<String, Object> paramas = new HashMap<>();
//        paramas.put("associatedFlag", flag);
//        paramas.put("knowledgeId", knowledgeId);
//        paramas.put("pageStart", (pageNo > 0) ? (pageNo - 1) * pageSize : 0);
//        paramas.put("pageSize", pageSize);
//        List<RelatedKnowledges> knowledgeList = knowledgeMapper.getKnowledgeList(paramas);
//        //  整理排列顺序
//        ArrayList<RelatedKnowledge> relatedKnowledgeList = new ArrayList<>();
//        RelatedKnowledge relatedKnowledge = new RelatedKnowledge();
//        for (RelatedKnowledges relatedKnowledges : knowledgeList) {
//            if (knowledgeId.equals(relatedKnowledges.getKnowledgeId())) {
//                relatedKnowledge.setKnowledgeId(relatedKnowledges.getKnowledgeId());
//                relatedKnowledge.setKnowledgeName(relatedKnowledges.getKnowledgeName());
//                relatedKnowledge.setRelatedknowledgeId(relatedKnowledges.getRelatedknowledgeId());
//                relatedKnowledge.setRelatedknowledgeName(relatedKnowledges.getRelatedknowledgeName());
//                relatedKnowledge.setAuthor(relatedKnowledges.getAuthor());
//                relatedKnowledge.setAbstracts(relatedKnowledges.getAbstracts());
//                relatedKnowledge.setAssociatedFlag(relatedKnowledges.getAssociatedFlag());
//            } else {
//                relatedKnowledge.setKnowledgeId(relatedKnowledges.getRelatedknowledgeId());
//                relatedKnowledge.setKnowledgeName(relatedKnowledges.getRelatedknowledgeName());
//                relatedKnowledge.setRelatedknowledgeId(relatedKnowledges.getKnowledgeId());
//                relatedKnowledge.setRelatedknowledgeName(relatedKnowledges.getKnowledgeName());
//                relatedKnowledge.setAuthor(relatedKnowledges.getRelatedAuthor());
//                relatedKnowledge.setAbstracts(relatedKnowledges.getRelatedAbstracts());
//                if ("S".equals(relatedKnowledges.getAssociatedFlag())) {
//                    relatedKnowledge.setAssociatedFlag("L");
//                } else if ("L".equals(relatedKnowledges.getAssociatedFlag())) {
//                    relatedKnowledge.setAssociatedFlag("S");
//                } else {
//                    relatedKnowledge.setAssociatedFlag(relatedKnowledges.getAssociatedFlag());
//                }
//            }
//            relatedKnowledgeList.add(relatedKnowledge);
//        }
//        Integer totalCount = knowledgeMapper.getKnowledgeLimitCount(paramas);
//        returnParamas.put("totalCount", totalCount);
//        returnParamas.put("data", relatedKnowledgeList);
//        return returnParamas;
//    }

    /*
     * @Author gouliangcheng
     * @Description 通过知识点获取上级下级平级的信息（带分页）
     * @Date 14:56 2020/8/10
     * @Param [pageNo, pageSize, knowledgeId]
     * @return com.sofn.bigdata.model.Result<java.util.List<com.sofn.bigdata.model.RelatedKnowledge>>
     **/
    @Override
    public Result<HashMap<String, Object>> getKnowledgeList(int pageNo, int pageSize, String knowledgeId) {
        if (StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> returnMap = new HashMap<>();
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("knowledgeId", knowledgeId);
            paramas.put("pageStart", (pageNo > 0) ? (pageNo - 1) * pageSize : 0);
            paramas.put("pageSize", pageSize);
            List<RelatedKnowledges> knowledgeList = knowledgeMapper.getKnowledgeList(paramas);

            //  整理排列顺序
            ArrayList<RelatedKnowledge> relatedKnowledgeList = new ArrayList<>();
            for (RelatedKnowledges relatedKnowledges : knowledgeList) {
                RelatedKnowledge relatedKnowledge = new RelatedKnowledge();
                if (knowledgeId.equals(relatedKnowledges.getKnowledgeId())) {
                    relatedKnowledge.setKnowledgeId(relatedKnowledges.getKnowledgeId());
                    relatedKnowledge.setKnowledgeName(relatedKnowledges.getKnowledgeName());
                    relatedKnowledge.setRelatedknowledgeId(relatedKnowledges.getRelatedknowledgeId());
                    relatedKnowledge.setRelatedknowledgeName(relatedKnowledges.getRelatedknowledgeName());
                    relatedKnowledge.setAuthor(relatedKnowledges.getAuthor());
                    relatedKnowledge.setAbstracts(relatedKnowledges.getAbstracts());
                    relatedKnowledge.setAssociatedFlag(relatedKnowledges.getAssociatedFlag());
                } else {
                    relatedKnowledge.setKnowledgeId(relatedKnowledges.getRelatedknowledgeId());
                    relatedKnowledge.setKnowledgeName(relatedKnowledges.getRelatedknowledgeName());
                    relatedKnowledge.setRelatedknowledgeId(relatedKnowledges.getKnowledgeId());
                    relatedKnowledge.setRelatedknowledgeName(relatedKnowledges.getKnowledgeName());
                    relatedKnowledge.setAuthor(relatedKnowledges.getRelatedAuthor());
                    relatedKnowledge.setAbstracts(relatedKnowledges.getRelatedAbstracts());
                    if ("S".equals(relatedKnowledges.getAssociatedFlag())) {
                        relatedKnowledge.setAssociatedFlag("L");
                    } else if ("L".equals(relatedKnowledges.getAssociatedFlag())) {
                        relatedKnowledge.setAssociatedFlag("S");
                    } else {
                        relatedKnowledge.setAssociatedFlag(relatedKnowledges.getAssociatedFlag());
                    }
                }
                relatedKnowledgeList.add(relatedKnowledge);
            }

            Integer totalCount = knowledgeMapper.getKnowledgeLimitCount(paramas);
            returnMap.put("totalCount", totalCount);
            returnMap.put("data", relatedKnowledgeList);
            return new Result<>(200, returnMap);
        }
        return new Result<>(500, null);
    }

    /*
     * @Author gouliangcheng
     * @Description 模糊查询知识点
     * @Date 14:57 2020/8/10
     * @Param [knowledgeName]
     * @return com.sofn.bigdata.model.Result<java.util.List<com.sofn.bigdata.model.MyKnowledge>>
     **/
    @Override
    public Result<List<MyKnowledge>> fuzzySearchKnowledge(String knowledgeName) {
        return new Result<>(200, knowledgeMapper.fuzzySearchKnowledge(knowledgeName));
    }

    /*
     * @Author gouliangcheng
     * @Description 模糊查询已收藏知识点（带分页）
     * @Date 14:58 2020/8/10
     * @Param [loginUserId, pageNo, pageSize, knowledgeName]
     * @return com.sofn.bigdata.model.Result<java.util.List<com.sofn.bigdata.model.MyKnowledge>>
     **/
    @Override
    public Result<HashMap<String, Object>> fuzzySearchCollectKnowledge(String loginUserId, int pageNo,
                                                                       int pageSize, String knowledgeName) {
        HashMap<String, Object> paramas = new HashMap<>();
        HashMap<String, Object> returnParames = new HashMap<>();
        paramas.put("loginUserId", loginUserId);
        paramas.put("pageStart", (pageNo > 0) ? (pageNo - 1) * pageSize : 0);
        paramas.put("pageSize", pageSize);
        paramas.put("knowledgeName", knowledgeName);
        List<MyKnowledge> myKnowledges = knowledgeMapper.fuzzySearchCollectKnowledge(paramas);
        Integer totalCount = knowledgeMapper.fuzzySearchCollectKnowledgeCount(paramas);
        returnParames.put("totalCount", totalCount);
        returnParames.put("data", myKnowledges);
        return new Result<>(200, returnParames);
    }

    /*
     * @Author gouliangcheng
     * @Description 新增知识点关联（需要修改，不允许跨级关联）
     * @Date 14:59 2020/8/10
     * @Param [loginUserId, relatedKnowledgeVo]
     * @return java.lang.String
     **/
    @Override
    public String associatedKnowledge(String loginUserId, RelatedKnowledgeVo relatedKnowledgeVo) {
        if (StringUtils.isNotEmpty(relatedKnowledgeVo.getKnowledgeId()) && StringUtils.isNotEmpty(relatedKnowledgeVo.getRelatedknowledgeId())) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("id", IdUtil.getUUId());
            paramas.put("knowledge_id", relatedKnowledgeVo.getKnowledgeId());
            paramas.put("knowledge_next_id", relatedKnowledgeVo.getRelatedknowledgeId());
            Integer level = knowledgeMapper.getKnowledgeLevel(relatedKnowledgeVo.getKnowledgeId());
            Integer level1 = knowledgeMapper.getKnowledgeLevel(relatedKnowledgeVo.getRelatedknowledgeId());
            if (Math.abs(level - level1) > 1) {
                return null;
            }
            String associatedFlag = level < level1 ? "S" : level.equals(level1) ? "P" : "L";
            paramas.put("associated_flag", associatedFlag);
            paramas.put("create_user_id", loginUserId);
            paramas.put("create_time", new Date());
            knowledgeMapper.associatedKnowledge(paramas);
            return String.valueOf(1);
        }
        return null;
    }

    /*
     * @Author gouliangcheng
     * @Description 获取知识点详细信息
     * @Date 15:03 2020/8/10
     * @Param [loginUserId, knowledgeId]
     * @return com.sofn.bigdata.model.Result<com.sofn.bigdata.model.Knowledge>
     **/
    @Override
    public Result<Knowledge> getKnowledge(String loginUserId, String knowledgeId) {
        if (StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("loginUser_id", loginUserId);
            paramas.put("knowledge_id", knowledgeId);
            Knowledge knowledge = knowledgeMapper.getKnowledge(paramas);
            Integer colFlag = knowledgeMapper.getKnowledgeCol(paramas);
            Integer colCount = knowledgeMapper.getKnowledgeColCount(paramas);
            knowledge.setColFlag(colFlag == 1);
            knowledge.setCollections(colCount);
            return new Result<>(200, knowledge);
        }
        return new Result<>(500, null);
    }

    /*
     * @Author gouliangcheng
     * @Description 可关联知识点查询
     * @Date 15:51 2020/8/26
     * @Param [knowledgeId]
     * @return com.sofn.bigdata.model.Result<String>>
     **/
    @Override
    public Result<List<MyKnowledge>> getAbleKnowledgeList(Integer level, String flag) {
        if (StringUtils.isNotEmpty(flag) || flag.trim().equals("S") && flag.trim().equals("L") && flag.trim().equals("P")) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("level", level);
            paramas.put("flag", flag);
            return new Result<>(200, knowledgeMapper.getAbleKnowledgeList(paramas));
        }
        return new Result<>(500, null);
    }

    /*
     * @Author gouliangcheng
     * @Description 同名检测
     * @Date 14:28 2020/8/27
     * @Param [knowledgeName]
     * @return Integer
     **/
    @Override
    public Integer isSameName(String knowledgeName) {
        return knowledgeMapper.isSameName(knowledgeName);
    }

    /*
     * @Author gouliangcheng
     * @Description 查询是否已收藏
     * @Date 12:50 2020/9/10
     * @Param [loginUserId, knowledgeId]
     * @return java.lang.String
     **/
    @Override
    public String isCollectKnowledge(String loginUserId, String knowledgeId) {
        if (StringUtils.isNotEmpty(knowledgeId)) {
            HashMap<String, Object> paramas = new HashMap<>();
            paramas.put("loginUser_id", loginUserId);
            paramas.put("knowledge_id", knowledgeId);
            Integer colFlag = knowledgeMapper.getKnowledgeCol(paramas);
            // 判断是否已经收藏
            if (colFlag == 1) {
                return String.valueOf(1);
            }
        }
        return null;
    }
}
