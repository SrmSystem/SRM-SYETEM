package com.qeweb.scm.basemodule.constants;

import org.springside.modules.mapper.JsonMapper;

/**
 * 该模块所有类型的常量
 * @author pjjxiajun
 * @date 2015年6月17日
 * @path com.qeweb.scm.basemodule.constans.TypeConstant.java
 */
public class TypeConstant {
	public static JsonMapper mapper = JsonMapper.nonDefaultMapper();
	
	//公司业务范围的类型
	/** 业务范围 */
	public final static int BUSSINESSRANGE_TYPE_0 = 0;
	/** 业务类型 */
	public final static int BUSSINESSRANGE_TYPE_1 = 1;
	/** 品牌 */
	public final static int BUSSINESSRANGE_TYPE_2 = 2;
	/** 产品线 */
	public final static int BUSSINESSRANGE_TYPE_3 = 3;
	
	
	//统计分析 供应商数量 统计类型
	/** 统计分析 */
	public final static int STATISTICS_VENDOR_COUNT_LIST = 0;
	/** 统计分析--->按省份统计 */
	public final static int STATISTICS_VENDOR_COUNT_PROV = 1;
	/** 统计分析--->按省份、供应商性质统计供应商数量 */
	public static final int STATISTICS_VENDOR_COUNT_PROV_PHASE = 2;
	/** 统计分析--->按主供产品品类、供应商性质统计供应商数量 */
	public static final int STATISTICS_VENDOR_COUNT_MAT_PHASE = 3;
	/** 统计分析--->按省份、供应商所供车型统计供应商数量 */
	public static final int STATISTICS_VENDOR_COUNT_PROV_CARMODEL = 4;
	/** 统计分析--->筛选品牌按照省份统计供应商数量 */
	public static final int STATISTICS_VENDOR_COUNT_PROV_BRAND = 5;
	/** 统计分析--->筛选产品线、品牌按照供应商分类统计供应商数量 */
	public static final int STATISTICS_VENDOR_COUNT_VENDORCLASSIFY = 6;
	
	/**省份 */
	public static final String STATISTICS_PROV = "PROV";
	/**供应商性质*/
	public static final String STATISTICS_PHASE = "PHASE";
	/**零部件类别*/
	public static final String STATISTICS_PARTS_TYPE = "PARTS_TYPE";
	/**与工厂距离*/
	public static final String STATISTICS_DISTANCE = "DISTANCE";
	/**业务类型*/
	public static final String STATISTICS_BUSSINESS_TYPE = "BUSSINESS_TYPE";
	/**供应商分类 （A、B、C）*/
	public static final String STATISTICS_CLASSIFY2 = "CLASSIFY2";
	/**系统   物料一级分类   (动力，底盘，电气，车身……)*/
	public static final String STATISTICS_SYSTEM = "SYSTEM";
	
	


}
