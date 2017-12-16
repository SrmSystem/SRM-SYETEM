package com.qeweb.scm.purchasemodule.webservice;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qeweb.sap.CommonSapFw;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.service.OrderSyncService;

@WebService(endpointInterface = "com.qeweb.scm.purchasemodule.webservice.OrderUpdateWebService", serviceName = "writeinfo")
public class OrderUpdateServiceImp implements OrderUpdateWebService {

	private static final Logger logger = LoggerFactory.getLogger(OrderUpdateWebService.class);
	
	@Autowired
	private OrderSyncService orderSyncService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public String writeinfo(String xml) {
		 Document doc = null;
		 Map<String, List<PurchaseOrderItemEntity>> res=new HashMap<String, List<PurchaseOrderItemEntity>>();
		 List<String> resList=null;
		 String msg="";
		 FileWriter fw=null;
		 StringBuilder log =new StringBuilder();
		try {
			  xml="<ROOT>"+xml.substring(xml.indexOf("<ITAB>"),xml.indexOf("</ITAB>")+7)+"</ROOT>";
			  fw = CommonSapFw.initFw(OrderUpdateServiceImp.class);
			  SAXReader reader = new SAXReader();
			  Document document =DocumentHelper.parseText(xml);  
			  Element root = document.getRootElement();
			  Element LIST = root.element("ITAB");
			  List<Element> elementsList = LIST.elements();
			  for (Element element : elementsList) {
				  String MATERIAL_CODE=element.elementText("MATERIAL_CODE");
					String BSART=element.elementText("BSART");	 //BSART		采购凭证类型
		    		String EBELN=element.elementText("EBELN");	 //EBELN		采购凭证号 
		    		String LIFNR=element.elementText("LIFNR");	// LIFNR			供应商或债权人的帐号
		    		String LIFNRMS=element.elementText("LIFNRMS");	// LIFNRMS			供应商描述 
		    		String ZTERM=element.elementText("ZTERM");	// ZTERM		付款条件代码
		    		String ZTERMMS=element.elementText("ZTERMMS");	// ZTERMMS		付款条件描述
		    		String WAERS=element.elementText("WAERS");	// WAERS			货币码 
		    		String WAERSMS=element.elementText("WAERSMS");	// WAERSMS			货币码描述 
		    		String BUKRS=element.elementText("BUKRS");	// BUKRS		公司代码
		    		String BUKRSMS=element.elementText("BUKRSMS");	// BUKRSMS			公司描述
		    		String EKORG=element.elementText("EKORG");	// EKORG			采购组织
		    		String EKORGMS=element.elementText("EKORGMS");	// EKORGMS			采购组织描述
		    		String EKGRP=element.elementText("EKGRP");	 //EKGRP			采购组 
		    		String EKGRPMS=element.elementText("EKGRPMS");	 //EKGRPMS			采购组描述 
		    		
		    		String EBELP=element.elementText("EBELP");	// EBELP			采购凭证的项目编号 
		    		String KNTTP=element.elementText("KNTTP");	// KNTTP		科目分配类别
		    		String PSTYP=element.elementText("PSTYP");	// PSTYP			采购凭证中的项目类别
		    		String MATNR=element.elementText("MATNR");	// MATNR			物料号 
		    		String MAKTX=element.elementText("MAKTX");	// MAKTX			物料描述（短文本） 
		    		Double MENGE=Double.valueOf(element.elementText("MENGE"));	// MENGE		采购订单数量
		    		String MEINS=element.elementText("MEINS");	 //MEINS			采购订单的计量单位 
		    		String EEIND=element.elementText("EEIND");	// EINDT			交货日期
		    		String WERKS=element.elementText("WERKS");	// WERKS			工厂
		    		String WERKSMS=element.elementText("WERKSMS");	// WERKSMS			工厂描述
		    		String MATKL=element.elementText("MATKL");	// MATKL			物料组 
		    		String MATKLMS=element.elementText("MATKLMS");	// MATKLMS			物料组描述 
		    		String IDNLF=element.elementText("IDNLF");	// IDNLF			供应商使用的物料编号
		    		String RETPO=element.elementText("RETPO");	// RETPO			退货项目 
		    		String ZFREE=element.elementText("ZFREE");	 //ZFREE			免费标识
		    		String AEDAT=element.elementText("AEDAT");	// AEDAT			记录的创建日期 
		    		String ERNAM=element.elementText("ERNAM");	// ERNAM			创建对象的人员名称 
		    		String FRGKE=element.elementText("FRGKE");	// FRGKE			批准标识：采购凭证 
		    		String LOEKZ=element.elementText("LOEKZ");// LOEKZ			采购凭证中的删除标识
		    		String ZLOCK=element.elementText("ZLOCK"); //ZLOCK			锁定标识
		    		String KZABS=element.elementText("KZABS");	// KZABS		订单回执需求 
		    		
		    		String ELIKZ=element.elementText("ELIKZ");
		    		String LGORT=element.elementText("LGORT");	// 库存地点
		    		String LGORTMS=element.elementText("LGORTMS");	// 仓储地点的描述 
		    		String ADDRESS=element.elementText("ADDRESS");	// 送货地址
		    		String ISMODIFY=element.elementText("ISMODIFY");	// 数量修改标识
		    		
		    		String AEDATTIME=element.elementText("AEDATTIME");	// AEDATTIME			记录的创建时间
		    		String UDATE=element.elementText("UDATE");	// AEDATTIME			记录的修改时间
		    		String UDATETIME=element.elementText("UDATETIME");	// AEDATTIME			记录的修改时间
		    		
		    		 String BPRME=element.elementText("BPRME"); //基本单位
		    		 String BPUMZ=element.elementText("BPUMZ"); //分母
		    		 String BPUMN=element.elementText("BPUMN"); //分子
		    		 Double LFIMG=Double.valueOf(element.elementText("LFIMG")); //基本数量
		    		 
		    		 //add by chao.gu 20170812
		    		 String ZSFXP=element.elementText("ZSFXP");//是否新品，X为新品，''为量产
		    		 String ZWQSL=element.elementText("ZWQSL");//SAP未清数量
		    		 String ZSLSX=element.elementText("ZSLSX");//订单数量上限
		    		 String BSTAE=element.elementText("BSTAE");//订单确认状态（003或者004为需推送srm的数据）
		    		 if(StringUtils.isNotEmpty(BSTAE) && (("0003").equals(BSTAE) || ("0004").equals(BSTAE))){
		    			 BSTAE="";//正常单子
		    		 }else{
		    			 BSTAE="X";//非内向交货单标识
		    		 }
		    		 //add end
		    		 
		    		PurchaseOrderItemEntity item=new PurchaseOrderItemEntity();
		    		
		    		item.setElikz(ELIKZ);
		    		item.setBPRME(BPRME);
		    		item.setBPUMZ(BPUMZ);
		    		item.setBPUMN(BPUMN);
		    		item.setLFIMG(LFIMG.toString());
		    		item.setLgort(LGORT);
		    		item.setLgortms(LGORTMS);
		    		item.setReceiveOrg(ADDRESS);
		    		item.setIsModify(Integer.parseInt(ISMODIFY));
		    		item.setItemNo(Integer.parseInt(EBELP));
		    		item.setKnttp(KNTTP);
		    		item.setPstyp(PSTYP);
		    		item.setMaterialCode(MATNR);
		    		item.setMaterialName(MAKTX);
		    		item.setMenge(MENGE+"");
		    		item.setOrderQty(MENGE);
		    		item.setSurOrderQty(MENGE);//add by eleven (默认把剩余订单数量=订单总数量)
		    		item.setSurBaseQty(LFIMG);//add by eleven (默认把剩余基本数量=基本总数量)
		    		item.setIsModify(Integer.parseInt(ISMODIFY));
		    		item.setMeins(MEINS);
		    		item.setUnitName(MEINS);
		    		item.setRequestTime(DateUtil.stringToTimestamp(EEIND+" 000000", "yyyyMMdd HHmmss"));
		    		item.setWerks(WERKS);
		    		item.setWerksms(WERKSMS);
		    		item.setMatkl(MATKL);
		    		item.setMatklms(MATKLMS);
		    		item.setIdnlf(IDNLF);
		    		item.setRetpo(RETPO);
		    		item.setZfree(ZFREE);
		    		item.setLoekz(LOEKZ);
		    		item.setZlock(ZLOCK);
		    		item.setKzabs(KZABS);
		    		//add by chao.gu 20170812
		    		item.setZSFXP(ZSFXP);
		    		item.setZWQSL(ZWQSL);
		    		item.setZSLSX(ZSLSX);
		    		item.setBstae(BSTAE);
		    		//add end
		    		if(res.get(EBELN)==null){
		    			List<PurchaseOrderItemEntity> list=new ArrayList<PurchaseOrderItemEntity>();
		    			PurchaseOrderEntity order=new PurchaseOrderEntity();
		    			order.setBsart(BSART);
		    			order.setOrderCode(EBELN);
		    			order.setVendorCode(LIFNR);
		    			order.setVendorName(LIFNRMS);
		    			order.setZterm(ZTERM);
		    			order.setZtermms(ZTERMMS);
		    			order.setWaers(WAERS);
		    			order.setWaersms(WAERSMS);
		    			order.setBukrs(BUKRS);
		    			order.setBukrsms(BUKRSMS);
		    			order.setBuyerCode(EKORG);
		    			order.setBuyerName(EKORGMS);
		    			order.setEkgrp(EKGRP);
		    			order.setEkgrpms(EKGRPMS);
		    			order.setAedat(DateUtil.stringToTimestamp(AEDAT+" "+AEDATTIME, "yyyyMMdd HHmmss"));
		    			order.setUdate(DateUtil.stringToTimestamp(UDATE+" "+UDATETIME, "yyyyMMdd HHmmss"));
		    			
		    			order.setErnam(ERNAM);
		    			order.setFrgke(FRGKE);//B代表未审批，R代表审批
		    			item.setOrder(order);
		    			list.add(item);
		    			res.put(EBELN, list);
		    		}else{
		    			List<PurchaseOrderItemEntity> list=res.get(EBELN);
		    			list.add(item);
		    			res.put(EBELN, list);
		    		}
			  }
			  resList= orderSyncService.execute(res,log,resList);
			  msg=resList.get(0);
			  log=new StringBuilder(resList.get(1));
		} catch (Exception e) {
			msg="数据异常";
			 StringWriter sw = new StringWriter();   
	         e.printStackTrace(new PrintWriter(sw, true));   
	         log.append("报错信息如下：-----");
	         log.append(sw.toString());   
			e.printStackTrace();
		} finally{
			try {
				log.append("\r\n");
				log.append(xml);
				log.append("\r\n");
				log.append(msg);
				fw.write(log.toString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return msg;
		}
	}
	

	public OrderSyncService getOrderSyncService() {
		if(orderSyncService == null)
			orderSyncService = SpringContextUtils.getBean("orderSyncService");
		return orderSyncService;
	}

	public void setOrderSyncService(OrderSyncService orderSyncService) {
		this.orderSyncService = orderSyncService;
	}	
}
