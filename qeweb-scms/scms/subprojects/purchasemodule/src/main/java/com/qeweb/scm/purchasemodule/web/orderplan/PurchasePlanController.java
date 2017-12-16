package com.qeweb.scm.purchasemodule.web.orderplan;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVenodrItemEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanService;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;

/**
 * 采购计划管理
 * @author alex
 * @date 2015年4月20日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchasePlanController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseplan")
public class PurchasePlanController implements ILog {
	
	private ILogger logger = new FileLogger();
	 
	private Map<String,Object> map;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PurchasePlanService purchasePlanService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/orderplan/orderPlanList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<PurchasePlanEntity> userPage = purchasePlanService.getPurchasePlans(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getPlan/{id}")
	@ResponseBody
	public PurchasePlanEntity getPlan(@PathVariable("id") Long id){
		return purchasePlanService.getPlan(id);
	}
	
	@RequestMapping(value = "planitem/{planid}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		Page<PurchasePlanVenodrItemEntity> userPage = purchasePlanService.getPurchasePlanVendorItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 批量发布采购计划
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "publishPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishPlan(@RequestBody List<PurchasePlanEntity> planList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.publishPlans(planList);;
		map.put("success", true);
		return map;
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchasePlanTransfer.class), getLogger()); 
			List<PurchasePlanTransfer> list = (List<PurchasePlanTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = purchasePlanService.combinePurchasePlan(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购计划成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购计划失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
			map.put("success", false);
			e.printStackTrace();
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));   
		}
		return map;   
	}
	
	@RequestMapping(value = "updatePlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatePlan(@Valid PurchasePlanEntity purchasePlan, @RequestParam(value="datas") String billItemdatas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		//采购计划明细
		List<PurchasePlanVenodrItemEntity> purchasePlanItem = new ArrayList<PurchasePlanVenodrItemEntity>();
		PurchasePlanVenodrItemEntity item = null;
		JSONObject object = JSONObject.fromObject(billItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new PurchasePlanVenodrItemEntity(); 
			item.setId(StringUtils.convertToLong(object.get("id") + ""));
			item.setPlanQty(StringUtils.convertToDouble(object.get("planQty") + ""));
			purchasePlanItem.add(item);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchasePlanService.savePurchasePlan(purchasePlan, purchasePlanItem, new UserEntity(user.id));
		map.put("message", "修改采购计划成功");
		map.put("success", true);
		return map;   
	}
	
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("采购计划", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = new HashMap<String, Object>();   
		searchParamMap.put("EQ_plan.id", request.getParameter("id"));  
		List<PurchasePlanTransfer> list = purchasePlanService.getPurchasePlanVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchasePlanTransfer.class), null);  
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
