package com.qeweb.scm.contractmodule.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.contractmodule.ContractConstant;
import com.qeweb.scm.contractmodule.entity.ContractEntity;
import com.qeweb.scm.contractmodule.entity.ContractFilesEntity;
import com.qeweb.scm.contractmodule.service.ContractService;

/**
 * 合同Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contract/vendorContract")
public class VendorContractController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 合同Service
	 */
	@Autowired
	private ContractService contractService;
	


	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/contract/vendorContractList";
	}

	
	@RequestMapping(value="getList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//条件 to do
		searchParamMap.put("EQ_publishStatus",ContractConstant.PUBLISH_STATUS_YES);
		searchParamMap.put("EQ_vendor.id",user.orgId);
		searchParamMap.put("EQ_abolished", 0);
		Page<ContractEntity> userPage = contractService.getList(pageNumber,pageSize,searchParamMap);
		for (int i = 0; i < userPage.getContent().size(); i++) {
			int hasSealAttchement=userPage.getContent().get(i).getHasSealAttchement();
			if(ContractConstant.FILE_STATUS_YES==hasSealAttchement){
				ContractFilesEntity file=contractService.findContractSealFile(userPage.getContent().get(i).getId());
				userPage.getContent().get(i).setFileSealName(file.getFileName());
				userPage.getContent().get(i).setFileSealUrl(file.getFilePath());
			}
			
			int confirmStatus = userPage.getContent().get(i).getConfirmStatus();
			if(ContractConstant.CONFIRM_STATUS_PASS == confirmStatus){
				ContractFilesEntity confirmFile=contractService.findContractConfirmFile(userPage.getContent().get(i).getId());
				userPage.getContent().get(i).setFileConfirmName(confirmFile.getFileName());
				userPage.getContent().get(i).setFileConfirmUrl(confirmFile.getFilePath());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	

	
	/**
	 * 跳转合同查看页面
	 * @param contractId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toContractView/{contractId}")
	public String toContractView(@PathVariable("contractId") Long contractId,Model model){
	
		ContractEntity contract=contractService.findOne(contractId);
		model.addAttribute("moduleId",contract.getModuleId());
		if(contract.getHasAttachement()==1){
			ContractFilesEntity file=contractService.findContractFile(contractId);
			contract.setFileName(file.getFileName());
			contract.setFileUrl(file.getFilePath());
			
		}
		contract.setVendorName(contract.getVendor().getName());
		model.addAttribute("contract",contract);
		return "back/contract/vendorContractView";
	}
	
	
	
	/**
	 * 跳转条款页面
	 * @param contractId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toDisplayContent")
	public String toEdit(Long contractId,Model model,ServletRequest request){
		ContractEntity contract=contractService.findOne(contractId);
		model.addAttribute("contract", contract);
		
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("roleType", user.orgRoleType);
		return "back/contract/contractContent";
	}
	
	
	/**
	 * 合同确认
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "confirmContract/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmContract(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.confirmContract(contractId,ContractConstant.CONFIRM_STATUS_PASS);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 合同驳回
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "rejectContract/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> rejectContract(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.confirmContract(contractId,ContractConstant.CONFIRM_STATUS_REJECT);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 盖章附件上传
	 * @param id
	 * @param files
	 * @return
	 */
	@RequestMapping("filesUpload/{id}")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUpload(@PathVariable("id") Long id,@RequestParam("planfiles") MultipartFile files) {
		File savefile = null;
		map = new HashMap<String, Object>();
		try{ 
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				throw new Exception();
			}
			contractService.saveSealFile(id, savefile,savefile==null?"":files.getOriginalFilename());
			map.put("success", true);
	
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		} 
		return map;   
	}
	

	
	

	@Override
	public void log(Object message) {
		
	}
	

	@RequestMapping("downloadSealFile")
	public ModelAndView downloadSealFile(@RequestParam("contractId") Long contractId, HttpServletResponse response) throws Exception {
		ContractFilesEntity files=contractService.findContractSealFile(contractId);
		String filePath=files.getFilePath();
		String fileName=files.getFileName();

		return downFile(response, filePath, fileName);
	}
	
					 
	@RequestMapping("downloadConfirmFile")
	public ModelAndView downloadConfirmFile(@RequestParam("contractId") Long contractId, HttpServletResponse response) throws Exception {
		ContractFilesEntity files=contractService.findContractConfirmFile(contractId);
		String filePath=files.getFilePath();
		String fileName=files.getFileName();

		return downFile(response, filePath, fileName);
	}


	private ModelAndView downFile(HttpServletResponse response,
			String filePath, String fileName)
			throws UnsupportedEncodingException, IOException {
		//下载文件
		response.setContentType("application/octet-stream;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");    
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		if(filePath == null) {
			return null;
		}
		filePath = filePath.replaceAll("\\\\", "/");
		if(filePath.startsWith("WEB-INF")) {
			filePath =  request.getSession().getServletContext().getRealPath("/") + "/" + filePath;  
		}  
		if(StringUtils.isEmpty(fileName)) {
			fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));
		}  
		File file = new File(filePath);
		try {
			long fileLength = new File(file.getAbsolutePath()).length();
			response.setContentType("application/x-msdownload;");   
			response.setHeader("Content-disposition", "attachment;filename="  + (new String(fileName.getBytes("GBK"), "ISO8859-1") + "." + getExtensionName(filePath))) ;  
			response.setHeader("Content-Length", String.valueOf(fileLength));  
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];  
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}
	
	public static String getExtensionName(String filepath) {
		if (filepath != null && filepath.length() > 0 && filepath.lastIndexOf(".") > 0) {
			int dot = filepath.lastIndexOf('.');
			if (dot > -1 && dot < filepath.length() - 1) { 
				return filepath.substring(dot + 1);
			}
		}
		return filepath;
	}
	

}



