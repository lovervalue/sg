package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.constant.StatusCode;
import com.sofn.bigdata.mapper.DataItemMapper;
import com.sofn.bigdata.mapper.TemplateMapper;
import com.sofn.bigdata.model.DataItem;
import com.sofn.bigdata.model.PageResult;
import com.sofn.bigdata.model.Template;
import com.sofn.bigdata.service.TemplateService;
import com.sofn.bigdata.utils.MailMessageUtils;
import com.sofn.bigdata.utils.SnowflakeIdWorker;
import com.sofn.bigdata.utils.UserUtils;
import com.sofn.common.exception.SofnException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @author:袁彬锋
 * @date:2020/4/29 11:41
 * @company:曙光集团绿康元公司
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private DataItemMapper dataItemMapper;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Value("${spring.mail.username}")
    private String username;
//    @Autowired
//    @Qualifier("waterQualityScheduler")
//    private Scheduler scheduler;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(Template template) {
        //1.将模板对象数据保存到数据库,且模板的状态为不可用
        //1.1封装id
        long id = snowflakeIdWorker.nextId();
        long num = id;
        template.setId(id);
        template.setStatus(Constant.DISABLED);
        //1.2封装数据项集合
        List<DataItem> dataItemList = template.getDataItemList();
        if (dataItemList != null && dataItemList.size() > 0) {
            List<Long> dataItemIds = new ArrayList<>();
            for (DataItem dataItem : dataItemList) {
                id = snowflakeIdWorker.nextId();
                dataItem.setId(id);
                dataItemIds.add(id);
            }
            template.setDataItemIds(dataItemIds);
        }

        //1.3保存模板信息
        String userName = UserUtils.getUserName();
        template.setOperator(userName);
        template.setStoreDate(new Date());
        template.setUpdateDate(new Date());
        templateMapper.insert(template);

        //2保存数据项
        if (dataItemList != null && dataItemList.size() > 0) {
            dataItemMapper.batchInsert(dataItemList);
        }

        //3.发送邮件通知完善接口功能
//        sendEmail(num);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        judge(id);
        //1.通过id获取到模板的数据项id集合
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("data_item_ids");
        queryWrapper.in("id", id);
        List<Map<String, Object>> maps = templateMapper.selectMaps(queryWrapper);
        templateMapper.deleteById(id);
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = maps.get(0);
            if (map != null && map.size() > 0) {
                String strDataItemIds = (String) map.get("data_item_ids");
                if (strDataItemIds != null && !strDataItemIds.equals("")) {
                    String[] strings = strDataItemIds.split(",");
                    dataItemMapper.deleteBatchIds(Arrays.asList(strings));
                }
            }
        }
    }

    @Override
    public Template findById(Long id) {
        Template template = templateMapper.selectById(id);
        List<Long> dataItemIds = template.getDataItemIds();
        if (dataItemIds != null || dataItemIds.size() > 0) {
//            template.setDataItemIds(null);
            List<DataItem> dataItemList = dataItemMapper.selectBatchIds(dataItemIds);
            template.setDataItemList(dataItemList);
        }
        return template;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(Template template) {
        judge(template.getId());
        Long id = template.getId();
        Template oldTemplate = findById(id);
        if (oldTemplate == null) {
            throw new RuntimeException("错误的请求");
        }
        template.setStatus(null);
        template.setDataItemIds(null);
        template.setStoreDate(null);
        template.setUpdateDate(new Date());
        String operator = UserUtils.getUserName();
        template.setOperator(operator);
        List<DataItem> oldDataItemList = oldTemplate.getDataItemList();
        List<DataItem> dataItemList = template.getDataItemList();
        if (dataItemList == null || dataItemList.size() == 0) {
            templateMapper.updateById(template);
            if (oldDataItemList != null || oldDataItemList.size() > 0) {
                templateMapper.deleteBatchIds(oldTemplate.getDataItemIds());
            }
        } else {
            if (oldDataItemList == null || oldDataItemList.size() == 0) {
                List<Long> dataItemIds = new ArrayList<>();
                for (DataItem dataItem : dataItemList) {
                    long dataItemId = snowflakeIdWorker.nextId();
                    dataItem.setId(dataItemId);
                    dataItemIds.add(dataItemId);
                }
                template.setDataItemIds(dataItemIds);
                templateMapper.updateById(template);
                dataItemMapper.batchInsert(dataItemList);
            } else {
                List<Long> oldDataItemIds = oldTemplate.getDataItemIds();
                List<Long> dataItemIds = new ArrayList<>();
                dataItemMapper.deleteBatchIds(oldDataItemIds);//删除原有的数据项数据集合
                for (DataItem dataItem : dataItemList) {
                    long dataItemId = snowflakeIdWorker.nextId();
                    dataItem.setId(dataItemId);
                    dataItemIds.add(dataItemId);
                }


                template.setDataItemIds(dataItemIds);
                templateMapper.updateByTemplate(template);
                dataItemMapper.batchInsert(dataItemList);
            }
        }
    }

    @Override
    public PageResult pageQuery(Long pageNum, Long pageSize, String name, String status) {
        if (pageSize == null) {
            pageSize = Constant.PAGE_SIZE;
        }
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.equals("")) {
            queryWrapper.like("name", name);
        }
        if (status != null && !status.equals("")) {
            queryWrapper.eq("status", status);


        }
        IPage<Template> iPage = templateMapper.selectPage(new Page<Template>(pageNum, pageSize), queryWrapper);

        return new PageResult(StatusCode.OK, Constant.PAGE_QUERY_SUCCESS, iPage.getTotal(), pageSize, pageNum, iPage.getRecords());
    }

    public void sendEmail(Long num) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("1559285547@qq.com");
        simpleMailMessage.setSubject("模板开发通知");
        simpleMailMessage.setText(MailMessageUtils.getTemplateTaskMessage(num));
        javaMailSender.send(simpleMailMessage);
    }

    public void judge(Long id){
//        boolean started = false;
//        try {
//            TriggerKey triggerKey = TriggerKey.triggerKey(Constant.WATER_QUALITY_JOB_NAME);
//            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
//            /**
//             * "BLOCKED", "阻塞"
//             * "COMPLETE", "完成"
//             * "ERROR", "出错"
//             * "NONE", "不存在"
//             * "NORMAL", "正常"
//             * "PAUSED", "暂停"
//             */
//            if (triggerState.equals(Trigger.TriggerState.BLOCKED) || triggerState.equals(Trigger.TriggerState.NORMAL))
//            started = scheduler.isStarted();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//        if (started){
//            throw new SofnException("模板开启中，无法进行该操作");
//        }
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("id", id);
        List<Map<String, Object>> maps = templateMapper.selectMaps(queryWrapper);
        String status = (String) maps.get(0).get("status");
        if (status.equals(Constant.UP)){
            throw new SofnException("不允许的操作，模板为停止");
        }
    }
}
