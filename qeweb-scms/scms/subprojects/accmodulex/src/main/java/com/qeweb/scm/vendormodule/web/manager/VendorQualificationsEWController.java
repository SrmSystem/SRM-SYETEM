package com.qeweb.scm.vendormodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.service.VendorSurveyService;

@Controller
@RequestMapping(value="/manager/vendor/vendorQEW")
public class VendorQualificationsEWController {

	@Autowired
	private VendorSurveyService vendorSurveyService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="资质预警管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorQEWList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorQEWList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//通过组织权限查找供应商
		searchParamMap.put("IN_orgId", buyerOrgPermissionUtil.getVendorIds());
		Page<VendorSurveyDataEntity> page = vendorSurveyService.getVendorSurveyDataList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/toemilQWE",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> toemilQWE(@RequestBody List<VendorSurveyDataEntity> vendorSurveyDataEntity){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorSurveyService.toemilQWE(vendorSurveyDataEntity);
		map.put("success", true);
		return map;
	}
}
