package com.qeweb.scm.basemodule.service;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.sap.PurchaseGoodsRequestSAP;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.entity.BacklogCfgEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.BacklogCfgDao;
import com.qeweb.scm.basemodule.repository.ViewDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
/**
 * 待办事项的服务类
 * @author pjjxiajun
 * @date 2015年8月26日
 * @path com.qeweb.scm.basemodule.service.BacklogService.java
 */
@Service
@Transactional
public class BacklogService {
	
	@Autowired
	private BacklogCfgDao backlogCfgDao;
	@Autowired
	private GenerialDao dao;
	@Autowired
	private ViewDao viewDao;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	/**
	 * 查询待办配置的的列表
	 * @param pageNumber 页数
	 * @param pageSize 每页大小
	 * @param queryParamMap 查询参数
	 * @return 每页的配置待办集合
	 */
	public Page<BacklogCfgEntity> getCfgList(int pageNumber, int pageSize, Map<String, Object> queryParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(queryParamMap);
		Specification<BacklogCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BacklogCfgEntity.class);
		return backlogCfgDao.findAll(spec,pagin);
	}
	
	/**
	 * 待办列表【采，供】
	 */
	public List<BacklogCfgEntity> getBacklogList(int pageNumber, int pageSize, Map<String, Object> queryParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(queryParamMap);
		Specification<BacklogCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BacklogCfgEntity.class);
		return backlogCfgDao.findAll(spec);
	}
	
	public Map<String,Object> add(BacklogCfgEntity backlogCfg){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		ViewEntity view = viewDao.findOne(backlogCfg.getViewId());
		backlogCfg.setViewUrl(view.getViewUrl());
		backlogCfg.setViewName(view.getViewName());
		backlogCfgDao.save(backlogCfg);
		return map;
	}
	
	public Map<String,Object> update(BacklogCfgEntity backlogCfg){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		backlogCfgDao.save(backlogCfg);
		return map;
	}
	
	public Map<String,Object> delete(List<BacklogCfgEntity> backlogCfgList){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		backlogCfgDao.delete(backlogCfgList);
		return map;
	}
	
	public Map<String,Object> abolish(List<BacklogCfgEntity> backlogCfgList){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		for(BacklogCfgEntity backlogCfg : backlogCfgList) {
			backlogCfgDao.abolish(backlogCfg.getId());
		}
		return map;
	}

	/**
	 * 获得待办事项列表
	 * @param pageNumber 页数
	 * @param pageSize 页面大小
	 * @param queryParamMap 查询参数
	 * @return 待办列表
	 */
	public List<BacklogCfgEntity> getList(int pageNumber, int pageSize, Map<String, Object> queryParamMap,Map<String, String> systemMap,Integer orgRoleType) {
		List<BacklogCfgEntity> page = new ArrayList<BacklogCfgEntity>();
		StringBuffer log =new StringBuffer();
		try{
			queryParamMap.put("EQ_orgRoleType", orgRoleType);
			
			List<BacklogCfgEntity> cfgList = getBacklogList(pageNumber, pageSize, queryParamMap);
			for(BacklogCfgEntity cfg : cfgList){
				//主要是对数量的查询
				String hql = cfg.getQueryHql();
				if(StringUtils.isNotEmpty(hql)){
					hql = hql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
					hql = hql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
					if(StringUtils.isNotEmpty(systemMap.get("#currentGroupId#"))){
						hql = hql.replaceAll("#currentGroupId#", systemMap.get("#currentGroupId#"));
					}
					List<Object[]> list = (List<Object[]>) dao.queryByHql(hql);
					if(null!=list & list.size()>0){
						cfg.setCount(list.size()+"");
						page.add(cfg);
					}
					continue;
				}
				String sql = cfg.getQuerySql();
			
				if(StringUtils.isNotEmpty(sql)){
					sql = sql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
					sql = sql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
					if(StringUtils.isNotEmpty(systemMap.get("#currentGroupId#"))){
						sql = sql.replaceAll("#currentGroupId#", systemMap.get("#currentGroupId#"));
					}
					log.append(sql);
					List<?> list = dao.queryBySql(sql);
					if(null!=list & list.size()>0){
						cfg.setCount(list.size()+"");
						page.add(cfg);
					}
					
					continue;
				}
			}
		}catch(Throwable e){
			try {
				FileWriter fw = initFw(BacklogService.class);
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
			} catch (Exception e1) {
			}
		}
		return page; 
	}
	
	public static FileWriter initFw(Object object) throws Exception{
		   String cName =  object.toString().replaceAll("_", "");  
	        Date d = new Date();
	        String dstr = DateUtil.dateToString(d, DateUtil.DATE_FORMAT_YYYYMMDD);
	        String fstr = DateUtil.dateToString(d, "yyyyMMdd_HH_mm_ss");
	        File fileroot = new File(PropertiesUtil.getProperty("file.log.dir", "/logs") + "/backlog/" + cName + "/" + dstr);
			if(!fileroot.isDirectory()){
				fileroot.mkdirs();
			}
	        
	        FileWriter fw = new FileWriter(new File(PropertiesUtil.getProperty("file.log.dir", "/logs") + "/backlog/" + cName + "/" + dstr + "/" + fstr + ".log"));
	        return fw;
	}

	/**
	 * 根据ID获得待办配置
	 * @param id 待办配置ID
	 * @return
	 */
	public BacklogCfgEntity getCfg(Long id) {
		BacklogCfgEntity cfg = backlogCfgDao.findOne(id);
		return cfg;
	}
	
	
	/**
	 * 获取待办查询到的所有ID
	 * @param backlogId
	 * @return
	 */
	public List<Long> getBackLogValueIdsById(String backlogId){
		List<Long> idList = new ArrayList<Long>();
		Map<String,String> systemMap =getParameters();
		BacklogCfgEntity  backlog= getCfg(Long.valueOf(backlogId));
		//主要是对数量的查询
		String hql = backlog.getQueryHql();
		if(StringUtils.isNotEmpty(hql)){
			hql = hql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
			hql = hql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
			if(StringUtils.isNotEmpty(systemMap.get("#currentGroupId#"))){
				hql = hql.replaceAll("#currentGroupId#", systemMap.get("#currentGroupId#"));
			}
			List<Object[]> list = (List<Object[]>) dao.queryByHql(hql);
			if(null!=list && list.size()>0){
				for (Object objects : list) {
					idList.add(Long.valueOf(objects.toString()));
				}
			}
		}
		String sql = backlog.getQuerySql();
		if(StringUtils.isNotEmpty(sql)){
			sql = sql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
			sql = sql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
			if(StringUtils.isNotEmpty(systemMap.get("#currentGroupId#"))){
				sql = sql.replaceAll("#currentGroupId#", systemMap.get("#currentGroupId#"));
			}
			List<?> list = dao.queryBySql(sql);
			if(null!=list && list.size()>0){
				for(Object o:list){
					idList.add(Long.valueOf(o.toString()));
				}
			}
		}
		
		if(idList.size()==0){
			idList.add(-1L);
		}
		return idList;
		
	}

	
	/**
	 * 待办替换参数
	 * @return
	 */
	public Map<String,String> getParameters(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,String> systemMap = new HashMap<String, String>();
		systemMap.put("#currentUserId#", user.id+"");
		systemMap.put("#currentOrgId#", user.orgId+"");
		//添加采购组
		List<Long> groupIds = buyerOrgPermissionUtil.getGroupIds();
				StringBuffer groupIdsStr = new StringBuffer();
				if(groupIds != null && groupIds.size()>0){
					groupIdsStr.append("(");
					for (int i=0;i<groupIds.size();i++) {
						groupIdsStr.append("'").append(groupIds.get(i)).append("'");
						if(i!=groupIds.size()-1){
							groupIdsStr.append(",");
						}
					}
					groupIdsStr.append(")");
					systemMap.put("#currentGroupId#", groupIdsStr.toString());
				}
		return systemMap;		
	}
}
