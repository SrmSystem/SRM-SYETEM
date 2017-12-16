package com.qeweb.scm.vendormodule.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyTemplateEntity;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyTemplateDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplatePhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplateSurveyDao;
import com.qeweb.scm.vendormodule.utils.SurveyUtil;
import com.qeweb.scm.vendormodule.vo.CtVo;

@Service
@Transactional
public class VendorSurveyTemplateService {
	
	@Autowired
	private VendorSurveyTemplateDao vendorSurveyTemplateDao;
	@Autowired
	private VendorTemplateSurveyDao vendorTemplateSurveyDao;
	@Autowired
	private VendorTemplatePhaseDao vendorTemplatePhaseDao;
	@Autowired
	private VendorSurveyCfgDao cfgDao;
	
	public final static String TEMPLATEDIRPRO = "vendor.survey.dir";
	


	public Page<VendorSurveyTemplateEntity> getVendorSurveyTemplateList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "sn" , "asc");
		searchParamMap.put("EQ_abolished", 0);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorSurveyTemplateEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorSurveyTemplateEntity.class);
		Page<VendorSurveyTemplateEntity>  page = vendorSurveyTemplateDao.findAll(spec,pagin);
		return page;
	}


	public Map<String,Object> addNewVendorSurveyTemplate(VendorSurveyTemplateEntity vendorSurveyTemplate,MultipartFile pathFile) throws IOException {
		//先查询下code不能重复
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		VendorSurveyTemplateEntity surveyTemplate_repeat = vendorSurveyTemplateDao.findByCode(vendorSurveyTemplate.getCode()); 
		if(surveyTemplate_repeat!=null){
			map.put("success", false);
			map.put("msg", "编码已存在！");
			return map;
		}
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		vendorSurveyTemplate.setOrgId(user.orgId); 
		if(pathFile!=null && !pathFile.isEmpty()){
			File file = FileUtil.savefileFix(pathFile, PropertiesUtil.getProperty(TEMPLATEDIRPRO));
			vendorSurveyTemplate.setFileName(pathFile.getOriginalFilename());
			vendorSurveyTemplate.setPath(file.getPath());
		}
		vendorSurveyTemplateDao.save(vendorSurveyTemplate);
		return map;
	}

	public VendorSurveyTemplateEntity getVendorSurveyTemplate(Long id) {
		return vendorSurveyTemplateDao.findOne(id);
	}

	/**
	 * 更新模版信息，更新时会有文件上传，上传目录必须有值，否则抛出异常
	 * @param vendorSurveyTemplate 调查表模版
	 * @param pathFile 模版文件
	 * @throws IOException
	 */
	public Map<String,Object> updateVendorSurveyTemplate(VendorSurveyTemplateEntity vendorSurveyTemplate, MultipartFile pathFile) throws IOException {
		//先查询下code不能重复
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		VendorSurveyTemplateEntity surveyTemplate_repeat = vendorSurveyTemplateDao.findByCodeAndIdNotIn(vendorSurveyTemplate.getCode(),Lists.newArrayList(vendorSurveyTemplate.getId())); 
		if(surveyTemplate_repeat!=null){
			map.put("success", false);
			map.put("msg", "编码已存在！");
			return map;
		}
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		vendorSurveyTemplate.setOrgId(user.orgId); 
		if(pathFile!=null && !pathFile.isEmpty()){
			File file = FileUtil.savefileFix(pathFile, PropertiesUtil.getProperty(TEMPLATEDIRPRO));
			vendorSurveyTemplate.setFileName(pathFile.getOriginalFilename());
			vendorSurveyTemplate.setPath(file.getPath());
		}
		vendorSurveyTemplateDao.save(vendorSurveyTemplate);
		return map;
	}

	public void deleteVendorSurveyTemplateList(List<VendorSurveyTemplateEntity> vendorSurveyTemplateList) {
		vendorSurveyTemplateDao.delete(vendorSurveyTemplateList);
	}
	
	public void loadingVendorSurveyTemplateList(List<VendorSurveyTemplateEntity> vendorSurveyTemplateList) {
		List<VendorSurveyCfgEntity> cds=new ArrayList<VendorSurveyCfgEntity>();
		for(VendorSurveyTemplateEntity vs:vendorSurveyTemplateList)
		{
			List<VendorSurveyCfgEntity> vscfgs=cfgDao.selectOrgSurveyCfgList(vs.getId());
			for(VendorSurveyCfgEntity vscfg:vscfgs)
			{
				VendorSurveyCfgEntity cd=new VendorSurveyCfgEntity();
				cd.setOrgId(vscfg.getOrgId());
				cd.setNavTemplateId(vscfg.getNavTemplateId());
				cd.setPhaseId(vscfg.getPhaseId());
				cd.setSurveyTemplateId(vs.getId());
				cd.setAuditStatus(0);
				cd.setApproveStatus(0);
				cd.setPhaseName(vscfg.getPhaseName());
				cd.setPhaseCode(vscfg.getPhaseCode());
				cd.setSurveyCode(vs.getCode());
				cd.setSurveyName(vs.getName());
				cd.setSubmitStatus(0);
				cd.setVendorPhasecfgId(vscfg.getVendorPhasecfgId());
				cd.setPhaseSn(vscfg.getPhaseSn());
				cds.add(cd);
			}
		}
		cfgDao.save(cds);
	}


	public List<VendorSurveyTemplateEntity> getAll() {
		List<VendorSurveyTemplateEntity> surveyTemplateList = (List<VendorSurveyTemplateEntity>) vendorSurveyTemplateDao.findAll(new Sort("id"));
		return surveyTemplateList;
	}
	
	//add by yao.jin 2017.02.23
	
	public List<VendorSurveyTemplateEntity> getAllByBuyerId(List<Long> buyerIds) {
		List<VendorSurveyTemplateEntity> surveyTemplateList = vendorSurveyTemplateDao.findByBuyerId(buyerIds);
		return surveyTemplateList;
	}
	//end add

    /**
     * 获取指定目录下的模版文件
     * @param rootPath
     */
	public void initTemplate() {
		String templateDir = PropertiesUtil.getProperty(TEMPLATEDIRPRO);
		File rootDir = new File(templateDir);
		if(rootDir.isDirectory()){
			File[] templateList = rootDir.listFiles();
			if(ArrayUtils.isEmpty(templateList)){
				return;
			}
			for(File file : templateList){
				//判断如果不是XML文件，则跳过
				String fileName = file.getName();
				if(file.isDirectory() || !FilenameUtils.isExtension(fileName, "xml")) {
					continue;
				}
				List<VendorSurveyTemplateEntity> surveyList = vendorSurveyTemplateDao.findByTemplateTypeAndFileName(VendorModuleTypeConstant.SUR_TEM_TYPE_XML,fileName);
				if(!CollectionUtils.isEmpty(surveyList)) {
					continue;
				}
				String filePath = file.getPath();
				VendorSurveyTemplateEntity survey = new VendorSurveyTemplateEntity();
				survey.setTemplateType(VendorModuleTypeConstant.SUR_TEM_TYPE_XML);
				//设置Code和Name默认为文件名吧
				survey.setCode(fileName.split("\\.")[0]);
				survey.setName(fileName.split("\\.")[0]);
				survey.setFileName(fileName);
				survey.setPath(filePath);
			    vendorSurveyTemplateDao.save(survey);
			}
			
		}
		
	}


	public Map<String, Object> getSurvey(Long id) throws DocumentException {
		VendorSurveyTemplateEntity vendorSurveyTemplate = vendorSurveyTemplateDao.findOne(id);
		Map<String,Object> map = new HashMap<String, Object>();
		if(vendorSurveyTemplate!=null && "base".equals(vendorSurveyTemplate.getCode())){
			map.put("template", vendorSurveyTemplate);
			return map;
		}
		Integer type = vendorSurveyTemplate.getTemplateType();
		SAXReader reader = new SAXReader();
		String templatePath = vendorSurveyTemplate.getPath();
		Document templateDoc = reader.read(new File(templatePath));
		String rootId = templateDoc.getRootElement().attributeValue("id");
		if(type.intValue()==VendorModuleTypeConstant.SUR_TEM_TYPE_XML){
			List<CtVo> ctList = SurveyUtil.parserSurveyXML(templateDoc);
			map.put("ctList", ctList);
		}
		map.put("templateId", vendorSurveyTemplate.getId());
		map.put("templatePath", vendorSurveyTemplate.getPath());
		map.put("templateCode", vendorSurveyTemplate.getCode());
		map.put("template", vendorSurveyTemplate);
		map.put("rootId", rootId);
		return map;
	}


}
