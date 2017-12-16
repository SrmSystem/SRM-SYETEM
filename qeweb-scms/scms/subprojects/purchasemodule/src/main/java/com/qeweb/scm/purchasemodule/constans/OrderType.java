package com.qeweb.scm.purchasemodule.constans;

/**
 * 订单类型
 */
public class OrderType {
	/**
	 * 国内订单
	 */
	public static final int DOMESTIC = 1;
	/**
	 * 国外订单
	 */
	public static final int ABROAD = 2; 
	/**
	 * 外协订单
	 */
	public static final int OUTSIDE = 3; 
	/**
	 * 反向生成订单
	 */
	public static final int REVERSE = 4; 
	
	//add by ZhangJieJun 2015.09.10 start
	/**
	 * 通过外部传入的订单状态，返回订单类型(外部传入的订单状态指的就是DB的订单类型)
	 * @param 	orderType_outSide	外部传入的订单状态
	 * @return	订单类型
	 */
	public static int getOrderType(String orderType_outSide){
		orderType_outSide = orderType_outSide.toUpperCase(); 
		int orderType = 0;
		if(orderType_outSide.equals("D")){
			orderType = DOMESTIC;
		}else if(orderType_outSide.equals("A")){
			orderType = ABROAD;
		}if(orderType_outSide.equals("S")){
			orderType = OUTSIDE;
		}
		return orderType;
	}
	//add by ZhangJieJun 2015.09.10 end
}