package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.GroupOrganizationRelEntity;
import com.qeweb.scm.basemodule.repository.GroupOrganizationRelDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class GroupOrganizationRelService {
	
	@Autowired
	private GroupOrganizationRelDao groupOrganizationRelDao;
	
	
    /**
     * 获取对应关系表
     */
	public Page<GroupOrganizationRelEntity> getGroupOrganizationRelList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<GroupOrganizationRelEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), GroupOrganizationRelEntity.class);
		return groupOrganizationRelDao.findAll(spec,pagin);
	}
	
	
	/**
     * 新增对应关系
     */
	public void addNewGroupOrganizationRel(GroupOrganizationRelEntity groupOrganizationRel) {
		groupOrganizationRelDao.save(groupOrganizationRel);
	}
	
	/**
     * 查詢对应关系
     */
	public List<GroupOrganizationRelEntity> findAll(){
		return groupOrganizationRelDao.findAll();
	}
	
	/**
     * 获取单个对应关系
     */
	public  GroupOrganizationRelEntity getGroupOrganizationRel(Long id) {
		return groupOrganizationRelDao.findOne(id);
	}
	
	/**
     * 通过采购组和采购组织的id获取数据
     */
	public  GroupOrganizationRelEntity getGroupOrganizationRelByGroupIdAndOrgId(Long groupId , Long orgId) {
		return groupOrganizationRelDao.getGroupOrganizationRelByGroupIdAndOrgIdAndAbolished(groupId , orgId,0);
	}
	
	
	/**
     * 删除对应关系
     */
	public void deleteGroupOrganizationRel(List<GroupOrganizationRelEntity> groupOrganizationRelList) {
		groupOrganizationRelDao.delete(groupOrganizationRelList);
	}
	
	/**
     *作废对应关系
     */
	public Map<String, Object> abolishBatch(List<GroupOrganizationRelEntity> groupOrganizationRelList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Long result = 0l;
		for(GroupOrganizationRelEntity groupOrganizationRel : groupOrganizationRelList){
			groupOrganizationRelDao.abolishGroupOrg(groupOrganizationRel.getId());
			result++;
		}
		if(result != groupOrganizationRelList.size() ){
			map.put("success", false);
		}else{
			map.put("success", true);
		}
		return map;
	}
	
}
