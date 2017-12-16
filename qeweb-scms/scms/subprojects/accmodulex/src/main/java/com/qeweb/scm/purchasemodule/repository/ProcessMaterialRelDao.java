package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.ProcessMaterialRelEntity;


public interface ProcessMaterialRelDao extends BaseRepository<ProcessMaterialRelEntity, Serializable>,JpaSpecificationExecutor<ProcessMaterialRelEntity>{

	List<ProcessMaterialRelEntity> findByProcessIdAndAbolished(long processId,Integer abolished);
	
	List<ProcessMaterialRelEntity> findByMaterialIdAndAbolished(long materialId,Integer abolished);

	ProcessMaterialRelEntity findByProcessIdAndMaterialId(Long processId,long materialId);
	
	
}
