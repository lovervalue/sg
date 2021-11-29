package com.sofn.bigdata.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.mapper.KQuartzMapper;
import com.sofn.bigdata.mapper.KeRepositoryMapper;
import com.sofn.bigdata.model.DBConnectionModel;
import com.sofn.bigdata.model.KQuartz;
import com.sofn.bigdata.model.KRepository;
import com.sofn.bigdata.utils.QuartzManager;
import com.sofn.bigdata.utils.TransQuartz;
import org.apache.commons.lang.StringUtils;
import com.sofn.bigdata.mapper.KeTransMapper;
import com.sofn.bigdata.model.KTrans;
import com.sofn.bigdata.service.KeTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/3 10:21
 * @Description kettle转换核心实现类
 */
@Service
public class KeTransServiceImpl implements KeTransService {

    @Autowired
    private KeRepositoryMapper kRepositoryDao;

    @Autowired
    private KeTransMapper kTransDao;

    @Autowired
    private KQuartzMapper kQuartzDao;

    @Value("/kelogs")
    private String kettleLogFilePath;

    @Value("/test")
    private String kettleFileRepository;

    @Value("com.uxsino.uxdb.Driver")
    private String jdbcDriver;

    @Value("jdbc:uxdb://192.168.21.62:5432/astcap_dev")
    private String jdbcUrl;

    @Value("uxdb")
    private String jdbcUsername;

    @Value("sofn@123")
    private String jdbcPassword;

