package com.qeweb.scm.vendorperformancemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceTypeConstant;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDimensionsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateSettingDao;

@Service
@Transactional
public class VendorPerforDimensionsService {
	
	@Autowired
	private VendorPerforDimensionsDao dao;
	
	@Autowired
	private VendorPerforIndexDao indexDao;
	
	@Autowired
	private VendorPerforTemplateDao vendorPerforTemplateDao;
	
	@Autowired
	private VendorPerforTemplateSettingDao vendorPerforTemplateSettingDao;

	@Autowired
	private SerialNumberService serialNumberService;


	public List<VendorPerforDimensionsEntity> getDimensionsList() {
		List<VendorPerforDimensionsEntity> dimensionsEntityPage = (List<VendorPerforDimensionsEntity>) dao.findByAbolished(Constant.UNDELETE_FLAG);
		for(int i=0;i<dimensionsEntityPage.size();i++)
		{
			VendorPerforDimensionsEntity ve=dimensionsEntityPage.get(i);
			if(ve.getAbolished()==0)
			{
				if(null!=ve.getParentId()&&!ve.getParentId().equals(""))
				{
					ve.set_parentId(ve.getParentId());
				}
				else
				{
					ve.set_parentId(null);
				}
			}
			else
			{
				dimensionsEntityPage.remove(i);
			}
			
		}
		return dimensionsEntityPage;
	}

