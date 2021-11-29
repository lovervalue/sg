package com.sofn.bigdata.enums;

import lombok.Getter;

/**
 * 决策模块所需状态
 */

@Getter
public enum DecisionStatusEnum {

    ASKING("1","提问中"),
    Decision_PROPOSAL_ING("2","提案进行中"),
    Decision_PROPOSAL_ED("3","完成"),
    ASTCAP_ID("bigdata","系统ID"),
    BE_NOT_SELECTED("1","未入选"),
    PROPOSAL_PROPOSAL_ING("2","提案进行中"),
    PROPOSAL_PROPOSAL_ED("3","完成");

    private String code;
    private String desc;

    DecisionStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
