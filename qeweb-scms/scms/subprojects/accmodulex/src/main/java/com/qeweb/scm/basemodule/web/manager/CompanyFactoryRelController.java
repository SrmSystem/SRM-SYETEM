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
import com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.CompanyFactoryRelService;
import com.qeweb.scm.sap.service.CompanyFactoryRelSyncService;
import com.qeweb.scm.sap.service.MaterialSyncService;

@Controller
@RequestMapping("/manager/basedata/companyFactoryRel")
public class CompanyFactoryRelController implements ILog{
	private ILogger logger = new FileLogger();
	
	@Autowired
	private CompanyFactoryRelService companyFactoryRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/companyFactoryRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> companyList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("EQ_factory.abolished", "0");  
		Page<CompanyFactoryRelEntity> page = companyFactoryRelService.getCompanyFactoryRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		CompanyFactoryRelSyncService companyFactoryRelSyncService = SpringContextUtils.getBean("companyFactoryRelSyncService");	
		boolean isSuccess = companyFactoryRelSyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		
		return map;
	}
	
	
	@RequestMapping(value = "addNewCompanyFactoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewCompanyFactoryRel(@Valid CompanyFactoryRelEntity companyFactoryRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		//查询是否已经建立关系
		CompanyFactoryRelEntity cp =  companyFactoryRelService.findByCompanyIdAndFactoryId(companyFactoryRel.getCompanyId(),companyFactoryRel.getFactoryId());
		if( cp != null) {
			map.put("msg", "关系已存在！");
			map.put("success", false);
			return map;
		}
		companyFactoryRelService.addNewCompanyFactoryRel(companyFactoryRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteCompanyFactoryRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCompanyFactoryRel(@RequestBody List<CompanyFactoryRelEntity> companyFactoryList){
		Map<String,Object> map = new HashMap<String, Object>();
		companyFactoryRelService.deleteCompanyFactoryRel(companyFactoryList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getCompanyFactoryRel/{id}")
	@ResponseBody
	public CompanyFactoryRelEntity getCompanyFactoryRel(@PathVariable("id") Long id){
		return companyFactoryRelService.getCompanyFactoryRelEntity(id);
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<CompanyFactoryRelEntity> companyFactoryList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = companyFactoryRelService.abolishBatch(companyFactoryList);
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
