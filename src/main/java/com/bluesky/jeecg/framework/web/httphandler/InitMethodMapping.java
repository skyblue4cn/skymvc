package com.bluesky.jeecg.framework.web.httphandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.*;

/**
 * User: bluesky
 * Date: 14-1-19
 * Time: 下午1:12
 */
public class InitMethodMapping  extends AbstractHandlerMapping{

    //方法列表跟url对应
    private final Map<String, HandlerMethod> handlerMethods = new LinkedHashMap<String, HandlerMethod>();
    /** Logger that is available to subclasses */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 初始化方法列表
     * 郭建林
     * 2014年1月19日13:24:00
     */
    protected void initMethods()
    {
        logger.info("Looking for request methodMappings in application context ");
            selectMethods();

        logger.info("initMethods completed! ");

    }

    /**
     * 获取执行对象
     * @return
     */
    @Override
    public HandlerMapping getHandler() {
        initMethods();
        return this;
    }

    /**
     * 获取一个类的方法
     * @return
     */
    public void selectMethods() {
        //获取所有controller
        Map<String,Object> beans=this.getWebApplicationContext().getBeansWithAnnotation(Controller.class);

        //循环取出每个控制器的方法
        for (String bean:beans.keySet())
        {
            String ControllerValue=(beans.get(bean).getClass().getAnnotation(RequestMapping.class)).value()[0];

            //获取所有方法
            Method[] methods=beans.get(bean).getClass().getMethods();

            //获取含有annotion的类
            for (Method methodValue:methods)
            {
                 if(methodValue.getAnnotation(RequestMapping.class)!=null)
                 {
                     handlerMethods.put(ControllerValue,new HandlerMethod(bean,methodValue));

                 }
            }
        }


    }

    public Map<String, HandlerMethod> getHandlerMethods() {
        return handlerMethods;
    }
}
