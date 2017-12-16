package com.qeweb.scm.basemodule.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




/**
 * 日期工具类
 *
 */
public class DateUtil {

	public static final String DATE_FORMAT_YY = "yy";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDD_HH_MM = "yyyyMMdd-HHmm";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_MM_DD_YY_HH_MM_SS = "MM/dd/yy HH:mm:ss";
	public static final String DATE_FORMAT_YYYY_MM_DD_ = "yyyy/MM/dd";
	
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 

	/**
	 * 跟据模版返回sql.date时间
	 *
	 * @param str
	 * @param parsePatterns
	 * @return
	 */
	public static Date parseSqlDate(String str, String[] parsePatterns) {

		java.sql.Date sdate = null;
		try {
			java.util.Date udate = org.apache.commons.lang3.time.DateUtils.parseDate(str, parsePatterns);
			sdate = new java.sql.Date(udate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdate;

	}

	/**
	 * 跟据模版返回util.date时间
	 *
	 * @param str
	 * @param parsePatterns
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date parseUtilDate(String str,
			String... parsePatterns) throws ParseException {
		if(str.length() > parsePatterns[0].length()) {
			str = str.substring(0, parsePatterns[0].length());
		}
		java.util.Date udate = org.apache.commons.lang3.time.DateUtils.parseDate(str, parsePatterns);

		return udate;
	}

	/**
	 * 返回yyyy-MM-dd 格式的util.date时间
	 *
	 * @param str
	 * @return
	 */
	public static java.util.Date parseUtilDate(String str) {
		try {
			return StringUtils.isEmpty(str) ? null : parseUtilDate(str, new String[]{DATE_FORMAT_YYYY_MM_DD});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前时间Timestamp
	 *
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取Timestamp
	 * @param str
	 * @return
	 */
	public static Timestamp getTimestamp(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}

		//yyyy-MM-dd HH:mm:ss 格式
		if(str.length() > 10){
			if(str.length() == 13) {
				return Timestamp.valueOf(str + ":00:00");
			} else if(str.length() == 16) {
				return Timestamp.valueOf(str + ":00");
			} else {
				return Timestamp.valueOf(str);
			}
		}
		//yyyy
		else if(str.length() == 4) {
			return Timestamp.valueOf(str + "-01-01 00:00:00");
		}
		//yyyy-mm
		else if(str.length() == 7) {
			return Timestamp.valueOf(str + "-01 00:00:00");
		}
		else {
			return Timestamp.valueOf(str + " 00:00:00");
		}
	}

	/**
	 * 将Date类型日期转化成String类型"任意"格式
	 * java.sql.Date,java.sql.Timestamp类型是java.util.Date类型的子类
	 *
	 * @param date
	 *            Date
	 * @param format
	 *            String "2003-01-01"格式 "yyyy年M月d日" "yyyy-MM-dd HH:mm:ss"格式
	 * @return String
	 * @author sbt 2007-09-02
	 */
	public static String dateToString(java.util.Date date, String format) {
		if (date == null || format == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 获得当前时间并转换成yyyyMMdd-HHmm格式
	 * @return
	 */
	public static String getNowStr() {
		return dateToString(new java.util.Date(), DATE_FORMAT_YYYYMMDD_HH_MM);
	}

	/**
	 * 将Date类型日期转化成String类型:yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date) {
		return dateToString(date, DATE_FORMAT_YYYY_MM_DD);
	}

	/**
	 * 字符串转时间
	 * @param dateStr
	 * @return
	 */
	public static Timestamp stringToTimestamp(String dateStr) {
		Timestamp time = stringToTimestamp(dateStr, DATE_FORMAT_YYYY_MM_DD);
		if(time==null){
			time = stringToTimestamp(dateStr, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		}
		return time;
	}

	public static Timestamp stringToTimestamp(String dateStr, String fmt) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}

		if (StringUtils.isEmpty(fmt)) {
			return null;
		}

		DateFormat format = new SimpleDateFormat(fmt);
		java.util.Date d = null;
		try {
			d = format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}

		return new Timestamp(d.getTime());
	}

	/**
	 * 获取当前时间毫秒
	 * @return
	 */
	public static long getCurrentTimeInMillis(){
		Calendar c = new GregorianCalendar();
		return c.getTimeInMillis();
	}
	
	/**
	 * 获取日期的年份
	 * @param date
	 * @return
	 */
	public static int getDateYear(Timestamp date) {
		Calendar c = new GregorianCalendar();   
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * 获取日期月份
	 * @param date
	 * @return
	 */
	public static int getDateMonth(Timestamp date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	/**
	 * 获取当前年
	 */
	public static int getCurrentYear(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月
	 */
	public static int getCurrentMonth(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日期
	 * @return
	 */
	public static int getCurrentDate(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.DATE);
	}

	/**
	 * 获取当前小时
	 * @return
	 */
	public static int getCurrentHour(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取当前分钟
	 * @return
	 */
	public static int getCurrentMinute(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.MINUTE);
	}
	
	/**
	 * 获取当前秒
	 * @return
	 */
	public static int getCurrentSecond(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.SECOND);
	}

	/**
	 * 获取当前季度
	 */
	public static int getCurrentQuarter(){
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.MONTH) / 3 + 1;
	}

	/**
	 * 获取上个月对应的年份
	 * @return
	 */
	public static int getYearOfLastMonth(){
		return getCurrentMonth() - 1 == 0 ? getCurrentYear() - 1 : getCurrentYear();
	}

	/**
	 * 获取上个季度对应的年份
	 * @return
	 */
	public static int getYearOfLastQuarter(){
		return getCurrentQuarter() - 1 == 0 ? getCurrentYear() - 1 : getCurrentYear();
	}

	/**
	 * 获取上个月
	 * @return
	 */
	public static int getLastMonth(){
		return getCurrentMonth() - 1 == 0 ? 12 : getCurrentMonth() - 1;
	}

	/**
	 * 获取上个季度
	 * @return
	 */
	public static int getLastQuarter(){
		return getCurrentQuarter() - 1 == 0 ? 4 : getCurrentQuarter() - 1;
	}

	/**
	 * 获取上一年
	 * @return
	 */
	public static int getLastYear(){
		return getCurrentYear() - 1;
	}

	/**
	 * 获取指定时间的前几天或后几天
	 * @param date
	 * @param day  待加减的天数
	 * @return
	 */
	public static java.util.Date addDay(java.util.Date date, int day) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DATE, day);
		return rightNow.getTime();
	}
	
	/**
	 * 获取指定时间的前几月或后几月
	 * @param date
	 * @param month  待加减的月数
	 * @return
	 */
	public static java.util.Date addMonth(java.util.Date date, int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, month);
		return rightNow.getTime();
	}
	
	public static Timestamp[] getBeginAndEndTimes_year(int addYears) throws Exception{
		return getBeginAndEndTimes_year(addYears, new Date());
	}
	
	/**
	 * 取得年的开始时间和结束时间
	 * @param addYears 相对年份。例如：0为当前年，1为下一年，-1为上一年
	 * @return 开始时间和结束时间的数组
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_year(int addYears,Date date) throws Exception{
		Calendar c = Calendar.getInstance();
		if(date!=null){
			c.setTime(date);
		}
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, 0);
    	c.add(Calendar.YEAR, addYears);
    	Date beginTime = sdf.parse(sdf.format(c.getTime()));
        c.add(Calendar.YEAR, 1);
        Date endTime = sdf.parse(sdf.format(c.getTime()));
        return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}
	
	/**
	 * 取得半年的开始时间和结束时间
	 * @param addHalfYears 相对半年。例如：0为当前的半年，1为下一个半年，-1为上一个半年
	 * @return 开始时间和结束时间的数组
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_halfYear(int addHalfYears,Date date) throws Exception{
		Calendar c = Calendar.getInstance();
		if(date!=null){
			c.setTime(date);
		}
		Date beginTime = null;
		Date endTime = null;
		if(c.get(Calendar.MONTH)<=5){
        	c.set(Calendar.MONTH, 0);
        }else{
        	c.set(Calendar.MONTH, 6);
        }
		c.set(Calendar.DATE, 1);
		c.add(Calendar.MONTH, 6*addHalfYears);
		beginTime = sdf.parse(sdf.format(c.getTime()));
		c.add(Calendar.MONTH, 6);
		endTime = sdf.parse(sdf.format(c.getTime()));
		return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}
	
	public static Timestamp[] getBeginAndEndTimes_month(int addMonths) throws Exception{
		return getBeginAndEndTimes_month(addMonths, new Date());
	}
	
	/**
	 * 取得月的开始时间和结束时间
	 * @param addMonths 相对月份。例如：0为当前月份，1为下月，-1为上月
	 * @return 开始时间和结束时间的数组
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_month(int addMonths,Date date) throws Exception{
		Calendar c = Calendar.getInstance();
		if(date!=null){
			c.setTime(date);
		}
		Date beginTime = null;
		Date endTime = null;
		c.add(Calendar.MONTH, addMonths);
    	c.set(Calendar.DATE, 1);
    	beginTime = sdf.parse(sdf.format(c.getTime()));
    	c.add(Calendar.MONTH, 1);
    	endTime = sdf.parse(sdf.format(c.getTime()));
        return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}
	
	public static Timestamp[] getBeginAndEndTimes_quarter(int addQuarters) throws Exception{
		return getBeginAndEndTimes_quarter(addQuarters, new Date());
	}
	/**
	 * 取得季度的开始时间和结束时间
	 * @param addQuarters 相对季度。例如：0为当前季度，1为下季度，-1为上季度
	 * @return 开始时间和结束时间的数组
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_quarter(int addQuarters,Date date) throws Exception{
		Calendar c = Calendar.getInstance();
		if(date!=null){
			c.setTime(date);
		}
		Date beginTime = null;
		Date endTime = null;
    	c.set(Calendar.DATE, 1);
    	c.add(Calendar.MONTH, -DateUtil.getCurrentMonth()%3+1+3*addQuarters);
    	beginTime = sdf.parse(sdf.format(c.getTime()));
    	c.add(Calendar.MONTH, 3);
    	endTime = sdf.parse(sdf.format(c.getTime()));
        return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}

	/**
	 * 获取当前时间是一周的第几天
	 * @return
	 */
	public static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}
	
	/**
	 * 取得周的开始时间和结束时间
	 * @param addweek 相对周。例如：0为当前周，1为下周，-1为上周
	 * @return 开始时间和结束时间的数组
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_week(int addweek) throws Exception {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + addweek * 7);
		Date beginTime = currentDate.getTime();
		currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + addweek * 7 + 6);
        Date endTime =  currentDate.getTime();
		return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}

	/**
	 * 
	 * @param addMonths
	 * @return
	 * @throws Exception
	 */
	public static Timestamp[] getBeginAndEndTimes_day(int addDays) throws Exception{
		Calendar c = Calendar.getInstance();
		Date beginTime = null;
		Date endTime = null;
    	c.add(Calendar.DATE, addDays);
    	beginTime = sdf.parse(sdf.format(c.getTime()));
    	c.add(Calendar.DATE, 1);
    	endTime = sdf.parse(sdf.format(c.getTime()));
        return new Timestamp[]{new Timestamp(beginTime.getTime()), new Timestamp(endTime.getTime())};
	}
	
    /**
     * 检查给定的日期是否在两个日期中间
     *
     * @param current compare date
     * @param min min date
     * @param max max date
     * @return if between min date and max date, then return true.
     */
    public static boolean between(Date current, Date min, Date max) {
        return current.after(min) && current.before(max);
    }

    /**
     * 获取前几个月的开始结束时间
     * @param addMonth
     * @param day
     * @return
     * @throws ParseException
     */
    public static Timestamp getMothBefore(int addMonth, int day) throws ParseException {
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MONTH, addMonth);
    	calendar.set(Calendar.DAY_OF_MONTH, day);
    	return new Timestamp(sdf.parse(sdf.format(calendar.getTime())).getTime());
    }
    

    /**
     * 将日期字符串转换成数据库时间戳 yyyy-MM-dd
     * @param dates 时间
     * @return
     * @throws ParseException 
     */
    public static Timestamp parseTimeStamp(String dates) throws ParseException {
    	return  parseTimeStamp(dates,null);
    }
    /**
     * 将日期字符串转换成数据库时间戳
     * 如果没有格式采用默认格式 yyyy-MM-dd
     * @param dates 时间
     * @return
     * @throws ParseException 
     */
	public static Timestamp parseTimeStamp(String dates,String pattern) throws ParseException {
		if(StringUtils.isEmpty(pattern)){
			pattern = DATE_FORMAT_YYYY_MM_DD;
		}
		Date date = parseUtilDate(dates, pattern);
		return new Timestamp(date.getTime());
	}
   
}

