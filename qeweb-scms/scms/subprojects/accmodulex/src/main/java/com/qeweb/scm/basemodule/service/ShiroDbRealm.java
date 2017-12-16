package com.qeweb.scm.basemodule.service;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.FirstLoginException;
import org.apache.shiro.IpLoginException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.WebSubject;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Objects;
import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.webservice.LoginWebService;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

public class ShiroDbRealm extends AuthorizingRealm{
	
	protected AccountService accountService;

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * 认证回调函数
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken authToken = (UsernamePasswordToken) token;
		UserEntity user = accountService.findUserEntityByLoginName(StringUtils.toUpperCase(authToken.getUsername()));
		if(user==null) {
			return null;
		}
		if(user.getEnabledStatus()!=null&&user.getEnabledStatus().intValue()!=StatusConstant.STATUS_YES){
			throw new LockedAccountException("用户已被锁定!");
		}
		if(user.getEffectiveTime()!=null&&user.getEffectiveTime().getTime()<DateUtil.getCurrentTimeInMillis()){
			throw new ExpiredCredentialsException("用户账户已到期!");
		}
		
		OrganizationEntity org = user.getCompany();
		//如果组织为空而ID却不为空
		if(org==null && user.getCompanyId()!=null){
			org = accountService.findOrg(user.getCompanyId());
		}
		//如果组织被禁用，无法登录
		if(org!=null && org.getEnableStatus()!=null && org.getEnableStatus().intValue()!=StatusConstant.STATUS_YES){
			throw new DisabledAccountException("用户已被禁用!");
		}
		//供应商第一次登录需要修改密码
		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();  
		if(user.getCompany().getRoleType()==1){
			if(user.getIsNeedUpdatePass()==null||user.getIsNeedUpdatePass()==0){
				request.setAttribute("loginUserId", user.getId());
				throw new FirstLoginException("第一次登录，需要修改密码!");
			}
		}
		
