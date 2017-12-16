package com.qeweb.scm.purchasemodule.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemEntity;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemPlanEntity;
import com.qeweb.scm.purchasemodule.repository.GoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.GoodsRequestItemDao;
import com.qeweb.scm.purchasemodule.repository.GoodsRequestItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.InventoryDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanItemDao;
import com.qeweb.scm.purchasemodule.repository.WmsInventoryDao;
import com.qeweb.scm.purchasemodule.web.vo.GoodsRequestTransfer;



@Service
@Transactional
public class GoodsRequestService extends BaseService {

	@Autowired
	private GoodsRequestDao goodsRequestDao;
	
	@Autowired
	private GoodsRequestItemDao goodsRequestItemDao;
	
	@Autowired
	private GoodsRequestItemPlanDao goodsRequestItemPlanDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private InventoryDao inventoryDao;

	@Autowired
	private WmsInventoryDao wmsInventoryDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchasePlanItemDao purchasePlanItemDao;
	/**
	 * 获取订单列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<GoodsRequestEntity> getGoodsRequests(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");  
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<GoodsRequestEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), GoodsRequestEntity.class);
		return goodsRequestDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取发货方
	 * @param orgId
	 * @param roleType
	 * @return
	 */
	public List<OrganizationEntity> getSenders(long orgId, Integer roleType) {
		return organizationDao.findByIdOrRoleType(orgId, roleType);
	}
	
	public GoodsRequestEntity getRequest(Long id) {
		return goodsRequestDao.findOne(id);
	}
	
