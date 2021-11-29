package com.sofn.bigdata.service;

import com.sofn.bigdata.model.UnstructuredAnalysis;

import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/3/2 18:28
 * @Description
 */
public interface UnstructuredAnalysisService {

    void insert (UnstructuredAnalysis unstructuredAnalysis);

    List<UnstructuredAnalysis> getAllList(String category,String modelName);

    UnstructuredAnalysis getOne(String modelName);

    void updatemodel( String modelName,Integer status,String keyword,String subordinate,String category,String knowledgeLevel,String fileSource);

    List getfileList(String fileName, String fileSource,String startDate,String endDate,String category);

    void deleteinformation(String modelName,Integer flag);

    void writeevaluation(String modelName,String evaluation);

}
