package com.qeweb.scm.purchasemodule.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
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
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;


@Service
@Transactional(rollbackOn=Exception.class)  
public class DeliveryService extends BaseService {

	@Autowired
	private DeliveryDao deliveryDao;
	
	@Autowired
	private DeliveryItemDao deliveryItemDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;  
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;  
	
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;  
	
	@Autowired
	private OrganizationDao organizationDao;  
	
	/**
	 * 获取待发货列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<PurchaseOrderItemPlanEntity> getPendingDeliverys(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		Page<PurchaseOrderItemPlanEntity> page =  purchaseOrderItemPlanDao.findAll(spec, pagin);   
		List<PurchaseOrderItemPlanEntity> list = page.getContent();
		setPlanDefaultQty(list);
		return page;
	}

	public List<PurchaseOrderItemPlanEntity> getPendingDeliverys(List<Long> ids) {
		List<PurchaseOrderItemPlanEntity> list = purchaseOrderItemPlanDao.findByIdIn(ids); 
		setPlanDefaultQty(list);
		return list;
	}
	
	//设置数量
	protected void setPlanDefaultQty(List<PurchaseOrderItemPlanEntity> list) {
		for(PurchaseOrderItemPlanEntity plan: list) { 
			plan.setShouldQty(BigDecimalUtil.sub(plan.getOrderQty(), BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty())));  	//应发数量
		}
	}
	
	/**
	 * 获取发货单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<DeliveryEntity> getDeliverys(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DeliveryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DeliveryEntity.class);
		return deliveryDao.findAll(spec,pagin);
	}
	
	public DeliveryEntity getDeliveryById(Long id) {
		return deliveryDao.findOne(id);
	}
	
	public DeliveryEntity getDeliveryByCode(String code) {
		return deliveryDao.findDeliveryEntityByCode(code);
	}
	
	public DeliveryItemEntity getDeliveryItemById(Long itemId) {
		return deliveryItemDao.findOne(itemId);
	}
	
	/**
	 * 获取发货单明细
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<DeliveryItemEntity> getDeliveryItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DeliveryItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DeliveryItemEntity.class);
		Page<DeliveryItemEntity> page = deliveryItemDao.findAll(spec,pagin);
		for(DeliveryItemEntity item : page.getContent()) {
			item.setOrderCode(item.getOrderItem().getOrder().getOrderCode()); 
		}
		return page;
	}
	
	public List<DeliveryItemEntity> getDeliveryItems(DeliveryEntity deliveryEntity) {
		return deliveryItemDao.findByDelivery(deliveryEntity);
	}

	/**
	 * 保存发货单
	 * @param delivery
	 * @param deliveryItem
	 */
	public void saveDelivery(DeliveryEntity delivery, List<DeliveryItemEntity> deliveryItem, UserEntity userEntity, String type) throws Exception {
		PurchaseOrderItemPlanEntity itemPlan = null;
		DeliveryItemEntity item = null;
		Double deliveryQty = 0d;
		for(int i = 0; i < deliveryItem.size(); i ++) {
			item = deliveryItem.get(i);
			itemPlan = purchaseOrderItemPlanDao.findOne(item.getOrderItemPlanId());
			if(i == 0) {
				delivery.setBuyer(itemPlan.getOrder().getBuyer());
				delivery.setVendor(itemPlan.getOrder().getVendor());
				delivery.setAuditStatus(PurchaseConstans.AUDIT_NO);
				if(PurchaseConstans.COMMON_SAVE.equals(type)) {
					delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				} else if(PurchaseConstans.COMMON_SAVE_PUBLIC.equals(type)) {
					delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
					delivery.setDeliveyTime(DateUtil.getCurrentTimestamp());
					delivery.setDeliveryUser(userEntity);
				}
				delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				delivery.setReceiveOrg(itemPlan.getReceiveOrg()); 
			}
			item.setDelivery(delivery);
			item.setOrderItem(itemPlan.getOrderItem());
			item.setOrderItemPlan(itemPlan);
			item.setMaterial(itemPlan.getMaterial());
			item.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			//更新供货计划
			deliveryQty = BigDecimalUtil.add(item.getDeliveryQty(), item.getUnqualifiedQty() == null ? 0d : item.getUnqualifiedQty());
			if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO) {
				itemPlan.setToDeliveryQty(BigDecimalUtil.add(itemPlan.getToDeliveryQty(), deliveryQty));
			} else {
				itemPlan.setDeliveryQty(BigDecimalUtil.add(itemPlan.getDeliveryQty(), deliveryQty));
				itemPlan.setOnwayQty(BigDecimalUtil.add(itemPlan.getOnwayQty(), deliveryQty));  //在途数量
				itemPlan.setToDeliveryQty(0D);
			}
			itemPlan.setUndeliveryQty(BigDecimalUtil.sub(itemPlan.getUndeliveryQty(), deliveryQty));//未发货数量
			updateDeliveryStatus(itemPlan);
			purchaseOrderItemPlanDao.save(itemPlan);
			PurchaseOrderItemEntity orderItem = itemPlan.getOrderItem();
			purchaseOrderItemDao.save(orderItem);
		}
		deliveryDao.save(delivery);
		deliveryItemDao.save(deliveryItem);
	}

	/**
	 * 更新订单发货状态
	 * @param itemPlan
	 */
	protected void updateDeliveryStatus(PurchaseOrderItemPlanEntity itemPlan) { 
		PurchaseOrderItemEntity orderItem = itemPlan.getOrderItem();
		PurchaseOrderEntity order = itemPlan.getOrder();
		double deliveryQty = BigDecimalUtil.add(itemPlan.getDeliveryQty(), itemPlan.getToDeliveryQty());
		
		//国内、外协：未收货的可以取消发货单，此时发货状态为已发货，发货数量不为0
		//国内、外协：未收货的可以取消发货单，此时发货状态为未发货，发货数量为0
		//国外：未发货的可以取消发货，此时发货状态为未发货，发货数量为0
		/*if(deliveryQty == 0 || (itemPlan.getDeliveryStatus().equals(PurchaseConstans.DELIVERY_STATUS_YES) && deliveryQty != 0 )) {*/
		if(deliveryQty == 0) {	
			itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			updatePurchaeseDeliveryStatus(itemPlan, orderItem, order);
			//部分发货
		} else
		if(deliveryQty > 0 && deliveryQty < itemPlan.getOrderQty() && itemPlan.getUndeliveryQty()!=0) {
			itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART); 
			if(orderItem != null) {
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART); 
				purchaseOrderItemDao.save(orderItem);
			}
			
			if(order != null) {
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				purchaseOrderDao.save(order);
			}
		// 供货计划已发货
		} else if(deliveryQty >= itemPlan.getOrderQty() || itemPlan.getUndeliveryQty()==0){  
			if(itemPlan.getDeliveryQty()<itemPlan.getOrderQty()){
				itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}
			updatePurchaeseDeliveryStatus(itemPlan, orderItem, order);
		}
	}

	/**
	 * 更新订单明细及主信息发货状态
	 * @param itemPlan
	 * @param orderItem
	 * @param order
	 */
	protected void updatePurchaeseDeliveryStatus(PurchaseOrderItemPlanEntity itemPlan, PurchaseOrderItemEntity orderItem, PurchaseOrderEntity order) {
		purchaseOrderItemPlanDao.save(itemPlan);
		Set<PurchaseOrderItemPlanEntity> planSet=orderItem.getOrderItemPlan();
		TreeSet<PurchaseOrderItemPlanEntity> planTreeSet=new TreeSet<PurchaseOrderItemPlanEntity>(new Comparator<PurchaseOrderItemPlanEntity>(){
			@Override
			public int compare(PurchaseOrderItemPlanEntity o1,
					PurchaseOrderItemPlanEntity o2) {
				//根据发货状态降序排列
				int num=new Integer(o2.getDeliveryStatus()).compareTo(new Integer(o1.getDeliveryStatus()));
				if(num==0){
					return StringUtils.convertToString(o2.getId()).compareTo( StringUtils.convertToString(o1.getId()));
				}
				return num;
			}
			
		});
		planTreeSet.addAll(planSet);
		for (PurchaseOrderItemPlanEntity iplan : planTreeSet) {
			/*if(iplan.getId() == itemPlan.getId()) {
				continue;
			}*/
			if(iplan.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO.intValue() || 
					iplan.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				//purchaseOrderItemDao.save(orderItem);
				//purchaseOrderDao.save(order);
				//return;
			}else if(iplan.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()){
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
				//purchaseOrderItemDao.save(orderItem);
				//purchaseOrderDao.save(order);
				//return;
			}
		} 
		purchaseOrderItemDao.save(orderItem);
		purchaseOrderDao.save(order);
		//orderItem.setDeliveryStatus(itemPlan.getDeliveryStatus());
		//purchaseOrderItemDao.save(orderItem);
		
		for(PurchaseOrderItemEntity item : order.getOrderItem()) {
			if(item.getId() == orderItem.getId()) {
				continue;
			}
			
			if(item.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO.intValue() ||
					item.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				purchaseOrderDao.save(order);
				return;
			}
		}
		order.setDeliveryStatus(itemPlan.getDeliveryStatus());
		purchaseOrderDao.save(order);
	} 

	/**
	 * 发货
	 * @param orderList
	 * @param userEntity
	 */
	public void dodelivery(List<DeliveryEntity> deliveryList, UserEntity userEntity) {
		DeliveryEntity delivery = null;
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
			delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			delivery.setDeliveryUser(userEntity);
			delivery.setDeliveyTime(DateUtil.getCurrentTimestamp());
			List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDelivery(delivery);   
			for(DeliveryItemEntity item : deliveryItem) {    
				orderItemPlan = item.getOrderItemPlan();
				orderItemPlan.setToDeliveryQty(BigDecimalUtil.sub(orderItemPlan.getToDeliveryQty(),  item.getDeliveryQty()));
				orderItemPlan.setDeliveryQty(BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), item.getDeliveryQty()));
				orderItemPlan.setUndeliveryQty(BigDecimalUtil.sub(orderItemPlan.getUndeliveryQty(),  item.getDeliveryQty()));
				orderItemPlan.setOnwayQty(BigDecimalUtil.add(orderItemPlan.getOnwayQty(), item.getDeliveryQty()));
				//更新订单发货状态
				updateDeliveryStatus(orderItemPlan);
				purchaseOrderItemPlanDao.save(orderItemPlan);
				
				PurchaseOrderItemEntity itemEntity=orderItemPlan.getOrderItem();
				itemEntity.setOnwayQty(BigDecimalUtil.add(itemEntity.getOnwayQty(), item.getDeliveryQty()));
				itemEntity.setUndeliveryQty(BigDecimalUtil.sub(itemEntity.getUndeliveryQty(),  item.getDeliveryQty()));
				purchaseOrderItemDao.save(itemEntity);
			}
			deliveryDao.save(delivery);
		}
	}
	
	/**
	 * 取消发货单
	 * @param deliveryList
	 * @param userEntity
	 */
	public void cancelDelivery(List<DeliveryEntity> deliveryList, UserEntity userEntity) {
		DeliveryEntity delivery = null;
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		OrganizationEntity org = organizationDao.findOne(userEntity.getCompanyId());
		Double deliveryQty = 0d;
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
			List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDelivery(delivery);   
			for(DeliveryItemEntity item : deliveryItem) {   
				if(OrgType.ROLE_TYPE_VENDOR.equals(org.getRoleType()))
					item.setAbolished(Constant.DELETE_FLAG);
				
				orderItemPlan = item.getOrderItemPlan();
				Double _deliveryQty = BigDecimalUtil.add(item.getDeliveryQty(), item.getUnqualifiedQty() == null ? 0d : item.getUnqualifiedQty());	//发货单中的发货数量
				if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO || delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_SAVE) {
					Double delQty = BigDecimalUtil.sub(orderItemPlan.getDeliveryQty(), _deliveryQty);
					orderItemPlan.setDeliveryQty(delQty<0?0D:delQty);
					Double toDeliveryQty = BigDecimalUtil.sub(orderItemPlan.getToDeliveryQty(), _deliveryQty);	//已创建未发数量=原来的已创建未发数量-发货单中的发货数量
					orderItemPlan.setToDeliveryQty(toDeliveryQty>0?toDeliveryQty:0D);
					Double onwayQty = BigDecimalUtil.sub(orderItemPlan.getOnwayQty(), _deliveryQty);
					orderItemPlan.setOnwayQty(onwayQty<0?0D:onwayQty);
					Double undeliveryQty = BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty()));
					orderItemPlan.setUndeliveryQty(undeliveryQty>orderItemPlan.getOrderQty()?orderItemPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
					
				} else if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES) {
					if((OrgType.ROLE_TYPE_VENDOR.equals(org.getRoleType()) && PurchaseConstans.COMMON_NORMAL.equals(delivery.getStatus())) 
							|| (OrgType.ROLE_TYPE_BUYER.equals(org.getRoleType()))) {
						Double onwayQty=BigDecimalUtil.sub(orderItemPlan.getOnwayQty(), _deliveryQty);	//在途数量=原来的在途数量-发货单中的发货数量
						orderItemPlan.setOnwayQty(onwayQty>0?onwayQty:0D);
						Double delQty=BigDecimalUtil.sub(orderItemPlan.getDeliveryQty(), _deliveryQty);	//发货数量=原来的发货数量-发货单中的发货数量
						orderItemPlan.setDeliveryQty(delQty>0?delQty:0D);
						Double undeliveryQty=BigDecimalUtil.add(orderItemPlan.getUndeliveryQty(),_deliveryQty);	//未发数量=原来的未发数量+发货单中的发货数量
						orderItemPlan.setUndeliveryQty(undeliveryQty>orderItemPlan.getOrderQty()?orderItemPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
					}
				}
				//更新订单发货状态
				updateDeliveryStatus(orderItemPlan);
				purchaseOrderItemPlanDao.save(orderItemPlan);
				
				PurchaseOrderItemEntity itemEntity=orderItemPlan.getOrderItem();
				Double itemOnWayQty=BigDecimalUtil.sub(itemEntity.getOnwayQty(), _deliveryQty);
				itemEntity.setOnwayQty(itemOnWayQty>0?itemOnWayQty:0D);
				Double itemUndelQty=BigDecimalUtil.add(itemEntity.getUndeliveryQty(), _deliveryQty);
				itemEntity.setUndeliveryQty(itemUndelQty>itemEntity.getOrderQty()?itemEntity.getOrderQty():itemUndelQty>0?itemUndelQty:0D);
//				purchaseOrderItemDao.save(itemEntity);
				
				delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				if(OrgType.ROLE_TYPE_BUYER.equals(org.getRoleType())) {
					delivery.setStatus(PurchaseConstans.COMMON_INVALID); //采购商取消发货单
				} else {
					delivery.setAbolished(Constant.DELETE_FLAG);	//供应商删除发货单
				}
				deliveryItemDao.save(item);
				
				//add by zhangjiejun 2016.01.20 start
				//查询所有未删除且未失效的发货明细
				List<DeliveryItemEntity> deliveryItemList = deliveryItemDao.
						findByOrderItemIdAndDeliveryAbolishedAndDeliveryStatus(itemEntity.getId(), Constant.UNDELETE_FLAG, PurchaseConstans.COMMON_NORMAL);
				if(deliveryItemList != null && deliveryItemList.size() != 0){	//不为空,且有发货明细数据,则表示部分发货
					itemEntity.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				}else{															//为空，则表示待发货
					itemEntity.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				}
				//add by zhangjiejun 2016.01.20 start
				purchaseOrderItemDao.save(itemEntity);
			}
			deliveryDao.save(delivery);
		}  
	}

	public DeliveryItemEntity getDeliveryItemByCode(String deliveryCode, String mCode) {
		return deliveryDao.getDeliveryItemByCode(deliveryCode, mCode);
	}
	
	/**
	 * 根据订单号查询订单计划
	 */
	public List<PurchaseOrderItemPlanEntity> getOrderItemPlanByOrderCode(String orderCode){
		return purchaseOrderItemPlanDao.findByOrderCode(orderCode);
	}
	
	/**
	 * 获取订单详情计划
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderItemPlanEntity> getPurchaseOrderItemPlans(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		List<PurchaseOrderItemPlanEntity> itemPlanList=purchaseOrderItemPlanDao.findAll(spec);
		return itemPlanList;
	}

	public DeliveryEntity getDeliveryByOrderCode(String poNumber) {
		return deliveryDao.findDeliveryEntityByOrderCode(poNumber);
	}

	public void updateReceiveStatus(DeliveryEntity deliveryEntity) {
		deliveryDao.save(deliveryEntity);
	}

	public void updateDeliveryReceiveStatus(DeliveryItemEntity deliveryItem) {
		deliveryItemDao.save(deliveryItem);
	}
}
