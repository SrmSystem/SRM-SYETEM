package com.qeweb.scm.purchasemodule.web.receive;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.FileDescriptEntity;
import com.qeweb.scm.purchasemodule.entity.FileUpEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.FileDescriptService;
import com.qeweb.scm.purchasemodule.service.FileUpService;
import com.qeweb.scm.purchasemodule.service.PurchaseMainOrderService;
import com.qeweb.scm.purchasemodule.service.PurchaseOrderItemService;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.vo.ReceiveTransfer;
import com.qeweb.scm.sap.service.OrderReceiveSyncService;

/**
 * 采购收货单管理
 * @author alex
 * @date 2015年5月4日
 * @path com.qeweb.scm.purchasemodule.web.delivery.ReceiveController.java
 */
@Controller
@RequestMapping(value = "/manager/order/receive")
public class ReceiveController implements ILog {
	
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private PurchaseOrderItemService orderItemService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private FileDescriptService fileDescriptService;
	
	@Autowired
	private FileUpService fileUpService;
	
	@Autowired
	private PurchaseMainOrderService mainOrderService;
	
	@Autowired
	private DictItemService dictItemService;
	
	/**
	 * 收货看板
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pending", method = RequestMethod.GET)
	public String pendingList(Model model) {
		return "back/receive/receivePendingList";
	}
	
	@RequestMapping("sycOrder/{timeS}/{timeE}")
	@ResponseBody
	public Map<String,Object> sycOrder(@PathVariable("timeS")String timeS,@PathVariable("timeE")String timeE) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    Date dateS = sdf.parse(timeS); 
	    Date dateE = sdf.parse(timeE); 
		OrderReceiveSyncService orderReceiveSyncService = SpringContextUtils.getBean("orderReceiveSyncService");	
		boolean isSuccess = orderReceiveSyncService.execute(logger,DateUtil.dateToString(dateS, "yyyyMMdd"),DateUtil.dateToString(dateE, "yyyyMMdd"));
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP同步失败");
			map.put("success", false);
		}
		return map;
	}
	
	//@RequiresPermissions("rec:pending:view")
	@RequestMapping(value="pending", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPendingList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_deliveryStatus", PurchaseConstans.DELIVERY_STATUS_YES + "");
		//add by chao.gu 20161226 默认是未收货
		if(null==searchParamMap.get("EQ_receiveStatus")){
			searchParamMap.put("EQ_receiveStatus", PurchaseConstans.RECEIVE_STATUS_NO + "");
		}
		//add end
		//组织权限
		//searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		
		//采购组权限
		searchParamMap.put("IN_purchasingGroupId", buyerOrgPermissionUtil.getGroupIds());
		Page<DeliveryEntity> userPage = deliveryService.getDeliverys(pageNumber,pageSize,searchParamMap);
		userPage = deliveryService.initData(userPage);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 收货记录
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(0 == user.orgRoleType.intValue()){//采购商
			model.addAttribute("vendor", false);
		}else{
			model.addAttribute("vendor", true);
		}
		return "back/receive/receiveList";
	}
	
	//@RequiresPermissions("rec:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//用户权限
		//searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
		//组织权限
		//searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(0 == user.orgRoleType.intValue()){//采购商
			//采购组权限
			searchParamMap.put("IN_purchasingGroupId", buyerOrgPermissionUtil.getGroupIds());
		}else{//供应商
			searchParamMap.put("EQ_vendor.id", user.orgId + "");    
		}
		searchParamMap.put("IN_purchasingGroupId", buyerOrgPermissionUtil.getGroupIds());
		Page<ReceiveEntity> userPage = receiveService.getReceives(pageNumber,pageSize,searchParamMap);
		userPage = receiveService.initData(userPage);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取收货主单信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getReceive/{id}")
	@ResponseBody
	public ReceiveEntity getDelivery(@PathVariable("id") Long id){
		return receiveService.getReceiveById(id);
	}
	
	/**
	 * 查看收货单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "receiveitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long receiveid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_receive.id", receiveid + "");
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				item.setDeliveryItemId(deliveryItemId);
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
				item.setDn(deliveryItem.getDn());
				item.setDeliveryCode(deliveryItem.getDelivery().getDeliveryCode());
				long orderItemId = deliveryItem.getOrderItem().getId();
				PurchaseOrderItemEntity orderItemEntity = orderItemService.getItemById(orderItemId);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setMaterialDescribe(deliveryItem.getMaterial().getDescribe());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
				item.setDeliveryCreateTime(deliveryItem.getDelivery().getCreateTime());
				item.setPublishTime(deliveryItem.getDelivery().getDeliveyTime());
				PurchaseOrderItemPlanEntity plan=orderItemService.getItemPlanById(deliveryItem.getOrderItemPlan().getId());
				item.setUnitName(plan.getUnitName());
				//来料不良率zzjbl zllbl
				if(item.getZzjbl() != null && item.getZllbl() != null && deliveryItem.getDeliveryQty() != null && deliveryItem.getDeliveryQty()>0){
					BigDecimal rate = new BigDecimal(BigDecimalUtil.div( BigDecimalUtil.add(item.getZzjbl(), item.getZllbl()), deliveryItem.getDeliveryQty()) *100);
					item.setBadRate(rate.setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
				}
				if(orderItemEntity != null){
					item.setRquestTime(StringUtils.convertToString(orderItemEntity.getRequestTime()));
				}
				 //送货差异数量{ASN数量-AAC实际收货数量（AAC待检数量+质量不良数量+送检合格数量+来料不良数量）
				//发货-待检
				Double diffQty=BigDecimalUtil.sub(null==item.getDeliveryQty()?0:item.getDeliveryQty(), null==item.getZdjsl()?0:item.getZdjsl());
				//-质量不良
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZzjbl()?0:item.getZzjbl());
				//-送检合格
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZsjhg()?0:item.getZsjhg());
				//-来料不良
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZllbl()?0:item.getZllbl());
				item.setDiffQty(diffQty);
				
				Double onwayQty = mainOrderService.getOrderItemOnwayQty(orderItemEntity.getId());
				orderItemEntity.setOnwayQty(onwayQty);
				
				//判断是否有补货
				item.setShipType(deliveryService.findShipTypeBySourceDn(item.getDn()));
				item.setOrderItem(orderItemEntity);
				   
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 获得收货单详情
	 * @param itemId
	 * @param model
	 * @return
	 */
	@LogClass(method="获得收货单详情",module="收货管理")
	@RequestMapping(value="geReceiveDetailt/{id}", method = RequestMethod.GET)
	public String geReceiveDetailt(@PathVariable(value="id") Long id, Model model) {
		ReceiveEntity receive=new ReceiveEntity();
		receive=receiveService.getReceiveById(id);
		DeliveryEntity deliveryEntity=receive.getDelivery();
		
		DictItemEntity dictItem = dictItemService.findDictItemByCode(deliveryEntity.getTransportType());
		deliveryEntity.setTransportName(dictItem.getName());
		
		List<FileDescriptEntity> fileDescriptList = fileDescriptService.getAllEntity();

		for (FileDescriptEntity fileDescript : fileDescriptList) {
			FileUpEntity fileUp=fileUpService.getByDesriptIdAndReceiveId(fileDescript.getId(),id);
			if(fileUp!=null){
			fileDescript.setFilePath(fileUp.getFilePath());
			fileDescript.setFileName(fileUp.getFileName());
			}
		}
		model.addAttribute("re", receive);
		model.addAttribute("de", deliveryEntity);
		model.addAttribute("fd", fileDescriptList);
		return "back/receive/receiveListView";

	}
	
