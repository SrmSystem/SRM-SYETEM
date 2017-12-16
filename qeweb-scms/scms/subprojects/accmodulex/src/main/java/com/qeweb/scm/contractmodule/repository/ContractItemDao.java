package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;


public interface ContractItemDao extends BaseRepository<ContractItemEntity, Serializable>,JpaSpecificationExecutor<ContractItemEntity>{
 
	List<ContractItemEntity> findByContractIdAndAbolished(Long contractId,Integer abolished);
	
}
