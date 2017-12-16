package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.VendorBFMatTypeEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface VendorBFMatTypeDao extends BaseRepository<VendorBFMatTypeEntity, Serializable>,JpaSpecificationExecutor<VendorBFMatTypeEntity>{


	public VendorBFMatTypeEntity findByOrgIdAndBrandIdAndFactoryIdAndMatTypeId(Long orgId, Long brandId,
			Long factoryId, Long matTypeId);

	@Query(value="select distinct brandId from VendorBFMatTypeEntity where orgId=?1")
	public List<Long> getBrandIdListByOrgId(Long orgId);

	public List<VendorBFMatTypeEntity> findByOrgId(Long orgId);

	@Query(value="select distinct brandId,brandName from VendorBFMatTypeEntity where orgId=?1")
	public List<Object[]> getBrandListByOrgId(Long orgId);
	

}
