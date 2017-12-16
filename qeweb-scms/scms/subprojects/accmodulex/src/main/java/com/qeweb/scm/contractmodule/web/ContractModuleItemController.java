package com.qeweb.scm.contractmodule.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.contractmodule.entity.ContractModuleEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleItemEntity;
import com.qeweb.scm.contractmodule.service.ContractModuleItemService;
import com.qeweb.scm.contractmodule.service.ContractModuleService;

/**
 * 合同条款Controller
 * @author u
 *
 */
@Controller
@RequestMapping("/manager/contractmoduleItem/moduleItem")
public class ContractModuleItemController implements ILog  {
	private ILogger logger=new FileLogger();
	
	private static final Long TYPE_ZERO = 0L;
	private static final Long TYPE_ONE = 1L;
	private static String DIR_PATH = PropertiesUtil.getProperty("file.dir","/temp");
	
	private Map<String,Object> map;
	
	/**
	 * 合同模板Service
	 */
	@Autowired
	private ContractModuleService moduleService;
	
	/**
	 * 合同条款Service
	 */
	@Autowired
	private ContractModuleItemService moduleItemService;
	
	/**
	 * 通过条款ID获得条款
	 * @param id
	 * @return
	 */
	@RequestMapping("getModuleItemById/{id}")
	@ResponseBody
	public ContractModuleItemEntity getModuleItemById(@PathVariable("id") Long id){
		ContractModuleItemEntity newItem = null;
		if(id == 0){
			newItem = new ContractModuleItemEntity();
			newItem.setCode(moduleItemService.createModuleItemCode());
			return newItem;
		}else{
			newItem = moduleItemService.findOne(id);
			return newItem;
		}
	}

	/**
	 * 保存新增的条款
	 * @param moduleItem
	 * @return
	 */
	@LogClass(method="保存", module="条款保存")
	@RequestMapping(value = "saveModuleItem")
	@ResponseBody
	//public Map<String,Object> saveModuleItem(@Valid ContractModuleItemEntity moduleItem){
	public Map<String,Object> saveModuleItem(ContractModuleItemEntity moduleItem){
		List<ContractModuleItemEntity> moduleItemList = new ArrayList<ContractModuleItemEntity>();
		if((moduleItem.getId()==0)){ //如果 moduleItem 的id 为 0 的时候表示为新增的条款
			if(moduleItem.getParentId() == null){
				moduleItem.setParentId(0L); //如果新增条款的上级条款为null,则默认为上级条款的id 为 0L
			}
			if(moduleItem.getBeforeId() ==null){
				moduleItem.setBeforeId(0L);
			}
			moduleItemList.add(moduleItem);
			map = moduleItemService.saveModuleItem(moduleItemList);
		}
		if(!(moduleItem.getId()==0)){ //如果 moduleItem 的id 不为 0 的时候表示为编辑的条款
			ContractModuleItemEntity _moduleItem = new ContractModuleItemEntity();
			_moduleItem = moduleItem; //将页面的值赋值给一个新的对象
			moduleItem = moduleItemService.findOne(_moduleItem.getId());
			moduleItem.setCode(_moduleItem.getCode());
			moduleItem.setName(_moduleItem.getName());
			moduleItem.setContent(_moduleItem.getContent());
			moduleItemList.add(moduleItem);
			map = moduleItemService.updateModuleItem(moduleItemList);
		}
		
		return map;
	}
	
