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
import com.qeweb.scm.basemodule.entity.MaterialEntity;

@Entity
@Table(name = "qeweb_delivery_item")
public class DeliveryItemEntity extends BaseEntity {

	private DeliveryEntity delivery;
	private Integer itemNo;
	private PurchaseOrderItemEntity orderItem;			//订单明细
	private PurchaseOrderItemPlanEntity orderItemPlan;	//订单计划
	private PurchaseGoodsRequestEntity request;	//要货计划
	private MaterialEntity material;
	private Double deliveryQty;							//发货数量/外协合格数量
	private Double unqualifiedQty;						//不合格数量			
	private Double receiveQty;							//收货数量
	private Integer receiveStatus;
	private String dn;              //DN号
	private Integer dnCreateStatus;//创建DN状态 1成功 -1失败  //持久化
	private String dnErrorMessage;//创建DN失败信息
	private String posnr;//交货项目
	private String meins;//基本单位
	private String charg;//批号

	private String boxNum;//箱数量，系统计算的
	private String minBoxNum;//小包装箱数量，系统计算的
	private String version;//供应商填写的文本行
	private Timestamp manufactureDate;//生产日期
	private  Double standardBoxNum;//标准箱数(大包装)
	private  Double minPackageQty;//标准箱数(小包装)
	private String vendorCharg;//追溯批号
	
	
	private String inspectionPath;//检验报告路径
	private String inspectionName;//检验报告名称
	private String remark;//备注
	
	
	//非持久化字段
	private Double toreceiveQty;						//收货的数量
	private Double toreturnQty;							//验退数量
	private long orderItemPlanId;
	private String orderCode;							//采购订单号
	private Double orderQty;							//需求数量
	private Integer orderItemNo;						//订单行号
	private String requireEtaTime; 
	
	private String trueMaterialCode;					//实际收货零件号
	private String location;							//库位
	private String site;								//地址
	private String receiveTime;							//收货时间
	private String buyerName;							//采购员
	private String reqEtaTime;							//Require ETA port date
	private String quaProLine;							//产品线
	private String isReplenish;							//是否为补货
	private String process;								//工序
	private Integer syncStatus;//创建DN号成功状态 0未创建 -1失败 1成功
	private String message;//创建DN时返回的信息     
	private String sapDn;              //创建DN号 暂存DN号
	private String sapPosnr;//创建DN号交货项目 暂存交货项目
	
	private Integer errorStatus; 	//异常状态
	private Long changeUserId; 		//导致异常的人员
	private Timestamp changeTime; 	//导致异常的时间
	
    //非持久字段end
	
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
	

	//冗余字段
	private String requestCode;			//要货单号
	private String receiveItemAttr18;	//实际合格数量
	private String receiveItemAttr19;	//实际不合格数量
	private String unitName;//单位
	private String currency;//币种
	private String receiveOrg;//收货方
	private Double shouldQty;					// 应发数量 (orderQty-deliveryQty-toDeliveryQty))
	private Double sendQty;						// 发货数量/合格数量
	private Timestamp requestTime;				// 要求到货时间
	private Double oldDeliveryQty;//上一次保存的发货数量
	
	private String zlock;		//锁定标识
	private String lockStatus;	//冻结状态
	private String loekz;		//删除标识
	private String elikz;		//交付标识
	private String bstae;		//内向交货标识
	
	private String isQtyModify;//订单数量是否减少，当前发货单数量是否大于订单数量，异常状态 add by chao.gu
	
	private String shipQrCode;//补货二维码
	
	
	
