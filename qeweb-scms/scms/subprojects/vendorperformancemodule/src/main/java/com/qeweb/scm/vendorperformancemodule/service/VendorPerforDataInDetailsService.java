package com.qeweb.scm.vendorperformancemodule.service;

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
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInDetailsEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDataInDetailsDao;

@Service
@Transactional
public class VendorPerforDataInDetailsService {
	
	@Autowired
	private VendorPerforDataInDetailsDao vendorPerforDataInDetailsDao;

	public Page<VendorPerforDataInDetailsEntity> getDataInDetailsList(int pageNumber, int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforDataInDetailsEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforDataInDetailsEntity.class);
		Page<VendorPerforDataInDetailsEntity>  page= vendorPerforDataInDetailsDao.findAll(spec,pagin);
		return page;
	}
	

}
