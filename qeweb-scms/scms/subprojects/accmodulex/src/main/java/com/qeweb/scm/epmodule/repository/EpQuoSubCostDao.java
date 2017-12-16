package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpQuoSubCostEntity;


public interface EpQuoSubCostDao extends BaseRepository<EpQuoSubCostEntity, Serializable>,JpaSpecificationExecutor<EpQuoSubCostEntity>{

	public List<EpQuoSubCostEntity> findByEpQuoSubId(Long epQuoSubId);
	
}
