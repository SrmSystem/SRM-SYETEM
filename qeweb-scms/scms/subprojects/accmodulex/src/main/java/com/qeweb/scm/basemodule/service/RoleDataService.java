package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
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

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.service.BaseLogService;
import com.qeweb.scm.basemodule.entity.RoleDataCfgEntity;
import com.qeweb.scm.basemodule.entity.RoleDataEntity;
import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.repository.RoleDataCfgDao;
import com.qeweb.scm.basemodule.repository.RoleDataDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDataDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.RoleDataVO;

@Service
@Transactional
public class RoleDataService {
	
	@Autowired
	private RoleDataCfgDao roleDataCfgDao;
	
	@Autowired
	private RoleDataDao roleDataDao;
	
	@Autowired
	private UserDataDao userDataDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired BaseLogService baseLogService;

	public Page<RoleDataCfgEntity> getRoleDataCfgList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleDataCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleDataCfgEntity.class);
		return roleDataCfgDao.findAll(spec,pagin);
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleDataVO> getRoleDataList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		Long roleCfgId = StringUtils.convertToLong(StringUtils.convertToString(searchParamMap.get("roleDataCfgId")));
		RoleDataCfgEntity dataCfgEntity = roleDataCfgDao.findOne(roleCfgId);
		String hql = dataCfgEntity.getDataScope();
		String code = StringUtils.convertToString(searchParamMap.get("LIKE_code"));
		String name = StringUtils.convertToString(searchParamMap.get("LIKE_name"));
		if(code != null && !"".equals(code)){
			if("userEntity".equals(dataCfgEntity.getDataClazz())){
				hql += " and loginName like '%"+ code +"%'";
			}else{
				hql += " and code like '%"+ code +"%'";
			}
		}
		if(name != null && !"".equals(name)){
			hql += " and name like '%"+ name +"%'";
		}
		List<Object[]> list = (List<Object[]>) generialDao.queryByHql(hql);
		if(CollectionUtils.isEmpty(list)) {
			return new ArrayList<RoleDataVO>();
		}
		List<RoleDataVO> rlist = Lists.newArrayList();
		RoleDataVO vo = null;
		for(Object[] obj : list) {
			vo = new RoleDataVO();
			vo.setId(((Long)obj[0]).longValue());
			vo.setCode(StringUtils.convertToString(obj[1]));
			vo.setName(StringUtils.convertToString(obj[2]));
			rlist.add(vo);
		}
		
		return rlist;
	}

	public void deleteUserData(long userId, long roleDataCfgId,ServletRequest request) {
		userDataDao.deleteUserData(userId, roleDataCfgId);
		accountService.initUserDataRight(userId);
		
		//当前操作人
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
				
		baseLogService.saveLog(userId, LogType.USER,"清除数据权限:编码"+roleDataCfgId,user.getName(),pcName);
		
	}
	
	public void deleteRoleData(final long roleId, long roleDataCfgId,ServletRequest request) {
		roleDataDao.deleteRoleData(roleId, roleDataCfgId);
		reloadUserRight(roleId);
		//当前操作人
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
				
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
						
		baseLogService.saveLog(roleId, LogType.ROLE_DATA,"清除数据权限:编码"+roleDataCfgId,user.getName(),pcName);
	}

	private void reloadUserRight(final long roleId) {
		new Thread(new Runnable() { 
			@Override
			public void run() {
				List<Long> userIds = roleUserDao.findUserIdByRoleId(roleId);
				for(Long userId : userIds) {
					accountService.initUserDataRight(userId);
				}
			}
		}).start();
	}
	
	public RoleDataEntity getRoleDataEntity(Long roleId, Long roleDataCfgId) {
		RoleDataEntity roleData = roleDataDao.findByRoleIdAndRoleDataCfgId(roleId, roleDataCfgId);
		if(roleData == null) {
			roleData = new RoleDataEntity();
			roleData.setRoleId(roleId);
			roleData.setRoleDataCfgId(roleDataCfgId);
		}
		
		return roleData;
	}
	
	public UserDataEntity getUserDataEntity(Long userId, Long roleDataCfgId) {
		UserDataEntity userData = userDataDao.findByUserIdAndRoleDataCfgId(userId, roleDataCfgId);
		if(userData == null) {
			userData = new UserDataEntity();
			userData.setUserId(userId);
			userData.setRoleDataCfgId(roleDataCfgId);
		}
		
		return userData;
	}
	
	/**
	 * 保存角色数据权限
	 * @param roleId
	 * @param roleDataCfgId
	 * @param dataIds
	 * @return
	 */
	public Map<String, Object> addRoleData(Long roleId, Long roleDataCfgId, String dataIds,ServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		RoleDataEntity roleData = getRoleDataEntity(roleId, roleDataCfgId);
		String oldDataIds=roleData.getDataIds();
		roleData.setDataIds(dataIds);
		roleDataDao.save(roleData);
		reloadUserRight(roleId);
		
		StringBuilder logContent=new StringBuilder();
		logContent.append("用户数据变更:").append(roleDataCfgId).append(",原权限：").append(oldDataIds==null?"无":oldDataIds).append(",新权限：").append("".equals(dataIds)?"无":dataIds);
		
		//当前操作人
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		baseLogService.saveLog(roleId, LogType.ROLE_DATA, logContent.toString(),user.getName(),pcName);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 保存用户数据权限
	 * @param userId
	 * @param roleDataCfgId
	 * @param dataIds
	 * @return
	 */
	public Map<String, Object> addUserData(Long userId, Long roleDataCfgId, String dataIds,ServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserDataEntity userData = getUserDataEntity(userId, roleDataCfgId);
		String oldDataIds=userData.getDataIds();
		userData.setDataIds(dataIds);
		userDataDao.save(userData);
		accountService.initUserDataRight(userId);
		
		StringBuilder logContent=new StringBuilder();
		logContent.append("用户数据变更:").append(roleDataCfgId).append(",原权限：").append(oldDataIds==null?"无":oldDataIds).append(",新权限：").append("".equals(dataIds)?"无":dataIds);
		
		//当前操作人
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		baseLogService.saveLog(userId, LogType.USER, logContent.toString(),user.getName(),pcName);
		map.put("success", true);
		return map;
	}

	public long getRoleDataListSize(Map<String, Object> searchParamMap) {
		Long roleCfgId = StringUtils.convertToLong(StringUtils.convertToString(searchParamMap.get("roleDataCfgId")));
		RoleDataCfgEntity dataCfgEntity = roleDataCfgDao.findOne(roleCfgId);
		String hql = dataCfgEntity.getDataScope();
		String code = StringUtils.convertToString(searchParamMap.get("LIKE_code"));
		String name = StringUtils.convertToString(searchParamMap.get("LIKE_name"));
		if(code != null && !"".equals(code)){
			if("userEntity".equals(dataCfgEntity.getDataClazz())){
				hql += " and loginName like '%"+ code +"%'";
			}else{
				hql += " and code like '%"+ code +"%'";
			}
		}
		if(name != null && !"".equals(name)){
			hql += " and name like '%"+ name +"%'";
		}
		List<Object[]> list = (List<Object[]>) generialDao.queryByHql(hql);
		return list.size();
	}
}
