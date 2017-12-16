package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.qeweb.scm.basemodule.entity.UserWarnRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface UserWarnRelDao extends BaseRepository<UserWarnRelEntity, Serializable>,JpaSpecificationExecutor<UserWarnRelEntity>{
	
	@Override
	public List<UserWarnRelEntity> findAll();
	
	
	@Query("from UserWarnRelEntity a where a.user.id = ?1 ")
	List<UserWarnRelEntity> getRelByUserId(Long userId);

}
