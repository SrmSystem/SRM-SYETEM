package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.service.BaseLogService;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.RoleViewEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.repository.RoleDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.RoleViewDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
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
	
	@Autowired
	private BaseLogService baseLogService;


	public Page<RoleEntity> getRoleList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleEntity.class);
		return roleDao.findAll(spec,pagin);
	}
	
	public Page<RoleUserEntity> getRoleUser(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleUserEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleUserEntity.class);
		return roleUserDao.findAll(spec,pagin);
	}


	public Map<String,Object> addNewRole(Map<String,String> mapC) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		String code = mapC.get("code");
		String name = mapC.get("name");
		String isVendor = mapC.get("isVendor");
		String remark = mapC.get("remark");
		
		//校验编码和名称的重复性
		if(!checkCode(code, -1L)){
			map.put("success", false);
			map.put("msg", "角色编码已存在");
			return map;
		}
		//校验编码和名称的重复性
		if(!checkName(name,-1L)){
			map.put("success", false);
			map.put("msg", "角色名称已存在");
			return map;
		}
		
		RoleEntity role = new RoleEntity();
		role.setCode(code);
		role.setName(name);
		role.setIsVendor(isVendor);
		role.setRemark(remark);
		roleDao.save(role);
		map.put("success", true);
		return map;
	}

	public RoleEntity getRole(Long id) {
		return roleDao.findOne(id);
	}

	public Map<String,Object> updateRole(Map<String,String> mapC,ServletRequest request) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		Long id = Long.valueOf(mapC.get("id"));
		String code = mapC.get("code");
		String name = mapC.get("name");
		String isVendor = mapC.get("isVendor");
		String remark = mapC.get("remark");
		
		
		
		//校验编码和名称的重复性
		if(!checkCode(code, id)){
			map.put("success", false);
			map.put("msg", "角色编码已存在");
			return map;
		}
		//校验编码和名称的重复性
		if(!checkName(name, id)){
			map.put("success", false);
			map.put("msg", "角色名称已存在");
			return map;
		}
		
		//当前操作人
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuilder sb=new StringBuilder();
		
		modifyData(sb,code,name,isVendor,remark,id);//修改数据变更拼接

		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		baseLogService.saveLog(id, LogType.ROLE_DATA, sb.toString(),user.getName(),pcName);
		RoleEntity role = new RoleEntity();
		role.setId(id);
		role.setCode(code);
		role.setName(name);
		role.setIsVendor(isVendor);
		role.setRemark(remark);
		roleDao.save(role);
		map.put("success", true);
		return map;
	}

	private void modifyData(StringBuilder sb, String code, String name,
			String isVendor, String remark,Long id) {
		RoleEntity oldRole = roleDao.findOne(id);
		if(oldRole.getCode()!=null&&code!=null){
			if(!oldRole.getCode().equals(code)){
				sb.append("角色编码").append(oldRole.getCode()).append("改为").append(code).append(",");
			}
		}
		
		if(oldRole.getName()!=null&&name!=null){
			if(!oldRole.getName().equals(name)){
				sb.append("角色名称").append(oldRole.getName()).append("改为").append(name).append(",");
			}
		}
		
		if(oldRole.getIsVendor()!=null&&isVendor!=null){
			if(!oldRole.getIsVendor().equals(isVendor)){
				String oldStr = "";
				if("Y".equals(oldRole.getIsVendor())){
					oldStr = "供应商";
				}else{
					oldStr="采购商";
				}
				
				String newStr = "";
				if("Y".equals(isVendor)){
					newStr = "供应商";
				}else{
					newStr="采购商";
				}
				
				sb.append("角色类型").append(oldStr).append("改为").append(newStr).append(",");
			}
		}
		
		if(oldRole.getRemark()!=null&&remark!=null){
			if(!oldRole.getRemark().equals(remark)){
				sb.append("备注").append(oldRole.getRemark()).append("改为").append(remark).append(",");
			}
		}
		
	}

	public void deleteRoleList(List<RoleEntity> roleList,ServletRequest request) {
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		for(RoleEntity role:roleList){
			baseLogService.saveLog(role.getId(), LogType.ROLE_DATA, "删除角色"+role.getName(),user.getName(),pcName);
		}
		
		roleDao.delete(roleList);
		roleDao.findAll();
	}
	
	public Boolean validateDeleteRole(List<RoleEntity> roleList) {
		Boolean bool=true;
		for (RoleEntity roleEntity : roleList) {
			List<RoleUserEntity> res=roleUserDao.findByRoleId(roleEntity.getId());
			if(res!=null&&res.size()>0){
				bool=false;
				return bool;
			}
		}
		return bool;
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
		  RoleEntity role = roleDao.findByCodeAndAbolishedAndIdNot(code, Constant.UNDELETE_FLAG, id);
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
			RoleEntity role = roleDao.findByNameAndAbolishedAndIdNot(name, Constant.UNDELETE_FLAG, id);
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
	public Map<String, Object> addRoleUser(Long roleId, List<Long> userIdList,ServletRequest request) {
		
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
		
		//记录用户角色权限变动日志
		RoleEntity role=roleDao.findOne(roleId);
		
		List<Long> delIds=userIds;
		List<Long> newIds=userIdList;
		delIds.removeAll(userIdList);//删除的
		newIds.removeAll(userIds);//新增的
		//当前操作人
		ShiroUser optUser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		
		for (Long long1 : delIds) {
			baseLogService.saveLog(long1, LogType.USER, "权限删除，角色编码"+role.getCode(),optUser.getName(),pcName);
		}
		for (Long long1 : newIds) {
			baseLogService.saveLog(long1, LogType.USER, "权限新增，角色编码"+role.getCode(),optUser.getName(),pcName);
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
