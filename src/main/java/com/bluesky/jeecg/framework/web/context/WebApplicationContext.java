package com.bluesky.jeecg.framework.web.context;

import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * User: blusky
 * Date: 14-1-16
 * Time: 下午5:38
 */
public interface WebApplicationContext extends ApplicationContext {
    /**
     * Return the standard Servlet API ServletContext for this application.
     * <p>Also available for a Portlet application, in addition to the PortletContext.
     */
    ServletContext getServletContext();
}
