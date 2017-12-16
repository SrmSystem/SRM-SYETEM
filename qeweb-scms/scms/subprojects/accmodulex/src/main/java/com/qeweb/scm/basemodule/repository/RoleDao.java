package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleDao extends BaseRepository<RoleEntity, Serializable>,JpaSpecificationExecutor<RoleEntity>{

	@Query("from RoleEntity where code=? and abolished = 0")
	RoleEntity findByCode(String code);

	@Query("from RoleEntity where name=? and abolished = 0")
	RoleEntity findByName(String name);

	RoleEntity findByCodeAndAbolishedAndIdNot(String code,Integer abolished, Long id);

	RoleEntity findByNameAndAbolishedAndIdNot(String name,Integer abolished, Long id);

	
	
	

}
