package com.qeweb.scm.purchasemodule.web.goodsrequest.vo;

import java.sql.Timestamp;

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;

public class PurchaseOrderItemPlanVo {

	private Long orderItemPlanId; //供货计划的id
	private Long orderItemPlanTemId; //临时表的id
	private PurchaseOrderEntity order;			// 订单主信息
	private PurchaseOrderItemEntity orderItem;	//订单明细
	private String version;						// 版本号
	private Integer itemNo;						// 行号
	private MaterialEntity material;			// 物料
	private String receiveOrg;					// 收货方
	private Double orderQty;					// 订购数量
	private Timestamp requestTime;				// 要求到货时间
	private String currency;					// 币种
	private String unitName;					// 单位
	private Integer confirmStatus;				// 确认状态
	private UserEntity confirmUser;				// 确认人
	private Timestamp confirmTime;				// 确认时间
	private Integer deliveryStatus;				// 发货状态
	private Integer receiveStatus;				// 收货状态
	private Integer orderStatus;				// 订单状态
	private Integer publishStatus;				// 发布状态
	private UserEntity publishUser;				// 发布人
	private Timestamp publishTime;				// 发布时间
	private Double deliveryQty;					// 已发数量
	private Double toDeliveryQty;				// 已创建未发数量
	private Double receiveQty;					// 实收数量
	private Double returnQty;					// 退货数量
	private Double diffQty;						// 差异数量
	private Double onwayQty;					// 在途数量
	private Double undeliveryQty;				// 未发数量
	private Integer isPurchaseItemPlan;    //是否是供货计划的数据
	private PurchaseGoodsRequestEntity purchaseGoodsRequest; //要货计划
	private PurchaseOrderItemPlanEntity purchaseItemPlan; //供货计划
	private String rejectReason;
	
	
	public Long getOrderItemPlanId() {
		return orderItemPlanId;
	}
	public void setOrderItemPlanId(Long orderItemPlanId) {
		this.orderItemPlanId = orderItemPlanId;
	}

	public Long getOrderItemPlanTemId() {
		return orderItemPlanTemId;
	}
	public void setOrderItemPlanTemId(Long orderItemPlanTemId) {
		this.orderItemPlanTemId = orderItemPlanTemId;
	}
	public PurchaseOrderEntity getOrder() {
		return order;
	}
	public void setOrder(PurchaseOrderEntity order) {
		this.order = order;
	}
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	public String getReceiveOrg() {
		return receiveOrg;
	}
	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}
	public Double getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Integer getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public UserEntity getConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(UserEntity confirmUser) {
		this.confirmUser = confirmUser;
	}
	public Timestamp getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}
	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public Integer getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public UserEntity getPublishUser() {
		return publishUser;
	}
	public void setPublishUser(UserEntity publishUser) {
		this.publishUser = publishUser;
	}
	public Timestamp getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}
	public Double getDeliveryQty() {
		return deliveryQty;
	}
	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	public Double getToDeliveryQty() {
		return toDeliveryQty;
	}
	public void setToDeliveryQty(Double toDeliveryQty) {
		this.toDeliveryQty = toDeliveryQty;
	}
	public Double getReceiveQty() {
		return receiveQty;
	}
	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	public Double getReturnQty() {
		return returnQty;
	}
	public void setReturnQty(Double returnQty) {
		this.returnQty = returnQty;
	}
	public Double getDiffQty() {
		return diffQty;
	}
	public void setDiffQty(Double diffQty) {
		this.diffQty = diffQty;
	}
	public Double getOnwayQty() {
		return onwayQty;
	}
	public void setOnwayQty(Double onwayQty) {
		this.onwayQty = onwayQty;
	}
	public Double getUndeliveryQty() {
		return undeliveryQty;
	}
	public void setUndeliveryQty(Double undeliveryQty) {
		this.undeliveryQty = undeliveryQty;
	}
	public PurchaseGoodsRequestEntity getPurchaseGoodsRequest() {
		return purchaseGoodsRequest;
	}
	public void setPurchaseGoodsRequest(
			PurchaseGoodsRequestEntity purchaseGoodsRequest) {
		this.purchaseGoodsRequest = purchaseGoodsRequest;
	}
	public Integer getIsPurchaseItemPlan() {
		return isPurchaseItemPlan;
	}
	public void setIsPurchaseItemPlan(Integer isPurchaseItemPlan) {
		this.isPurchaseItemPlan = isPurchaseItemPlan;
	}
	public PurchaseOrderItemPlanEntity getPurchaseItemPlan() {
		return purchaseItemPlan;
	}
	public void setPurchaseItemPlan(PurchaseOrderItemPlanEntity purchaseItemPlan) {
		this.purchaseItemPlan = purchaseItemPlan;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
}
