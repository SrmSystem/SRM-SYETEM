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
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.repository.CompanyFactoryRelDao;
import com.qeweb.scm.basemodule.repository.CompanyOrganizationRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class CompanyService {
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private CompanyFactoryRelDao companyFactoryRelDao;
	
	@Autowired
	private CompanyOrganizationRelDao companyOrganizationRelDao;
	
	
    /**
     * 获取公司的列表
     */
	public Page<CompanyEntity> getCompanyList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CompanyEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), CompanyEntity.class);
		return companyDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增公司的信息
     */
	public void addNewCompany(CompanyEntity companyEntity) {
		companyDao.save(companyEntity);
	}
	
	/**
     * 查詢公司的信息
     */
	public List<CompanyEntity> findAll(){
		return companyDao.findAll();
	}
	
	/**
     * 获取单个公司的信息
     */
	public  CompanyEntity getCompanyEntity(Long id) {
		return companyDao.findOne(id);
	}
	
	/**
     * 更新公司
     */
	
	public void updateCompany(CompanyEntity companyEntity) {
		companyDao.save(companyEntity);
	}
	
	/**
     * 删除公司
     */
	public void deleteCompanyList(List<CompanyEntity> companyList) {
		//companyDao.delete(companyList);
		for (CompanyEntity companyEntity : companyList) {
			companyDao.abolish(companyEntity.getId());
		}
		
	}
	
	/**
     *作废公司
     */
	public Map<String, Object> abolishBatch(List<CompanyEntity> companyEntityList) {
		Map<String,Object> map = new HashMap<String, Object>();
		String msg = "";
		Boolean flag = true;
		
		//验证公司是否绑定工厂和采购组织
		for(CompanyEntity companyEntity : companyEntityList){
			List<CompanyFactoryRelEntity> cfList = companyFactoryRelDao.findByCompanyIdAndAbolished(companyEntity.getId(), 0) ;
			List<CompanyOrganizationRelEntity> coList =companyOrganizationRelDao.findByCompanyIdAndAbolished(companyEntity.getId(), 0); 
			if(cfList  != null  && cfList.size() != 0){
				for(CompanyFactoryRelEntity cf : cfList){
					msg = msg + " 公司："+cf.getCompany().getName()+"已于工厂："+cf.getFactory().getName()+"绑定,无法废除！\r\n";
				}
				flag = false;
			}
           if(coList  != null  && coList.size() != 0 ){
        	   for(CompanyOrganizationRelEntity  co : coList){
        		   msg = msg + " 公司："+co.getCompany().getName()+"已于采购组织："+co.getOrganizationEntity().getName()+"绑定,无法废除！\r\n";
        	   }
        
        		flag = false;
           }
		}
		
		if(!flag){
			map.put("msg", msg);
			map.put("success", false);
		}else{
			//废除
			for(CompanyEntity companyEntity : companyEntityList){
				companyDao.abolish(companyEntity.getId());
			}
			map.put("success", true);
		}
		return map;
	}
	
	public CompanyEntity getCompanyByCode(String code){
		return companyDao.findByCode(code);
	}
	
	
	public List<CompanyEntity> findEffective(){
		return companyDao.findByAbolished(0);
	}
	
	
}
