package com.qeweb.scm.vendormodule.constants;

import java.util.HashMap;
import java.util.Map;

import org.springside.modules.mapper.JsonMapper;

import com.qeweb.scm.basemodule.constants.TypeConstant;

/**
 * 供应商的模块类型
 * @author pjjxiajun
 * @date 2015年6月24日
 * @path com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant.java
 */
public class VendorModuleTypeConstant extends TypeConstant{
	
	public final static int VENDOR_PROPERTY_1 = 1;//国有企业
	public final static int VENDOR_PROPERTY_2 = 2;//国有控股企业
	public final static int VENDOR_PROPERTY_3 = 3;//外资企业
	public final static int VENDOR_PROPERTY_4 = 4;//合资企业
	public final static int VENDOR_PROPERTY_5 = 5;//私营企业
	
	/** 禁用 */
	public final static int VENDOR_CHANGETYPE_0 = 0;//禁用
	/** 启用 */
	public final static int VENDOR_CHANGETYPE_1 = 1;//启用
	/** 晋级 */
	public final static int VENDOR_CHANGETYPE_2 = 2;//晋级
	/** 降级 */
	public final static int VENDOR_CHANGETYPE_3 = 3;//降级
	
	public static Map<String,Object> vendorChangeTypeMap;//供应商变更类型
	
	public static Map<String, Object> getVendorChangeTypeMap() {
		if(vendorChangeTypeMap==null){
			vendorChangeTypeMap = new HashMap<String, Object>();
			vendorChangeTypeMap.put(VENDOR_CHANGETYPE_0+"", "禁用");
			vendorChangeTypeMap.put(VENDOR_CHANGETYPE_1+"", "启用");
			vendorChangeTypeMap.put(VENDOR_CHANGETYPE_2+"", "晋级");
			vendorChangeTypeMap.put(VENDOR_CHANGETYPE_3+"", "降级");
		}
		return vendorChangeTypeMap;
	}
	public static String getVendorChangeTypeJson() {
		return mapper.toJson(getVendorChangeTypeMap());
		
	}
	
	
	
	
	public static Map<String,Object> vendorPropertyMap;//供应商企业性质

    
	public static Map<String, Object> getVendorPropertyMap() {
		if(vendorPropertyMap==null){
			vendorPropertyMap = new HashMap<String, Object>();
			vendorPropertyMap.put(VENDOR_PROPERTY_1+"", "国有企业");
			vendorPropertyMap.put(VENDOR_PROPERTY_2+"", "国有控股企业");
			vendorPropertyMap.put(VENDOR_PROPERTY_3+"", "外资企业");
			vendorPropertyMap.put(VENDOR_PROPERTY_4+"", "合资企业");
			vendorPropertyMap.put(VENDOR_PROPERTY_5+"", "私营企业");
		}
		return vendorPropertyMap;
	}
	public static String getVendorPropertyJson() {
		return mapper.toJson(getVendorPropertyMap());
		
	}
	
	/* 调查表模版类型  */
	/** XML */
	public final static int SUR_TEM_TYPE_XML = 0;
	/** bean */
	public final static int SUR_TEM_TYPE_BEAN = 1;
	private static Map<String,Object> surveyTemplateTypeMap;
	public static Map<String,Object> getSurveyTemplateTypeMap(){
		if(surveyTemplateTypeMap==null){
			surveyTemplateTypeMap = new HashMap<String, Object>();
			surveyTemplateTypeMap.put(SUR_TEM_TYPE_XML+"", "XML");
			surveyTemplateTypeMap.put(SUR_TEM_TYPE_BEAN+"", "bean");
		}
		return surveyTemplateTypeMap;
	}
	public static String getSurveyTemplateTypeJson(){
		return mapper.toJson(getSurveyTemplateTypeMap());
	}
	
	/* 向导模版的类型 */
	/** 默认 */
	public final static int NAV_TEM_TYPE_DEFALUT = 1;
	/** 物料 */
	public final static int NAV_TEM_TYPE_MATERIAL = 2;
	/** 物料分类 */
	public final static int NAV_TEM_TYPE_MATERIALTYPE = 3;
	/** 供应商 */
	public final static int NAV_TEM_TYPE_VENDOR = 4;
	
	private static Map<String,Object> navTemRangeTypeMap;


	public static Map<String,Object> getNavTemRangeTypeMap(){
		if(navTemRangeTypeMap==null){
			navTemRangeTypeMap = new HashMap<String, Object>();
			navTemRangeTypeMap.put(NAV_TEM_TYPE_DEFALUT+"", "默认");
			navTemRangeTypeMap.put(NAV_TEM_TYPE_MATERIAL+"", "物料");
			navTemRangeTypeMap.put(NAV_TEM_TYPE_MATERIALTYPE+"", "物料分类");
			navTemRangeTypeMap.put(NAV_TEM_TYPE_VENDOR+"", "供应商");
		}
		return navTemRangeTypeMap;
	}
	public static String getNavTemRangeTypeJson(){
		return mapper.toJson(getNavTemRangeTypeMap());
	}
	

	
	
	

	
}
