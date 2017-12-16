package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.CompanyFactoryRelSAP;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.repository.CompanyFactoryRelDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;

/**
 * 公司对应工厂信息 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class CompanyFactoryRelSyncService {
	@Autowired
	private CompanyFactoryRelDao  companyFactoryRelDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	public boolean execute(ILogger iLogger)throws Exception {
		   iLogger.log("CompanyFactoryRelSyncService execute method execute start");
		   List<CompanyFactoryRelEntity> res=CompanyFactoryRelSAP.sync(iLogger);
		   if(res!=null){
			   List<CompanyFactoryRelEntity> list= new ArrayList<CompanyFactoryRelEntity>();
			   iLogger.log("总条数:"+res.size());
			   for(CompanyFactoryRelEntity re: res){
				   CompanyFactoryRelEntity comFacRel=companyFactoryRelDao.findByCompanyCodeFactoryCode(re.getCompanyCode(),re.getFactoryCode());
			       if(null==comFacRel){
			    	   iLogger.log("新增companyCode:"+re.getCompanyCode()+",factoryCode:"+re.getFactoryCode());
			    	   comFacRel = new CompanyFactoryRelEntity();
			    	   CompanyEntity company=companyDao.findByCode(re.getCompanyCode());
			    	   FactoryEntity factory=factoryDao.findByCode(re.getFactoryCode());
			    	   if(null!=company && null!=factory){
			    		   comFacRel.setCompanyId(company.getId());
			    		   comFacRel.setFactoryId(factory.getId());
			    		   list.add(comFacRel);
			    	   }else{
			    		   iLogger.log("companyCode:"+re.getCompanyCode()+",或factoryCode:"+re.getFactoryCode()+"不存在，不处理"); 
			    	   }
			       }
			   }
			   companyFactoryRelDao.save(list);
			   iLogger.log("CompanyFactoryRelSyncService execute method execute end");
				return true;
		   }else{
			   iLogger.log("SAP连接失败");
				return false;
		   }
		  
	}
}
