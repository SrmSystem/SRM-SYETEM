package com.qeweb.scm.basemodule.web.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.PurchasingGroupService;
import com.qeweb.scm.sap.service.PurchasingGroupSyncService;

@Controller
@RequestMapping("/manager/basedata/purchasingGroup")
public class PurchasingGroupController implements ILog{
	private ILogger logger = new FileLogger();
	@Autowired
	private PurchasingGroupService purchasingGroupService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/organizationStructure/purchasingGroupList";
	}
	
	@RequestMapping(value = "sycOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sycOrder() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		PurchasingGroupSyncService purchasingGroupSyncService = SpringContextUtils.getBean("purchasingGroupSyncService");	
		boolean isSuccess = purchasingGroupSyncService.execute(logger);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> purchasingGroupList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<PurchasingGroupEntity> page = purchasingGroupService.getPurchasingGroupList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value="getCheckpurchasingGroupList/{dataIds}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getCheckpurchasingGroupList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,@PathVariable("dataIds") String dataIds,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		PageRequest pagin = new PageRequest(pageNumber , pageSize, null);
		List<PurchasingGroupEntity> arrayList = purchasingGroupService.getPurchasingGroupListForCheck(searchParamMap);
		if(!dataIds.equals("-1") ){
			String[] datas = dataIds.split(",");
			for(PurchasingGroupEntity item : arrayList ){
				item.setIsCheck(0);
				for(String data : datas){
					Long id = Long.parseLong(data);
					if(id == item.getId() ){
						item.setIsCheck(1);
						break;
					}
				}
			}
		
			Collections.sort(arrayList, new Comparator<PurchasingGroupEntity>() {  
	            public int compare(PurchasingGroupEntity o1, PurchasingGroupEntity o2) {  
	                return o2.getIsCheck().compareTo( o1.getIsCheck());  
	            }  
	        });
			
		}
	
		//假分页
		int totalcount=arrayList.size();
		int pagecount=0;
		int m=totalcount%pageSize;
		if  (m>0)
		{
		   pagecount=totalcount/pageSize+1;
		}
		else
		{
		   pagecount=totalcount/pageSize;
		}
		
		if (m==0)
		{
			arrayList= arrayList.subList((pageNumber-1)*pageSize,pageSize*(pageNumber));
		}
		else
		{
		   if (pageNumber==pagecount)
		   {
			   arrayList= arrayList.subList((pageNumber-1)*pageSize,totalcount);
		   }
		   else
		   {
			   arrayList= arrayList.subList((pageNumber-1)*pageSize,pageSize*(pageNumber));
		   }
		}
		
		Page<PurchasingGroupEntity> page2=new PageImpl<PurchasingGroupEntity>(arrayList,pagin,totalcount);
		map = new HashMap<String, Object>();
		map.put("rows",page2.getContent());
		map.put("total",page2.getTotalElements());
		return map;
	}
	

	@RequestMapping(value = "addNewPurchasingGroup",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewPurchasingGroup(@Valid PurchasingGroupEntity purchasingGroup){
		
		Map<String,Object> map = new HashMap<String, Object>();
		purchasingGroupService.addNewPurchasingGroup(purchasingGroup);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("inventoryLocation") PurchasingGroupEntity purchasingGroup) {
		map = new HashMap<String, Object>();
		purchasingGroupService.update(purchasingGroup);
		map.put("success", true);
		return map;
		
	}
	
	@RequestMapping(value = "effectBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> effectBatch(@RequestBody List<PurchasingGroupEntity> factoryList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = purchasingGroupService.effectBatch(factoryList);
		return map;
	}
	
	
	
	@RequestMapping(value = "deletePurchasingGroup",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletePurchasingGroup(@RequestBody List<PurchasingGroupEntity> purchasingGroupList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasingGroupService.deletePurchasingGroup(purchasingGroupList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getPurchasingGroup/{id}")
	@ResponseBody
	public PurchasingGroupEntity getPurchasingGroup(@PathVariable("id") Long id){
		return purchasingGroupService.getPurchasingGroup(id);
	}
	
	@RequestMapping(value = "getPurchasingGroupSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getPurchasingGroupSelect(ServletRequest request){
		List<PurchasingGroupEntity> list = purchasingGroupService.findAll();
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < list.size();i++){
			PurchasingGroupEntity bo = list.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<PurchasingGroupEntity> purchasingGroupList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = purchasingGroupService.abolishBatch(purchasingGroupList);
		return map;
	}
	
	
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindInventoryLocationEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("purchasingGroup", purchasingGroupService.getPurchasingGroup(id));
		}
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
