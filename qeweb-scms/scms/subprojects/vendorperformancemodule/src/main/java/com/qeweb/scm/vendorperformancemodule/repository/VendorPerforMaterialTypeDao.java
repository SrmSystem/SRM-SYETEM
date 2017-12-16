package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMaterialTypeEntity;

/**
 * 物料类别管理Dao
 * @author sxl
 * @date 2015年8月10日
 */
public interface VendorPerforMaterialTypeDao extends BaseRepository<VendorPerforMaterialTypeEntity, Serializable>,JpaSpecificationExecutor<VendorPerforMaterialTypeEntity>{


}
