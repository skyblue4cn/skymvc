package com.bluesky.jeecg.framework.web.servlet;

import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 上午10:43
 */
public class DiHandlerInterceptor implements IHandlerInterceptor {
        private Object[] parameterObject;
    /**
     * 执行依赖转换
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod=((HandlerExecutionChain) handler).getHandlerMethod();

        //获取参数
        getAllParameters(handlerMethod);
        setValueforParameters(request, response);
        return true;
    }

    /**
     * 获取所有参数
     * 郭建林
     * 2014年1月20日10:58:14
     * @param handlerMethod  方法对象
     */
    private Object[] getAllParameters(HandlerMethod handlerMethod)
    {
        //判断是否有值
        if( handlerMethod!=null)
        {
        parameterObject=   handlerMethod.getMethod().getParameterTypes();
        }
       return parameterObject;
    }
    /**
     * 设置参数的值
     * 郭建林
     * 2014年1月20日11:34:50
     *
     * */

     private void setValueforParameters(HttpServletRequest request, HttpServletResponse response) throws Exception
     {
         //页面的参数
         Map map=  request.getParameterMap();
         //封装后的对象列表
         Map<String,Object> ParametersValue=new HashMap<String, Object>();
         //循环参数列表
         for(Object target:parameterObject)
          {
                 target =target.getClass().newInstance();
              System.out.println(target.getClass());
              //判断是否是req和resp如果是则进行下次循环
              if (target.getClass().getPackage().equals(request) )
              {
                  target=request;
                  ParametersValue.put(target.getClass().getName(),target);
                  continue;
              }
              if ( target.getClass().equals(response))
              {
                  target=response;
                  ParametersValue.put(target.getClass().getName(),target);
                  continue;
              }
              //如果是其他对象


             if(target.getClass().getDeclaredFields().length>0)
             {
            target.getClass().getDeclaredFields()[0].getName();
             }
             System.out.println( target.getClass().getDeclaredFields()[0].getName());
           }
     }
    public Object[] getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object[] parameterObject) {
        this.parameterObject = parameterObject;
    }
}
