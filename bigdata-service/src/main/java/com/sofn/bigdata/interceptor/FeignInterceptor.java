package com.sofn.bigdata.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author:袁彬锋
 * @date:2020/4/27 14:28
 * @company:曙光集团绿康元公司
 * @description:feign调用拦截器，用于增加请求头
 */

@Component
public class FeignInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if(request != null){
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()){
                    String headerName = headerNames.nextElement();
                    if("Authorization".equals(headerName)){
                        String headerValue = request.getHeader(headerName);
                        requestTemplate.header(headerName,headerValue);
                    }
                }
            }
        }
    }
}
