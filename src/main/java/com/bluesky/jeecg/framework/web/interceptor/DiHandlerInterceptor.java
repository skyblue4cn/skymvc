package com.bluesky.jeecg.framework.web.interceptor;

import com.bluesky.jeecg.framework.web.servlet.HandlerExecutionChain;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 上午10:43
 */
public class DiHandlerInterceptor implements IHandlerInterceptor {
    //参数列表类型
        private Object[] parameterObject;
    //参数名
    private String[] parameterNames;
    //封装后的对象列表
    Map<String,Object> ParametersValue;
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
        setValueOfParameters(request, response,handlerMethod);
        //执行方法调用
        InvokMethod(handlerMethod,((HandlerExecutionChain) handler).getApplicationContext().getBean(handlerMethod.getBeanType()));
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
            // 方法参数名
            Paranamer paranamer = new BytecodeReadingParanamer();
            parameterNames= paranamer.lookupParameterNames(handlerMethod.getMethod());
        }
       return parameterObject;
    }
    /**
     * 设置参数的值
     * 郭建林
     * 2014年1月20日11:34:50
     *
     * */

     private void setValueOfParameters(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception
     {
         //页面的参数
         Map<String,Object> requestMap=  request.getParameterMap();
         Map<String,Object> map =new HashMap<String, Object>();
         //处理map的字符数组问题
         Set<String> mapkey = requestMap.keySet();
         for(String key:mapkey)
         {
             String temp=((String[])requestMap.get(key))[0];
             map.put(key,temp);
         }


         //封装后的对象列表
          ParametersValue=new HashMap<String, Object>();

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
                  i++;
                  continue;
              }
              if ( val.equals("javax.servlet.http.HttpServletResponse"))
              {
                  target=response;
                  ParametersValue.put(parameterNames[i],target);
                  i++;
                  continue;
              }
                  ParametersValue.put(parameterNames[i], setValueOfSingeObject(map, target));
              i++;
           }

         /**给对象设置值*/



     }

    /**
     * 执行方法调用
     * 郭建林
     * 2014年1月21日14:52:39
     * @return
     */

    private Object InvokMethod(HandlerMethod handlerMethod,Object handler) throws Exception
    {

        //初始化参数进行调用
                 Object[] args= new Object[parameterNames.length];
                  int i=0;
                  for(String key:ParametersValue.keySet())
                  {

                      args[i]=ParametersValue.get(key);
                      i++;
                  }
                 handlerMethod.getMethod().invoke(handler,args);
                   return null;
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
        // 调用getter方法获取属性值
        Method m = (Method) target.getClass().getMethod(
                "getName");
        target.getClass().getName();
        String val = (String) m.invoke(target);
        target=Class.forName(val).newInstance();
        //如果是其他对象
        if (target instanceof String)
        {
            target=target.toString();
            return target;
        }else if (target instanceof Integer)
        {
            target=Integer.parseInt(target.toString());
            return target;
        }
        //如果页面提交的有值在进行注入
        for(String key : keyValue.keySet())
        {
                //挨个给字段赋值
                    if(target.getClass().getDeclaredFields().length>0)
                    {
                                for(Field field:target.getClass().getDeclaredFields())
                                {
                                    //如果数据不匹配就进行下一次循环
                                    if(!field.getName().equals(key))continue;
                                    try{
                                    Method[] method = target.getClass().getMethods();
                                    //设置值
                                    Object[] args={keyValue.get(key)};
                                       target.getClass().getDeclaredMethod("set" + StringUtils.capitalize(field.getName()),field.getType()).invoke(target,args);
                                    }   catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                    }
            }
         return target;
    }

}
