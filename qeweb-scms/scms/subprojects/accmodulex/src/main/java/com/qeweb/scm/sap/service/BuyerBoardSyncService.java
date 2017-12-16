package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.BuyerBoardSAP;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.purchasemodule.entity.BuyerBoardEntity;
import com.qeweb.scm.purchasemodule.repository.BuyerBoardDao;
/**
 * 
 * @author haiming.huang
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class BuyerBoardSyncService extends BaseService{
	
	@Autowired
	private BuyerBoardDao buyerBoardDao;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	
	/**
	 * 同步采购员看板
	 * @param iLogger
	 * @param factoryCode 
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger, Map<String ,Object> param)throws Exception {
	   iLogger.log("BuyerBoardSyncService execute method execute start");
	   List<BuyerBoardEntity> res=BuyerBoardSAP.sync(iLogger,param);
	   if(res!=null){
		   
		   buyerBoardDao.updateState();
		   
		    List<BuyerBoardEntity> list =new ArrayList<BuyerBoardEntity>();
		    
		    PurchasingGroupEntity en = null;
		    for(BuyerBoardEntity entity:res){
		    	en = purchasingGroupDao.getId(entity.getGroupCode());
		    	entity.setGroupId(en.getId());
		    }
			iLogger.log("总条数:"+res.size());
			buyerBoardDao.save(list);
			iLogger.log("BuyerBoardSyncService execute method execute end");
			return true;
	   }else{
		    iLogger.log("SAP连接失败");
			return false;
	   }
	 
   }

}
