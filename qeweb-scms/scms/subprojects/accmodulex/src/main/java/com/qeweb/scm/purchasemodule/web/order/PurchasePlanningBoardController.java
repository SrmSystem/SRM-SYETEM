package com.qeweb.scm.purchasemodule.web.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.EasyUISortUtil;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanningBoardEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanningBoardService;
import com.qeweb.scm.sap.service.PurchasePlanningBoardSyncService;

/**
 * 采购计划看板
 * @author haiming.huang
 * @date 2017年9月30日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchasePlanningBoardController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchasePlanningBoard")
public class PurchasePlanningBoardController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private PurchasePlanningBoardService purchasePlanningBoard;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/purchasePlanningBoard/purchasePlanningBoardList";  
	}


	/**
	 * 采购计划看板查询列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{getList}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		
		//采购组权限
		searchParamMap.put("IN_groupId", buyerOrgPermissionUtil.getGroupIds());
		searchParamMap.put("EQ_state", "0");
		String sort = "factoryCode,materialCode";
		String orderby = "asc,asc";
		
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		Page<PurchasePlanningBoardEntity> userPage = purchasePlanningBoard.getPurchasePlanningBoardItems(pageNumber,pageSize,searchParamMap,EasyUISortUtil.getSort(sort, orderby));
		
		
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 同步采购计划看板
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/synPurchasePlanningBoard",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> synPurchasePlanningBoard(@RequestBody Map<String ,Object> param) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		PurchasePlanningBoardSyncService purchasePlanningBoardSyncService = SpringContextUtils.getBean("purchasePlanningBoardSyncService");	
		boolean isSuccess = purchasePlanningBoardSyncService.execute(logger,param);
		if(isSuccess){
			map.put("message", "同步成功");
			map.put("success", true);
		}else{
			map.put("message", "SAP连接失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 获取头部信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getPurchasePlanBoardHead",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPurchasePlanBoardHead(ServletRequest request) throws Exception{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		//采购组权限
		searchParamMap.put("IN_groupId", buyerOrgPermissionUtil.getGroupIds());
		searchParamMap.put("EQ_state", "0");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("entity",purchasePlanningBoard.getFindOne(searchParamMap));
		return map;
	}
	
	/**
	 * 采购计划看板导出
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/exportExcel",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exportExcel(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String ,Object> param) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
			//采购组权限
			searchParamMap.put("IN_groupId", buyerOrgPermissionUtil.getGroupIds());
			searchParamMap.put("EQ_factoryCode", param.get("search-LIKE_factoryCode"));
			searchParamMap.put("EQ_materialCode", param.get("search-materialCode"));
			searchParamMap.put("EQ_state", "0");
			String sort = "factoryCode,materialCode";
			String orderby = "asc,asc";
			
			 List<PurchasePlanningBoardEntity> list = purchasePlanningBoard.getfindAll(searchParamMap,EasyUISortUtil.getSort(sort, orderby));
			 List<String> strList =  new ArrayList<String>();
			 
			 if(list.size()>0){
				 SimpleDateFormat simp = new SimpleDateFormat("yyyyMMddHHmm");
					String fileName = "采购计划看板导出" + simp.format(new Date())+ ".xls";
					String filePath = PropertiesUtil.getProperty("filePath","");
					String localPath = filePath + fileName;
					
					HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = null;
					HSSFRow	row = null;
					HSSFCell cell = null;
					
					HSSFFont tFont1 = wb.createFont();
					tFont1.setFontName("宋体");
					tFont1.setFontHeightInPoints((short) 22);
					tFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					
				    HSSFCellStyle tStyle1 = wb.createCellStyle(); 
				    tStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
				    tStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平
				    tStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				    tStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
				    tStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				    tStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
				    tStyle1.setFont(tFont1);
				    
				    HSSFFont tFont2 = wb.createFont();
				    tFont2.setFontName("宋体");
				    tFont2.setFontHeightInPoints((short) 11);
				    
				    HSSFCellStyle tStyle2 = wb.createCellStyle(); 
				    tStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				    tStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				    tStyle2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				    tStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
				    tStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				    tStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				    tStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				    tStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				    tStyle2.setFont(tFont2);
				    
				    HSSFFont conFont = wb.createFont();
				    conFont.setFontName("宋体");
				    conFont.setFontHeightInPoints((short) 11);
				    
				    HSSFCellStyle conStyle = wb.createCellStyle(); 
				    conStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				    conStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				    conStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				    conStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				    conStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				    conStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				    conStyle.setFont(conFont);
				    
				    sheet = wb.createSheet("sheet1");
				    sheet.addMergedRegion(new CellRangeAddress(0,0,0,67));
					row = sheet.createRow(0);
					
					HSSFCell titleCell1 = row.createCell(0);
					String headTitle1 = "采购计划看板";
					titleCell1.setCellValue(headTitle1);
					titleCell1.setCellStyle(tStyle1);
					
					
					 List<String> headList = new ArrayList<String>();
			         headList.add("工厂");
			         headList.add("物料");
			         headList.add("物料描述");
			         headList.add("采购组");
			         headList.add("计划周转天数");
			         headList.add("质量检验天数");
			         headList.add("单位");
			         headList.add("");
			         PurchasePlanningBoardEntity entity = purchasePlanningBoard.getFindOne(searchParamMap);
			         
			         headList.add(entity.getDAT01());headList.add(entity.getDAT02());headList.add(entity.getDAT03());headList.add(entity.getDAT04());
			         headList.add(entity.getDAT05());headList.add(entity.getDAT06());headList.add(entity.getDAT07());headList.add(entity.getDAT08());
			         headList.add(entity.getDAT09());headList.add(entity.getDAT10());headList.add(entity.getDAT11());headList.add(entity.getDAT12());
			         headList.add(entity.getDAT13());headList.add(entity.getDAT14());headList.add(entity.getDAT15());headList.add(entity.getDAT16());
			         headList.add(entity.getDAT17());headList.add(entity.getDAT18());headList.add(entity.getDAT19());headList.add(entity.getDAT20());
			         headList.add(entity.getDAT21());headList.add(entity.getDAT22());headList.add(entity.getDAT23());headList.add(entity.getDAT24());
			         headList.add(entity.getDAT25());headList.add(entity.getDAT26());headList.add(entity.getDAT27());headList.add(entity.getDAT28());
			         headList.add(entity.getDAT29());headList.add(entity.getDAT30());headList.add(entity.getDAT31());headList.add(entity.getDAT32());
			         headList.add(entity.getDAT33());headList.add(entity.getDAT34());headList.add(entity.getDAT35());headList.add(entity.getDAT36());
			         headList.add(entity.getDAT37());headList.add(entity.getDAT38());headList.add(entity.getDAT39());headList.add(entity.getDAT40());
			         headList.add(entity.getDAT41());headList.add(entity.getDAT42());headList.add(entity.getDAT43());headList.add(entity.getDAT44());
			         headList.add(entity.getDAT45());headList.add(entity.getDAT46());headList.add(entity.getDAT47());headList.add(entity.getDAT48());
			         headList.add(entity.getDAT49());headList.add(entity.getDAT50());headList.add(entity.getDAT51());headList.add(entity.getDAT52());
			         headList.add(entity.getDAT53());headList.add(entity.getDAT54());headList.add(entity.getDAT55());headList.add(entity.getDAT56());
			         headList.add(entity.getDAT57());headList.add(entity.getDAT58());headList.add(entity.getDAT59());headList.add(entity.getDAT60());
					
					row = sheet.createRow(1);
					row.setHeightInPoints(27);
					for(int i = 0; i < headList.size(); i++){
						cell = row.createCell(i);
						cell.setCellValue(headList.get(i));
						cell.setCellStyle(tStyle2);
					}
					
					 
					for(int i = 0; i <list.size(); i++){
						PurchasePlanningBoardEntity entityPpb = list.get(i);
						String str =  entityPpb.getFactoryCode()+entityPpb.getMaterialCode();
						if(!strList.contains(str)){
							strList.add(str);
							List<PurchasePlanningBoardEntity> listDtl = purchasePlanningBoard.getListDtl(entityPpb.getMaterialCode(),entityPpb.getFactoryCode());
							
							int startRow=0;
							if(listDtl != null ){
								if(list.size() == 0){
									startRow = sheet.getLastRowNum();
								}else{
									startRow = sheet.getLastRowNum()+1;
								}
							}
							
							for(int j = 0; j < listDtl.size(); j++){
								
								PurchasePlanningBoardEntity entityDtl = listDtl.get(j);
								row = sheet.createRow(startRow+j);
									for(int m = 0; m < 4; m++){
										cell = row.createCell(m);
										cell.setCellValue("");
										cell.setCellStyle(conStyle);
									}
									cell = row.createCell(7);cell.setCellValue(entityDtl.getDtype());cell.setCellStyle(conStyle);
									cell = row.createCell(8);cell.setCellValue(entityDtl.getQTY01());cell.setCellStyle(conStyle);
									cell = row.createCell(9);cell.setCellValue(entityDtl.getQTY02());cell.setCellStyle(conStyle);
									cell = row.createCell(10);cell.setCellValue(entityDtl.getQTY03());cell.setCellStyle(conStyle);
									cell = row.createCell(11);cell.setCellValue(entityDtl.getQTY04());cell.setCellStyle(conStyle);
									cell = row.createCell(12);cell.setCellValue(entityDtl.getQTY05());cell.setCellStyle(conStyle);
									cell = row.createCell(13);cell.setCellValue(entityDtl.getQTY06());cell.setCellStyle(conStyle);
									cell = row.createCell(14);cell.setCellValue(entityDtl.getQTY07());cell.setCellStyle(conStyle);
									cell = row.createCell(15);cell.setCellValue(entityDtl.getQTY08());cell.setCellStyle(conStyle);
									cell = row.createCell(16);cell.setCellValue(entityDtl.getQTY09());cell.setCellStyle(conStyle);
									cell = row.createCell(17);cell.setCellValue(entityDtl.getQTY10());cell.setCellStyle(conStyle);
									cell = row.createCell(18);cell.setCellValue(entityDtl.getQTY11());cell.setCellStyle(conStyle);
									cell = row.createCell(19);cell.setCellValue(entityDtl.getQTY12());cell.setCellStyle(conStyle);
									cell = row.createCell(20);cell.setCellValue(entityDtl.getQTY13());cell.setCellStyle(conStyle);
									cell = row.createCell(21);cell.setCellValue(entityDtl.getQTY14());cell.setCellStyle(conStyle);
									cell = row.createCell(22);cell.setCellValue(entityDtl.getQTY15());cell.setCellStyle(conStyle);
									cell = row.createCell(23);cell.setCellValue(entityDtl.getQTY16());cell.setCellStyle(conStyle);
									cell = row.createCell(24);cell.setCellValue(entityDtl.getQTY17());cell.setCellStyle(conStyle);
									cell = row.createCell(25);cell.setCellValue(entityDtl.getQTY18());cell.setCellStyle(conStyle);
									cell = row.createCell(26);cell.setCellValue(entityDtl.getQTY19());cell.setCellStyle(conStyle);
									cell = row.createCell(27);cell.setCellValue(entityDtl.getQTY20());cell.setCellStyle(conStyle);
									cell = row.createCell(28);cell.setCellValue(entityDtl.getQTY21());cell.setCellStyle(conStyle);
									cell = row.createCell(29);cell.setCellValue(entityDtl.getQTY22());cell.setCellStyle(conStyle);
									cell = row.createCell(30);cell.setCellValue(entityDtl.getQTY23());cell.setCellStyle(conStyle);
									cell = row.createCell(31);cell.setCellValue(entityDtl.getQTY24());cell.setCellStyle(conStyle);
									cell = row.createCell(32);cell.setCellValue(entityDtl.getQTY25());cell.setCellStyle(conStyle);
									cell = row.createCell(33);cell.setCellValue(entityDtl.getQTY26());cell.setCellStyle(conStyle);
									cell = row.createCell(34);cell.setCellValue(entityDtl.getQTY27());cell.setCellStyle(conStyle);
									cell = row.createCell(35);cell.setCellValue(entityDtl.getQTY28());cell.setCellStyle(conStyle);
									cell = row.createCell(36);cell.setCellValue(entityDtl.getQTY29());cell.setCellStyle(conStyle);
									cell = row.createCell(37);cell.setCellValue(entityDtl.getQTY30());cell.setCellStyle(conStyle);
									cell = row.createCell(38);cell.setCellValue(entityDtl.getQTY31());cell.setCellStyle(conStyle);
									cell = row.createCell(39);cell.setCellValue(entityDtl.getQTY32());cell.setCellStyle(conStyle);
									cell = row.createCell(40);cell.setCellValue(entityDtl.getQTY33());cell.setCellStyle(conStyle);
									cell = row.createCell(41);cell.setCellValue(entityDtl.getQTY34());cell.setCellStyle(conStyle);
									cell = row.createCell(42);cell.setCellValue(entityDtl.getQTY35());cell.setCellStyle(conStyle);
									cell = row.createCell(43);cell.setCellValue(entityDtl.getQTY36());cell.setCellStyle(conStyle);
									cell = row.createCell(44);cell.setCellValue(entityDtl.getQTY37());cell.setCellStyle(conStyle);
									cell = row.createCell(45);cell.setCellValue(entityDtl.getQTY38());cell.setCellStyle(conStyle);
									cell = row.createCell(46);cell.setCellValue(entityDtl.getQTY39());cell.setCellStyle(conStyle);
									cell = row.createCell(47);cell.setCellValue(entityDtl.getQTY40());cell.setCellStyle(conStyle);
									cell = row.createCell(48);cell.setCellValue(entityDtl.getQTY41());cell.setCellStyle(conStyle);
									cell = row.createCell(49);cell.setCellValue(entityDtl.getQTY42());cell.setCellStyle(conStyle);
									cell = row.createCell(50);cell.setCellValue(entityDtl.getQTY43());cell.setCellStyle(conStyle);
									cell = row.createCell(51);cell.setCellValue(entityDtl.getQTY44());cell.setCellStyle(conStyle);
									cell = row.createCell(52);cell.setCellValue(entityDtl.getQTY45());cell.setCellStyle(conStyle);
									cell = row.createCell(53);cell.setCellValue(entityDtl.getQTY46());cell.setCellStyle(conStyle);
									cell = row.createCell(54);cell.setCellValue(entityDtl.getQTY47());cell.setCellStyle(conStyle);
									cell = row.createCell(55);cell.setCellValue(entityDtl.getQTY48());cell.setCellStyle(conStyle);
									cell = row.createCell(56);cell.setCellValue(entityDtl.getQTY49());cell.setCellStyle(conStyle);
									cell = row.createCell(57);cell.setCellValue(entityDtl.getQTY50());cell.setCellStyle(conStyle);
									cell = row.createCell(58);cell.setCellValue(entityDtl.getQTY51());cell.setCellStyle(conStyle);
									cell = row.createCell(59);cell.setCellValue(entityDtl.getQTY52());cell.setCellStyle(conStyle);
									cell = row.createCell(60);cell.setCellValue(entityDtl.getQTY53());cell.setCellStyle(conStyle);
									cell = row.createCell(61);cell.setCellValue(entityDtl.getQTY54());cell.setCellStyle(conStyle);
									cell = row.createCell(62);cell.setCellValue(entityDtl.getQTY55());cell.setCellStyle(conStyle);
									cell = row.createCell(63);cell.setCellValue(entityDtl.getQTY56());cell.setCellStyle(conStyle);
									cell = row.createCell(64);cell.setCellValue(entityDtl.getQTY57());cell.setCellStyle(conStyle);
									cell = row.createCell(65);cell.setCellValue(entityDtl.getQTY58());cell.setCellStyle(conStyle);
									cell = row.createCell(66);cell.setCellValue(entityDtl.getQTY59());cell.setCellStyle(conStyle);
									cell = row.createCell(67);cell.setCellValue(entityDtl.getQTY60());cell.setCellStyle(conStyle);
									
								
							}
							int endRow = sheet.getLastRowNum();
							for(int n = 0; n < 67; n++){
								if(n<7){
									sheet.addMergedRegion(new CellRangeAddress(startRow,endRow,n,n));
								}
							}
							row = sheet.getRow(startRow);
							cell = row.createCell(0);
							cell.setCellValue(entityPpb.getFactoryCode());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(1);
							cell.setCellValue(entityPpb.getMaterialCode());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(2);
							cell.setCellValue(entityPpb.getMaterialName());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(3);
							cell.setCellValue(entityPpb.getGroupCode());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(4);
							cell.setCellValue(entityPpb.getPlannedTurnaroundDay());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(5);
							cell.setCellValue(entityPpb.getQualityDay());
							cell.setCellStyle(conStyle);
							
							cell = row.createCell(6);
							cell.setCellValue(entityPpb.getUnit());
							cell.setCellStyle(conStyle);
						}
					}
					for(int i = 0; i < 67; i++){
						sheet.autoSizeColumn(i,true);
					}
					
					File file = new File(localPath);
					if(!file.exists()){
						new File(file.getParent()).mkdirs();
					}
					BufferedOutputStream out = null;
					try {
						out = new BufferedOutputStream(new FileOutputStream(file));
						wb.write(out);
						out.flush();
						resultMap.put("code", true);
						resultMap.put("fileName", fileName);
						resultMap.put("msg", "生成采购计划看板excel成功");
					} catch (Exception e) {
						resultMap.put("code", false);
						resultMap.put("fileName", "");
						resultMap.put("msg", "生成采购计划看板excel失败");
					}finally{
						try {
							out.close();
						} catch (Exception e2) {
							resultMap.put("code", false);
							resultMap.put("fileName", "");
							resultMap.put("msg", "生成采购计划看板excel失败");
						}
					}
				} else{
					resultMap.put("code", false);
					resultMap.put("fileName", "");
					resultMap.put("msg", "生成采购计划看板excel失败,无数据");
				}
			 }catch (Exception e) {
					resultMap.put("code", false);
					resultMap.put("fileName", "");
					resultMap.put("msg", "生成采购计划看板excel失败,请联系技术人员!");
				}
			  
		    
		
		return resultMap;
	}
	
	@RequestMapping(value="/downloadExcel", method = RequestMethod.GET)
	public void downloadExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String fileName = new String(request.getParameter("fileName").getBytes("iso8859-1"),"UTF-8");
			String filePath = PropertiesUtil.getProperty("filePath","");
			String localName = filePath + fileName;
			File file = new File(localName);
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			response.addHeader("Content-Length", String.valueOf(file.length()));
			InputStream fis = new BufferedInputStream(new FileInputStream(
					localName));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
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
