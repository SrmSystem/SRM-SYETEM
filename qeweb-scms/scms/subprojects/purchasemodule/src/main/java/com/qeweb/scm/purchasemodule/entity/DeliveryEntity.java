package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_delivery")
public class DeliveryEntity extends BaseEntity {

	private String deliveryCode;			//发货单编号
	private OrganizationEntity vendor;		//供应商
	private OrganizationEntity buyer;		//采购商
	private String receiveOrg;				//收货方
	private Integer deliveryStatus;			//发货状态 @see PurchaseConstans
	private Timestamp expectedArrivalTime;	//预计到货时间
	private Timestamp deliveyTime;			//发货时间(提货时间)
	private UserEntity deliveryUser;		//发货人
	private Integer receiveStatus;			//收货状态@see PurchaseConstans
	private Integer auditStatus;			//审核状态
	private String remark;					//备注
	private PurchaseOrderEntity order;  	//订单信息
	private Integer status;					//发货单状态 0:正常 1：失效 
	private Integer deliveryType;			//发货单类型 针对不同订单创建的发货单 @see OrderType.java
	private Set<DeliveryItemEntity> deliveryItem;
	
	private String version;					//版本号
	
	private OrganizationEntity logistics;	//物流公司
	private String col1;					//收货地址
	private String col2;					//运输工具
	private String col3;					//地点
	private String col4;					//供应商联系人1
	private String col5;					//收货联系人
	private String col6;					//收货联系电话
	private String col7;					//收货地址
	private String col8;					//物流联系人
	private String col9;					//供应商上传附件的地址
	private String col10;					//供应商上传附件的名称
	private String col11;					//审核的备注
	private String col12;					//供应商联系电话1
	private String col13;					//物流联系人电话
	private String col14;					//物流公司名称(运输商ID)
	private String col15;					//收货时间(实际提货时间)
	private String col16;					//发票单号
	private String col17;					//发票日期
	private String col18;					//发票金额
	private String col19;					//预计靠岸时间-
	private String col20;					//靠岸时间
	private String col21;					//预计登船时间-
	private String col22;					//实际登船时间-
	private String col23;					//采购商审核文件名
	private String col24;					//采购商审核文件地址
	private String col25;					//要求提货时间
	private String col26;					//供应商SiteCode
	private String col27;
	private String col28;					//运输商名称
	private String col29;					//运输商处理时间
	private String col30;					//国外提货单使用
	private String col31;					//供应商联系人2
	private String col32;					//供应商联系电话2
	private String col33;
	private String col34;
	private String col35;
	//非持久化数据
	private String vendorBathNum;			//供应商批次号
	private String orderCode;

