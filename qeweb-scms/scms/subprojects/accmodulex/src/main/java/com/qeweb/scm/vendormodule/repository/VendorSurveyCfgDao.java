package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorSurveyCfgDao extends BaseRepository<VendorSurveyCfgEntity, Serializable>,JpaSpecificationExecutor<VendorSurveyCfgEntity>{

	List<VendorSurveyCfgEntity> findByVendorPhasecfgId(Long id);

	VendorSurveyCfgEntity findBySurveyTemplateIdAndOrgId(Long id, Long orgId);

	List<VendorSurveyCfgEntity> findByPhaseIdAndOrgId(Long phaseId, Long orgId);

	List<VendorSurveyCfgEntity> findByPhaseSnLessThanEqualAndOrgId(Integer phaseSn, Long orgId);

	VendorSurveyCfgEntity findBySurveyCodeAndOrgId(String string, Long orgId);

	List<VendorSurveyCfgEntity> findByVendorPhasecfgIdOrderBySurveyTemplateIdAsc(Long id);

	List<VendorSurveyCfgEntity> findByOrgIdAndPhaseSnGreaterThanEqual(Long orgId, Integer phaseSn);
	
	List<VendorSurveyCfgEntity> findByOrgId(Long orgId);

	List<VendorSurveyCfgEntity> findByVendorPhasecfgIdOrderBySurveyTemplateSnAsc(long id);

	@Query(value="from VendorSurveyCfgEntity where vendorPhasecfgId = ?1 order by surveyTemplate.sn asc")
	List<VendorSurveyCfgEntity> findByVendorPhasecfgIdOrderBySnAsc(long id);
	
	@Query(value="select bs.* from QEWEB_VENDOR_SURVEY_CFG bs where bs.ID in(select max(id) from (select bb.* from QEWEB_VENDOR_SURVEY_CFG bb where bb.org_id IN(select c.org_id from(select a.ORG_ID,b.id from (select DISTINCT org_id from QEWEB_VENDOR_SURVEY_CFG) a LEFT JOIN QEWEB_VENDOR_SURVEY_CFG b on a.ORG_ID = b.ORG_ID and b.SURVEY_TEMPLATE_ID=?1) c where c.id is null)) GROUP BY ORG_ID)",nativeQuery=true)
	List<VendorSurveyCfgEntity> selectOrgSurveyCfgList(Long suid);

}
