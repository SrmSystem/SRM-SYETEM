package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractExtEntity;


public interface ContractExtDao extends BaseRepository<ContractExtEntity, Serializable>,JpaSpecificationExecutor<ContractExtEntity>{
 
	List<ContractExtEntity> findByContractIdOrderByCreateTimeDesc(Long contractId);
}
