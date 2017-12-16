package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.context.MessageSourceUtil;
import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.FeedbackService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping("/manager/basedata/feedback")
public class FeedbackController{
	
	@Autowired
	private FeedbackService feedbackService; 
	@Autowired
	private OrgService orgService; 
	
	private Map<String,Object> map;
	
	/**
	 * 获取反馈信息列表
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> factoryList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<FeedbackEntity> page = feedbackService.getFeedbacks(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 添加反馈信息
	 * @param feedback
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addFeedback",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addFeedBack(FeedbackEntity feedback, HttpServletRequest request, HttpServletResponse response) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		map = new HashMap<String, Object>();
		OrganizationEntity org = orgService.getOrg(user.orgId);
		if(OrgType.ROLE_TYPE_BUYER.equals(org.getRoleType())) {
			feedback.setRecvOrgId(feedback.getVendorId());
		} else {
			feedback.setRecvOrgId(feedback.getBuyerId());
		}
		feedback.setFeedbackOrgId(user.orgId);
		feedback.setFeedbackOrgName(user.orgName);
		feedbackService.addNewFeedback(feedback);
		map.put("feedback", feedback);
		map.put("msg", MessageSourceUtil.getMessage("message.feedback.success", request));  
		map.put("success", true);
		return map;

	}
	
}
