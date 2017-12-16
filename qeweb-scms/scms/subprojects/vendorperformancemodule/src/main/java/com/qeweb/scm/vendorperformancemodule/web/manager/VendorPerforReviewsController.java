package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforReviewsEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforReviewsService;

@Controller
@RequestMapping("/manager/vendor/vendorPerforReviews")
public class VendorPerforReviewsController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforReviewsService vendorPerforReviewsService;
	
	@RequiresPermissions("perfor:reviews:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/reviewsList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> reviewsList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforReviewsEntity> page = vendorPerforReviewsService.getVendorPerforReviewsList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/getVendorPerforCycle")
	@ResponseBody
	public String getVendorPerforCycle()
	{
		return vendorPerforReviewsService.getVendorPerforCycle();
	}

	@RequiresPermissions("perfor:reviews:join")
	@RequestMapping(value = "releaseLevelj",method = RequestMethod.POST)
	@ResponseBody
	public String releaseLevelj(@RequestBody List<VendorPerforReviewsEntity> vendorPerforReviewsEntityd,String cycid){
		return vendorPerforReviewsService.releaseLevelj(vendorPerforReviewsEntityd);
	}
	
	@RequiresPermissions("	perfor:reviews:choice")
	@RequestMapping(value = "releaseReviews/{ids}",method = RequestMethod.POST)
	@ResponseBody
	public String releaseLevel(@PathVariable("ids") String ids,@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoEntitys,String cycid){
		return vendorPerforReviewsService.releaseReviews(ids,vendorBaseInfoEntitys);
	}
	@RequiresPermissions("	perfor:reviews:choice")
	@RequestMapping(value = "releaseReviews2/{ids}",method = RequestMethod.POST)
	@ResponseBody
	public String releaseReviews2(@PathVariable("ids") String ids){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		return vendorPerforReviewsService.releaseReviews2(ids,searchParamMap);
	}
	@RequiresPermissions("	perfor:reviews:choice")
	@RequestMapping(value = "releaseReviews3/{ids}",method = RequestMethod.POST)
	@ResponseBody
	public String releaseReviews3(@PathVariable("ids") String ids){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		return vendorPerforReviewsService.releaseReviews2(ids,searchParamMap);
	}
	
	@RequiresPermissions("perfor:reviews:unjoin")
	@RequestMapping(value = "delsReviews",method = RequestMethod.POST)
	@ResponseBody
	public String delsReviews(@RequestBody List<VendorPerforReviewsEntity> VendorPerforReviewsEntity){
		return vendorPerforReviewsService.delsReviews(VendorPerforReviewsEntity);
	}

	@RequiresPermissions("perfor:reviews:del")
	@RequestMapping(value = "removeReviews",method = RequestMethod.POST)
	@ResponseBody
	public String removeReviews(@RequestBody List<VendorPerforReviewsEntity> VendorPerforReviewsEntity){
		return vendorPerforReviewsService.removeReviews(VendorPerforReviewsEntity);  
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
