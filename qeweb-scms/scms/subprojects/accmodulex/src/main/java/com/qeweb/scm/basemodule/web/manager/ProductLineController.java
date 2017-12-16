package com.qeweb.scm.basemodule.web.manager;

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

import com.qeweb.scm.basemodule.entity.ProductLineEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.ProductLineService;
import com.qeweb.scm.basemodule.utils.TreeUtil;

@Controller
@RequestMapping("/manager/basedata/productLine")
public class ProductLineController {
	
	@Autowired
	private ProductLineService productLineService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/productLineList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> productLineList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<ProductLineEntity> page = productLineService.getProductLineList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewProductLine",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewProductLine(@Valid ProductLineEntity productLine){
		Map<String,Object> map = new HashMap<String, Object>();
		productLineService.addNewProductLine(productLine);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("productLine") ProductLineEntity productLine) {
		map = new HashMap<String, Object>();
		productLineService.updateProductLine(productLine);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteProductLine",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteProductLineList(@RequestBody List<ProductLineEntity> productLineList){
		Map<String,Object> map = new HashMap<String, Object>();
		productLineService.deleteProductLineList(productLineList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getProductLine/{id}")
	@ResponseBody
	public ProductLineEntity getProductLine(@PathVariable("id") Long id){
		return productLineService.getProductLine(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindProductLine(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("productLine", productLineService.getProductLine(id));
		}
	}
	
	

}
