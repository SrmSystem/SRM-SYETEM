package com.qeweb.scm.check.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * @author huanghm
 *
 */
@Entity
@Table(name = "QEWEB_VIEW_NO_CHECK_ITEM")
public class NoCheckItemsEntity extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String orderCode;				//采购凭证号
	private Integer orderNo;				//采购凭证的项目编号
	private String dlvCode;				//采购凭证号
	private String factoryCode;   //工厂编码
	private String factoryName;   //工厂名称
	private Long buyerId;			//采购组织Id
	private String buyerCode;			//采购组织编码
	private String buyerName;			//采购组织名称
	private Long vendorId;			//供应商Id
	private String vendorCode;			//供应商编码
	private String vendorName;			//供应商名称
	private String groupCode; 	//采购组
	private String groupName;	//采购组的描述
	private Double recQty;				//收货数量
	private Timestamp postDate; //凭证中的过帐日期
	private String materialNo;	//物料凭证编号
	private Integer materialProject;	//物料凭证中的项目
	private Double invoicedQuantity; //开票数量
	private String currencyCode;   //货币码
	private Integer itemNo;				//订单行
	private Long materialId;		//物料Id
	private String materialCode;		//物料编码
	private String materialName;		//物料名称
	private String unitName;			//单位
	private Timestamp changeDate;		//变更时间
	private Timestamp createTime;		//创建时间
	


	private Double orderQty;   //订单数量
	private String txz01;//短文本 
	private String type;//区分状态
	private Double price; //不含税价格
	
	private Integer dataType;			//数据类型 1=收货单，-1=退货单
	
	private String state;//状态
	
	private Double conInvoicedQuantity; //仍要开票数量(grqty - ivqty)
	private Double conPrice; //仍要开票价格
	
	@Column(name="ORDER_CODE")
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@Column(name="ORDER_NO")
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name="DLV_CODE")
	public String getDlvCode() {
		return dlvCode;
	}
	public void setDlvCode(String dlvCode) {
		this.dlvCode = dlvCode;
	}
	
	@Column(name="BUYER_ID")
	public Long getBuyerId() {
		return buyerId;
	}
	
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	@Column(name="BUYER_CODE")
	public String getBuyerCode() {
		return buyerCode;
	}
	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}
	
	@Column(name="BUYER_NAME")
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@Column(name="VENDOR_ID")
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	@Column(name="VENDOR_CODE")
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	@Column(name="VENDOR_NAME")
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	@Column(name="GROUP_CODE")
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@Column(name="GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name="REC_QTY")
	public Double getRecQty() {
		return recQty;
	}
	public void setRecQty(Double recQty) {
		this.recQty = recQty;
	}
	
	@Column(name="POST_DATE")
	public Timestamp getPostDate() {
		return postDate;
	}
	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}
	
	@Column(name="MATERIAL_NO")
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	
	@Column(name="MATERIAL_PROJECT")
	public Integer getMaterialProject() {
		return materialProject;
	}
	public void setMaterialProject(Integer materialProject) {
		this.materialProject = materialProject;
	}
	
	
	@Column(name="INVOICED_QUANTITY")
	public Double getInvoicedQuantity() {
		return invoicedQuantity;
	}
	public void setInvoicedQuantity(Double invoicedQuantity) {
		this.invoicedQuantity = invoicedQuantity;
	}
	
	@Column(name="CURRENCY_CODE")
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Column(name="ITEM_NO")
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	
	@Column(name="MATERIAL_ID")
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	
	@Column(name="MATERIAL_CODE")
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Column(name="MATERIAL_NAME")
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@Column(name="UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@Column(name="CREATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="FACTORY_CODE")
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	
	@Column(name="FACTORY_NAME")
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	
	@Column(name="ORDER_QTY")
	public Double getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	
	
	@Column(name="TXZ01")
	public String getTxz01() {
		return txz01;
	}
	public void setTxz01(String txz01) {
		this.txz01 = txz01;
	}
	
	@Column(name="TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="PRICE")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(name="DATA_TYPE")
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	
	@Column(name="STATE")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Column(name="CHANGE_DATE")
	public Timestamp getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Timestamp changeDate) {
		this.changeDate = changeDate;
	}
	
	@Column(name="CON_INVOICED_QUANTITY")
	public Double getConInvoicedQuantity() {
		return conInvoicedQuantity;
	}
	public void setConInvoicedQuantity(Double conInvoicedQuantity) {
		this.conInvoicedQuantity = conInvoicedQuantity;
	}
	
	@Column(name="CON_PRICE")
	public Double getConPrice() {
		return conPrice;
	}
	public void setConPrice(Double conPrice) {
		this.conPrice = conPrice;
	}
	
	
	
	
}