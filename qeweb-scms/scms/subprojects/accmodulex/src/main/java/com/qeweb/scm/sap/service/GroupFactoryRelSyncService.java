package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.GroupFactoryRelSAP;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.GroupFactoryRelDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;

/**
 * 工厂对应采购组 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class GroupFactoryRelSyncService {
	@Autowired
	private GroupFactoryRelDao  groupFactoryRelDao;
	
	@Autowired
	private PurchasingGroupDao groupDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	public boolean execute(ILogger iLogger)throws Exception {
		   iLogger.log("GroupFactoryRelSyncService execute method execute start");
		   List<GroupFactoryRelEntity> res=GroupFactoryRelSAP.sync(iLogger);
		   if(res!=null){
			   List<GroupFactoryRelEntity> list= new ArrayList<GroupFactoryRelEntity>();
			   iLogger.log("总条数:"+res.size());
			   for(GroupFactoryRelEntity re: res){
				   GroupFactoryRelEntity groupFacRel=groupFactoryRelDao.findByGroupCodeFactoryCode(re.getGroupCode(),re.getFactoryCode());
			       if(null==groupFacRel){
			    	   iLogger.log("新增采购组:"+re.getGroupCode()+",工厂:"+re.getFactoryCode()+"的关系");
			    	   groupFacRel = new GroupFactoryRelEntity();
			    	   PurchasingGroupEntity group=groupDao.findByCodeAndAbolished(re.getGroupCode(), BOHelper.UNABOLISHED_SINGEL);
			    	   FactoryEntity factory=factoryDao.findByCodeAndAbolished(re.getFactoryCode(), BOHelper.UNABOLISHED_SINGEL);
			    	   if(null!=group && null!=factory){
			    		   groupFacRel.setGroup(group);
			    		   groupFacRel.setFactory(factory);
			    		   groupFacRel.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    		   list.add(groupFacRel);
			    	   }else{
			    		   iLogger.log("采购组:"+re.getGroupCode()+",或工厂:"+re.getFactoryCode()+"不存在，不处理"); 
			    	   }
			       }
			   }
			   groupFactoryRelDao.save(list);
			   iLogger.log("CompanyFactoryRelSyncService execute method execute end");
				return true;
		   }else{
			   iLogger.log("SAP连接失败");
				return false;
		   }
		  
	}
}
