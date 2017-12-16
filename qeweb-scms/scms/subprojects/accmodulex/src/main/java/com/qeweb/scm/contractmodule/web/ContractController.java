package com.qeweb.scm.contractmodule.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.contractmodule.ContractConstant;
import com.qeweb.scm.contractmodule.entity.ContractEntity;
import com.qeweb.scm.contractmodule.entity.ContractFilesEntity;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;
import com.qeweb.scm.contractmodule.service.ContractModuleService;
import com.qeweb.scm.contractmodule.service.ContractService;

/**
 * 合同Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contract/contract")
public class ContractController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 合同Service
	 */
	@Autowired
	private ContractService contractService;
	
	/**
	 * 合同模板Service
	 */
	@Autowired
	private ContractModuleService moduleService;
	
	
	/**
	 * 生成流水号Service
	 */
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/contract/contractList";
	}

	
	@RequestMapping(value="getList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//组织权限
		searchParamMap.put("IN_buyer.id", buyerOrgPermissionUtil.getBuyerIds());
		//用户权限
		searchParamMap.put("IN_createUserId", buyerOrgPermissionUtil.getUserIds());
		searchParamMap.put("EQ_abolished", 0);
		
		Page<ContractEntity> userPage = contractService.getList(pageNumber,pageSize,searchParamMap);
		Timestamp now=DateUtil.getCurrentTimestamp();
		
		for (int i = 0; i < userPage.getContent().size(); i++) {
			int hasSealAttchement=userPage.getContent().get(i).getHasSealAttchement();
			if(ContractConstant.FILE_STATUS_YES==hasSealAttchement){
				ContractFilesEntity file=contractService.findContractSealFile(userPage.getContent().get(i).getId());
				ContractFilesEntity confirmFile=contractService.findContractConfirmFile(userPage.getContent().get(i).getId());
				
				userPage.getContent().get(i).setFileSealName(file.getFileName());
				userPage.getContent().get(i).setFileSealUrl(file.getFilePath());
				userPage.getContent().get(i).setFileConfirmName(confirmFile.getFileName());
				userPage.getContent().get(i).setFileConfirmUrl(confirmFile.getFilePath());
			}
			Timestamp t=userPage.getContent().get(i).getEffrctiveDateEnd();
				if(t.getTime()<=now.getTime()){
					userPage.getContent().get(i).setIsDisable("1");
				}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 选择模板列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getModuleList/{contractType}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getModuleList(@PathVariable("contractType") Long contractType,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if(contractType==ContractConstant.CONTRACT_TYPE_YEAR){
			searchParamMap.put("EQ_contractType", 1L);
		}else{
			searchParamMap.put("EQ_contractType", 0L);
		}
		Page<ContractModuleEntity> userPage = moduleService.getList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 选择物料列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getMaterialList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaterialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
//		if(buyerOrgPermissionUtil.getBuyerIds()!=null){
//			searchParamMap.put("IN_buyerId", buyerOrgPermissionUtil.getBuyerIds());
//		}else{
//			searchParamMap.put("EQ_id", -1);
//		}
		Page<MaterialEntity> userPage = materialService.getMaterialList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 跳转合同新增页面
	 * @param moduleId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toContractAdd/{param}")
	public String toContractAdd(@PathVariable("param") String param,Model model){
		String[] paramList=param.split(",");
		Long moduleId =Long.valueOf(paramList[0]);
		Long contractType=Long.valueOf(paramList[1]);
		 
		model.addAttribute("moduleId",moduleId);
		ContractEntity contract=createContractCode();
		contract.setContractType(contractType);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		contract.setApplyUser(user.id);
		contract.setApplyUserName(user.name);
		contract.setSignUser(user.name);
		contract.setSignDateStr(DateUtil.dateToString(DateUtil.getCurrentTimestamp(), "yyyy-MM-dd"));
		model.addAttribute("contract",contract);
		return "back/contract/contractAddOrEdit";
	}
	
	/**
	 * 跳转合同编辑页面
	 * @param contractId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toContractEdit/{contractId}")
	public String toContractEdit(@PathVariable("contractId") Long contractId,Model model){
	
		ContractEntity contract=contractService.findOne(contractId);
		model.addAttribute("moduleId",contract.getModuleId());
		if(contract.getHasAttachement()==1){
			ContractFilesEntity file=contractService.findContractFile(contractId);
			contract.setFileName(file.getFileName());
			contract.setFileUrl(file.getFilePath());
			
		}
		contract.setVendorName(contract.getVendor().getName());
		model.addAttribute("contract",contract);
		return "back/contract/contractAddOrEdit";
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
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("roleType", user.orgRoleType);
		return "back/contract/contractView";
	}
	
	@RequestMapping(value="toVendorContractView/{contractId}")
	public String toVendorContractView(@PathVariable("contractId") Long contractId,Model model){
	
		ContractEntity contract=contractService.findOne(contractId);
		model.addAttribute("moduleId",contract.getModuleId());
		if(contract.getHasAttachement()==1){
			ContractFilesEntity file=contractService.findContractFile(contractId);
			contract.setFileName(file.getFileName());
			contract.setFileUrl(file.getFilePath());
			
		}
		contract.setVendorName(contract.getVendor().getName());
		model.addAttribute("contract",contract);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("roleType", user.orgRoleType);
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
	 * 合同提交审核
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "checkContract/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkContract(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();

		
		try {
			String res= contractService.checkContract(contractId);
			if(res==""){
				map.put("success", true);
				map.put("msg", "提交审核成功");
				return map;
			}else{
				map.put("success", false);
				map.put("msg", res);
				return map;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		map.put("success", true);
		map.put("msg", "提交审核成功");
		return map;
	}
	
	/**
	 * 合同发布
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "publishContract/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishContract(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.publishContract(contractId);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 合同确认生效
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "effectivePass/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> effectivePass(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.effectiveContract(contractId, ContractConstant.FILE_CONFIRM_STATUS_PASS);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 盖章附件退回
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "effectiveReject/{contractId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> effectiveReject(@PathVariable("contractId") Long contractId){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.effectiveContract(contractId, ContractConstant.FILE_CONFIRM_STATUS_REJECT);
		map.put("success", true);
		return map;
	}
	

	
	/**
	 * 合同保存
	 * @param moduleId
	 * @param contract
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveContract/{moduleId}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveContract(@PathVariable("moduleId") Long moduleId,@Valid ContractEntity contract,@RequestParam("planfiles") MultipartFile files) throws Exception{ 
		String contractItems=contract.getTableData();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> map = new HashMap<String, Object>();
		File savefile = null;
		if(files!=null&&!StringUtils.isEmpty(files.getOriginalFilename())){
			//1、保存文件至服务器
			try {
				savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
				if(savefile == null || savefile.length() == 0) {
					throw new Exception();
				}
			} catch (Exception e) {
				map.put("success", false);
			}
		}
		//明细
		List<ContractItemEntity> itemList = new ArrayList<ContractItemEntity>();
		if(!StringUtils.isEmpty(contractItems)){
			ContractItemEntity item = null;
			JSONObject object = JSONObject.fromObject(contractItems);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new ContractItemEntity(); 
					if(contract.getContractType()==ContractConstant.CONTRACT_TYPE_LINSHI){
						if(StringUtils.isEmpty(StringUtils.convertToString(object.get("taxRate")))){
							map.put("success", false);
							map.put("msg", "合同明细税率不能为空");
							return map;
						}
						if(StringUtils.isEmpty(StringUtils.convertToString(object.get("totalPrice")))){
							map.put("success", false);
							map.put("msg", "合同明细价格不能为空");
							return map;
						}
					}
					item.setId(object.get("id") == null ? 0l : StringUtils.convertToLong(object.get("id") + ""));
					item.setMaterialIdFk(StringUtils.convertToLong(object.get("materialIdFk") + ""));
					item.setMaterialCode(StringUtils.convertToString(object.get("materialCode")));
					item.setMaterialName(StringUtils.convertToString(object.get("materialName")));
					item.setItemQty(StringUtils.convertToDouble(object.get("itemQty")+""));
					item.setTotalPrice(StringUtils.convertToDouble(object.get("totalPrice")+""));
					item.setTaxPrice(StringUtils.convertToDouble(object.get("taxPrice")+""));
					item.setTaxRate(StringUtils.convertToDouble(object.get("taxRate")+""));
					item.setRemarks(StringUtils.convertToString(object.get("remarks")));
				    item.setBuyerId(user.orgId);
					
					if(object.get("sourceItemId")!=null){
						item.setSourceItemId(StringUtils.convertToLong(object.get("sourceItemId") + ""));
						item.setSourceBillCode(StringUtils.convertToString(object.get("sourceBillCode")));
						item.setSourceItemPrice(StringUtils.convertToDouble(object.get("sourceItemPrice")+""));
					}
					itemList.add(item);
				}
			}
		}
		
		contract.setSignDate(DateUtil.stringToTimestamp(contract.getSignDateStr(),"yyyy-MM-dd"));
		if(!StringUtils.isEmpty(contract.getEffrctiveDateStartStr())){
			contract.setEffrctiveDateStart(DateUtil.stringToTimestamp(contract.getEffrctiveDateStartStr(),"yyyy-MM-dd"));
		}
		if(!StringUtils.isEmpty(contract.getEffrctiveDateEndStr())){
			contract.setEffrctiveDateEnd(DateUtil.stringToTimestamp(contract.getEffrctiveDateEndStr(),"yyyy-MM-dd"));
		}
	
		if(!StringUtils.isEmpty(contract.getStopContractTimeStr())){
			contract.setStopContractTime(DateUtil.stringToTimestamp(contract.getStopContractTimeStr(),"yyyy-MM-dd"));
		}
		contract.setBuyerId(user.orgId);
		contractService.saveContract(moduleId,itemList,contract,savefile,savefile==null?"":files.getOriginalFilename().substring(0,files.getOriginalFilename().lastIndexOf(".")));
		map.put("success", true);
		return map;   
	}
	
	

	
	
	
	/**
	 * 生成流水号
	 * @return
	 */
	@LogClass(method="流水号",module="生成流水号")
	@RequestMapping(value = "createContractCode")
	@ResponseBody
	public ContractEntity createContractCode(){
		ContractEntity c = new ContractEntity();
		String code = contractService.createContractCode();
		c.setCode(code);
		return c;
	} 

	@Override
	public void log(Object message) {
		
	}
	

	@RequestMapping("downloadFile")
	public ModelAndView downloadFile(@RequestParam("fileName") String fileName,@RequestParam("fileUrl") String fileUrl,HttpServletRequest request, HttpServletResponse response) throws Exception {

		String filePath=fileUrl;

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
	
	@RequestMapping(value="getOrgList")
	@ResponseBody
	public Map<String,Object> getOrgList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_roleType", "1");
//		if(buyerOrgPermissionUtil.getVendorIds()!=null){
//			searchParamMap.put("IN_id", buyerOrgPermissionUtil.getVendorIds());
//		}else{
//			searchParamMap.put("EQ_id", -1);
//		}
		Page<OrganizationEntity> userPage = orgService.getOrgs(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getUserList")
	@ResponseBody
	public Map<String,Object> getUserList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", Constant.UNDELETE_FLAG+"");
		searchParamMap.put("EQ_company.roleType",OrgType.ROLE_TYPE_BUYER);
/*		if(buyerOrgPermissionUtil.getBuyerIds()!=null){
			searchParamMap.put("IN_companyId", buyerOrgPermissionUtil.getBuyerIds());
		}else{
			searchParamMap.put("EQ_id", -1);
		}*/
		Page<UserEntity> userPage = accountService.getUsers(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getUserListByRoleType/{roleType}")
	@ResponseBody
	public Map<String,Object> getUserListByRoleType(@PathVariable("roleType") String roleType,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		if("ApplyUser".equals(roleType)){
			Page<UserEntity> userPage = contractService.getAllUser(pageNumber, pageSize, roleType);
			map = new HashMap<String, Object>();
			map.put("rows",userPage.getContent());
			map.put("total",userPage.getTotalElements());
		}else{
			Page<UserEntity> userPage = contractService.getByRoleCode(pageNumber, pageSize, roleType);
			map = new HashMap<String, Object>();
			map.put("rows",userPage.getContent());
			map.put("total",userPage.getTotalElements());
		}
		return map;
	}
	
	/**
	 * 确认附件上传
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
			contractService.saveConfirmFile(id, savefile,savefile==null?"":files.getOriginalFilename());
			map.put("success", true);
	
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		} 
		return map;   
	}
	
	@RequestMapping(value = "deleteContract",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteContract(@RequestBody List<ContractEntity> list){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.deleteContract(list);
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(value = "disableContract/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> disableContract(@PathVariable("id") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		contractService.disableContract(id);
		map.put("success", true);
		return map;
	}
	
}



