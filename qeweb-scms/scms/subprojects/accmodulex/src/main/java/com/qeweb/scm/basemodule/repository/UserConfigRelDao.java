package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface UserConfigRelDao extends BaseRepository<GroupConFigRelEntity, Serializable>,JpaSpecificationExecutor<GroupConFigRelEntity>{
	
	@Override
	public List<GroupConFigRelEntity> findAll();
	
	
	@Query("from GroupConFigRelEntity a where a.user.id = ?1 ")
	GroupConFigRelEntity getRelByUserId(Long userId);
	
	@Query("from GroupConFigRelEntity a where a.groupIds Like ?1 ")
	List<GroupConFigRelEntity> getRelByGroupId(String groupId);

}
