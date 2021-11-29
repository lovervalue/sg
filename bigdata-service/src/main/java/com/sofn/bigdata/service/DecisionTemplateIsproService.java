package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.TemplateIspro;
import com.sofn.bigdata.vo.TemplateIsproVo;
import com.sofn.common.utils.PageUtils;

import java.util.List;

public interface DecisionTemplateIsproService extends IService<TemplateIspro> {

    void saveTemplateIspro(TemplateIsproVo templateIsproVo);

    void delTemplateIspro(String id);

    void updateTemplateIspro(TemplateIsproVo templateIsproVo);

    PageUtils<TemplateIsproVo> getTemplateIsproList(String fieldname, String fieldtype, String ispro, Integer pageNo, Integer pageSize);

    List<TemplateIsproVo> getAllTemplateIsproList(String ispro);

    void startTemplateIspro(String id);

    void stopTemplateIspro(String id);
}
