package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInDetailsEntity;

/**
 * 数据准备情况设置Dao
 * @author sxl
 * @date 2015年8月25日
 */
public interface VendorPerforDataInDetailsDao extends BaseRepository<VendorPerforDataInDetailsEntity, Serializable>,JpaSpecificationExecutor<VendorPerforDataInDetailsEntity>{

	VendorPerforDataInDetailsEntity findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndAssessDate(long id,String iName,String vendorCode,String materialCode,String brandName,String factoryName,Timestamp belongDate,Timestamp assessDate);
}
