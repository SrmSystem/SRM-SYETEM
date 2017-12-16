package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.baseline.common.entity.WarnItemEntity;
import com.qeweb.scm.baseline.common.entity.WarnMainEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface WarnMainDao extends BaseRepository<WarnMainEntity, Serializable>,JpaSpecificationExecutor<WarnMainEntity>{
	
		
	@Query("update WarnMainEntity set enableStatus = ?1 where id = ?2 ")
	void updateWarnMainWithEnableStatus(int enableStatus,long id);
	
	@Query("from WarnItemEntity where warnMainId = ?1 ")
	List<WarnItemEntity> findItemListByMainId(long mainId);
	
	@Query("from WarnMainEntity where code = ?1 ")
	WarnMainEntity findByCode(String code);
		

}
