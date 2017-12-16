package com.qeweb.scm.epmodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * 报价模型与物料关系
 */
@Entity
@Table(name = "QEWEB_EP_MODULE_MATERIAL_REL")
public class EpModuleMaterialRelEntity extends EPBaseEntity {

	private MaterialEntity material;
	private EpModuleEntity emModule;
	private Long materialId; // 物料id
	private Long moduleId; // 模型id
	
	private String moduleName;
	private String materialName;
	
	@ManyToOne
	@JoinColumn(name = "materialId", insertable = false, updatable = false)
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	@ManyToOne
	@JoinColumn(name = "moduleId", insertable = false, updatable = false)
	public EpModuleEntity getEmModule() {
		return emModule;
	}

	public void setEmModule(EpModuleEntity emModule) {
		this.emModule = emModule;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Transient
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Transient
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	

}
