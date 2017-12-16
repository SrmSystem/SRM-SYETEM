package com.qeweb.scm.purchasemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.vo.ReceiveTransfer;

@Service
@Transactional(rollbackOn=Exception.class)  
public class ReceiveService extends BaseService {
	 
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;

	@Autowired
	private DeliveryDao deliveryDao;
	
	@Autowired
	private DeliveryItemDao deliveryItemDao;
	
	@Autowired
	private ReceiveDao receiveDao;
	
	@Autowired
	private ReceiveItemDao receiveItemDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 获取收货单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<ReceiveEntity> getReceives(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ReceiveEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ReceiveEntity.class);
		return receiveDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取收货单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<ReceiveEntity> getAllReceives(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ReceiveEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ReceiveEntity.class);
		return receiveDao.findAll(spec);
	}
	
	public ReceiveEntity getReceiveById(Long id) {
		return receiveDao.findOne(id);
	}
	
	public ReceiveItemEntity getReceiveByItemId(Long itemId) {
		return receiveItemDao.findOne(itemId);
	}

	public List<ReceiveItemEntity> getReceiveItems(ReceiveEntity receiveEntity) {
		return receiveItemDao.findByReceive(receiveEntity);
	}
	
	public List<ReceiveItemEntity> getReceiveItems(PurchaseOrderItemEntity orderItem){
		return receiveItemDao.findByOrderItem(orderItem);
	}
	
	/**
	 * 获取收货单明细
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<ReceiveItemEntity> getReceiveItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ReceiveItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ReceiveItemEntity.class);
		return receiveItemDao.findAll(spec,pagin);
	}

	/**
	 * 根据发货单批量收货
	 * @param deliveryList
	 * @param userEntity
	 */
	public void doReceives(List<DeliveryEntity> deliveryList, UserEntity userEntity) {
		Set<DeliveryItemEntity> deliveryItem = null;
		DeliveryEntity delivery = null;
		ReceiveEntity receive = null;
		ReceiveItemEntity receiveItem = null;
		Set<Long> orderItemPlanIdSet = new HashSet<Long>();
		Set<Long> orderItemIdSet = new HashSet<Long>();
		Set<Long> orderIdSet = new HashSet<Long>(); 
		for (DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
			deliveryItem = delivery.getDeliveryItem();
			delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			receive = new ReceiveEntity();
			receive.setReceiveCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.RECEIVE));
			receive.setBuyer(delivery.getBuyer());
			receive.setVendor(delivery.getVendor());
			receive.setReceiveOrg(delivery.getReceiveOrg());
			receive.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			receive.setReceiveUser(userEntity);
			receive.setReceiveTime(DateUtil.getCurrentTimestamp());
			receiveDao.save(receive);

			// 明细
			if (CollectionUtils.isEmpty(deliveryItem))
				continue;

			for (DeliveryItemEntity ditem : deliveryItem) {
				receiveItem = new ReceiveItemEntity();
				receiveItem.setReceive(receive);
				receiveItem.setDeliveryItem(ditem);
				receiveItem.setOrderItem(ditem.getOrderItem());
				receiveItem.setOrderItemPlan(ditem.getOrderItemPlan());
				receiveItem.setReceiveQty(ditem.getDeliveryQty());
				receiveItem.setInStoreQty(ditem.getDeliveryQty());
				receiveItem.setReturnQty(0d);
				receiveItemDao.save(receiveItem);
				orderItemPlanIdSet.add(ditem.getOrderItemPlan().getId());
				orderItemIdSet.add(ditem.getOrderItem().getId());
				orderIdSet.add(ditem.getOrderItem().getOrder().getId());
				ditem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES); 
				deliveryItemDao.save(ditem);
			}
			deliveryDao.save(delivery);
		}
		updateReceiveStatus(orderItemPlanIdSet, orderItemIdSet, orderIdSet);
	}
	
	/**
	 * Single收货
	 * @param deliveryId 发货单ID
	 * @param receive 收货主单
	 * @param receiveItem 收货明细
	 */
	public void saveReceive(long deliveryId, ReceiveEntity receive, List<ReceiveItemEntity> receiveItem, UserEntity user) {
		DeliveryEntity delivery = deliveryDao.findOne(deliveryId);
		if (delivery == null)
			return;
		
		receive.setBuyer(delivery.getBuyer());
		receive.setVendor(delivery.getVendor());
		receive.setReceiveOrg(delivery.getReceiveOrg());
		receive.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
		receive.setReceiveUser(user);
		receive.setReceiveTime(DateUtil.getCurrentTimestamp());
		receiveDao.save(receive);

		DeliveryItemEntity deliveryItem = null;
		Set<Long> orderItemPlanIdSet = new HashSet<Long>();
		Set<Long> orderItemIdSet = new HashSet<Long>();
		Set<Long> orderIdSet = new HashSet<Long>();
		boolean haspart = false;
		for (ReceiveItemEntity item : receiveItem) {
			deliveryItem = deliveryItemDao.findOne(item.getDeliveryItemId());
			item.setDeliveryItem(deliveryItem);
			item.setOrderItem(deliveryItem.getOrderItem());
			item.setOrderItemPlan(deliveryItem.getOrderItemPlan());
			receiveItemDao.save(item);
			if(deliveryItem.getDeliveryQty() > item.getReceiveQty()) {
				haspart = true;
			}
			orderItemPlanIdSet.add(deliveryItem.getOrderItemPlan().getId());
			orderItemIdSet.add(deliveryItem.getOrderItem().getId());
			orderIdSet.add(deliveryItem.getOrderItem().getOrder().getId());
			deliveryItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			deliveryItemDao.save(deliveryItem);
		}
		if(haspart)
			delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
		else
			delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
		deliveryDao.save(delivery);
		//更新收货状态
		updateReceiveStatus(orderItemPlanIdSet, orderItemIdSet, orderIdSet);
	}

	/**
	 * 更新订单收货状态
	 * @param orderItemPlanIdSet
	 * @param orderItemIdSet
	 * @param orderIdSet
	 */
	public void updateReceiveStatus(Set<Long> orderItemPlanIdSet, Set<Long> orderItemIdSet, Set<Long> orderIdSet) {
		//更新供货计划收货状态
		updateOrderItemPlanReceiveStatus(orderItemPlanIdSet);
		//更新订单明细收货状态
		updateOrderItemReceiveStatus(orderItemIdSet);
		//更新订单主表收货状态
		updateOrderReceiveStatus(orderIdSet);
	}

	/**
	 * 更新供货计划收货状态
	 * @param orderItemPlanIdSet
	 */
	private void updateOrderItemPlanReceiveStatus(Set<Long> orderItemPlanIdSet) {
		if(CollectionUtils.isEmpty(orderItemPlanIdSet))
			return;
		
		PurchaseOrderItemPlanEntity itemPlan = null;
		Double receiveQty = null;
		List<PurchaseOrderItemPlanEntity> itemPlanList = new ArrayList<PurchaseOrderItemPlanEntity>();
		for(Long itemPlanId : orderItemPlanIdSet) {
			itemPlan = purchaseOrderItemPlanDao.findOne(itemPlanId);
			//receiveQty =receiveItemDao.getOrderItemPlanReceiveQty(itemPlanId);
			receiveQty=itemPlan.getReceiveQty();
			if(itemPlan.getReceiveStatus()!=PurchaseConstans.RECEIVE_STATUS_YES){
				if(receiveQty != null && itemPlan.getOrderQty() <= receiveQty) {
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES); 
				}else if( receiveQty.equals(0D) || receiveQty==null){
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO); 
					itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				}
				else {
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART); 
					itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				}
			}else if(itemPlan.getDeliveryQty()>=itemPlan.getOrderQty()){
				itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}
			//itemPlan.setDeliveryQty(receiveQty);
			//itemPlan.setUndeliveryQty(BigDecimalUtil.sub(itemPlan.getDeliveryQty(), receiveQty));
			itemPlanList.add(itemPlan);
		}
		purchaseOrderItemPlanDao.save(itemPlanList);
	}

	/**
	 * 更新订单明细行收货状态
	 * @param orderItemIdSet
	 */
	private void updateOrderItemReceiveStatus(Set<Long> orderItemIdSet) {
		if(CollectionUtils.isEmpty(orderItemIdSet))
			return;
		
		PurchaseOrderItemEntity orderItem = null;
		Set<PurchaseOrderItemPlanEntity>  orderItemPlan = null;
		List<PurchaseOrderItemEntity> orderItemList = new ArrayList<PurchaseOrderItemEntity>();
		for(Long orderItemId : orderItemIdSet) {
			orderItem = purchaseOrderItemDao.findOne(orderItemId);
			orderItemPlan = orderItem.getOrderItemPlan();
			if(CollectionUtils.isEmpty(orderItemPlan))
				continue;
			
			boolean hasPart = false;
			for(PurchaseOrderItemPlanEntity itemPlan : orderItemPlan) {
				if(itemPlan.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()
						|| itemPlan.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_NO.intValue()) {
					hasPart = true;
					break;
				}
			}
			if(hasPart){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			}
			else{
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}
			/*if(orderItem.getReceiveQty()!=null && orderItem.getOrderQty() <= orderItem.getReceiveQty()){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}else if(orderItem.getReceiveQty()!=null&& orderItem.getReceiveQty()!=0 && orderItem.getReceiveQty() < orderItem.getOrderQty()){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			}*/
			orderItemList.add(orderItem);
		}
		purchaseOrderItemDao.save(orderItemList);
	}
	
	/**
	 * 更新订单收货状态
	 * @param orderIdSet
	 */
	private void updateOrderReceiveStatus(Set<Long> orderIdSet) {
		if(CollectionUtils.isEmpty(orderIdSet))
			return;
		
		PurchaseOrderEntity order = null;
		Set<PurchaseOrderItemEntity>  orderItem = null;
		List<PurchaseOrderEntity> orderList = new ArrayList<PurchaseOrderEntity>();
		for(Long orderId : orderIdSet) {
			order = purchaseOrderDao.findOne(orderId);
			orderItem = order.getOrderItem();
			
			if(CollectionUtils.isEmpty(orderItem))
				continue;
			
			boolean hasPart = false;
			for(PurchaseOrderItemEntity item : orderItem) {
				if(item.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()
						|| item.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_NO.intValue()) {
					hasPart = true;
					break;
				}
			}
			if(hasPart)
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			else
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			orderList.add(order);
		}
		purchaseOrderDao.save(orderList);
	}
	
	/**
	 * 更新发货单收货状态
	 * @param deliverItemMap
	 */
	private void updateDeliveryReceiveStatus(Map<String, DeliveryItemEntity> deliverItemMap) {
		if(CollectionUtils.isEmpty(deliverItemMap))
			return;
		
		Double receiveQty = null;
		Map<Long, DeliveryEntity> deliveryMap = new HashMap<Long, DeliveryEntity>();
		for(DeliveryItemEntity deliveryItem : deliverItemMap.values()) {
			if(!deliveryMap.containsKey(deliveryItem.getDelivery().getId())) {
				deliveryMap.put(deliveryItem.getDelivery().getId(), deliveryItem.getDelivery());
			}
			//获取发货单收货数量
			receiveQty = receiveItemDao.getDeliveryItemReceiveQty(deliveryItem.getId());
			if(receiveQty >= deliveryItem.getDeliveryQty()) {
				deliveryItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			} else {
				deliveryItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			} 
		}
		deliveryItemDao.save(deliverItemMap.values());
		//更新发货主单收货状态
		Set<DeliveryItemEntity> deliveryItemSet = null;
		boolean haspart = false;
		for(DeliveryEntity delivery : deliveryMap.values()) {
			deliveryItemSet = delivery.getDeliveryItem();
			if(CollectionUtils.isEmpty(deliveryItemSet))
				continue;
			
			for(DeliveryItemEntity item : deliveryItemSet) {
				if(item.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_NO.intValue() 
						|| item.getReceiveStatus() == PurchaseConstans.RECEIVE_STATUS_PART.intValue())
					haspart = true;
			}
			if(haspart) {
				delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			} else {
				delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}
			haspart = false;
		}
		deliveryDao.save(deliveryMap.values());
	}

	/**
	 * 保存收货单信息
	 * @param receiveTransList
	 * @param logger
	 * @return
	 */
	public boolean saveReceives(List<ReceiveTransfer> receiveTransList, ILogger logger) {
		Map<String, DeliveryItemEntity> deliverItemMap = new HashMap<String, DeliveryItemEntity>();
		Map<String, UserEntity> userMap = new HashMap<String, UserEntity>();
		List<ReceiveTransfer> list = validateTransfers(receiveTransList, deliverItemMap, userMap, logger);
		if(list.size() != receiveTransList.size())  
			return false;
		
		logger.log("\n->正在准备合并主数据与明细数据\n");
		ReceiveEntity receive = null;
		ReceiveItemEntity receiveItem = null;
		List<ReceiveItemEntity> receiveItemList = new ArrayList<ReceiveItemEntity>();
		DeliveryItemEntity deliveryItem = null;
		Map<String, ReceiveEntity> tmpMap = new HashMap<String, ReceiveEntity>();
		String key = null;
		Set<Long> orderItemPlanIdSet = new HashSet<Long>();
		Set<Long> orderItemIdSet = new HashSet<Long>();
		Set<Long> orderIdSet = new HashSet<Long>();
		for(ReceiveTransfer trans : list) {
			key = trans.getDeliveryCode() + ";" + trans.getMaterialCode() + ";" + trans.getItemNo();
			deliveryItem = deliverItemMap.get(key);
			if(!tmpMap.containsKey(trans.getReceiveCode())) {
				receive = getReceive(trans.getReceiveCode(), deliveryItem.getDelivery().getBuyer().getCode(), deliveryItem.getDelivery().getVendor().getCode());
				receive.setReceiveCode(trans.getReceiveCode());
				receive.setBuyer(deliveryItem.getDelivery().getBuyer());
				receive.setVendor(deliveryItem.getDelivery().getVendor()); 
				receive.setReceiveOrg(deliveryItem.getDelivery().getReceiveOrg());
				receive.setReceiveTime(DateUtil.stringToTimestamp(trans.getReceiveTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
				receive.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
				receive.setReceiveUser(StringUtils.isEmpty(trans.getReceiveUserCode()) ? null : userMap.get(trans.getReceiveUserCode())); 
				tmpMap.put(trans.getReceiveCode(), receive);
			} else {
				receive = tmpMap.get(trans.getReceiveCode());
			}
			receiveItem = getReceiveItem(trans.getReceiveCode(), deliveryItem);   
			receiveItem.setReceive(receive);
			receiveItem.setDeliveryItem(deliveryItem);
			receiveItem.setOrderItemPlan(deliveryItem.getOrderItemPlan());
			receiveItem.setOrderItem(deliveryItem.getOrderItem());
			receiveItem.setReceiveQty(StringUtils.convertToDouble(trans.getReceiveQty()));
			receiveItem.setInStoreQty(StringUtils.convertToDouble(trans.getInStoreQty()));
			receiveItem.setReturnQty(StringUtils.convertToDouble(trans.getReturnQty()));
			receiveItemList.add(receiveItem);
			orderItemPlanIdSet.add(deliveryItem.getOrderItemPlan().getId());
			orderItemIdSet.add(deliveryItem.getOrderItem().getId());
			orderIdSet.add(deliveryItem.getOrderItem().getOrder().getId());
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		//保存
		receiveDao.save(tmpMap.values());
		receiveItemDao.save(receiveItemList);   
		//更新发货单收状态
		updateDeliveryReceiveStatus(deliverItemMap);
		//更新订单收货状态
		updateReceiveStatus(orderItemPlanIdSet, orderItemIdSet, orderIdSet);
		return true;  
	}

	/**
	 * 验证数据有效新
	 * @param receiveTransList
	 * @param deliverItemMap  key : deliveryCode + materialCode + itemNo
	 * @param userMap
	 * @param logger
	 * @return
	 */
	private List<ReceiveTransfer> validateTransfers(List<ReceiveTransfer> receiveTransList, Map<String, DeliveryItemEntity> deliverItemMap,
			Map<String, UserEntity> userMap, ILogger logger) {
		String logMsg = "->现在对导入收货单信息进行预处理,共有[" + (receiveTransList == null ? 0 : receiveTransList.size()) + "]条数据";
		logger.log(logMsg);

		List<ReceiveTransfer> ret = new ArrayList<ReceiveTransfer>();
		List<DeliveryItemEntity> deliveryItem = null;
		UserEntity receiveUser = null;
		boolean lineValidat = true;
		int index = 2;
		String key = "";
		for (ReceiveTransfer trans : receiveTransList) {
			key = trans.getDeliveryCode() + ";" + trans.getMaterialCode() + ";" + trans.getItemNo();
			if(!deliverItemMap.containsKey(key)) {
				deliveryItem = deliveryItemDao.findDeliveryItemEntityByReceiveInfo(trans.getDeliveryCode(), trans.getMaterialCode(), StringUtils.convertToInteger(trans.getItemNo()));
				if(CollectionUtils.isEmpty(deliveryItem)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + (index) + "],发货单信息(" + key + "], 不存在,忽略此收货明细");
				} else {
					deliverItemMap.put(key, deliveryItem.get(0));
				}
			}
			if(!StringUtils.isEmpty(trans.getReceiveUserCode()) && !userMap.containsKey(trans.getReceiveUserCode())) {
				receiveUser = userDao.findByLoginName(trans.getReceiveUserCode());
				if(receiveUser == null) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],收货人员[" + trans.getReceiveUserCode() + "]未在系统中维护,忽略此收货明细");
				} else {
					userMap.put(trans.getReceiveUserCode(), receiveUser);
				}
			}
			if(lineValidat) {
				logMsg = "[SUCCESS]行索引[" + (index) + "],预处理[" + key + "|" + trans.getMaterialCode() + "|" + trans.getReceiveQty() + "|" + trans.getInStoreQty() + "|" + trans.getReturnQty() + "]成功。";
				logger.log(logMsg);
				ret.add(trans);
			}
			index ++;
			lineValidat = true;
		}
		logMsg = "<-导入的收货单主信息预处理完毕,共有[" + ret.size() + "]条有效数据";
		logger.log(logMsg);
		return ret;
	}
	
	private ReceiveEntity getReceive(String receiveCode, String buyerCode, String vendorCode) {
		ReceiveEntity receive = receiveDao.getReceiveEntityByCode(receiveCode, buyerCode, vendorCode);
		return receive == null ? new ReceiveEntity() : receive;
	}
	
	private ReceiveItemEntity getReceiveItem(String receiveCode, DeliveryItemEntity deliveryItem) {
		ReceiveItemEntity receiveItem = receiveItemDao.getReceiveItemEntityByCode(receiveCode, deliveryItem.getId());
		return receiveItem == null ?  new ReceiveItemEntity() : receiveItem;
	}

	public ReceiveEntity getReceiveByCode(String receiptNumber) {
		return receiveDao.getReceiveByCode(receiptNumber);
	}

	/**
	 * 同步erp收货单
	 * @param itemList
	 */
	public void saveErpReceives(List<ReceiveItemEntity> itemList) {
		for (ReceiveItemEntity item : itemList){
			ReceiveEntity entity = item.getReceive();
			receiveDao.save(entity);
			receiveItemDao.save(itemList);
		}
	}
	
	public void saveReceiveEntity(ReceiveEntity entity) {
		receiveDao.save(entity);
		
	}
	
	public ReceiveItemEntity getReceiveItemByCode(String receiveCode, long deliveryItmeId) {
		return receiveItemDao.getReceiveItemEntityByCode(receiveCode, deliveryItmeId);
	}

	public long getDeliveryItemIdByReceiveItemId(long id) {
		return receiveItemDao.getDeliveryItemIdByReceiveItemId(id);
	}
	
}
