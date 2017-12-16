package com.qeweb.scm.vendormodule.web.manager;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.BussinessRangeService;
import com.qeweb.scm.basemodule.service.FactoryService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMaterialRelService;
import com.qeweb.scm.vendormodule.service.VendorMaterialSupplyRelService;
import com.qeweb.scm.vendormodule.vo.VendorMaterialSupplyRelTransfer;
/**
 * 供货关系Controller
 * @author lw
 * 创建时间：2015年6月15日15:42:31
 * 最后更新时间：2015年6月30日09:43:16
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/vendor/materialSupplyRel")
public class VendorMaterialSupplyRelController implements ILog{
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private VendorMaterialSupplyRelService vendormaterialSupplyRelService;
	
	@Autowired
	private VendorMaterialRelService vendorMaterialRelService;
	
	@Autowired
	private BussinessRangeService bussinessRangeService;
	
	@Autowired
	private FactoryService factoryService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorMaterialSupplyRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorMaterialSupplyRelList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorMaterialSupplyRelEntity> page = vendormaterialSupplyRelService.getVendorMaterialSupplyRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="getMSRelList/{id}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getMSRelList(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<BussinessRangeEntity> page = vendormaterialSupplyRelService.getMSRelList(pageNumber,pageSize,id,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 根据VendorMaterialRel id 获取 供货关系List
	 * @param id VendorMaterialRel.id
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getMaterialSupplyRelList/{id}",method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> getMaterialSupplyRelList(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorMaterialSupplyRelEntity> page = vendormaterialSupplyRelService.getVendorMaterialSupplyRelByRelId(id,pageNumber,pageSize,searchParamMap);
		Double totalSupplyCoefficient = vendormaterialSupplyRelService.getTotalSupplyCoeFFicientByRelId(id);
		map = new HashMap<String, Object>();
		map.put("totalSuCoe", totalSupplyCoefficient);
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewVendorMaterialSupplyRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorMaterialSupplyRel(@Valid VendorMaterialSupplyRelEntity vendorMaterialSupplyRel){
		Map<String,Object> map = new HashMap<String, Object>();
		BussinessRangeEntity bRange = new BussinessRangeEntity();
		if(vendorMaterialSupplyRel.getBussinessRangeId()!=null){
			bRange = bussinessRangeService.getBussinessRange(vendorMaterialSupplyRel.getBussinessRangeId());
			vendorMaterialSupplyRel.setBussinessRangeName(bRange.getName());
		}
		if(vendorMaterialSupplyRel.getBussinessId()!=null){
			bRange = bussinessRangeService.getBussinessRange(vendorMaterialSupplyRel.getBussinessId());
			vendorMaterialSupplyRel.setBussinessName(bRange.getName());
		}
		if(vendorMaterialSupplyRel.getBrandId()!=null){
			bRange = bussinessRangeService.getBussinessRange(vendorMaterialSupplyRel.getBrandId());
			vendorMaterialSupplyRel.setBrandName(bRange.getName());
		}
		if(vendorMaterialSupplyRel.getProductLineId()!=null){
			bRange = bussinessRangeService.getBussinessRange(vendorMaterialSupplyRel.getProductLineId());
			vendorMaterialSupplyRel.setProductLineName(bRange.getName());
		}
		if(vendorMaterialSupplyRel.getProductLineId()!=null){
			FactoryEntity fEntity = factoryService.getFactory(vendorMaterialSupplyRel.getFactoryId());
			vendorMaterialSupplyRel.setFactoryName(fEntity.getName());
		}
		VendorMaterialRelEntity materialRel = vendorMaterialRelService.getVendorMaterialRel(vendorMaterialSupplyRel.getMaterialRelId());
		vendorMaterialSupplyRel.setOrgId(materialRel.getOrgId());
		vendorMaterialSupplyRel.setVendorId(materialRel.getVendorId());
		vendorMaterialSupplyRel.setMaterialId(materialRel.getMaterialId());
		vendorMaterialSupplyRel.setVendorName(materialRel.getVendorName());
		vendorMaterialSupplyRel.setMaterialName(materialRel.getMaterialName());
		vendormaterialSupplyRelService.addNewVendorMaterialSupplyRel(vendorMaterialSupplyRel);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorMaterialSupplyRel") VendorMaterialSupplyRelEntity vendorMaterialSupplyRel) {
		map = new HashMap<String, Object>();
		vendormaterialSupplyRelService.updateVendorMaterialSupplyRel(vendorMaterialSupplyRel);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorMaterialSupplyRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorMaterialSupplyRelList(@RequestBody List<VendorMaterialSupplyRelEntity> vendorMaterialSupplyRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendormaterialSupplyRelService.deleteVendorMaterialSupplyRelList(vendorMaterialSupplyRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorMaterialSupplyRel/{id}")
	@ResponseBody
	public VendorMaterialSupplyRelEntity getVendorMaterialSupplyRel(@PathVariable("id") Long id){
		return vendormaterialSupplyRelService.getVendorMaterialSupplyRel(id);
	}
	
	@RequestMapping("filesUpload/{materialRelId}")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUpload(@PathVariable("materialRelId")Long materialRelId,@RequestParam("planfiles") MultipartFile files) {
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(VendorMaterialSupplyRelTransfer.class), getLogger()); 
			List<VendorMaterialSupplyRelTransfer> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			map = vendormaterialSupplyRelService.combineMaterialSupple(list, getLogger(),materialRelId);
		} catch (Exception e) {
			map.put("msg", "导入供货系数失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));    
		}
		return map;   
	}
	
	/**
	 * 供货系数导出
	 * @param materialRelId 供货关系ID
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exportExcel/{materialRelId}")
	public String download(@PathVariable("materialRelId")Long materialRelId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供货系数", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<VendorMaterialSupplyRelTransfer> list = vendormaterialSupplyRelService.getVendorMaterialSupplyRelVo(materialRelId);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendorMaterialSupplyRelTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorMaterialSupplyRel(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorMaterialSupplyRel", vendormaterialSupplyRelService.getVendorMaterialSupplyRel(id));
		}
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
