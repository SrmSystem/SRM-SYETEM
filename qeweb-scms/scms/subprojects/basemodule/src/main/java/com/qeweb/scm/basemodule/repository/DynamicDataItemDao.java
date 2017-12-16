package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.qeweb.scm.basemodule.entity.DynamicDataSceneEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 */
public interface DynamicDataItemDao extends BaseRepository<DynamicDataSceneEntity, Serializable>,JpaSpecificationExecutor<DynamicDataSceneEntity>{
	
	@QueryHints(value={
			@QueryHint(name="org.hibernate.cacheable",value="true"),
			@QueryHint(name="org.hibernate.cacheRegion",value="com.qeweb.scm.basemodule.entity.main")
	})
	@Query("from DynamicDataSceneEntity")
	public List<DynamicDataSceneEntity>  findAllWithCache();

	@Query("from DynamicDataSceneEntity a where a.dataEx.beanId = ?1 and a.colCode = ?2 and a.way = ?3")
	public DynamicDataSceneEntity findDynamicDataItem(String beanId, String colCode, String way);
}
