package com.sofn.bigdata.utils;

import com.sofn.bigdata.enums.RuleEnum;
import com.sofn.bigdata.model.CleanRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class CleanUtils {

    private static boolean isValid(List<CleanRule> rules, Object data)
            throws IllegalArgumentException, IllegalAccessException, ParseException {
        Field[] fields = data.getClass().getDeclaredFields();

        boolean flag = true;
        for (CleanRule rule : rules) {
            String fieldName = rule.getFieldName();
            RuleEnum ruleType = rule.getRuleType();

            //获取当前规则对应约束的那个字段的值
            Object content = null;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(fieldName)) {
                    content = field.get(data);
                }
            }

            if (rule.getFieldType().equals("Number")) {
                double objValue = Double.parseDouble(content.toString());
                double ruleValue = Double.parseDouble(rule.getRuleValue());
                switch (ruleType) {
                    case MORE:
                        flag = objValue > ruleValue;
                        break;
                    case EQUALS:
                        flag = Math.abs(objValue - ruleValue) < 0.00000001;
                        break;
                    case LESS:
                        flag = objValue < ruleValue;
                        break;
                }
                if(!flag) return false;
            } else if (rule.getFieldType().equals("String")) {
                switch (ruleType) {
                    case NOT_EMPTY:
                        flag = StringUtils.isNotEmpty(content.toString());
                        break;
                    case CONTAINS:
                        flag = StringUtils.isNotEmpty(content.toString())
                                && content.toString().contains(rule.getRuleValue());
                        break;
                    case NOT_CONTAINS:
                        flag = !(StringUtils.isNotEmpty(content.toString())
                                && content.toString().contains(rule.getRuleValue()));
                        break;
                }
                if(!flag) return false;
            } else if (rule.getFieldType().equals("Date")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date objDate = (Date) content;
                Date ruleDate = sdf.parse(rule.getRuleValue());
                switch (ruleType) {
                    case EARLIER:
                        flag = objDate.before(ruleDate);
                        break;
                    case LATER:
                        flag = objDate.after(ruleDate);
                        break;
                }
                if(!flag) return false;
            }
        }
        return flag;
    }

}
