package com.qeweb.scm.purchasemodule.web.order;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.FeedbackService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.service.PurchaseMainOrderService;
import com.qeweb.scm.purchasemodule.service.PurchaseOrderService;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;

/**
 * 采购订单管理
 * @author alex
 * @date 2015年4月22日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchaseOrderController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseorder")
public class PurchaseOrderController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private PurchaseMainOrderService  mainOrderService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		model.addAttribute("extended", getDynamicDataService().getDynamicData("purchaseOrderEntity"));
		return "back/order/orderList";  
	}
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/order/orderList";
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
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_order.vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
			//add by chao.gu 删除的不要显示
			searchParamMap.put("ISNULL_loekz",searchParamMap);
			//add end
		}else{//采购商
			//用户权限
//			searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
			//组织权限
//			searchParamMap.put("IN_order.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
			//采购组权限
			searchParamMap.put("IN_order.purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		}
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("ISNULL_retpo","111");//具有“X”的为退货采购订单行项目，为空的为标准的采购订单
		
		
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
		
		
		
		Page<PurchaseOrderItemEntity> userPage = purchaseOrderService.getPurchaseOrderItems(pageNumber,pageSize,searchParamMap);
		for(PurchaseOrderItemEntity purchaseOrderItem  : userPage.getContent() ){
			String col1 = receiveService.getOrderItemZllblQty(purchaseOrderItem.getId()) == null ? "" :   receiveService.getOrderItemZllblQty(purchaseOrderItem.getId()).toString() ;
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
			if(   !StringUtils.isEmpty( purchaseOrderItem.getBstae() )  ||  !StringUtils.isEmpty( purchaseOrderItem.getLoekz() )  || !StringUtils.isEmpty( purchaseOrderItem.getLockStatus()) || !StringUtils.isEmpty( purchaseOrderItem.getZlock()) || !StringUtils.isEmpty( purchaseOrderItem.getElikz())    ){
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
	
	@RequestMapping("getOrder/{id}")
	@ResponseBody
	public PurchaseOrderEntity getOrder(@PathVariable("id") Long id){
		return purchaseOrderService.getOrder(id);
	}
	
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
			boolean flag = purchaseOrderService.combinePurchaseOrder(list, getLogger());
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
		PurchaseOrderEntity order = purchaseOrderService.getOrder(id);
		purchaseOrderService.publishOrder(order);
		redirectAttributes.addFlashAttribute("message", "发布订单 " + order.getOrderCode() + "成功");
		return "redirect:/admin/user";
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		purchaseOrderService.sycOrder();
		map.put("message", "同步订单成功");
		map.put("success", true);
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
		PurchaseOrderItemEntity orderItem=purchaseOrderService.getOrderItem(orderItemId);
		if(null!=orderItem){
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()==orderItem.getPublishStatus().intValue()){
				map.put("message", "已发布不能再发布");
				map.put("success", false);
				return map;
			}
		}
		orderItemList.add(orderItem);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.publishOrderItems(orderItemList, new UserEntity(user.id));
		map.put("message", "发布成功");
		map.put("success", true);
		return map;
	}
	

	/**
	 * 单个取消发布订单明细【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "unpublishSingleOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unpublishSingleOrderItem(@RequestParam(value="orderItemId") Long orderItemId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList=new ArrayList<PurchaseOrderItemEntity>();
		PurchaseOrderItemEntity orderItem=purchaseOrderService.getOrderItem(orderItemId);
		if(null!=orderItem){
			if(PurchaseConstans.PUBLISH_STATUS_YES.intValue()!=orderItem.getPublishStatus().intValue()){
				map.put("message", "未发布不能取消发布");
				map.put("success", false);
				return map;
			}
		}
		orderItemList.add(orderItem);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.unpublishOrderItem(orderItemList, new UserEntity(user.id));
		map.put("message", "取消发布成功");
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 单个确认订单明细【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "confirmSingleOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmSingleOrderItem(@RequestParam(value="orderItemId") Long orderItemId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList=new ArrayList<PurchaseOrderItemEntity>();
		PurchaseOrderItemEntity orderItem=purchaseOrderService.getOrderItem(orderItemId);
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
		purchaseOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null);
		map.put("message", "确认成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量发布订单明细【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "publishOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.publishOrderItems(orderItemList, new UserEntity(user.id));
		map.put("message", "发布订单明细成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量确认订单明细【供】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "confirmOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null);
		map.put("message", "确认订单明细成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量驳回订单明细【供】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "rejectOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectOrderItems(RejectVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemEntity> orderItemList = null;
		String [] orderItemIds=vo.getReject_ids().split(",");
		if(null!=orderItemIds && orderItemIds.length>0){
			orderItemList=new ArrayList<PurchaseOrderItemEntity>();
			for (String str : orderItemIds) {
				orderItemList.add(purchaseOrderService.getOrderItem(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemList && orderItemList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			purchaseOrderService.confirmOrderItems(orderItemList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_REJECT,vo.getReject_reason());
			map.put("message", "驳回订单明细成功");
			map.put("success", true);
		}else{
			map.put("message", "驳回订单明细失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 确认订单-供货关系【供】
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
		purchaseOrderService.confirmOrderItemPlans(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_YES,null);
		map.put("message", "确认供货关系成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 驳回供货关系【供】
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
				orderItemPlanList.add(purchaseOrderService.getOrderItemPlan(Long.valueOf(str)));
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
			purchaseOrderService.confirmOrderItemPlans(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.CONFIRM_STATUS_REJECT,vo.getReject_reason());
			map.put("message", "驳回供货关系成功");
			map.put("success", true);
		}else{
			map.put("msg","驳回供货关系失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 关闭订单明细【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "closeOrderItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> closeOrderItems(@RequestBody List<PurchaseOrderItemEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.closeOrderItems(orderList, new UserEntity(user.id));
		map.put("message", "关闭订单明细成功");
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 显示订单明细详情
	 * @param itemId 订单明细行
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemDetail/{itemId}/{vendor}", method = RequestMethod.GET)
	public String viewItemDetail(@PathVariable(value="itemId") Long itemId, @PathVariable(value="vendor") boolean vendor, Model model) {
		PurchaseOrderItemEntity orderItem = purchaseOrderService.getOrderItem(itemId);
		model.addAttribute("po", orderItem);
		model.addAttribute("vendor", vendor);
		return "back/order/orderView";
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
		//供货关系
		Page<PurchaseOrderItemPlanEntity> userPage = purchaseOrderService.getPurchaseOrderItemPlans(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	
	/**
	 * 保存拆分供货计划
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
		PurchaseOrderItemEntity purchaseOrderItemEntity = purchaseOrderService.getOrderItem(id);
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
				plan=purchaseOrderService.getOrderPlanByPlanId(StringUtils.convertToLong(object.get("id") + ""));
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
		purchaseOrderService.saveSplitOrderPlan( planList, new UserEntity(user.id), changedFlag, orderType, purchaseOrderItemEntity);
		map.put("msg", MessageSourceUtil.getMessage("message.plan.save.success", request));
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 订单明细页面删除订单计划
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
		  orderItem = purchaseOrderService.getOrderItem(orderItem.getId());
		  if(CommonUtil.isNotNullAndNotEmpty(orderItem)){ //根据item 获得 paln 的集合
			List<PurchaseOrderItemPlanEntity> planSet = purchaseOrderService.findItemPlanEntitysByItem(orderItem.getId());
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
						purchaseOrderService.updatePlan(purchaseOrderItemPlanEntity,userEntity);		//保存修改的订单计划
						purchaseOrderService.deletePlan(plan,userEntity);		//删除选择需要删除的订单计划
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
	 * 批量发布订单
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
			orderItemPlan = purchaseOrderService.getOrderPlanByPlanId(itemPlan.getId());
			if(orderItemPlan.getPublishStatus().intValue() == PurchaseConstans.PUBLISH_STATUS_YES.intValue()) {
				map.put("message", "已发布的供货计划不能再次发布");
				map.put("success", false);
				return map;
			}
		}
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.publishOrderItemPlans(orderItemPlanList, new UserEntity(user.id));
		map.put("message", "发布成功！"); 
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量取消发布订单
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
			orderItemPlan = purchaseOrderService.getOrderPlanByPlanId(itemPlan.getId());
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
		purchaseOrderService.unPublishOrderItemPlans(orderItemPlanList, new UserEntity(user.id));
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
		PurchaseOrderItemEntity orderItem = purchaseOrderService.getOrderItem(itemId);
		Map<String, Object> searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("EQ_billType", PurchaseConstans.PURCHASE_ORDER_ITEM + "");
		searchParamMap.put("EQ_billId", itemId + "");
		List<FeedbackEntity> list = feedbackService.getFeedbacks(searchParamMap);
		
		model.addAttribute("vendor", true);
		model.addAttribute("po", orderItem);
		model.addAttribute("feedList", list);
		return "back/order/feedback";
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
