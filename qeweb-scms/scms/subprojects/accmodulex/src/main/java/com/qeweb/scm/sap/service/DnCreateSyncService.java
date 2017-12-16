package com.qeweb.scm.sap.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.DnCreateSAP;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;

/**
 * DN号创建 SAP->SRM
 * SRM获取SAP的DN号
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class DnCreateSyncService extends BaseService{
	@Autowired
	private DeliveryItemDao dlvItemDao;
	
	@Autowired
	private PurchaseOrderItemDao orderItemDao;
	
	/**
	 * 创建DN号
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger,List<DeliveryItemEntity> dlvItemList,StringBuffer msg)throws Exception {
		boolean isSuccess=true;
		dlvItemList=DnCreateSAP.dnDelToSap(dlvItemList,iLogger);
		if(null==dlvItemList || dlvItemList.size()==0){
			return false;
		}
		
		for (DeliveryItemEntity dlvItem : dlvItemList) {
			if(null==dlvItem.getSyncStatus() || PurchaseConstans.SYNC_SUCCESS.intValue()!=dlvItem.getSyncStatus().intValue()){
				isSuccess=false;//存在创建失败的发货单
				if(null==msg){
					msg=new StringBuffer();
				}
				PurchaseOrderItemEntity orderItem = orderItemDao.findById(dlvItem.getOrderItem().getId());
				msg.append("采购订单号：").append(orderItem.getOrCode()).append("、");
				msg.append("行号：").append(dlvItem.getItemNo()).append("、");
				msg.append("供应商编码：").append(dlvItem.getDelivery().getVendor().getCode()).append("、");
				msg.append("发货数量：").append(dlvItem.getDeliveryQty());
				msg.append("创建DN失败，原因：").append(dlvItem.getMessage());//错误信息
				msg.append(",请点击重写失败数据");
				msg.append("<br>");
			}
		}
		
		return isSuccess;
		
	}
	
	
}
