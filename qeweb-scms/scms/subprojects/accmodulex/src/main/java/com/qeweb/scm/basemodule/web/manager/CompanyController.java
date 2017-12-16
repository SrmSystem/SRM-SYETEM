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

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.CompanyService;
import com.qeweb.scm.sap.service.CompanySyncService;
import com.qeweb.scm.sap.service.PurchaseOrderSyncService;

@Controller
@RequestMapping("/manager/basedata/company")
public class CompanyController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private CompanyService companyService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/companyList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> companyList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<CompanyEntity> page = companyService.getCompanyList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
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
	
	
	@RequestMapping(value = "addNewCompany",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewCompany(@Valid CompanyEntity company){
		
		Map<String,Object> map = new HashMap<String, Object>();
		CompanyEntity cp =  companyService.getCompanyByCode(company.getCode());
		if( cp != null) {
			map.put("msg", "编码已存在！");
			map.put("success", false);
			return map;
		}
		companyService.addNewCompany(company);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("company") CompanyEntity companyEntity) {
		map = new HashMap<String, Object>();
		companyService.updateCompany(companyEntity);
		map.put("success", true);
		return map;
		
	}
	
	
	@RequestMapping(value = "deleteCompany",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCompanyList(@RequestBody List<CompanyEntity> companyList){
		Map<String,Object> map = new HashMap<String, Object>();
		companyService.deleteCompanyList(companyList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getCompany/{id}")
	@ResponseBody
	public CompanyEntity getCompany(@PathVariable("id") Long id){
		return companyService.getCompanyEntity(id);
	}
	
	@RequestMapping(value = "getCompanySelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getCompanySelect(ServletRequest request){
		List<CompanyEntity> list = companyService.findAll();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			CompanyEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "getEffectiveCompanySelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getEffectiveCompanySelect(ServletRequest request){
		List<CompanyEntity> list = companyService.findEffective();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			CompanyEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	
	@RequestMapping(value = "abolishBatchCompany",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchBrand(@RequestBody List<CompanyEntity> companyList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = companyService.abolishBatch(companyList);
		return map;
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindCompanyEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("company", companyService.getCompanyEntity(id));
		}
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
