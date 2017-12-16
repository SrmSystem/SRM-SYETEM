package com.qeweb.scm.vendormodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;
import com.qeweb.scm.vendormodule.repository.VendorTemplatePhaseDao;

@Service
@Transactional
public class VendorTemplatePhaseService {
	
	@Autowired
	private VendorTemplatePhaseDao vendorTemplatePhaseDao;


	public Page<VendorTemplatePhaseEntity> getVendorTemplatePhaseList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorTemplatePhaseEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorTemplatePhaseEntity.class);
		return vendorTemplatePhaseDao.findAll(spec,pagin);
	}


	public void addNewVendorTemplatePhase(VendorTemplatePhaseEntity vendorTemplatePhase) {
		vendorTemplatePhaseDao.save(vendorTemplatePhase);
	}

	public VendorTemplatePhaseEntity getVendorTemplatePhase(Long id) {
		return vendorTemplatePhaseDao.findOne(id);
	}

	public void updateVendorTemplatePhase(VendorTemplatePhaseEntity vendorTemplatePhase) {
		vendorTemplatePhaseDao.save(vendorTemplatePhase);
	}

	public void deleteVendorTemplatePhaseList(List<VendorTemplatePhaseEntity> vendorTemplatePhaseList) {
		vendorTemplatePhaseDao.delete(vendorTemplatePhaseList);
	}

    /**
     * 获得所有的阶段
     * @return 阶段集合
     */
	public List<VendorTemplatePhaseEntity> getVendorTemplatePhaseListAll() {
		return (List<VendorTemplatePhaseEntity>) vendorTemplatePhaseDao.findAll();
	}


	/**
	 * 获得模版下的晋级阶段
	 * @param templateId 模版ID
	 * @return 晋级阶段集合
	 */
	public List<VendorTemplatePhaseEntity> getByTemplate(Long templateId) {
		return vendorTemplatePhaseDao.findByVendorNavTemplate(templateId);
	}


	

}
