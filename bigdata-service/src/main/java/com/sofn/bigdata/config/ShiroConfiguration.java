package com.sofn.bigdata.config;

import com.sofn.common.config.BaseShiroConfig;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by sofn
 */
@Configuration
public class ShiroConfiguration extends BaseShiroConfig {

    @Override
    protected void filter(Map<String, String> filterMap) {
        filterMap.put("/marshInfo/marshInfos/toHdfs", "anon");
    }
}
