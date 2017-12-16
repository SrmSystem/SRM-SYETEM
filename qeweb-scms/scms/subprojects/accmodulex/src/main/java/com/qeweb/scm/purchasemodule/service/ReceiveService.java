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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
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
import com.qeweb.scm.purchasemodule.entity.FileDescriptEntity;
import com.qeweb.scm.purchasemodule.entity.FileUpEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.FileUpDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.vo.ReceiveTransfer;
import com.qeweb.scm.purchasemodule.webservice.vo.ReceiveItemVo;

/**
 * 收货管理service
 */
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
	
	@Autowired
	private FileUpDao fileUpDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	/**
	 * 获取收货单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<ReceiveEntity> getReceives(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		Sort sort = new Sort(Direction.DESC, "receiveTime");
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
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
		return receiveItemDao.findById(itemId);
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
	@SuppressWarnings("null")
	public void doReceives(List<DeliveryEntity> deliveryList, UserEntity userEntity) {
		Set<DeliveryItemEntity> deliveryItem = null;
		DeliveryEntity delivery = null;
		ReceiveEntity receive = null;
		ReceiveItemEntity receiveItem = null;
		PurchaseOrderItemPlanEntity itemPlanEntiy=null;
		PurchaseOrderItemEntity  orderItemEntity=null;
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
			receive.setOrderType(delivery.getDeliveryType());
			receive.setDelivery(d);
			receiveDao.save(receive);

			// 明细
			if (CollectionUtils.isEmpty(deliveryItem))
				continue;
			//批量审核的收货数量与入库数量默认等于发货数量 
			for (DeliveryItemEntity ditem : deliveryItem) {
				receiveItem = new ReceiveItemEntity();
				receiveItem.setReceive(receive);
				receiveItem.setDeliveryItem(ditem);
				receiveItem.setOrderItem(ditem.getOrderItem());
				receiveItem.setOrderItemPlan(ditem.getOrderItemPlan());
				receiveItem.setReceiveQty(ditem.getDeliveryQty());
				receiveItem.setInStoreQty(ditem.getDeliveryQty());
				receiveItem.setReturnQty(0d);
				receiveItem.setItemNo(ditem.getItemNo());
				receiveItemDao.save(receiveItem);
				//供货计划收货数量变更
				itemPlanEntiy=ditem.getOrderItemPlan();
				itemPlanEntiy.setReceiveQty(BigDecimalUtil.add(null==itemPlanEntiy.getReceiveQty()?0d:itemPlanEntiy.getReceiveQty().doubleValue(), receiveItem.getReceiveQty().doubleValue()));
				//在途数量=已发货数量-已收货数量
				double onwayQty = BigDecimalUtil.sub(null==ditem.getDeliveryQty()?0d:ditem.getDeliveryQty().doubleValue(), receiveItem.getReceiveQty().doubleValue());
				itemPlanEntiy.setOnwayQty(new Double(onwayQty));
				//订单明细收货数量变更
				orderItemEntity=itemPlanEntiy.getOrderItem();
				orderItemEntity.setReceiveQty(BigDecimalUtil.add(null==orderItemEntity.getReceiveQty()?0d:orderItemEntity.getReceiveQty().doubleValue(), receiveItem.getReceiveQty().doubleValue()));
				
				orderItemPlanIdSet.add(ditem.getOrderItemPlan().getId());
				orderItemIdSet.add(ditem.getOrderItem().getId());
				orderIdSet.add(ditem.getOrderItem().getOrder().getId());
				ditem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES); 
				//设置收货数量等于发货数量
				ditem.setReceiveQty(ditem.getDeliveryQty());
				deliveryItemDao.save(ditem);
				purchaseOrderItemPlanDao.save(itemPlanEntiy);
				purchaseOrderItemDao.save(orderItemEntity);
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
		receive.setDelivery(delivery);
		receive.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
		receive.setReceiveUser(user);
		//receive.setReceiveTime(DateUtil.getCurrentTimestamp());
        receive.setOrderType(delivery.getDeliveryType());
        receive.setPurchasingGroupCode(delivery.getPurchasingGroupCode());//采购组编码
        receive.setPurchasingGroupId(delivery.getPurchasingGroupId());//采购组Id
		receiveDao.save(receive);

		DeliveryItemEntity deliveryItem = null;
		PurchaseOrderItemPlanEntity itemPlanEntiy=null;
		PurchaseOrderItemEntity  orderItemEntity=null;
		Set<Long> orderItemPlanIdSet = new HashSet<Long>();
		Set<Long> orderItemIdSet = new HashSet<Long>();
		Set<Long> orderIdSet = new HashSet<Long>();
		boolean haspart = false;
		for (ReceiveItemEntity item : receiveItem) {
			deliveryItem = deliveryItemDao.findOne(item.getDeliveryItemId());
			item.setDeliveryItem(deliveryItem);
			item.setOrderItem(deliveryItem.getOrderItem());
			itemPlanEntiy=deliveryItem.getOrderItemPlan();//供货关系
			item.setOrderItemPlan(itemPlanEntiy);
			item.setReceive(receive);
			receiveItemDao.save(item);
			if(deliveryItem.getDeliveryQty() > item.getReceiveQty()) {
				haspart = true;
			}
			//供货计划收货数量变更
			itemPlanEntiy.setReceiveQty(BigDecimalUtil.add(null==itemPlanEntiy.getReceiveQty()?0d:itemPlanEntiy.getReceiveQty().doubleValue(), item.getReceiveQty().doubleValue()));
			//在途数量=已发货数量-已收货数量
			double onwayQty = BigDecimalUtil.sub(null==deliveryItem.getDeliveryQty()?0d:deliveryItem.getDeliveryQty().doubleValue(), item.getReceiveQty().doubleValue());
			itemPlanEntiy.setOnwayQty(new Double(onwayQty));
			
			//订单名词收货数量变更
			orderItemEntity=itemPlanEntiy.getOrderItem();
			orderItemEntity.setReceiveQty(BigDecimalUtil.add(null==orderItemEntity.getReceiveQty()?0d:orderItemEntity.getReceiveQty().doubleValue(), item.getReceiveQty().doubleValue()));
			
			orderItemPlanIdSet.add(deliveryItem.getOrderItemPlan().getId());
			orderItemIdSet.add(deliveryItem.getOrderItem().getId());
			orderIdSet.add(deliveryItem.getOrderItem().getOrder().getId());
			deliveryItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			deliveryItem.setReceiveQty(item.getReceiveQty());
			deliveryItemDao.save(deliveryItem);
			purchaseOrderItemPlanDao.save(itemPlanEntiy);
			purchaseOrderItemDao.save(orderItemEntity);
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
			receiveQty=itemPlan.getReceiveQty();
			if(itemPlan.getReceiveStatus()!=PurchaseConstans.RECEIVE_STATUS_YES){
				if(receiveQty != null && itemPlan.getOrderQty() <= receiveQty) {
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES); 
				}else if( receiveQty.equals(0D) || receiveQty==null){
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO); 
				}else {
					itemPlan.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART); 
				}
			}else if(itemPlan.getDeliveryQty()>=itemPlan.getOrderQty()){
				itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}
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
		List<PurchaseOrderItemPlanEntity>  orderItemPlan = null;
		List<PurchaseOrderItemEntity> orderItemList = new ArrayList<PurchaseOrderItemEntity>();
		int count_all=0;
		int count_part=0;
		for(Long orderItemId : orderItemIdSet) {
			orderItem = purchaseOrderItemDao.findOne(orderItemId);
			orderItemPlan = purchaseOrderItemPlanDao.findItemPlanEntitysByItem(orderItem.getId());
			if(CollectionUtils.isEmpty(orderItemPlan))
				continue;
			
			count_all=0;
			count_part=0;
			for(PurchaseOrderItemPlanEntity itemPlan : orderItemPlan) {
				if(itemPlan.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_YES.intValue()) {
					count_all++;
				}else if(itemPlan.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()) {
					count_part++;
				}
			}
			if(count_all>0 && count_all==orderItemPlan.size()){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			}else{
				orderItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			}
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
			
			int count_all=0;
			int count_part=0;
			for(PurchaseOrderItemEntity item : orderItem) {
				if(item.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_YES.intValue()) {
					count_all++;
				}else if(item.getReceiveStatus().intValue() == PurchaseConstans.RECEIVE_STATUS_PART.intValue()) {
					count_part++;
				}
			}
			if(count_all>0 && count_all==orderItem.size()){
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_PART);
			}else{
				order.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			}
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
				receiveUser = userDao.findByLoginNameAndAbolished(trans.getReceiveUserCode(),StatusConstant.STATUS_NO);
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
	
	/**
	 * 上传绩效文件
	 * @param filesPath
	 * @param filesName
	 * @param checkdId
	 * @param receive
	 */
	public void sendReceiveFiles(String[] filesPath, String[] filesName, String[] checkdId,ReceiveEntity receive){
		FileUpEntity fileEntity = null;
		List<FileUpEntity> fileList = new ArrayList<FileUpEntity>();
		if (checkdId != null){
			for (int i = 0; i < checkdId.length; i++) {
				fileEntity =fileUpDao.findByDesriptIdAndReceiveId(Long.valueOf(checkdId[i]), receive.getId());
				if(fileEntity!=null){
					fileEntity.setFilePath(filesPath[i]);
					fileEntity.setFileName(filesName[i]);
				}else{
					fileEntity=new FileUpEntity();
					fileEntity.setDescriptEntity(new FileDescriptEntity(Long.valueOf(checkdId[i])));
					fileEntity.setFilePath(filesPath[i]);
					fileEntity.setFileName(filesName[i]);
					fileEntity.setReceive(receive);
				}
				fileList.add(fileEntity);
			}
			fileUpDao.save(fileList);
		}
	}

	public String saveReceiveItemVoList(List<ReceiveItemVo> voList) {
		// 用于判断是否需要保存
		int count = 0;
		DeliveryEntity delivery = deliveryDao.findDeliveryEntityByAsnCode(voList.get(0).getDeliverycode());
		if(delivery == null){
			return "发货单不存在";
		}
		PurchaseOrderEntity order = purchaseOrderDao.getOrderByCode(voList.get(0).getPocode());
		if(order == null){
			return "采购订单不存在";
		}
		ReceiveEntity receive = receiveDao.findByReceiveCode(voList.get(0).getReceivecode());
		if(receive != null){
			return "收货单已存在";
		}
		receive = new ReceiveEntity();
		receive.setReceiveCode(voList.get(0).getReceivecode());
		receive.setBuyer(delivery.getBuyer());
		receive.setVendor(delivery.getVendor());
		receive.setReceiveOrg(delivery.getReceiveOrg());
		receive.setReceiveStatus(StatusConstant.STATUS_YES);
		receive.setReceiveTime(voList.get(0).getReceivetime());
		receive.setAttr8(voList.get(0).getReceiver());
		receive.setAttr7(voList.get(0).getPocode()); // 采购订单号
		receive.setAbolished(StatusConstant.STATUS_NO);
		receive.setDelivery(delivery);
		
		List<PurchaseOrderItemEntity> orderItemList = new ArrayList<PurchaseOrderItemEntity>();
		List<DeliveryItemEntity> deliveryItemList = new ArrayList<DeliveryItemEntity>();
		List<ReceiveItemEntity> receiveItemList = new ArrayList<ReceiveItemEntity>();
		for (int i = 0; i < voList.size(); i++) {
			ReceiveItemVo vo = voList.get(i);
			DeliveryItemEntity deliveryItem = deliveryDao.getDeliveryItem(delivery.getId(), vo.getMaterialcode());
			if(deliveryItem == null){
				return "po号为："+vo.getPocode()+"，物料编码为："+vo.getMaterialcode()+"的发货明细不存在";
			}
			if (vo.getReceiveqty() < 0) {
				deliveryItem.setReceiveQty((double) 0); // 退货的时候收货数量为0
				deliveryItem.setReceiveStatus(3); // 退货
			} else {
				deliveryItem.setReceiveQty(vo.getReceiveqty());
				deliveryItem.setReceiveStatus(1); // 已收货
			}
			deliveryItemList.add(deliveryItem);
			
			PurchaseOrderItemEntity orderItem = deliveryItem.getOrderItem();
			if (orderItem != null) {
				orderItem.setReceiveStatus(StatusConstant.STATUS_YES);
				orderItemList.add(orderItem);
			}
			PurchaseOrderItemPlanEntity orderItemPlan = deliveryItem.getOrderItemPlan();
			ReceiveItemEntity itemEntity = new ReceiveItemEntity();
			itemEntity.setIsOutData(StatusConstant.STATUS_YES);
			itemEntity.setReceive(receive);
			itemEntity.setDeliveryItem(deliveryItem);
			itemEntity.setOrderItem(orderItem);
			itemEntity.setOrderItemPlan(orderItemPlan);
			if (vo.getReceiveqty() < 0) {
				itemEntity.setReceiveQty((double) 0);
				itemEntity.setReturnQty(Math.abs(vo.getReceiveqty()));
			} else {
				itemEntity.setReceiveQty(vo.getReceiveqty());
				itemEntity.setReturnQty((double) 0);
			}
			itemEntity.setAttr14(vo.getLinenumber());
			itemEntity.setAbolished(StatusConstant.STATUS_NO);
			receiveItemList.add(itemEntity);
			
			count++;
		}
		
		if(count == voList.size()){
			receiveDao.save(receive);
			receiveItemDao.save(receiveItemList);
			deliveryItemDao.save(deliveryItemList);
			purchaseOrderItemDao.save(orderItemList);
			// 同步发货单的收货状态
			delivery.setReceiveStatus(1);
			deliveryDao.save(delivery);
			order.setReceiveStatus(1);
			purchaseOrderDao.save(order);
		}
		return "写入成功";
	}
	
	/**
	 * 获取收货单明细(通过主订单的id)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<ReceiveItemEntity> getReceiveItemsByMainOrderId(Long mainOrderId) {
		//通过主订单的id获取下面的明细行
		List<PurchaseOrderItemEntity>   poItemList  =  purchaseOrderItemDao.findByOrderId(mainOrderId, 0);
		List<Long> ids = new ArrayList<Long>();	
		if(poItemList !=  null && poItemList.size() != 0){
			for(PurchaseOrderItemEntity poItem : poItemList ){
				ids.add(poItem.getId());
			}
		}
		List<ReceiveItemEntity>  reItemList= receiveItemDao.findByorderItemIdInAndAbolished(ids,0);
	    return reItemList;
	}
	
	
	
	//通过订单行的id获取aac来料不良数量
	
	public Double getOrderItemZllblQty(Long orderItemId){
		return receiveItemDao.getOrderItemZllblQty(orderItemId);
	}
	
	//通过订单行的id获取aac质检不良数量
	public Double getOrderItemZzjblQty(Long orderItemId){
		return receiveItemDao.getOrderItemZzjblQty(orderItemId);
	}
	
	
	/**
	 * 处理page数据
	 * @param page
	 * @return
	 */
	public Page<ReceiveEntity> initData(Page<ReceiveEntity> page){
		for (ReceiveEntity receiveEntity : page) {
			if(receiveEntity.getPurchasingGroupId() != null){
				PurchasingGroupEntity group = purchasingGroupDao.findById(receiveEntity.getPurchasingGroupId());
				if(group!= null){
					receiveEntity.setPurchasingGroupName(group.getName());
				}
			}
		}
		return page;
	}
	
}

