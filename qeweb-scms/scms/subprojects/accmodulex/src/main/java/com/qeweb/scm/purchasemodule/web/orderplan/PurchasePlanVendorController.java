package com.qeweb.scm.purchasemodule.web.orderplan;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityInfoEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanService;
import com.qeweb.scm.purchasemodule.web.vo.AddCapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.CapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;

/**
 * 供应商采购计划管理
 * @author alex
 * @date 2015年4月20日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchasePlanVendorController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseplanvendor")
public class PurchasePlanVendorController {
	
	private Map<String,Object> map;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PurchasePlanService purchasePlanService;
	

	@Autowired
	private BacklogService backlogService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		Long planId = null;
		//判断是否是同一个大版本
		List<Long> vendorPlanIds =getVendorPlanIdsByBackLogId(backlogId);
		if(null!=vendorPlanIds && vendorPlanIds.size()>0){
			if(vendorPlanIds.size()==1){
				planId=purchasePlanService.findPlanIdByPlanVendorId(vendorPlanIds.get(0));
			}
		}
		model.addAttribute("vendorPlanId", planId);
		//待办end
		return "back/orderplan/vendorOrderPlanList";
	}
	
	/**
	 * 根据待办的预测计划明细获取大版本id集合
	 * @param backlogId
	 * @return
	 */
	public List<Long> getVendorPlanIdsByBackLogId(String backlogId){
		if(!StringUtils.isEmpty(backlogId)){
			List<Long> planItemIdList= backlogService.getBackLogValueIdsById(backlogId);
			return purchasePlanService.findPurchasePlanVendorIdsByItemIds(planItemIdList);
		}
		return null;
	}
	
	/**
	 * 打开预测计划详情页
	 * @author chao.gu
	 * 20170828
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderPlanInfoList")
	public String infolist(Model model) {
		String id=request.getParameter("id");
		model.addAttribute("id", id);
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		return "back/orderplan/vendorOrderPlanInfoList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_vendor.id", user.orgId + "");  
		searchParamMap.put("NEQ_plan.publishStatus", PurchaseConstans.PUBLISH_STATUS_NO + "");  
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> idList= getVendorPlanIdsByBackLogId(backlogId);
			searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		Page<PurchasePlanVendorEntity> userPage = purchasePlanService.getPurchaseVenodrPlans(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	//字表数据查询
	@RequestMapping(value = "vendorPlanItem/{planid}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_vendor.id", user.orgId+"");
		searchParamMap.put("EQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_YES + "");  
		searchParamMap.put("EQ_isNew", 1);  
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> idList= backlogService.getBackLogValueIdsById(backlogId);
			searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		
		Page<PurchasePlanItemEntity> userPage = purchasePlanService.getPurchasePlanItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 打开产能信息显示list页面查看（供应商页面）
	 * @param 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="showCapacityInfoList/{poPlanid}/{vendorPlanid}/{isVendor}", method = RequestMethod.GET)
	public String showCapacityInfoList(@PathVariable(value="poPlanid") Long poPlanid, @PathVariable(value="vendorPlanid") Long vendorPlanid,@PathVariable(value="isVendor") Boolean isVendor,Model model) {
		//判断当前登录人的身份
		model.addAttribute("isVendor", isVendor);
		model.addAttribute("poPlanid", poPlanid);
		model.addAttribute("vendorPlanid", vendorPlanid);
		return "back/orderplan/vendorCapacityInfoList";
	}
	
	/**
	 * 产能信息列表显示查看
	 * @param 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getCapacityInfo/{poPlanid}/{isVendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getCapacityInfo(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request,@PathVariable(value="poPlanid") Long poPlanid, @PathVariable(value="isVendor") Boolean isVendor) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_planItem.plan.id", poPlanid+"");
		searchParamMap.put("EQ_abolished", "0");
		if(isVendor){
			searchParamMap.put("EQ_planItem.vendor.id", user.orgId+""); 
		}
		
		Page<PurchasePlanVendorCapacityInfoEntity> userPage = purchasePlanService.getCapacityInfo(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
		
	}
	

	/**
	 * 打开产能信息显示查看（供应商页面）
	 * @param 
	 * @param model
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value="viewCapacityInfo/{id}", method = RequestMethod.GET)
	public String viewCapacityInfo(@PathVariable(value="id") Long id,Model model) throws IllegalArgumentException, IllegalAccessException {
		
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	    //根据供应商的id获取设置的产能表信息
		List<CapacityVO> cvList = purchasePlanService.showCapacityInfo(user.orgId);
		//根据订单行id获取填写的产能表信息
		PurchasePlanVendorCapacityInfoEntity purchasePlanVendorCapacityInfo = purchasePlanService.getCapacityInfoByPlanItem(id);
		
		List<CapacityVO> newCapacityList = new  ArrayList<CapacityVO>();//显示效果（code：项目名称    name ：值）
		Class clazz1 = purchasePlanVendorCapacityInfo.getClass();  
		Field[] fs1 = clazz1.getDeclaredFields();  
        for(int i = 0;i<fs1.length;i++){
        	Field f1 = fs1[i];
        	f1.setAccessible(true); //设置些属性是可以访问的  
            Object val1 = f1.get(purchasePlanVendorCapacityInfo);//得到此属性的值 
            for(CapacityVO cv : cvList){
            	if(f1.getName().equals(cv.getCode())){
            		CapacityVO tem = new CapacityVO();
            		tem.setCode(cv.getName());
            		tem.setName(val1 == null ? "" :  val1.toString());
            		newCapacityList.add(tem);
            		break;
            	}
            }
        }
        model.addAttribute("viewData",newCapacityList);
		return "back/orderplan/viewVendorCapacityInfo";
	}
	
	
	
	/**
	 * 产能信息显示新增（供应商页面）
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="showCapacityInfo/{poPlanid}/{vendorPlanid}", method = RequestMethod.GET)
	public String showCapacityInfo(@PathVariable(value="poPlanid") Long poPlanid, @PathVariable(value="vendorPlanid") Long vendorPlanid,Model model) {
		//获取当前人产能表的信息(未设置的显示全部)
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<CapacityVO> cvList = purchasePlanService.showCapacityInfo(user.orgId);
		
		model.addAttribute("poPlanid", poPlanid);
		model.addAttribute("vendorPlanid", vendorPlanid);
		model.addAttribute("cvList", cvList);
		return "back/orderplan/vendorCapacityInfo";
	}
	
	
	/**
	 * 产能信息增加（供应商页面）
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="addShowCapacityInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addShowCapacityInfo(@Valid AddCapacityVO addCapacityVo) {
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.addCapacityInfo(addCapacityVo);
		map.put("success", true);
		return map;
		
	}
	


	
	//供应商同意采购计划
	@RequestMapping(value = "confirmItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmItemPlan(@RequestBody List<PurchasePlanItemEntity> planList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.confirmPlans(planList);
		map.put("success", true);
		return map;
	}
	
	//供应商驳回采购计划
	@RequestMapping(value = "rejectItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectItemPlan(RejectVO vo){
		
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchasePlanItemEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getReject_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchasePlanItemEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchasePlanService.getOrderItemPlan(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			purchasePlanService.rejectPlans(orderItemPlanList,vo.getReject_reason());
			map.put("message", "驳回订单成功");
			map.put("success", true);
		}else{
			map.put("message", "驳回订单失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	
	}
	
	
	
/*	@RequestMapping("getPlan/{id}")
	@ResponseBody
	public PurchasePlanVendorEntity getPlan(@PathVariable("id") Long id){
		return purchasePlanService.getVendorPlan(id);
	}
	
	@RequestMapping(value = "planitem/{planid}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_vendorPlan.id", planid+"");
		Page<PurchasePlanVenodrItemEntity> userPage = purchasePlanService.getPurchasePlanVendorItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "confirmPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmPlan(@RequestBody List<PurchasePlanVendorEntity> planList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.confirmPlans(planList);;
		map.put("success", true);
		return map;
	}
	*/
	
	//导出
	@RequestMapping("exportExcel/{planid}")
	@ResponseBody
	public String download(@PathVariable("planid") Long planid,Model model,ServletRequest request,HttpServletResponse  response) throws Exception {
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("预测计划", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_vendor.id", user.orgId+"");
		searchParamMap.put("EQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_YES + "");
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> planItemIdList= backlogService.getBackLogValueIdsById(backlogId);
			searchParamMap.put("IN_id", planItemIdList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		purchasePlanService.exportExcel(searchParamMap,planid,response);
		return null;   
	}
	
}
