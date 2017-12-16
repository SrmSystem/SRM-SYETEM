package com.qeweb.scm.contractmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.contractmodule.entity.ContractModuleItemEntity;

/**
 * 合同条款Dao
 * @author u
 *
 */
public interface ContractModuleItemDao extends BaseRepository<ContractModuleItemEntity, Serializable>,JpaSpecificationExecutor<ContractModuleItemEntity>{
 
	/**
	 * 根据合同模板Id获取合同条款
	 * @return
	 */
	@Query("from ContractModuleItemEntity a where a.moduleId= ?1 and a.abolished = ?2 order by a.sqenum asc")
	public List<ContractModuleItemEntity> getItemByModuleId(long moduleId,int abolished);
	
	/**
	 * 根据合同条款的parentId与sqenum获取
	 * @return
	 */
	@Query("from ContractModuleItemEntity a where a.parentId= ?1 and a.sqenum = ?2 and a.abolished = ?3 and a.moduleId = ?4")
	public ContractModuleItemEntity findByParentIdAndSqenum(long parentId,int sqenum,int abolished,long moduleId);
	
	/**
	 * 根据合同条款的parentId获取
	 * @return
	 */
	@Query("from ContractModuleItemEntity a where a.parentId= ?1 and a.moduleId = ?2 order by a.sqenum asc")
	public List<ContractModuleItemEntity> findByParentId(long parentId,long moduleId);
	
	/**
	 *  根据合同模板Id 与 合同条款的parentId 获取
	 * @return
	 */
	@Query("from ContractModuleItemEntity a where a.moduleId = ?1 and a.parentId= ?2 order by a.sqenum asc")
	public List<ContractModuleItemEntity> findByModuleIdAndParentId(long moduleId , long parentId);
	
}
