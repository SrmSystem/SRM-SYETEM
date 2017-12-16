package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
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

import com.qeweb.modules.utils.ClockEX;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.utils.TreeUtil;

/**
 * 组织管理
 * @author pjjxiajun
 * @date 2015年3月16日
 * @path com.qeweb.scm.baseweb.web.manager.OrgAdminController.java
 */
@Controller
@RequestMapping(value = "/manager/admin/org")
public class OrgAdminController {
	
	private Map<String,Object> map;

	@Autowired
	private OrgService orgService;

	@LogClass(method="查看", module="组织管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/org/adminOrgList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		searchParamMap.put("EQ_abolished", "0");
		Page<OrganizationEntity> userPage = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
		List<OrganizationEntity> orgs = userPage.getContent();
		orgs = orgService.reLoad(orgs);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getLists",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getLists(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		searchParamMap.put("EQ_abolished", "0");
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(!user.loginName.equalsIgnoreCase("admin"))
		{
			searchParamMap.put("EQ_id",user.orgId+"");
		}
		Page<OrganizationEntity> userPage = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	@RequestMapping(value="getVendorList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getVendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search_");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType","1" );
		Page<OrganizationEntity> userPage = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
		List<OrganizationEntity> orgs = userPage.getContent();
		orgs = orgService.reLoad(orgs);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	
	
	@RequestMapping(value="buyerJson",method=RequestMethod.POST)
	@ResponseBody
	public List<OrganizationEntity> getBuyerJson(){
		Map<String,Object> searchParamMap = new HashMap<String, Object>();
		searchParamMap.put("EQ_roleType", 0);
		List<OrganizationEntity> orgList = orgService.getOrgList(searchParamMap);
		/*String jsonString = "[";
		int i = 0 ;
		for(OrganizationEntity org:orgList){
			if(i++!=0){
				jsonString += ",";
			}
			jsonString += "{'id':'"+org.getId()+"',orgName:'"+org.getName()+"'}";
		}
		jsonString += "]";*/
		return orgList;
	}
	
	@RequestMapping(value="getOrgEasyuiTree",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTree> getOrgEasyuiTree(Long id,Integer orgType,ServletRequest request){
		List<OrganizationEntity> orgList = orgService.getOrgListByPIdAndOrgType(id,orgType);
		List<EasyuiTree> orgNodeList = TreeUtil.toEasyuiTree(orgList);
		return orgNodeList;
	}
	
	@RequestMapping(value="getOrgByRoleTypeEasyuiTree",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTree> getOrgByRoleTypeEasyuiTree(Long id,Integer roleType,ServletRequest request){
		List<OrganizationEntity> orgList = orgService.getOrgListByPIdAndRoleType(id,roleType);
		List<EasyuiTree> orgNodeList = TreeUtil.toEasyuiTree(orgList);
		return orgNodeList;
	}
	
	/**
	 * 根据组织的角色获取组织集合
	 * @param orgRoleType 组织角色类型
	 */
	@RequestMapping(value="getOrgListByRoleType",method=RequestMethod.POST)
	@ResponseBody
	public List<OrganizationEntity> getOrgList(Integer orgRoleType,ServletRequest request){
		List<OrganizationEntity> orgList = orgService.getOrgListByRoleType(orgRoleType);
		return orgList;
	}
	
	/**
	 * 根据组织的角色获取组织集合(副本)
	 * @param orgRoleType 组织角色类型
	 */
	@RequestMapping(value="getOrgListByType/{type}",method=RequestMethod.POST)
	@ResponseBody
	public List<OrganizationEntity> getOrgListByType(@PathVariable("type") Integer type){
		List<OrganizationEntity> orgList = orgService.getOrgListByRoleType(type);
		return orgList;
	}
	

	@LogClass(method="修改", module="组织修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("org") OrganizationEntity org) {
		map = new HashMap<String, Object>();
		orgService.updateOrg(org);
		map.put("success", true);
		map.put("node", TreeUtil.toEasyuiTreeNode(org));
		return map;
	}
	
	@LogClass(method="新增", module="组织新增")
	@RequestMapping(value = "addNewOrg",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNew(@Valid OrganizationEntity org){
		map = new HashMap<String, Object>();
		org.setRegisterTime(ClockEX.DEFAULT.getCurrentStamp());
		orgService.addOrg(org);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 开通供应商
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "openVendor",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> openVendor(@Valid OrganizationEntity org){
		map = new HashMap<String, Object>();
		//通过code查询供应商
		OrganizationEntity organization  =  orgService.findByCodeAndAbolished(org.getCode(),0);
		if(organization != null){
			map.put("success", false);
			map.put("msg", "供应商已存在！");
			return map;
		}
		
		org.setRegisterTime(ClockEX.DEFAULT.getCurrentStamp());
		String msg = orgService.openVendor(org);
		if(StringUtils.isEmpty(msg)){
			map.put("success", true);
		}else{
			map.put("success", false);
			map.put("msg", msg);
		}
		return map;
	}
	
	/**
	 * 获取所有部门
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getDepartment")
	@ResponseBody
	public List<EasyuiComboBox> getDepartment(ServletRequest request){
		List<OrganizationEntity> orgList = orgService.getOrgListByRoleType(OrgType.ORG_TYPE_DEPARMENT);
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < orgList.size();i++){
			OrganizationEntity bo = orgList.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "addNewDepartment",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewDepartment(@Valid OrganizationEntity org){
		map = new HashMap<String, Object>();
		org.setRegisterTime(ClockEX.DEFAULT.getCurrentStamp());
		orgService.addOrg(org);
		map.put("success", true);
		map.put("node", TreeUtil.toEasyuiTreeNode(org));
		return map;
	}
	
	/**
	 * 废除组织
	 * @param orgList
	 * @return
	 */
	@LogClass(method="作废", module="组织作废")
	@RequestMapping(value = "deleteOrg",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteOrgList(@RequestBody List<OrganizationEntity> orgList){
		//废除
		Map<String,Object> map = orgService.abolishBatch(orgList);
		return map;
	}
	
	@RequestMapping("getOrg/{id}")
	@ResponseBody
	public OrganizationEntity getUser(@PathVariable("id") Long id){
		return orgService.getOrg(id);
	}
	
	@RequestMapping(value = "goDepartment",method=RequestMethod.GET)
	public String goDepartment(Model model){
		return "back/org/departmentList";
	}

	/**
	 * 查找所有的组织
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAllOrg")
	@ResponseBody
	public Map<String,Object> getAllOrgList(Map<String, Object> map,Model model,ServletRequest request){
		List<OrganizationEntity> orgList=orgService.findAll();
		map = new HashMap<String, Object>();
		map.put("orgList",orgList);
		return map;
	}
	
	/**
	 * 查找未废除的组织
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "getEffectiveOrgSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getEffectiveOrgSelect(ServletRequest request){
		List<OrganizationEntity> list = orgService.findEffective();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			OrganizationEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindOrg(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("org", orgService.getOrg(id));
		}
	}
}
