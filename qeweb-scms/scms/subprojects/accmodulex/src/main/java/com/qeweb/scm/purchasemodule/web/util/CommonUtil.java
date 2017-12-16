package com.qeweb.scm.purchasemodule.web.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.quartz.AbstractJobBean;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FTPUtil;
import com.qeweb.scm.purchasemodule.constants.CommonConstant;

/**
 * 通用工具类
 * @author zhangjiejun
 */
public abstract class CommonUtil extends AbstractJobBean implements ModelCountUtil,  CommonConstant{
	
	/**
	 * 日志打印
	 * @param message	要打印的数据
	 */
	public static void logger(Object message) {
		logger.log(message);
	}
	
	/**
	 * 流水号自动补零
	 * @param 	count			数字
	 * @param 	countLength		流水号长度
	 * @return	res				流水号
	 */
	public static String fillCount(Integer count, int countLength) {
		String res = count + "";
		int length = count.toString().length();
		if(length >= countLength) {
			return res;
		}
		for(int i = 0; i < countLength - length; i++){
			res = "0" + res;
		}
		return res;
	}
	
	/**
	 * 流水号自动补零(0 代表前面补充0, countLength代表长度, d 代表参数为正数型)
	 * @param 	count			数字
	 * @param 	countLength		流水号长度
	 * @return	res				流水号
	 */
	public static String fillCount2(Integer count, int countLength) {
		String res = String.format("%0" + countLength + "d", count);
		return res;
	}
	

	
	/**
	 * 通过ftpUtil获取文件到本地路劲下
	 * @param 	downloadPath	服务器下载地址
	 * @param 	ftpFileName		文件名
	 * @param 	localDir		本地下载路劲
	 * @return	下载完成状态
	 */
	public static boolean getFtpFile(String downloadPath, String ftpFileName, String localDir){
		logger("get ftp file..."); 
		FTPUtil ftpUtil = new FTPUtil();
		return ftpUtil.down(downloadPath, ftpFileName, localDir);
	}
	
	/**
	 * 获取tempUrl
	 * @param 	folder		拼接的文件夹路劲
	 * @return	tempUrl		文件路劲
	 */
	public static String getTempUrl(String folder) {
		String tempUrl = PropertiesUtil.getProperty("file.dir", "");			//获取project.properties中配置的临时路径
		tempUrl += folder;
		File tempFile = new File(tempUrl);										//不存在，则创建
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		logger("tempFile == " + tempFile);
		return tempUrl;
	}
	
	/**
	 * 复制对象
	 * @param 	object	传入的对象
	 * @return	复制好的对象
	 * @throws 	Exception
	 */
	public static Object copy(Object object) throws Exception {
		Class<?> classType = object.getClass();
		Object objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];String fieldName = field.getName();
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = "get" + firstLetter + fieldName.substring(1);
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			Method getMethod = classType.getMethod(getMethodName,new Class[] {});
			Method setMethod = classType.getMethod(setMethodName,new Class[] { field.getType() });
			Object value = getMethod.invoke(object, new Object[] {});
			setMethod.invoke(objectCopy, new Object[] { value });
		}
		return objectCopy;
	}

