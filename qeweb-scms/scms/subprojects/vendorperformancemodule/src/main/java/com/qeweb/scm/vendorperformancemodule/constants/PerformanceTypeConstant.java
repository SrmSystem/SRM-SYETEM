package com.qeweb.scm.vendorperformancemodule.constants;

import java.util.List;

import com.google.common.collect.Lists;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;

/**
 * 绩效的各种类型的常量
 * @author pjjxiajun
 * @date 2015年8月15日
 * @path com.qeweb.scm.vendorperformancemodule.constants.PerfromanceTypeConstant.java
 */
public class PerformanceTypeConstant extends VendorModuleTypeConstant{
	/*************** 模版类型 ******************/
	/** 普通 */
	public final static Integer TEMPLATE_TYPE_COMMON = 0;
	/** 综合 */
	public final static Integer TEMPLATE_TYPE_SPUER = 1;

	/*************** 模版设置权重类型常量 ******************/
	/** 平均 */
	public final static Integer PER_TEMPLATE_WEI_AVG = 0;
	/** 手动 */
	public final static Integer PER_TEMPLATE_WEI_MANUAL = 1;
	/** 公式 */
	public final static Integer PER_TEMPLATE_WEI_FORMULA = 2;
	
	/*************** 模版设置来源类型常量 ******************/
	/** 维度 */
	public final static Integer PER_TEMPLATE_SET_DIM = 0;
	/** 指标 */
	public final static Integer PER_TEMPLATE_SET_INDEX = 1;
	/** 模版 */
	public final static Integer PER_TEMPLATE_SET_TEMPLATE = 2;
	
	/*************** 周期固定编码 ******************/
	/** 周*/
	public final static String PER_CYCLE_WEEK = "week";
	/** 月 */
	public final static String PER_CYCLE_MONTH = "month";
	/** 季度 */
	public final static String PER_CYCLE_QUARTER = "quarter";
	/** 半年 */
	public final static String PER_CYCLE_HALFYEAR = "halfyear";
	/** 年 */
	public final static String PER_CYCLE_YEAR = "year";
	
	
	/*************** 维度映射集合 ******************/
	public final static List<String> MAPPINGSCORELIST = Lists.newArrayList(
	"score1","score2","score3","score4","score5","score6","score7","score8","score9","score10",
	"score11","score12","score13","score14","score15","score16","score17","score18","score19","score20",
	"score21","score22","score23","score24","score25","score26","score27","score28","score29","score30"
			);
	
	/*************** 总分映射集合 ******************/
	public final static List<String> TOTALMAPPINGSCORELIST = Lists.newArrayList(
			"totals1","totals2","totals3","totals4","totals5"
			);
	
	/** 零部件的最低分 */
	public final static Integer PER_DIM_LOWEST = 1;
	
	/** 零部件的平均分 */
	public final static Integer PER_DIM_AVERAGE = 2;
	
	/** 总分—零部件的扣分综合  */
	public final static Integer PER_DIM_COMPREHENSIVE = 3;
	
	public final static int PERFORMANCE_LEVEL_VEN = 0;
	public final static int PERFORMANCE_LEVEL_VEN_MATTYPE = 1;
	
	
}
