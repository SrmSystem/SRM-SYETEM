package com.qeweb.scm.check.web;


import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.service.NoCheckedItemsService;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.task.service.CreateCheckService;
import com.qeweb.scm.sap.service.NoCheckedItemsSyncService;


/**
 * 未对账明细
 */
@Controller
@RequestMapping(value = "/manager/check/nocheckeditems")
public class NoCheckedItemsController implements ILog {
	
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private NoCheckedItemsService service;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private CreateCheckService createCheckService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/check/buyer/noCheckItemList";
	}
	
	@RequestMapping(value="/vendor",method = RequestMethod.GET)
	public String list_vendor(Model model) {
		return "back/check/vendor/noCheckItemList";
	}
	/**
	 * 获取订单明细数据
	 * @param type
	 * @param vendor
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(value={"no:check:list:view","no:check:list:v:view"},logical=Logical.OR)
	@LogClass(method="查询未对账的收货明细",module="对账管理")
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isEmpty(sort)){
			sort = "createTime";
			order = "desc";
		}
		logger.log("sort == " + sort);
		logger.log("order == " + order);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		OrganizationEntity org = orgService.getOrg(user.orgId);
		if(org.getRoleType()==OrgType.ROLE_TYPE_VENDOR){
			searchParamMap.put("EQ_vendorId", user.orgId);
		}
		//组织权限
		searchParamMap.put("IN_buyerId", buyerOrgPermissionUtil.getBuyerIds());
		searchParamMap = initSearchTime(searchParamMap, "GTE_createTime");			//开始时间
		searchParamMap = initSearchTime(searchParamMap, "LTE_createTime");
		searchParamMap.put("NEQ_state", "1");
		searchParamMap.put("NEQ_type", "1");
		
		Page<NoCheckItemsEntity> page = service.getNoCheckItems(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, order));

		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	/**
	 * 初始化查询时间
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	private Map<String, Object> initSearchTime(Map<String, Object> searchParamMap, String key) {
		String time = (String) searchParamMap.get(key);
		if(time!=null && !time.isEmpty()){
			searchParamMap.put(key, Timestamp.valueOf(time+" 00:00:00"));
		}
		return searchParamMap;
	}
	
	/**
	 * 显示收货单明细详情
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewItemDetail/{itemId}", method = RequestMethod.GET)
	public String viewItemDetail(@PathVariable(value="itemId") Long itemId, Model model) {
		ReceiveItemEntity recItem = service.getRecItemEntity(itemId);
		model.addAttribute("po", recItem);
		return "back/check/recItem";
	}
	
	

	/**
	 * 根据选择的数据生成对账单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "createCheck",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createCheck(@RequestBody Map<String ,Object> param)  throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		//组织权限
		param.put("IN_buyerId", buyerOrgPermissionUtil.getBuyerIds());
		param = initSearchTime(param, "GTE_createTime");			//开始时间
		param = initSearchTime(param, "LTE_createTime");
		List<NoCheckItemsEntity> noCheckItemList = service.getNoCheckItemList(param);
		StringBuffer msgBuf = new StringBuffer();
		boolean temp = createCheckService.createNoCheck(noCheckItemList,msgBuf,logger);
		map.put("message", msgBuf.toString());
		map.put("success", temp);
		return map;
	}
	
	/**
	 * 未对账信息,同步采购结算
	 * @author haiming.huang
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sycNocheckeditems",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycNocheckeditems() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		NoCheckedItemsSyncService noCheckedItemsSyncService = SpringContextUtils.getBean("noCheckedItemsSyncService");	
		boolean isSuccess = noCheckedItemsSyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除未对账信息
	 * @author haiming.huang
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteCheckOpt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteCheckOpt(@RequestBody Map<String ,Object> param) throws Exception {
		return service.deleteCheckOpt(param);
	}

	@Override
	public void log(Object message) {
		
	}

}
