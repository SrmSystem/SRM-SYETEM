package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforPurchasedatainEntity;

/**
 * 数据导入设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforPurchasedatainDao extends BaseRepository<VendorPerforPurchasedatainEntity, Serializable>,JpaSpecificationExecutor<VendorPerforPurchasedatainEntity>{

	List<VendorPerforPurchasedatainEntity> findByVendorCodeAndBrandIdAndVendorDate(
			String vendorCode, Long brandId, String vendorDate);


}
