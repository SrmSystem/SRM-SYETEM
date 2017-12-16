package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.service.BuyerVendorRelService;

@Controller
@RequestMapping("/manager/vendor/buyerVendorRel")
public class BuyerVendorRelController{

	@Autowired
	private BuyerVendorRelService buyerVendorRelService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private MaterialTypeService materialTypeService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;

	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/buyerList";
	}
	
	@RequestMapping(value="getBuyerVendorRelList/{buyerId}")
	public String displayVendor(@PathVariable("buyerId") Long buyerId,Model model){
		model.addAttribute("buyerId",buyerId);
		return "back/vendor/buyerVendorRelList";
	}
	
	@RequestMapping(value="getBuyerMaterialRelList/{buyerId}")
	public String displayMaterial(@PathVariable("buyerId") Long buyerId,Model model){
		model.addAttribute("buyerId",buyerId);
		return "back/vendor/buyerMaterialRelList";
	}
	
	@RequestMapping(value="getBuyerMaterialTypeRelList/{buyerId}")
	public String displayMaterialType(@PathVariable("buyerId") Long buyerId,Model model){
		model.addAttribute("buyerId",buyerId);
		return "back/vendor/buyerMaterialTypeRelList";
	}
	
	/**
	 * 所有采购组织
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> buyerList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "0");
		searchParamMap.put("IN_id", buyerOrgPermissionUtil.getBuyerIds());
		Page<OrganizationEntity> page = buyerVendorRelService.getOrgList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 某个采购组织下面的供应商
	 * @param buyerId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getBuyerVendorRelList/{buyerId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorMaterialTypeRelList(@PathVariable("buyerId")Long buyerId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_buyerId", buyerId);
		Page<BuyerVendorRelEntity> page = buyerVendorRelService.getBuyerVendorRelList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getBuyerMaterialRelList/{buyerId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getBuyerMaterialRelList(@PathVariable("buyerId")Long buyerId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_buyerId", buyerId);
		Page<BuyerMaterialRelEntity> page = buyerVendorRelService.getBuyerMaterialRelList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getBuyerMaterialTypeRelList/{buyerId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getBuyerMaterialTypeRelList(@PathVariable("buyerId")Long buyerId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_buyerId", buyerId);
		Page<BuyerMaterialTypeRelEntity> page = buyerVendorRelService.getBuyerMaterialTypeRelList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	/**
	 * 选择供应商
	 * @return
	 */
	@RequestMapping(value = "selVendor/{buyerId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selVendor(@PathVariable("buyerId") Long buyerId,@RequestBody List<OrganizationEntity> vendorList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.saveBuyerVendorRel(buyerId, vendorList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "selMaterial/{buyerId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selMaterial(@PathVariable("buyerId") Long buyerId,@RequestBody List<MaterialEntity> materialList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.saveBuyerMaterialRel(buyerId, materialList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "selMaterialType/{buyerId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selMaterialType(@PathVariable("buyerId") Long buyerId,@RequestBody List<MaterialTypeEntity> materialList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.saveBuyerMaterialTypeRel(buyerId, materialList);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除组织下的供应商关系
	 * @return
	 */
	@RequestMapping(value = "delBuyerVendorRelList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delBuyerVendorRelList(@RequestBody List<BuyerVendorRelEntity> relList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.deleteBuyerVendorRelList(relList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "delBuyerMaterialRelList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delBuyerMaterialRelList(@RequestBody List<BuyerMaterialRelEntity> relList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.deleteBuyerMaterialRelList(relList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "delBuyerMaterialTypeRelList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delBuyerMaterialTypeRelList(@RequestBody List<BuyerMaterialTypeRelEntity> relList){
		Map<String,Object> map = new HashMap<String, Object>();
		buyerVendorRelService.deleteBuyerMaterialTypeRelList(relList);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "getVendorList",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "1");
		Page<OrganizationEntity> page = buyerVendorRelService.getOrgList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getMaterialList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaterialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		/*Page<MaterialEntity> userPage = materialService.getMaterialList(pageNumber,pageSize,searchParamMap);*/
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<MaterialEntity> list = materialService.getMaterialListByBuyer(pageNumber,pageSize,searchParamMap,user.orgId);
		map = new HashMap<String, Object>();
		map.put("rows",list);
		map.put("total",materialService.getMaterialListCount(searchParamMap,user.orgId));
		return map;
	}
	
	@RequestMapping(value="getMaterialTypeList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaterialTypeList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		Page<MaterialTypeEntity> userPage = materialTypeService.getMatTypeList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
}
