package com.bluesky.jeecg.framework.web.tools;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 下午5:49
 */
public class Classes {


    /**
     * <p>
     * <font color="red">依赖javassit</font>的工具类，获取方法的参数名
     * </p>
     *
     * @author dixingxing
     * @date Apr 20, 2012
     */
        private Classes() {}
    /**
     * 反射获取方法参数名称
     */
    public static String[] getReflectParamName(Class clazz,String methodName) {
        String[] paramNames=null;
        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());
            CtMethod cm = cc.getDeclaredMethod(methodName);

            // 使用javaassist的反射方法获取方法的参数名
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                // exception
            }
            paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++)
                paramNames[i] = attr.variableName(i + pos);


        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return paramNames;
    }


}
