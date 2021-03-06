package com.sofn.inceptor;



import com.sofn.core.support.dubbo.spring.annotation.DubboReference;
import com.sofn.core.util.RedisUtil;
import com.sofn.core.util.StringUtils;
import com.sofn.core.util.UUIDUtil;
import com.sofn.model.dgap.*;
import com.sofn.model.generator.*;
import com.sofn.provider.dgap.TDgapResourceProvider;
import com.sofn.provider.dgap.TDgapWsDailyStatProvider;
//import com.sofn.provider.sys.SysUserProvider;
import com.sofn.service.TDgapCheckResourceApplicationService;
import com.sofn.service.TDgapWsLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * author tianjie
 *
 * @date 2016/10/17
 */
@Component
@Aspect
public class LoggerAspect2 {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect2.class.getName());

//    private SysUserProvider sysUserProvider;

    private TDgapWsDailyStatProvider tDgapWsDailyStatProvider;

    @DubboReference
    public void setWsDailyStatProvider(TDgapWsDailyStatProvider tDgapWsDailyStatProvider) {
        this.tDgapWsDailyStatProvider = tDgapWsDailyStatProvider;
    }

//    @DubboReference
//    public void setSysUserProvider(SysUserProvider sysUserProvider) {
//        this.sysUserProvider = sysUserProvider;
//    }

    @Autowired
    private TDgapWsLogService tDgapWsLogService;

    @Autowired
    private TDgapCheckResourceApplicationService tDgapCheckResourceApplicationService;


    @Value("${dgap.dailystat.dbupdate.datagap}")
    int times;


    @Around(value = "execution(public * com.sofn.wsService.impl.DataExportImpl2.*(..)) && args(accessToken,..)")
    public Object aroundDataExport(ProceedingJoinPoint jp, String accessToken) {
        /*
         */
        String methodName = jp.getSignature().getName();
        ErrorSupport ret = null;
        if ("getDataChangeLog".equals(methodName)) {
            ret = new DataChangeLogResult();
        } else if ("getData".equals(methodName)) {
            ret = new DataExportResult();
        }
        //ErrorSupport ret = new DataExportResult();
        return process(jp, accessToken, ret);
    }

    @Around(value = "execution(public * com.sofn.wsService.impl.DataImportImpl2.*(..)) && args(accessToken,..)")
    public Object aroundDataImport(ProceedingJoinPoint jp, String accessToken) {
        ErrorSupport ret = new DataImportResult();
        return process(jp, accessToken, ret);
    }

    public Object process(ProceedingJoinPoint jp, String accessToken, ErrorSupport ret) {
        TDgapWsErrorLog erroLog = null;
        TDgapWsLog wsLog = null;

        Map<String, Object> map;
        String resourceId = null;
        String resourceDirId = null;
        String userId = null;
        String userName = null;
        String methodName = null;
        String errorType = null;
        try {
            methodName = jp.getSignature().getName();
            logger.info("methodName:" + methodName);
            logger.info("accessToken:" + accessToken);

            map = tDgapCheckResourceApplicationService.getTdgapResourceApplication(accessToken);
            if (map != null) {
                //???????????????
                resourceId = (String) map.get("WS_ID");
                resourceDirId = (String) map.get("DIRECTORY_ID");
                userId = (String) map.get("USER_ID");

                userName = null==map.get("USER_NAME")?"defaultUser":(String)map.get("USER_NAME");
//                SysUser sysUser = sysUserProvider.queryById(userId);
//                logger.debug("user:" + sysUser);
//                if (sysUser != null) {
//                    userName = sysUser.getUserName();
//                }

                wsLog = createWsLog(resourceDirId, resourceId, userId, userName, methodName);
                try {
                    ret = (ErrorSupport) jp.proceed();
                } catch (Throwable throwable) {
                    logger.error("", throwable);
                    erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                            Constants.WSErrorType.SYSTEM_ERR.getErrorCode(),
                            Constants.WSErrorType.SYSTEM_ERR.getErrorInnerDesc());

                    errorType = setErrorType(ret, Constants.WSErrorType.SYSTEM_ERR);
                }
            } else {
                //???????????????
                wsLog = createWsLog(resourceDirId, resourceId, userId, userName, methodName);

                erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                        Constants.WSErrorType.AUTH_ERROR.getErrorCode(),
                        Constants.WSErrorType.AUTH_ERROR.getErrorInnerDesc());

                errorType = setErrorType(ret, Constants.WSErrorType.AUTH_ERROR);
            }

            if (ret.isSuccess() == false) {
                if ("100000005".equals(ret.getErrorCode())) {
                    erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                            Constants.WSErrorType.DATA_ERROR.getErrorCode(),
                            Constants.WSErrorType.DATA_ERROR.getErrorInnerDesc());

                    errorType = setErrorType(ret, Constants.WSErrorType.DATA_ERROR);
                }
                if ("100000006".equals(ret.getErrorCode())) {
                    erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                            Constants.WSErrorType.DATALEN_ERROR.getErrorCode(),
                            Constants.WSErrorType.DATALEN_ERROR.getErrorInnerDesc());

                    errorType = setErrorType(ret, Constants.WSErrorType.DATALEN_ERROR);
                }
                if ("100000000".equals(ret.getErrorCode())) {
                    erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                            Constants.WSErrorType.SYSTEM_ERR.getErrorCode(),
                            Constants.WSErrorType.SYSTEM_ERR.getErrorInnerDesc());

                    errorType = setErrorType(ret, Constants.WSErrorType.SYSTEM_ERR);
                }
            }
            return ret;
        } catch (Exception e) {
            logger.error("", e);
            wsLog = createWsLog(resourceDirId, resourceId, userId, userName, methodName);
            erroLog = createTDgapWsErrorLog(resourceDirId, resourceId, userId, userName, methodName,
                    Constants.WSErrorType.SYSTEM_ERR.getErrorCode(),
                    Constants.WSErrorType.SYSTEM_ERR.getErrorInnerDesc());
            setErrorType(ret, Constants.WSErrorType.SYSTEM_ERR);
            return ret;
        } finally {
            try {
                wsLog.setInvokeEndDate(new Date());
                wsLog.setUpdateTime(new Date());
                wsLog.setUpdateBy(wsLog.getCreateBy());
                if (null == erroLog) {
                    wsLog.setErrorType("");
                } else {
                    wsLog.setErrorType(errorType);
                }
                wsLog.setErrorType(errorType);
                //???????????????wslog?????????redis?????????redis????????????
                saveRedis(wsLog);

                tDgapWsLogService.insertWsLog(wsLog);

                if (erroLog != null) {
                    erroLog.setUpdateTime(new Date());
                    erroLog.setUpdateBy(erroLog.getCreateBy());
                    tDgapWsLogService.insertWsErrLog(erroLog);
                }

                //?????????????????????????????????????????????????????????????????????redis
//                tDgapWsDailyStatProvider.stateDailyLog(new Date());
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @DubboReference
    private TDgapResourceProvider tDgapResourceProvider;

    public void saveRedis(TDgapWsLog wsLog) throws Exception {
        //key???????????????	????????????????????????????????????	??????????????????????????????
//        Map<String, Object> keyMap = new HashMap<>();
//        keyMap.put("????????????", wsLog.getWebServiceId());//????????????
//        keyMap.put("??????????????????", wsLog.getMethodName());//??????????????????
//        keyMap.put("???????????????", wsLog.getCallerUser());//???????????????
//        keyMap.put("????????????", wsLog.getResourceDir());//????????????
//        keyMap.put("????????????", wsLog.getCreateTime());//????????????
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//??????????????????
        int sNumber = 0;//????????????
        int eNumber = 0;//????????????

        TDgapResource tDgapResource = tDgapResourceProvider.getDgapResource(wsLog.getWebServiceId());

        if (tDgapResource != null) {
            wsLog.setWebServiceId(tDgapResource.getId());
            wsLog.setResourceDir(tDgapResource.getDirectoryId());
            wsLog.setWebServiceName(tDgapResource.getName());
        }

        String keyMapValue = df.format(wsLog.getCreateTime().getTime())
                + "," + wsLog.getCallerUser()
                + "," + wsLog.getMethodName()
                + "," + wsLog.getWebServiceId()
                + "," + wsLog.getWebServiceName()
                + "," + wsLog.getResourceDir();

        //value 	??????????????????\	??????????????????\	??????????????????(??????)
        if (RedisUtil.get(keyMapValue) == null || "".equals(RedisUtil.get(keyMapValue))) {
            Long consumeTime;//????????????
            consumeTime = wsLog.getInvokeEndDate().getTime() - wsLog.getInvokeStartDate().getTime();

            if (StringUtils.isNotBlank(wsLog.getErrorType())) {
                //??????
                eNumber = 1;
                RedisUtil.setByTime(keyMapValue, "0,1," + String.valueOf(consumeTime), 108000);
            } else {
                //??????
                sNumber = 1;
                RedisUtil.setByTime(keyMapValue, "1,0," + String.valueOf(consumeTime), 108000);
            }
        } else {
            Long consumeTime;//????????????
            String[] value = String.valueOf(RedisUtil.get(keyMapValue)).split(",");
            if (StringUtils.isNotBlank(wsLog.getErrorType())) {
                //??????
                eNumber = Integer.parseInt(value[1]) + 1;
                sNumber = Integer.parseInt(value[0]);
                //???????????????value[2]
                consumeTime = Long.parseLong(value[2]);
                consumeTime = consumeTime + (wsLog.getInvokeEndDate().getTime() - wsLog.getInvokeStartDate().getTime());

            } else {
                //??????
                eNumber = Integer.parseInt(value[1]);
                sNumber = Integer.parseInt(value[0]) + 1;
                //???????????????value[2]
                consumeTime = Long.parseLong(value[2]);
                consumeTime = consumeTime + (wsLog.getInvokeEndDate().getTime() - wsLog.getInvokeStartDate().getTime());

            }

            String valueMapValue = String.valueOf(sNumber)
                    + "," + String.valueOf(eNumber)
                    + "," + String.valueOf(consumeTime);

            RedisUtil.setByTime(keyMapValue, valueMapValue, 108000);

        }

        //???????????????????????????
        if ((sNumber + eNumber) % times == 0) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 0);

            String nowTime = df.format(cal.getTime());

            Set<Serializable> keysSet = RedisUtil.getKeysSet(nowTime);

            for (Serializable key : keysSet) {
                String[] value = RedisUtil.get((String) key).toString().split(",");

                String[] keys = ((String) key).split(",");

                TDgapWsDailyStat tDgapWsDailyStat = new TDgapWsDailyStat();
                tDgapWsDailyStat.setWebServiceId(keys[3]);

                tDgapWsDailyStat.setStatDate(df.parse(df.format(new Date())));

                tDgapWsDailyStat.setCallerUser(keys[1]);
                tDgapWsDailyStat.setBussnessName(keys[2]);
                tDgapWsDailyStat.setWebServiceName(keys[4]);
                tDgapWsDailyStat.setResourceDir(keys[5]);

                if (tDgapWsDailyStat.getWebServiceName().equals("null") || tDgapWsDailyStat.getResourceDir().equals("null")) {
                    continue;
                }

                int avgTimecost = Integer.parseInt(value[2]) / (Integer.parseInt(value[0]) + Integer.parseInt(value[1]));
                tDgapWsDailyStat.setSuccessTimes(new BigDecimal(value[0]));
                tDgapWsDailyStat.setFailTimes(new BigDecimal(value[1]));
                tDgapWsDailyStat.setAvgTimecost(new BigDecimal(avgTimecost));
                String uid = UUIDUtil.getUUID();
                tDgapWsDailyStat.setId(uid == null ? "" : uid);

                tDgapWsDailyStat.setUpdateBy("0");
                tDgapWsDailyStat.setRemark("0");
                tDgapWsDailyStat.setUpdateTime(df.parse(df.format(new Date())));
                tDgapWsDailyStat.setCreateTime(df.parse(df.format(new Date())));

                tDgapWsDailyStat.setCreateBy("0");
                tDgapWsDailyStat.setDelFlag("N");
                tDgapWsDailyStatProvider.setWebServiceLog(tDgapWsDailyStat);

            }
        }


    }

    public String setErrorType(ErrorSupport ret, Constants.WSErrorType errorType) {
        ret.setSuccess(false);
        ret.setErrorCode(errorType.getErrorCode());
        ret.setErrorDesc(errorType.getErrorInnerDesc());
        return ret.getErrorDesc();
    }

    private TDgapWsLog createWsLog(String resourceDirId, String resourceId, String userId, String userName, String methodName) {
        TDgapWsLog tDgapWsLog = new TDgapWsLog();
        tDgapWsLog.setResourceDir(resourceDirId);
        tDgapWsLog.setWebServiceId(resourceId);
        tDgapWsLog.setCallerUser(userName);
        tDgapWsLog.setMethodName(methodName);

        tDgapWsLog.setInvokeStartDate(new Date());
        tDgapWsLog.setCreateTime(new Date());
        tDgapWsLog.setCreateBy(userId);
        return tDgapWsLog;
    }


    private TDgapWsErrorLog createTDgapWsErrorLog(String resourceDirId, String resourceId, String userId, String userName, String methodName, String errorCode, String errorDescription) {

        TDgapWsErrorLog tDgapWsErrorLog = new TDgapWsErrorLog();
        tDgapWsErrorLog.setResourceDirectoryId(resourceDirId);
        tDgapWsErrorLog.setWebServiceId(resourceId);
        tDgapWsErrorLog.setMethodName(methodName);
        tDgapWsErrorLog.setCallerUser(userName);

        tDgapWsErrorLog.setErrorDate(new Date());
        tDgapWsErrorLog.setCreateTime(new Date());
        tDgapWsErrorLog.setErrorType(errorCode);
        tDgapWsErrorLog.setErrorDesc(errorDescription);

        tDgapWsErrorLog.setCreateTime(new Date());
        tDgapWsErrorLog.setCreateBy(userId);
        return tDgapWsErrorLog;
    }

}