package com.qeweb.scm.basemodule.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Service
@Transactional
public class UserServiceImpl {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	
	public List<Long> findEnableUserIds(){
		List<Long> list = userDao.findEnableUserIds();
		return list;
	}

	public void personalSetting(HttpServletRequest request,HttpServletResponse response) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return;
		}
		String loginName=user.loginName;
		UserEntity userEntity=userDao.findByLoginName(loginName);
		request.setAttribute("userEntity", userEntity);
	}

	public String updatePersonalSetting(UserEntity userEntity,HttpServletRequest request,	HttpServletResponse response) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return "0";
		}
		String loginName=StringUtils.toUpperCase(user.loginName);
		userDao.updateUserEntity(loginName, userEntity.getName(), userEntity.getMobile(), userEntity.getEmail());
		return "1";
	}

	public String updatePassword(HttpServletRequest request,HttpServletResponse response) throws Exception {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return "0";
		}
		String loginName=user.loginName;
		String passwords=(String) (request.getParameter("passwords"));
		String newpassword=(String) (request.getParameter("newpassword"));
		String newpassword1=(String) (request.getParameter("newpassword1"));

		if(null!=passwords&&null!=newpassword&&null!=newpassword1)
		{
			UserEntity userEntity=userDao.findByLoginName(loginName);
			if(null!=userEntity.getPassword()&&!("".equals(userEntity.getPassword())))
			{
				byte[] oldPasswordbyte = Digests.sha1(passwords.getBytes(), Encodes.decodeHex(userEntity.getSalt()), HASH_INTERATIONS);
				if(Encodes.encodeHex(oldPasswordbyte).equals(userEntity.getPassword()))
				{
					if(newpassword.equals(newpassword))
					{
						byte[] salt = Digests.generateSalt(SALT_SIZE);
						userEntity.setSalt(Encodes.encodeHex(salt));
						
						byte[] Passwordbyte = Digests.sha1(newpassword.getBytes(),salt, HASH_INTERATIONS);
						userEntity.setPassword(Encodes.encodeHex(Passwordbyte));
						userDao.updatePassword(loginName, userEntity.getPassword(), userEntity.getSalt());
						return "1";
					}
					else
					{
						return "3";
					}
					
				}
				else
				{
					return "0";
				}
			}
			else
			{
				return "3";
			}
		}
		else
		{
			return "3";
		}
	}

}
