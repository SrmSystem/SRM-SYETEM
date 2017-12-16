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
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;

@Entity
@Table(name = "qeweb_purchase_plan_item")
public class PurchasePlanItemEntity extends BaseEntity {

	private PurchasePlanEntity plan;
	private OrganizationEntity vendor;
	private MaterialEntity material;		//物料
	private Integer itemNo;					//行号
	private Double totalPlanQty;			//计划总量
	private Timestamp planRecTime;		
	private FactoryEntity factory;        //工厂
	
	private String versionNumber; //版本号
	
	private Integer isNew;  //是否最新
	
	private Set<PurchasePlanHeadEntity> purchasePlanHeadEntity; //表头的表
	private Set<PurchasePlanVendorCapacityInfoEntity> purchasePlanVendorCapacityInfo;  //产能的表

	private Integer uploadStatus;  //上传状态
	
	private Integer publishStatus; //发布状态
	private UserEntity publishUser; //发布人
	private Timestamp publishTime; //发布时间
	
	private Integer confirmStatus;			// 确认状态  0：未确认 1：部分确认 2：已确认 -1：驳回 -2：驳回拒绝
	private String rejectReason;//驳回原因（供应商的操作）
	private UserEntity rejectUser;//驳回人（供应商的操作）
	private Timestamp rejectTime;//驳回时间（供应商的操作）
	
	private Integer vetoStatus;			// 驳回状态  未驳回  驳回
	private String vetoReason;//驳回原因（采购商的操作）
	private UserEntity vetoUser;//驳回人（采购商的操作）
	private Timestamp vetoTime;//驳回时间（采购商的操作）
	
	private String dayStock;   //当日库存
	private String unpaidQty;  //当日未交po数量
	
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
	
	private PurchasingGroupEntity group; //采购组
	
	//非持久数据（临时）
	public PurchasePlanItemHeadVO purchasePlanItemHeadVO;
	public PurchasePlanItemHeadVO headVO;

	
	@ManyToOne
	@JoinColumn(name="group_id")
	public PurchasingGroupEntity getGroup() {
		return group;
	}

	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="planItem") 
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<PurchasePlanVendorCapacityInfoEntity> getPurchasePlanVendorCapacityInfo() {
		return purchasePlanVendorCapacityInfo;
	}
	public void setPurchasePlanVendorCapacityInfo(
			Set<PurchasePlanVendorCapacityInfoEntity> purchasePlanVendorCapacityInfo) {
		this.purchasePlanVendorCapacityInfo = purchasePlanVendorCapacityInfo;
	}
	
	public Integer getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
	@ManyToOne
	@JoinColumn(name="factory_id")
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	
	@ManyToOne
	@JoinColumn(name="plan_id")
	public PurchasePlanEntity getPlan() {
		return plan;
	}

	public void setPlan(PurchasePlanEntity plan) {
		this.plan = plan;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	@Column(name="item_no")
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@Column(name="total_plan_qty")	
	public Double getTotalPlanQty() {
		return totalPlanQty;
	}

	public void setTotalPlanQty(Double totalPlanQty) {
		this.totalPlanQty = totalPlanQty;
	}

	@Column(name="plan_rec_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getPlanRecTime() {
		return planRecTime;
	}

	public void setPlanRecTime(Timestamp planRecTime) {
		this.planRecTime = planRecTime;
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

	@OneToMany(fetch=FetchType.LAZY, mappedBy="planItem") 
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<PurchasePlanHeadEntity> getPurchasePlanHeadEntity() {
		return purchasePlanHeadEntity;
	}

	public void setPurchasePlanHeadEntity(
			Set<PurchasePlanHeadEntity> purchasePlanHeadEntity) {
		this.purchasePlanHeadEntity = purchasePlanHeadEntity;
	}

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

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	@ManyToOne
	@JoinColumn(name="reject_user_id")
	public UserEntity getRejectUser() {
		return rejectUser;
	}
	
	public void setRejectUser(UserEntity rejectUser) {
		this.rejectUser = rejectUser;
	}

	public Timestamp getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Timestamp rejectTime) {
		this.rejectTime = rejectTime;
	}

	public Integer getVetoStatus() {
		return vetoStatus;
	}

	public void setVetoStatus(Integer vetoStatus) {
		this.vetoStatus = vetoStatus;
	}

	public String getVetoReason() {
		return vetoReason;
	}

	public void setVetoReason(String vetoReason) {
		this.vetoReason = vetoReason;
	}
	@ManyToOne
	@JoinColumn(name="veto_user_id")
	public UserEntity getVetoUser() {
		return vetoUser;
	}

	public void setVetoUser(UserEntity vetoUser) {
		this.vetoUser = vetoUser;
	}

	public Timestamp getVetoTime() {
		return vetoTime;
	}

	public void setVetoTime(Timestamp vetoTime) {
		this.vetoTime = vetoTime;
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
	public Integer getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
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
	
	
}
