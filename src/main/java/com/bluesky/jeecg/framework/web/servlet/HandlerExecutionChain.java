package com.bluesky.jeecg.framework.web.servlet;

import com.bluesky.jeecg.framework.web.httphandler.AbstractHandlerMapping;
import com.bluesky.jeecg.framework.web.httphandler.HandlerMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 作者: bluesky
 * Date: 14-1-19
 * Time: 下午1:17
 * 封装控制器需要的参数
 */
public class HandlerExecutionChain extends AbstractHandlerMapping implements HandlerMapping{
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

    /**
     * 执行handler方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void ExecutionHandler(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
         for (String key:handlerMethodsMapping.keySet())
         {

           if(req.getRequestURI().indexOf(key)!=-1)
           {
             //获取控制以后的路径和方法的路径
            String[]  ControllerAndMethodStr = req.getRequestURI().split(key) ;
               //判断调用哪个方法
               if(key.equals(key))
               {
                   //方法注解信息
                   RequestMapping requestMapping=handlerMethodsMapping.get(key).getMethodAnnotation(RequestMapping.class);
                      if(ControllerAndMethodStr[1].replace("/","").equals(requestMapping.value()[0]) )
                      {
                          logger.info("----------"+req.getMethod()+"-----------"+requestMapping.method()[0]+"----------"+req.getMethod().equals(requestMapping.method()[0].toString()));
                   //是否采用对应的协议进行访问
                           if(req.getMethod().equals(requestMapping.method()[0].toString()))
                           {
                               try
                               {
                                handlerMethodsMapping.get(key).getMethod().invoke(this.getApplicationContext().getBean(handlerMethodsMapping.get(key).getBeanType()));
                               }catch (Exception e)
                               {
                                   e.printStackTrace();
                               }
                               break;
                           }
                      }
               }
           }
         }
    }
}
