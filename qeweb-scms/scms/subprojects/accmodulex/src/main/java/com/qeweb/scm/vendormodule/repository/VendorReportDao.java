package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorReportEntity;

public interface VendorReportDao  extends BaseRepository<VendorReportEntity, Serializable>,JpaSpecificationExecutor<VendorReportEntity>{
	

	List<VendorReportEntity> findByOrgid(Long orgid);
}
