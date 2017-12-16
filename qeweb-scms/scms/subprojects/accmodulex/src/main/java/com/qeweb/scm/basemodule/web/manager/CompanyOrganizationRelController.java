package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.CompanyOrganizationRelService;
import com.qeweb.scm.sap.service.CompanySyncService;

@Controller
@RequestMapping("/manager/basedata/companyOrganizationRel")
public class CompanyOrganizationRelController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private CompanyOrganizationRelService companyOrganizationRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/companyOrganizationRelList";
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		CompanySyncService companySyncService = SpringContextUtils.getBean("companySyncService");	
		boolean isSuccess = companySyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> companyList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<CompanyOrganizationRelEntity> page = companyOrganizationRelService.getCompanyOrganiztionRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewCompanyOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewCompanyOrganizationRel(@Valid CompanyOrganizationRelEntity companyOrganizationRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		//查询是否已经建立关系
		CompanyOrganizationRelEntity cp =  companyOrganizationRelService.findByCompanyIdAndOrganizationId(companyOrganizationRel.getCompanyId(),companyOrganizationRel.getOrganizationId());
		if( cp != null) {
			map.put("msg", "已存在生效关系！");
			map.put("success", false);
			return map;
		}
		companyOrganizationRelService.addNewCompanyOrganizationRel(companyOrganizationRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteCompanyOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCompanyOrganizationRel(@RequestBody List<CompanyOrganizationRelEntity> CompanyOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		companyOrganizationRelService.deleteCompanyOrganizationRel(CompanyOrganizationRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getCompanyFactoryRel/{id}")
	@ResponseBody
	public CompanyOrganizationRelEntity getCompanyOrganizationRel(@PathVariable("id") Long id){
		return companyOrganizationRelService.getCompanyOrganizationRel(id);
	}

	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<CompanyOrganizationRelEntity> companyOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = companyOrganizationRelService.abolishBatch(companyOrganizationRelList);
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