	@Column(name="version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name="delivery_code")
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name="buyer_id")
	public OrganizationEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}
	
	@Column(name="rec_org")
	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	@Column(name="delivery_status")
	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	@Column(name="expected_arrival_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getExpectedArrivalTime() {
		return expectedArrivalTime;
	}

	public void setExpectedArrivalTime(Timestamp expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Column(name="delivery_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getDeliveyTime() {
		return deliveyTime;
	}

	public void setDeliveyTime(Timestamp deliveyTime) {
		this.deliveyTime = deliveyTime;
	}

	@ManyToOne
	@JoinColumn(name="delivery_user_id")
	public UserEntity getDeliveryUser() {
		return deliveryUser;
	}

	public void setDeliveryUser(UserEntity deliveryUser) {
		this.deliveryUser = deliveryUser;
	}

	@ManyToOne
	@JoinColumn(name="order_id")
	public PurchaseOrderEntity getOrder() {
		return order;
	}

	public void setOrder(PurchaseOrderEntity order) {
		this.order = order;
	}

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Column(name="audit_status")
	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "delivery")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<DeliveryItemEntity> getDeliveryItem() {
		return deliveryItem;
	}

	public void setDeliveryItem(Set<DeliveryItemEntity> deliveryItem) {
		this.deliveryItem = deliveryItem;
	}

	@ManyToOne
	@JoinColumn(name="logistics_id")
	public OrganizationEntity getLogistics() {
		return logistics;
	}

	public void setLogistics(OrganizationEntity logistics) {
		this.logistics = logistics;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name="delivery_type")
	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
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

	@Column(name="col12")
	public String getCol12() {
		return col12;
	}

	public void setCol12(String col12) {
		this.col12 = col12;
	}

	@Column(name="col13")
	public String getCol13() {
		return col13;
	}

	public void setCol13(String col13) {
		this.col13 = col13;
	}

	@Column(name="col14")
	public String getCol14() {
		return col14;
	}

	public void setCol14(String col14) {
		this.col14 = col14;
	}

	@Column(name="col15")
	public String getCol15() {
		return col15;
	}

	public void setCol15(String col15) {
		this.col15 = col15;
	}

	@Column(name="col16")
	public String getCol16() {
		return col16;
	}

	public void setCol16(String col16) {
		this.col16 = col16;
	}

	@Column(name="col17")
	public String getCol17() {
		return col17;
	}

	public void setCol17(String col17) {
		this.col17 = col17;
	}

	@Column(name="col18")
	public String getCol18() {
		return col18;
	}

	public void setCol18(String col18) {
		this.col18 = col18;
	}

	@Column(name="col19")
	public String getCol19() {
		return col19;
	}

	public void setCol19(String col19) {
		this.col19 = col19;
	}

	@Column(name="col20")
	public String getCol20() {
		return col20;
	}

	public void setCol20(String col20) {
		this.col20 = col20;
	}
	
	@Column(name="col21")
	public String getCol21() {
		return col21;
	}

	public void setCol21(String col21) {
		this.col21 = col21;
	}

	@Column(name="col22")
	public String getCol22() {
		return col22;
	}

	public void setCol22(String col22) {
		this.col22 = col22;
	}

	@Column(name="col23")
	public String getCol23() {
		return col23;
	}

	public void setCol23(String col23) {
		this.col23 = col23;
	}

	@Column(name="col24")
	public String getCol24() {
		return col24;
	}

	public void setCol24(String col24) {
		this.col24 = col24;
	}

	@Column(name="col25")
	public String getCol25() {
		return col25;
	}

	public void setCol25(String col25) {
		this.col25 = col25;
	}

	@Column(name="col26")
	public String getCol26() {
		return col26;
	}

	public void setCol26(String col26) {
		this.col26 = col26;
	}

	@Column(name="col27")
	public String getCol27() {
		return col27;
	}

	public void setCol27(String col27) {
		this.col27 = col27;
	}

	@Column(name="col28")
	public String getCol28() {
		return col28;
	}

	public void setCol28(String col28) {
		this.col28 = col28;
	}

	@Column(name="col29")
	public String getCol29() {
		return col29;
	}

	public void setCol29(String col29) {
		this.col29 = col29;
	}

	@Column(name="col30")
	public String getCol30() {
		return col30;
	}

	public void setCol30(String col30) {
		this.col30 = col30;
	}
	
	@Column(name="col31")
	public String getCol31() {
		return col31;
	}

	public void setCol31(String col31) {
		this.col31 = col31;
	}

	@Column(name="col32")
	public String getCol32() {
		return col32;
	}

	public void setCol32(String col32) {
		this.col32 = col32;
	}

	@Column(name="col33")
	public String getCol33() {
		return col33;
	}

	public void setCol33(String col33) {
		this.col33 = col33;
	}

	@Column(name="col34")
	public String getCol34() {
		return col34;
	}

	public void setCol34(String col34) {
		this.col34 = col34;
	}

	@Column(name="col35")
	public String getCol35() {
		return col35;
	}

	public void setCol35(String col35) {
		this.col35 = col35;
	}

	@Transient
	public String getVendorBathNum() {
		return vendorBathNum;
	}

	public void setVendorBathNum(String vendorBathNum) {
		this.vendorBathNum = vendorBathNum;
	}

	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
}
