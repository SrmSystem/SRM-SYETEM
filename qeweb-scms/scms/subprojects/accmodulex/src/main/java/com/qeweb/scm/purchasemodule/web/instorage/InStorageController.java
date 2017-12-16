package com.qeweb.scm.purchasemodule.web.instorage;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.BillListEntity;
import com.qeweb.scm.purchasemodule.entity.BillListItemEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;
import com.qeweb.scm.purchasemodule.service.InStorageService;

/**
 * 入库管理
 * @author ALEX
 *
 */
@Controller
@RequestMapping(value = "/manager/account/instorage")
public class InStorageController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private InStorageService inStorageService;    
	
	@Autowired
	private SerialNumberService serialNumberService;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/instorage/instorageList"; 
	}
	
	/**
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="buyer",method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/instorage/instorageList";                 
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"vendor:instorage:view","buyer:instorage:view"}, logical=Logical.OR)
	@LogClass(method="查询库存",module="库存管理")
	@RequestMapping(value="{boo}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable("boo")boolean boo, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws ParseException{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_invoiceStatus", PurchaseConstans.INVOICE_MAKE_OUT_NO + "");
		searchParamMap.put("ISNULL_inStorage.consignedFlag", searchParamMap);
		
		if(searchParamMap.get("GT_inStorageTime") != null && searchParamMap.get("GT_inStorageTime") != "")
			searchParamMap.put("GT_inStorageTime", DateUtil.parseTimeStamp(searchParamMap.get("GT_inStorageTime")+""));
		if(searchParamMap.get("LT_inStorageTime")!=null && searchParamMap.get("LT_inStorageTime") != "")
			searchParamMap.put("LT_inStorageTime", DateUtil.parseTimeStamp(searchParamMap.get("LT_inStorageTime")+""));
		Page<InStorageItemEntity> userPage = inStorageService.getInStorageItems(boo,pageNumber,pageSize,searchParamMap);
		for (InStorageItemEntity inStorageItemEntity : userPage) {
			String unitName = inStorageItemEntity.getReceiveItem().getOrderItemPlan().getUnitName();
			Timestamp requestTime = inStorageItemEntity.getReceiveItem().getOrderItemPlan().getRequestTime();
			String deliveryCode =inStorageItemEntity.getReceiveItem().getDeliveryItem().getDelivery().getDeliveryCode(); 
			inStorageItemEntity.setUnitName(unitName);
			inStorageItemEntity.setRequestTime(StringUtils.convertToString(requestTime));
			inStorageItemEntity.setDeliveryCode(deliveryCode);
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="online",method = RequestMethod.GET)
	public String vendorOnlinelist(Model model) {
		model.addAttribute("vendor", true);
		return "back/instorage/instorageListOnline"; 
	}
	
	/**
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="buyer/online",method = RequestMethod.GET)
	public String online(Model model) {
		model.addAttribute("vendor", false);
		return "back/instorage/instorageListOnline";                 
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@LogClass(method="查询库存",module="库存管理")
	@RequestMapping(value="onlineList/{boo}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getOnlineList(@PathVariable("boo")boolean boo, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws ParseException{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_invoiceStatus", PurchaseConstans.INVOICE_MAKE_OUT_NO + "");
		searchParamMap.put("ISNOTNULL_inStorage.consignedFlag", searchParamMap);
		
		if(searchParamMap.get("GT_inStorageTime") != null && searchParamMap.get("GT_inStorageTime") != "")
			searchParamMap.put("GT_inStorageTime", DateUtil.parseTimeStamp(searchParamMap.get("GT_inStorageTime")+""));
		if(searchParamMap.get("LT_inStorageTime")!=null && searchParamMap.get("LT_inStorageTime") != "")
			searchParamMap.put("LT_inStorageTime", DateUtil.parseTimeStamp(searchParamMap.get("LT_inStorageTime")+""));
		Page<InStorageItemEntity> userPage = inStorageService.getInStorageItems(boo,pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getInventory/{id}")
	@ResponseBody
	public InStorageEntity getInStorage(@PathVariable("id") Long id){
		InStorageEntity entity = inStorageService.getInStorageById(id);   
		return entity;
	}
	
	/**
	 * TO创建对账单
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="createBill/{ids}")
	@ResponseBody
	public BillListEntity getBill(@PathVariable("ids") String idstr,  Model model,ServletRequest request){
		BillListEntity bill = new BillListEntity();
		//生产对账清单号
		bill.setBillCode(serialNumberService.geneterNextNumberByKey("BILL_CODE"));
		bill.setSyncStatus(0);
		//获取默认金额信息
		String[] search = idstr.split(",");
		long[] ids = StringUtils.convertToLong(search);
		List<Long> billIdList = new ArrayList<Long>();
		for (long id : ids)
			billIdList.add(id);
		inStorageService.initBillInfo(billIdList, bill);
		return bill;
	}
	
	/**
	 * TO创建对账单明细
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="createBillItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getInstoreItem(Model model,ServletRequest request){
		if(request.getParameter("search-IN_id") == null)
			return null;
		
		String[] search = request.getParameter("search-IN_id").split(",");
		long[] ids = StringUtils.convertToLong(search);
		List<Long> list = new ArrayList<Long>();
		for (long id : ids)
			list.add(id);
		List<InStorageItemEntity> userPage = inStorageService.getInstorageItmes(list);
		map = new HashMap<String, Object>();
		map.put("rows", userPage);
		map.put("total", userPage.size());
		return map;
	}
	
	/**
	 * 新增发票信息
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="保存发票信息",module="对账管理")
	@RequestMapping(value="createInvoice",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getInvoiceList(Model model,ServletRequest request){
		map = new HashMap<String, Object>();
		List<InvoiceEntity> invoiceList = new ArrayList<InvoiceEntity>();
		invoiceList.add( new InvoiceEntity());  
		map.put("rows", invoiceList);
		map.put("total", 1);
		return map;
	}
	
	/**
	 * 根据入库单生成对账清单
	 * @param billEntity 清单主信息
	 * @param billItemdatas 清单明细
	 * @param invoicedatas 发票明细
	 * @return
	 * @throws Exception
	 */
	@LogClass(method="根据入库单生成对账清单",module="对账管理")
	@RequestMapping(value = "saveBill",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveBill(@Valid BillListEntity billEntity, @RequestParam(value="datas") String billItemdatas,  
			@RequestParam(value="datas2") String invoicedatas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		// 判断是否直接开票
		if(billEntity.getInvoiceStatus() == 1){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			billEntity.setInvoiceUser(new UserEntity(user.id));
			billEntity.setInvoiceTime(DateUtil.getCurrentTimestamp());
			billEntity.setSyncStatus(0);
		}
		//结算明细
		List<BillListItemEntity> billItem = new ArrayList<BillListItemEntity>();
		BillListItemEntity item = null;
		JSONObject object = JSONObject.fromObject(billItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new BillListItemEntity(); 
			item.setAccountItemId(StringUtils.convertToLong(object.get("id") + ""));
			item.setBillList(billEntity);
			item.setTax(billEntity.getTax());
			item.setAccountQty(StringUtils.convertToDouble(object.get("accountQty") + ""));
			item.setPrice(StringUtils.convertToDouble(object.get("price") + ""));
			item.setSyncStatus(0);
			billItem.add(item);
		}
		//结算发票
		List<InvoiceEntity> invoiceList = new ArrayList<InvoiceEntity>();
		InvoiceEntity invoice = null;
		object = JSONObject.fromObject(invoicedatas); 
		array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			invoice = new InvoiceEntity(); 
			invoice.setInvoiceCode(object.get("invoiceCode") + "");
			invoice.setInvoiceMoney(StringUtils.convertToDouble(object.get("invoiceMoney") + ""));
			String invoiceTime = StringUtils.convertToString(object.get("invoiceTime"));
			invoice.setInvoiceTime(Timestamp.valueOf(invoiceTime));
			invoice.setBillList(billEntity); 
			invoiceList.add(invoice);
		}
		inStorageService.saveBill(billEntity, billItem, invoiceList);
		map.put("message", "保存开票清单成功");
		map.put("success", true);
		return map;   
	}
	
}
