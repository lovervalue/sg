package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import org.stringtemplate.v4.ST;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/3/2 17:53
 * @Description
 */
public class UnstructuredAnalysis {

    @TableId(value = "model_id",type= IdType.UUID)
    private String modelId;

    @ApiModelProperty("模型名称")
    private String modelName;

    @ApiModelProperty("创建人")
    private String createName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("状态，1是启用，2是停用")
    private Integer status;

    @ApiModelProperty("类别 畜牧业 渔业 环境")
    private String category;

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("精确1或者模糊2")
    private Integer preciseOrFuzzy;

    @ApiModelProperty("下级关系'并且' '或者' '不含'没有则是空")
    private String subordinate;

    @ApiModelProperty("关键字2")
    private String keywordTwo;

    @ApiModelProperty("精确1或者模糊2")
    private Integer preciseOrFuzzyTwo;

    @ApiModelProperty("知识级别 一级 二级 其他")
    private String knowledgeLevel;

    @ApiModelProperty("文件级别 网络爬取 数据共享 业务系统 全部")
    private String fileSource;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentAddress;

    @ApiModelProperty("是否删除1是存在，2是删除")
    private Integer flag;

    @ApiModelProperty("评估")
    private String evaluation;

    @ApiModelProperty("是否查看，1是查看，2是未查看")
    private String seeStatus;
    @Override
    public String toString() {
        return "UnstructuredAnalysis{" +
                "modelId='" + modelId + '\'' +
                ", modelName='" + modelName + '\'' +
                ", createName='" + createName + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", category='" + category + '\'' +
                ", keyword='" + keyword + '\'' +
                ", preciseOrFuzzy='" + preciseOrFuzzy + '\'' +
                ", subordinate='" + subordinate + '\'' +
                ", keywordTwo='" + keywordTwo + '\'' +
                ", preciseOrFuzzyTwo='" + preciseOrFuzzyTwo + '\'' +
                ", knowledgeLevel='" + knowledgeLevel + '\'' +
                ", fileSource='" + fileSource + '\'' +
                '}';
    }

    public String getSeeStatus() {
        return seeStatus;
    }

    public void setSeeStatus(String seeStatus) {
        this.seeStatus = seeStatus;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentAddress() {
        return attachmentAddress;
    }

    public void setAttachmentAddress(String attachmentAddress) {
        this.attachmentAddress = attachmentAddress;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPreciseOrFuzzy() {
        return preciseOrFuzzy;
    }

    public void setPreciseOrFuzzy(Integer preciseOrFuzzy) {
        this.preciseOrFuzzy = preciseOrFuzzy;
    }

    public String getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(String subordinate) {
        this.subordinate = subordinate;
    }

    public String getKeywordTwo() {
        return keywordTwo;
    }

    public void setKeywordTwo(String keywordTwo) {
        this.keywordTwo = keywordTwo;
    }

    public Integer getPreciseOrFuzzyTwo() {
        return preciseOrFuzzyTwo;
    }

    public void setPreciseOrFuzzyTwo(Integer preciseOrFuzzyTwo) {
        this.preciseOrFuzzyTwo = preciseOrFuzzyTwo;
    }

    public String getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(String knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getFileSource() {
        return fileSource;
    }

    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }
}









