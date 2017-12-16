package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorTemplateSurveyDao extends BaseRepository<VendorTemplateSurveyEntity, Serializable>,JpaSpecificationExecutor<VendorTemplateSurveyEntity>{

	@Query("from VendorTemplateSurveyEntity where vendorNavTemplate.id=:templateId")
	List<VendorTemplateSurveyEntity> findByVendorNavTemplate(@Param("templateId") Long templateId);

	@Modifying
	@Query("delete from VendorTemplateSurveyEntity where vendorNavTemplate.id=?1")
	void deleteByVendorNavTemplate(long id);

	/**
	 * 获取模版阶段下的模版调查表
	 * @param id 模版阶段ID
	 * @return 模版调查表
	 */
	List<VendorTemplateSurveyEntity> findByTemplatePhaseId(long id);

	/**
	 * 删除阶段下的调查表配置
	 * @param id
	 */
	@Modifying
	@Query("delete from VendorTemplateSurveyEntity where templatePhaseId=?1")
	void deleteByTPhaseId(long id);

	/**
	 * 获取阶段下调查表并按照ID升序
	 * @param id 阶段配置ID
	 * @return 模版调查表
	 */
	List<VendorTemplateSurveyEntity> findByTemplatePhaseIdOrderByIdAsc(long id);

	List<VendorTemplateSurveyEntity> findByTemplatePhaseIdAndPhaseCode(long id, String string);


}
