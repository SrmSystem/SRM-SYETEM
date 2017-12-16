package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorBUEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorBUDao extends BaseRepository<VendorBUEntity, Serializable>,JpaSpecificationExecutor<VendorBUEntity>{

	VendorBUEntity findByCodes(String mainBU);


}
