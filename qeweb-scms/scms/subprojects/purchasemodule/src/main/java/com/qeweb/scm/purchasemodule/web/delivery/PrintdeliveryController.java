package com.qeweb.scm.purchasemodule.web.delivery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
@RequestMapping(value = "/manager/order/printdelivery")
public class PrintdeliveryController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	
	@RequestMapping(value="pending/{id}",method = RequestMethod.GET)
	public String pendingList(@PathVariable("id") Long id,Model model) {
		DeliveryEntity deliveryInfo=deliveryService.getDeliveryById(id);
		model.addAttribute("delivery_Info", deliveryInfo);
		model.addAttribute("list", deliveryService.getDeliveryItems(deliveryInfo));
		return "back/delivery/printDeliveryorder";

	}
	
	@RequestMapping(value="pending_metaldyne/{id}",method = RequestMethod.GET)
	public String metaldynePendingList(@PathVariable("id") Long id,Model model) {
		DeliveryEntity deliveryInfo=deliveryService.getDeliveryById(id);
		model.addAttribute("delivery_Info", deliveryInfo);
		model.addAttribute("list", deliveryService.getDeliveryItems(deliveryInfo));
		List<DeliveryItemEntity> itemEntityList= deliveryService.getDeliveryItems(deliveryInfo);
		if(itemEntityList!=null){
			for (DeliveryItemEntity deliveryItemEntity : itemEntityList) {
				model.addAttribute("isbh", deliveryItemEntity.getOrderItem().getOrder().getOrderType());
			}
		}
		
		return "back/delivery/printDeliveryorder_metaldyne";

	}

}
