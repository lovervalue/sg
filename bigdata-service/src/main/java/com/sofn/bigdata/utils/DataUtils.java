package com.sofn.bigdata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUtils {

    public static List<String> getFormatList(String oldStr) {
        List<String> newArr = new ArrayList<>();
        if(null == oldStr || "".equals(oldStr)){
            return newArr;
        }
        if(oldStr.contains(",")){
            String[] newStr = oldStr.split(",");
            newArr.addAll(Arrays.asList(newStr));
        }else{
            newArr.add(oldStr);
        }

        return newArr;
    }
}
