package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;

/**
 *入厂检验不合格信息管理
 * @author sxl
 *
 */
@Entity
@Table(name = "qeweb_qm_infactoryunqu")
public class InFactoryUnqualifiedEntity extends BaseEntity{

	private Timestamp startTime;//开始时间
	
	private Timestamp endTime;//结束时间
	
	private String month; // 月份
	
	private Long vendorId;//供应商ID
	
	private Long materialId;//物料ID
	
	private Integer totalNumber;//总数
	
	private String describe;//表述
	
	private Integer dispose;//处置类型 1:让步接收，2:返修，3:报废
	
	private Integer samplings;//抽检数
	
	private Integer unqualified;//不合格
	
	private Integer ppm;//PPM计算数量  PPM计算：如果抽检数为0，=数量；如果不为0，=不合格数/抽检数*数量
	
	private String deliveryPpm;    //交付PPM
	
	private String partybCounts;	//售后总数
	
	private Integer qualityStatus;//状态
	
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	
	private MaterialEntity materialEntity;
	
	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}

	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}
	@ManyToOne
	@JoinColumn(name = "materialId",insertable = false,updatable = false)
	public MaterialEntity getMaterialEntity() {
		return materialEntity;
	}

	public void setMaterialEntity(MaterialEntity materialEntity) {
		this.materialEntity = materialEntity;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getDispose() {
		return dispose;
	}

	public void setDispose(Integer dispose) {
		this.dispose = dispose;
	}

	public Integer getSamplings() {
		return samplings;
	}

	public void setSamplings(Integer samplings) {
		this.samplings = samplings;
	}

	public Integer getUnqualified() {
		return unqualified;
	}

	public void setUnqualified(Integer unqualified) {
		this.unqualified = unqualified;
	}

	public Integer getPpm() {
		return ppm;
	}

	public void setPpm(Integer ppm) {
		this.ppm = ppm;
	}

	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}

	public String getDeliveryPpm() {
		return deliveryPpm;
	}

	public void setDeliveryPpm(String deliveryPpm) {
		this.deliveryPpm = deliveryPpm;
	}

	public String getPartybCounts() {
		return partybCounts;
	}

	public void setPartybCounts(String partybCounts) {
		this.partybCounts = partybCounts;
	}
	
}
