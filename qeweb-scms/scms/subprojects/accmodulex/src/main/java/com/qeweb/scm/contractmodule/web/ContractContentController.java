package com.qeweb.scm.contractmodule.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.contractmodule.entity.ContractContentEntity;
import com.qeweb.scm.contractmodule.entity.ContractEntity;
import com.qeweb.scm.contractmodule.entity.ContractExtEntity;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;
import com.qeweb.scm.contractmodule.repository.ContractDao;
import com.qeweb.scm.contractmodule.service.ContractContentService;
import com.qeweb.scm.contractmodule.service.ContractItemService;

/**
 * 合同条款Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contract/contractContent")
public class ContractContentController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private Map<String,Object> map;
	private static String DIR_PATH = PropertiesUtil.getProperty("file.dir","/temp");
	
	@Autowired
	private ContractDao contractDao;
	/**
	 * 合同条款Service
	 */
	@Autowired
	private ContractContentService contractContentService;
	
	/**
	 * 合同明細Service
	 */
	@Autowired
	private ContractItemService contractItemService;
	
	/**
	 * 通过条款ID获得条款
	 * @param id
	 * @return
	 */
	@RequestMapping("getContentById/{id}")
	@ResponseBody
	public ContractContentEntity getContentById(@PathVariable("id") Long id){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		ContractContentEntity newItem = null;
		if(id == 0){
			newItem = new ContractContentEntity();
			newItem.setCode(contractContentService.createContentCode());
			newItem.setBuyerId(user.orgId);
			return newItem;
		}else{
			newItem = contractContentService.findOne(id);
			return newItem;
		}
	}

	/**
	 * 保存新增的条款
	 * @param moduleItem
	 * @return
	 */
	@LogClass(method="保存", module="条款保存")
	@RequestMapping(value = "saveContent")
	@ResponseBody
	//public Map<String,Object> saveModuleItem(@Valid ContractModuleItemEntity moduleItem){
	public Map<String,Object> saveModuleItem(ContractContentEntity moduleItem){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<ContractContentEntity> moduleItemList = new ArrayList<ContractContentEntity>();
		if((moduleItem.getId()==0)){ //如果 moduleItem 的id 为 0 的时候表示为新增的条款
			if(moduleItem.getParentId() == null){
				moduleItem.setParentId(0L); //如果新增条款的上级条款为null,则默认为上级条款的id 为 0L
			}
			if(moduleItem.getBeforeId() ==null){
				moduleItem.setBeforeId(0L);
			}
			moduleItemList.add(moduleItem);
			map = contractContentService.saveModuleItem(moduleItemList);
		}
		if(!(moduleItem.getId()==0)){ //如果 moduleItem 的id 不为 0 的时候表示为编辑的条款
			ContractContentEntity _moduleItem = new ContractContentEntity();
			_moduleItem = moduleItem; //将页面的值赋值给一个新的对象
			moduleItem = contractContentService.findOne(_moduleItem.getId());
			moduleItem.setCode(_moduleItem.getCode());
			moduleItem.setName(_moduleItem.getName());
			moduleItem.setContent(_moduleItem.getContent());
			moduleItem.setBuyerId(user.orgId);
			moduleItemList.add(moduleItem);
			map = contractContentService.updateModuleItem(moduleItemList);
		}
		
		return map;
	}
	
	/**
	 * 复制条款
	 * @param moduleItem
	 * @return
	 */
	@RequestMapping(value = "copyContent")
	@ResponseBody
	public Map<String,Object> copyContent(@Valid @ModelAttribute("content") ContractContentEntity moduleItem){
		map = contractContentService.copyContent(moduleItem);
		return map;
	}
	
	/**
	 * 保存更新的条款
	 * @param moduleItem
	 * @return
	 */
	/*@LogClass(method="更新", module="更新条款")
	@RequestMapping(value = "updateModuleItem")
	@ResponseBody
	public Map<String,Object> updateModuleItem(@Valid @ModelAttribute("moduleItem") ContractModuleItemEntity moduleItem){
		List<ContractModuleItemEntity> itemList = new ArrayList<ContractModuleItemEntity>();
		itemList.add(moduleItem);
		map = moduleItemService.saveModuleItem(itemList);
		return map;
	}*/
	
	/**
	 * 删除条款
	 * @param moduleItem
	 * @return
	 */
	@LogClass(method="删除", module="删除条款")
	@RequestMapping(value = "deleteContent")
	@ResponseBody
	public Map<String,Object> deleteContent(@ModelAttribute("content") ContractContentEntity moduleItem){
		List<ContractContentEntity> itemList = new ArrayList<ContractContentEntity>();
		itemList.add(moduleItem);
		map = contractContentService.deleteContent(itemList);
		return map;
	}
	

	/**
	 * 移动条款
	 * @param moduleItem
	 * @param moveType
	 * @return
	 */
	@LogClass(method="移动", module="移动条款")
	@RequestMapping(value = "moveContent")
	@ResponseBody
	public Map<String,Object> moveContent(@ModelAttribute("content") ContractContentEntity moduleItem , int moveType){
		map = contractContentService.moveContent(moduleItem, moveType);
		return map;
	}
	
	/**
	 * 生成条款编号的流水号
	 * @return
	 */
	@LogClass(method="流水号",module="生成流水号")
	@RequestMapping(value = "createContractCode")
	@ResponseBody
	public ContractContentEntity createContractCode(@RequestParam(value="contractId") Long contractId, 
			@RequestParam(value="parentId") Long parentId, @RequestParam(value="beforeId") Long beforeId, @RequestParam(value="moduleItemId") Long moduleItemId , Model model,ServletRequest request){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		ContractContentEntity moduleItem = new ContractContentEntity();
		String moduleItemCode = contractContentService.createContentCode();
		moduleItem.setId(moduleItemId);
		moduleItem.setCode(moduleItemCode);
		moduleItem.setContractId(contractId);
		moduleItem.setParentId(parentId);
		moduleItem.setBeforeId(beforeId);
		moduleItem.setBuyerId(user.orgId);
		return moduleItem;
	} 
	
	/**
	 * 根据合同模板Id获取合同条款
	 * @return
	 */
	@RequestMapping(value = "getContentTreeGrid/{contractId}")
	@ResponseBody
	public Map<String,Object> getContentTreeGrid(@PathVariable(value="contractId") Long contractId){
		map = new HashMap<String, Object>();
		List<ContractContentEntity> moduleItemList = contractContentService.getContentList(contractId);
		map.put("rows", moduleItemList);
		map.put("total", moduleItemList.size());
		return map;
	}
	
	@RequestMapping(value = "getContentEasyuiTree")
	public String getContentEasyuiTree(Long contractId,Model model,ServletRequest request){
		List<ContractContentEntity> contentList = contractContentService.getAllOperateList(contractId);
		List<ContractItemEntity> itemList = contractItemService.getAllItemByContractId(contractId, Constant.UNDELETE_FLAG);
		model.addAttribute("contractId", contractId);
		model.addAttribute("contentList", contentList);
		model.addAttribute("contentItemList", itemList);
		ContractEntity contract=contractDao.findOne(contractId);
		model.addAttribute("contract", contract);
		int year=DateUtil.getDateYear(contract.getSignDate());
		int month=DateUtil.getDateMonth(contract.getSignDate());
		Calendar c = new GregorianCalendar();
		c.setTime(contract.getSignDate());
		int day=c.get(Calendar.DATE);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		
		ContractExtEntity contractExt=contractContentService.findContractExt(contractId);
		if(contractExt==null){
			contractExt=new ContractExtEntity();
			//contractExt.setAttr_1("10.0");
			// to do
		}
		model.addAttribute("contractExt", contractExt);
		if(contract.getContractType().equals(10L)){	//合同类型：10=年度合同；20=临时合同
			return "back/contract/contractContentBody";
		}else{
			return "back/contract/contractContentBodyTemp";
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="buildPDF/{contractId}/{type}")
	public void buildPDF(@Valid ContractExtEntity contractExt,@PathVariable("contractId") Long contractId,@PathVariable("type") String type,@RequestParam(value="source") String source,@RequestParam(value="mapSource") String datas,HttpServletRequest request, HttpServletResponse response) throws Exception{
		String logpath = getLogger().init(this);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(datas.replaceAll("\"", ""));
		//System.out.println(strBuf.toString());
		JSONObject object = JSONObject.fromObject(strBuf.toString());  
			//数据JSON数据添加至contractExt中
			contractExt.setId(StringUtils.convertToLong(object.get("id")+""));
			contractExt.setContractId(StringUtils.convertToLong(object.get("contractId")+""));
			contractExt.setAttr_1(StringUtils.convertToString(object.get("attr_1")));
			contractExt.setAttr_2(StringUtils.convertToString(object.get("attr_2")));
			contractExt.setAttr_3(StringUtils.convertToString(object.get("attr_3")));
			contractExt.setAttr_4(StringUtils.convertToString(object.get("attr_4")));
			contractExt.setAttr_5(StringUtils.convertToString(object.get("attr_5")));
			contractExt.setAttr_6(StringUtils.convertToString(object.get("attr_6")));
			contractExt.setAttr_7(StringUtils.convertToString(object.get("attr_7")));
			contractExt.setAttr_8(StringUtils.convertToString(object.get("attr_8")));
			contractExt.setAttr_9(StringUtils.convertToString(object.get("attr_9")));
			contractExt.setAttr_10(StringUtils.convertToString(object.get("attr_10")));
			contractExt.setAttr_11(StringUtils.convertToString(object.get("attr_11")));
			contractExt.setAttr_12(StringUtils.convertToString(object.get("attr_12")));
			contractExt.setAttr_13(StringUtils.convertToString(object.get("attr_13")));
			contractExt.setAttr_14(StringUtils.convertToString(object.get("attr_14")));
			contractExt.setAttr_15(StringUtils.convertToString(object.get("attr_15")));
			contractExt.setAttr_16(StringUtils.convertToString(object.get("attr_16")));
			contractExt.setAttr_17(StringUtils.convertToString(object.get("attr_17")));
			contractExt.setAttr_18(StringUtils.convertToString(object.get("attr_18")));
			contractExt.setAttr_19(StringUtils.convertToString(object.get("attr_19")));
			contractExt.setAttr_20(StringUtils.convertToString(object.get("attr_20")));
			contractContentService.saveContractExt(contractExt);
		
			logger.log("method build pdf start");
		List<ContractContentEntity> moduleItemList = contractContentService.getAllOperateList(contractId);		//获得合同条款的树型结构
	    String fileName="合同"+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM);  
	    
	    response.setContentType("application/pdf");
	    response.setCharacterEncoding(Constant.ENCODING_UTF8);  
		response.addHeader("Content-disposition", "attachment;filename="  + new String(fileName.getBytes("GBK"), "ISO8859-1")+".pdf"); 
		
		ContractEntity contract=contractDao.findOne(contractId);
		if(type.equals("1")){
			Document document=new Document(PageSize.A4,50,50,50,50);
			PdfWriter writer=PdfWriter.getInstance(document,response.getOutputStream());
			
			document.open();
			contractContentService.createPdf(writer, document, moduleItemList,contract);
			document.close();
		}
		
		if(type.equals("2")){
			source.replaceAll("<br><br>", "<br></br>");
			StringBuffer strBuffer = new StringBuffer(source);
			strBuffer.replace(strBuffer.indexOf("<meta"), strBuffer.indexOf("</head>"), "");
			strBuffer.replace(strBuffer.indexOf("<script"), strBuffer.indexOf("<div"), "");
			strBuffer.replace(strBuffer.indexOf("<script"), strBuffer.indexOf("</body>"), "");
			
			String rootDir = PropertiesUtil.getProperty(ApplicationProConstant.FILE_DIR, "/");
			if(rootDir == null){
				rootDir = request.getSession(true).getServletContext().getRealPath("/");
			}
			String fileDir=rootDir+"/upload/";
			logger.log("fileDir == "+fileDir);
		    FileUtil.createDirs(fileDir, true);
			String path = fileDir + fileName+".html";
			contractContentService.writeHTML(strBuffer.toString(), path);
			File inputFile = new File(path);
			Document document = new Document();
			/* PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		    document.open();
		        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
		                new FileInputStream(inputFile), Charset.forName("UTF-8"));
		        document.close();*/
		    
		    
		    File savefile = null;
		    String servicePath = rootDir+"/upload";
			String saveDir = FileUtil.getFileSeparator() + DateUtil.dateToString(DateUtil.getCurrentTimestamp()) + FileUtil.getFileSeparator();
			/*if(DIR_PATH != null && DIR_PATH.trim().length() > 0) {
				saveDir = DIR_PATH.trim() + saveDir;
			} else {
				saveDir = servicePath + saveDir;
			}*/
			saveDir = servicePath + saveDir;
			logger.log("saveDir == "+saveDir);
			FileUtil.createDirs(saveDir, true);
			//String pathStr = saveDir + FileUtil.getFileSeparator() + new Date().getTime() + FileUtil.getFileSuffix(new String(fileName.getBytes("GBK"), "ISO8859-1")+".pdf");
			String pathStr = saveDir + (fileName+".pdf");
			logger.log("pathStr == "+pathStr);
			
			savefile = new File(pathStr);
			logger.log("savefile == "+savefile.getPath());
			PdfWriter writer1 = PdfWriter.getInstance(document, new FileOutputStream(savefile));
		    document.open();
		   
	       XMLWorkerHelper.getInstance().parseXHtml(writer1, document,
	                new FileInputStream(inputFile), Charset.forName("UTF-8"));
		    //html文件
		  /*  InputStreamReader isr = new InputStreamReader(new FileInputStream(inputFile),"UTF-8");
		    XMLWorkerHelper.getInstance().parseXHtml(writer1, document, isr);*/
	        document.close();
	        
	        BufferedInputStream bis =null;
			try{
				bis= new BufferedInputStream(new FileInputStream(savefile));
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				
				byte[] buff = new byte[1024];  
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
					bos.write(buff, 0, bytesRead);
					bos.flush();
				}
			}catch(Exception e){
				e.printStackTrace(); 
			}finally{
				try{
					bis.close();
				}catch (IOException e) {  
	                e.printStackTrace();  
	            }  
			}
	        
			 //删除文件    
			FileUtil.deleteFile(path);
			
			//保存服务器上生成的pdf的路径到ContractEntity 中
			pathStr = pathStr.replaceAll("/", "\\\\");
			contract.setPdfPath(pathStr);
			contractDao.save(contract);
			
		}
		logger.log("method build pdf end");
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