	@ManyToOne
	@JoinColumn(name="delivery_id")
	public DeliveryEntity getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryEntity delivery) {
		this.delivery = delivery;
	}

	@Column(name="item_no")
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_item_id")
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_item_plan_id")
	public PurchaseOrderItemPlanEntity getOrderItemPlan() {
		return orderItemPlan;
	}

	public void setOrderItemPlan(PurchaseOrderItemPlanEntity orderItemPlan) {
		this.orderItemPlan = orderItemPlan;
	}
	
	
	

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="request_id")
	public PurchaseGoodsRequestEntity getRequest() {
		return request;
	}

	public void setRequest(PurchaseGoodsRequestEntity request) {
		this.request = request;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="delivery_qty")
	public Double getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	
	@Column(name="unqualified_qty")
	public Double getUnqualifiedQty() {
		return unqualifiedQty;
	}

	public void setUnqualifiedQty(Double unqualifiedQty) {
		this.unqualifiedQty = unqualifiedQty;
	}

	@Column(name="receive_qty")
	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	@Column(name="receive_status")
	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Transient
	public long getOrderItemPlanId() {
		return orderItemPlanId;
	}

	public void setOrderItemPlanId(long orderItemPlanId) {
		this.orderItemPlanId = orderItemPlanId;
	}

	@Transient
	public Double getToreceiveQty() {
		return toreceiveQty;
	}

	public void setToreceiveQty(Double toreceiveQty) {
		this.toreceiveQty = toreceiveQty;
	}

	@Transient
	public Double getToreturnQty() {
		return toreturnQty;
	}

	public void setToreturnQty(Double toreturnQty) {
		this.toreturnQty = toreturnQty;
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

	@Transient
	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	@Transient
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Transient
	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	@Transient
	public Integer getOrderItemNo() {
		return orderItemNo;
	}

	public void setOrderItemNo(Integer orderItemNo) {
		this.orderItemNo = orderItemNo;
	}

	@Transient
	public String getTrueMaterialCode() {
		return trueMaterialCode;
	}

	public void setTrueMaterialCode(String trueMaterialCode) {
		this.trueMaterialCode = trueMaterialCode;
	}

	@Transient
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Transient
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	@Transient
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Transient
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	@Transient
	public String getReqEtaTime() {
		return reqEtaTime;
	}

	public void setReqEtaTime(String reqEtaTime) {
		this.reqEtaTime = reqEtaTime;
	}

	
	
	@Transient
	public String getReceiveItemAttr18() {
		return receiveItemAttr18;
	}

	public void setReceiveItemAttr18(String receiveItemAttr18) {
		this.receiveItemAttr18 = receiveItemAttr18;
	}

	@Transient
	public String getReceiveItemAttr19() {
		return receiveItemAttr19;
	}

	public void setReceiveItemAttr19(String receiveItemAttr19) {
		this.receiveItemAttr19 = receiveItemAttr19;
	}

	@Transient
	public String getRequireEtaTime() {
		return requireEtaTime;
	}

	public void setRequireEtaTime(String requireEtaTime) {
		this.requireEtaTime = requireEtaTime;
	}

	@Transient
	public String getIsReplenish() {
		return isReplenish;
	}

	public void setIsReplenish(String isReplenish) {
		this.isReplenish = isReplenish;
	}
	
	@Transient
	public String getQuaProLine() {
		return quaProLine;
	}

	public void setQuaProLine(String quaProLine) {
		this.quaProLine = quaProLine;
	}
	
	@Transient
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getMeins() {
		if(meins!=null&&meins.indexOf("null")!=-1){
			return "";
		}
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getCharg() {
		return charg;
	}

	public void setCharg(String charg) {
		this.charg = charg;
	}

	


	@Transient
	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Transient
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPosnr() {
		return posnr;
	}

	public void setPosnr(String posnr) {
		this.posnr = posnr;
	}

	public String getVersion() {
		if(version!=null&&version.indexOf("null")!=-1){
			return "";
		}
		return version;
	}

	public void setVersion(String version) {
		
		this.version = version;
	}

	@Column(name="manufacture_date")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Timestamp manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	

	public String getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(String boxNum) {
		this.boxNum = boxNum;
	}

	public Double getStandardBoxNum() {
		return standardBoxNum;
	}

	public void setStandardBoxNum(Double standardBoxNum) {
		this.standardBoxNum = standardBoxNum;
	}

	public String getInspectionPath() {
		return inspectionPath;
	}

	public void setInspectionPath(String inspectionPath) {
		this.inspectionPath = inspectionPath;
	}

	public String getInspectionName() {
		return inspectionName;
	}

	public void setInspectionName(String inspectionName) {
		this.inspectionName = inspectionName;
	}

	public String getRemark() {
		if(remark!=null&&remark.indexOf("null")!=-1){
			return "";
		}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
	@Transient
	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
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
	public Double getOldDeliveryQty() {
		return oldDeliveryQty;
	}

	public void setOldDeliveryQty(Double oldDeliveryQty) {
		this.oldDeliveryQty = oldDeliveryQty;
	}

	public String getMinBoxNum() {
		return minBoxNum;
	}

	public void setMinBoxNum(String minBoxNum) {
		this.minBoxNum = minBoxNum;
	}

	public Double getMinPackageQty() {
		return minPackageQty;
	}

	public void setMinPackageQty(Double minPackageQty) {
		this.minPackageQty = minPackageQty;
	}

	public String getVendorCharg() {
		return vendorCharg;
	}

	public void setVendorCharg(String vendorCharg) {
		this.vendorCharg = vendorCharg;
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
	public String getZlock() {
		return zlock;
	}

	public void setZlock(String zlock) {
		this.zlock = zlock;
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

	@Transient
	public String getIsQtyModify() {
		return isQtyModify;
	}

	public void setIsQtyModify(String isQtyModify) {
		this.isQtyModify = isQtyModify;
	}

	@Transient
	public String getSapDn() {
		return sapDn;
	}

	public void setSapDn(String sapDn) {
		this.sapDn = sapDn;
	}

	@Transient
	public String getSapPosnr() {
		return sapPosnr;
	}

	public void setSapPosnr(String sapPosnr) {
		this.sapPosnr = sapPosnr;
	}

	public Integer getDnCreateStatus() {
		return dnCreateStatus;
	}

	public void setDnCreateStatus(Integer dnCreateStatus) {
		this.dnCreateStatus = dnCreateStatus;
	}

	public String getDnErrorMessage() {
		return dnErrorMessage;
	}

	public void setDnErrorMessage(String dnErrorMessage) {
		this.dnErrorMessage = dnErrorMessage;
	}

	@Transient
	public String getShipQrCode() {
		return shipQrCode;
	}

	public void setShipQrCode(String shipQrCode) {
		this.shipQrCode = shipQrCode;
	}

	@Transient
	public String getBstae() {
		return bstae;
	}

	public void setBstae(String bstae) {
		this.bstae = bstae;
	}
	
	
}
