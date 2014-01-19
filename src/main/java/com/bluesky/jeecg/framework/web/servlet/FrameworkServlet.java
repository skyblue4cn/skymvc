package com.bluesky.jeecg.framework.web.servlet;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: bluesky
 * Date: 14-1-17
 * Time: 上午9:43
 */
public class FrameworkServlet extends HttpServletBean {
    //servlet配置参数值
    private String contextConfigLocation;
    //继承spring的子容器

    /**
     * Suffix for WebApplicationContext namespaces. If a servlet of this class is
     * given the name "test" in a context, the namespace used by the servlet will
     * resolve to "test-servlet".
     */
    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
    /** Namespace for this servlet */
    private String namespace;
    /** WebApplicationContext id to assign */
    private String contextId;
    private WebApplicationContext webApplicationContext;
    /** ServletContext attribute to find the WebApplicationContext in */
    private String contextAttribute;
    /** WebApplicationContext implementation class to create */
    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
    /** Flag used to detect whether onRefresh has already been called */
    private boolean refreshEventReceived = false;
    /**
     * Default context class for FrameworkServlet.
     * @see org.springframework.web.context.support.XmlWebApplicationContext
     */
    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    /**
     * Return the custom context class.
     */
    public Class<?> getContextClass() {
        return this.contextClass;
    }
    /**
     * 获取httpservletBean通过beanwarp注册的属性
     */
    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * Return the explicit context config location, if any.
     */
    public String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    public WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    /**
     * 对httpservletbean的实现
     * User bluesky
     * @throws ServletException
     */
    @Override
    protected void initServletBean() throws ServletException {
        getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
        if (this.logger.isInfoEnabled()) {
            this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
        }
        long startTime = System.currentTimeMillis();

        try {
            //初始化基于spring容器的上下文对象
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        }
        catch (ServletException ex) {
            this.logger.error("Context initialization failed", ex);
            throw ex;
        }
        catch (RuntimeException ex) {
            this.logger.error("Context initialization failed", ex);
            throw ex;
        }

        if (this.logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " +
                    elapsedTime + " ms");
        }
    }
    /**
     *初始化子上下文对象设置spring顶级容器为父上下文对象
     * this code from spring mvc httpservletbean
     */
    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext wac = null;

        if (this.webApplicationContext != null) {
            // A context instance was injected at construction time -> use it
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                if (!cwac.isActive()) {
                    // The context has not yet been refreshed -> provide services such as
                    // setting the parent context, setting the application context id, etc
                    if (cwac.getParent() == null) {
                        // The context instance was injected without an explicit parent -> set
                        // the root application context (if any; may be null) as the parent
                        cwac.setParent(rootContext);
                    }
                }
            }
        }
        if (wac == null) {
            // No context instance was injected at construction time -> see if one
            // has been registered in the servlet context. If one exists, it is assumed
            // that the parent context (if any) has already been set and that the
            // user has performed any initialization such as setting the context id
            wac = findWebApplicationContext();
        }
        if (wac == null) {
            // No context instance is defined for this servlet -> create a local one
            wac = createWebApplicationContext(rootContext);
        }
        if (!this.refreshEventReceived) {
            // Either the context is not a ConfigurableApplicationContext with refresh
            // support or the context injected at construction time had already been
            // refreshed -> trigger initial onRefresh manually here.
            onRefresh(wac);
        }
        return wac;
    }


    /**
     * Callback that receives refresh events from this servlet's WebApplicationContext.
     * <p>The default implementation calls {@link #onRefresh},
     * triggering a refresh of this servlet's context-dependent state.
     * @param event the incoming ApplicationContext event
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived = true;
        onRefresh(event.getApplicationContext());
    }


    /**
     * Refresh this servlet's application context, as well as the
     * dependent state of the servlet.
     * @see #getWebApplicationContext()
     * @see org.springframework.context.ConfigurableApplicationContext#refresh()
     */
    public void refresh() {
        WebApplicationContext wac = getWebApplicationContext();
        if (!(wac instanceof ConfigurableApplicationContext)) {
            throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
        }
        ((ConfigurableApplicationContext) wac).refresh();
    }
    /**
     * Template method which can be overridden to add servlet-specific refresh work.
     * Called after successful context refresh.
     * <p>This implementation is empty.
     * @param context the current WebApplicationContext
     * @see #refresh()
     */
    protected void onRefresh(ApplicationContext context) {
        // For subclasses: do nothing by default.
    }
    /**
     * 创建一个基于父容器的子上下文对象
     * this code from spring mvc httpservletbean
     */
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        Class<?> contextClass = getContextClass();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Servlet with name '" + getServletName() +
                    "' will try to create custom WebApplicationContext context of class '" +
                    contextClass.getName() + "'" + ", using parent context [" + parent + "]");
        }
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException(
                    "Fatal initialization error in servlet with name '" + getServletName() +
                            "': custom WebApplicationContext class [" + contextClass.getName() +
                            "] is not of type ConfigurableWebApplicationContext");
        }
        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        wac.setConfigLocation(getContextConfigLocation());
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }
    protected WebApplicationContext findWebApplicationContext() {
        String attrName = getContextAttribute();
        if (attrName == null) {
            return null;
        }
        WebApplicationContext wac =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
        }

        return wac;
    }


    /**
     * Set the name of the ServletContext attribute which should be used to retrieve the
     * {@link WebApplicationContext} that this servlet is supposed to use.
     */
    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    /**
     * Return the name of the ServletContext attribute which should be used to retrieve the
     * {@link WebApplicationContext} that this servlet is supposed to use.
     */
    public String getContextAttribute() {
        return this.contextAttribute;
    }

    /**
     * 创建好上下文对象后对容器进行刷新和加载配置文件的bean
     * @param wac
     */
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
            // The application context id is still set to its original default value
            // -> assign a more useful id based on available information
            if (this.contextId != null) {
                wac.setId(this.contextId);
            }
            else {
                // Generate default id...
                ServletContext sc = getServletContext();
                if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
                    // Servlet <= 2.4: resort to name specified in web.xml, if any.
                    String servletContextName = sc.getServletContextName();
                    if (servletContextName != null) {
                        wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + servletContextName +
                                "." + getServletName());
                    }
                    else {
                        wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + getServletName());
                    }
                }
                else {
                    // Servlet 2.5's getContextPath available!
                    wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
                            ObjectUtils.getDisplayString(sc.getContextPath()) + "/" + getServletName());
                }
            }
        }

        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        wac.setNamespace(getNamespace());
        wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));

        // the wac environment's #initPropertySources will be called in any case when
        // the context is refreshed; do it eagerly here to ensure servlet property sources
        // are in place for use in any post-processing or initialization that occurs
        // below prior to #refresh
        ConfigurableEnvironment env = wac.getEnvironment();
        if (env instanceof ConfigurableWebEnvironment) {
            ((ConfigurableWebEnvironment)env).initPropertySources(getServletContext(), getServletConfig());
        }
        wac.refresh();
    }
    /**
     * ApplicationListener endpoint that receives events from this servlet's WebApplicationContext
     * only, delegating to {@code onApplicationEvent} on the FrameworkServlet instance.
     */
    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

        public void onApplicationEvent(ContextRefreshedEvent event) {
            FrameworkServlet.this.onApplicationEvent(event);
        }
    }
    /**
     * Return the namespace for this servlet, falling back to default scheme if
     * no custom namespace was set: e.g. "test-servlet" for a servlet named "test".
     */
    public String getNamespace() {
        return (this.namespace != null ? this.namespace : getServletName() + DEFAULT_NAMESPACE_SUFFIX);
    }
    protected void initFrameworkServlet() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
    /**
     * 处理请求方法
     * 郭建林
     * 2014年1月19日10:09:03
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    }

}