    @Override
    public boolean check(Integer repositoryId, String kTransPath) {
        List<KTrans> kTransList = (List<KTrans>) kTransDao.selectOne(new QueryWrapper<KTrans>()
                .eq("trans_path", kTransPath)
                .eq("trans_repository_id", repositoryId));
        System.out.println(kTransList);
        if (null != kTransList) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void inset(KTrans kTrans, String customerQuarz) throws SQLException {
        kTrans.setAddTime(new Date());
        kTrans.setDelFlag(1);
        kTrans.setTransStatus(2);
        if (StringUtils.isNotBlank(customerQuarz)) {
            KQuartz kQuartz = new KQuartz();
            kQuartz.setAddTime(new Date());
            kQuartz.setDelFlag(1);
            kQuartz.setQuartzCron(customerQuarz);
            kQuartz.setQuartzDescription(kTrans.getTransName() + "的定时策略");
            kQuartzDao.insert(kQuartz);
        } else if (StringUtils.isBlank(customerQuarz) && new Integer(0).equals(kTrans.getTransQuartz())) {
            kTrans.setTransQuartz(1);
        } else if (StringUtils.isBlank(customerQuarz) && kTrans.getTransQuartz() == null) {
            kTrans.setTransQuartz(1);
        }
        kTransDao.insert(kTrans);
    }

    @Override
    public void start(Integer transId) {
        // 获取到转换对象
        KTrans kTrans = kTransDao.selectOne(
                new QueryWrapper<KTrans>().eq("trans_id",transId));
        // 获取到定时策略对象
        KQuartz kQuartz = kQuartzDao.selectOne(
                new QueryWrapper<KQuartz>().eq("quartz_id",kTrans.getTransQuartz()));
        //定时策略
        String quartzCron = kQuartz.getQuartzCron();
        // 获取Quartz执行的基础信息
        Map<String, String> quartzBasic = getQuartzBasic(kTrans);
        // 获取Quartz的参数
        Map<String, Object> quartzParameter = getQuartzParameter(kTrans);
        Date nextExecuteTime = null;
        // 添加任务
        // 判断转换执行类型
        try {
            if (new Integer(1).equals(kTrans.getTransQuartz())) {//如果是只执行一次
                nextExecuteTime = QuartzManager.addOnceJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                        quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"),
                        TransQuartz.class, quartzParameter);
            } else {// 如果是按照策略执行
                //添加任务
                nextExecuteTime = QuartzManager.addJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                        quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"),
                        TransQuartz.class, quartzCron, quartzParameter);
            }
        } catch (Exception e) {
            kTrans.setTransStatus(2);
            //       kTransDao.update(kTrans);
            kTransDao.updatestatus(transId);
            return;
        }
        kTrans.setTransStatus(1);
        kTransDao.updatestatustrue(transId);
    }

    @Override
    public void stop(Integer transId) {
        // 获取到作业对象
        KTrans kTrans = kTransDao.selectOne(
                new QueryWrapper<KTrans>().eq("trans_id",transId));
        // 获取Quartz执行的基础信息
        Map<String, String> quartzBasic = getQuartzBasic(kTrans);
        // 移除任务
        if (new Integer(1).equals(kTrans.getTransQuartz())) {//如果是只执行一次
            // 一次性执行任务，不允许手动停止
            QuartzManager.removeJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                    quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"));
        } else {// 如果是按照策略执行
            QuartzManager.removeJob(quartzBasic.get("jobName"), quartzBasic.get("jobGroupName"),
                    quartzBasic.get("triggerName"), quartzBasic.get("triggerGroupName"));
        }
        // 移除监控
        //removeMonitor(userId, transId);
        // 更新任务状态
        kTrans.setTransStatus(2);
        kTransDao.updatestatus(transId);
    }

    @Override
    public void delete(Integer transId) {

        kTransDao.deleteById(transId);
    }

    @Override
    public List getquartzlist() {
        return kQuartzDao.selectList(new QueryWrapper<KQuartz>());
    }

    @Override
    public List getList() {
        return kTransDao.selectList(new QueryWrapper<>());
    }

    @Override
    public KTrans getTrans(Integer transId) {
        return kTransDao.selectOne(
                new QueryWrapper<KTrans>().eq("trans_id",transId)
        );
    }

    /**
     * @param kTrans 转换对象
     * @return Map<String               ,            String> 任务调度的基础信息
     * @Title getQuartzBasic
     * @Description 获取任务调度的基础信息
     */
    private Map<String, String> getQuartzBasic(KTrans kTrans) {
        Integer transRepositoryId = kTrans.getTransRepositoryId();
        String transPath = kTrans.getTransPath();
        Map<String, String> quartzBasic = new HashMap<String, String>();
        // 拼接Quartz的任务名称
        StringBuilder jobName = new StringBuilder();
        jobName.append(Constant.JOB_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(transPath);
        // 拼接Quartz的任务组名称
        StringBuilder jobGroupName = new StringBuilder();
        jobGroupName.append(Constant.JOB_GROUP_PREFIX).append(Constant.QUARTZ_SEPARATE)
                .append(Constant.QUARTZ_SEPARATE)
                .append(transRepositoryId).append(Constant.QUARTZ_SEPARATE)
                .append(transPath);
        // 拼接Quartz的触发器名称
        String triggerName = StringUtils.replace(jobName.toString(), Constant.JOB_PREFIX, Constant.TRIGGER_PREFIX);
        // 拼接Quartz的触发器组名称
        String triggerGroupName = StringUtils.replace(jobGroupName.toString(), Constant.JOB_GROUP_PREFIX, Constant.TRIGGER_GROUP_PREFIX);
        quartzBasic.put("jobName", jobName.toString());
        quartzBasic.put("jobGroupName", jobGroupName.toString());
        quartzBasic.put("triggerName", triggerName);
        quartzBasic.put("triggerGroupName", triggerGroupName);
        return quartzBasic;
    }

    /**
     * @param kTrans 转换对象
     * @return Map<String               ,                               Object>
     * @Title getQuartzParameter
     * @Description 获取任务调度的参数
     */
    private Map<String, Object> getQuartzParameter(KTrans kTrans) {
        // Quartz执行时的参数
        Map<String, Object> parameter = new HashMap<String, Object>();
        // 资源库对象
        Integer transRepositoryId = kTrans.getTransRepositoryId();
        KRepository kRepository = null;
        if (transRepositoryId != null) {// 这里是判断是否为资源库中的转换还是文件类型的转换的关键点
            kRepository = kRepositoryDao.selectOne(
                    new QueryWrapper<KRepository>().eq("repository_id",transRepositoryId));
        }
        // 资源库对象
        parameter.put(Constant.REPOSITORYOBJECT, kRepository);
        // 数据库连接对象
        parameter.put(Constant.DBCONNECTIONOBJECT, new DBConnectionModel(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword));
        // 转换ID
        parameter.put(Constant.TRANSID, kTrans.getTransId());
        parameter.put(Constant.JOBTYPE, 2);
        String transPath = kTrans.getTransPath();
        if (transPath.contains("/")) {
            int lastIndexOf = StringUtils.lastIndexOf(transPath, "/");
            String path = transPath.substring(0, lastIndexOf);
            // 转换在资源库中的路径
            parameter.put(Constant.TRANSPATH, StringUtils.isEmpty(path) ? "/" : path);
            // 转换名称
            parameter.put(Constant.TRANSNAME, transPath.substring(lastIndexOf + 1, transPath.length()));
        }
        // 用户ID
       // parameter.put(Constant.USERID, kTrans.getAddUser());
        // 转换日志等级
        parameter.put(Constant.LOGLEVEL, kTrans.getTransLogLevel());
        // 转换日志的保存位置
        parameter.put(Constant.LOGFILEPATH, kettleLogFilePath);
        return parameter;
    }

}