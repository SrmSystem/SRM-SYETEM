package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoExEntity;

/**
 * 供应商基本信息-扩展信息的DAO
 * @author pjjxiajun
 * @date 2015年6月24日
 * @path com.qeweb.scm.vendormodule.repository.VendorBaseInfoExDao.java
 */
public interface VendorBaseInfoExDao extends BaseRepository<VendorBaseInfoExEntity, Serializable>,JpaSpecificationExecutor<VendorBaseInfoExEntity>{

	List<VendorBaseInfoExEntity> findByVendorId(Long id);

	

}
