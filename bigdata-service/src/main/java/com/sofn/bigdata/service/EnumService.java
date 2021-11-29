package com.sofn.bigdata.service;

import com.sofn.common.utils.PageUtils;

public interface EnumService {
    //审核状态枚举
    PageUtils getRuleEunms();
    String getRuleEnumDescBycode(String code);
}
