package com.qeweb.scm.contractmodule.service;
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
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;
import com.qeweb.scm.contractmodule.repository.ContractModuleDao;
@Service
@Transactional(rollbackOn=Exception.class)
public class ContractModuleService extends BaseService{
	
	@Autowired
	private ContractModuleDao contractModuleDao;
	
	private final static String CODE_KEY = "CONTMO";    //模板编号流水号的开头 CONTMO = contrastModule
	
	public ContractModuleEntity findOne(Long id) {
		return contractModuleDao.findOne(id);
	}
	
	/**
	 * 条件查询所有的合同模板
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<ContractModuleEntity> getList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ContractModuleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ContractModuleEntity.class);
		Page<ContractModuleEntity> page = contractModuleDao.findAll(spec, pagin);
		return page;
	}
	
	/**
	 * 保存模板
	 * @param moduleList
	 * @return
	 */
	public Map<String,Object> saveModule(List<ContractModuleEntity> moduleList){
		Map<String,Object> map = new HashMap<String, Object>();
		contractModuleDao.save(moduleList);
		map.put("msg", "保存成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除模板
	 * @param moduleList
	 * @return
	 */
	public Map<String,Object> deleteModule(List<ContractModuleEntity> moduleList){
		Map<String,Object> map = new HashMap<String, Object>();
		contractModuleDao.delete(moduleList);
		map.put("msg", "删除成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 创建模板的编号
	 */
	public String createModuleCode(){
		String moduleCode = getSerialNumberService().geneterNextNumberByKey(CODE_KEY);
		return moduleCode;
	}
}
