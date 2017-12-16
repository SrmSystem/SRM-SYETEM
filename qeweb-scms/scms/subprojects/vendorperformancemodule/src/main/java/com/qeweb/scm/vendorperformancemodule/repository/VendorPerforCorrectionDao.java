package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionEntity;

/**
 * 供应商整改Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforCorrectionDao extends BaseRepository<VendorPerforCorrectionEntity, Serializable>,JpaSpecificationExecutor<VendorPerforCorrectionEntity>{

	List<VendorPerforCorrectionEntity> findByVendorCode(String code);

	@Query(value="select distinct vendor_code from qeweb_assess_correction",nativeQuery=true)
	public List getDistinct();
}
