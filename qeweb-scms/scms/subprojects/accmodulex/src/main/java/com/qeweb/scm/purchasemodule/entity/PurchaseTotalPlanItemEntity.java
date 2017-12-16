package com.qeweb.scm.purchasemodule.entity;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;

@Entity
@Table(name = "QEWEB_PURCHASE_TOTAL_PLAN_ITEM")
public class PurchaseTotalPlanItemEntity extends BaseEntity {

	private PurchaseTotalPlanEntity plan; //总量计划的头
	private MaterialEntity material;		//物料
	private Double totalPlanQty;			//计划总量	
	private FactoryEntity factory;        //工厂
	private UserEntity  buyer;              //采购员
	private PurchasingGroupEntity  purchasingGroup;              //采购组
	private String versionNumber;       //版本号
	private Integer isNew;                   //是否最新
	private String dayStock;   //当日库存
	private String unpaidQty;  //当日未交po数量
	
	private Set<PurchaseTotalPlanHeadEntity> purchaseTotalPlanHeadEntity; 
	
	//非持久数据（临时）
	public PurchasePlanItemHeadVO purchasePlanItemHeadVO;
	public PurchasePlanItemHeadVO headVO;
	
	
	@ManyToOne
	@JoinColumn(name="group_id")
	public PurchasingGroupEntity getPurchasingGroup() {
		return purchasingGroup;
	}
	public void setPurchasingGroup(PurchasingGroupEntity purchasingGroup) {
		this.purchasingGroup = purchasingGroup;
	}
	
	
	@ManyToOne
	@JoinColumn(name="buyer_id")
	public UserEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(UserEntity buyer) {
		this.buyer = buyer;
	}
	@ManyToOne
	@JoinColumn(name="plan_id")
	public PurchaseTotalPlanEntity getPlan() {
		return plan;
	}
	public void setPlan(PurchaseTotalPlanEntity plan) {
		this.plan = plan;
	}
	
	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	public Double getTotalPlanQty() {
		return totalPlanQty;
	}
	public void setTotalPlanQty(Double totalPlanQty) {
		this.totalPlanQty = totalPlanQty;
	}
	
	@ManyToOne
	@JoinColumn(name="factory_id")
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	
	public String getVersionNumber() {
		return versionNumber;
	}
	
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public Integer getIsNew() {
		return isNew;
	}
	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	public String getDayStock() {
		return dayStock;
	}
	public void setDayStock(String dayStock) {
		this.dayStock = dayStock;
	}
	public String getUnpaidQty() {
		return unpaidQty;
	}
	public void setUnpaidQty(String unpaidQty) {
		this.unpaidQty = unpaidQty;
	}
	
	@Transient
	public PurchasePlanItemHeadVO getPurchasePlanItemHeadVO() {
		return purchasePlanItemHeadVO;
	}

	public void setPurchasePlanItemHeadVO(
			PurchasePlanItemHeadVO purchasePlanItemHeadVO) {
		this.purchasePlanItemHeadVO = purchasePlanItemHeadVO;
	}
	@Transient
	public PurchasePlanItemHeadVO getHeadVO() {
		return headVO;
	}

	public void setHeadVO(PurchasePlanItemHeadVO headVO) {
		this.headVO = headVO;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="planItem") 
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<PurchaseTotalPlanHeadEntity> getPurchaseTotalPlanHeadEntity() {
		return purchaseTotalPlanHeadEntity;
	}
	public void setPurchaseTotalPlanHeadEntity(
			Set<PurchaseTotalPlanHeadEntity> purchaseTotalPlanHeadEntity) {
		this.purchaseTotalPlanHeadEntity = purchaseTotalPlanHeadEntity;
	}
	

}
