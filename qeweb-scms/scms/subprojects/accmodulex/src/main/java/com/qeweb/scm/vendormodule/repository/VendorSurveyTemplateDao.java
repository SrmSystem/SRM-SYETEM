package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorSurveyTemplateEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorSurveyTemplateDao extends BaseRepository<VendorSurveyTemplateEntity, Serializable>,JpaSpecificationExecutor<VendorSurveyTemplateEntity>{

	List<VendorSurveyTemplateEntity> findByTemplateType(Integer typeXml);

	List<VendorSurveyTemplateEntity> findByTemplateTypeAndFileName(Integer typeXml, String fileName);

	VendorSurveyTemplateEntity findByCode(String code);

	VendorSurveyTemplateEntity findByCodeAndIdNotIn(String code, ArrayList<Long> newArrayList);
	
	@Query("from VendorSurveyTemplateEntity t where t.buyer.id in (?1) order by id ")
	List<VendorSurveyTemplateEntity> findByBuyerId(List<Long> buyerIds);


}
