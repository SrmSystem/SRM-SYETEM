package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface UserDataDao extends BaseRepository<UserDataEntity, Serializable>,JpaSpecificationExecutor<UserDataEntity>{
	
	@Modifying
	@Query("delete UserDataEntity where userId=?1 and roleDataCfgId=?2")
	public void deleteUserData(long userId, long roleDataCfgId);
	
	public UserDataEntity findByUserIdAndRoleDataCfgId(Long userId, Long roleDataCfgId);
}
