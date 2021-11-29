package com.sofn.bigdata.utils;

import com.sofn.common.utils.JwtUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

public class UserUtils {
    public static String getUserName(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null){
            String token = null;
            try {
                token = request.getHeader("Authorization");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("未登录，请登陆之后在操作");
            }
            return JwtUtils.getUsername(token);
        }else {
            throw new RuntimeException("未获取到请求对象");
        }
    }
}