	/**
	 * 复制条款
	 * @param moduleItem
	 * @return
	 */
	@RequestMapping(value = "copyModuleItem")
	@ResponseBody
	public Map<String,Object> copyModuleItem(@Valid @ModelAttribute("moduleItem") ContractModuleItemEntity moduleItem){
		map = moduleItemService.copyModuleItem(moduleItem);
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
	@RequestMapping(value = "deleteModuleItem")
	@ResponseBody
	public Map<String,Object> deleteModuleItem(@ModelAttribute("moduleItem") ContractModuleItemEntity moduleItem){
		List<ContractModuleItemEntity> itemList = new ArrayList<ContractModuleItemEntity>();
		itemList.add(moduleItem);
		map = moduleItemService.deleteModuleItem(itemList);
		return map;
	}
	@Override
	public void log(Object message) {
		
	}
	

	/**
	 * 移动条款
	 * @param moduleItem
	 * @param moveType
	 * @return
	 */
	@LogClass(method="移动", module="移动条款")
	@RequestMapping(value = "moveModuleItem")
	@ResponseBody
	public Map<String,Object> moveModuleItem(@ModelAttribute("moduleItem") ContractModuleItemEntity moduleItem , int moveType){
		map = moduleItemService.moveModuleItem(moduleItem, moveType);
		return map;
	}
	
	/**
	 * 生成条款编号的流水号
	 * @return
	 */
	@LogClass(method="流水号",module="生成流水号")
	@RequestMapping(value = "createModuleItemCode")
	@ResponseBody
	public ContractModuleItemEntity createModuleItemCode(@RequestParam(value="moduleId") Long moduleId, 
			@RequestParam(value="parentId") Long parentId, @RequestParam(value="beforeId") Long beforeId, @RequestParam(value="moduleItemId") Long moduleItemId , Model model,ServletRequest request){
		ContractModuleItemEntity moduleItem = new ContractModuleItemEntity();
		String moduleItemCode = moduleItemService.createModuleItemCode();
		moduleItem.setId(moduleItemId);
		moduleItem.setCode(moduleItemCode);
		moduleItem.setModuleId(moduleId);
		moduleItem.setParentId(parentId);
		moduleItem.setBeforeId(beforeId);
		return moduleItem;
	} 
	
	/**
	 * 获取合同条款Tree
	 * @return
	 */
	@LogClass(method="获取合同条款",module="获取合同条款")
	@RequestMapping(value = "getModuleItemEasyuiTree")
	public String getModuleItemEasyuiTree(Long moduleId,Model model,ServletRequest request){
		ContractModuleEntity contractModule = moduleService.findOne(moduleId);
		List<ContractModuleItemEntity> moduleItemList = moduleItemService.getAllOperateList(moduleId);
		model.addAttribute("moduleId", moduleId);
		model.addAttribute("moduleItemList", moduleItemList);
		if(contractModule.getContractType().equals(TYPE_ZERO)){	//合同模板类型：0=临时模板；1=终极模板
			return "back/contract/contractPrintBodyTemp";
		}else{
			return "back/contract/contractPrintBody";
		}
	}

	/**
	 * 生成pdf
	 * @param moduleId
	 * @param indexUrl
	 * @param source
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@LogClass(method="pdf",module="生成pdf")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="buildPDF/{moduleId}/{type}")
	public void buildPDF(@PathVariable("moduleId") Long moduleId,@PathVariable("type") String type,@RequestParam(value="source") String source,HttpServletRequest request, HttpServletResponse response) throws Exception{
		log("method build pdf start");
		ContractModuleEntity module = moduleService.findOne(moduleId);
		List<ContractModuleItemEntity> moduleItemList = moduleItemService.getAllOperateList(moduleId);		//获得合同条款的树型结构
	    String fileName="合同模板"+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM);  
	    
	    response.setContentType("application/pdf");
	    response.setCharacterEncoding(Constant.ENCODING_UTF8);  
		response.addHeader("Content-disposition", "attachment;filename="  + new String(fileName.getBytes("GBK"), "ISO8859-1")+".pdf"); 
		
		
		if(type.equals("1")){
			Document document=new Document(PageSize.A4,50,50,50,50);
			PdfWriter writer=PdfWriter.getInstance(document,response.getOutputStream());
			
			document.open();
			if(module.getContractType().equals(TYPE_ONE)){	//模板类型：0=临时模板；1=终极模板
				moduleItemService.createPdf(writer, document, moduleItemList);
			}else if(module.getContractType().equals(TYPE_ZERO)){
				moduleItemService.createPdfTemp(writer, document, moduleItemList);
			}
			
			document.close();
		}
		
		if(type.equals("2")){
			source.replaceAll("<br><br>", "<br></br>");
			StringBuffer strBuffer = new StringBuffer(source);
			strBuffer.replace(strBuffer.indexOf("<meta"), strBuffer.indexOf("</head>"), "");
			strBuffer.replace(strBuffer.indexOf("<script"), strBuffer.indexOf("<div"), "");
			strBuffer.replace(strBuffer.indexOf("<script"), strBuffer.indexOf("</body>"), "");
			String fileDir = request.getSession().getServletContext().getRealPath("/").substring(0, request.getSession().getServletContext().getRealPath("/").indexOf(":")+1);
			FileUtil.createDirs(fileDir, true);
			String path = fileDir + fileName+".html";
			moduleItemService.writeHTML(strBuffer.toString(), path);
			File inputFile = new File(path);
			Document document = new Document();
		  /*  PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		    document.open();
		        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
		                new FileInputStream(inputFile), Charset.forName("UTF-8"));
		        document.close();*/
			
	        File savefile = null;
			String servicePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
			String saveDir = FileUtil.getFileSeparator() + DateUtil.dateToString(DateUtil.getCurrentTimestamp()) + FileUtil.getFileSeparator();
			if(DIR_PATH != null && DIR_PATH.trim().length() > 0) {
				saveDir = DIR_PATH.trim() + saveDir;
			} else {
				saveDir = servicePath + saveDir;
			}    
			FileUtil.createDirs(saveDir, true);
			//String pathStr = saveDir + FileUtil.getFileSeparator() + new Date().getTime() + FileUtil.getFileSuffix(new String(fileName.getBytes("GBK"), "ISO8859-1")+".pdf");
			String pathStr = saveDir + (fileName+".pdf");
			pathStr = pathStr.replaceAll("/", "\\\\");
			savefile = new File(pathStr);

			PdfWriter writer1 = PdfWriter.getInstance(document, new FileOutputStream(savefile));
		    document.open();
	        XMLWorkerHelper.getInstance().parseXHtml(writer1, document,
	                new FileInputStream(inputFile), Charset.forName("UTF-8"));
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
		}
		log("method build pdf end");
	}
}



