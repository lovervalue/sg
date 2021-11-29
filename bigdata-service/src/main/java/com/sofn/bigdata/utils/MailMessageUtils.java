package com.sofn.bigdata.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:袁彬锋
 * @date:2020/4/29 12:34
 * @company:曙光集团绿康元公司
 * @description:
 */
public class MailMessageUtils {

    public static String getTemplateTaskMessage(Long id){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        return "您好，新的模板已经生成，模板编号为：" + id + "\r\n" + "请尽快完善！" + "\r\n" + date;
    }
}
