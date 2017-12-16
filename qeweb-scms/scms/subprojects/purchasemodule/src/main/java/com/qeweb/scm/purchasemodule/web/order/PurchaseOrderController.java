package com.qeweb.scm.purchasemodule.web.order;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.service.PurchaseOrderService;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseOrderTransfer;

/**
 * 采购订单管理
 * @author alex
 * @date 2015年4月22日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchaseOrderController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseorder")
public class PurchaseOrderController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		model.addAttribute("extended", getDynamicDataService().getDynamicData("purchaseOrderEntity"));
		return "back/order/orderList";  
	}
	
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/order/orderList";
	}
	
	@RequestMapping(value="/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(vendor) {
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");  
		}
		Page<PurchaseOrderEntity> userPage = purchaseOrderService.getPurchaseOrders(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getOrder/{id}")
	@ResponseBody
	public PurchaseOrderEntity getOrder(@PathVariable("id") Long id){
		return purchaseOrderService.getOrder(id);
	}
	
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchaseOrderTransfer.class), getLogger()); 
			List<PurchaseOrderTransfer> list = (List<PurchaseOrderTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = purchaseOrderService.combinePurchaseOrder(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购订单成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购订单失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
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
		PurchaseOrderEntity order = purchaseOrderService.getOrder(id);
		purchaseOrderService.publishOrder(order);
		redirectAttributes.addFlashAttribute("message", "发布订单 " + order.getOrderCode() + "成功");
		return "redirect:/admin/user";
	}
	
	/**
	 * 批量发布订单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "publishOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishOrders(@RequestBody List<PurchaseOrderEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.publishOrders(orderList, new UserEntity(user.id));
		map.put("message", "发布订单成功");
		map.put("success", true);
		return map;
	}

	/**
	 * 确认订单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "confirmOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmOrders(@RequestBody List<PurchaseOrderEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.confirmOrders(orderList, new UserEntity(user.id));
		map.put("message", "确认订单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 关闭订单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "closeOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> closeOrders(@RequestBody List<PurchaseOrderEntity> orderList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchaseOrderService.closeOrders(orderList, new UserEntity(user.id));
		map.put("message", "关闭订单成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 查看订单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orderitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long orderid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_order.id", orderid + "");
		Page<PurchaseOrderItemEntity> userPage = purchaseOrderService.getPurchaseOrderItems(pageNumber,pageSize,searchParamMap);
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
}
