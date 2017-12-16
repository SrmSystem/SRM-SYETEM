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
import com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity;
import com.qeweb.scm.basemodule.repository.CompanyOrganizationRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class CompanyOrganizationRelService {
	
	@Autowired
	private CompanyOrganizationRelDao companyOrganizationRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<CompanyOrganizationRelEntity> getCompanyOrganiztionRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CompanyOrganizationRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), CompanyOrganizationRelEntity.class);
		return companyOrganizationRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewCompanyOrganizationRel(CompanyOrganizationRelEntity companyOrganizationRel) {
		companyOrganizationRel.setAbolished(0);
		companyOrganizationRelDao.save(companyOrganizationRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<CompanyOrganizationRelEntity> findAll(){
		return companyOrganizationRelDao.findAll();
	}
	
	/**
     * 获取单个对应关系
     */
	public  CompanyOrganizationRelEntity getCompanyOrganizationRel(Long id) {
		return companyOrganizationRelDao.findOne(id);
	}
	
	/**
     * 是否已经建立关系
     */
	public CompanyOrganizationRelEntity findByCompanyIdAndOrganizationId(Long  companyId , Long organizationId){
		return companyOrganizationRelDao.findByCompanyIdAndOrganizationIdAndAbolished( companyId , organizationId,0);
	}
	
	
	/**
     * 删除对应关系
     */
	public void deleteCompanyOrganizationRel(List<CompanyOrganizationRelEntity> companyOrganizationRelList) {
		companyOrganizationRelDao.delete(companyOrganizationRelList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<CompanyOrganizationRelEntity> companyOrganizationRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(CompanyOrganizationRelEntity companyOrganizatioRel : companyOrganizationRelList){
			companyOrganizationRelDao.abolishCompanyOrg(companyOrganizatioRel.getId());
			result++;
		}
		if(result != companyOrganizationRelList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
