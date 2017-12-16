package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.ColumnSettingEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface ColumnSettingDao extends BaseRepository<ColumnSettingEntity, Serializable>,JpaSpecificationExecutor<OrganizationEntity>{
	

	ColumnSettingEntity findByUserIdAndPathAndTable(Long userId,String path,String table);
	
	@Override
	public Page<ColumnSettingEntity> findAll(Pageable page);
	
	
}
