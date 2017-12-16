package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FactoryDao extends BaseRepository<FactoryEntity, Serializable>,JpaSpecificationExecutor<FactoryEntity>{

	@Override
	public List<FactoryEntity> findAll();
	
	FactoryEntity findByCode(String code);

	public List<FactoryEntity> findByAbolished(Integer abolished);

	public FactoryEntity findByNameAndAbolished(String name,Integer abolished);
	
}
