package com.qeweb.scm.vendormodule.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyTemplateEntity;
import com.qeweb.scm.vendormodule.repository.VendorSurveyBaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyDataDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyTemplateDao;
import com.qeweb.scm.vendormodule.utils.SurveyUtil;
import com.qeweb.scm.vendormodule.vo.CtVo;

@Service
@Transactional
public class VendorSurveyCfgService {
	
	@Autowired
	private VendorSurveyCfgDao vendorSurveyCfgDao;
	@Autowired
	private VendorSurveyDataDao vendorSurveyDataDao;
	@Autowired
	private VendorSurveyBaseDao vendorSurveyBaseDao;
	@Autowired
	private VendorSurveyTemplateDao vendorSurveyTemplateDao;
	
	private final static String SURVEYUPLOADDIR = "vendor.survey.updir";
	
	


	public Page<VendorSurveyCfgEntity> getVendorSurveyCfgList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorSurveyCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorSurveyCfgEntity.class);
		return vendorSurveyCfgDao.findAll(spec,pagin);
	}


	public void addNewVendorSurveyCfg(VendorSurveyCfgEntity vendorPhaseCfg) {
		vendorSurveyCfgDao.save(vendorPhaseCfg);
	}

	public VendorSurveyCfgEntity getVendorSurveyCfg(Long id) {
		return vendorSurveyCfgDao.findOne(id);
	}

	public void updateVendorSurveyCfg(VendorSurveyCfgEntity vendorPhaseCfg) {
		vendorSurveyCfgDao.save(vendorPhaseCfg);
	}

	public void deleteVendorSurveyCfgList(List<VendorSurveyCfgEntity> vendorPhaseCfgList) {
		vendorSurveyCfgDao.delete(vendorPhaseCfgList);
	}

    /**
     * 获得所有的阶段
     * @return 阶段集合
     */
	public List<VendorSurveyCfgEntity> getVendorSurveyCfgListAll() {
		return (List<VendorSurveyCfgEntity>) vendorSurveyCfgDao.findAll();
	}


	/**
	 * 根据配置ID获取配置模版以及内容,维护调查表使用
	 * @param id 模版配置ID
	 * @return 模版信息
	 * @throws DocumentException 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> getVendorSurveyByCfg(Long cfgId) throws Exception {
		VendorSurveyCfgEntity vendorSurveyCfg = vendorSurveyCfgDao.findOne(cfgId);
		VendorSurveyTemplateEntity surveyTemplate = vendorSurveyCfg.getSurveyTemplate();
		//获取需要的调查表基本信息
		List<VendorSurveyBaseEntity> baseList = vendorSurveyBaseDao.findByVendorCfgIdOrderByVersionNODesc(vendorSurveyCfg.getId());
		VendorSurveyBaseEntity base = null;
		if(Collections3.isNotEmpty(baseList)){
			base = baseList.get(0);
		}
		Map<String,Object> map = getVendorSurvey(vendorSurveyCfg,base,surveyTemplate);
		return map;
	}
	
    /**
     * 获取供审核使用的调查表信息
     * @param cfgId 供应商调查表配置ID
     * @return 审核用调查表信息
     * @throws Exception 
     */
	public Map<String, Object> getVendorSurveyAuditByCfg(Long cfgId) throws Exception {
		VendorSurveyCfgEntity vendorSurveyCfg = vendorSurveyCfgDao.findOne(cfgId);
		VendorSurveyTemplateEntity surveyTemplate = vendorSurveyCfg.getSurveyTemplate();
		//获取需要的调查表基本信息，审核的是当前版本，如果未提交，则只能看不能审核
		VendorSurveyBaseEntity base = null;
		List<VendorSurveyBaseEntity> surveys = vendorSurveyBaseDao.findByVendorCfgIdAndCurrentVersionOrderByIdDesc(vendorSurveyCfg.getId(),StatusConstant.STATUS_YES);
		VendorSurveyBaseEntity survey = null;
		if(surveys!=null&&surveys.size()>0)
		{
			if(surveys.size()>1)
			{
				for(int i=1;i<surveys.size();i++)
				{
					survey=surveys.get(i);
					survey.setCurrentVersion(0);
					vendorSurveyBaseDao.save(survey);
				}
			}
			base =surveys.get(0);
		}
		Map<String,Object> map = getVendorSurvey(vendorSurveyCfg,base,surveyTemplate);
		return map;
	}
	

	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> getVendorSurvey(Long id) throws Exception{
		//获取需要的调查表基本信息
		VendorSurveyBaseEntity base = vendorSurveyBaseDao.findOne(id);
		VendorSurveyTemplateEntity surveyTemplate = vendorSurveyTemplateDao.findOne(base.getTemplateId());
		Integer type = surveyTemplate.getTemplateType();
		Map<String,Object> map = new HashMap<String, Object>();
		if(type.intValue()==VendorModuleTypeConstant.SUR_TEM_TYPE_XML){
			map = convertSurveyTemplate(base,surveyTemplate.getPath());
		}
		map.put("templateId", surveyTemplate.getId());
		map.put("templatePath", surveyTemplate.getPath());
		map.put("templateCode", surveyTemplate.getCode());
		map.put("surveyBase", base);
		return map;
	}
	
	/**
	 * 根据供应商调查表配置，调查表头信息，调查表模版获取调查表的信息
	 * @param vendorSurveyCfg 供应商调查表配置
	 * @param base 调查表头信息
	 * @param surveyTemplate 调查表模版
	 * @return 调查表信息
	 * @throws Exception
	 */
	public Map<String, Object> getVendorSurvey(VendorSurveyCfgEntity vendorSurveyCfg, VendorSurveyBaseEntity base,VendorSurveyTemplateEntity surveyTemplate) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		Integer type = surveyTemplate.getTemplateType();
		if(type.intValue()==VendorModuleTypeConstant.SUR_TEM_TYPE_XML){
			map = convertSurveyTemplate(base,surveyTemplate.getPath());
		}
		map.put("templateId", surveyTemplate.getId());
		map.put("templatePath", surveyTemplate.getPath());
		map.put("templateCode", surveyTemplate.getCode());
		map.put("vendorSurveyCfg", vendorSurveyCfg);
		map.put("surveyBase", base);
		return map;
	}
	
	/**
	 * 转换调查表模版信息
	 * @param base 调查表基本信息
	 * @param vendorSurveyCfg 调查表配置 
	 * @param surveyTemplate 调查表模版
	 * @return 转换后的调查表信息
	 * @throws DocumentException
	 */
	public Map<String, Object> convertSurveyTemplate(VendorSurveyBaseEntity base, String templatePath) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		SAXReader reader = new SAXReader();
		Document templateDoc = reader.read(new File(templatePath));
		String rootId = templateDoc.getRootElement().attributeValue("id");
		if(base!=null){
			List<VendorSurveyDataEntity> dataList = vendorSurveyDataDao.findByBaseIdOrderByLastUpdateTimeDesc(base.getId());
			List<CtVo> ctList = SurveyUtil.parserSurveyedXML(base, dataList, templateDoc);
			map.put("surveyBase", base);
			map.put("ctList", ctList);
		}else{
			List<CtVo> ctList = SurveyUtil.parserSurveyXML(templateDoc);
			map.put("ctList", ctList);
		}
		map.put("rootId", rootId);
		return map;
	}


}
