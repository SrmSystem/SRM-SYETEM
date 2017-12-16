package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_receive_item")
public class ReceiveItemEntity extends BaseEntity {

	private ReceiveEntity receive;
	private DeliveryItemEntity deliveryItem;
	private PurchaseOrderItemEntity orderItem;
	private PurchaseOrderItemPlanEntity orderItemPlan;
	private Double receiveQty;
	private Double inStoreQty;
	private Double returnQty;
	private String attr1;
	private String attr2;					
	private String attr3;					
	private String attr4;					
	private String attr5;							
	private String attr6;						
	private String attr7;					
	private String attr8;					
	private String attr9;									
	private String attr10;					
	private String attr11;					
	private String attr12;					
	private String attr13;					
	private String attr14;					
	private String attr15;					
	private String attr16;					
	private String attr17;					
	private String attr18;					
	private String attr19;					
	private String attr20;					
	private String attr21;					
	private String attr22;					
	private String attr23;
	private String attr24;
	private String attr25;
	private String attr26;					
	private String attr27;					
	private String attr28;					
	private String attr29;				
	private String attr30;                 
	
	//add by zhangjiejun 2015.10.25 start
	private Integer publishStatus;			// 发布状态
	private UserEntity publishUser;			// 发布人
	private Timestamp publishTime;			// 发布时间
	private Integer confirmStatus;			// 确认状态
	private UserEntity confirmUser;			// 确认人
	private Timestamp confirmTime;			// 确认时间
	//add by zhangjiejun 2015.10.25 end
	
	//add by zhangjiejun 2015.09.18 start
	private int voucher;					//队列号
	
	private Integer itemNo; 				//行号
	
	//add by chao.gu 20170710
	private int balanceStatus;//对账标识
	//add end
	
	private Timestamp balanceTime;
	
	private String dn;   //DN号
	
	private Double replOnwayQty;			// 补货在途数量
	private Double replReceiveQty;			// 补货收货数量
	private Double replUndeliveryQty;		// 补货未发数量 (orderQty-deliveryQty-toDeliveryQty)
	private Integer replDeliveryStatus;	    // 补货发货状态  未发货  部分发货  已发货
	
	private String orderCode;
	
	private Long viewNoCheckItemId; //未对账明细Id
	
	@Column(name="VIEW_NO_CHECK_ITEM_ID")
	public Long getViewNoCheckItemId() {
		return viewNoCheckItemId;
	}

	public void setViewNoCheckItemId(Long viewNoCheckItemId) {
		this.viewNoCheckItemId = viewNoCheckItemId;
	}
	
	private Double BuyerCheckPrice;
	
	
	@Transient
	public Double getBuyerCheckPrice() {
		return BuyerCheckPrice;
	}

	public void setBuyerCheckPrice(Double buyerCheckPrice) {
		BuyerCheckPrice = buyerCheckPrice;
	}

	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getVoucher() {
		return voucher;
	}

	public void setVoucher(int voucher) {
		this.voucher = voucher;
	}
	//add by zhangjiejun 2015.09.18 end
	

	//add by qinhao 2017.07.05 start
	public Double  zdjsl;   //AAC待检数量
	public Double  zzjbl;   //AAC质检不良数量
	public Double  zsjhg;   //AAC送检合格数量
	public Double  zllbl;   //AAC来料不良数量
	
	
	//非持久字段
	public String  badRate ;   //不良率(质检不良数量+来料不良数量)/发货数量
	//add by qinhao 2017.07.05 end
	
	
	
	//非持久化
	private long deliveryItemId;
	private String deliveryCode;
	private Timestamp deliveryCreateTime;		//发货单创建时间
	private String materialCode;			//零件号
	private String materialName;			//零件号
	private String materialDescribe;			//描述
	private String unitName;				//单位
	private String packageNum;				//标包数
	private String supportNum;				//托数
	private Double deliveryQty;				//发货数量
	private String devl;					//devl #(供应商发货单号)
	private Timestamp deliveyTime;			//发货时间(提货时间)
	private String supplierLot;				//产品批注号
	private String reqEtaTime;				//Require ETA port date
	private String deliveryCol7;			//发货单的供应商批次号
	private String rquestTime;			//要求到货时间
	private Double orderQty;			//需求数量
	private Double containerQty;		//容器数量
	private Double diffQty;           //送货差异数量{ASN数量-AAC实际收货数量（AAC待检数量+质量不良数量+送检合格数量+来料不良数量）
	private Integer shipType;          //补货状态 1：正常   -1补货
	
	public Double getZdjsl() {
		return zdjsl;
	}

	public void setZdjsl(Double zdjsl) {
		this.zdjsl = zdjsl;
	}

	public Double getZzjbl() {
		return zzjbl;
	}

