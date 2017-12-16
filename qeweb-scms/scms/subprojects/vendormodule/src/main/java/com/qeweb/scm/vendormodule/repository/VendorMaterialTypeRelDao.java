package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;

/**
 * 供应商物料分类关联Dao
 * @author pjjxiajun
 * @date 2015年7月28日
 * @path com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao.java
 */
public interface VendorMaterialTypeRelDao extends PagingAndSortingRepository<VendorMaterialTypeRelEntity, Serializable>,JpaSpecificationExecutor<VendorMaterialTypeRelEntity>{

	List<VendorMaterialTypeRelEntity> findById(Long id);

	List<VendorMaterialTypeRelEntity> findByOrgId(Long orgId);

	VendorMaterialTypeRelEntity findByOrgIdAndMaterialTypeId(Long orgId, Long materialTypeId);
	
}
