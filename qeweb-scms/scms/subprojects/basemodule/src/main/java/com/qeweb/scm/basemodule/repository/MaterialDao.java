package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 物料DAO 
 */
public interface MaterialDao extends BaseRepository<MaterialEntity, Serializable>,JpaSpecificationExecutor<MaterialEntity>{
	
	List<MaterialEntity> findByCode(String code);

	List<MaterialEntity> findByCodeAndIdNotIn(String code, ArrayList<Long> idList);

	List<MaterialEntity> findByMaterialTypeId(long id);
	
	MaterialEntity findByName(String name);
	
	@Query("from MaterialEntity a where upper(a.code)=?1")
	List<MaterialEntity> findByUpperCode(String code);
}
