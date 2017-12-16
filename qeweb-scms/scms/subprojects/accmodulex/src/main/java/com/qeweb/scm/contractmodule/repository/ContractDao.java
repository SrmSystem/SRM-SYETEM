package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractEntity;


public interface ContractDao extends BaseRepository<ContractEntity, Serializable>,JpaSpecificationExecutor<ContractEntity>{
 
	ContractEntity findByCodeAndAbolished(String code,Integer abolished);
}
