package com.qeweb.scm.vendormodule.web.manager;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.service.MaterialTypeService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.service.VendCountSurveyService;
import com.qeweb.scm.vendormodule.vo.VendCountSurveyTransfer;

@Controller
@RequestMapping(value="/manager/vendor/vendCountSurvey")
public class VendCountSurveyController {
	@Autowired
	private MaterialTypeService materialTypeService;

	
	@Autowired
	private VendCountSurveyService vendCountSurveyService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="供应商供货关系及系数统计")
	@RequestMapping(method=RequestMethod.GET)
	public String vendCountSurveyPage()
	{
		return "back/vendor/vendCountSurveyList";
	}
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendCountSurveyList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,HttpServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
//		//特殊搜索，需要多级，及In查询
//		Map<String,Object> speParamMap = Servlets.getParametersStartingWith(request, "special-");
//		//物料分类
//		List<MaterialTypeEntity> mat = new ArrayList<MaterialTypeEntity>(); 
//		//In查询用到的parentId，最终为四级分类ID
//		List<Long> longList = new  ArrayList<Long>();
//		
//		if(speParamMap!=null&&speParamMap.size()>0){
//			if(StringUtils.isNotEmpty(speParamMap.get("matt1")+"")){
//				//一级系统
//				 mat = materialTypeService.getMaterialByNameAndLevelLayer(speParamMap.get("matt1")+"",1);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				 //二级分组
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				 //三级子组
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				 //四级零部件
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				 
//			}else if(StringUtils.isNotEmpty(speParamMap.get("matt2")+"")){
//				mat = materialTypeService.getMaterialByNameAndLevelLayer(speParamMap.get("matt2")+"",2);
//				//三级子组
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				 //四级零部件
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//			}else if(StringUtils.isNotEmpty(speParamMap.get("matt3")+"")){
//				mat = materialTypeService.getMaterialByNameAndLevelLayer(speParamMap.get("matt3")+"",3);
//				longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//				//四级零部件
//				 mat = materialTypeService.getMaterialListByParentIdIn(longList);
//				 longList = new  ArrayList<Long>();
//				 for(MaterialTypeEntity m : mat){
//					 longList.add(m.getId());
//				 }
//			}
//		}
//		if(longList.size()>0){
//			searchParamMap.put("IN_materialEntity.materialTypeId", longList);
//		}
		Page<VendorMaterialSupplyRelEntity> page = vendCountSurveyService.getVendCountSurveyList(pageNumber, pageSize, searchParamMap, request);
		for (int i= 0 ; i < page.getContent().size(); i++) {
			VendorMaterialSupplyRelEntity v = page.getContent().get(i);
			MaterialTypeEntity mt4 = materialTypeService.getMaterial(v.getMaterialEntity().getMaterialTypeId());
			MaterialTypeEntity mt3 = null;
			MaterialTypeEntity mt2 = null;
			MaterialTypeEntity mt1 = null;
			if(mt4!=null)
				mt3 = materialTypeService.getMaterial(mt4.getParentId());
			if(mt3!=null)
				mt2 = materialTypeService.getMaterial(mt3.getParentId());
			if(mt2!=null)
				mt1 = materialTypeService.getMaterial(mt2.getParentId());
			page.getContent().get(i).setMaterialType1(mt1);
			page.getContent().get(i).setMaterialType2(mt2);
			page.getContent().get(i).setMaterialType3(mt3);
			page.getContent().get(i).setMaterialType4(mt4);
			if(page.getContent().get(i).getVendorBaseInfoEntity()!=null)
			{
				if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty()!=null)
				{
					if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty().equals("1"))
					{
						page.getContent().get(i).getVendorBaseInfoEntity().setProperty("国有企业");
					}
					else if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty().equals("2"))
					{
						page.getContent().get(i).getVendorBaseInfoEntity().setProperty("国有控股企业");
					}
					else if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty().equals("3"))
					{
						page.getContent().get(i).getVendorBaseInfoEntity().setProperty("外资企业");
					}
					else if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty().equals("4"))
					{
						page.getContent().get(i).getVendorBaseInfoEntity().setProperty("合资企业");
					}
					else if(page.getContent().get(i).getVendorBaseInfoEntity().getProperty().equals("5"))
					{
						page.getContent().get(i).getVendorBaseInfoEntity().setProperty("私营企业");
					}
				}
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping("/exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商供货关系及系数统计", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<VendCountSurveyTransfer> list = vendCountSurveyService.getVendCountSurveyVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendCountSurveyTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
}
