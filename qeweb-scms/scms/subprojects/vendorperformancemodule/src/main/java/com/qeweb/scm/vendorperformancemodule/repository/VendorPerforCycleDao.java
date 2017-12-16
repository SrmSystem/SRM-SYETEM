package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;

/**
 * 周期设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforCycleDao extends BaseRepository<VendorPerforCycleEntity, Serializable>,JpaSpecificationExecutor<VendorPerforCycleEntity>{

	List<VendorPerforCycleEntity> findByAbolished(int statusNo);

	//add by zhangjiejun 2015.11.25 start
	/**
	 * 根据可用状态和周期名称查询周期集合
	 * @param 	abolished	可用状态
	 * @param 	cycleName	周期名称
	 * @return	周期集合
	 */
	List<VendorPerforCycleEntity> findByAbolishedAndCycleName(int abolished, String cycleName);
	//add by zhangjiejun 2015.11.25 end

	VendorPerforCycleEntity findByCode(String code);
	
}
