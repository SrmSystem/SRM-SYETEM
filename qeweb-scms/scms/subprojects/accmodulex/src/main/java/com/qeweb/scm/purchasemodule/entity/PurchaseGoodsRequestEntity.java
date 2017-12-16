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
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
@Entity
@Table(name = "purchase_goods_request")
public class PurchaseGoodsRequestEntity extends BaseEntity{
    private FactoryEntity factory;//工厂
    private MaterialEntity material;//物料
    private PurchasingGroupEntity group;//采购组
    private OrganizationEntity vendor;//供应商
    private Timestamp rq;//日期（预计到货日期）
    private String zb;//占比
    private String jhzzt;//计划周转天
    private String zljyt;//质量检验天数
    private String shpl;//送货频率（天数）
    private String ysts;//运输天数
    private String pcsl;//排产数量
    private String dhsl;//到货数量
    private String kcsl;//库存数量
    private String fhsl;//发货数量
    private Integer publishStatus;//发布状态
    private Integer vendorConfirmStatus;//供应商确认状态
    private Integer buyerConfirmStatus;//采购商确认状态
	private Timestamp syncTime;//同步时间
	private Integer  isFull; //是否匹配订单行完全
	private String   surQry; //剩余的数量
	
	private String flag;  //	新品/量产标识（X - 新品，O - 量产）
	private String meins;  //单位
	
	
	private Integer isModify; //修改标识
	
	
    //非持久化字段
    private String factoryCode;//工厂编码
    private String materialCode;//物料编码
    private String materialName;//物料名称
    private String groupCode;//采购组编码
    private String vendorCode;//供应商编码
    private String rqStr;//日期-字符串  
    private String isRed;//标红数据
    private String DrawingNumber; //图号
    
    
    @ManyToOne
	@JoinColumn(name="factory_id")
    public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	
	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	@ManyToOne
	@JoinColumn(name="purchasing_group_id")
	public PurchasingGroupEntity getGroup() {
		return group;
	}
	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@Column(name="rq")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getRq() {
		return rq;
	}
	public void setRq(Timestamp rq) {
		this.rq = rq;
	}
	public String getZb() {
		return zb;
	}
	public void setZb(String zb) {
		this.zb = zb;
	}
	public String getJhzzt() {
		return jhzzt;
	}
	public void setJhzzt(String jhzzt) {
		this.jhzzt = jhzzt;
	}
	public String getZljyt() {
		return zljyt;
	}
	public void setZljyt(String zljyt) {
		this.zljyt = zljyt;
	}
	public String getShpl() {
		return shpl;
	}
	public void setShpl(String shpl) {
		this.shpl = shpl;
	}
	public String getYsts() {
		return ysts;
	}
	public void setYsts(String ysts) {
		this.ysts = ysts;
	}
	public String getPcsl() {
		return pcsl;
	}
	public void setPcsl(String pcsl) {
		this.pcsl = pcsl;
	}
	public String getDhsl() {
		return dhsl;
	}
	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}
	public String getKcsl() {
		return kcsl;
	}
	public void setKcsl(String kcsl) {
		this.kcsl = kcsl;
	}
	public String getFhsl() {
		return fhsl;
	}
	public void setFhsl(String fhsl) {
		this.fhsl = fhsl;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public Integer getVendorConfirmStatus() {
		return vendorConfirmStatus;
	}
	public void setVendorConfirmStatus(Integer vendorConfirmStatus) {
		this.vendorConfirmStatus = vendorConfirmStatus;
	}
	public Integer getBuyerConfirmStatus() {
		return buyerConfirmStatus;
	}
	public void setBuyerConfirmStatus(Integer buyerConfirmStatus) {
		this.buyerConfirmStatus = buyerConfirmStatus;
	}
	
	@Transient
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	
	@Transient
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Transient
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@Transient
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@Transient
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	@Transient
	public String getIsRed() {
		return isRed;
	}
	public void setIsRed(String isRed) {
		this.isRed = isRed;
	}
	public Timestamp getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	
	@Transient
	public String getRqStr() {
		return rqStr;
	}
	public void setRqStr(String rqStr) {
		this.rqStr = rqStr;
	}

	public Integer getIsFull() {
		return isFull;
	}
	public void setIsFull(Integer isFull) {
		this.isFull = isFull;
	}
	public String getSurQry() {
		return surQry;
	}
	public void setSurQry(String surQry) {
		this.surQry = surQry;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMeins() {
		return meins;
	}
	public void setMeins(String meins) {
		this.meins = meins;
	}
	public Integer getIsModify() {
		return isModify;
	}
	public void setIsModify(Integer isModify) {
		this.isModify = isModify;
	}
	@Column(name="DRAWING_NUMBER")
	public String getDrawingNumber() {
		return DrawingNumber;
	}
	public void setDrawingNumber(String drawingNumber) {
		DrawingNumber = drawingNumber;
	}
	
	

}