package com.qeweb.scm.purchasemodule.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;



@Service
@Transactional
public class PurchaseOrderItemService {

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private UserDao userDao;

	
	/**
	 * 根据同步状态查询所有订单及明细
	 * @param string
	 * @return
	 */
	public List<PurchaseOrderItemEntity> getAllOrderItemsNullOrNotSync() {
		return purchaseOrderItemDao.findByCol1IsNullOrCol1Not();
	}


	/**
	 * 更新状态，置为已同步
	 * @param orderItemList
	 */
	public void updateOrderItemsIsSync(
			List<PurchaseOrderItemEntity> orderItemList) {
		for (int i = 0; i < orderItemList.size(); i++) {
			orderItemList.get(i).setCol1("true");
		}
		purchaseOrderItemDao.save(orderItemList);
	}
	
}