	public Map<String,Object> addDimensions(VendorPerforDimensionsEntity dimensionsEntity) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(dimensionsEntity.getCode()==null||dimensionsEntity.getCode().equals("")){
			dimensionsEntity.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.DIM));
		}
		else if(null!=dao.findByCode(dimensionsEntity.getCode()))
		{
			map.put("success", false);
			map.put("msg", "维度编码不能相同");
			return map;
		}
		if(null!=dao.findByDimName(dimensionsEntity.getDimName()))
		{
			map.put("success", false);
			map.put("msg", "维度名称不能相同");
			return map;
		}
		dao.save(dimensionsEntity);
		map.put("success", true);
		return map;
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public Map<String,Object> updateDimensions(VendorPerforDimensionsEntity dimensionsEntity) {
		Map<String,Object> map = new HashMap<String, Object>();
		VendorPerforDimensionsEntity dimensionsEntity1=dao.findOne(dimensionsEntity.getId());
		if(dimensionsEntity.getCode()!=null&&!(dimensionsEntity.getCode().equals(""))){
			if(null!=dao.findByCode(dimensionsEntity.getCode())&&!dimensionsEntity1.getCode().equals(dimensionsEntity.getCode()))
			{
				map.put("success", false);
				map.put("msg", "维度编码不能相同");
				return map;
			}
			dimensionsEntity1.setCode(dimensionsEntity.getCode());
		}
		
		if(null!=dao.findByDimName(dimensionsEntity.getDimName())&&!dimensionsEntity1.getDimName().equals(dimensionsEntity.getDimName()))
		{
			map.put("success", false);
			map.put("msg", "维度名称不能相同");
			return map;
		}
		
		
		dimensionsEntity1.setDimName(dimensionsEntity.getDimName());
		dimensionsEntity1.setRemarks(dimensionsEntity.getRemarks());
		dimensionsEntity1.setFactoryType(dimensionsEntity.getFactoryType());
		dao.save(dimensionsEntity1);
		
		map.put("success", true);
		return map;
	}

	public Map<String,Object> releaseDimensions(VendorPerforDimensionsEntity dimensionsEntity) {
		Map<String,Object> map = new HashMap<String, Object>();
		VendorPerforDimensionsEntity d=dao.findOne(dimensionsEntity.getId());
		d.setAbolished(0);
		dao.save(d);
		if(dimensionsEntity.getChildren()!=null) {
			for(VendorPerforDimensionsEntity di:dimensionsEntity.getChildren())
			{
				releaseDimensions(di);
			}
		}
		List<VendorPerforIndexEntity> in=indexDao.findByDimensionsId(dimensionsEntity.getId());
		for(VendorPerforIndexEntity i:in)
		{
			i.setAbolished(0);
		}
		indexDao.save(in);
		map.put("success", true);
		return map;
	}

	public Map<String,Object> delsDimensions(VendorPerforDimensionsEntity dimensionsEntity) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(dimensionsEntity.getChildren()!=null) {
			for(VendorPerforDimensionsEntity di:dimensionsEntity.getChildren())
			{
				delsDimensions(di);
			}
		}
		dao.abolish(dimensionsEntity.getId());
		List<VendorPerforIndexEntity> in=indexDao.findByDimensionsId(dimensionsEntity.getId());
		for(VendorPerforIndexEntity i:in)
		{
			i.setAbolished(1);
		}
		map.put("success", true);
		indexDao.save(in);
		return map;
	}
	public Map<String,Object> deleteDimensions(VendorPerforDimensionsEntity dimensionsEntity) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(dimensionsEntity.getChildren()!=null) {
			for(VendorPerforDimensionsEntity di:dimensionsEntity.getChildren())
			{
				deleteDimensions(di);
			}
		}
		dao.delete(dimensionsEntity.getId());
		List<VendorPerforIndexEntity> in=indexDao.findByDimensionsId(dimensionsEntity.getId());
		indexDao.delete(in);
		map.put("success", true);
		return map;
	}
	/**
	 * 查询已经被使用的映射
	 * @return 查询映射集合
	 */
	public List<String> getMappingScores() {
		List<String> mappingScoreList = dao.findMappingScores(StatusConstant.STATUS_NO);
		return Collections3.subtract(PerformanceTypeConstant.MAPPINGSCORELIST, mappingScoreList);
		
	}

	/**
	 * 获得所有的维度
	 * @return 维度集合
	 */
	public List<VendorPerforDimensionsEntity> getList() {
		return (List<VendorPerforDimensionsEntity>) dao.findAll();
	}
	
	/**
	 * 根据绩效模型获取维度
	 * @param modId
	 * @return
	 */
	public List<VendorPerforDimensionsEntity> getListByMod() {
		return dao.findByAbolished(StatusConstant.STATUS_NO);
	}
	/**
	 * 根据绩效模型获取维度
	 * @param modId
	 * @return
	 */
	public List<VendorPerforDimensionsEntity> getListByMod(Long modelId) {
		List<Long>  list = vendorPerforTemplateDao.findIdByModId(modelId);
		if(CollectionUtils.isEmpty(list))
			return Lists.newArrayList();
		
		List<Long> dimIdList = Lists.newArrayList();
		for(Long templateId : list) {
			getDimId(dimIdList, templateId);
		}
		if(CollectionUtils.isEmpty(dimIdList))
			return Lists.newArrayList();
		
		return dao.findByAbolishedAndIdIn(StatusConstant.STATUS_NO, dimIdList);
	}
	/**
	 * 用户配件服务绩效的基础数据新增（多个品牌批量新增） 中的维度combobox初始化
	 * @param modId
	 * @return
	 */
	public List<VendorPerforDimensionsEntity> getListByModEx(String code) {
		return dao.findByAbolishedAndAndCode(StatusConstant.STATUS_NO, code);
	}
	
	private void getDimId(List<Long> dimIdList, Long templateId) {
		List<VendorPerforTemplateSettingEntity> list = vendorPerforTemplateSettingDao.findByTemplateIdAndEnableStatus(templateId, 1);
		if(CollectionUtils.isEmpty(list))
			return;
		
		for(VendorPerforTemplateSettingEntity setting : list) {
			if(PerformanceTypeConstant.PER_TEMPLATE_SET_INDEX.intValue() == setting.getSourceType())
				continue;
			
			if(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM.intValue() == setting.getSourceType()) {
				dimIdList.add(setting.getSourceId());
				continue;
			}
			if(PerformanceTypeConstant.PER_TEMPLATE_SET_TEMPLATE.intValue() == setting.getSourceType()) {
				getDimId(dimIdList, setting.getSourceId());
			}
		}
	}
}
