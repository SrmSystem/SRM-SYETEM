package com.qeweb.scm.basemodule.entity.listener;

import javax.persistence.PrePersist;

import org.apache.shiro.SecurityUtils;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;

public class PersistenceObjectListener {
	
	@PrePersist
	public void preSave(BaseEntity baseEntity){
		//判断是否来源于外部数据，接口等
		if(baseEntity.getIsOutData()==StatusConstant.STATUS_YES){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return;
		}
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return;
		}
		
		if(baseEntity.getId()==0l){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setCreateUserId(user.id);
			baseEntity.setCreateUserName(user.getName());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setUpdateUserId(user.id);
			baseEntity.setUpdateUserName(user.getName());
		}else{
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setUpdateUserId(user.id);
			baseEntity.setUpdateUserName(user.getName());
		}
		if(baseEntity.getAbolished()==null){
			baseEntity.setAbolished(0);
		}
	}

}
