package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_purchase_order_item_plan")
public class PurchaseOrderItemPlanEntity extends BaseEntity {

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
	
	//add by zhangjiejun 2015.10.24 start
	private Integer publishStatus;				// 发布状态
	private UserEntity publishUser;				// 发布人
	private Timestamp publishTime;				// 发布时间
	//add by zhangjiejun 2015.10.24 end
	
	private Double deliveryQty;					// 已发数量
	private Double toDeliveryQty;				// 已创建未发数量
	private Double receiveQty;					// 实收数量
	private Double returnQty;					// 退货数量
	private Double diffQty;						// 差异数量
	private Double onwayQty;					// 在途数量
	private Double undeliveryQty;				// 未发数量 (orderQty-deliveryQty-toDeliveryQty)
	//非持久化字段
	private Double shouldQty;					// 应发数量 (orderQty-deliveryQty-toDeliveryQty)
	private Double sendQty;						// 发货数量/合格数量
	private Double unqualifiedQty;				// 不合格数量
	private String buyerCode;
	private String buyerName;
	private String vendorCode;
	private String vendorName;
	private Integer orderType;//订单类型
	
	//add by zhangjiejun 2015.09.25 start
	private String packageQty;					// 标包数量
	//add by zhangjiejun 2015.09.25 end
	
	private String rejectReason;//驳回原因（供应商）
	private UserEntity rejectUser;//驳回人（供应商）
	private Timestamp rejectTime;//驳回时间（供应商（
	
	private Integer vetoStatus;			// 驳回状态  同意  ，拒绝驳回
	private String vetoReason;//驳回原因（采购商的操作）
	private UserEntity vetoUser;//驳回人（采购商的操作）
	private Timestamp vetoTime;//驳回时间（采购商的操作）
	
	private Integer errorStatus; 	//异常状态
	private Long changeUserId; 		//导致异常的人员
	private Timestamp changeTime; 	//导致异常的时间
	
	
	//扩展字段
	private String col1;						
	private String col2;						
	private String col3;						
	private String col4;						
	private String col5;						
	private String col6;						
	private String col7;						
	private String col8;						
	private String col9;
	private String col10;
	private String col11;
	private String col12;
	private String col13;
	private String col14;
	private String col15;
	private String col16;
	private String col17;
	private String col18;
	private String col19;
	private String col20;
	private String col21;
	private String col22;
	private String col23;
	private String col24;
	private String col25;
	private String col26;
	private String col27;                      
	private String col28;              		  
	private String col29;					  										
	private String col30;                       
	
	private Double dcol1;						
	private Double dcol2;						
	private Double dcol3;
	private Double dcol4;
	private Double dcol5;						
	
	private Timestamp tcol1;					
	private Timestamp tcol2;					
	private Timestamp tcol3;					
	private Timestamp tcol4;
	private Timestamp tcol5;
	
	private  Long goodsRequestId; //要货计划的id
	private PurchaseGoodsRequestEntity purchaseGoodsRequest; //要货计划
	
	private Double baseQty;
	
	private int shipType;//发货类型【1普通的发货 -1补货】
	
	private Long recItemId;//收货明细ID
	
	private Long sourceOrderItemPlanId;//原供货计划，即普通发货单的供货计划
	private String dn;//DN号
	//非持久化
	private String orderCode;
	private String zlock;			//锁定标识
	private String bstae;           //内向交货标识
	private  Double standardBoxNum;//标准箱数
	private Double minPackageQty;//小包装箱数量
	private String charg;//批号
	private String lockStatus;	//冻结状态
	private String loekz;		//删除标识
	private String elikz;		//交付标识
	private String isRed;//标红数据
	private String isDelete;//是否可以删除
	 
	 
	 
	 @Transient
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
	@Transient
	public String getIsRed() {
		return isRed;
	}
	public void setIsRed(String isRed) {
		this.isRed = isRed;
	}
	

	public Double getBaseQty() {
		return baseQty;
	}

	public void setBaseQty(Double baseQty) {
		this.baseQty = baseQty;
	}

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
//	@JsonFormat(pattern = "yyyy-MM-dd HH",timezone="GMT+8")
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

