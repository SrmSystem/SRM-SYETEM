package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.PurchasePlanningBoardSAP;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanningBoardEntity;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanningBoardDao;
/**
 * 
 * @author haiming.huang
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchasePlanningBoardSyncService extends BaseService{
	@Autowired
	private PurchasePlanningBoardDao purchasePlanningBoardDao;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	/**
	 * 同步采购计划看板
	 * @param iLogger
	 * @param factoryCode 
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger, Map<String ,Object> param)throws Exception {
	   iLogger.log("PurchasePlanningBoardSyncService execute method execute start");
	   List<PurchasePlanningBoardEntity> res=PurchasePlanningBoardSAP.sync(iLogger,param);
	   if(res!=null){
		   
		   purchasePlanningBoardDao.updateState();
		   
		    List<PurchasePlanningBoardEntity> list =new ArrayList<PurchasePlanningBoardEntity>();
		    PurchasingGroupEntity en = null;
		    for(PurchasePlanningBoardEntity entity:res){
		    	en = purchasingGroupDao.getId(entity.getGroupCode());
		    	entity.setGroupId(en.getId());
		    }
			iLogger.log("总条数:"+res.size());
			purchasePlanningBoardDao.save(list);
			iLogger.log("PurchasePlanningBoardSyncService execute method execute end");
			return true;
	   }else{
		    iLogger.log("SAP连接失败");
			return false;
	   }
	 
   }

}
