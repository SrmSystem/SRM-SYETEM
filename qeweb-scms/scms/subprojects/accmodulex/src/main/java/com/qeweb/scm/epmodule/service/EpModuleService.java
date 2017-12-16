package com.qeweb.scm.epmodule.service;

import java.util.Collection;
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
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpModuleItemEntity;
import com.qeweb.scm.epmodule.repository.EpModuleDao;
import com.qeweb.scm.epmodule.repository.EpModuleItemDao;

/**
 * 询比价模板service
 * @author ronnie
 *
 */
@Service
@Transactional
public class EpModuleService {

	@Autowired
	private EpModuleDao epmoduleDao;
	
	@Autowired
	private EpModuleItemDao epmoduleItemDao;
	
	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<EpModuleEntity> getEpModuleLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpModuleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpModuleEntity.class);
		return epmoduleDao.findAll(spec, pagin);
	}

	public void addEpModule(EpModuleEntity epmodule, List<EpModuleItemEntity> moduleItemList) {
		if(epmodule.getIsDefault()!=null&&epmodule.getIsDefault().intValue()==1){
			List<EpModuleEntity> res=epmoduleDao.findByIsDefault(1);
			if(res!=null){
				for (EpModuleEntity epModuleEntity : res) {
					if(epModuleEntity.getId()!=epmodule.getId()){
						epModuleEntity.setIsDefault(0);
						epmoduleDao.save(epModuleEntity);
					}
				}
			}
		}
		EpModuleEntity module=epmoduleDao.findOne(epmodule.getId());
		if(module==null){
			module=new EpModuleEntity();
			module.setIsDefault(epmodule.getIsDefault());
		}
		module.setCode(epmodule.getCode());
		module.setName(epmodule.getName());
    	module.setIsDefault(epmodule.getIsDefault());
		module.setRemarks(epmodule.getRemarks());
		module.setBuyerId(epmodule.getBuyerId());
		
		epmoduleDao.save(module);
		List<EpModuleItemEntity> res=epmoduleItemDao.findByModuleId(module.getId());
		if(res!=null){
			epmoduleItemDao.delete(res);
		}
		for (EpModuleItemEntity epModuleItemEntity : moduleItemList) {
			epModuleItemEntity.setModule(module);
			epmoduleItemDao.save(epModuleItemEntity);
		}
		
	}

	public EpModuleEntity getEpModule(Long id) {
		return epmoduleDao.findOne(id);
	}

	public Page<EpModuleItemEntity> getEpModuleItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpModuleItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpModuleItemEntity.class);
		return epmoduleItemDao.findAll(spec,pagin);
	}

	public EpModuleItemEntity getEpModuleItem(Long id) {
		return epmoduleItemDao.findOne(id);
	}

	public void addEpModuleItemSecond(List<EpModuleItemEntity> moduleItemList) {
		epmoduleItemDao.save(moduleItemList);
	}

	public List<EpModuleItemEntity> getSecondItems(long id) {
		return epmoduleItemDao.findByModuleItemId(id);
	}

	public void deleteModuleItems(Collection<EpModuleItemEntity> moduleItems) {
		epmoduleItemDao.delete(moduleItems);
	}

	public void deleteEpModule(Long epModuleId) {
		epmoduleDao.delete(epModuleId);
	}
	
	/**
	 * 根据报价模板id集合查找报价模型明细
	 * @param moduleIds
	 * @return
	 */
	public List<EpModuleItemEntity> findByModuleId(Long module){
		return epmoduleItemDao.findByModuleId(module);
	}
	
	/**
	 * 根据是否是默认模板查询
	 * 0=非默认；1=默认模板
	 * @param isDefault
	 * @return
	 */
	public List<EpModuleEntity> findByIsDefault(Integer isDefault){
		return  epmoduleDao.findByIsDefault(isDefault);
	}
	
	/**
	 * 废除报价模板明细
	 * @param moduleItems
	 */
	public void abolishModuleItems(Collection<EpModuleItemEntity> moduleItems) {
		for (EpModuleItemEntity epModuleItemEntity : moduleItems) {
			epmoduleDao.abolish(epModuleItemEntity.getId());
		}
	}
	
	/**
	 * 废除报价模板
	 * @param epModuleId
	 */
	public void abolishEpModule(Long epModuleId) {
		epmoduleDao.abolish(epModuleId);
	}
}
