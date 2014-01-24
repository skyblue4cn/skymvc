package com.bluesky.jeecg.framework.web.servlet;

import com.bluesky.jeecg.framework.web.httphandler.AbstractHandlerMapping;
import com.bluesky.jeecg.framework.web.httphandler.HandlerMapping;
import com.bluesky.jeecg.framework.web.interceptor.DiHandlerInterceptor;
import com.bluesky.jeecg.framework.web.interceptor.IHandlerInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: bluesky
 * Date: 14-1-19
 * Time: 下午1:17
 * 封装控制器需要的参数
 */
public class HandlerExecutionChain extends AbstractHandlerMapping implements HandlerMapping,InitializingBean {
    private Map<String, HandlerMethod> handlerMethodsMapping=null;
    private HandlerMethod handlerMethod=null;
    private List<IHandlerInterceptor> handlerInterceptors=null;

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
    public HandlerMethod ExecutionHandler(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
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
                      if(checkUrlIsMatch(requestMapping.value()[0],ControllerAndMethodStr[1]))
                      {
                   //是否采用对应的协议进行访问
                           if(req.getMethod().equals(requestMapping.method()[0].toString()))
                           {
                               try
                               {
                                   handlerMethod=handlerMethodsMapping.get(key);
                                   //如果成则执行拦截器
                                   exctionInterceptor(req, resp);
                                   return   handlerMethodsMapping.get(key);
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
        return null;
    }

    /**
     * 根据页面传递过来的参数进行依赖注入
     */
    private void setDiDependes()
    {

    }

    /**
     * 初始化方法
     * 郭建林
     * 2014年1月20日11:02:13
     * @return
     */
    private void initHandlerExcutionchain()
    {
        handlerInterceptors=new ArrayList<IHandlerInterceptor>();
        handlerInterceptors.add(new DiHandlerInterceptor());
    }
    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerExcutionchain();
    }

    public List<IHandlerInterceptor> getHandlerInterceptors() {
        return handlerInterceptors;
    }

    public void setHandlerInterceptors(List<IHandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }
    /**
     * 执行拦截器
     * 郭建林
     * 2014年1月20日11:06:23
     */
    private void exctionInterceptor(HttpServletRequest req, HttpServletResponse resp) throws Exception
    {
        for(IHandlerInterceptor interceptor:this.getHandlerInterceptors())
        {
            interceptor.preHandle(req,resp,this);
        }
    }

    /**
     * 检测提交的Url是否与控制器的方法匹配
     * 郭建林
     * 2014年1月24日14:23:34
     * @param pathInController       控制器里的路径   /user/{userId}/queryType/id/{typeId}
     * @param pathInReq              浏览器访问的地址 /user/13/queryType/id/12
     * */
    private boolean checkUrlIsMatch(String pathInController ,String pathInReq)
    {
        String pathInJava=new String(pathInController);
        String   frontPath=new String(pathInReq);

        pathInJava= pathInJava.replaceAll("\\{\\w*\\}","*");

        String[] temp= pathInJava.split("\\*");
        for(int i=0;i<temp.length;i++)
        {
            frontPath =frontPath.replace(temp[i],"");
        }
        if(frontPath.indexOf("/") ==-1)
        {
            return true;
        }
        return false;

    }
}
