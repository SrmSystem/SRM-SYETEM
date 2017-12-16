package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMappedEntity;

/**
 * 公式配置设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforMappedDao extends BaseRepository<VendorPerforMappedEntity, Serializable>,JpaSpecificationExecutor<VendorPerforMappedEntity>{

	VendorPerforMappedEntity findByNameAndMappedName(String name,
			String mappedName);
	List<VendorPerforMappedEntity> findByMappedName(String key);
	
	//add by zhangjiejun 2015.12.03 start
	/**
	 * 根据描述查询集合（麦特达因专用，用来查询不同类型的SQL）
	 * @param 	describe	描述
	 * @return	字符映射集合
	 */
	Iterable<VendorPerforMappedEntity> findByDescribe(String describe);
	//add by zhangjiejun 2015.12.03 end
}
