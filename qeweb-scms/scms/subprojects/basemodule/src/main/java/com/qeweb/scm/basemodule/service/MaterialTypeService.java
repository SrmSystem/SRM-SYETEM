package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.MaterialTypeImpVO;
import com.qeweb.scm.basemodule.vo.MaterialTypeNsvImpVO;

@Service
@Transactional
public class MaterialTypeService {
	
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private MaterialDao materialDao;

	public List<MaterialTypeEntity> getMaterialListByPId(Long pId) {
		List<MaterialTypeEntity> materialTypeList = null;
		if(pId==null){
			materialTypeList = materialTypeDao.findByParentIdIsNullAndAbolished(StatusConstant.STATUS_NO);
		}else{
			materialTypeList = materialTypeDao.findByParentIdAndAbolished(pId,StatusConstant.STATUS_NO);
		}
		return materialTypeList;
	}

	/**
	 * 根据ID获取物料分类
	 * @param id ID
	 * @return 物料分类ID
	 */
	public MaterialTypeEntity getMaterial(Long id) {
		MaterialTypeEntity materialType = materialTypeDao.findOne(id);
		return materialType;
	}

	/**
	 * 增加一个新的物料分类
	 * @param materialType 物料分类对象
	 */
	public Map<String,Object> addNewMaterialType(MaterialTypeEntity materialType) {
		Map<String,Object> map = new HashMap<String, Object>();
		//校验是否有相同的code
		List<MaterialTypeEntity> exsitTypeList = materialTypeDao.findByCode(materialType.getCode());
		if(Collections3.isNotEmpty(exsitTypeList)){
			map.put("success", false);
			map.put("msg", "该编码已存在");
			return map;
		}
		MaterialTypeEntity pMaterialType = materialTypeDao.findOne(materialType.getParentId());
		if(pMaterialType!=null){
			pMaterialType.setLeaf(StatusConstant.STATUS_NO);
			materialTypeDao.save(pMaterialType);
			//设置级别信息
			materialType.setLevelLayer(pMaterialType.getLevelLayer()+1);
		}else{
			materialType.setLevelLayer(1);
		}
		materialType.setLeaf(StatusConstant.STATUS_YES);
		
		materialTypeDao.save(materialType);
		map.put("success", true);
		return map;
	}

	public Map<String, Object> updateMaterialType(MaterialTypeEntity materialType) {
		Map<String,Object> map = new HashMap<String, Object>();
		//校验是否有相同的code
		List<MaterialTypeEntity> exsitTypeList = materialTypeDao.findByCodeAndIdNotIn(materialType.getCode(),Lists.newArrayList(materialType.getId()));
		if(Collections3.isNotEmpty(exsitTypeList)){
			map.put("success", false);
			map.put("msg", "该编码已存在");
			return map;
		}
		materialTypeDao.save(materialType);
		map.put("success", true);
		return map;
	}

	/**
	 * 根据ID删除物料分类
	 * @param id 物料分类ID
	 * @return 删除信息
	 */
	public Map<String, Object> delete(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		//递归删除分类和将其下物料分类重置
		reDelete(id);
		map.put("success", true);
		return map;
	}

	/**
	 * 递归删除物料分类和重置物料分类
	 * @param pId
	 */
	private void reDelete(Long pId) {
		List<MaterialTypeEntity> list = materialTypeDao.findByParentId(pId);
		resetTypeOfMaterial(pId);
		if(!Collections3.isEmpty(list)) {
			for(MaterialTypeEntity materialType : list){
				reDelete(materialType.getId());
			}
		}
		materialTypeDao.delete(pId);
	}

	/**
	 * 重设物料的分类
	 * @param id 物料分类ID
	 */
	private void resetTypeOfMaterial(long id) {
		List<MaterialEntity> materialList = materialDao.findByMaterialTypeId(id);
		if(Collections3.isEmpty(materialList)) {
			return;
		}
		for(MaterialEntity material : materialList){
			material.setMaterialTypeId(null);
			material.setCategoryStatus(StatusConstant.STATUS_NO);
		}
		materialDao.save(materialList);
		
	}

