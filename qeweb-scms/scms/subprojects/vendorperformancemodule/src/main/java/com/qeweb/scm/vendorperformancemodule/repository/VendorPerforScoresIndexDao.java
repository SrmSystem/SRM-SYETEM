package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresIndexEntity;

/**
 * 数据导入设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforScoresIndexDao extends BaseRepository<VendorPerforScoresIndexEntity, Serializable>,JpaSpecificationExecutor<VendorPerforScoresIndexEntity>{

	public List<VendorPerforScoresIndexEntity> findByScoresId(long id);

	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where orgId=?1 and scoresId=?2")
	public void deleteByOrg(Long orgId,Long scoresId);
	
	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where scoresId=?1")
	public void deleteByOrgx(Long scoresId);
	
	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where orgId=?1 and materialTypeId=?2 and scoresId=?3")
	public void deleteByOrgAndMaterial(Long orgId, Long materialTypeId, long id);

	@Query("select count(*) from VendorPerforScoresIndexEntity where scoresId=?1")
	public Long findCountByScoresId(Long id);

	
	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimId(Long id, Long sourceId);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimIdAndOrgId(Long id, Long sourceId, Long orgId);

	public List<VendorPerforScoresIndexEntity> findByDimIdIn(List<Long> dimScoreIdList);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndOrgIdAndDimIdIn(Long scoresId, Long orgId,
			List<Long> dimScoreIdList);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimIdAndOrgIdAndMaterialTypeId(long id, Long sourceId,
			Long orgId, Long materialTypeId);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndOrgIdAndMaterialTypeIdAndDimIdIn(Long scoresId,
			Long orgId, Long materialTypeId, List<Long> dimScoreIdList);

	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where orgId=?1 and brandId=?2 and scoresId=?3")
	public void deleteByOrgAndBrand(Long orgId, Long brandId, long id);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimIdAndOrgIdAndBrandIdAndFactoryIdAndMaterialTypeId(
			long id, Long sourceId, Long orgId, Long brandId, Long factoryId, Long materialTypeId);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimIdAndBrandId(Long scoresId, Long dimId, Long brandId);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndDimIdAndBrandIdOrderByIndexIdAsc(Long scoresId,
			Long dimId, Long brandId);

	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where orgId=?1 and brandId=?2 and factoryId=?3 and materialTypeId=?4 and scoresId=?5")
	public void deleteByOrgAndBrandAndFactoryAndMatType(Long orgId, Long brandId, long factoryId, Long matTypeId, long id);

	public List<VendorPerforScoresIndexEntity> findByScoresIdAndOrgIdAndBrandIdAndDimIdIn(Long scoresId, Long orgId,
			Long brandId, List<Long> dimScoreIdList);

	@Modifying
	@Query("delete VendorPerforScoresIndexEntity where scoresId=?1")
	public void deleteByScoresId(long id);

	public VendorPerforScoresIndexEntity findByScoresIdAndTemplateIdAndDimIdAndIndexIdAndOrgId(long scoresId, Long templateId,
			long dimId, long indexId, Long orgId);
}
