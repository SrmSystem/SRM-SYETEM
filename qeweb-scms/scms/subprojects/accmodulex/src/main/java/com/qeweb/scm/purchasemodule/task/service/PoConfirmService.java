package com.qeweb.scm.purchasemodule.task.service;
import java.sql.Timestamp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.service.PurchaseMainOrderService;

@Service
@Transactional(rollbackOn=Exception.class)
public class PoConfirmService extends BaseService{
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	PurchaseMainOrderService purchaseMainOrderService;
	
	public boolean execute(ILogger iLogger) throws Exception {
		iLogger.log("method execute start");
		List<PurchaseOrderItemEntity>  orderItemList = purchaseOrderItemDao.findByConfirmStatusAndIsModifyAndabolished(0,1,0);
		for(PurchaseOrderItemEntity  orderItem  :  orderItemList){
			if(orderItem.getAbolished().equals(0) && orderItem.getConfirmStatus().equals(0) && orderItem.getIsModify().equals(1)  ){
				//時間比較
				Timestamp curr=DateUtil.getCurrentTimestamp();
				int hours=(int) (( curr.getTime() -orderItem.getModifyTime().getTime() )/3600000);
				if( hours > 4){
					//自动的确认订单
					purchaseMainOrderService.modifyOrderItemAndDN(orderItem,new UserEntity(1L),PurchaseConstans.CONFIRM_STATUS_YES);
				}
			}
		}
		iLogger.log("method execute end");
		return true;
	}

}
