package com.qeweb.scm.vendorperformancemodule.web.manager;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Lists;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.vendormodule.vo.VendorExampleTransfer;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalHisEntity;
import com.qeweb.scm.vendorperformancemodule.service.VendorPerforScoresService;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforScoresTotalVo;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforScoresTotalVo2;

@Controller
@RequestMapping("/manager/vendor/vendorPerforScores")
public class VendorPerforScoresController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@Autowired
	private VendorPerforScoresService service;
	
	@RequiresPermissions("perfor:process:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/performance/scores/vendorPerforScoresList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforScoresEntity> page = service.getVendorPerforScoresList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="vendorList",method = RequestMethod.GET)
	public ModelAndView vendorList(Model model) {
		ModelAndView mv = new ModelAndView("back/performance/scores/vendor/list");
		Map<String,Object> map = service.getScoresColumsAll();
		mv.addAllObjects(map);
		return mv;
	}
	
	@RequestMapping(value="vendorList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orgId", user.orgId);
		searchParamMap.put("EQ_publishStatus", StatusConstant.STATUS_YES);
		Page<VendorPerforScoresTotalEntity> page = service.getVendorScoresTotalList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="totalList",method = RequestMethod.GET)
	public ModelAndView totalList(Model model) {
		ModelAndView mv = new ModelAndView("back/performance/scores/totalList");
		Map<String,Object> map = service.getScoresColumsAll();
		mv.addAllObjects(map);
		return mv;
	}
	
	@RequestMapping(value="sjtx/{type}")
	@ResponseBody
	public Map<String,Object> sjtx(@PathVariable("type") String type) {
		Map<String,Object> map = service.sjtx(type);
		return map;
	}
	
	@RequestMapping(value="totalList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> totalList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//searchParamMap.put("EQ_orgId", user.orgId);
		//searchParamMap.put("EQ_publishStatus", StatusConstant.STATUS_YES);
		Page<VendorPerforScoresTotalEntity> page = service.getVendorScoresTotalList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 对账明细导出
	 * @param id			对账单ID
	 * @param request		浏览器请求
	 * @param response		服务器响应
	 * @return				对账明细集合
	 * @throws Exception	异常
	 */
	@LogClass(method="导出评估结果",module="评估结果")
	@RequestMapping(value="exportTotalList/{vendor}/{id}")
	public String exportTotalList(@PathVariable(value="vendor") boolean vendor, @PathVariable(value="id") long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.log("method exportTotalList start");
		VendorPerforScoresTotalEntity total = service.getScoresTotalById(id);
		StringBuffer fileBuff = new StringBuffer();
		fileBuff.append("评估结果");
		String fileName = fileBuff.toString();				//下载文件名
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		service.exportTotalList(total, response.getOutputStream());
		
//		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
////		List<VendorPerforScoresTotalEntity> list = service.getVendorScoresTotalList(searchParamMap);
//		if(list != null && list.size() != 0){
//			logger.log("list == " + list.size());
//			for (VendorPerforScoresTotalEntity total : list) {
//				StringBuffer fileBuff =new StringBuffer();
//				fileBuff.append("评估结果");
//				String fileName = fileBuff.toString();				//下载文件名
//				response.setContentType("octets/stream");
//				response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
//				service.exportTotalList(total, response.getOutputStream());
//			}
//		}
		logger.log("method exportTotalList end");
		return null;   
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/exportExcel")
	public String download(HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商评估结果", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<VendorPerforScoresTotalVo2> list = service.getVendorPerforScoresTotalVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendorPerforScoresTotalVo2.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	
	/**
	 * 初始化该时间段内需要评估的列表
	 * @return 初始化结果
	 * @throws Exception 
	 */
	@RequiresPermissions("perfor:process:ini")
	@RequestMapping("initList")
	@ResponseBody
	public Map<String,Object> initList(Model model,ServletRequest request) throws Exception{
		map = service.initList();
		return map;
	}
	
	/**
	 * 删除列表
	 * @param id
	 */
	@RequiresPermissions("perfor:process:del")
	@RequestMapping("delete")
	@ResponseBody
	public Map<String,Object> delete(Long id,Model model,ServletRequest request) throws Exception{
		map = service.delete(id);
		return map;
	}
	
	/**
	 * 发布列表
	 * @param id
	 */
	@RequestMapping("publishList")
	@ResponseBody
	public Map<String,Object> publishList(@RequestParam(value="ids[]")List<Long> ids,Model model,ServletRequest request) throws Exception{
		map = service.publish(ids);
		return map;
	}
	
	/**
	 * 发布总分
	 * @param ids
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("perfor:score:rel")
	@RequestMapping("publishTotalList")
	@ResponseBody
	public Map<String,Object> publishTotalList(@RequestParam(value="ids[]")List<Long> ids,Model model,ServletRequest request) throws Exception{
		map = service.publishTotal(ids);
		return map;
	}
	
	/**
	 * 提交整改
	 * @param correntIds 整改结果ID
	 * @param requireDate 要求整改时间
	 * @param correctionContent 整改要求
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("perfor:score:sub")
	@RequestMapping("correctionList")
	@ResponseBody
	public Map<String,Object> correctionList(@RequestParam("planfiles") MultipartFile planfiles,@RequestParam(value="correntIds") String correntIds,
			@RequestParam(value="requireDate") String requireDate,
			@RequestParam(value="correctionContent") String correctionContent,
			Model model) throws Exception{
		File savefile = null;
		map = new HashMap<String, Object>();
		try{ 
			String url="";
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(planfiles, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，上传失败");
				url="";
			}
			else
			{
				url=savefile.getPath().replaceAll("\\\\", "/");
			}
			//2、组装并保存数据			
			boolean flag = service.correction(url,correntIds, requireDate, correctionContent);;
			log("->上传文件执行成功"+url);
			if(flag) {
				map.put("msg", "提交成功!");
				map.put("success", true);
			} else {
				map.put("msg", "提交失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "提交失败!");  
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 取消发布列表
	 * @param id
	 */
	@RequestMapping("unPublishList")
	@ResponseBody
	public Map<String,Object> unPublishList(@RequestParam(value="ids[]")List<Long> ids,Model model,ServletRequest request) throws Exception{
		map = service.unPublish(Lists.newArrayList(ids));
		return map;
	}
	
	/**
	 * 计算指标-评估列表
	 * @param id 评估列表ID
	 * @throws Exception 
	 */
	@RequiresPermissions("perfor:process:index")
	@RequestMapping("countIndex")
	@ResponseBody
	public Map<String,Object> countIndex(Long id,Model model,ServletRequest request) throws Exception{
		map = service.countIndex(id);
		return map;
	}
	
	/**
	 * 计算维度-评估列表
	 * @param id 评估列表ID
	 * @throws Exception 
	 */
	@RequiresPermissions("perfor:process:dimen")
	@RequestMapping("countDim")
	@ResponseBody
	public Map<String,Object> countDim(Long id,Model model,ServletRequest request) throws Exception{
		map = service.countDim(id);
		return map;
	}
	
	/**
	 * 计算总得分-评估列表
	 * @param id 评估列表ID
	 * @throws Exception 
	 */
	@RequestMapping("countTotal")
	@ResponseBody
	public Map<String,Object> countTotalScore(Long id,Model model,ServletRequest request) throws Exception{
		map = service.countTotalScore(id);
		return map;
	}
	
	@RequestMapping("toViewScore")
	public ModelAndView toViewScore(Long id,Model model,ServletRequest request){
		ModelAndView mv = new ModelAndView("back/performance/scores/vendorPerforScores");
		Map<String,Object> map = service.getScoresColums(id);
		mv.addAllObjects(map);
		return mv;
	}
	
	@RequestMapping(value="viewScore",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> viewScore(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforScoresTotalEntity> page = service.getVendorPerforScoresTotalList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 
	 * @param scoreId 评估记录ID
	 * @param id 调分记录ID
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toViewScoreHis/{scoreId}/{id}")
	public ModelAndView toViewScoreHis(@PathVariable("scoreId") Long scoreId, @PathVariable("id") Long id, Model model,ServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("back/performance/scores/vendorPerforScoresHis");
		Map<String,Object> map = service.getScoresColums(scoreId);
		VendorPerforScoresTotalEntity entity = service.getScoresTotalById(id);
		mv.addAllObjects(map);
		mv.addObject("orgId", entity.getOrgId());
		mv.addObject("brandId", entity.getBrandId());
		return mv;
	}
	
	/**
	 * 调分历史查看
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewScoreHis",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> viewScoreHis(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<VendorPerforScoresTotalHisEntity> page = service.getVendorScoresTotalHisList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="adjustScore",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> adjustScore(VendorPerforScoresTotalEntity total,
			Model model) throws Exception{
		Map<String,Object> map = service.adjustScore(total);
		return map;
	}
	
	@RequiresPermissions(value={"perfor:score:seecost","perfor:score:seequality","perfor:score:seegoods"},logical=Logical.OR)
	@RequestMapping(value="getIndexScore",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getIndexScore(Long id,String mapping,
			Model model) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorPerforScoresIndexEntity> indexList = service.getIndexScore(id,mapping);
		map.put("rows",indexList);
		map.put("total",indexList.size());
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
