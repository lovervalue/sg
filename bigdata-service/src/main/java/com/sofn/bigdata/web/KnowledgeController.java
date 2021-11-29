package com.sofn.bigdata.web;

import com.sofn.bigdata.model.Knowledge;
import com.sofn.bigdata.model.MyKnowledge;
import com.sofn.bigdata.model.RelatedKnowledge;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.service.KnowledgeService;
import com.sofn.bigdata.service.KnowledgeFileService;
import com.sofn.bigdata.vo.KnowledgeVo;

import com.sofn.bigdata.vo.RelatedKnowledgeVo;
import com.sofn.bigdata.vo.SysFileManageForm;
import com.sofn.bigdata.vo.SysFileVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.utils.UserUtil;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author gouliangcheng
 * @Description 知识整合模块
 * @Date 13:53 2020/8/3
 **/
@Slf4j
@Api(value = "知识整合", tags = "知识整合")
@RequestMapping("/knowledge")
@RestController
public class KnowledgeController extends BaseController {

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private KnowledgeFileService knowledgeFileService;

    @SofnLog("模糊搜索知识点")
    @ApiOperation(value = "模糊搜索知识点(新增+知识网络使用)")
    @PostMapping("/fuzzySearchKnowledge")
    public Result<List<MyKnowledge>> fuzzySearchKnowledge(@RequestParam(value = "knowledgeName", required = false, defaultValue = "") @ApiParam(name = "knowledgeName", value = "知识点,（非必传）", required = false) String knowledgeName) {
        return knowledgeService.fuzzySearchKnowledge(knowledgeName);
    }

//    @SofnLog("我的知识+模糊搜索收藏知识点")
//    @ApiOperation(value = "我的知识+模糊搜索收藏知识点(支持分页)")
//    @PostMapping("/fuzzySearchCollectKnowledCge")
//    public Result<List<MyKnowledge>> fuzzySearchCollectKnowledCge(@RequestParam(value = "knowledgeName", required = false, defaultValue = "") @ApiParam(name = "knowledgeName", value = "知识点,（非必传）", required = false) String knowledgeName,
//                                                                  @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam(name = "pageNo", value = "页码,（非必传,默认为1）", required = false) int pageNo,
//                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", value = "分页大小,（非必传,默认为10）", required = false) int pageSize) {
//        String loginUserId = UserUtil.getLoginUserId();
//        return knowledgeService.fuzzySearchCollectKnowledge(loginUserId, pageNo, pageSize, knowledgeName);
//    }

    @SofnLog("我的知识+模糊搜索收藏知识点")
    @ApiOperation(value = "我的知识+模糊搜索收藏知识点(支持分页)")
    @PostMapping("/fuzzySearchCollectKnowledCge")
    public Result<HashMap<String, Object>> fuzzySearchCollectKnowledCge(@RequestParam(value = "knowledgeName", required = false, defaultValue = "") @ApiParam(name = "knowledgeName", value = "知识点,（非必传）", required = false) String knowledgeName,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam(name = "pageNo", value = "页码,（非必传,默认为1）", required = false) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", value = "分页大小,（非必传,默认为10）", required = false) int pageSize) {
        String loginUserId = UserUtil.getLoginUserId();
        return knowledgeService.fuzzySearchCollectKnowledge(loginUserId, pageNo, pageSize, knowledgeName);
    }


