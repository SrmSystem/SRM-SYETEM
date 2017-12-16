package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractFilesEntity;


public interface ContractFilesDao extends BaseRepository<ContractFilesEntity, Serializable>,JpaSpecificationExecutor<ContractFilesEntity>{
 
	List<ContractFilesEntity> findByBusIdAndBusTypeOrderByCreateTimeDesc(Long busId,Long busType);
}
