package com.bluesky.jeecg.framework.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * User: bluesky
 * Date: 14-1-16
 * Time: 下午5:43
 */
public class HttpServletBean extends HttpServlet {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 设置properties (Strings)的必须属性
     * config parameters to this servlet.
     */
    private final Set<String> requiredProperties = new HashSet<String>();
    //环境
    private ConfigurableEnvironment environment;
    @Override
    public final void init() throws ServletException {

            logger.info("Initializing servlet '" + getServletName() + "'");

        // 设置初始化参数中设置properties.
        try {
            PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
            bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
            //把配置值注册到容器中，在子类可以通过setter获取
            bw.setPropertyValues(pvs, true);
        }
        catch (BeansException ex) {
            logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
            throw ex;
        }
        initServletBean();
            logger.info("Servlet '" + getServletName() + "' configured successfully");

    }
    /**
     * 添加properties (Strings)的属性
     */
    protected final void addRequiredProperty(String property) {
        this.requiredProperties.add(property);
    }
    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if environment is not assignable to
     * {@code ConfigurableEnvironment}.
     */
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }

    /**
     * {@inheritDoc}
     * <p>If {@code null}, a new environment will be initialized via
     * {@link #createEnvironment()}.
     */
    public ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = this.createEnvironment();
        }
        return this.environment;
    }

    /**
     * Create and return a new {@link org.springframework.web.context.support.StandardServletEnvironment}. Subclasses may override
     * in order to configure the environment or specialize the environment type returned.
     */
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardServletEnvironment();
    }

    /**
     * 提供子类的扩展方法
     * @throws ServletException
     */
    protected void initServletBean() throws ServletException {
    }
    /**
     * PropertyValues implementation created from ServletConfig init parameters.
     * this code from spring mvc httpservletbean
     */
    private static class ServletConfigPropertyValues extends MutablePropertyValues {

        /**
         * Create new ServletConfigPropertyValues.
         * @param config ServletConfig we'll use to take PropertyValues from
         * @param requiredProperties set of property names we need, where
         * we can't accept default values
         * @throws ServletException if any required properties are missing
         */
        public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
                throws ServletException {

            Set<String> missingProps = (requiredProperties != null && !requiredProperties.isEmpty()) ?
                    new HashSet<String>(requiredProperties) : null;

            Enumeration en = config.getInitParameterNames();
            while (en.hasMoreElements()) {
                String property = (String) en.nextElement();
                Object value = config.getInitParameter(property);
                addPropertyValue(new PropertyValue(property, value));
                if (missingProps != null) {
                    missingProps.remove(property);
                }
            }

            // Fail if we are still missing properties.
            if (missingProps != null && missingProps.size() > 0) {
                throw new ServletException(
                        "Initialization from ServletConfig for servlet '" + config.getServletName() +
                                "' failed; the following required properties were missing: " +
                                StringUtils.collectionToDelimitedString(missingProps, ", "));
            }
        }
    }

}
