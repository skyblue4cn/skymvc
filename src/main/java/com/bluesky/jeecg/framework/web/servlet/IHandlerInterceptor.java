package com.bluesky.jeecg.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 上午10:35
 */
public interface IHandlerInterceptor {
    /**
     * 每个请求都需要执行的拦截器方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception;
}