	@Transient
	public Double getShouldQty() {
		return shouldQty;
	}

	public void setShouldQty(Double shouldQty) {
		this.shouldQty = shouldQty;
	}

	@Transient
	public Double getSendQty() {
		return sendQty;
	}

	public void setSendQty(Double sendQty) {
		this.sendQty = sendQty;
	}
	
	@Transient
	public Double getUnqualifiedQty() {
		return unqualifiedQty;
	}

	public void setUnqualifiedQty(Double unqualifiedQty) {
		this.unqualifiedQty = unqualifiedQty;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}

	public String getCol7() {
		return col7;
	}

	public void setCol7(String col7) {
		this.col7 = col7;
	}

	public String getCol8() {
		return col8;
	}

	public void setCol8(String col8) {
		this.col8 = col8;
	}

	public String getCol9() {
		return col9;
	}

	public void setCol9(String col9) {
		this.col9 = col9;
	}

	public String getCol10() {
		return col10;
	}

	public void setCol10(String col10) {
		this.col10 = col10;
	}

	public String getCol11() {
		return col11;
	}

	public void setCol11(String col11) {
		this.col11 = col11;
	}

	public String getCol12() {
		return col12;
	}

	public void setCol12(String col12) {
		this.col12 = col12;
	}

	public String getCol13() {
		return col13;
	}

	public void setCol13(String col13) {
		this.col13 = col13;
	}

	public String getCol14() {
		return col14;
	}

	public void setCol14(String col14) {
		this.col14 = col14;
	}

	public String getCol15() {
		return col15;
	}

	public void setCol15(String col15) {
		this.col15 = col15;
	}

	public String getCol16() {
		return col16;
	}

	public void setCol16(String col16) {
		this.col16 = col16;
	}

	public String getCol17() {
		return col17;
	}

	public void setCol17(String col17) {
		this.col17 = col17;
	}

	public String getCol18() {
		return col18;
	}

	public void setCol18(String col18) {
		this.col18 = col18;
	}

	public String getCol19() {
		return col19;
	}

	public void setCol19(String col19) {
		this.col19 = col19;
	}

	public String getCol20() {
		return col20;
	}

	public void setCol20(String col20) {
		this.col20 = col20;
	}

	public String getCol21() {
		return col21;
	}

	public void setCol21(String col21) {
		this.col21 = col21;
	}

	public String getCol22() {
		return col22;
	}

	public void setCol22(String col22) {
		this.col22 = col22;
	}

	public String getCol23() {
		return col23;
	}

	public void setCol23(String col23) {
		this.col23 = col23;
	}

	public String getCol24() {
		return col24;
	}

	public void setCol24(String col24) {
		this.col24 = col24;
	}

	public String getCol25() {
		return col25;
	}

	public void setCol25(String col25) {
		this.col25 = col25;
	}

	public String getCol26() {
		return col26;
	}

	public void setCol26(String col26) {
		this.col26 = col26;
	}

	public String getCol27() {
		return col27;
	}

	public void setCol27(String col27) {
		this.col27 = col27;
	}

	public String getCol28() {
		return col28;
	}

	public void setCol28(String col28) {
		this.col28 = col28;
	}

	public String getCol29() {
		return col29;
	}

	public void setCol29(String col29) {
		this.col29 = col29;
	}

	public String getCol30() {
		return col30;
	}

	public void setCol30(String col30) {
		this.col30 = col30;
	}

	public Double getDcol1() {
		return dcol1;
	}

	public void setDcol1(Double dcol1) {
		this.dcol1 = dcol1;
	}

	public Double getDcol2() {
		return dcol2;
	}

	public void setDcol2(Double dcol2) {
		this.dcol2 = dcol2;
	}

	public Double getDcol3() {
		return dcol3;
	}

	public void setDcol3(Double dcol3) {
		this.dcol3 = dcol3;
	}

	public Double getDcol4() {
		return dcol4;
	}

	public void setDcol4(Double dcol4) {
		this.dcol4 = dcol4;
	}

	public Double getDcol5() {
		return dcol5;
	}

