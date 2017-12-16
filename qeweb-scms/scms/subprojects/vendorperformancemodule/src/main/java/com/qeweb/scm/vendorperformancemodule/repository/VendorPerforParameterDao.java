package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforParameterEntity;

/**
 *参数设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforParameterDao extends BaseRepository<VendorPerforParameterEntity, Serializable>,JpaSpecificationExecutor<VendorPerforParameterEntity>{

	VendorPerforParameterEntity findByCycleIdAndBrandIdAndFactoryIdAndParameter(Long cycleId, Long brandId,
			Long factoryId, String param);

	@Query("select sum(parameterValue) from VendorPerforParameterEntity where brandId=?1 and cycleId=?2")
	Double getSumByBrandAndCycle(Long brandId, Long cycleId);

	VendorPerforParameterEntity findByCycleIdAndBrandIdAndFactoryIdAndParameterAndJoinStatus(Long cycleId,
			Long brandId, Long factoryId, String param, int statusYes);

	@Query("select sum(parameterValue) from VendorPerforParameterEntity where brandId=?1 and cycleId=?2 and joinStatus=?3")
	Double getSumByBrandAndCycleAndJoin(Long brandId, Long cycleId, int statusYes);

	List<VendorPerforParameterEntity> findByOrgIdAndCycleIdAndBrandIdAndFactoryId(Long orgId, Long cycleId, Long brandId, Long factoryId);

	VendorPerforParameterEntity findByOrgIdAndCycleIdAndBrandIdAndFactoryIdAndParameterAndJoinStatus(Long orgId,
			Long cycleId, Long brandId, Long factoryId, String param, int statusYes);

	@Query("select sum(parameterValue) from VendorPerforParameterEntity where orgId=?1 and brandId=?2 and cycleId=?3 and joinStatus=?4")
	Double getSumByOrgIdBrandAndCycleAndJoin(Long orgId, Long brandId, Long cycleId, int statusYes);

	List<VendorPerforParameterEntity> findByOrgIdAndCycleIdAndBrandIdAndFactoryIdAndParameterAndAssessDateAndJoinStatusAndAbolished(
			Long orgId, Long cycleId, Long brandId, Long factoryId,String parameter, Timestamp assessDate, Integer joinStatus,
			Integer abolished);

	List<VendorPerforParameterEntity> findByCycleIdAndJoinStatus(long id, int i);

}
