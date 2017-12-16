package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.baseline.common.entity.WarnItemEntity;
import com.qeweb.scm.baseline.common.entity.WarnMainEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface WarnItemDao extends BaseRepository<WarnItemEntity, Serializable>,JpaSpecificationExecutor<WarnItemEntity>{

	@Query("update WarnItemEntity set roleId = ?1,warnContent = ?2 where id= ?3 ")
	void updateWarnItem(Long roleId,String warnContent,Long id);
	
	@Query("from WarnItemEntity where warnMainId = ?1 ")
	List<WarnItemEntity> findByWarnMainId(Long warnMainId);
}
