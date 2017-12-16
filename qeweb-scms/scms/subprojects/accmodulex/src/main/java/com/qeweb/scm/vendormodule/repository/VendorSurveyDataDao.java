package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorSurveyDataDao extends BaseRepository<VendorSurveyDataEntity, Serializable>,JpaSpecificationExecutor<VendorSurveyDataEntity>{

	List<VendorSurveyDataEntity> findByOrgIdAndTemplateIdOrderByLastUpdateTimeDesc(Long orgId, Long surveyTemplateId);

	@Modifying
	@Query("delete from VendorSurveyDataEntity where baseId=?1")
	void deleteByBaseId(long baseId);

	List<VendorSurveyDataEntity> findByBaseIdOrderByLastUpdateTimeDesc(Long orgId);

	List<VendorSurveyDataEntity> findByBaseIdAndCtId(long id, String string);
	
	@Override
	public List<VendorSurveyDataEntity> findAll(
			Specification<VendorSurveyDataEntity> spec);

	VendorSurveyDataEntity findByBaseIdAndCtIdAndCol1(long id,String string,String col1);

	List<VendorSurveyDataEntity> findByCtIdAndOrgIdAndCol1(String string,Long orgid, String string2);


}