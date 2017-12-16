package com.qeweb.scm.purchasemodule.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 物料结算类型
 */
public class PaymentCycleType {

	public static final Integer TYPE_I = 1;  		//1:30天付款
	public static final Integer TYPE_II = 2;  		//2:60天付款
	public static final Integer TYPE_III = 3;  		//3:90天付款
	public static final Integer TYPE_IIII = 4;  	//4:30天以内付款
	
	public static final String TYPE_I_STR = "30天付款";  		
	public static final String TYPE_II_STR = "60天付款";  		
	public static final String TYPE_III_STR = "90天付款";  		
	public static final String TYPE_IIII_STR = "30天以内付款";  		
	
	private static Map<Integer, String> PaymentCycleTypeMap;
	
	public static Map<Integer, String> getPaymentCycleTypeMap(){ 
		if(PaymentCycleTypeMap==null){
			PaymentCycleTypeMap = new HashMap<Integer, String>();
			PaymentCycleTypeMap.put(TYPE_I, TYPE_I_STR);
			PaymentCycleTypeMap.put(TYPE_II, TYPE_II_STR);
			PaymentCycleTypeMap.put(TYPE_III, TYPE_III_STR);
			PaymentCycleTypeMap.put(TYPE_IIII, TYPE_IIII_STR);
		}
		return PaymentCycleTypeMap;
	}
	
	public static String getPaymentCycleTypeStr(Integer key) {
		return getPaymentCycleTypeMap().get(key);
	}
	
	public static Integer getPaymentCycleType(String value) {
		if(StringUtils.isEmpty(value))
			return null;
		
		Map<Integer, String> map = getPaymentCycleTypeMap();
		for(Map.Entry<Integer, String> entry : map.entrySet()) {
			if(entry.getValue().equals(value))
				return entry.getKey();
		}
		return null;
	}
	
}
