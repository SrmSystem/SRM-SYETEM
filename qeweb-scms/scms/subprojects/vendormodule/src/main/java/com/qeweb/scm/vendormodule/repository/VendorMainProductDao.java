package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;

/**
 * 主要产品维护Dao
 */
public interface VendorMainProductDao extends BaseRepository<VendorMaterialTypeRelEntity, Serializable>,JpaSpecificationExecutor<VendorMaterialTypeRelEntity>{
	@Override
	public Page<VendorMaterialTypeRelEntity> findAll(Pageable page);
	
	public List<VendorMaterialTypeRelEntity> findByOrgId(Integer orgId);
	
	List<MaterialTypeEntity> findById(Long id);
}