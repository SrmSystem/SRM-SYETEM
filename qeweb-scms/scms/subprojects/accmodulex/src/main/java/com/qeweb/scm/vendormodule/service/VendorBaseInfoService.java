package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.persistence.SearchFilterEx.Operator;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.constants.TypeConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.StatusDictEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.AreaDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.StatusDictDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.constants.VendorModuleTypeConstant;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoExEntity;
import com.qeweb.scm.vendormodule.entity.VendorChangeHisEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity;
import com.qeweb.scm.vendormodule.repository.BuyerVendorRelDao;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoExDao;
import com.qeweb.scm.vendormodule.repository.VendorChangeHisDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.VendorNavTemplateDao;
import com.qeweb.scm.vendormodule.repository.VendorPhaseCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorPhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyBaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplatePhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorTemplateSurveyDao;
import com.qeweb.scm.vendormodule.vo.StatisticsVendorCountTransfer;
import com.qeweb.scm.vendormodule.vo.VendorAdmittanceTransfer;

@Service
@Transactional
public class VendorBaseInfoService {
	
	@Autowired
	protected VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	protected OrganizationDao orgDao;
	@Autowired
	protected VendorNavTemplateDao vendorNavTemplateDao;
	@Autowired
	protected VendorTemplatePhaseDao vendorTemplatePhaseDao;
	@Autowired
	protected VendorTemplateSurveyDao vendorTemplateSurveyDao;
	@Autowired
	protected UserDao userDao;
	@Autowired
	protected RoleUserDao roleUserDao;
	@Autowired
	protected VendorPhaseDao vendorPhaseDao;
	@Autowired
	protected VendorPhaseCfgDao vendorPhaseCfgDao;
	@Autowired
	protected VendorSurveyCfgDao vendorSurveyCfgDao;
	@Autowired
	protected VendorBaseInfoExDao vendorBaseInfoExDao;
	@Autowired
	protected AreaDao areaDao;
	@Autowired
	protected MaterialTypeDao materialTypeDao;
	@Autowired
	protected VendorChangeHisDao vendorChangeHisDao;
	@Autowired
	protected VendorPhaseCfgService vendorPhaseCfgService;
	@Autowired
	protected MailSendService mailSendService;
	@Autowired
	protected VendorSurveyBaseDao vendorSurveyBaseDao;
	@Autowired
	protected StatusDictDao statusDictDao;
	@Autowired
	protected GenerialDao daoImpl;
	@Autowired
	protected VendorMaterialRelDao vendorMaterialRelDao;
	
