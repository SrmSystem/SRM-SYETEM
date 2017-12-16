package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpQuoSubItemEntity;

/**
 * 复制报价模型明细DAO
 * @author u
 *
 */
public interface EpQuoSubItemDao extends BaseRepository<EpQuoSubItemEntity, Serializable>,JpaSpecificationExecutor<EpQuoSubItemEntity>{
	
	
	List<EpQuoSubItemEntity> findByParentId(Long parentId);
	
	List<EpQuoSubItemEntity> findByEpMaterialIdAndIsTop(Long epMaterialId,Integer isTop);
	
	
	@Query("from EpQuoSubItemEntity a where a.epMaterialId = ?1")
	List<EpQuoSubItemEntity> findByEpMaterialId(Long epMaterialId);
}
