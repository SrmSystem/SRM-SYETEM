package com.qeweb.scm.vendormodule.web.manager;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
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

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.service.BuyerVendorRelService;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.vo.VendorAdmittanceTransfer;
import com.qeweb.scm.vendormodule.web.manager.impl.VendorBaseInfoControllerImpl;

@Controller
@RequestMapping("/manager/vendor/vendorBaseInfo")
public class VendorBaseInfoController {
	
	/**
	 * 覆盖实现
	 */
	@Autowired
	private VendorBaseInfoControllerImpl vendorBaseInfoControllerImpl;
	
	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private VendorBaseInfoService vendorBaseInfoServices;
	
	@Autowired
	private BuyerVendorRelService buyerVendorRelService;
	

	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/vendor/vendorBaseInfoList";
	}
	
	/**
	 * 采购商获取每一个阶段的供应商列表
	 * @param pageNumber 页数
	 * @param pageSize 每页数量
	 * @return 某个阶段的供应商列表
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorBaseInfoList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//通过组织权限查找供应商
		searchParamMap.put("IN_orgId", buyerOrgPermissionUtil.getVendorIds());
		Page<VendorBaseInfoEntity> page = vendorBaseInfoServices.getVendorBaseInfoList(pageNumber,pageSize,searchParamMap);
		for (int i= 0 ; i < page.getContent().size(); i++) {
			if(page.getContent().get(i)!=null)
			{
				if(page.getContent().get(i).getOrgId() != null){
					OrganizationEntity buyerVen = buyerVendorRelService.getBuyerByVendor(page.getContent().get(i).getOrgId());
					page.getContent().get(i).setBuyerCode(buyerVen.getCode());
					page.getContent().get(i).setBuyerName(buyerVen.getName());
					
				}
				
				if(page.getContent().get(i).getProperty()!=null)
				{
					if(page.getContent().get(i).getProperty().equals("1"))
					{
						page.getContent().get(i).setProperty("国有企业");
					}
					else if(page.getContent().get(i).getProperty().equals("2"))
					{
						page.getContent().get(i).setProperty("国有控股企业");
					}
					else if(page.getContent().get(i).getProperty().equals("3"))
					{
						page.getContent().get(i).setProperty("外资企业");
					}
					else if(page.getContent().get(i).getProperty().equals("4"))
					{
						page.getContent().get(i).setProperty("合资企业");
					}
					else if(page.getContent().get(i).getProperty().equals("5"))
					{
						page.getContent().get(i).setProperty("私营企业");
					}
				}
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	/**
	 * 采购商获取每一个阶段的供应商列表
	 * @param pageNumber 页数
	 * @param pageSize 每页数量
	 * @return 某个阶段的供应商列表
	 */
	@RequestMapping(value="getVendorList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_currentVersion",StatusConstant.STATUS_YES);
		Page<VendorBaseInfoEntity> page = vendorBaseInfoService.getVendorList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getBuyerList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> buyerList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<OrganizationEntity> page = orgService.getBuyerListByRoleType(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	@LogClass(method="查看", module="供应商阶段管理")
	@RequestMapping("admittance")
	public String admittancePage(Model model,ServletRequest request){
		//查出所有的阶段
		//List<VendorPhaseEntity> phaseList = vendorBaseInfoService.getAllPhase();
		//modify by yao.jin 2017.02.24
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<VendorPhaseEntity> phaseList = vendorBaseInfoService.getAllPhaseByOrgId(user.orgId);
		//end modify
		model.addAttribute("phaseList", phaseList);
		return "back/vendor/vendorAdmittance";
	}
	
	/**
	 * 进入供应商意向确认界面
	 * @return 界面
	 */
	@LogClass(method="查看", module="意向确认")
	@RequestMapping(value = "vendorConfirm",method = RequestMethod.GET)
	public String vendorConfirmListPage(){
		return "back/vendor/vendorConfirmList";
	}
	
	/**
	 * 确认意向拒绝
	 * @param vendorBaseInfoList
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "vendorConfirm",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorConfirm(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList, Model model,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		map = vendorBaseInfoService.confirm(vendorBaseInfoList,basePath, user.orgId);
		
		return map;
	}
	/**
	 * 确认意向拒绝
	 * @param vendorBaseInfoList
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "vendorConfirm2/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorConfirm2(@PathVariable("id") Long id, Model model,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal(); 
		map = vendorBaseInfoService.confirm2(id,basePath, user.orgId);
		
		return map;
	}
	
	/**
	 * 确认意向拒绝
	 * @param vendorBaseInfoList
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirmReject",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vendorConfirmReject(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList, Model model,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		map = vendorBaseInfoService.confirm(vendorBaseInfoList,basePath,user.orgId);
		return map;
	}
	
	/**
	 * 确认意向拒绝
	 * @param vendorBaseInfoList
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirmReject2/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmReject2(@PathVariable("id") Long id,  Model model,HttpServletRequest request){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		map = vendorBaseInfoService.confirm2(id,basePath,user.orgId);
		return map;
	}
	
	@RequestMapping(value = "vendorPromote")
	@ResponseBody
	public Map<String,Object> vendorPromote(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList,String auditMsg,Model model,ServletRequest request) throws Exception{
		return vendorBaseInfoControllerImpl.vendorPromote(vendorBaseInfoList, auditMsg, model, request);
	}
	
	/**
	 * 供应商晋级带质量报告
	 * @param vendorBaseInfoList
	 * @param auditMsg
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "vendorPromoteWithQsReport")
	@ResponseBody
	public Map<String,Object> vendorPromoteWithQsReport(VendorBaseInfoEntity vendor,String auditMsg,MultipartFile qsReportFile,Model model,ServletRequest request) throws Exception{
		return vendorBaseInfoControllerImpl.vendorPromoteWithQsReport(vendor, auditMsg, qsReportFile,model, request);
	}
	
	/**
	 * 供应商降级，降级到指定阶段
	 * @param vendorPhaseId 配置的指定阶段
	 * @param changeReason 降级原因
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "vendorDemotion")
	@ResponseBody
	public Map<String,Object> vendorDemotion(Long vendorPhaseId,String changeReason,Model model,ServletRequest request){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return vendorBaseInfoService.demotion(vendorPhaseId,changeReason,user.getName());
	}
	
	@RequestMapping(value = "addNewVendorBaseInfo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewVendorBaseInfo(@Valid VendorBaseInfoEntity vendorBaseInfo){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorBaseInfoService.addNewVendorBaseInfo(vendorBaseInfo);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("vendorBaseInfo") VendorBaseInfoEntity vendorBaseInfo) {
		map = new HashMap<String, Object>();
		vendorBaseInfoService.updateVendorBaseInfo(vendorBaseInfo);
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value = "deleteVendorBaseInfo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteVendorBaseInfoList(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList){
		Map<String,Object> map = new HashMap<String, Object>();
		vendorBaseInfoService.deleteVendorBaseInfoList(vendorBaseInfoList);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("getVendorBaseInfo/{id}")
	@ResponseBody
	public VendorBaseInfoEntity getVendorBaseInfo(@PathVariable("id") Long id){
		return vendorBaseInfoService.getVendorBaseInfo(id);
	}
	
	@RequestMapping("confirmInfoPage/{id}")
	public String confirmInfoPage(@PathVariable("id") Long id,Model model){
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoService.getVendorBaseInfoEx(id);
		model.addAttribute("vendorBaseInfo", vendorBaseInfo);
		return "back/vendor/component/confirmInfoView";
	}
	
	@RequestMapping("enable")
	@ResponseBody
	public Map<String,Object> enable(Long orgId,Integer enableStatus,Integer changeType,String changeReason,Model model){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = vendorBaseInfoService.enable(orgId,enableStatus,changeType,changeReason,user.getName());
		return map;
	}
	
	@RequestMapping("exportExcel/{phaseId}")
	public String download(@PathVariable("phaseId")Long phaseId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_phaseId", phaseId+"");
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商准入列表", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
	    List<VendorAdmittanceTransfer> list = vendorBaseInfoService.getVendorAdmittanceTransfer(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(VendorAdmittanceTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindVendorBaseInfo(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("vendorBaseInfo", vendorBaseInfoService.getVendorBaseInfo(id));
		}
	}
	
	@RequestMapping(value="/getSturst")
	@ResponseBody
	public String getSturst()
	{
		return vendorBaseInfoService.getSturst("confirm");
	}
	@RequestMapping(value="/getSturst2")
	@ResponseBody
	public String getSturst2()
	{
		return vendorBaseInfoService.getSturst2("enable");
	}
	

}