	@RequestMapping(value = "byorderreceiveitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByOrder(@PathVariable("id") Long orderid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.id", orderid + "");   
		searchParamMap.put("EQ_abolished", "0");   
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
				item.setDeliveryCode(deliveryItem.getDelivery().getDeliveryCode());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 通过主订单的id获取明细下面所有的收货单的信息
	 * @param  主订单ID
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "byMainOrderId/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByMainOrder(@PathVariable("id") Long mainOrderId, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.order.id", mainOrderId + "");   
		searchParamMap.put("EQ_abolished", "0");   
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
				item.setDeliveryCode(deliveryItem.getDelivery().getDeliveryCode());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	/**
	 * 通过要货计划id获取明细下面所有的收货单的信息
	 * @param  主订单ID
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "byRequestId")
	@ResponseBody
	public Map<String,Object> getItemListbyRequestId(
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq,
			@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_deliveryItem.request.flag", flag);
		searchParamMap.put("EQ_deliveryItem.request.factory.id", factoryId);
		searchParamMap.put("EQ_deliveryItem.request.group.id", purchasingGroupId);
		searchParamMap.put("EQ_deliveryItem.request.material.id", materialId);
		searchParamMap.put("EQ_deliveryItem.request.meins", meins);
		searchParamMap.put("EQ_deliveryItem.request.vendor.id", vendorId);
		searchParamMap.put("GTE_deliveryItem.request.rq", beginRq);
		searchParamMap.put("LTE_deliveryItem.request.rq", endRq);
		searchParamMap.put("EQ_abolished", "0");   
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
				item.setDn(deliveryItem.getDn());
				item.setDeliveryCode(deliveryItem.getDelivery().getDeliveryCode());
				long orderItemId = deliveryItem.getOrderItem().getId();
				PurchaseOrderItemEntity orderItemEntity = orderItemService.getItemById(orderItemId);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setMaterialDescribe(deliveryItem.getMaterial().getDescribe());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
				item.setDeliveryCreateTime(deliveryItem.getDelivery().getCreateTime());
				item.setPublishTime(deliveryItem.getDelivery().getDeliveyTime());
				PurchaseOrderItemPlanEntity plan=orderItemService.getItemPlanById(deliveryItem.getOrderItemPlan().getId());
				item.setUnitName(plan.getUnitName());
				//来料不良率zzjbl zllbl
				if(item.getZzjbl() != null && item.getZllbl() != null && deliveryItem.getDeliveryQty() != null && deliveryItem.getDeliveryQty()>0){
					BigDecimal rate = new BigDecimal(BigDecimalUtil.div( BigDecimalUtil.add(item.getZzjbl(), item.getZllbl()), deliveryItem.getDeliveryQty()) *100);
					item.setBadRate(rate.setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
				}
				if(orderItemEntity != null){
					item.setRquestTime(StringUtils.convertToString(orderItemEntity.getRequestTime()));
				}
				 //送货差异数量{ASN数量-AAC实际收货数量（AAC待检数量+质量不良数量+送检合格数量+来料不良数量）
				//发货-待检
				Double diffQty=BigDecimalUtil.sub(null==item.getDeliveryQty()?0:item.getDeliveryQty(), null==item.getZdjsl()?0:item.getZdjsl());
				//-质量不良
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZzjbl()?0:item.getZzjbl());
				//-送检合格
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZsjhg()?0:item.getZsjhg());
				//-来料不良
				diffQty=BigDecimalUtil.sub(diffQty,null==item.getZllbl()?0:item.getZllbl());
				item.setDiffQty(diffQty);
				
				Double onwayQty = mainOrderService.getOrderItemOnwayQty(orderItemEntity.getId());
				orderItemEntity.setOnwayQty(onwayQty);
				item.setOrderItem(orderItemEntity);
				   
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	
	
	
	
	/**
	 * 收货
	 * @param deliveryId 发货单ID
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "doreceivesingle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveReceive(@RequestParam("deliveryId") long deliveryId,@RequestParam(value="checkdId", required=false) String checkedIds, 
			@RequestParam("receiveUser") String receiveUser,@RequestParam("receiveTime") String receiveTime,@RequestParam(value="datas") String datas,
			@RequestParam("files") MultipartFile[] files) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ReceiveEntity receive = new ReceiveEntity();
		receive.setReceiveCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.RECEIVE));
		receive.setAttr8(receiveUser);  //收货人
		Timestamp _receiveTime = DateUtil.stringToTimestamp(receiveTime,DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		if(_receiveTime == null){
			_receiveTime = DateUtil.getCurrentTimestamp();
		}
		receive.setReceiveTime(_receiveTime); //收货时间
		List<ReceiveItemEntity> receiveItem = new ArrayList<ReceiveItemEntity>();
		ReceiveItemEntity item = null;
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");  
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new ReceiveItemEntity();
//			item.setReceive(receive); 
			item.setReceiveQty(StringUtils.convertToDouble(object.get("toreceiveQty") + ""));
			item.setReturnQty(StringUtils.convertToDouble(object.get("toreturnQty") + ""));
			item.setInStoreQty(BigDecimalUtil.sub(item.getReceiveQty(), item.getReturnQty())); //入库=收货-验退
			item.setDeliveryItemId(StringUtils.convertToLong(object.get("id") + "")); 
			item.setAttr30("0");//未对账
			item.setItemNo(StringUtils.convertToInt(object.get("itemNo") + ""));
			receiveItem.add(item);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		receiveService.saveReceive(deliveryId, receive, receiveItem, new UserEntity(user.id));
		
		//上传附件
		String[] checkdId = null;
		if(StringUtils.isNotEmpty(checkedIds)){ 
			checkdId=checkedIds.split(",");
		}
		// 用作绩效的附件
		String filesPath = "";
		String filesName = "";
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (file == null || StringUtils.isEmpty(file.getOriginalFilename()))
					continue;

				File savefile = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
				filesPath += savefile.getPath() + ",";
				filesName += file.getOriginalFilename() + ",";
			}
		}
		String[] filesPath1 = filesPath.split(",");
		String[] filesName1 = filesName.split(",");
		receiveService.sendReceiveFiles(filesPath1, filesName1, checkdId, receive);
		
		
		map.put("message", "保存收货单成功");  
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 批量收货
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "doreceive",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doReceives(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		receiveService.doReceives(deliveryList, new UserEntity(user.id));
		map.put("message", "批量收货成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 导入收货单
	 * @param files
	 * @return
	 */
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) {
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(ReceiveTransfer.class), getLogger()); 
			List<ReceiveTransfer> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = receiveService.saveReceives(list, getLogger());
			if(flag) {
				map.put("msg", "导入收货单成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入收货单失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));    
		}
		return map;   
	}

	@Override
	public void log(Object message) {
		getLogger().log(message); 
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	
}
