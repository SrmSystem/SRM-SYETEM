package com.qeweb.scm.purchasemodule.web.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.web.vo.RejectVO;

/**
 * 采购发货单审核管理
 * @author chao.gu
 * @date 2017.05.31
 */
@Controller
@RequestMapping(value = "/manager/order/deliveryAudit")
public class DeliveryAuditController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private BacklogService backlogService;
	
	/**
	 * 发货单审核列表【采】
	 * @param theme
	 * @param model
	 * @return
	 */
	@LogClass(method="查看",module="发货单审核列表【采】")
	@RequestMapping(method=RequestMethod.GET)
	public String pendingList(Model model) {
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		//待办end
		return "back/delivery/deliveryAuditList";
	}
	
	/**
	 * 发货单审核列表【采】
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getAuditingList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG + "");						//未删除
		//采购组权限
		searchParamMap.put("IN_purchasingGroupId", buyerOrgPermissionUtil.getGroupIds());
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
					String backlogId = searchParamMap.get("IN_backlogId").toString();
					List<Long> idList= backlogService.getBackLogValueIdsById(backlogId);
					searchParamMap.put("IN_id", idList);
				}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		Page<DeliveryEntity> userPage = deliveryService.getToAuditDeliverys(pageNumber,pageSize,searchParamMap);
		userPage = deliveryService.initData(userPage);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 *  批量审核同意【采】
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "doAgree",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doAgree(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		StringBuffer msg = new StringBuffer();
		try {
			boolean isSuccess=deliveryService.doAgree(deliveryList, user.id,msg);
			if(isSuccess){
				map.put("message", "操作成功");
				map.put("success", true);
			}else{
				map.put("message", msg);
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "操作失败，请联系管理员");
		}
		
		return map;
	}
	
	/**
	 *  单个审核同意【采】
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "doSingleAgree",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doSingleAgree(@RequestParam("id")Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		DeliveryEntity dlv =deliveryService.findDlvById(id);
		List<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
		deliveryList.add(dlv);
		StringBuffer msg = new StringBuffer();
		try {
			boolean isSuccess=deliveryService.doAgree(deliveryList, user.id,msg);
			if(isSuccess){
				map.put("message", "操作成功");
				map.put("success", true);
			}else{
				map.put("message", msg);
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "操作失败，请联系管理员");
		}
		
		return map;
	}
	
	/**
	 * 批量驳回发货单【采】
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "doReject",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doReject(RejectVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isSucess=deliveryService.doReject(vo);
		if(isSucess){
			map.put("message", "驳回成功");
			map.put("success", true);
		}else{
			map.put("message", "驳回失败，请联系管理员");
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 批量审核添加锁定状态、冻结状态、删除状态、交付状态|订单数量减小的判断
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "judgeAudit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> judgeAudit(@RequestBody List<DeliveryEntity> deliveryList) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		map = deliveryService.judgeAudit(deliveryList);
		return map;   
	}
	
	/**
	 * 单个审核添加锁定状态、冻结状态、删除状态、交付状态|订单数量减小的判断
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "singleJudgeAudit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> singleJudgeAudit(@RequestParam("id")Long id) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		DeliveryEntity dlv =deliveryService.findDlvById(id);
		List<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
		deliveryList.add(dlv);
		map = deliveryService.judgeAudit(deliveryList);
		return map;   
	}
}
