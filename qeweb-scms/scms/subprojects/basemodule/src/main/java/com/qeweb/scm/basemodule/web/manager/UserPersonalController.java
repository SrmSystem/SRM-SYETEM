package com.qeweb.scm.basemodule.web.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.UserServiceImpl;

@Controller
@RequestMapping("/manager/vendor/personal")
public class UserPersonalController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET)
	public String   personalSetting(HttpServletRequest request,HttpServletResponse response)
	{
		userServiceImpl.personalSetting(request,response);
		return "back/user/personalSetting";
	}
	
	@RequestMapping(value="/updatePersonal",method=RequestMethod.POST)
	@ResponseBody
	public String updatePersonalSetting(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response)
	{
		return userServiceImpl.updatePersonalSetting(userEntity,request,response);
	}
	@RequestMapping(value="/updatePassword",method=RequestMethod.POST)
	@ResponseBody
	public String updatePassword(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			return userServiceImpl.updatePassword(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
