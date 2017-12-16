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
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.RoleViewEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.RoleDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.RoleViewDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleViewDao roleViewDao;
	@Autowired
	private RoleUserDao roleUserDao;
	@Autowired
	private ViewService viewService;


	public Page<RoleEntity> getRoleList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleEntity.class);
		return roleDao.findAll(spec,pagin);
	}


	public Map<String,Object> addNewRole(RoleEntity role) {
		Map<String,Object> map = new HashMap<String, Object>();
		//校验编码和名称的重复性
		if(!checkCode(role.getCode(), role.getId())){
			map.put("success", false);
			map.put("msg", "角色编码已存在");
			return map;
		}
		//校验编码和名称的重复性
		if(!checkName(role.getName(), role.getId())){
			map.put("success", false);
			map.put("msg", "角色名称已存在");
			return map;
		}
		roleDao.save(role);
		map.put("success", true);
		return map;
	}

	public RoleEntity getRole(Long id) {
		return roleDao.findOne(id);
	}

	public Map<String,Object> updateRole(RoleEntity role) {
		Map<String,Object> map = new HashMap<String, Object>();
		//校验编码和名称的重复性
		if(!checkCode(role.getCode(), role.getId())){
			map.put("success", false);
			map.put("msg", "角色编码已存在");
			return map;
		}
		//校验编码和名称的重复性
		if(!checkName(role.getName(), role.getId())){
			map.put("success", false);
			map.put("msg", "角色名称已存在");
			return map;
		}
		roleDao.save(role);
		map.put("success", true);
		return map;
	}

	public void deleteRoleList(List<RoleEntity> roleList) {
		roleDao.delete(roleList);
	}


	/**
	 * 校验编码是否存在
	 * @param code 校验的编码
	 * @param id 
	 * @return
	 */
	public boolean checkCode(String code, Long id) {
		if(id==-1l){
		  RoleEntity role = roleDao.findByCode(code);
		  return role==null?true:false;
		}else{
		  RoleEntity role = roleDao.findByCodeAndIdNot(code,id);
		  return role==null?true:false;
		}
	}


	/**
	 * 校验名称是否存在
	 * @param name 校验的名称
	 * @return 校验结果
	 */
	public boolean checkName(String name, Long id) {
		if(id==-1l){
			RoleEntity role = roleDao.findByName(name);
			return role==null?true:false;
		}else{
			RoleEntity role = roleDao.findByNameAndIdNot(name,id);
			return role==null?true:false;
		}
	}


	/**
	 * 给角色添加菜单权限
	 * @param roleId 角色ID
	 * @param roleMenuList 菜单ID集合
	 * @return
	 */
	public Map<String, Object> addMenuRight(List<RoleViewEntity> roleMenuList) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isEmpty(roleMenuList)){
			map.put("success", true);
			return map;
		}
		Long roleId = roleMenuList.get(0).getRoleId();
		roleViewDao.deleteByRoleId(roleId);
		roleViewDao.save(roleMenuList);
		//更新用户菜单
		List<Long> userIds = roleUserDao.findUserIdByRoleId(roleId);
		if(!CollectionUtils.isEmpty(userIds)) {
			List<ViewEntity> viewList = null;
			for(Long userId : userIds) {
				viewList = viewService.getUserMenusByUserId(userId);
				ViewService.putUserViews(userId, viewList);
			}
		}
		map.put("success", true);
		return map;
	}

    /**
     * 给这个角色指定用户
     * @param roleId 角色ID
     * @param userIdList 用户IdList
     * @return 结果
     */
	public Map<String, Object> addRoleUser(Long roleId, List<Long> userIdList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> userIds = roleUserDao.findUserIdByRoleId(roleId);
		for(Long uid:userIds){
			ViewService.removeUserViews(uid);
		}
		//先删除对应的关系
		roleUserDao.deleteByRoleId(roleId);
		if(CollectionUtils.isEmpty(userIdList)){
			map.put("success", true);
			return map;
		}
		List<ViewEntity> viewList = null;
		for(Long userId : userIdList){
			RoleUserEntity roleUser = new RoleUserEntity();
			roleUser.setRoleId(roleId);
			roleUser.setUserId(userId);
			roleUserDao.save(roleUser);
		}
		for(Long uid:userIds){
			viewList = viewService.getUserMenusByUserId(uid);
			ViewService.putUserViews(uid, viewList);
		}
		map.put("success", true);
		return map;
	}
	
    /**
     * 获得该角色的所有用户
     * @param roleId 角色ID
     * @return 用户集合
     */
	public List<UserEntity> getRoleUserList(Long roleId) {
		return roleUserDao.findUserByRoleId(roleId);
	}

	
	public List<RoleUserEntity> getRoleListByUser(Long userId){
		return roleUserDao.findByUserId(userId);
	}

	public List<RoleEntity> getRoleList() {
		return (List<RoleEntity>) roleDao.findAll();
	}

	public Page<RoleUserEntity> getRoleUserList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleUserEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleUserEntity.class);
		return roleUserDao.findAll(spec,pagin);
	}


}
