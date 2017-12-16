package com.qeweb.scm.epmodule.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;

/**
 * 报价模型
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_MODULE")
public class EpModuleEntity extends EPBaseEntity {

	private String code; // 编号
	private String name; // 名称
	private String remarks; // 备注
/*	private Integer abolished;//废除状态
*/	private Integer isDefault; // 是否默认模板
	private Set<EpModuleItemEntity> moduleItems;
	
	public EpModuleEntity(){}
	
	public EpModuleEntity(Long id){
		setId(id);
	}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "module")
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<EpModuleItemEntity> getModuleItems() {
		return moduleItems;
	}

	public void setModuleItems(Set<EpModuleItemEntity> moduleItems) {
		this.moduleItems = moduleItems;
	}

}
