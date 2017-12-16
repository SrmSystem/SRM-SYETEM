package com.qeweb.scm.sap.service;

import org.apache.shiro.SecurityUtils;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;

public class BOHelper {
	public static  Integer UNABOLISHED_SINGEL=0;//未作废
	public static  Integer ABOLISHED_SINGEL=1;//作废
	
	public static Integer OUT_DATA_YES =1;//来源于外部数据
	
	
	public static void setBOPublicFields_insert(BaseEntity bc) {
		bc.setCreateTime(DateUtil.getCurrentTimestamp());
		bc.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		bc.setAbolished(UNABOLISHED_SINGEL);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		bc.setCreateUserId(user.id);
		bc.setUpdateUserId(user.id);
		bc.setCreateUserName(user.name);
		bc.setUpdateUserName(user.name);
	}
	
	
	public static void setBOPublicFields_update(BaseEntity bc) {
		bc.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		bc.setAbolished(UNABOLISHED_SINGEL);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		bc.setUpdateUserId(user.id);
		bc.setUpdateUserName(user.name);
	}
	
	public static void setBOPublicFields_abolish(BaseEntity bc) {
		bc.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		bc.setAbolished(ABOLISHED_SINGEL);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		bc.setUpdateUserId(user.id);
		bc.setUpdateUserName(user.name);
	}
}
