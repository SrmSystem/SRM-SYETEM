package com.qeweb.scm.basemodule.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Excel 转换注解解析
 * @author ALEX
 *
 */
public class ExcelAnnotationReader {
	
	private int start = 0;		//开始行
	private String desc = "";	//标头
	
	private Class<?> clazz;		//类
	private Map<String, Field> fieldMap = new HashMap<String, Field>();
	
	public ExcelAnnotationReader(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * @return
	 */
	public Map<Integer, Map<String, Annotation>> getAnnotationMap() {
		Map<Integer, Map<String, Annotation>> annoMap = new HashMap<Integer, Map<String, Annotation>>();
		ExcelTransfer annoTransfer = (ExcelTransfer) clazz.getAnnotation(ExcelTransfer.class);
		if(annoTransfer != null) {
			setStart(annoTransfer.start());
			setDesc(annoTransfer.describe()); 
		}
		
		ExcelCol annoCol = null;
		Map<String, Annotation> fieldAnnoMap = null;
		for(Field field : clazz.getDeclaredFields()){
			  annoCol =  field.getAnnotation(ExcelCol.class);
			  if(annoCol == null) 
				  continue;
			  
			  fieldAnnoMap = new HashMap<String, Annotation>();
			  fieldAnnoMap.put(field.getName(), annoCol);
			  annoMap.put(annoCol.column().val(), fieldAnnoMap);
			  getFieldMap().put(field.getName(), field);
		  }
		
		return annoMap;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Field> fieldMap) {
		this.fieldMap = fieldMap;
	}

}
