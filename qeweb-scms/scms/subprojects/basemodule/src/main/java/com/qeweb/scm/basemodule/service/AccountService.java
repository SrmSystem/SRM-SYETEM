package com.qeweb.scm.basemodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.ClockEX;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Component
@Transactional
public class AccountService {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);
	private static ClockEX clock = ClockEX.DEFAULT;
	public static Map<Long, Map<String, Set<Long>>> USER_DATARIGHT_MAP = new HashMap<Long, Map<String,Set<Long>>>();
	
	@Autowired
	private UserDao userDao; 
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private GenerialDao generialDao;

	public UserEntity findUserEntityByLoginName(String loginName) {
		UserEntity user = userDao.findByLoginName(loginName);
		return user;
	}
	
	public Page<UserEntity> getAllUser(int pageNumber, int number) {
		PageRequest pagin = new PageRequest(pageNumber-1, number);
		return userDao.findAll(pagin);
	}
	
	public Page<UserEntity> getUsers(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<UserEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), UserEntity.class);
		return userDao.findAll(spec,pagin);
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> findUserPermission(UserEntity user) {
		List<Object> list = null;
		String sql = "SELECT PERMISSION FROM QEWEB_VIEW WHERE ID IN("
				+ "SELECT VIEW_ID FROM QEWEB_ROLE_VIEW WHERE ROLE_ID IN("
				+ "SELECT ROLE_ID FROM QEWEB_ROLE_USER WHERE USER_ID = :uid))";
		
		if(ViewService.ADMIN_LOGIN_NAME.equalsIgnoreCase(user.getLoginName())){
			sql = "SELECT PERMISSION FROM QEWEB_VIEW";
			list = (List<Object>) generialDao.queryBySql(sql);
		} else {
			Map<String, Object> param = Maps.newHashMap();
			param.put("uid", user.getId());
			list = (List<Object>) generialDao.queryBySql(sql, param);
		}
		Set<String> retSet = Sets.newHashSet();
		if(CollectionUtils.isEmpty(list))
			return retSet;
		
		for(Object obj : list) {
			if(obj == null)
				continue;
			
			retSet.add(StringUtils.convertToString(obj));
		}

		return retSet;
	}
	
	public void initUserDataRight(Long userId) {
		USER_DATARIGHT_MAP.put(userId, findUserDataPermission(userId));
	}
	
	public Map<String, Set<Long>> findUserDataRight(Long userId) {
		if(USER_DATARIGHT_MAP.get(userId) == null)
			initUserDataRight(userId);
			
		return USER_DATARIGHT_MAP.get(userId);
	}
	
	/**
	 * 获取用户数据权限
	 * @param user
	 * @return Map<String, Set<Long>> key:数据权限code, value:数据权限值
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Set<Long>> findUserDataPermission(Long userId) {
		logger.info("Start loading user data right."); 
		Map<String, Set<Long>> retMap = Maps.newHashMap();
//		long t1 = DateUtil.getCurrentTimeInMillis();
//		String sql =  "SELECT DC.ID, DC.DATA_CODE,RD.DATA_IDS FROM ("
//			+ " SELECT * FROM QEWEB_ROLE_USER RU  WHERE RU.USER_ID = :uid) RUX INNER JOIN QEWEB_ROLE_DATA RD ON RUX.ROLE_ID = RD.ROLE_ID "
//			+ " INNER JOIN QEWEB_ROLE_DATA_CFG DC ON RD.ROLE_DATA_CFG_ID = DC.ID "
//			+ " UNION ALL "
//			+ " SELECT DC2.ID,DC2.DATA_CODE, UDX.DATA_IDS FROM (SELECT * FROM QEWEB_USER_DATA UD WHERE UD.USER_ID = :uid) UDX " 
//			+ " INNER JOIN QEWEB_ROLE_DATA_CFG DC2 ON DC2.ID = UDX.ROLE_DATA_CFG_ID ";
//		Map<String, Object> param = Maps.newHashMap();
//		param.put("uid", userId);
//		List<Object[]> list = (List<Object[]>) generialDao.queryBySql(sql, param);
//		if(CollectionUtils.isEmpty(list))
//			return retMap;
//		
//		for(Object[] obj : list) {
//			String reString = StringUtils.convertToString(obj[2]);
//			Set<Long> set= convertDataToSet(reString);
//			if(retMap.get(StringUtils.convertToString(obj[1]))==null){
//				retMap.put(StringUtils.convertToString(obj[1]), set);
//			}else{
//				set.addAll(retMap.get(StringUtils.convertToString(obj[1])));
//				retMap.put(StringUtils.convertToString(obj[1]), set);
//			}
//		}
//		
//		long t2 = DateUtil.getCurrentTimeInMillis();
//		logger.info("Loading user data right success use time :" + (t2-t1)); 
		return retMap;
	}

	/**
	 * 权限数据Id转成Set
	 * @param params
	 * @return
	 */
	private Set<Long> convertDataToSet(String params) {
		Set<Long> retSet = Sets.newHashSet();
		//没有数据权限
		if(StringUtils.isEmpty(params)) {
			retSet.add(Long.MAX_VALUE);
			return retSet;
		}
		
		String[] dataids = StringUtils.split(params, ",");
		Long data = null;
		for(String _data : dataids) {
			data = StringUtils.convertToLong(_data);
			if(data == null)
				continue;
			
			retSet.add(data);
		}
		
		return retSet;
	}

	public UserEntity getUser(Long id) {
		UserEntity user = userDao.findOne(id);
		return user;
	}

	public void updateUser(UserEntity user) {
		userDao.save(user);
		
	}
	
	public void updateUsers(List<UserEntity> userList){
		userDao.save(userList);
	}

	public void deleteUser(Long id) {
		userDao.delete(id);
		
	}
	
	public void registerUser(UserEntity user) {
		entryptPassword(user);
		user.setRoles("user");
		user.setLoginName(StringUtils.toUpperCase(user.getLoginName()));
		user.setRegisterDate(clock.getCurrentStamp());
		user.setEnabledStatus(StatusConstant.STATUS_YES);
		try{
		userDao.save(user);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteUsers(List<UserEntity> userList) {
		userDao.delete(userList);
	}
	
	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	protected void entryptPassword(UserEntity user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}
	
	/**
	 * 查找组织
	 * @param companyId 公司ID
	 * @return 组织
	 */
	public OrganizationEntity findOrg(Long companyId) {
		return orgDao.findOne(companyId);
	}

}
