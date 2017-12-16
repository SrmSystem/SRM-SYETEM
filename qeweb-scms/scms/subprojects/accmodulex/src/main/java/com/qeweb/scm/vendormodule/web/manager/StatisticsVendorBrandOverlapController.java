package com.qeweb.scm.vendormodule.web.manager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.service.VendorMaterialSupplyRelService;
import com.qeweb.scm.vendormodule.vo.StatisticsVendorBrandOverlapTransfer;

@Controller
@RequestMapping(value="/manager/vendor/brandOverlap")
public class StatisticsVendorBrandOverlapController {

	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	@Autowired
	private VendorMaterialSupplyRelService materialSupplyRelService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="供货品牌重合度统计")
	@RequestMapping(method = RequestMethod.GET)
	public String statistics(Model model){
		return "back/vendor/statiVendorBrandOverlapList";
	}
	
	@RequestMapping(value = "statiVendorBrandOverlapList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> statiVendorCountList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		
		
		List<VendorMaterialSupplyRelEntity> allList = materialSupplyRelService.getVendorMaterialSupplyRelAll(searchParamMap);
		Map<String,VendorMaterialSupplyRelEntity> lMap = new HashMap<String, VendorMaterialSupplyRelEntity>();
		for (VendorMaterialSupplyRelEntity vmsrEntity : allList) {
			VendorMaterialSupplyRelEntity entity = lMap.get(vmsrEntity.getVendorName());
			if(entity==null){
				entity = vmsrEntity;
				entity.setAllBrand(entity.getBrandName());
				entity.setBrandCount(1);
			}else{
				
				if((entity.getAllBrand()+"").indexOf(vmsrEntity.getBrandName()+"")==-1){
					entity.setAllBrand(entity.getAllBrand()+","+vmsrEntity.getBrandName());
					entity.setBrandCount(entity.getBrandCount()+1);
				}
			}
			lMap.put(entity.getVendorName(), entity);
		}
		List<VendorMaterialSupplyRelEntity> retList = new ArrayList<VendorMaterialSupplyRelEntity>();
		for (String key : lMap.keySet()) {
			retList.add(lMap.get(key));
		}
		map = new HashMap<String, Object>();
		
		
		Integer i=(pageNumber-1)*pageSize;
		Integer p=0;
		if((retList.size()-i)<=pageSize)
			p=retList.size()-i;
		else
			p=pageSize;
		Page<VendorMaterialSupplyRelEntity> page =new PageImpl<VendorMaterialSupplyRelEntity>(retList.subList(i, p+i), pagin, retList.size());
		
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商品牌重合度统计", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<StatisticsVendorBrandOverlapTransfer> list = materialSupplyRelService.getStatisticsVendorBrandOverlapVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(StatisticsVendorBrandOverlapTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	
}
