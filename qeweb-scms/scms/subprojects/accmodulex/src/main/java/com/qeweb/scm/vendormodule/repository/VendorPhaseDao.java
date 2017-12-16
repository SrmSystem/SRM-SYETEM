package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorPhaseDao extends BaseRepository<VendorPhaseEntity, Serializable>,JpaSpecificationExecutor<VendorPhaseEntity>{

	List<VendorPhaseEntity> findByCode(String code);

	List<VendorPhaseEntity> findByOrgId(Long orgId);
	
	List<VendorPhaseEntity> findByOrgIdAndAbolished(Long orgId,Integer abolished);

	List<VendorPhaseEntity> findByAbolished(Integer abolished);
}
