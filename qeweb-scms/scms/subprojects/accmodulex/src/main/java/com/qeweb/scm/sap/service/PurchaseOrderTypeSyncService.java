package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.PurchaseOrderTypeSAP;
import com.qeweb.scm.basemodule.entity.DictEntity;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.DictDao;
import com.qeweb.scm.basemodule.repository.DictItemDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
/**
 * 采购订单类型 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchaseOrderTypeSyncService extends BaseService{
	@Autowired
	private DictItemDao dictItemDao;
	
	@Autowired
	private DictDao dictDao;
	/**
	 * 同步sap公司数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("PurchaseOrderTypeSyncService execute method execute start");
	   List<DictItemEntity> res=PurchaseOrderTypeSAP.sync(iLogger);
	   if(res!=null){
		    List<DictItemEntity> list =new ArrayList<DictItemEntity>();
			iLogger.log("总条数:"+res.size());
			
			//1、创建数据字典主表，采购订单类型
			DictEntity dict=dictDao.findByDictCodeAndAbolished("PURCHASE_ORDER_TYPE", BOHelper.UNABOLISHED_SINGEL);
			if(null==dict){
				dict=new DictEntity();
				dict.setDictCode("PURCHASE_ORDER_TYPE");
				dict.setDictName("采购订单类型");
				dict.setAbolished(BOHelper.UNABOLISHED_SINGEL);
				dict.setCreateTime(DateUtil.getCurrentTimestamp());
				dict.setLastUpdateTime(DateUtil.getCurrentTimestamp());
				dict.setIsOutData(BOHelper.OUT_DATA_YES);
				dictDao.save(dict);
			}
			for (DictItemEntity re : res) {
				DictItemEntity dictItem=dictItemDao.findByDictItemDictCodeAndCode("PURCHASE_ORDER_TYPE",re.getCode());
			     if(null==dictItem){
			    	 iLogger.log("新增"+re.getCode());
			    	 dictItem = new DictItemEntity();
			    	 dictItem.setCode(re.getCode());
			    	 dictItem.setName(re.getName());
			    	 dictItem.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    	 dictItem.setIsOutData(BOHelper.OUT_DATA_YES);
			    	 dictItem.setDict(dict);
			    	 list.add(dictItem);
			     }else{
			    	 if(!re.getName().equals(dictItem.getName())){
			    		 iLogger.log("修改"+re.getCode());
			    		 dictItem.setName(re.getName());
			    		 dictItem.setIsOutData(BOHelper.OUT_DATA_YES);
				    	 list.add(dictItem);
			    	 }
			     }
			}
			
			dictItemDao.save(list);
			 iLogger.log("PurchaseOrderTypeSyncService execute method execute end");
			 return true;
	   }else{
		     iLogger.log("SAP连接失败");
			 return false;
	   }
	   
   }
	
	
}
