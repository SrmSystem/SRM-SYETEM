package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractContentEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleItemEntity;


public interface ContractContentDao extends BaseRepository<ContractContentEntity, Serializable>,JpaSpecificationExecutor<ContractContentEntity>{
 
	/**
	 * 根据合同Id获取合同条款
	 * @return
	 */
	@Query("from ContractContentEntity a where a.contractId= ?1 and a.abolished = ?2 order by a.sqenum asc")
	public List<ContractContentEntity> getItemByContractId(long contractId,int abolished);
	
	/**
	 * 根据合同条款的parentId与sqenum获取
	 * @return
	 */
	@Query("from ContractContentEntity a where a.parentId= ?1 and a.sqenum = ?2 and a.abolished = ?3 and a.contractId= ?4")
	public ContractContentEntity findByParentIdAndSqenum(long parentId,int sqenum,int abolished,long contractId);
	
	/**
	 * 根据合同条款的parentId获取
	 * @return
	 */
	@Query("from ContractContentEntity a where a.parentId= ?1 and a.contractId= ?2")
	public List<ContractContentEntity> findByParentId(long parentId,long contractId);
	
	/**
	 *  根据合同模板Id 与 合同条款的parentId 获取
	 * @return
	 */
	@Query("from ContractContentEntity a where a.contractId = ?1 and a.parentId= ?2 order by a.sqenum asc")
	public List<ContractContentEntity> findByContractIdAndParentId(long contractId , long parentId);
}
