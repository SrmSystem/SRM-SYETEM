package com.qeweb.scm.basemodule.web.manager;

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

import com.qeweb.scm.basemodule.entity.RoleDataCfgEntity;
import com.qeweb.scm.basemodule.entity.RoleDataEntity;
import com.qeweb.scm.basemodule.entity.UserDataEntity;
import com.qeweb.scm.basemodule.service.RoleDataService;
import com.qeweb.scm.basemodule.vo.RoleDataVO;

@Controller
@RequestMapping("/manager/admin/roledata")
public class RoleDataController {
	
	@Autowired
	private RoleDataService roleDataService;
	
	private Map<String,Object> map;
	
	/**
	 * 获取数据权限配置
	 * @param roleId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{roleId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> roleDataList(@PathVariable("roleId") long roleId, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<RoleDataCfgEntity> page = roleDataService.getRoleDataCfgList(pageNumber,pageSize,searchParamMap);
		for(RoleDataCfgEntity rdc : page.getContent()){
			rdc.setRoleId(roleId);
		} 
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getRoleDataList/{roleId}/{roleDataCfgId}", method = RequestMethod.GET)
	public String getRoleDataList(@PathVariable("roleId") long roleId, @PathVariable("roleDataCfgId") long roleDataCfgId, Model model) {
		model.addAttribute("type", "Role");
		model.addAttribute("roleId", roleId);
		model.addAttribute("roleDataCfgId", roleDataCfgId);
		RoleDataEntity roleData = roleDataService.getRoleDataEntity(roleId, roleDataCfgId);
		model.addAttribute("dataIds", roleData.getDataIds());
		return "back/role/roleDataList";
	}
	
	@RequestMapping(value="getUserDataList/{userId}/{roleDataCfgId}", method = RequestMethod.GET)
	public String getUserDataList(@PathVariable("userId") long userId, @PathVariable("roleDataCfgId") long roleDataCfgId, Model model) {
		model.addAttribute("type", "User");
		model.addAttribute("roleId", userId);
		model.addAttribute("roleDataCfgId", roleDataCfgId);
		UserDataEntity userData = roleDataService.getUserDataEntity(userId, roleDataCfgId);
		model.addAttribute("dataIds", userData.getDataIds()); 
		return "back/role/roleDataList";
	}
	
	@RequestMapping(value="/getDataList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getDataList(Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<RoleDataVO> page = roleDataService.getRoleDataList(1, 1000, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 清除角色数据权限
	 * @param roleId
	 * @param roleDataCfgId
	 * @return
	 */
	@RequestMapping(value="/clearRoleData/{roleId}/{roleDataCfgId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> clearRoleData(@PathVariable("roleId") long roleId, @PathVariable("roleDataCfgId") long roleDataCfgId) {
		map = new HashMap<String, Object>();
		roleDataService.deleteRoleData(roleId, roleDataCfgId);
		map.put("success", true);
		map.put("msg", "清除数据权限成功");
		return map;
	}
	
	/**
	 * 清除用户数据权限
	 * @param roleId
	 * @param roleDataCfgId
	 * @return
	 */
	@RequestMapping(value="/clearUserData/{userId}/{roleDataCfgId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> clearUserData(@PathVariable("userId") long userId, @PathVariable("roleDataCfgId") long roleDataCfgId) {
		map = new HashMap<String, Object>();
		roleDataService.deleteUserData(userId, roleDataCfgId);
		map.put("success", true);
		map.put("msg", "清除数据权限成功");   
		return map;
	}
}
