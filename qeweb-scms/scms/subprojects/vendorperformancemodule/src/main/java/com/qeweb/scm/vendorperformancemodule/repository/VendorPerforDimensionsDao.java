package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;

/**
 * 维度设置Dao
 * @author sxl
 * @date 2015年8月11日
 */
public interface VendorPerforDimensionsDao extends BaseRepository<VendorPerforDimensionsEntity, Serializable>,JpaSpecificationExecutor<VendorPerforDimensionsEntity>{

	List<VendorPerforDimensionsEntity> findByParentIdIsNull();

	List<VendorPerforDimensionsEntity> findByParentId(long id);
	
	List<VendorPerforDimensionsEntity> findByAbolishedAndIdIn(Integer abolished, List<Long> ids);

	List<VendorPerforDimensionsEntity> findByParentIdAndAbolished(long id, int statusNo);

	VendorPerforDimensionsEntity findByDimName(String name);

	VendorPerforDimensionsEntity findByCode(String code);

	@Query("select mappingScore from VendorPerforDimensionsEntity where abolished=?1")
	List<String> findMappingScores(int statusNo);

	VendorPerforDimensionsEntity findByMappingScore(String mapping);

	List<VendorPerforDimensionsEntity> findByParentIdIsNullAndAbolished(int statusNo);

	List<VendorPerforDimensionsEntity> findByAbolished(int statusNo);

	List<VendorPerforDimensionsEntity> findByAbolishedAndAndCode(int statusNo,
			String code);

	VendorPerforDimensionsEntity findByAbolishedAndDimName(int i, String string);

}
