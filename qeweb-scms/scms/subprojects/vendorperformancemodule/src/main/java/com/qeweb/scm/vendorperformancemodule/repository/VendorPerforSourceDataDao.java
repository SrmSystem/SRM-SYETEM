package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforSourceDataEntity;

/**
 * 原始数据Dao
 * @author pjjxiajun
 * @date 2015年10月28日
 * @path com.qeweb.scm.vendorperformancemodule.repository.VendorPerforSourceDataDao.java
 */
public interface VendorPerforSourceDataDao extends BaseRepository<VendorPerforSourceDataEntity, Serializable>,JpaSpecificationExecutor<VendorPerforSourceDataEntity>{


	List<VendorPerforSourceDataEntity> findByPerformanceModelIdAndKeyNameAndIndexNameAndOrgIdAndBrandIdAndFactoryIdAndMatTypeIdAndAssessTimeBetween(
			Long modelId, String key, String name, Long orgId, Long brandId, Long factoryId, Long materialTypeId,
			Timestamp startTime, Timestamp endTime);
	
	List<VendorPerforSourceDataEntity> findByPerformanceModelIdAndKeyNameAndIndexNameAndOrgIdAndBrandIdAndAssessTimeBetween(
			Long modelId, String key, String name, Long orgId, Long brandId,
			Timestamp startTime, Timestamp endTime);

	List<VendorPerforSourceDataEntity> findByPerformanceModelIdAndKeyNameAndIndexNameAndOrgIdAndAssessTimeBetween(Long modelId, String key, String name, Long orgId,Timestamp startTime, Timestamp endTime);

	

}
