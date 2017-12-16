package com.qeweb.scm.vendorperformancemodule.service;

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

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.ResultUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceTypeConstant;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDimensionsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateSettingDao;

@Service
@Transactional
public class VendorPerforTemplateService {
	
	@Autowired
	private VendorPerforTemplateDao dao;
	@Autowired
	private VendorPerforTemplateSettingDao settingDao;
	@Autowired
	private VendorPerforDimensionsDao dimDao;
	@Autowired
	private VendorPerforIndexDao indexDao;
	@Autowired
	private VendorPerforFormulasDao vendorPerforFormulasDao;

	@Autowired
	private SerialNumberService serialNumberService;

	/**
	 * 条件查询所有的模版
	 * @param pageNumber 开始页数
	 * @param pageSize 每页数量
	 * @param searchParamMap 查询参数 
	 * @return 模版集合
	 */
	public Page<VendorPerforTemplateEntity> getList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforTemplateEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforTemplateEntity.class);
		Page<VendorPerforTemplateEntity> page = dao.findAll(spec,pagin);
		return page;
	}

	/**
	 * 新增一个模版
	 * @param template 模版
	 * @return 模版结果
	 */
	public Map<String, Object> add(VendorPerforTemplateEntity template) {
		if(template.getCode()==null||template.getCode().equals(""))
		{
			template.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.TEM));
		}
		Map<String,Object> map = validate(template);
		if(!(Boolean)map.get("success")) {
			return map;
		}
		//如果是默认模版，原来的默认模版置为非默认
		if(template.getDefaulted().intValue()==StatusConstant.STATUS_YES){
			List<VendorPerforTemplateEntity> defaultTemplateList = dao.findByDefaulted(StatusConstant.STATUS_YES, template.getCycleId(), template.getTemplateType(), template.getModelId());
			for(VendorPerforTemplateEntity defaultTemplate : defaultTemplateList){
				defaultTemplate.setDefaulted(StatusConstant.STATUS_NO);
			}
			dao.save(defaultTemplateList);
		}
		
		template = dao.save(template);
		
		map.put("templateId", template.getId());
		return map;
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	/**
	 * 校验模版不正确的的值
	 * @param template 模版
	 * @return 校验结果
	 */
	private Map<String, Object> validate(VendorPerforTemplateEntity template) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