//	public static int getVoucher(VoucherDao voucherDao) {
//		VoucherEntity voucherEntity = voucherDao.findLastVoucherEntity();	//获取自增的队列号对象
//		int voucher = 0;
//		if(voucherEntity != null){
//			voucher = (int) voucherEntity.getVoucher();						//不为null，则获取DB中保存的数量
//		}
//		voucher++;
//		voucherEntity = new VoucherEntity();
//		voucherEntity.setVoucher(voucher);
//		voucherDao.save(voucherEntity);										//有效数据，则添加自增队列号
//		return voucher;
//	}
	
	/**
	 * 初始化查询时间
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	public static Map<String, Object> initSearchTime(Map<String, Object> searchParamMap, String key, int type) {
		String time = (String) searchParamMap.get(key);
		if(time!=null && !time.isEmpty()){			//时间不为空，则自动添加时分秒后缀
			if(type == 0){
				time += " 00:00:00";				//开始时间
			}else{
				time += " 23:59:59";				//结束时间
			}
			searchParamMap.put(key, Timestamp.valueOf(time));
		}
		return searchParamMap;
	}
	
	
	/**
	 * 初始化查询时间
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	public static Map<String, Object> initRequestTime(Map<String, Object> searchParamMap, String key, int type) {
		String time = (String) searchParamMap.get(key);
		if(time!=null && !time.isEmpty()){			//时间不为空，则自动添加时分秒后缀
			if(type == 0){
				time += " 00";				//开始时间
			}else{
				time += " 23";				//结束时间
			}
			try {
				searchParamMap.put(key, DateUtil.parseUtilDate(time, new String[]{DateUtil.DATE_FORMAT_YYYY_MM_DD_HH}));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return searchParamMap;
	}
	
	/**
	 * 初始化查询时间为String类型
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	public static Map<String, Object> initStringTime(Map<String, Object> searchParamMap, String key, int type) {
		String time = (String) searchParamMap.get(key);
		if(time!=null && !time.isEmpty()){
			if(type == 0){
				time += " 00:00:00";				//开始时间
			}else if(type==1){
				time += " 23:59:59";				//结束时间
			}else if(type==2){
				time=time;
			}
			searchParamMap.put(key,"@"+ time);
		}
		return searchParamMap;
	}
	
	/**
	 * String的数值自动判空，空值返回空字符串
	 * @param 	field	传入的数值
	 * @return	判断后的数值
	 */
	public static String getIfNullField(String field) {
		if(field == null || field.equals("null") || field.equals("")){
			field = "";
		}
		return field;
	}
	
	/**
	 * Double的数值自动判空，空值返回0.0
	 * @param 	count	传入的数值
	 * @return	判断后的数值
	 */
	public static double getIfNullCount(Double count) {
		if(count == null || count.equals("null") || count.equals("")){
			count = 0.0;
		}
		return count;
	}
	
	/**
	 * 发送邮件
	 * @param toEmail	收件邮箱
	 * @param toPerson	收件人
	 * @param title		标题
	 * @param msg		内容
	 */
	public static void sendMail(String toEmail, String toPerson, String title, String msg) {
		if(toEmail != null && !toEmail.equals("null") && !toEmail.equals("")){
			Map<String, String> params = new HashMap<String, String>();
			logger("send toPerson..." + toPerson);
			logger("send mail..." + toEmail);
			logger("send title is " + title);
			logger("send msg is " + msg);
			params.put("vendorName", toPerson);
			params.put("tempMessage", msg);
			params.put("signText", "管理员/System Manager<br/>");
			params.put("link", "http://"+PropertiesUtil.getProperty("project.url"));
			MailObject mail = new MailObject();
			mail.toMail = toEmail;
			mail.title = title;
			mail.templateName = "defaultTemp";
			mail.params = params;
			MailSendService mailSendService = SpringContextUtils.getBean("mailSendService");
			mailSendService.send(mail, 88);
		}else{
			logger("the mail is null");
		}
	}
	
	/**
	 * 发送邮件
	 * @param toEmail	收件邮箱
	 * @param toPerson	收件人
	 * @param title		标题
	 * @param msg		内容
	 * @param type		EN则为英文模板
	 */
	public static void sendMail_LG(String toEmail, String toPerson, String title, String msg, String type) {
		if(toEmail != null && !toEmail.equals("null") && !toEmail.equals("")){
			Map<String, String> params = new HashMap<String, String>();
			logger("send toPerson..." + toPerson);
			logger("send mail..." + toEmail);
			logger("send title is " + title);
			logger("send msg is " + msg);
			params.put("vendorName", toPerson);
			params.put("tempMessage", msg);
//			params.put("signText", "管理员/System Manager<br/>");
			params.put("link", "http://"+PropertiesUtil.getProperty("project.url"));
			MailObject mail = new MailObject();
			mail.toMail = toEmail;
			mail.title = title;
			if(type.trim().equalsIgnoreCase("EN")){						//英文
				params.put("signText", "System Manager<br/>");
				mail.templateName = "mailSendTemp";
			}else{
				params.put("signText", "管理员<br/>");
				mail.templateName = "defaultTemp";
			}
			mail.params = params;
			MailSendService mailSendService = SpringContextUtils.getBean("mailSendService");
			mailSendService.send(mail, 88);
		}else{
			logger("the mail is null");
		}
	}
	
	
	/**
	 * 发送邮件
	 * @param toEmail	收件邮箱
	 * @param toPerson	收件人
	 * @param title		标题
	 * @param msg		内容
	 * @param request	request自动判断模板
	 */
	public static void sendMail_LG_request(String toEmail, String toPerson, String title, String msg, HttpServletRequest request) {
		if(toEmail != null && !toEmail.equals("null") && !toEmail.equals("")){
			Map<String, String> params = new HashMap<String, String>();
			logger("send toPerson..." + toPerson);
			logger("send mail..." + toEmail);
			logger("send title is " + title);
			logger("send msg is " + msg);
			params.put("vendorName", toPerson);
			params.put("tempMessage", msg);
			params.put("link", "http://"+PropertiesUtil.getProperty("project.url"));
			MailObject mail = new MailObject();
			mail.toMail = toEmail;
			mail.title = title;
//			Locale local = request.getLocale();
			Locale local = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
			logger("local == " + local);
//			logger("local2 == " + RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
			logger(local.equals(Locale.ENGLISH));
			if(local.equals(Locale.ENGLISH)){						//英文
				params.put("signText", "System Manager<br/>");
				mail.templateName = "mailSendTemp";
			}else{
				params.put("signText", "管理员<br/>");
				mail.templateName = "defaultTemp";
			}
			mail.params = params;
			MailSendService mailSendService = SpringContextUtils.getBean("mailSendService");
			mailSendService.send(mail, 88);
		}else{
			logger("the mail is null");
		}
	}
	
	/**
	 * 判断是否为null，或者字符串的null，或者是空字符串
	 * @param 	obj		传入的对象
	 * @return	boolean	是否状态
	 */
	public static boolean isNotNullAndNotEmpty(Object obj){
		if(obj != null && !obj.equals("null") && !obj.equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断集合是否为空
	 * @param 	list	传入的集合
	 * @return	boolean	是否状态
	 */
	public static boolean isNotNullCollection(Collection<?> list){
		if(list != null && list.size() != 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断集合是否为空
	 * @param 	list	传入的集合
	 * @return	boolean	是否状态
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotNullMap(Map map){
		if(map != null && map.size() != 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 初始化时间戳
	 * @param 	cycleName	周期名称
	 * @return				时间戳
	 * @throws 	Exception	异常
	 */
	public static Timestamp[] initTimes(String cycleName) throws Exception {
		Timestamp[] times = null;
		Date date =  new Date();
		if(cycleName.equals(COMMON_WEEK)){										//自动计算周期为周的时间戳
			times = DateUtil.getBeginAndEndTimes_week(-1);
		}else if(cycleName.equals(COMMON_MONTH)){								//自动计算周期为月的时间戳
			times = DateUtil.getBeginAndEndTimes_month(-1, date);
		}else if(cycleName.equals(COMMON_QUARTER)){								//自动计算周期为季度的时间戳
			times = DateUtil.getBeginAndEndTimes_quarter(-1, date);
		}else if(cycleName.equals(COMMON_HALF_YEAR)){							//自动计算周期为半年的时间戳
			times = DateUtil.getBeginAndEndTimes_halfYear(-1, date);
		}else if(cycleName.equals(COMMON_YEAR)){								//自动计算周期为年的时间戳
			times = DateUtil.getBeginAndEndTimes_year(-1, date);
		}else if(cycleName.equals(COMMON_DAY)){									//自动计算周期为天的时间戳
			times = DateUtil.getBeginAndEndTimes_day(-1);
		}
		return times;
	}
	
	/**
	 * 初始化默认一周的时间周期
	 * @param 	showMonth		显示默认一周的状态
	 * @param 	date			时间
	 * @param 	searchParamMap	条件查询map
	 * @return					获取到的默认一周的时间周期
	 */
	public static Map<String, Object> initDefaultOneWeek(String showWeek, Date date, Map<String, Object> searchParamMap, String sortType) {
		if(!CommonUtil.isNotNullAndNotEmpty(showWeek)){
			String startTime = DateUtil.dateToString(DateUtil.addDay(date, -7), 
					DateUtil.DATE_FORMAT_YYYY_MM_DD) + " 00:00:00";
			String endTime = DateUtil.dateToString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD) + " 23:59:59";
			logger.log("startTime == " + startTime + ", endTime == " + endTime);
			try {
				searchParamMap.put("GTE_" + sortType, Timestamp.valueOf(startTime)); 
				searchParamMap.put("LTE_" + sortType, Timestamp.valueOf(endTime)); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return searchParamMap;
	}
	
	/**
	 * 初始化默认一个月的时间周期
	 * @param 	showMonth		显示默认一月的状态
	 * @param 	date			时间
	 * @param 	searchParamMap	条件查询map
	 * @return					获取到的默认一个月的时间周期
	 */
	public static Map<String, Object> initDefaultOneMonth(String showMonth, Date date, Map<String, Object> searchParamMap, String sortType) {
		if(!CommonUtil.isNotNullAndNotEmpty(showMonth)){
//			if(showMonth.equals("true")){
				String startTime = DateUtil.dateToString(DateUtil.addMonth(date, -1), DateUtil.DATE_FORMAT_YYYY_MM_DD) + " 00:00:00";
				String endTime = DateUtil.dateToString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD) + " 23:59:59";
				logger.log("startTime == " + startTime + ", endTime == " + endTime);
//				Timestamp[] times = null;
				try {
//					times = DateUtil.getBeginAndEndTimes_month(-1);
//					times = DateUtil.getBeginAndEndTimes_month(-1, new Date());
//					searchParamMap.put("GTE_createTime", times[0]); 
//					searchParamMap.put("LTE_createTime", times[1]); 
//					searchParamMap.put("GTE_createTime", Timestamp.valueOf(startTime)); 
//					searchParamMap.put("LTE_createTime", Timestamp.valueOf(endTime)); 
					searchParamMap.put("GTE_" + sortType, Timestamp.valueOf(startTime)); 
					searchParamMap.put("LTE_" + sortType, Timestamp.valueOf(endTime)); 
//					logger.log(times[0] + " -- " + times[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
		}
		return searchParamMap;
	}
//	
//	/**
//	 * 过滤已经发送过的邮件
//	 * @param remindMap		邮件提醒map
//	 * @param remindKey		邮件提醒key
//	 * @return				邮件提醒map
//	 */
//	public static Map<String, Boolean> filterRemindedObject(Map<String, Boolean> remindMap, String remindKey) {
//		if(remindMap.containsKey(remindKey)){			//如果提醒map包含key，则表示该计划已经发过邮件，直接略过
//			logger.log("remindMap contains remindKey == " + remindKey + ", so continue...");
//			remindMap.put(remindKey, true);
//		}else{
//			logger.log("remindMap donsen't contains remindKey == " + remindKey + ", so put in remindMap...");
//			remindMap.put(remindKey, false);
//		}
//		return remindMap;
//	}
	
//	
//	/**
//	 * 过滤已经发送过的邮件
//	 * @param remindMap		邮件提醒map
//	 * @param remindKey		邮件提醒key
//	 * @return				邮件提醒map
//	 */
//	public static Map<String, Boolean> filterRemindedObject(String remindKey) {
//		if(remindMap.containsKey(remindKey)){			//如果提醒map包含key，则表示该计划已经发过邮件，直接略过
//			logger.log("remindMap contains remindKey == " + remindKey + ", so continue...");
//			remindMap.put(remindKey, true);
//		}else{
//			logger.log("remindMap donsen't contains remindKey == " + remindKey + ", so put in remindMap...");
//			remindMap.put(remindKey, false);
//		}
//		return remindMap;
//	}
	
	

	/**
	 * 初始化确认状态
	 * @param exConfirmStatus	确认状态
	 * @return					确认状态
	 */
	public static String initExConfirmStatus(Integer exConfirmStatus) {
		String res = "";
		if(exConfirmStatus == 0){
			res = "待确认";
		}else if(exConfirmStatus == 1){
			res = "已确认";
		}else if(exConfirmStatus == 2){
			res = "部分确认";
		}else if(exConfirmStatus == -1){
			res = "已驳回";
		}
		return res;
	}
	
	/**
	 * excel刷新有公式的单元格
	 * @param workbook	excel的IO流
	 * @param sheet		excel的sheet
	 */
	public static void refreshCellData(Workbook workbook,Sheet sheet){
		logger.log("method refreshCellData start");
        FormulaEvaluator eval = null; 				//初始化公式刷新
        if(workbook instanceof HSSFWorkbook) 		//2003版本
            eval = new HSSFFormulaEvaluator((HSSFWorkbook) workbook); 
        else if(workbook instanceof XSSFWorkbook) 	//2007版本
            eval = new XSSFFormulaEvaluator((XSSFWorkbook) workbook); 
//        Cell c = null; 
//        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
//        	Row r = sheet.getRow(j);				//获取行
//        	if(r != null){
//        		for(int i = r.getFirstCellNum(); i < r.getLastCellNum(); i++){
//        			c = r.getCell(i); 
//        			if(c != null && c.getCellType() == Cell.CELL_TYPE_FORMULA) {
//        				eval.evaluateFormulaCell(c);//刷新公式
//        			}
//        		}
//        	}
//        }
        eval.evaluateAll();							//自动刷新所有公式
        logger.log("method refreshCellData end");
    }

	/**
	 * 判断grade级别
	 * @param score	分数
	 * @return		grade级别
	 */
	public static String initGeade(double score) {
		String grade = "D";
		if(score >= 60 && score <= 69){
			grade = "C";
		}else if(score >= 70 && score <= 89){
			grade = "B";
		}else if(score >= 90 && score <= 100){
			grade = "A";
		}
		return grade;
	}
	
	
	
	/**
	 * 初始化查询时间成为Date类型
	 * @param 	searchParamMap	条件map
	 * @param 	key	 			条件map的key
	 * @param 	type			类型(0:开始时间，1:结束时间)
	 * @return	searchParamMap	更新后的条件map
	 */
	public static Map<String, Object> initCreateTime(Map<String, Object> searchParamMap, String key, int type) {
		String time = (String) searchParamMap.get(key);
		if(CommonUtil.isNotNullAndNotEmpty(time)){			//时间不为空，则自动添加时分秒后缀
			try {
				searchParamMap.put(key,DateUtil.parseUtilDate(time, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return searchParamMap;
	}

	/**
	 * 拼接日志
	 * @param 	flag	新日志
	 * @param 	log		原日志
	 * @return			拼接好的日志
	 */
	public static String getAppendLog(String flag, String log) {
		if (log == null || log.equals("null")) {
			log = flag;
		} else {
			log += "</br>" + flag;
		}
		return log;
	}

	/**
	 * 获取周几
	 * @param dayIndex	周下标
	 * @return			周几
	 */
	public static Timestamp getDayOfWeek(int dayIndex) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, dayIndex);			//获取时间
		Date assess = cal.getTime();
		Timestamp day = DateUtil.stringToTimestamp(DateUtil.dateToString(assess));
		return day;
	}
}
