package com.qeweb.scm.purchasemodule.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryDTEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryPCEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryDTDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.DeliveryPCDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseGoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;
import com.qeweb.scm.purchasemodule.web.vo.ReplishmentVO;
import com.qeweb.scm.sap.service.BOHelper;
import com.qeweb.scm.sap.service.DnCreateSyncService;
import com.qeweb.scm.sap.service.DnDelToSapService;
import com.qeweb.scm.sap.service.DnUpdateSyncService;

/**
 * 发货管理service
 */
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
	
	@Autowired
	private DeliveryPCDao deliveryPCDao;  
	
	@Autowired
	private DeliveryDTDao deliveryDTDao;  
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private DnCreateSyncService dnCreateSyncSer;
	
	@Autowired
	private DnUpdateSyncService dnUpdateSyncSer;
	
	@Autowired
	private DnDelToSapService dnDelSyncSer;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	@Autowired
	private PurchaseGoodsRequestDao purchaseGoodsRequestDao;
	
	@Autowired
	private ReceiveItemDao recItemDao;
	
	@Autowired
	private ReceiveDao recDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	@Autowired
	private MaterialService materialService;
	
	private ILogger logger = new FileLogger();
	/**
	 * 获取待发货列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<PurchaseOrderItemPlanEntity> getPendingDeliverys(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, null);
		String pendingCommonSql =pendingCommonSql(searchParamMap);
		String sql1="SELECT ITEM_PLAN.ID "+pendingCommonSql;
		List<?> idList = generialDao.querybysql(sql1,pagin);
		String countSql ="SELECT COUNT(ITEM_PLAN.ID)"+pendingCommonSql;
		int totalCount=generialDao.findCountBySql(countSql);
		List<PurchaseOrderItemPlanEntity> list = dealQueryMap(idList);
		if(null==list){
			list = new ArrayList<PurchaseOrderItemPlanEntity>();
		}
		setPlanDefaultQty(list);
		Page<PurchaseOrderItemPlanEntity> page = new PageImpl<PurchaseOrderItemPlanEntity>(list,pagin,totalCount);
		return page;
	}
	
	/**
	 * 处理Map
	 * @param idList
	 * @return
	 */
	public List<PurchaseOrderItemPlanEntity> dealQueryMap(List<?> idList){
		List<Long> ids = null;
		if(null!=idList && idList.size()>0){
			ids = new ArrayList<Long>();
		}
		for(Object o:idList){
			Map<String,Object> m =  (Map<String, Object>) o;
			ids.add(((BigDecimal)m.get("ID")).longValue());
		}
		
		if(null!=ids && ids.size()>0){
			return purchaseOrderItemPlanDao.findByIdInOrderByReqTime(ids);
		}
		return null;
	}
	
	/**
	 * 公用待发货看板sql
	 * @param searchParamMap
	 * @return
	 */
	public String pendingCommonSql(Map<String, Object> searchParamMap){
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" QEWEB_PURCHASE_ORDER_ITEM_PLAN ITEM_PLAN");
		sql.append(" LEFT JOIN QEWEB_PURCHASE_ORDER_ITEM ORDER_ITEM");
		sql.append(" ON ITEM_PLAN.ORDER_ITEM_ID = ORDER_ITEM.ID");
		sql.append(" LEFT JOIN QEWEB_PURCHASE_ORDER PUR_ORDER");
		sql.append(" ON ORDER_ITEM.ORDER_ID = PUR_ORDER.ID");
		sql.append(" LEFT JOIN QEWEB_ORGANIZATION ORG");
		sql.append(" ON ORG.ID = PUR_ORDER.VENDOR_ID");
		sql.append(" LEFT JOIN QEWEB_MATERIAL MAT");
		sql.append(" ON MAT.ID = ITEM_PLAN.MATERIAL_ID");
		sql.append(" WHERE ITEM_PLAN.PUBLISH_STATUS=1");
		sql.append(" AND ITEM_PLAN.CONFIRM_STATUS=1");
		sql.append(" AND ORDER_ITEM.CLOSE_STATUS=0");
		sql.append(" AND ITEM_PLAN.ABOLISHED=0");
		sql.append(" AND (ITEM_PLAN.ORDER_QTY-ITEM_PLAN.DELIVERY_QTY-TO_DELIVERY_QTY)>0");//有可发数量
		
		if(searchParamMap.containsKey("EQ_order.vendor.id") && !StringUtils.isEmpty(searchParamMap.get("EQ_order.vendor.id").toString())){
			sql.append(" AND PUR_ORDER.VENDOR_ID=").append(searchParamMap.get("EQ_order.vendor.id").toString());
		}
		
		if(searchParamMap.containsKey("LIKE_order.orderCode") && !StringUtils.isEmpty(searchParamMap.get("LIKE_order.orderCode").toString())){
		sql.append(" AND PUR_ORDER.ORDER_CODE LIKE '%"+searchParamMap.get("LIKE_order.orderCode").toString()+"%'");
		}
		if(searchParamMap.containsKey("GTE_requestTime") && !StringUtils.isEmpty(searchParamMap.get("GTE_requestTime").toString())){
		sql.append(" AND ITEM_PLAN.REQUEST_TIME>=TO_DATE('"+searchParamMap.get("GTE_requestTime").toString()+"', 'yyyy-MM-dd')");
		}
		if(searchParamMap.containsKey("LTE_requestTime") && !StringUtils.isEmpty(searchParamMap.get("LTE_requestTime").toString())){
		sql.append(" AND ITEM_PLAN.REQUEST_TIME<=TO_DATE('"+searchParamMap.get("LTE_requestTime").toString()+"', 'yyyy-MM-dd')");
		}
		if(searchParamMap.containsKey("LIKE_material.code") && !StringUtils.isEmpty(searchParamMap.get("LIKE_material.code").toString())){
		sql.append(" AND MAT.CODE LIKE '%"+searchParamMap.get("LIKE_material.code").toString()+"%'");
		}
		if(searchParamMap.containsKey("LIKE_material.name") && !StringUtils.isEmpty(searchParamMap.get("LIKE_material.name").toString())){
		sql.append(" AND MAT.NAME LIKE '%"+searchParamMap.get("LIKE_material.name").toString()+"%'");
		}
		if(searchParamMap.containsKey("IN_order.purchasingGroup.id") && !StringUtils.isEmpty(searchParamMap.get("IN_order.purchasingGroup.id").toString())){
			List<Long> groupIds=(List<Long>) searchParamMap.get("IN_order.purchasingGroup.id");
			if(null!=groupIds && groupIds.size()>0){
				StringBuffer str = new StringBuffer();
				for (Long id : groupIds) {
					str.append(id).append(",");
				}
				if(str.length()>0){
					sql.append(" AND PUR_ORDER.PURCHASING_GROUP_ID IN ("+str.subSequence(0, str.length()-1).toString()+")");
				}else{
					sql.append(" AND PUR_ORDER.PURCHASING_GROUP_ID IN (-1)");
				}
			}
		}
		sql.append(" ORDER BY ITEM_PLAN.REQUEST_TIME ASC");
		return sql.toString();
	}
	

	public List<PurchaseOrderItemPlanEntity> getPendingDeliverys(List<Long> ids) {
		List<PurchaseOrderItemPlanEntity> list = purchaseOrderItemPlanDao.findByIdIn(ids); 
		setPlanDefaultQty(list);
		return list;
	}
	
	//设置数量
	protected void setPlanDefaultQty(List<PurchaseOrderItemPlanEntity> list) {
		if(null!=list && list.size()>0){
			for(PurchaseOrderItemPlanEntity plan: list) { 
				PurchaseOrderItemEntity orderItem = plan.getOrderItem();
				plan.setShouldQty(BigDecimalUtil.sub(plan.getOrderQty(), BigDecimalUtil.add(plan.getDeliveryQty(), plan.getToDeliveryQty())));  	//应发数量
				plan.setBuyerCode(plan.getOrder().getBuyer().getCode());
				plan.setBuyerName(plan.getOrder().getBuyer().getName());
				plan.setVendorCode(plan.getOrder().getVendor().getCode());
				plan.setVendorName(plan.getOrder().getVendor().getName());
				plan.setOrderType(plan.getOrder().getOrderType());
				plan.setZlock(orderItem.getZlock());//锁定状态
				plan.setBstae(orderItem.getBstae());//内向交货标识
				plan.setLockStatus(orderItem.getLockStatus());	//冻结状态
				plan.setLoekz(orderItem.getLoekz());		//删除状态
				plan.setElikz(orderItem.getElikz());		//交付状态
				
				//标识采购订单行有删除，冻结，锁定，交货已完成的添加红色标识
				if(orderItem != null){
					if(null!=orderItem.getZlock() && "X".equals(orderItem.getZlock())){
						plan.setIsRed("1");
					}else if(null!=orderItem.getLockStatus() && "1".equals(orderItem.getLockStatus())){
						plan.setIsRed("1");
					}else if(null!=orderItem.getLoekz() && "X".equals(orderItem.getLoekz())){
						plan.setIsRed("1");
					}else if(null!=orderItem.getElikz() && "X".equals(orderItem.getElikz())){
						plan.setIsRed("1");
					}else if(!validatePendingOrderQty(orderItem,plan)){
						plan.setIsRed("1");
					}
					
				}
			}
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
		return deliveryDao.findById(id);
	}
	
	public DeliveryEntity getDeliveryByCode(String code) {
		return deliveryDao.findDeliveryEntityByCode(code);
	}
	
	public DeliveryItemEntity getDeliveryItemById(Long itemId) {
		return deliveryItemDao.findById(itemId);
	}
	
	public PurchaseOrderItemPlanEntity getPurchaseOrderItemPlanById(Long id) {
		return purchaseOrderItemPlanDao.findById(id);
	}
	
	public PurchaseOrderItemEntity getPurchaseOrderItemById(Long id) {
		return purchaseOrderItemDao.findById(id);
	}
	
	public PurchaseOrderEntity getPurchaseOrderById(Long id) {
		return purchaseOrderDao.findById(id);
	}
	
	
	//通过供货计划的id获取发货单的数据
	public List<DeliveryItemEntity> findDeliveryItemEntitysByOrderItemPlanId(Long id) {
		return deliveryItemDao.findDeliveryItemEntitysByOrderItemPlanId(id);
	}
	
	/**
	 * 获取发货单明细
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<DeliveryItemEntity> getDeliveryItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "dnCreateStatus","asc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DeliveryItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DeliveryItemEntity.class);
		Page<DeliveryItemEntity> page = deliveryItemDao.findAll(spec,pagin);
		for(DeliveryItemEntity item : page.getContent()) {
			PurchaseOrderItemPlanEntity plan = getPurchaseOrderItemPlanById(item.getOrderItemPlan().getId());
			PurchaseOrderItemEntity orderItem = getPurchaseOrderItemById(item.getOrderItem().getId());
			
			item.setOrderCode(item.getOrderItem().getOrder().getOrderCode()); 
			item.setUnitName(item.getOrderItem().getUnitName());
			//默认收货数=发货数量
			item.setToreceiveQty(item.getDeliveryQty());
			item.setToreturnQty(0d);
			//要求到货时间  格式为：yyyy-MM-dd
			item.setRequestTime(item.getRequest().getRq());
	
			item.setOrderCode(orderItem.getOrder().getOrderCode());
			item.setOrderQty(plan.getOrderQty());
			item.setCurrency(plan.getCurrency());
			
			item.setZlock(orderItem.getZlock());	//锁定标识
			item.setLockStatus(orderItem.getLockStatus());	//冻结标识
			item.setLoekz(orderItem.getLoekz());	//删除标识
			item.setElikz(orderItem.getElikz());	//交付标识
			item.setBstae(orderItem.getBstae());  //内向交货标识
			//验证当前发货数量是否已经大于订单数量 ---modfiy by chao.gu 20170821
			boolean flag=validateDlvInOrderQty(item,plan,orderItem,null,false,plan.getShipType());
			if(flag){
				item.setIsQtyModify(PurchaseConstans.EXCEPTION_STATUS_NO+"");
			}else{
				item.setIsQtyModify(PurchaseConstans.EXCEPTION_STATUS_YES+"");
			}
		}
		return page;
	}
	
	public List<DeliveryItemEntity> getDeliveryItems(DeliveryEntity deliveryEntity) {
		return deliveryItemDao.findByDeliveryAndAbolished(deliveryEntity, 0);
	}
	/**
	 * 验证修改发货单【供】
	 * @param type
	 * @param msg
	 * @param datas
	 * @return
	 */
	public boolean validateEdit(String type,StringBuffer msg,String datas){
		DeliveryItemEntity deliveryItemEntity = null;
		PurchaseOrderItemPlanEntity itemPlan = null;
		//验证发货数量是否大于订单数量
		boolean isNew = true;
		if("edit".equals(type)){
			isNew=false;
		}
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			deliveryItemEntity =getDeliveryItemById(StringUtils.convertToLong(object.get("id") + ""));
			itemPlan =deliveryItemEntity.getOrderItemPlan();
			 boolean flag;
			 if(isNew){
				 flag=validateOrderQty(deliveryItemEntity,itemPlan,itemPlan.getOrderItem(),msg,isNew,itemPlan.getShipType());
			 }else{
				 Double deliveryQty = StringUtils.convertToDouble(object.get("sendQty") + "");
				 flag=validateHasDlvOrderQty(deliveryItemEntity,itemPlan,itemPlan.getOrderItem(),msg,deliveryQty,itemPlan.getShipType());
			 }
			 if(!flag){
					return flag;
			 }
		}
		return true;
	}
	
	/**
	 * 保存发货单
	 * @param delivery
	 * @param deliveryItem
	 */
	public boolean saveDelivery(DeliveryEntity delivery, List<DeliveryItemEntity> deliveryItem, UserEntity userEntity, String type,StringBuffer msg) throws Exception {
		PurchaseOrderItemPlanEntity itemPlan = null;
		DeliveryItemEntity item = null;
		Map<Long,Double> requestMap = new HashMap<Long, Double>();//key为要返回要货计划的planId,value为返还数量
	    /////验证发货数量是否大于订单数量
		boolean isNew=true;
		if("edit".equals(type)){
			isNew=false;
		}
		if(isNew){//新建验证
			if(null!=deliveryItem && deliveryItem.size()>0){
				for (DeliveryItemEntity deliveryItemEntity : deliveryItem) {
					itemPlan = getPurchaseOrderItemPlanById(deliveryItemEntity.getOrderItemPlanId());
					 boolean flag=validateOrderQty(deliveryItemEntity,itemPlan,itemPlan.getOrderItem(),msg,isNew,itemPlan.getShipType());
						 if(!flag){
								return flag;
						 }
				}
			}
	}
		//////
		
		
		for(int i = 0; i < deliveryItem.size(); i ++) {
			item = deliveryItem.get(i);
			itemPlan = getPurchaseOrderItemPlanById(item.getOrderItemPlanId());
					if(i == 0) {
						delivery.setBuyer(itemPlan.getOrder().getBuyer());
						delivery.setVendor(itemPlan.getOrder().getVendor());
						delivery.setDeliveryType(itemPlan.getOrder().getOrderType());//订单类型
						delivery.setAuditStatus(PurchaseConstans.AUDIT_NO);//未审核
						delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
						delivery.setDeliveryUser(userEntity);
						delivery.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
						delivery.setReceiveOrg(itemPlan.getReceiveOrg()); 
						delivery.setFactory(itemPlan.getOrderItem().getFactoryEntity());
						delivery.setPurchasingGroupCode(itemPlan.getOrder().getPurchasingGroup().getCode());//采购组编码
						delivery.setPurchasingGroupId(itemPlan.getOrder().getPurchasingGroup().getId());//采购组Id
					}
					item.setDelivery(delivery);
					item.setItemNo(itemPlan.getItemNo());
					item.setOrderItem(itemPlan.getOrderItem());
					item.setOrderItemPlan(itemPlan);
					item.setRequest(itemPlan.getPurchaseGoodsRequest());
					item.setMaterial(itemPlan.getMaterial());
					item.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
					
					//--更新包装数量start
					materialService.updateVendorMaterialPackage(delivery.getVendor().getId(), item.getMaterial().getId(), delivery.getFactory().getId(), item.getStandardBoxNum(), item.getMinPackageQty());
					//--更新包装数量end
					
					//若是补货则直接加入DN号 start
					if(PurchaseConstans.SHIP_TYPE_REPL == itemPlan.getShipType()){
						ReceiveItemEntity recItem = recItemDao.findById(itemPlan.getRecItemId());
						item.setDn(recItem.getDeliveryItem().getDn());
					}
					//补货加DN号 end
					
					//新增不变
					if(isNew){
						//供货计划：默认加上已创建未发数量，发布时候减去
						//保存未发数量=+发货数量
						itemPlan.setToDeliveryQty(BigDecimalUtil.add(itemPlan.getToDeliveryQty().doubleValue(), item.getDeliveryQty()));
						//未发数量=-发货
						itemPlan.setUndeliveryQty(BigDecimalUtil.sub(itemPlan.getUndeliveryQty()==null?0d:itemPlan.getUndeliveryQty().doubleValue(), item.getDeliveryQty()));//未发货数量
						if(itemPlan.getUndeliveryQty()<0d){
							itemPlan.setUndeliveryQty(0d);
						}
						updateDeliveryStatus(itemPlan);
					}else{
						//修改,需要判断订单数量减小的情况
						updateDlvItem(item, PurchaseConstans.COMMON_NORMAL,requestMap,itemPlan.getShipType());
					}
					if(i == 0) {
					deliveryDao.save(delivery);
					}
					if(isNew){
						deliveryItemDao.save(item);
					}
					
		}
		
		//若订单数量减小，释放要货计划(该供货计划当前可能各种状态，有可能已经删除)
		if(null!=requestMap && requestMap.keySet().size()>0){
			for (Long key : requestMap.keySet()) {
				PurchaseOrderItemPlanEntity orderPlan =purchaseOrderItemPlanDao.findById(key);
				writeQtyByPlan(orderPlan,(Double)requestMap.get(key));
			}
		}
		
/*		if(isNew){
		//生成消息 预警 预警提醒（采购方）
		 List<Long> userIdList = new ArrayList<Long>();
		 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(delivery.getPurchasingGroupId());
		 for(GroupConFigRelEntity rel : userOrderList ){
			 userIdList.add(rel.getUserId());
		 }
	     warnMainService.warnMessageSet(delivery.getId(), WarnConstant.ASN_UN_APPROAL, userIdList);
		}*/
		
		return true;
	}

	private void createPCAndDTCode(DeliveryItemEntity item) {
		int boxQty = StringUtils.convertToInt(item.getCol3()); //包裝数  
		int deliveryQty = item.getDeliveryQty().intValue();  //发货数量   
		int pcQty = (deliveryQty+boxQty-1)/boxQty;   //批次数量   
		List<DeliveryPCEntity> pcList = new ArrayList<DeliveryPCEntity>();
		List<DeliveryDTEntity> dtList = new ArrayList<DeliveryDTEntity>();
		if(pcQty == 1){
			DeliveryPCEntity pcEntity = new DeliveryPCEntity();
			pcEntity.setAbolished(0);
			pcEntity.setDeliveryItem(item);
			String asnCode = serialNumberService.geneterNextPCNumberByKey(OddNumbersConstant.DELIVERY_PC).substring(4);
			pcEntity.setAsnCode(asnCode);
			pcEntity.setBoxCount(item.getDeliveryQty());
			pcEntity.setPrintStatus(0);
			pcList.add(pcEntity);
		}else{
			for (int i = 0; i < pcQty; i++) {
				DeliveryPCEntity pcEntity = new DeliveryPCEntity();
				pcEntity.setAbolished(0);
				pcEntity.setDeliveryItem(item);
				String asnCode = serialNumberService.geneterNextPCNumberByKey(OddNumbersConstant.DELIVERY_PC).substring(4);
				pcEntity.setAsnCode(asnCode);
				double count = 0;
				if(i < pcQty-1){
					count = boxQty;
				}else{
					count = deliveryQty - i*boxQty;
				}
				pcEntity.setBoxCount(count);
				pcEntity.setPrintStatus(0);
				pcList.add(pcEntity);
			}
		}
		//关键件物料需要生成单体标签
		if("X".equals(item.getMaterial().getCol3())){
			for (int i = 0; i < pcList.size(); i++) {
				double count = pcList.get(i).getBoxCount();
				for (int j = 0; j < count; j++) {
					DeliveryDTEntity dtEntity = new DeliveryDTEntity();
					dtEntity.setAbolished(0);
					dtEntity.setPcEntity(pcList.get(i));
					String asnCode = getCodePrefix() + serialNumberService.geneterNextNumberByKey(OddNumbersConstant.DELIVERY_DT).substring(10);
					dtEntity.setAsnCode(asnCode);
					dtEntity.setPrintStatus(0);
					dtList.add(dtEntity);
				}
			}
		}
		deliveryPCDao.save(pcList);
		deliveryDTDao.save(dtList);
	}
	

	/**
	 * 更新订单发货状态
	 * @param itemPlan
	 */
	protected void updateDeliveryStatus(PurchaseOrderItemPlanEntity itemPlan) { 
		PurchaseOrderItemEntity orderItem = itemPlan.getOrderItem();
		PurchaseOrderEntity order = itemPlan.getOrder();
		double deliveryQty = BigDecimalUtil.add(itemPlan.getDeliveryQty(), itemPlan.getToDeliveryQty());
		
		if(deliveryQty == 0) {	
			itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			//部分发货
		} else if(deliveryQty > 0 && deliveryQty < itemPlan.getOrderQty()) {
			itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART); 
		// 供货计划已发货
		} else if(deliveryQty >= itemPlan.getOrderQty()){  
			itemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
		}
		BOHelper.setBOPublicFields_update(itemPlan);
		purchaseOrderItemPlanDao.save(itemPlan);
		//正常的单子更新订单明细状态
		if(PurchaseConstans.SHIP_TYPE_NORMAL == itemPlan.getShipType()){
			updatePurchaeseDeliveryStatus(orderItem, order);
		}else if(PurchaseConstans.SHIP_TYPE_REPL == itemPlan.getShipType()){
			//补货的单子更新收货明细
			updateReceiveItemDeliveryStatus(itemPlan);
		}
		
		//更新在途与未发数量，增加补货 
		updateSourcePlanQty(itemPlan);
	}

	/**
	 * 更新订单明细及主信息发货状态
	 * @param itemPlan
	 * @param orderItem
	 * @param order
	 */
	protected void updatePurchaeseDeliveryStatus(PurchaseOrderItemEntity orderItem, PurchaseOrderEntity order) {
		
		List<PurchaseOrderItemPlanEntity> planSet =purchaseOrderItemPlanDao.findItemPlanEntitysByItem(orderItem.getId());
		////1、更新明细表发货状态
		int count_all=0;
		int count_part=0;
		if(CommonUtil.isNotNullCollection(planSet)){
			for (PurchaseOrderItemPlanEntity planEntity : planSet) {
				if(planEntity.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
					count_part++;
				}else if(planEntity.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()){
					count_all++;
				}
			}
			
			if(count_all>0 && count_all==planSet.size()){
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			}
			BOHelper.setBOPublicFields_update(orderItem);
			purchaseOrderItemDao.save(orderItem);
			
		}
		
    ////2、更新主表发货状态 
		Set<PurchaseOrderItemEntity> itemSet=order.getOrderItem();
		if(CommonUtil.isNotNullCollection(itemSet)){
			 count_all=0;
			 count_part=0;
			for(PurchaseOrderItemEntity item : itemSet) {
				
				if(StringUtils.isEmpty(item.getRetpo())){
					if(item.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
						count_part++;
					}else if(item.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()){
						count_all++;
					}
				}else{
					count_all++;
					count_part++;
				}
			}
			if(count_all>0 && count_all==itemSet.size()){
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}else if(count_all>0 || count_part>0){
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				order.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			}
			BOHelper.setBOPublicFields_update(order);
			purchaseOrderDao.save(order);
		}
	} 

	/**
	 * 补货，更新收货明细和收货主表
	 * @param itemPlan
	 */
	public void updateReceiveItemDeliveryStatus(PurchaseOrderItemPlanEntity itemPlan){
	        ReceiveItemEntity recItem = recItemDao.findById(itemPlan.getRecItemId());
	        List<PurchaseOrderItemPlanEntity> planList =  purchaseOrderItemPlanDao.findByReceiveItemId(itemPlan.getRecItemId());
	       ////1、更新订单明细表发货状态
			int count_all=0;
			int count_part=0;
			if(CommonUtil.isNotNullCollection(planList)){
				for (PurchaseOrderItemPlanEntity planEntity : planList) {
					if(planEntity.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
						count_part++;
					}else if(planEntity.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()){
						count_all++;
					}
				}
				
				if(count_all>0 && count_all==planList.size()){
					recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
				}else if(count_all>0 || count_part>0){
					recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				}else{
					recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				}
				BOHelper.setBOPublicFields_update(recItem);
				recItemDao.save(recItem);
				
			}
			
			
			 ////2、更新发货单主表发货状态 
			ReceiveEntity receive = recItem.getReceive();
			Set<ReceiveItemEntity> itemSet=receive.getReceiveItem();
			if(CommonUtil.isNotNullCollection(itemSet)){
				 count_all=0;
				 count_part=0;
				for(ReceiveItemEntity item : itemSet) {
						if(item.getReplDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_PART.intValue()) {
							count_part++;
						}else if(item.getReplDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES.intValue()){
							count_all++;
						}
				}
				if(count_all>0 && count_all==itemSet.size()){
					receive.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
				}else if(count_all>0 || count_part>0){
					receive.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				}else{
					receive.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				}
				BOHelper.setBOPublicFields_update(receive);
				recDao.save(receive);
			}
	}
	
	/**
	 * 发货记录-发货【供】
	 * @param orderList
	 * @param userEntity
	 * @throws Exception 
	 */
	public Boolean dodelivery(List<DeliveryEntity> deliveryList, UserEntity userEntity,StringBuffer msg) throws Exception {
		DeliveryEntity delivery = null;
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		List<DeliveryItemEntity> itemListCreate=new ArrayList<DeliveryItemEntity>();
		List <DeliveryItemEntity> allDlvItems;
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
		
			delivery.setDeliveryUser(userEntity);
			delivery.setDeliveyTime(DateUtil.getCurrentTimestamp());
			List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDeliveryAndAbolished(delivery,0);   
			/////验证发货数量是否大于订单数量
			if(null!=deliveryItem && deliveryItem.size()>0){
				for (DeliveryItemEntity deliveryItemEntity : deliveryItem) {
					PurchaseOrderItemPlanEntity orderPlan=deliveryItemEntity.getOrderItemPlan();
					PurchaseOrderItemEntity orderItem =purchaseOrderItemDao.findById( deliveryItemEntity.getOrderItem().getId());
					
					boolean flag=validateHasDlvOrderQty(deliveryItemEntity,orderPlan,orderItem,msg,deliveryItemEntity.getDeliveryQty(),orderPlan.getShipType());
					//
					if(orderItem != null){
						if(null!=orderItem.getZlock() && "X".equals(orderItem.getZlock())){
							msg.append("存在已锁定的数据无法进行发货!");
							flag=false;
						}else if(null!=orderItem.getLockStatus() && "1".equals(orderItem.getLockStatus())){
							msg.append("存在已冻结的数据无法进行发货！");
							flag=false;
						}else if(null!=orderItem.getLoekz() && "X".equals(orderItem.getLoekz())){
							msg.append("存在已删除的数据无法进行发货！");
							flag=false;
						}else if(null!=orderItem.getElikz() && "X".equals(orderItem.getElikz())){
							msg.append("存在已交付的数据无法进行发货！");
							flag=false;
						}else if(null!=orderItem.getBstae() && "X".equals(orderItem.getBstae())){
							msg.append("该订单不是内向交货，无法发货！");
							flag=false;
						}
						
					}
					
					if(!flag){
						return flag;
					}
					
					if(StringUtils.isEmpty(deliveryItemEntity.getDn())){
						itemListCreate.add(deliveryItemEntity);
					}
				}
			}else{
				msg.append("无发货明细，无法进行发货！");
				return false;
			}
			//////
			
		}
		Boolean isSuccess=true;
		if(null!=itemListCreate && itemListCreate.size()>0){
			isSuccess=dnCreateSyncSer.execute(logger, itemListCreate, msg);
			///////将已创建的DN号保存，失败的更新失败状态,以便行项目单独调用DN接口
					for(DeliveryItemEntity item : itemListCreate) {  
						if(PurchaseConstans.SYNC_SUCCESS.intValue() == item.getSyncStatus().intValue()){
							item.setDn(item.getSapDn());
							item.setPosnr(item.getSapPosnr());
							item.setDnCreateStatus(PurchaseConstans.SYNC_SUCCESS);
							item.setDnErrorMessage("");
						}else{
							item.setDnCreateStatus(PurchaseConstans.SYNC_FAIL);
							item.setDnErrorMessage(item.getMessage());
						}
						deliveryItemDao.save(item);
					}
					///////
		}
		if(!isSuccess){
			return isSuccess;
		}
		
		//重新插下明细
		allDlvItems =  deliveryItemDao.findByDeliveryAndAbolished(delivery,0);
		for(DeliveryItemEntity item : allDlvItems) {  
			deliveryItemDao.save(item);
			orderItemPlan = item.getOrderItemPlan();
			// 已创建未发数量
			orderItemPlan.setToDeliveryQty(BigDecimalUtil.sub(orderItemPlan.getToDeliveryQty(),  item.getDeliveryQty()));
			orderItemPlan.setDeliveryQty(BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), item.getDeliveryQty()));
			orderItemPlan.setOnwayQty(BigDecimalUtil.add(orderItemPlan.getOnwayQty()==null?0d:orderItemPlan.getOnwayQty(), item.getDeliveryQty()));
			orderItemPlan.setErrorStatus(item.getErrorStatus());	//异常状态
			orderItemPlan.setChangeUserId(item.getChangeUserId());	//导致异常的人的Id
			orderItemPlan.setChangeTime(item.getChangeTime());		//导致异常的时间
			//更新订单发货状态
			updateDeliveryStatus(orderItemPlan);
		}
		BOHelper.setBOPublicFields_update(delivery);
		delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
		deliveryDao.save(delivery);
		return isSuccess;
	}
	
	
	/**
	 * 删除发货单明细
	 * @param deliveryList
	 * @param userEntity
	 * @throws Exception 
	 */
	public boolean singleSyncCreateDn(DeliveryItemEntity deliveryItem,StringBuffer msg) throws Exception {
		boolean isSuccess=true;
		//创建DN号 start
		List<DeliveryItemEntity> itemListCreate=new ArrayList<DeliveryItemEntity>();
		if(StringUtils.isEmpty(deliveryItem.getDn())){
			itemListCreate.add(deliveryItem);
		}

		if(null!=itemListCreate && itemListCreate.size()>0){
		isSuccess=dnCreateSyncSer.execute(logger, itemListCreate, msg);
	    ///////将已创建的DN号保存，失败的更新失败状态,以便行项目单独调用DN接口
			for(DeliveryItemEntity item : itemListCreate) {  
				if(PurchaseConstans.SYNC_SUCCESS.intValue() == item.getSyncStatus().intValue()){
							item.setDn(item.getSapDn());
							item.setPosnr(item.getSapPosnr());
							item.setDnCreateStatus(PurchaseConstans.SYNC_SUCCESS);
							item.setDnErrorMessage("");
				}else{
							item.setDnCreateStatus(PurchaseConstans.SYNC_FAIL);
							item.setDnErrorMessage(item.getMessage());
				}
						deliveryItemDao.save(item);
			}
					///////
		}
		//创建DN号  end
		
		return isSuccess;
	}
	
	/**
	 * 验证该发货单明细是否都已经创建DN[供]
	 * @param delivery
	 * @return
	 */
	public boolean validateAllDnCreated(DeliveryEntity delivery){
		Set<DeliveryItemEntity> dlvItemSet = delivery.getDeliveryItem();
		if(null!=dlvItemSet && dlvItemSet.size()>0){
			for (DeliveryItemEntity deliveryItemEntity : dlvItemSet) {
				if(StringUtils.isEmpty(deliveryItemEntity.getDn())){
					return false;
				}
			}
		}
		return true;
	}
	
	

	/**
	 * 验证待发货看板的订单数量是否有减小【供】
	 * @param shouldQty
	 * @param orderItem
	 * @return
	 */
	public boolean validatePendingOrderQty(PurchaseOrderItemEntity orderItem,PurchaseOrderItemPlanEntity orderPlan){
		if(PurchaseConstans.SHIP_TYPE_NORMAL==orderPlan.getShipType()){
			//1、判断当前需要回到发货看板上面的值
			Double planOrderQty = purchaseOrderItemPlanDao.getOrderItemOrderQty(orderItem.getId());
			//差异数量=ASN数量比订单数量多
			Double subQty =BigDecimalUtil.sub(null==planOrderQty?0:planOrderQty, orderItem.getOrderQty());
			Double downQty=0D;
			if(subQty>0){
				//按时间升序
				List<PurchaseOrderItemPlanEntity> planList = purchaseOrderItemPlanDao.findItemPlanEntitysByItemOrderByRequest(orderItem.getId());
				for (PurchaseOrderItemPlanEntity orderItemPlan : planList) {
					Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
					if(subQty<=0){//需要处理的量处理完了，结束
						return true;
					}
					//需要改小的数量与发货数量比较
	    			Double d=BigDecimalUtil.sub(subQty,shouldQty);
	    			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔发货明细，当前发货明细删除
	    				downQty=shouldQty;
	    			}else{//需要改小的数量小，当前发货单即可
	    				downQty=subQty;
	    			}
	    			subQty=BigDecimalUtil.sub(subQty,downQty);
	    			if(orderPlan.getId()==orderItemPlan.getId()){
	    				//当前的供货计划就是被扣减的，则直接标红
	    				return false;
	    			}
				}
			}
		
		}else if(PurchaseConstans.SHIP_TYPE_REPL==orderPlan.getShipType()){
			   ReceiveItemEntity recItem =recItemDao.findById(orderPlan.getRecItemId());
				//判断当前供货计划的orderQty是否大于收货的（实际已交货量-待检数量-送检合格)+已收货数量
				//剩余补货数=实际已交货量-待检数量-送检合格
				Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
		        ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
				Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(orderPlan.getId());
				hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
				Double shouldOrderQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
				if(orderPlan.getOrderQty()>shouldOrderQty){
					return false;
				}
				///////
		}
		return true;
		}
	
	/**
	 * 验证当前发货明细处于所有发货明细位置，由此看是否需要标红[审核，发货列表，发货单 详情]
	 * @param deliveryItem
	 * @param orderItem
	 * @param msg
	 * @return
	 */
	public boolean validateDlvInOrderQty(DeliveryItemEntity deliveryItem,PurchaseOrderItemPlanEntity itemPlan,PurchaseOrderItemEntity orderItem ,StringBuffer msg,boolean isNew,int shipType){
		
		if(!isNew){
			//1、判断是否已发货，已发货不受红色标识影响
			if(PurchaseConstans.DELIVERY_STATUS_YES.intValue() == deliveryItem.getDelivery().getDeliveryStatus().intValue()){
				if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){
					return true;
				}
			}
		}
		//2、判断订单数量是否减小
		boolean flag=validateOrderQty(deliveryItem,itemPlan,orderItem,msg,isNew,shipType);
		if(flag){
			//订单数量无异常，未减小
			return flag;//true
		}else{
			if (PurchaseConstans.SHIP_TYPE_NORMAL == shipType) {// 正常
					/////////////////////////先处理应发数量/////////////////////////
					//1、判断当前需要回到发货看板上面的值
					Double planOrderQty = purchaseOrderItemPlanDao.getOrderItemOrderQty(orderItem.getId());
					//差异数量=ASN数量比订单数量多
					Double subQty =BigDecimalUtil.sub(null==planOrderQty?0:planOrderQty, orderItem.getOrderQty());
					Double downQty=0D;
					if(subQty>0){
						//按时间升序
						List<PurchaseOrderItemPlanEntity> planList = purchaseOrderItemPlanDao.findItemPlanEntitysByItemOrderByRequest(orderItem.getId());
						for (PurchaseOrderItemPlanEntity orderItemPlan : planList) {
							Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
							if(subQty<=0){//需要处理的量处理完了，结束
								return true;
							}
							//需要改小的数量与发货数量比较
			    			Double d=BigDecimalUtil.sub(subQty,shouldQty);
			    			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔发货明细，当前发货明细删除
			    				downQty=shouldQty;
			    			}else{//需要改小的数量小，当前发货单即可
			    				downQty=subQty;
			    			}
			    			subQty=BigDecimalUtil.sub(subQty,downQty);
						}
					}
					
					////////////////////////////////////////////////////
					
					///////////////////////////////////////扣减数量不够再扣减发货单///////////////////////////
					// 已创建ASN数量
					List<DeliveryItemEntity> dlvItemList= deliveryItemDao.findNoDlvItemByOrderItemIdOrderByAuditStatusAndRq(orderItem.getId());
				    if(subQty>0){
				    	if(null!=dlvItemList && dlvItemList.size()>0){
				    		for (DeliveryItemEntity deliveryItemEntity : dlvItemList) {
				    			if(subQty<=0){//需要处理的量处理完了，结束
									return true;
								}
				    			
				    			//需要改小的数量与发货数量比较
				    			Double d=BigDecimalUtil.sub(subQty, deliveryItemEntity.getDeliveryQty());
				    			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔发货明细，当前发货明细删除
				    				downQty=deliveryItemEntity.getDeliveryQty();
				    			}else{//需要改小的数量小，当前发货单即可
				    				downQty=subQty;
				    			}
				    			
				    			subQty=BigDecimalUtil.sub(subQty,downQty);
				    			if(deliveryItem.getId()==deliveryItemEntity.getId()){
				    				//当前的发货单就是被扣减的，则直接标红
				    				return false;
				    			}
							}
				    	}
				    }
				    /////////////////////////////////////////////////////////////////
			}else if(PurchaseConstans.SHIP_TYPE_REPL == shipType) {// 补货
					/////////////////////////先处理应发数量/////////////////////////
				    PurchaseOrderItemPlanEntity replPlan=deliveryItem.getOrderItemPlan();
				    ReceiveItemEntity  recItem =recItemDao.findById(replPlan.getRecItemId());
					//差异数量=ASN数量比订单数量多
					   //剩余补货数=实际已交货量-待检数量-送检合格
				    Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
		            ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
				    List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
					Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanList.get(0).getId());
					hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
					Double replQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
					///////
					Double subQty =BigDecimalUtil.sub(null==replPlan.getOrderQty()?0:replPlan.getOrderQty(),replQty);
					Double downQty=0D;
					if(subQty>0){
						//按时间升序
						List<PurchaseOrderItemPlanEntity> planList = purchaseOrderItemPlanDao.findItemPlanEntitysByRecItemOrderByRequest(recItem.getId());
						for (PurchaseOrderItemPlanEntity orderItemPlan : planList) {
							Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
							if(subQty<=0){//需要处理的量处理完了，结束
								return true;
							}
							//需要改小的数量与发货数量比较
			    			Double d=BigDecimalUtil.sub(subQty,shouldQty);
			    			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔发货明细，当前发货明细删除
			    				downQty=shouldQty;
			    			}else{//需要改小的数量小，当前发货单即可
			    				downQty=subQty;
			    			}
			    			subQty=BigDecimalUtil.sub(subQty,downQty);
						}
					}
			
					////////////////////////////////////////////////////
					///////////////////////////////////////扣减数量不够再扣减发货单///////////////////////////
						// 已创建ASN数量
						List<DeliveryItemEntity> dlvItemList= deliveryItemDao.findNoRecDlvItemByPlanIdOrderByAuditStatusAndRq(replPlan.getId());
					  if(subQty>0){
					  	if(null!=dlvItemList && dlvItemList.size()>0){
					  		for (DeliveryItemEntity deliveryItemEntity : dlvItemList) {
					  			if(subQty<=0){//需要处理的量处理完了，结束
										return true;
									}
					  			
					  			//需要改小的数量与发货数量比较
					  			Double d=BigDecimalUtil.sub(subQty, deliveryItemEntity.getDeliveryQty());
					  			if(d>=0){//需要改小的数量大，也就是说还需要改下一笔发货明细，当前发货明细删除
					  				downQty=deliveryItemEntity.getDeliveryQty();
					  			}else{//需要改小的数量小，当前发货单即可
					  				downQty=subQty;
					  			}
					  			
					  			subQty=BigDecimalUtil.sub(subQty,downQty);
					  			if(deliveryItem.getId()==deliveryItemEntity.getId()){
					  				//当前的发货单就是被扣减的，则直接标红
					  				return false;
					  			}
								}
					  	}
					  }
					  /////////////////////////////////////////////////////////////////
			}
		}
		return true;//无异常
	}
	
	
	/**
	 * 验证当前ASN发货数量是否已经大于订单数量
	 * @param deliveryItem
	 * @param msg
	 * @return
	 */
	public boolean validateOrderQty(DeliveryItemEntity deliveryItem,PurchaseOrderItemPlanEntity itemPlan ,PurchaseOrderItemEntity orderItem ,StringBuffer msg,boolean isNew,int shipType){
		boolean flag = true;//正常
		Double dlvQty =0D;
		// 已创建ASN数量
		if (PurchaseConstans.SHIP_TYPE_NORMAL == shipType) {// 正常
			dlvQty = deliveryItemDao.findDlvQtyByItemId(orderItem.getId());
		}else if (PurchaseConstans.SHIP_TYPE_REPL == shipType) {// 补货
			dlvQty = deliveryItemDao.findDlvQtyByRecItemId(itemPlan.getRecItemId());
		}
		dlvQty=null==dlvQty?0:dlvQty;
		Double totalAmount;
		//是否新建的发货单还未保存时
		if(isNew){
		// 验证数量=创建ASN发货数量+当前发货数量
		 totalAmount = BigDecimalUtil.add(
				null == dlvQty ? 0 : dlvQty,
				null == deliveryItem.getDeliveryQty() ? 0 : deliveryItem
						.getDeliveryQty());
		}else{
			//验证数量=创建ASN发货数量
			totalAmount=dlvQty;
		}
		if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){//正常与订单明细比较
			if (totalAmount > orderItem.getOrderQty()) {
				if(null!=msg){
					msg.append("行号：" + orderItem.getItemNo()).append("已创建ASN数量：")
							.append(dlvQty).append(",当前数量大于订单数量：")
							.append(orderItem.getOrderQty()).append("请修改发货数量！");
				}
				flag=false; //异常
			}
		}else if (PurchaseConstans.SHIP_TYPE_REPL == shipType) {// 补货 与收货不良比较
			ReceiveItemEntity recItem = recItemDao.findById(itemPlan.getRecItemId());
			    //剩余补货数=实际已交货量-待检数量-送检合格
			    Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
	            ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
			    List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
				Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanList.get(0).getId());
				hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
				Double replQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
				///////
			if (totalAmount > replQty) {
				if(null!=msg){	
				     msg.append("行号：" + orderItem.getItemNo())
							.append("已创建ASN数量：").append(dlvQty)
							.append(",当前数量大于收货不良数量：").append(replQty)
							.append("请修改发货数量！");
				}
					flag=false; //异常		
			}
		}
		return flag;
	}
	
	
	/**
	 * 验证当前ASN已发货数量是否已经大于订单数量
	 * @param deliveryItem
	 * @param msg
	 * @return
	 */
	public boolean validateHasDlvOrderQty(DeliveryItemEntity deliveryItem,PurchaseOrderItemPlanEntity itemPlan,PurchaseOrderItemEntity orderItem ,StringBuffer msg,Double deliveryQty,int shipType){
		boolean flag = true;//正常
		// 已创建ASN数量
		Double dlvQty = null;
		if (PurchaseConstans.SHIP_TYPE_NORMAL == shipType) {// 正常
			dlvQty = deliveryItemDao.findHasDlvQtyByItemId(orderItem.getId());
		} else if (PurchaseConstans.SHIP_TYPE_REPL == shipType) {// 补货
			dlvQty = deliveryItemDao.findHasDlvQtyByRecItemId(itemPlan.getRecItemId());
		}
		
		dlvQty=null==dlvQty?0:dlvQty;
		Double totalAmount;
		//是否新建的发货单还未保存时
		// 验证数量=创建ASN发货数量+当前发货数量
		 totalAmount = BigDecimalUtil.add(
				null == dlvQty ? 0 : dlvQty,
				null == deliveryItem.getDeliveryQty() ? 0 : deliveryItem
						.getDeliveryQty());
		 if (PurchaseConstans.SHIP_TYPE_NORMAL == shipType) {// 正常
				if (totalAmount > orderItem.getOrderQty()) {
					if(null!=msg){
						msg.append("行号：" + deliveryItem.getItemNo()).append("已发货ASN数量：")
								.append(dlvQty).append(",当前数量大于订单数量：")
								.append(orderItem.getOrderQty()).append("请修改发货数量！");
					}
					flag=false; //异常
				}
		 }else if (PurchaseConstans.SHIP_TYPE_REPL == shipType) {// 补货
			 ReceiveItemEntity recItem = recItemDao.findById(itemPlan.getRecItemId());
			 //剩余补货数=实际已交货量-待检数量-送检合格
			 Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
	         ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
			 List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
		     Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanList.get(0).getId());
		     hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
			 Double replQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
			 ///////
			if (totalAmount > replQty) {
				if(null!=msg){	
				msg.append("行号：" + deliveryItem.getItemNo())
						.append("已发货ASN数量：").append(dlvQty)
						.append(",当前数量大于收货不良数：").append(replQty)
						.append("请修改发货数量！");
				}
				flag=false; //异常
			}	
		 }
		return flag;
	}

	/**
	 * 取消发货单
	 * @param deliveryList
	 * @param userEntity
	 * @throws Exception 
	 */
	public boolean cancelDelivery(List<DeliveryEntity> deliveryList, UserEntity userEntity,StringBuffer msg) throws Exception {
		boolean isSuccess=true;
		DeliveryEntity delivery = null;
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		Map<Long,Double> requestMap = new HashMap<Long, Double>();//key为要返回要货计划的planId,value为返还数量
		
		//删除DN号 start
		List<DeliveryItemEntity> delItemList=new ArrayList<DeliveryItemEntity>();
		
		for(DeliveryEntity d : deliveryList) {
			if(PurchaseConstans.SHIP_TYPE_NORMAL==d.getShipType()){
				//补货无需删除DN号
				delItemList.addAll(deliveryItemDao.findByDeliveryAndAbolishedAndIsNotNullDn(d.getId(), 0));
			}
		}
		if(null!=delItemList && delItemList.size()>0){
		isSuccess=dnDelSyncSer.execute(delItemList, msg,logger);
			if(!isSuccess){//失败
				return isSuccess;
			}
		}
		//删除DN号  end
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
			List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDeliveryAndAbolished(delivery,0);   
			for(DeliveryItemEntity item : deliveryItem) {   
				updateDlvItem(item,PurchaseConstans.COMMON_INVALID,requestMap,item.getOrderItemPlan().getShipType());
			}
			BOHelper.setBOPublicFields_abolish(delivery);
			deliveryDao.save(delivery);
		} 
		
		//若订单数量减小，释放要货计划(该供货计划当前可能各种状态，有可能已经删除)
		if(null!=requestMap && requestMap.keySet().size()>0){
					for (Long key : requestMap.keySet()) {
						PurchaseOrderItemPlanEntity orderPlan =purchaseOrderItemPlanDao.findById(key);
						writeQtyByPlan(orderPlan,(Double)requestMap.get(key));
					}
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 取消发货单/删除发货单明细(包含订单数量改小的情况)
	 * @param item
	 * @param invalidStatus 1表示是删除或者取消发货单   0表示修改发货单
	 * @param requestMap 需要重新匹配要货计划的数量
	 */
	public void updateDlvItem(DeliveryItemEntity item,int invalidStatus,Map<Long,Double> requestMap,int shipType){
		PurchaseOrderItemPlanEntity orderItemPlan = item.getOrderItemPlan();
		PurchaseOrderItemEntity orderItem=orderItemPlan.getOrderItem();
		Double downQty =0D;//告知供货计划应该扣减多少=要货计划需增加多少
		Double _deliveryQty;
		ReceiveItemEntity recItem = null ;
		
		//1、判断当前需要回到发货看板上面的值
		Double planOrderQty = null;
		//差异数量=ASN数量比订单数量多
		Double sub = null;
		if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){
			planOrderQty = purchaseOrderItemPlanDao.getOrderItemOrderQty(orderItem.getId());
			sub =BigDecimalUtil.sub(null==planOrderQty?0:planOrderQty, orderItem.getOrderQty());
		}else if(PurchaseConstans.SHIP_TYPE_REPL == shipType){
			 recItem = recItemDao.findById(orderItemPlan.getRecItemId());
			 planOrderQty = purchaseOrderItemPlanDao.getSumPlanOrderQtyByReceiveItemId(orderItemPlan.getRecItemId());
			 
			 //剩余补货数=实际已交货量-待检数量-送检合格
			 Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
	         ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
			 List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
			 Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanList.get(0).getId());
			 hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
			 Double replQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
			 ///////
			 sub =BigDecimalUtil.sub(null==planOrderQty?0:planOrderQty,replQty);
		}
		
		//[1]取消发货单|删除明细行
		if(PurchaseConstans.COMMON_INVALID.intValue() == invalidStatus){
			 _deliveryQty = item.getDeliveryQty();
		}else{
			//[2]修改发货单
			_deliveryQty=BigDecimalUtil.sub(null==item.getOldDeliveryQty()?0:item.getOldDeliveryQty(), item.getDeliveryQty());
		}
			
			
			//订单数据是否改小
			if(sub>0){
				//2、判断当前应该退多少回去
				if(_deliveryQty<=sub){
					//无需退发货看板，因为差异数量比发货单大，供货计划都可直接删除
					//即退0
					//returnQty=0D;
					downQty=_deliveryQty;
					//判断当前是否还有应发数量，有应发则还是可以扣减
					Double shouldQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(null==orderItemPlan.getDeliveryQty()?0:orderItemPlan.getDeliveryQty(), null==orderItemPlan.getToDeliveryQty()?0:orderItemPlan.getToDeliveryQty()));
					Double subSub=BigDecimalUtil.sub(sub, _deliveryQty);
					if(shouldQty>=subSub){
						//应发数量大于余量则可以全部释放
						downQty=sub;
					}else{
						//应发也不够扣减，则用原来的+所有的应发去释放尽可能多的
						downQty=BigDecimalUtil.add(shouldQty, _deliveryQty);
					}
				}else{
					//需要退的数量已经大于发货数量，则退发货数量，余下的由别的发货单去回退
					downQty=sub;
				}
				//2、退到发货看板
				
				//3、退到要货计划
			}else{
				//2、按照原来的逻辑回退
				//returnQty=0D;
			}
		
		//取消|修改发货单
		cancelDlvItem(item,orderItemPlan,orderItem,recItem,downQty,invalidStatus,requestMap,shipType);
	}
	
	/**
	 * 取消|修改发货单
	 * @param item
	 * @param org
	 * @param orderItemPlan
	 * @param orderItem
	 * @param delivery
	 */
	public void cancelDlvItem(DeliveryItemEntity item,PurchaseOrderItemPlanEntity orderItemPlan,PurchaseOrderItemEntity orderItem,ReceiveItemEntity recItem,Double downQty,int invalidStatus,Map<Long,Double> requestMap,int shipType){
		//若是删除，则直接删除
		if(invalidStatus == PurchaseConstans.COMMON_INVALID){
			BOHelper.setBOPublicFields_abolish(item);
		}else{
			//修改直接保存即可
			BOHelper.setBOPublicFields_update(item);
		}
		deliveryItemDao.save(item);
		
		//修改供货计划及订单明细数量
		updatePlan(orderItemPlan,recItem,downQty,requestMap,shipType);
	}
	
	/**
	 * 取消发货-修改供货计划
	 * @param orderItemPlan
	 * @param returnQty
	 * @param downQty
	 */
	public void updatePlan(PurchaseOrderItemPlanEntity orderItemPlan,ReceiveItemEntity recItem,Double downQty,Map<Long,Double> requestMap,int shipType){
		//1、下降的数量>=计划数量
		if(downQty>=orderItemPlan.getOrderQty()){
		//删除供货关系，并将计划数量返回要货计划
			if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){
				//正常发货单数量减小回释放要货计划
				requestMap.put(orderItemPlan.getId(), orderItemPlan.getOrderQty());
				BOHelper.setBOPublicFields_abolish(orderItemPlan);
			}
			purchaseOrderItemPlanDao.save(orderItemPlan);
		}else{
		//2、下降数量<计划数量，也将计划数量返回要货计划
		//3、下降数量为0
			
			//供货计划数量
			Double orderQty=BigDecimalUtil.sub(orderItemPlan.getOrderQty(),downQty);
			orderItemPlan.setOrderQty(orderQty);
			orderItemPlan.setBaseQty(orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(), orderItemPlan.getOrderItem().getBPUMZ(), orderItemPlan.getOrderQty()));
			//发货数量=实时查询
			Double hasDelQty = deliveryItemDao.findDlvQtyByPlanId(orderItemPlan.getId(), PurchaseConstans.DELIVERY_STATUS_YES);
			hasDelQty=null==hasDelQty?0:hasDelQty;
			orderItemPlan.setDeliveryQty(hasDelQty<0?0D:hasDelQty);
			
			//已创建未发数量=实时查询
			Double toDeliveryQty = deliveryItemDao.findDlvQtyByPlanId(orderItemPlan.getId(), PurchaseConstans.DELIVERY_STATUS_NO);
			toDeliveryQty=null==toDeliveryQty?0:toDeliveryQty;
			orderItemPlan.setToDeliveryQty(toDeliveryQty>0?toDeliveryQty:0D);
			
