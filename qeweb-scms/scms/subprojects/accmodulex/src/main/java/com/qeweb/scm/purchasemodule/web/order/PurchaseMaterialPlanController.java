package com.qeweb.scm.purchasemodule.web.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchaseMaterialPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseMaterialVendorPlanEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseMaterialPlanDao;
import com.qeweb.scm.purchasemodule.service.PurchaseMaterialPlanService;

/**
 * 瑞声弃用
 */
@Controller
@RequestMapping(value = "/manager/order/materialPlan")
public class PurchaseMaterialPlanController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PurchaseMaterialPlanService purchaseMaterialPlanService;
	
	@Autowired
	private PurchaseMaterialPlanDao purchaseMaterialPlanDao;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrganizationDao organizationDao;

	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/order/materialPlanList";  
	}

	
	@RequestMapping(method=RequestMethod.GET,value = "displayVendorList/{materialPlanId}")
	public String displayVendorList(Model model ,@PathVariable("materialPlanId") Long materialPlanId){
		PurchaseMaterialPlanEntity materialPlan=purchaseMaterialPlanDao.findOne(materialPlanId);
		model.addAttribute("materialPlan", materialPlan);
		return "back/order/materialVendorPlanList";  
	}
	
	@RequestMapping(value="getMaterialPlanList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<PurchaseMaterialPlanEntity> userPage = purchaseMaterialPlanService.getMaterialPlanList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getMaterialVendorPlanList/{materialPlanId}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("materialPlanId") long materialPlanId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_planId", materialPlanId);
		Page<PurchaseMaterialVendorPlanEntity> userPage = purchaseMaterialPlanService.getMaterialVendorPlanList(pageNumber, pageSize, searchParamMap);
		if(userPage!=null){
			for (int i = 0 ; i < userPage.getContent().size() ; i++) {
				OrganizationEntity vendor=organizationDao.findOne(userPage.getContent().get(i).getVendor().getId());
				userPage.getContent().get(i).setVendorCode(vendor.getCode());
				userPage.getContent().get(i).setVendorName(vendor.getName());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 选择供应商分配数量保存发布
	 * @param publishStatus
	 * @param plan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveMaterialPlanVendor/{publishStatus}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveMaterialPlanVendor(@PathVariable("publishStatus") Integer publishStatus,@Valid PurchaseMaterialPlanEntity plan) throws Exception{ 
		String items=plan.getTableDatas();
		Map<String,Object> map = new HashMap<String, Object>();
		//明细
		List<PurchaseMaterialVendorPlanEntity> itemList = new ArrayList<PurchaseMaterialVendorPlanEntity>();
		if(!StringUtils.isEmpty(items)){
			PurchaseMaterialVendorPlanEntity item = null;
			JSONObject object = JSONObject.fromObject(items);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new PurchaseMaterialVendorPlanEntity(); 
					item.setId(StringUtils.convertToLong(StringUtils.convertToString(object.get("id"))==null?"0":StringUtils.convertToString(object.get("id"))));
					item.setVendorId(StringUtils.convertToLong(object.get("vendorId")+""));
					item.setPlanNum(StringUtils.convertToDouble(object.get("planNum")+""));
					itemList.add(item);
				}
			}
		}
		plan.setPublishStatus(publishStatus);
		purchaseMaterialPlanService.saveMaterialPlanVendor(plan,itemList);
		map.put("msg", "操作成功");
		map.put("success", true);
		return map;   
	}
	
	
	@RequestMapping(value="getOrgList")
	@ResponseBody
	public Map<String,Object> getOrgList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "1");
		Page<OrganizationEntity> userPage = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
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
