package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;

/**
 * 报价模型明细DAO
 * @author ronnie
 *
 */
public interface EpModuleItemDao extends BaseRepository<EpModuleItemEntity, Serializable>,JpaSpecificationExecutor<EpModuleItemEntity>{

	@Query("from EpModuleItemEntity a where a.parentId = ?1")
	List<EpModuleItemEntity> findByModuleItemId(Long epModuleItemId);
	
	@Query("from EpModuleItemEntity a where a.module.id = ?1")
	List<EpModuleItemEntity> findByModuleId(Long moduleId);
	
	@Query("from EpModuleItemEntity a where a.parentId = ?1")
	List<EpModuleItemEntity> findByParentId(Long parentId);
}
