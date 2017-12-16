package com.qeweb.scm.vendorperformancemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMaterialTypeEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforVendorMatEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforMaterialTypeDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforVendorMatDao;

@Service
@Transactional
public class VendorPerforVendorMatService {
	
	@Autowired
	private VendorPerforVendorMatDao vendorMatDao;

	public Page<VendorPerforVendorMatEntity> getPage(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforVendorMatEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforVendorMatEntity.class);
		Page<VendorPerforVendorMatEntity> page = vendorMatDao.findAll(spec,pagin);
		return page;
	}

	public Map<String, Object> addUpdateVendorMat(VendorPerforVendorMatEntity vendorMatEntity) {
		Map<String, Object>  map=new HashMap<String, Object>();
		vendorMatDao.save(vendorMatEntity);
		map.put("success",true);
		map.put("msg", "执行成功！！");
		return map;
	}
	public String releaseVendorMat(List<VendorPerforVendorMatEntity> vendorMatEntitys) {
		for(VendorPerforVendorMatEntity vendorMatEntity:vendorMatEntitys)
		{
			vendorMatEntity.setAbolished(0);
			vendorMatDao.save(vendorMatEntity);
		}
		return "1";
	}

	public String delsVendorMat(List<VendorPerforVendorMatEntity> vendorMatEntitys) {
		for(VendorPerforVendorMatEntity vendorMatEntity:vendorMatEntitys)
		{
			vendorMatDao.abolish(vendorMatEntity.getId());
		}
		return "1";
	}
}
