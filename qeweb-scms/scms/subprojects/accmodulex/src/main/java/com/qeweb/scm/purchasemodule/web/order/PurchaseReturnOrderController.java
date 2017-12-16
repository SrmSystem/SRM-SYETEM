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
 * 退货采购订单Controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/order/returnPurchase")
public class PurchaseReturnOrderController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PurchaseMainOrderService mainOrderService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;	
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/orderMain/order/returnOrderList";  
	}
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/orderMain/order/returnOrderList";
	}
	
	
	/**
	 * 退货采购订单明细列表查询[采.供]
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getReturnOrderItemList/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getOrderItemList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {//供应商
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_order.vendor.id", user.orgId + "");  
			searchParamMap.put("NEQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_NO);
		}else{//采购商
			//用户权限
			//searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
			//组织权限
			//searchParamMap.put("IN_order.buyer.id", buyerOrgPermissionUtil.getBuyerIds());
			//采购组权限
			searchParamMap.put("IN_order.purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		}
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_retpo","X");//具有“X”的为退货采购订单行项目，为空的为标准的采购订单
		
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "asc";
		}
		
		Page<PurchaseOrderItemEntity> userPage = mainOrderService.getPurchaseOrderItems(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));
		for(PurchaseOrderItemEntity purchaseOrderItem  : userPage.getContent() ){
			String col1 = receiveService.getOrderItemZllblQty(purchaseOrderItem.getId()) == null ? "" :   receiveService.getOrderItemZllblQty(purchaseOrderItem.getId()).toString() ;
		    String col2 = receiveService.getOrderItemZzjblQty(purchaseOrderItem.getId()) == null ? "" :   receiveService.getOrderItemZzjblQty(purchaseOrderItem.getId()).toString() ;
			purchaseOrderItem.setCol1(col1);
			purchaseOrderItem.setCol1(col2);	
			Double col3 = BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), Double.valueOf(purchaseOrderItem.getReceiveQty()));
			Double onwayQty = mainOrderService.getOrderItemOnwayQty(purchaseOrderItem.getId());
			Double undeliveryQty = mainOrderService.getOrderItemUndeliveryQty(purchaseOrderItem.getId());
			Double receiveQty=mainOrderService.getOrderItemReceiveQty(purchaseOrderItem.getId()); 
			purchaseOrderItem.setCol3(col3.toString());
			purchaseOrderItem.setOnwayQty(onwayQty);
			purchaseOrderItem.setUndeliveryQty(undeliveryQty);
			purchaseOrderItem.setReceiveQty(receiveQty);
			
			if( !StringUtils.isEmpty( purchaseOrderItem.getBstae() )  || !StringUtils.isEmpty( purchaseOrderItem.getLoekz() )  || !StringUtils.isEmpty( purchaseOrderItem.getLockStatus()) || !StringUtils.isEmpty( purchaseOrderItem.getZlock()) || !StringUtils.isEmpty( purchaseOrderItem.getElikz())    ){
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
	@RequestMapping("exportExcel/{vendor}")
	public String download(@PathVariable("vendor")Boolean vendor, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("退货采购订单明细", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
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
		searchParamMap.put("EQ_retpo","X");//具有“X”的为退货采购订单行项目，为空的为标准的采购订单

		List<PurchaseOrderItemTransfer> list = mainOrderService.getOrderItemVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchaseOrderItemTransfer.class), null);  
		excelUtil.export(list);  
		return null;    
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
