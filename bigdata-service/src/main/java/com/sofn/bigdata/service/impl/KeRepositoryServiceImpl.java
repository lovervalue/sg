package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sofn.bigdata.mapper.KeRepositoryMapper;
import com.sofn.bigdata.model.KRepository;
import com.sofn.bigdata.model.RepositoryTree;
import com.sofn.bigdata.service.KeRepositoryService;
import com.sofn.bigdata.utils.KeRepositoryUtils;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/2/2 16:27
 * @Description
 */

@Service
public class KeRepositoryServiceImpl implements KeRepositoryService {

    @Autowired
    private KeRepositoryMapper keRepositoryMapper;

    /**
     * @Title ckeck
     * @Description 判断是否可以连接上资源库
     * @param kRepository
     * @return
     * @throws KettleException
     * @return boolean
     */
    @Override
    public boolean check(KRepository kRepository) throws KettleException {
        KettleDatabaseRepository kettleDatabaseRepository = KeRepositoryUtils.connectionRepository(kRepository);
        if (kettleDatabaseRepository != null){
            if (kettleDatabaseRepository.isConnected()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void insert(KRepository kRepository) {
        KRepository kRepositoryId=keRepositoryMapper.selectById(kRepository.getRepositoryId());
        if(null !=kRepositoryId){
            kRepository.setRepositoryId(kRepository.getRepositoryId()+1);
            kRepository.setDelFlag(1);
            kRepository.setAddTime(new Date());
            keRepositoryMapper.insert(kRepository);
        }else {
            kRepository.setRepositoryId(kRepository.getRepositoryId()+1);
            kRepository.setDelFlag(1);
            kRepository.setAddTime(new Date());
            keRepositoryMapper.insert(kRepository);
        }
    }

    @Override
    public void delete(Integer repositoryId) {
        keRepositoryMapper.delete(
                new QueryWrapper<KRepository>().eq("repository_id",repositoryId));
    }

    @Override
    public List<KRepository> getList() {
      return   keRepositoryMapper.selectList(new QueryWrapper<KRepository>()) ;
    }

    @Override
    public List<RepositoryTree> getTreeList(Integer repositoryId) throws KettleException {
        KettleDatabaseRepository kettleDatabaseRepository = null;
        List<RepositoryTree> allRepositoryTreeList = new ArrayList<RepositoryTree>();
        if (KeRepositoryUtils.KettleDatabaseRepositoryCatch.containsKey(repositoryId)){
            kettleDatabaseRepository = KeRepositoryUtils.KettleDatabaseRepositoryCatch.get(repositoryId);
        }else {
            KRepository kRepository = keRepositoryMapper.selectOne(
                    new QueryWrapper<KRepository>().eq("repository_id",repositoryId));
            kettleDatabaseRepository = KeRepositoryUtils.connectionRepository(kRepository);
        }
        if (null != kettleDatabaseRepository){
            List<RepositoryTree> repositoryTreeList = new ArrayList<RepositoryTree>();
            allRepositoryTreeList = KeRepositoryUtils.getAllDirectoryTreeList(kettleDatabaseRepository, "/", repositoryTreeList);
        }
        return allRepositoryTreeList;
    }
}
