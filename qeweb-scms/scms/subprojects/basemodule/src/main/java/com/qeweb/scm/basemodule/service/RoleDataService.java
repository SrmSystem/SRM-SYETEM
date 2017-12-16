package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
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
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.RoleDataCfgEntity;
import com.qeweb.scm.basemodule.entity.RoleDataEntity;
import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.repository.RoleDataCfgDao;
import com.qeweb.scm.basemodule.repository.RoleDataDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDataDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
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

	public Page<RoleDataCfgEntity> getRoleDataCfgList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<RoleDataCfgEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleDataCfgEntity.class);
		return roleDataCfgDao.findAll(spec,pagin);
	}
	
	@SuppressWarnings("unchecked")
	public Page<RoleDataVO> getRoleDataList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		Long roleCfgId = StringUtils.convertToLong(StringUtils.convertToString(searchParamMap.get("roleDataCfgId")));
		RoleDataCfgEntity dataCfgEntity = roleDataCfgDao.findOne(roleCfgId);
		List<Object[]> list = (List<Object[]>) generialDao.queryByHql(dataCfgEntity.getDataScope());
		if(CollectionUtils.isEmpty(list)) {
			return new PageImpl<RoleDataVO>(new ArrayList<RoleDataVO>());
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
		
		return new PageImpl<RoleDataVO>(rlist);
	}

	public void deleteUserData(long userId, long roleDataCfgId) {
		userDataDao.deleteUserData(userId, roleDataCfgId);
		accountService.initUserDataRight(userId);
		
	}
	
	public void deleteRoleData(final long roleId, long roleDataCfgId) {
		roleDataDao.deleteRoleData(roleId, roleDataCfgId);
		reloadUserRight(roleId);
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
	public Map<String, Object> addRoleData(Long roleId, Long roleDataCfgId, String dataIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		RoleDataEntity roleData = getRoleDataEntity(roleId, roleDataCfgId);
		roleData.setDataIds(dataIds);
		roleDataDao.save(roleData);
		reloadUserRight(roleId);
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
	public Map<String, Object> addUserData(Long userId, Long roleDataCfgId, String dataIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserDataEntity userData = getUserDataEntity(userId, roleDataCfgId);
		userData.setDataIds(dataIds);
		userDataDao.save(userData);
		accountService.initUserDataRight(userId);
		map.put("success", true);
		return map;
	}
}
