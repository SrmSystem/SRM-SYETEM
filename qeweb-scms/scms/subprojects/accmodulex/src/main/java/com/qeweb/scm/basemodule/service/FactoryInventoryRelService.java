package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
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
import com.qeweb.scm.basemodule.entity.FactoryInventoryRelEntity;
import com.qeweb.scm.basemodule.repository.FactoryInventoryRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class FactoryInventoryRelService {
	
	@Autowired
	private FactoryInventoryRelDao factoryInventoryRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<FactoryInventoryRelEntity> getFactoryInventoryRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FactoryInventoryRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), FactoryInventoryRelEntity.class);
		return factoryInventoryRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewFactoryInventoryRel(FactoryInventoryRelEntity factoryInventoryRel) {
		factoryInventoryRel.setAbolished(0);
		factoryInventoryRelDao.save(factoryInventoryRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<FactoryInventoryRelEntity> findAll(){
		return factoryInventoryRelDao.findAll();
	}
	
	/**
     * 获取单个对应关系
     */
	public  FactoryInventoryRelEntity getFactoryInventoryRel(Long id) {
		return factoryInventoryRelDao.findOne(id);
	}
	
	
	/**
     * 是否已经建立关系
     */
	public FactoryInventoryRelEntity findByFactoryIdAndInventoryId(Long  factoryId , Long orgId){
		return factoryInventoryRelDao.findByFactoryIdAndInventoryIdAndAbolished( factoryId , orgId,0);
	}
	
	
	/**
     * 删除对应关系
     */
	public void deleteFactoryInventoryRel(List<FactoryInventoryRelEntity> factoryInventoryRelList) {
		factoryInventoryRelDao.delete(factoryInventoryRelList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<FactoryInventoryRelEntity> factoryInventoryRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(FactoryInventoryRelEntity factoryInventoryRel : factoryInventoryRelList){
			factoryInventoryRelDao.abolishFactoryInventory(factoryInventoryRel.getId());
			result++;
		}
		if(result != factoryInventoryRelList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
