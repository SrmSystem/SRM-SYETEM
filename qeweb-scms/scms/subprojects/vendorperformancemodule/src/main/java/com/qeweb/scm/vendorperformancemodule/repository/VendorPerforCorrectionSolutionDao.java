package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionSolutionEntity;

/**
 * 供应商方案Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforCorrectionSolutionDao extends BaseRepository<VendorPerforCorrectionSolutionEntity, Serializable>,JpaSpecificationExecutor<VendorPerforCorrectionSolutionEntity>{

	VendorPerforCorrectionSolutionEntity findByCorrectionIdAndCurrentVersion(long id, int i);
	
}
