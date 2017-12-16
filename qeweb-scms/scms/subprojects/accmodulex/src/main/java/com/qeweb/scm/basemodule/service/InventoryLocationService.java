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
import com.qeweb.scm.basemodule.entity.InventoryLocationEntity;
import com.qeweb.scm.basemodule.repository.FactoryInventoryRelDao;
import com.qeweb.scm.basemodule.repository.InventoryLocationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class InventoryLocationService {
	
	@Autowired
	private InventoryLocationDao inventoryLocationDao;
	
	@Autowired
	private FactoryInventoryRelDao factoryInventoryRelDao;
	
	
    /**
     * 获取库存位置的列表
     */
	public Page<InventoryLocationEntity> getInventoryLocationList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InventoryLocationEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), InventoryLocationEntity.class);
		return inventoryLocationDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增库存位置的信息
     */
	public void addNewInventoryLocation(InventoryLocationEntity inventoryLocation) {
		inventoryLocationDao.save(inventoryLocation);
	}
	
	/**
     * 查詢库存位置的信息
     */
	public List<InventoryLocationEntity> findAll(){
		return inventoryLocationDao.findAll();
	}
	
	/**
     * 获取单个库存位置的信息
     */
	public  InventoryLocationEntity getInventoryLocation(Long id) {
		return inventoryLocationDao.findOne(id);
	}
	
	/**
     * 更新公司
     */
	
	public void updateInventoryLocation(InventoryLocationEntity inventoryLocation) {
		inventoryLocationDao.save(inventoryLocation);
	}
	
	/**
     * 删除公司
     */
	public void deleteInventoryLocationList(List<InventoryLocationEntity> InventoryLocationList) {
		//inventoryLocationDao.delete(InventoryLocationList);
		for (InventoryLocationEntity inventoryLocationEntity : InventoryLocationList) {
			inventoryLocationDao.abolish(inventoryLocationEntity.getId());
		}
	}
	
	/**
     *作废库存位置的信息
     */
	public Map<String, Object> abolishBatch(List<InventoryLocationEntity> InventoryLocationList) {
		Map<String,Object> map = new HashMap<String, Object>();
		String msg = "";
		Boolean flag = true;
		
		for(InventoryLocationEntity inventoryLocation : InventoryLocationList){
			List<FactoryInventoryRelEntity> fiList = factoryInventoryRelDao.findByInventoryIdAndAbolished(inventoryLocation.getId(), 0);
			if(fiList   != null  && fiList.size() != 0){
				for(FactoryInventoryRelEntity fi : fiList){
					msg = msg + " 库存地点："+fi.getInventory().getName()+"已于工厂："+fi.getFactory().getName()+"绑定,无法废除！\r\n";
				}
				flag = false;
			}	
		}
		if(!flag){
			map.put("msg", msg);
			map.put("success", false);
		}else{
			//废除
			for(InventoryLocationEntity inventoryLocation : InventoryLocationList){
				inventoryLocationDao.abolish(inventoryLocation.getId());
			}
			map.put("success", true);
		}
		return map;
	}
	
	public InventoryLocationEntity getInventoryLocationByCode(String code){
		return inventoryLocationDao.findByCode(code);
	}
	
	public List<InventoryLocationEntity> findEffective(){
		return inventoryLocationDao.findByAbolished(0);
	}
	
}
