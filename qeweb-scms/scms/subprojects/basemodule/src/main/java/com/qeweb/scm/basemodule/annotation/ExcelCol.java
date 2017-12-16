package com.qeweb.scm.basemodule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标实Excel导入导出POJO列信息
 * @author ALEX
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCol { 
	
	/**
	 * 列类型
	 * @return
	 */
	ColType type() default ColType.STRING;
	
	/**
	 * 所在列
	 * @return
	 */
	Column column();
	
	/**
	 * 是否必须非空
	 * @return
	 */
	boolean required() default false;
	
	/**
	 * 列字段描述
	 * @return
	 */
	String desc() default "";
}
