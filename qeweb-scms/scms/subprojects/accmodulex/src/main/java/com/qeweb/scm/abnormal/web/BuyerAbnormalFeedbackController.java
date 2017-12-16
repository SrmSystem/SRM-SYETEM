package com.qeweb.scm.abnormal.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.abnormal.entity.AbnormalFeedbackEntity;
import com.qeweb.scm.abnormal.service.BuyerAbnormalFeedbackService;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Controller
@RequestMapping("/manager/abnormal/abnormalFeedback")
public class BuyerAbnormalFeedbackController {

	@Autowired
	private BuyerAbnormalFeedbackService buyerAbnormalFeedbackService;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrganizationDao organizationDao; 
	
	private Map<String, Object> map;

	//采方
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "/back/abnormal/abnormalFeedbackList";
	}
	
	//供方
	@RequestMapping(value="vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		return "/back/abnormal/abnormalbackVendorList";
	}

	/**
	 * 获取异常反馈管理页面(采方和供方)
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> abnormalFeedList(@RequestParam(value = "page") int pageNumber,
			@RequestParam(value = "rows") int pageSize, Model model, ServletRequest request) {
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("EQ_isVendor", curruser.orgRoleType);
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_createUserId",curruser.id );
		String sort = request.getParameter("sort");
		String orderby = request.getParameter("order");
		sort = "createTime";
		orderby="asc";
		Page<AbnormalFeedbackEntity> page = buyerAbnormalFeedbackService.getAbnormalFeedList(pageNumber, pageSize,
				searchParamMap,EasyUISortUtil.getSort(sort, orderby));
		map = new HashMap<String, Object>();
		map.put("rows", page.getContent());
		map.put("total", page.getTotalElements());
		return map;
	}

	/**
	 * 新增采方异常反馈管理页面
	 * 
	 * @param abnormalFeedback
	 * @return
	 */

	@RequestMapping("AddAbnormal")
	@ResponseBody
	public Map<String, Object> addgetAddAbnormalFeedList(AbnormalFeedbackEntity abnormalFeedback) {
		buyerAbnormalFeedbackService.getAddAbnormalFeed(abnormalFeedback);
		map.put("success", true);
		return map;
	}

	/**
	 * 对数据进行删除操作 2017年7月12日15:34:20
	 */
	@RequestMapping(value = "deleteAddAbnormalFeed",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteAddAbnormalFeed(@RequestBody List<AbnormalFeedbackEntity> abnormalFeedbackList) {
		buyerAbnormalFeedbackService.getDeleteAbnormalFeed(abnormalFeedbackList);
		map.put("massage", "删除成功");
		map.put("success", true);
		return map;
	}

	/**
	 * 采方异常反馈更新操作
	 */
	@RequestMapping(value = "editAbnormal")
	@ResponseBody
	public Map<String, Object> editAddAbnormalFeed(AbnormalFeedbackEntity editabfe) {
		buyerAbnormalFeedbackService.getEditAbnormalFeed(editabfe);
		map.put("success", true);
		return map;
	}

	@RequestMapping("getAbnormal/{id}")
	@ResponseBody
	public AbnormalFeedbackEntity getEditPaymentId(@PathVariable("id") Long id) {
		AbnormalFeedbackEntity e =    buyerAbnormalFeedbackService.getAbnormalFeedbackId(id);
		String vendorNames = "";
		if(e.getVendorIds() != null ){
			String [] vendorIds=e.getVendorIds().split(",");
			if(null!=vendorIds && vendorIds.length>0){
				for (String str : vendorIds) {
					if(!StringUtils.isEmpty(str)){
						OrganizationEntity org = 	organizationDao.findOne(Long.valueOf(str));
						vendorNames= vendorNames + org.getName()+",";
					}
				}	
			}
			e.setVendorNames(vendorNames);
		}
		return e;
	}

/**
	 * 
	 * 发布异常信息
	 * 
	 */
	@RequestMapping(value = "publishAbnormal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publish(@RequestBody List<AbnormalFeedbackEntity> AbnormalFeedbacklist) {
		Map<String,Object> map = new HashMap<String, Object>();
			buyerAbnormalFeedbackService.publish(AbnormalFeedbacklist);
			map.put("success", true);
			return map;
		}
	
	
	
	
	
	
	
	/**
	 *  显示页面获取异常反馈详细列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap FileCollaboration
	 * @return
	 */
	@RequestMapping(value="getViewabnormalFeedbackList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getViewabnormalFeedbackList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_publishStatus","1");
	
		if(user.orgRoleType == 1){//供应商
			searchParamMap.put("LIKE_vendorIds",user.orgId);
			searchParamMap.put("EQ_isVendor",0);//获取采购商的数据
		}else{	//采购商
			searchParamMap.put("EQ_isVendor",1);//获取供应商创建的数据
		}
		String sort = request.getParameter("sort");
		String orderby = request.getParameter("order");
		sort = "topYn,effectiveStartDate";
		orderby="desc,desc";
		Page<AbnormalFeedbackEntity> page = buyerAbnormalFeedbackService.getAbnormalFeedList(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, orderby));
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	}