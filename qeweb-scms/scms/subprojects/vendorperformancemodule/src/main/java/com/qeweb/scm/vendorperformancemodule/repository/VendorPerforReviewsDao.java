package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforReviewsEntity;

/**
 * 等级设置Dao
 * @author sxl
 * @date 2015年8月10日
 */
public interface VendorPerforReviewsDao extends BaseRepository<VendorPerforReviewsEntity, Serializable>,JpaSpecificationExecutor<VendorPerforReviewsEntity>{

	VendorPerforReviewsEntity findByVendorIdAndCycleId(long id,long cid);
	
	List<VendorPerforReviewsEntity> findByModelIdAndOrgIdAndCycleId(long modelId, Long orgId, long cycleId);

	/**
	 * 获得该周期内参评供应商数量
	 * @param cycleId 周期ID
	 * @param abolished 废除标记
	 * @return 参评供应商数量
	 */
	@Query(value="select count(*) from VendorPerforReviewsEntity where cycleId=?1 and joinStatus=?2")
	Integer getVendorCount(Long cycleId,Integer joinStatus);

	List<VendorPerforReviewsEntity> findByCycleIdAndJoinStatus(Long cycleId, int statusYes);
	
	
	VendorPerforReviewsEntity findByCycleIdAndOrgId(Long cycleId, Long orgId);

	VendorPerforReviewsEntity findByVendorIdAndCycleIdAndModelId(long id, long parseLong, Long modelId);

	@Query(value="select count(*) from VendorPerforReviewsEntity where cycleId=?1 and modelId=?2 and joinStatus=?3")
	Integer getVendorModelCount(Long cycleId, Long modelId, int statusYes);

	@Query(value="select distinct t.orgId from VendorPerforReviewsEntity t where t.joinStatus=?1")
	List<Long> getVendorIdList(int statusYes);

	List<VendorPerforReviewsEntity> findByCycleIdAndModelIdAndJoinStatus(Long cycleId, Long modelId, int statusYes);
	

}
