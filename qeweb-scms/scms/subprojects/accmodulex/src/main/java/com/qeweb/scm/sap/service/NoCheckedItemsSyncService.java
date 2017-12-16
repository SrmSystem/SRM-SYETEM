package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.NoCheckItemsSAP;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.repository.NoCheckItemsDao;
/**
 * 
 * @author haiming.huang
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class NoCheckedItemsSyncService extends BaseService{
	@Autowired
	private NoCheckItemsDao noCheckItemsDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	/**
	 * 同步sap采购结算,未对账明细
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("NoCheckedItemsSyncService execute method execute start");
	   List<NoCheckItemsEntity> res=NoCheckItemsSAP.sync(iLogger);
	   if(res!=null){
		    List<NoCheckItemsEntity> list =new ArrayList<NoCheckItemsEntity>();
		    
		    for(NoCheckItemsEntity entity:res){
		    	iLogger.log("BuyerCode()-->:"+entity.getBuyerCode());
		    	Long buyerId = getBuyerOrVendor(entity.getBuyerCode());
		    	entity.setBuyerId(buyerId);
		    	iLogger.log("VendorCode()-->:"+entity.getVendorCode());
		    	Long vendorId = getBuyerOrVendor(entity.getVendorCode());
		    	entity.setVendorId(vendorId);
		    	iLogger.log("MaterialCode()-->:"+entity.getMaterialCode());
		    	MaterialEntity material = materialDao.getMaterials(entity.getMaterialCode());
		    	if(material!=null){
		    		entity.setMaterialId(material.getId());
		    	}
		    	entity.setState("0");
		    	if(buyerId!=null&&vendorId!=null&&material!=null){
		    		list.add(entity);
		    	}
		    }
			//iLogger.log("总条数:"+res.size());
			noCheckItemsDao.save(list);
			iLogger.log("NoCheckedItemsSyncService execute method execute end");
			return true;
	   }else{
		    iLogger.log("SAP连接失败");
			return false;
	   }
	 
   }

	private Long getBuyerOrVendor(String buyerCode) {
		OrganizationEntity entity	= organizationDao.getBuyerOrVendor(buyerCode);
		return entity==null?null:entity.getId();
	}
	
}