	public void setZzjbl(Double zzjbl) {
		this.zzjbl = zzjbl;
	}

	public Double getZsjhg() {
		return zsjhg;
	}

	public void setZsjhg(Double zsjhg) {
		this.zsjhg = zsjhg;
	}

	public Double getZllbl() {
		return zllbl;
	}

	public void setZllbl(Double zllbl) {
		this.zllbl = zllbl;
	}

	@ManyToOne
	@JoinColumn(name="receive_id")
	public ReceiveEntity getReceive() {
		return receive;
	}

	public void setReceive(ReceiveEntity receive) {
		this.receive = receive;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="delivery_item_id")
	@JsonIgnore
	public DeliveryItemEntity getDeliveryItem() {
		return deliveryItem;
	}

	public void setDeliveryItem(DeliveryItemEntity deliveryItem) {
		this.deliveryItem = deliveryItem;
	}

	@ManyToOne
	@JoinColumn(name="order_item_id")
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_item_plan_id")
	@JsonIgnore
	public PurchaseOrderItemPlanEntity getOrderItemPlan() {
		return orderItemPlan;
	}

	public void setOrderItemPlan(PurchaseOrderItemPlanEntity orderItemPlan) {
		this.orderItemPlan = orderItemPlan;
	}

	@Column(name="receive_qty")
	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	@Column(name="in_store_qty")
	public Double getInStoreQty() {
		return inStoreQty;
	}

	public void setInStoreQty(Double inStoreQty) {
		this.inStoreQty = inStoreQty;
	}

