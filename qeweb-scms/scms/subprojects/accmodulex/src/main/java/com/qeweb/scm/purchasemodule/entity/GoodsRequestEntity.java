package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 要货单主信息
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_goods_request")
public class GoodsRequestEntity extends BaseEntity {

	private String requestCode; 			// 要货单号
	private PurchaseOrderEntity order;		// 采购订单
	private OrganizationEntity buyer;		// 采方		
	private OrganizationEntity sender;		// 发货方（三方仓库或供方）
	private OrganizationEntity vendor;		// 供方
	private String receiveOrg;				// 收货方
	private UserEntity purchaseUser;		// 采购员
	private Timestamp orderDate;			// 订单日期
	private Integer publishStatus;			// 发布状态
	private UserEntity publishUser;			// 发布人
	private Timestamp publishTime;			// 发布时间
	private Integer confirmStatus;			// 确认状态
	private UserEntity confirmUser;			// 确认人
	private Timestamp confirmTime;			// 确认时间
	private Integer closeStatus;			// 关闭状态
	private UserEntity closeUser;			// 关闭人
	private Timestamp closeTime;			// 关闭时间
	private Integer deliveryStatus;			// 发货状态
	private Integer receiveStatus;			// 收货状态
	private Set<GoodsRequestItemEntity> requestItem;
	
	private Long senderId;					//发货方ID
	private Long vendorId;					//供应商ID
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

	@ManyToOne
	@JoinColumn(name="order_id") 
	public PurchaseOrderEntity getOrder() {
		return order;
	}

	public void setOrder(PurchaseOrderEntity order) {
		this.order = order;
	}

	@Column(name="request_code")
	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	@ManyToOne
	@JoinColumn(name="buyer_id")
	public OrganizationEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}
	
	@ManyToOne
	@JoinColumn(name="sender_id")
	public OrganizationEntity getSender() {
		return sender;
	}

	public void setSender(OrganizationEntity sender) {
		this.sender = sender;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@Column(name="rec_org")
	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	@ManyToOne
	@JoinColumn(name="pur_user_id")
	public UserEntity getPurchaseUser() {
		return purchaseUser;
	}

	public void setPurchaseUser(UserEntity purchaseUser) {
		this.purchaseUser = purchaseUser;
	}

	@Column(name="order_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	@Column(name="close_status")
	public Integer getCloseStatus() {
		return closeStatus;
	}

	public void setCloseStatus(Integer closeStatus) {
		this.closeStatus = closeStatus;
	}

	@ManyToOne
	@JoinColumn(name="close_user_id")
	public UserEntity getCloseUser() {
		return closeUser;
	}

	public void setCloseUser(UserEntity closeUser) {
		this.closeUser = closeUser;
	}

	@Column(name="close_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}

	@Column(name="delivery_status")
	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	@Column(name="rec_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	
	@OneToMany(mappedBy="request") 
	@JsonIgnore
	public Set<GoodsRequestItemEntity> getRequestItem() {
		return requestItem;
	}

	public void setRequestItem(Set<GoodsRequestItemEntity> requestItem) {
		this.requestItem = requestItem;
	}
	
	@Transient
	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	@Transient
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
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

}
