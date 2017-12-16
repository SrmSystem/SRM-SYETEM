package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FactoryOrganizationRelDao extends BaseRepository<FactoryOrganizationRelEntity, Serializable>,JpaSpecificationExecutor<FactoryOrganizationRelEntity>{

	@Override
	public List<FactoryOrganizationRelEntity> findAll();
	
	@Query("select a from FactoryOrganizationRelEntity a where a.factory.code=?1 and a.org.code=?2")
	public FactoryOrganizationRelEntity findByFactoryCodeAndOrgCode(String factoryCode,String organizationCode);
    
	@Modifying  
	@Query("update FactoryOrganizationRelEntity set abolished=1 where id=?1")
	public void abolishFactoryOrg(long id);
	
	FactoryOrganizationRelEntity findByFactoryIdAndOrgIdAndAbolished(Long  factoryId , Long orgId,Integer abolished);
	
	//通过工厂的id获取工厂和采购组织的关系
	List<FactoryOrganizationRelEntity> findByFactoryIdAndAbolished(Long  factoryId ,Integer abolished);
	
	//通过采购组织id获取工厂和采购组织的关系
	List<FactoryOrganizationRelEntity> findByOrgIdAndAbolished(Long orgId,Integer abolished);
	
	
}
