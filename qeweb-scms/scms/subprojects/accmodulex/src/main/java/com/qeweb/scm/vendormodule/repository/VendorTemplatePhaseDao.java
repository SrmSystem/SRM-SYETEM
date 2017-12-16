package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorTemplatePhaseDao extends BaseRepository<VendorTemplatePhaseEntity, Serializable>,JpaSpecificationExecutor<VendorTemplatePhaseEntity>{

	@Query("from VendorTemplatePhaseEntity where vendorNavTemplate.id=:templateId order by phaseSn asc")
	List<VendorTemplatePhaseEntity> findByVendorNavTemplate(@Param("templateId") Long templateId);

	@Modifying
	@Query("delete from VendorTemplatePhaseEntity where vendorNavTemplate.id=?")
	void deleteByVendorNavTemplate(long id);

	List<VendorTemplatePhaseEntity> findByTemplateIdOrderByPhaseSnAsc(long id);

}
