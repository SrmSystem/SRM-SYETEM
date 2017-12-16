package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforPurchaseEntity;

/**
 * 采购额分界线Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforPurchaseDao extends BaseRepository<VendorPerforPurchaseEntity, Serializable>,JpaSpecificationExecutor<VendorPerforPurchaseEntity>{

	List<VendorPerforPurchaseEntity> findByCycleId(Long id);

	VendorPerforPurchaseEntity findByMaterialtypeIdAndCycleId(long id, long parseLong);
	
}
