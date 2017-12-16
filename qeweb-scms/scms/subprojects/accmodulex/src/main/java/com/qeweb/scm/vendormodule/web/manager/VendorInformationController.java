package com.qeweb.scm.vendormodule.web.manager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorChangeHisEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.service.VendorInformationService;
import com.qeweb.scm.vendormodule.vo.QSATransfer;
import com.qeweb.scm.vendormodule.vo.VendorLevelTransfer;

@Controller
@RequestMapping("/manager/vendor/vendorInfor")
public class VendorInformationController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private VendorInformationService vendorInformationService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="供应商信息管理")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorInfoList";
	}
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorInformationList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", 0);
		//通过组织权限查找供应商
		searchParamMap.put("IN_orgId", buyerOrgPermissionUtil.getVendorIds());
		Page<VendorBaseInfoEntity> page = vendorInformationService.getVendorInfoList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@LogClass(method="查看", module="供应商科室分配")
	@RequestMapping(value="so",method = RequestMethod.GET)
	public String solist(Model model) {
		return "back/vendor/vendorInfoSOList";
	}
	
	@RequestMapping(value="so",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sovendorInformationList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorBaseInfoEntity> page = vendorInformationService.getVendorInfoListSo(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/updateSoff/{vid}/{sid}")
	@ResponseBody
	public  Map<String,Object> updateSoff(@PathVariable("vid") String vid,@PathVariable("sid") Long sid)
	{
		return vendorInformationService.updateSoff(vid,sid);
	}
	@RequestMapping(value="/getVendorPhase")
	@ResponseBody
	public String getVendorPhase()
	{
		return vendorInformationService.getVendorPhase();
	}
	@RequestMapping(value="/vendorInfoRela/{orgId}")
	public String vendorInfoRela(@PathVariable("orgId") Long orgId,HttpServletRequest request,HttpServletResponse response) {
		vendorInformationService.vendorInfoRela(orgId,request,response);
		return "back/vendor/vendorInfoRelationship";
	}

	@RequestMapping(value="/examine/{orgId}/{cfgid}")
	@ResponseBody
	public Map<String,Object> examine(@PathVariable("orgId") Long orgId,@PathVariable("cfgid") Long cfgid,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request,HttpServletRequest httpServletRequest) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorSurveyBaseEntity> page = vendorInformationService.examine(orgId,cfgid,pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/lookGKJJSS/{orgId}")
	@ResponseBody
	public Map<String,Object> lookGKJJSS(@PathVariable("orgId") Long orgId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request,HttpServletRequest httpServletRequest) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorChangeHisEntity> page = vendorInformationService.lookGKJJSS(orgId,pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/vendorInfoCon/{orgId}")
	@ResponseBody
	public Map<String,Object> vendorInfoCon(@PathVariable("orgId") Long orgId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request,HttpServletRequest httpServletRequest) {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorMaterialRelEntity> page = vendorInformationService.vendorInfoCon(orgId,pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	@RequestMapping(value="/getLookgonghuoxishu/{mid}/{orgId}")
	@ResponseBody
	public String getLookgonghuoxishu(@PathVariable("mid") Long mid,@PathVariable("orgId") Long orgId) {
		return vendorInformationService.getLookgonghuoxishu(mid,orgId);
	}
	@RequestMapping(value="/getReport/{orgId}")
	@ResponseBody
	public String getReport(@PathVariable("orgId") Long orgId) {
		return vendorInformationService.getReport2(orgId);
	}
	@RequestMapping(value="/getVendorLevel")
	@ResponseBody
	public String getVendorLevel()
	{
		return vendorInformationService.getVendorLevel();
	}
	@RequestMapping(value="/getVendorClassify2")
	@ResponseBody
	public String getVendorClassify2()
	{
		return vendorInformationService.getVendorClassify2();
	}
	@RequestMapping(value="/getVendorClassify")
	@ResponseBody
	public String getVendorClassify()
	{
		return vendorInformationService.getVendorClassify();
	}
	@RequestMapping(value="/getprovince")
	@ResponseBody
	public String getprovince()
	{
		return vendorInformationService.getprovince();
	}
	@RequestMapping(value="/vendorInfoWeiHuStart/{orgId}")
	public String vendorInfoWeiHuStart(@PathVariable("orgId") Long orgId,HttpServletRequest request,HttpServletResponse response) {
		vendorInformationService.vendorInfoWeiHuStart(orgId,request,response);
		return "back/vendor/vendorInfoWeiHu";
	}
	@RequestMapping(value="/updateVendorClassify")
	@ResponseBody
	public String updateVendorClassify(VendorBaseInfoEntity vendorBaseInfoEntity,HttpServletRequest request,HttpServletResponse response)
	{
		return vendorInformationService.updateVendorClassify(vendorBaseInfoEntity,request,response);
	}
	@RequestMapping(value="/updateVendorQSA")
	@ResponseBody
	public String updateVendorQSA(VendorBaseInfoEntity vendorBaseInfoEntity,HttpServletRequest request,HttpServletResponse response)
	{
		return vendorInformationService.updateVendorQSA(vendorBaseInfoEntity,request,response);
	}
	@RequestMapping(value="/vendorInfoWeiHuStart2/{orgId}")
	public String vendorInfoWeiHuStart2(@PathVariable("orgId") Long orgId,HttpServletRequest request,HttpServletResponse response) {
		vendorInformationService.vendorInfoWeiHuStart2(orgId,request,response);
		return "back/vendor/vendorInfoWeiHu2";
	}
	
	@RequestMapping("filesUpload")
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(VendorLevelTransfer.class), getLogger()); 
			List<VendorLevelTransfer> list = (List<VendorLevelTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			String flag = vendorInformationService.combineVendorLevel(list);
			if(flag.equals("1")) {
				map.put("msg", "批量更新分类/等级成功!");
				map.put("success", true);
			} else {
				map.put("msg", "批量更新分类/等级失败!");
				map.put("success", false);
				log(flag);
			}
		} catch (Exception e) {
			map.put("msg", "批量更新分类/等级失败!");  
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
	@RequestMapping("filesUpload2")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	public Map<String,Object> filesUpload2(@RequestParam("planfiles") MultipartFile files) {
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(QSATransfer.class), getLogger()); 
			List<QSATransfer> list = (List<QSATransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			String flag = vendorInformationService.combineQSA(list);
			if(flag.equals("1")) {
				map.put("msg", "批量质量体系审核结果和评优结果成功!");
				map.put("success", true);
			} else {
				map.put("msg", "批量质量体系审核结果和评优结果失败!");
				map.put("success", false);
				log(flag);
			}
		} catch (Exception e) {
			map.put("msg", "批量质量体系审核结果和评优结果失败!");  
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
	@RequestMapping("filesUpload3/{vendid}")
	@ResponseBody
	public Map<String,Object> filesUpload3(@PathVariable("vendid") long vendid,@RequestParam("planfiles") MultipartFile files) {
		File savefile = null;
		String logpath = null;
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			// 获取文件的真实名称
			String fileName = files.getOriginalFilename();
			// 文件的后缀
			String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			
			savefile = FileUtil.savefile2(files, request.getSession().getServletContext().getRealPath("/") + "/static/report/",fileType);
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			  String saveUrl  = request.getContextPath() + "/static/report/";
	          boolean flag =  vendorInformationService.filesUpload3(vendid,saveUrl+"/"+savefile.getName(),fileName);
	          if(flag) {
					map.put("msg", "成功!");
					map.put("success", true);
				} else {
					map.put("msg", "失败!");
					map.put("success", false);
				}
		} catch (Exception e) {
			map.put("msg", "失败!");  
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
	@RequestMapping("getCountryProvince/{id}")
	public void getCountryProvince(@PathVariable("id") String id,HttpServletResponse response)
	{
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(vendorInformationService.getCountryProvince(id));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)
			{
				out.flush();
				out.close();
			}
		}
		
	}
	@RequestMapping("/download/{id}")
	public String download(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商信息", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		vendorInformationService.download(id,response);
		return null;   
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
