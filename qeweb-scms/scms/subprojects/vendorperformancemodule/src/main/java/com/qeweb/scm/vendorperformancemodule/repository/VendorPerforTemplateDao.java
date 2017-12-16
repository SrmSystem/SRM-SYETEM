package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateEntity;

/**
 * 公式配置设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforTemplateDao extends BaseRepository<VendorPerforTemplateEntity, Serializable>,JpaSpecificationExecutor<VendorPerforTemplateEntity>{

	VendorPerforTemplateEntity findByCode(String code);

	List<VendorPerforTemplateEntity> findByDefaulted(Integer defaulted);
	
	@Query("from VendorPerforTemplateEntity where defaulted = ?1 and cycleId = ?2 and templateType = ?3 and modelId = ?4 and abolished = 0")
	List<VendorPerforTemplateEntity> findByDefaulted(Integer defaulted, long cycleId, Integer templateType, Long modelId);

	@Query("from VendorPerforTemplateEntity where cycleId = ?1 and abolished = 0 order by createTime desc")
	List<VendorPerforTemplateEntity> findByCycleIdOrderByCreateTimeDesc(Long cycleId);

	List<VendorPerforTemplateEntity> findByTemplateTypeOrTemplateTypeIsNull(Integer templateTypeCommon);

	List<VendorPerforTemplateEntity> findByModelId(Long id);

	@Query(value="select id from VendorPerforTemplateEntity where modelId=?1 and finishStatus=1")
	List<Long> findIdByModelId(Long modelId);
	
	@Query(value="select id from VendorPerforTemplateEntity where modelId=?1 and abolished=0")
	List<Long> findIdByModId(Long modelId);

	List<VendorPerforTemplateEntity> findByTemplateTypeOrTemplateTypeIsNullAndAbolished(Integer templateTypeCommon,
			int statusNo);

	@Query("from VendorPerforTemplateEntity where 1=1 and (templateType=?1 or templateType is null) and abolished=?2")
	List<VendorPerforTemplateEntity> getEnableTemplateList(Integer templateTypeCommon, int statusNo);

	@Query("select mappingScore from VendorPerforTemplateEntity where abolished=?1 and id!=?2")
	List<String> findMappingScores(int statusNo, Long id);

	VendorPerforTemplateEntity findByTemplateTypeAndAbolished(Integer templateTypeSpuer, int statusNo);

}
