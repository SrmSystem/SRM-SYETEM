package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFAllocationEntity;

/**
 * 公式配置设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforFAllocationDao extends BaseRepository<VendorPerforFAllocationEntity, Serializable>,JpaSpecificationExecutor<VendorPerforFAllocationEntity>{

	VendorPerforFAllocationEntity findByName(String element);

}
