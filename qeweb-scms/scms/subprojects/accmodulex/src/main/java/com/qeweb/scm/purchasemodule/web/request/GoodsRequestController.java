package com.qeweb.scm.purchasemodule.web.request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.MatcherUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemEntity;
import com.qeweb.scm.purchasemodule.service.GoodsRequestService;
import com.qeweb.scm.purchasemodule.web.vo.GoodsRequestTransfer;

/**
 * 要货单管理
 * @author alex
 * @date 2015年4月22日
 * @path com.qeweb.scm.purchasemodule.web.request.GoodsRequestController.java
 */
@Controller
@RequestMapping(value = "/manager/order/goodsrequest")
public class GoodsRequestController implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private GoodsRequestService goodsRequestService; 
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired 
	private MaterialService materialService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/request/requestList";
	}
	
	@RequestMapping(value="vendor",method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/request/requestList";
	}
	
	@RequestMapping(value="/{vendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize, Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_sender.id", user.orgId + "");
			searchParamMap.put("EQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_YES + "");   
		}else{
			//采购组权限
			searchParamMap.put("IN_order.purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		}    
		Page<GoodsRequestEntity> userPage = goodsRequestService.getGoodsRequests(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getSenders/{orgid}",method = RequestMethod.POST)
	@ResponseBody
	public List<OrganizationEntity> getSenders(@PathVariable(value="orgid") long orgid){
		List<OrganizationEntity> orgList=goodsRequestService.getSenders(orgid, 2);
		return orgList;
	}
	
	@RequestMapping(value = "getRequest/{id}/{vendor}", method = RequestMethod.GET)
	public String getOrder(@PathVariable("id") Long id,@PathVariable("vendor") boolean vendor, Model model){
		GoodsRequestEntity goodsRequest = goodsRequestService.getRequest(id);
		model.addAttribute("vendor", vendor);  
		model.addAttribute("po", goodsRequest);
		return "back/request/requestView";
	}
	
	@RequestMapping(value = "material", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> materialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request, HttpSession session){
		return materialService.materialList1(pageNumber, pageSize, model, request, session);
	}
	
	@RequestMapping("filesUpload")
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
			ExcelUtil<GoodsRequestTransfer> excelutil = new ExcelUtil<GoodsRequestTransfer>(savefile.getPath(), new ExcelAnnotationReader(GoodsRequestTransfer.class), getLogger()); 
			List<GoodsRequestTransfer> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = goodsRequestService.combineGoodsRequest(list, getLogger());
			if(flag) {
				map.put("msg", "导入要货单成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入要货单失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入要货单失败!");  
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
		GoodsRequestEntity goodsrequest = goodsRequestService.getRequest(id);
		goodsRequestService.publishGoodsRequest(goodsrequest);
		redirectAttributes.addFlashAttribute("message", "发布要货单 " + goodsrequest.getRequestCode() + "成功");
		return "redirect:/admin/user";
	}
	
	/**
	 * 批量发布要货单
	 * @param requestList
	 * @return
	 */
	@RequestMapping(value = "publishRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishRequests(@RequestBody List<GoodsRequestEntity> requestList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		goodsRequestService.publishGoodsRequests(requestList, new UserEntity(user.id));
		map.put("message", "发布要货单成功");
		map.put("success", true);
		return map;
	}

	/**
	 * 确认要货单
	 * @param requestList
	 * @return
	 */
	@RequestMapping(value = "confirmRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmRequests(@RequestBody List<GoodsRequestEntity> requestList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		goodsRequestService.confirmGoodsRequests(requestList, new UserEntity(user.id));
		map.put("message", "确认要货单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 关闭要货单
	 * @param requestList
	 * @return
	 */
	@RequestMapping(value = "closeRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> closeRequests(@RequestBody List<GoodsRequestEntity> requestList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		goodsRequestService.closeGoodsRequests(requestList, new UserEntity(user.id));
		map.put("message", "关闭要货单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 查看要货单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "requestitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long requestid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_request.id", requestid+"");
		Page<GoodsRequestItemEntity> userPage = goodsRequestService.getGoodsRequestItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
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
	
	
	@RequestMapping(value = "createReq", method = RequestMethod.GET)
	public String createReq(Model model,ServletRequest request){
		GoodsRequestEntity req = goodsRequestService.createRequest();
		model.addAttribute("po", req);
		return "back/request/requestAdd";
	}
	
	/**
	 * 添加要货单
	 * @param goodsRequest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addRequest")
	@ResponseBody
	public Map<String, Object> addRequest(GoodsRequestEntity goodsRequest,@RequestParam(value="datas") String reqItemDatas) throws Exception{//
		map = new HashMap<String, Object>();
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();//获得当前登录的用户
		OrganizationEntity buyer = new OrganizationEntity();
		buyer.setId(shiroUser.orgId);
		goodsRequest.setBuyer(buyer);
		
		OrganizationEntity vendor = new OrganizationEntity(); 
		vendor.setId(goodsRequest.getVendorId());
		goodsRequest.setVendor(vendor); 
		goodsRequest.setPurchaseUser(new UserEntity(shiroUser.id));		 
		goodsRequest.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_NO);
		goodsRequest.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
		goodsRequest.setCloseStatus(PurchaseConstans.CLOSE_STATUS_NO);
		goodsRequest.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
		goodsRequest.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
		OrganizationEntity sender = new OrganizationEntity();
		//没有取到发货方，设置为第三方仓库发货
		if(goodsRequest.getSenderId()==null){
			goodsRequest.setSenderId(-1l);
		}
		sender.setId(goodsRequest.getSenderId());
		goodsRequest.setSender(sender); 
		
		List<GoodsRequestItemEntity> goodsRequestItemList = new ArrayList<GoodsRequestItemEntity>();
		JSONObject object = JSONObject.fromObject(reqItemDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		GoodsRequestItemEntity item = null;
		MaterialEntity material = null;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new GoodsRequestItemEntity();
			item.setRequest(goodsRequest);
			item.setItemNo(StringUtils.convertToInteger(object.get("itemNo") + ""));
			material=new MaterialEntity();
			material.setId(StringUtils.convertToLong(object.get("material") + ""));
			item.setMaterial(material);
			item.setReceiveOrg(goodsRequest.getReceiveOrg());
			item.setOrderQty(StringUtils.convertToDouble(object.get("orderQty") + ""));
			item.setCurrency(StringUtils.convertToString(object.get("currency")));
			item.setUnitName(StringUtils.convertToString(object.get("unitName")));
			item.setRequestTime(DateUtil.stringToTimestamp(object.get("requestTime") + "", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));	
			item.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
			item.setDeliveryStatus(PurchaseConstans.DELIVERY_STATUS_NO);
			item.setReceiveStatus(PurchaseConstans.RECEIVE_STATUS_NO);
			goodsRequestItemList.add(item);
		}	
		goodsRequestService.addNewGoodRequest(goodsRequest, goodsRequestItemList);
		map.put("message", "新增要货单成功");
		map.put("success",true);
		return map;  
	}
	
	
	/**
	 * 修改要货单明细
	 */
	@RequestMapping(value = "updateRequest",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateGoodsRequest(@Valid GoodsRequestEntity goodsrequest,@RequestParam(value="datas") String billItemdatas) throws Exception{		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = new HashMap<String, Object>();
		//要货单明细
		List<GoodsRequestItemEntity> goodsRequestItemList = new ArrayList<GoodsRequestItemEntity>();
		GoodsRequestItemEntity item = null;
		JSONObject object = JSONObject.fromObject(billItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new GoodsRequestItemEntity();
			long id=StringUtils.convertToLong(object.get("id") + "");
			item=goodsRequestService.getRequestItem(id);
			item.setOrderQty(StringUtils.convertToDouble(object.get("orderQty") + ""));
			if(!MatcherUtil.isNumber(object.get("requestTime") + ""))   
				item.setRequestTime(DateUtil.stringToTimestamp(object.get("requestTime") + "", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));	
			goodsRequestItemList.add(item);   
		}
		goodsRequestService.saveGoodsRequest(goodsrequest, goodsRequestItemList, new UserEntity(user.id));
		map.put("message", "修改采购计划成功");
		map.put("success", true);
		return map;   
	}
	
}
