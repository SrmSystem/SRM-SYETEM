package com.qeweb.scm.contractmodule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleItemEntity;
import com.qeweb.scm.contractmodule.service.ContractModuleItemService;
import com.qeweb.scm.contractmodule.service.ContractModuleService;

/**
 * 合同模板Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contractmodule/contractmodule")
public class ContractModuleController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	/**
	 * 合同模板Service
	 */
	@Autowired
	private ContractModuleService moduleService;
	
	/**
	 * 合同条款Service
	 */
	@Autowired
	private ContractModuleItemService moduleItemService;
	
	/**
	 * 生成流水号Service
	 */
	@Autowired
	private SerialNumberService serialNumberService;

	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	/**
	 * 跳转到合同模板页面
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/contract/contractModule";
	}

	/**
	 * 条件查询获取所有的合同模板
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		//用户权限
		searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
		searchParamMap.put("EQ_abolished", 0);
		Page<ContractModuleEntity> userPage = moduleService.getList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 合同模板页面编辑--跳转到合同条款页面
	 */
	@RequestMapping(value="toEdit")
	public String toEdit(Long moduleId,Model model,ServletRequest request){
		ContractModuleEntity module = moduleService.findOne(moduleId);  //根据模板id获取合同模板
		model.addAttribute("module", module);
		return "back/contract/contractModuleItem";
	}
	
	
	/**
	 * 根据合同模板Id获取合同条款
	 * @return
	 */
	@RequestMapping(value = "getModuleItemTreeGrid/{moduleId}")
	@ResponseBody
	public Map<String,Object> getModuleItemTreeGrid(@PathVariable(value="moduleId") Long moduleId){
		map = new HashMap<String, Object>();
		List<ContractModuleItemEntity> moduleItemList = moduleItemService.getModuleItemList(moduleId);
		map.put("rows", moduleItemList);
		map.put("total", moduleItemList.size());
		return map;
	}
	
	/**
	 * 保存新增的模板
	 * @param module
	 * @return
	 */
	@LogClass(method="保存", module="模板保存")
	@RequestMapping(value = "saveModule")
	@ResponseBody
	public Map<String,Object> saveModule(@Valid ContractModuleEntity module){
		List<ContractModuleEntity> moduleList = new ArrayList<ContractModuleEntity>();
		moduleList.add(module);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();	
		module.setBuyerId(user.orgId);
		map = moduleService.saveModule(moduleList);
		return map;
	}
	
	
	
	/**
	 * 保存更新的模板
	 * @param module
	 * @return
	 */
	@LogClass(method="更新", module="更新模板")
	@RequestMapping(value = "updateModuleEdit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateModuleEdit(@RequestBody List<ContractModuleEntity> moduleList){
		if(moduleList !=null && moduleList.size() != 0){
			map = moduleService.saveModule(moduleList);
		}else{
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除模板
	 * @param moduleList
	 * @return
	 */
	@LogClass(method="删除", module="删除模板")
	@RequestMapping(value = "deleteModule",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteModule(@RequestBody List<ContractModuleEntity> moduleList){
		List<ContractModuleItemEntity> moduleItemList = new ArrayList<ContractModuleItemEntity>();
		if(moduleList !=null && moduleList.size() != 0){
			for (ContractModuleEntity module : moduleList) {  //根据模板ID获得条款,删除模板的同时要删除模板下面的条款
				List<ContractModuleItemEntity> _moduleItemList = moduleItemService.getModuleItemList(module.getId());
				if(_moduleItemList !=null && _moduleItemList.size() !=0){
					moduleItemList.addAll(_moduleItemList);
				}
			}
			map = moduleService.deleteModule(moduleList);	//删除模板
			map = moduleItemService.deleteModuleItem(moduleItemList);	//删除模板下的条款
		}else{
			map.put("msg", "删除失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 生成模板编号的流水号
	 * @return
	 */
	@LogClass(method="流水号",module="生成流水号")
	@RequestMapping(value = "createModuleCode")
	@ResponseBody
	public ContractModuleEntity createModuleCode(){
		ContractModuleEntity module = new ContractModuleEntity();
		String moduleCode = moduleService.createModuleCode();
		module.setCode(moduleCode);
		return module;
	} 

	@Override
	public void log(Object message) {
		
	}
}



