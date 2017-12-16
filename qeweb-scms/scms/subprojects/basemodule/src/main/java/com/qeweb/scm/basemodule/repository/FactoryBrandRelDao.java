package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.FactoryBrandRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FactoryBrandRelDao extends BaseRepository<FactoryBrandRelEntity, Serializable>,JpaSpecificationExecutor<FactoryBrandRelEntity>{

	@Modifying
	@Query("delete from FactoryBrandRelEntity where factoryId=?1")
	void deleteByFactory(long id);

	List<FactoryBrandRelEntity> findByFactoryId(Long id);

	

}
