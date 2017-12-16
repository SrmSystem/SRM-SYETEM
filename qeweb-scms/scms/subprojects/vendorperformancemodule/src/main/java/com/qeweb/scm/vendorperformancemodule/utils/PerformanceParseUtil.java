package com.qeweb.scm.vendorperformancemodule.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceTypeConstant;

/**
 * 绩效转换工具类
 * @author pjjxiajun
 * @date 2015年9月14日
 * @path com.qeweb.scm.vendorperformancemodule.utils.PerformanceParseUtil.java
 */
public class PerformanceParseUtil {
	
	/**
	 * 解析公式
	 * @param indexFormula
	 * @return
	 */
	public static Map<String,List<String>> parseFormulaParams(String indexFormula){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		//解析要素和元素
		try{
		Pattern p = Pattern.compile("\\[(.+?)\\]");
		Matcher m = p.matcher(indexFormula);
		List<String> keyList = new ArrayList<String>();
 		while(m.find()){
			keyList.add(m.group(1));
		}
 		map.put("keyList", keyList);
 		p = Pattern.compile("\\{(.+?)\\}");
		m = p.matcher(indexFormula);
		List<String> elementList = new ArrayList<String>();
		while(m.find()){
			elementList.add(m.group(1));
		}
		map.put("elementList", elementList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static Timestamp[] parseAssessTime(String cycleCode,Integer type) throws Exception{
		Timestamp[] dateArray = null;
		if(PerformanceTypeConstant.PER_CYCLE_MONTH.equals(cycleCode)){
			//如果周期为月
			dateArray = DateUtil.getBeginAndEndTimes_month(type,null);
		}else if(PerformanceTypeConstant.PER_CYCLE_QUARTER.equals(cycleCode)){
			//如果周期为季度
			dateArray = DateUtil.getBeginAndEndTimes_quarter(type,null);
		}else if(PerformanceTypeConstant.PER_CYCLE_HALFYEAR.equals(cycleCode)){
			//如果周期为半年
			dateArray = DateUtil.getBeginAndEndTimes_halfYear(type,null);
		}else if(PerformanceTypeConstant.PER_CYCLE_YEAR.equals(cycleCode)){
			//如果周期为年
			dateArray = DateUtil.getBeginAndEndTimes_year(type,null);
		}
		return dateArray;
	}
	
	public static Timestamp[] parseAssessTime(String cycleCode,Integer type,Date date) throws Exception{
		Timestamp[] dateArray = null;
		if(PerformanceTypeConstant.PER_CYCLE_MONTH.equals(cycleCode)){
			//如果周期为月
			dateArray = DateUtil.getBeginAndEndTimes_month(type,date);
		}else if(PerformanceTypeConstant.PER_CYCLE_QUARTER.equals(cycleCode)){
			//如果周期为季度
			dateArray = DateUtil.getBeginAndEndTimes_quarter(type,date);
		}else if(PerformanceTypeConstant.PER_CYCLE_HALFYEAR.equals(cycleCode)){
			//如果周期为半年
			dateArray = DateUtil.getBeginAndEndTimes_halfYear(type,date);
		}else if(PerformanceTypeConstant.PER_CYCLE_YEAR.equals(cycleCode)){
			//如果周期为年
			dateArray = DateUtil.getBeginAndEndTimes_year(type,date);
		}
		return dateArray;
	}

    /**
     * 转换公式
     * @param formula 转换前公式
     * @param preId 跟设置配对的ID，作为前缀标记
     * @param keyMap 要素的map,key为 preId-要素名称
     * @param elementPre 元素前缀
     * @param elementMap 元素的map,key为preId-元素名称
     * @return 转换后的公式
     */
	public static String parseFormula(String formula, String keyPre, Map<String, Object> keyMap,
			String elementPre,Map<String, Object> elementMap) {
		Map<String,List<String>> paramsMap = parseFormulaParams(formula);
		List<String> keyList = paramsMap.get("keyList");
		List<String> elementList = paramsMap.get("elementList");
		for(String key : keyList){
			String value = (String) keyMap.get(keyPre+"-"+key);
			formula = formula.replaceAll("\\["+key+"\\]", value);
			System.out.println("1:" + key + "----> " + value);
		}
		for(String element : elementList){
			String value = (String) elementMap.get(elementPre+"-"+element);
			formula = formula.replaceAll("\\{"+element+"\\}", value);
			System.out.println("2:" + element + "----> " + value);
		}
		return formula;
	}

    /**
     * 计算公式
     * @param formula
     * @return 计算结果
     */
	public static Double countFormula(String formula) {
		ExpressionParser parser = new SpelExpressionParser();
		Double value = parser.parseExpression(formula).getValue(Double.class);
//		if(value.doubleValue()<0) {
//			value = 0d;
//		}
		return value;
	}

    
	

}
