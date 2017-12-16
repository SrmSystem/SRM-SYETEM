package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;

/**
 * 合同模板Dao
 * @author u
 *
 */
public interface ContractModuleDao extends BaseRepository<ContractModuleEntity, Serializable>,JpaSpecificationExecutor<ContractModuleEntity>{
 
}
