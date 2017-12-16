package com.qeweb.sap;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanningBoardEntity;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
/**
 * 采购计划看板接口
 * @author haiming.huang
 * 20170919
 *
 */
public class PurchasePlanningBoardSAP extends CommonSapFw{

	
	public static List<PurchasePlanningBoardEntity> sync(ILogger iLogge, Map<String ,Object> param) throws Exception{
		FileWriter fw = initFw(PurchasePlanningBoardSAP.class);
		
		StringBuffer log =new StringBuffer();
		log.append("调用sap接口,采购计划看板开始").append(DateUtil.getCurrentDate());
		iLogge.log("调用sap接口，采购计划看板开始 "+DateUtil.getCurrentDate()+"\r\n");
		JCoFunction function = null;  
		List<PurchasePlanningBoardEntity> res = null;   
	       try{
	    	   
	    	   JCoDestination destination = SAPConn.connect();  
		       iLogge.log("与sap接口建立连接"+"\r\n");
		       res=new ArrayList<PurchasePlanningBoardEntity>();
		       
		       
		       //调用函数  
	           function = destination.getRepository().getFunction(SapTableStructure.PURCHASE_PLANNING_BOARD_Z_SRM_DELIVERY_SPLIT);  
	            //参数
	            JCoParameterList input = function.getImportParameterList();  
	            
	            input.setValue("I_SEL", "1");
	            input.setValue("I_WERKS", param.get("factoryCode"));
	            input.setValue("I_FORMAT", "X");
	            JCoTable tableInput = function.getTableParameterList().getTable("IT_MATNR");
	            Object fCode = param.get("factoryCode");
	            if(fCode!=null){
	            	String[] str = fCode.toString().split(",");
	            	for(String s:str){
	            		tableInput.appendRow();
			            tableInput.setValue("MATNR", s);//物料
	            	}
	            }else{
	            	tableInput.appendRow();
		            tableInput.setValue("MATNR", "");//物料
	            }
	            
	            function.execute(destination); 
	            
	            JCoTable T_RESULT = function.getTableParameterList().getTable(SapTableStructure.PURCHASE_PLANNING_BOARD_IT_MAT_FORMATED);
	            iLogge.log("调用接口名称 ： "+ SapTableStructure.PURCHASE_PLANNING_BOARD_Z_SRM_DELIVERY_SPLIT +" ; 表名："+SapTableStructure.PURCHASE_PLANNING_BOARD_IT_MAT_FORMATED+"\r\n");
	            if (T_RESULT != null && !T_RESULT.isEmpty()) {
	            	 //log.append("总条数:").append(T_RESULT.getNumRows()).append("开始转成CheckItemEntity\r\n");
	    			 log.append(T_RESULT.toXML()).append("\r\n");
	    			 iLogge.log("总条数:"+T_RESULT.getNumRows()+"开始转成PurchasePlanningBoardEntity\r\n");
	    			 iLogge.log("内容为:"+T_RESULT.toXML()+"\r\n");
	    			 for (int i = 0; i < T_RESULT.getNumRows(); i++) {
	 					T_RESULT.setRow(i);
	 					
	 					String factoryCode = T_RESULT.getString("WERKS");//工厂
	 					String materialCode = T_RESULT.getString("MATNR");//物料号 
	 					String materialName = T_RESULT.getString("MAKTX");//物料描述
	 					String groupCode = T_RESULT.getString("EKGRP");//采购组 
	 					String plannedTurnaroundDay = T_RESULT.getString("JHZZT");//计划周转天 
	 					String qualityDay = T_RESULT.getString("ZLJYT");//质量检验天数 
	 					String flag = T_RESULT.getString("FLAG");//新品/量产标识（X - 新品，空白 - 量产）
	 					String dtype = T_RESULT.getString("DTYPE");//排产数量/到货数量/库存数量/发货数量
	 					String unit = T_RESULT.getString("MEINS");//单位
	 					String DAT01 = T_RESULT.getString("DAT01");
	 					String QTY01 = T_RESULT.getString("QTY01");
	 					String DAT02 = T_RESULT.getString("DAT02");
	 					String QTY02 = T_RESULT.getString("QTY02");
	 					String DAT03 = T_RESULT.getString("DAT03");
	 					String QTY03 = T_RESULT.getString("QTY03");
	 					String DAT04 = T_RESULT.getString("DAT04");
	 					String QTY04 = T_RESULT.getString("QTY04");
	 					String DAT05 = T_RESULT.getString("DAT05");
	 					String QTY05 = T_RESULT.getString("QTY05");
	 					String DAT06 = T_RESULT.getString("DAT06");
	 					String QTY06 = T_RESULT.getString("QTY06");
	 					String DAT07 = T_RESULT.getString("DAT07");
	 					String QTY07 = T_RESULT.getString("QTY07");
	 					String DAT08 = T_RESULT.getString("DAT08");
	 					String QTY08 = T_RESULT.getString("QTY08");
	 					String DAT09 = T_RESULT.getString("DAT09");
	 					String QTY09 = T_RESULT.getString("QTY09");
	 					String DAT10 = T_RESULT.getString("DAT10");
	 					String QTY10 = T_RESULT.getString("QTY10");
	 					String DAT11 = T_RESULT.getString("DAT11");
	 					String QTY11 = T_RESULT.getString("QTY11");
	 					String DAT12 = T_RESULT.getString("DAT12");
	 					String QTY12 = T_RESULT.getString("QTY12");
	 					String DAT13 = T_RESULT.getString("DAT13");
	 					String QTY13 = T_RESULT.getString("QTY13");
	 					String DAT14 = T_RESULT.getString("DAT14");
	 					String QTY14 = T_RESULT.getString("QTY14");
	 					String DAT15 = T_RESULT.getString("DAT15");
	 					String QTY15 = T_RESULT.getString("QTY15");
	 					String DAT16 = T_RESULT.getString("DAT16");
	 					String QTY16 = T_RESULT.getString("QTY16");
	 					String DAT17 = T_RESULT.getString("DAT17");
	 					String QTY17 = T_RESULT.getString("QTY17");
	 					String DAT18 = T_RESULT.getString("DAT18");
	 					String QTY18 = T_RESULT.getString("QTY18");
	 					String DAT19 = T_RESULT.getString("DAT19");
	 					String QTY19 = T_RESULT.getString("QTY19");
	 					String DAT20 = T_RESULT.getString("DAT20");
	 					String QTY20 = T_RESULT.getString("QTY20");
	 					String DAT21 = T_RESULT.getString("DAT21");
	 					String QTY21 = T_RESULT.getString("QTY21");
	 					String DAT22 = T_RESULT.getString("DAT22");
	 					String QTY22 = T_RESULT.getString("QTY22");
	 					String DAT23 = T_RESULT.getString("DAT23");
	 					String QTY23 = T_RESULT.getString("QTY23");
	 					String DAT24 = T_RESULT.getString("DAT24");
	 					String QTY24 = T_RESULT.getString("QTY24");
	 					String DAT25 = T_RESULT.getString("DAT25");
	 					String QTY25 = T_RESULT.getString("QTY25");
	 					String DAT26 = T_RESULT.getString("DAT26");
	 					String QTY26 = T_RESULT.getString("QTY26");
	 					String DAT27 = T_RESULT.getString("DAT27");
	 					String QTY27 = T_RESULT.getString("QTY27");
	 					String DAT28 = T_RESULT.getString("DAT28");
	 					String QTY28 = T_RESULT.getString("QTY28");
	 					String DAT29 = T_RESULT.getString("DAT29");
	 					String QTY29 = T_RESULT.getString("QTY29");
	 					String DAT30 = T_RESULT.getString("DAT30");
	 					String QTY30 = T_RESULT.getString("QTY30");
	 					String DAT31 = T_RESULT.getString("DAT31");
	 					String QTY31 = T_RESULT.getString("QTY31");
	 					String DAT32 = T_RESULT.getString("DAT32");
	 					String QTY32 = T_RESULT.getString("QTY32");
	 					String DAT33 = T_RESULT.getString("DAT33");
	 					String QTY33 = T_RESULT.getString("QTY33");
	 					String DAT34 = T_RESULT.getString("DAT34");
	 					String QTY34 = T_RESULT.getString("QTY34");
	 					String DAT35 = T_RESULT.getString("DAT35");
	 					String QTY35 = T_RESULT.getString("QTY35");
	 					String DAT36 = T_RESULT.getString("DAT36");
	 					String QTY36 = T_RESULT.getString("QTY36");
	 					String DAT37 = T_RESULT.getString("DAT37");
	 					String QTY37 = T_RESULT.getString("QTY37");
	 					String DAT38 = T_RESULT.getString("DAT38");
	 					String QTY38 = T_RESULT.getString("QTY38");
	 					String DAT39 = T_RESULT.getString("DAT39");
	 					String QTY39 = T_RESULT.getString("QTY39");
	 					String DAT40 = T_RESULT.getString("DAT40");
	 					String QTY40 = T_RESULT.getString("QTY40");
	 					String DAT41 = T_RESULT.getString("DAT41");
	 					String QTY41 = T_RESULT.getString("QTY41");
	 					String DAT42 = T_RESULT.getString("DAT42");
	 					String QTY42 = T_RESULT.getString("QTY42");
	 					String DAT43 = T_RESULT.getString("DAT43");
	 					String QTY43 = T_RESULT.getString("QTY43");
	 					String DAT44 = T_RESULT.getString("DAT44");
	 					String QTY44 = T_RESULT.getString("QTY44");
	 					String DAT45 = T_RESULT.getString("DAT45");
	 					String QTY45 = T_RESULT.getString("QTY45");
	 					String DAT46 = T_RESULT.getString("DAT46");
	 					String QTY46 = T_RESULT.getString("QTY46");
	 					String DAT47 = T_RESULT.getString("DAT47");
	 					String QTY47 = T_RESULT.getString("QTY47");
	 					String DAT48 = T_RESULT.getString("DAT48");
	 					String QTY48 = T_RESULT.getString("QTY48");
	 					String DAT49 = T_RESULT.getString("DAT49");
	 					String QTY49 = T_RESULT.getString("QTY49");
	 					String DAT50 = T_RESULT.getString("DAT50");
	 					String QTY50 = T_RESULT.getString("QTY50");
	 					String DAT51 = T_RESULT.getString("DAT51");
	 					String QTY51 = T_RESULT.getString("QTY51");
	 					String DAT52 = T_RESULT.getString("DAT52");
	 					String QTY52 = T_RESULT.getString("QTY52");
	 					String DAT53 = T_RESULT.getString("DAT53");
	 					String QTY53 = T_RESULT.getString("QTY53");
	 					String DAT54 = T_RESULT.getString("DAT54");
	 					String QTY54 = T_RESULT.getString("QTY54");
	 					String DAT55 = T_RESULT.getString("DAT55");
	 					String QTY55 = T_RESULT.getString("QTY55");
	 					String DAT56 = T_RESULT.getString("DAT56");
	 					String QTY56 = T_RESULT.getString("QTY56");
	 					String DAT57 = T_RESULT.getString("DAT57");
	 					String QTY57 = T_RESULT.getString("QTY57");
	 					String DAT58 = T_RESULT.getString("DAT58");
	 					String QTY58 = T_RESULT.getString("QTY58");
	 					String DAT59 = T_RESULT.getString("DAT59");
	 					String QTY59 = T_RESULT.getString("QTY59");
	 					String DAT60 = T_RESULT.getString("DAT60");
	 					String QTY60 = T_RESULT.getString("QTY60");

	 					
	 					PurchasePlanningBoardEntity entity =new PurchasePlanningBoardEntity();
	 					entity.setFactoryCode(factoryCode);
	 					entity.setMaterialCode(materialCode);
	 					entity.setMaterialName(materialName);
	 					entity.setGroupCode(groupCode);
	 					entity.setPlannedTurnaroundDay(plannedTurnaroundDay);
	 					entity.setQualityDay(qualityDay);
	 					entity.setFlag(flag);
	 					entity.setDtype(dtype);
	 					entity.setUnit(unit);
	 					entity.setDAT01(DAT01);
	 					entity.setQTY01(QTY01);
	 					entity.setDAT02(DAT02);
	 					entity.setQTY02(QTY02);
	 					entity.setDAT03(DAT03);
	 					entity.setQTY03(QTY03);
	 					entity.setDAT04(DAT04);
	 					entity.setQTY04(QTY04);
	 					entity.setDAT05(DAT05);
	 					entity.setQTY05(QTY05);
	 					entity.setDAT06(DAT06);
	 					entity.setQTY06(QTY06);
	 					entity.setDAT07(DAT07);
	 					entity.setQTY07(QTY07);
	 					entity.setDAT08(DAT08);
	 					entity.setQTY08(QTY08);
	 					entity.setDAT09(DAT09);
	 					entity.setQTY09(QTY09);
	 					entity.setDAT10(DAT10);
	 					entity.setQTY10(QTY10);
	 					entity.setDAT11(DAT11);
	 					entity.setQTY11(QTY11);
	 					entity.setDAT12(DAT12);
	 					entity.setQTY12(QTY12);
	 					entity.setDAT13(DAT13);
	 					entity.setQTY13(QTY13);
	 					entity.setDAT14(DAT14);
	 					entity.setQTY14(QTY14);
	 					entity.setDAT15(DAT15);
	 					entity.setQTY15(QTY15);
	 					entity.setDAT16(DAT16);
	 					entity.setQTY16(QTY16);
	 					entity.setDAT17(DAT17);
	 					entity.setQTY17(QTY17);
	 					entity.setDAT18(DAT18);
	 					entity.setQTY18(QTY18);
	 					entity.setDAT19(DAT19);
	 					entity.setQTY19(QTY19);
	 					entity.setDAT20(DAT20);
	 					entity.setQTY20(QTY20);
	 					entity.setDAT21(DAT21);
	 					entity.setQTY21(QTY21);
	 					entity.setDAT22(DAT22);
	 					entity.setQTY22(QTY22);
	 					entity.setDAT23(DAT23);
	 					entity.setQTY23(QTY23);
	 					entity.setDAT24(DAT24);
	 					entity.setQTY24(QTY24);
	 					entity.setDAT25(DAT25);
	 					entity.setQTY25(QTY25);
	 					entity.setDAT26(DAT26);
	 					entity.setQTY26(QTY26);
	 					entity.setDAT27(DAT27);
	 					entity.setQTY27(QTY27);
	 					entity.setDAT28(DAT28);
	 					entity.setQTY28(QTY28);
	 					entity.setDAT29(DAT29);
	 					entity.setQTY29(QTY29);
	 					entity.setDAT30(DAT30);
	 					entity.setQTY30(QTY30);
	 					entity.setDAT31(DAT31);
	 					entity.setQTY31(QTY31);
	 					entity.setDAT32(DAT32);
	 					entity.setQTY32(QTY32);
	 					entity.setDAT33(DAT33);
	 					entity.setQTY33(QTY33);
	 					entity.setDAT34(DAT34);
	 					entity.setQTY34(QTY34);
	 					entity.setDAT35(DAT35);
	 					entity.setQTY35(QTY35);
	 					entity.setDAT36(DAT36);
	 					entity.setQTY36(QTY36);
	 					entity.setDAT37(DAT37);
	 					entity.setQTY37(QTY37);
	 					entity.setDAT38(DAT38);
	 					entity.setQTY38(QTY38);
	 					entity.setDAT39(DAT39);
	 					entity.setQTY39(QTY39);
	 					entity.setDAT40(DAT40);
	 					entity.setQTY40(QTY40);
	 					entity.setDAT41(DAT41);
	 					entity.setQTY41(QTY41);
	 					entity.setDAT42(DAT42);
	 					entity.setQTY42(QTY42);
	 					entity.setDAT43(DAT43);
	 					entity.setQTY43(QTY43);
	 					entity.setDAT44(DAT44);
	 					entity.setQTY44(QTY44);
	 					entity.setDAT45(DAT45);
	 					entity.setQTY45(QTY45);
	 					entity.setDAT46(DAT46);
	 					entity.setQTY46(QTY46);
	 					entity.setDAT47(DAT47);
	 					entity.setQTY47(QTY47);
	 					entity.setDAT48(DAT48);
	 					entity.setQTY48(QTY48);
	 					entity.setDAT49(DAT49);
	 					entity.setQTY49(QTY49);
	 					entity.setDAT50(DAT50);
	 					entity.setQTY50(QTY50);
	 					entity.setDAT51(DAT51);
	 					entity.setQTY51(QTY51);
	 					entity.setDAT52(DAT52);
	 					entity.setQTY52(QTY52);
	 					entity.setDAT53(DAT53);
	 					entity.setQTY53(QTY53);
	 					entity.setDAT54(DAT54);
	 					entity.setQTY54(QTY54);
	 					entity.setDAT55(DAT55);
	 					entity.setQTY55(QTY55);
	 					entity.setDAT56(DAT56);
	 					entity.setQTY56(QTY56);
	 					entity.setDAT57(DAT57);
	 					entity.setQTY57(QTY57);
	 					entity.setDAT58(DAT58);
	 					entity.setQTY58(QTY58);
	 					entity.setDAT59(DAT59);
	 					entity.setQTY59(QTY59);
	 					entity.setDAT60(DAT60);
	 					entity.setQTY60(QTY60);
	 					entity.setState("0");
	 					res.add(entity);
	    			 }
	            }else{
	            	iLogge.log("IT_MAT_FORMATED 中未获得到数据"+"\r\n");
	            }
	       }catch(Throwable e) {  
	    	    iLogge.log(e.toString()+"\r\n");
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("调用sap接口，采购计划看板结束").append(DateUtil.getCurrentDate());
	        	iLogge.log("调用sap接口，采购计划看板结束"+DateUtil.getCurrentDate()+"\r\n");
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
	       return res;
	}
	
   

}
