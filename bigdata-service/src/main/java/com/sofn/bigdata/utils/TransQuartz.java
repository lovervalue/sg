package com.sofn.bigdata.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.mapper.KeTransMonitorMapper;
import com.sofn.bigdata.mapper.KeTransRecordMapper;
import com.sofn.bigdata.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/3 15:39
 * @Description
 */
@DisallowConcurrentExecution
@Service
public class TransQuartz implements InterruptableJob {
    private Trans trans;

    @Autowired
    private KeTransRecordMapper kTransRecordDao;

    @Autowired
    private KeTransMonitorMapper kTransMonitorDao;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap transDataMap = context.getJobDetail().getJobDataMap();
        Object KRepositoryObject = transDataMap.get(Constant.REPOSITORYOBJECT);
        Object DbConnectionObject = transDataMap.get(Constant.DBCONNECTIONOBJECT);
        String transId = String.valueOf(transDataMap.get(Constant.TRANSID));
        String transPath = String.valueOf(transDataMap.get(Constant.TRANSPATH));
        String transName = String.valueOf(transDataMap.get(Constant.TRANSNAME));
        String userId = String.valueOf(1);//transDataMap.get(Constant.USERID));
        String logLevel = String.valueOf(transDataMap.get(Constant.LOGLEVEL));
        String logFilePath = String.valueOf(transDataMap.get(Constant.LOGFILEPATH));
        Date lastExecuteTime = context.getFireTime();
        Date nexExecuteTime = context.getNextFireTime();
        if (null != DbConnectionObject && DbConnectionObject instanceof DBConnectionModel) {// 首先判断数据库连接对象是否正确
            // 判断转换类型
            if (null != KRepositoryObject && KRepositoryObject instanceof KRepository) {// 证明该转换是从资源库中获取到的
                try {
                    System.out.println("======检查===="+KRepositoryObject+ DbConnectionObject+ transId+ transPath+
                            transName+ userId+ logLevel+ logFilePath+ lastExecuteTime+ nexExecuteTime);
                    runRepositorytrans(KRepositoryObject, DbConnectionObject, transId, transPath, transName, userId,
                            logLevel, logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (KettleException e) {
                    System.out.println("======报错====");
                    e.printStackTrace();
                }
            } else {
                try {
                    runFiletrans(DbConnectionObject, transId, transPath, transName, userId, logLevel, logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (KettleXMLException | KettleMissingPluginsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param KRepositoryObject 数据库连接对象
     * @param KRepositoryObject 资源库对象
     * @param transId           转换ID
     * @param transPath         转换在资源库中的路径信息
     * @param transName         转换名称
     * @param userId            转换归属者ID
     * @param logLevel          转换的日志等级
     * @param logFilePath       转换日志保存的根路径
     * @return void
     * @throws KettleException
     * @Title runRepositorytrans
     * @Description 运行资源库中的转换
     */
    private void runRepositorytrans(Object KRepositoryObject, Object DbConnectionObject, String transId,
                                    String transPath, String transName, String userId, String logLevel, String logFilePath, Date executeTime, Date nexExecuteTime)
            throws KettleException {
        KRepository kRepository = (KRepository) KRepositoryObject;
        Integer repositoryId = kRepository.getRepositoryId();
        KettleDatabaseRepository kettleDatabaseRepository = null;
        if (KeRepositoryUtils.KettleDatabaseRepositoryCatch.containsKey(repositoryId)) {
            kettleDatabaseRepository = KeRepositoryUtils.KettleDatabaseRepositoryCatch.get(repositoryId);
        } else {
            kettleDatabaseRepository = KeRepositoryUtils.connectionRepository(kRepository);
        }
        if (null != kettleDatabaseRepository) {
            RepositoryDirectoryInterface directory = kettleDatabaseRepository.loadRepositoryDirectoryTree()
                    .findDirectory(transPath);
            TransMeta transMeta = kettleDatabaseRepository.loadTransformation(transName, directory,
                    new ProgressNullMonitorListener(), true, null);
            trans = new Trans(transMeta);
            trans.setLogLevel(LogLevel.DEBUG);
            if (StringUtils.isNotEmpty(logLevel)) {
                trans.setLogLevel(Constant.logger(logLevel));
            }
            String exception = null;
            Integer recordStatus = 1;
//            Date transStartDate = null;
            Date transStopDate = null;
            String logText = null;
            try {
//                transStartDate = new Date();
                trans.execute(null);
                trans.waitUntilFinished();
                transStopDate = new Date();
            } catch (Exception e) {
                exception = e.getMessage();
                recordStatus = 2;
            } finally {
                if (trans.isFinished()) {
                    if (trans.getErrors() > 0) {
                        recordStatus = 2;
                        if (null == trans.getResult().getLogText() || "".equals(trans.getResult().getLogText())){
                            logText = exception;
                        }
                    }
                    // 写入转换执行结果
                    StringBuilder allLogFilePath = new StringBuilder();
                    allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                            .append(StringUtils.remove(transPath, "/")).append("@").append(transName).append("-log")
                            .append("/").append(new Date().getTime()).append(".").append("txt");
                    String logChannelId = trans.getLogChannelId();
                    LoggingBuffer appender = KettleLogStore.getAppender();
                    logText = appender.getBuffer(logChannelId, true).toString();
                    try {
                        KTransRecord kTransRecord = new KTransRecord();
                        kTransRecord.setRecordTrans(Integer.parseInt(transId));
                        kTransRecord.setLogFilePath(allLogFilePath.toString());
                        kTransRecord.setAddUser(1);//Integer.parseInt(userId));
                        kTransRecord.setRecordStatus(recordStatus);
                        kTransRecord.setStartTime(executeTime);
                        kTransRecord.setStopTime(transStopDate);
                        writeToDBAndFile(DbConnectionObject, kTransRecord, logText, executeTime, nexExecuteTime);
//                        kTransRecordService.insert(kTransRecord);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param DbConnectionObject 数据库连接对象
     * @param transId            装换ID
     * @param transPath          转换文件所在的路径
     * @param transName          转换的名称
     * @param userId             用户ID
     * @param logLevel           装换运行的日志等级
     * @param logFilePath        转换的运行日志保存的位置
     * @return void
     * @throws KettleXMLException
     * @throws KettleMissingPluginsException
     * @Title runFiletrans
     * @Description 运行文件类型的转换
     */
    private void runFiletrans(Object DbConnectionObject, String transId, String transPath, String transName,
                              String userId, String logLevel, String logFilePath, Date lastExecuteTime, Date nexExecuteTime)
            throws KettleXMLException, KettleMissingPluginsException {
        TransMeta transMeta = new TransMeta(transPath);
        trans = new Trans(transMeta);
        trans.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(logLevel)) {
            trans.setLogLevel(Constant.logger(logLevel));
        }
        String exception = null;
        Integer recordStatus = 1;
        Date transStartDate = null;
        Date transStopDate = null;
        String logText = null;
        try {
            transStartDate = new Date();
            trans.execute(null);
            trans.waitUntilFinished();
            transStopDate = new Date();
        } catch (Exception e) {
            exception = e.getMessage();
            recordStatus = 2;
        } finally {
            if (null != trans && trans.isFinished()) {
                if (trans.getErrors() > 0
                        && (null == trans.getResult().getLogText() || "".equals(trans.getResult().getLogText()))) {
                    logText = exception;
                }
                // 写入转换执行结果
                StringBuilder allLogFilePath = new StringBuilder();
                allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                        .append(StringUtils.remove(transPath, "/")).append("@").append(transName).append("-log")
                        .append("/").append(new Date().getTime()).append(".").append("txt");
                String logChannelId = trans.getLogChannelId();
                LoggingBuffer appender = KettleLogStore.getAppender();
                logText = appender.getBuffer(logChannelId, true).toString();
                try {
                    KTransRecord kTransRecord = new KTransRecord();
                    kTransRecord.setRecordTrans(Integer.parseInt(transId));
                    kTransRecord.setAddUser(Integer.parseInt(userId));
                    kTransRecord.setLogFilePath(allLogFilePath.toString());
                    kTransRecord.setRecordStatus(recordStatus);
                    kTransRecord.setStartTime(transStartDate);
                    kTransRecord.setStopTime(transStopDate);
                    writeToDBAndFile(DbConnectionObject, kTransRecord, logText, lastExecuteTime, nexExecuteTime);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param DbConnectionObject 数据库连接对象
     * @param kTransRecord       转换运行记录对象
     * @param logText            日志记录
     * @return void
     * @throws IOException
     * @throws SQLException
     * @Title writeToDBAndFile
     * @Description 保存转换运行日志信息到文件和数据库
     */
    private void writeToDBAndFile(Object DbConnectionObject, KTransRecord kTransRecord, String logText, Date lastExecuteTime, Date nextExecuteTime)
            throws IOException, SQLException {
        // 将日志信息写入文件
        FileUtils.writeStringToFile(new File(kTransRecord.getLogFilePath()), logText, Constant.DEFAULT_ENCODING, false);
        // 写入转换运行记录到数据库
/*        DBConnectionModel DBConnectionModel = (DBConnectionModel) DbConnectionObject;
        ConnectionSource source = ConnectionSourceHelper.getSimple(DBConnectionModel.getConnectionDriveClassName(),
                DBConnectionModel.getConnectionUrl(), DBConnectionModel.getConnectionUser(), DBConnectionModel.getConnectionPassword());
        DBStyle dbStyle = null;
        if("oracle".equalsIgnoreCase(Constant.DATASOURCE_TYPE)){
            dbStyle = new OracleStyle();
        }else if("mysql".equalsIgnoreCase(Constant.DATASOURCE_TYPE)){
            dbStyle = new MySqlStyle();
        }else{
            dbStyle = new OracleStyle();
        }
        SQLLoader loader = new ClasspathLoader("/");
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(dbStyle, loader,
                source, nc, new Interceptor[]{new DebugInterceptor()});
        DSTransactionManager.start();*/
        KTransRecord kTransRecord1=new KTransRecord();
        kTransRecord1.setLogFilePath(kTransRecord.getLogFilePath());
        kTransRecord1.setRecordStatus(kTransRecord.getRecordStatus());
        kTransRecord1.setStartTime(kTransRecord.getStartTime());
        kTransRecord1.setStopTime(kTransRecord.getStopTime());
        kTransRecord1.setRecordTrans(kTransRecord.getRecordTrans());
        kTransRecordDao=(KeTransRecordMapper) SpringContextJobUtil.getBean("keTransRecordMapper");
        kTransMonitorDao=(KeTransMonitorMapper) SpringContextJobUtil.getBean("keTransMonitorMapper");
        kTransRecordDao.insert(kTransRecord1);
        KTransMonitor template = new KTransMonitor();
        template.setMonitorTrans(kTransRecord.getRecordTrans());
        KTransMonitor templateOne = kTransMonitorDao.selectOne(
                new QueryWrapper<KTransMonitor>()
                        .eq("monitor_trans",kTransRecord.getRecordTrans()));
        templateOne.setLastExecuteTime(lastExecuteTime);
        templateOne.setNextExecuteTime(nextExecuteTime);
//        kTransMonitorDao.update();
        if (kTransRecord.getRecordStatus() == 1) {// 证明成功
            //         //成功次数加1
//            templateOne.setMonitorSuccess(templateOne.getMonitorSuccess() + 1);
            UpdateWrapper<KTransMonitor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(
                    "monitor_trans", kTransRecord.getRecordTrans()
            ).set("monitor_success", templateOne.getMonitorSuccess() + 1);
            kTransMonitorDao.update(null,updateWrapper);
        } else if (kTransRecord.getRecordStatus() == 2) {// 证明失败
            //失败次数加1
            //templateOne.setMonitorFail(templateOne.getMonitorFail() + 1);
            UpdateWrapper<KTransMonitor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(
                    "monitor_trans", kTransRecord.getRecordTrans()
            ).set("monitor_fail", templateOne.getMonitorFail() + 1);
            kTransMonitorDao.update(null,updateWrapper);
        }
        //       DSTransactionManager.commit();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        //stop the running transformation
        this.trans.stopAll();
    }
}