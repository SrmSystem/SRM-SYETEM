package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 发票
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_invoice")
public class InvoiceEntity extends BaseEntity {

	private BillListEntity billList;		// 开票清单主单
	private String invoiceCode;				// 发票号
	private Double invoiceMoney;			// 金额
	private Timestamp invoiceTime;			// 发票时间

	@ManyToOne
	@JoinColumn(name="bill_list_id")
	public BillListEntity getBillList() {
		return billList;
	}

	public void setBillList(BillListEntity billList) {
		this.billList = billList;
	}

	@Column(name="invoice_code")
	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	@Column(name="invoice_money")
	public Double getInvoiceMoney() {
		return invoiceMoney;
	}

	public void setInvoiceMoney(Double invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}

	@Column(name="invoice_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Timestamp getInvoiceTime() {
		return invoiceTime;
	}

	public void setInvoiceTime(Timestamp invoiceTime) {
		this.invoiceTime = invoiceTime;
	}
	
}
