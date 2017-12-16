package com.qeweb.scm.basemodule.web.manager;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import org.springside.modules.web.Servlets;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.baseline.common.constants.LogType;
import com.qeweb.scm.baseline.common.entity.BaseLogEntity;
import com.qeweb.scm.baseline.common.service.BaseLogService;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.entity.UserWarnRelEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.RoleDataService;
import com.qeweb.scm.basemodule.service.RoleService;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserWarnRelService;

/**
 * 管理员管理用户
 * @author pjjxiajun
 * @date 2015年3月16日
 * @path com.qeweb.scm.baseweb.web.manager.user.UserAdminController.java
 */
@Controller
@RequestMapping(value = "/manager/admin/user")
public class UserAdminController {
	
	public static String DEFAULT_PWD = PropertiesUtil.getProperty("defult.login.pwd","111111");
	private Map<String,Object> map;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoleDataService roleDataService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private BaseLogService baseLogService;
	
	@Autowired
	private UserWarnRelService userWarnRelService;
	
	@Autowired
	private  UserConfigRelService userConfigRelService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/user/adminUserList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		if(searchParamMap.get("EQ_company.roleType") != null){
			if(searchParamMap.get("EQ_company.roleType").equals(OrgType.ROLE_TYPE_VENDOR + "")){
				searchParamMap.put("EQ_company.abolished", StatusConstant.STATUS_NO);
			}
		}
		Page<UserEntity> userPage = accountService.getUsers(pageNumber,pageSize,searchParamMap);
		for (int i= 0 ; i < userPage.getContent().size(); i++) {
					OrganizationEntity buyerVen =accountService.findOrg(userPage.getContent().get(i).getCompanyId());
					if(buyerVen.getRoleType()==1){
						userPage.getContent().get(i).setCompanyCode(buyerVen.getCode());
						userPage.getContent().get(i).setCompanyName(buyerVen.getName());
					}else{
						userPage.getContent().get(i).setCompanyName(userPage.getContent().get(i).getAacCompany());
					}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getUserList/{roleId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getUserList(@PathVariable("roleId") Long roleId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		RoleEntity role=roleService.getRole(roleId);
		
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		if("Y".equals(role.getIsVendor())){
			searchParamMap.put("EQ_company.roleType", OrgType.ROLE_TYPE_VENDOR);
		}else{
			searchParamMap.put("EQ_company.roleType", OrgType.ROLE_TYPE_BUYER);
		}

		Page<UserEntity> userPage = accountService.getUsers(pageNumber,pageSize,searchParamMap);
		if("Y".equals(role.getIsVendor())){
			List<UserEntity> list = userPage.getContent();
			if(list.size()>0){
				for(UserEntity user:list){
					user.setAacCompany(user.getCompany().getName());
				}
			}
			
		}
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "getHead/{roleId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getHead(@PathVariable("roleId") Long roleId,Model model,ServletRequest request){
		RoleEntity role=roleService.getRole(roleId);
		map = new HashMap<String, Object>();
		if("Y".equals(role.getIsVendor())){
			map.put("roleType", OrgType.ROLE_TYPE_VENDOR);
		}else{
			map.put("roleType", OrgType.ROLE_TYPE_BUYER);
		}
		return map;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", accountService.getUser(id));
		return "back/user/adminUserForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(UserEntity user,ServletRequest request) {
		map = new HashMap<String, Object>();
		accountService.updateUser(user);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		UserEntity user = accountService.getUser(id);
		accountService.deleteUser(id);
		redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
		return "redirect:/admin/user";
	}
	
	@RequestMapping(value = "addNewUser",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewUser(UserEntity user,ServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		
		OrganizationEntity org=accountService.findOrg(user.getCompanyId());
		if(org!=null){
			if(org.getRoleType()==0){
				user.setPassword("111111");
			}else{
				user.setPassword(DEFAULT_PWD);
			}
		}else{
			user.setPassword(DEFAULT_PWD);
		}
		UserEntity userEntity = accountService.findUserEntityByLoginName(user.getLoginName());
		if(userEntity != null) {
			map.put("success", false);
			map.put("msg", "登录账户已存在");
			return map;
		}
		accountService.registerUser(user);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "deleteUser",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteUsers(@RequestBody List<UserEntity> userList){
		Map<String,Object> map = new HashMap<String, Object>();
		accountService.deleteUsers(userList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "resetPass",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> resetPass(@RequestBody List<UserEntity> userList){
		Map<String,Object> map = new HashMap<String, Object>();
		for (int i = 0; i < userList.size(); i++) {
			byte[] salt = Digests.generateSalt(8);
			userList.get(i).setSalt(Encodes.encodeHex(salt));

			byte[] hashPassword = Digests.sha1(DEFAULT_PWD.getBytes(), salt, AccountService.HASH_INTERATIONS);
			userList.get(i).setPassword(Encodes.encodeHex(hashPassword));
			userList.get(i).setIsNeedUpdatePass(null);
		}
		accountService.updateUsers(userList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value="addUserData",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addUserData(Long userId, Long roleDataCfgId, @RequestParam(value="dataIds",required=false) String dataIds,ServletRequest request){
		map = roleDataService.addUserData(userId, roleDataCfgId, dataIds,request);
		return map;
	}
	
	@RequestMapping("getUser/{id}")
	@ResponseBody
	public UserEntity getUser(@PathVariable("id") Long id){
		return accountService.getUser(id);
	}
	
	@RequestMapping("disableUser/{id}")
	@ResponseBody
	public Map<String,Object> disableUser(@PathVariable("id") Long id,ServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		UserEntity user=accountService.getUser(id);
		user.setEnabledStatus(StatusConstant.STATUS_NO);
		accountService.disableUserEntity(user,request);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("enableUser/{id}")
	@ResponseBody
	public Map<String,Object> enableUser(@PathVariable("id") Long id,ServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		UserEntity user=accountService.getUser(id);
		user.setEnabledStatus(StatusConstant.STATUS_YES);
		accountService.enableUserEntity(user,request);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "showUserLog/{userId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showUserLog(@PathVariable("userId") Long roleId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_billId", roleId);
		searchParamMap.put("EQ_billType", LogType.USER);
		
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
	/*###########################################采购组配置相关*##########################*/
	@RequestMapping(value="showGroupConfigList/{userId}", method = RequestMethod.GET)
	public String getRoleDataList(@PathVariable("userId") Long userId,Model model) {
		model.addAttribute("userId", userId);
		GroupConFigRelEntity groupConFigRel = userConfigRelService.getRelByUserId(userId);
		if(groupConFigRel != null ){
			if(!com.qeweb.scm.basemodule.utils.StringUtils.isEmpty(groupConFigRel.getGroupIds())){
				model.addAttribute("dataIds", groupConFigRel.getGroupIds());
			}else{
				model.addAttribute("dataIds", "-1");
			}
			
		}else{
			model.addAttribute("dataIds", "-1");
		}
		return "back/user/groupConfigRelList";
	}
	
	
	@RequestMapping(value="addGroupConfig",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addGroupConfig(Long userId, @RequestParam(value="dataIds",required=false) String dataIds){
		userConfigRelService.addGroupConfig(userId, dataIds);
		map.put("success", true);
		return map;
	}
	
	
	
	
	
	/*###########################################预警相关*##########################*/
	
	@RequestMapping(value = "getUserWarnRelList/{userId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getUserWarnRelList(@PathVariable("userId") Long userId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_user.id",userId);
		Page<UserWarnRelEntity> userPage = userWarnRelService.getUserWarnRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 增加信息
	 * @param 
	 * @param 
	 * @return
	 */
	@RequestMapping(value="showUserWarnRelList/{userId}",method = RequestMethod.GET)
	public String showUserWarnRelList(@PathVariable("userId") Long userId,Model model){
		model.addAttribute("userId", userId);
		return "back/user/userWarnRelList";
	}
	
	
	@RequestMapping(value = "addUserWarnRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addUserWarnRel(@Valid UserWarnRelEntity rel){
		Map<String,Object> map = new HashMap<String, Object>();
		userWarnRelService.add(rel);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "deleteUserWarnRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> DeleteUserWarnRel(@RequestBody List<UserWarnRelEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		userWarnRelService.deleteUserWarnRelList(list);
		return map;
	}
	
	//通过id获取角色下的人员
	@RequestMapping(value="getWarnUserListByRoleId/{id}")
	@ResponseBody
	public List<UserEntity> getWarnUserListByRoleId(@PathVariable(value="id") Long id){
		return roleService.getRoleUserList(id);
	}
	
	
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
/*		if (id != -1) {
			model.addAttribute("user", accountService.getUser(id));
		}*/
	}
	
	
	

	/**
	 * 新增公告时候获取人员
	 * @author chao.gu
	 * 20171117
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getNoteUserList")
	@ResponseBody
	public Map<String,Object> getNoteUserList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_enabledStatus", "1");
		searchParamMap.put("EQ_company.roleType", "0");
		Page<UserEntity> userPage = accountService.getUsers(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
}
