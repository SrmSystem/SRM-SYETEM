package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforModelEntity;

/**
 * 绩效模型Dao
 * @author pjjxiajun
 * @date 2015年10月22日
 * @path com.qeweb.scm.vendorperformancemodule.repository.VendorPerforModelDao.java
 */
public interface VendorPerforModelDao extends BaseRepository<VendorPerforModelEntity, Serializable>,JpaSpecificationExecutor<VendorPerforModelEntity>{

	VendorPerforModelEntity findByName(String name);

	List<VendorPerforModelEntity> findByEnableStatus(Integer statusYes);

	@Query("from VendorPerforModelEntity where name=?1 and id <> ?2")
	VendorPerforModelEntity findByNameAndIdNot(String name, long id);

	List<VendorPerforModelEntity> findByEnableStatusAndCode(int statusYes,
			String code);

}
