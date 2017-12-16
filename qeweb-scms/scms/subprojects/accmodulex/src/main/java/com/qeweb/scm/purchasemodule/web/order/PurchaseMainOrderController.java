package com.qeweb.scm.purchasemodule.web.order;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.context.MessageSourceUtil;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.FeedbackService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.service.PurchaseMainOrderService;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderItemTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;
import com.qeweb.scm.purchasemodule.web.vo.VetoVO;
import com.qeweb.scm.sap.service.PurchaseOrderSyncService;

/**
 * 采购订单主单Controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/order/purchasemainorder")
public class PurchaseMainOrderController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PurchaseMainOrderService mainOrderService;
	
	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;	
	
	@Autowired
	private BacklogService backlogService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		model.addAttribute("vendor", false);
		model.addAttribute("extended", getDynamicDataService().getDynamicData("purchaseOrderEntity"));
		return "back/orderMain/order/orderList";  
	}
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		model.addAttribute("vendor", true);
		return "back/orderMain/order/orderList";
	}
	
	
	/**
	 * 采购订单列表查询[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		String sort = request.getParameter("sort");
		String orderby = request.getParameter("order");
		sort = "confirmStatus,createTime";
		orderby="asc,asc";
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
					String backlogId = searchParamMap.get("IN_backlogId").toString();
					List<Long> idList= backlogService.getBackLogValueIdsById(backlogId);
					searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
			//只显示已审核的采购订单
			searchParamMap.put("EQ_frgke", "R");
			
		}else{//采购商
		/*	//用户权限
			searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());*/
			//组织权限
			//searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
			//采购组权限
			searchParamMap.put("IN_purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		}
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_returnStatus", "0");
		Page<PurchaseOrderEntity> userPage = mainOrderService.getPurchaseOrders(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, orderby));
		
		//标识采购订单行有删除，冻结，锁定，交货已完成的添加红色标识
		for(PurchaseOrderEntity orderMain : userPage.getContent() ){
			Set<PurchaseOrderItemEntity> orderItemSet  =orderMain.getOrderItem();
			if(CommonUtil.isNotNullCollection(orderItemSet)){
				for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
					if(  !StringUtils.isEmpty( purchaseOrderItemEntity.getBstae())  || !StringUtils.isEmpty( purchaseOrderItemEntity.getLoekz() )  || !StringUtils.isEmpty( purchaseOrderItemEntity.getLockStatus()) || !StringUtils.isEmpty( purchaseOrderItemEntity.getZlock()) || !StringUtils.isEmpty( purchaseOrderItemEntity.getElikz())    ){
						orderMain.setIsRed("1");
						break;
					}
					if(purchaseOrderItemEntity.getConfirmStatus() == -1 || purchaseOrderItemEntity.getConfirmStatus() == -2){
						orderMain.setIsRed("1");
						break;
					}
				}
			}
		}
		
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 显示订单明细列表
	 * @param itemId 订单明细行
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemList/{orderId}/{vendor}", method = RequestMethod.GET)
	public String viewItemList(@PathVariable(value="orderId") Long orderId, @PathVariable(value="vendor") boolean vendor, Model model) {
		//通过id获取订单头的信息
		PurchaseOrderEntity  po = mainOrderService.getOrder(orderId);
		model.addAttribute("po", po);
		model.addAttribute("orderId", orderId);
		model.addAttribute("vendor", vendor);
		return "back/orderMain/order/orderItemList";
	}
	
	/**
	 * 采购订单明细列表查询[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getOrderItemList/{orderId}/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getOrderItemList(@PathVariable(value="orderId") Long orderId,@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		
		String zfree = searchParamMap.get("EQ_zfree")+"";
		String loekz = searchParamMap.get("EQ_loekz")+"";
		String elikz = searchParamMap.get("EQ_elikz")+"";
		
		if(zfree.equals("X")){
			searchParamMap.put("ISNOTNULL_zfree",searchParamMap);  
			searchParamMap.remove("EQ_zfree");
		}else if(zfree.equals("!X")){
			searchParamMap.put("ISNULL_zfree",searchParamMap);  
			searchParamMap.remove("EQ_zfree");
		}
		
		if(loekz.equals("X")){
			searchParamMap.put("ISNOTNULL_loekz",searchParamMap);  
			searchParamMap.remove("EQ_loekz");
		}else if(loekz.equals("!X")){
			searchParamMap.put("ISNULL_loekz",searchParamMap);  
			searchParamMap.remove("EQ_loekz");
		}
		
		if(elikz.equals("X")){
			searchParamMap.put("ISNOTNULL_elikz",searchParamMap);  
			searchParamMap.remove("EQ_elikz");
		}else if(elikz.equals("!X")){
			searchParamMap.put("ISNULL_elikz",searchParamMap);  
			searchParamMap.remove("EQ_elikz");
		}
		
		
		
		
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_order.vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
			//add by chao.gu 删除的不要显示
			searchParamMap.put("ISNULL_loekz",searchParamMap);
			//add end
		}else{//采购商
			//用户权限
			//searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
			//组织权限
			//searchParamMap.put("IN_order.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		}
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "itemNo";
			order = "asc";
		}
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("ISNULL_retpo","111");//具有“X”的为退货采购订单行项目，为空的为标准的采购订单
		searchParamMap.put("EQ_order.id", StringUtils.convertToString(orderId));
		Page<PurchaseOrderItemEntity> userPage = mainOrderService.getPurchaseOrderItems(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		for(PurchaseOrderItemEntity purchaseOrderItem  : userPage.getContent() ){
			String col1 = receiveService.getOrderItemZllblQty(purchaseOrderItem.getId())== null ? "" :   receiveService.getOrderItemZllblQty(purchaseOrderItem.getId()).toString() ;
		    String col2 = receiveService.getOrderItemZzjblQty(purchaseOrderItem.getId()) == null ? "" :   receiveService.getOrderItemZzjblQty(purchaseOrderItem.getId()).toString() ;
		    Double col3 = BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), Double.valueOf(purchaseOrderItem.getReceiveQty()));
		    Double onwayQty = mainOrderService.getOrderItemOnwayQty(purchaseOrderItem.getId());
			Double undeliveryQty = mainOrderService.getOrderItemUndeliveryQty(purchaseOrderItem.getId());
		    Double receiveQty=mainOrderService.getOrderItemReceiveQty(purchaseOrderItem.getId());
			purchaseOrderItem.setCol1(col1);
			purchaseOrderItem.setCol1(col2);
			purchaseOrderItem.setCol3(col3.toString());
			purchaseOrderItem.setOnwayQty(onwayQty);
			purchaseOrderItem.setUndeliveryQty(undeliveryQty);
			purchaseOrderItem.setReceiveQty(receiveQty);
			if(  !StringUtils.isEmpty( purchaseOrderItem.getBstae() )  ||   !StringUtils.isEmpty( purchaseOrderItem.getLoekz() )  || !StringUtils.isEmpty( purchaseOrderItem.getLockStatus()) || !StringUtils.isEmpty( purchaseOrderItem.getZlock()) || !StringUtils.isEmpty( purchaseOrderItem.getElikz())    ){
				purchaseOrderItem.setIsRed("1");
			}
			if(purchaseOrderItem.getConfirmStatus() == -1 || purchaseOrderItem.getConfirmStatus() == -2){
				purchaseOrderItem.setIsRed("1");
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 采购订单明细列表导出[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("exportExcel/{orderId}/{vendor}")
	public String download(@PathVariable("vendor")Boolean vendor,@PathVariable("orderId")Long orderId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("采购订单明细", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_order.vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
		}else{//采购商
			//用户权限
			searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
			//组织权限
			searchParamMap.put("IN_order.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		}
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_order.id", StringUtils.convertToString(orderId));

		List<PurchaseOrderItemTransfer> list = mainOrderService.getOrderItemVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchaseOrderItemTransfer.class), null);  
		excelUtil.export(list);  
		return null;    
	}
	
	/**
	 * 采购通过主订单导出订单的明细列表导出[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("exportExcelByMainIds/{vendor}/{ids}")
	public String exportExcelByMainIds(@PathVariable("vendor")Boolean vendor, @PathVariable("ids")String ids,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("采购订单明细", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
		}else{//采购商
		/*	//用户权限
			searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());*/
			//组织权限
			//searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		}
		searchParamMap.put("EQ_abolished", "0");
		

		List<PurchaseOrderItemTransfer> list = mainOrderService.getOrderItemVoByMainIds(searchParamMap,ids);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchaseOrderItemTransfer.class), null);  
		excelUtil.export(list);  
		return null;    
	}
	
	
	
	@RequestMapping("getOrder/{id}")
	@ResponseBody
	public PurchaseOrderEntity getOrder(@PathVariable("id") Long id){
		return mainOrderService.getOrder(id);
	}
	
	@RequestMapping("getOrderItem/{id}")
	@ResponseBody
	public PurchaseOrderItemEntity getOrderItem(@PathVariable("id") Long id){
		return mainOrderService.getOrderItem(id);
	}
	
	@RequestMapping(value = "editOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editOrderItem(@Valid PurchaseOrderItemEntity m){
		map = new HashMap<String, Object>();
		mainOrderService.editOrderItem(m);
		map.put("success", true);
		return map;
	}
	
	
	//瑞声不需要
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchaseOrderTransfer.class), getLogger()); 
			List<PurchaseOrderTransfer> list = (List<PurchaseOrderTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = mainOrderService.combinePurchaseOrder(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购订单成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购订单失败!");
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
	
	@RequestMapping(value = "publish/{id}")
	public String publish(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		PurchaseOrderEntity order = mainOrderService.getOrder(id);
		mainOrderService.publishOrder(order);
		redirectAttributes.addFlashAttribute("message", "发布订单 " + order.getOrderCode() + "成功");
		return "redirect:/admin/user";
	}
	
/*	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		mainOrderService.sycOrder();
		map.put("message", "同步订单成功");
		map.put("success", true);
		return map;
	}*/
	
	/**
	 * 同步整个订单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		PurchaseOrderSyncService purchaseOrderSyncService = SpringContextUtils.getBean("purchaseOrderSyncService");	
		boolean isSuccess = purchaseOrderSyncService.execute(logger,null,null);
		if(isSuccess){
			map.put("message", "同步订单成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 同步单个订单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "sycOneOrder/{orderCodes}")
	@ResponseBody
	public Map<String,Object> sycOneOrder(@PathVariable("orderCodes") String orderCodes) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		PurchaseOrderSyncService purchaseOrderSyncService = SpringContextUtils.getBean("purchaseOrderSyncService");	
		boolean isSuccess = purchaseOrderSyncService.execute(logger,null,orderCodes);
		if(isSuccess){
			map.put("message", "同步订单成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	

	/**
	 * 单个发布订单明细【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "publishSingleOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishSingleOrderItem(@RequestParam(value="orderItemId") Long orderItemId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList=new ArrayList<PurchaseOrderItemEntity>();
		PurchaseOrderItemEntity orderItem=mainOrderService.getOrderItem(orderItemId);
		if(null!=orderItem){
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==orderItem.getPublishStatus().intValue()){
				map.put("message", "已发布不能再发布");
				map.put("success", false);
				return map;
			}
		}
		orderItemList.add(orderItem);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.publishOrderItems(orderItemList, new UserEntity(user.id));
		map.put("message", "发布成功");
		map.put("success", true);
		return map;
	}
	

	/**
	 * 单个取消发布订单明细【采】瑞声不需要（采购商不能取消发布）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "unpublishSingleOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unpublishSingleOrderItem(@RequestParam(value="orderItemId") Long orderItemId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList=new ArrayList<PurchaseOrderItemEntity>();
		PurchaseOrderItemEntity orderItem=mainOrderService.getOrderItem(orderItemId);
		if(null!=orderItem){
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()!=orderItem.getPublishStatus().intValue()){
				map.put("message", "未发布不能取消发布");
				map.put("success", false);
				return map;
			}
		}
		orderItemList.add(orderItem);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.unpublishOrderItem(orderItemList, new UserEntity(user.id));
		map.put("message", "取消发布成功");
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 单个确认订单明细【采】（瑞声不需要）
	 * @param orderList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "confirmSingleOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmSingleOrderItem(@RequestParam(value="orderItemId") Long orderItemId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList=new ArrayList<PurchaseOrderItemEntity>();
		PurchaseOrderItemEntity orderItem=mainOrderService.getOrderItem(orderItemId);
		if(null!=orderItem){
			if(PurchaseConstans.CONFIRM_STATUS_YES.intValue()==orderItem.getConfirmStatus().intValue()){
				map.put("message", "已确认不能重复确认");
				map.put("success", false);
				return map;
			}else if(PurchaseConstans.DELIVERY_STATUS_NO.intValue()!=orderItem.getDeliveryStatus().intValue()){
				map.put("message", "已存在发货单不能整单确认");
				map.put("success", false);
				return map;
			}
		}
		orderItemList.add(orderItem);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null,logger);
		map.put("message", "确认成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量发布订单【采】（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "publishOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishOrders(@RequestBody List<PurchaseOrderEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.publishOrders(orderList, new UserEntity(user.id));
		map.put("message", "发布订单成功");
		map.put("success", true);
		return map;
	}	
	
	/**
	 * 关闭订单【采】（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "closeOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> closeOrders(@RequestBody List<PurchaseOrderEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.closeOrders(orderList, new UserEntity(user.id));
		map.put("message", "关闭订单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量确认订单【供】
	 * @param orderList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "confirmOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmOrders(@RequestBody List<PurchaseOrderEntity> orderList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.confirmOrders(orderList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null,logger);
		map.put("message", "确认订单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量驳回订单【供】
	 * @param orderList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "rejectOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectOrders(RejectVO vo) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderEntity> orderList = null;
		String [] orderIds=vo.getReject_ids().split(",");
		if(null!=orderIds && orderIds.length>0){
			orderList=new ArrayList<PurchaseOrderEntity>();
			for (String str : orderIds) {
				orderList.add(mainOrderService.getOrder(Long.valueOf(str)));
			}
			
		}
		
		
		
		
		
		if(null!=orderList && orderList.size()>0){
			
			//获取当前选择的是否有采购商驳回拒绝行项目
			for(PurchaseOrderEntity po : orderList){
				Set<PurchaseOrderItemEntity> orderItemSet  =po.getOrderItem();
				if(CommonUtil.isNotNullCollection(orderItemSet)){
					for (PurchaseOrderItemEntity purchaseOrderItemEntity : orderItemSet) {
						if(purchaseOrderItemEntity.getConfirmStatus() == -2){
							map.put("message", "驳回订单失败，存在采购商驳回拒绝数据，已拒绝的订单应该在详情页面进行确认");
							map.put("success", false);
							return map;
						}
					}
				}
			}
			
			
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			mainOrderService.confirmOrders(orderList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_REJECT,vo.getReject_reason(),logger);
			map.put("message", "驳回订单成功");
			map.put("success", true);
		}else{
			map.put("message", "驳回订单失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	

	
	
	
	/**
	 * 批量发布订单明细【采】（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "publishOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.publishOrderItems(orderItemList, new UserEntity(user.id));
		map.put("message", "发布订单明细成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量确认订单明细【供】
	 * @param orderList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "confirmOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderItemList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null,logger);
		map.put("message", "确认订单明细成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量驳回订单明细【供】
	 * @param orderList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "rejectOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectOrderItems(RejectVO vo) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList = null;
		String [] orderItemIds=vo.getReject_ids().split(",");
		if(null!=orderItemIds && orderItemIds.length>0){
			orderItemList=new ArrayList<PurchaseOrderItemEntity>();
			for (String str : orderItemIds) {
				orderItemList.add(mainOrderService.getOrderItem(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemList && orderItemList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			mainOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_REJECT,vo.getReject_reason(),logger);
			map.put("message", "驳回订单明细成功");
			map.put("success", true);
		}else{
			map.put("message", "驳回订单明细失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 批量拒绝供应商驳回条件【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "vetoOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vetoOrderItems(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList = null;
		String [] orderItemIds=vo.getVeto_ids().split(",");
		if(null!=orderItemIds && orderItemIds.length>0){
			orderItemList=new ArrayList<PurchaseOrderItemEntity>();
			for (String str : orderItemIds) {
				orderItemList.add(mainOrderService.getOrderItem(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemList && orderItemList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			mainOrderService.vetoOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.VETO_STATUS_VETO,vo.getVetoReason());
			map.put("message", "拒绝供应商驳回成功");
			map.put("success", true);
		}else{
			map.put("message", "拒绝供应商驳回失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 批量同意供应商驳回条件【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "unVetoOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unVetoOrderItem(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList = null;
		String [] orderItemIds=vo.getVeto_ids().split(",");
		if(null!=orderItemIds && orderItemIds.length>0){
			orderItemList=new ArrayList<PurchaseOrderItemEntity>();
			for (String str : orderItemIds) {
				orderItemList.add(mainOrderService.getOrderItem(Long.valueOf(str)));
			}
		}
		if(null!=orderItemList && orderItemList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			mainOrderService.unVetoOrderItem(orderItemList, new UserEntity(user.id),PurchaseConstans.VETO_STATUS_AGREE,vo.getVetoReason());
			map.put("message", "同意供应商驳回");
			map.put("success", true);
		}else{
			map.put("message", "同意供应商驳回失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	/**
	 * 确认订单-供货关系【供】（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "confirmItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmItemPlan(@RequestBody List<PurchaseOrderItemPlanEntity> orderItemPlanList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : orderItemPlanList) {
			if(purchaseOrderItemPlanEntity.getDeliveryStatus().intValue()!=PurchaseConstans.DELIVERY_STATUS_NO.intValue()){
				map.put("message","确认失败,已发货不能确认");
				map.put("success", false);
				return map;
			}
		}
		mainOrderService.confirmOrderItemPlans(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null);
		map.put("message", "确认供货关系成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 驳回供货关系【供】（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "rejectItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectItemPlan(RejectVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<PurchaseOrderItemPlanEntity> orderItemPlanList = null;
		String [] orderPlanIds=vo.getReject_ids().split(",");
		if(null!=orderPlanIds && orderPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchaseOrderItemPlanEntity>();
			for (String str : orderPlanIds) {
				orderItemPlanList.add(mainOrderService.getOrderItemPlan(Long.valueOf(str)));
			}
			
		}
		
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : orderItemPlanList) {
				if(purchaseOrderItemPlanEntity.getDeliveryStatus().intValue()!=PurchaseConstans.DELIVERY_STATUS_NO.intValue()){
					map.put("message","驳回失败,已发货不能驳回");
					map.put("success", false);
					return map;
				}
			}
			mainOrderService.confirmOrderItemPlans(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_REJECT,vo.getReject_reason());
			map.put("message", "驳回供货关系成功");
			map.put("success", true);
		}else{
			map.put("msg","驳回供货关系失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 关闭订单明细【采】（暂不需要）
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "closeOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> closeOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.closeOrderItems(orderList, new UserEntity(user.id));
		map.put("message", "关闭订单明细成功");
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 显示订单明细详情 （瑞声不许哟）
	 * @param itemId 订单明细行
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemDetail/{itemId}/{vendor}", method = RequestMethod.GET)
	public String viewItemDetail(@PathVariable(value="itemId") Long itemId, @PathVariable(value="vendor") boolean vendor, Model model) {
		PurchaseOrderItemEntity orderItem = mainOrderService.getOrderItem(itemId);
		model.addAttribute("po", orderItem);
		model.addAttribute("vendor", vendor);
		return "back/orderMain/order/orderView";
	}
	
	/**
	 * 查看订单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orderitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@RequestParam(value="vendor") boolean vendor,@PathVariable("id") Long orderitemid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.id", orderitemid + "");
		searchParamMap.put("EQ_abolished", "0");
		if(vendor){//是供应商
			searchParamMap.put("EQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_YES);
		}
	    searchParamMap.put("EQ_shipType", PurchaseConstans.SHIP_TYPE_NORMAL);
	    
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "requestTime";
			order="asc";
		}
	    
		//供货关系
		Page<PurchaseOrderItemPlanEntity> userPage = mainOrderService.getPurchaseOrderItemPlans(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	
	/**
	 * 保存拆分供货计划（瑞声不需要）
	 * @param datas
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@LogClass(method="保存拆分供货计划",module="采购协同")
	@RequestMapping(value = "saveOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveOrderItemPlan(@RequestParam(value="datas") String datas, @RequestParam(value="itemId") Long id, @RequestParam(value="orderType") Integer orderType) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		
		Set<String> requestTimeSet=new HashSet<String>();
		//验证计划数量总和，是否和订单明细需求数量相同
		double total = 0;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			String orderQty_str = object.get("orderQty") + "";
			if(CommonUtil.isNotNullAndNotEmpty(orderQty_str)){	//订单数量不为空
				total += Double.parseDouble(orderQty_str);
			}
			
			String requestTime_str=object.get("requestTime")+ "";
			if(CommonUtil.isNotNullAndNotEmpty(requestTime_str)){//需求日期
				requestTimeSet.add(requestTime_str);
			}
		}
		
		if(requestTimeSet.size()<array.size()){
			map.put("msg", "需求到货日期不能相同");//需求到货日期不能相同
			map.put("success", false);
			return map;
		}
		
		total = BigDecimalUtil.add(total, 0);
		PurchaseOrderItemEntity purchaseOrderItemEntity = mainOrderService.getOrderItem(id);
		double orderQty_compare = purchaseOrderItemEntity.getOrderQty();
		if(total != orderQty_compare){
			map.put("msg", MessageSourceUtil.getMessage("order.message12", request));//所有供货计划需求之和必须等于订单总需求量
			map.put("success", false);
			return map;
		}
		
		List<PurchaseOrderItemPlanEntity> planList = new ArrayList<PurchaseOrderItemPlanEntity>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		PurchaseOrderItemPlanEntity plan = null;
		double orderQty =0;
		Timestamp requestTime=null;
		boolean changedFlag = false;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			if(StringUtils.convertToLong(object.get("id") + "") != null){
				plan=mainOrderService.getOrderPlanByPlanId(StringUtils.convertToLong(object.get("id") + ""));
				//已经确认的不能编辑，因此无需保存
				if(null!=plan.getConfirmStatus() && plan.getConfirmStatus().intValue() == PurchaseConstans.CONFIRM_STATUS_YES.intValue()){
				    continue;
				}
			}else{
				plan=new PurchaseOrderItemPlanEntity();
			}
			
			if(plan.getRequestTime()!=null)
				requestTime = plan.getRequestTime();
			Timestamp _requestTime = DateUtil.stringToTimestamp(requestTimeHandle(object.get("requestTime") + " 23:59:59"), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			if(plan.getOrderQty()!=null)
				orderQty = plan.getOrderQty();
			double _orderQty = StringUtils.convertToDouble(object.get("orderQty") + "");
			

			if(CommonUtil.isNotNullAndNotEmpty(requestTime)&& CommonUtil.isNotNullAndNotEmpty(_requestTime)&&(!requestTime.equals(_requestTime) || orderQty != _orderQty)){//数量或者确认到货时间不同，则说明已产生变更，需要发送邮件
				changedFlag = true;
				if(null!=plan.getConfirmStatus() && plan.getConfirmStatus().intValue() == PurchaseConstans.CONFIRM_STATUS_REJECT.intValue()){
				    plan.setConfirmStatus(PurchaseConstans.CLOSE_STATUS_NO);
				}
			}
			
			plan.setItemNo(purchaseOrderItemEntity.getItemNo()); 
			plan.setOrderQty(StringUtils.convertToDouble(object.get("orderQty") + ""));
			plan.setRequestTime(DateUtil.stringToTimestamp(requestTimeHandle(object.get("requestTime") + " 23:59:59"), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			planList.add(plan);
		}
		mainOrderService.saveSplitOrderPlan( planList, new UserEntity(user.id), changedFlag, orderType, purchaseOrderItemEntity);
		map.put("msg", MessageSourceUtil.getMessage("message.plan.save.success", request));
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 订单明细页面删除订单计划（瑞声不需要）
	 * @param plan
	 * @return
	 */
	@RequestMapping(value = "deletePlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletePlan( @RequestBody PurchaseOrderItemPlanEntity plan){
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity userEntity=new UserEntity(user.id);
		Map<String,Object> map = new HashMap<String, Object>();
		String msg = "删除失败！";
		PurchaseOrderItemEntity orderItem = null;
		if( (CommonUtil.isNotNullAndNotEmpty(plan)) && (plan.getPublishStatus().equals(PurchaseConstans.PUBLISH_STATUS_NO))){
		  orderItem = plan.getOrderItem(); //根据paln获得item
		  orderItem = mainOrderService.getOrderItem(orderItem.getId());
		  if(CommonUtil.isNotNullAndNotEmpty(orderItem)){ //根据item 获得 paln 的集合
			Set<PurchaseOrderItemPlanEntity> planSet = orderItem.getOrderItemPlan();
			if(planSet.size() == 1){
				msg = "不允许删除原始数据行！";
				map.put("msg",msg);
				map.put("success", false);
				return map;
			}else{
				for (PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity : planSet) {
					//如果集合中存在除要删除的订单计划之外还未发布的订单计划，则修改需求数量
					if(plan.getId()!=purchaseOrderItemPlanEntity.getId() && purchaseOrderItemPlanEntity.getPublishStatus().equals(PurchaseConstans.PUBLISH_STATUS_NO)){
						purchaseOrderItemPlanEntity.setOrderQty(BigDecimalUtil.add(purchaseOrderItemPlanEntity.getOrderQty(), plan.getOrderQty()));
						purchaseOrderItemPlanEntity.setUndeliveryQty(BigDecimalUtil.add(purchaseOrderItemPlanEntity.getUndeliveryQty(), plan.getUndeliveryQty()));
						mainOrderService.updatePlan(purchaseOrderItemPlanEntity,userEntity);		//保存修改的订单计划
						mainOrderService.deletePlan(plan,userEntity);		//删除选择需要删除的订单计划
						msg = "删除成功！";
						map.put("msg", msg);
						map.put("success", true);
						return map;
					}
				  }
				//不存在未发布的订单计划
				msg = "不存在未发布的订单计划,删除的订单数量无法累加到其他计划上！";
				map.put("msg", msg);
				map.put("success", false);
				return map;
				}
		  }
	  }
		map.put("msg", msg);
		map.put("success", false);
		return map;
	}
	
	
	/**
	 * 批量发布订单 （瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@LogClass(method="批量发布订单",module="采购协同")
	@RequestMapping(value = "publishItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishItemPlan(@RequestBody List<PurchaseOrderItemPlanEntity> orderItemPlanList){
		Map<String,Object> map = new HashMap<String, Object>();
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		for(PurchaseOrderItemPlanEntity itemPlan : orderItemPlanList) {
			orderItemPlan = mainOrderService.getOrderPlanByPlanId(itemPlan.getId());
			if(orderItemPlan.getPublishStatus().intValue() == PurchaseConstans.PUBLISH_STATUS_YES.intValue()) {
				map.put("message", "已发布的供货计划不能再次发布");
				map.put("success", false);
				return map;
			}
		}
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.publishOrderItemPlans(orderItemPlanList, new UserEntity(user.id));
		map.put("message", "发布成功！"); 
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量取消发布订单（瑞声不需要）
	 * @param orderList
	 * @return
	 */
	@LogClass(method="批量取消发布订单",module="采购协同")
	@RequestMapping(value = "unpublishItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unpublishItemPlan(@RequestBody List<PurchaseOrderItemPlanEntity> orderItemPlanList){
		Map<String,Object> map = new HashMap<String, Object>();
		PurchaseOrderItemPlanEntity orderItemPlan = null;
		for(PurchaseOrderItemPlanEntity itemPlan : orderItemPlanList) {
			orderItemPlan = mainOrderService.getOrderPlanByPlanId(itemPlan.getId());
			if(orderItemPlan.getConfirmStatus().intValue() == PurchaseConstans.CONFIRM_STATUS_YES.intValue()) {
				map.put("message", "已确认的供货计划不能取消发布");
				map.put("success", false);
				return map;
			}else if(orderItemPlan.getPublishStatus().intValue() == PurchaseConstans.PUBLISH_STATUS_NO.intValue()){
				map.put("message", "未发布的供货计划不能取消发布");
				map.put("success", false);
				return map;
			}
		}
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		mainOrderService.unPublishOrderItemPlans(orderItemPlanList, new UserEntity(user.id));
		map.put("message", "取消发布成功"); 
		map.put("success", true);
		return map;
	}
	/**
	 * 要货到货时间处理
	 * @param requestTime
	 * @return
	 */
	private String requestTimeHandle(String requestTime) {
		if(StringUtils.isEmpty(requestTime))
			return null;
		if(requestTime.length() == 19)
			return requestTime;
		
		return requestTime + ":00:00";
	}
	
	
	/**
	 * 反馈信息显示
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="feedback/{itemId}", method = RequestMethod.GET)
	public String feedback(@PathVariable(value="itemId") Long itemId, Model model) {
		PurchaseOrderItemEntity orderItem = mainOrderService.getOrderItem(itemId);
		Map<String, Object> searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("EQ_billType", PurchaseConstans.PURCHASE_ORDER_ITEM + "");
		searchParamMap.put("EQ_billId", itemId + "");
		List<FeedbackEntity> list = feedbackService.getFeedbacks(searchParamMap);
		
		model.addAttribute("vendor", true);
		model.addAttribute("po", orderItem);
		model.addAttribute("feedList", list);
		return "back/orderMain/order/feedback";
	}
	
	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	@Override
	public void log(Object message) {
		getLogger().log(message); 
	}


	
}
