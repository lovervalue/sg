package com.sofn.bigdata.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sofn.bigdata.enums.DataCacheKeyEnum;
import com.sofn.bigdata.vo.SysInterfaceManageVo;
import com.sofn.common.utils.JsonUtils;
import com.sofn.common.utils.RedisHelper;
import com.sofn.common.utils.RedisUtils;
import com.sofn.common.utils.SpringContextHolder;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存工具类
 */
@Slf4j
public class CacheUtils {

    private static RedisHelper redisHelper = SpringContextHolder.getBean(RedisHelper.class);
    private static RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

    private static boolean checkRedisHelper(){
        return redisHelper != null;
    }

    /**
     * 缓存农村清洁工程建设状况调查子系统数据共享接口对象
     *
     */
    public static void cacheAszdqqInter(SysInterfaceManageVo manageVo){
        if (!checkRedisHelper()){
            return;
        }
        if ( null == manageVo){
            return;
        }

        String aszdqqInterJson = JsonUtils.obj2json(manageVo);
        redisHelper.set(DataCacheKeyEnum.ASZDQQ_INTER_KEY.getCode(),aszdqqInterJson);
        log.info(String.format("Cache manageVo:%s",manageVo));
    }

    /**
     * 获取农村清洁工程建设状况调查子系统数据共享接口对象
     *
     */

    public static SysInterfaceManageVo getAszdqqInterByCache(){
        if (!checkRedisHelper()){
            return null;
        }

        if (redisHelper.hasKey(DataCacheKeyEnum.ASZDQQ_INTER_KEY.getCode())) {
            String aszdqqInterJson = (String) redisHelper.get(DataCacheKeyEnum.ASZDQQ_INTER_KEY.getCode());
            if (StringUtils.isNotBlank(aszdqqInterJson)) {
                return JsonUtils.json2obj(aszdqqInterJson, SysInterfaceManageVo.class);
            }
            return new SysInterfaceManageVo();
        }
        return null;
    }

}