//			//未发
//			Double undeliveryQty = BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty()));
//			undeliveryQty=null==undeliveryQty?0:undeliveryQty;
//			orderItemPlan.setUndeliveryQty(undeliveryQty>orderItemPlan.getOrderQty()?orderItemPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
//			
//			//在途
//			Double onwayQty=deliveryItemDao.findOnWayQtyByPlanId(orderItemPlan.getId());
//			onwayQty=null==onwayQty?0:onwayQty;
//			orderItemPlan.setOnwayQty(onwayQty);
			//更新在途与未发数量，增加补货 
			updateSourcePlanQty(orderItemPlan);
			
			//更新订单发货状态
			double deliveryQty = BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty());
			if(deliveryQty == 0) {	
				orderItemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				//部分发货
			} else if(deliveryQty > 0 && deliveryQty < orderItemPlan.getOrderQty()) {
				orderItemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART); 
			// 供货计划已发货
			} else if(deliveryQty >= orderItemPlan.getOrderQty() || orderItemPlan.getUndeliveryQty()==0){  
				orderItemPlan.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}
			BOHelper.setBOPublicFields_update(orderItemPlan);
			purchaseOrderItemPlanDao.save(orderItemPlan);
			if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){
				//正常发货单数量减小回释放要货计划
				if(downQty>0){
					requestMap.put(orderItemPlan.getId(), downQty);
				}
			}
		}
		if(PurchaseConstans.SHIP_TYPE_NORMAL == shipType){
			//4、更新订单明细在途数量和未发数量和发货状态
			updateOrderItem(orderItemPlan.getOrderItem());
		}else if(PurchaseConstans.SHIP_TYPE_REPL  == shipType){
			//4、更新收货明细在途数量和未发数量和发货状态
			updateReceiveItem(recItem);
			if(downQty>=orderItemPlan.getOrderQty()){
				BOHelper.setBOPublicFields_abolish(orderItemPlan);
				purchaseOrderItemPlanDao.save(orderItemPlan);
			
			}
		}
		
	}
	
	/**
	 * [正常]取消发货-更新订单明细在途数量和未发数量
	 * @param orderItem
	 */
	public void updateOrderItem(PurchaseOrderItemEntity orderItem){
		PurchaseOrderEntity order = purchaseOrderDao.findById(orderItem.getOrder().getId());
		//在途数量
		Double itemOnWayQty=deliveryItemDao.findOnWayQtyByOrderItemId(orderItem.getId());
		itemOnWayQty=null==itemOnWayQty?0:itemOnWayQty;
		orderItem.setOnwayQty(itemOnWayQty>0?itemOnWayQty:0D);
		//已发数量
		Double hasDlvQty=deliveryItemDao.findHasDlvQtyByOrderItemId(orderItem.getId());
		hasDlvQty=null==hasDlvQty?0:hasDlvQty;
		Double itemUndelQty=BigDecimalUtil.sub(orderItem.getOrderQty(), hasDlvQty);
		orderItem.setUndeliveryQty(itemUndelQty>orderItem.getOrderQty()?orderItem.getOrderQty():itemUndelQty>0?itemUndelQty:0D);
		
		//查询所有未删除且未失效的发货明细
		if(null!=hasDlvQty){
			if(hasDlvQty>=orderItem.getOrderQty()){
			   orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}else if(hasDlvQty>0){
			   orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			}
		}else {
			orderItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
		}
		
		BOHelper.setBOPublicFields_update(orderItem);
		purchaseOrderItemDao.save(orderItem);
		updatePurchaeseDeliveryStatus(orderItem,order);
	}
	
	/**
	 * 【补货】取消发货-更新收货明细在途数量和未发数量
	 * @param orderItem
	 */
	public void updateReceiveItem(ReceiveItemEntity recItem){
		//在途数量
		Double itemOnWayQty=deliveryItemDao.findOnWayQtyByRecItemId(recItem.getId());
		itemOnWayQty=null==itemOnWayQty?0:itemOnWayQty;
		recItem.setReplOnwayQty(itemOnWayQty>0?itemOnWayQty:0D);
		
		//已发数量
		Double hasDlvQty=deliveryItemDao.findHasDlvQtyByRecItemId(recItem.getId());
		hasDlvQty=null==hasDlvQty?0:hasDlvQty;
		 //剩余补货数=实际已交货量-待检数量-送检合格
		 Double badNumber=BigDecimalUtil.sub(null==recItem.getReceiveQty()?0:recItem.getReceiveQty(),BigDecimalUtil.add(null==recItem.getZdjsl()?0:recItem.getZdjsl(), null==recItem.getZsjhg()?0:recItem.getZsjhg()));
        ////////当前发货看板的数量应为=剩余补货数+该计划的所有已收货的发货单数量
		 List<PurchaseOrderItemPlanEntity> replPlanList=purchaseOrderItemPlanDao.findByReceiveItemId(recItem.getId());
		 Double hasDeliveryQtyHasRec=deliveryItemDao.findHasDlvQtyRecByPlanId(replPlanList.get(0).getId());
		 hasDeliveryQtyHasRec=null==hasDeliveryQtyHasRec?0:hasDeliveryQtyHasRec;
		 Double replQty=BigDecimalUtil.add(badNumber, hasDeliveryQtyHasRec);
		 ///////
		Double itemUndelQty=BigDecimalUtil.sub(replQty, hasDlvQty);
		recItem.setReplUndeliveryQty(itemUndelQty>replQty?replQty:itemUndelQty>0?itemUndelQty:0D);
		
		//查询所有未删除且未失效的发货明细
		if(null!=hasDlvQty){
			if(hasDlvQty>=replQty){
				recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_YES);
			}else if(hasDlvQty>0){
				recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
			}else{
				recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			}
		}else {
			recItem.setReplDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
		}
		
		BOHelper.setBOPublicFields_update(recItem);
		recItemDao.save(recItem);
	}
	
	
	/**
	 * 回写要货计划数量 释放订单数量
	 * 当完全删除plan时，qty参数就是plan的数量
	 * 当更新plan是，qty是需要处理扣除的量
	 */
	public void writeQtyByPlan(PurchaseOrderItemPlanEntity plan,Double qty){
		PurchaseOrderItemEntity orderItem=plan.getOrderItem();
		
		PurchaseGoodsRequestEntity request=plan.getPurchaseGoodsRequest();

		//1、释放到要货计划
		qty=orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), qty);
		double surQry =Double.valueOf(request.getSurQry());
		request.setSurQry(""+BigDecimalUtil.add(surQry, qty));
		purchaseGoodsRequestDao.save(request);
		
		//2、更新行剩余匹配数量更新更新
		orderItem.setSurOrderQty(BigDecimalUtil.add(orderItem.getSurOrderQty(),qty));
		orderItem.setSurBaseQty(orderQtyToBaseQty(orderItem.getBPUMN(), orderItem.getBPUMZ(), orderItem.getSurOrderQty()));
		purchaseOrderItemDao.save(orderItem);
		
		//3、要货计划释放数量匹配订单
        goodsRequestMatchPo(request.getId());
	}
	
	/**
	 * 删除发货单明细
	 * @param deliveryList
	 * @param userEntity
	 * @throws Exception 
	 */
	public boolean deleteItemDelivery(DeliveryItemEntity item, UserEntity userEntity,StringBuffer msg) throws Exception {
		boolean isSuccess=true;
		Map<Long,Double> requestMap = new HashMap<Long, Double>();//key为要返回要货计划的planId,value为返还数量
		//删除DN号 start
		List<DeliveryItemEntity> delItemList=new ArrayList<DeliveryItemEntity>();
        if(StringUtils.isNotEmpty(item.getDn()) && PurchaseConstans.SHIP_TYPE_NORMAL ==item.getDelivery().getShipType()){
        	delItemList.add(item);
        }

		if(null!=delItemList && delItemList.size()>0){
		isSuccess=dnDelSyncSer.execute(delItemList, msg,logger);
			if(!isSuccess){//失败
				return isSuccess;
			}
		}
		//删除DN号  end
		
		//修改发货明细
		updateDlvItem(item,PurchaseConstans.COMMON_INVALID,requestMap,item.getOrderItemPlan().getShipType());
		
		//若订单数量减小，释放要货计划(该供货计划当前可能各种状态，有可能已经删除)
		if(null!=requestMap && requestMap.keySet().size()>0){
					for (Long key : requestMap.keySet()) {
						PurchaseOrderItemPlanEntity orderPlan =purchaseOrderItemPlanDao.findById(key);
						writeQtyByPlan(orderPlan,(Double)requestMap.get(key));
					}
		}
		
		//审核状态(通过或者驳回)-已审批ASN修改数量提醒
/*		DeliveryEntity  delivery = deliveryDao.findById(item.getDelivery().getId());
		if(delivery.getAuditStatus() != 0){
			 List<Long> userIdList = new ArrayList<Long>();
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(delivery.getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 userIdList.add(rel.getUserId());
			 }
		     warnMainService.warnMessageSet(delivery.getId(), WarnConstant.ASN_NUMBER_CHANGE, userIdList);
			
		}
		*/

		return isSuccess;
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

	public String getCodePrefix() {
		String year = (DateUtil.getCurrentYear()+"").substring(2);
		int month = DateUtil.getCurrentMonth();
		String mon = "";
		if(month == 10){
			mon = "A";
		}else if(month == 11){
			mon = "B";
		}else if(month == 12){
			mon = "C";
		}else{
			mon = month +"";
		}
		int day = DateUtil.getCurrentDate();
		String date = "";
		if(day <10){
			date = "0" +day;
		}else{
			date = day +"";
		}
		String codePrefix = year + mon + date;
		return codePrefix;
	}

	
	/**
	 * 获取审核列表【采】
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<DeliveryEntity> getToAuditDeliverys(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<DeliveryEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DeliveryEntity.class);
		Page<DeliveryEntity> page =  deliveryDao.findAll(spec, pagin);   
		return page;
	}
	
	
	/**
	 * 审核通过-审核【采】
	 * @param orderList
	 * @param userEntity
	 * @throws Exception 
	 */
	public boolean doAgree(List<DeliveryEntity> deliveryList, Long userId,StringBuffer msg) throws Exception {
		DeliveryEntity delivery = null;
		boolean isSuccess=true;
		Set<DeliveryItemEntity> dlvItemSet;
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
		   /////验证发货数量是否大于订单数量
			dlvItemSet=delivery.getDeliveryItem();
			if(null!=dlvItemSet && dlvItemSet.size()>0){
						for (DeliveryItemEntity deliveryItemEntity : dlvItemSet) {
							PurchaseOrderItemEntity orderItem =purchaseOrderItemDao.findById( deliveryItemEntity.getOrderItem().getId());
							PurchaseOrderItemPlanEntity plan=purchaseOrderItemPlanDao.findById(deliveryItemEntity.getOrderItemPlan().getId());
							boolean flag=validateOrderQty(deliveryItemEntity,plan,orderItem,msg,false,plan.getShipType());
							if(!flag){
								return flag;
							}
						}
					}
			//////
			delivery.setAuditStatus(PurchaseConstans.AUDIT_YES);
			delivery.setAuditUserId(userId);;
			delivery.setAuditTime(DateUtil.getCurrentTimestamp());
			BOHelper.setBOPublicFields_update(delivery);
		}
		
		//获取成功则保存
		deliveryDao.save(delivery);
		
		//ASN审批确认提醒   删除待审批的
		/*for(DeliveryEntity temDelivery : deliveryList) {
			temDelivery = deliveryDao.findOne(temDelivery.getId());
			//发送asn审批的消息
			List<UserEntity> users = userServiceImpl.findByCompany(temDelivery.getVendor().getId());
		    List<Long> userIdList = new ArrayList<Long>();
		    for( UserEntity us :  users){
				 userIdList.add(us.getId());
			 }
			warnMainService.warnMessageSet(temDelivery.getId(), WarnConstant.ASN_APPROAL, userIdList);
			
			//删除ASN待审批提醒
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(temDelivery.getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 warnMainService.delMessageByBillIdAndBillType(temDelivery.getId(), WarnConstant.ASN_UN_APPROAL,rel.getUserId());
			}
		}*/
		return isSuccess;
	}
	
	/**
	 * 审核驳回-审核【采】
	 * @param vo
	 * @return
	 */
	public boolean doReject(RejectVO vo){
		String [] ids=vo.getReject_ids().split(",");
		DeliveryEntity delivery;
		List<DeliveryEntity> deliveryList;
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(null!=ids && ids.length>0){
			deliveryList=new ArrayList<DeliveryEntity>();
			for (String str : ids) {
				delivery=deliveryDao.findOne(Long.valueOf(str));
				delivery.setAuditStatus(PurchaseConstans.AUDIT_REFUSE);
				delivery.setAuditUserId(user.id);;
				delivery.setAuditTime(DateUtil.getCurrentTimestamp());
				delivery.setRejectReason(vo.getReject_reason());
				BOHelper.setBOPublicFields_update(delivery);
				deliveryList.add(delivery);
			}
			deliveryDao.save(deliveryList);
		}
		return true;
	}
	
	public List<DeliveryPCEntity> getPCListBYDeliveryItemId(Long id) {
		return deliveryPCDao.getByDeliveryItemId(id);
	}

	public List<DeliveryDTEntity> getDTListBYDeliveryItemId(Long id) {
		return deliveryDTDao.getByDeliveryItemId(id);
	}
	
	/**
	 * 获取收货单明细(通过主订单的id)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<DeliveryItemEntity> getDeliveryItemsByMainOrderId(Long mainOrderId) {
		//通过主订单的id获取下面的明细行
		List<PurchaseOrderItemEntity>   poItemList  =  purchaseOrderItemDao.findByOrderId(mainOrderId, 0);
		List<Long> ids = new ArrayList<Long>();	
		if(poItemList !=  null && poItemList.size() != 0){
			for(PurchaseOrderItemEntity poItem : poItemList ){
				ids.add(poItem.getId());
			}
		}

		List<DeliveryItemEntity>  diItemList= deliveryItemDao.findByorderItemIdInAndAbolished(ids,0);
		
		return diItemList;
		
	}
	
	
	/**
	 * 根据id查发货单主表
	 */
	public DeliveryEntity findDlvById(Long id){
		return deliveryDao.findById(id);
	}
	
		
	//取消未被审核通过的发货单(用于接口同步的要货计划的修改)
	public void cancelUnAuditDelivery(List<DeliveryEntity> deliveryList) throws Exception {
		DeliveryEntity delivery = null;
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		for(DeliveryEntity d : deliveryList) {
			delivery = deliveryDao.findOne(d.getId());
			List <DeliveryItemEntity> deliveryItem = deliveryItemDao.findByDeliveryAndAbolished(delivery,0);   
			for(DeliveryItemEntity item : deliveryItem) {   
				item.setAbolished(Constant.DELETE_FLAG);
				
				orderItemPlan = item.getOrderItemPlan();
				
				Double _deliveryQty = item.getDeliveryQty();	//发货单中的发货数量
				if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_NO) {
					Double delQty = BigDecimalUtil.sub(orderItemPlan.getDeliveryQty(), _deliveryQty);//发货数量=plan的发货数量-发货单中的发货数量
					orderItemPlan.setDeliveryQty(delQty<0?0D:delQty);
					
					Double toDeliveryQty = BigDecimalUtil.sub(orderItemPlan.getToDeliveryQty(), _deliveryQty);	//已创建未发数量=原来的已创建未发数量-发货单中的发货数量
					orderItemPlan.setToDeliveryQty(toDeliveryQty>0?toDeliveryQty:0D);
					
					Double undeliveryQty = BigDecimalUtil.sub(orderItemPlan.getOrderQty(), BigDecimalUtil.add(orderItemPlan.getDeliveryQty(), orderItemPlan.getToDeliveryQty()));
					orderItemPlan.setUndeliveryQty(undeliveryQty>orderItemPlan.getOrderQty()?orderItemPlan.getOrderQty():undeliveryQty>0?undeliveryQty:0D);
					
				}else if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES){
					Double delQty = BigDecimalUtil.sub(orderItemPlan.getDeliveryQty(), _deliveryQty);//发货数量=plan的发货数量-发货单中的发货数量
					orderItemPlan.setDeliveryQty(delQty<0?0D:delQty);
					
					//更新在途与未发数量，增加补货 
					updateSourcePlanQty(orderItemPlan);
				}
				//更新订单发货状态
				updateDeliveryStatus(orderItemPlan);
				BOHelper.setBOPublicFields_update(orderItemPlan);
				purchaseOrderItemPlanDao.save(orderItemPlan);
				PurchaseOrderItemEntity itemEntity=orderItemPlan.getOrderItem();
				//取消发货
				if(delivery.getDeliveryStatus() == PurchaseConstans.DELIVERY_STATUS_YES){
				Double itemOnWayQty=BigDecimalUtil.sub(itemEntity.getOnwayQty(), _deliveryQty);
				itemEntity.setOnwayQty(itemOnWayQty>0?itemOnWayQty:0D);
				Double itemUndelQty=BigDecimalUtil.add(itemEntity.getUndeliveryQty(), _deliveryQty);
				itemEntity.setUndeliveryQty(itemUndelQty>itemEntity.getOrderQty()?itemEntity.getOrderQty():itemUndelQty>0?itemUndelQty:0D);
				}
				
				delivery.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				delivery.setAbolished(Constant.DELETE_FLAG);	//自动删除发货单
				deliveryItemDao.save(item);
				
				//查询所有未删除且未失效的发货明细
				List<DeliveryItemEntity> deliveryItemList = deliveryItemDao.
						findByOrderItemIdAndDeliveryAbolishedAndDeliveryStatus(itemEntity.getId(), Constant.UNDELETE_FLAG, PurchaseConstans.COMMON_NORMAL);
				if(deliveryItemList != null && deliveryItemList.size() != 0){	//不为空,且有发货明细数据,则表示部分发货
					itemEntity.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_PART);
				}else{															//为空，则表示待发货
					itemEntity.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				}
				BOHelper.setBOPublicFields_update(itemEntity);
				purchaseOrderItemDao.save(itemEntity);
			}
			deliveryDao.save(delivery);
		} 
	}

	/**
	 * 保存修改的发货数量
	 * @param datas
	 * @return
	 */
	public Map<String,Object> saveChange(String datas){
		Map<String,Object> map = new HashMap<String, Object>();
		JSONObject object = JSONObject.fromObject(datas);     
		JSONArray array = (JSONArray) object.get("rows");
		StringBuffer msg = new StringBuffer();
		Map<Long,Double> requestMap = new HashMap<Long, Double>();//key为要返回要货计划的planId,value为返还数量
		//验证修改发货单数量
		if(!validateChange(object,array,msg)){
			map.put("success", false);
			map.put("message", msg.toString());
			return map;
		}
		
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			Long deliveryItemId = StringUtils.convertToLong(object.get("id") + "");
			DeliveryItemEntity itemEntity = deliveryItemDao.findById(deliveryItemId);
			itemEntity.setOldDeliveryQty(itemEntity.getDeliveryQty());
			itemEntity.setDeliveryQty(StringUtils.convertToDouble(object.get("deliveryQty") + "")); //发货数量
			itemEntity.setStandardBoxNum(StringUtils.convertToDouble(object.get("standardBoxNum") + ""));	//标准箱数
			itemEntity.setBoxNum(StringUtils.convertToString(object.get("boxNum") + ""));	//箱数量
			itemEntity.setMinPackageQty(StringUtils.convertToDouble(object.get("minPackageQty") + ""));	//小包装数量
			itemEntity.setMinBoxNum(StringUtils.convertToString(object.get("minBoxNum") + ""));	//小包装箱数
			itemEntity.setErrorStatus(PurchaseConstans.DIFFERENCE_STATUS_YES); //异常状态
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			itemEntity.setChangeUserId(user.id);	//导致异常的人的ID
			itemEntity.setChangeTime(DateUtil.getCurrentTimestamp()); //导致异常的时间
			//修改数量
			updateDlvItem(itemEntity, PurchaseConstans.COMMON_NORMAL,requestMap,itemEntity.getOrderItemPlan().getShipType());
		}
		
		//若订单数量减小，释放要货计划(该供货计划当前可能各种状态，有可能已经删除)
		if(null!=requestMap && requestMap.keySet().size()>0){
					for (Long key : requestMap.keySet()) {
						PurchaseOrderItemPlanEntity orderPlan =purchaseOrderItemPlanDao.findById(key);
						writeQtyByPlan(orderPlan,(Double)requestMap.get(key));
					}
		}
		
		//审核状态(通过或者驳回)-已审批ASN修改数量提醒
