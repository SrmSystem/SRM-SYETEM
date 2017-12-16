package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.RoleDataEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleDataDao extends BaseRepository<RoleDataEntity, Serializable>,JpaSpecificationExecutor<RoleDataEntity>{
	
	@Modifying
	@Query("delete RoleDataEntity where roleId=?1 and roleDataCfgId=?2")
	public void deleteRoleData(long roleId, long roleDataCfgId);
	
	public RoleDataEntity findByRoleIdAndRoleDataCfgId(Long roleId, Long roleDataCfgId);
}
