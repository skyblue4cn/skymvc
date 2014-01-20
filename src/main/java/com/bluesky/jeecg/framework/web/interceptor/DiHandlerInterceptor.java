package com.bluesky.jeecg.framework.web.interceptor;

import com.bluesky.jeecg.framework.web.servlet.HandlerExecutionChain;
import com.bluesky.jeecg.framework.web.tools.Classes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 上午10:43
 */
public class DiHandlerInterceptor implements IHandlerInterceptor {
        private Object[] parameterObject;
    private String[] parameterNames;
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
        getAllParameters(handlerMethod, handler);
        //给参数设置值
        setValueOfParameters(request, response);
        return true;
    }

    /**
     * 获取所有参数
     * 郭建林
     * 2014年1月20日10:58:14
     * @param handlerMethod  方法对象
     */
    private Object[] getAllParameters(HandlerMethod handlerMethod,Object handler)
    {
        //判断是否有值
        if( handlerMethod!=null)
        {
        parameterObject=   handlerMethod.getMethod().getParameterTypes();
            parameterNames= Classes.getMethodParamNames(handler.getClass(),handlerMethod.getMethod().getName()+"");
        }
       return parameterObject;
    }
    /**
     * 设置参数的值
     * 郭建林
     * 2014年1月20日11:34:50
     *
     * */

     private void setValueOfParameters(HttpServletRequest request, HttpServletResponse response) throws Exception
     {
         //页面的参数
         Map map=  request.getParameterMap();
         //封装后的对象列表
         Map<String,Object> ParametersValue=new HashMap<String, Object>();

         int i=0;
         //循环参数列表
         for(Object target:parameterObject)
          {
              // 调用getter方法获取属性值
              Method m = (Method) target.getClass().getMethod(
                      "getName");
                        target.getClass().getName();
              String val = (String) m.invoke(target);
              //判断是否是req和resp如果是则进行下次循环
              if (val.equals("javax.servlet.http.HttpServletRequest") )
              {
                  target=request;
                  ParametersValue.put(parameterNames[i],target);
                  continue;
              }
              if ( val.equals("javax.servlet.http.HttpServletResponse"))
              {
                  target=response;
                  ParametersValue.put(parameterNames[i],target);
                  continue;
              }
              //如果是其他对象
              ParametersValue.put(parameterNames[i], setValueOfSingeObject(map, target));
              i++;
           }
         System.out.println();
     }
    public Object[] getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object[] parameterObject) {
        this.parameterObject = parameterObject;
    }

    /**
     * 给具体一个对象赋值
     * 郭建林
     * 2014年1月20日16:13:17
     * @param keyValue 键值对列表
     * @param target  目标对象
     */
    public Object setValueOfSingeObject(Map<String,Object> keyValue,Object target)  throws Exception
    {

        for(String key : keyValue.keySet())
        {
        if(!target.getClass().isPrimitive())
        {
        // 调用getter方法获取属性值
        Method m = (Method) target.getClass().getMethod(
                "getName");
        target.getClass().getName();
        String val = (String) m.invoke(target);
        target=Class.forName(val).newInstance();
        if(target.getClass().getDeclaredFields().length>0)
        {
            target.getClass().getDeclaredFields()[0].getName();
        }
        }
        else
        {
            target=keyValue.get(key);
        }
        }
         return target;
    }

}