		//采购商ip网段限制
		if(user.getCompany().getRoleType()==0){
			List<String> ipList=accountService.findListByDictCode("IP_SET");
			String ip = ((HttpServletRequest)request).getRemoteAddr();
			if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
				//
				throw new IpLoginException("外网无法访问SRM系统!");
			}else{
				if ("0:0:0:0:0:0:0:1".equals(ip)) {
					ip = "127.0.0.1";
				}
				if(ipList==null||!ipList.contains(ip)){
				}else{
					// 
					throw new IpLoginException("外网无法访问SRM系统!");
				}
			}
		}
		
		//load user data right
		Map<String, Set<Long>> dataPermission = accountService.findUserDataRight(user.getId());
		
		if(user.getRoles().equals("admin")||user.getCompany().getRoleType()==1){
				byte[] saltByte = Encodes.decodeHex(user.getSalt() == null ? "" : user.getSalt());
				SimpleAuthenticationInfo info= new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName(),user.getRoles(),
						org!=null?org.getId():null,
						org!=null?org.getCode():null,
						org!=null?org.getName():null,
						org!=null?org.getRoleType():null,
						org!=null?org.getActiveStatus():null,
						org!=null?org.getEnableStatus():null,				
						org!=null?org.getConfirmStatus():null,
						dataPermission		
						),
						user.getPassword(), ByteSource.Util.bytes(saltByte), user.getName());
				if(info!=null){
					//获取当前电脑的主机名
					String pcName = "";
					InetAddress a;
					try {
						a = InetAddress.getLocalHost();
						pcName = a.getHostName();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					//获取当前电脑的IP
					String ip = ((HttpServletRequest)request).getHeader("x-forwarded-for");
					if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
						ip = ((HttpServletRequest)request).getHeader("Proxy-Client-IP");
					}
					if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
						ip = ((HttpServletRequest)request).getHeader("WL-Proxy-Client-IP");
					}
					if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
						ip = ((HttpServletRequest)request).getRemoteAddr();
					}
					if ("0:0:0:0:0:0:0:1".equals(ip)) {
						ip = "127.0.0.1";
					}
					accountService.saveLoginLog(user.getId(), LogType.USER_LOGIN, user.getName(), user.getCompany().getRoleType()+"", pcName, ip);
				}
				return info;
		}else{
			//采购方使用域集成
			LoginWebService login=new LoginWebService();
//				Boolean bool=login.login(authToken.getUsername(), String.valueOf(authToken.getPassword()));
				Boolean bool=true;
				if(bool){
					authToken.setPassword("111111".toCharArray());
					byte[] saltByte = Encodes.decodeHex(user.getSalt() == null ? "" : user.getSalt());
					SimpleAuthenticationInfo info= new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName(),user.getRoles(),
							org!=null?org.getId():null,
							org!=null?org.getCode():null,
							org!=null?org.getName():null,
							org!=null?org.getRoleType():null,
							org!=null?org.getActiveStatus():null,
							org!=null?org.getEnableStatus():null,				
							org!=null?org.getConfirmStatus():null,
							dataPermission		
							),
							user.getPassword(), ByteSource.Util.bytes(saltByte), user.getName());
					if(info!=null){
						//获取当前电脑的主机名
						String pcName = "";
						InetAddress a;
						try {
							a = InetAddress.getLocalHost();
							pcName = a.getHostName();
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						String ip = ((HttpServletRequest)request).getHeader("x-forwarded-for");
						if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
							ip = ((HttpServletRequest)request).getHeader("Proxy-Client-IP");
						}
						if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
							ip = ((HttpServletRequest)request).getHeader("WL-Proxy-Client-IP");
						}
						if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
							ip = ((HttpServletRequest)request).getRemoteAddr();
						}
						if ("0:0:0:0:0:0:0:1".equals(ip)) {
							ip = "127.0.0.1";
						}
						accountService.saveLoginLog(user.getId(), LogType.USER_LOGIN, user.getName(), user.getCompany().getRoleType()+"", pcName, ip);
					}
					return info;
				}
		}
		return null;
	}
	
	

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		UserEntity user = accountService.findUserEntityByLoginName(shiroUser.loginName);
		SimpleAuthorizationInfo authorInfo = new SimpleAuthorizationInfo();
		authorInfo.addRoles(user.getRoleList());
		Set<String> permissionSet = accountService.findUserPermission(user);
		authorInfo.addStringPermissions(permissionSet);    
		return authorInfo;
	}
	
	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher(){
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
		matcher.setHashIterations(AccountService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable{
		private static final long serialVersionUID = 8027232120487531292L;
		public Long id;
		public String loginName;
		public String name;
		public String roles;
		public String theme;
		public String orgCode;
		public String orgName;
		public Long orgId;
		public Integer orgRoleType;
		public Integer orgActiveStatus;
		public Integer orgEnableStatus;
		public Integer orgConfirmStatus;
		public Map<String, Set<Long>> dataRight;
		
		public ShiroUser(Long id,String loginName,String name,String roles,Long orgId,
				String orgCode,String orgName,Integer orgRoleType,Integer orgActiveStatus
				,Integer orgEnableStatus,Integer orgConfirmStatus, Map<String, Set<Long>> dataRight){
			this.id = id;
			this.loginName = loginName;
			this.name = name;
			this.roles = roles;
			this.orgId = orgId;
			this.orgCode = orgCode;
			this.orgName = orgName;
			this.orgRoleType = orgRoleType;
			this.orgActiveStatus = orgActiveStatus;
			this.orgEnableStatus = orgEnableStatus;
			this.orgConfirmStatus = orgConfirmStatus;
			this.dataRight = dataRight;
		}
		
		public String getName(){
			return name;
		}
		
		public String getTheme() {
			return theme;
		}

		public void setTheme(String theme) {
			this.theme = theme;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public Integer getOrgActiveStatus() {
			return orgActiveStatus;
		}

		public void setOrgActiveStatus(Integer orgActiveStatus) {
			this.orgActiveStatus = orgActiveStatus;
		}

		public Integer getOrgEnableStatus() {
			return orgEnableStatus;
		}

		public void setOrgEnableStatus(Integer orgEnableStatus) {
			this.orgEnableStatus = orgEnableStatus;
		}

		public Integer getOrgConfirmStatus() {
			return orgConfirmStatus;
		}

		public void setOrgConfirmStatus(Integer orgConfirmStatus) {
			this.orgConfirmStatus = orgConfirmStatus;
		}

		public String getRoles() {
			return roles;
		}

		public void setRoles(String roles) {
			this.roles = roles;
		}

		public Integer getOrgRoleType() {
			return orgRoleType;
		}

		public void setOrgRoleType(Integer orgRoleType) {
			this.orgRoleType = orgRoleType;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}
		
		
		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}
		
		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}
	
}
