package com.qeweb.scm.purchasemodule.constants;

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
	public static final Integer CONFIRM_STATUS_REJECT_REJECT = -2;	//拒绝驳回
	
	/**
	 * 驳回(确认)状态
	 */
	public static final Integer VETO_STATUS_VETO = 0;
	public static final Integer VETO_STATUS_AGREE = 1;
	
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
	public static final Integer CHECK_BILL = 4;				//对账单主单

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
	
	/**
	 * 同步状态
	 */
	public static final Integer SYNC_STATUS = 0;//未同步
	public static final Integer SYNC_FAIL = -1;//同步失败
	public static final Integer SYNC_SUCCESS = 1;//同步成功
	
	public static final String PUBLISH="PUBLISH";
	public static final String CLOSE="CLOSE";
	public static final String CONFIRM="CONFIRM";
	
	
	//SESSION  创建发货单检验报告
	public static final String SESSION_INSPECTION_REPORT="SESSION_INSPECTION_REPORT";
	
	/**
	 * 对账状态
	 */
	public static final int BALANCE_STATUS_NO=0;//未对账
	public static final int BALANCE_STATUS_YES=1;//已对账
	
	/**
	 * 对账明细来源
	 */
	public static final int CHECK_ITEM_SOURCE_REV=1;//收货
	public static final int CHECK_ITEM_SOURCE_RET=-1;//退货
	
	/**
	 * 异常状态：判断锁定状态、冻结状态、删除状态、交付状态
	 */
	public static final Integer EXCEPTION_STATUS_NO=0;//无异常
	public static final Integer EXCEPTION_STATUS_YES=1;//有异常
	
	/**
	 * 发货类型
	 */
	public static final int SHIP_TYPE_NORMAL=1;//普通发货
	public static final int SHIP_TYPE_REPL=-1;//补货发货
	
	/**
	 * 发货类型名称
	 */
	public static final String SHIP_TYPE_NORMAL_NAME="正常";
	public static final String SHIP_TYPE_REPL_NAME="补货";
	
	/**
	 * 供应商确认状态
	 */
	public static final int  VENDOR_CONFIRM_STATUS_BUY_REJECT=-2;//驳回拒绝
	public static final int  VENDOR_CONFIRM_STATUS_PART_CONFIRM=2;//部分确认
	public static final int  VENDOR_CONFIRM_STATUS_VENDOR_REJECT=-1;//驳回
	public static final int  VENDOR_CONFIRM_STATUS_CONFIRM=1;//已确认
	public static final int  VENDOR_CONFIRM_STATUS_WAIT=0;//待确认
}
