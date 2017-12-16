package com.qeweb.scm.epmodule.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
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

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpQuoSubItemEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.repository.EpQuoSubItemDao;
import com.qeweb.scm.epmodule.service.EpPriceService;
import com.qeweb.scm.epmodule.service.EpQuoSubItemService;
import com.qeweb.scm.epmodule.service.EpSubQuoService;
import com.qeweb.scm.epmodule.service.EpWholeQuoService;

/**
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epComparePrice")
public class EpComparePriceController {
	

	
	private Map<String,Object> map;
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	@Autowired
	private EpSubQuoService epSubQuoService;
	
	@Autowired
	private EpPriceService epPriceService;
	
	@Autowired
	private EpQuoSubItemService epQuoSubItemService;
	
	@Autowired
	private EpQuoSubItemDao epQuoSubItemDao;


	@LogClass(method="比价",module="比价管理")
	@RequestMapping(value = "openComparePriceList")
	public String openComparePriceList(Long epMaterialId,Long epPriceId,Model model,ServletRequest request){
		EpPriceEntity epPrice = epPriceService.findById(epPriceId);
		model.addAttribute("epPrice", epPrice);
		model.addAttribute("epPriceId", epPriceId);
		model.addAttribute("epMaterialId", epMaterialId);
		model.addAttribute("isVendor", 0);
		return "back/ep/epComparePriceList";       
	}
	
	@LogClass(method="比价",module="比价管理")
	@RequestMapping(value="buyerGetList/{epPriceId}/{epMaterialId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="epPriceId") String epPriceId,@PathVariable(value="epMaterialId") String epMaterialId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_epPrice.id", epPriceId);
		searchParamMap.put("EQ_epMaterial.id", epMaterialId);
		searchParamMap.put("EQ_isVendor", 0);
		Page<EpWholeQuoEntity> epWholeQuoPage = epWholeQuoService.getEpWholeQuoLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	
	@LogClass(method="分项信息",module="分项信息")
	@RequestMapping(value="getModuleItemList/{epMaterialId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getModuleItemList(@PathVariable(value="epMaterialId") String epMaterialId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_epMaterialId", epMaterialId);
		Page<EpQuoSubItemEntity> epWholeQuoPage = epQuoSubItemService.getList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	
	@LogClass(method="合作",module="合作")
	@RequestMapping(value = "cooperation/{wholeId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cooperation(@PathVariable("wholeId") Long wholeId){
		Map<String,Object> map = new HashMap<String, Object>();
		EpWholeQuoEntity epWholeQuo=epWholeQuoService.findById(wholeId);
		epWholeQuo.setCooperationStatus(EpConstans.STATUS_YES);
		epWholeQuoService.save(epWholeQuo);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="分项比价",module="分项比价管理")
	@RequestMapping(value = "getSubList/{moduleItemId}")
	@ResponseBody
	public Map<String,Object> getSubList(@PathVariable("moduleItemId") Long moduleItemId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		EpQuoSubItemEntity item=epQuoSubItemDao.findOne(moduleItemId);
		
		searchParamMap.put("EQ_wholeQuo.epMaterial.id", item.getEpMaterialId());
		searchParamMap.put("EQ_moduleItemId", item.getId());
		
		Page<EpSubQuoEntity> epWholeQuoPage = epSubQuoService.getList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map; 
	}
	
	
	
}
