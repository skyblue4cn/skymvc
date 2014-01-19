package com.bluesky.jeecg.framework.web.httphandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 * User: bluesky
 * Date: 14-1-19
 * Time: 下午2:13
 */
public class AbstractHandlerMapping extends WebApplicationObjectSupport
        implements HandlerMapping {
    /** Logger that is available to subclasses */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public HandlerMapping getHandler() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
