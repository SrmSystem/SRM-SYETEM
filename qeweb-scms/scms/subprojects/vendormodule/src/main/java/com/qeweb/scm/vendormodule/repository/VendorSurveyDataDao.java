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
	
	@Query(value="select sd.col5,sd.col10,sd.col15,sd.col20,sd.col6,sd.col11,sd.col16,sd.col21,sd.col2,sd.col4,sd.col7,sd.col9,sd.col12,sd.col14,sd.col17,sd.col19 " +
			" from qeweb_vendor_survey_data sd where sd.ct_id = ?1 and sd.org_id = ?2"
			+ " and sd.col1 = ?3",nativeQuery=true)
	List<Object[]> findByCtIdAndOrgIdAndCol1BySql(String string,Long orgid, String string2);
	
	@Query(value="select sd.org_id,sd.ct_id,sd.col5,sd.col10,sd.col15,sd.col20 " +
			" from qeweb_vendor_survey_data sd left join qeweb_vendor_survey_base sb on sd.base_id = sb.id and sb.current_version =1  "
			+ " where sb.id is not null and sd.ct_id in ?1 and sd.col1 = ?2 "
			+ " order by sd.org_id,sd.ct_id",nativeQuery=true)
	List<Object[]> findByCtIdAndCol1(List<String> ctIdList,String string2);


}