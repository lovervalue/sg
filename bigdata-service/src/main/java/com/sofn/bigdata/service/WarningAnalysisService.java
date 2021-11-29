package com.sofn.bigdata.service;

import com.sofn.bigdata.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/7 11:16
 * @Description
 */
public interface WarningAnalysisService {

    void insert(WarningAnalysis warningAnalysis);

    WarningAnalysis getOne(String dataName, Integer warningStatus);

    List<WarningAnalysis> getList();

    void insertOthername(String databaseName,String dataName,String businessOthername);

    WarningAnalysis getOthername(String databaseName);

    void insertThresholdmodel(Thresholdmodel thresholdmodel);

    void deleteThresholdmodel(Integer thresholdvalue);

    List<WarningResult> getWarningResultList(String dataName, Integer status, String startdate,String stopdate);

    List<WarningDetails> getWarningDetailsList(String dataName);

    WarningDisposal getWarningDisposalOne(String dataName);

    List<WarningResult> getWarningResultoperating (String dataName);

    List getWarningAnalysisDisplay(String dataName, String startdate, String stopdate);

}

