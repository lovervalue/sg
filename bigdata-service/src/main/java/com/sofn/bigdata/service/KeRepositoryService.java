package com.sofn.bigdata.service;

import com.sofn.bigdata.model.KRepository;
import com.sofn.bigdata.model.RepositoryTree;
import org.pentaho.di.core.exception.KettleException;

import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/2 16:27
 * @Description
 */
public interface KeRepositoryService {
    boolean check(KRepository kRepository) throws KettleException;

    void insert(KRepository kRepository);

    void delete(Integer repositoryId);

    List<KRepository> getList();

    List<RepositoryTree> getTreeList(Integer repositoryId) throws KettleException;
}
