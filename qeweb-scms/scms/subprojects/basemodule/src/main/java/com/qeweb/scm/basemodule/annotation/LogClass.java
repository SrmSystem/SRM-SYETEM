package com.qeweb.scm.basemodule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogClass {
	/**
	 * 模块名称
	 * @return
	 */
	String module() default "";
	/**
	 * 方法
	 * @return
	 */
	String method() default "";
}
