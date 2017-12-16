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
import com.qeweb.scm.epmodule.entity.EpWholeQuoHisEntity;
import com.qeweb.scm.epmodule.service.EpWholeQuoHisService;

/**
 *  整项报价历史controller
 * @author u
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epWholeQuoHis")
public class EpWholeQuoHisController {
	
	private Map<String,Object> map;
	
	@Autowired
	private EpWholeQuoHisService epWholeQuoHisService;
	
	/**
	 * 获得整项报价历史列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@LogClass(method="整项报价历史列表",module="整项报价历史管理")
	@RequestMapping(value="getEpWholeQuoHis/{epPriceId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="epPriceId") String epPriceId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_epPrice.id", epPriceId);
		searchParamMap.put("EQ_epVendor.vendorId", user.orgId+"");
		Page<EpWholeQuoHisEntity> epWholeQuoHisPage = epWholeQuoHisService.getEpWholeQuoHisLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoHisPage.getContent());
		map.put("total", epWholeQuoHisPage.getTotalElements());
		return map;
	}
	
	/**
	 * 打开供应商整项报价历史页面
	 * @return
	 */
	@LogClass(method="供应商整项报价历史",module="供应商整项报价历史管理")
	@RequestMapping(value = "openEpWholeQuoHisWin")
	public String openEpWholeQuoHisWin(Long epWholeQuoHisId,Model model,ServletRequest request){
		EpWholeQuoHisEntity epWholeQuoHis = new EpWholeQuoHisEntity();
		epWholeQuoHis = epWholeQuoHisService.findById(epWholeQuoHisId);
		if(epWholeQuoHis !=null){
			model.addAttribute("epWholeQuoHis", epWholeQuoHis);
		}
		return "back/ep/epWholeQuoHisItem";       
	}
}
