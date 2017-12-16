package com.qeweb.scm.purchasemodule.web.delivery;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springside.modules.web.Servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;

/**
 * 采购发货单管理
 * @author alex
 * @date 2015年5月4日
 * @path com.qeweb.scm.purchasemodule.web.delivery.DeliveryController.java
 */
@Controller
@RequestMapping(value = "/manager/order/delivery")
public class DeliveryController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	/**
	 * 发货看板
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pending",method = RequestMethod.GET)
	public String pendingList(Model model) {
		return "back/delivery/deliveryPendingList";
	}
	
	/**
	 * 发货看板列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="pending",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPendingList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_confirmStatus", PurchaseConstans.PUBLISH_STATUS_YES + "");		//已发布
		searchParamMap.put("EQ_order.confirmStatus", PurchaseConstans.CONFIRM_STATUS_YES + "");	//已确认
		searchParamMap.put("EQ_order.closeStatus", PurchaseConstans.CLOSE_STATUS_NO + "");		//未关闭
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + "");						//未删除
		searchParamMap.put("NEQ_deliveryStatus", PurchaseConstans.DELIVERY_STATUS_YES + "");	//不等于已发货
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_order.vendor.id", user.orgId + "");    
		Page<PurchaseOrderItemPlanEntity> userPage = deliveryService.getPendingDeliverys(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * TO创建发货单
	 * @return
	 */
	@RequestMapping("createDelivery")
	@ResponseBody
	public DeliveryEntity createDelivery() {
		DeliveryEntity delivery = new DeliveryEntity();
		delivery.setDeliveryCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.DELIVERY)); 
		return delivery;
	}
	
	/**
	 * TO创建发货单明细
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="createDeliveryItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDeliveryItem(Model model,ServletRequest request){
		if(request.getParameter("search-IN_id") == null)
			return null;
		
		String[] search = request.getParameter("search-IN_id").split(",");
		long[] ids = StringUtils.convertToLong(search);
		List<Long> list = new ArrayList<Long>();
		for (long id : ids)
			list.add(id);
		List<PurchaseOrderItemPlanEntity> userPage = deliveryService.getPendingDeliverys(list);
		map = new HashMap<String, Object>();
		map.put("rows", userPage);
		map.put("total", userPage.size());
		return map;
	}
	
	/**
	 * 保存发货单
	 * @param deliveryCode 发货单号
	 * @param datas 发货明细
	 * @param type 0:保存 1：保存并发布
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "saveDelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveDelivery(@RequestParam("deliveryCode") String deliveryCode, @RequestParam(value="datas") String datas, @RequestParam(value="type") String type) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		DeliveryEntity delivery = new DeliveryEntity();
		delivery.setDeliveryCode(deliveryCode);
		delivery.setStatus(PurchaseConstans.COMMON_NORMAL);
		List<DeliveryItemEntity> deliveryItem = new ArrayList<DeliveryItemEntity>();
		DeliveryItemEntity item = null;
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new DeliveryItemEntity();
			item.setItemNo(i + 1);
			item.setOrderItemPlanId(StringUtils.convertToLong(object.get("id") + ""));
			item.setDeliveryQty(StringUtils.convertToDouble(object.get("sendQty") + ""));
			deliveryItem.add(item);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		deliveryService.saveDelivery(delivery, deliveryItem, new UserEntity(user.id), type);
		map.put("message", "保存发货单成功");
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 发货记录
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {   
		return "back/delivery/deliveryList";
	}
	
	/**
	 * 发货记录列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + "");						//未删除
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_vendor.id", user.orgId + "");       
		Page<DeliveryEntity> userPage = deliveryService.getDeliverys(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());   
		return map;
	}
	
	/**
	 * 获取发货主单信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getDelivery/{id}")
	@ResponseBody
	public DeliveryEntity getDelivery(@PathVariable("id") Long id){
		//request.getSession().setAttribute("deliverId", id);
		return deliveryService.getDeliveryById(id);
	}
	
	/**
	 * 查看发货单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deliveryitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long deliveryid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_delivery.id", deliveryid + "");
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "byorderdeliveryitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByOrder(@PathVariable("id") Long orderid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.order.id", orderid + "");   
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 发货
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "dodelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deliverys(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		deliveryService.dodelivery(deliveryList, new UserEntity(user.id));
		map.put("message", "发货成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 取消发货
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "canceldelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> canceldeliverys(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		deliveryService.cancelDelivery(deliveryList, new UserEntity(user.id, user.orgId));
		map.put("message", "取消发货成功");
		map.put("success", true);
		return map;
	}
	
}
