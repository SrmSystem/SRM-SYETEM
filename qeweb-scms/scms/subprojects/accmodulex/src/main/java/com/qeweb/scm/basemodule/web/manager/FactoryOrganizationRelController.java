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
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.FactoryOrganizationRelService;
import com.qeweb.scm.sap.service.OrgFactoryRelSyncService;

@Controller
@RequestMapping("/manager/basedata/factoryOrganizationRel")
public class FactoryOrganizationRelController implements ILog{
	private ILogger logger = new FileLogger();
	@Autowired
	private FactoryOrganizationRelService factoryOrganizationRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/factoryOrganizationRelList";
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		OrgFactoryRelSyncService orgFactoryRelSyncService = SpringContextUtils.getBean("orgFactoryRelSyncService");	
		boolean isSuccess = orgFactoryRelSyncService.execute(logger);
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
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("EQ_factory.abolished", "0"); 
		Page<FactoryOrganizationRelEntity> page = factoryOrganizationRelService.getFactoryOrganizationRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value = "addNewFactoryOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewFactoryOrganizationRel(@Valid FactoryOrganizationRelEntity factoryOrganizationRel){
		
		Map<String,Object> map = new HashMap<String, Object>();
		//查询是否已经建立关系
		FactoryOrganizationRelEntity cp =  factoryOrganizationRelService.findByFactoryIdAndOrgId(factoryOrganizationRel.getFactoryId(),factoryOrganizationRel.getOrgId());
		if( cp != null) {
			map.put("msg", "已存在生效关系！");
			map.put("success", false);
			return map;
		}
		factoryOrganizationRelService.addNewFactoryOrganizationRel(factoryOrganizationRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteFactoryOrganizationRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteFactoryOrganizationRel(@RequestBody List<FactoryOrganizationRelEntity> factoryOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		factoryOrganizationRelService.deleteFactoryOrganizationRel(factoryOrganizationRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getFactoryOrganizationRel/{id}")
	@ResponseBody
	public FactoryOrganizationRelEntity getFactoryOrganizationRel(@PathVariable("id") Long id){
		return factoryOrganizationRelService.getFactoryOrganizationRel(id);
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<FactoryOrganizationRelEntity> factoryOrganizationRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = factoryOrganizationRelService.abolishBatch(factoryOrganizationRelList);
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
