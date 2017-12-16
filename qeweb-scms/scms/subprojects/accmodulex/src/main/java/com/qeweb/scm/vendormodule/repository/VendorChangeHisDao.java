package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorChangeHisEntity;

/**
 * 供应商变更历史Dao
 * @author pjjxiajun
 * @date 2015年7月9日
 * @path com.qeweb.scm.vendormodule.repository.VendorChangeHisDao.java
 */
public interface VendorChangeHisDao extends BaseRepository<VendorChangeHisEntity, Serializable>,JpaSpecificationExecutor<VendorChangeHisEntity>{


}
