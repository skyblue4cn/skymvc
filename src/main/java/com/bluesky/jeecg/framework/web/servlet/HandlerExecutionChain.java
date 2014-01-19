package com.bluesky.jeecg.framework.web.servlet;

import com.bluesky.jeecg.framework.web.httphandler.HandlerMapping;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;

/**
 * 作者: bluesky
 * Date: 14-1-19
 * Time: 下午1:17
 * 封装控制器需要的参数
 */
public class HandlerExecutionChain extends WebApplicationObjectSupport implements HandlerMapping{
    private Map<String, HandlerMethod> handlerMethodsMapping=null;

    /**
     * 作者 bluesky
     * 2014年1月19日15:26:01
     * 获取控制器对象
     *
     * @return
     */
    @Override
    public HandlerMapping getHandler() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, HandlerMethod> getHandlerMethodsMapping() {
        return handlerMethodsMapping;
    }

    public void setHandlerMethodsMapping(Map<String, HandlerMethod> handlerMethodsMapping) {
        this.handlerMethodsMapping = handlerMethodsMapping;
    }
}
