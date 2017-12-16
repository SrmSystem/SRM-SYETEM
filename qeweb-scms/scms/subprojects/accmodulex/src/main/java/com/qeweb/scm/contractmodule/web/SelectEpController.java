package com.qeweb.scm.contractmodule.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.service.EpWholeQuoService;

/**
 * 合同条款Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contract/selEp")
public class SelectEpController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private EpWholeQuoService epWholeQuoService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@LogClass(method="选择报价单列表",module="选择报价单管理")
	@RequestMapping(value="getList/{vendorId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendorId") String vendorId,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
//		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_epVendor.vendorId", vendorId);
		searchParamMap.put("EQ_cooperationStatus", EpConstans.STATUS_YES);
		searchParamMap.put("EQ_abolished", 0);
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		Page<EpWholeQuoEntity> epWholeQuoPage = epWholeQuoService.getEpWholeQuoLists(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", epWholeQuoPage.getContent());
		map.put("total", epWholeQuoPage.getTotalElements());
		return map;
	}
	
	

	@Override
	public void log(Object message) {
		
	}
	
}
	