	@Autowired
	private VendorMaterialRelDao venMatRelDao;
	@Autowired
	private BuyerVendorRelDao buyerVendorRelDao;
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;



	
    /**
     * 查询供应商集合，默认为当前版本
     * @param pageNumber 页数
     * @param pageSize 每页大小
     * @param searchParamMap 查询参数
     * @return 供应商集合
     */
	public Page<VendorBaseInfoEntity> getVendorList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorBaseInfoEntity.class);
		Page<VendorBaseInfoEntity> vendorBaseInfoPage = vendorBaseInfoDao.findAll(spec,pagin);
		return vendorBaseInfoPage;
	}


	public void addNewVendorBaseInfo(VendorBaseInfoEntity vendorBaseInfo) {
		vendorBaseInfoDao.save(vendorBaseInfo);
	}

	public VendorBaseInfoEntity getVendorBaseInfo(Long id) {
		return vendorBaseInfoDao.findOne(id);
	}

	public void updateVendorBaseInfo(VendorBaseInfoEntity vendorBaseInfo) {
		vendorBaseInfoDao.save(vendorBaseInfo);
	}

	public void deleteVendorBaseInfoList(List<VendorBaseInfoEntity> vendorBaseInfoList) {
		//vendorBaseInfoDao.delete(vendorBaseInfoList);
		for (VendorBaseInfoEntity vendorBaseInfoEntity : vendorBaseInfoList) {
			vendorBaseInfoDao.abolish(vendorBaseInfoEntity.getId());
		}
	}


	/**
	 * 确认供应商意向
	 * @param vendorBaseInfoList 供应商基本信息集合
	 * @param basePath 
	 * @return 供应商确认结果
	 */
	public Map<String, Object> confirm(List<VendorBaseInfoEntity> vendorBaseInfoList, String basePath,  Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoList){
			long buyerId=buyerOrgPermissionUtil.getBuyerIdsByOrgId(vendorBaseInfo.getOrgId());
			vendorBaseInfo = vendorBaseInfoDao.findOne(vendorBaseInfo.getId());
			OrganizationEntity org = vendorBaseInfo.getOrg();
			//判断是否已经确认，如果已经确认，跳过
			if(org.getConfirmStatus().intValue()==StatusConstant.STATUS_YES) {
				continue;
			}
			org.setConfirmStatus(StatusConstant.STATUS_YES);
			orgDao.save(org);
			//开始设置晋级配置
			setVendorCfg(org,vendorBaseInfo, buyerId,map);
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorName", org.getName());
			params.put("message", "<a href="+basePath+">点击访问</a>");
			params.put("link", "<a href="+basePath+">"+basePath+"</a>");
//			mailSendService.send(org.getEmail(),"注册信息已被确认！","defaultTemp", params,3);
			MailObject mo = new MailObject();
			mo.params = params;
			mo.title = "注册信息已被确认！";
			mo.toMail = org.getEmail();
			mailSendService.send(mo, 3);
		}
		map.put("success", true);
		return map;
	}
	public Map<String, Object> confirm2(List<VendorBaseInfoEntity> vendorBaseInfoList) {

		Map<String, Object> map = new HashMap<String, Object>();
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoList){
			vendorBaseInfo = vendorBaseInfoDao.findOne(vendorBaseInfo.getId());
			OrganizationEntity org = vendorBaseInfo.getOrg();
			//判断是否已经确认，如果已经确认，跳过
			if(org.getConfirmStatus().intValue()==StatusConstant.STATUS_YES) {
				continue;
			}
			org.setConfirmStatus(StatusConstant.STATUS_YES);
			orgDao.save(org);
			//开始设置晋级配置 TODO:::
			setVendorCfg(org,vendorBaseInfo, null,map);
		}
		map.put("success", true);
		return map;
	}
	
	/**
	 * 确认供应商意向
	 * @param vendorBaseInfoList 供应商基本信息集合
	 * @param basePath 
	 * @return 供应商确认结果
	 */
	public Map<String, Object> confirmReject(List<VendorBaseInfoEntity> vendorBaseInfoList, String basePath) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoList){
			vendorBaseInfo = vendorBaseInfoDao.findOne(vendorBaseInfo.getId());
			OrganizationEntity org = vendorBaseInfo.getOrg();
			//判断是否已经确认，如果已经确认，跳过
			if(org.getConfirmStatus().intValue()==StatusConstant.STATUS_REJECT) {
				continue;
			}
			org.setConfirmStatus(StatusConstant.STATUS_REJECT);
			orgDao.save(org);
			Map<String, String> params = new HashMap<String, String>();
			params.put("vendorName", org.getName());
			params.put("message", "<a href="+basePath+">点击访问</a>");
			params.put("link", "<a href="+basePath+">"+basePath+"</a>");
			mailSendService.send(org.getEmail(),"注册信息已被驳回！","defaultTemp", params,4);
		}
		map.put("success", true);
		return map;
	}


	/**
	 * 对供应商进行配置
	 * @param org 组织
	 * @param vendorBaseInfo 供应商基本信息
	 */
	private Map<String, Object> setVendorCfg(OrganizationEntity org, VendorBaseInfoEntity vendorBaseInfo, Long orgId,Map<String, Object> map) {
		//先获取供应商的默认模版
		VendorNavTemplateEntity defaultTemplate = vendorNavTemplateDao.findByDefaultFlagAndAbolishedAndOrgId(StatusConstant.STATUS_YES, 
				StatusConstant.STATUS_NO, orgId);
		if(defaultTemplate == null){
			map.put("text", "该供应商所属采购组织尚无默认向导模板");
			return map;
		}
		//为该供应商指定模版
		vendorBaseInfo.setTemplateId(defaultTemplate.getId());
		vendorBaseInfoDao.save(vendorBaseInfo);
		//配置供应商阶段和供应商调查表
		List<VendorTemplatePhaseEntity> templatePhaseList = vendorTemplatePhaseDao.findByTemplateIdOrderByPhaseSnAsc(defaultTemplate.getId());
		List<VendorPhaseCfgEntity> vendorPhaseCfgList = new ArrayList<VendorPhaseCfgEntity>();
		for(VendorTemplatePhaseEntity templatePhase : templatePhaseList){
			VendorPhaseCfgEntity vendorPhaseCfg = new VendorPhaseCfgEntity();
			vendorPhaseCfg.setOrgId(org.getId());
			vendorPhaseCfg.setPhaseId(templatePhase.getPhaseId());
			vendorPhaseCfg.setPhaseSn(templatePhase.getPhaseSn());
			vendorPhaseCfg.setPhaseCode(templatePhase.getCode());
			vendorPhaseCfg.setPhaseName(templatePhase.getName());
			vendorPhaseCfg.setTemplateId(templatePhase.getTemplateId());
			vendorPhaseCfg.setVendorId(vendorBaseInfo.getId());
			vendorPhaseCfgDao.save(vendorPhaseCfg);
			//初始化调查表
			//获取配置下的调查表
			List<VendorTemplateSurveyEntity> templateSurveyList = vendorTemplateSurveyDao.findByTemplatePhaseIdOrderByIdAsc(templatePhase.getId());
			for(VendorTemplateSurveyEntity templateSurvey: templateSurveyList){
				VendorSurveyCfgEntity vendorSurveyCfg = new VendorSurveyCfgEntity();
				vendorSurveyCfg.setApproveStatus(0);
				vendorSurveyCfg.setAuditStatus(0);
				vendorSurveyCfg.setSubmitStatus(0);
				vendorSurveyCfg.setNavTemplateId(vendorPhaseCfg.getTemplateId());
				vendorSurveyCfg.setOrgId(org.getId());
				vendorSurveyCfg.setPhaseCode(vendorPhaseCfg.getPhaseCode());
				vendorSurveyCfg.setPhaseId(vendorPhaseCfg.getPhaseId());
				vendorSurveyCfg.setPhaseName(vendorPhaseCfg.getPhaseName());
				vendorSurveyCfg.setPhaseSn(vendorPhaseCfg.getPhaseSn());
				vendorSurveyCfg.setSurveyName(templateSurvey.getSurveyName());
				vendorSurveyCfg.setSurveyCode(templateSurvey.getSurveyCode());
				vendorSurveyCfg.setSurveyTemplateId(templateSurvey.getSurveyTemplateId());
				vendorSurveyCfg.setSurveyTemplateSn(templateSurvey.getSurveyTemplateSn());
				vendorSurveyCfg.setVendorPhasecfgId(vendorPhaseCfg.getId());
				vendorSurveyCfgDao.save(vendorSurveyCfg);
			}
			vendorPhaseCfgList.add(vendorPhaseCfg);
			
		}
		VendorPhaseCfgEntity nextPhaseCfg = null;
		for(VendorPhaseCfgEntity phaseCfg : vendorPhaseCfgList){
			if(phaseCfg.getPhaseSn()==1){
				nextPhaseCfg = phaseCfg;
				break;
			}
			
		}
		if(nextPhaseCfg!=null){
			setVendorNextPhase(org,vendorBaseInfo,nextPhaseCfg);
			
		}
		return map;
	}
	



	/**
	 * 批量晋级
	 * @param vendorBaseInfoList 供应商基本信息列表
	 * @param changeUser 变更用户
	 * @return 批量晋级信息
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String,Object> promteList(List<VendorBaseInfoEntity> vendorBaseInfoList,String auditMsg, String changeUser) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		StringBuffer errorMsg = new StringBuffer();
		Long phaseId = null;
		for(VendorBaseInfoEntity baseInfo : vendorBaseInfoList){
			VendorBaseInfoEntity existBaseInfo = vendorBaseInfoDao.findOne(baseInfo.getId());
			BeanUtil.copyPropertyNotNull(baseInfo,existBaseInfo);
			VendorBaseInfoEntity newBaseInfo = existBaseInfo;
			if(newBaseInfo.getPhaseId() == null){
				phaseId = baseInfo.getPhaseId();
				map.put("phaseId", phaseId);
			}
			map = promte(newBaseInfo.getOrg(),newBaseInfo,auditMsg,changeUser);
			boolean success = (Boolean) map.get("success");
			if(!success){
				errorMsg.append(map.get("msg"));
			}
		}
		if(StringUtils.isNotEmpty(errorMsg.toString())){
			map.put("success", false);
			map.put("msg", errorMsg.toString());
		}
		map.put("msg",StringUtils.encode( ""+ map.get("msg")));
		return map;
	}


    
    /**
     * 校验供应商是否可以晋级，如果不能晋级，就返回信息，如果可以晋级，就返回空信息
     * @param curVendorPhase 当前供应商配置阶段
     * @return 消息
     */
    protected String validatePromote(VendorPhaseCfgEntity curVendorPhase) {
		List<VendorSurveyCfgEntity> surveyCfgList = vendorSurveyCfgDao.findByPhaseSnLessThanEqualAndOrgId(curVendorPhase.getPhaseSn(), curVendorPhase.getOrgId());
		StringBuffer msg = new StringBuffer();
		int i=0;
		msg.append("<table style='width: 100%;'>");
		for(VendorSurveyCfgEntity surveyCfg : surveyCfgList){
			if(surveyCfg.getAuditStatus()!=StatusConstant.STATUS_YES){
				msg.append("<tr><td>调查表【").append(surveyCfg.getSurveyName()+"】</td>").append("<td style='color: #D00000;'>未通过审核</td></tr><tr><td>&nbsp;</td>&nbsp;<td></td></tr>");
				i++;
			}
			
		}
		msg.append("</table>");
		if(i==0)
		{
			msg = new StringBuffer();
		}
		return msg.toString();
	}


	/**
     * 设置供应商的下一个阶段
     * @param org 组织
     * @param vendorBaseInfo 供应商基本信息
     * @param nextPhaseCfg 下一个阶段配置
     */
    protected void setVendorNextPhase(OrganizationEntity org, VendorBaseInfoEntity vendorBaseInfo, VendorPhaseCfgEntity nextPhaseCfg) {
		VendorPhaseEntity vendorPhase = vendorPhaseDao.findOne(nextPhaseCfg.getPhaseId());
		vendorBaseInfo.setPhaseId(vendorPhase.getId());
		vendorBaseInfo.setPhaseSn(nextPhaseCfg.getPhaseSn());
		vendorBaseInfo.setPhaseCfgId(nextPhaseCfg.getId());
		vendorBaseInfo = vendorBaseInfoDao.save(vendorBaseInfo);
		
		Long roleId = vendorPhase.getRoleId();
		//如果没有关联角色，则无需再分配
		if(roleId==null){
			return;
		}
		
		//获得对应的供应商帐号，并晋级
		List<UserEntity> userList = userDao.findByCompany(org.getId());
		for(UserEntity user : userList){
			//要查询该用户是否有这个角色，有的话，则不需增加
			RoleUserEntity exsitRoleUser = roleUserDao.findByUserIdAndRoleId(user.getId(),roleId);
			if(exsitRoleUser!=null) {
				continue;
			}
			RoleUserEntity roleUser = new RoleUserEntity();
			roleUser.setRoleId(roleId);
			roleUser.setUserId(user.getId());
			roleUserDao.save(roleUser);
		}
	}


	/**
     * 获得所有阶段
     * @return 供应商阶段
     */
	public List<VendorPhaseEntity> getAllPhase() {
		return (List<VendorPhaseEntity>) vendorPhaseDao.findByAbolished(0);
	}
	
	/**
	 * add by yao.jin 2017.02.24
	 * 采购组织用户应该只能看到属于自己默认向导模板中设置的阶段，不应该显示供应商阶段管理的所有的阶段信息
	 * @return
	 */
	public List<VendorPhaseEntity> getAllPhaseByOrgId(Long orgId) {
		return (List<VendorPhaseEntity>) vendorPhaseDao.findByOrgIdAndAbolished(orgId,0);
	}
	
	


	/**
	 * 根据组织ID获取当前供应商信息
	 * @param orgId
	 * @return
	 */
	public VendorBaseInfoEntity getVendorBaseInfoByOrg(Long orgId) {
		return vendorBaseInfoDao.findByOrgId(orgId);
	}


	/**
	 * 获得供应商基本信息和扩展信息
	 * @param id 供应商信息表
	 * @return
	 */
	public VendorBaseInfoEntity getVendorBaseInfoEx(Long id) {
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(id);
		//获取扩展信息并设置
		List<VendorBaseInfoExEntity> exList = vendorBaseInfoExDao.findByVendorId(id);
		vendorBaseInfo.setExList(exList);
		return vendorBaseInfo;
	}


	/**
	 * 获取供应商基本信息历史，供应商方获取时取的是除开当前的
	 * @param orgId 组织ID
	 * @param currentId 当前基本信息ID
	 * @return 基本信息历史集合
	 */
	public List<VendorBaseInfoEntity> getVendorBaseInfoHisList(Long orgId, Long currentId) {
		List<VendorBaseInfoEntity> vendorBaseInfoList = vendorBaseInfoDao.findByOrgIdAndSubmitStatusOrderByVersionNODesc(orgId,StatusConstant.STATUS_YES);
		return vendorBaseInfoList;
	}

	/**
	 * 获取供应商基本信息历史，审核时是要小于当前版本的
	 * @param orgId 组织ID
	 * @param currentId 当前基本信息ID
	 * @return 基本信息历史集合
	 */
	public List<VendorBaseInfoEntity> getVendorBaseInfoAuditHisList(Long orgId, Long currentId) {
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findOne(currentId);
		List<VendorBaseInfoEntity> vendorBaseInfoList = vendorBaseInfoDao.findByOrgIdAndVersionNOLessThanEqualOrderByVersionNODesc(orgId,vendor.getVersionNO());
		return vendorBaseInfoList;
	}

	
	


	/**
	 * 禁用/启用供应商，根据changeType判断类型，并记录变更历史
	 * @param orgId 组织ID
	 * @param enableStatus 0-禁用，1-启用
	 * @param changeType 0-禁用，1-启用
	 * @param changeReason 变更原因
	 * @param changeUser 变更人
	 * @return 变更结果
	 */
	public Map<String, Object> enable(Long orgId, Integer enableStatus, Integer changeType, String changeReason, String changeUser) {
		Map<String,Object> map = new HashMap<String, Object>();
		OrganizationEntity org = orgDao.findOne(orgId);
		org.setEnableStatus(enableStatus);
		orgDao.save(org);
		String changeTypeText =""; 
		if(enableStatus==1)
		{
			changeTypeText="启用";
		}
		else if(enableStatus==0)
		{
			changeTypeText="禁用";
		}
		saveChangeHis(org,changeType,changeTypeText,changeReason,changeUser);
		map.put("success", true);
		return map;
	}


	/**
	 * 保存供应商变更历史
	 * @param org.getId() 供应商组织ID
	 * @param changeType 变更类型
	 */
	protected void saveChangeHis(OrganizationEntity org, Integer changeType,String changeTypeText,String changeReason,String changeUser) {
		VendorChangeHisEntity changeHis = new VendorChangeHisEntity();
		changeHis.setChangeReason(changeReason);
		changeHis.setChangeTime(DateUtil.getCurrentTimestamp());
		changeHis.setChangeType(changeType);
		changeHis.setChangeTypeText(changeTypeText);
		changeHis.setChangeUser(changeUser);
		changeHis.setOrgId(org.getId());
		changeHis.setVendorName(org.getName());
		vendorChangeHisDao.save(changeHis);
	}

    /**
     * 供应商降级到指定阶段
     * @param vendorPhaseId 供应商指定阶段
     * @param changeUser 变更人
     * @return 指定阶段
     */
	public Map<String, Object> demotion(Long vendorPhaseId,String changeReason ,String changeUser) {
		Map<String,Object> map = new HashMap<String, Object>();
		//获取指定的配置阶段
		VendorPhaseCfgEntity phaseCfg = vendorPhaseCfgDao.findOne(vendorPhaseId);
		//获取该阶段以上的调查表配置，该阶段
		List<VendorSurveyCfgEntity> surveyCfgList = vendorSurveyCfgDao.findByOrgIdAndPhaseSnGreaterThanEqual(phaseCfg.getOrgId(), phaseCfg.getPhaseSn());
		//获取调查表配置对应的调查表数据
//		if(Collections3.isEmpty(surveyCfgList)){
//			map.put("success", false);
//			map.put("msg", "没有可以降级的阶段");
//			return map;
//		}
		VendorBaseInfoEntity vendor = vendorBaseInfoDao.findByOrgIdAndCurrentVersion(phaseCfg.getOrgId(), StatusConstant.STATUS_YES);
		//降级操作将导致调查表全部被驳回
		for(VendorSurveyCfgEntity surveyCfg : surveyCfgList){
			surveyCfg.setAuditStatus(StatusConstant.STATUS_REJECT);
			surveyCfg.setAuditReason("降级");
			surveyCfg.setAuditUser(changeUser);
			vendorSurveyCfgDao.save(surveyCfg);
			//找到该阶段对应的当前调查表,同样被驳回,这里区分基本信息和调查表
			if("base".equals(surveyCfg.getSurveyCode())){
				vendor.setAuditStatus(StatusConstant.STATUS_REJECT);
				vendor.setAuditReason("降级");
				vendor.setAuditUser(changeUser);
				continue;
			}
			VendorSurveyBaseEntity survey = null;
			List<VendorSurveyBaseEntity> surveys = vendorSurveyBaseDao.findByVendorCfgIdAndCurrentVersionOrderByIdDesc(surveyCfg.getId(),StatusConstant.STATUS_YES);
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
				survey =surveys.get(0);
			}
			//如果导入的，除了基本信息，都没有该数据，所以不需要处理
			if(survey==null){
				continue;
			}
			survey.setAuditStatus(StatusConstant.STATUS_REJECT);
			survey.setAuditReason("降级");
			survey.setAuditUser(changeUser);
			vendorSurveyBaseDao.save(survey);
		}
		vendor.setPhaseSn(phaseCfg.getPhaseSn());
		vendor.setPhaseCfgId(phaseCfg.getId());
		vendor.setPhaseId(phaseCfg.getPhaseId());
		vendorBaseInfoDao.save(vendor);
		//调用历史记录
		OrganizationEntity vendorOrg = orgDao.findOne(phaseCfg.getOrgId());
		String changeTypeText ="";
		if(VendorModuleTypeConstant.getVendorChangeTypeMap().get(VendorModuleTypeConstant.VENDOR_CHANGETYPE_3)!=null)
		{
			changeTypeText=VendorModuleTypeConstant.getVendorChangeTypeMap().get(VendorModuleTypeConstant.VENDOR_CHANGETYPE_3) +phaseCfg.getPhaseName();
		}
		else
		{
			changeTypeText=""+phaseCfg.getPhaseName();
		}
		saveChangeHis(vendorOrg, VendorModuleTypeConstant.VENDOR_CHANGETYPE_3,"降级--"+changeTypeText,changeReason, changeUser);
		
		
		//供货关系-----供货状态--->>停止
		List<VendorMaterialRelEntity> materialRelList = vendorMaterialRelDao.findByOrgId(vendor.getOrgId());
		for (int i = 0; i < materialRelList.size(); i++) {
			materialRelList.get(i).setStatus(0);
		}
		vendorMaterialRelDao.save(materialRelList);
		map.put("success", true);
		return map;
	}


	public List<VendorAdmittanceTransfer> getVendorAdmittanceTransfer(
			Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorBaseInfoEntity.class);
		List<VendorBaseInfoEntity> vendorBaseInfoList = vendorBaseInfoDao.findAll(spec);
		//获取调查表的情况。目前一个一个查询，如果有性能需求，此处可做优化
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoList){
			//定义调查表情况
			int total = 0,submitTotal=0,auditTotal=0;
			String submitInfo="",auditInfo="";//提交信息和审核信息	
			//如果未确认，跳过
			if(vendorBaseInfo.getOrg().getConfirmStatus()==StatusConstant.STATUS_NO) {
				continue;
			}
			//获取该阶段和以前所有的调查表
			List<VendorSurveyCfgEntity> vendorSurveyCfgList = null;
			if(vendorBaseInfo.getPhaseSn()!=null&&vendorBaseInfo.getOrgId()!=null) {
				vendorSurveyCfgList = vendorSurveyCfgDao.findByPhaseSnLessThanEqualAndOrgId(vendorBaseInfo.getPhaseSn(),vendorBaseInfo.getOrgId());
			};
			if(Collections3.isEmpty(vendorSurveyCfgList)) {
				continue;
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
			submitInfo = submitTotal+"/"+total;
			auditInfo = auditTotal+"/"+total;
			vendorBaseInfo.setSurveyTotal(total);
			vendorBaseInfo.setSurveySubmitInfo(submitInfo);
			vendorBaseInfo.setSurveyAuditInfo(auditInfo);
		}
		List<VendorAdmittanceTransfer> retList = new ArrayList<VendorAdmittanceTransfer>();
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoList){
			VendorAdmittanceTransfer trans = new VendorAdmittanceTransfer();
			trans.setCountryText(vendorBaseInfo.getCountryText());
			trans.setEnableStatus(vendorBaseInfo.getOrg().getEnableStatus()+"");
			trans.setOrgCode(vendorBaseInfo.getOrg().getCode());
			if((VendorModuleTypeConstant.VENDOR_PROPERTY_1+"").equals(vendorBaseInfo.getProperty())){
				trans.setProperty("国有企业");
			}else if((VendorModuleTypeConstant.VENDOR_PROPERTY_2+"").equals(vendorBaseInfo.getProperty())){
				trans.setProperty("国有控股企业");
			}else if((VendorModuleTypeConstant.VENDOR_PROPERTY_3+"").equals(vendorBaseInfo.getProperty())){
				trans.setProperty("外资企业");
			}else if((VendorModuleTypeConstant.VENDOR_PROPERTY_4+"").equals(vendorBaseInfo.getProperty())){
				trans.setProperty("合资企业");
			}
			else if((VendorModuleTypeConstant.VENDOR_PROPERTY_5+"").equals(vendorBaseInfo.getProperty())){
				trans.setProperty("私营企业");
			}
			else{
				trans.setProperty(vendorBaseInfo.getProperty());
			}
			trans.setProvinceText(vendorBaseInfo.getProvinceText());
			trans.setRegtime(vendorBaseInfo.getRegtime()+"");
			trans.setShortName(vendorBaseInfo.getShortName());
			trans.setSurveyAuditInfo(vendorBaseInfo.getSurveyAuditInfo());
			trans.setSurveySubmitInfo(vendorBaseInfo.getSurveySubmitInfo());
			retList.add(trans);
		}
		return retList;
	}
	public String getSturst(String statusType) {
		List<StatusDictEntity> list=statusDictDao.findByStatusType(statusType);
		String data="[";
		int i=0;
		for(StatusDictEntity statusDictEntity:list){
			if(i>0)
			{
				data=data+",";
			}
			System.out.println();
			data=data+"{\"id\":\""+statusDictEntity.getStatusValue()+"\",\"text\":\""+statusDictEntity.getStatusText()+"\"}";
			i++;
		}
		data=data+"]";
		return data;  
	}


	public String getSturst2(String statusType) {
		List<StatusDictEntity> list=statusDictDao.findByStatusType(statusType);
		String data="[";
		int i=0;
		for(StatusDictEntity statusDictEntity:list){
			if(i>0)
			{
				data=data+",";
			}
			System.out.println();
			data=data+"{\"id\":\""+statusDictEntity.getStatusValue()+"\",\"text\":\""+statusDictEntity.getStatusText()+"\"}";
			i++;
		}
		data=data+"]";
		return data;  
	}


	public Map<String, Object> confirm2(Long id, String basePath, Long orgId) {
		List<VendorBaseInfoEntity> vendorBaseInfoList= new ArrayList<VendorBaseInfoEntity>();
		VendorBaseInfoEntity v=vendorBaseInfoDao.findOne(id);
		List<BuyerVendorRelEntity> buyerList=buyerVendorRelDao.findByVendorId(v.getOrgId());
		if(buyerList!=null&&buyerList.size()>0){
			BuyerVendorRelEntity buyer=buyerList.get(0);
			v.setBuyerId(buyer.getBuyerId());
			v.setBuyerName(buyer.getBuyer().getName());
		}
		vendorBaseInfoList.add(v);
		return confirm(vendorBaseInfoList,basePath, orgId);
	}


	public Map<String, Object> confirmReject2(Long id, String basePath) {
		List<VendorBaseInfoEntity> vendorBaseInfoList= new ArrayList<VendorBaseInfoEntity>();
		VendorBaseInfoEntity v=vendorBaseInfoDao.findOne(id);
		vendorBaseInfoList.add(v);
		return confirmReject(vendorBaseInfoList,basePath);
	}


	/**
	 * 供应商数量统计
	 * @param checkParamMap 维度
	 * @param searchParamMap 搜索条件
	 * @return
	 */
	public List<StatisticsVendorCountTransfer> getStatisticsVendorCountTransfer(
			String checkParam,
			Map<String, Object> searchParamMap) {
		
		//返回到页面图表中显示的数据格式
		List<Map<String, Object>> list = getChartData(0,checkParam,searchParamMap);
		//返回到页面table中显示的数据格式
		List<StatisticsVendorCountTransfer> vList = null;
		//[start]单个维度下数据组装
		if(checkParam.indexOf(",")==-1){
			vList = new ArrayList<StatisticsVendorCountTransfer>();
			if(TypeConstant.STATISTICS_PROV.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String num1 = list.get(i).get("注册供应商")==null?"0":list.get(i).get("注册供应商")+"";
					String num2 = list.get(i).get("体系外备选供应商")==null?"0":list.get(i).get("体系外备选供应商")+"";
					String num3 = list.get(i).get("体系内供应商")==null?"0":list.get(i).get("体系内供应商")+"";
					v.setTotalCount(Integer.parseInt(num1)+Integer.parseInt(num2)+Integer.parseInt(num3)+"");
					v.setProvince(list.get(i).get("x-axis")+"");
					vList.add(v);
				}
			}
			if(TypeConstant.STATISTICS_PHASE.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String phase = list.get(i).get("x-axis")+"";
					v.setPhase(phase);
					v.setTotalCount(list.get(i).get(phase)+"");
					vList.add(v);
				}
			}
			if(TypeConstant.STATISTICS_CLASSIFY2.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String classify = list.get(i).get("x-axis")+"";
					v.setClassify(classify);
					v.setTotalCount(list.get(i).get(classify)+"");
					vList.add(v);
				}
			}
			if(TypeConstant.STATISTICS_PARTS_TYPE.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String partsType = list.get(i).get("x-axis")+"";
					v.setPartsType(partsType);
					v.setTotalCount(list.get(i).get(partsType)+"");
					vList.add(v);
				}
			}
			if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String bussinessType = list.get(i).get("x-axis")+"";
					v.setBussinessType(bussinessType);
					v.setTotalCount(list.get(i).get(bussinessType)+"");
					vList.add(v);
				}
			}
			if(TypeConstant.STATISTICS_SYSTEM.equals(checkParam)){
				for (int i = 0; i < list.size(); i++) {
					StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
					String system = list.get(i).get("x-axis")+"";
					v.setMaterialSystem(system);
					v.setTotalCount(list.get(i).get(system)+"");
					vList.add(v);
				}
			}
		}
		//两个维度下数据组装
		else{
			vList = new ArrayList<StatisticsVendorCountTransfer>();
			for (int i = 0; i < list.size(); i++) {
				StatisticsVendorCountTransfer v = new StatisticsVendorCountTransfer();
				Map<String,Object> vMap = list.get(i);
				for (String str : vMap.keySet()) {
					//省份 
					if(TypeConstant.STATISTICS_PROV.equals(str)){
						v.setProvince(vMap.get(str)+"");
					}
					//供应商性质
					if(TypeConstant.STATISTICS_PHASE.equals(str)){
						v.setPhase(vMap.get(str)+"");
					}
					//零部件类别
					if(TypeConstant.STATISTICS_PARTS_TYPE.equals(str)){
						v.setPartsType(vMap.get(str)+"");
					}
					//与工厂距离
					if(TypeConstant.STATISTICS_DISTANCE.equals(str)){
						v.setDistance(vMap.get(str)+"");
					}
					//业务类型
					if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(str)){
						v.setBussinessType(vMap.get(str)+"");
					}
					//供应商分类 （A、B、C）
					if(TypeConstant.STATISTICS_CLASSIFY2.equals(str)){
						v.setClassify(vMap.get(str)+"");
					}
					//系统   物料一级分类   (动力，底盘，电气，车身……)
					if(TypeConstant.STATISTICS_SYSTEM.equals(str)){
						v.setMaterialSystem(vMap.get(str)+"");
					}
				}
				v.setTotalCount(list.get(i).get("vendorCount")+"");
				vList.add(v);
			}
		}
		return vList;
	}

	/**
	 * 根据code获取当前版本供应商
	 * @param code
	 * @return
	 */
	public VendorBaseInfoEntity findByCodeAndCurrentVersion(String code){
		return vendorBaseInfoDao.findByCodeAndCurrentVersion(code,StatusConstant.STATUS_YES);
	}

	/**
	 * 同步erp供应商
	 * @param entityList
	 */
	public void saveErpVendors(List<VendorBaseInfoEntity> entityList) {
		vendorBaseInfoDao.save(entityList);
	}
	
	public List<Map<String, Object>> getChartData(Integer type,String check,Map<String, Object> searchParams){
		String sql = "";
		String where = "";
		List<Object[]> list = null;
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		
		
		
		if(check!=null){
			//组装where语句
			if(searchParams!=null){
				for (String key : searchParams.keySet()) {
					String[] names = StringUtils.split(key, "_");
					//单维度where特殊处理  == 省份，供应商性质，供应商分类不与供货关系关联，所以搜索条件中MSR,MAT....之类的要排除
					if(TypeConstant.STATISTICS_PROV.equals(check)||TypeConstant.STATISTICS_PHASE.equals(check)||TypeConstant.STATISTICS_CLASSIFY2.equals(check)){
						if(names[1].indexOf("msr")!=-1||names[1].indexOf("mat")!=-1||names[1].indexOf("matt")!=-1){
							continue;
						}
					}
					//多维度where特殊处理  == 省份，供应商性质，供应商分类随机组合不与供货关系关联，搜索中排除MSR,MAT,MATT
					if(check.indexOf(",")!=-1){
						if((TypeConstant.STATISTICS_PROV+","+TypeConstant.STATISTICS_PHASE).equals(check)||(TypeConstant.STATISTICS_PHASE+","+TypeConstant.STATISTICS_CLASSIFY2).equals(check)||(TypeConstant.STATISTICS_PROV+","+TypeConstant.STATISTICS_CLASSIFY2).equals(check)){
							if(names[1].indexOf("msr")!=-1||names[1].indexOf("mat")!=-1||names[1].indexOf("matt")!=-1){
								continue;
							}
						}
					}
					if (names.length > 2) {
					}else if(names.length == 1){
						//默认为EQ
						names = new String[]{Operator.EQ.toString(),key};
					}
					if(where.equals("")) {
						where = " WHERE 1=1";
					}
					if(StringUtils.isEmpty(searchParams.get(key)+"")) {
						continue;
					}
					if(Operator.EQ.toString().equals(names[0]+"")){
						where += " AND "+StringUtils.underString(names[1])+"='"+searchParams.get(key)+"' ";
					}else{
						where += " AND "+StringUtils.underString(names[1])+" LIKE '%"+searchParams.get(key)+"%' ";
					}
				}
			}
			if(where.equals("")&&TypeConstant.STATISTICS_SYSTEM.equals(check)) {
				where = " WHERE MATT_LEVEL_LAYER = 1"; 
			}else if(TypeConstant.STATISTICS_SYSTEM.equals(check)){
				where += " AND MATT_LEVEL_LAYER = 1"; 
			}
			
			//[start]单维度
			//省份
			if(TypeConstant.STATISTICS_PROV.equals(check)){
				sql = "SELECT V_PROVINCE_TEXT,VP_NAME,COUNT(DISTINCT V_ID) FROM VIEW_VENDOR "+where+" GROUP BY V_PROVINCE_TEXT,VP_NAME ORDER BY V_PROVINCE_TEXT";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = map.get(obj[0]);
					if(dataMap==null){
						dataMap = new HashMap<String, Object>();
					}
					if(obj[1]==null){
						continue;
					}
					if(obj[0]==null){
						obj[0]="其他";
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[2]);
					dataMap.put(obj[1]+"",obj[2]);
					map.remove(obj[0]);
					map.put(obj[0]+"", dataMap);
				}
				for (String key : map.keySet()) {
					ret.add(map.get(key));
				}
			}
			//供应商性质
			if(TypeConstant.STATISTICS_PHASE.equals(check)){
				sql = "SELECT VP_NAME,COUNT (DISTINCT V_ID) FROM	VIEW_VENDOR "+where+" GROUP BY VP_NAME ORDER BY VP_NAME";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[1]);
					dataMap.put(obj[0]+"",obj[1]);
					ret.add(dataMap);
				}
			}
			
			//供应商分类
			if(TypeConstant.STATISTICS_CLASSIFY2.equals(check)){
				sql = "SELECT V_VENDOR_CLASSIFY2,COUNT (DISTINCT V_ID) FROM	VIEW_VENDOR "+where+" GROUP BY V_VENDOR_CLASSIFY2  ORDER BY V_VENDOR_CLASSIFY2";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[1]);
					dataMap.put(obj[0]+"",obj[1]);
					ret.add(dataMap);
				}
			}
			
			//零部件类别
			if(TypeConstant.STATISTICS_PARTS_TYPE.equals(check)){
				sql = "SELECT MAT_PARTS_TYPE,COUNT (DISTINCT V_ID) FROM	VIEW_MATERIAL_SUPPLY_REL "+where+" GROUP BY MAT_PARTS_TYPE ORDER BY MAT_PARTS_TYPE";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[1]);
					dataMap.put(obj[0]+"",obj[1]);
					ret.add(dataMap);
				}
			}
			
			//业务类型
			if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(check)){
				sql = "SELECT MSR_BUSSINESS_NAME,COUNT (DISTINCT V_ID) FROM	VIEW_MATERIAL_SUPPLY_REL "+where+" GROUP BY MSR_BUSSINESS_NAME ORDER BY MSR_BUSSINESS_NAME";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[1]);
					dataMap.put(obj[0]+"",obj[1]);
					ret.add(dataMap);
				}
			}
			
			//系统
			if(TypeConstant.STATISTICS_SYSTEM.equals(check)){
				sql = "SELECT MATT_MA,COUNT (DISTINCT V_ID) FROM VIEW_MATERIAL_SUPPLY_REL "+where+" GROUP BY MATT_MA";
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					dataMap.put("x-axis",obj[0]);
					dataMap.put("y-axis",obj[1]);
					dataMap.put(obj[0]+"",obj[1]);
					ret.add(dataMap);
				}
			}
			//[end]
			
			//[start]多维度
			if(check.indexOf(",")!=-1){
				String [] checks = check.split(",");
				String selectItem = "COUNT (DISTINCT V_ID)";
				for(String str : checks){
					//省份 
					if(TypeConstant.STATISTICS_PROV.equals(str)){
						selectItem += ",V_PROVINCE_TEXT";
					}
					//供应商性质
					if(TypeConstant.STATISTICS_PHASE.equals(str)){
						selectItem += ",VP_NAME";
					}
					//零部件类别
					if(TypeConstant.STATISTICS_PARTS_TYPE.equals(str)){
						selectItem += ",MAT_PARTS_TYPE";
					}
					//与工厂距离
					if(TypeConstant.STATISTICS_DISTANCE.equals(str)){
						selectItem = "工厂距离暂未实现";
					}
					//业务类型
					if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(str)){
						selectItem += ",MSR_BUSSINESS_NAME";
					}
					//供应商分类 （A、B、C）
					if(TypeConstant.STATISTICS_CLASSIFY2.equals(str)){
						selectItem += ",V_VENDOR_CLASSIFY2";
					}
					//系统   物料一级分类   (动力，底盘，电气，车身……)
					if(TypeConstant.STATISTICS_SYSTEM.equals(str)){
						selectItem += ",MATT_NAME";
					}
				}
				if(selectItem.indexOf("工厂距离暂未实现")!=-1){
					return ret;
				}
				String groupItem = selectItem.substring(selectItem.indexOf(",")+1);
				sql = "SELECT "+selectItem+" FROM (SELECT DISTINCT V_ID,"+groupItem+" FROM VIEW_MATERIAL_SUPPLY_REL "+where+" ) GROUP BY "+groupItem +" ORDER BY "+groupItem;
				//多维度时按照省份，供应商性质，供应商分类随机组合的，不关联供货关系，所以使用VIEW_VENDOR视图查询
				if(groupItem.indexOf("V_PROVINCE_TEXT,VP_NAME")!=-1||groupItem.indexOf("V_PROVINCE_TEXT,V_VENDOR_CLASSIFY2")!=-1||groupItem.indexOf("VP_NAME,V_VENDOR_CLASSIFY2")!=-1){
					sql = "SELECT "+selectItem+" FROM (SELECT DISTINCT V_ID,"+groupItem+" FROM VIEW_VENDOR "+where+" ) GROUP BY "+groupItem +" ORDER BY "+groupItem;
				}
				list = (List<Object[]>) daoImpl.queryBySql(sql);
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = list.get(i);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if(obj[0]==null){
						continue;
					}
					if(obj[1]==null){
						obj[1]="";
					}
					if(obj[2]==null){
						obj[2]="";
					}
					//为图表组装数据做准备，靠前为主要，做Y轴
					dataMap.put("y-axis",obj[1]);
					dataMap.put(checks[0],obj[1]);
					dataMap.put(checks[1],obj[2]);
					dataMap.put("vendorCount",obj[0]);
					ret.add(dataMap);
				}
			}
			//[end]
			return ret;
		}
		
		//[start]下面代码为以前版本，留作开发参考及后期优化，请勿删除 TODO
		/*
		
		if(searchParams!=null){
			for (String key : searchParams.keySet()) {
				String[] names = StringUtils.split(key, "_");
				if (names.length > 2) {
				}else if(names.length == 1){
					//默认为EQ
					names = new String[]{Operator.EQ.toString(),key};
				}
				if(where.equals("")) {
					where = " WHERE 1=1";
				}
				if(Operator.EQ.toString().equals(names[0]+"")){
					where += " AND V."+StringUtils.underString(names[1])+"='"+searchParams.get(key)+"' ";
				}else{
					where += " AND V."+StringUtils.underString(names[1])+" LIKE '%"+searchParams.get(key)+"%' ";
				}
			}
		}
		
		//按省份统计供应商性质数量
				if(type==TypeConstant.STATISTICS_VENDOR_COUNT_LIST){
					sql = "SELECT V.PROVINCE_TEXT,V.PHASE_ID,COUNT(V.PHASE_ID) FROM QEWEB_VENDOR_BASE_INFO V "+where+" GROUP BY V.PROVINCE_TEXT,V.PHASE_ID";
					list = (List<Object[]>) daoImpl.queryBySql(sql);
					Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = list.get(i);
						Map<String, Object> dataMap = map.get(obj[0]);
						if(dataMap==null){
							dataMap = new HashMap<String, Object>();
						}
						if(obj[1]==null){
							continue;
						}
						dataMap.put("province",obj[0]);
						VendorPhaseEntity phase = vendorPhaseDao.findOne(Long.parseLong(obj[1]+""));
						dataMap.put(phase.getName(),obj[2]);
						map.remove(obj[0]);
						map.put(obj[0]+"", dataMap);
					}
					for (String key : map.keySet()) {
						ret.add(map.get(key));
					}
				}
		
		//按省份统计供应商数量
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV){
			sql = "SELECT V.PROVINCE,V.PROVINCE_TEXT,COUNT(V.ID) FROM QEWEB_VENDOR_BASE_INFO V "+where+" GROUP BY V.PROVINCE,V.PROVINCE_TEXT";
			list = (List<Object[]>) daoImpl.queryBySql(sql);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Object[] obj = list.get(i);
				map.put("province",obj[1]);
				map.put("totalCount",obj[2]);
				ret.add(map);
			}
		}
		//按省份统计供应商性质数量
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV_PHASE){
			sql = "SELECT V.PROVINCE_TEXT,V.PHASE_ID,COUNT(V.PHASE_ID) FROM QEWEB_VENDOR_BASE_INFO V "+where+" GROUP BY V.PROVINCE_TEXT,V.PHASE_ID";
			list = (List<Object[]>) daoImpl.queryBySql(sql);
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = list.get(i);
				Map<String, Object> dataMap = map.get(obj[0]);
				if(dataMap==null){
					dataMap = new HashMap<String, Object>();
				}
				if(obj[1]==null){
					continue;
				}
				dataMap.put("province",obj[0]);
				VendorPhaseEntity phase = vendorPhaseDao.findOne(Long.parseLong(obj[1]+""));
				dataMap.put(phase.getName(),obj[2]);
				map.remove(obj[0]);
				map.put(obj[0]+"", dataMap);
			}
			for (String key : map.keySet()) {
				ret.add(map.get(key));
			}
		}
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_MAT_PHASE){
			sql = "SELECT V.MATERIAL_TYPE_ID,V.PHASE_ID,COUNT(V.PHASE_ID) FROM QEWEB_VENDOR_BASE_INFO V INNER JOIN QEWEB_MATERIAL_TYPE M ON V.MATERIAL_TYPE_ID=M.ID WHERE M.LEVEL_LAYER=3 GROUP BY V.MATERIAL_TYPE_ID,V.PHASE_ID";
			list = (List<Object[]>) daoImpl.queryBySql(sql);
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = list.get(i);
				if(obj[0]==null){
					continue;
				}
				MaterialTypeEntity mt = materialTypeDao.findOne(Long.parseLong(obj[0]+""));
				Map<String, Object> dataMap = map.get(mt.getName());
				if(dataMap==null){
					dataMap = new HashMap<String, Object>();
				}
				dataMap.put("province",mt.getName());
				VendorPhaseEntity phase = vendorPhaseDao.findOne(Long.parseLong(obj[1]+""));
				dataMap.put(phase.getName(),obj[2]);
				map.remove(mt.getName());
				map.put(mt.getName()+"", dataMap);
			}
			for (String key : map.keySet()) {
				ret.add(map.get(key));
			}
		}
		
		//此处以下搜索条件是供货系数表，故where条件指定表为QEWEB_VENDOR_MATERIAL_SUPPLY_REL MSR
		if(searchParams!=null){
			where = "";
			for (String key : searchParams.keySet()) {
				String[] names = StringUtils.split(key, "_");
				if (names.length > 2) {
				}else if(names.length == 1){
					//默认为EQ
					names = new String[]{Operator.EQ.toString(),key};
				}
				if(where.equals("")) {
					where = " WHERE 1=1";
				}
				if(Operator.EQ.toString().equals(names[0]+"")){
					where += " AND MSR."+StringUtils.underString(names[1])+"='"+searchParams.get(key)+"' ";
				}else{
					where += " AND MSR."+StringUtils.underString(names[1])+" LIKE '%"+searchParams.get(key)+"%' ";
				}
			}
		}
		//筛选品牌按照省份统计供应商数量 
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV_BRAND){
			sql = "SELECT V.PROVINCE,V.PROVINCE_TEXT,COUNT(V.ID) FROM QEWEB_VENDOR_BASE_INFO V INNER JOIN QEWEB_VENDOR_MAT_SUPPLY_REL MSR ON V.ID=MSR.VENDOR_ID "+where+" GROUP BY V.PROVINCE,V.PROVINCE_TEXT";
			list = (List<Object[]>) daoImpl.queryBySql(sql);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Object[] obj = list.get(i);
				map.put("province",obj[1]);
				map.put("totalCount",obj[2]);
				ret.add(map);
			}
		}
		//筛选产品线、品牌按照供应商分类统计供应商数量 
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_VENDORCLASSIFY){
			sql = "SELECT V.VENDOR_CLASSIFY2,COUNT(V.ID) FROM QEWEB_VENDOR_BASE_INFO V INNER JOIN QEWEB_VENDOR_MAT_SUPPLY_REL MSR ON V.ID=MSR.VENDOR_ID "+where+" GROUP BY V.VENDOR_CLASSIFY2";
			list = (List<Object[]>) daoImpl.queryBySql(sql);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Object[] obj = list.get(i);
				map.put("province",obj[0]);
				map.put("totalCount",obj[1]);
				ret.add(map);
			}
		}
		//[end]
			*/
				
		return ret;
	}
	
	
	
	/**
     * 获得指定阶段的供应商信息列表，需要做额外的处理，查询出当前调查表的审核情况
     * 目前为采购员使用
     * @param pageNumber 当前页数
     * @param pageSize 每页数量
     * @param searchParamMap 查询参数
     * @return 指定阶段的供应商信息列表
     */
	public Page<VendorBaseInfoEntity> getVendorBaseInfoList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequestList(pageNumber, pageSize, Arrays.asList(new Order(Direction.DESC, "org.auditSn"),new Order(Direction.DESC, "id")));

		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorBaseInfoEntity.class);
		Page<VendorBaseInfoEntity> vendorBaseInfoPage = vendorBaseInfoDao.findAll(spec,pagin);
		//获取调查表的情况。目前一个一个查询，如果有性能需求，此处可做优化
		for(VendorBaseInfoEntity vendorBaseInfo : vendorBaseInfoPage.getContent()){
			//定义调查表情况
			int total = 0,submitTotal=0,auditTotal=0;
			String submitInfo="",auditInfo="";//提交信息和审核信息	
			//如果未确认，跳过
			if(vendorBaseInfo.getOrg().getConfirmStatus()!=StatusConstant.STATUS_YES) {
				continue;
			}
			if(vendorBaseInfo.getPhaseSn()==null){
				continue;
			}
			//获取该阶段和以前所有的调查表
			List<VendorSurveyCfgEntity> vendorSurveyCfgList = vendorSurveyCfgDao.findByPhaseSnLessThanEqualAndOrgId(vendorBaseInfo.getPhaseSn(),vendorBaseInfo.getOrgId());;
			if(Collections3.isEmpty(vendorSurveyCfgList)) {
				continue;
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
			submitInfo = submitTotal+"/"+total;
			auditInfo = auditTotal+"/"+total;
			vendorBaseInfo.setSurveyTotal(total);
			vendorBaseInfo.setSurveySubmitInfo(submitInfo);
			vendorBaseInfo.setSurveyAuditInfo(auditInfo);
		}
		
		return vendorBaseInfoPage;
	}
	/**
	 * 供应商晋级
	 * @param org 组织
	 * @param vendorBaseInfo 供应商基本信息
	 * @param changeUser 变更用户
	 * @param changeReason 变更原因 
	 * @return 
	 * @throws Exception 
	 */
	public Map<String,Object> promte(OrganizationEntity org, VendorBaseInfoEntity vendorBaseInfo, String changeReason, String changeUser) throws Exception {
    	Map<String,Object> map = new HashMap<String, Object>();
    	map.put("success", true);
    	boolean isEndPhase = vendorPhaseCfgService.validateIsEndPhase(vendorBaseInfo);
    	if(isEndPhase){
    		map.put("success", false);
    		map.put("msg", "已是最后阶段，无需晋级！");
    		return map;
    	}
		Long phaseId = vendorBaseInfo.getPhaseId();
		VendorPhaseCfgEntity curVendorPhase = null;
		VendorPhaseCfgEntity nextPhaseCfg = null;
		List<VendorPhaseCfgEntity> vendorPhaseList = vendorPhaseCfgDao.findByOrgIdAndTemplateId(vendorBaseInfo.getOrgId(),vendorBaseInfo.getTemplateId());
		for(VendorPhaseCfgEntity vendorPhase : vendorPhaseList){
			//如果相等进入下一阶段
			if(vendorPhase.getPhaseId().intValue()==phaseId.intValue()){
				curVendorPhase = vendorPhase;
				break;
			}
			
		}
		//校验是否可以晋级
		String msg = validatePromote(curVendorPhase);
		if(StringUtils.isNotEmpty(msg)) {
			map.put("success", false);
    		map.put("msg", msg);
    		return map;
		}

		
		//获得下一个阶段
		for(VendorPhaseCfgEntity vendorPhase : vendorPhaseList){
			if(vendorPhase.getPhaseSn()==curVendorPhase.getPhaseSn()+1){
				nextPhaseCfg = vendorPhase;
				break;
			}
		}
		
		if(nextPhaseCfg!=null) {
			setVendorNextPhase(org, vendorBaseInfo, nextPhaseCfg);
		}
		String changeTypeText = "晋级---"+nextPhaseCfg.getPhaseName();
		saveChangeHis(org, VendorModuleTypeConstant.VENDOR_CHANGETYPE_2,changeTypeText, changeReason, changeUser);
		
		//改为调用晋级代码，最后一级就调用接口方法
		isEndPhase = vendorPhaseCfgService.validateIsEndPhase(vendorBaseInfo);
		return map;
	}
    
	public void saveOff(VendorBaseInfoEntity vendorBaseInfoList)
	{
//		VendorBaseInfoEntity existBaseInfo = vendorBaseInfoDao.findOne(vendorBaseInfoList.getId());
//		existBaseInfo.setSoId(vendorBaseInfoList.getSoId());
//		vendorBaseInfoDao.save(existBaseInfo);
	}

    /**
     * 保存供货关系
     * @param org
     * @param vendorBaseInfo
     */
	private void saveVenMatRel(OrganizationEntity org, VendorBaseInfoEntity vendorBaseInfo) {
		List<VendorMaterialRelEntity> venMatRelList = vendorBaseInfo.getVenMatRelList();
		if(Collections3.isEmpty(venMatRelList)) {
			return;
		}
		for(VendorMaterialRelEntity venMatRelEntity : venMatRelList){
			venMatRelEntity.setOrgId(org.getId());
			venMatRelEntity.setVendorId(vendorBaseInfo.getId());
			venMatRelEntity.setVendorName(org.getName());
			venMatRelEntity.setStatus(1);
		}
		venMatRelDao.save(venMatRelList);
	}


}
