package com.qeweb.scm.baseline.common.entity;

public class WarnConstant {
	
	//类型
	public static String INFO= "INFO";//消息
	
	public static String WARN = "WARN";//预警
	
	public static String PROMOTIN_WARN = "PROMOTIN_WARN";//晋级预警
	
	
	//预测计划 --- 采方
	
	public static String ORDER_PLAN_UN_PUBLISH = "PLAN-10110";//预测计划待发布提醒
	
	public static String ORDER_PLAN_CONFIRM = "PLAN-10120";//预测计划已确认提醒
	
	public static String ORDER_PLAN_REJECT = "PLAN-10130";//预测计划驳回待处理提醒
	
	//预测计划 --- 供方
	public static String ORDER_PLAN_UN_CONFIRM = "PLAN-10140";//预测计划待确认提醒
	
	public static String ORDER_PLAN_VETO = "PLAN-10150";//预测计划拒绝驳回待确认提醒
	
	public static String ORDER_PLAN_UN_VETO = "PLAN-10160";//预测计划同意驳回提醒
	
	
	//采购订单 -- 采方
	
	public static String ORDER_MODIFY = "ORDER-20110";//采购订单同步(修改)提醒
	
	public static String ORDER_CONFIRM = "ORDER-20120";//采购订单已确认提醒
	
	public static String ORDER_REJECT = "ORDER-20130";//采购订单驳回待处理提醒
	
	
	//采购订单 -- 供方
	
	public static String ORDER_UN_CONFIRM = "ORDER-20140";//采购订单待确认提醒
	
	public static String ORDER_VETO = "ORDER-20150";//采购订单拒绝驳回待确认提醒
	
	public static String ORDER_UN_VETO = "ORDER-20160";//采购订单同意驳回提醒
	
	
	//要货计划 -- 采方
	
	public static String GOODS_UN_PUBLISH = "GOODS-30110";//要货计划待发布提醒
	
	public static String GOODS_CONFIRM = "GOODS-30120";//要货计划满足提醒
	
	public static String GOODS_REJECT = "GOODS-30130";//要货计划不满足提醒

	//要货计划 -- 供方
	public static String GOODS_UN_CONFIRM ="GOODS-30140";//要货计划待确认提醒
	
	
	
	//采购收发货---采方
	public static String ASN_UN_APPROAL  = "ASN-40110";//ASN待审批提醒
	
	public static String ASN_NUMBER_CHANGE = "ASN-40120";//已审批ASN修改数量提醒
	
	public static String DN_UPDATE_FOR_PURCHASER = "DN-40130";//订单收货数据更新提醒
	
	
	//采购收发货---供方
	
	public static String ASN_APPROAL = "ASN-40140";//ASN审批确认提醒
	
	public static String DN_UPDATE_FOR_VENDOR = "DN-40150";//订单收货数据更新提醒
	
}
