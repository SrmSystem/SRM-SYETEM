package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.OrganizationSAP;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.DateUtil;

/**
 * 采购组织 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class OrganizationSyncService {
	@Autowired
	private OrganizationDao organizationDao;
	
	/**
	 * 同步sap采购组织数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("OrganizationSyncService execute method execute start");
	   List<OrganizationEntity> res=OrganizationSAP.sync(iLogger);
	   if(res!=null){
		    List<OrganizationEntity> list =new ArrayList<OrganizationEntity>();
			iLogger.log("总条数:"+res.size());
			for (OrganizationEntity re : res) {
				List<OrganizationEntity> orgList=organizationDao.findByCode(re.getCode());
				OrganizationEntity org = null;
				if(null!=orgList && orgList.size()>0){
					org=orgList.get(0);
				}
				if(null==org){
			    	 iLogger.log("新增"+re.getCode());
			    	 org = new OrganizationEntity();
			    	 org.setCode(re.getCode());
			    	 org.setName(re.getName());
			    	 org.setCreateTime(DateUtil.getCurrentTimestamp());
			    	 org.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    	 org.setOrgType(OrgType.ORG_TYPE_COMPANY);
			    	 org.setRoleType(OrgType.ROLE_TYPE_BUYER);
			    	 org.setActiveStatus(StatusConstant.STATUS_YES);
			    	 org.setEnableStatus(StatusConstant.STATUS_YES);
			    	 org.setConfirmStatus(StatusConstant.STATUS_YES);
			    	 org.setIsOutData(BOHelper.OUT_DATA_YES);
			    	 list.add(org);
			     }else{
			    	 if(!re.getName().equals(org.getName())){
			    		 iLogger.log("修改"+re.getCode());
			    		 org.setName(re.getName());
			    		 org.setIsOutData(BOHelper.OUT_DATA_YES);
				    	 list.add(org);
			    	 }
			     }
			}
			
			organizationDao.save(list);
			 iLogger.log("OrganizationSyncService execute method execute end");
				return true;
	   }else{
		   iLogger.log("sap连接失败");
			return false;
	   }
	  
   }
	
}
