package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.FileUpEntity;


public interface FileUpDao extends BaseRepository<FileUpEntity, Serializable>,JpaSpecificationExecutor<FileUpEntity>{

	
	@Query("from FileUpEntity a where a.descriptEntity.id= ?1 and a.receive.id = ?2")
	FileUpEntity findByDesriptIdAndReceiveId(Long desriptId,Long receiveId);
}
