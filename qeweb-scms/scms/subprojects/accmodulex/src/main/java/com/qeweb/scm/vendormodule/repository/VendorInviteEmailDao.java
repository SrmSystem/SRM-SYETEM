package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;

/**
 * 邀请注册Dao
 * @author lw
 * @date 2015年7月1日13:12:58
 * @path com.qeweb.scm.vendormodule.repository.VendorInviteEmailDao.java
 */
public interface VendorInviteEmailDao extends BaseRepository<VendorInviteEmailEntity, Serializable>,JpaSpecificationExecutor<VendorInviteEmailEntity>{
	@Override
	public Iterable<VendorInviteEmailEntity> findAll();

	public List<VendorInviteEmailEntity> findByVendorName(String name);

	public List<VendorInviteEmailEntity> findByVendorNameAndVendorEmail(
			String vendorName, String vendorEmail);
}
