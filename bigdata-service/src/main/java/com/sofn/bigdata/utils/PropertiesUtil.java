package com.sofn.bigdata.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties getProperties(ClassLoader classLoader, String filePath) throws IOException {
        Properties properties = new Properties();
        InputStreamReader resourceAsStream = new InputStreamReader(classLoader.getResourceAsStream(filePath),"UTF-8");
        properties.load(resourceAsStream);
        return properties;
    }
}
