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
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.MaterialImpVO;

@Service
@Transactional
public class MaterialService {
	
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;

	/**
	 * 获取物料列表
	 * @param pageNumber 页数
	 * @param pageSize 每页大小
	 * @param searchParamMap 查询参数
	 * @return
	 */
	public Page<MaterialEntity> getMaterialList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MaterialEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MaterialEntity.class);
		return materialDao.findAll(spec,pagin);
	}

	/**
	 * 物料分类设置
	 * @param materialIds 物料的ID
	 * @param materialTypeId 物料分类ID
	 * @return 分类结果
	 */
	public Map<String, Object> category(String materialIds, String materialTypeId) {
		String[] idArray = materialIds.split(",");
		List<MaterialEntity> materialList = new ArrayList<MaterialEntity>();
		for(String id : idArray){
			MaterialEntity material = materialDao.findOne(Long.parseLong(id));
			MaterialTypeEntity mt= materialTypeDao.findOne(Long.parseLong(materialTypeId));
			material.setMaterialTypeId(Long.parseLong(materialTypeId));
			material.setCategoryStatus(StatusConstant.STATUS_YES);
			material.setPartsCode(mt.getCode());
			material.setPartsName(mt.getName());
			if(mt.getImportance()==null|| mt.getImportance().equals(""))
			{
				material.setPartsType("");
			}
			else
			{
				material.setPartsType(""+mt.getImportance());
			}
			materialList.add(material);
		}
		materialDao.save(materialList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	public List<MaterialEntity> getMaterialByCode(String code){
		return materialDao.findByCode(code);
	}
	
	public MaterialEntity getMaterialById(Long id) {
		return materialDao.findOne(id);
	}

	/**
	 * 添加一个物料
	 * @param material 物料对象
	 * @return 添加结果
	 */
	public Map<String, Object> addNew(MaterialEntity material) {
		//检查是否有重复图号的物料
		Map<String,Object> map = new HashMap<String, Object>();
		List<MaterialEntity> materialList = materialDao.findByCode(material.getCode());
		if(Collections3.isNotEmpty(materialList)){
			map.put("success", false);
			map.put("msg", "已经存在该图号！");
			return map;
		}
		materialDao.save(material);
		map.put("success", true);
		return map;
	}

	
	public Map<String, Object> update(MaterialEntity material) {
		//检查是否有重复图号的物料
		Map<String,Object> map = new HashMap<String, Object>();
		List<MaterialEntity> materialList = materialDao.findByCodeAndIdNotIn(material.getCode(),Lists.newArrayList(material.getId()));
		MaterialEntity m = materialDao.findOne(material.getId());
		if(Collections3.isNotEmpty(materialList)){
			map.put("success", false);
			map.put("msg", "已经存在该图号！");
			return map;
		}
//		material.setAbolished(m.getAbolished());
		m.setCode(material.getCode());
		m.setName(material.getName());
		m.setDescribe(material.getDescribe());
		m.setPicStatus(material.getPicStatus());
		m.setPartsType(material.getPartsType());
		m.setTechnician(material.getTechnician());
		m.setAbolished(material.getAbolished());
		materialDao.save(m);
		map.put("success", true);
		return map;
	}

	/**
	 * 获得全部的物料
	 * @return
	 */
	public List<MaterialEntity> getAll() {
		return (List<MaterialEntity>) materialDao.findAll();
	}

	public void deleteMaterialList(List<MaterialEntity> materialList) {
		materialDao.delete(materialList);
	}

	public String getPicStatus() {
		String data="[";
		int i=0;
		for(String str:Constant.PIC_STATUS){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+i+"\",\"text\":\""+str+"\"}";
					i++;
		}
		data=data+"]";
		return data;
	}

	public Map<String, Object> abolishBatch(List<MaterialEntity> materials) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(MaterialEntity material:materials)
		{
			materialDao.abolish(material.getId());
		}
		map.put("success", true);
		return map;
	}

	/**
	 * 导入物料
	 * @param list
	 * @param logger
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> importFile(List<MaterialImpVO> list, ILogger logger) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> pMap = new HashMap<String, Object>();//上级物料分组
		Map<String,Object> yanz = new HashMap<String, Object>();//上级物料分组
		List<MaterialEntity> mslist=new ArrayList<MaterialEntity>();
		for(MaterialImpVO vo : list){
			int statr=yanz.size();
			yanz.put(""+vo.getCode(),vo);
			int end=yanz.size();
			if(statr==end)
			{
				map.put("success", false);
				map.put("msg", "导入数据中，有相同的物料编号（"+vo.getCode()+"）！");
				return map;
			}
			List<MaterialEntity> exsitTypeList = materialDao.findByCode(vo.getCode());
			if(Collections3.isNotEmpty(exsitTypeList)){
				map.put("success", false);
				map.put("msg", "系统中，有相同的物料编号（"+vo.getCode()+"）！");
				return map;
			}
			
			MaterialTypeEntity pMaterialType = null;
			if(StringUtils.isNotEmptyStr(vo.getPartsCode())){
				String typeCode = vo.getPartsCode();
				pMaterialType = (MaterialTypeEntity) pMap.get(typeCode);
				if(pMaterialType==null) {
					List<MaterialTypeEntity> pMaterialTypeList =  materialTypeDao.findByCode(typeCode);//上级分类
					if(Collections3.isNotEmpty(pMaterialTypeList)) {
						pMaterialType = pMaterialTypeList.get(0);
						pMap.put(typeCode, pMaterialType);
					}
					else{
						map.put("success", false);
						map.put("msg", "零部件："+typeCode+"不存在");
						return map;
					}
				}
			}
			
			if(StringUtils.isNotEmpty(vo.getPicStatus())){
				boolean flag = true;
				String picList = "";
				for(String str:Constant.PIC_STATUS){
					if(vo.getPicStatus().equals(str)){
						flag = false;
					}
					picList += str+",";
				}
				if(flag){
					logger.log("图纸状态格式为："+picList+"----编码["+vo.getCode()+"]的图纸状态'"+vo.getPicStatus()+"'不正确！");
					continue;
				}
			}
			MaterialEntity material = new MaterialEntity();
			BeanUtil.copyPropertyNotNull(vo, material);
			material.setCategoryStatus(StatusConstant.STATUS_NO);
			material.setEnableStatus(StatusConstant.STATUS_YES);
			if(pMaterialType!=null){
				material.setMaterialTypeId(pMaterialType.getId());
				material.setCategoryStatus(StatusConstant.STATUS_YES);
				material.setPartsCode(pMaterialType.getCode());
				material.setPartsName(pMaterialType.getName());
				if(pMaterialType.getImportance()==null)
				{
					if(vo.getPartsType()==null)
					{
						material.setPartsType("次要零件");
					}
					else
					{
						material.setPartsType(vo.getPartsType());
					}
				}
				else if(pMaterialType.getImportance()==1)
				{
					material.setPartsType("主要零件");
				}
			}
			mslist.add(material);
		}
		materialDao.save(mslist);
		map.put("success", true);
		return map;
	}

	/**
	 * 查找所有的物料
	 * @return
	 */
	public List<MaterialEntity> findAll() {
		return (List<MaterialEntity>) materialDao.findAll();
	}
	
	public void saveMaterials(List<MaterialEntity> materialEntitys){
		materialDao.save(materialEntitys);
	}

}
