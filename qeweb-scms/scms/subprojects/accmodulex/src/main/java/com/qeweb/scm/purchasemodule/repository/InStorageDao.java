package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.InStorageEntity;


public interface InStorageDao extends BaseRepository<InStorageEntity, Serializable>,JpaSpecificationExecutor<InStorageEntity>{

	@Override
	public List<InStorageEntity> findAll();
	
	@Override
	public Page<InStorageEntity> findAll(Pageable page);

	public InStorageEntity findByinStorageCode(String inStorageCode);

	@Query("from InStorageEntity a where a.inStorageCode = ?1 and a.orgId = ?2")
	public InStorageEntity findByinStorageCodeAndOrgID(String inStorageCode, String orgId);
}
