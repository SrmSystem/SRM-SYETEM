package com.qeweb.scm.epmodule.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpQuoSubCostEntity;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;
import com.qeweb.scm.epmodule.service.EpQuoSubService;

/**
 *
 */
@Controller
@RequestMapping(value = "/manager/ep/epSubQuo")
public class EpSubQuoController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private EpQuoSubService epQuoSubService;    
	
	@RequestMapping(value = "displaySubQuoView")
	public String displaySubQuoView(Long epMaterialId, Model model){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EpWholeQuoEntity res=epQuoSubService.findWholeQuoByVendorAndMaterial(user.orgId, epMaterialId,EpConstans.STATUS_YES);
		if(res==null){
			res=epQuoSubService.findWholeQuoByVendorAndMaterial(user.orgId, epMaterialId,EpConstans.STATUS_NO);
		}
		res.setTaxRate(0.17);
		res.setIsIncludeTax("1");
		model.addAttribute("epWholeQuo", res);
		
		
		List<EpSubQuoEntity> itemList=epQuoSubService.findEpSubQuoListByWholeId(res.getId());
		if(itemList==null||itemList.size()<=0){
			epQuoSubService.getNewSubQuo(res.getId(),epMaterialId);
		}
		return "back/ep/epVendorSubQuoView";
	}
	
	@RequestMapping(value = "getSubQuoByWholeId/{wholeId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getSubQuoByWholeId(@PathVariable("wholeId")Long wholeId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_wholeQuo.id", wholeId);
		
		Page<EpSubQuoEntity> page = epQuoSubService.getSubQuoList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 根据分项id查cost
	 * @param epQuoSubId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSubQuoCostByQuoSubId/{epQuoSubId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getSubQuoCostByQuoSubId(@PathVariable("epQuoSubId")Long epQuoSubId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_epQuoSubId", epQuoSubId);
		
		Page<EpQuoSubCostEntity> page = epQuoSubService.getSubQuoCostList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 根据整项id查cost
	 * @param epQuoSubId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSubQuoCostByQuoWholeId/{epQuoWholeId}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getSubQuoCostByQuoWholeId(@PathVariable("epQuoWholeId")Long epQuoWholeId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_epQuoWholeId", epQuoWholeId);
		
		Page<EpQuoSubCostEntity> page = epQuoSubService.getSubQuoCostList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "saveSubQuo/{submitStatus}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveSubQuo(@PathVariable("submitStatus") int submitStatus,@Valid EpWholeQuoEntity whole,@RequestParam("quoteTemplate") MultipartFile files) throws Exception{ 
		String subItems=whole.getTableDatas();
		Map<String,Object> map = new HashMap<String, Object>();
		
		File savefile = null;
		if(files!=null&&!StringUtils.isEmpty(files.getOriginalFilename())){
			//1、保存文件至服务器
			try {
				savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
				if(savefile == null || savefile.length() == 0) {
					throw new Exception();
				}
			} catch (Exception e) {
				map.put("success", false);
				map.put("msg", "");
			}
		}
		//明细
		List<EpSubQuoEntity> itemList = new ArrayList<EpSubQuoEntity>();
		if(!StringUtils.isEmpty(subItems)){
			EpSubQuoEntity item = null;
			JSONObject object = JSONObject.fromObject(subItems);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new EpSubQuoEntity(); 

					if(StringUtils.isEmpty(StringUtils.convertToString(object.get("subtotal")))){
							map.put("success", false);
							map.put("msg", "小计不能为空");
							return map;
					}
					item.setId(object.get("id") == null ? 0l : StringUtils.convertToLong(object.get("id") + ""));
					
					item.setQty(StringUtils.convertToDouble(object.get("qty")+""));
					item.setTotalQuotePrice(StringUtils.convertToDouble(object.get("totalQuotePrice")+""));
					item.setSubtotal(StringUtils.convertToDouble(object.get("subtotal")+""));

					itemList.add(item);
				}
			}
		}
		epQuoSubService.saveQuoSub(submitStatus,itemList,whole,savefile,savefile==null?"":files.getOriginalFilename().substring(0,files.getOriginalFilename().lastIndexOf(".")));
		map.put("success", true);
		map.put("msg", "操作成功");
		return map;   
	}
	
	
	
	@RequestMapping(value = "saveSubQuoCost/{subQuoId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveSubQuoCost(@PathVariable("subQuoId") Long subQuoId,@Valid EpSubQuoEntity sub) throws Exception{ 
		String subItems=sub.getTableCostDatas();
		//明细
		List<EpQuoSubCostEntity> itemList = new ArrayList<EpQuoSubCostEntity>();
		if(!StringUtils.isEmpty(subItems)){
			EpQuoSubCostEntity item = null;
			JSONObject object = JSONObject.fromObject(subItems);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new EpQuoSubCostEntity(); 

					if(StringUtils.isEmpty(StringUtils.convertToString(object.get("totalPrice")))){
							map.put("success", false);
							map.put("msg", "小计不能为空");
							return map;
					}
					item.setId(object.get("id") == null ? 0l : StringUtils.convertToLong(object.get("id") + ""));
					
					item.setQty(StringUtils.convertToDouble(object.get("qty")+""));
					item.setPrice(StringUtils.convertToDouble(object.get("price")+""));
					item.setTotalPrice(StringUtils.convertToDouble(object.get("totalPrice")+""));
					item.setName(StringUtils.convertString(object.get("name")+""));

					itemList.add(item);
				}
			}
		}
		epQuoSubService.saveQuoSubCost(itemList,subQuoId);
		map.put("success", true);
		map.put("msg", "操作成功");
		return map;   
	}
	
}
