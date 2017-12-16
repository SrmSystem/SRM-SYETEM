package com.qeweb.scm.basemodule.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qeweb.scm.basemodule.convert.EasyuiTree;

/**
 * 公司类型的常量类
 * @author pjjxiajun
 * @date 2015年3月13日
 * @path com.qeweb.scm.basemodule.constans.com.qeweb.scm.basemodule.constans
 */
public class OrgType {

	/** 公司 */
	public final static Integer ORG_TYPE_COMPANY = 0;
	/** 部门 */
	public final static Integer ORG_TYPE_DEPARMENT = 1;
	
	/** 采购商 */
	public final static Integer ROLE_TYPE_BUYER = 0;
	/** 供应商 */
	public final static Integer ROLE_TYPE_VENDOR = 1;
	/** 仓储商 */
	public final static Integer ROLE_TYPE_STORE = 2;
	
	/** 运输商 */
	public final static Integer ROLE_TYPE_TRANSPORT = 3;
	/** 采购商部门 */
	public final static Integer ROLE_TYPE_BUYER_DEPART = 4;
	
	private static Map<Integer,String> orgTypeMap;
	private static Map<Integer,String> roleTypeMap;

	public static Map<Integer,String> getOrgTypeMap(){
		if(orgTypeMap==null){
			orgTypeMap = new HashMap<Integer, String>();
			orgTypeMap.put(ORG_TYPE_COMPANY, "公司");
			orgTypeMap.put(ORG_TYPE_DEPARMENT, "部门");
		}
		return orgTypeMap;
	}
	
	public static Map<Integer,String> getRoleTypeMap(){
		if(roleTypeMap==null){
			roleTypeMap = new HashMap<Integer, String>();
			roleTypeMap.put(ROLE_TYPE_BUYER, "采购商");
			roleTypeMap.put(ROLE_TYPE_VENDOR, "供应商");
			roleTypeMap.put(ROLE_TYPE_STORE, "仓储商");
			roleTypeMap.put(ROLE_TYPE_TRANSPORT, "运输商");
		}
		return roleTypeMap;
	}
	
	public static String getOrgType(Integer key){
		return getOrgTypeMap().get(key);
	}
	
	public static String getRoleType(Integer key){
		return getRoleTypeMap().get(key);
	}
	
	public static List<EasyuiTree> orgTreeList = new ArrayList<EasyuiTree>();
}
