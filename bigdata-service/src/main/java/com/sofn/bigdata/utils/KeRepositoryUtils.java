package com.sofn.bigdata.utils;

import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.model.RepositoryTree;
import com.sofn.bigdata.model.KRepository;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectory;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/2 16:45
 * @Description
 */
public class KeRepositoryUtils {
    public static Map<Integer, KettleDatabaseRepository> KettleDatabaseRepositoryCatch
            = new HashMap<Integer, KettleDatabaseRepository>();

    public static String[] getDataBaseAccess(){
        String[] dataBaseAccess = DatabaseMeta.dbAccessTypeCode;
        return dataBaseAccess;
    }

    public static KettleDatabaseRepository connectionRepository(KRepository kRepository) throws KettleException {
        //初始化
        KettleEnvironment.init();
        if (null != kRepository) {
            //创建连接
            DatabaseMeta databaseMetaData = new DatabaseMeta(
                    null, kRepository.getRepositoryType(), kRepository.getDatabaseAccess(),
                    kRepository.getDatabaseHost(), kRepository.getDatabaseName(),
                    kRepository.getDatabasePort(), kRepository.getDatabaseUsername(),
                    kRepository.getDatabasePassword());
            //资源库元对象
            KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
            repositoryInfo.setConnection(databaseMetaData);
            //资源库初始化
            KettleDatabaseRepository repository = new KettleDatabaseRepository();
            repository.init(repositoryInfo);
            repository.connect(kRepository.getRepositoryUsername(), kRepository.getRepositoryPassword());
            //添加缓存
            if (null != kRepository.getRepositoryId()) {
//                int repositoryintId = Integer.valueOf(kRepository.getRepositoryId()).intValue();
                KettleDatabaseRepositoryCatch.put(kRepository.getRepositoryId(), repository);
            }
            return repository;
        }
        return null;
    }

    /**
     * @Title getAllDirectoryTreeList
     * @Description 递归调用获取全部的树形菜单
     * @param kettleDatabaseRepository 资源库
     * @param path 当前路径
     * @param allRepositoryTreeList 所有的树形菜单
     * @return
     * @throws KettleException
     * @return List<RepositoryTree> 所有的树形菜单
     */
    public static List<RepositoryTree> getAllDirectoryTreeList(KettleDatabaseRepository kettleDatabaseRepository,
                                                               String path, List<RepositoryTree> allRepositoryTreeList)throws KettleException{
        List<RepositoryTree> repositoryTreeList=getJobAndTrans(kettleDatabaseRepository,path);
        if(repositoryTreeList.size()!=0){
            for (RepositoryTree repositoryTree : repositoryTreeList) {
                if(!repositoryTree.isLasted()){
                    getAllDirectoryTreeList(kettleDatabaseRepository,repositoryTree.getPath(),allRepositoryTreeList);
                    allRepositoryTreeList.add(repositoryTree);
                }else {
                    allRepositoryTreeList.add(repositoryTree);
                }
            }
        }
        return allRepositoryTreeList;
    }



    /**
     * @Title getJobAndTrans
     * @Description 获取Job和Transformation和Directory的信息
     * @param repository
     * @param path
     * @return RepositoryTree的集合
     * @throws KettleException
     * @return List<RepositoryTree>
     */
    public static List<RepositoryTree> getJobAndTrans(KettleDatabaseRepository repository,
                                                      String path) throws KettleException {
        //获取当前的路径信息
        RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory(path);
        //获取Directory信息
        List<RepositoryTree> repositoryTreeList = getDirectory(repository, rDirectory);
        //获取Job和Transformation的信息
        List<RepositoryElementMetaInterface> li = repository.getJobAndTransformationObjects(rDirectory.getObjectId(), false);
        if (null != li) {
            for (RepositoryElementMetaInterface repel : li) {
                if (Constant.TYPE_JOB.equals(repel.getObjectType().toString())) {
                    RepositoryTree repositoryTree = new RepositoryTree();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Constant.TYPE_JOB).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    repositoryTree.setType(Constant.TYPE_JOB);
                    repositoryTree.setLasted(true);
                    repositoryTreeList.add(repositoryTree);
                }else if (Constant.TYPE_TRANS.equals(repel.getObjectType().toString())){
                    RepositoryTree repositoryTree = new RepositoryTree();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Constant.TYPE_TRANS).append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    repositoryTree.setType(Constant.TYPE_TRANS);
                    repositoryTree.setLasted(true);
                    repositoryTreeList.add(repositoryTree);
                }
            }
        }
        return repositoryTreeList;
    }

    /**
     * @Title getDirectory
     * @Description 获取Directory信息
     * @param repository
     * @param rDirectory
     * @return
     * @throws KettleException
     * @return List<RepositoryTree>
     */
    private static List<RepositoryTree> getDirectory(KettleDatabaseRepository repository,
                                                     RepositoryDirectoryInterface rDirectory) throws KettleException {
        List<RepositoryTree> repositoryTreeList = new ArrayList<RepositoryTree>();
        if (null != repository && null != rDirectory){
            RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree().findDirectory(rDirectory.getObjectId());
            if (rDirectory.getNrSubdirectories() > 0){
                for (int i = 0; i < rDirectory.getNrSubdirectories(); i++) {
                    RepositoryDirectory subTree = tree.getSubdirectory(i);
                    RepositoryTree repositoryTree = new RepositoryTree();
                    repositoryTree.setId(subTree.getObjectId().toString());
                    repositoryTree.setParent(rDirectory.getObjectId().toString());
                    repositoryTree.setText(subTree.getName());
                    repositoryTree.setPath(subTree.getPath());
                    //判断是否还有子Directory或者Job和Transformation
                    List<RepositoryElementMetaInterface> RepositoryElementMetaInterfaceList =
                            repository.getJobAndTransformationObjects(subTree.getObjectId(), false);
                    if (subTree.getNrSubdirectories() > 0 || RepositoryElementMetaInterfaceList.size() > 0){
                        repositoryTree.setLasted(false);
                    }else{
                        repositoryTree.setLasted(true);
                    }
                    repositoryTreeList.add(repositoryTree);
                }
            }
        }
        return repositoryTreeList;
    }


}
