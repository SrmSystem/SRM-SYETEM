package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.qeweb.scm.basemodule.entity.DynamicDataEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 动态数据管理DAO 
 */
public interface DynamicDataDao extends BaseRepository<DynamicDataEntity, Serializable>,JpaSpecificationExecutor<DynamicDataEntity>{
	
	public DynamicDataEntity findByBeanId(String beanId);
	
	@QueryHints(value={
			@QueryHint(name="org.hibernate.cacheable",value="true"),
			@QueryHint(name="org.hibernate.cacheRegion",value="com.qeweb.scm.basemodule.entity.main")
	})
	@Query("from DynamicDataEntity")
	public List<DynamicDataEntity>  findAllWithCache();
}
