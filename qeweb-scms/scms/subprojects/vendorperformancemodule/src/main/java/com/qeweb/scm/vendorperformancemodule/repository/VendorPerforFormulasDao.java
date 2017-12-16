package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;

/**
 * 公式配置设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforFormulasDao extends BaseRepository<VendorPerforFormulasEntity, Serializable>,JpaSpecificationExecutor<VendorPerforFormulasEntity>{

	List<VendorPerforFormulasEntity> findByIndexId(long id);
	
	VendorPerforFormulasEntity findByIndexIdAndCycleId(long id,long cid);

}
