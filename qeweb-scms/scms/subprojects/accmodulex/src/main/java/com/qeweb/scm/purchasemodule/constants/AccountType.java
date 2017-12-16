package com.qeweb.scm.purchasemodule.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 物料结算类型
 */
public class AccountType {

	public static final Integer ACCOUNT_TYPE_IN = 0;  		//0:入库结算
	public static final String ACCOUNT_TYPE_IN_STR = "入库结算";  		 
	public static final Integer ACCOUNT_TYPE_ONLINE = 1;	//1：上线结算
	public static final String ACCOUNT_TYPE_ONLINE_STR = "上线结算";
	
	private static Map<Integer, String> accountTypeMap;
	
	public static Map<Integer, String> getAccountTypeMap(){
		if(accountTypeMap==null){
			accountTypeMap = new HashMap<Integer, String>();
			accountTypeMap.put(ACCOUNT_TYPE_IN, ACCOUNT_TYPE_IN_STR);
			accountTypeMap.put(ACCOUNT_TYPE_ONLINE, ACCOUNT_TYPE_ONLINE_STR);
		}
		return accountTypeMap;
	}
	
	public static String getAccountTypeStr(Integer key) {
		return getAccountTypeMap().get(key);
	}
	
	public static Integer getAccountType(String value) {
		if(StringUtils.isEmpty(value))
			return null;
		
		Map<Integer, String> map = getAccountTypeMap();
		for(Map.Entry<Integer, String> entry : map.entrySet()) {
			if(entry.getValue().equals(value))
				return entry.getKey();
		}
		return null;
	}
	
}
