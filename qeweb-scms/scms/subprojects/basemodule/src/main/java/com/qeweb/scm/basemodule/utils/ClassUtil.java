package com.qeweb.scm.basemodule.utils;

import java.sql.Timestamp;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;


public class ClassUtil {

    private static final Logger _logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (Exception ex) {
            _logger.warn("Cant instance object for '" + clazz + "', " + ex.getMessage());
        }
        return o;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(String clazz) {
        Object o = null;
        Class<?> c = null;
        try {
            c = Class.forName(clazz);
        } catch (Exception ex) {
            _logger.warn("Cant instance object for '" + clazz + "', " + ex.getMessage());
        }
        if (c != null) {
            o = newInstance(c);
        }
        return o;
    }

    /**
     * 取得一个对象的真实类名，主要处理cglib动态代理问题
     *
     * @param obj
     * @return
     */
    public static Class<?> getRealClass(Object obj) {
        return AopUtils.getTargetClass(obj);
    }

    /**
     *
     * @param methodName
     * @return
     */
    public static String getMethodProperty(String methodName) {
        String f = methodName;
        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            f = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
        return f;
    }

    /**
     * check give class whether or not basic class type.
     *
     * @param c
     * @return
     */
    public static boolean isBasicClassType(Class<?> c) {
        return c == String.class
                || c == Integer.class
                || c == int.class
                || c == Long.class || c == long.class
                || c == Double.class || c == double.class
                || c == Date.class
                || c == Timestamp.class
                || c == Boolean.class
                || c == boolean.class;
    }

    /**
     * fix the class simple name to property style.
     *
     * @param obj
     * @return
     */
    public static String getSimpleClassName(Class<?> c) {
        String sname = c.getSimpleName();
        return sname.substring(0, 1).toLowerCase() + sname.substring(1);
    }

    /**
     *
     * @param t
     * @return
     */
    public static String getCurrentClassAndMethod(Throwable t) {
        String cName = t.getStackTrace()[0].getClassName();
        String cMethod = t.getStackTrace()[0].getMethodName();
        return cName + "." + cMethod;
    }
}