/*		if(delivery.getAuditStatus() != 0){
			 List<Long> userIdList = new ArrayList<Long>();
			 List<GroupConFigRelEntity> userOrderList= userConfigRelService.getRelByGroupId(delivery.getPurchasingGroupId());
			 for(GroupConFigRelEntity rel : userOrderList ){
				 userIdList.add(rel.getUserId());
			 }
		     warnMainService.warnMessageSet(delivery.getId(), WarnConstant.ASN_NUMBER_CHANGE, userIdList);
			
		}*/

		map.put("success", true);
		map.put("message", "保存成功");
		return map;
	}
	
	/**
	 * 验证修改发货单数量
	 * 已审核的数量不能大于原先数量
	 * 当前数量与已发货的数量比是否大于订单数量
	 * @param object
	 * @param array
	 * @param msg
	 * @return
	 */
	public boolean validateChange(JSONObject object,JSONArray array,StringBuffer msg){
		boolean flag =true;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			Long deliveryItemId = StringUtils.convertToLong(object.get("id") + "");
			DeliveryItemEntity itemEntity = deliveryItemDao.findById(deliveryItemId);
			if(StringUtils.convertToDouble(object.get("deliveryQty") + "")>itemEntity.getDeliveryQty() && PurchaseConstans.AUDIT_YES.intValue()==itemEntity.getDelivery().getAuditStatus().intValue()){
				msg.append("采购订单号：").append(itemEntity.getOrderItem().getOrder().getOrderCode()).append(",行号：").append(itemEntity.getItemNo()).append(",审核通过的发货单,修改的发货数量不能大于原先的发货数量!");
				flag=false;
			}else{
				Double deliveryQty=StringUtils.convertToDouble(object.get("deliveryQty") + "");
				PurchaseOrderItemPlanEntity orderPlan = itemEntity.getOrderItemPlan();
				if(!validateHasDlvOrderQty(itemEntity,orderPlan, itemEntity.getOrderItem(), msg,deliveryQty,orderPlan.getShipType())){
					flag=false;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 处理page数据
	 * @param page
	 * @return
	 */
	public Page<DeliveryEntity> initData(Page<DeliveryEntity> page){
		for (DeliveryEntity deliveryEntity : page) {
			if(deliveryEntity.getPurchasingGroupId() != null){
				PurchasingGroupEntity group = purchasingGroupDao.findById(deliveryEntity.getPurchasingGroupId());
				if(group!= null){
					deliveryEntity.setPurchasingGroupName(group.getName());
				}
			}
			if(fmtExceptStatus(deliveryEntity)){
			    deliveryEntity.setExceptionStatus(PurchaseConstans.EXCEPTION_STATUS_YES);
			}else{
				deliveryEntity.setExceptionStatus(PurchaseConstans.EXCEPTION_STATUS_NO);
			}
		}
		return page;
	}
	
	/**
	 * 判断锁定状态、冻结状态、删除状态、交付状态 、订单数量改小
	 * @param deliveryEntity
	 * @return
	 */
	public boolean fmtExceptStatus(DeliveryEntity deliveryEntity){
		//add by chao.gu 20171012 提高性能
			//1、判断是否已发货，已发货不受红色标识影响
			if(PurchaseConstans.DELIVERY_STATUS_YES.intValue() ==deliveryEntity.getDeliveryStatus().intValue()){
				if(PurchaseConstans.SHIP_TYPE_NORMAL == deliveryEntity.getShipType()){
					return false;
				}
			}
		//add end
		
		Set<DeliveryItemEntity> set =deliveryEntity.getDeliveryItem();
		for (DeliveryItemEntity item : set) {
			PurchaseOrderItemEntity orderItem = getPurchaseOrderItemById(item.getOrderItem().getId());
			PurchaseOrderItemPlanEntity itemPlan =getPurchaseOrderItemPlanById(item.getOrderItemPlan().getId());
			if(orderItem != null){
				if(null!=orderItem.getZlock() && "X".equals(orderItem.getZlock())){
					return true;
				}else if(null!=orderItem.getLockStatus() && "1".equals(orderItem.getLockStatus())){
					return true;
				}else if(null!=orderItem.getLoekz() && "X".equals(orderItem.getLoekz())){
					return true;
				}else if(null!=orderItem.getElikz() && "X".equals(orderItem.getElikz())){
					return true;
				}else if(null!=orderItem.getBstae() && "X".equals(orderItem.getBstae())){
					return true;
				}else if(!validateDlvInOrderQty(item, itemPlan,orderItem, null, false,itemPlan.getShipType())){//订单数量已减小--根据当前发货单判断处于什么位置，是否需要扣减标红  ---modfiy by chao.gu 20170821
					return true;
				}
				
			}
		}
		return false;
	}
	
	/**
	 * 审核添加锁定状态、冻结状态、删除状态、交付状态的判断
	 * @param selectIdStr
	 * @return
	 */
	public Map<String,Object> judgeAudit(List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		boolean temp = true;
		StringBuffer msgBUf = new StringBuffer();
		for (DeliveryEntity deliveryEntity : deliveryList) {
			msgBUf.append("发货单号：").append(deliveryEntity.getDeliveryCode());
			List<DeliveryItemEntity>  itemList = deliveryItemDao.findByDeliveryAndAbolished(deliveryEntity, Constant.UNDELETE_FLAG);
			for (DeliveryItemEntity item : itemList) {
				PurchaseOrderItemEntity orderItem = getPurchaseOrderItemById(item.getOrderItem().getId());
				PurchaseOrderItemPlanEntity orderItemPlan =getPurchaseOrderItemPlanById(item.getOrderItemPlan().getId());
				if(orderItem != null){
					if(orderItem.getZlock()!=null && "X".equals(orderItem.getZlock())){
						temp = false;
						msgBUf.append("发货单明细中存在已锁定的数据无法进行审核！").append("</br>");
					}else if(orderItem.getLockStatus()!=null && "1".equals(orderItem.getLockStatus())){
						temp = false;
						msgBUf.append("发货单明细中存在已冻结的数据无法进行审核！").append("</br>");
					}else if(orderItem.getLoekz()!=null && "X".equals(orderItem.getLoekz())){
						temp = false;
						msgBUf.append("发货单明细中存在已删除的数据无法进行审核！").append("</br>");
					}else if(orderItem.getElikz()!=null && "X".equals(orderItem.getElikz())){
						temp = false;
						msgBUf.append("发货单明细中存在已交付的数据无法进行审核！").append("</br>");
					}else if(orderItem.getBstae()!=null && "X".equals(orderItem.getBstae())){
						temp = false;
						msgBUf.append("该订单不是内向交货标识,无法进行审核！").append("</br>");
					}else if(!validateOrderQty(item, orderItemPlan,orderItem, null, false,orderItemPlan.getShipType())){
						temp = false;
						msgBUf.append("发货数量大于订单数量无法进行审核！").append("</br>");
					}
				}else{
					temp = false;
					msgBUf.append("采购订单明细不存在！");
				}
				
			}
		}
		map.put("success", temp);
		map.put("message", msgBUf.toString());
		return map;
	}
	
	
	/**
	 * 要货计划直接匹配订单
	 * 
	 * @return
	 */
	public void goodsRequestMatchPo(Long goodsId) {	
		
			List<PurchaseOrderItemEntity> poItemList = new ArrayList<PurchaseOrderItemEntity>();
			PurchaseOrderItemEntity poItem=null; 
			
			List<PurchaseOrderItemPlanEntity> poItemPlanList  = new ArrayList<PurchaseOrderItemPlanEntity>();
			PurchaseOrderItemPlanEntity poItemPlan=null;
			
			List<PurchaseGoodsRequestEntity> goodsRequestList  = new ArrayList<PurchaseGoodsRequestEntity>();
			PurchaseGoodsRequestEntity  goodsRequest=null;
			
			
			//查询当前的要货计划
			PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(goodsId);
			
			//通过查询已确认（不等于已发货（包括未发货和部分发货的数量））的订单的行创建时间的先后匹配（工厂，物料和供应商）
			List<PurchaseOrderItemEntity> poItemList1 = purchaseOrderItemDao.findGoodsRequestMatchPo(goods.getFactory().getId(),goods.getMaterial().getId(),goods.getVendor().getId());

			Double number =Double.valueOf(goods.getSurQry());//基本数量
			
			for(PurchaseOrderItemEntity item : poItemList1){
				poItem = new PurchaseOrderItemEntity();//订单行
				poItemPlan = new PurchaseOrderItemPlanEntity();//供货计划
				poItemPlan.setShipType(PurchaseConstans.SHIP_TYPE_NORMAL);//正常
				goodsRequest = new PurchaseGoodsRequestEntity();//要货计划
				if(item.getSurBaseQty() <= 0.01){
					continue;
				}
				
				
				if(BigDecimalUtil.sub(item.getSurBaseQty(), number)  >= 0  &&  number>0){ //订单行满足要货计划（多余和正好满足）
					//订单行项目
					poItem=item;
					
					//设置剩余基本数量
					poItem.setSurBaseQty(BigDecimalUtil.sub(poItem.getSurBaseQty(),number));
					//设置剩余订单数量
					Double  oNumber = baseQtyToOrderQty(item.getBPUMN(),item.getBPUMZ(),number);
					poItem.setSurOrderQty(BigDecimalUtil.sub(poItem.getSurOrderQty(),oNumber));
					
					
					//供货计划的添加
					poItemPlan.setOrder(poItem.getOrder());//订单（订单号和采购组）
					poItemPlan.setOrderItem(poItem);  	//订单行项目
					poItemPlan.setItemNo(poItem.getItemNo());//行号
					poItemPlan.setGoodsRequestId(goods.getId());
					poItemPlan.setPurchaseGoodsRequest(goods);//要货计划（工厂，物料，预计到货日期，物流天数，供应商，以及各种状态）
					poItemPlan.setMaterial(materialService.getMaterialById(goods.getMaterial().getId()));
					poItemPlan.setAbolished(0);
					
					poItemPlan.setOrderQty(oNumber);//要货的订单数量
					poItemPlan.setBaseQty(number);//要货的基本数量
					
					poItemPlan.setUndeliveryQty(oNumber);
					poItemPlan.setRequestTime(goods.getRq());//要求到货时间
					poItemPlan.setCurrency(poItem.getOrder().getWaers());//币种waers
					poItemPlan.setUnitName(poItem.getMeins());//订单单位
					poItemPlan.setDeliveryQty(0.00);	// 已发数量
					poItemPlan.setToDeliveryQty(0.00);// 已创建未发数量
					poItemPlan.setReceiveQty(0.00);// 实收数量
					poItemPlan.setReturnQty(0.00);// 退货数量
					poItemPlan.setDiffQty(0.00);	// 差异数量
					poItemPlan.setOnwayQty(0.00);	// 在途数量
					poItemPlan.setUndeliveryQty(0.00);// 未发数量
					poItemPlan.setConfirmStatus(0);//确认状态(初始化中4个状态都是为0)
					poItemPlan.setPublishStatus(0);//发布状态
					poItemPlan.setDeliveryStatus(0);//发货状态(初始化中4个状态都是为0)
					poItemPlan.setReceiveStatus(0);//收货状态(初始化中4个状态都是为0)
					//要货计划的修改
					goodsRequest = goods;
					goodsRequest.setIsFull(1);
					goodsRequest.setSurQry("0.000");
					poItemList.add(poItem);
					poItemPlanList.add(poItemPlan);
					goodsRequestList.add(goodsRequest);
					break;
					
				}else if(  (BigDecimalUtil.sub(item.getSurBaseQty(), number)  < 0 ) && item.getSurBaseQty() >0.01 &&  number>0){//订单行未满足要货计划（未满足）
					//订单行项目
					poItem=item;
					//要货计划的添加
					poItemPlan.setOrder(poItem.getOrder());//订单（订单号和采购组）
					poItemPlan.setOrderItem(poItem);  	//订单行项目
					poItemPlan.setItemNo(poItem.getItemNo());//行号
					poItemPlan.setGoodsRequestId(goods.getId());
					poItemPlan.setPurchaseGoodsRequest(goods);//要货计划（工厂，物料，预计到货日期，物流天数，供应商，以及各种状态）
					poItemPlan.setMaterial(materialService.getMaterialById(goods.getMaterial().getId()));
					poItemPlan.setAbolished(0);
					poItemPlan.setOrderQty(poItem.getSurOrderQty());//供货计划订单数量 == 订单的订单数量
					poItemPlan.setBaseQty(poItem.getSurBaseQty());//供货计划基本数量 == 订单的基本数量
					
					poItemPlan.setUndeliveryQty(poItem.getSurOrderQty());

					poItemPlan.setRequestTime(goods.getRq());//要求到货时间
					poItemPlan.setCurrency(poItem.getOrder().getWaers());//币种waers
					poItemPlan.setUnitName(poItem.getMeins());//订单单位
					poItemPlan.setDeliveryQty(0.00);	// 已发数量
					poItemPlan.setToDeliveryQty(0.00);// 已创建未发数量
					poItemPlan.setReceiveQty(0.00);// 实收数量
					poItemPlan.setReturnQty(0.00);// 退货数量
					poItemPlan.setDiffQty(0.00);	// 差异数量
					poItemPlan.setOnwayQty(0.00);	// 在途数量
					poItemPlan.setUndeliveryQty(0.00);// 未发数量
					poItemPlan.setConfirmStatus(0);//确认状态(初始化中4个状态都是为0)
					poItemPlan.setPublishStatus(0);//发布状态
					poItemPlan.setDeliveryStatus(0);//发货状态(初始化中4个状态都是为0)
					poItemPlan.setReceiveStatus(0);//收货状态(初始化中4个状态都是为0)
					//要货计划的修改
					number= BigDecimalUtil.sub(number, poItem.getSurBaseQty()); 
					
					goodsRequest = goods;
					
					if(number <= 0 ){
						goodsRequest.setIsFull(1);
						number=0.000;
					}
					
					goodsRequest.setSurQry(number+"");
					
					poItem.setSurOrderQty(0.000);
					poItem.setSurBaseQty(0.000);
					
					poItemList.add(poItem);
					poItemPlanList.add(poItemPlan);
					goodsRequestList.add(goodsRequest);
					continue;
				}
			}
			
			//保存要货计划、供货计划、订单的行项目
			purchaseOrderItemDao.save(poItemList);
			purchaseOrderItemPlanDao.save(poItemPlanList);
			purchaseGoodsRequestDao.save(goodsRequestList);
			
			//更新发布状态和确认状态

			modifyGoodsRequestPublishStatus(goods.getId());

		 
		    modifyGoodsRequestConfirmStatus(goods.getId());

			
		}
	
	//修改主要货计划发布状态
		public void modifyGoodsRequestPublishStatus(Long id) {
			//查询由要货计划生成的供货计划
			PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(id);
			
			//获取未发布的供货计划
			List<PurchaseOrderItemPlanEntity> orderItemPlanList = purchaseOrderItemPlanDao.getUnPublishPoItemplanListBygoodsId(id);
			Double number = 0.00;
			
			if(orderItemPlanList != null && orderItemPlanList.size() != 0 ){
				for(PurchaseOrderItemPlanEntity  orderItemPlan : orderItemPlanList){
					//讲订单数量改为基本数量
					Double  bNumber = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,orderItemPlan.getOrderQty());
					number = BigDecimalUtil.add(number, bNumber);
				}
			}

			number = BigDecimalUtil.add(number, Double.valueOf(goods.getSurQry()));

			Double totalNumber= Double.valueOf(goods.getDhsl());
			if(goods != null){
				if( number== 0.00 ){
					goods.setPublishStatus(1);//发布
				}else if(  totalNumber >   number  && 0.00  <  number){
					goods.setPublishStatus(2);//部分发布
				}else if(   BigDecimalUtil.sub(totalNumber, number) <= 0 ){
					goods.setPublishStatus(0);//未发布
				}
				purchaseGoodsRequestDao.save(goods);
			}
			
			
		}
	
		//修改主要货计划确认状态的状态
		public void modifyGoodsRequestConfirmStatus(Long id) {
			//查询由要货计划生成的供货计划
			PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(id);
			List<PurchaseOrderItemPlanEntity> orderItemPlanList = purchaseOrderItemPlanDao.getConfirmPoItemplanListBygoodsId(id);
			Double number = 0.00;
			if(orderItemPlanList != null && orderItemPlanList.size() != 0 ){
				for(PurchaseOrderItemPlanEntity  orderItemPlan : orderItemPlanList){
					number = BigDecimalUtil.add(number, orderItemPlan.getOrderQty());
				}
			}
			if(goods != null){
				if(number == 0.00){
					goods.setVendorConfirmStatus(0);//未确认
				}else if(   BigDecimalUtil.sub(Double.valueOf( goods.getDhsl()),number)  > 0){
					goods.setVendorConfirmStatus(2);//部分确认
				}else if(BigDecimalUtil.sub(Double.valueOf( goods.getDhsl()),number) == 0){
					goods.setVendorConfirmStatus(1);//已确认
				}
				purchaseGoodsRequestDao.save(goods);
			}
		}
	//订单单位转基本单位
		public Double orderQtyToBaseQty(String fenzi ,String fenmu ,Double qty ){
			Double ty = qty;
			if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
				Double   fennzi  =  Double.valueOf(fenzi);//分子
				Double   fennmu  =  Double.valueOf(fenmu);  //分母
				ty =BigDecimalUtil.div(  BigDecimalUtil.mul(qty, fennmu, 3)  , fennzi, 3) ;
			}
			return ty;
		}
		
		//基本单位转转订单单位
		public Double baseQtyToOrderQty(String fenzi ,String fenmu ,Double qty ){
			Double ty = qty;
			if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
				Double   fennzi  =  Double.valueOf(fenzi);//分子
				Double   fennmu  =  Double.valueOf(fenmu);  //分母
				ty = BigDecimalUtil.mul( qty, BigDecimalUtil.div(fennzi, fennmu, 3) ,3);
			}
			return ty;
		}

		
		/**
		 * 根据补货的ASN号获取所有补货明细
		 * @param asnCode
		 * @return
		 */
		public List<ReplishmentVO> getReplishmentVOs(String asnCode){
			DeliveryEntity replDelivery = deliveryDao.findDeliveryEntityByCode(asnCode);
			DeliveryItemEntity sourceDeliveryItem;
			List<ReplishmentVO> vos = null;
			ReplishmentVO vo;
			PurchaseOrderItemEntity replOrderItem;
			if(null!=replDelivery){
				Set<DeliveryItemEntity> set = replDelivery.getDeliveryItem();
				vos = new ArrayList<ReplishmentVO>();
				for (DeliveryItemEntity dlvItem : set) {
					vo = new ReplishmentVO();
					replOrderItem=dlvItem.getOrderItem();
					vo.setBOLNR(replDelivery.getDeliveryCode());	// ASN单号
					vo.setEBELP(replOrderItem.getItemNo()+"");//ASN单号行项目
					//////原數據///////
					sourceDeliveryItem=deliveryItemDao.findDeliveryItemByDnAndShipType(dlvItem.getDn(), PurchaseConstans.SHIP_TYPE_NORMAL);
				    vo.setSOURCE_BOLNR(sourceDeliveryItem.getDelivery().getDeliveryCode());// 原ASN单号
				    vo.setVBELN(sourceDeliveryItem.getDn());// 原交货单号（DN）
				   //////////////// 
				    vo.setMATNR(replOrderItem.getMaterialCode());// 物料号
				    vo.setMAKTX(replOrderItem.getMaterialName());// 物料描述
				    vo.setLIFNR(replDelivery.getVendor().getCode());// 供应商账号
				    vo.setEBELN(replOrderItem.getOrder().getOrderCode());// 采购订单号
				    vo.setLFIMG(null==dlvItem.getDeliveryQty()?"":dlvItem.getDeliveryQty()+"");// 补货数量
				    vo.setMEINS(replOrderItem.getMeins());// 采购订单单位
				    vo.setCHARG(dlvItem.getCharg());// 批号
				    vo.setHSDAT(null==dlvItem.getManufactureDate()?"":DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));//生产日期
				    vo.setVERSION(dlvItem.getVersion());//版本
				    vos.add(vo);
				}
			}
			return vos;
		}
		
		
		
		/**
		 * 更新原计划的在途数量，未发货数量
		 * @param receiveItemEntity
		 * @param orderPlan
		 */
		public void updateSourcePlanQty(PurchaseOrderItemPlanEntity orderPlan){
			  ///原供货计划更新///////////
			       //在途=发货（普通的发货+补货的发货）-收货
					List<Long> planIds = new ArrayList<Long>();
					if(PurchaseConstans.SHIP_TYPE_NORMAL == orderPlan.getShipType()){
						//【1】获取该供货计划下的补货id
						List<Long> replPlanIds=purchaseOrderItemPlanDao.findReplPlanIdsBySourcePlanId(orderPlan.getId());
						if(null!=replPlanIds && replPlanIds.size()>0){
							planIds.addAll(replPlanIds);
						}
						//【2】增加普通供货计划id
						planIds.add(orderPlan.getId());
					}else{
						//是补货
						//[1]增加补货
						planIds.add(orderPlan.getId());
						//[2]增加原计划id
						planIds.add(orderPlan.getSourceOrderItemPlanId());
						orderPlan=purchaseOrderItemPlanDao.findById(orderPlan.getSourceOrderItemPlanId());
						
					}
					//【3】根据所有id查发货数量
					Double hasDeliveryQty=deliveryItemDao.findHasDlvQtyByPlanIds(planIds);
					hasDeliveryQty=null==hasDeliveryQty?0:hasDeliveryQty;
					//【3】收货数量
					Double receiveQty =recItemDao.getReceiveQtyByPlanIds(planIds);
					receiveQty=null==receiveQty?0:receiveQty;
					//【4】在途=发货（普通的发货+补货的发货）-收货
					Double onWayQty = BigDecimalUtil.sub(null==hasDeliveryQty?0:hasDeliveryQty, receiveQty);
					Double replHasRecQty =deliveryItemDao.findHasDlvQtyRecByPlanId(orderPlan.getId());
					onWayQty=BigDecimalUtil.sub(onWayQty, null==replHasRecQty?0:replHasRecQty);
					orderPlan.setOnwayQty(onWayQty);
					
					//【5】未发数量=orderQty(普通的订单数量+普通的订单数量)-deliveryQty(普通+补货)-toDeliveryQty(普通+补货)
					Double orderQty=purchaseOrderItemPlanDao.findOrderQtyByIds(planIds);
					orderQty=null==orderQty?0:orderQty;
					Double deliveryQty=purchaseOrderItemPlanDao.findDeliveryQtyByIds(planIds);
					deliveryQty=null==deliveryQty?0:deliveryQty;
					Double toDeliveryQty=purchaseOrderItemPlanDao.findToDeliveryQtyByIds(planIds);
					toDeliveryQty=null==toDeliveryQty?0:toDeliveryQty;
					Double undeliveryQty = BigDecimalUtil.sub(orderQty,BigDecimalUtil.add(deliveryQty, toDeliveryQty));
					orderPlan.setUndeliveryQty(undeliveryQty);
					purchaseOrderItemPlanDao.save(orderPlan);
			     ///原供货计划更新end///////////	
					
				////原订单明细更新//////////////
					PurchaseOrderItemEntity orderItem=orderPlan.getOrderItem();
					//【6】在途=发货（普通的发货+补货的发货）-收货
					Double orderItemHasDeliveryQty=deliveryItemDao.findAllHasDlvQtyByOrderItemId(orderItem.getId());
					orderItemHasDeliveryQty=null==orderItemHasDeliveryQty?0:orderItemHasDeliveryQty;
					Double orderItemReceiveQty=recItemDao.getOrderItemReceiveQty(orderItem.getId());
					orderItemReceiveQty=null==orderItemReceiveQty?0:orderItemReceiveQty;
					Double orderItemOnwayQty=BigDecimalUtil.sub(orderItemHasDeliveryQty,orderItemReceiveQty);
					//补货的收货数量
					Double orderItemReplHasRecQty =deliveryItemDao.findHasDlvQtyRecByItemId(orderItem.getId());
					orderItemOnwayQty=BigDecimalUtil.sub(orderItemOnwayQty,null==orderItemReplHasRecQty?0:orderItemReplHasRecQty);
					orderItem.setOnwayQty(orderItemOnwayQty);
					
					//【7】未发数量=orderQty(orderQty+退货数量)-deliveryQty(普通+补货)-toDeliveryQty(普通+补货)
					Double orderItemOrderQty=BigDecimalUtil.add(null==orderItem.getOrderQty()?0:orderItem.getOrderQty(), null==orderItem.getReturnQty()?0:orderItem.getReturnQty());
					orderItemOrderQty=null==orderItemOrderQty?0:orderItemOrderQty;
					Double orderItemDeliveryQty = purchaseOrderItemPlanDao.findDeliveryQtyByOrderItemId(orderItem.getId());
					orderItemDeliveryQty=null==orderItemDeliveryQty?0:orderItemDeliveryQty;
					Double orderItemToDeliveryQty = purchaseOrderItemPlanDao.findToDeliveryQtyByOrderItemId(orderItem.getId());
					orderItemToDeliveryQty=null==orderItemToDeliveryQty?0:orderItemToDeliveryQty;
					Double orderItemUndeliveryQty=BigDecimalUtil.sub(orderItemOrderQty, BigDecimalUtil.add(orderItemDeliveryQty, orderItemToDeliveryQty));
					orderItem.setUndeliveryQty(orderItemUndeliveryQty);
					purchaseOrderItemDao.save(orderItem);
			   ////原订单明细更新end///////////
		}
		
		
		//判断原供货计划下面是否有补货
		public Integer findShipTypeBySourceDn(String dn){
			if(StringUtils.isNotEmpty(dn)){
				List<DeliveryItemEntity> replPlanIds=deliveryItemDao.findReplDeliveryItemByDn(dn);
				if(null!=replPlanIds && replPlanIds.size()>0){
					return PurchaseConstans.SHIP_TYPE_REPL;
				}
			}
			return PurchaseConstans.SHIP_TYPE_NORMAL;
		}
		
		
		/**
		 * 根据map获取发货单列表
		 * @param pageNumber
		 * @param pageSize
		 * @param searchParamMap
		 * @return
		 */
		public Page<DeliveryItemEntity> getDeliveryItemsByMap(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
			PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
			Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
			Specification<DeliveryItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), DeliveryItemEntity.class);
			return deliveryItemDao.findAll(spec,pagin);
		}
		
}
