package com.qeweb.scm.purchasemodule.web.orderplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.BacklogService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.service.PurchasePlanService;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanDeleteTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;
import com.qeweb.scm.purchasemodule.web.vo.VetoVO;

/**
 * 采购计划管理
 * @author alex
 * @date 2015年4月20日
 * @path com.qeweb.scm.purchasemodule.web.order.PurchasePlanController.java
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseplan")
public class PurchasePlanController implements ILog {
	
	private ILogger logger = new FileLogger();
	 
	private Map<String,Object> map;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PurchasePlanService purchasePlanService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private BacklogService backlogService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		//待办start
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		Long planId = null;
		//判断是否是同一个大版本
		List<Long> planIds =getPlanIdsByBackLogId(backlogId);
		if(null!=planIds && planIds.size()>0){
			if(planIds.size()==1){
				planId=planIds.get(0);
			}
		}
		model.addAttribute("planId", planId);
		//待办end
		return "back/orderplan/orderPlanList";
	}
	
	/**
	 * 根据待办的预测计划明细获取大版本id集合
	 * @param backlogId
	 * @return
	 */
	public List<Long> getPlanIdsByBackLogId(String backlogId){
		if(!StringUtils.isEmpty(backlogId)){
			List<Long> planItemIdList= backlogService.getBackLogValueIdsById(backlogId);
			return purchasePlanService.findPurchasePlanIdsByItemIds(planItemIdList);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_abolished", "0");
		//用户权限
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		/*searchParamMap.put("IN_createUserId", user.id);*/
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> idList= getPlanIdsByBackLogId(backlogId);
			searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		
		Page<PurchasePlanEntity> userPage = purchasePlanService.getPurchasePlans(pageNumber,pageSize,searchParamMap);
		
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 打开预测计划详情页【采】
	 * @author chao.gu
	 * 20170829
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "planitemInfo")
	public String planitemInfo(Model model) {
		String id=request.getParameter("id");
		model.addAttribute("id", id);
		String backlogId = request.getParameter("backlogId");
		backlogId = null==backlogId?"":backlogId;
		model.addAttribute("backlogId", backlogId);
		return "back/orderplan/orderPlanInfoList";
	}
	
	//字表数据查询
	@RequestMapping(value = "planitem/{planid}" ,method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		searchParamMap.put("EQ_abolished", "0");
		
		//待办开始
		if(searchParamMap.containsKey("IN_backlogId") && !StringUtils.isEmpty(searchParamMap.get("IN_backlogId").toString())){
			String backlogId = searchParamMap.get("IN_backlogId").toString();
			List<Long> idList= backlogService.getBackLogValueIdsById(backlogId);
			searchParamMap.put("IN_id", idList);
		}
		searchParamMap.remove("IN_backlogId");
		//待办结束
		
		//明细添加采购组的限制
		searchParamMap.put("IN_group.id", buyerOrgPermissionUtil.getGroupIds());
		
		
		Page<PurchasePlanItemEntity> userPage = purchasePlanService.getPurchasePlanItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取表头数据
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "planitemHead/{planid}")
	@ResponseBody
	public PurchasePlanItemHeadVO getItemHead(@PathVariable("planid") Long planid,@RequestParam("startDate") String  startDate,@RequestParam("endDate") String endDate ) throws Exception{
		return purchasePlanService.getItemHead(planid,startDate,endDate);
	}
	
	
	//增加计划
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
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
				log("-><b><font color='red'> 上传文件为空，导入失败 </font></b>");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			HSSFWorkbook wb = new HSSFWorkbook();
			try {
				InputStream inp = new FileInputStream(savefile.getPath());
				POIFSFileSystem fs = new POIFSFileSystem(inp);
			    wb = new HSSFWorkbook(fs);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
			HSSFSheet  sheet = wb.getSheetAt(0);
			int columnNum = sheet.getRow(0).getLastCellNum();
			HSSFRow row = sheet.getRow(0);	
			List<String> impDateHeader = new ArrayList<String>();
			
			 //获取excel里面的内容
	        if (columnNum == 0) {
				logger.log("<b><font color='red'> Excel中无数据，请检查文件是否正确...</font></b>");
			}
			//获取表头(关于日期)
		    for(int j = 7 ;j < 31;j++){
		        impDateHeader.add(getCellFormatValue(row.getCell((short) j)).trim());
		     }
	        //对于表头进行验证是否符合当前的时间
	        if(!valiHeaderTransfers(impDateHeader)){
	        	logger.log("<b><font color='red'>表头被修改（导入模板的时间周期和当前的时间周期不相符）！重新下载该模板！</font></b>");
	        	throw new Exception("表头被修改（导入模板的时间周期和当前的时间周期不相符）！重新下载该模板！");
	        }		
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchasePlanTransfer.class), getLogger()); 
			List<PurchasePlanTransfer> list = (List<PurchasePlanTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0 || list.size() == 1) {
				logger.log("<b><font color='red'>上传文件为空，或无内容</font></b>");
				throw new Exception("上传文件为空，或无内容");
			}
			int count = list.size() -1;
			//3、组装并保存数据
			log("->数据转换完成共" + count + " 条，开始构建持久化对象...");
			boolean flag = purchasePlanService.combinePurchasePlan(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购计划成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购计划失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
			map.put("success", false);
			e.printStackTrace();
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));   
		}
		return map;   
	}
	
	
	
	/**
	 * 批量拒绝供应商驳回条件【采】
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "vetoOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> vetoOrderItemPlan(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchasePlanItemEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getVeto_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchasePlanItemEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchasePlanService.getOrderItemPlan(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			purchasePlanService.vetoPlans(orderItemPlanList,vo.getVetoReason());
			map.put("message", "拒绝供应商驳回成功");
			map.put("success", true);
		}else{
			map.put("message", "拒绝供应商驳回失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 批量同意供应商驳回条件【采】
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "unVetoOrderItemPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unVetoOrderItemPlan(VetoVO vo){
		Map<String,Object> map = new HashMap<String, Object>();
		List<PurchasePlanItemEntity> orderItemPlanList = null;
		String [] orderItemPlanIds=vo.getVeto_ids().split(",");
		if(null!=orderItemPlanIds && orderItemPlanIds.length>0){
			orderItemPlanList=new ArrayList<PurchasePlanItemEntity>();
			for (String str : orderItemPlanIds) {
				orderItemPlanList.add(purchasePlanService.getOrderItemPlan(Long.valueOf(str)));
			}
			
		}
		if(null!=orderItemPlanList && orderItemPlanList.size()>0){
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			purchasePlanService.unVetoPlans(orderItemPlanList,vo.getVetoReason());
			map.put("message", "同意供应商驳回");
			map.put("success", true);
		}else{
			map.put("message", "同意供应商驳回失败，请至少选择一条记录");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	//删除计划
	@RequestMapping("filesUploadDelete")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUploadDelete(@RequestParam("planfiles") MultipartFile files) {
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchasePlanDeleteTransfer.class), getLogger()); 
			List<PurchasePlanDeleteTransfer> list = (List<PurchasePlanDeleteTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = purchasePlanService.combinePurchaseDeletePlan(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购删除成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购删除失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购删除失败!");  
			map.put("success", false);
			e.printStackTrace();
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));   
		}
		return map;   
	}
	
	
	/**
	 * 模板下载(自动生成二十四周的日期)
	 * @param 
	 * @return
	 */
	@RequestMapping("filesDownload/{type}")
	public String filesDownload(HttpServletRequest request, HttpServletResponse response,@PathVariable("type") String type )throws Exception {
		
		String fileName="采购预测计划导入模版";
		
		if(type.equals("delete")){
			fileName="采购预测计划删除模版";
		}
		
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchasePlanTransfer.class), null); 		
		String [] handers ={};
		List<String> titleList = new ArrayList<String>();
		titleList.add("工厂编码");
		titleList.add("采购组编码");
		titleList.add("供应商编码");
		titleList.add("物料号");
		titleList.add("当日库存");
		titleList.add("当日未交PO数量");
		if(!type.equals("delete")){
			//组织表头的信息
			Date date = new Date();
			if(!IsMonday(date)){
				date = getNextMonday(date); 
			}
			for(int i = 0 ;i < 24 ; i++){
				Calendar cd = Calendar.getInstance();  
			    cd.setTime(date);  
				titleList.add(cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
				date=getSeventDay(date);
			}
		}
		handers = titleList.toArray(new String[0]);
		excelUtil.createExcelTemplate(handers);  
		return null; 
	}
	
    //验证表头是否修改
    public Boolean  valiHeaderTransfers(List<String> impHeader) {
	  List<String> nowHeader = new ArrayList<String>();
      //获取当前时间的表头进行匹配
	  Date date = new Date();
	  if(!IsMonday(date)){
		  date = getNextMonday(date); 
	  }
	  for(int i = 0 ;i < 24 ; i++){
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);  
	    nowHeader.add(cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
		date=getSeventDay(date);
	  }
      if(nowHeader.size() != impHeader.size()){
    	  return false;
      }
      if(!(nowHeader.containsAll(impHeader) && impHeader.containsAll(nowHeader))){
    	  return false;
      }
      return true;
    } 

	//获取当前日期是否为周一
    public Boolean  IsMonday(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == 1){
        	return true;
        }
        return false;
    } 
    
   // 获得下周星期一的日期  
    public  Date getNextMonday(Date gmtCreate) {  
        int mondayPlus = getMondayPlus(gmtCreate);  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);  
        Date monday = currentDate.getTime();  
        return monday;  
    }  
    // 获得上周周星期一的日期  
    public  Date getLastMonday(Date gmtCreate) {  
        int mondayPlus = getMondayPlus(gmtCreate);  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);  
        Date monday = currentDate.getTime();  
        return monday;  
    }  
    
    // 获得当前日期与本周日相差的天数  
    private int getMondayPlus(Date gmtCreate) {  
        Calendar cd = Calendar.getInstance();    
        cd.setTime(gmtCreate);  
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......    
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //因为按中国礼拜一作为第一天所以这里减1    
        if (dayOfWeek == 1) {    
            return 0;    
        } else if(dayOfWeek == 0){    
        	return -6;
        }  else{
        	return 1 - dayOfWeek;
        }
    } 
    
    //获取七天后的日期
    public  Date getSeventDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +7);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }
    
    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
	
    
	/**
	 * 删除采购计划
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "deletePlan/{planid}")
	@ResponseBody
	public Map<String,Object> deletePlan(@PathVariable("planid") Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.deletePlanItem(id);;
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量发布采购计划（全部）
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "publishPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishPlan(@RequestBody List<PurchasePlanEntity> planList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.publishPlans(planList);;
		map.put("success", true);
		return map;
	}
	
	/**
	 * 批量发布采购计划明细
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "publishPlanItem",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publishPlanItem(@RequestBody List<PurchasePlanItemEntity> planItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.publishOrderPlanItems(planItemList);;
		map.put("success", true);
		return map;
	}
	
	
	//导出
	@RequestMapping("exportPlanner")
	@ResponseBody
	public String download(Model model,ServletRequest request,HttpServletResponse  response) throws Exception {
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("预测计划-计划员", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Date date = new Date();
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);
	    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("IN_purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_isNew", "1"); 
		searchParamMap.put("EQ_plan.month", month); 
		purchasePlanService.exportExcelForPlanner(searchParamMap,month,response);
		return null;   
	}
	
	
	
/*	@RequestMapping("getPlan/{id}")
	@ResponseBody
	public PurchasePlanEntity getPlan(@PathVariable("id") Long id){
		return purchasePlanService.getPlan(id);
	}
	
	@RequestMapping(value = "planitem/{planid}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		searchParamMap.put("EQ_abolished", "0");
		Page<PurchasePlanVenodrItemEntity> userPage = purchasePlanService.getPurchasePlanVendorItems(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	*//**
	 * 批量发布采购计划
	 * @param planList
	 * @return
	 *//*

	
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchasePlanTransfer.class), getLogger()); 
			List<PurchasePlanTransfer> list = (List<PurchasePlanTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = purchasePlanService.combinePurchasePlan(list, getLogger());
			if(flag) {
				map.put("msg", "导入采购计划成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入采购计划失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
			map.put("success", false);
			e.printStackTrace();
		} finally {
			 getLogger().destory();  
			 map.put("name", StringUtils.encode(new File(logpath).getName()));    
			 map.put("log", StringUtils.encode(logpath));   
		}
		return map;   
	}
	
	@RequestMapping(value = "updatePlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatePlan(@Valid PurchasePlanEntity purchasePlan, @RequestParam(value="datas") String billItemdatas) throws Exception{ 
		Map<String,Object> map = new HashMap<String, Object>();
		//采购计划明细
		List<PurchasePlanVenodrItemEntity> purchasePlanItem = new ArrayList<PurchasePlanVenodrItemEntity>();
		PurchasePlanVenodrItemEntity item = null;
		JSONObject object = JSONObject.fromObject(billItemdatas);     
		JSONArray array = (JSONArray) object.get("rows");
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new PurchasePlanVenodrItemEntity(); 
			item.setId(StringUtils.convertToLong(object.get("id") + ""));
			item.setPlanQty(StringUtils.convertToDouble(object.get("planQty") + ""));
			item.setPlanRecTime(DateUtil.stringToTimestamp(object.get("planRecTime") + ""));
			purchasePlanItem.add(item);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		purchasePlanService.savePurchasePlan(purchasePlan, purchasePlanItem, new UserEntity(user.id));
		map.put("message", "修改采购计划成功");
		map.put("success", true);
		return map;   
	}
	
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("采购计划", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = new HashMap<String, Object>();   
		searchParamMap.put("EQ_plan.id", request.getParameter("id"));  
		searchParamMap.put("EQ_abolished", "0");
		List<PurchasePlanTransfer> list = purchasePlanService.getPurchasePlanVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchasePlanTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	
	*//**
	 * 批量删除采购计划
	 * @param planList
	 * @return
	 *//*
	@RequestMapping(value = "deletePlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletePlan(@RequestBody List<PurchasePlanEntity> planList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchasePlanService.deletePlans(planList);;
		map.put("success", true);
		return map;
	}*/

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

























































