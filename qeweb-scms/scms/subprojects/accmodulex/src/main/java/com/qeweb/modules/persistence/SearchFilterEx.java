package com.qeweb.modules.persistence;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.common.collect.Maps;

public class SearchFilterEx {
	
	public enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE, NEQ,IN,NIN,ISNULL,ISNOTNULL, OR
	}
	
	public enum Ftype {
		DATE
	}
	
	public static final String DATE_FORMAT_YY = "yy";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDD_HH_MM = "yyyyMMdd-HHmm";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_MM_DD_YY_HH_MM_SS = "MM/dd/yy HH:mm:ss";
	
	public static final String QUERY_PERFIX = "::";
	
	public static final String OPERATE_EQ = "=";
	public static final String OPERATE_NEQ = "!=";
	public static final String OPERATE_LT = "<";
	public static final String OPERATE_GT = ">";
	public static final String OPERATE_LTE = "<=";
	public static final String OPERATE_GTE = ">=";
	
	public String fieldName;
	public Object value;
	public Operator operator;
	public String [] fieldNames;
	public Operator [] operators;
	
	public SearchFilterEx(String [] fieldNames, Operator [] operators, Object value) {
		this.fieldNames = fieldNames;
		this.value = value;
		this.operators = operators;
	}
	public SearchFilterEx(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}
	
	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 * 默认为EQ
	 */
	public static Map<String, SearchFilterEx> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilterEx> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value==null){
				continue;
			}
			if (StringUtils.isBlank(value+"")) {
				continue;
			}
			//验证是否是日期类型
			if(juctDate(""+value,"yyyy-MM-dd HH:mm:ss"))
			     {value=Timestamp.valueOf(""+value);}
			else if(juctDate(""+value,"yyyy-MM-dd")){
					//modify by chao.gu 20170808 解决开始时间结束时间选择同一天查询不出的问题
					 if(key.startsWith("GT")){
					   value=Timestamp.valueOf(value+" 00:00:00");
					 }else if(key.startsWith("LT")){
					   value=Timestamp.valueOf(value+" 23:59:59"); 
					 }
					 //modify end
					}
			
			value=juctString(value);
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length > 3) {
				//throw new IllegalArgumentException(key + " is not a valid search filter name");
				
				
				if("OR".equals(names[0])){
					String [] field = new String[(names.length-1)/2];
					Operator [] opt= new Operator[(names.length-1)/2];
					int j = 0;
					for(int i = 1; i < names.length ;){
						opt[j] = Operator.valueOf(names[i++]);
						field[j] = names[i++];
						j++;
					}
					SearchFilterEx filter = new SearchFilterEx(field, opt, value);
					for (int i = 0; i < field.length; i++) {
						
						filters.put(field[i], filter);
					}
				}
				continue;
				
			}else if(names.length == 1){
				//默认为EQ
				names = new String[]{Operator.EQ.toString(),key};
			}else if(names.length==3){
				if(Ftype.DATE.equals(Ftype.valueOf(names[2]))){
					try {
						value = DateUtils.parseDate(String.valueOf(value), DATE_FORMAT_YYYY_MM_DD);
						//key = names[1];
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilterEx filter = new SearchFilterEx(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}
	private static Object juctString(Object value2) {
		String s=""+value2;
		if(s.substring(0, 1).equals("@"))
		{
			s=s.substring(1, s.length());
			return s;
		}
		else
		{
			return value2;
		}
	}

	private static Boolean juctDate(String inDate,String fix){
		
		try{
		    SimpleDateFormat dateFormat = new SimpleDateFormat(fix);
		    if (inDate.trim().length() != dateFormat.toPattern().length()) {
				return false;
			}
		      dateFormat.setLenient(false);
		      
		      dateFormat.parse(inDate.trim());
		      return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 查询过滤，解析输入的查询字符串，如果以‘操作符+分隔符’开头，则进行转换。
	 * @param searchParams
	 * @return
	 */
	public static Map<String, SearchFilterEx> parse_(Map<String, Object> searchParams) {
		Map<String, SearchFilterEx> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank(value+"")) {
				continue;
			}
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length > 2) {
				//throw new IllegalArgumentException(key + " is not a valid search filter name");
			}else if(names.length == 1){
				//默认为EQ
				names = new String[]{Operator.EQ.toString(),key};
			}
			String filedName = names[1];
			String v = value.toString();
			SearchFilterEx filter = null;
			if(v.contains(QUERY_PERFIX)&&(
					v.startsWith(OPERATE_GTE+QUERY_PERFIX)||
					v.startsWith(OPERATE_LTE+QUERY_PERFIX)||
					v.startsWith(OPERATE_NEQ+QUERY_PERFIX)||
					v.startsWith(OPERATE_EQ+QUERY_PERFIX)||
					v.startsWith(OPERATE_LT+QUERY_PERFIX)||
					v.startsWith(OPERATE_GT+QUERY_PERFIX)
					)){
				Operator operator = null;
				if(v.startsWith(OPERATE_GTE+QUERY_PERFIX)){
					operator = Operator.GTE;
					v = v.substring((OPERATE_GTE+QUERY_PERFIX).length());
				}else if(v.startsWith(OPERATE_LTE+QUERY_PERFIX)){
					operator = Operator.LTE;
					v = v.substring((OPERATE_LTE+QUERY_PERFIX).length());
				}else if(v.startsWith(OPERATE_NEQ+QUERY_PERFIX)){
					operator = Operator.NEQ;
					v = v.substring((OPERATE_NEQ+QUERY_PERFIX).length());
				}else if(v.startsWith(OPERATE_EQ+QUERY_PERFIX)){
					operator = Operator.EQ;
					v = v.substring((OPERATE_EQ+QUERY_PERFIX).length());
				}else if(v.startsWith(OPERATE_LT+QUERY_PERFIX)){
					operator = Operator.LT;
					v = v.substring((OPERATE_LT+QUERY_PERFIX).length());
				}else if(v.startsWith(OPERATE_GT+QUERY_PERFIX)){
					operator = Operator.GT;
					v = v.substring((OPERATE_GT+QUERY_PERFIX).length());
				}
				filter = new SearchFilterEx(filedName, operator, v);
			}else{
				Operator operator = Operator.valueOf(names[0]);
				filter = new SearchFilterEx(filedName, operator, value);
			}
			filters.put(key, filter);
		}

		return filters;
	}
}
