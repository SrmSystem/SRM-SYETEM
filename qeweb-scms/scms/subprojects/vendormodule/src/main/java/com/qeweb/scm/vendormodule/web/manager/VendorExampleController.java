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
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.service.VendorExampleService;
import com.qeweb.scm.vendormodule.vo.VendorExampleTransfer;

@Controller
@RequestMapping(value="/manager/vendor/vendorExample")
public class VendorExampleController {
	
	@Autowired
	private VendorExampleService vendorExampleService;
	
	@Autowired
	private HttpServletRequest request;
//	
//	@Autowired
//	private HttpServletResponse response;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="标杆企业供应商统计")
	@RequestMapping(method=RequestMethod.GET)
	public String vendorExamplePage()
	{
		return "back/vendor/vendorExampleList";
	}
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorExampleList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorMaterialSupplyRelEntity> page = vendorExampleService.getVendorExampleList(pageNumber, pageSize, searchParamMap, request);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping("/exportExcel")
	public String download(HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("标杆企业供应商统计", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<VendorExampleTransfer> list = vendorExampleService.getVendorExampleVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendorExampleTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
}
