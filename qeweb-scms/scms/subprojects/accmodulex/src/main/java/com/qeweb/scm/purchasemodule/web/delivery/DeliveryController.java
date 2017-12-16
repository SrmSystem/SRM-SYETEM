package com.qeweb.scm.purchasemodule.web.delivery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.FileDescriptEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.FileDescriptService;
import com.qeweb.scm.purchasemodule.service.PurchaseGoodsRequestService;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.KeyValueVO;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;

/**
 * 采购发货单管理
 * @author chao.gu
 * @date 2017.05.27
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
	
	@Autowired
	private FileDescriptService fileDescriptService;
	
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	
	@Autowired
	private DictItemService dictItemService;
	
	@Autowired
	private PurchaseGoodsRequestService goodsRequestService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private BacklogService backlogService;
	/**
	 * 待发货看板【供】
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pending",method = RequestMethod.GET)
	public String pendingList(Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		if(0 == user.orgRoleType.intValue()){//采购商
			model.addAttribute("vendor", false);
		}else{
			model.addAttribute("vendor", true);
		}
		return "back/delivery/deliveryPendingList";
	}
	
	/**
	 * 发货看板列表【供】
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
//	@RequiresPermissions("dlv:pending:view")
	@RequestMapping(value="pending",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPendingList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(0 == user.orgRoleType.intValue()){//采购商
			List<Long> groupIds = buyerOrgPermissionUtil.getGroupIds();
			if(null ==groupIds ||  groupIds.size() <= 0){
				groupIds = new ArrayList<Long>();
				groupIds.add(-1L);
			}
			//采购组权限
			searchParamMap.put("IN_order.purchasingGroup.id",groupIds );
		}else{
			searchParamMap.put("EQ_order.vendor.id", user.orgId + "");    
		}
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> idList= backlogService.getBackLogValueIdsById(backlogId);
			searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
				//待办结束
		Page<PurchaseOrderItemPlanEntity> userPage = deliveryService.getPendingDeliverys(pageNumber,pageSize,searchParamMap,null);
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 待发货看板-创建发货单【供】
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="createDelivery")
	@ResponseBody
	public DeliveryEntity createDelivery(HttpSession session,ServletRequest request) throws UnsupportedEncodingException {
		
		String col = new String(request.getParameter("col").getBytes("iso8859-1"),"UTF-8");
		String[] cols = col.split(",");
		//1、删除session中检验报告
		session.removeAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT);
		//2、创建发货信息主表
		PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity = deliveryService.getPurchaseOrderItemPlanById(Long.valueOf(cols[0]));
		DeliveryEntity delivery = new DeliveryEntity();
		String str=serialNumberService.geneterNextNumberByKey(OddNumbersConstant.DELIVERY).substring(8);
		String vendorCode=purchaseOrderItemPlanEntity.getOrder().getVendor().getMidCode()==null?"":purchaseOrderItemPlanEntity.getOrder().getVendor().getMidCode();
		delivery.setDeliveryCode(vendorCode+str); 
		delivery.setBuyer(purchaseOrderItemPlanEntity.getOrder().getBuyer());
		delivery.setVendor(purchaseOrderItemPlanEntity.getOrder().getVendor());
		delivery.setDeliveryType(purchaseOrderItemPlanEntity.getOrder().getOrderType());
		delivery.setDeliveryAddress(purchaseOrderItemPlanEntity.getOrderItem().getReceiveOrg()); //收货地址
		delivery.setCompanyCode(cols[1]);//公司代码
		delivery.setCompanyName(cols[2]);//公司名称
		
		//获得要货计划
		PurchaseGoodsRequestEntity goodsRequest = goodsRequestService.getPurchaseGoodsRequestById(purchaseOrderItemPlanEntity.getGoodsRequestId());
		delivery.setYsts(StringUtils.convertToInteger(goodsRequest.getYsts()));  //物流天数
		delivery.setShipType(purchaseOrderItemPlanEntity.getShipType());//发货类型
		return delivery;
	}
	
	/**
	 * 发货看板-创建发货单明细【供】
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="createDeliveryItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDeliveryItem(Model model,ServletRequest request){
		if(request.getParameter("search-IN_id") == null)
			return null;
		PurchaseOrderItemPlanEntity purchaseOrderItemPlanEntity;
		String[] search = request.getParameter("search-IN_id").split(",");
		long[] ids = StringUtils.convertToLong(search);
		List<Long> list = new ArrayList<Long>();
		for (long id : ids)
			list.add(id);
		List<PurchaseOrderItemPlanEntity> userPage = deliveryService.getPendingDeliverys(list);
		OrganizationEntity vendor=userPage.get(0).getOrder().getVendor();
		String vendorCode=vendor.getCode().substring(0,2);
		
		for (int i=0;i<userPage.size();i++) {
			purchaseOrderItemPlanEntity=userPage.get(i);
			purchaseOrderItemPlanEntity.setSendQty(purchaseOrderItemPlanEntity.getShouldQty());
			purchaseOrderItemPlanEntity.setOrderCode(purchaseOrderItemPlanEntity.getOrder().getOrderCode());
			VendorMaterialRelEntity rel=vendorMaterialRelDao.findByOrgIdAndMaterialIdAndFactoryId(purchaseOrderItemPlanEntity.getOrder().getVendor().getId(), purchaseOrderItemPlanEntity.getMaterial().getId(), purchaseOrderItemPlanEntity.getOrderItem().getFactoryId());
			if(rel!=null){
				purchaseOrderItemPlanEntity.setStandardBoxNum(rel.getMaxPackageQty());
				purchaseOrderItemPlanEntity.setMinPackageQty(rel.getMinPackageQty());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows", userPage);
		map.put("total", userPage.size());
		return map;
	}
	

	
	/**
	 * 图纸上传【采】
	 * @author chao.gu
	 * 20170424
	 * @param id
	 * @param files
	 * @return
	 */
	@RequestMapping("inspectUploadFile")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> inspectUploadFile(@RequestParam("fids") String fids,@RequestParam("inspectfiles") MultipartFile files,HttpSession session) {
		File savefile = null;
		map = new HashMap<String, Object>();
		try{ 
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				throw new Exception();
			}
			//2、保存对象 放入session
			String filePath=savefile.getPath().replaceAll("\\\\", "/");
			Map<Long,KeyValueVO> sessionInspectReportMap=(Map<Long, KeyValueVO>) session.getAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT);
			if(null==sessionInspectReportMap){
				sessionInspectReportMap=new HashMap<Long, KeyValueVO>();
			}
			
			KeyValueVO keyValue=sessionInspectReportMap.get(Long.valueOf(fids));
			if(null==keyValue){
				keyValue=new KeyValueVO();
			}
			
			keyValue.setKey(savefile==null?"":files.getOriginalFilename());
			keyValue.setValue(filePath);
			sessionInspectReportMap.put(Long.valueOf(fids), keyValue);
			session.setAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT, sessionInspectReportMap);
			
			if(null!=savefile){
				map.put("success", true);
				map.put("fileName", files.getOriginalFilename());
				map.put("filePath", filePath);
				map.put("msg", "文件上传成功");
			}else{
				map.put("success", false);
				map.put("msg", "文件上传失败");
			}
	
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		} 
		return map;   
	}
	
	/**
	 * 验证保存发货单的运输方式
	 * @param flag
	 * @param delivery
	 * @param files
	 * @return
	 */
	public boolean validateTransportType(boolean flag,DeliveryEntity delivery,MultipartFile files,StringBuffer str){
		String transportType=delivery.getTransportType();
		if(null==transportType){
			flag=false;
			str.append("运输方式不能为空!");
		}else{
			//1、当运输方式选择为‘快递’和‘物流’，验证必须要上传附件
			if("EXPRESS".equals(transportType) || "LOGISTICS".equals(transportType)){
				if(!CommonUtil.isNotNullAndNotEmpty(files) || !CommonUtil.isNotNullAndNotEmpty(files.getOriginalFilename())) {
					flag=false;
					str.append("运输方式为'快递'或'物流'时,附件不能为空！");
				}
			}else if("OTHER".equals(transportType)){//2、当运输方式选择‘其他’，备注字段为必填项
				if(StringUtils.isEmpty(delivery.getRemark().trim())){
					flag=false;
					str.append("运输方式为'其他',备注不能为空！");
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 发货看板-保存发货单【供】
	 * @param deliveryCode 发货单号
	 * @param datas 发货明细
	 * @param type 0:提交审核 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "saveDelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveDelivery(DeliveryEntity delivery,@RequestParam("files") MultipartFile files,@RequestParam(value="datas") String datas, @RequestParam(value="type") String type,HttpSession session) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		File vendorFile = null;
		boolean flag = true;
		StringBuffer str=new StringBuffer();
		if(null==delivery.getExpectedArrivalTime()){
			flag=false;
			str.append("预计到货时间不能为空！");
		}else if(null!=delivery.getPlanDeliveryDate()){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String currTime=sdf.format(DateUtil.getCurrentTimestamp());
				if(delivery.getPlanDeliveryDate().before(DateUtil.getTimestamp(currTime))){
					flag=false;
					str.append("预计发货时间不能小于系统当前时间！");
				}
		}
		
		//验证运输方式
		flag=validateTransportType(flag,delivery,files,str);
		
		delivery.setStatus(PurchaseConstans.COMMON_NORMAL);
		if(CommonUtil.isNotNullAndNotEmpty(files) && CommonUtil.isNotNullAndNotEmpty(files.getOriginalFilename())) {
			vendorFile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			delivery.setDeliveryFilePath(vendorFile.getPath().replaceAll("\\\\", "/"));		  //文件地址
			delivery.setDeliveryFileName(files.getOriginalFilename()); //文件名称
		}
		List<DeliveryItemEntity> deliveryItem = new ArrayList<DeliveryItemEntity>();
		//获取上传的检验报告
		Map<Long,KeyValueVO> sessionInspectReportMap=(Map<Long, KeyValueVO>) session.getAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT);
		//组装数据
		DeliveryItemEntity item = null;
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new DeliveryItemEntity();
			item.setOrderItemPlanId(StringUtils.convertToLong(object.get("id") + ""));
			Double sendQty = null;
			if(null==object.get("sendQty") || "null".equals(object.get("sendQty")+"")){
				flag=false;
				str.append("发货数量不能为空！");
			}else{
				sendQty=StringUtils.convertToDouble(object.get("sendQty") + "");
				item.setDeliveryQty(sendQty);
			}
			if(null!=sessionInspectReportMap){//组装检验报告数据
				KeyValueVO reportVO=sessionInspectReportMap.get(StringUtils.convertToLong(object.get("id") + ""));
				if(null!=reportVO){
					item.setInspectionName(reportVO.getKey());
					item.setInspectionPath(reportVO.getValue());
				}
			}
			Double standardBoxNum=StringUtils.convertToDouble(object.get("standardBoxNum") + "");
			item.setStandardBoxNum(standardBoxNum);//标准箱数
			
			Double minPackageQty=StringUtils.convertToDouble(object.get("minPackageQty") + "");
			item.setMinPackageQty(minPackageQty);//小包装数
			
			
			if(null!=sendQty && null!=standardBoxNum && standardBoxNum>0){
				item.setBoxNum(StringUtils.convertToString(Math.ceil(sendQty/standardBoxNum)));
			}
			
			if(null!=sendQty && null!=minPackageQty && minPackageQty>0){
			item.setMinBoxNum(StringUtils.convertToString(Math.ceil(sendQty/minPackageQty)));
			}
			
			
			if(null!=object.get("manufactureDate") &&  !"null".equals(object.get("manufactureDate")+"")){//生产日期
				item.setManufactureDate(DateUtil.stringToTimestamp(object.get("manufactureDate")+"", DateUtil.DATE_FORMAT_YYYY_MM_DD));
			}else{
				flag=false;
				str.append("生产日期不能为空！");
			}
			
			if(null==object.get("version") || "null".equals(object.get("version")+"")){
				flag=false;
				str.append("版本不能为空！");
			}else{
				item.setVersion((object.get("version") + ""));//版本
			}
			
			if(null==object.get("charg") || "null".equals(object.get("charg")+"")){
				flag=false;
				str.append("批号不能为空！");
			}else{
				item.setCharg((object.get("charg") + "").toUpperCase());//批次
			}
			
			if(null!=object.get("remark") && !"null".equals(object.get("remark")+"")){
			    item.setRemark((object.get("remark") + ""));//备注
			}
			
			if(null==object.get("vendorCharg")|| "null".equals(object.get("vendorCharg")+"")){
				flag=false;
				str.append("追溯批号不能为空！");
			}else{
				item.setVendorCharg((object.get("vendorCharg") + "").toUpperCase());//追溯批号
			}
			
			item.setMeins((object.get("unitName") + ""));//单位
			deliveryItem.add(item);
		}
		if(flag){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			StringBuffer msg = new StringBuffer();
			flag=deliveryService.saveDelivery(delivery, deliveryItem, new UserEntity(user.id), type,msg);
			if(flag){
				map.put("message", "发货单提交审核成功");
			}else{
				map.put("message", msg);
			}
		}else{
			map.put("message", str.toString());
		}
			map.put("success", flag);
		return map;   
	}
	
	
	
	
	/**
	 * 发货记录
	 * @param theme
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("dlv:view")
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
		
		//默认显示未发货的数据
		if(!searchParamMap.containsKey("EQ_deliveryStatus")){
			searchParamMap.put("EQ_deliveryStatus", PurchaseConstans.DELIVERY_STATUS_NO);
		}
		Page<DeliveryEntity> userPage = deliveryService.getDeliverys(pageNumber,pageSize,searchParamMap);
		userPage = deliveryService.initData(userPage);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());   
		return map;
	}
	
	/**
	 * 发货记录-保存发货单提交审核【供】
	 * @param deliveryCode 发货单号
	 * @param datas 发货明细
	 * @param type 0:提交审核 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "editDelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editDelivery(DeliveryEntity pageDelivery,@RequestParam("files") MultipartFile files,@RequestParam(value="datas") String datas, @RequestParam(value="type") String type,HttpSession session) throws Exception{
		StringBuffer msg = new StringBuffer();
		boolean flag=true;
		Map<String,Object> map = new HashMap<String, Object>();
		flag=deliveryService.validateEdit("edit",msg,datas);
		if(!flag){
		     map.put("message", msg.toString());
		     map.put("success", flag);
			 return map;
		}
		
		StringBuffer str=new StringBuffer();
		
		DeliveryEntity delivery=deliveryService.getDeliveryById(pageDelivery.getId());
		//设置发货主表的值 start
		delivery.setTransportType(pageDelivery.getTransportType());//运输方式
		delivery.setDeliveryContacter(pageDelivery.getDeliveryContacter());//收货联系人
		delivery.setDeliveryAddress(pageDelivery.getDeliveryAddress());//货地址
		delivery.setLogisticsCompany(pageDelivery.getLogisticsCompany());//物流公司
		delivery.setLogisticsContacter(pageDelivery.getLogisticsContacter());//物流联系人
		delivery.setDeliveryTel(pageDelivery.getDeliveryTel());//收货联系电话
		delivery.setLogisticsTel(pageDelivery.getLogisticsTel());//物流联系电话
		delivery.setAnzpk(pageDelivery.getAnzpk());//总箱数
		delivery.setPlanDeliveryDate(pageDelivery.getPlanDeliveryDate());//预计发货时间
		delivery.setYsts(pageDelivery.getYsts());//物流天数
		delivery.setExpectedArrivalTime(pageDelivery.getExpectedArrivalTime());//预计到货时间
		// end
		
		if(null!=delivery.getPlanDeliveryDate()){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String currTime=sdf.format(DateUtil.getCurrentTimestamp());
				if(delivery.getPlanDeliveryDate().before(DateUtil.getTimestamp(currTime))){
					flag=false;
					str.append("预计发货时间不能小于系统当前时间！");
				}
		}
		
		//验证运输方式
		flag=validateTransportType(flag,delivery,files,str);
		
		File vendorFile = null;
		delivery.setStatus(PurchaseConstans.COMMON_NORMAL);
		if(CommonUtil.isNotNullAndNotEmpty(files) && StringUtils.isNotEmpty(files.getOriginalFilename())) {
			vendorFile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			delivery.setDeliveryFilePath(vendorFile.getPath().replaceAll("\\\\", "/"));		  //文件地址
			delivery.setDeliveryFileName(files.getOriginalFilename()); //文件名称
		}
		List<DeliveryItemEntity> deliveryItem = new ArrayList<DeliveryItemEntity>();//保存数据库的list
		//获取上传的检验报告
		Map<Long,KeyValueVO> sessionInspectReportMap=(Map<Long, KeyValueVO>) session.getAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT);
		//组装数据
		DeliveryItemEntity item = null;
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item =deliveryService.getDeliveryItemById(StringUtils.convertToLong(object.get("id") + ""));
			item.setOrderItemPlanId(item.getOrderItemPlan().getId());
			item.setOldDeliveryQty(item.getDeliveryQty());
			Double sendQty=null;
			if(null==object.get("sendQty") || "null".equals(object.get("sendQty")+"")){
				flag=false;
				str.append("发货数量不能为空！");
			}else{
				sendQty=StringUtils.convertToDouble(object.get("sendQty") + "");
				if(sendQty>item.getDeliveryQty() && PurchaseConstans.AUDIT_YES.intValue()==delivery.getAuditStatus().intValue()){
					flag=false;
					str.append("行号：").append(item.getItemNo()).append(",审核通过的发货单,修改的发货数量不能大于原先的发货数量:").append(item.getDeliveryQty()).append("！");
				}else{
					item.setDeliveryQty(StringUtils.convertToDouble(object.get("sendQty") + ""));
				}
			}
			if(null!=sessionInspectReportMap){//组装检验报告数据
				KeyValueVO reportVO=sessionInspectReportMap.get(StringUtils.convertToLong(object.get("id") + ""));
				if(null!=reportVO){
					item.setInspectionName(reportVO.getKey());
					item.setInspectionPath(reportVO.getValue());
				}
			}
			
			Double standardBoxNum=StringUtils.convertToDouble(object.get("standardBoxNum") + "");//标准箱数
			item.setStandardBoxNum(standardBoxNum);//标准箱数
			
			Double minPackageQty=StringUtils.convertToDouble(object.get("minPackageQty") + "");
			item.setMinPackageQty(minPackageQty);//小包装数
			
			
			if(null!=sendQty && null!=standardBoxNum && standardBoxNum>0){
				item.setBoxNum(StringUtils.convertToString(Math.ceil(sendQty/standardBoxNum)));//箱数量
			}
			
			if(null!=sendQty && null!=minPackageQty && minPackageQty>0){
			item.setMinBoxNum(StringUtils.convertToString(Math.ceil(sendQty/minPackageQty)));//小包装箱数
			}
			
			if(null!=object.get("manufactureDate") &&  !"null".equals(object.get("manufactureDate")+"")){//生产日期
				item.setManufactureDate(DateUtil.stringToTimestamp(object.get("manufactureDate")+"", DateUtil.DATE_FORMAT_YYYY_MM_DD));
			}else{
				flag=false;
				str.append("生产日期不能为空！");
			}
			
			if(null==object.get("version") || "null".equals(object.get("version")+"")){
				flag=false;
				str.append("版本不能为空！");
			}else{
				item.setVersion((object.get("version") + ""));//版本
			}
			
			if(null==object.get("charg") || "null".equals(object.get("charg")+"")){
				flag=false;
				str.append("批号不能为空！");
			}else{
				item.setCharg((object.get("charg") + "").toUpperCase());//批次
			}
			
			if(null!=object.get("remark") && !"null".equals(object.get("remark")+"")){
			    item.setRemark((object.get("remark") + ""));//备注
			}
			
			if(null==object.get("vendorCharg")|| "null".equals(object.get("vendorCharg")+"")){
				flag=false;
				str.append("追溯批号不能为空！");
			}else{
				item.setVendorCharg((object.get("vendorCharg") + "").toUpperCase());//追溯批号
			}
			
			
			
			item.setMeins((object.get("unitName") + ""));//单位
			
			
			deliveryItem.add(item);
			
		}
		if(flag){
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		flag=deliveryService.saveDelivery(delivery, deliveryItem, new UserEntity(user.id), "edit",msg);
		if(flag){
			map.put("message", "发货单提交审核成功");
		}else {
			map.put("message", msg);
		}
		}else{
			map.put("message", str.toString());
		}
		map.put("success", flag);
		return map;   
	}
	
	/**
	 * 获取发货主单信息-查看
	 * @param id
	 * @return
	 */
	@RequestMapping("getDeliveryView/{id}")
	public String getDeliveryView(@PathVariable("id") Long id,HttpSession session,Model model){
		DeliveryEntity entity = deliveryService.getDeliveryById(id);
		if(entity.getDeliveryFilePath() != null){
			entity.setDeliveryFilePath(entity.getDeliveryFilePath().replaceAll("\\\\", "/"));
		}
		DictItemEntity dictItem = dictItemService.findDictItemByCode(entity.getTransportType());
		entity.setTransportName(dictItem.getName());
		
		List<DeliveryItemEntity> userPage =deliveryService.getDeliveryItems(deliveryService.getDeliveryById(id));
		if(userPage.size()>0){
			PurchaseOrderItemPlanEntity plan=deliveryService.getPurchaseOrderItemPlanById(userPage.get(0).getOrderItemPlan().getId());
			entity.setCompanyCode(plan.getOrder().getCompany().getCode());
			entity.setCompanyName(plan.getOrder().getCompany().getName());
		}
		
		
		model.addAttribute("po", entity);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(0 == user.orgRoleType.intValue()){//采购商
			model.addAttribute("vendor", false);
		}else{//供应商
			model.addAttribute("vendor", true);
		}
		return "back/delivery/deliveryCommonView";
	}
	
	/**
	 * 获取发货主单信息-编辑
	 * @param id
	 * @return
	 */
	@RequestMapping("getDelivery/{id}")
	@ResponseBody
	public DeliveryEntity getDelivery(@PathVariable("id") Long id,HttpSession session){
		//1、删除session中检验报告
		session.removeAttribute(PurchaseConstans.SESSION_INSPECTION_REPORT);
		DeliveryEntity entity = deliveryService.getDeliveryById(id);
		
		List<DeliveryItemEntity> userPage =deliveryService.getDeliveryItems(deliveryService.getDeliveryById(id));
		if(userPage.size()>0){
			PurchaseOrderItemPlanEntity plan=deliveryService.getPurchaseOrderItemPlanById(userPage.get(0).getOrderItemPlan().getId());
			entity.setCompanyCode(plan.getOrder().getCompany().getCode());
			entity.setCompanyName(plan.getOrder().getCompany().getName());
		}
		
		if(entity != null && entity.getDeliveyTime() == null){
			entity.setDeliveyTime(DateUtil.getCurrentTimestamp());
		}
		return entity;
	}
	
	/**
	 * 根据发货主单 查看发货单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deliveryitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long deliveryid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", 0);
		searchParamMap.put("EQ_delivery.id", deliveryid + "");
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 根据订单明细 查看发货单详情--页面
	 * @param orderItemId
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("displayDeliveryItemsByOrderItemIdView/{orderItemId}")
	public String displayDeliveryItemsByOrderItemIdView(@PathVariable("orderItemId") Long orderItemId,HttpSession session,Model model){
			model.addAttribute("orderItemId", orderItemId);
		return "back/delivery/deliveryItemView";
	}
	
	/**
	 * 根据订单明细 查看发货单详情--数据
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getDlvItemsByOderItemId/{orderItemId}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDlvItemsByOderItemId(@PathVariable("orderItemId") Long orderItemId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", 0);
		searchParamMap.put("EQ_orderItem.id",orderItemId);
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 发货列表-修改发货单明细【供】
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getItemListByDlvId/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByDlvId(Model model,ServletRequest request,@PathVariable("id") Long deliveryid){
		List<DeliveryItemEntity> userPage =deliveryService.getDeliveryItems(deliveryService.getDeliveryById(deliveryid));
		Double shouldQty=0D;
		for (DeliveryItemEntity item : userPage) {
			item.setOrderCode(item.getOrderItem().getOrder().getOrderCode());
			
			PurchaseOrderItemPlanEntity plan=deliveryService.getPurchaseOrderItemPlanById(item.getOrderItemPlan().getId());
			item.setOrderQty(plan.getOrderQty());
			item.setReceiveOrg(plan.getReceiveOrg());
			//1、应发数量=订单数量-（已创建未发数量-发货数量）
			shouldQty=BigDecimalUtil.sub(plan.getToDeliveryQty(), item.getDeliveryQty());
			shouldQty=BigDecimalUtil.sub(plan.getOrderQty(), shouldQty);
			item.setShouldQty(shouldQty);
			
			//2、发货数量
			item.setSendQty(item.getDeliveryQty());
			
			//要求到货时间
            item.setRequestTime(plan.getRequestTime());
			//币种
            item.setCurrency(plan.getCurrency());
            //单位
            item.setUnitName(plan.getUnitName());
		}
		map = new HashMap<String, Object>();
		map.put("rows", userPage);
		map.put("total", userPage.size());
		return map;
	}
	
	
	@RequestMapping(value = "byorderdeliveryitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByOrder(@PathVariable("id") Long orderid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.id", orderid + "");   
		searchParamMap.put("EQ_abolished", "0");   
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 通过主订单的id获取明细下面所有的发货单的信息
	 * @param  主订单ID
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "byMainOrderId/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByMainOrder(@PathVariable("id") Long mainOrderId, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.order.id", mainOrderId + "");   
		searchParamMap.put("EQ_abolished", "0");   
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	/**
	 * 通过要货计划id获取明细下面所有的发货单的信息
	 * @param  要货计划id
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "byRequestId")
	@ResponseBody
	public Map<String,Object> getItemListByRequestId(
			@RequestParam(value = "flag") String flag,
			@RequestParam(value = "factoryId") String factoryId,
			@RequestParam(value = "purchasingGroupId") String purchasingGroupId,
			@RequestParam(value = "materialId") String materialId,
			@RequestParam(value = "meins") String meins,
			@RequestParam(value = "vendorId") String vendorId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "beginRq") String beginRq,
			@RequestParam(value = "endRq") String endRq,
			@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_request.flag", flag);
		searchParamMap.put("EQ_request.factory.id", factoryId);
		searchParamMap.put("EQ_request.group.id", purchasingGroupId);
		searchParamMap.put("EQ_request.material.id", materialId);
		searchParamMap.put("EQ_request.meins", meins);
		searchParamMap.put("EQ_request.vendor.id", vendorId);
		searchParamMap.put("GTE_request.rq", beginRq);
		searchParamMap.put("LTE_request.rq", endRq);
		searchParamMap.put("EQ_abolished", "0");   
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	
	/**
	 *  发货记录-批量发货【供】
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "dodelivery",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deliverys(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer msg = new StringBuffer();
		try {
			Boolean isSuccess=deliveryService.dodelivery(deliveryList, new UserEntity(user.id),msg);
			if(isSuccess){
				map.put("message", "发货成功");
				map.put("success", true);
			}else{
				map.put("message", msg);
				map.put("success", false);
			}
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", "操作失败，请联系管理员");
		}
		return map;
	}
	
	/**
	 *  写人：发货详情-行项目重新创建DN号【供】
	 * @param deliveryList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "singleSyncCreateDn/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> singleSyncCreateDn(@PathVariable("id") Long deliveryItemId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer msg=new StringBuffer();
		map.put("method", "singleSyncCreateDn");
		DeliveryItemEntity item  = deliveryService.getDeliveryItemById(deliveryItemId);
		boolean isSuccess=deliveryService.singleSyncCreateDn(item,msg);
		if(isSuccess){
			//验证该发货单下所有的明细是否已经都有DN号
			DeliveryEntity dlv =deliveryService.findDlvById(item.getDelivery().getId());
			if(deliveryService.validateAllDnCreated(dlv)){
				//若是，则直接调用发货
				return doSingledelivery(dlv.getId(),dlv);
			}
			//
			
			
			map.put("message", "写入发货明细成功");
			map.put("success", true);
		}else{
			map.put("message", msg.toString());
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 *  发货记录-单条发货【供】
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "doSingledelivery/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doSingledelivery(@PathVariable("id") Long deliveryId,DeliveryEntity delivery){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("method", "doSingledelivery");
		List<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
		DeliveryEntity deliveryEntity  = deliveryService.getDeliveryById(deliveryId);
		if (deliveryEntity== null) {
			map.put("message", "发货失败");
			map.put("success", false);
			return map;
		}
		deliveryList.add(deliveryEntity);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		StringBuffer msg = new StringBuffer();
		try {
			Boolean isSuccess=deliveryService.dodelivery(deliveryList, new UserEntity(user.id),msg);
			if(isSuccess){
				map.put("message", "发货成功");
				map.put("success", true);
			}else{
				map.put("message", msg);
				map.put("success", false);
			}
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", "操作失败，请联系管理员");
		}
		return map;
	}
	
	/**
	 *  发货记录-批量取消发货【供】
	 * @param deliveryList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "canceldeliverys",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> canceldeliverys(@RequestBody List<DeliveryEntity> deliveryList) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer msg=new StringBuffer();
		boolean isSuccess=deliveryService.cancelDelivery(deliveryList, new UserEntity(user.id, user.orgId),msg);
		if(isSuccess){
			map.put("message", "取消发货成功");
			map.put("success", true);
		}else{
			map.put("message", msg.toString());
			map.put("success", false);
		}
		
		return map;
	}
	
	/**
	 *  发货记录-单个取消发货【供】
	 * @param deliveryList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "singleCancelDelivery/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> singleCancelDelivery(@PathVariable("id") Long deliveryId,DeliveryEntity delivery) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
		DeliveryEntity deliveryEntity  = deliveryService.getDeliveryById(deliveryId);
		if (deliveryEntity== null) {
			map.put("message", "取消发货失败");
			map.put("success", false);
			return map;
		}
		deliveryList.add(deliveryEntity);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer msg=new StringBuffer();
		boolean isSuccess=deliveryService.cancelDelivery(deliveryList, new UserEntity(user.id, user.orgId),msg);
		if(isSuccess){
			map.put("message", "取消发货成功");
			map.put("success", true);
		}else{
			map.put("message", msg.toString());
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 *  发货记录-行项目删除【供】
	 * @param deliveryList
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "singleDeleteDelivery/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> singleDeleteDelivery(@PathVariable("id") Long deliveryItemId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		DeliveryItemEntity item  = deliveryService.getDeliveryItemById(deliveryItemId);
		if (null==item) {
			map.put("message", "删除发货明细失败");
			map.put("success", false);
			return map;
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer msg=new StringBuffer();
		boolean isSuccess=deliveryService.deleteItemDelivery(item, new UserEntity(user.id, user.orgId),msg);
		if(isSuccess){
			map.put("message", "删除发货明细成功");
			map.put("success", true);
		}else{
			map.put("message", msg.toString());
			map.put("success", false);
		}
		return map;
	}
	
	
	
	@RequestMapping("downloadFile")
	public ModelAndView downloadFile(@RequestParam("fileName") String fileName,@RequestParam("fileUrl") String fileUrl,HttpServletRequest request, HttpServletResponse response) throws Exception {

		String filePath=fileUrl;

		//下载文件
		response.setContentType("application/octet-stream;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");    
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		if(filePath == null) {
			return null;
		}
		filePath = filePath.replaceAll("\\\\", "/");
		if(filePath.startsWith("WEB-INF")) {
			filePath =  request.getSession().getServletContext().getRealPath("/") + "/" + filePath;  
		}  
		if(StringUtils.isEmpty(fileName)) {
			fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));
		}  
		File file = new File(filePath);
		try {
			long fileLength = new File(file.getAbsolutePath()).length();
			response.setContentType("application/x-msdownload;");   
//			response.setHeader("Content-disposition", "attachment;filename="  + (new String(fileName.getBytes("GBK"), "ISO8859-1") + "." + getExtensionName(filePath))) ;  
			response.setHeader("Content-disposition", "attachment;filename="  + (new String(fileName.getBytes("GBK"), "ISO8859-1") )) ;
			response.setHeader("Content-Length", String.valueOf(fileLength));  
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];  
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}
	
	public static String getExtensionName(String filepath) {
		if (filepath != null && filepath.length() > 0 && filepath.lastIndexOf(".") > 0) {
			int dot = filepath.lastIndexOf('.');
			if (dot > -1 && dot < filepath.length() - 1) { 
				return filepath.substring(dot + 1);
			}
		}
		return filepath;
	}
	
	
	
	/**
	 * 收货看板点击发货单编号--查看发货单详情
	 * @param itemId
	 * @param model
	 * @return
	 * add by yao.jin 2017.02.22
	 */
	@RequestMapping(value="getAsnInfo/{id}/{receiveStatus}", method = RequestMethod.GET)
	public String getAsnInfo(@PathVariable(value="id") Long id,@PathVariable(value="receiveStatus") String receiveStatus, Model model) {
		DeliveryEntity delivery = deliveryService.getDeliveryById(id);
		if(delivery != null && delivery.getDeliveyTime() == null){
			delivery.setDeliveyTime(DateUtil.getCurrentTimestamp());
		}
		
		
		DictItemEntity dictItem = dictItemService.findDictItemByCode(delivery.getTransportType());
		delivery.setTransportName(dictItem.getName());
		
		List<DeliveryItemEntity> userPage =deliveryService.getDeliveryItems(deliveryService.getDeliveryById(id));
		if(userPage.size()>0){
			PurchaseOrderItemPlanEntity plan=deliveryService.getPurchaseOrderItemPlanById(userPage.get(0).getOrderItemPlan().getId());
			delivery.setCompanyCode(plan.getOrder().getCompany().getCode());
			delivery.setCompanyName(plan.getOrder().getCompany().getName());
		}
		
		
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		delivery.setReceiveUser(user.getName()); 		//收货人一开始默认为当前登录者
		delivery.setReceiveTime(DateUtil.getCurrentTimestamp());
		
		List<FileDescriptEntity> fileDescriptList=new ArrayList<FileDescriptEntity>();
		fileDescriptList=fileDescriptService.getAllEntity();	
		if(StringUtils.isNotEmpty(receiveStatus)&&receiveStatus.equals("1")){
			model.addAttribute("isReceive",true);
		}else{
			model.addAttribute("isReceive",false);
		}
		
		
		model.addAttribute("de", delivery);
		model.addAttribute("fd", fileDescriptList);
		
		return "back/receive/receivePendingView";
	}
	
	
	/**
	 * 保存修改
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveChange",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveChange(@RequestParam(value="datas") String datas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		map = deliveryService.saveChange(datas);
		return map;   
	}
	
	
	
	/**
	 * 根据dn号查补货发货单页面【采】
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toReplDlvItem/{id}",method = RequestMethod.GET)
	public String toReplDlvItem(@PathVariable("id") Long id,Model model) {
		model.addAttribute("id", id);
		return "back/receive/replListView";
	}
	
	/**
	 * 根据dn查补货发货单数据【供】
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="replDlvItem/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> replDlvItem(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		DeliveryItemEntity dlvItem=deliveryService.getDeliveryItemById(id);
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
	    searchParamMap.put("EQ_dn", dlvItem.getDn());   
	    searchParamMap.put("EQ_abolished", 0);  
	    searchParamMap.put("EQ_delivery.shipType", PurchaseConstans.SHIP_TYPE_REPL+"");
		Page<DeliveryItemEntity> userPage = deliveryService.getDeliveryItemsByMap(pageNumber,pageSize,searchParamMap);
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
}
