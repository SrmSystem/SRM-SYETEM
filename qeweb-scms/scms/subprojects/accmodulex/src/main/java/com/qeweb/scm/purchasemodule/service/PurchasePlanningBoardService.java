package com.qeweb.scm.purchasemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanningBoardEntity;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanningBoardDao;

/**
 * 采购计划看板
 * @author haiming.huang
 *
 */
@Service
@Transactional
public class PurchasePlanningBoardService {
	
	@Autowired
	PurchasePlanningBoardDao purchasePlanningBoardDao;
	

	public Page<PurchasePlanningBoardEntity> getPurchasePlanningBoardItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap, Sort sort) {
		
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanningBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanningBoardEntity.class);
		Page<PurchasePlanningBoardEntity> page = purchasePlanningBoardDao.findAll(spec, pagin);
		return page;
	}


	public List<PurchasePlanningBoardEntity> getfindAll(Map<String, Object> searchParamMap, Sort sort) {
		
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanningBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanningBoardEntity.class);
		List<PurchasePlanningBoardEntity> list = purchasePlanningBoardDao.findAll(spec,sort);
		return list;
	}
	

	public PurchasePlanningBoardEntity getFindOne(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchasePlanningBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchasePlanningBoardEntity.class);
		List<PurchasePlanningBoardEntity> list = purchasePlanningBoardDao.findAll(spec);
		return list.get(0);
	}


	public List<PurchasePlanningBoardEntity> getListDtl(String materialCode,
			String factoryCode) {
		return purchasePlanningBoardDao.getListDtl(materialCode,factoryCode);
	}
	
}