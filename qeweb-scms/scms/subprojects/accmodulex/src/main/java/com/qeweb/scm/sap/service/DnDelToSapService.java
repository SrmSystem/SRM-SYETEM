package com.qeweb.scm.sap.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.DnDelSAP;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
/**
 * DN号删除 SRM->SAP
 * SRM通知SAP，DN号已被删除
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class DnDelToSapService extends BaseService{
	@Autowired
	private CompanyDao companyDao;
	
	/**
	 * 删除DN号
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(List<DeliveryItemEntity> dlvItemList,StringBuffer msg,ILogger logger)throws Exception {
		boolean isSuccess=true; 
		dlvItemList= DnDelSAP.dnDelToSap(dlvItemList,logger);
		if(null==dlvItemList && dlvItemList.size()==0){
			return false;
		}
		for (DeliveryItemEntity dlvItem : dlvItemList) {
			if(PurchaseConstans.SYNC_SUCCESS.intValue()!=dlvItem.getSyncStatus().intValue()){
				isSuccess=false;//存在更新失败的发货单
				if(null==msg){
					msg=new StringBuffer();
				}
				msg.append("采购订单号：").append(dlvItem.getOrderItem().getOrCode()).append("、");
				msg.append("行号：").append(dlvItem.getItemNo()).append("、");
				msg.append("供应商编码：").append(dlvItem.getDelivery().getVendor().getCode()).append("、");
				msg.append("发货数量：").append(dlvItem.getDeliveryQty());
				msg.append("删除DN失败，原因：").append(dlvItem.getMessage());//错误信息
				msg.append("<br>");
			}
			
		}
		
		return isSuccess;
		
	}
	
	
}