	public GoodsRequestEntity createRequest() {
		GoodsRequestEntity req = new GoodsRequestEntity();
		req.setRequestCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.REQUEST));
		return req;
	}
	
	/**
	 * 获取订单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<GoodsRequestItemEntity> getGoodsRequestItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<GoodsRequestItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), GoodsRequestItemEntity.class);
		return goodsRequestItemDao.findAll(spec,pagin);
	}
	
	/**
	 * 根据物料获取库存数量
	 * @param mId
	 * @param type 0:vmi  1:wms
	 * @return
	 */
	public BigDecimal getStockQtyByMaterialId(Long mId,String type){
		BigDecimal d = null;
		if("0".equals(type)){
			d = inventoryDao.findStockQtyByMaterial(mId);
		}
		if("1".equals(type))
			d = wmsInventoryDao.findStockQtyByMaterial(mId);
		return d;
	}
	
	/**
	 * 根据供应商和物料id查询采购量
	 * @param vendorId
	 * @param id
	 * @return
	 */
	public BigDecimal findOrderQtyByVendorAndMaterial(Long vendorId, Long id) {
		BigDecimal d = purchaseOrderItemDao.findOrderQtyByVendorAndMaterial(vendorId, id);
		return d;
	}
	
	
	/**
	 * 根据物料查询计划采购量
	 * @param id
	 * @return
	 */
	public BigDecimal findTotalPlanQtyByMaterial(Long id){
		BigDecimal d = purchasePlanItemDao.findPlanQtyByMaterial(id);
		return d;
	}
	
	/**
	 * 发布单个订单
	 * @param order
	 */
	public void publishGoodsRequest(GoodsRequestEntity order) {
		order.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES); 
		goodsRequestDao.save(order);
	}
	
	/**
	 * 批量发布订单
	 * @param goodsrequests
	 */
	public void publishGoodsRequests(List<GoodsRequestEntity> goodsrequests, UserEntity user) {
		for(GoodsRequestEntity request : goodsrequests) {
			request.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			request.setPublishTime(DateUtil.getCurrentTimestamp());
			request.setPublishUser(user); 
		}
		goodsRequestDao.save(goodsrequests);
	}
	
	/**
	 * 确认订单
	 * @param goodsrequests
	 */
	public void confirmGoodsRequests(List<GoodsRequestEntity> goodsrequests, UserEntity user) {
		for(GoodsRequestEntity request : goodsrequests) {
			request.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			request.setConfirmUser(user);
			request.setConfirmTime(DateUtil.getCurrentTimestamp());
			//设置明细确认状态
			confirmOrderItems(request, user);
		}
		goodsRequestDao.save(goodsrequests);
	}
	
	public void closeGoodsRequests(List<GoodsRequestEntity> goodsrequests, UserEntity user) {
		for(GoodsRequestEntity request : goodsrequests) {
			request.setCloseStatus(PurchaseConstans.CLOSE_STATUS_YES);
			request.setCloseUser(user);
			request.setConfirmTime(DateUtil.getCurrentTimestamp()); 
		}
		goodsRequestDao.save(goodsrequests);
	}
	
	/**
	 * 合并保存采购订单
	 * @param list
	 * @param logger
	 * @throws NoSuchMethodException  
	 * @throws InvocationTargetException    
	 * @throws IllegalAccessException    
	 */
	public boolean combineGoodsRequest(List<GoodsRequestTransfer> goodsrequestTranList, ILogger logger) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<String, OrganizationEntity> orgMap = new HashMap<String, OrganizationEntity>();
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		Map<String, UserEntity> userMap = new HashMap<String, UserEntity>();
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<GoodsRequestTransfer> list = validateTransfers(goodsrequestTranList, orgMap, materialMap, userMap, counts, logger);
		if(list.size() != goodsrequestTranList.size())  
			return false;
		
		logger.log("\n->正在准备合并主数据与明细数据\n");
		GoodsRequestEntity goodsRequest = null;
		GoodsRequestItemEntity goodsRequestItem = null;
		GoodsRequestItemPlanEntity goodsRequestItemPlan = null;
		Map<String, GoodsRequestEntity> tmpMap = new HashMap<String, GoodsRequestEntity>();
		List<GoodsRequestItemEntity> goodsRequestItemList = new ArrayList<GoodsRequestItemEntity>(); 
		List<GoodsRequestItemPlanEntity> goodsRequestItemPlanList = new ArrayList<GoodsRequestItemPlanEntity>();
		String key = null;
		for(GoodsRequestTransfer trans : list) {
			key = trans.getRequestCode() + ";" + trans.getBuyerCode() + ";" + trans.getVendorCode();
			if(!tmpMap.containsKey(key)) {
				goodsRequest = getGoodsRequestEntity(trans.getRequestCode(), trans.getBuyerCode(), trans.getVendorCode());
				goodsRequest.setRequestCode(trans.getRequestCode());
				goodsRequest.setBuyer(orgMap.get(trans.getBuyerCode()));
				goodsRequest.setPurchaseUser(userMap.get(trans.getPurchaseUserCode())); 
				goodsRequest.setVendor(orgMap.get(trans.getVendorCode()));
				goodsRequest.setReceiveOrg(trans.getReceiveOrg()); 
				goodsRequest.setOrderDate(DateUtil.stringToTimestamp(trans.getOrderDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
				goodsRequest.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				goodsRequest.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				goodsRequest.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
				goodsRequest.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
				goodsRequest.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
				tmpMap.put(key, goodsRequest);
			} else {
				goodsRequest = tmpMap.get(key);
			}
			//设置明细单表信息
			goodsRequestItem = getGoodsRequestItemEntity(goodsRequest, trans.getItemNo());
			goodsRequestItem.setRequest(goodsRequest);
			goodsRequestItem.setMaterial(materialMap.get(trans.getMaterialCode()));
			goodsRequestItem.setItemNo(StringUtils.convertToInteger(trans.getItemNo()));
			goodsRequestItem.setReceiveOrg(trans.getReceiveOrg());
			goodsRequestItem.setOrderQty(StringUtils.convertToDouble(trans.getOrderQty()));
			goodsRequestItem.setRequestTime(DateUtil.stringToTimestamp(trans.getRequestTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			goodsRequestItem.setCurrency(trans.getCurrency());
			goodsRequestItem.setUnitName(trans.getUnitName());
			goodsRequestItem.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			goodsRequestItem.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			goodsRequestItem.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			goodsRequestItemList.add(goodsRequestItem);
			//采购计划
			goodsRequestItemPlan = getPurchaseItemPlan(goodsRequestItem);
			long id = goodsRequestItemPlan.getId();
			PropertyUtils.copyProperties(goodsRequestItemPlan, goodsRequestItem);
			if(id == 0l) {
				goodsRequestItemPlan.setReceiveQty(0d);
				goodsRequestItemPlan.setReturnQty(0d); 
				goodsRequestItemPlan.setToDeliveryQty(0d); 
				goodsRequestItemPlan.setDeliveryQty(0d); 
				goodsRequestItemPlan.setDiffQty(0d); 
			}
			goodsRequestItemPlan.setId(id);
			goodsRequestItemPlan.setRequestItem(goodsRequestItem);  
			goodsRequestItemPlanList.add(goodsRequestItemPlan); 
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + tmpMap.size() + "]条有效的数据";
		logger.log(logMsg);
		//保存
		goodsRequestDao.save(tmpMap.values());
		goodsRequestItemDao.save(goodsRequestItemList);    
		goodsRequestItemPlanDao.save(goodsRequestItemPlanList);
		return true;	
	}

	private List<GoodsRequestTransfer> validateTransfers(List<GoodsRequestTransfer> goodsrequestTranList, Map<String, OrganizationEntity> orgMap, Map<String, MaterialEntity> materialMap, 
			Map<String, UserEntity> userMap, Integer[] counts, ILogger logger) {
		String logMsg = "->现在对导入的要货单信息进行预处理,共有[" + (goodsrequestTranList == null ? 0 : goodsrequestTranList.size()) + "]条数据";
		logger.log(logMsg);
		counts[0] = goodsrequestTranList.size();
		List<GoodsRequestTransfer> ret = new ArrayList<GoodsRequestTransfer>();
		
		List<OrganizationEntity> orgList = null;
		List<MaterialEntity> materialList = null;
		UserEntity purchaseUser = null;
		boolean lineValidat = true;
		int index  = 2; 
		for(GoodsRequestTransfer trans : goodsrequestTranList) {
			if(!userMap.containsKey(trans.getPurchaseUserCode())) {
				purchaseUser = userDao.findByLoginName(trans.getPurchaseUserCode());
				if(purchaseUser == null) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],采方代码[" + trans.getBuyerCode() + "]未在系统中维护,忽略此要货明细");
				} else {
					userMap.put(trans.getPurchaseUserCode(), purchaseUser);
				}
			}
			if(!orgMap.containsKey(trans.getBuyerCode())) {
				orgList = organizationDao.findByCode(trans.getBuyerCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],采方代码[" + trans.getBuyerCode() + "]未在系统中维护,忽略此要货明细");
				} else {
					orgMap.put(trans.getBuyerCode(), orgList.get(0));
				}
			}
			
			if(!orgMap.containsKey(trans.getVendorCode())) {
				orgList = organizationDao.findByCode(trans.getVendorCode());
				if(CollectionUtils.isEmpty(orgList)) {
					lineValidat = false;
					logger.log("->[FAILED]行索引[" + (index) + "],供方代码[" + trans.getVendorCode() + "]未在系统中维护,忽略此要货明细");
				} else {
					orgMap.put(trans.getVendorCode(), orgList.get(0));
				}
			}
			
			if(!materialMap.containsKey(trans.getMaterialCode())) {
				materialList = materialDao.findByCode(trans.getMaterialCode());
				if(CollectionUtils.isEmpty(materialList)) {
					lineValidat = false;
					logger.log(logMsg = "->[FAILED]行索引[" + (index) + "],物料(" + trans.getMaterialCode() + "], 不存在,忽略此要货明细");
				} else {
					materialMap.put(trans.getMaterialCode(), materialList.get(0));
				}
			}
			if(lineValidat) {
				logMsg = "[SUCCESS]行索引[" + (index) + "],预处理[" + trans.getVendorCode() + "|" + trans.getMaterialCode() + "|" + trans.getOrderDate() + "]成功。";
				logger.log(logMsg);
				ret.add(trans);
			}
			index ++;
			lineValidat = true;
		}
		counts[1] = ret.size();
		logMsg = "<-导入的要货单主信息预处理完毕,共有[" + ret.size() + "]条有效数据";
		logger.log(logMsg);
		return ret;   
	}

	private GoodsRequestItemPlanEntity getPurchaseItemPlan(GoodsRequestItemEntity purchaseOrderItem) {
		if(purchaseOrderItem.getId() == 0l)
			return new GoodsRequestItemPlanEntity(); 
		
		List<GoodsRequestItemPlanEntity> itemplanList = goodsRequestItemPlanDao.findGoodsRequestItemPlanEntityByRequestItem(purchaseOrderItem);
		return CollectionUtils.isEmpty(itemplanList) ? new GoodsRequestItemPlanEntity() : itemplanList.get(0);
	}

	private GoodsRequestItemEntity getGoodsRequestItemEntity(GoodsRequestEntity purchaseorder, String itemNo) {
		if(purchaseorder.getId() == 0l)
			return new GoodsRequestItemEntity();
		
		List<GoodsRequestItemEntity> item = goodsRequestItemDao.findGoodsRequestItemEntityByRequestAndItemNo(purchaseorder, StringUtils.convertToInteger(itemNo));
		return CollectionUtils.isEmpty(item) ? new GoodsRequestItemEntity() : item.get(0); 
	}

	private GoodsRequestEntity getGoodsRequestEntity(String orderCode, String buyerCode, String vendorCode) {
		GoodsRequestEntity order = goodsRequestDao.findGoodsRequestEntityByCode(orderCode, buyerCode, vendorCode);
		return order == null ?  new GoodsRequestEntity() : order;
	}
	
	/**
	 * 确认要货明细行
	 * @param order
	 */
	public void confirmOrderItems(GoodsRequestEntity order, UserEntity user) {
		List<GoodsRequestItemEntity> items = goodsRequestItemDao.findGoodsRequestItemEntityByRequestAndItemNo(order, null);
		if(CollectionUtils.isEmpty(items))
			return;
		
		for(GoodsRequestItemEntity item : items) {
			item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			item.setConfirmUser(user);
			item.setConfirmTime(DateUtil.getCurrentTimestamp()); 
			confirmOrderItemPlans(item, user);
		}
		goodsRequestItemDao.save(items);
	}
	
	/**
	 * 确认要货供货计划
	 * @param item
	 */
	private void confirmOrderItemPlans(GoodsRequestItemEntity item, UserEntity user) {
		List<GoodsRequestItemPlanEntity> plans = goodsRequestItemPlanDao.findGoodsRequestItemPlanEntityByRequestItem(item);
		if(CollectionUtils.isEmpty(plans))
			return;
		
		for(GoodsRequestItemPlanEntity itemPlan : plans) {
			itemPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
			itemPlan.setConfirmUser(user);
			itemPlan.setConfirmTime(DateUtil.getCurrentTimestamp()); 
		}
		goodsRequestItemPlanDao.save(plans);
	}
	
	/**
	 * 根据id获得详细要货单
	 */
	public GoodsRequestItemEntity getRequestItem(Long id) {
		return goodsRequestItemDao.findOne(id);
	}
	
	/**
	 * 添加新的要货单
	 * @param goodsRequestItemList 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	public void addNewGoodRequest(GoodsRequestEntity goodsRequest, List<GoodsRequestItemEntity> goodsRequestItemList) throws Exception {
		goodsRequestDao.save(goodsRequest);	
		goodsRequestItemDao.save(goodsRequestItemList);
		List<GoodsRequestItemPlanEntity> itemPlanList=new ArrayList<GoodsRequestItemPlanEntity>();
		GoodsRequestItemPlanEntity itemPlan = null;
		for(GoodsRequestItemEntity item : goodsRequestItemList) {
			itemPlan = new GoodsRequestItemPlanEntity();
			PropertyUtils.copyProperties(itemPlan, item);
			itemPlan.setRequestItem(item);
			itemPlan.setReceiveQty(0d);
			itemPlan.setReturnQty(0d); 
			itemPlan.setToDeliveryQty(0d); 
			itemPlan.setDeliveryQty(0d); 
			itemPlan.setDiffQty(0d);   
			itemPlanList.add(itemPlan);
		}
		goodsRequestItemPlanDao.save(itemPlanList);
	}
	
	/**
	 * 修改要货单
	 */
	public void saveGoodsRequest(GoodsRequestEntity goodsRequest, List<GoodsRequestItemEntity> goodsRequestItemList, UserEntity userEntity) {
		OrganizationEntity sender = new OrganizationEntity();
		sender.setId(goodsRequest.getSenderId());
		GoodsRequestEntity entity = goodsRequestDao.findOne(goodsRequest.getId());
		entity.setSender(sender);
		goodsRequestDao.save(entity);
		goodsRequestItemDao.save(goodsRequestItemList);
	}

	public List<OrganizationEntity> getReceivers(Integer roleType) {
		return organizationDao.findByRoleType(roleType);
	}
}