//		StringBuilder msgBui = new StringBuilder();
		//查询是否有存在的编号
		VendorPerforTemplateEntity codeTemplate = dao.findByCode(template.getCode());
		if(codeTemplate!=null){
			map.put("success", false);
			map.put("msg", "已存在相同编号");
			return map;
		}
		return map;
	}

	/**
	 * 根据ID获取模版
	 * @param templateId 模版主键
	 * @return 模版
	 */
	public VendorPerforTemplateEntity getById(Long templateId) {
		return dao.findOne(templateId);
	}

	/**
	 * 根据模版获取模版设置
	 * @param templateId 模版ID
	 * @return 模版设置集合
	 */
	public List<VendorPerforTemplateSettingEntity> getSetting(Long templateId) {
		VendorPerforTemplateEntity template = dao.findOne(templateId);
		List<VendorPerforTemplateSettingEntity> settingList = settingDao.findByTemplateId(templateId);
		//获得初始化的设置
		List<VendorPerforTemplateSettingEntity> initSettingList = initSettingList(template);
		settingList = processSettingList(settingList,initSettingList);
		return settingList;
	}

	/**
	 * 处理模版原有配置和新增项的处理
	 * @param settingList 原有配置
	 * @param initSettingList 新增项
	 * @return
	 */
	private List<VendorPerforTemplateSettingEntity> processSettingList(
			List<VendorPerforTemplateSettingEntity> settingList, List<VendorPerforTemplateSettingEntity> initSettingList) {
		if(Collections3.isEmpty(settingList)){
			return initSettingList;
		}
		//老配置需要替换一下_parentId
		for(VendorPerforTemplateSettingEntity oldSetting : settingList){
			if(oldSetting.getParentId()==null) {
				continue;
			}
			for(VendorPerforTemplateSettingEntity oldSettingP : settingList){
				if(oldSetting.getParentId().equals(oldSettingP.getSourceId())){
					oldSetting.set_parentId(oldSettingP.getId());
				}
			}
		}
		
		for(VendorPerforTemplateSettingEntity setting : initSettingList){
			boolean addFlag = true;
			for(VendorPerforTemplateSettingEntity oldSetting : settingList){
				if(setting.getSourceId().equals(oldSetting.getSourceId()) && setting.getSourceType().equals(oldSetting.getSourceType())){
					addFlag = false;
				}
				//看看父类是不是在这个里面
				if(setting.getParentId()!=null && setting.getParentId().equals(oldSetting.getSourceId())){
					setting.set_parentId(oldSetting.getId());
				}
			}
			//说明为新增项，新增项都是为禁用，不破坏原有模版结构
			if(addFlag){
				setting.setEnableStatus(StatusConstant.STATUS_NO);
				settingList.add(setting);
			}
		}
		return settingList;
	}

	private List<VendorPerforTemplateSettingEntity> initSettingList(VendorPerforTemplateEntity template) {
		Integer type = template.getTemplateType();
		List<VendorPerforTemplateSettingEntity> settingList = new ArrayList<VendorPerforTemplateSettingEntity>();
		//初始化分为两类，综合类型获取模版转换
		if(type!=null && type.equals(PerformanceTypeConstant.TEMPLATE_TYPE_SPUER)){
			//获取普通类型模版
			List<VendorPerforTemplateEntity> commonList = dao.getEnableTemplateList(PerformanceTypeConstant.TEMPLATE_TYPE_COMMON,StatusConstant.STATUS_NO);
		    settingList = convert(commonList,settingList,template);
		}
		//先获取顶级的维度，这种是属于刚刚的初始化，都为启用状态
		List<VendorPerforDimensionsEntity> topDimList = dimDao.findByParentIdIsNullAndAbolished(StatusConstant.STATUS_NO);
		settingList = recursiveSetting(topDimList,settingList,template);
		
		return settingList;
	}

	/**
	 * 转换普通模版为模版设置
	 * @param commonList 普通模版
	 * @param template2 
	 * @param settingList 
	 * @return 模版设置集合
	 */
	private List<VendorPerforTemplateSettingEntity> convert(List<VendorPerforTemplateEntity> commonList, List<VendorPerforTemplateSettingEntity> settingList, VendorPerforTemplateEntity template) {
		for(VendorPerforTemplateEntity common : commonList){
			VendorPerforTemplateSettingEntity setting = new VendorPerforTemplateSettingEntity();
			setting.setId(common.getId());
			setting.setTemplateId(template.getId());
			setting.setSourceId(common.getId());
			setting.setName(common.getName());
			setting.setSourceType(PerformanceTypeConstant.PER_TEMPLATE_SET_TEMPLATE);
			setting.setEnableStatus(StatusConstant.STATUS_YES);
			settingList.add(setting);
		}
		return settingList;
	}

	private List<VendorPerforTemplateSettingEntity> recursiveSetting(List<VendorPerforDimensionsEntity> dimList, List<VendorPerforTemplateSettingEntity> settingList,VendorPerforTemplateEntity template) {
		for(VendorPerforDimensionsEntity dim : dimList){
			VendorPerforTemplateSettingEntity setting = new VendorPerforTemplateSettingEntity();
			setting.setId(dim.getId());
			if(dim.getParentId()!=null) {
				setting.setParentId(dim.getParentId());
				setting.setParentType(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM);
			}
			setting.setTemplateId(template.getId());
			setting.setSourceId(dim.getId());
			setting.setName(dim.getDimName());
			setting.setSourceType(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM);
			setting.setEnableStatus(StatusConstant.STATUS_YES);
			settingList.add(setting);
			//查找子集
			List<VendorPerforDimensionsEntity> childList = dimDao.findByParentIdAndAbolished(dim.getId(),StatusConstant.STATUS_NO);
			if(Collections3.isNotEmpty(childList)){
				//如果不为空，继续递归
				recursiveSetting(childList,settingList,template);
			}else{
				List<VendorPerforIndexEntity> indexList = indexDao.findByDimensionsIdAndAbolished(dim.getId(),0);
				//开始递归指标
				recursiveSettingIndex(indexList,settingList,template);
			}
		}
		return settingList;
	}

	/**
	 * 递归设置中的指标
	 * @param indexList 指标集合
	 * @param settingList 设置集合
	 * @param template 模版
	 */
	private void recursiveSettingIndex(List<VendorPerforIndexEntity> indexList,
			List<VendorPerforTemplateSettingEntity> settingList, VendorPerforTemplateEntity template) {
		for(VendorPerforIndexEntity index : indexList){
			VendorPerforTemplateSettingEntity setting = new VendorPerforTemplateSettingEntity();
			setting.setId(index.getId());
			if(index.getIndexId()==null){
				setting.setParentId(index.getDimensionsId());
				setting.setParentType(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM);
			}else{
				setting.setParentId(index.getIndexId());
				setting.setParentType(PerformanceTypeConstant.PER_TEMPLATE_SET_INDEX);
			}
			setting.setTemplateId(template.getId());
			setting.setSourceId(index.getId());
			setting.setName(index.getIndexName());
			setting.setSourceType(PerformanceTypeConstant.PER_TEMPLATE_SET_INDEX);
			setting.setEnableStatus(StatusConstant.STATUS_YES);
			settingList.add(setting);
			//查找子集
			List<VendorPerforIndexEntity> childList = indexDao.findByIndexIdAndAbolished(index.getId(),StatusConstant.STATUS_NO);
			if(Collections3.isNotEmpty(childList)){
				//如果不为空，继续递归
				recursiveSettingIndex(childList,settingList,template);
			}
		}
	}

	/**
	 * 更新模版和模版设置
	 * @param template 模版
	 * @return 信息
	 */
	public Map<String, Object> update(VendorPerforTemplateEntity template) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		template.setFinishStatus(StatusConstant.STATUS_YES);
		dao.save(template);
		if(template.getDefaulted().intValue()==StatusConstant.STATUS_YES){
			List<VendorPerforTemplateEntity> defaultTemplateList = dao.findByDefaulted(StatusConstant.STATUS_YES, template.getCycleId(), template.getTemplateType(), template.getModelId());
			for(VendorPerforTemplateEntity defaultTemplate : defaultTemplateList){
				if(defaultTemplate.getId() == template.getId())
					continue;
				
				defaultTemplate.setDefaulted(StatusConstant.STATUS_NO);
			}
			dao.save(defaultTemplateList);
		}
		settingDao.deleteByTemplateId(template.getId());
		List<VendorPerforTemplateSettingEntity> settingList = template.getSettingList();
		for(VendorPerforTemplateSettingEntity setting : settingList){
			if(setting.getSourceType().equals(PerformanceTypeConstant.PER_TEMPLATE_SET_INDEX) 
					&& StringUtils.isEmpty(setting.getFormula())
					&& setting.getEnableStatus().intValue()==StatusConstant.STATUS_YES
					){
				VendorPerforFormulasEntity formulas = vendorPerforFormulasDao.findByIndexIdAndCycleId(setting.getSourceId(), template.getCycleId());
			    if(formulas==null){
			    	map.put(ResultUtil.SUCCESS, false);
			    	map.put(ResultUtil.MSG, setting.getName()+"：没有配置公式!");
			    	return map;
			    }
				setting.setFormula(formulas.getContent());
			}
		}
		settingDao.save(settingList);
		return map;
	}
	
	/**
	 * 批量删除模版
	 * @param ids 模版IDS
	 * @return
	 */
	public Map<String,Object> deleteList(List<VendorPerforTemplateEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		for(VendorPerforTemplateEntity entity : list){
			delete(entity.getId());
		}
		return map;
		
	}
	
	public Map<String,Object> abolishList(List<VendorPerforTemplateEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		for(VendorPerforTemplateEntity entity : list){
			dao.save(entity);
		}
		return map;
	}

	/**
	 * 删除模版
	 * @param id 模版ID
	 */
	public void delete(Long id) {
		settingDao.deleteByTemplateId(id);
		dao.delete(id);
	}

	/**
	 * 查询已经被使用的映射
	 * @return 查询映射集合
	 */
	public List<String> getMappingScores(Long id) {
		List<String> mappingScoreList = dao.findMappingScores(StatusConstant.STATUS_NO, id == null ? 0l : id);
		return Collections3.subtract(PerformanceTypeConstant.TOTALMAPPINGSCORELIST, mappingScoreList);
		
	}



}
