package com.qeweb.scm.vendormodule.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.vo.ColVo;
import com.qeweb.scm.vendormodule.vo.CtVo;
import com.qeweb.scm.vendormodule.vo.TrVo;

/**
 * 调查表工具类主要用来做转换
 * @author pjjxiajun
 * @date 2015年6月12日
 * @path com.qeweb.scm.vendormodule.utils.SurveyUtil.java
 */
public class SurveyUtil {

	private final static String SURVEYUPLOADDIR = "vendor.survey.updir";
	/**
	 * 转换调查表模版
	 * @author pjjxiajun 2015年4月20日 下午8:43:28 
	 * @param templateDoc XML模版内容
	 * @return
	 */
	public static List<CtVo> parserSurveyXML(Document dc) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<CtVo> ctList = new ArrayList<CtVo>();
		Element root = dc.getRootElement();
		List<Element> nodeList = root.elements();
		for(Element node : nodeList){
			if("form".equals(node.getName())){
				CtVo ctVo = convertXmlForm(node);
				ctList.add(ctVo);
			}else if("table".equals(node.getName())){
				CtVo ctVo = convertXmlTb(node);
				ctList.add(ctVo);
			}
		}
		return ctList;
	}
	
	/**
	 * 将模版中的form转换成Map
	 * @author pjjxiajun 2015年4月20日 下午10:30:39 
	 * @param typeEl 模版中的form类型
	 * @return Map
	 */
	public static CtVo convertXmlForm(Element typeEl) {
		CtVo ctVo = new CtVo(typeEl.attributeValue("id"),CtVo.TYPE_F, typeEl.attributeValue("title"));
		Map<String,Object> map = new HashMap<String, Object>();
		List<Element> fieldElList = typeEl.elements("field");
		List<ColVo> fieldList = new ArrayList<ColVo>();
		
		for(Element fieldEl : fieldElList){
			ColVo fieldNode = new ColVo(fieldEl.attributeValue("id"),fieldEl.attributeValue("name"), 
			fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
			fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
			fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
			fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),null,null,null,fieldEl.attributeValue("tip"),null,fieldEl.attributeValue("dayFmt"));
			fieldList.add(fieldNode);
		}
		ctVo.setColList(fieldList);
		return ctVo;
	}
	

	/**
	 * 将模版中的Table转换成CT
	 * @author pjjxiajun 2015年4月20日 下午10:32:41 
	 * @param typeEl table类型
	 * @return table类型的CT
	 */
	public static CtVo convertXmlTb(Element typeEl) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Element> elements = typeEl.elements();
		Map<String,Object> tbMap = new HashMap<String, Object>();
		List<TrVo> trList = new ArrayList<TrVo>();
		Element theadEl = typeEl.element("thead");
		Element tbodyEl = typeEl.element("tbody");
		Element tfootEl = typeEl.element("tfoot");
		List<Element> thEls = theadEl==null?new ArrayList<Element>():theadEl.elements("field");
		List<Element> trEls = tbodyEl==null?new ArrayList<Element>():tbodyEl.elements("tr");
		List<Element> tfEls = tfootEl==null?new ArrayList<Element>():tfootEl.elements("field");

		CtVo ctVo = new CtVo(typeEl.attributeValue("id"),CtVo.TYPE_T, typeEl.attributeValue("title"));
		//转换表头
		List<ColVo> thNodeList = new ArrayList<ColVo>();
		for(Element fieldEl : thEls){
			ColVo fieldNode = new ColVo(fieldEl.attributeValue("id"),fieldEl.attributeValue("name"), 
					fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
					fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
					fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
					fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),null,null,null,fieldEl.attributeValue("tip"),null,
					fieldEl.attributeValue("dayFmt")
					);
			thNodeList.add(fieldNode);
		}
		ctVo.setColList(thNodeList);
		//转换默认行
		for(Element el : trEls){
			TrVo trNode = new TrVo();
			String fixed = el.attributeValue("fixed");
			List<Element> thFieldElList = el.elements();
			List<String> tdNodeList = new ArrayList<String>();
			for(Element fieldEl : thFieldElList){
				tdNodeList.add(fieldEl.getText());
			}
			trNode.setFixed(StringUtils.isEmpty(fixed)?"false":fixed);
			trNode.setTdList(tdNodeList);
			trList.add(trNode);
		}
		ctVo.setTrList(trList);

		//转换表尾
		List<ColVo> tfNodeList = new ArrayList<ColVo>();
		for(Element fieldEl : tfEls){
			ColVo fieldNode = new ColVo(null,fieldEl.attributeValue("name"), 
					fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
					fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
					fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
					fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),fieldEl.attributeValue("colspan"),
					fieldEl.attributeValue("sumId"),fieldEl.attributeValue("isFee"),
					fieldEl.attributeValue("tip"),
					null,fieldEl.attributeValue("dayFmt")
					);
			tfNodeList.add(fieldNode);
		}
		ctVo.setTfList(tfNodeList);
		return ctVo;
	}


	public static List<CtVo> parserSurveyedXML(VendorSurveyBaseEntity base, List<VendorSurveyDataEntity> itemList, Document dc) {
		List<CtVo> ctList = new ArrayList<CtVo>();
		Element root = dc.getRootElement();
		List<Element> nodeList = root.elements();
		for(Element node : nodeList){
			if("form".equals(node.getName())){
				CtVo ctVo = convertXmlForm(node,base,itemList);
				ctList.add(ctVo);
			}else if("table".equals(node.getName())){
				CtVo ctVo = convertXmlTb(node,base,itemList);
				ctList.add(ctVo);
			}
		}
		return ctList;
	}



	public static CtVo convertXmlForm(Element typeEl, VendorSurveyBaseEntity base, List<VendorSurveyDataEntity> itemList) {
		CtVo ctVo = new CtVo(typeEl.attributeValue("id"),CtVo.TYPE_F, typeEl.attributeValue("title"));
		Map<String,Object> map = new HashMap<String, Object>();
		List<Element> fieldElList = typeEl.elements("field");
		List<ColVo> fieldList = new ArrayList<ColVo>();
		String ctId = typeEl.attributeValue("id");
		for(Element fieldEl : fieldElList){
			String value = convertFormValue(ctId,fieldEl.attributeValue("name"),itemList);
			ColVo fieldNode = new ColVo(fieldEl.attributeValue("id"),fieldEl.attributeValue("name"), 
			fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
			fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
			fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
			fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),null,null,null,fieldEl.attributeValue("tip"),value,fieldEl.attributeValue("dayFmt"));
			fieldList.add(fieldNode);
		}
		ctVo.setColList(fieldList);
		return ctVo;
	}


	public static String convertFormValue(String ctId, String colName, List<VendorSurveyDataEntity> itemList) {
		for(VendorSurveyDataEntity data : itemList){
			if(data.getCtId().equals(ctId)){
				return data.getXMLVal(colName);
			}
		}
		return null;
	}
	

	public static CtVo convertXmlTb(Element typeEl, VendorSurveyBaseEntity base, List<VendorSurveyDataEntity> itemList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Element> elements = typeEl.elements();
		Map<String,Object> tbMap = new HashMap<String, Object>();
		List<TrVo> trList = new ArrayList<TrVo>();
		Element theadEl = typeEl.element("thead");
		Element tbodyEl = typeEl.element("tbody");
		Element tfootEl = typeEl.element("tfoot");
		List<Element> thEls = theadEl==null?new ArrayList<Element>():theadEl.elements("field");
//		List<Element> trEls = tbodyEl==null?new ArrayList<Element>():tbodyEl.elements("tr");
		List<Element> tfEls = tfootEl==null?new ArrayList<Element>():tfootEl.elements("field");
		CtVo ctVo = new CtVo(typeEl.attributeValue("id"),CtVo.TYPE_T, typeEl.attributeValue("title"));
		String ctId = typeEl.attributeValue("id");
		//转换表头
		List<ColVo> thNodeList = new ArrayList<ColVo>();
		for(Element fieldEl : thEls){
			ColVo fieldNode = new ColVo(fieldEl.attributeValue("id"),fieldEl.attributeValue("name"), 
					fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
					fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
					fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
					fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),null,null,null,fieldEl.attributeValue("tip"),null,fieldEl.attributeValue("dayFmt")
					);
			thNodeList.add(fieldNode);
		}
		ctVo.setColList(thNodeList);
		//转换填写的数据
		trList = convertRrXMLValue(ctId,thNodeList,itemList);
		ctVo.setTrList(trList);

		//转换表尾
		List<ColVo> tfNodeList = new ArrayList<ColVo>();
		for(Element fieldEl : tfEls){
			ColVo fieldNode = new ColVo(null,fieldEl.attributeValue("name"), 
					fieldEl.attributeValue("type"), fieldEl.attributeValue("required"), 
					fieldEl.attributeValue("maxLength"),fieldEl.attributeValue("url"),fieldEl.attributeValue("dataSource"),
					fieldEl.attributeValue("varName"),fieldEl.attributeValue("formula"),
					fieldEl.attributeValue("label"),fieldEl.attributeValue("sum"),fieldEl.attributeValue("colspan"),
					fieldEl.attributeValue("sumId"),fieldEl.attributeValue("isFee"),
					fieldEl.attributeValue("tip"),
					null,fieldEl.attributeValue("dayFmt")
					);
			tfNodeList.add(fieldNode);
		}
		ctVo.setTfList(tfNodeList);
		return ctVo;
	}


	public static List<TrVo> convertRrXMLValue(String ctId, List<ColVo> thNodeList, List<VendorSurveyDataEntity> itemList) {
		List<TrVo> trList = new ArrayList<TrVo>();
		for(VendorSurveyDataEntity item : itemList){
			if(!item.getCtId().equals(ctId) || !"tbody".equals(item.getCtCode())) {
				continue;
			}
			List<String> tdList = new ArrayList<String>();
			for(ColVo col : thNodeList){
				String colName =col.getName();
				String value = item.getXMLVal(colName);
				if(col.getType().equals("file")){
					value = PropertiesUtil.getProperty(SURVEYUPLOADDIR)+"/"+item.getBaseId()+"/"+value;
				}
				tdList.add(value);
			}
			TrVo tr = new TrVo();
			tr.setFixed(item.getFixed()+"");
			tr.setTdList(tdList);
			tr.setId(item.getId()+"");
			trList.add(tr);
		}
		return trList;
	}
	
}
