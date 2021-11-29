package com.sofn.bigdata.enums;

import lombok.Getter;

/**
 * 地图所需系统AppID及名称
 */

@Getter
public enum MapSysCodeEnum {

    DUCSS("ducss","农作物秸秆资源台账子系统"),
    AEEM("aeem","地膜系统"),
    ASTCAP("astcap","沼气系统"),
    ASTII_REPA("astii_repa","农业体系-资源环境"),
    ASTII_MAIN("astii_main","农业体系-全国可再生能源"),
    ASTII_RREM("astii_rrem","农业体系-可再生能源"),
    ASTII("astii","农业体系-全国资源环境"),
    PMLCSC("pmlcsc","国控点土壤信息采集子系统"),
    AGPJZB("agpjzb","农业野生植物保护名录管理子系统"),
    PCNPL("pcnpl","种植业氮磷监测子系统");

    private String code;
    private String desc;

    MapSysCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
