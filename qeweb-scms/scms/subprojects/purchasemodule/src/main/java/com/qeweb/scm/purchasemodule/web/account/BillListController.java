package com.qeweb.scm.purchasemodule.web.account;

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

import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.AccountType;
import com.qeweb.scm.purchasemodule.entity.BillListEntity;
import com.qeweb.scm.purchasemodule.entity.BillListItemEntity;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;
import com.qeweb.scm.purchasemodule.service.BillListService;
import com.qeweb.scm.purchasemodule.service.InvoiceService;

/**
 * 开票清单管理
 * @author alex
 * @date 2015年5月4日
 */
@Controller
@RequestMapping(value = "/manager/account/bill")
public class BillListController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private BillListService billListService;    
	
	@Autowired
	private InvoiceService invoiceService;   
	
	enum BillType{in, online};			//对账清单类型 in:入库对账 online 上线对账
	
	/**采方
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{type}", method = RequestMethod.GET)
	public String list(@PathVariable("type") String type, Model model) {
		model.addAttribute("type", type);
		return "back/account/billList";                 
	}
	
	/**
	 * 供方
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/vendor/{type}", method = RequestMethod.GET)
	public String vendorlist(@PathVariable("type") String type, Model model) {
		model.addAttribute("type", type);
		return "back/account/vendorbillList";                 
	}
	
	/** 开票清单
	 * @param type  in: 入库开票清单  online: 上线开票清单
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getInList(@PathVariable("type") String type, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(BillType.in.toString().equals(type))
			searchParamMap.put("EQ_billType", AccountType.ACCOUNT_TYPE_IN + "");
		else if(BillType.online.toString().equals(type)) 
			searchParamMap.put("EQ_billType", AccountType.ACCOUNT_TYPE_ONLINE + "");   
		Page<BillListEntity> userPage = billListService.getBillLists(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 开票
	 * @param billEntity
	 * @param billItemdatas
	 * @param invoicedatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveBill",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveBill(@Valid BillListEntity billEntity, @RequestParam(value="datas") String billItemdatas,  
			@RequestParam(value="datas2") String invoicedatas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		//结算明细
		List<BillListItemEntity> billItem = new ArrayList<BillListItemEntity>();
		BillListItemEntity item = null;
		JSONObject object = JSONObject.fromObject(billItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new BillListItemEntity(); 
			item.setId(StringUtils.convertToLong(object.get("id") + ""));
			item.setAccountQty(StringUtils.convertToDouble(object.get("accountQty") + ""));
			item.setPrice(StringUtils.convertToDouble(object.get("price") + ""));
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
			invoice.setId(StringUtils.convertToLong(object.get("id") + "")); 
			invoice.setInvoiceCode(object.get("invoiceCode") + "");
			invoice.setInvoiceMoney(StringUtils.convertToDouble(object.get("invoiceMoney") + ""));
			invoice.setBillList(billEntity); 
			invoiceList.add(invoice);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		billListService.saveBill(billEntity, billItem, invoiceList, new UserEntity(user.id));
		map.put("message", "保存开票清单成功");
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 获取开票清单主信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getBillList/{id}")
	@ResponseBody
	public BillListEntity getBillList(@PathVariable("id") Long id){
		return billListService.getBillListById(id);
	}
	
	/**
	 * 获取清单详情
	 * @param billid
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "billitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long billid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_billList.id", billid + "");
		Page<BillListItemEntity> userPage = billListService.getBillListItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取清单发票信息
	 * @param billid
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "billinvoice/{id}")
	@ResponseBody
	public Map<String,Object> getInvoiceList(@PathVariable("id") Long billid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		map = new HashMap<String, Object>();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_billList.id", billid + "");
		Page<InvoiceEntity> userPage = invoiceService.getInvoices(pageNumber,pageSize,searchParamMap);
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	/**
	 * 接收发票
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "receiveInvoice",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> receiveInvoice(@RequestParam(value="billId") long billId, @RequestParam(value="recstatus") Integer receiveStatus){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		billListService.receiveInvoice(billId, receiveStatus, new UserEntity(user.id));
		map.put("message", "确认订单成功");
		map.put("success", true);
		return map;
	}
}
