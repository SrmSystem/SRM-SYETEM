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

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

/**
 * 开票清单
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_bill_list")
public class BillListEntity extends BaseEntity {
	
	private String billCode;				// 清单号
	private OrganizationEntity buyer;
	private OrganizationEntity vendor;
	private Double totalPrice;				// 合计金额
	private Double tax;						// 税率
	private Double totalTaxPrice;			// 含税合计
	private Integer billType;				// 清单类型 0：入库开票 1：上线结算开票
	private Integer invoiceStatus;			// 开票状态 0：未开票  1：已开票
	private UserEntity invoiceUser;			// 开票人
	private Timestamp invoiceTime;			// 开票时间
	private Integer receiveStatus;			// 接收状态 0：未接收 1：已接收  -1： 驳回
	private UserEntity receiveUser;			// 发票接收人
	private Timestamp receiveTime;			// 发票接收时间
	private Set<BillListItemEntity> billListItem;	//清单明细
	private Set<InvoiceEntity> invoiceItem;			//发票信息
	private Integer syncStatus;              // 同步状态标识
	private String poNumber;                 // 采购订单号

	@Column(name="bill_code")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
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

	@Column(name="total_price")
	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name="tax")
	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	@Column(name="total_tax_price")
	public Double getTotalTaxPrice() {
		return totalTaxPrice;
	}

	public void setTotalTaxPrice(Double totalTaxPrice) {
		this.totalTaxPrice = totalTaxPrice;
	}

	@Column(name="bill_type")
	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	@Column(name="invoice_status")
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="billList")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<BillListItemEntity> getBillListItem() {
		return billListItem;
	}

	public void setBillListItem(Set<BillListItemEntity> billListItem) {
		this.billListItem = billListItem;
	}

	@ManyToOne
	@JoinColumn(name="invoice_user_id")
	public UserEntity getInvoiceUser() {
		return invoiceUser;
	}

	public void setInvoiceUser(UserEntity invoiceUser) {
		this.invoiceUser = invoiceUser;
	}

	@Column(name="invoice_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getInvoiceTime() {
		return invoiceTime;
	}

	public void setInvoiceTime(Timestamp invoiceTime) {
		this.invoiceTime = invoiceTime;
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

	@OneToMany(fetch=FetchType.LAZY, mappedBy="billList")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<InvoiceEntity> getInvoiceItem() {
		return invoiceItem;
	}

	public void setInvoiceItem(Set<InvoiceEntity> invoiceItem) {
		this.invoiceItem = invoiceItem;
	}

	@Column(name="sync_status")
	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Column(name="po_number")
	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
}
