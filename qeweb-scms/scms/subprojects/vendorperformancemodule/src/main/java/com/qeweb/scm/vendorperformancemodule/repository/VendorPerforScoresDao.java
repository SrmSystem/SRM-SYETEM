package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresEntity;

/**
 * 数据导入设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforScoresDao extends BaseRepository<VendorPerforScoresEntity, Serializable>,JpaSpecificationExecutor<VendorPerforScoresEntity>{


	VendorPerforScoresEntity findByCycleIdAndAssessEndDateIsNull(Long cycleId);

	VendorPerforScoresEntity findByCycleIdAndModelIdAndAssessEndDateIsNull(Long modelId, Long cycleId);

	/**
	 * 获取待计算记录
	 * @return
	 */
	@Query("from VendorPerforScoresEntity where countStatus <> 1 or countStatus is null")
	List<VendorPerforScoresEntity> findPendingCountList();

	VendorPerforScoresEntity findByCycleIdAndModelId(Long cycleId, Long modelId);

	VendorPerforScoresEntity findByCycleIdAndModelIdAndAssessStartDateBetween(Long cycleId, Long modelId,
			Timestamp countStartDate, Timestamp countEndDate);

	List<VendorPerforScoresEntity> findByCycleIdAndAssessEndDateIsNullOrderByIdDesc(long id);
}