	public void setDcol5(Double dcol5) {
		this.dcol5 = dcol5;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getTcol1() {
		return tcol1;
	}

	public void setTcol1(Timestamp tcol1) {
		this.tcol1 = tcol1;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getTcol2() {
		return tcol2;
	}

	public void setTcol2(Timestamp tcol2) {
		this.tcol2 = tcol2;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getTcol3() {
		return tcol3;
	}

	public void setTcol3(Timestamp tcol3) {
		this.tcol3 = tcol3;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getTcol4() {
		return tcol4;
	}

	public void setTcol4(Timestamp tcol4) {
		this.tcol4 = tcol4;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getTcol5() {
		return tcol5;
	}

	public void setTcol5(Timestamp tcol5) {
		this.tcol5 = tcol5;
	}

	@Transient
	public String getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(String packageQty) {
		this.packageQty = packageQty;
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

	@Transient
	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	@Transient
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	@Transient
	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	@Transient
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@Transient
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	@ManyToOne
	@JoinColumn(name="reject_user_id")
	public UserEntity getRejectUser() {
		return rejectUser;
	}
	
	public void setRejectUser(UserEntity rejectUser) {
		this.rejectUser = rejectUser;
	}

	public Timestamp getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Timestamp rejectTime) {
		this.rejectTime = rejectTime;
	}

	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Transient
	public String getZlock() {
		return zlock;
	}

	public void setZlock(String zlock) {
		this.zlock = zlock;
	}
	
	
	@Transient
	public String getCharg() {
		return charg;
	}

	public void setCharg(String charg) {
		this.charg = charg;
	}

	@Transient
	public Double getStandardBoxNum() {
		return standardBoxNum;
	}

	public void setStandardBoxNum(Double standardBoxNum) {
		this.standardBoxNum = standardBoxNum;
	}
	

	public Long getGoodsRequestId() {
		return goodsRequestId;
	}
	public void setGoodsRequestId(Long goodsRequestId) {
		this.goodsRequestId = goodsRequestId;
	}

	@Transient
	public Double getMinPackageQty() {
		return minPackageQty;
	}

	public void setMinPackageQty(Double minPackageQty) {
		this.minPackageQty = minPackageQty;
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

	public Integer getVetoStatus() {
		return vetoStatus;
	}

	public void setVetoStatus(Integer vetoStatus) {
		this.vetoStatus = vetoStatus;
	}

	public String getVetoReason() {
		return vetoReason;
	}

	public void setVetoReason(String vetoReason) {
		this.vetoReason = vetoReason;
	}

	@ManyToOne
	@JoinColumn(name="veto_user_id")
	public UserEntity getVetoUser() {
		return vetoUser;
	}

	public void setVetoUser(UserEntity vetoUser) {
		this.vetoUser = vetoUser;
	}

	public Timestamp getVetoTime() {
		return vetoTime;
	}

	public void setVetoTime(Timestamp vetoTime) {
		this.vetoTime = vetoTime;
	}

	@Column(name="error_status")
	public Integer getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(Integer errorStatus) {
		this.errorStatus = errorStatus;
	}

	@Column(name="change_user_id")
	public Long getChangeUserId() {
		return changeUserId;
	}

	public void setChangeUserId(Long changeUserId) {
		this.changeUserId = changeUserId;
	}

	@Column(name="change_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}

	@Transient
	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	@Transient
	public String getLoekz() {
		return loekz;
	}

	public void setLoekz(String loekz) {
		this.loekz = loekz;
	}

	@Transient
	public String getElikz() {
		return elikz;
	}

	public void setElikz(String elikz) {
		this.elikz = elikz;
	}
	
	public Long getRecItemId() {
		return recItemId;
	}
	public void setRecItemId(Long recItemId) {
		this.recItemId = recItemId;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public Long getSourceOrderItemPlanId() {
		return sourceOrderItemPlanId;
	}
	public void setSourceOrderItemPlanId(Long sourceOrderItemPlanId) {
		this.sourceOrderItemPlanId = sourceOrderItemPlanId;
	}
	public void setShipType(int shipType) {
		this.shipType = shipType;
	}
	public int getShipType() {
		return shipType;
	}
	
	@Transient
	public String getBstae() {
		return bstae;
	}
	public void setBstae(String bstae) {
		this.bstae = bstae;
	}
	
}