package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.TemplateIspro;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TemplateIsproMapper extends BaseMapper<TemplateIspro> {

    void delTemplateIspro(String id);

    void updateTemplateIspro(TemplateIspro ispro);

    List<TemplateIspro> getTemplateIsproList(@Param("fieldname") String fieldname,@Param("fieldtype") String fieldtype,@Param("ispro") String ispro);

    void startTemplateIspro(String id);

    void stopTemplateIspro(String id);
}
