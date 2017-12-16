package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.OrderReturnSAP;
import com.qeweb.sap.vo.OrderReturnVo;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;

/**
 * 订单退货SAP->SRM
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class OrderReturnSyncService {

	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private  PurchaseOrderDao purchaseOrderDao;
	

	
	public boolean execute(ILogger iLogger)throws Exception {
		   iLogger.log("OrderReturnSyncService execute method execute start");
		   List<OrderReturnVo> res=OrderReturnSAP.getReturnByOrderItem(DateUtil.dateToString(DateUtil.getCurrentTimestamp(), "yyyyMMdd"),iLogger);
		   List<PurchaseOrderItemEntity> saveList=new ArrayList<PurchaseOrderItemEntity>();
		   if(res!=null){
			   iLogger.log("总条数:"+res.size());
			   for (OrderReturnVo orderReturnVo : res) {
				   PurchaseOrderItemEntity orderItem=purchaseOrderItemDao.findPurchaseOrderItemByOrderCodeAndItemNo(orderReturnVo.getEBELN(),orderReturnVo.getEBELP());
				   if(orderItem==null){
						iLogger.log("订单明细在系统里不存在:"+orderReturnVo.getEBELN()+"-"+orderReturnVo.getEBELP());
						continue;
				   }
				   Double returnQty= orderItem.getReturnQty()==null?0d:orderItem.getReturnQty();//退货数量
				   Double surOrderQty=orderItem.getSurOrderQty()==null?0d:orderItem.getSurOrderQty();//未清数量
				   orderItem.setSurOrderQty(BigDecimalUtil.add(surOrderQty, BigDecimalUtil.sub(orderReturnVo.getMENGE(), returnQty)));
				   orderItem.setReceiveQty(orderReturnVo.getMENGE());
				   
				   orderItem.setBalanceTime(DateUtil.stringToTimestamp(orderReturnVo.getBUDAT()+" "+orderReturnVo.getCPUTM(), "yyyyMMdd HHmmss"));
				   saveList.add(orderItem);
			   }
		   }
		   purchaseOrderItemDao.save(saveList);
		   iLogger.log("OrderReturnSyncService execute method execute end");
		   return true;
	}
	
	
	
	

}
