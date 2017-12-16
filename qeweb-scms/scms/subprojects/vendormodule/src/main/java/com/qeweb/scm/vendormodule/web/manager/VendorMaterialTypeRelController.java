package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMaterialTypeRelService;
/**
 * 物料关系Controller
 * @author lw
 * 创建时间：2015年6月15日 11:09:03
 * 最后更新时间：2015年6月30日09:43:05
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/vendor/vendorMaterialTypeRel")
public class VendorMaterialTypeRelController{

	@Autowired
	private OrgService orgService;
	
	@Autowired
	private VendorMaterialTypeRelService vendorMaterialTypeRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorMaterialTypeRelList";
	}
	
	/**
	 * 所有供应商
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "1");
		Page<OrganizationEntity> page = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 某个供应商下面的物料类型
	 * @param vendorId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getVendorMaterialTypeRelList/{vendorId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorMaterialTypeRelList(@PathVariable("vendorId")Long vendorId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orgId", vendorId);
		Page<VendorMaterialTypeRelEntity> page = vendorMaterialTypeRelService.getVendorMaterialTypeRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 所有物料类型
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getMaterialTypeList",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getMaterialTypeList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		
		Page<MaterialTypeEntity> page = vendorMaterialTypeRelService.getMaterialTypeList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 某个供应商下面的物料类型
	 * @param vendorId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "displayVendorMaterialTypeRelList/{vendorId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> displayVendorMaterialTypeRelList(@PathVariable("vendorId")Long vendorId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orgId", vendorId);
		Page<VendorMaterialTypeRelEntity> page = vendorMaterialTypeRelService.getVendorMaterialTypeRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="displayVendor/{id}")
	public String displayVendor(@PathVariable("id") Long id,Model model){
		OrganizationEntity vendor=orgService.getOrg(id);
//		model.addAttribute("vendorBO",vendor);
		model.addAttribute("vendorId",vendor.getId());
		return "back/vendor/vendorMaterialTypeRelEdit";
	}
	
	@RequestMapping(value="viewVendor/{id}")
	public String viewVendor(@PathVariable("id") Long id,Model model){
		OrganizationEntity vendor=orgService.getOrg(id);
//		model.addAttribute("vendorBO",vendor);
		model.addAttribute("vendorId",vendor.getId());
		return "back/vendor/vendorMaterialTypeRelView";
	}
	
	/**
	 * 删除
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "delRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delRel(@RequestBody List<VendorMaterialTypeRelEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorMaterialTypeRelService.delRel(list);
		map.put("message", "删除成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 选择保存关系
	 * @param vendorId
	 * @param list
	 * @return
	 */
	@RequestMapping(value="selRel/{vendorId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selRel(@PathVariable("vendorId") Long vendorId,@RequestBody List<MaterialTypeEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorMaterialTypeRelService.saveRel(vendorId,list);
		map.put("message", "保存成功");
		map.put("success", true);
		return map;
	}
}
