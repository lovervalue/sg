package com.sofn.bigdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.enums.RuleEnum;
import com.sofn.bigdata.service.EnumService;
import com.sofn.bigdata.vo.EnumVo;
import com.sofn.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumServiceImpl implements EnumService {

    @Override
    public PageUtils getRuleEunms() {
        List<EnumVo> list = RuleEnum.getEnumInfo();
        PageInfo<EnumVo> pageInfo = new PageInfo<>(list);
        return PageUtils.getPageUtils(pageInfo);
    }

    @Override
    public String getRuleEnumDescBycode(String code) {
        return RuleEnum.getDescByCode(code);
    }


}
