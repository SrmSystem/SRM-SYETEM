package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorPhaseCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorPhaseCfgDao extends BaseRepository<VendorPhaseCfgEntity, Serializable>,JpaSpecificationExecutor<VendorPhaseCfgEntity>{

	List<VendorPhaseCfgEntity> findByVendorIdAndTemplateId(long id, Long templateId);

	/**
	 * 获得该供应商的阶段配置，并按照阶段顺序升序排序
	 * @param vendorId 供应商ID
	 * @return 阶段配置
	 */
	List<VendorPhaseCfgEntity> findByVendorIdOrderByPhaseSnAsc(Long vendorId);

	/**
	 * 获得该供应商的阶段配置，并按照阶段顺序升序排序
	 * @param orgId 组织ID
	 * @return
	 */
	List<VendorPhaseCfgEntity> findByOrgIdOrderByPhaseSnAsc(Long orgId);

	List<VendorPhaseCfgEntity> findByOrgIdAndTemplateId(Long orgId, Long templateId);
	
	List<VendorPhaseCfgEntity> findByOrgIdAndPhaseSnGreaterThan(Long orgId, Integer phaseSn);

	List<VendorPhaseCfgEntity> findByOrgIdAndPhaseSnLessThanOrderByPhaseSnDesc(Long orgId, Integer phaseSn);


}
