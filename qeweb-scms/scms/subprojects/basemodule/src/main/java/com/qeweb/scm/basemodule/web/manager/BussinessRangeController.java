package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.TypeConstant;
import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.service.BussinessRangeService;
import com.qeweb.scm.basemodule.utils.TreeUtil;
@Controller
@RequestMapping("/manager/basedata/bussinessRange")
public class BussinessRangeController {
	
	@Autowired
	private BussinessRangeService bussinessRangeService;
	
	private Map<String,Object> map;
	
	@RequiresPermissions(value={"sys:bus:view", "sys:busrang:view", "sys:brand:view", "sys:prodl:view"}, logical=Logical.OR)
	@RequestMapping(value="{page}",method = RequestMethod.GET)
	public String list(@PathVariable String page,Model model) {
//		List<UserEntity> users = accountService.getAllUser();
//		model.addAttribute("users", users);
		return "back/basedata/"+page;
	}
	
	@RequestMapping(value="toRangeTypePage",method = RequestMethod.GET)
	public String toRangeTypePage(Model model) {
//		List<UserEntity> users = accountService.getAllUser();
//		model.addAttribute("users", users);
		return "back/basedata/bussinessRangeTypeList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bussinessRangeList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<BussinessRangeEntity> page = bussinessRangeService.getBussinessRangeList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@LogClass(method="新增", module="业务范围新增")
	@RequiresPermissions(value={"sys:bus:add", "sys:busrang:add"}, logical=Logical.OR)
	@RequestMapping(value = "addNewBussinessRange",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewBussinessRange(@Valid BussinessRangeEntity bussinessRange){
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_0);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		bussinessRangeService.addNewBussinessRange(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	public Map<String,Object> validataCode(BussinessRangeEntity bussinessRange){
		map = new HashMap<String, Object>();
		List<BussinessRangeEntity> entity = bussinessRangeService.getBussinessRangeByCodeAndBussinessType(bussinessRange.getCode(), bussinessRange.getBussinessType());
		if(entity!=null&&entity.size()>0){
			if(entity.get(0).getId()==bussinessRange.getId()){
				return null;
			}
			map.put("msg", "编码已存在！");
			map.put("success", false);
			return map;
		}
		return null;
	}
	@LogClass(method="修改", module="业务范围修改")
	@RequiresPermissions(value={"sys:bus:upd", "sys:busrang:upd"}, logical=Logical.OR)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("bussinessRange") BussinessRangeEntity bussinessRange) {
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_0);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		map = new HashMap<String, Object>();
		bussinessRangeService.updateBussinessRange(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="新增", module="业务类型新增")
	@RequiresPermissions(value={"sys:bus:add", "sys:busrang:add"}, logical=Logical.OR)
	@RequestMapping(value = "addNewBussinessRangeType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewBussinessRangeType(@Valid BussinessRangeEntity bussinessRange){
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_1);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		bussinessRangeService.addNewBussinessRangeType(bussinessRange);
		map.put("success", true);
		return map;
	}
	@LogClass(method="修改", module="业务类型修改")
	@RequiresPermissions(value={"sys:bus:upd", "sys:busrang:upd"}, logical=Logical.OR)
	@RequestMapping(value = "updateRangeType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateRangeType(@Valid @ModelAttribute("bussinessRange") BussinessRangeEntity bussinessRange) {
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_1);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		map = new HashMap<String, Object>();
		bussinessRangeService.updateBussinessRangeType(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="新增", module="品牌新增")
	@RequiresPermissions(value={"sys:brand:add"}, logical=Logical.OR)
	@RequestMapping(value = "addNewBussinessRangeBrand",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewBussinessRangeBrand(@Valid BussinessRangeEntity bussinessRange){
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_2);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		bussinessRangeService.addNewBussinessRangeBrand(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="修改", module="品牌修改")
	@RequiresPermissions(value={"sys:brand:upd"}, logical=Logical.OR)
	@RequestMapping(value = "updateRangeBrand", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateRangeBrand(@Valid @ModelAttribute("bussinessRange") BussinessRangeEntity bussinessRange) {
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_2);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		map = new HashMap<String, Object>();
		bussinessRangeService.updateBussinessRangeBrand(bussinessRange);
		map.put("success", true);
		return map;
	}

	@LogClass(method="新增", module="产品线新增")
	@RequiresPermissions(value={"sys:prodl:add"}, logical=Logical.OR)
	@RequestMapping(value = "addNewBussinessRangeLine",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewBussinessRangeLine(@Valid BussinessRangeEntity bussinessRange){
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_3);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		bussinessRangeService.addNewBussinessRangeLine(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	@LogClass(method="修改", module="产品线修改")
	@RequiresPermissions(value={"sys:prodl:upd"}, logical=Logical.OR)
	@RequestMapping(value = "updateRangeLine", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateRangeLine(@Valid @ModelAttribute("bussinessRange") BussinessRangeEntity bussinessRange) {
		bussinessRange.setBussinessType(TypeConstant.BUSSINESSRANGE_TYPE_3);
		map = validataCode(bussinessRange);
		if(map != null){
			return map;
		}
		map = new HashMap<String, Object>();
		bussinessRangeService.updateBussinessRangeLine(bussinessRange);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteBussinessRange",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteBussinessRangeList(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		bussinessRangeService.deleteBussinessRangeList(bussinessRangeList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getBussinessRange/{id}")
	@ResponseBody
	public BussinessRangeEntity getBussinessRange(@PathVariable("id") Long id){
		return bussinessRangeService.getBussinessRange(id);
	}
	
	@RequestMapping(value="getBussinessRangeEasyuiTree",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTree> getBussinessRangeEasyuiTree(Long id,Integer type,ServletRequest request){
		List<BussinessRangeEntity> bussinessRangeList = bussinessRangeService.getByBussinessRange(id);
		List<EasyuiTree> bussinessRangeNodeList = TreeUtil.toEasyuiTree_bussiness(bussinessRangeList,type);
		return bussinessRangeNodeList;
	}
	
	
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = bussinessRangeService.abolishBatch(bussinessRangeList);
		return map;
	}
	@LogClass(method="作废", module="业务范围作废")
	@RequestMapping(value = "abolishBatchRange",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchRange(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = bussinessRangeService.abolishBatch(bussinessRangeList);
		return map;
	}
	@LogClass(method="作废", module="业务类型作废")
	@RequestMapping(value = "abolishBatchType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchType(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = bussinessRangeService.abolishBatch(bussinessRangeList);
		return map;
	}
	@LogClass(method="作废", module="品牌作废")
	@RequestMapping(value = "abolishBatchBrand",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchBrand(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = bussinessRangeService.abolishBatch(bussinessRangeList);
		return map;
	}
	@LogClass(method="作废", module="产品线作废")
	@RequestMapping(value = "abolishBatchLine",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatchLine(@RequestBody List<BussinessRangeEntity> bussinessRangeList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = bussinessRangeService.abolishBatch(bussinessRangeList);
		return map;
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindBussinessRange(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("bussinessRange", bussinessRangeService.getBussinessRange(id));
		}
	}
	
	
	@RequestMapping(value = "getBussinessRange/{id}/{type}",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getBussinessRange(@PathVariable("id")Long id,@PathVariable("type")Integer type,ServletRequest request){
		List<BussinessRangeEntity> bList = null;
		
		if(id==-1 || id==0){
			bList = bussinessRangeService.getBussinessRangeByBussinessType(type);
		}else{
			bList = bussinessRangeService.getBussinessRangeByParentIdAndBussinessType(id,type);
		}
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		for(int i=0;i < bList.size();i++){
			BussinessRangeEntity bo = bList.get(i);
			EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
			couTreeList.add(option);
		}
		return couTreeList;
	}
	
	@RequestMapping(value = "getBussinessRange2/{id}/{type}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getBussinessRange(@PathVariable("id")Long id,@PathVariable("type")Integer type,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("EQ_bussinessType", type);
		Page<BussinessRangeEntity> page = bussinessRangeService.getBussinessRangeList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
}
