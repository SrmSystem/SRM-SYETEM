package com.qeweb.scm.basemodule.service;

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
import com.qeweb.scm.basemodule.entity.CompanyBussinessEntity;
import com.qeweb.scm.basemodule.repository.CompanyBussinessDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class CompanyBussinessService {
	
	@Autowired
	private CompanyBussinessDao companyBussinessDao;


	public Page<CompanyBussinessEntity> getCompanyBussinessList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CompanyBussinessEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), CompanyBussinessEntity.class);
		return companyBussinessDao.findAll(spec,pagin);
	}


	public void addNewCompanyBussiness(CompanyBussinessEntity companyBussiness) {
		companyBussinessDao.save(companyBussiness);
	}

	public CompanyBussinessEntity getCompanyBussiness(Long id) {
		return companyBussinessDao.findOne(id);
	}

	public void updateCompanyBussiness(CompanyBussinessEntity companyBussiness) {
		companyBussinessDao.save(companyBussiness);
	}

	public void deleteCompanyBussinessList(List<CompanyBussinessEntity> companyBussinessList) {
		companyBussinessDao.delete(companyBussinessList);
	}
	


}
