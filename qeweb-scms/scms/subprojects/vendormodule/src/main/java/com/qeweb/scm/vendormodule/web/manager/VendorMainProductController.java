package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMainProductService;

/**
 * 主要产品维护
 * @author minming.you
 *
 */
@Controller
@RequestMapping(value = "/manager/vendor/mainProduct")
public class VendorMainProductController {
	@Autowired
    private VendorMainProductService vendorMainProductService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="主要产品维护")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorProductList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> mainProductList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_orgId", user.orgId);

		Page<VendorMaterialTypeRelEntity> page = vendorMainProductService.getMainProductList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "saveMainProduct",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveMainProduct(@RequestBody String mainPt){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorMainProductService.saveMainProduct(mainPt);
		map.put("success", true);
		return map;
	}
	

}
