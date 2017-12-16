package com.qeweb.scm.epmodule.service;

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
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.entity.EpModuleEntity;
import com.qeweb.scm.epmodule.entity.EpModuleMaterialRelEntity;
import com.qeweb.scm.epmodule.repository.EpModuleDao;
import com.qeweb.scm.epmodule.repository.EpModuleMaterialRelDao;
import com.qeweb.scm.epmodule.web.vo.MaterialModuleVO;

@Service(value="epModuleMaterialRelService")
@Transactional
public class EpModuleMaterialRelService {
	
	@Autowired
	private EpModuleMaterialRelDao relDao; 
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private EpModuleDao epModuleDao;
	

	public  Page<MaterialEntity> getMaterialList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialEntity.class);
		return materialDao.findAll(spec, pagin);
	}
	
	public  Page<EpModuleEntity> getModuleList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpModuleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpModuleEntity.class);
		return epModuleDao.findAll(spec, pagin);
	}
	
	public  Page<EpModuleMaterialRelEntity> getEpModuleMaterialRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpModuleMaterialRelEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpModuleMaterialRelEntity.class);
		return relDao.findAll(spec, pagin);
	}

	/**
	 * 
	 * @param epModuleMaterialRelList
	 */
	public void batchDelete(List<EpModuleMaterialRelEntity> epModuleMaterialRelList) {
		relDao.delete(epModuleMaterialRelList);		
	}
	
	public List<EpModuleMaterialRelEntity> findMaterialIdAndModuleId(Long materialId,Long moduleId){
		return relDao.findByMaterialIdAndModuleId(materialId, moduleId);
	}
	
	public List<EpModuleMaterialRelEntity> findMaterialId(Long materialId){
		return relDao.findByMaterialId(materialId);
	}
	
	/**
	 * 根据物料id查找报价模型与物料关系中的报价模型id
	 * @param materialId
	 * @return
	 */
	public List<Long> findModuleIdByMaterialId(Long materialId){
		List<Long> moduleIdList = relDao.findModuleIdByMaterialId(materialId);
		return moduleIdList;
	}

	public void saveRel(EpModuleMaterialRelEntity rel) {
		relDao.save(rel);
		
	}
	
	
	public boolean saveSplit(List<MaterialModuleVO> list, ILogger logger) {
		logger.log("--> 开始导入");
		if(list != null && list.size() > 0 ){
			for (MaterialModuleVO materialModuleVO : list) {
				if(StringUtils.isEmpty(materialModuleVO.getMaterialCode())){
					continue;
				}
				if(StringUtils.isEmpty(materialModuleVO.getModuleCode())){
					continue;
				}
				
				EpModuleEntity module=epModuleDao.findByCodeAndAbolished(materialModuleVO.getModuleCode().trim(), 0);
				MaterialEntity material=materialDao.findByCodeAndAbolished(materialModuleVO.getMaterialCode(),0).get(0);
				
				if(module==null){
					continue;
				}
				if(material==null){
					continue;
				}
				
				List<EpModuleMaterialRelEntity> old=relDao.findByMaterialIdAndModuleId(material.getId(), module.getId());
				if(old==null||old.size()<=0){
					EpModuleMaterialRelEntity rel=new EpModuleMaterialRelEntity();
					rel.setModuleId(module.getId());
					rel.setMaterialId(material.getId());
					relDao.save(rel);
				}
			}
		}
		logger.log("--> 结束导入");
		return true;
	}
	
}
