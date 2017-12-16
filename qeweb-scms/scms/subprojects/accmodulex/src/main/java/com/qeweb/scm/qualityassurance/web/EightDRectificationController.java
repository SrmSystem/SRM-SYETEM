package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.qualityassurance.entity.EightDRectification;
import com.qeweb.scm.qualityassurance.entity.EightDReport;
import com.qeweb.scm.qualityassurance.entity.EightDReportDetail;
import com.qeweb.scm.qualityassurance.service.EightDService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manager/qualityassurance/eightD")
public class EightDRectificationController  implements ILog {

	private Map<String,Object> map;
	
	private ILogger logger=new FileLogger();
	
//	@Autowired
//	private HttpServletRequest request;
	@Autowired
	private EightDService eightDService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/eightD/eightDList";                 
	}
	
	@RequestMapping(value="vendor",method = RequestMethod.GET)
	public String vendorList(Model model) {
		model.addAttribute("vendor", true);
		return "back/eightD/vendorEightDList";                 
	}
	
	@RequestMapping(value="/{vendor}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@PathVariable(value="vendor") boolean vendor,@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap = CommonUtil.initSearchTime(searchParamMap, "GT_happenTime", 0);//开始时间
		searchParamMap = CommonUtil.initSearchTime(searchParamMap, "LT_HappenTime", 1);//结束时间
		if(vendor) {		
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + ""); 
			searchParamMap.put("EQ_publishStatus", 1); 
		}
		Page<EightDRectification> userPage = eightDService.getEightDList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "addEightD",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addEightD(HttpServletRequest request, @RequestParam("fujian") MultipartFile files, @Valid EightDRectification rectification){
		Map<String,Object> map = new HashMap<String, Object>();
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			String filePath = "";
			String fileName = "";
			if(files != null && StringUtils.isNotEmpty(files.getOriginalFilename())){
				savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
				if(savefile == null || savefile.length() == 0) {
					log("->上传文件为空，上传失败");
					throw new Exception();
				}else{
					filePath = savefile.getPath();
					fileName = files.getOriginalFilename().substring(0, files.getOriginalFilename().indexOf("."));
				}
				//2、组装并保存数据
			}
				map = eightDService.fileUpLoad(filePath,fileName,rectification);
			
		} catch (Exception e) {
			map.put("message", "上传附件失败!");  
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
	
	@RequestMapping(value = "updateEightD",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateEightD(HttpServletRequest request, @Valid EightDRectification rectification,  @RequestParam("fujian") MultipartFile files, @RequestParam("mid") long mid){
		EightDRectification rec = eightDService.getRectification(mid);
		Map<String,Object> map = new HashMap<String, Object>();
		if (rectification.getHappenTime() != null){
			rec.setHappenTime(rectification.getHappenTime());
		}
		if (rectification.getHappenPlace() != null){
			rec.setHappenPlace(rectification.getHappenPlace());
		}
		if (rectification.getMalfunctionQty() != null){
			rec.setMalfunctionQty(rectification.getMalfunctionQty());
		}
		if (rectification.getVendor() != null){
			rec.setVendor(rectification.getVendor());
		}
		if (rectification.getMaterial() != null){
			rec.setMaterial(rectification.getMaterial());
		}
		if (rectification.getRecDescription() != null){
			rec.setRecDescription(rectification.getRecDescription());
		}
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile != null && savefile.length() != 0) {
				String originalName = files.getOriginalFilename();
				String fileName = originalName.substring(0, originalName.lastIndexOf("."));
				rec.setAttachmentName(fileName);
				rec.setAttachmentPath(savefile.getPath().replaceAll("\\\\", "/"));
			}
		} catch (Exception e) {
			map.put("message", "上传附件失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));    
		}
		eightDService.saveEightD(rec);
		map.put("success", true);
		map.put("message", "修改8D整改成功！");
		return map;
	}
	
	@RequestMapping(value = "uploadReprove",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadReprove(HttpServletRequest request,  @RequestParam("fujian") MultipartFile files, @RequestParam("mid") long mid){
		EightDRectification rec = eightDService.getRectification(mid);
		Map<String,Object> map = new HashMap<String, Object>();
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile != null && savefile.length() != 0) {
				String originalName = files.getOriginalFilename();
				String fileName = originalName.substring(0, originalName.lastIndexOf("."));
				rec.setReproveAttachmentName(fileName);
				rec.setReproveAttachmentPath(savefile.getPath().replaceAll("\\\\", "/"));
			}
		} catch (Exception e) {
			map.put("message", "上传附件失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			getLogger().destory();  
			map.put("name", StringUtils.encode(new File(logpath).getName()));    
			map.put("log", StringUtils.encode(logpath));    
		}
		rec.setReproveStatus(1);
		eightDService.saveEightD(rec);
		map.put("success", true);
		map.put("message", "上传8D整改附件成功！");
		return map;
	}
	
	@RequestMapping(value = "approve",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> approve(@Valid EightDRectification rectification){
		Map<String,Object> map = new HashMap<String, Object>();
		EightDRectification eightD = eightDService.getRectification(rectification.getId());
		eightD.setStatus(1);  // 已审核
		eightD.setApproveAdvice(rectification.getApproveAdvice());
		eightDService.saveEightD(eightD);
		map.put("success", true);
		map.put("message", "审核8D整改成功！");
		return map;
	}
	@RequestMapping(value = "reject",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> reject(@Valid EightDRectification rectification){
		Map<String,Object> map = new HashMap<String, Object>();
		EightDRectification eightD = eightDService.getRectification(rectification.getId());
		eightD.setStatus(1);  
		eightD.setReproveStatus(0);
		eightD.setApproveAdvice(rectification.getApproveAdvice());
		eightDService.saveEightD(eightD);
		map.put("success", true);
		map.put("message", "审核8D整改不通过！");
		return map;
	}
	@RequestMapping("publishEightD/{id}")
	@ResponseBody
	public Map<String,Object> publishEightD(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		EightDRectification rectification = eightDService.getRectification(id);
		rectification.setPublishStatus(1);  // 已发布
		eightDService.saveEightD(rectification);
		map.put("success", true);
		map.put("message", "发布8D整改成功！");
		return map;
	}
	
	@RequestMapping("submitReprove/{id}")
	@ResponseBody
	public Map<String,Object> submitReprove(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		EightDRectification rectification = eightDService.getRectification(id);
		rectification.setReproveStatus(1);  // 已整改
		eightDService.saveEightD(rectification);
		map.put("success", true);
		map.put("message", "提交8D整改成功！");
		return map;
	}
	
	@RequestMapping("deleteEightD/{id}")
	@ResponseBody
	public Map<String,Object> deleteEightD(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		eightDService.deleteEightD(id);
		map.put("success", true);
		map.put("message", "删除8D整改成功！");
		return map;
	}
	
	@RequestMapping("getRectification/{id}")
	@ResponseBody
	public EightDRectification getRectification(@PathVariable("id") Long id){
		EightDRectification entity = eightDService.getRectification(id);   
		return entity;       
	}
	
	@RequestMapping("getReport/{id}")
	@ResponseBody
	public EightDReport getReport(@PathVariable("id") Long id){
		EightDReport report = eightDService.getReport(id);
		return report;       
	}
	
	@RequestMapping("getDetails/{id}/{type}")
	@ResponseBody
	public List<EightDReportDetail> getDetails(@PathVariable("id") Long id,@PathVariable("type") int type){
		List<EightDReportDetail> details = eightDService.getDetails(id,type);
		if (type == 0){
			if (details != null && details.size() > 0){
				for (EightDReportDetail e:details){
//					e.setMaterialCode(e.getMaterial().getCode());
//					e.setMaterialName(e.getMaterial().getName());
					e.getMaterial().setC22(e.getMaterial().getCode()+"/"+e.getMaterial().getName());
				}
			}
		}
		return details;       
	}
	
	/**
	 * 保存问题说明
	 * @param id
	 * @param report
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save1/{id}")
	@ResponseBody
	public Map<String, Object> save1(@PathVariable("id") Long id,@Valid EightDReport report) throws Exception{//
		map = new HashMap<String, Object>();
		EightDReport eightDReport = eightDService.getReport(id);
		if(eightDReport != null){
			eightDReport.setRepHappenPlace(report.getRepHappenPlace());
			eightDReport.setRepMalfunctionQty(report.getRepMalfunctionQty());
			eightDReport.setAffectedBatchQty(report.getAffectedBatchQty());
			eightDReport.setReportDescription(report.getReportDescription());
		}else{
			eightDReport = report;
			EightDRectification rectification = new EightDRectification();
			rectification.setId(id);
			eightDReport.setRectification(rectification);
		}
		eightDService.saveEightDReport(eightDReport);
		map.put("success", true);
		map.put("message", "保存成功！");
		return map;
	}
	
	/**
	 * 保存向类似零件展开
	 * @param id
	 * @param detailDatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save2/{id}")
	@ResponseBody
	public Map<String, Object> save2(@PathVariable("id") Long id,@RequestParam(value="datas") String detailDatas) throws Exception{//
		map = new HashMap<String, Object>();
		List<EightDReportDetail> reportDetailList = new ArrayList<EightDReportDetail>();
		JSONObject object = JSONObject.fromObject(detailDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EightDReportDetail detail = null;
		MaterialEntity material = null;
		EightDRectification rectification = null;
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			detail = new EightDReportDetail();
			detail.setReportType(0);   // 新增向类似零件展开
			rectification = new EightDRectification();
			rectification.setId(id);
			detail.setRectification(rectification);
			if(object.get("id") != "" && object.get("id") != null){
				detail.setId(StringUtils.convertToLong(object.get("id") + ""));
			}
			material=new MaterialEntity();
			material.setId(StringUtils.convertToLong(object.get("material") + ""));
			detail.setMaterial(material);
			detail.setSimIsProblem(StringUtils.convertToString(object.get("simIsProblem")));
			detail.setSimRemark(StringUtils.convertToString(object.get("simRemark")));
			
			reportDetailList.add(detail);
		}
		eightDService.addReportDetails(reportDetailList);
		map.put("message", "保存向类似零件展开成功");
		map.put("success",true);
		return map; 
	}
	
	/**
	 * 保存临时围堵对策-立即的
	 * @param id
	 * @param specialMark
	 * @param detailDatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save3/{id}")
	@ResponseBody
	public Map<String, Object> save3(@PathVariable("id") Long id,@RequestParam(value="specialMark") String specialMark,
			@RequestParam(value="datas") String detailDatas) throws Exception{//
		map = new HashMap<String, Object>();
		List<EightDReportDetail> reportDetailList = new ArrayList<EightDReportDetail>();
		JSONObject object = JSONObject.fromObject(detailDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EightDReportDetail detail = null;
		EightDRectification rectification = eightDService.getRectification(id);
		EightDReport eightDReport = eightDService.getReport(id);
		if(eightDReport == null){
			eightDReport = new EightDReport();
			eightDReport.setRectification(rectification);
		}
		eightDReport.setSpecialMark(specialMark);
		eightDService.saveEightDReport(eightDReport);
		
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			detail = new EightDReportDetail();
			detail.setReportType(1);   // 新增临时围堵政策--立即的
			rectification = new EightDRectification();
			rectification.setId(id);
			detail.setRectification(rectification);
			if(object.get("id") != "" && object.get("id") != null){
				detail.setId(StringUtils.convertToLong(object.get("id") + ""));
			}
			detail.setTemConsiderMatter(StringUtils.convertToString(object.get("temConsiderMatter")));
			detail.setTemQualifiedQty(StringUtils.convertToDouble(object.get("temQualifiedQty")+""));
			detail.setTemUnqualifiedQty(StringUtils.convertToDouble(object.get("temUnqualifiedQty")+""));
			detail.setTemHandleWay(StringUtils.convertToString(object.get("temHandleWay")));
			
			reportDetailList.add(detail);
		}
		eightDService.addReportDetails(reportDetailList);
		map.put("message", "保存临时围堵对策-立即的成功");
		map.put("success",true);
		return map; 
	}
	
	/**
	 * 保存原因分析
	 * @param id
	 * @param report
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save4/{id}")
	@ResponseBody
	public Map<String, Object> save4(@PathVariable("id") Long id,@Valid EightDReport report) throws Exception{//
		map = new HashMap<String, Object>();
		EightDReport eightDReport = eightDService.getReport(id);
		if(eightDReport != null){
			eightDReport.setReason(report.getReason());
		}else{
			eightDReport = report;
			EightDRectification rectification = new EightDRectification();
			rectification.setId(id);
			eightDReport.setRectification(rectification);
		}
		eightDService.saveEightDReport(eightDReport);
		map.put("success", true);
		map.put("message", "保存成功！");
		return map;
	}
	
	/**
	 * 保存根本原因确认
	 * @param id
	 * @param detailDatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save5/{id}")
	@ResponseBody
	public Map<String, Object> save5(@PathVariable("id") Long id,@RequestParam(value="datas") String detailDatas) throws Exception{//
		map = new HashMap<String, Object>();
		List<EightDReportDetail> reportDetailList = new ArrayList<EightDReportDetail>();
		JSONObject object = JSONObject.fromObject(detailDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EightDReportDetail detail = null;
		
		EightDRectification rectification = null;
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			detail = new EightDReportDetail();
			detail.setReportType(2);   // 新增根本原因确认
			rectification = new EightDRectification();
			rectification.setId(id);
			detail.setRectification(rectification);
			if(object.get("id") != "" && object.get("id") != null){
				detail.setId(StringUtils.convertToLong(object.get("id") + ""));
			}
			detail.setReaCategory(StringUtils.convertToString(object.get("reaCategory")));
			detail.setReaIsProblem(StringUtils.convertToString(object.get("reaIsProblem")));
			detail.setReaRootCause(StringUtils.convertToString(object.get("reaRootCause")));
			detail.setReaConfirmedWay(StringUtils.convertToString(object.get("reaConfirmedWay")));
			String finishTime = StringUtils.convertToString(object.get("reaFinishTime"));
			detail.setReaFinishTime(Timestamp.valueOf(finishTime));
			reportDetailList.add(detail);
		}
		eightDService.addReportDetails(reportDetailList);
		map.put("message", "保存根本原因确认成功");
		map.put("success",true);
		return map; 
	}
	
	/**
	 * 保存制定永久措施并实施
	 * @param id
	 * @param detailDatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save6/{id}")
	@ResponseBody
	public Map<String, Object> save6(@PathVariable("id") Long id,@RequestParam(value="datas") String detailDatas) throws Exception{//
		map = new HashMap<String, Object>();
		List<EightDReportDetail> reportDetailList = new ArrayList<EightDReportDetail>();
		JSONObject object = JSONObject.fromObject(detailDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EightDReportDetail detail = null;
		
		EightDRectification rectification = null;
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			detail = new EightDReportDetail();
			detail.setReportType(3);   // 新增制定永久措施并实施
			rectification = new EightDRectification();
			rectification.setId(id);
			detail.setRectification(rectification);
			if(object.get("id") != "" && object.get("id") != null){
				detail.setId(StringUtils.convertToLong(object.get("id") + ""));
			}
			detail.setMakNumber(StringUtils.convertToString(object.get("makNumber")));
			detail.setMakRootCause(StringUtils.convertToString(object.get("makRootCause")));
			detail.setMakEvidence(StringUtils.convertToString(object.get("makEvidence")));
			detail.setMakDutyDept(StringUtils.convertToString(object.get("makDutyDept")));
			String finishTime = StringUtils.convertToString(object.get("makFinishTime"));
			detail.setMakFinishTime(Timestamp.valueOf(finishTime));
			reportDetailList.add(detail);
		}
		eightDService.addReportDetails(reportDetailList);
		map.put("message", "保存制定永久措施并实施成功");
		map.put("success",true);
		return map; 
	}
	
	/**
	 * 保存永久措施效果验证
	 * @param id
	 * @param report
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save7/{id}")
	@ResponseBody
	public Map<String, Object> save7(@PathVariable("id") Long id,@Valid EightDReport report) throws Exception{//
		map = new HashMap<String, Object>();
		EightDReport eightDReport = eightDService.getReport(id);
		if(eightDReport != null){
			eightDReport.setMeasureVerification(report.getMeasureVerification());
		}else{
			eightDReport = report;
			EightDRectification rectification = new EightDRectification();
			rectification.setId(id);
			eightDReport.setRectification(rectification);
		}
		eightDService.saveEightDReport(eightDReport);
		map.put("success", true);
		map.put("message", "保存成功！");
		return map;
	}
	
	/**
	 * 保存文件固化
	 * @param id
	 * @param detailDatas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save8/{id}")
	@ResponseBody
	public Map<String, Object> save8(@PathVariable("id") Long id,@RequestParam(value="datas") String detailDatas) throws Exception{//
		map = new HashMap<String, Object>();
		List<EightDReportDetail> reportDetailList = new ArrayList<EightDReportDetail>();
		JSONObject object = JSONObject.fromObject(detailDatas);     
		JSONArray array = (JSONArray) object.get("rows");
		EightDReportDetail detail = null;
		
		EightDRectification rectification = null;
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			detail = new EightDReportDetail();
			detail.setReportType(4);   // 新增文件固化
			rectification = new EightDRectification();
			rectification.setId(id);
			detail.setRectification(rectification);
			if(object.get("id") != "" && object.get("id") != null){
				detail.setId(StringUtils.convertToLong(object.get("id") + ""));
			}
			detail.setFilName(StringUtils.convertToString(object.get("filName")));
			detail.setFilYes(StringUtils.convertToString(object.get("filYes")));
			detail.setFilDept(StringUtils.convertToString(object.get("filDept")));
			detail.setFilEvidence(StringUtils.convertToString(object.get("filEvidence")));
			String filTime = StringUtils.convertToString(object.get("filTime"));
			detail.setFilTime(Timestamp.valueOf(filTime));
			reportDetailList.add(detail);
		}
		eightDService.addReportDetails(reportDetailList);
		map.put("message", "保存文件固化成功");
		map.put("success",true);
		return map; 
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
