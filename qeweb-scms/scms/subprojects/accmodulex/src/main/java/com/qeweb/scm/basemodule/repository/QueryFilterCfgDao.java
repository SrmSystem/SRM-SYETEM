package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.QueryFilterCfgEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface QueryFilterCfgDao extends BaseRepository<QueryFilterCfgEntity, Serializable>,JpaSpecificationExecutor<QueryFilterCfgEntity>{
	
}
