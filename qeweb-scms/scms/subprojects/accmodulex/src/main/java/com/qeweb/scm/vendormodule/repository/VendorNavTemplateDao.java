package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorNavTemplateDao extends BaseRepository<VendorNavTemplateEntity, Serializable>,JpaSpecificationExecutor<VendorNavTemplateEntity>{


	/**
	 * 获得有效的默认导航模版
	 * @param defaultFlag 默认标记
	 * @param abolished 废除标记
	 * @return 有效导航模版
	 */
	VendorNavTemplateEntity findByDefaultFlagAndAbolished(int defaultFlag, int abolished);

	VendorNavTemplateEntity findByCode(String code, int statusNo);

	List<VendorNavTemplateEntity> findByCode(String code);

	List<VendorNavTemplateEntity> findByCodeAndIdNot(String code, long id);
	
	/**
	 * 获取采购组织的默认导航模版
	 * @param defaultFlag
	 * @param abolished
	 * @return
	 */
	VendorNavTemplateEntity findByDefaultFlagAndAbolishedAndOrgId(int defaultFlag, int abolished, Long orgId);

}
