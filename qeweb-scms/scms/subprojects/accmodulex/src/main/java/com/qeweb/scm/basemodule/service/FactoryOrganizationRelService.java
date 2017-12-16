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
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.repository.FactoryOrganizationRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class FactoryOrganizationRelService {
	
	@Autowired
	private FactoryOrganizationRelDao factoryOrganizationRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<FactoryOrganizationRelEntity> getFactoryOrganizationRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FactoryOrganizationRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), FactoryOrganizationRelEntity.class);
		return factoryOrganizationRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewFactoryOrganizationRel(FactoryOrganizationRelEntity factoryOrganizationRel) {
		factoryOrganizationRel.setAbolished(0);
		factoryOrganizationRelDao.save(factoryOrganizationRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<FactoryOrganizationRelEntity> findAll(){
		return factoryOrganizationRelDao.findAll();
	}
	
	/**
     * 获取单个对应关系
     */
	public  FactoryOrganizationRelEntity getFactoryOrganizationRel(Long id) {
		return factoryOrganizationRelDao.findOne(id);
	}
	
	/**
     * 是否已经建立关系
     */
	public FactoryOrganizationRelEntity findByFactoryIdAndOrgId(Long  factoryId , Long orgId){
		return factoryOrganizationRelDao.findByFactoryIdAndOrgIdAndAbolished( factoryId , orgId,0);
	}
	
	
	
	/**
     * 删除对应关系
     */
	public void deleteFactoryOrganizationRel(List<FactoryOrganizationRelEntity> factoryOrganizationRelList) {
		factoryOrganizationRelDao.delete(factoryOrganizationRelList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<FactoryOrganizationRelEntity> factoryOrganizationRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(FactoryOrganizationRelEntity factoryOrganizationRel : factoryOrganizationRelList){
			factoryOrganizationRelDao.abolishFactoryOrg(factoryOrganizationRel.getId());
			result++;
		}
		if(result != factoryOrganizationRelList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
