package com.qeweb.scm.purchasemodule.web.orderplan;

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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityRelEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanVendorCapacityRelService;
import com.qeweb.scm.purchasemodule.web.vo.CapacityVO;
import com.qeweb.scm.purchasemodule.web.vo.CapacityViewVO;


/**
 * 供应商产能关系维护Controller
 * @author hao.qin
 * @date 2017年6月15日
 */
@Controller
@RequestMapping("/manager/order/purchasePlanVendorCapacityRel")
public class PurchasePlanVendorCapacityRelController {
	
	@Autowired
	private PurchasePlanVendorCapacityRelService purchasePlanVendorCapacityRelService;
	
	@Autowired
	private DictItemService dictItemService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/orderplan/purchasePlanVendorCapacityRelList";
	}
	
	/**
	 * 添加和编辑显示
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="capacityRel/{id}", method = RequestMethod.GET)
	public String capacityRel(@PathVariable(value="id") Long id, Model model) {
		
	    //获取产能表选择信息
		List<CapacityVO>  cvList = purchasePlanVendorCapacityRelService.getCapacityVOList(id);
		//获取产能表与供应商对应关系信息
		PurchasePlanVendorCapacityRelEntity vc = purchasePlanVendorCapacityRelService.getPurchasePlanVendorCapacityRel(id);
		model.addAttribute("cvList", cvList);
		model.addAttribute("vc", vc);
		return "back/orderplan/capacityRelAdd";
	}
	
	
    /**
     * 获取供应商产能关系维护的列表
     */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> purchasePlanVendorCapacityRelList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<PurchasePlanVendorCapacityRelEntity> page = purchasePlanVendorCapacityRelService.getList(pageNumber,pageSize,searchParamMap);
		List<PurchasePlanVendorCapacityRelEntity> newList = page.getContent();
		if(newList != null && newList.size() > 0){
			for(PurchasePlanVendorCapacityRelEntity temp : newList){ 
				String strName = "";
				int cur=0;
				String [] codes=temp.getCapacityCodes().split(",");
				for (String str : codes) {
					 ++cur;
					//通过code转换为name
					DictItemEntity  dictItem = dictItemService.findDictItemByCode(str);
					if(codes.length == cur){
						strName = strName +dictItem.getName();
					}else{
						strName = strName +dictItem.getName() +",";
					}
				}
				temp.setCapacityNames(strName);
			}
 		}
		map = new HashMap<String, Object>();
		map.put("rows",newList);
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
     * 新增供应商产能关系的信息
     */
	@RequestMapping(value = "add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(@Valid CapacityViewVO capacityViewVO){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanVendorCapacityRelService.addNewPurchasePlanVendorCapacityRel(capacityViewVO);
		map.put("success", true);
		return map;
	}
	
	/**
     * 更新供应商产能关系
     */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid CapacityViewVO capacityViewVO) {
		map = new HashMap<String, Object>();
		purchasePlanVendorCapacityRelService.update(capacityViewVO);
		map.put("success", true);
		return map;
	}
	
	/**
     * 删除供应商产能关系
     */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@RequestBody List<PurchasePlanVendorCapacityRelEntity> purchasePlanVendorCapacityRelEntityList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanVendorCapacityRelService.deletePurchasePlanVendorCapacityRelList(purchasePlanVendorCapacityRelEntityList);
		map.put("success", true);
		return map;
	}
	
	/**
     * 获取单个供应商产能关系的信息
     */
	@RequestMapping("getPurchasePlanVendorCapacityRel/{id}")
	@ResponseBody
	public PurchasePlanVendorCapacityRelEntity getPurchasePlanVendorCapacityRel(@PathVariable("id") Long id){
		return purchasePlanVendorCapacityRelService.getPurchasePlanVendorCapacityRel(id);
	}
		
	/**
     *作废关系
     */
	@RequestMapping(value = "abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<PurchasePlanVendorCapacityRelEntity> purchasePlanVendorCapacityRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = purchasePlanVendorCapacityRelService.abolishBatch(purchasePlanVendorCapacityRelList);
		return map;
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindPurchasePlanVendorCapacityRelEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("purchasePlanVendorCapacityRel", purchasePlanVendorCapacityRelService.getPurchasePlanVendorCapacityRel(id));
		}
	}
	

}
