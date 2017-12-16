package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.BacklogCfgEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.BacklogCfgDao;
import com.qeweb.scm.basemodule.repository.ViewDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
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
	public Page<BacklogCfgEntity> getList(int pageNumber, int pageSize, Map<String, Object> queryParamMap,Map<String, String> systemMap,Integer orgRoleType) {
		queryParamMap.put("EQ_orgRoleType", orgRoleType);
		Page<BacklogCfgEntity> page = getCfgList(pageNumber, pageSize, queryParamMap);
		List<BacklogCfgEntity> cfgList = page.getContent();
		for(BacklogCfgEntity cfg : cfgList){
			//主要是对数量的查询
			String hql = cfg.getQueryHql();
			if(StringUtils.isNotEmpty(hql)){
				hql = hql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
				hql = hql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
				Integer count = dao.findCountByHql(hql);
				cfg.setCount(count+"");
				continue;
			}
			String sql = cfg.getQuerySql();
			if(StringUtils.isNotEmpty(sql)){
				sql = sql.replaceAll("#currentUserId#", systemMap.get("#currentUserId#"));
				sql = sql.replaceAll("#currentOrgId#", systemMap.get("#currentOrgId#"));
				Integer count = dao.findCountBySql(sql);
				cfg.setCount(count+"");
				continue;
			}
		}
		return page;
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
	

}
