package com.qeweb.scm.qualityassurance.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.TwoAuditEntity;
import com.qeweb.scm.qualityassurance.entity.TwoAuditSolutionEntity;
import com.qeweb.scm.qualityassurance.repository.TwoAuditDao;
import com.qeweb.scm.qualityassurance.repository.TwoAuditFileDao;
import com.qeweb.scm.qualityassurance.repository.TwoAuditSolutionDao;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCorrectionEntity;

@Service
@Transactional
public class TwoAuditService {
	
	@Autowired
	private TwoAuditDao twoAuditDao;
	
	
	@Autowired
	private TwoAuditSolutionDao twoAuditSolutionDao;
	
	@Autowired
	private TwoAuditFileDao twoAuditFileDao ;
	
	@Autowired
	private SerialNumberService serialNumberService;

	public Page<TwoAuditEntity> getcorrectionList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<TwoAuditEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), TwoAuditEntity.class);
		Page<TwoAuditEntity>  page= twoAuditDao.findAll(spec,pagin);
		for(TwoAuditEntity entity:page.getContent())
		{
			TwoAuditSolutionEntity vcs=twoAuditSolutionDao.findByTwoauditIdAndCurrentVersion(entity.getId(),1);
			if(vcs!=null)
			{
				entity.setSolutionContent(vcs.getSolutionContent());
				if(vcs.getFileUrl()!=null&&!(vcs.getFileUrl().equals("")))
				{
					entity.setFileUrl(vcs.getFileUrl());
				}
			}
		}
		return page;
	}
	
	public Page<TwoAuditEntity> getcorrectionList2(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_orgId", user.orgId);
		searchParamMap.put("NEQ_correctionStatus", -1);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<TwoAuditEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), TwoAuditEntity.class);
		Page<TwoAuditEntity>  page= twoAuditDao.findAll(spec,pagin);
		for(TwoAuditEntity entity:page.getContent())
		{
			TwoAuditSolutionEntity vcs=twoAuditSolutionDao.findByTwoauditIdAndCurrentVersion(entity.getId(),1);
			if(vcs!=null)
			{
				entity.setSolutionContent(vcs.getSolutionContent());
				if(vcs.getFileUrl()!=null&&!(vcs.getFileUrl().equals("")))
				{
					entity.setFileUrl(vcs.getFileUrl());
				}
			}
		}
		return page;
	}

	public Map<String, Object> solutionSubmit(String uid, String typee,String sContent) {
		Map<String, Object> map=new HashMap<String, Object>();
		TwoAuditSolutionEntity solutionEntity=twoAuditSolutionDao.findByTwoauditIdAndCurrentVersion(Long.parseLong(uid),1);
		TwoAuditSolutionEntity solution=new TwoAuditSolutionEntity();
		try {
			if(solutionEntity!=null)
			{
				List<TwoAuditSolutionEntity> list=new ArrayList<TwoAuditSolutionEntity>();
				PropertyUtils.copyProperties(solution, solutionEntity);
				solution.setId(0);
				solution.setAuditStatus(Integer.parseInt(typee));
				solution.setAuditReason(sContent);
				list.add(solution);
				solutionEntity.setCurrentVersion(0);
				list.add(solutionEntity);
				twoAuditSolutionDao.save(list);
				TwoAuditEntity v=twoAuditDao.findOne(Long.parseLong(uid));
				v.setCorrectionStatus(Integer.parseInt(typee)+1);
				twoAuditDao.save(v);
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

	public Page<TwoAuditSolutionEntity> lookSolution(int pageNumber, int pageSize, Map<String, Object> searchParamMap, Long id) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_twoauditId", id);
		searchParamMap.put("NEQ_auditStatus", 0);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<TwoAuditSolutionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), TwoAuditSolutionEntity.class);
		Page<TwoAuditSolutionEntity>  page= twoAuditSolutionDao.findAll(spec,pagin);
		return page;
	}

	public Map<String, Object> endSolution(List<TwoAuditEntity> twoAuditEntitys, String usContent) {
		Map<String, Object> map=new HashMap<String, Object>();
		for(TwoAuditEntity v:twoAuditEntitys)
		{
			TwoAuditEntity vv=twoAuditDao.findOne(v.getId());
			vv.setEndStatus(1);
			vv.setCorrectionStatus(4);
			vv.setCorrectionEndContent(usContent);
			twoAuditDao.save(vv);
		}
		map.put("success", true);
		map.put("msg", "审核成功！");
		return map;
	}

	public boolean combine(String uid, String solutionContent, String url) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TwoAuditSolutionEntity entity=twoAuditSolutionDao.findByTwoauditIdAndCurrentVersion(Long.parseLong(uid), 1);
		TwoAuditSolutionEntity  solution=new TwoAuditSolutionEntity();
		List<TwoAuditSolutionEntity> list=new ArrayList<TwoAuditSolutionEntity>();
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
			twoAuditSolutionDao.save(list);
		}
		else
		{
			solution.setAuditStatus(0);
			solution.setCurrentVersion(1);
			solution.setSolutionContent(solutionContent);
			solution.setFileUrl(url.replaceAll("\\\\", "/"));
			solution.setTwoauditId(Long.parseLong(uid));
			twoAuditSolutionDao.save(solution);
		}
		TwoAuditEntity v=twoAuditDao.findOne(Long.parseLong(uid));
		v.setCorrectionStatus(1);
		twoAuditDao.save(v);
		return true;
	}
	
	public Map<String, Object> qrs(
			List<TwoAuditEntity> twoAuditEntitys) {
		Map<String, Object> map=new HashMap<String, Object>();
		for(TwoAuditEntity v:twoAuditEntitys)
		{
			TwoAuditEntity vv=twoAuditDao.findOne(v.getId());
			vv.setCorrectionStatus(0);
			twoAuditDao.save(vv);
		}
		map.put("success", true);
		map.put("msg", "确认成功！");
		return map;
	}
	
//上传审核  附件  这块
	public Map<String,Object> planUpLoad(File savefile,ILogger logger,TwoAuditEntity PlanEntity,String Millis,String fName) {
		Map<String,Object> map = new HashMap<String, Object>();
		PlanEntity.setWorkOrder(Millis);
		PlanEntity.setPlanFilePath(savefile.getPath().replaceAll("\\\\", "/"));	  
		PlanEntity.setCorrectionStatus(-1);
		PlanEntity.setEndStatus(StatusConstant.STATUS_NO);  					
		twoAuditFileDao.save(PlanEntity);
		map.put("success", true);
		map.put("msg", "审核 计划 附件上传成功!");
		return map;				
	}
}
