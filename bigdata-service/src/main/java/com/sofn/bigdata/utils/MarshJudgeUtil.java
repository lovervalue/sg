package com.sofn.bigdata.utils;

import com.sofn.bigdata.entities.MarshInfo;
import com.sofn.bigdata.vo.MarshInfoForm;
import com.sofn.bigdata.vo.MarshInfoVo;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarshJudgeUtil {
    //上次加载配置文件的时间标识
    private static Long lastTime = -1L;
    private static final FastDateFormat standardTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static Map<String, List<String>> workStage2targetParams;

    //key:工段名， value:工段id
    private static Map<String, String> workStage2workStageId;
    //key:工段名_参数名， value:参数id
    private static Map<String, String> workStage_targetParam2targetParamId;
    private static final String reg = "[1-9]\\d*.\\d*|0.\\d*|0";


    static {
        loadConfig();
    }

    public static void reLoadConfig() {
        long intervalTime = (System.currentTimeMillis()-lastTime)/1000;
        if(intervalTime>5*60){ //每5分钟刷新一次数据库配置
            loadConfig();
        }
    }
    private static synchronized void loadConfig() {
        lastTime = System.currentTimeMillis();
        Map<String, List<String>> workStage2targetParams1 = new HashMap<>();
        Map<String, String> workStage2workStageId1 = new HashMap<>();
        Map<String, String> workStage_targetParam2targetParamId1 = new HashMap<>();
        try {
            //1.通过配置文件匹配
            //Properties properties = PropertiesUtil.getProperties(MarshJudgeUtil.class.getClassLoader(),"marsh_config.properties");
            //Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
            //while (it.hasNext()) {
            //    Map.Entry<Object, Object> entry = it.next();
            //    String key = entry.getKey().toString();
            //    List<String> value = Arrays.asList(entry.getValue().toString().split(","));
            //    workStage2targetParams.put(key,value);
            //}

            //2.查询数据库
            String sql = "select a.id workStageId,a.gdname workStage,c.id targetParamId,c.configname targetParam " +
                    "from astcap_worksection a join astcap_gdyjc_config b on a.id=b.gdid join astcap_monitorconfig c on b.configid=c.id";
            Connection connection = DbMarshConfigUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String workStage = rs.getString("workStage");
                String targetParam = rs.getString("targetParam");
                String workStageId = rs.getString("workStageId");
                String targetParamId = rs.getString("targetParamId");
                workStage2workStageId1.put(workStage,workStageId);
                workStage_targetParam2targetParamId1.put(workStage+"_"+targetParam,targetParamId);

                List<String> list = workStage2targetParams1.get(workStage);
                if (list == null) {
                    list = new ArrayList<>();
                    workStage2targetParams1.put(workStage, list);
                }
                list.add(targetParam);
            }
            DbMarshConfigUtils.close(connection,rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(workStage2targetParams1.size()>0&&workStage2workStageId1.size()>0&&workStage_targetParam2targetParamId1.size()>0){
            workStage2targetParams = workStage2targetParams1;
            workStage2workStageId = workStage2workStageId1;
            workStage_targetParam2targetParamId = workStage_targetParam2targetParamId1;
        }
    }

    /**
     *  校验marshInfoList的所有元素， 如果都有效，返回validIndex的值为marshInfoList的size
     *  对校验成功的元素，将工段内容，参数内容转为对应的id，并完善采集频率字段，站点ID字段等
     * @param marshInfoForm
     * @param marshInfos
     * @return
     */
    public static int checkAndFormatMarshInfos(MarshInfoForm marshInfoForm, List<String> marshInfos) {
        reLoadConfig();
        int invalidIndex = 0;
        for (MarshInfoVo vo : marshInfoForm.getMarshInfoList()) {
            MarshInfo marshInfo = new MarshInfo();
            BeanUtils.copyProperties(vo,marshInfo);
            if (MarshJudgeUtil.isValid(marshInfo)) {
                //如果当前报文有效，补充采集频率字段、站点编码字段、按天分区字段，录入到hive仓库
                marshInfo.setCollectFrequency(marshInfoForm.getCollectFrequency());
                marshInfo.setSiteId(marshInfoForm.getSiteId());
                marshInfo.setDay(marshInfo.getMonitorTime().substring(0, 10).replaceAll("-", ""));
                marshInfos.add(marshInfo.toString());
                invalidIndex++;
            } else {
                break;
            }
        }
        return invalidIndex;
    }

    public static boolean isValid(MarshInfo marshInfo) {
        if (marshInfo.getWorkStage() == null
                || marshInfo.getCurrentValue() == null
                || marshInfo.getTargetParam() == null
                || marshInfo.getMonitorTime() == null
                || marshInfo.getParamUnit() == null) {
            return false;
        }

        //判断当前值是不是数值类型
        if (!marshInfo.getCurrentValue().matches(reg)) {
            return false;
        }

        //判断监测时间是不是标准的日期格式
        try {
            standardTimeFormat.parse(marshInfo.getMonitorTime());
        } catch (Exception e) {
            return false;
        }

        //校验工段和参数
        String workStage = marshInfo.getWorkStage();
        String targetParam = marshInfo.getTargetParam();
        if (!workStage2targetParams.containsKey(workStage)) {
            return false;
        } else {
            List<String> targetParams = workStage2targetParams.get(workStage);
            if (!targetParams.contains(targetParam)) {
                return false;
            }
        }
        marshInfo.setWorkStage(workStage2workStageId.get(workStage));
        marshInfo.setTargetParam(workStage_targetParam2targetParamId.get(workStage+"_"+targetParam));
        return true;
    }
}
