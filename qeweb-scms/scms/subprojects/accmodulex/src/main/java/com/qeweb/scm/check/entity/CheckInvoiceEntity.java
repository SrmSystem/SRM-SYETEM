package com.qeweb.scm.check.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
/**
 * 
 * @author VILON
 *
 */
@Entity
@Table(name = "qeweb_check_invoice")
public class CheckInvoiceEntity extends BaseEntity {
	/**
	 * 发票号
	 */
	private String code;
	/**
	 * 接收状态
	 */
	private Integer receiveStatus;
	/**
	 * 发票日期
	 */
	private Timestamp billTime;
	/**
	 * 税前单价
	 */
	private Double noTaxAmount;
	/**
	 * 税率
	 */
	private Double taxRate;
	/**
	 * 税后单价
	 */
	private Double taxAmount;
	/**
	 * 税金
	 */
	private Double tax;
	/**
	 * 开票人
	 */
	private UserEntity billUser;
	/**
	 * 附件名
	 */
	private String invoiceFileName;
	/**
	 * 附件路径
	 */
	private String invoiceFilePath;
	/**
	 * 对账单
	 */
	private CheckEntity check;
	
	private String col1;		//开票人名称	 
	private String col2;		//索赔金额开票状态(1为已开索赔金额发票)	 
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	
	//非持久化字段
	private String checkItemIds;	//对账单明细id多个以逗号分隔
	
	@Column(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	@Column(name="bill_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getBillTime() {
		return billTime;
	}
	public void setBillTime(Timestamp billTime) {
		this.billTime = billTime;
	}
	@Column(name="no_tax_amount")
	public Double getNoTaxAmount() {
		return noTaxAmount;
	}
	public void setNoTaxAmount(Double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}
	@Column(name="tax_rate")
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	@Column(name="tax_amount")
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	@Column(name="tax")
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	@ManyToOne
	@JoinColumn(name="bill_user_id")
	public UserEntity getBillUser() {
		return billUser;
	}
	public void setBillUser(UserEntity billUser) {
		this.billUser = billUser;
	}
	@Column(name="invoice_file_name")
	public String getInvoiceFileName() {
		return invoiceFileName;
	}
	public void setInvoiceFileName(String invoiceFileName) {
		this.invoiceFileName = invoiceFileName;
	}
	@Column(name="invoice_file_path")
	public String getInvoiceFilePath() {
		return invoiceFilePath;
	}
	public void setInvoiceFilePath(String invoiceFilePath) {
		this.invoiceFilePath = invoiceFilePath;
	}
	@ManyToOne
	@JoinColumn(name="check_id")
	public CheckEntity getCheck() {
		return check;
	}
	public void setCheck(CheckEntity check) {
		this.check = check;
	}
	@Transient
	public String getCheckItemIds() {
		return checkItemIds;
	}
	public void setCheckItemIds(String checkItemIds) {
		this.checkItemIds = checkItemIds;
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
	
}
