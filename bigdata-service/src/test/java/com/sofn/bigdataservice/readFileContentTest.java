package com.sofn.bigdataservice;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Acthor Tao.Lee @date 2021/3/8 15:49
 * @Description
 */

public class readFileContentTest {
    @Test
    public void readFileContentone() {
        File file = new File("D:\\tmp\\1.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        String[] s=sbf.toString().split("，");
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();

            for (int i=0;i<s.length;i++) {
                System.out.println( s[i].toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        System.out.println("++"+ sbf.toString());
    }
}
