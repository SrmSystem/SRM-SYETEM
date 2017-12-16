package com.qeweb.scm.basemodule.web.manager;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.entity.BaseLogEntity;
import com.qeweb.scm.baseline.common.service.BaseLogService;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.RoleViewEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.RoleDataService;
import com.qeweb.scm.basemodule.service.RoleService;

@Controller
@RequestMapping("/manager/admin/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleDataService roleDataService;
	
	private Map<String,Object> map;
	
	@Autowired
	private BaseLogService baseLogService;
	
	@LogClass(method="查看", module="角色管理")
	@RequiresPermissions("sys:role:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/role/roleList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> roleList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<RoleEntity> page = roleService.getRoleList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getRoleList/{userId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getUserList(@PathVariable("userId") Long userId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_role.abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_userId", userId);
		

		Page<RoleUserEntity> userPage = roleService.getRoleUser(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@LogClass(method="新增", module="角色新增")
	@RequiresPermissions("sys:role:add")
	@RequestMapping(value = "addNewRole",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewRole(@RequestBody Map<String,String> map){
		return roleService.addNewRole(map);
	}
	
	@LogClass(method="修改", module="角色修改")
	/*@RequiresPermissions("sys:role:update")*/
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@RequestBody Map<String,String> map,ServletRequest request) {
		return roleService.updateRole(map,request);
	}
	
	@LogClass(method="删除", module="角色删除")
	@RequiresPermissions("sys:role:del")
	@RequestMapping(value = "deleteRole",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteRoleList(@RequestBody List<RoleEntity> roleList,ServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		roleService.deleteRoleList(roleList,request);
		map.put("success", true);
		return map;
	}
	/**
	 * 验证是否能删除角色
	 * @param roleList
	 * @return
	 */
	@RequestMapping(value = "validateDeleteRole",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> validateDeleteRole(@RequestBody List<RoleEntity> roleList){
		Map<String,Object> map = new HashMap<String, Object>();
		Boolean bool= roleService.validateDeleteRole(roleList);
		if(bool){
			map.put("success", true);
		}else{
			map.put("success", false);
			map.put("msg","角色下存在用户，不能删除");
		}
		return map;
	}
	
	@RequestMapping("getRole/{id}")
	@ResponseBody
	public RoleEntity getRole(@PathVariable("id") Long id){
		return roleService.getRole(id);
	}
	
	@RequestMapping(value="getRoleList")
	@ResponseBody
	public List<RoleEntity> getRoleList(Model model,ServletRequest request){
		return roleService.getRoleList();
	}
	
	@RequestMapping(value="addMenuRight",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addMenuRight(@RequestBody List<RoleViewEntity> roleMenuList){
		map = roleService.addMenuRight(roleMenuList);
		return map;
	}
	
	@RequestMapping(value="addRoleUser",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addRoleUser(Long roleId,@RequestParam(value="userIdList[]",required=false) List<Long> idList,ServletRequest request){
		map = roleService.addRoleUser(roleId,idList,request);
		return map;
	}
	
	@RequestMapping(value="addRoleData",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addRoleData(Long roleId, Long roleDataCfgId, @RequestParam(value="dataIds",required=false) String dataIds,ServletRequest request){
		map = roleDataService.addRoleData(roleId, roleDataCfgId, dataIds , request);
		return map;
	}
	
	@RequestMapping(value="getRoleUser")
	@ResponseBody
	public List<UserEntity> getRoleUser(Long roleId){
		List<UserEntity> userList = roleService.getRoleUserList(roleId);
		return userList;
	}
	

	
	@RequestMapping("checkCode")
	@ResponseBody
	public boolean checkCode(String code,Long id){
		return roleService.checkCode(code,id);
	}
	
	@RequestMapping("checkName")
	@ResponseBody
	public boolean checkName(String name,Long id){
		return roleService.checkName(name,id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindRole(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("role", roleService.getRole(id));
		}
	}
	
	@RequestMapping(value = "showRoleLog/{userId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showUserLog(@PathVariable("userId") Long roleId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_billId", roleId);
		searchParamMap.put("EQ_billType", LogType.ROLE_DATA);
		
		Page<BaseLogEntity> userPage = baseLogService.getLogList(pageNumber, pageSize, searchParamMap);
		for (BaseLogEntity baseLogEntity : userPage.getContent()) {
			try {
				Clob logContent = baseLogEntity.getLogContent();
				if(logContent != null && logContent.length()>0){
					String content = logContent.getSubString(1, (int)logContent.length());
					baseLogEntity.setContentStr(content);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	

}
