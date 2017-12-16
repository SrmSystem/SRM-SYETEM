package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * 开票清单明细
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_bill_list_item")
public class BillListItemEntity extends BaseEntity {

	private BillListEntity billList;		// 清单主表
	private Long accountItemId;				// 结算明细[入库单、上线单]
	private MaterialEntity material;		// 物料
	private Double accountQty;				// 结算数量
	private Double price;					// 价格
	private Double tax;						// 税率
	private Integer lineNumber;             // 行号
	private Integer syncStatus;             // 同步状态标识
	
	//非持久化
	private String code;				//结算单号
	private Double qty;					//数量
	private String inStorageCode;		//入库单号
	private String money;		        //金额

	@ManyToOne
	@JoinColumn(name="bill_list_id")
	public BillListEntity getBillList() {
		return billList;
	}

	public void setBillList(BillListEntity billList) {
		this.billList = billList;
	}

	@Column(name="account_item_id")
	public Long getAccountItemId() {
		return accountItemId;
	}

	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="account_qty")
	public Double getAccountQty() {
		return accountQty;
	}

	public void setAccountQty(Double accountQty) {
		this.accountQty = accountQty;
	}

	@Column(name="price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name="tax")
	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}
	
	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Transient
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	@Column(name="line_number")
	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Column(name="sync_status")
	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Transient
	public String getInStorageCode() {
		return inStorageCode;
	}

	public void setInStorageCode(String inStorageCode) {
		this.inStorageCode = inStorageCode;
	}

	@Transient
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

}
