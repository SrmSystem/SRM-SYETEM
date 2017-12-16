package com.qeweb.scm.basemodule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTransfer {
	
	/**
	 * 从Excel第几行开始读写
	 * @return
	 */
	int start() default 0;
	
	/**
	 * 描述，报表抬头
	 * @return
	 */
	String describe();
}
