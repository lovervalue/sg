package com.sofn.bigdata.service;

import com.sofn.bigdata.model.KTransMonitor;
import com.sofn.bigdata.model.KTransRecord;

import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/19 11:17
 * @Description
 */
public interface KeTransMonitorService {

    Integer alltranscount();

    Integer alltranssuccesscount();

    Integer alltransfailcount();

    List<KTransMonitor> getTransList();

    List<KTransRecord> seetrans(Integer recordTrans);

    Integer seetransconut(Integer recordTrans);
}
