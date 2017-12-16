package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.CompanyService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.sap.service.BOHelper;
import com.qeweb.scm.sap.service.CompanySyncService;
import com.qeweb.scm.sap.service.OrganizationSyncService;
import com.qeweb.scm.sap.service.PurchaseOrderSyncService;

@Controller
@RequestMapping("/manager/basedata/buyerOrg")
public class BuyerOrgController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private OrgService orgService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/buyerOrgList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("abolished", 0);
		searchParamMap.put("orgType", OrgType.ORG_TYPE_COMPANY);
		searchParamMap.put("roleType", OrgType.ROLE_TYPE_BUYER);
		searchParamMap.put("activeStatus", StatusConstant.STATUS_YES);
		searchParamMap.put("enableStatus", StatusConstant.STATUS_YES);
		searchParamMap.put("confirmStatus", StatusConstant.STATUS_YES);
    	 
		Page<OrganizationEntity> page = orgService.getOrgs(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		OrganizationSyncService organizationSyncService = SpringContextUtils.getBean("organizationSyncService");	
		boolean isSuccess = organizationSyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	

	@Override
	public void log(Object message) {
		getLogger().log(message);
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	

}
