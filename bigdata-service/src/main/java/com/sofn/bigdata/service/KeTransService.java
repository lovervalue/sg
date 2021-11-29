package com.sofn.bigdata.service;

import com.sofn.bigdata.model.KTrans;

import java.sql.SQLException;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/3 10:20
 * @Description
 */
public interface KeTransService {

    boolean check(Integer repositoryId, String kTransPath);

    void inset(KTrans kTrans, String customerQuarz) throws SQLException;

    void start(Integer transId);

    void stop(Integer transId);

    void delete(Integer transId);

    List getquartzlist();

    List getList();

    KTrans getTrans(Integer transId);
}
