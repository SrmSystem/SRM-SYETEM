package com.qeweb.scm.vendorperformancemodule.entity;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 模版设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_template")
public class VendorPerforTemplateEntity  extends BaseEntity{
	
	private String code;	
	private String name;	
	private Integer weight;//权重模式
	private Integer templateType;//模版类型 @PerformanceTypeConstant
	private long cycleId;//周期ID
	private Integer defaulted;//是否默认
	private String describe;
	private Integer finishStatus;//完成状态
	private Long modelId;//绩效模型ID
	private String mappingScore;//分数映射
	
	
	private VendorPerforCycleEntity cycle;
	private VendorPerforModelEntity model;
	
	//用来作为传递数据使用
	private List<VendorPerforTemplateSettingEntity> settingList;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public long getCycleId() {
		return cycleId;
	}
	public void setCycleId(long cycleId) {
		this.cycleId = cycleId;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Integer getDefaulted() {
		return defaulted;
	}
	public void setDefaulted(Integer defaulted) {
		this.defaulted = defaulted;
	}
	
	@ManyToOne
	@JoinColumn(name="cycleId",insertable=false,updatable=false)
	public VendorPerforCycleEntity getCycle() {
		return cycle;
	}
	public void setCycle(VendorPerforCycleEntity cycle) {
		this.cycle = cycle;
	}
	public Integer getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}
	public Integer getTemplateType() {
		return templateType;
	}
	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="modelId",insertable=false,updatable=false)
	public VendorPerforModelEntity getModel() {
		return model;
	}
	public void setModel(VendorPerforModelEntity model) {
		this.model = model;
	}
	
	public String getMappingScore() {
		return mappingScore;
	}
	public void setMappingScore(String mappingScore) {
		this.mappingScore = mappingScore;
	}
	@Transient
	public List<VendorPerforTemplateSettingEntity> getSettingList() {
		return settingList;
	}
	public void setSettingList(List<VendorPerforTemplateSettingEntity> settingList) {
		this.settingList = settingList;
	}
	
	
	
	
}
