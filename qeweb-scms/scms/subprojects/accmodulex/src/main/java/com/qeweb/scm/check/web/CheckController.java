package com.qeweb.scm.check.web;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.TimetaskSettingEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.FeedbackService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.check.entity.CheckEntity;
import com.qeweb.scm.check.entity.CheckExceptionEntity;
import com.qeweb.scm.check.entity.CheckInvoiceEntity;
import com.qeweb.scm.check.entity.CheckItemEntity;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.service.CheckService;
import com.qeweb.scm.check.service.NoCheckedItemsService;
import com.qeweb.scm.purchasemodule.constants.OrderType;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.InStorageService;
import com.qeweb.scm.purchasemodule.service.PurchaseOrderItemService;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.vo.CheckItemInBuyerTransfer;
import com.qeweb.scm.purchasemodule.web.vo.CheckItemInVendorTransfer;


/**
 * 未对账明细
 */
@Controller
@RequestMapping(value = "/manager/check/checks")
public class CheckController implements ILog {
	//add by zhangjiejun 2015.10.16 start
	private ILogger logger = new FileLogger();
	//add by zhangjiejun 2015.10.16 end
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CheckService service;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private InStorageService inStorageService;
	
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private PurchaseOrderItemService retService;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	NoCheckedItemsService  noCheckedItemsService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/check/buyer/checkList";
	}
	
	@RequestMapping(value="/abroad",method = RequestMethod.GET)
	public String abroadlist(Model model) {
		return "back/check/buyer/checkAbroadList";
	}
	
	@RequestMapping(value="/vendor",method = RequestMethod.GET)
	public String list_vendor(Model model) {
		return "back/check/vendor/checkList";
	}
	
	@RequestMapping(value="/abroadvendor",method = RequestMethod.GET)
	public String abroadvendor(Model model) {
		return "back/check/vendor/checkAbroadList";
	}
	
	@RequestMapping(value="/ex",method = RequestMethod.GET)
	public String list_ex(Model model) {
		return "/back/check/buyer/checkExList";
	}
	
	@RequestMapping(value="/exVendor",method = RequestMethod.GET)
	public String list_exVendor(Model model) {
		return "/back/check/vendor/checkExList";
	}
	
	@RequestMapping(value="/dateSetting",method = RequestMethod.GET)
	public String list_dateSetting(Model model) {
		model.addAttribute("defaultDateSetting", service.getDefauleDateSetting());
		return "/back/check/buyer/dateSetting";
	}

	/*@RequiresPermissions("check:list:view")*/
	@LogClass(method="查询对账单",module="对账管理")
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//searchParamMap.put("NEQ_type", OrderType.ABROAD+"");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		Page<CheckEntity> page = service.getChecks(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		for (CheckEntity checkEntity : page) {
			checkEntity.setFeedbackCount(feedbackService.findFeedBackCountByOrderItemId(checkEntity.getId()));
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@LogClass(method="查询国外对账单",module="对账管理")
	@RequestMapping(value="/getAbroadList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAbroadList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_type", OrderType.ABROAD+"");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		Page<CheckEntity> page = service.getChecks(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	/*@RequiresPermissions("check:ex:view")*/
	@LogClass(method="查询异常账单",module="对账管理")
	@RequestMapping(value="/getExceptionList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getExceptionList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		//用户权限
		//searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
		//组织权限
		//List<Long> checkItemIDList = checkService.getCheckItemIds(buyerOrgPermissionUtil.getBuyerIds());
		searchParamMap.put("IN_checkItem.check.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		//if(checkItemIDList != null && checkItemIDList.size()>0){
		//	searchParamMap.put("IN_checkItem.id", checkItemIDList);
		//}
		Page<CheckExceptionEntity> page = service.getExceptions(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		List<CheckExceptionEntity> pageList = page.getContent();
		for (CheckExceptionEntity checkExceptionEntity : pageList) {
			Long checkItemId = checkExceptionEntity.getCheckItem().getId();
			CheckItemEntity checkItem = service.getCheckItemsByCheckItemId(checkItemId);
			if(checkItem != null){
				Double buyerCheckPrice = checkItem.getBuyerCheckPrice();
				
				NoCheckItemsEntity entity =noCheckedItemsService.getByItemId(checkItem.getViewNoCheckItemId());
				
				checkExceptionEntity.setMaterialCode(entity.getMaterialCode());
				checkExceptionEntity.setMaterialName(entity.getMaterialName());
				checkExceptionEntity.setUnitName(entity.getUnitName());
				checkExceptionEntity.setCheckPrice(entity.getPrice());
				
				if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==checkItem.getSource()){
					checkExceptionEntity.setReceiveQty(entity.getRecQty());
					
				}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==checkItem.getSource()){
					checkExceptionEntity.setReceiveQty(entity.getOrderQty());
				}
				
				if(buyerCheckPrice!=null){
					logger.log("has buyerCheckPrice ...");
					checkExceptionEntity.setBuyerCheckPrice(buyerCheckPrice);
				}else{
					logger.log("has no buyerCheckPrice ...");
					
					logger.log("VendorCheckPrice == " + checkItem.getVendorCheckPrice());
					if(null!=checkItem.getVendorCheckPrice() && null!=checkItem.getCheckPrice() && checkItem.getVendorCheckPrice() != checkItem.getCheckPrice()){				//供应商填写单价不等于核价单价,采购商填写价格需要系统默认为核价价格
						logger.log("if(checkItem.getVendorCheckPrice() != d_attr3) set d_attr3 to vendorCheckPrice...");
						checkExceptionEntity.setBuyerCheckPrice(checkItem.getCheckPrice());
					}
				}
			
				checkExceptionEntity.setExDiscription(checkItem.getExDiscription());
			}
		}
		
		/*for(CheckExceptionEntity ex :page.getContent()){
			
		}*/
		map = new HashMap<String, Object>();
		//map.put("rows",page.getContent());
		map.put("rows",pageList);
		map.put("total",page.getTotalElements());
		return map;
	}

	@LogClass(method="查询对账单",module="对账管理")
	@RequestMapping(value="/getVendorAbroadList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getVendorAbroadList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_type", OrderType.ABROAD+"");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("vendor.id", user.orgId);
		searchParamMap.put("publishStatus", 1);
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		Page<CheckEntity> page = service.getChecks(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	/*@RequiresPermissions("check:list:v:view")*/
	@LogClass(method="查询对账单",module="对账管理")
	@RequestMapping(value="/getVendorList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getVendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("vendor.id", user.orgId);
		searchParamMap.put("publishStatus", PurchaseConstans.PUBLISH_STATUS_YES);
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		Page<CheckEntity> page = service.getChecks(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		for (CheckEntity checkEntity : page) {
			checkEntity.setFeedbackCount(feedbackService.findFeedBackCountByOrderItemId(checkEntity.getId()));
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	


	@LogClass(method="查看对账单",module="对账管理")
	@RequestMapping(value="viewDetail/{id}", method = RequestMethod.GET)
	public String viewDetail(@PathVariable(value="id") Long id, Model model) {
		logger.log("method viewDetail start");
		CheckEntity check = service.getCheckById(id);
		model.addAttribute("check", check);
		Set<CheckItemEntity> checkItemList = check.getCheckItem();
		double allAttr3 = 0;															//核价总金额
		double allBillAmount = 0;														//实际开票金额
		double allBuyerCheckPrice = 0;													//采购核对总金额
		double allVendorCheckPrice = 0;													//供应商核对总金额
		
		Double claimAmount = check.getClaimAmount();									//获取索赔金额
		if(claimAmount==null){
			claimAmount = 0.0;									
		}
		for (CheckItemEntity checkItem : checkItemList) {
			Double checkPrice = checkItem.getCheckPrice();											//核价价格
			Double vendorCheckPrice = checkItem.getVendorCheckPrice();					//供应商核对价格
			Double buyerCheckPrice = checkItem.getVendorCheckPrice();					//采购组织核对价格
			vendorCheckPrice = vendorCheckPrice == null ? 0 : vendorCheckPrice;
			buyerCheckPrice = buyerCheckPrice == null ? 0 : buyerCheckPrice;
			
			NoCheckItemsEntity  entity  = noCheckedItemsService.getByItemId(checkItem.getViewNoCheckItemId());
			
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==checkItem.getSource()){
				Double receiveQty = entity.getRecQty();								//收货数量
				allAttr3 = BigDecimalUtil.add(allAttr3,BigDecimalUtil.mul(receiveQty, null==checkPrice?0:checkPrice));	//计算核价总金额
				allBuyerCheckPrice = BigDecimalUtil.add(allBuyerCheckPrice,BigDecimalUtil.mul(receiveQty, buyerCheckPrice));		//计算采购核对总金额
				allVendorCheckPrice = BigDecimalUtil.add(allVendorCheckPrice,BigDecimalUtil.mul(receiveQty, vendorCheckPrice));	//计算供应商核对总金额
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==checkItem.getSource()){
				Double receiveQty = entity.getOrderQty();
				allAttr3 = BigDecimalUtil.sub(allAttr3,BigDecimalUtil.mul(receiveQty, null==checkPrice?0:checkPrice));	//计算核价总金额
				allBuyerCheckPrice = BigDecimalUtil.sub(allBuyerCheckPrice,BigDecimalUtil.mul(receiveQty, buyerCheckPrice));		//计算采购核对总金额
				allVendorCheckPrice = BigDecimalUtil.add(allVendorCheckPrice,BigDecimalUtil.mul(receiveQty, vendorCheckPrice));	//计算供应商核对总金额
			}
			

		}
		allAttr3 = BigDecimalUtil.add(allAttr3, claimAmount);
		allBuyerCheckPrice = BigDecimalUtil.add(allBuyerCheckPrice, claimAmount);
		allVendorCheckPrice = BigDecimalUtil.add(allVendorCheckPrice, claimAmount);
				
		List<CheckInvoiceEntity> list = service.getInvoicesByCheckId(id);
		for (CheckInvoiceEntity checkInvoice : list) {
			Double noTaxAmount = checkInvoice.getNoTaxAmount();
			noTaxAmount = noTaxAmount == null ? 0 : noTaxAmount;
			allBillAmount += noTaxAmount;												//税前金额之和
		}
		model.addAttribute("allAttr3", allAttr3);
		model.addAttribute("allBillAmount", allBillAmount);
		model.addAttribute("allBuyerCheckPrice", allBuyerCheckPrice);
		model.addAttribute("allVendorCheckPrice", allVendorCheckPrice);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		OrganizationEntity org = orgService.getOrg(user.orgId);
		logger.log("allAttr3 == " + allAttr3);
		logger.log("allBillAmount == " + allBillAmount);
		logger.log("allBuyerCheckPrice == " + allBuyerCheckPrice);
		logger.log("allVendorCheckPrice == " + allVendorCheckPrice);
		logger.log("method viewDetail end");
		if(org.getRoleType()==OrgType.ROLE_TYPE_VENDOR){
			return "back/check/vendor/checkView";
		}
		return "back/check/buyer/checkView";
	}

	@LogClass(method="查看对账单",module="对账管理")
	@RequestMapping(value="showAbroadDetail/{id}", method = RequestMethod.GET)
	public String showAbroadDetail(@PathVariable(value="id") Long id, Model model) {
		CheckEntity check = service.getCheckById(id);
		Set<CheckItemEntity> set = check.getCheckItem();
		DeliveryEntity delivery = null;
		double allCheckPrice = 0;														//核价总金额
		if(set != null && set.size() != 0){
			for (CheckItemEntity checkItemEntity : set) {
				if(checkItemEntity != null){
					ReceiveItemEntity recItem = checkItemEntity.getRecItem();
					DeliveryItemEntity deliveryItem = recItem.getDeliveryItem();
					if(delivery == null){
						delivery = deliveryItem.getDelivery();
						logger.log("has delivery id == " + delivery.getId());
					}
					
					double receiveQty = recItem.getReceiveQty();						//国外获取的是收货明细的收货数量
					String attr3 = recItem.getAttr3();									//核价价格
					Double checkPrice = 0.0;
					if(attr3 != null && !attr3.equals("")){
						checkPrice = Double.parseDouble(recItem.getAttr3());			//核价价格
					}
					allCheckPrice = BigDecimalUtil.add(allCheckPrice, BigDecimalUtil.mul(checkPrice, receiveQty));		//核价价格 * 数量
				}
			}
		}
		double allInvoicePrice =0;		//开票金额
		if(check.getBillAmount()!=null){
			allInvoicePrice=check.getBillAmount();
		}
		
		if(StringUtils.isEmpty(check.getCol1())){
			//差异价格=开票金额-核价总金额
			check.setCol1(StringUtils.convertToString(allInvoicePrice-allCheckPrice));
		}
		model.addAttribute("check", check);
		model.addAttribute("delivery", delivery);
		model.addAttribute("allCheckPrice", allCheckPrice);
		logger.log("allCheckPrice == " + allCheckPrice);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		OrganizationEntity org = orgService.getOrg(user.orgId);
		if(org.getRoleType()==OrgType.ROLE_TYPE_VENDOR){
			return "back/check/vendor/checkAbroadView";
		}
		return "back/check/buyer/checkAbroadView";
	}
	
//	@RequestMapping(value="modInvoice/{id}", method = RequestMethod.GET)
//	public String modInvoice(@PathVariable(value="id") Long id, Model model) {
//		CheckInvoiceEntity invoice = service.getInvoicesById(id);
//		model.addAttribute("invoice", invoice);
//		return "back/metaldyne/check/vendor/modInvoice";
//	}
	
	@RequestMapping(value="modInvoice/{id}", method = RequestMethod.GET)
	public String modInvoice(@PathVariable(value="id") Long id, Model model) {
		CheckInvoiceEntity invoice = service.getInvoicesById(id);
		/*CheckEntity check = invoice.getCheck();
		Set<CheckItemEntity> checkItem = check.getCheckItem();*/
		List<CheckItemEntity> checkItem = service.getCheckItemsByInvoiceCode(invoice.getCode());
		String pks = "";
		for(CheckItemEntity entity : checkItem) {
			pks += entity.getId() + ",";
		}
		if(!pks.equals("")){
			pks = pks.substring(0, pks.length() -1);
		}
		model.addAttribute("bill_checkItem_pks", pks);
		model.addAttribute("invoice", invoice);
		return "back/check/vendor/modInvoice2";
	}
	
	@RequestMapping(value="modClaimInvoice/{id}", method = RequestMethod.GET)
	public String modClaimInvoice(@PathVariable(value="id") Long id, Model model) {
		CheckInvoiceEntity invoice = service.getInvoicesById(id);
		CheckEntity check = invoice.getCheck();
		model.addAttribute("check", check);
		model.addAttribute("invoice", invoice);
		return "back/check/vendor/modClaimInvoice";
	}

	@LogClass(method="保存发票",module="对账管理")
	@RequestMapping(value="/opt/saveInvoice/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveInvoice(@PathVariable(value="id") Long id, CheckInvoiceEntity invoice, @RequestParam("file") MultipartFile file) throws IOException {
		logger.log("method saveInvoice start");
		File temp = null;
		if(file != null && file.getSize() > 0) {
			temp = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			invoice.setInvoiceFilePath(temp.getPath());
			invoice.setInvoiceFileName(file.getOriginalFilename()); 
		}
		logger.log("temp == " + temp);
		logger.log("getInvoiceFileName == " + invoice.getInvoiceFileName());
		service.saveInvoice(invoice, id);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		logger.log("method saveInvoice end");
		return map;
	}
	
	@RequestMapping(value="/getItemsByCheckId/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getCheckItemsByCheckId(@PathVariable(value="id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		List<CheckItemEntity> list = service.getCheckItemsByCheckId(id);
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Page<CheckItemEntity> page =new PageImpl<CheckItemEntity>(list, pagin, list.size());
		for (CheckItemEntity checkItemEntity : page) {	
			NoCheckItemsEntity entity =noCheckedItemsService.getByItemId(checkItemEntity.getViewNoCheckItemId());
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==checkItemEntity.getSource()){
				
				checkItemEntity.setReceiveTime(entity.getCreateTime());
				checkItemEntity.setMaterialCode(entity.getMaterialCode());
				checkItemEntity.setMaterialName(entity.getMaterialName());	
				checkItemEntity.setUnitName(entity.getUnitName());
				checkItemEntity.setDeliveryCode(entity.getOrderCode());
				//checkItemEntity.setVendorBatchNum(receiveItem.getDeliveryItem().getCol7());
				checkItemEntity.setReceiveCode(entity.getOrderCode());
				//checkItemEntity.setBuyerCheckPrice(entity.getPrice());
				checkItemEntity.setReceiveQty(entity.getRecQty());
				checkItemEntity.setCheckPrice(entity.getPrice());
				checkItemEntity.setPrice(entity.getPrice()==null?"":String.valueOf(entity.getPrice()));
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==checkItemEntity.getSource()){
				checkItemEntity.setRetrunCode(entity.getOrderCode());
				checkItemEntity.setReceiveTime(entity.getCreateTime());
				checkItemEntity.setMaterialCode(entity.getMaterialCode());
				checkItemEntity.setMaterialName(entity.getMaterialName());	
				checkItemEntity.setUnitName(entity.getUnitName());
				checkItemEntity.setReceiveQty(entity.getOrderQty()*-1);
				//checkItemEntity.setBuyerCheckPrice(entity.getPrice());
				checkItemEntity.setCheckPrice(entity.getPrice());
				checkItemEntity.setPrice(entity.getPrice()==null?"":String.valueOf(entity.getPrice()));
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	/**
	 * 对账明细导出
	 * @param id			对账单ID
	 * @param request		浏览器请求
	 * @param response		服务器响应
	 * @return				对账明细集合
	 * @throws Exception	异常
	 */
	@LogClass(method="导出对账明细",module="对账管理")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="exportCheckItemList/{type}/{vendor}/{id}")
	public String exportCheckItemList(@PathVariable(value="type") Integer type, @PathVariable(value="vendor") boolean vendor, @PathVariable(value="id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log("method exportCheckItemList start");
		List<CheckItemEntity> list = service.getCheckItemsByCheckId(id);
		for (CheckItemEntity checkItemEntity : list) {
			NoCheckItemsEntity  entity  = noCheckedItemsService.getByItemId(checkItemEntity.getViewNoCheckItemId());
			
			checkItemEntity.setReceiveTime(entity.getCreateTime());
			checkItemEntity.setQadCode(entity.getOrderCode());
			checkItemEntity.setMaterialCode(entity.getMaterialCode());
			checkItemEntity.setMaterialName(entity.getMaterialName());	
			checkItemEntity.setUnitName(entity.getUnitName());
			checkItemEntity.setPrice(entity.getPrice()==null?"":String.valueOf(entity.getPrice()));
			checkItemEntity.setDeliveryCode(entity.getDlvCode());
			//checkItemEntity.setVendorBatchNum("11");
			
			if(entity.getDataType()==1){
				checkItemEntity.setReceiveQty(entity.getRecQty());
			}else{
				checkItemEntity.setReceiveQty(entity.getOrderQty());
			}
			
			//logger.log("orderCode == " + order.getOrderCode());
			//receiveItem.setDeliveryCode(delivery.getDeliveryCode());
		}
		
		StringBuffer fileBuff =new StringBuffer();
		fileBuff.append("对账明细");
		String fileName = fileBuff.toString();				//下载文件名
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
	    
	    ExcelUtil excelUtil = null;
		if(vendor){							// 供应商
			List<CheckItemInVendorTransfer> checkItemInVendorTransfer = service.initCheckItemInVendorTransfer(list);
			excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(CheckItemInVendorTransfer.class), null);
			log("checkItemInVendorTransfer == " + checkItemInVendorTransfer.size());
			excelUtil.export(checkItemInVendorTransfer);
		}else if(!vendor){						// 采购商
			List<CheckItemInBuyerTransfer> checkItemInBuyerTransfer = service.initCheckItemInBuyerTransfer(list);
			excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(CheckItemInBuyerTransfer.class), null);
			log("checkItemInBuyerTransfer == " + checkItemInBuyerTransfer.size());
			excelUtil.export(checkItemInBuyerTransfer);
		}
		log("method exportCheckItemList end");
		return null;   
	}
	
	@LogClass(method="查询对账单发票",module="对账管理")
	@RequestMapping(value="/getInvoicesByCheckId/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getInvoicesByCheckId(@PathVariable(value="id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		List<CheckInvoiceEntity> list = service.getInvoicesByCheckId(id);
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Page<CheckInvoiceEntity> page =new PageImpl<CheckInvoiceEntity>(list, pagin, list.size());
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="/getItemsByPks/{checkItemIds}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getItemsByPks(@PathVariable("checkItemIds") String checkItemIds,
			@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize){
		String [] pks_ = checkItemIds.split(",");
		List<CheckItemEntity> list = new ArrayList<CheckItemEntity>();
		for(String pk:pks_){
			CheckItemEntity cie = service.getCheckItemById(Long.valueOf(pk));
			list.add(cie);
		}
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Page<CheckItemEntity> page =new PageImpl<CheckItemEntity>(list, pagin, list.size());
		for (CheckItemEntity checkItemEntity : page) {
			 NoCheckItemsEntity  entity  = noCheckedItemsService.getByItemId(checkItemEntity.getViewNoCheckItemId());
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==checkItemEntity.getSource()){
				//收货+
				checkItemEntity.setReceiveQty(entity.getRecQty());
				checkItemEntity.setReceiveTime(entity.getCreateTime());
				checkItemEntity.setReceiveCode(entity.getOrderCode());
				checkItemEntity.setMaterialCode(entity.getMaterialCode());
				checkItemEntity.setMaterialName(entity.getMaterialName());
				//PurchaseOrderItemEntity orderItem=retService.getItemById(receiveItem.getOrderItem().getId());
				checkItemEntity.setUnitName(entity.getUnitName());
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==checkItemEntity.getSource()){
				//退货-
				checkItemEntity.setReceiveQty(entity.getOrderQty()*-1);
				checkItemEntity.setReturnTime(entity.getCreateTime());
				checkItemEntity.setRetrunCode(entity.getOrderCode());
				checkItemEntity.setMaterialCode(entity.getMaterialCode());
				checkItemEntity.setMaterialName(entity.getMaterialName());
				checkItemEntity.setUnitName(entity.getUnitName());
			}
			
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@LogClass(method="发布对账单",module="对账管理")
	@RequestMapping(value="/opt/publish/{id}"/*,method=RequestMethod.POST*/)
	@ResponseBody
	public Map<String,Object> publish(@PathVariable(value="id") Long id, Model model,@RequestParam(value="claimDescription") String claimDescription,@RequestParam(value="claimAmount") Double claimAmount){
		service.publish(id,claimDescription, claimAmount, request);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="删除发票",module="对账管理")
	@RequestMapping(value="/opt/delInvoice/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delInvoice(@PathVariable(value="id") Long id, Model model){
		service.delInvoice(id);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="修改索赔描述",module="对账管理")
	@RequestMapping(value="/opt/editClaimDescription/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editClaimDescription(@PathVariable(value="id") Long id, @RequestBody Map<String ,Object> param){
		String claimDescription = (String)param.get("claimDescription");
		service.editClaimDescription(id,claimDescription);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="修改索赔金额",module="对账管理")
	@RequestMapping(value="/opt/editClaimAmount/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editClaimAmount(@PathVariable(value="id") Long id,@RequestBody Map<String ,Object> param){
		
		Double claimAmount = Double.valueOf((String)param.get("claimAmount"));
		service.editClaimAmount(id,claimAmount);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="供应商确认对账单",module="对账管理")
	@RequestMapping(value="/opt/vConfirm/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vConfirm(@PathVariable(value="id") Long id,@RequestBody Map<String ,Object> param){
		JSONObject object = JSONObject.fromObject(param);     
		JSONArray array = (JSONArray) object.get("rows");
		service.vConfirm(array, id, request);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="异常处理",module="对账管理")
	@RequestMapping(value="/opt/dealEx",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dealEx(@Valid CheckEntity check) throws Exception{
		service.dealEx(check);
		map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="采购商确认",module="对账管理")
	@RequestMapping(value="/opt/bConfirm/{id}", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bConfirm(@PathVariable(value="id") Long id,@RequestParam(value="claimDescription", required=false) String claimDescription,
			@RequestParam(value="claimAmount", required=false) Double claimAmount) throws Exception{
		map = new HashMap<String, Object>();
		CheckEntity check = service.getCheckById(id);
		Set<CheckItemEntity> itemSet = check.getCheckItem();
		for(CheckItemEntity item : itemSet) {
			if(item.getExStatus() == 1 && item.getExConfirmStatus() == 0) {
				throw new Exception("对账单明细存在差异未确认记录，无法确认!");
			}
		}
		service.bConfirm(id,claimDescription,claimAmount, request);
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="开票完成-billOk",module="对账管理")
	@RequestMapping(value="/opt/billOk/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> billOk(@PathVariable(value="id") Long id, @RequestBody Map<String ,Object> param){
		JSONObject object = JSONObject.fromObject(param);
		JSONArray array = (JSONArray) object.get("rows");
		service.billOk(array, id);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	
	
	/**
	 * 转向开票画面
	 * @param pks				对账明细ID集合
	 * @param checkId			对账单ID
	 * @param claimBillFlag		索赔金额开票状态
	 * @param model				视图
	 * @return					开票画面
	 */
	@RequestMapping(value="/tobill/{pks}/{checkId}/{claimBillFlag}/{isAbroad}",method=RequestMethod.GET)
	public String tobill(@PathVariable(value="pks") String pks, @PathVariable(value="checkId") long checkId,
			@PathVariable(value="claimBillFlag") boolean claimBillFlag, @PathVariable(value="isAbroad") boolean isAbroad, Model model){
		String[] array = StringUtils.split(pks, ",");
		CheckItemEntity entity = null;
		Double total = 0d;
		for(String idStr : array) {
			entity = service.getCheckItemById(StringUtils.convertToLong(idStr)); 	//获取明细对象
			 NoCheckItemsEntity  ceckItemsEntity  = noCheckedItemsService.getByItemId(entity.getViewNoCheckItemId());
			 double receiveQty = 0;
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==entity.getSource()){
				//收货+
				receiveQty = ceckItemsEntity.getRecQty(); 
				total =BigDecimalUtil.add(total, BigDecimalUtil.mul(entity.getBuyerCheckPrice() == null ?
					entity.getVendorCheckPrice() : entity.getBuyerCheckPrice(), receiveQty));
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==entity.getSource()){
				//退货-
				receiveQty = ceckItemsEntity.getOrderQty();
				total = BigDecimalUtil.add(total, BigDecimalUtil.mul(entity.getBuyerCheckPrice() == null ?
					entity.getVendorCheckPrice() : entity.getBuyerCheckPrice(), receiveQty));
			}
		}
		if(claimBillFlag){												//非国外，并且勾选索赔金额开票，则加入索赔开票的税前金额
			CheckEntity check = service.getCheckById(checkId);
			if(check!=null){
				Double claimAmount = check.getClaimAmount();
				total = BigDecimalUtil.add(total, claimAmount);
				model.addAttribute("claimAmount", claimAmount);
			}
		}
		model.addAttribute("bill_checkItem_pks", pks);
		model.addAttribute("total_price", total);
		model.addAttribute("claimBillFlag", claimBillFlag);
		model.addAttribute("isAbroad", isAbroad);
		model.addAttribute("checkId", checkId);
		return "back/check/vendor/billAdd";
	}
	
	/**
	 * 开票
	 * @param invoice 发票信息
	 * @param file 附件
	 * @return
	 * @throws Exception
	 */
	@LogClass(method="开票",module="对账管理")
	@RequestMapping(value="/opt/bill",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bill(@Valid CheckInvoiceEntity invoice, @RequestParam("file") MultipartFile file, 
			@RequestParam(value="claimBillFlag") boolean claimBillFlag, @RequestParam(value="isAbroad") boolean isAbroad) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<CheckInvoiceEntity> invoiceList = service.findInvoiceListByCode(invoice.getCode());
		if(!invoiceList.isEmpty()){
			map.put("success", false);
			map.put("msg", "已存在该发票单号！");
			return map;
		}
		File temp = null;
		if(file != null && file.getSize() > 0) {
			temp = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			invoice.setInvoiceFilePath(temp.getPath());
			invoice.setInvoiceFileName(file.getOriginalFilename()); 
		}
		String [] pks_ = invoice.getCheckItemIds().split(",");
		List<CheckItemEntity> list = new ArrayList<CheckItemEntity>();
		synchronized (this) {
			for(String pk : pks_){
				CheckItemEntity cie = service.getCheckItemById(Long.valueOf(pk));   
				if(cie.getInvoice()!=null){
					throw new Exception("包含已开票的明细，不能重复开票");
				}
				list.add(cie);
			}
			service.bill(invoice, list, claimBillFlag, isAbroad);		//开票
		}
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	
	@RequestMapping(value="/toClaimBill/{checkId}",method=RequestMethod.GET)
	public String toClaimBill(@PathVariable(value="checkId") long checkId,Model model){
		logger.log("method toClaimBill start");
		CheckEntity check = service.getCheckById(checkId);
		model.addAttribute("check", check);
		logger.log("method toClaimBill end");
		return "back/check/vendor/claimBillAdd";
	}

	/**
	 * 索赔金额开票
	 * @param 	invoice 发票信息
	 * @param 	file 	附件
	 * @return	结果
	 * @throws 	Exception
	 */
	@LogClass(method="索赔金额开票",module="对账管理")
	@RequestMapping(value="/opt/addClaimBill",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addClaimBill(@Valid CheckInvoiceEntity invoice, @RequestParam("file") MultipartFile file) throws Exception{
		logger.log("method addClaimBill start");
		File temp = null;
		if(file != null && file.getSize() > 0) {
			temp = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			invoice.setInvoiceFilePath(temp.getPath());
			invoice.setInvoiceFileName(file.getOriginalFilename()); 
		}
		service.addClaimBill(invoice);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		logger.log("method addClaimBill end");
		return map;
	}
	
	/**
	 * 确认核对异常管理数据
	 * @param 	param	异常管理数据
	 * @return			成功状态
	 */
	@LogClass(method="确认核对异常管理数据",module="对账管理")
	@RequestMapping(value="/opt/confirmCheckPrice", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmCheckPrice(@RequestBody Map<String ,Object> param){
		logger.log("method confirmCheckPrice start");
		JSONObject object = JSONObject.fromObject(param);
		JSONArray array = (JSONArray) object.get("rows");
		service.confirmCheckPrice(array);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		logger.log("method confirmCheckPrice end");
		return map;
	}

	@LogClass(method="批量修改金额",module="对账管理")
	@RequestMapping(value="/opt/batchModifyPrice",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> batchModifyPrice(@RequestBody Map<String ,Object> param){
		JSONObject object = JSONObject.fromObject(param);
		JSONArray array = (JSONArray) object.get("rows");
		service.batchModifyPrice(array);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="确认修改金额",module="对账管理")
	@RequestMapping(value="/opt/confirmModifyPrice",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmModifyPrice(@RequestBody Map<String ,Object> param) throws Exception{
		JSONObject object = JSONObject.fromObject(param);
		JSONArray array = (JSONArray) object.get("rows");
		CheckExceptionEntity entity = null;
		List<CheckExceptionEntity> exList = new ArrayList<CheckExceptionEntity>();
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			entity = service.getCheckExById(StringUtils.convertToLong(StringUtils.convertToString(object.get("id")))); 
			if(entity == null)
				continue;
			
			entity.setBuyerCheckPrice(StringUtils.convertToDouble(StringUtils.convertToString(object.get("buyerCheckPrice"))));
			entity.setExDiscription(StringUtils.convertToString(object.get("exDiscription")));
			exList.add(entity);
		}
		service.confirmModifyPrice(exList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	
	/*@RequiresPermissions("check:ex:v:view")*/
	@RequestMapping(value="/getExceptionVendorList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getExceptionVendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
//		searchParamMap = CommonUtil.initSearchTime(searchParamMap, "GTE_checkItem.createTime", 0);//开始时间
//		searchParamMap = CommonUtil.initSearchTime(searchParamMap, "LTE_checkItem.createTime", 1);//结束时间

		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_checkItem.check.vendor.id", user.orgId);
		
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		
		Page<CheckExceptionEntity> page = service.getExceptions(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		for(CheckExceptionEntity ex :page.getContent()){
			CheckItemEntity checkItem=service.getCheckItemById(Long.valueOf(ex.getCheckItem().getId()));
			ex.setBuyerCheckPrice(checkItem.getBuyerCheckPrice());
			ex.setExDiscription(checkItem.getExDiscription());
			NoCheckItemsEntity  entity  = noCheckedItemsService.getByItemId(checkItem.getViewNoCheckItemId());
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==checkItem.getSource()){
				ex.setReceiveQty(entity.getRecQty());
				ex.setUnitName(entity.getUnitName());
				ex.setCheckPrice(checkItem.getCheckPrice());
				ex.setMaterialCode(entity.getMaterialCode());
				ex.setMaterialName(entity.getMaterialName());
				//DeliveryEntity delivery=deliveryService.findDlvById(recItem.getDeliveryItem().getDelivery().getId());
				ex.setDeliveryCode(entity.getDlvCode());
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==checkItem.getSource()){
				ex.setReceiveQty(entity.getOrderQty());
				ex.setUnitName(entity.getUnitName());
				ex.setCheckPrice(checkItem.getCheckPrice());
				ex.setMaterialCode(entity.getMaterialCode());
				ex.setMaterialName(entity.getMaterialName());
				ex.setReturnCode(entity.getOrderCode());
			}
		
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	@LogClass(method="确认异常",module="对账管理")
	@RequestMapping(value="/opt/confirmEx",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmEx(@RequestBody List<CheckExceptionEntity> exList) throws Exception{
		service.confirmEx(exList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="确认发票",module="对账管理")
	@RequestMapping(value="/opt/confirmInvoice/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmInvoice(@PathVariable(value="id") Long id) throws Exception{
		//modify by zhangjiejun 2015.11.04 start
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		service.confirmInvoice(id, new UserEntity(user.id));
		//modify by zhangjiejun 2015.11.04 end
		
//		service.confirmInvoice(id);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="驳回发票",module="对账管理")
	@RequestMapping(value="/opt/rejectInvoice/{id}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectInvoice(@PathVariable(value="id") Long id,@RequestParam(value="billRejectReason") String billRejectReason) throws Exception{
		//modify by zhangjiejun 2015.11.04 start
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		service.rejectInvoice(id, billRejectReason, new UserEntity(user.id));
		//modify by zhangjiejun 2015.11.04 end
		
//		service.rejectInvoice(id,billRejectReason);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	@LogClass(method="关闭对账单",module="对账管理")
	@RequestMapping(value="/opt/close",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> close(@RequestBody List<CheckEntity> checkList) throws Exception{
		service.closeCheck(checkList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	
	/**
	 * 修改对账单为发货状态
	 * @param 	checkList	对账单集合
	 * @return	操作状态
	 * @throws 	Exception	异常
	 */
	@LogClass(method="手动创建对账单",module="对账管理")
	@RequestMapping(value="/opt/create",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> create() throws Exception{
		Class clazz = Class.forName("com.qeweb.scm.purchasemodule.task.CreateCheckTask");
		Method method = clazz.getDeclaredMethod("exec");
		method.setAccessible(true);
		method.invoke(clazz.newInstance());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}
	
	@Override
	public void log(Object message) {
		
	}
	
	//add by zhangjiejun 2015.10.16 start
	/**
	 * 修改对账单为发货状态
	 * @param 	checkList	对账单集合
	 * @return	操作状态
	 * @throws 	Exception	异常
	 */
	@LogClass(method="发布对账单",module="对账管理")
	@RequestMapping(value="/opt/publish",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publish(@RequestBody List<CheckEntity> checkList) throws Exception{
		logger.log("method publish start");
		service.publishCheck(checkList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "操作成功！");
		logger.log("map == " + map);
		logger.log("method publish end");
		return map;
	}
	//add by zhangjiejun 2015.10.16 end

	
	@RequestMapping(value="getCheckByInvoiceCode/{invoiceCode}",method=RequestMethod.POST)
	@ResponseBody
	public Long getCheckByInvoiceCode(@PathVariable(value="invoiceCode") String invoiceCode) throws Exception{
		long checkId=0;
		CheckEntity entity=null;
		List<CheckItemEntity> itemList=service.getCheckItemsByInvoiceCode(invoiceCode);
		for (CheckItemEntity checkItemEntity : itemList) {
			entity=checkItemEntity.getCheck();
			checkId=entity.getId();
		}
		return checkId;
	}
	
	/**
	 * 通过check的id查询到发货单对象
	 * @return	list 查询到的发货单对象
	 */
	@RequestMapping(value="findDeliveryInvoiceInfoByCheckId/{id}", method = RequestMethod.POST)
	@ResponseBody
	public List<DeliveryEntity> findDeliveryInvoiceInfoByCheckId(@PathVariable("id") long id, Model model){
		logger.log("method findDeliveryInvoiceInfoByCheckId start");
		List<DeliveryEntity> list = new ArrayList<DeliveryEntity>();
		CheckEntity check = service.getCheckById(id);
		Set<CheckItemEntity> set = check.getCheckItem();
		if(set != null && set.size() != 0){
			List<CheckItemEntity> setList = new ArrayList<CheckItemEntity>();
			setList.addAll(set);
			CheckItemEntity checkItemEntity = setList.get(0);
			if(checkItemEntity != null){
				ReceiveItemEntity recItem = checkItemEntity.getRecItem();
				DeliveryItemEntity deliveryItem = recItem.getDeliveryItem();
				DeliveryEntity deliveryEntity = deliveryItem.getDelivery();
				if(deliveryEntity != null){
					logger.log("has deliveryEntity id == " + deliveryEntity.getId());
					list.add(deliveryEntity);
				}
			}
		}
		logger.log("list == " + list);
		logger.log("method findDeliveryInvoiceInfoByCheckId end");
		return list;
	}
	
	/*@RequiresPermissions("data:setting:view")*/
	@RequestMapping(value="/dateSetting", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dateSettingList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("NEQ_vendorId", "-1");
		Page<TimetaskSettingEntity> page = service.getDateSettingList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="addDateSetting",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(TimetaskSettingEntity dateSetting) throws Exception
	{
		return service.add(dateSetting);
	}
	
	@RequestMapping(value = "releaseDateSetting",method = RequestMethod.POST)
	@ResponseBody
	public String releaseLevel(@RequestBody List<TimetaskSettingEntity> dateSettings){
		return service.releaseDateSetting(dateSettings);
	}
	
	@RequestMapping(value = "delsDateSetting",method = RequestMethod.POST)
	@ResponseBody
	public String delsLevel(@RequestBody List<TimetaskSettingEntity> dateSettings){
		return service.delsDateSetting(dateSettings);
	}
	
	@RequestMapping(value = "deleteDatesettings",method = RequestMethod.POST)
	@ResponseBody
	public String deleteDatesetting(@RequestBody List<TimetaskSettingEntity> dateSettings){
		return service.deleteDateSetting(dateSettings);
	}
	
	@RequestMapping(value = "getDatesetting/{id}",method = RequestMethod.POST)
	@ResponseBody
	public String getDatesetting(@PathVariable("id") Long id){
		return service.getDateSetting(id);
	}
	
	/**
	 * 修改默认对账日
	 * @param dateSetting
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateDefaultDateSetting",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateDefaultDateSetting(TimetaskSettingEntity dateSetting) throws Exception
	{
		return service.updateDefaultDateSetting(dateSetting);
	}
	
	/**
	 * add by yao.jin 2017.03.03 对账单中反馈信息的显示
	 * 反馈信息显示
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="feedback/{checkId}", method = RequestMethod.GET)
	public String feedback(@PathVariable(value="checkId") Long checkId, Model model) {
		CheckEntity check= service.getCheckById(checkId);
		Map<String, Object> searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("EQ_billType", StringUtils.convertToString(PurchaseConstans.CHECK_BILL));
		searchParamMap.put("EQ_billId", checkId + "");
		List<FeedbackEntity> list = feedbackService.getFeedbacks(searchParamMap);
		model.addAttribute("vendor", true);
		model.addAttribute("check", check);
		model.addAttribute("feedList", list);
		return "back/check/buyer/feedback";
	}
	
	
	
/*	public Map<String,Object> createCheck(ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
	}*/
}
