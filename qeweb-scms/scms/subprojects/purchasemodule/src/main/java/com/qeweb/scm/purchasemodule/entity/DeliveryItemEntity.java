package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

@Entity
@Table(name = "qeweb_delivery_item")
public class DeliveryItemEntity extends BaseEntity {

	private DeliveryEntity delivery;
	private Integer itemNo;
	private PurchaseOrderItemEntity orderItem;			//订单明细
	private PurchaseOrderItemPlanEntity orderItemPlan;	//订单计划
	private GoodsRequestItemPlanEntity requestItemPlan;	//要货计划
	private MaterialEntity material;
	private Double deliveryQty;							//发货数量/外协合格数量
	private Double unqualifiedQty;						//不合格数量			
	private Double receiveQty;							//收货数量
	private Integer receiveStatus;
	//非持久化字段
	private Double toreceiveQty;						//收货的数量
	private Double toreturnQty;							//验退数量
	private long orderItemPlanId;
	private String orderCode;							//采购订单号
	private Double orderQty;							//需求数量
	private String requestTime;							// 要求到货时间
	private Integer orderItemNo;						//订单行号
	private String requireEtaTime; 
	
	private String trueMaterialCode;					//实际收货零件号
	private String location;							//库位
	private String site;								//地址
	private String receiveTime;							//收货时间
	private String buyerName;							//采购员
	private String reqEtaTime;							//Require ETA port date
	private String quaProLine;							//产品线
	private String isReplenish;							//是否为补货
	private String process;								//工序

	private String col1;			//要货单备注
	private String col2;			//单位
	private String col3;			//标包数量
	private String col4;			//托数
	private String col5;			//备注
	private String col6;			//devl #(供应商发货单号)
	private String col7;			//供应商批次号
	private String col8;			//产品批注号
	private String col9;			//erp同步状态
	private String col10;			//炉号
	private String col11;			//容器数量

	//冗余字段
	private String requestCode;			//要货单号
	private String receiveItemAttr18;	//实际合格数量
	private String receiveItemAttr19;	//实际不合格数量
	private String waveCode;	//波次号（WMS工单）
	
	
	@ManyToOne
	@JoinColumn(name="delivery_id")
	public DeliveryEntity getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryEntity delivery) {
		this.delivery = delivery;
	}

	@Column(name="item_no")
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_item_id")
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_item_plan_id")
	public PurchaseOrderItemPlanEntity getOrderItemPlan() {
		return orderItemPlan;
	}

	public void setOrderItemPlan(PurchaseOrderItemPlanEntity orderItemPlan) {
		this.orderItemPlan = orderItemPlan;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="request_item_plan_id")
	public GoodsRequestItemPlanEntity getRequestItemPlan() {
		return requestItemPlan;
	}

	public void setRequestItemPlan(GoodsRequestItemPlanEntity requestItemPlan) {
		this.requestItemPlan = requestItemPlan;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="delivery_qty")
	public Double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	
	@Column(name="unqualified_qty")
	public Double getUnqualifiedQty() {
		return unqualifiedQty;
	}

	public void setUnqualifiedQty(Double unqualifiedQty) {
		this.unqualifiedQty = unqualifiedQty;
	}

	@Column(name="receive_qty")
	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Transient
	public long getOrderItemPlanId() {
		return orderItemPlanId;
	}

	public void setOrderItemPlanId(long orderItemPlanId) {
		this.orderItemPlanId = orderItemPlanId;
	}

	@Transient
	public Double getToreceiveQty() {
		return toreceiveQty;
	}

	public void setToreceiveQty(Double toreceiveQty) {
		this.toreceiveQty = toreceiveQty;
	}

	@Transient
	public Double getToreturnQty() {
		return toreturnQty;
	}

	public void setToreturnQty(Double toreturnQty) {
		this.toreturnQty = toreturnQty;
	}

	@Column(name="col1")
	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	@Column(name="col2")
	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	@Column(name="col3")
	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	@Column(name="col4")
	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	@Column(name="col5")
	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	@Column(name="col6")
	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}

	@Column(name="col7")
	public String getCol7() {
		return col7;
	}

	public void setCol7(String col7) {
		this.col7 = col7;
	}

	@Column(name="col8")
	public String getCol8() {
		return col8;
	}

	public void setCol8(String col8) {
		this.col8 = col8;
	}

	@Column(name="col9")
	public String getCol9() {
		return col9;
	}

	public void setCol9(String col9) {
		this.col9 = col9;
	}

	@Column(name="col10")
	public String getCol10() {
		return col10;
	}

	public void setCol10(String col10) {
		this.col10 = col10;
	}
	
	@Column(name="col11")
	public String getCol11() {
		return col11;
	}

	public void setCol11(String col11) {
		this.col11 = col11;
	}

	@Transient
	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Transient
	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	@Transient
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	@Transient
	public Integer getOrderItemNo() {
		return orderItemNo;
	}

	public void setOrderItemNo(Integer orderItemNo) {
		this.orderItemNo = orderItemNo;
	}

	@Transient
	public String getTrueMaterialCode() {
		return trueMaterialCode;
	}

	public void setTrueMaterialCode(String trueMaterialCode) {
		this.trueMaterialCode = trueMaterialCode;
	}

	@Transient
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Transient
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	@Transient
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Transient
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	@Transient
	public String getReqEtaTime() {
		return reqEtaTime;
	}

	public void setReqEtaTime(String reqEtaTime) {
		this.reqEtaTime = reqEtaTime;
	}

	
	
	@Transient
	public String getReceiveItemAttr18() {
		return receiveItemAttr18;
	}

	public void setReceiveItemAttr18(String receiveItemAttr18) {
		this.receiveItemAttr18 = receiveItemAttr18;
	}

	@Transient
	public String getReceiveItemAttr19() {
		return receiveItemAttr19;
	}

	public void setReceiveItemAttr19(String receiveItemAttr19) {
		this.receiveItemAttr19 = receiveItemAttr19;
	}

	@Transient
	public String getRequireEtaTime() {
		return requireEtaTime;
	}

	public void setRequireEtaTime(String requireEtaTime) {
		this.requireEtaTime = requireEtaTime;
	}

	@Transient
	public String getIsReplenish() {
		return isReplenish;
	}

	public void setIsReplenish(String isReplenish) {
		this.isReplenish = isReplenish;
	}
	
	@Transient
	public String getQuaProLine() {
		return quaProLine;
	}

	public void setQuaProLine(String quaProLine) {
		this.quaProLine = quaProLine;
	}
	
	@Transient
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@Transient
	public String getWaveCode() {
		return waveCode;
	}

	public void setWaveCode(String waveCode) {
		this.waveCode = waveCode;
	}
	
	
	
}
