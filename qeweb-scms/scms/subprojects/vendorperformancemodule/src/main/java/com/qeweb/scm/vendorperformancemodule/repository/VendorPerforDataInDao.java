package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInEntity;

/**
 * 数据导入设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforDataInDao extends BaseRepository<VendorPerforDataInEntity, Serializable>,JpaSpecificationExecutor<VendorPerforDataInEntity>{

	VendorPerforDataInEntity findByCycleIdAndElementAndAssessDateBetween(long id, String key, Timestamp startTime, Timestamp endTime);
	
	VendorPerforDataInEntity findByCycleIdAndIndexNameAndVendorCodeAndMaterialCode(long id,String iName,String vendorCode,String materialCode);
	
	@Query(value="SELECT VENDOR_CODE,CYCLE_ID,MATERIAL_CODE,INDEX_NAME,FACTORY_NAME,BELONG_DATE,BRAND_NAME,ASSESS_DATE,VENDOR_NAME,MATERIAL_NAME FROM QEWEB_ASSESS_DATAIN WHERE ASSESS_DATE=?1  GROUP BY CYCLE_ID,MATERIAL_CODE,INDEX_NAME,VENDOR_CODE,BELONG_DATE,FACTORY_NAME,BRAND_NAME,ASSESS_DATE,VENDOR_NAME,MATERIAL_NAME",nativeQuery=true)
	List<Object[]> getVendorPerforDataInEntity(Timestamp timestamp);

	VendorPerforDataInEntity findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndElementAndAssessDate(long id,String iName,String vendorCode,String materialCode,String brandName,String factoryName,Timestamp belongDate,String element,Timestamp assessDate);
	
	List<VendorPerforDataInEntity> findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndAssessDate(long id,String iName,String vendorCode,String materialCode,String brandName,String factoryName,Timestamp belongDate,Timestamp assessDate);

	VendorPerforDataInEntity findByCycleIdAndIndexNameAndVendorCode(Long cycleId, String name, String orgCode);

	VendorPerforDataInEntity findByElementAndCycleIdAndIndexNameAndVendorCode(String element, Long cycleId,
			String name, String orgCode);
	
	@Query(value="from VendorPerforDataInEntity where element=?1 and cycleId=?2 and indexName=?3 and vendorCode=?4 and assessDate>=?5 and assessDate<=?6")
	VendorPerforDataInEntity getElementAndCycleIdAndIndexNameAndVendorCode(String element, Long cycleId,
			String name, String orgCode,Timestamp startTime, Timestamp endTime);

	@Query(value="from VendorPerforDataInEntity where element=?1 and cycleId=?2 and indexName=?3 and vendorCode=?4 and materialCode=?5 and assessDate>?6 and assessDate<=?7")
	VendorPerforDataInEntity getElementAndCycleIdAndIndexNameAndVendorCodeAndMaterialCode(String element,Long cycleId, String name, String orgCode, String materialTypeCode,Timestamp startTime, Timestamp endTime);

	VendorPerforDataInEntity findByCycleIdAndOrgIdAndElementAndAssessDateBetween(
			Long cycleId, Long orgId, String name, Timestamp timestamp,
			Timestamp timestamp2);

}
