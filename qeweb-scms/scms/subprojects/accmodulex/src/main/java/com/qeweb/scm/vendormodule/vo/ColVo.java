package com.qeweb.scm.vendormodule.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class ColVo {
	private String id;
	private String name;
	private String type;//text,label,checkbox
	private String required;
	private String maxLength;
	private String varName;
	private String formula;
	private String value;
	private String label;
	private String sum;
	private String tip;
	private String url;
	private String dayFmt;//如果日期类型可以获取其格式
	private List<Map<String,String>> dataSource;
	
	//以下只有表尾有的属性
	private String isFee;//是否为费用
	private String sumId;//小计的ID
	private String colspan;//占据了几列
	
	public ColVo(String value){
		this.value = value;
	}
	
	/**
	 * 字段的构造函数
	 * @param id ID
	 * @param name 名字
	 * @param type 类型
	 * @param required 是否必填
	 * @param maxLength 最大长度
	 * @param url 访问后台地址
	 * @param dataSource 固定数据
	 * @param varName 变量
	 * @param formula 
	 * @param label
	 * @param sum
	 * @param colspan
	 * @param sumId
	 * @param isFee
	 * @param tip
	 * @param value
	 */
	public ColVo(String id,String name,String type,String required,String maxLength,String url,String dataSource,String varName,String formula,String label,String sum,String colspan, String sumId, String isFee,String tip ,String value,String dayFmt){
		this.id = id;
		this.name = name;
		this.type = type;
		this.required = required;
		this.maxLength = maxLength;
		this.varName = varName;
		this.formula = formula;
		this.label = label;
		this.sum = sum;
		this.colspan = colspan;
		this.sumId = sumId;
		this.isFee = isFee;
		this.tip = tip;
		this.url = url;
		this.dayFmt = "yyyy-MM-dd";
		if(!StringUtils.isEmpty(dayFmt)){
			this.dayFmt = dayFmt;
		}
		
		if(!StringUtils.isEmpty(dataSource)){
			List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
			String[] sourceArray = dataSource.split(",");
			for(String source : sourceArray){
				Map<String,String> data = new HashMap<String, String>();
				String[] sourceCt = source.split(":");
				data.put(sourceCt[0], sourceCt[1]);
				dataList.add(data);
			}
			this.dataSource = dataList;
		}
		
		
		//设置value值
		if(!StringUtils.isEmpty(value)){
			this.value = "";
			if("checkbox".equals(type)){
				String[] values = value.split(",");
				for(String valueTemp : values){
					for(Map<String,String> map : this.dataSource){
						if(map.get(valueTemp)!=null) {
							this.value+=map.get(valueTemp)+";";
						}
					}
				}
			}else{
				this.value = value;
			}
		}

	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public List<Map<String, String>> getDataSource() {
		return dataSource;
	}
	public void setDataSource(List<Map<String, String>> dataSource) {
		this.dataSource = dataSource;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getIsFee() {
		return isFee;
	}

	public void setIsFee(String isFee) {
		this.isFee = isFee;
	}

	public String getSumId() {
		return sumId;
	}

	public void setSumId(String sumId) {
		this.sumId = sumId;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDayFmt() {
		return dayFmt;
	}

	public void setDayFmt(String dayFmt) {
		this.dayFmt = dayFmt;
	}
	
	
	
	
}
