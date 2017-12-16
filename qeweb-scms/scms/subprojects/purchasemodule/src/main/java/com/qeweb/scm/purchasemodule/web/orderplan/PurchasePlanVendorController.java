package com.qeweb.scm.purchasemodule.web.orderplan;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVenodrItemEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanService;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;

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
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/orderplan/vendorOrderPlanList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_vendor.id", user.orgId + "");  
		searchParamMap.put("EQ_plan.publishStatus", PurchaseConstans.PUBLISH_STATUS_YES + "");  
		Page<PurchasePlanVendorEntity> userPage = purchasePlanService.getPurchaseVenodrPlans(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getPlan/{id}")
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
	
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("采购计划", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = new HashMap<String, Object>();   
		searchParamMap.put("EQ_vendorPlan.id", request.getParameter("id"));  
		List<PurchasePlanTransfer> list = purchasePlanService.getPurchasePlanVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchasePlanTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	
}
