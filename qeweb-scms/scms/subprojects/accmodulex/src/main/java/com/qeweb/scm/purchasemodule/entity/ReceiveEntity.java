package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_receive")
public class ReceiveEntity extends BaseEntity {

	private String receiveCode;					//收货单号
	private OrganizationEntity buyer;
	private OrganizationEntity vendor;
	private String receiveOrg;
	private Integer receiveStatus;
	private UserEntity receiveUser;
	private Timestamp receiveTime;				
	private Set<ReceiveItemEntity> receiveItem;
	
	private String storeCode;	//仓库批次号
	
	private DeliveryEntity delivery;	//发货单(此对象仅限于收货单与发货单一一对应的情况)
	private Integer orderType;///1，国内，2，国外，3，外协
	
	private String purchasingGroupCode;		//采购组编码
	private Long purchasingGroupId;		//采购组Id
	
	private Integer replDeliveryStatus;			// 补货发货状态
	private Integer replReceiveStatus;          //补货收货状态
	
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
	private String attr16;		//反馈状态
	private String attr17;		//反馈日志
	private String attr18;		//雇员
	private String attr19;		//负数收货状态(0:正常收货， 1:负数收货(国内)， 2:负数收货(国外)， 3:负数收货(外协), 默认0)
	private String attr20;    	
	
	//非持久化字段
	private String deliveryCreateTime;		//发货单的创建时间
	private String orderCode;				//订单号
	private String invoiceCode;				//发票号
	private String transport;				//运输商
	private String etaTime;					//靠岸时间
	private String preEtaTime;				//预计靠岸时间
	private String preBoardTime;			//预计登船时间
	private String boardTime;				//登船时间
	private String materialCode;			//零件号
	private String logFlag;					//日志状态
	private String logs;					//日志
	private String scrapLogFlag;			//日志状态
	private String scrapLogs;				//日志
	private String purchasingGroupName;		//采购组名称
	
	@Transient
	public String getScrapLogFlag() {
		return scrapLogFlag;
	}

	public void setScrapLogFlag(String scrapLogFlag) {
		this.scrapLogFlag = scrapLogFlag;
	}

	@Transient
	public String getScrapLogs() {
		return scrapLogs;
	}

	public void setScrapLogs(String scrapLogs) {
		this.scrapLogs = scrapLogs;
	}

	@Transient
	public String getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(String logFlag) {
		this.logFlag = logFlag;
	}

	@Transient
	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	@Transient
	public String getDeliveryCreateTime() {
		return deliveryCreateTime;
	}

	public void setDeliveryCreateTime(String deliveryCreateTime) {
		this.deliveryCreateTime = deliveryCreateTime;
	}
	
	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Transient
	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	@Transient
	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	@Transient
	public String getEtaTime() {
		return etaTime;
	}

	public void setEtaTime(String etaTime) {
		this.etaTime = etaTime;
	}

	@Transient
	public String getPreEtaTime() {
		return preEtaTime;
	}

	public void setPreEtaTime(String preEtaTime) {
		this.preEtaTime = preEtaTime;
	}

	@Transient
	public String getPreBoardTime() {
		return preBoardTime;
	}

	public void setPreBoardTime(String preBoardTime) {
		this.preBoardTime = preBoardTime;
	}

	@Transient
	public String getBoardTime() {
		return boardTime;
	}

	public void setBoardTime(String boardTime) {
		this.boardTime = boardTime;
	}

	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@Column(name="receive_code")
	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
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

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@ManyToOne
	@JoinColumn(name="receive_user_id")
	public UserEntity getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(UserEntity receiveUser) {
		this.receiveUser = receiveUser;
	}

	@Column(name="receive_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	@OneToMany(fetch=FetchType.LAZY,mappedBy="receive")
	@JsonIgnore
	public Set<ReceiveItemEntity> getReceiveItem() {
		return receiveItem;
	}

	public void setReceiveItem(Set<ReceiveItemEntity> receiveItem) {
		this.receiveItem = receiveItem;
	}
	
	@Column(name="purchasing_group_code")
	public String getPurchasingGroupCode() {
		return purchasingGroupCode;
	}

	public void setPurchasingGroupCode(String purchasingGroupCode) {
		this.purchasingGroupCode = purchasingGroupCode;
	}
	
	@Column(name="purchasing_group_id")
	public Long getPurchasingGroupId() {
		return purchasingGroupId;
	}

	public void setPurchasingGroupId(Long purchasingGroupId) {
		this.purchasingGroupId = purchasingGroupId;
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

	@Column(name="store_code")
	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	//仅限于收货单与发货单一一对应时使用
	@OneToOne
	@JoinColumn(name="delivery_id")
	public DeliveryEntity getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryEntity delivery) {
		this.delivery = delivery;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Transient
	public String getPurchasingGroupName() {
		return purchasingGroupName;
	}

	public void setPurchasingGroupName(String purchasingGroupName) {
		this.purchasingGroupName = purchasingGroupName;
	}

	public Integer getReplDeliveryStatus() {
		return replDeliveryStatus;
	}

	public void setReplDeliveryStatus(Integer replDeliveryStatus) {
		this.replDeliveryStatus = replDeliveryStatus;
	}

	public Integer getReplReceiveStatus() {
		return replReceiveStatus;
	}

	public void setReplReceiveStatus(Integer replReceiveStatus) {
		this.replReceiveStatus = replReceiveStatus;
	}
	
}
