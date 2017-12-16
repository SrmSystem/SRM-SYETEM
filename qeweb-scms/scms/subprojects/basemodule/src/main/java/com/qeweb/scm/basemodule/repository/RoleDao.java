package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleDao extends BaseRepository<RoleEntity, Serializable>,JpaSpecificationExecutor<RoleEntity>{

	RoleEntity findByCode(String code);

	RoleEntity findByName(String name);

	RoleEntity findByCodeAndIdNot(String code, Long id);

	RoleEntity findByNameAndIdNot(String name, Long id);

	
	
	

}
