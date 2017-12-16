package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorSurveyBaseDao extends BaseRepository<VendorSurveyBaseEntity, Serializable>,JpaSpecificationExecutor<VendorSurveyBaseEntity>{

	VendorSurveyBaseEntity findByVendorCfgId(long id);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndCurrentVersionOrderByIdDesc(long id, int statusYes);

	List<VendorSurveyBaseEntity> findByVendorCfgIdOrderByVersionNODesc(long id);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndIdNot(Long cfgId, Long currentId);

	VendorSurveyBaseEntity findByVendorCfgIdAndCurrentVersionAndSubmitStatus(long id, int statusYes, int statusYes2);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndVersionNOLessThan(Long cfgId, Integer versionNO);

	VendorSurveyBaseEntity findByVendorCfgIdAndSubmitStatusAndAuditStatus(long vendorCfgId, int statusYes, int statusNo);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndCurrentVersion(long vendorCfgId,Integer CurrentVersion);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndVersionNOLessThanEqual(Long cfgId, Integer versionNO);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndVersionNOLessThanEqualOrderByVersionNODesc(Long cfgId,
			Integer versionNO);

	List<VendorSurveyBaseEntity> findByVendorCfgIdAndSubmitStatusOrderByVersionNODesc(Long cfgId, int statusYes);

	VendorSurveyBaseEntity findByTemplateCodeAndOrgIdAndCurrentVersion(String surveyCode,
			Long orgId,Integer currentVersion);

	List<VendorSurveyBaseEntity> findByVendorCfgIdOrderByVersionNODesc(Long surveyCfgId);

}
