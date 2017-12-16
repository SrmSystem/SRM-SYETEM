package com.qeweb.scm.basemodule.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/**
 * 读取简单格式excel工具类
 * @author Vilon
 *
 */
public class SimpleExcelUtil {
	private static Log logger = LogFactory.getLog(SimpleExcelUtil.class);			//日志记录
	/**
	 * 默认读取第一个sheet页
	 * @param t   entity的实例
	 * @param path  excel路径
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readExcel(T t,String path) throws Exception {
		return readExcel(0,t, path);
	}

	/**
	 * @param sheetIndex  sheet的index
	 * @param t   entity的实例
	 * @param path  excel路径
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readExcel(int sheetIndex,T t,String path) throws Exception {
		
		Class<?> clazz = t.getClass();
		InputStream inp = new FileInputStream(path);
		Workbook wb = WorkbookFactory.create(inp);
		List<T> list = new ArrayList<T>();
		try {
			logger.info("->解析Excel开始...");
			Sheet sheet = wb.getSheetAt(sheetIndex);
			int columnNum = sheet.getRow(0).getLastCellNum();
			if (columnNum == 0) {
				logger.info("Excel中无数据，请检查文件是否正确...");
				return list;
			}
	
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (Row row : sheet) {
				T t_ = (T) ClassUtil.newInstance(clazz);
				if(row.getRowNum()==0){
					logger.info("->开始解析列头 .");
					for (int i = 0; i < columnNum; i++) {
						Cell cell = row.getCell(i);
						if(cell.getCellComment()==null||StringUtils.isEmpty(cell.getCellComment().toString())){
							continue;
						}
						String cs = cell.getCellComment().getString().toString();
						map.put(i, cs);
					}
					continue;
				}
				logger.info("->开始解析第 " + row.getRowNum() + "行.");  
				for (int i :map.keySet()) {
					Cell cell = row.getCell(i);
					Field field = clazz.getDeclaredField(map.get(i));
					Method method = clazz.getDeclaredMethod(getMethodNameByFieldName(field.getName()),field.getType());
					if(cell==null){
						//method.invoke(t_,null);
					}else{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						method.invoke(t_,convert(field, cell.getStringCellValue()==null?null:cell.getStringCellValue().trim()));
					}
				}
				list.add(t_);
			}
		} finally {
			if(inp!=null){
				inp.close();
			}
			if(wb!=null){
				wb.close();
			}
		}
		return list;
	}
	/**
	 * 根据字段名，得到set方法名
	 * @param fieldName
	 * @return
	 */
	private static String getMethodNameByFieldName(String fieldName){
		if(fieldName.length()==1){
			return "set" + fieldName.toUpperCase();
		}else{
			return "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
		}
	}
	/**
	 * 值的类型转换
	 * @param field
	 * @param cellValue
	 * @return
	 */
	private static Object convert(Field field, String cellValue) {
		if(field == null) {
			return cellValue;
		}
		
		Class<?> type = field.getType();
		if (type.equals(int.class) || type.equals(Integer.class)) {
            return StringUtils.convertToInt(cellValue);
        }
        if (type.equals(long.class) || type.equals(Long.class)) {
            return StringUtils.convertToLong(cellValue);
        }
        if (type.equals(float.class) || type.equals(Float.class)) {
            return StringUtils.convertToFloat(cellValue);
        }
        if (type.equals(double.class) || type.equals(Double.class)) {
            return StringUtils.convertToDouble(cellValue);
        }
        if(type.equals(Date.class) || type.equals(Timestamp.class)) {
        	return DateUtil.stringToTimestamp(cellValue);
        } 
		
		return cellValue;
	}
	
}
