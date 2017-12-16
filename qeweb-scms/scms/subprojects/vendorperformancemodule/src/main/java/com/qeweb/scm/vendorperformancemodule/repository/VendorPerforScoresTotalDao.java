package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity;

/**
 * 总得分Dao
 * @author pjjxiajun
 * @date 2015年9月20日
 * @path com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresTotalDao.java
 */
public interface VendorPerforScoresTotalDao extends BaseRepository<VendorPerforScoresTotalEntity, Serializable>,JpaSpecificationExecutor<VendorPerforScoresTotalEntity>{

	public List<VendorPerforScoresTotalEntity> findByScoresId(long id);

	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where orgId=?1 and scoresId=?2")
	public void deleteByOrg(Long orgId,Long scoresId);
	
	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where scoresId=?1")
	public void deleteByOrgx(Long scoresId);

	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where orgId=?1 and materialTypeId=?2 and scoresId=?3")
	public void deleteByOrgAndMaterial(Long orgId, Long materialTypeId, long id);

	@Query("select count(*) from VendorPerforScoresTotalEntity where scoresId=?1")
	public Long findCountByScoresId(Long id);

	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where orgId=?1 and brandId=?2 and scoresId=?3")
	public void deleteByOrgAndBrand(Long orgId, Long brandId, long id);
	
	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where scoresId=?1")
	public void deleteByOrgScoresTotal(long id);

	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where orgId=?1 and brandId=?2 and factoryId=?3 and materialTypeId=?4 and scoresId=?5")
	public void deleteByOrgAndBrandAndFactoryAndMaterialType(Long orgId, Long brandId, Long factoryId,
			Long materialTypeId, long id);

	public List<VendorPerforScoresTotalEntity> findByScoresIdAndMaterialTypeIdIsNull(long id);

	@Modifying
	@Query("delete VendorPerforScoresTotalEntity where scoresId=?1")
	public void deleteByScoresId(long id);

	public List<VendorPerforScoresTotalEntity> findByOrgIdAndBrandIdAndTemplateIdAndMaterialTypeIdIsNullAndPerformanceDateBetween(
			Long orgId, Long brandId, Long templateId, Timestamp startTime, Timestamp endTime);

	public List<VendorPerforScoresTotalEntity> findByOrgIdAndBrandIdAndTemplateIdAndYearAndMaterialTypeIdIsNull(
			Long orgId, Long brandId, Long templateId, Integer year);

	public List<VendorPerforScoresTotalEntity> findByCorrectionStatusAndLevelNameIn(int i, List<String> list);

	@Query(value="select distinct o.code from qeweb_assess_scores_total oi left join QEWEB_ORGANIZATION o  on o.id=oi.org_id",nativeQuery=true)
	public List getDistinct();
}
