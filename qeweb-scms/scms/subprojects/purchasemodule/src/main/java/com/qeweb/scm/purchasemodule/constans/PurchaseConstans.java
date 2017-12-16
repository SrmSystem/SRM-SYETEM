package com.qeweb.scm.purchasemodule.constans;

/**
 * 采购模块常量
 * @author ALEX
 *
 */
public class PurchaseConstans {
	
	/**
	 * 发布状态
	 */
	public static final Integer PUBLISH_STATUS_NO = 0;
	public static final Integer PUBLISH_STATUS_YES = 1;
	public static final Integer PUBLISH_STATUS_PART = 2;
	
	/**
	 * 查看状态
	 */
	public static final Integer SEE_STATUS_NO = 0;
	public static final Integer SEE_STATUS_YES = 1;
	
	/**
	 * 确认状态
	 */
	public static final Integer CONFIRM_STATUS_NO = 0;
	public static final Integer CONFIRM_STATUS_YES = 1;
	public static final Integer CONFIRM_STATUS_PART = 2;
	public static final Integer CONFIRM_STATUS_REJECT = -1;
	
	/**
	 * 发货状态
	 */
	public static final Integer DELIVERY_STATUS_NO = 0;
	public static final Integer DELIVERY_STATUS_YES = 1;
	public static final Integer DELIVERY_STATUS_PART = 2;
	public static final Integer DELIVERY_STATUS_SAVE = 3;
	
	/**
	 * 收货状态
	 */
	public static final Integer RECEIVE_STATUS_NO = 0;
	public static final Integer RECEIVE_STATUS_YES = 1;
	public static final Integer RECEIVE_STATUS_PART = 2;
	public static final Integer RECEIVE_STATUS_REJECT = -1;  
	
	/**
	 * 关闭状态
	 */
	public static final Integer CLOSE_STATUS_NO = 0;
	public static final Integer CLOSE_STATUS_YES = 1;
	public static final Integer CLOSE_STATUS_PART = 2;
	
	/**
	 * 开票状态
	 */
	public static final Integer INVOICE_MAKE_OUT_NO = 0;
	public static final Integer INVOICE_MAKE_OUT_YES = 1;
	
	/**
	 * common状态
	 */
	public static final String COMMON_SAVE = "0";
	public static final String COMMON_SAVE_PUBLIC = "1";
	
	public static final Integer COMMON_NORMAL = 0;		//正常
	public static final Integer COMMON_INVALID = 1;		//失效
	
	
	/**
	 * 单据类型
	 */
	public static final Integer PURCHASE_ORDER = 1;				//订单主表
	public static final Integer PURCHASE_ORDER_ITEM =2;			//订单明细
	public static final Integer PURCHASE_ORDER_ITEM_PLAN =3;	//订单明细供货计划
	
	/**
	 * 审核状态
	 */
	public static final Integer AUDIT_NO = 0;
	public static final Integer AUDIT_YES = 1;
	public static final Integer AUDIT_REFUSE = -1;
	public static final Integer AUDIT_CHECKING = -2;
	
	/**
	 * 提货状态
	 */
	public static final Integer TAKE_NO = 0;
	public static final Integer TAKE_YES = 1;
	
	/**
	 * 回复状态
	 */
	public static final Integer REPLY_STATUS_NO = 0;
	public static final Integer REPLY_STATUS_YES = 1;	
	public static final Integer REPLY_STATUS_PART = 2;
	
	public static final Integer REPLY_STATUS_RECEIVE = 1;		//接收
	public static final Integer REPLY_STATUS_REJECT = -1;		//驳回
	
	
	/**
	 * 提交状态
	 */
	public static final Integer SUBMIT_STATUS_NO = 0;
	public static final Integer SUBMIT_STATUS_YES = 1;
	
	/**
	 * 过期状态
	 */
	public static final Integer OVERDUE_STATUS_NO = 0;
	public static final Integer OVERDUE_STATUS_YES = 1;
	
	/**
	 * 考核状态
	 */
	public static final Integer RECORD_NO=0;
	public static final Integer RECORD_YES=1;
	
	/**
	 * 超时状态
	 */
	public static final Integer TIMEOUT_STATUS_NO = 0;
	public static final Integer TIMEOUT_STATUS_YES = 1;
	
	/**
	 * 库存状态
	 */
	public static final Integer STOCK_STATUS_NO = 0;
	public static final Integer STOCK_STATUS_YES = 1;
	
	/**
	 * 差异状态
	 */
	public static final Integer DIFFERENCE_STATUS_NO = 0;
	public static final Integer DIFFERENCE_STATUS_YES = 1;
	
	public static final Integer EXCEPTION_STATUS_YES=1;
	public static final Integer EXCEPTION_STATUS_NO=0;
}
