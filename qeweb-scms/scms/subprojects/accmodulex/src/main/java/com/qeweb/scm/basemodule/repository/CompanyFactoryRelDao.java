package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface CompanyFactoryRelDao extends BaseRepository<CompanyFactoryRelEntity, Serializable>,JpaSpecificationExecutor<CompanyFactoryRelEntity>{

	List<CompanyFactoryRelEntity> findByFactoryId(Long id);
	
	@Override
	public List<CompanyFactoryRelEntity> findAll();
	
	public List<CompanyFactoryRelEntity> findByAbolished(Integer abolished);
	
    CompanyFactoryRelEntity findByCompanyIdAndFactoryIdAndAbolished(Long  companyId , Long factoryId,Integer abolished);
	
	@Query("select a from CompanyFactoryRelEntity a where a.company.code=?1 and a.factory.code=?2")
	public CompanyFactoryRelEntity findByCompanyCodeFactoryCode(String companyCode,String factoryCode);
	
	@Modifying  
	@Query("update CompanyFactoryRelEntity set abolished=1 where id=?1")
	public void abolishCompanyFactory(Long id);
	
    //通过公司的id查询未废除的公司的与工厂的关系
	List<CompanyFactoryRelEntity> findByCompanyIdAndAbolished(Long  companyId ,Integer abolished);
	
   //通过工厂的id获取未废除的公司和工厂的关系
	List<CompanyFactoryRelEntity> findByFactoryIdAndAbolished(Long  factoryId ,Integer abolished);
}