    @SofnLog("查询是否已收藏")
    @ApiOperation(value = "查询是否已收藏")
    @PostMapping("/isCollectKnowledge")
    public Result<Object> isCollectKnowledge(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId) {
        try {
            String loginUserId = UserUtil.getLoginUserId();
            String s = knowledgeService.isCollectKnowledge(loginUserId, knowledgeId);
            Result<Object> result = new Result<>(200);
            if (StringUtils.isNotEmpty(s)) {
                result.setMsg("已收藏");
                result.setData(true);
                return result;
            }
            result.setData(false);
            result.setMsg("未收藏");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "查询失败: " + e.getMessage());
        }
    }

    @SofnLog("收藏")
    @ApiOperation(value = "收藏")
    @PostMapping("/collectKnowledge")
    public Result<String> collectKnowledge(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId) {
        try {
            String loginUserId = UserUtil.getLoginUserId();

            String s = knowledgeService.collectKnowledge(loginUserId, knowledgeId);
            if (StringUtils.isNotEmpty(s)) {
                return new Result<>(200, "收藏成功");
            }
            return new Result<>(500, "收藏失败...");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "收藏失败: " + e.getMessage());
        }
    }

    @SofnLog("取消收藏")
    @ApiOperation(value = "取消收藏")
    @PostMapping("/cancelCollectKnowledge")
    public Result<String> cancelCollectKnowledge(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId) {
        try {
            String loginUserId = UserUtil.getLoginUserId();
            String s = knowledgeService.cancelCollectKnowledge(loginUserId, knowledgeId);
            if (StringUtils.isNotEmpty(s)) {
                return new Result<>(200, "取消收藏成功..");
            }
            return new Result<>(500, "取消收藏失败...");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "取消收藏失败: " + e.getMessage());
        }
    }

    @SofnLog("新增知识点")
    @ApiOperation(value = "新增知识点")
    @PostMapping("/insertKnowledge")
    public Result<HashMap<String, Object>> insertKnowledge(@RequestBody KnowledgeVo knowledgeVo) {
        try {
            String loginUserId = UserUtil.getLoginUserId();
            String loginUserName = UserUtil.getLoginUserName();
            // 同名处理
            String knowledgeVoName = knowledgeVo.getKnowledgeName().trim();
            if (knowledgeService.isSameName(knowledgeVoName) != 0) {
                return new Result<>(501, "已有同名知识点存在");
            }
            knowledgeVo.setKnowledgeName(knowledgeVoName);
            knowledgeVo.setCreateTime(new Date(System.currentTimeMillis()));
            knowledgeVo.setCreateUser(loginUserName);
            HashMap<String, Object> map = knowledgeService.insertKnowledge(loginUserId, knowledgeVo);
            return new Result<>(200, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "新增知识点失败: " + e.getMessage());
        }
    }

    @SofnLog("可关联知识点查询")
    @ApiOperation(value = "可关联知识点查询(相邻的知识点查询1-6)")
    @PostMapping("/getAbleKnowledgeList")
    public Result<List<MyKnowledge>> getAbleKnowledgeList(@RequestParam(value = "level", required = true) @ApiParam(name = "level", value = "知识点等级,（必传）", required = true) Integer level,
                                                          @RequestParam(value = "flag", required = false, defaultValue = "S") @ApiParam(name = "flag", value = "查询知识点类型,默认为S，可选S L P", required = false) String flag) {
        return knowledgeService.getAbleKnowledgeList(level, flag);
    }

    @SofnLog("关联知识点")
    @ApiOperation(value = "关联知识点(需要先新增知识点)")
    @PostMapping("/associatedKnowledge")
    public Result<String> associatedKnowledge(@RequestBody RelatedKnowledgeVo relatedKnowledgeVo) {
        try {
            String loginUserId = UserUtil.getLoginUserId();
            String s = knowledgeService.associatedKnowledge(loginUserId, relatedKnowledgeVo);
            if (StringUtils.isNotEmpty(s)) {
                return new Result<>(200, "关联知识点成功");
            }
            return new Result<>(500, "关联知识点失败...");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "关联知识点失败: " + e.getMessage());
        }
    }

    @SofnLog("查看知识网络")
    @ApiOperation(value = "查看知识网络")
    @PostMapping("/getShow")
    public Result<List<RelatedKnowledge>> getShow(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId) {
        return knowledgeService.getShow(knowledgeId);
    }

    @SofnLog("知识点详情")
    @ApiOperation(value = "知识点详情")
    @PostMapping("/getKnowledge")
    public Result<Knowledge> getKnowledge(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId) {
        String loginUserId = UserUtil.getLoginUserId();
        return knowledgeService.getKnowledge(loginUserId, knowledgeId);
    }

//    @SofnLog("关联知识下拉页面")
//    @ApiOperation(value = "关联知识下拉页面(每次各种类型刷新十条)")
//    @PostMapping("/getKnowledgeList")
//    public Result<List<RelatedKnowledge>> getKnowledgeList(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId,
//                                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam(name = "pageNo", value = "页码,（非必传,默认为1）", required = false) int pageNo,
//                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", value = "分页大小,（非必传,默认为10）", required = false) int pageSize) {
//        return knowledgeService.getKnowledgeList(pageNo, pageSize, knowledgeId);
//    }

    @SofnLog("关联知识下拉页面")
    @ApiOperation(value = "关联知识下拉页面(每次各种类型刷新十条)")
    @PostMapping("/getKnowledgeList")
    public Result<HashMap<String, Object>> getKnowledgeList(@RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "知识点id,（必传）", required = true) String knowledgeId,
                                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam(name = "pageNo", value = "页码,（非必传,默认为1）", required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") @ApiParam(name = "pageSize", value = "分页大小,（非必传,默认为10）", required = false) int pageSize) {
        return knowledgeService.getKnowledgeList(pageNo, pageSize, knowledgeId);
    }

    @SofnLog("上传附件")
    @ApiOperation(value = "上传附件(需要先创建知识点)")
    @PostMapping("/uploadAttachments")
    public Result<String> uploadAttachments(@RequestParam MultipartFile file,
                                            @RequestParam(value = "knowledgeId", required = true) @ApiParam(name = "knowledgeId", value = "文件,（必传）", required = true) String knowledgeId) {
        try {
            String loginUserId = UserUtil.getLoginUserId();
            // 将附件上传文件库
            Result<SysFileVo> sysFileVoResult = knowledgeFileService.uploadFile(file);
            // 判断是否成功
            if (sysFileVoResult.getCode() != 200 || sysFileVoResult.getData() == null) {
                throw new RuntimeException("文件上传失败");
            }
            // 获取存储信息，并写入数据库
            knowledgeService.insertAttachments(loginUserId, knowledgeId, sysFileVoResult.getData().getFileName(), sysFileVoResult.getData().getFilePath());
            SysFileManageForm sfmf = new SysFileManageForm();
            sfmf.setIds(sysFileVoResult.getData().getId());
            sfmf.setInterfaceNum("hidden");
            sfmf.setSystemId("pmpps");
            // 激活文件
            knowledgeFileService.activationFile(sfmf);
            return new Result<>(200, "文件上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "文件上传失败: " + e.getMessage());
        }
    }
}
