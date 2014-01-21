package com.bluesky.jeecg.framework.web.servlet;

import com.bluesky.jeecg.framework.web.httphandler.HandlerMapping;
import com.bluesky.jeecg.framework.web.httphandler.InitMethodMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: bluesky
 * Date: 14-1-16
 * Time: 下午5:43
 */
public class DispatcherServlet extends FrameworkServlet {
    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
    HandlerExecutionChain mappedHandler =null;
    HandlerMapping handlerMapping =null;

    /**
     * 初始化控制器和url映射
     * 郭建林
     * 2014年1月19日10:03:55
     * @throws ServletException
     */
    @Override
    protected void initFrameworkServlet() throws ServletException {

       initHandlerMapping();
        initHandlerExecutionChain();
    }

    /**
     * 初始化handlermethodsmapping
     */
    protected void initHandlerMapping()
    {
        try{
            logger.info("init all methods start!");
        Class<?> clazz = ClassUtils.forName("com.bluesky.jeecg.framework.web.httphandler.InitMethodMapping", DispatcherServlet.class.getClassLoader());
         Object strategy = createDefaultStrategy(this.getWebApplicationContext(), clazz);
            handlerMapping= (HandlerMapping)strategy;
            logger.info("init all methods complete!");
        }
        catch (Exception e)
        {
             e.printStackTrace();
        }
    }

    /**
     * 初始化执行对象
     * 郭建林
     * 2014年1月19日15:42:06
     */
    private void initHandlerExecutionChain()
    {
        mappedHandler=(HandlerExecutionChain)createDefaultStrategy(getWebApplicationContext(),HandlerExecutionChain.class);
        //设置执行类中的方法属性
        mappedHandler.setHandlerMethodsMapping(((InitMethodMapping)handlerMapping).getHandlerMethods());
    }

    /**
     * 在spring容器中创建一个对象
     * @param context
     * @param clazz
     * @return
     */
    protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }
    @Override
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req,resp);
    }


    /**
     * 执行mvc业务处理方法
     * 郭建林
     * 2014年1月19日15:22:31
     * @param req
     * @param resp
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {


        try
        {
            //执行对象
            mappedHandler.ExecutionHandler(req, resp);

           //时间问题暂时默认实现一种
            RequestDispatcher dispatcher = req.getRequestDispatcher((String)req.getAttribute("URL"));

            dispatcher .forward(req, resp);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
