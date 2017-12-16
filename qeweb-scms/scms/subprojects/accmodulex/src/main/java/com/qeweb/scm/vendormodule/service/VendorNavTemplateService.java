package com.qeweb.scm.vendormodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springside.modules.utils.Collections3;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;
import com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyTemplateEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity;
import com.qeweb.scm.vendormodule.repository.VendorNavTemplateDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyTemplateDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplatePhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplateSurveyDao;

@Service
@Transactional
public class VendorNavTemplateService {
	
	@Autowired
	private VendorNavTemplateDao vendorNavTemplateDao;
	@Autowired
	private VendorTemplatePhaseDao vendorTemplatePhaseDao;
	@Autowired
	private VendorTemplateSurveyDao vendorTemplateSurveyDao;
	@Autowired
	private VendorSurveyTemplateDao vendorSurveyTemplateDao;


	public Page<VendorNavTemplateEntity> getVendorNavTemplateList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorNavTemplateEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorNavTemplateEntity.class);
		return vendorNavTemplateDao.findAll(spec,pagin);
	}

	/**
	 * 添加一个新的向导模版
	 * @param vendorNavTemplate
	 */
	public VendorNavTemplateEntity addNewVendorNavTemplate(VendorNavTemplateEntity vendorNavTemplate) {
		//必须设置的默认和完成状态
		vendorNavTemplate.setDefaultFlag(StatusConstant.STATUS_NO);
		vendorNavTemplate.setFinishStatus(StatusConstant.STATUS_NO);
		return vendorNavTemplateDao.save(vendorNavTemplate);
	}

	public VendorNavTemplateEntity getVendorNavTemplate(Long id) {
		return vendorNavTemplateDao.findOne(id);
	}

	public void updateVendorNavTemplate(VendorNavTemplateEntity vendorNavTemplate) {
		vendorNavTemplateDao.save(vendorNavTemplate);
	}

	public Map<String,Object> deleteVendorNavTemplateList(List<VendorNavTemplateEntity> vendorNavTemplateList) {
		//需要删除一系列关系。
		if(CollectionUtils.isEmpty(vendorNavTemplateList)){
			return null;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		for(VendorNavTemplateEntity template : vendorNavTemplateList){
			if(template.getDefaultFlag()!=null && template.getDefaultFlag()==StatusConstant.STATUS_YES) {
				//如果为默认不允许删除
				map.put("success", false);
				map.put("msg", "删除失败,默认模版不允许删除！");
				return map;
			}
			//删除调查表关系
			vendorTemplateSurveyDao.deleteByVendorNavTemplate(template.getId());
			//删除范围关系 
			
			//删除阶段关系 TODO
			vendorTemplatePhaseDao.deleteByVendorNavTemplate(template.getId());
		}
		vendorNavTemplateDao.delete(vendorNavTemplateList);
		return map;
	}


	public VendorNavTemplateEntity createTemplatePhase(List<VendorTemplatePhaseEntity> phaseList, Long templateId) {
		//先清除原来的阶段设置
		vendorTemplatePhaseDao.deleteByVendorNavTemplate(templateId);
		for(VendorTemplatePhaseEntity templatePhase : phaseList){
			Long phaseId = templatePhase.getId();
			templatePhase.setTemplateId(templateId);
			templatePhase.setPhaseId(phaseId);
			vendorTemplatePhaseDao.save(templatePhase);
		}
		return vendorNavTemplateDao.findOne(templateId);
		
	}


	/**
	 * 创建导航模版的类型，并将范围分配给导航模版
	 * @param navType 导航模版类型
	 * @param templateId 导航模版ID
	 * @return 处理结果
	 */
	public Map<String, Object> createNavType(VendorNavTemplateEntity navTemplate) {
		Map<String, Object> map = new HashMap<String, Object>();
		VendorNavTemplateEntity existNavTemplate = vendorNavTemplateDao.findOne(navTemplate.getId());
		if(navTemplate.getRangeType()!=null && navTemplate.getRangeType().intValue()==VendorModuleTypeConstant.NAV_TEM_TYPE_DEFALUT){
			//查询是否有默认的模版，如果有默认的，会被替换，原有默认模版废除
			/*VendorNavTemplateEntity defaultNavTemplate = vendorNavTemplateDao.findByDefaultFlagAndAbolished(StatusConstant.STATUS_YES,StatusConstant.STATUS_NO
					);
			if(defaultNavTemplate!=null){
				defaultNavTemplate.setAbolished(StatusConstant.STATUS_YES);
				vendorNavTemplateDao.save(defaultNavTemplate);
			}*/
			//查询是否有默认的模版，如果有默认的，将默认的状态更改为否，把新建默认模板加入列表
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();		
			VendorNavTemplateEntity defaultNavTemplate = vendorNavTemplateDao.findByDefaultFlagAndAbolishedAndOrgId(StatusConstant.STATUS_YES, StatusConstant.STATUS_NO,user.orgId);
			if(defaultNavTemplate!=null){
				defaultNavTemplate.setDefaultFlag(StatusConstant.STATUS_NO);				
				vendorNavTemplateDao.save(defaultNavTemplate);
			}
			existNavTemplate.setDefaultFlag(StatusConstant.STATUS_YES);
			vendorNavTemplateDao.save(existNavTemplate);
		}
		map.put("vendorNavTemplate", navTemplate);
		return map;
	}


	public List<VendorNavTemplateEntity> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 给模版的每个阶段分配调查表
     * @param templateSurveyList 模版调查表集合
     * @return
     */
	public Map<String, Object> createTemplateSurvey(List<VendorTemplateSurveyEntity> templateSurveyList) {
		Long vendorNavTemplateId = null;
		for(VendorTemplateSurveyEntity templateSurvey : templateSurveyList){
			VendorSurveyTemplateEntity surveyTemplate = vendorSurveyTemplateDao.findOne(templateSurvey.getSurveyTemplateId());
			templateSurvey.setSurveyName(surveyTemplate.getName());
			templateSurvey.setSurveyCode(surveyTemplate.getCode());
			templateSurvey.setSurveyTemplateSn(surveyTemplate.getSn());
			templateSurvey.setSurveyTemplateId(surveyTemplate.getId());
			vendorNavTemplateId = templateSurvey.getVendorTemplateId();
		}
		//最后一步，所以需要设置为完成状态
		VendorNavTemplateEntity vendorNavTemplate = vendorNavTemplateDao.findOne(vendorNavTemplateId);
		vendorNavTemplate.setFinishStatus(StatusConstant.STATUS_YES);
		updateVendorNavTemplate(vendorNavTemplate);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		vendorTemplateSurveyDao.save(templateSurveyList);
		return map;
	}

	/**
	 * 根据模版的阶段获取当前阶段选择的调查表
	 * @param id 当前模版阶段的ID
	 * @return 当前阶段的调查表
	 */
	public Map<String, Object> getTemplateSurveyByPhase(Long id) {
		List<VendorTemplateSurveyEntity> templateSurveyList = vendorTemplateSurveyDao.findByTemplatePhaseId(id);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("dataList", templateSurveyList);
		return map;
	}


	/**
	 * 重设向导模板的阶段和调查表
	 * @param templatePhaseList 阶段
	 * @return
	 */
	public Map<String, Object> reset(List<VendorTemplatePhaseEntity> templatePhaseList) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(VendorTemplatePhaseEntity tphase : templatePhaseList){
			//为了防止页面问题，导致重复插入
			VendorTemplatePhaseEntity existTphase = vendorTemplatePhaseDao.findOne(tphase.getId());
			if(existTphase==null){
				map.put("success", false);
				map.put("msg", "该阶段配置已不存在，请刷新后重试！");
				return map;
			}
			
			//删除原有的调查表配置
			vendorTemplateSurveyDao.deleteByTPhaseId(tphase.getId());
			vendorTemplatePhaseDao.delete(tphase);
			tphase.setId(0);
			vendorTemplatePhaseDao.save(tphase);
			List<VendorTemplateSurveyEntity> tsurveyList = tphase.getItemList();
			if(Collections3.isEmpty(tsurveyList)) {
				continue;
			}
			for(VendorTemplateSurveyEntity tsurvey : tsurveyList){
				tsurvey.setTemplatePhaseId(tphase.getId());
				//还需要重设下调查表名称
				VendorSurveyTemplateEntity surveyTemplate = vendorSurveyTemplateDao.findOne(tsurvey.getSurveyTemplateId());
				tsurvey.setSurveyName(surveyTemplate.getName());
				tsurvey.setSurveyCode(surveyTemplate.getCode());
				tsurvey.setSurveyTemplateSn(surveyTemplate.getSn());
			}
			vendorTemplateSurveyDao.save(tsurveyList);
		}
		map.put("success", true);
		return map;
	}

	/**
	 * 创建一个供应商准入的向导模版，如果已经在数据中。
	 * 则是由上一步引发，那么需要清除阶段设置
	 * @param vendorNavTemplate
	 */
	public Map<String,Object> createNavTemplate(VendorNavTemplateEntity vendorNavTemplate) {
		Map<String,Object> map = validateVenNavTem(vendorNavTemplate);
		boolean flag = (Boolean) map.get("success");
		if(!flag){
			return map;
		}
		if(vendorNavTemplate.getId()>0l){
			vendorTemplatePhaseDao.deleteByVendorNavTemplate(vendorNavTemplate.getId());
			updateVendorNavTemplate(vendorNavTemplate);
		}else {
			vendorNavTemplate = addNewVendorNavTemplate(vendorNavTemplate);
		}
		map.put("vendorNavTemplate", vendorNavTemplate);
		return map;
		
	}

	/**
	 * 校验向导模版，主要校验编号
	 * @param vendorNavTemplate
	 * @return
	 */
	private Map<String, Object> validateVenNavTem(VendorNavTemplateEntity vendorNavTemplate) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<VendorNavTemplateEntity> existVenNavTemList = vendorNavTemplateDao.findByCode(vendorNavTemplate.getCode());
		if(vendorNavTemplate.getId()>0l){
			existVenNavTemList = vendorNavTemplateDao.findByCodeAndIdNot(vendorNavTemplate.getCode(),vendorNavTemplate.getId());
		}
		if(Collections3.isNotEmpty(existVenNavTemList)){
			map.put("success", false);
			map.put("msg", "模版编码已存在！");
			return map;
		}
		map.put("success", true);
		return map;
	}
	
	public VendorNavTemplateEntity getOrgNavTemplate(Long orgId) {
		VendorNavTemplateEntity defaultTemplate = vendorNavTemplateDao.findByDefaultFlagAndAbolishedAndOrgId(StatusConstant.STATUS_YES, 
				StatusConstant.STATUS_NO, orgId);
		return defaultTemplate;
	}

}
