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

import com.qeweb.scm.basemodule.entity.BrandEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.service.BrandService;
import com.qeweb.scm.basemodule.utils.TreeUtil;

@Controller
@RequestMapping("/manager/basedata/brand")
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/brandList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> brandList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<BrandEntity> page = brandService.getBrandList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping("getList")
	@ResponseBody
	public List<BrandEntity> getList(Model model,ServletRequest request){
		List<BrandEntity> list = brandService.getBrandList();
		return list;
	}
	
	@RequestMapping(value = "addNewBrand",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewBrand(@Valid BrandEntity brand){
		Map<String,Object> map = new HashMap<String, Object>();
		brandService.addNewBrand(brand);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("brand") BrandEntity brand) {
		map = new HashMap<String, Object>();
		brandService.updateBrand(brand);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteBrand",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteBrandList(@RequestBody List<BrandEntity> brandList){
		Map<String,Object> map = new HashMap<String, Object>();
		brandService.deleteBrandList(brandList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getBrand/{id}")
	@ResponseBody
	public BrandEntity getBrand(@PathVariable("id") Long id){
		return brandService.getBrand(id);
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindBrand(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("brand", brandService.getBrand(id));
		}
	}
	
	

}
