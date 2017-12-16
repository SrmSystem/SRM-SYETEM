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
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.service.VendorMaterialRelService;
import com.qeweb.scm.vendormodule.vo.VendorMaterialRelTransfer;
import com.qeweb.scm.vendormodule.vo.VendorMaterialRelTransfer;
import com.qeweb.scm.vendormodule.vo.VendorMaterialRelTransfer2;
/**
 * 物料关系Controller
 * @author lw
 * 创建时间：2015年6月15日 11:09:03
 * 最后更新时间：2015年6月30日09:43:05
 * 最后更新人：lw
 */
@Controller
@RequestMapping("/manager/vendor/materialRel")
public class VendorMaterialRelController implements ILog{
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private VendorMaterialRelService vendormaterialRelService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorMaterialRelList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody  
	public Map<String,Object> vendorMaterialRelList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorMaterialRelEntity> page = vendormaterialRelService.getVendorMaterialRelList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addNewVendorMaterialRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorMaterialRel(@RequestBody List<VendorMaterialRelEntity> vendorMaterialRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		map = vendormaterialRelService.addNewVendorMaterialRel(vendorMaterialRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorMaterialRel") VendorMaterialRelEntity vendorMaterialRel) {
		map = new HashMap<String, Object>();
		vendormaterialRelService.updateVendorMaterialRel(vendorMaterialRel);
		map.put("success", true);
		return map;
	}
	@RequestMapping(value = "changeStatus/{id}/{flag}", method = RequestMethod.POST)
	@ResponseBody
	public void changeStatus(@PathVariable("id") Long id,@PathVariable("flag") Integer flag){
		VendorMaterialRelEntity vm = vendormaterialRelService.getVendorMaterialRel(id);
		vm.setStatus(flag);
		vendormaterialRelService.updateVendorMaterialRel(vm);
	}
	
	@RequestMapping(value = "deleteVendorMaterialRel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorMaterialRelList(@RequestBody List<VendorMaterialRelEntity> vendorMaterialRelList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendormaterialRelService.deleteVendorMaterialRelList(vendorMaterialRelList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorMaterialRel/{id}")
	@ResponseBody
	public VendorMaterialRelEntity getVendorMaterialRel(@PathVariable("id") Long id){
		return vendormaterialRelService.getVendorMaterialRel(id);
	}
	
	
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) {
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(VendorMaterialRelTransfer.class), getLogger()); 
			List<VendorMaterialRelTransfer> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			map = vendormaterialRelService.combineMaterialSupple(list, getLogger());
		} catch (Exception e) {
			map.put("msg", "导入供货关系失败!");  
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
	 * 供货关系导出
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供货关系", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<VendorMaterialRelTransfer2> list = vendormaterialRelService.getVendorMaterialRelTransfer(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendorMaterialRelTransfer2.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorMaterialRel(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorMaterialRel", vendormaterialRelService.getVendorMaterialRel(id));
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