	public Page<MaterialTypeEntity> getMatTypeList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialTypeEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialTypeEntity.class);
		return materialTypeDao.findAll(spec,pagin);
	}

	public List<MaterialTypeEntity> findLeafMaterialTypeList(Integer leaf) {
		return materialTypeDao.getMaterialType(leaf,3);
	}

	/**
	 * 根据级别层次获取物料分类
	 * @param level 级别
	 * @param id 指定ID,如果有指定ID，则获取指定ID
	 * @return 物料分类
	 */
	public List<MaterialTypeEntity> getMaterialTypeListByLevel(Integer level, Long id) {
		if(id!=null && id>0l) {
			return materialTypeDao.findById(id);
		}
		if(level==0){
			String leafLevel = PropertiesUtil.getProperty(ApplicationProConstant.MATERIALTYPE_LEAFLEVEL, "");
			level = Integer.parseInt(leafLevel);
		}
		return materialTypeDao.findByLevelLayer(level);
	}

	public Map<String, Object> abolishBatch(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		//递归删除分类和将其下物料分类重置
		MaterialTypeEntity materialTypeEntity=materialTypeDao.findOne(id);
		materialTypeDao.abolish(materialTypeEntity.getId());
		tongyongUpdateDelete(materialTypeEntity.getId());
		map.put("success", true);
		return map;
	}
	
	private void  tongyongUpdateDelete(Long id)
	{
		List<MaterialTypeEntity> list=new ArrayList<MaterialTypeEntity>();
		list=materialTypeDao.findByParentId(id);
		for(MaterialTypeEntity m2:list)
		{
			materialTypeDao.abolish(m2.getId());
			tongyongUpdateDelete(m2.getId());
		}
		
	}

	public Map<String, Object> importFile(List<MaterialTypeImpVO> list, ILogger logger) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> pMap = new HashMap<String, Object>();//上级物料分组
		for(MaterialTypeImpVO vo : list){
			MaterialTypeEntity pMaterialType = null;
			if(StringUtils.isNotEmptyStr(vo.getParentCode())){
				pMaterialType = (MaterialTypeEntity) pMap.get(vo.getParentCode());
				if(pMaterialType==null) {
					List<MaterialTypeEntity> pMaterialTypeList =  materialTypeDao.findByCode(vo.getParentCode());//上级分类
					if(Collections3.isNotEmpty(pMaterialTypeList)) {
						pMaterialType = pMaterialTypeList.get(0);
						pMap.put(vo.getParentCode(), pMaterialType);
					}
				}
			}
			MaterialTypeEntity materialType = new MaterialTypeEntity();
			vo.convertTarget(materialType);
			if(pMaterialType!=null){
				materialType.setParentId(pMaterialType.getId());
			}
			List<MaterialTypeEntity> exsitTypeList = materialTypeDao.findByCode(materialType.getCode());
			if(Collections3.isNotEmpty(exsitTypeList)){
				logger.log("编码["+materialType.getCode()+"]已存在");
				continue;
			}
			if(pMaterialType!=null){
				pMaterialType.setLeaf(StatusConstant.STATUS_NO);
				materialTypeDao.save(pMaterialType);
				//设置级别信息
				materialType.setLevelLayer(pMaterialType.getLevelLayer()+1);
			}else{
				materialType.setLevelLayer(1);
				materialType.setParentId(0l);
			}
			materialType.setLeaf(StatusConstant.STATUS_YES);
			
			materialTypeDao.save(materialType);
		}
		map.put("success", true);
		return map;
	}

	public String combineMaterialSupple(List<MaterialTypeNsvImpVO> list,
			ILogger logger) {
		String flas="";
		List<MaterialTypeEntity> saveList = new ArrayList<MaterialTypeEntity>();
		for (MaterialTypeNsvImpVO vo : list) {
			Integer nsv = 0;
			if("是".equals(vo.getNeedSecondVendor())) {
				nsv=1;
			} else if("否".equals(vo.getNeedSecondVendor()))
			{
				nsv=0;
			}
			List<MaterialTypeEntity> mtEntityList = materialTypeDao.findByCode(vo.getCode());
			if(Collections3.isNotEmpty(mtEntityList))
			{
				for (int i = 0; i < mtEntityList.size(); i++) {
					mtEntityList.get(i).setNeedSecondVendor(nsv);
					saveList.add(mtEntityList.get(i));
				}
			}
			else
			{
				flas=flas+"</br>报表中物料分类编码: "+vo.getCode()+"不存在";
			}
		}
		materialTypeDao.save(saveList);
		return flas;
	}

	public List<MaterialTypeEntity> getMaterialByNameAndLevelLayer(
			String name, int i) {
		return materialTypeDao.findByLevelLayerAndAbolishedAndNameLike(i,0,name);
	}

	public List<MaterialTypeEntity> getMaterialListByParentIdIn(
			List<Long> longList) {
		Map<String,Object> searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("IN_parentId", longList);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialTypeEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialTypeEntity.class);
		return materialTypeDao.findAll(spec);
	}

}
