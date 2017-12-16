package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.BrandEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface BrandDao extends BaseRepository<BrandEntity, Serializable>,JpaSpecificationExecutor<BrandEntity>{

	@Override
	public List<BrandEntity> findAll();
	

}
