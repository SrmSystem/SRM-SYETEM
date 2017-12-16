package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforIndexService;

/**
 * 指标设置
 */
@Controller
@RequestMapping("/manager/vendor/performance/index")
public class VendorPerforIndexController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforIndexService service;
	
	@LogClass(method="查看", module="指标设置")
	@RequiresPermissions("perfor:index:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/vendorPerforIndexList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorPerforIndexList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforIndexEntity> page = service.getVendorPerforIndexList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 获取维度下的指标
	 * @param dimId
	 * @return
	 */
	@RequestMapping("getList/{dimId}")
	@ResponseBody
	public List<VendorPerforIndexEntity> getList(@PathVariable(value="dimId") Long dimId){
		List<VendorPerforIndexEntity> list = service.getList(dimId);
		return list;
	}
	
	/**
	 * 获取指标下的要素
	 * @param indexId
	 * @return
	 */
	@RequestMapping("getkey/{indexId}")
	@ResponseBody
	public List<Map<String, String>> getkey(@PathVariable(value="indexId") Long indexId){
		return service.getkeyList(indexId);
	}

	@RequiresPermissions("perfor:index:add")
	@RequestMapping(value="addVendorPerforIndex",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addVendorPerforIndex(VendorPerforIndexEntity vendorPerforIndexEntity){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "cycleId-");
		String[] cycleIds=null;
		String[] contents=null;
		if(null!=searchParamMap)
		{
			cycleIds=new String[searchParamMap.size()];
			contents=new String[searchParamMap.size()];
			Iterator<Entry<String, Object>> it=searchParamMap.entrySet().iterator();
			int i=0;
			while(it.hasNext())
			{
				String number=it.next().getKey();
				cycleIds[i]=""+searchParamMap.get(number);
				contents[i]=request.getParameter("content"+number);
				i++;
			}
		}
		return service.addVendorPerforIndex(vendorPerforIndexEntity,cycleIds,contents);
	}
	@RequestMapping(value="updateVendorPerforIndexStart/{id}",method = RequestMethod.GET)
	public String updateVendorPerforIndexStart(@PathVariable("id") long id){
		VendorPerforIndexEntity vi= service.updateVendorPerforIndexStart2(id);
		List<VendorPerforFormulasEntity> vfs=service.updateVendorPerforIndexStart(id);
		request.setAttribute("vi",vi);
		request.setAttribute("vfs",vfs);
		return "back/performance/index/update";
	}
	@RequiresPermissions("perfor:index:upd")
	@RequestMapping(value="updateVendorPerforIndex",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateVendorPerforIndex(VendorPerforIndexEntity vendorPerforIndexEntity){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "cycleId-");
		String[] cycleIds=null;
		String[] contents=null;
		if(null!=searchParamMap)
		{
			cycleIds=new String[searchParamMap.size()];
			contents=new String[searchParamMap.size()];
			Iterator<Entry<String, Object>> it=searchParamMap.entrySet().iterator();
			int i=0;
			while(it.hasNext())
			{
				String number=it.next().getKey();
				cycleIds[i]=""+searchParamMap.get(number);
				contents[i]=request.getParameter("content"+number);
				i++;
			}
		}
		return service.updateVendorPerforIndex(cycleIds,contents,vendorPerforIndexEntity);
	}
	
	@RequiresPermissions("perfor:index:cancel")
	@RequestMapping(value = "deleteVendorPerforIndex",method = RequestMethod.POST)
	@ResponseBody
	public String deleteVendorPerforIndex(@RequestBody List<VendorPerforIndexEntity> vendorPerforIndexEntitys){
		return service.deleteVendorPerforIndex(vendorPerforIndexEntitys);
	}
	
	@RequiresPermissions("perfor:index:del")
	@RequestMapping(value = "deleteList",method = RequestMethod.POST)
	@ResponseBody
	public String deleteList(@RequestBody List<VendorPerforIndexEntity> vendorPerforIndexEntitys){
		return service.deleteList(vendorPerforIndexEntitys);
	}
	@RequestMapping(value = "getDimensions")
	@ResponseBody
	public String getDimensions(){
		return service.getDimensions();
	}
	@RequestMapping(value = "getVendorPerforCycle",method = RequestMethod.POST)
	@ResponseBody
	public String getVendorPerforCycle(){
		return service.getVendorPerforCycle();
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	@Override
	public void log(Object message) {
		getLogger().log(message); 
	} 
}
