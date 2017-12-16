package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;

/**
 * 报价模型DAO
 * @author ronnie
 *
 */
public interface EpModuleDao extends BaseRepository<EpModuleEntity, Serializable>,JpaSpecificationExecutor<EpModuleEntity>{

	@Query("from EpModuleEntity a where a.id in (?1)")
	List<EpModuleEntity> findByIds(List<Long> ids);

	public List<EpModuleEntity> findByIsDefault(Integer isDefault);
	
	public EpModuleEntity findByCodeAndAbolished(String code,Integer abolished);
	
	
}
