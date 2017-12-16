package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;

/**
 * 模版设置Dao
 * @author pjjxiajun
 * @date 2015年8月15日
 * @path com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateSettingDao.java
 */
public interface VendorPerforTemplateSettingDao extends BaseRepository<VendorPerforTemplateSettingEntity, Serializable>,JpaSpecificationExecutor<VendorPerforTemplateSettingEntity>{

	List<VendorPerforTemplateSettingEntity> findByTemplateId(Long templateId);
	
	@Query("from VendorPerforTemplateSettingEntity where sourceId = ?1 and template.cycleId = ?2")
	List<VendorPerforTemplateSettingEntity> findBySourceIdAndCycleId(Long sourceId, Long cycleId);

	@Modifying
	@Query("delete VendorPerforTemplateSettingEntity where templateId=?1")
	void deleteByTemplateId(long id);

	List<VendorPerforTemplateSettingEntity> findByTemplateIdAndEnableStatus(long id, int statusYes);

	List<VendorPerforTemplateSettingEntity> findByTemplateIdAndEnableStatusAndSourceType(Long id, int statusYes,
			Integer perTemplateSetDim);

	List<VendorPerforTemplateSettingEntity> findByTemplateIdAndEnableStatusAndSourceTypeAndParentIdIsNull(Long id,
			int statusYes, Integer perTemplateSetDim);

	List<VendorPerforTemplateSettingEntity> findByTemplateIdAndEnableStatusAndSourceTypeAndParentIdOrderBySourceIdAsc(
			long id, int statusYes,Integer sourceType ,Long dimId);

	VendorPerforTemplateSettingEntity findByTemplateIdAndSourceId(long id, Long indexId);

	@Query("select count(*) from VendorPerforTemplateSettingEntity where templateId=?1 and enableStatus=?2 and parentId=?3")
	Long findCountByTemplateIdAndEnableStatusAndParentId(long templateId, int statusYes, Long dimId);

	@Query("from VendorPerforTemplateSettingEntity where template.modelId = ?1 and template.abolished=0 and enableStatus = 1")
	List<VendorPerforTemplateSettingEntity> findSettingList(Long modelId);

	@Query("from VendorPerforTemplateSettingEntity where (sourceType=0 or sourceType=2) and parentId is null and enableStatus=1 and templateId=?1 order by id asc")
	List<VendorPerforTemplateSettingEntity> getByZhDimSetting(long templateId);
}
