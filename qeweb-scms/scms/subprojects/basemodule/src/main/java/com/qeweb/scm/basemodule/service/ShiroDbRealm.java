package com.qeweb.scm.basemodule.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Objects;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
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
		OrganizationEntity org = user.getCompany();
		//如果组织为空而ID却不为空
		if(org==null && user.getCompanyId()!=null){
			org = accountService.findOrg(user.getCompanyId());
		}
		//如果组织被禁用，无法登录
		if(org!=null && org.getEnableStatus()!=null && org.getEnableStatus().intValue()!=StatusConstant.STATUS_YES){
			throw new DisabledAccountException("用户已被禁用!");
		}
		//load user data right
		Map<String, Set<Long>> dataPermission = accountService.findUserDataRight(user.getId());
		if(user != null) {
			byte[] saltByte = Encodes.decodeHex(user.getSalt() == null ? "" : user.getSalt());
			return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName(),
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
		public String theme;
		public String orgCode;
		public String orgName;
		public Long orgId;
		public Integer orgRoleType;
		public Integer orgActiveStatus;
		public Integer orgEnableStatus;
		public Integer orgConfirmStatus;
		public Map<String, Set<Long>> dataRight;
		
		public ShiroUser(Long id,String loginName,String name,Long orgId,
				String orgCode,String orgName,Integer orgRoleType,Integer orgActiveStatus
				,Integer orgEnableStatus,Integer orgConfirmStatus, Map<String, Set<Long>> dataRight){
			this.id = id;
			this.loginName = loginName;
			this.name = name;
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
