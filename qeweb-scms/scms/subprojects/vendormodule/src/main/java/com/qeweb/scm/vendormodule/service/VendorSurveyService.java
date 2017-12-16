package com.qeweb.scm.vendormodule.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.constants.VendorApplicationProConstant;
import com.qeweb.scm.vendormodule.entity.VendorBUEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.repository.VendorBUDao;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;
import com.qeweb.scm.vendormodule.repository.VendorPhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyBaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyDataDao;
import com.qeweb.scm.vendormodule.utils.SurveyStatusUtil;

@Service
@Transactional
public class VendorSurveyService {
	
	@Autowired
	private VendorSurveyCfgDao vendorSurveyCfgDao;
	@Autowired
	private VendorBUDao vendorBUDao;
	@Autowired
	private VendorSurveyDataDao vendorSurveyDataDao;
	@Autowired
	private VendorSurveyBaseDao vendorSurveyBaseDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private VendorPhaseCfgService vendorPhaseCfgService;
	@Autowired
	private VendorMaterialTypeRelDao vendorMaterialTypeRelDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private VendorPhaseDao vendorPhaseDao;	
	@Autowired
	private MailSendService mailSendService;
	
	private final static String SURVEYUPLOADDIR = "vendor.survey.updir";
	
	
	/**
	 * 暂存调查表信息，不提交
	 * @param survey 调查表信息
	 * @param orgId 当前组织ID
	 * @param trFiles 上传的文件
	 * @return 保存结果
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Transactional(rollbackOn=Exception.class) 
	public Map<String, Object> save(VendorSurveyBaseEntity survey, MultipartFile[] trFiles,long orgId) throws Exception {
		Map<String,Object> map = null;
		//有文件就要校验文件。IE下不能前端校验
		map = validateSurveyAttachment(trFiles);
		boolean validateFlag = (Boolean) map.get("success");
		if(!validateFlag){
			return map;
		}
		map = validateSurvey(survey);
		if(!validateFlag){
			return map;
		}
		//设置一个新的对象
	    VendorSurveyBaseEntity newSurvey = new VendorSurveyBaseEntity();
		//先取出子集和配置
		List<VendorSurveyDataEntity> itemList = survey.getItemList();
		if(Collections3.isEmpty(itemList)){
			//如果数据为空，应该返回错误
			map.put("success", false);
			map.put("msg", "数据为空，不能保存");
			return map;
		}
		Long surveyCfgId = survey.getVendorCfgId();
		//如果调查表初始
		if(survey.getId()<=0l){
			newSurvey = survey;
			newSurvey.setVersionNO(1);//1号版本
			newSurvey.setCurrentVersion(StatusConstant.STATUS_YES);
		    initSurveyStatus(newSurvey);
		}else{//如果已经不是初始数据
			survey = vendorSurveyBaseDao.findOne(survey.getId());
			//如果未提交，需删除原来记录 ,如果已提交,新增记录。版本，状态重置，
			if(survey.getSubmitStatus().intValue()!=StatusConstant.STATUS_YES){
				deleteOldSurvey(survey.getId());
				BeanUtil.copyPropertyNotNull(survey, newSurvey);
			}else{
				Integer versionNO = 1;//初始版本
				Integer currentVersion = StatusConstant.STATUS_YES;//是否当前版本
				if(survey!=null && survey.getId()>0l){//真正产生了记录，否则为初始版本
					versionNO = survey.getVersionNO()+1;
					currentVersion = StatusConstant.STATUS_NO;
				}
				BeanUtil.copyPropertyNotNull(survey,newSurvey);
				newSurvey.setVersionNO(versionNO);
				newSurvey.setCurrentVersion(currentVersion);
				initSurveyStatus(newSurvey);
				//同时将调查表配置的状态要变更
				VendorSurveyCfgEntity surveyCfg = vendorSurveyCfgDao.findOne(surveyCfgId);
				surveyCfg.setSubmitStatus(StatusConstant.STATUS_NO);
				surveyCfg.setAuditStatus(StatusConstant.STATUS_NO);
				vendorSurveyCfgDao.save(surveyCfg);
				
			}
		}
		newSurvey.setId(0);
		newSurvey.setOrgId(orgId);
		newSurvey = vendorSurveyBaseDao.save(newSurvey);
		for(VendorSurveyDataEntity item : itemList){
			item.setOrgId(orgId);
			item.setVendorCfgId(newSurvey.getVendorCfgId());
			item.setBaseId(newSurvey.getId());
		}
		vendorSurveyDataDao.save(itemList);
		//保存上传文件
		String upLoadFileDir = PropertiesUtil.getProperty(SURVEYUPLOADDIR);
		if(trFiles!=null && trFiles.length>0){
			File fileDir = new File(upLoadFileDir,newSurvey.getId()+"");
			if(!fileDir.exists()) {
				fileDir.mkdirs();
			}
			for(MultipartFile file : trFiles){
				if(file.isEmpty()) {
					continue;
				}
				File newFile = new File(fileDir,file.getOriginalFilename());
				file.transferTo(newFile);
			}
		}
		map.put("success", true);
		map.put("surveyBase", newSurvey);
		map.put("currentId", newSurvey.getId());
		map.put("submitStatus", newSurvey.getSubmitStatus());
		map.put("auditStatus", newSurvey.getAuditStatus());
		return map;
		
		
	}
	
	/**
	 * 校验附件是否超出限制
	 * @param trFiles
	 * @return
	 */
	private Map<String, Object> validateSurveyAttachment(MultipartFile[] trFiles) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String size = PropertiesUtil.getProperty(VendorApplicationProConstant.SURVEY_ATTACHMENT_SIZE, "0");
		if("0".equals(size)) {
			return map;
		}
		StringBuilder msg = new StringBuilder();
		for(MultipartFile file : trFiles){
			long fsize = file.getSize();
			long fsizeM = fsize/1024/1024;
			if(fsizeM>Long.parseLong(size)){
				msg.append(file.getOriginalFilename()+"超过上传限制("+size+"M)<br>");
			}
		}
		if(StringUtils.isNotEmpty(msg.toString())){
			map.put("success", false);
			map.put("msg", msg.toString());
		}
		return map;
	}

	/**
	 * 提交或保存时都要校验，如果存在已提交未审核的调查表，则不能提交和保存
	 * @param survey 调查表
	 * @return
	 */
	private Map<String, Object> validateSurvey(VendorSurveyBaseEntity survey) {
		Map<String, Object> map = new HashMap<String, Object>();
		VendorSurveyBaseEntity surveySubmited = vendorSurveyBaseDao.findByVendorCfgIdAndSubmitStatusAndAuditStatus(survey.getVendorCfgId(),StatusConstant.STATUS_YES,StatusConstant.STATUS_NO);
		map.put("success", true);
		if(surveySubmited!=null){
			//处理下对应的调查表配置
			VendorSurveyCfgEntity cfg = vendorSurveyCfgDao.findOne(surveySubmited.getVendorCfgId());
			if(cfg!=null && (cfg.getSubmitStatus()==null || cfg.getSubmitStatus().intValue()==StatusConstant.STATUS_NO)){
				cfg.setSubmitStatus(StatusConstant.STATUS_YES);
				vendorSurveyCfgDao.save(cfg);
			}
			map.put("success", false);
			map.put("msg", "已有提交数据，请刷新页面");
		}
		return map;
	}

	/**
	 * 初始化调查表的状态
	 * @param survey 调查表对象
	 */
	private void initSurveyStatus(VendorSurveyBaseEntity survey) {
		survey.setSubmitStatus(StatusConstant.STATUS_NO);
		survey.setAuditStatus(StatusConstant.STATUS_NO);
		survey.setApproveStatus(StatusConstant.STATUS_NO);
		survey.setAuditUser("");
		survey.setAuditReason("");
	}


	/**
	 * 删除已经存在的调查表信息
	 * @param id 调查表基本信息ID
	 */
	private void deleteOldSurvey(long id) {
		//删除数据
		vendorSurveyDataDao.deleteByBaseId(id);
		//删除基本信息
		vendorSurveyBaseDao.delete(id);
		
	}


	/**
	 * 保存供应商基本信息
	 * @param baseInfo 基本信息
	 * @param orgId 供应商ID
	 * @return 供应商基本信息
	 */
	public Map<String, Object> save(VendorBaseInfoEntity baseInfo, Long orgId) {
		Map<String, Object> map = validateBaseInfo(baseInfo);
		boolean validateFlag = (Boolean) map.get("success");
		if(!validateFlag){
			return map;
		}
		//base要重新copy
		VendorBaseInfoEntity base = vendorBaseInfoDao.findOne(baseInfo.getId());
		Integer versionNO = base.getVersionNO();
		BeanUtil.copyPropertyNotNull(baseInfo, base);
		if(versionNO==null){
			base.setVersionNO(1);
			base.setCurrentVersion(StatusConstant.STATUS_YES);
		}
		//如果已经提交了，属于变更。产生新版本
		if(base.getSubmitStatus()!=null && base.getSubmitStatus().intValue()==StatusConstant.STATUS_YES){
			VendorBaseInfoEntity newBase = new VendorBaseInfoEntity();
			BeanUtil.copyPropertyNotNull(base, newBase);
			newBase.setId(0l);
			newBase.setVersionNO(versionNO+1);
			newBase.setSubmitStatus(StatusConstant.STATUS_NO);
			newBase.setCurrentVersion(StatusConstant.STATUS_NO);
			newBase.setAuditStatus(StatusConstant.STATUS_NO);
			newBase.setAuditReason("");
			base = newBase;
			//同时将调查表配置的状态要变更
			VendorSurveyCfgEntity surveyCfg = vendorSurveyCfgDao.findBySurveyCodeAndOrgId("base",orgId);
			surveyCfg.setSubmitStatus(StatusConstant.STATUS_NO);
			surveyCfg.setAuditStatus(StatusConstant.STATUS_NO);
			vendorSurveyCfgDao.save(surveyCfg);
		}
		
		base = vendorBaseInfoDao.save(base);
		map.put("success", true);
		map.put("baseInfo", base);
		map.put("orgId", base.getOrgId());
		map.put("currentId", base.getId());
		map.put("submitStatus", base.getSubmitStatus());
		map.put("auditStatus", base.getAuditStatus());
		return map;
	}

	/**
	 * 校验供应商基本信息
	 * @param baseInfo 基本信息
	 * @return
	 */
	private Map<String, Object> validateBaseInfo(VendorBaseInfoEntity baseInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		VendorBaseInfoEntity baseInfoSubmited = vendorBaseInfoDao.findByOrgIdAndSubmitStatusAndAuditStatus(baseInfo.getOrgId(),StatusConstant.STATUS_YES,StatusConstant.STATUS_NO);
		map.put("success", true);
		if(baseInfoSubmited!=null){
			//处理下对应的调查表配置
			VendorSurveyCfgEntity cfg = vendorSurveyCfgDao.findBySurveyCodeAndOrgId("base",baseInfoSubmited.getOrgId());
			if(cfg.getSubmitStatus()==null || cfg.getSubmitStatus().intValue()==StatusConstant.STATUS_NO){
				cfg.setSubmitStatus(StatusConstant.STATUS_YES);
				vendorSurveyCfgDao.save(cfg);
			}
			map.put("success", false);
			map.put("msg", "已有提交数据，请刷新页面");
		}
		return map;
	}

	/**
	 * 提交供应商基本信息
	 * @param baseInfo 基本信息
	 * @param orgId 供应商ID
	 * @return 供应商基本信息
	 */
	public Map<String, Object> submit(VendorBaseInfoEntity baseInfo, Long orgId) {
		//base要重新copy
		Map<String,Object> map = save(baseInfo, orgId);
		boolean validateFlag = (Boolean) map.get("success");
		if(!validateFlag){
			return map;
		}
		//提交基本信息，获取调查表配置,基本信息默认为1
		VendorSurveyCfgEntity surveyCfg = vendorSurveyCfgDao.findBySurveyTemplateIdAndOrgId(1l,orgId);
		surveyCfg.setSubmitStatus(StatusConstant.STATUS_YES);
		surveyCfg.setAuditStatus(StatusConstant.STATUS_NO);
		vendorSurveyCfgDao.save(surveyCfg);
		
		//同时将调查表配置的状态要变更
		
		baseInfo = (VendorBaseInfoEntity) map.get("baseInfo");
		baseInfo.setSubmitStatus(StatusConstant.STATUS_YES);
		baseInfo.setAuditStatus(StatusConstant.STATUS_NO);
		baseInfo.setAuditUser("");
		baseInfo.setAuditReason("");
		baseInfo.setCurrentVersion(StatusConstant.STATUS_YES);
		baseInfo = vendorBaseInfoDao.save(baseInfo);
		//将其他基本信息置为非当前版本
		List<VendorBaseInfoEntity> otherList = vendorBaseInfoDao.findByOrgIdAndIdNot(baseInfo.getOrgId(),baseInfo.getId());
		for(VendorBaseInfoEntity other : otherList){
			other.setCurrentVersion(StatusConstant.STATUS_NO);
		}
		if(Collections3.isNotEmpty(otherList)) {
			vendorBaseInfoDao.save(otherList);
		}
		
		map.put("success", true);
		map.put("surveyCfgId", surveyCfg.getId());
		map.put("submitStatus", baseInfo.getSubmitStatus());
		map.put("auditStatus", baseInfo.getAuditStatus());
		//更新整个供应商的提交状态
		changeOrgSubmit(baseInfo.getOrgId());
		return map;
	}
	
	/**
	 * 整体改变调查表状态
	 * @param orgId 组织ID
	 */
	private void changeOrgSubmit(Long orgId) {
		OrganizationEntity org = orgDao.findOne(orgId);
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findByOrgIdAndCurrentVersion(orgId, StatusConstant.STATUS_YES);
		//获取该阶段和以前所有的调查表
		int total = 0,submitTotal = 0,auditTotal = 0;
		List<VendorSurveyCfgEntity> vendorSurveyCfgList = vendorSurveyCfgDao.findByPhaseSnLessThanEqualAndOrgId(vendor.getPhaseSn(),orgId);
		if(Collections3.isEmpty(vendorSurveyCfgList)) {
			return;
		}
		total = vendorSurveyCfgList.size();
		for(VendorSurveyCfgEntity vendorSurCfg : vendorSurveyCfgList){
			if(vendorSurCfg.getSubmitStatus() == StatusConstant.STATUS_YES) {
				submitTotal++;
			} 
			if(vendorSurCfg.getAuditStatus() == StatusConstant.STATUS_YES) {
				auditTotal++;
			}
		}
		if(submitTotal==total){
			org.setSubmitStatus(StatusConstant.STATUS_YES);
		}else if(submitTotal<total && submitTotal!=0){
			org.setSubmitStatus(StatusConstant.STATUS_PART);
		}else{
			org.setSubmitStatus(StatusConstant.STATUS_NO);
		}
		if(auditTotal==total){
			org.setAuditStatus(StatusConstant.STATUS_YES);
		}else if(auditTotal<total && auditTotal!=0){
			org.setAuditStatus(StatusConstant.STATUS_PART);
		}else{
			org.setAuditStatus(StatusConstant.STATUS_NO);
		}
		//设定一下审核顺序
		int waitAudtit = submitTotal-auditTotal;
		org.setAuditSn(waitAudtit);
		orgDao.save(org);
	}

	/**
	 * 提交调查表信息
	 * @param survey 调查表信息
	 * @param surveyCfgId 
	 * @param orgId 当前组织ID
	 * @return 保存结果
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Transactional(rollbackOn=Exception.class) 
	public Map<String, Object> submit(VendorSurveyBaseEntity survey,MultipartFile[] trFiles,Long surveyCfgId, long orgId) throws Exception {
		Map<String,Object> map = save(survey,trFiles, orgId);
		if(!(Boolean) map.get("success")){
			return map;
		}
		survey = (VendorSurveyBaseEntity) map.get("surveyBase");
		//提交基本信息，获取调查表配置,基本信息默认为1
		VendorSurveyCfgEntity surveyCfg = vendorSurveyCfgDao.findOne(surveyCfgId);
		surveyCfg.setSubmitStatus(StatusConstant.STATUS_YES);
		vendorSurveyCfgDao.save(surveyCfg);
		//将调查表状态也要更新
		survey.setSubmitStatus(StatusConstant.STATUS_YES);
		survey.setCurrentVersion(StatusConstant.STATUS_YES);
		survey = vendorSurveyBaseDao.save(survey);
		//更新其他调查表为非当前调查表
		List<VendorSurveyBaseEntity> otherList = vendorSurveyBaseDao.findByVendorCfgIdAndIdNot(surveyCfgId,survey.getId());
		for(VendorSurveyBaseEntity other : otherList){
			other.setCurrentVersion(StatusConstant.STATUS_NO);
		}
		if(Collections3.isNotEmpty(otherList)) {
			vendorSurveyBaseDao.save(otherList);
		}
		
		map.put("success", true);
		map.put("surveyCfgId", surveyCfg.getId());
		map.put("submitStatus", survey.getSubmitStatus());
		map.put("auditStatus", survey.getAuditStatus());
		changeOrgSubmit(orgId);
		return map;
		
		
	}

    /**
     * 根据组织获取改用户的调查表维护配置信息，主要包含基本信息和基本信息的配置信息
     * 供应商维护使用
     * @param httpServletRequest 
     * @param id 当前用户ID
     * @return
     */
	public Map<String, Object> getSurveyManagerInfo(Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//供应商需获取最大版本的信息
		List<VendorBaseInfoEntity> vendorList = vendorBaseInfoDao.findByOrgIdOrderByVersionNODesc(orgId);
		VendorBaseInfoEntity vendor = new VendorBaseInfoEntity();
		if(vendorList.size()>0)
		{
			vendor= vendorList.get(0);
			if(vendor!=null){
				VendorPhaseEntity phaseEntity = vendorPhaseDao.findOne(vendor.getPhaseId());
				map.put("vendorPhase", phaseEntity);
			}
		}
		VendorSurveyCfgEntity vendorSurveyCfg = vendorSurveyCfgDao.findBySurveyTemplateIdAndOrgId(1l,orgId);
		map.put("orgId", vendor.getOrgId());
		map.put("vendor", vendor);
		map.put("vendorSurveyCfg", vendorSurveyCfg);
		List<VendorBUEntity> list=(List<VendorBUEntity>) vendorBUDao.findAll();
		map.put("vendorBUs",list);
		return map;
	}
	
	/**
     * 根据组织获取改用户的调查表审核配置信息，主要包含基本信息和基本信息的配置信息
     * 审核使用
     * @param id 当前组织ID
     * @return
     */
	public Map<String, Object> getSurveyAuditInfo(Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//采购商获取当前版本
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findByOrgIdAndCurrentVersion(orgId,StatusConstant.STATUS_YES);
		String bus="";
		String mainBU=vendor.getMainBU();
		if(mainBU!=null&&mainBU.equals(""))
		{
			String[] buY=mainBU.split(",");
			for(int i=0;i<buY.length;i++)
			{
				VendorBUEntity vb=vendorBUDao.findByCodes(buY[i]);
				if(i!=0)
				{
					bus=bus+","+vb.getName();
				}
				else
				{
					bus=vb.getName();
				}
			}
		}
		vendor.setMainBUss(bus);
		VendorSurveyCfgEntity vendorSurveyCfg = vendorSurveyCfgDao.findBySurveyTemplateIdAndOrgId(1l,orgId);
		//需要判读是不是已经为最后阶段
		boolean isEndPhase = vendorPhaseCfgService.validateIsEndPhase(vendor);
		map.put("isEndPhase",isEndPhase);
		map.put("vendor", vendor);
		map.put("vendorBU",vendorBUDao.findAll());
		map.put("vendorSurveyCfg", vendorSurveyCfg);
		map.put("orgId", vendor.getOrgId());
		return map;
	}


	/**
     * 审核调查表
     * @param surveyCfg 调查表配置
     * @param auditUser 审核人
     * @return 审核结果
     */
	public Map<String, Object> auditSurvey(VendorSurveyCfgEntity surveyCfg, String auditUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		surveyCfg.setAuditUser(auditUser);
		surveyCfg = vendorSurveyCfgDao.save(surveyCfg);
		//当前匹配的调查表也需要设置状态
		String code = surveyCfg.getSurveyCode();
		if("base".equals(code)){
			VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findByOrgIdAndCurrentVersion(surveyCfg.getOrgId(), StatusConstant.STATUS_YES);
			vendorBaseInfo.setAuditStatus(surveyCfg.getAuditStatus());
			vendorBaseInfo.setAuditReason(surveyCfg.getAuditReason());
			vendorBaseInfo.setAuditUser(auditUser);
			vendorBaseInfoDao.save(vendorBaseInfo);
			map.put("currentId", vendorBaseInfo.getId());
			map.put("ctId", vendorBaseInfo.getOrgId());
		}else{
			VendorSurveyBaseEntity survey = null;
			List<VendorSurveyBaseEntity> surveys = vendorSurveyBaseDao.findByVendorCfgIdAndCurrentVersionOrderByIdDesc(surveyCfg.getId(),StatusConstant.STATUS_YES);
			VendorSurveyBaseEntity survey2 = null;
			if(surveys!=null&&surveys.size()>0)
			{
				if(surveys.size()>1)
				{
					for(int i=1;i<surveys.size();i++)
					{
						survey2=surveys.get(i);
						survey2.setCurrentVersion(0);
						vendorSurveyBaseDao.save(survey2);
					}
				}
				survey =surveys.get(0);
			}
			survey.setAuditStatus(surveyCfg.getAuditStatus());
			survey.setAuditReason(surveyCfg.getAuditReason());
			survey.setAuditUser(auditUser);
			vendorSurveyBaseDao.save(survey);
			map.put("currentId", survey.getId());
			map.put("ctId", surveyCfg.getId());
		}
		
		map.put("success", true);
		map.put("surveyCfg", surveyCfg);
		map.put("statusIcon", SurveyStatusUtil.getSurveyStatusIcon(surveyCfg.getSubmitStatus(), surveyCfg.getAuditStatus()));
		changeOrgSubmit(surveyCfg.getOrgId());
		return map;
	}


	/**
	 * 基本调查表查看页面
	 * @param id 当前基本调查表ID
	 * @param request 
	 * @return 基本调查表信息
	 */
	public Map<String, Object> getBaseSurveyInfo(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(id);
		map.put("vendor", vendorBaseInfo);
		map.put("vendorBU",vendorBUDao.findAll());
		return map;
	}


	/**
	 * 获取供应商调查表历史，供应商获取的是除开的当前的
	 * @param cfgId 配置ID
	 * @param currentId 当前调查表ID
	 * @return 历史信息集合
	 */
	public List<VendorSurveyBaseEntity> getVendorSurveyInfoHisList(Long cfgId, Long currentId) {
		if(currentId==null) {
			return Lists.newArrayList();
		}
		List<VendorSurveyBaseEntity> surveyBaseList = vendorSurveyBaseDao.findByVendorCfgIdAndSubmitStatusOrderByVersionNODesc(cfgId,StatusConstant.STATUS_YES);
		return surveyBaseList;
	}

	/**
	 * 获取供应商调查表历史-审核时是取小于当前的版本的历史
	 * @param cfgId
	 * @param currentId
	 * @return
	 */
	public List<VendorSurveyBaseEntity> getVendorSurveyInfoAuditHisList(Long cfgId, Long currentId) {
		if(currentId==null) {
			return Lists.newArrayList();
		}
		VendorSurveyBaseEntity surveyBase = vendorSurveyBaseDao.findOne(currentId);
		List<VendorSurveyBaseEntity> surveyBaseList = vendorSurveyBaseDao.findByVendorCfgIdAndVersionNOLessThanEqualOrderByVersionNODesc(cfgId,surveyBase.getVersionNO());
		return surveyBaseList;
	}
	
	public Page<VendorSurveyDataEntity> getVendorSurveyDataList(Integer pageNumber,
			Integer pageSize, Map<String, Object> searchParamMap) {
		Page<VendorSurveyDataEntity> page = null;
		//当前时间
		Timestamp tt = new Timestamp(System.currentTimeMillis()); 
		Calendar cc = Calendar.getInstance(); 
		cc.setTime(tt); 
		//当前时间加45天
		cc.add(Calendar.DAY_OF_MONTH, 45); 
//		System.out.println(cc.getTime()); 
//		Timestamp time = DateUtil.stringToTimestamp(DateUtil.dateToString(cc.getTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD));
		String date = DateUtil.dateToString(cc.getTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
		searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("LIKE_ctId","%systemCertificate%");
		searchParamMap.put("EQ_vendorSurveybase.currentVersion","1");
		searchParamMap.put("EQ_vendorSurveybase.auditStatus","1");
		searchParamMap.put("LTE_col5","@"+date);

		
		//查询所有当前版本资质
		if(pageNumber==null&&pageSize==null){
			Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
			Specification<VendorSurveyDataEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorSurveyDataEntity.class);
			List<VendorSurveyDataEntity> list = vendorSurveyDataDao.findAll(spec);
			page = new PageImpl<VendorSurveyDataEntity>(list);
		}else{
			PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
			Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
			Specification<VendorSurveyDataEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorSurveyDataEntity.class);
			page = vendorSurveyDataDao.findAll(spec, pagin);
		}
		for(VendorSurveyDataEntity v:page.getContent() )
		{
			VendorBaseInfoEntity vendorBaseInfoEntity=vendorBaseInfoDao.findByOrgIdAndCurrentVersion(v.getOrgId(),StatusConstant.STATUS_YES);
			v.setVendorBaseInfoEntity(vendorBaseInfoEntity);
		}
		return page;
	}

	public void toemilQWE(List<VendorSurveyDataEntity> vendorSurveyDataEntitys) {
		for(VendorSurveyDataEntity vendorSurveyDataEntity :vendorSurveyDataEntitys)
		{
			MailObject mo = new MailObject();
			VendorBaseInfoEntity vendorBaseInfoEntity=vendorBaseInfoDao.findByOrgIdAndCurrentVersion(vendorSurveyDataEntity.getOrgId(),StatusConstant.STATUS_YES);
			mo.toMail = vendorSurveyDataEntity.getOrganizationEntity().getEmail();
			mo.templateName = "qualifications";
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorName",vendorBaseInfoEntity.getName());
			params.put("curr_date",""+new Date());
			params.put("fileName",vendorSurveyDataEntity.getCol1());
			mo.params = params;
			mo.title = "资质预警提醒";
			mailSendService.send(mo, 5);
		}
	}

	/**
	 * 检验是否存在未填写的关键二级供应商
	 * @param orgId 供应商ID
	 * @param mainProIds 主要产品ID集合
	 * @return
	 */
	public Map<String, Object> validateSecVen(Long orgId, String mainProIds) {
		List<VendorMaterialTypeRelEntity> typeRelList = vendorMaterialTypeRelDao.findByOrgId(orgId);
		String[] mainProIdArray = mainProIds.split(",");
		List<String> idList = Lists.newArrayList(mainProIdArray);
		StringBuilder msg = new StringBuilder();
		for(VendorMaterialTypeRelEntity typeRel : typeRelList){
			//查询下是否需要二级关键供应商
			MaterialTypeEntity matType = materialTypeDao.findByIdAndNeedSecondVendorAndAbolished(typeRel.getMaterialTypeId(),StatusConstant.STATUS_YES,StatusConstant.STATUS_NO);
			if(matType==null){
				continue;
			}
			if(idList.contains(typeRel.getId()+"")){
				continue;
			}
			msg.append("主要产品【"+matType.getName()+"】需要填写二级供应商信息！<br>");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", StringUtils.isEmpty(msg.toString())?true:false);
		map.put("msg", msg.toString());
		return map;
	}

}
