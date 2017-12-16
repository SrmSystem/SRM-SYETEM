package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpModuleMaterialRelEntity;

/**
 * 报价模型与物料关系DAO
 */
public interface EpModuleMaterialRelDao extends BaseRepository<EpModuleMaterialRelEntity, Serializable>,JpaSpecificationExecutor<EpModuleMaterialRelEntity>{
	
	@Override
	public List<EpModuleMaterialRelEntity> findAll();
	
	@Query("select a.moduleId from EpModuleMaterialRelEntity a where a.material.id =?1")
	public List<Long> findModuleIdByMaterialId(Long materialId);
	
	public List<EpModuleMaterialRelEntity> findByMaterialIdAndModuleId(Long materialId,Long moduleId);
	
	public List<EpModuleMaterialRelEntity> findByMaterialId(Long materialId);
}
