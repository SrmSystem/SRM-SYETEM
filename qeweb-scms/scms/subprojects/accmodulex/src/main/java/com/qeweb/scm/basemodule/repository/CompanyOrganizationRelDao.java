package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface CompanyOrganizationRelDao extends BaseRepository<CompanyOrganizationRelEntity, Serializable>,JpaSpecificationExecutor<CompanyOrganizationRelEntity>{

	
	
	@Override
	public List<CompanyOrganizationRelEntity> findAll();
	
	CompanyOrganizationRelEntity findByCompanyIdAndOrganizationIdAndAbolished(Long  companyId , Long organizationId,Integer abolished);
	
	@Modifying  
	@Query("update CompanyOrganizationRelEntity set abolished=1 where id=?1")
	public void abolishCompanyOrg(long id);
	
	//通过公司的id 查询未废除的额公司和采购组织的关系
	List<CompanyOrganizationRelEntity> findByCompanyIdAndAbolished(Long  companyId ,Integer abolished);
	
	//通过采购组织的id 查询未废除的额公司和采购组织的关系
	List<CompanyOrganizationRelEntity> findByOrganizationIdAndAbolished(Long  organizationId ,Integer abolished);
}
