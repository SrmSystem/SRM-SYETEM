package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresDimEntity;

/**
 * 维度得分Dao
 * @author pjjxiajun
 * @date 2015年9月18日
 * @path com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresDimDao.java
 */
public interface VendorPerforScoresDimDao extends BaseRepository<VendorPerforScoresDimEntity, Serializable>,JpaSpecificationExecutor<VendorPerforScoresDimEntity>{

	public List<VendorPerforScoresDimEntity> findByScoresId(long id);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where orgId=?1 and scoresId=?2")
	public void deleteByOrg(Long orgId,Long scoresId);
	
	@Modifying
	@Query("delete VendorPerforScoresDimEntity where scoresId=?1")
	public void deleteByOrgx(Long scoresId);
	
	@Modifying
	@Query("delete VendorPerforScoresDimEntity where orgId=?1 and materialTypeId=?2 and scoresId=?3")
	public void deleteByOrgAndMaterial(Long orgId, Long materialTypeId, long id);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndDimParentId(long id, Long sourceId);

	@Query("select count(*) from VendorPerforScoresDimEntity where scoresId=?1")
	public Long findCountByScoresId(Long id);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgId(Long id, Long orgId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndDimParentIdIsNull(Long id, Long orgId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndDimParentIdIsNull(Long scoresId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndDimParentId(Long scoresId, Long orgId, long id);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndMaterialTypeIdAndDimParentIdIsNull(long id,
			Long materialTypeId, Long orgId);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where orgId=?1 and brandId=?2 and factoryId=?3 and materialTypeId=?4 and scoresId=?5")
	public void deleteByOrgAndBrandAndFactoryAndMatType(Long orgId, Long brandId, Long factoryId, Long materialTypeId,
			long id);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndBrandIdAndDimParentIdIsNull(long id, Long orgId,
			Long brandId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndBrandIdAndFactoryIdAndMaterialTypeIdAndDimParentIdIsNull(
			long id, Long orgId, Long brandId, Long factoryId, Long materialTypeId);


	@Query("select distinct brandId from VendorPerforScoresDimEntity where scoresId=?1 and materialTypeId is null and factoryId is null order by brandId asc")
	public List<Long> getBrandByScoreListId(Long scoreListId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndMaterialTypeIdIsNullAndFactoryIdIsNull(Long scoreListId);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndMaterialTypeIdIsNullAndFactoryIdIsNullAndName(
			Long scoreListId, String string);

	public VendorPerforScoresDimEntity findByScoresIdAndTemplateIdAndOrgIdAndBrandIdAndNameAndMaterialTypeIdIsNullAndFactoryIdIsNull(
			Long scoresId,Long templateId, Long orgId, Long brandId, String name);

	public List<VendorPerforScoresDimEntity> findByScoresIdAndOrgIdAndBrandIdAndFactoryIdIsNullAndMaterialTypeIdIsNullAndDimParentIdIsNull(
			long id, Long orgId, Long brandId);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where scoresId=?1")
	public void deleteByScoresId(long id);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where orgId=?1 and brandId=?2 and factoryId=?3 and materialTypeId is null and scoresId=?4")
	public void deleteByOrgAndBrandAndFactory(Long orgId, Long brandId, Long factoryId, long id);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where orgId=?1 and brandId=?2 and factoryId is null and materialTypeId is null and scoresId=?3")
	public void deleteByOrgAndBrand(Long orgId, Long brandId, long id);

	@Modifying
	@Query("delete VendorPerforScoresDimEntity where scoresId=?1 and (factoryId is null or materialTypeId is null)")
	public void deleteByScoreList(long id);

	public List<VendorPerforScoresDimEntity> findByOrgIdAndBrandIdAndScoresIdAndMaterialTypeIdIsNullAndParentIdIsNullOrderByTemplateSettingIdAsc(
			Long orgId, Long brandId, Long scoresId);
}
