package com.qeweb.scm.vendorperformancemodule.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionSolutionEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCorrectionDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCorrectionSolutionDao;

@Service
@Transactional
public class VendorPerforCorrectionService {
	
	@Autowired
	private VendorPerforCorrectionDao correctionDao;
	
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	@Autowired
    private OrganizationDao organizationDao;
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private VendorPerforCorrectionSolutionDao correctionSolutionDao;

	public Page<VendorPerforCorrectionEntity> getcorrectionList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforCorrectionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforCorrectionEntity.class);
		Page<VendorPerforCorrectionEntity>  page= correctionDao.findAll(spec,pagin);
		for(VendorPerforCorrectionEntity vendorPerforCorrectionEntity:page.getContent())
		{
			VendorPerforCorrectionSolutionEntity vcs=correctionSolutionDao.findByCorrectionIdAndCurrentVersion(vendorPerforCorrectionEntity.getId(),1);
			if(vcs!=null)
			{
				vendorPerforCorrectionEntity.setSolutionContent(vcs.getSolutionContent());
				if(vcs.getFileUrl()!=null&&!(vcs.getFileUrl().equals("")))
				{
					vendorPerforCorrectionEntity.setFileUrl(vcs.getFileUrl());
				}
			}
		}
		return page;
	}
	
	public Page<VendorPerforCorrectionEntity> getcorrectionList2(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("LIKE_vendorCode", user.orgCode);
		searchParamMap.put("NEQ_correctionStatus", -1);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforCorrectionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforCorrectionEntity.class);
		Page<VendorPerforCorrectionEntity>  page= correctionDao.findAll(spec,pagin);
		for(VendorPerforCorrectionEntity vendorPerforCorrectionEntity:page.getContent())
		{
			VendorPerforCorrectionSolutionEntity vcs=correctionSolutionDao.findByCorrectionIdAndCurrentVersion(vendorPerforCorrectionEntity.getId(),1);
			if(vcs!=null)
			{
				vendorPerforCorrectionEntity.setSolutionContent(vcs.getSolutionContent());
				if(vcs.getFileUrl()!=null&&!(vcs.getFileUrl().equals("")))
				{
					vendorPerforCorrectionEntity.setFileUrl(vcs.getFileUrl());
				}
			}
		}
		return page;
	}

	public Map<String, Object> solutionSubmit(String uid, String typee,String sContent) {
		Map<String, Object> map=new HashMap<String, Object>();
		VendorPerforCorrectionSolutionEntity solutionEntity=correctionSolutionDao.findByCorrectionIdAndCurrentVersion(Long.parseLong(uid),1);
		VendorPerforCorrectionSolutionEntity solution=new VendorPerforCorrectionSolutionEntity();
		try {
			if(solutionEntity!=null)
			{
				List<VendorPerforCorrectionSolutionEntity> list=new ArrayList<VendorPerforCorrectionSolutionEntity>();
				PropertyUtils.copyProperties(solution, solutionEntity);
				solution.setId(0);
				solution.setAuditStatus(Integer.parseInt(typee));
				solution.setAuditReason(sContent);
				list.add(solution);
				solutionEntity.setCurrentVersion(0);
				list.add(solutionEntity);
				correctionSolutionDao.save(list);
				VendorPerforCorrectionEntity v=correctionDao.findOne(Long.parseLong(uid));
				v.setCorrectionStatus(Integer.parseInt(typee)+1);
				correctionDao.save(v);
				map.put("success", true);
				map.put("msg", "审核成功！");
			}
			else
			{
				map.put("success", false);
				map.put("msg", "审核失败！");
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "审核失败！");
			e.printStackTrace();
		}
		return map;
	}

	public Page<VendorPerforCorrectionSolutionEntity> lookSolution(int pageNumber, int pageSize, Map<String, Object> searchParamMap, Long id) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_correctionId", id);
		searchParamMap.put("NEQ_auditStatus", 0);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforCorrectionSolutionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforCorrectionSolutionEntity.class);
		Page<VendorPerforCorrectionSolutionEntity>  page= correctionSolutionDao.findAll(spec,pagin);
		return page;
	}

	public Map<String, Object> endSolution(List<VendorPerforCorrectionEntity> vendorPerforCorrectionEntitys, String usContent) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		for(VendorPerforCorrectionEntity v:vendorPerforCorrectionEntitys)
		{
			VendorPerforCorrectionEntity vv=correctionDao.findOne(v.getId());
			vv.setEndStatus(1);
			vv.setCorrectionStatus(4);
			vv.setCorrectionEndContent(URLDecoder.decode(usContent,"UTF-8") );
			correctionDao.save(vv);
		}
		map.put("success", true);
		map.put("msg", "审核成功！");
		return map;
	}

	public boolean combine(String uid, String solutionContent, String url) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		VendorPerforCorrectionSolutionEntity entity=correctionSolutionDao.findByCorrectionIdAndCurrentVersion(Long.parseLong(uid), 1);
		VendorPerforCorrectionSolutionEntity  solution=new VendorPerforCorrectionSolutionEntity();
		List<VendorPerforCorrectionSolutionEntity> list=new ArrayList<VendorPerforCorrectionSolutionEntity>();
		if(entity!=null)
		{
			PropertyUtils.copyProperties(solution, entity);
			solution.setId(0);
			solution.setAuditStatus(0);
			solution.setSolutionContent(solutionContent);
			if(!(url.equals("")))
			{
				solution.setFileUrl(url);
			}
			list.add(solution);
			entity.setCurrentVersion(0);
			list.add(entity);
			correctionSolutionDao.save(list);
		}
		else
		{
			solution.setAuditStatus(0);
			solution.setCurrentVersion(1);
			solution.setSolutionContent(solutionContent);
			solution.setFileUrl(url);
			solution.setCorrectionId(Long.parseLong(uid));
			correctionSolutionDao.save(solution);
		}
		VendorPerforCorrectionEntity v=correctionDao.findOne(Long.parseLong(uid));
		v.setCorrectionStatus(1);
		correctionDao.save(v);
		return true;
	}
	
	/**
	 * 提交整改数据
	 * @param list
	 */
	public boolean addVendorPerforCorrectionEntity(List<VendorPerforScoresTotalEntity> list, String url) {
		if(CollectionUtils.isEmpty(list))
			return false;
		
		VendorPerforCorrectionEntity correct = null;
		List<VendorPerforCorrectionEntity> correctList = Lists.newArrayList();
		for(VendorPerforScoresTotalEntity total : list) {
			correct = new VendorPerforCorrectionEntity();
			correct.setVendorCode(total.getOrgCode());
			correct.setVendorName(total.getOrgName());
			correct.setBrandName(total.getBrandName());
			correct.setBrandId(total.getBrandId());
			correct.setCycleId(total.getCycleId());
			correct.setAssessDate(total.getAssessDate());
			correct.setCorrectionDate(DateUtil.getCurrentTimestamp());
			correct.setRequireDate(total.getRequireDate());
			correct.setCorrectionStatus(-1);
			correct.setEndStatus(StatusConstant.STATUS_NO);
			correct.setCorrectionContent(total.getCorrectionContent());//整改要求,添加数据必填
			correct.setPlanFilePath(url);
			correctList.add(correct);
		}
		correctionDao.save(correctList);
		return true;
	}

	public Map<String, Object> qrs(
			List<VendorPerforCorrectionEntity> vendorPerforCorrectionEntitys) {
		Map<String, Object> map=new HashMap<String, Object>();
		for(VendorPerforCorrectionEntity v:vendorPerforCorrectionEntitys)
		{
			VendorPerforCorrectionEntity vv=correctionDao.findOne(v.getId());
			vv.setCorrectionStatus(0);
			correctionDao.save(vv);
			List<OrganizationEntity> os = organizationDao.findByCode(vv.getVendorCode());
			if(os!=null&&os.size()>0)
			{
				OrganizationEntity o=os.get(0);
				if(o.getEmail()!=null&&!(o.getEmail().equals("")))
				{
					MailObject mo = new MailObject();
					mo.toMail = o.getEmail();
					mo.templateName = "publishTotal";
					Map<String, String> params = new HashMap<String, String>();
					params.put("vendorName",o.getName());
					params.put("tempMessage","整改要求（“"+vv.getCorrectionContent()+"”）已经发布了，请及时登陆系统查看！");
					params.put("signText","邮件发送");
					mo.params = params;
					mo.title = "整改要求发布";
					mailSendService.send(mo, 5);
				}
			}
		}
		map.put("success", true);
		map.put("msg", "确认成功！");
		return map;
	}

	public String getEnableList3() {
		List<?>  list=correctionDao.getDistinct();
		return getdata(list);
	}
	private String getdata(List<?>  list)
	{
		String data="[";
		int i=0;
		for(Object object:list){
			if(object!=null)
			{
				if(i>0)
				{
					data=data+",";
				}
				data=data+"{\"id\":\""+object+"\",\"text\":\""+object+"\"}";
						i++;
			}
		}
		data=data+"]";
		return data;
	}
}