	@Column(name="return_qty")
	public Double getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Double returnQty) {
		this.returnQty = returnQty;
	}

	@Transient
	public long getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(long deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	@Column(name="attr1")
	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	@Column(name="attr2")
	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	@Column(name="attr3")
	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	@Column(name="attr4")
	public String getAttr4() {
		return attr4;
	}

	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	@Column(name="attr5")
	public String getAttr5() {
		return attr5;
	}

	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	@Column(name="attr6")
	public String getAttr6() {
		return attr6;
	}

	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}
	@Column(name="attr7")
	public String getAttr7() {
		return attr7;
	}

	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}
	@Column(name="attr8")
	public String getAttr8() {
		return attr8;
	}

	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}
	@Column(name="attr9")
	public String getAttr9() {
		return attr9;
	}

	public void setAttr9(String attr9) {
		this.attr9 = attr9;
	}
	@Column(name="attr10")
	public String getAttr10() {
		return attr10;
	}

	public void setAttr10(String attr10) {
		this.attr10 = attr10;
	}
	@Column(name="attr11")
	public String getAttr11() {
		return attr11;
	}

	public void setAttr11(String attr11) {
		this.attr11 = attr11;
	}
	@Column(name="attr12")
	public String getAttr12() {
		return attr12;
	}

	public void setAttr12(String attr12) {
		this.attr12 = attr12;
	}
	@Column(name="attr13")
	public String getAttr13() {
		return attr13;
	}

	public void setAttr13(String attr13) {
		this.attr13 = attr13;
	}
	@Column(name="attr14")
	public String getAttr14() {
		return attr14;
	}

	public void setAttr14(String attr14) {
		this.attr14 = attr14;
	}
	@Column(name="attr15")
	public String getAttr15() {
		return attr15;
	}

	public void setAttr15(String attr15) {
		this.attr15 = attr15;
	}
	@Column(name="attr16")
	public String getAttr16() {
		return attr16;
	}

	public void setAttr16(String attr16) {
		this.attr16 = attr16;
	}
	@Column(name="attr17")
	public String getAttr17() {
		return attr17;
	}

	public void setAttr17(String attr17) {
		this.attr17 = attr17;
	}
	@Column(name="attr18")
	public String getAttr18() {
		return attr18;
	}

	public void setAttr18(String attr18) {
		this.attr18 = attr18;
	}
	@Column(name="attr19")
	public String getAttr19() {
		return attr19;
	}

	public void setAttr19(String attr19) {
		this.attr19 = attr19;
	}
	@Column(name="attr20")
	public String getAttr20() {
		return attr20;
	}

	public void setAttr20(String attr20) {
		this.attr20 = attr20;
	}
	@Column(name="attr21")
	public String getAttr21() {
		return attr21;
	}

	public void setAttr21(String attr21) {
		this.attr21 = attr21;
	}
	@Column(name="attr22")
	public String getAttr22() {
		return attr22;
	}

	public void setAttr22(String attr22) {
		this.attr22 = attr22;
	}
	@Column(name="attr23")
	public String getAttr23() {
		return attr23;
	}

	public void setAttr23(String attr23) {
		this.attr23 = attr23;
	}
	@Column(name="attr24")
	public String getAttr24() {
		return attr24;
	}

	public void setAttr24(String attr24) {
		this.attr24 = attr24;
	}
	@Column(name="attr25")
	public String getAttr25() {
		return attr25;
	}

	public void setAttr25(String attr25) {
		this.attr25 = attr25;
	}
	@Column(name="attr26")
	public String getAttr26() {
		return attr26;
	}

	public void setAttr26(String attr26) {
		this.attr26 = attr26;
	}
	@Column(name="attr27")
	public String getAttr27() {
		return attr27;
	}

	public void setAttr27(String attr27) {
		this.attr27 = attr27;
	}
	@Column(name="attr28")
	public String getAttr28() {
		return attr28;
	}

	public void setAttr28(String attr28) {
		this.attr28 = attr28;
	}
	@Column(name="attr29")
	public String getAttr29() {
		return attr29;
	}

	public void setAttr29(String attr29) {
		this.attr29 = attr29;
	}
	@Column(name="attr30")
	public String getAttr30() {
		return attr30;
	}

	public void setAttr30(String attr30) {
		this.attr30 = attr30;
	}

	@Transient
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	@Transient
	public String getBadRate() {
		return badRate;
	}

	public void setBadRate(String badRate) {
		this.badRate = badRate;
	}
	
	

	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getDeliveryCreateTime() {
		return deliveryCreateTime;
	}
	
	public void setDeliveryCreateTime(Timestamp deliveryCreateTime) {
		this.deliveryCreateTime = deliveryCreateTime;
	}

	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Transient
	public String getMaterialName() {
		return materialName;
	}
	
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	
	@Transient
	public String getMaterialDescribe() {
		return materialDescribe;
	}

	public void setMaterialDescribe(String materialDescribe) {
		this.materialDescribe = materialDescribe;
	}

	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Transient
	public String getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(String packageNum) {
		this.packageNum = packageNum;
	}

	@Transient
	public String getSupportNum() {
		return supportNum;
	}

	public void setSupportNum(String supportNum) {
		this.supportNum = supportNum;
	}

	@Transient
	public Double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	@Transient
	public String getDevl() {
		return devl;
	}

	public void setDevl(String devl) {
		this.devl = devl;
	}

	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getDeliveyTime() {
		return deliveyTime;
	}

	public void setDeliveyTime(Timestamp deliveyTime) {
		this.deliveyTime = deliveyTime;
	}

	@Transient
	public String getSupplierLot() {
		return supplierLot;
	}

	public void setSupplierLot(String supplierLot) {
		this.supplierLot = supplierLot;
	}

	@Transient
	public String getReqEtaTime() {
		return reqEtaTime;
	}

	public void setReqEtaTime(String reqEtaTime) {
		this.reqEtaTime = reqEtaTime;
	}

	@Transient
	public String getDeliveryCol7() {
		return deliveryCol7;
	}

	public void setDeliveryCol7(String deliveryCol7) {
		this.deliveryCol7 = deliveryCol7;
	}
	
	@Transient
	public String getRquestTime() {
		return rquestTime;
	}

	public void setRquestTime(String rquestTime) {
		this.rquestTime = rquestTime;
	}
	
	@Transient
	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	
	@Transient
	public Double getContainerQty() {
		return containerQty;
	}

	public void setContainerQty(Double containerQty) {
		this.containerQty = containerQty;
	}

	@Column(name="publish_status")
	public Integer getPublishStatus() {
		return publishStatus;
	}

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	public int getBalanceStatus() {
		return balanceStatus;
	}

	public void setBalanceStatus(int balanceStatus) {
		this.balanceStatus = balanceStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getBalanceTime() {
		return balanceTime;
	}

	public void setBalanceTime(Timestamp balanceTime) {
		this.balanceTime = balanceTime;
	}

	@Transient
	public Double getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(Double diffQty) {
		this.diffQty = diffQty;
	}

	@Transient
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public Double getReplOnwayQty() {
		return replOnwayQty;
	}

	public void setReplOnwayQty(Double replOnwayQty) {
		this.replOnwayQty = replOnwayQty;
	}

	public Double getReplReceiveQty() {
		return replReceiveQty;
	}

	public void setReplReceiveQty(Double replReceiveQty) {
		this.replReceiveQty = replReceiveQty;
	}

	public Double getReplUndeliveryQty() {
		return replUndeliveryQty;
	}

	public void setReplUndeliveryQty(Double replUndeliveryQty) {
		this.replUndeliveryQty = replUndeliveryQty;
	}

	public Integer getReplDeliveryStatus() {
		return replDeliveryStatus;
	}

	public void setReplDeliveryStatus(Integer replDeliveryStatus) {
		this.replDeliveryStatus = replDeliveryStatus;
	}

	@Transient
	public Integer getShipType() {
		return shipType;
	}

	public void setShipType(Integer shipType) {
		this.shipType = shipType;
	}
	
	
}
