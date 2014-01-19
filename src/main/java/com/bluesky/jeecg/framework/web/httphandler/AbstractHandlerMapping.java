package com.bluesky.jeecg.framework.web.httphandler;

import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 * User: bluesky
 * Date: 14-1-19
 * Time: 下午2:13
 */
public class AbstractHandlerMapping extends WebApplicationObjectSupport
        implements HandlerMapping {


    @Override
    public HandlerMapping getHandler() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
