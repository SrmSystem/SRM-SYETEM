package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.RoleDataCfgEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleDataCfgDao extends BaseRepository<RoleDataCfgEntity, Serializable>,JpaSpecificationExecutor<RoleDataCfgEntity>{
	
}
