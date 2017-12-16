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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
import org.springside.modules.web.Servlets;

import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanItemEntity;
import com.qeweb.scm.purchasemodule.service.PurchaseTotalPlanService;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseTotalPlanTransfer;

/**
 * 预测总量计划管理
 * @author HAO.QIN
 * @date 2017年6月20日
 */
@Controller
@RequestMapping(value = "/manager/order/purchaseTotalPlan")
public class PurchaseTotalPlanController implements ILog {
	
	private ILogger logger = new FileLogger();
	 
	private Map<String,Object> map;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
	@Autowired
	private PurchaseTotalPlanService purchaseTotalPlanService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/orderplan/orderTotalPlanList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_abolished", "0");
		//用户权限
		/*searchParamMap.put("IN_createUserId", user.id);*/

		Page<PurchaseTotalPlanEntity> userPage = purchaseTotalPlanService.getPurchaseTotalPlans(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取计划总量行项目
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "planTotalitem/{planid}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("planid") Long planid, @RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request) throws Exception{
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", planid+"");
		searchParamMap.put("EQ_abolished", "0");
		//明细添加采购组的限制
		searchParamMap.put("IN_purchasingGroup.id", buyerOrgPermissionUtil.getGroupIds());
		Page<PurchaseTotalPlanItemEntity> userPage = purchaseTotalPlanService.getPurchasePlanItems(pageNumber,pageSize,searchParamMap);
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
	@RequestMapping(value = "planTotalItemHead/{planid}")
	@ResponseBody
	public PurchasePlanItemHeadVO getItemHead(@PathVariable("planid") Long planid,@RequestParam("startDate") String  startDate,@RequestParam("endDate") String endDate ) throws Exception{
		return purchaseTotalPlanService.getItemHead(planid,startDate,endDate);
	}
	
	//导出
	@RequestMapping("exportExcel/{totalPlanId}")
	@ResponseBody
	public String download(@PathVariable("totalPlanId") Long totalPlanId,Model model,ServletRequest request,HttpServletResponse  response) throws Exception {
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("预测计划-计划员", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_plan.id", totalPlanId+"");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_vendor.id", user.orgId+"");
		searchParamMap.put("EQ_publishStatus", PurchaseConstans.PUBLISH_STATUS_YES + ""); 
		purchaseTotalPlanService.exportExcel(searchParamMap,totalPlanId,response);
		return null;   
	}
	
	
	
	/**
	 * 导入
	 * @param 
	 * @return
	 */
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
				logger.log("<b><font color='red'>Excel中无数据，请检查文件是否正确... </font></b>");
			}
			//获取表头(关于日期)
		    for(int j = 6 ;j < 30;j++){
		        impDateHeader.add(getCellFormatValue(row.getCell((short) j)).trim());
		     }
	        //对于表头进行验证是否符合当前的时间
	        if(!valiHeaderTransfers(impDateHeader)){
	        	logger.log(" <b><font color='red'>表头被修改（导入模板的时间周期和当前的时间周期不相符）！重新下载该模板！</font></b>");
	        	throw new Exception("表头被修改（导入模板的时间周期和当前的时间周期不相符）！重新下载该模板！");
	        }		
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(PurchaseTotalPlanTransfer.class), getLogger()); 
			List<PurchaseTotalPlanTransfer> list = (List<PurchaseTotalPlanTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0 || list.size() == 1) {
				logger.log("<b><font color='red'> 上传文件为空，或无内容</font></b> ");
				throw new Exception("上传文件为空，或无内容");
			}
			
			int count = list.size() -1;
			//3、组装并保存数据
			log("->数据转换完成共" +  count + " 条，开始构建持久化对象...");
			boolean flag = purchaseTotalPlanService.combinePurchasePlan(list, getLogger());
			
			if(flag) {
				map.put("msg", "导入预测计划总量成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入预测计划总量失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入预测计划总量失败!");  
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
	 * 删除预测计划总量
	 * @param planList
	 * @return
	 */
	@RequestMapping(value = "deleteTotalPlan",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteTotalPlan(@RequestBody List<PurchaseTotalPlanItemEntity> totalPlanItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		purchaseTotalPlanService.deletePlanItem(totalPlanItemList);;
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 模板下载(自动生成二十四周的日期)
	 * @param 
	 * @return
	 */
	@RequestMapping("filesDownload")
	public String filesDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String fileName="采购预测计划总量导入模版";
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(PurchasePlanTransfer.class), null); 		
		String [] handers ={};
		List<String> titleList = new ArrayList<String>();
		titleList.add("工厂编码");
		titleList.add("采购组编码");
		titleList.add("物料号");
		titleList.add("当日库存");
		titleList.add("当日未交PO数量");
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
		handers = titleList.toArray(new String[0]);
		excelUtil.createExcelTemplate(handers);;  
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
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1  
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
