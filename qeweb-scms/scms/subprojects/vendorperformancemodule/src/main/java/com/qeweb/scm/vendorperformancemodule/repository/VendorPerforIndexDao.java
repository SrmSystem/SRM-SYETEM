package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;

/**
 * 公式配置设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforIndexDao extends BaseRepository<VendorPerforIndexEntity, Serializable>,JpaSpecificationExecutor<VendorPerforIndexEntity>{

	List<VendorPerforIndexEntity> findByDimensionsId(long id);

	List<VendorPerforIndexEntity> findByIndexIdAndAbolished(long id, int statusNo);

	VendorPerforIndexEntity findByIndexName(String indexName);

	VendorPerforIndexEntity findByIndexNameAndIdNot(String indexName, long id);

	List<VendorPerforIndexEntity> findByDimensionsIdAndAbolished(long id, int i);

	List<VendorPerforIndexEntity> findByIndexNameAndDimensionsIdAndAbolished(String name,long id, int i);

	VendorPerforIndexEntity findByIndexNameAndAbolished(String string, int i);

}
