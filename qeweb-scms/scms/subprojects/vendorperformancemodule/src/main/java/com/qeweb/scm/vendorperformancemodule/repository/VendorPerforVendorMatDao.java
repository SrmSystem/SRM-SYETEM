package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforVendorMatEntity;

/**
 * @author sxl
 * @date 2015年8月10日
 */
public interface VendorPerforVendorMatDao extends BaseRepository<VendorPerforVendorMatEntity, Serializable>,JpaSpecificationExecutor<VendorPerforVendorMatEntity>{

	List<VendorPerforVendorMatEntity> findByReviewsId(long id);

	List<VendorPerforVendorMatEntity> findByMaterialtypeId(long id);


}
