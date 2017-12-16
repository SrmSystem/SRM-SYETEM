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
import com.qeweb.scm.basemodule.entity.ProductLineEntity;
import com.qeweb.scm.basemodule.repository.ProductLineDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class ProductLineService {
	
	@Autowired
	private ProductLineDao productLineDao;


	public Page<ProductLineEntity> getProductLineList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ProductLineEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ProductLineEntity.class);
		return productLineDao.findAll(spec,pagin);
	}


	public void addNewProductLine(ProductLineEntity productLine) {
		productLineDao.save(productLine);
	}

	public ProductLineEntity getProductLine(Long id) {
		return productLineDao.findOne(id);
	}

	public void updateProductLine(ProductLineEntity productLine) {
		productLineDao.save(productLine);
	}

	public void deleteProductLineList(List<ProductLineEntity> productLineList) {
		productLineDao.delete(productLineList);
	}
	


}
