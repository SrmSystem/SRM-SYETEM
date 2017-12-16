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
import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.repository.CompanyFactoryRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class CompanyFactoryRelService {
	
	@Autowired
	private CompanyFactoryRelDao companyFactoryRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<CompanyFactoryRelEntity> getCompanyFactoryRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CompanyFactoryRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), CompanyFactoryRelEntity.class);
		return companyFactoryRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewCompanyFactoryRel(CompanyFactoryRelEntity companyFactoryRel) {
		companyFactoryRel.setAbolished(0);
		companyFactoryRelDao.save(companyFactoryRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<CompanyFactoryRelEntity> findAll(){
		return companyFactoryRelDao.findAll();
	}
	
	/**
     * 是否已经建立关系
     */
	public CompanyFactoryRelEntity findByCompanyIdAndFactoryId(Long  companyId , Long factoryId){
		return companyFactoryRelDao.findByCompanyIdAndFactoryIdAndAbolished(  companyId ,  factoryId,0);
	}
	
	
	/**
     * 获取单个对应关系
     */
	public  CompanyFactoryRelEntity getCompanyFactoryRelEntity(Long id) {
		return companyFactoryRelDao.findOne(id);
	}
	
	
	/**
     * 删除对应关系
     */
	public void deleteCompanyFactoryRel(List<CompanyFactoryRelEntity> companyFactoryRelList) {
		companyFactoryRelDao.delete(companyFactoryRelList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<CompanyFactoryRelEntity> companyFactoryRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(CompanyFactoryRelEntity companyFactoryRel : companyFactoryRelList){
			companyFactoryRelDao.abolishCompanyFactory(companyFactoryRel.getId());
			result++;
		}
		if(result != companyFactoryRelList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
