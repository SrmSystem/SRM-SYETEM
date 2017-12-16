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

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.RoleDataService;

/**
 * 管理员管理用户
 * @author pjjxiajun
 * @date 2015年3月16日
 * @path com.qeweb.scm.baseweb.web.manager.user.UserAdminController.java
 */
@Controller
@RequestMapping(value = "/manager/admin/user")
public class UserAdminController {
	
	public static String DEFAULT_PWD = "111111";
	private Map<String,Object> map;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoleDataService roleDataService;

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
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", accountService.getUser(id));
		return "back/user/adminUserForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("user") UserEntity user, RedirectAttributes redirectAttributes) {
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
	public Map<String,Object> addNewUser(@Valid UserEntity user){
		Map<String,Object> map = new HashMap<String, Object>();
		user.setPassword(DEFAULT_PWD);
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
		}
		accountService.updateUsers(userList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value="addUserData",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addUserData(Long userId, Long roleDataCfgId, @RequestParam(value="dataIds",required=false) String dataIds){
		map = roleDataService.addUserData(userId, roleDataCfgId, dataIds);
		return map;
	}
	
	@RequestMapping("getUser/{id}")
	@ResponseBody
	public UserEntity getUser(@PathVariable("id") Long id){
		return accountService.getUser(id);
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("user", accountService.getUser(id));
		}
	}
}
