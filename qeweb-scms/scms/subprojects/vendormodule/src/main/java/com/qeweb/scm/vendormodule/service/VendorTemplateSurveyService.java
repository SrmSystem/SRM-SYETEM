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
import com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity;
import com.qeweb.scm.vendormodule.repository.VendorTemplateSurveyDao;

@Service
@Transactional
public class VendorTemplateSurveyService {
	
	@Autowired
	private VendorTemplateSurveyDao vendorTemplateSurveyDao;


	public Page<VendorTemplateSurveyEntity> getVendorTemplateSurveyList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorTemplateSurveyEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorTemplateSurveyEntity.class);
		return vendorTemplateSurveyDao.findAll(spec,pagin);
	}


	public void addNewVendorTemplateSurvey(VendorTemplateSurveyEntity vendorTemplateSurvey) {
		vendorTemplateSurveyDao.save(vendorTemplateSurvey);
	}

	public VendorTemplateSurveyEntity getVendorTemplateSurvey(Long id) {
		return vendorTemplateSurveyDao.findOne(id);
	}

	public void updateVendorTemplateSurvey(VendorTemplateSurveyEntity vendorTemplateSurvey) {
		vendorTemplateSurveyDao.save(vendorTemplateSurvey);
	}

	public void deleteVendorTemplateSurveyList(List<VendorTemplateSurveyEntity> vendorTemplateSurveyList) {
		vendorTemplateSurveyDao.delete(vendorTemplateSurveyList);
	}

    /**
     * 获得所有的阶段
     * @return 阶段集合
     */
	public List<VendorTemplateSurveyEntity> getVendorTemplateSurveyListAll() {
		return (List<VendorTemplateSurveyEntity>) vendorTemplateSurveyDao.findAll();
	}


	/**
	 * 获得模版下的晋级阶段
	 * @param templateId 模版ID
	 * @return 晋级阶段集合
	 */
	public List<VendorTemplateSurveyEntity> getByTemplate(Long templateId) {
		return vendorTemplateSurveyDao.findByVendorNavTemplate(templateId);
	}


	

}
