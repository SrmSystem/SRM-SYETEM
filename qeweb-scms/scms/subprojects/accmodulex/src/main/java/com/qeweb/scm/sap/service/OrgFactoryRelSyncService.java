package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.OrgFactoryRelSAP;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.FactoryOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;

/**
 * 工厂对应采购组织 SAP->SRM   OrgFactoryRelSAP
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class OrgFactoryRelSyncService {

	@Autowired
	private FactoryOrganizationRelDao factoryOrganizationRelDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	public boolean execute(ILogger iLogger)throws Exception {
		   iLogger.log("OrgFactoryRelSyncService execute method execute start");
		   List<FactoryOrganizationRelEntity> res=OrgFactoryRelSAP.sync(iLogger);
		   if(res!=null){
			   List<FactoryOrganizationRelEntity> list= new ArrayList<FactoryOrganizationRelEntity>();
			   iLogger.log("总条数:"+res.size());
			   for(FactoryOrganizationRelEntity re: res){
				   FactoryOrganizationRelEntity orgFacRel=factoryOrganizationRelDao.findByFactoryCodeAndOrgCode(re.getFactoryCode(),re.getOrgCode());
			       if(null==orgFacRel){
			    	   iLogger.log("新增采购组织:"+re.getOrgCode()+",工厂:"+re.getFactoryCode()+"的关系");
			    	   orgFacRel = new FactoryOrganizationRelEntity();
			    	   
			    	   List<OrganizationEntity> orgList=organizationDao.findByCode(re.getOrgCode());
			    	   OrganizationEntity org =null;
			    	   if(null!=orgList && orgList.size()>0){
			    		   org=orgList.get(0);
			    	   }
			    	   FactoryEntity factory=factoryDao.findByCode(re.getFactoryCode());
			    	   if(null!=org && null!=factory){
			    		   orgFacRel.setOrgId(org.getId());
			    		   orgFacRel.setFactoryId(factory.getId());
			    		   list.add(orgFacRel);
			    	   }else{
			    		   iLogger.log("采购组织:"+re.getOrgCode()+",或工厂:"+re.getFactoryCode()+"不存在，不处理"); 
			    	   }
			       }
			   }
			   factoryOrganizationRelDao.save(list);
			   iLogger.log("OrgFactoryRelSyncService execute method execute end");
				return true;
		   }else{
			   iLogger.log("SAP连接失败");
				return false;
		   }
		  
	}
}
