package com.qeweb.scm.basemodule.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
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
import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.service.BaseLogService;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.DataType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.DictItemDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.UserDataDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
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
	
	@Autowired
	private UserDataDao userDataDao;
	
	@Autowired
	private BaseLogService  baseLogService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private DictItemDao dictItemDao;
	
	public List<String> findListByDictCode(String dictCode){
		return dictItemDao.findListByDictCode("IP_SET");
	}
	

	public void saveUserEntity(UserEntity user){
		userDao.save(user);
	}
	
	public void enableUserEntity(UserEntity user,ServletRequest request){
		userDao.save(user);
		//当前操作人
		ShiroUser optUser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		baseLogService.saveLog(user.getId(), LogType.USER, "生效",optUser.getName(),pcName);
	}
	public void disableUserEntity(UserEntity user,ServletRequest request){
		userDao.save(user);
		//当前操作人
		ShiroUser optUser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//获取当前电脑的主机名
		String pcName = request.getRemoteAddr();
		
		baseLogService.saveLog(user.getId(), LogType.USER, "锁定",optUser.getName(),pcName);
	}

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
		
		if(user.getRoles().equals("admin")){
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
		long t1 = DateUtil.getCurrentTimeInMillis();
		String sql =  "SELECT DC.ID, DC.DATA_CODE,RD.DATA_IDS FROM ("
			+ " SELECT * FROM QEWEB_ROLE_USER RU  WHERE RU.USER_ID = :uid) RUX INNER JOIN QEWEB_ROLE_DATA RD ON RUX.ROLE_ID = RD.ROLE_ID "
			+ " INNER JOIN QEWEB_ROLE_DATA_CFG DC ON RD.ROLE_DATA_CFG_ID = DC.ID "
			+ " UNION ALL "
			+ " SELECT DC2.ID,DC2.DATA_CODE, UDX.DATA_IDS FROM (SELECT * FROM QEWEB_USER_DATA UD WHERE UD.USER_ID = :uid) UDX " 
			+ " INNER JOIN QEWEB_ROLE_DATA_CFG DC2 ON DC2.ID = UDX.ROLE_DATA_CFG_ID ";
		Map<String, Object> param = Maps.newHashMap();
		param.put("uid", userId);
		List<Object[]> list = (List<Object[]>) generialDao.queryBySql(sql, param);
		if(CollectionUtils.isEmpty(list))
			return retMap;
		
		for(Object[] obj : list) {
			String reString = StringUtils.convertToString(obj[2]);
			Set<Long> set= convertDataToSet(reString);
			if(retMap.get(StringUtils.convertToString(obj[1]))==null){
				retMap.put(StringUtils.convertToString(obj[1]), set);
			}else{
				set.addAll(retMap.get(StringUtils.convertToString(obj[1])));
				retMap.put(StringUtils.convertToString(obj[1]), set);
			}
		}
		
		long t2 = DateUtil.getCurrentTimeInMillis();
		logger.info("Loading user data right success use time :" + (t2-t1)); 
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
		UserEntity oldUser=userDao.findById(user.getId());
		
		StringBuilder sb=new StringBuilder();
		if(oldUser.getLoginName()!=null&&user.getLoginName()!=null){
			if(!oldUser.getLoginName().equals(user.getLoginName())){
				sb.append("登录名由").append(oldUser.getLoginName()).append("改为").append(user.getLoginName());
			}
		}
		if(oldUser.getName()!=null&&user.getName()!=null){
			if(!oldUser.getName().equals(user.getName())){
				sb.append("昵称由").append(oldUser.getName()).append("改为").append(user.getName());
			}
		}
		if(oldUser.getDep()!=null&&user.getDep()!=null){
			if(!oldUser.getDep().equals(user.getDep())){
				sb.append("所属部门由").append(oldUser.getDep()).append("改为").append(user.getDep());
			}
		}
		if(oldUser.getEmail()!=null&&user.getEmail()!=null){
			if(!oldUser.getEmail().equals(user.getEmail())){
				sb.append("Email由").append(oldUser.getEmail()).append("改为").append(user.getEmail());
			}
		}
		if(oldUser.getEffectiveTime()!=null&&user.getEffectiveTime()!=null){
			if(oldUser.getEffectiveTime().getTime()!=user.getEffectiveTime().getTime()){
				sb.append("账户有效期由").append(DateUtil.dateToString(oldUser.getEffectiveTime(), "yyyy-MM-dd") ).append("改为").append(DateUtil.dateToString(user.getEffectiveTime(), "yyyy-MM-dd"));
			}
		}
		
		oldUser.setCompanyId(user.getCompanyId());
		oldUser.setLoginName(user.getLoginName());
		oldUser.setName(user.getName());
		oldUser.setDep(user.getDep());
		oldUser.setEmail(user.getEmail());
		oldUser.setEffectiveTime(user.getEffectiveTime());
		oldUser.setAacCompany(user.getAacCompany());
		oldUser.setAbolished(Constant.UNDELETE_FLAG);
		oldUser.setClassSystemId(user.getClassSystemId());
		if(sb.toString().length()>0){
			//当前操作人
			ShiroUser optUser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			
			//获取当前电脑的主机名
			String pcName = "";
			InetAddress a;
			try {
				a = InetAddress.getLocalHost();
				pcName = a.getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			baseLogService.saveLog(user.getId(), LogType.USER, sb.toString(),optUser.getName(),pcName);
		}
		userDao.save(oldUser);
	}
	
	public void updateUsers(List<UserEntity> userList){
		userDao.save(userList);
	}

	public void deleteUser(Long id) {
		//userDao.delete(id);
		userDao.abolish(id);
		
	}
	
	public void registerUser(UserEntity user) {
		entryptPassword(user);
		user.setRoles("user");
		user.setLoginName(StringUtils.toUpperCase(user.getLoginName()));
		user.setRegisterDate(clock.getCurrentStamp());
		user.setEnabledStatus(StatusConstant.STATUS_YES);
		user.setAbolished(StatusConstant.STATUS_NO);
		try{
			userDao.save(user);
			
			UserDataEntity userData = new UserDataEntity();
			userData.setRoleDataCfgId(DataType.DATA_TYPE_BUYER);
			userData.setDataIds(user.getCompanyId()+",");
			userData.setUserId(user.getId());
			userDataDao.save(userData);
			
			UserDataEntity userData2 = new UserDataEntity();
			userData2.setRoleDataCfgId(DataType.DATA_TYPE_USER);
			userData2.setDataIds(user.getId()+",");
			userData2.setUserId(user.getId());
			userDataDao.save(userData2);
			
			initUserDataRight(user.getId());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteUsers(List<UserEntity> userList) {
		//userDao.delete(userList);
		for (UserEntity userEntity : userList) {
			UserEntity _user = userDao.findById(userEntity.getId());
			_user.setLoginName(_user.getLoginName()+DateUtil.getCurrentTimeInMillis());
			_user.setAbolished(Constant.DELETE_FLAG);
			userDao.save(_user);
		}
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
	
	public void saveLoginLog(Long billId,String billType,String optUserName,String roleType,String pcName,String ip){
		baseLogService.saveLoginLog(billId, billType, optUserName, roleType, pcName, ip);
	}

}
