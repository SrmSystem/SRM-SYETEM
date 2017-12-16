package com.qeweb.scm.basemodule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过滤组织权限和用户等权限
 *
 * @author micfans
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QueryFilterRequired {

    /**
     * 过滤权限属性
     *
     * @return
     */
    String[] dataNames() default {};

    /**
     * 过滤权限类型
     *
     * @return
     */
    int[] dataTypes() default {};
}
