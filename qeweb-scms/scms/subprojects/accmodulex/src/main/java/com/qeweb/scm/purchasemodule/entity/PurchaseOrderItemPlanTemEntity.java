package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 供货计划临时表（用于存储未发布的要货计划和采购订单的关系）
 * @author eleven
 *
 */

@Entity
@Table(name = "QEWEB_PURCHASE_ORDER_ITEMPLANT")
public class PurchaseOrderItemPlanTemEntity extends BaseEntity {

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
	
	private Long userId;  //创建人的id

	private  Long goodsRequestId; //要货计划的id
	private PurchaseGoodsRequestEntity purchaseGoodsRequest; //要货计划
	
	@ManyToOne
	@JoinColumn(name="order_id")
	public PurchaseOrderEntity getOrder() {
		return order;
	}
	public void setOrder(PurchaseOrderEntity order) {
		this.order = order;
	}
	@ManyToOne
	@JoinColumn(name="order_item_id")
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}
	@Column(name="version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name="item_no")
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="rec_org")
	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	@Column(name="order_qty")
	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	@Column(name="request_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	@Column(name="currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name="unit_name")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name="confirm_status")
	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	@ManyToOne
	@JoinColumn(name="confirm_user_id")
	public UserEntity getConfirmUser() {
		return confirmUser;
	}

	public void setConfirmUser(UserEntity confirmUser) {
		this.confirmUser = confirmUser;
	}

	@Column(name="confirm_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	@Column(name="delivery_status")
	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Column(name="order_status")
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Column(name="delivery_qty")
	public Double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	@Column(name="to_delivery_qty")
	public Double getToDeliveryQty() {
		return toDeliveryQty;
	}

	public void setToDeliveryQty(Double toDeliveryQty) {
		this.toDeliveryQty = toDeliveryQty;
	}

	@Column(name="receive_qty")
	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	@Column(name="return_qty")
	public Double getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Double returnQty) {
		this.returnQty = returnQty;
	}

	@Column(name="diff_qty")
	public Double getDiffQty() {
		return diffQty;
	}
	
	public void setDiffQty(Double diffQty) {
		this.diffQty = diffQty;
	}
	
	@Column(name="onway_qty")
	public Double getOnwayQty() {
		return onwayQty;
	}

	public void setOnwayQty(Double onwayQty) {
		this.onwayQty = onwayQty;
	}

	@Column(name="undelivery_qty")
	public Double getUndeliveryQty() {
		return undeliveryQty;
	}

	public void setUndeliveryQty(Double undeliveryQty) {
		this.undeliveryQty = undeliveryQty;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	@Column(name="publish_status")
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	@ManyToOne
	@JoinColumn(name="publish_user_id")
	public UserEntity getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(UserEntity publishUser) {
		this.publishUser = publishUser;
	}

	@Column(name="publish_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public Long getGoodsRequestId() {
		return goodsRequestId;
	}

	public void setGoodsRequestId(Long goodsRequestId) {
		this.goodsRequestId = goodsRequestId;
	}


	@ManyToOne
	@JoinColumn(name="goodsRequestId",insertable=false,updatable=false)
	public PurchaseGoodsRequestEntity getPurchaseGoodsRequest() {
		return purchaseGoodsRequest;
	}
	public void setPurchaseGoodsRequest(
			PurchaseGoodsRequestEntity purchaseGoodsRequest) {
		this.purchaseGoodsRequest = purchaseGoodsRequest;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    

}
