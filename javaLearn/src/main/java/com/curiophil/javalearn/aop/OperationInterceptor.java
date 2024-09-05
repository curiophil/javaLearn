package com.curiophil.javalearn.aop;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class OperationInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private static final String REQUEST_ID = "requestId";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 生成唯一的 requestId
        String requestId = UUID.randomUUID().toString();
        // 将 requestId 放入 MDC 中
        MDC.put(REQUEST_ID, requestId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求处理完成后清除 MDC 中的 requestId
        MDC.remove(REQUEST_ID);
    }


}
