package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.sap.service.PurchaseOrderTypeSyncService;

@Controller
@RequestMapping("/manager/basedata/orderType")
public class OrderTypeController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private DictItemService dictItemService; 
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/orderTypeList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
	/*	searchParamMap.put("abolished", 0);*/
		searchParamMap.put("dict.dictCode", "PURCHASE_ORDER_TYPE");
		Page<DictItemEntity> page = dictItemService.getList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		PurchaseOrderTypeSyncService purchaseOrderTypeSyncService = SpringContextUtils.getBean("purchaseOrderTypeSyncService");	
		boolean isSuccess= purchaseOrderTypeSyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<DictItemEntity> dictItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = dictItemService.abolishBatch(dictItemList);
		return map;
	}
	
	@RequestMapping(value = "effect",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> effect(@RequestBody List<DictItemEntity> dictItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = dictItemService.effect(dictItemList);
		return map;
	}
	

	@Override
	public void log(Object message) {
		getLogger().log(message);
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	

}
