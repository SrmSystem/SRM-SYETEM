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
	private String attr2;					//反馈状态
	private String attr3;					//单价
	private String attr4;					//警告（输出队列在工序中为负数）
	private String attr5;					//供应商批次号			
	private String attr6;					//备注	
	private String attr7;					//实际收货零件号	
	private String attr8;					//库位
	private String attr9;					//QAD收货单号				
	private String attr10;					//类型(0:退货, 1:换货, 默认0, 换货就是补货)
	private String attr11;					//负数收货状态(0:正常收货， 1:负数收货(国内)， 2:负数收货(国外)， 3:负数收货(外协), 默认0)
	private String attr12;					//是否计算PPM(0:否， 1:是, 默认1)
	private String attr13;					//版本号
	private String attr14;					//行号
	private String attr15;					//Unqualified number（不合格单号）
	private String attr16;					//attachment(上传附件名称)
	private String attr17;					//attachment(上传附件地址)
	private String attr18;					//实际合格数量
	private String attr19;					//实际不合格数量
	private String attr20;					//合格生产线
	private String attr21;					//不合格生产线
	private String attr22;					//工序
	private String attr23;					//批次号
	private String attr24;					//参考号
	private String attr25;					//实际收货时间
	private String attr26;					//scrapVoucher，废品事物专用voucher，和收货接口的分开
	private String attr27;					//废品事物专用反馈状态
	private String attr28;					//废品事物专用反馈日志
	private String attr29;					//反馈日志
	private String attr30;                 	//是否已对账
	
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
	
	public int getVoucher() {
		return voucher;
	}

	public void setVoucher(int voucher) {
		this.voucher = voucher;
	}
	//add by zhangjiejun 2015.09.18 end

	//非持久化
	private long deliveryItemId;
	private String deliveryCode;
	private String deliveryCreateTime;		//发货单创建时间
	private String materialCode;			//零件号
	private String materialName;			//零件号
	private String unitName;				//单位
	private String packageNum;				//标包数
	private String supportNum;				//托数
	private Double deliveryQty;				//发货数量
	private String devl;					//devl #(供应商发货单号)
	private Timestamp deliveyTime;			//发货时间(提货时间)
	private String supplierLot;				//产品批注号
	private String reqEtaTime;				//Require ETA port date
	private String deliveryCol7;			//发货单的供应商批次号
	private String rquestTime;				//要求到货时间
	private Double orderQty;				//需求数量
	private Double containerQty;			//容器数量
	
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
	public String getDeliveryCreateTime() {
		return deliveryCreateTime;
	}

	public void setDeliveryCreateTime(String deliveryCreateTime) {
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
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
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
	
	
}
