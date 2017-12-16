package com.qeweb.scm.vendormodule.web.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;

@Component
public class VendorBaseInfoControllerImpl {
	
	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	
	private Map<String,Object> map;
	
	
	@RequestMapping(value = "vendorPromote")
	@ResponseBody
	public Map<String,Object> vendorPromote(@RequestBody List<VendorBaseInfoEntity> vendorBaseInfoList,String auditMsg,Model model,ServletRequest request) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return vendorBaseInfoService.promteList(vendorBaseInfoList,auditMsg,user.getName());
	}

	/**
	 * 供应商带质量报告晋级
	 * @param vendor 供应商
	 * @param auditMsg 审核信息
	 * @param qsReportFile 晋级报告
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> vendorPromoteWithQsReport(VendorBaseInfoEntity vendor, String auditMsg,
			MultipartFile qsReportFile, Model model, ServletRequest request) throws Exception {
		//做文件上传等处理
		//vendor.setSoId(Long.parseLong(request.getParameter("soff")));
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String qsDir = PropertiesUtil.getProperty("file.upload.vendor.qsreport")+"/"+user.orgCode;
		File file = FileUtil.savefileFix(qsReportFile, qsDir);
		String filePath = file.getAbsolutePath().replaceAll("\\\\", "/");
		vendor.setQsReport(filePath);
		List<VendorBaseInfoEntity> vendorBaseInfoList = new ArrayList<VendorBaseInfoEntity>();
		vendorBaseInfoList.add(vendor);
		Map<String,Object>  map=vendorBaseInfoService.promteList(vendorBaseInfoList,auditMsg,user.getName());
		if((Boolean) map.get("success"))
		{
			vendorBaseInfoService.saveOff(vendor);
		}
		return map;
	}

	

}
