package com.qeweb.scm.sap.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.OrganizationSAP;
import com.qeweb.sap.VendorSAP;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

/**
 * 供应商信息 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class VendorSyncService {
	@Autowired
	private VendorBaseInfoDao vendorBseInfoDao;
	
	/**
	 * 同步sap采购组织数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
/*	   iLogger.log("VendorSyncService execute method execute start");
	  List<VendorBaseInfoEntity> list= vendorBseInfoDao.findByCurrentVersionAndAbolished(StatusConstant.STATUS_YES, BOHelper.UNABOLISHED_SINGEL);
	  List<VendorBaseInfoEntity> data= VendorSAP.getVendorByCode(list);
	  iLogger.log("共有:"+data.size()+"条数据供应商名称改变，需要保存");	
	  vendorBseInfoDao.save(data);
	  iLogger.log("VendorSyncService execute method execute end");*/
	   return true;
   }
	
}
