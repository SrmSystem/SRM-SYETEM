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
import com.qeweb.scm.purchasemodule.entity.BuyerBoardEntity;
import com.qeweb.scm.purchasemodule.repository.BuyerBoardDao;

/**
 * 采购员看板
 * @author haiming.huang
 *
 */
@Service
@Transactional
public class BuyerBoardService {
	
	@Autowired
	BuyerBoardDao buyerBoardDao;
	

	public Page<BuyerBoardEntity> getBuyerBoardItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap, Sort sort) {
		
		PageRequest pagin =  new PageRequest(pageNumber-1, pageSize,sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerBoardEntity.class);
		Page<BuyerBoardEntity> page = buyerBoardDao.findAll(spec, pagin);
		return page;
	}

	public List<BuyerBoardEntity> getfindAll(Map<String, Object> searchParamMap, Sort sort) {
		
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerBoardEntity.class);
		List<BuyerBoardEntity> list = buyerBoardDao.findAll(spec,sort);
		return list;
	}
	
	public BuyerBoardEntity getFindOne(Map<String, Object> searchParamMap) {
		
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BuyerBoardEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BuyerBoardEntity.class);
		List<BuyerBoardEntity> list = buyerBoardDao.findAll(spec);
		return list.get(0);
	}


	public List<BuyerBoardEntity> getListDtl(String materialCode,String factoryCode) {
		
		return buyerBoardDao.getListDtl(materialCode,factoryCode);
	}
	
	
}