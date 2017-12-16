package com.qeweb.scm.purchasemodule.web.goodsrequest;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.FactoryService;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.PurchaseGoodsRequestService;
import com.qeweb.scm.purchasemodule.service.PurchaseMainOrderService;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestReportHeadViewVo;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestReportVo;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.PurchaseOrderItemPlanTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseGoodsRequestVO;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;
import com.qeweb.scm.purchasemodule.web.vo.VetoVO;
import com.qeweb.scm.sap.service.PurchaseGoodsRequestSyncService;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMaterialRelService;

/**
 * 要货计划
 * @author eleven
 *
 */
@Controller
@RequestMapping(value = "/manager/order/goodsRequest")
public class PurchaseGoodsRequestController extends BaseService implements ILog {
	
	private Map<String,Object> map;
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	PurchaseGoodsRequestService purchaseGoodsRequestService;
	
	@Autowired
	PurchaseGoodsRequestSyncService purchaseGoodsRequestSyncService;
	
	@Autowired
	private PurchaseMainOrderService mainOrderService;
	
	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private VendorMaterialRelService vendorMaterialRelService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private DeliveryService deliveryService;


	@Autowired
	private BacklogService backlogService;
	
	/**
	 *   admin
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		return "back/orderGoodsRequret/goodsRequestList";
	}
	
	/**
	 *    vendor
	 * @param model
	 * @return
	 */
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		return "back/orderGoodsRequret/goodsRequestList";
	}
	
	@RequestMapping(value="/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		String x = searchParamMap.get("EQ_flag")+"";
		purchaseGoodsRequestService.goodsReportHeadGet();
		if(x.equals("X")){
			searchParamMap.put("ISNOTNULL_flag",searchParamMap);  
			searchParamMap.remove("EQ_flag");
		}else if(x.equals("!X")){
			searchParamMap.put("ISNULL_flag",searchParamMap);  
			searchParamMap.remove("EQ_flag");
		}
		
		
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
			searchParamMap.put("NEQ_dhsl", "0.000");
		}else{
			//采购商
			//用户权限
//			searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
			//组织权限
//			searchParamMap.put("IN_order.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
			
			//采购组权限
			searchParamMap.put("IN_group.id", buyerOrgPermissionUtil.getGroupIds());
		}
		
		String vendorCodes = searchParamMap.get("IN_vendor.code")+"";
		String materialCodes = searchParamMap.get("IN_material.code")+"";
		
		List<String> codeList =new  ArrayList<String>();
		List<String> materialsList =new  ArrayList<String>();
		
		if(  !StringUtils.isEmpty(vendorCodes) && !vendorCodes.equals("null")  ){
			String [] codes=vendorCodes.split(",");
			if(null!=codes && codes.length>0){
				   for(String str : codes){
					   codeList.add(str);
				   }
				}
			searchParamMap.put("IN_vendor.code",codeList );
		}
		
		if(  !StringUtils.isEmpty(materialCodes) && !materialCodes.equals("null")){
			String [] materials=materialCodes.split(",");
			if(null!=materials && materials.length>0){
				   for(String str : materials){
					   materialsList.add(str);
				  }
			}
			searchParamMap.put("IN_material.code",materialsList);
		}

		
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "rq";
			order = "asc";
		}
		Page<PurchaseGoodsRequestEntity> userPage = purchaseGoodsRequestService.getPurchaseGoodsRequest(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		
		//标识采购订单行有删除，冻结，锁定，交货已完成的添加红色标识
		for(PurchaseGoodsRequestEntity pgr : userPage.getContent() ){
			List<PurchaseOrderItemPlanEntity> orderItemPlanList  =purchaseGoodsRequestService.getPoItemplanListBygoodsId(pgr.getId());
			for(PurchaseOrderItemPlanEntity  orderItemPlan : orderItemPlanList){
				if(  !StringUtils.isEmpty( orderItemPlan.getOrderItem().getBstae() )  ||  !StringUtils.isEmpty( orderItemPlan.getOrderItem().getLoekz() )  || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getLockStatus()) || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getZlock()) || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getElikz())    ){
					pgr.setIsRed("1");
				}
			}
		}
				
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 行转列显示要货数据
	 * @author chao.gu
	 * 20171031
	 * @param vendor
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getRequestRowList/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getRequestRowList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_singlePublishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
			searchParamMap.put("NEQ_dhsl", "0.000");
		}else{
			//采购商
			//采购组权限
			searchParamMap.put("IN_group.id", buyerOrgPermissionUtil.getGroupIds());
		}
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			//String backlogId = searchParamMap.get("IN_backlogId").toString();
			//如果是那句
			searchParamMap.put("NEQ_vendorConfirmStatus", 1);
			
			if("6056495".equals(searchParamMap.get("IN_backlogId").toString())){
				searchParamMap.put("NEQ_singlePublishStatus", 1);
			}
		}
			searchParamMap.remove("IN_backlogId");
		//待办结束
		
		List<String> dateStrList =purchaseGoodsRequestService.getDateStrList(searchParamMap.get("GTE_rq").toString(), searchParamMap.get("LTE_rq").toString());
		
		Page<PurchaseGoodsRequestVO> userPage = purchaseGoodsRequestService.getRequestRowList(pageNumber,pageSize,searchParamMap,dateStrList);
				
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取行转列动态表头数据
	 * @author chao.gu
	 * 20171031
	 * @param beginRq
	 * @param endRq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRequestRowHeader/{beginRq}/{endRq}",method = RequestMethod.POST)
	@ResponseBody
	public PurchaseGoodsRequestVO getRequestRowHeader(@PathVariable("beginRq") String beginRq,@PathVariable("endRq") String endRq) throws Exception{  
		return purchaseGoodsRequestService.getRequestRowHeader(beginRq,endRq);
	}
	/**
	 * 显示要货计划页面
	 * @param goodsId 要货计划的id
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemList/{vendor}", method = RequestMethod.GET)
	public String viewItemList(
			@PathVariable(value = "vendor") boolean vendor,
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq, Model model) {
		//通过id获取要货计划的的信息
		//PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(goodsId);
		//model.addAttribute("goods", goods);
		//model.addAttribute("goodsId", goodsId);
		model.addAttribute("vendor", vendor);
		model.addAttribute("flag", flag);
		model.addAttribute("factoryId", factoryId);
		model.addAttribute("purchasingGroupId", purchasingGroupId);
		model.addAttribute("materialId", materialId);
		model.addAttribute("meins", meins);
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("type", type);
		model.addAttribute("beginRq", beginRq);
		model.addAttribute("endRq", endRq);
		return "back/orderGoodsRequret/goodsRequestDetailedList";
	}
	
	
	/**
	 * 显示要货计划页面
	 * @param goodsId 要货计划的id
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemListModify/{vendor}", method = RequestMethod.GET)
	public String viewItemListModify(
			@PathVariable(value = "vendor") boolean vendor,
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq, Model model) {
		//通过id获取要货计划的的信息
		//PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(goodsId);
		//model.addAttribute("goods", goods);
		//model.addAttribute("goodsId", goodsId);
		model.addAttribute("vendor", vendor);
		model.addAttribute("flag", flag);
		model.addAttribute("factoryId", factoryId);
		model.addAttribute("purchasingGroupId", purchasingGroupId);
		model.addAttribute("materialId", materialId);
		model.addAttribute("meins", meins);
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("type", type);
		model.addAttribute("beginRq", beginRq);
		model.addAttribute("endRq", endRq);
		return "back/orderGoodsRequret/goodsRequestDetailedListModify";
	}

	/**
	 * 显示订单明细列表
	 * @param itemId 订单明细行
	 * @param vendor 是否是供应商
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewOrderItem/{orderId}/{vendor}", method = RequestMethod.GET)
	public String viewOrderItem(@PathVariable(value="orderId") Long orderId, @PathVariable(value="vendor") boolean vendor, Model model) {
		//通过id获取订单头的信息
		PurchaseOrderEntity  po = mainOrderService.getOrder(orderId);
		model.addAttribute("po", po);
		model.addAttribute("orderId", orderId);
		model.addAttribute("vendor", vendor);
		return "back/orderGoodsRequret/orderItemView";
	}
		
	
	//???????????????????????????????????????????????????????????????????????????????????????????????????????????
	/**
	 * 要货计划主表list（从要货计划行转列中查看明细的所有日期内的要货计划）
	 * @author chao.gu
	 * 20171101
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getRequestList/{vendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getRequestList(
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq,
			@PathVariable(value="vendor") boolean vendor,
			@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_flag", flag);
		searchParamMap.put("EQ_factory.id", factoryId);
		searchParamMap.put("EQ_group.id", purchasingGroupId);
		searchParamMap.put("EQ_material.id", materialId);
		searchParamMap.put("EQ_meins", meins);
		searchParamMap.put("EQ_vendor.id", vendorId);
		searchParamMap.put("GTE_rq", beginRq);
		searchParamMap.put("LTE_rq", endRq);
		
		searchParamMap.put("EQ_abolished", "0");
		//按照时间升序
		Sort sort = new Sort(Direction.ASC, "rq");
		Page<PurchaseGoodsRequestEntity> page = purchaseGoodsRequestService.getPurchaseGoodsRequest(pageNumber,pageSize,searchParamMap,sort);
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	
	
	/**
	 *  要货计划匹配订单（相当于要货计划的子项目）
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getItemList/{vendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getItemLists(
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq,
			@PathVariable(value="vendor") boolean vendor,
			@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		
		searchParamMap.put("EQ_purchaseGoodsRequest.flag", flag);
		searchParamMap.put("EQ_purchaseGoodsRequest.factory.id", factoryId);
		searchParamMap.put("EQ_purchaseGoodsRequest.group.id", purchasingGroupId);
		searchParamMap.put("EQ_purchaseGoodsRequest.material.id", materialId);
		searchParamMap.put("EQ_purchaseGoodsRequest.meins", meins);
		searchParamMap.put("EQ_purchaseGoodsRequest.vendor.id", vendorId);
		searchParamMap.put("GTE_purchaseGoodsRequest.rq", beginRq);
		searchParamMap.put("LTE_purchaseGoodsRequest.rq", endRq);
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_shipType", PurchaseConstans.SHIP_TYPE_NORMAL);
		if(vendor) {//供应商
			//显示供货计划的数据
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_purchaseGoodsRequest.vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
		}else{//采购商

		}
		searchParamMap.put("EQ_abolished", "0");
		//按照时间升序
		Sort sort = new Sort(Direction.ASC, "requestTime");
		Page<PurchaseOrderItemPlanEntity> page = purchaseGoodsRequestService.getPurchaseGoodsRequestItemPlans(pageNumber,pageSize,searchParamMap,sort);

		//标识采购订单行有删除，冻结，锁定，交货已完成的添加红色标识
		for(PurchaseOrderItemPlanEntity orderItemPlan : page.getContent() ){
				if( !StringUtils.isEmpty( orderItemPlan.getOrderItem().getBstae() )  || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getLoekz() )  || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getLockStatus()) || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getZlock()) || !StringUtils.isEmpty( orderItemPlan.getOrderItem().getElikz())    ){
					orderItemPlan.setIsRed("1");
					//添加是否删除的状态（有 asn 无删除的状态）
					List<DeliveryItemEntity>  deItemList  =  deliveryService.findDeliveryItemEntitysByOrderItemPlanId(orderItemPlan.getId()); 
				    if(deItemList != null && deItemList.size() != 0){
				    	orderItemPlan.setIsDelete("0");
				    }else{
				    	orderItemPlan.setIsDelete("1");
				    }
				
				}
		}
		
		
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	
	
	/**
	 *  批量要货计划的发布
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "batchPublishGoodsRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batchPublishGoodsRequest(
			@RequestBody List<PurchaseGoodsRequestVO> list, Model model) {
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Boolean flag =purchaseGoodsRequestService.batchPublishGoodsRequest(list,new UserEntity(user.id));
		if(flag){
			map.put("message", "有PO匹配的要货计划发布成功！");
			map.put("success", true);
			return map;
		}else{
			map.put("message", "操作失败！刷新页面重新提交！");
			map.put("success", false);
			return map;
		}
	}
	
	
	
	/**
	 *  单个要货计划的发布
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "singlePublishGoodsRequests",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> singlePublishGoodsRequests(@RequestBody List<PurchaseOrderItemPlanEntity> list, Model model){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Boolean flag =purchaseGoodsRequestService.singlePublishGoodsRequests(list,new UserEntity(user.id));
		if(flag){
			//通过id获取要货计划的的信息
			PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(list.get(0).getPurchaseGoodsRequest().getId());
			map.put("message", "发布成功");
			map.put("success", true);
			map.put("goods", goods);		
		}else{
			map.put("message", "操作失败！刷新页面重新提交！");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	/**
	 * 删除需要删除的要货计划的产生的供货计划【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "deleteOrderItemPlan/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteOrderItemPlan(@PathVariable(value="id") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemPlanEntity> voList = new ArrayList<PurchaseOrderItemPlanEntity>();  
		PurchaseOrderItemPlanEntity orderItemPlan = purchaseGoodsRequestService.getPurchaseOrderItemPlan(id);
		
		if(orderItemPlan != null){
			voList.add(orderItemPlan);
			Boolean flag = purchaseGoodsRequestService.deleteOrderItemPlan(voList);
			if(flag){
				//通过id获取要货计划的的信息
				PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(voList.get(0).getPurchaseGoodsRequest().getId());
				map.put("message", "操作成功");
				map.put("success", true);
				map.put("goods", goods);
			}else{
				map.put("message", "操作失败，重新提交数据");
				map.put("success", false);
			}
		}else{
			map.put("message", "操作失败，未找到相应数据");
			map.put("success", false);
		}
		
		
		return map;

	}
	
	
	
	
	
	
	//???????????????????????????????????????????????????????????????????????????????????????????????????????????

	/**
	 * 单个确认要货计划明细（确认供货计划）【供】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "confirmOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmOrderItemPlan(@RequestBody List<PurchaseOrderItemPlanEntity> volist){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Boolean flag = purchaseGoodsRequestService.confirmOrderItemPlan(volist, new UserEntity(user.id));
		if(flag){
			//通过id获取要货计划的的信息
			PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(volist.get(0).getPurchaseGoodsRequest().getId());
			map.put("message", "确认要货计划成功");
			map.put("success", true);
			map.put("goods", goods);
		}else{
			map.put("message", "确认要货计划失败，重新提交数据");
			map.put("success", true);
		}
		return map;
	}
	
	
	
	
	/**
	 * 批量确认要货计划（确认供货计划）【供】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "batchConfirmGoodsRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> batchConfirmGoodsRequest(
			@RequestBody List<PurchaseGoodsRequestVO> list){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Boolean flag = purchaseGoodsRequestService.batchConfirmGoodsRequest(list, new UserEntity(user.id));
		if(flag){
			map.put("message", "确认要货计划成功");
			map.put("success", true);
		}else{
			map.put("message", "确认要货计划失败，重新提交数据");
			map.put("success", true);
		}
		return map;
	}
	
	
	
	/**
	 * 批量驳回要货计划明细（驳回供货计划）【供】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "rejectOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectOrderItemPlan(RejectVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemPlanEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getReject_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchaseOrderItemPlanEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchaseGoodsRequestService.getPurchaseOrderItemPlan(Long.valueOf(str)));
			}
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Boolean flag = purchaseGoodsRequestService.rejectOrderItemPlan(orderItemPlanList, new UserEntity(user.id),vo.getReject_reason());
		if(flag){
			//通过id获取要货计划的的信息
			PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(orderItemPlanList.get(0).getPurchaseGoodsRequest().getId());
			map.put("message", "操作要货计划成功");
			map.put("success", true);
			map.put("goods", goods);
		}else{
			map.put("message", "操作要货计划失败，重新提交数据");
			map.put("success", true);
		}
		return map;
	}
	
	
	/**
	 * 批量拒绝供应商驳回要货计划（供货计划）【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "vetoOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vetoOrderItemPlan(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemPlanEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getVeto_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchaseOrderItemPlanEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchaseGoodsRequestService.getPurchaseOrderItemPlan(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			purchaseGoodsRequestService.vetoOrderItemPlans(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.VETO_STATUS_VETO,vo.getVetoReason());
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
/*	@RequestMapping(value = "unVetoOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unVetoOrderItemPlan(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchaseOrderItemPlanEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getVeto_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchaseOrderItemPlanEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchaseGoodsRequestService.getPurchaseOrderItemPlan(Long.valueOf(str)));
			}
		}
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			purchaseGoodsRequestService.unVetoOrderItemPlan(orderItemPlanList, new UserEntity(user.id),PurchaseConstans.VETO_STATUS_AGREE,vo.getVetoReason());
			//通过id获取要货计划的的信息
			PurchaseGoodsRequestEntity  goods = purchaseGoodsRequestService.getPurchaseGoodsRequestById(orderItemPlanList.get(0).getPurchaseGoodsRequest().getId());
			map.put("message", "同意供应商驳回");
			map.put("success", true);
			map.put("goods", goods);
		}else{
			map.put("message", "同意供应商驳回失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}*/
	
	
	/**
	 * 采购订单明细列表导出[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("exportExcel/{goodsId}/{vendor}")
	public String download(@PathVariable("vendor")Boolean vendor,@PathVariable("goodsId")Long goodsId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("要货计划明细", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_goodsRequestId", goodsId + "");
		searchParamMap.put("EQ_abolished", "0");
		
		if(vendor) {//供应商
			//显示供货计划的数据
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
		}else{//采购商

		}
		searchParamMap.put("EQ_goodsRequestId", goodsId + "");
		searchParamMap.put("EQ_abolished", "0");
	
		List<PurchaseOrderItemPlanTransfer> list = purchaseGoodsRequestService.getPurchaseGoodsRequestItemExport(searchParamMap);
		
		//讲数据转换成导出的list
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchaseOrderItemPlanTransfer.class), null);  
		excelUtil.export(list);  
		return null;    
	}

	
	/*导出要货计划主项目*/
	@RequestMapping(value = "exportExcel/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exportExcel(@PathVariable("vendor")Boolean vendor, HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String ,Object> param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
			
			searchParamMap.put("EQ_flag", param.get("search-EQ_flag"));
			searchParamMap.put("EQ_publishStatus", param.get("search-EQ_publishStatus"));
			searchParamMap.put("EQ_vendorConfirmStatus", param.get("search-EQ_vendorConfirmStatus"));
			searchParamMap.put("GTE_rq", param.get("search-GTE_rq"));
			searchParamMap.put("IN_backlogId", param.get("search-IN_backlogId"));
			searchParamMap.put("IN_material.code", param.get("search-IN_material.code"));
			searchParamMap.put("IN_vendor.code", param.get("search-IN_vendor.code"));
			searchParamMap.put("LIKE_group.name", param.get("search-LIKE_group.name"));
			searchParamMap.put("LTE_rq", param.get("search-LTE_rq"));
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
			searchParamMap.put("NEQ_dhsl", "0.000");
		}else{
			
			//采购组权限
			searchParamMap.put("IN_group.id", buyerOrgPermissionUtil.getGroupIds());
		}
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			//String backlogId = searchParamMap.get("IN_backlogId").toString();
			//如果是那句
			searchParamMap.put("NEQ_vendorConfirmStatus", 1);
		}
			searchParamMap.remove("IN_backlogId");
		//待办结束
		
		
		List<String> dateStrList =purchaseGoodsRequestService.getDateStrList(searchParamMap.get("GTE_rq").toString(), searchParamMap.get("LTE_rq").toString());
		
		List<PurchaseGoodsRequestVO> list = purchaseGoodsRequestService.getRequestRowListExcel(searchParamMap,dateStrList);
		
		if(list.size()>0){
			
		       SimpleDateFormat  simp = new SimpleDateFormat("yyyyMMddHHmm");
		       String fileName ="要货计划"+simp.format(new Date())+ ".xls";
		       String filePath = PropertiesUtil.getProperty("filePath","");
		       String localfileName = filePath +fileName;
		       
		         List<String> headList = new ArrayList<String>();
		         headList.add("确认状态");//0
		         headList.add("是否新品");//1
		         headList.add("工厂编码");//2
		         headList.add("工厂名称");//3
		         headList.add("采购组");//4
		         headList.add("物料号");//5
		         headList.add("物料描述");//6
		         headList.add("基本单位");//7
		         headList.add("供应商名称");//8
		         headList.add("供应商编码");//9
		         headList.add("发布状态");//10
		         headList.add("类型");//11
		         if(dateStrList.size()>0){
		        	 for(String str:dateStrList){
		        		  headList.add(str);
			         }
		         }
		        
		         int pageSize = 8000;
		         int sheetCount = list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
		         HSSFWorkbook wb = new HSSFWorkbook();
		         HSSFCell cell = null;
		         HSSFSheet sheet = null;
		         HSSFRow row = null;
		         HSSFCellStyle cellStyleTitle = wb.createCellStyle();
		         cellStyleTitle.setAlignment((short)2);
		         cellStyleTitle.setVerticalAlignment((short)1);
		         cellStyleTitle.setWrapText(false);
		         HSSFCellStyle cellStyle = wb.createCellStyle();
		         cellStyle.setAlignment((short)2);
		         cellStyle.setVerticalAlignment((short)1);
		         cellStyle.setWrapText(false);
		         HSSFFont font = wb.createFont();
		         font.setBoldweight((short)700);
		         font.setFontName("宋体");
		         font.setFontHeight((short)200);
		         cellStyleTitle.setFont(font);
		         
		         HSSFFont tFont2 = wb.createFont();
				 tFont2.setFontName("宋体");
				 tFont2.setFontHeightInPoints((short) 11);
				    
				 HSSFCellStyle tStyle2 = wb.createCellStyle(); 
				 tStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				 tStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				 tStyle2.setFillForegroundColor(IndexedColors.BLUE.getIndex());
				 tStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 tStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				 tStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				 tStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				 tStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				 tStyle2.setFont(tFont2);
		       
		         for (int i = 1; i <= sheetCount; i++) {
		           sheet = wb.createSheet("要货计划_"+i);// 创建sheet页面
		           row = sheet.createRow(0);// 第一行为标题
		           row.setHeightInPoints(20.0F);
		           sheet.setDefaultColumnWidth(20);
		           int j = 0; 
		           for (int k = headList.size(); j < k; j++) {
		             cell = row.createCell(j);
		             cell.setCellStyle(tStyle2);
		             cell.setCellValue(new HSSFRichTextString((String)headList.get(j)));
		           }
		           int begin = (i - 1) * pageSize;
		           int end = begin + pageSize > list.size() ? list.size() : begin + pageSize;
		           int rowCount = 1;
		           PurchaseGoodsRequestVO obj = null;
		           for (int j1 = begin; j1 < end; j1++) {
		             row = sheet.createRow(rowCount);
		             rowCount++;
		             obj = (PurchaseGoodsRequestVO)list.get(j1);
		             cell = null;
		             cell = row.createCell(0);
		             cell.setCellStyle(cellStyle);
		             
		             Object vendorConfirmStatus = obj.getVendorConfirmStatus();
		             String  vendorStatus = "";
		             if(vendorConfirmStatus!=null){
		            	 String str = vendorConfirmStatus.toString();
		            	 if("-2".equals(str)){
		            		 vendorStatus = "驳回拒绝";
		            	 }else if("2".equals(str)){
		            		 vendorStatus = "部分确认";
		            	 }else if("-1".equals(str)){
		            		 vendorStatus = "驳回";
		            	 }else if("1".equals(str)){
		            		 vendorStatus = "已确认";
		            	 }else if("0".equals(str)){
		            		 vendorStatus = "待确认";
		            	 }
		             }
		             cell.setCellValue(vendorStatus);
		             
		             Object flag =  obj.getFlag();
		             String f = "";
		             if(flag!=null){
		            	 if("X".equals(flag)){
		            		 f = "新品";
		            	 }else{
		            		 f = "量产";
		            	 }
		             }
		             cell = row.createCell(1);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(f);
		             
		             cell = row.createCell(2);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getFactoryCode());
		             
		             cell = row.createCell(3);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getFactoryName());
		             
		             cell = row.createCell(4);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getGroupName());
		             
		             cell = row.createCell(5);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getMaterialCode());
		             
		             cell = row.createCell(6);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getMaterialName());
		             
		             cell = row.createCell(7);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getMeins());
		             
		             cell = row.createCell(8);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getVendorName());
		             
		             cell = row.createCell(9);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(obj.getVendorCode());
		             
		             
		             Object publishStatus =  obj.getPublishStatus();
		             String pStatus = "";
		             if(publishStatus!=null){
		            	 String str = publishStatus.toString();
		            	 if("0".equals(str)){
		            		 pStatus = "未发布";
		            	 }else if("1".equals(str)){
		            		 pStatus = "已发布";
		            	 }else if("2".equals(str)){
		            		 pStatus = "部分发布";
		            	 }
		             }
		             cell = row.createCell(10);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(pStatus);
		             
		             
		             Object type =  obj.getType();
		             String t = "";
		             if(type!=null){
		            	 String str = type.toString();
		            	 if("1".equals(str)){
		            		 t = "到货数量";
		            	 }else if("2".equals(str)){
		            		 t = "剩余匹配量";
		            	 }
		             }
		             cell = row.createCell(11);
		             cell.setCellStyle(cellStyle);
		             cell.setCellValue(t);
		             
		             int c=0;
		             int b=0;
		             if(dateStrList.size()>0){
		            	 String stri =  obj.toString().replace("PurchaseGoodsRequestVO [", "").replace("]", "");
		            	  for(int k=0;k<dateStrList.size();k++){
		            		 c++;
		            		 cell = row.createCell(11+c);
		 		             cell.setCellStyle(cellStyle);
		 		             String[] str = stri.split(",");
		 		             String col = "col"+b;
		 		             for(String st:str){
		 		            	 String[] s = st.split("=");
		 		            	 if(col.equals(s[0].trim())){
		 		            		if(s.length==1){
		 		            			 cell.setCellValue("");
		 		            		 }else{
		 		            			cell.setCellValue(s[1]);
		 		            		 }
				 		            
		 		            	 }
		 		             }
		 		             b++;
				         }
			         }
		             
		            
		           }
		         }
		         list = null;
		         headList = null;
		         File file = new File(localfileName);
		         if (!file.exists()) {
		           new File(file.getParent()).mkdirs();
		         }
			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(file));
				wb.write(out);
				out.flush();
				resultMap.put("code", true);
				resultMap.put("fileName", fileName);
				resultMap.put("msg", "生成采购员看板excel成功");
			} catch (Exception e) {
				resultMap.put("code", false);
				resultMap.put("fileName", "");
				resultMap.put("msg", "生成采购员看板excel失败");
			}finally{
				try {
					out.close();
				} catch (Exception e2) {
					resultMap.put("code", false);
					resultMap.put("fileName", "");
					resultMap.put("msg", "生成采购员看板excel失败");
				}
			}
		}else{
			resultMap.put("code", false);
			resultMap.put("fileName", "");
			resultMap.put("msg", "生成采购计划看板excel失败,无数据");
		}
	    
	} catch (Exception e) {
		resultMap.put("code", false);
		resultMap.put("fileName", "");
		resultMap.put("msg", "生成采购员看板excel失败,请联系技术人员!");
	}
	
	return resultMap;	    
	}
	
	
	@RequestMapping(value="/downloadExcel", method = RequestMethod.GET)
	public void downloadExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String fileName = new String(request.getParameter("fileName").getBytes("iso8859-1"),"UTF-8");
			String filePath = PropertiesUtil.getProperty("filePath","");
			String localName = filePath + fileName;
			File file = new File(localName);
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			response.addHeader("Content-Length", String.valueOf(file.length()));
			InputStream fis = new BufferedInputStream(new FileInputStream(
					localName));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//修改要货计划（主）
	@RequestMapping(value = "editMainGoodsRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editMainGoodsRequest(@RequestParam(value="datas") String datas) throws Exception{
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		List<PurchaseGoodsRequestVO> list = new ArrayList<PurchaseGoodsRequestVO>();
		PurchaseGoodsRequestVO vo;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			vo = new PurchaseGoodsRequestVO();
			vo.setId(StringUtils.convertToLong(object.get("id") + ""));
		    vo.setDhsl(StringUtils.convertToDouble(object.get("dhsl") + ""));
		    list.add(vo);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map = purchaseGoodsRequestService.editMainGoodsRequest(list);
		return map;
	}
	
	
	/**
	 * 根据工厂和物料同步对应的要货计划
	 * @param orderList
	 * @return
	 */
	@RequestMapping("sycGoodsRequestByFactoryCodeAndMateriel/{factoryCode}/{materialcode}")
	@ResponseBody
	public Map<String,Object> sycGoodsRequestByFactoryCodeAndMateriel(@PathVariable("factoryCode")String factoryCode,@PathVariable("materialcode")String materialcode) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		
		//查询工厂是否存在
		FactoryEntity factory = factoryService.findByCodeAndAbolished(factoryCode,0);
		if(factory == null){
			map.put("message", "该工厂不存在或者该工厂未生效！");
			map.put("success", false);
			return map;
		}
		//查询物料是否存在
		
		List<String> materialsList =new  ArrayList<String>();
		
		if(  !StringUtils.isEmpty(materialcode) && !materialcode.equals("null")  ){
			String [] codes=materialcode.split(",");
			if(null!=codes && codes.length>0){
				   for(String str : codes){
					   materialsList.add(str);
				   }
				}
		}
		
		for(String code : materialsList){
			List<MaterialEntity> materialList = materialService.findByCodeAndAbolished(code,0);
			if(materialList == null || materialList.size() == 0){
				map.put("message", "物料"+code+"不存在或者该物料未生效！");
				map.put("success", false);
				return map;
			}
		}
		
		//查询工厂和物料是否存在关系
		for(String code : materialsList){
			List<VendorMaterialRelEntity> rel = vendorMaterialRelService.getVendorMaterialRelByFactoryCodeAndMaterialCode(code,factoryCode,0);
			if(rel == null  || rel.size() == 0){
				map.put("message", "该工厂和物料"+code+"没有匹配关系或未生效！");
				map.put("success", false);
				return map;
			}
		}

		
		//添加list
		List<String> list =  new  ArrayList<String>();
		for(String code : materialsList){
			List<MaterialEntity> templList = materialService.findByCodeAndAbolished(code,0);
			list.add(templList.get(0).getCode());
		}
		
		
		PurchaseGoodsRequestSyncService purchaseGoodsRequestSyncService = SpringContextUtils.getBean("purchaseGoodsRequestSyncService");	
		purchaseGoodsRequestSyncService.manualSync(logger,factory,list);
		
		map.put("message", "同步要货计划成功");
		map.put("success", true);
		
		return map;
	}
	
	
	//*********************************要货报表相关*******************************************//
	
	@RequestMapping(value="report", method = RequestMethod.GET)
	public String repostlist(Model model) {
		return "back/orderGoodsRequret/goodsRequestReport";
	}
	
	
	//获取表头数据
	@RequestMapping(value = "goodsReportHeadGet",method = RequestMethod.POST)
	@ResponseBody
	public GoodsRequestReportHeadViewVo goodsReportHeadGet() throws Exception{  
		return purchaseGoodsRequestService.goodsReportHeadGet();
	}

	@RequestMapping(value="getGoodsReportList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getGoodsReportList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception {
		map = new HashMap<String, Object>();

		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("IN_group.id", buyerOrgPermissionUtil.getGroupIds());
		
		String vendorCodes = searchParamMap.get("IN_vendor.code")+"";
		String materialCodes = searchParamMap.get("IN_material.code")+"";
		
		List<String> codeList =new  ArrayList<String>();
		List<String> materialsList =new  ArrayList<String>();
		
		if(  !StringUtils.isEmpty(vendorCodes) && !vendorCodes.equals("null")  ){
			String [] codes=vendorCodes.split(",");
			if(null!=codes && codes.length>0){
				   for(String str : codes){
					   codeList.add(str);
				   }
				}
			searchParamMap.put("IN_vendor.code",codeList );
		}
		
		if(  !StringUtils.isEmpty(materialCodes) && !materialCodes.equals("null")){
			String [] materials=materialCodes.split(",");
			if(null!=materials && materials.length>0){
				   for(String str : materials){
					   materialsList.add(str);
				  }
			}
			searchParamMap.put("IN_material.code",materialsList);
		}

		Page<GoodsRequestReportVo> page=purchaseGoodsRequestService.getPurchaseVenodrPlans(pageNumber,pageSize,searchParamMap);
		
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
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
