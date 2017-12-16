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
import com.qeweb.scm.basemodule.entity.BrandEntity;
import com.qeweb.scm.basemodule.repository.BrandDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class BrandService {
	
	@Autowired
	private BrandDao brandDao;


	public Page<BrandEntity> getBrandList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BrandEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BrandEntity.class);
		return brandDao.findAll(spec,pagin);
	}


	public void addNewBrand(BrandEntity brand) {
		brandDao.save(brand);
	}

	public BrandEntity getBrand(Long id) {
		return brandDao.findOne(id);
	}

	public void updateBrand(BrandEntity brand) {
		brandDao.save(brand);
	}

	public void deleteBrandList(List<BrandEntity> brandList) {
		//brandDao.delete(brandList);
		for (BrandEntity brandEntity : brandList) {
			brandDao.abolish(brandEntity.getId());
		}
	}


	/**
	 * 获得所有品牌
	 * @return
	 */
	public List<BrandEntity> getBrandList() {
		return brandDao.findAll();
	}
	


}
