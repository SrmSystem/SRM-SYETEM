package com.qeweb.scm.vendormodule.utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;

@SuppressWarnings("deprecation")
public class SimpleExcelWrite {
	
	// 创建Excel的工作书册 Workbook,对应到一个excel文档
	private  HSSFWorkbook wb = new HSSFWorkbook();
	// 创建Excel的工作sheet,对应到一个excel文档的tab
	private  HSSFSheet sheet ;
	
	// 创建字体样式
	private HSSFFont getHSSFFont(short w,short h)
	{
		HSSFFont font=wb.createFont();
		 font.setFontName("宋体");
		 font.setBoldweight(w);
		 font.setFontHeight(h);
		 font.setColor(HSSFColor.BLACK.index);//字体颜色
		return font;
	}
	// 创建单元格样式
	private HSSFCellStyle getStyle(int i,HSSFFont font,int j)
	{
		HSSFCellStyle style = wb.createCellStyle();
		if(i==1)
		{
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
		    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
		}
	    if(font!=null)
	    {
	    	 style.setFont(font);// 设置字体
	    }
	    if(j==1)
	    {
	    	style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    }
		return style;
	}
	private HSSFRow getrow(short i,int rw)
	{
		HSSFRow row = sheet.createRow(rw);
		if(i!=(short)0)
		{
			row.setHeight(i);// 设定行的高度
		}
		return row;
	}
	private void getcell(HSSFCellStyle style,HSSFRow row ,int cl,String name)
	{
		
		HSSFCell cell = row.createCell(cl);
		if(style!=null)
		{
			cell.setCellStyle(style);
		}
		if(name!=null)
		{
			cell.setCellValue(name);
		}
	}
	private void getStart(String name)
	{
		sheet= wb.createSheet(name);
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 3500);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1,1, 0,2));
		sheet.addMergedRegion(new CellRangeAddress(1,1, 4,7));
		sheet.addMergedRegion(new CellRangeAddress(2,2, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(3,3, 1, 3));
		sheet.addMergedRegion(new CellRangeAddress(3,3, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(3,3, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(4,4, 1, 3));
		sheet.addMergedRegion(new CellRangeAddress(4,4, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(4,4, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(5,5, 1, 3));
		sheet.addMergedRegion(new CellRangeAddress(5,5, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(5,5, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(6,6, 1, 7));
		sheet.addMergedRegion(new CellRangeAddress(7,7, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(8,9, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(8,9, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(8,9, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(8,9, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(8,9, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(10,10, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(10,10, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(10,10, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(11,11, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(11,11, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(11,11, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(12,12, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(12,12, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(12,12, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(13,13, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(13,13, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(13,13, 6, 7));
		
		sheet.addMergedRegion(new CellRangeAddress(14,14, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(14,14, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(14,14, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(14,14, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(14,14, 4, 5));
		sheet.addMergedRegion(new CellRangeAddress(14,14, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(15,15, 0, 7));
		
		sheet.addMergedRegion(new CellRangeAddress(16,17, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(16,17, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(16,17, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(16,17, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(16,17, 7, 7));
	}
	public void getExcelWrite(HttpServletResponse response,VendorBaseInfoEntity vendorBaseInfo,VendorSurveyDataEntity data,int year,List<VendorExcel2> list, List<VendorExcel> list1,List<VendorSurveyDataEntity> list2,List<VendorSurveyDataEntity> list3,List<VendorSurveyDataEntity> list4,List<VendorSurveyDataEntity> list5,List<VendorSurveyDataEntity> list55,List<VendorSurveyDataEntity> list6) throws IOException
	{
		
		 getStart("竖排");
		 HSSFRow rows =null;
		 rows=getrow((short) 500,0);
		 getcell(getStyle(1,getHSSFFont((short) 300,(short) 500) , 0),rows,0,"供应商基本信息表");
//---------------------------------------------------------------------------------------------		 
		 rows =getrow((short) 0,1);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 0),rows,0,"供应商名称："+vendorBaseInfo.getName());
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 0),rows,4,"供应商代码："+vendorBaseInfo.getCode());
		 
		 rows =getrow((short) 0,2);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"一、企业经营规模");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
//------------------------------------------------------------------------------------------------				 
		 rows =getrow((short) 0,3);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"公司性质");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,1,vendorBaseInfo.getPropertyText());
		 for(int i=2;i<=3;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 for(int i=4;i<=5;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"成立时间");
		 SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
		 String str = formatDate.format(vendorBaseInfo.getRegtime());
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,6,str);
		 for(int i=7;i<=7;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
//------------------------------------------------------------------------------------------------			    
		 rows =getrow((short) 0,4);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"注册资本");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,1,vendorBaseInfo.getRegCapital());
		 for(int i=2;i<=3;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 for(int i=4;i<=5;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"资产总额");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,6,vendorBaseInfo.getTotalCapital());
		 for(int i=7;i<=7;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
//------------------------------------------------------------------------------------------------			    
		 rows =getrow((short) 0,5);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"企业法人");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,1,vendorBaseInfo.getLegalPerson());
		 for(int i=2;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=4;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"股比构成");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,6,vendorBaseInfo.getStockShare());
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//------------------------------------------------------------------------------------------------			    
		 rows =getrow((short) 0,6);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"主要产品");
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,1,""+vendorBaseInfo.getMainProduct());
		 for(int i=2;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,7);
		 for(int i=0;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,8);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"人数（人）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"高级职称（人）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"中级职称（人）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"备注");
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 rows =getrow((short) 0,9);
		 for(int i=0;i<=0;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=1;i<=1;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=2;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=4;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=6;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,10);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"职工总人数");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,data.getCol1());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,data.getCol2());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,data.getCol3());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"");	    
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,11);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"管理人员");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,data.getCol4());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,data.getCol5());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,data.getCol6());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"");	    
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,12);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"研发人员");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,data.getCol7());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,data.getCol8());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,data.getCol9());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"");	    
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,13);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"生产制造人员");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,data.getCol10());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,data.getCol11());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,data.getCol12());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"");	    
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,14);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"质量人员数量");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,data.getCol13());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,data.getCol14());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,data.getCol15());
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"");	    
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=5;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,15);
		 for(int i=0;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 rows =getrow((short) 0,16);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"年份");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,""+(year-3));
		 getcell(getStyle(1,null , 1),rows,2,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,""+(year-2));
		 getcell(getStyle(1,null , 1),rows,4,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,""+(year-1));
		 getcell(getStyle(1,null , 1),rows,6,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		
		 rows =getrow((short) 0,17);
		 for(int i=0;i<=0;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=1;i<=2;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=3;i<=4;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=5;i<=6;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=7;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
//-------------------------------------------------------------------------------------------------
		 int number=18;
		 if(list!=null)
		 {
			 for(VendorExcel2 ve2:list)
			 {
				 sheet.addMergedRegion(new CellRangeAddress(number,number, 1, 2));
				 sheet.addMergedRegion(new CellRangeAddress(number,number, 3, 4));
				 sheet.addMergedRegion(new CellRangeAddress(number,number, 5, 6));
				 rows =getrow((short) 0,number);
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,ve2.getName());
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,ve2.getCol1());
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,ve2.getCol2());
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,ve2.getCol3());
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,ve2.getCol4());
				 for(int i=2;i<=2;i++){
					 HSSFCell cells = rows.createCell(i);
					 cells.setCellStyle(getStyle(0,null , 1));
				 }
				 for(int i=4;i<=4;i++){
					 HSSFCell cells = rows.createCell(i);
					 cells.setCellStyle(getStyle(0,null , 1));
				 }
				 for(int i=6;i<=6;i++){
					 HSSFCell cells = rows.createCell(i);
					 cells.setCellStyle(getStyle(0,null , 1));
				 }
				 number++;
			 }
		 }
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"二、产品、客户及销售收入");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"1、主营产品、客户及销售收入");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 0, 0));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1, 4));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5, 7));
		 sheet.addMergedRegion(new CellRangeAddress(number+1,number+1, 2, 3));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"主营产品");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"主机厂");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"销售收入（万元）");
		 for(int i=2;i<=4;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=6;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
		 rows =getrow((short) 0,number);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"主要客户");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"车型");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"供货比例");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"前3年年销售收入");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"前2年年销售收入");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"前1年年销售收入");
		 for(int i=0;i<=0;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int i=3;i<=3;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		// TODO 未做
		 
		// TODO list1 在这里
		 Double countyea=0d;
		 if(list1!=null)
		 {
			 int k=list1.size();
			 for(int i=0;i<k;i++)
			 {
				 int j=0;
				 int u=number;
				 if(list1.get(i).getList()!=null)
				 {
					j= list1.get(i).getList().size();
				 }
			     sheet.addMergedRegion(new CellRangeAddress(number,number+j, 0, 0));
			     rows =getrow((short) 0,number);
				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,list1.get(i).getMainPER());
				 if(j!=0)
				 {
					 sheet.addMergedRegion(new CellRangeAddress(number,number, 2, 3));
					 getcell(getStyle(1,null , 1),rows,1,list1.get(i).getList().get(0).getCol2());
					 getcell(getStyle(1,null , 1),rows,2,list1.get(i).getList().get(0).getCol3());
					 getcell(getStyle(1,null , 1),rows,4,list1.get(i).getList().get(0).getCol4());
					 getcell(getStyle(1,null , 1),rows,5,list1.get(i).getList().get(0).getCol5());
					 getcell(getStyle(1,null , 1),rows,6,list1.get(i).getList().get(0).getCol6());
					 getcell(getStyle(1,null , 1),rows,7,list1.get(i).getList().get(0).getCol7());
					 for(int x=3;x<=3;x++){
						 HSSFCell cells = rows.createCell(x);
						 cells.setCellStyle(getStyle(0,null , 1));
					 }
					 number=number+1;
					 for(;number<u+j;number++)
					 {
						 sheet.addMergedRegion(new CellRangeAddress(number,number, 2, 3));
						 rows =getrow((short) 0,number);
						 getcell(getStyle(1,null , 1),rows,1,list1.get(i).getList().get(number-u).getCol2());
						 getcell(getStyle(1,null , 1),rows,2,list1.get(i).getList().get(number-u).getCol3());
						 getcell(getStyle(1,null , 1),rows,4,list1.get(i).getList().get(number-u).getCol4());
						 getcell(getStyle(1,null , 1),rows,5,list1.get(i).getList().get(number-u).getCol5());
						 getcell(getStyle(1,null , 1),rows,6,list1.get(i).getList().get(number-u).getCol6());
						 getcell(getStyle(1,null , 1),rows,7,list1.get(i).getList().get(number-u).getCol7());
						 getcell(getStyle(1,null , 1),rows,3,"");
						 getcell(getStyle(1,null , 1),rows,0,"");
					 }
					 sheet.addMergedRegion(new CellRangeAddress(number,number, 2, 3));
					 rows =getrow((short) 0,number);
					 
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"合计");
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"");
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"");
					 BigDecimal b1 = new BigDecimal(list1.get(i).getThreeyear()); 
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,""+b1);
					 BigDecimal b2 = new BigDecimal(list1.get(i).getTwoyear()); 
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,""+b2);
					 BigDecimal b3 = new BigDecimal(list1.get(i).getOneyear()); 
					 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,""+b3);
					 getcell(getStyle(1,null , 1),rows,3,"");
					 getcell(getStyle(1,null , 1),rows,0,"");
					 number=number+1;
					 countyea=countyea+list1.get(i).getThreeyear()+list1.get(i).getTwoyear()+list1.get(i).getOneyear();
				 }

			 }
		 }
		 
//-------------------------------------------------------------------------------------------------	 
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,4));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(1,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"合计");
		 for(int i=1;i<=3;i++){
			 getcell(getStyle(1,null , 1),rows,i,"");
		 }
		 for(int i=5;i<=7;i++){
			 BigDecimal bd = new BigDecimal(countyea); 
			 getcell(getStyle(1,null , 1),rows,i,""+bd);
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------	
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"三、质量保证能力");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"1、体系认证情况");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		// TODO list2在这里
		 rows =getrow((short) 0,number);
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"管理体系");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"认证体系");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"体系名称");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,"认证机构");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"认证年份");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"有效期");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"认证范围");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 number=number+1;
		 int u=number;
		 for(VendorSurveyDataEntity vendorSurveyDataEntity:list2)
		 {
			 rows =getrow((short) 0,number);
			 getcell(getStyle(1,null, 1),rows,0,"");
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,vendorSurveyDataEntity.getCol2());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorSurveyDataEntity.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,vendorSurveyDataEntity.getCol6());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,vendorSurveyDataEntity.getCol4());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,vendorSurveyDataEntity.getCol5());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,vendorSurveyDataEntity.getCol7());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,vendorSurveyDataEntity.getCol8());
			 number++;
		 }
		 
		 sheet.addMergedRegion(new CellRangeAddress(u-1,number-1, 0,0));
//-------------------------------------------------------------------------------------------------		
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"2、生产能力");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------	
		// TODO list3在这里
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,3));
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"产品");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"生产地");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"生产线（条）");
		 getcell(getStyle(0,null ,1),rows ,3,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"设计产能");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"实际产能");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"产能利用率");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 number=number+1;
		 for(VendorSurveyDataEntity vendorSurveyDataEntity:list3)
		 {
			 rows =getrow((short) 0,number);
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,3));
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,vendorSurveyDataEntity.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,vendorSurveyDataEntity.getCol6());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorSurveyDataEntity.getCol7());
			 getcell(getStyle(0,null,1),rows ,3,"");  
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,vendorSurveyDataEntity.getCol8());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,vendorSurveyDataEntity.getCol9());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,vendorSurveyDataEntity.getCol10());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,vendorSurveyDataEntity.getCol22());
			 number++;
		 }
//-------------------------------------------------------------------------------------------------		
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"3、主要过程及工艺装备水平");
		 for(int i=1;i<=7;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;	
////-------------------------------------------------------------------------------------------------	
//		 // TODO list4在这里
		 
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1, 2));
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"关键过程");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"工艺装备水平");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,"投资金额（万元）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"制造商");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"制造年份");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"工艺能力（关键过程的CPK值）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 number=number+1;
		 for(VendorSurveyDataEntity v:list4)
		 {
			 rows =getrow((short) 0,number);
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 1, 2));
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,v.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,v.getCol2());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,v.getCol3());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,v.getCol4());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,v.getCol5());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,v.getCol6());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,v.getCol7());
			 for(int i=2;i<=2;i++){
			    	HSSFCell cells = rows.createCell(i);
			    	cells.setCellStyle(getStyle(0,null , 1));
		     }
			 number++;
		 }
////-------------------------------------------------------------------------------------------------	 
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"4、研发能力");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 number=number+1;
////-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"（1）近3年开发的产品情况");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;	
////-------------------------------------------------------------------------------------------------	
//		 // TODO list5在这里
		 
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1,2));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,6));
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"开发产品名称");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"主要客户");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,"投放时间");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"前1年年产量");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"技术来源");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 for(int x=2;x<=2;x++){
			 HSSFCell cells = rows.createCell(x);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int x=6;x<=6;x++){
			 HSSFCell cells = rows.createCell(x);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
		 for(VendorSurveyDataEntity v:list5)
		 {
			 rows =getrow((short) 0,number);
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 1,2));
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,6));
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,v.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,v.getCol2());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,v.getCol3());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,v.getCol4());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,v.getCol5());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,v.getCol6());
			 for(int x=2;x<=2;x++){
				 HSSFCell cells = rows.createCell(x);
				 cells.setCellStyle(getStyle(0,null , 1));
			 }
			 for(int x=6;x<=6;x++){
				 HSSFCell cells = rows.createCell(x);
				 cells.setCellStyle(getStyle(0,null , 1));
			 }
			 number++;
		 }
//-------------------------------------------------------------------------------------------------	
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,3));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 4,7));
		 rows =getrow((short) 0,number);
		 String s="";
		 if(list55.size()>0)
		 {
			 s=list55.get(0).getCol4();
		 }
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"（2）供应商试验室资质：");
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,4,s);
		 for(int i=1;i<=3;i++){
			 getcell(getStyle(0,null , 1),rows,i,"");
		 }
		 for(int i=5;i<=7;i++){
			 getcell(getStyle(0,null , 1),rows,i,"");
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------	
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"（3）开发能力");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		// TODO list6在这里
		
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,3));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 4,6));
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"开发人员数量及水平");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"设计手段及能力");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"试验验证设备及能力");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 for(int x=1;x<=1;x++){
			 HSSFCell cells = rows.createCell(x);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int x=3;x<=3;x++){
			 HSSFCell cells = rows.createCell(x);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 for(int x=5;x<=6;x++){
			 HSSFCell cells = rows.createCell(x);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
		 for(VendorSurveyDataEntity v:list55)
		 {
			 rows =getrow((short) 0,number);
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,3));
			 sheet.addMergedRegion(new CellRangeAddress(number,number, 4,6));
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,v.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,v.getCol2());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,v.getCol3());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,v.getCol5());
			 for(int x=1;x<=1;x++){
				 HSSFCell cells = rows.createCell(x);
				 cells.setCellStyle(getStyle(0,null , 1));
			 }
			 for(int x=3;x<=3;x++){
				 HSSFCell cells = rows.createCell(x);
				 cells.setCellStyle(getStyle(0,null , 1));
			 }
			 for(int x=5;x<=6;x++){
				 HSSFCell cells = rows.createCell(x);
				 cells.setCellStyle(getStyle(0,null , 1));
			 }
			 number++;
		 }
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"四、与福田公司合作情况");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 200,(short) 250) , 1),rows,0,"1、福田公司供货情况");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------	
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 0,0));
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 1,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 2,2));
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 3,3));
		 sheet.addMergedRegion(new CellRangeAddress(number,number+1, 7,7));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 4,6));
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"产品名称");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"供货品牌");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,"业务单元");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,"供货比例");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"供货金额（万元）");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 number=number+1;
		 rows =getrow((short) 0,number);

		 for(int i=0;i<=4;i++){
			 getcell(getStyle(0,null , 1),rows,i,"");
		 }

		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,"前3年年");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"前2年年");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,"前1年年");
		 getcell(getStyle(0,null , 1),rows,7,"");
		 number=number+1;
		 
//-------------------------------------------------------------------------------------------------
		 for(VendorSurveyDataEntity v:list6)
		 {
			 rows =getrow((short) 0,number);
			
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,v.getCol1());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,v.getCol2());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,v.getCol3());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,v.getCol4());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,4,v.getCol5());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,v.getCol6());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,6,v.getCol7());
			 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,v.getCol8());
			
			 number++;
		 }
		 
		 
//		 if(list1!=null)
//		 {
//			 int k=list1.size();
//			 for(int i=0;i<k;i++)
//			 {
//				 int j=0;
//				 int uu=number;
//				 if(list1.get(i).getList()!=null)
//				 {
//					j= list1.get(i).getList().size();
//				 }
//			     sheet.addMergedRegion(new CellRangeAddress(number,number+j-1, 0, 0));
//			     rows =getrow((short) 0,number);
//				 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"");
//				 getcell(getStyle(1,null , 1),rows,1,"");
//				 getcell(getStyle(1,null , 1),rows,2,"");
//				 getcell(getStyle(1,null , 1),rows,3,"");
//				 getcell(getStyle(1,null , 1),rows,4,"");
//				 getcell(getStyle(1,null , 1),rows,5,"");
//				 getcell(getStyle(1,null , 1),rows,6,"");
//				 getcell(getStyle(1,null , 1),rows,7,""); 
//				 for(;number<uu+j;number++)
//				 {
//					 rows =getrow((short) 0,number);
//					 getcell(getStyle(1,null , 1),rows,0,"");
//					 getcell(getStyle(1,null , 1),rows,1,"");
//					 getcell(getStyle(1,null , 1),rows,2,"");
//					 getcell(getStyle(1,null , 1),rows,3,"");
//					 getcell(getStyle(1,null , 1),rows,4,"");
//					 getcell(getStyle(1,null , 1),rows,5,"");
//					 getcell(getStyle(1,null , 1),rows,6,"");
//					 getcell(getStyle(1,null , 1),rows,7,"");  
//				 }
//			 }
//		 } 
//-------------------------------------------------------------------------------------------------	 
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"2、质量绩效(市场3个月千台）（供应商主要质量业务单元质量趋势图表）");
		 for(int i=1;i<8;i++){
		    	HSSFCell cells = rows.createCell(i);
		    	cells.setCellStyle(getStyle(0,null , 1));
	     }
		 number=number+1;
//-------------------------------------------------------------------------------------------------	 
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"3、订单绩效（供应商2014年影响订单交付的台数）");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------	 
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"4、成本绩效（近3年零部件商务价格变化幅度趋势图表）");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"5、其它项目");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"1）供应商绩效结果");
		 getcell(getStyle(1,null , 1),rows,1,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorBaseInfo.getVendorClassify2());
		 for(int i=3;i<=7;i++){
			 getcell(getStyle(1,null , 1),rows,i,"");
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"2）近3年评优结果");
		 getcell(getStyle(1,null , 1),rows,1,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorBaseInfo.getQsaResult());
		 for(int i=3;i<=7;i++){
			 getcell(getStyle(1,null , 1),rows,i,"");
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"3）行业地位");
		 getcell(getStyle(1,null , 1),rows,1,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorBaseInfo.getVendorLevel());
		 for(int i=3;i<=7;i++){
			 getcell(getStyle(1,null , 1),rows,i,"");
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,1));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 2,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"4）福田公司近3年质量体系审核结果");
		 getcell(getStyle(1,null , 1),rows,1,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,2,vendorBaseInfo.getQsa());
		 for(int i=3;i<=7;i++){
			 getcell(getStyle(1,null , 1),rows,i,"");
		 }
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0, 7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,getHSSFFont((short) 150,(short) 200) , 1),rows,0,"6、依存度");
		 for(int i=1;i<8;i++){
			 HSSFCell cells = rows.createCell(i);
			 cells.setCellStyle(getStyle(0,null , 1));
		 }
		 number=number+1;
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,0));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1,2));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 3,4));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,6));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 7,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,null , 1),rows,0,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,"前3年年");
		 getcell(getStyle(1,null , 1),rows,2,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,"前2年年");
		 getcell(getStyle(1,null, 1),rows,4,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,"前1年年");
		 getcell(getStyle(1,null , 1),rows,6,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"备注");
		 number=number+1;	
//-------------------------------------------------------------------------------------------------
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,0));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1,2));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 3,4));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,6));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 7,7));
		 rows =getrow((short) 0,number);
		 getcell(getStyle(0,null , 1),rows,0,"福田销售额占总销售额的比例");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,FotonCo.getfoton1(list6,list,1));
		 getcell(getStyle(1,null , 1),rows,2,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,FotonCo.getfoton1(list6,list,2));
		 getcell(getStyle(1,null, 1),rows,4,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,FotonCo.getfoton1(list6,list,3));
		 getcell(getStyle(1,null , 1),rows,6,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"");
		 number=number+1;	
		 rows =getrow((short) 0,number);
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 0,0));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 1,2));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 3,4));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 5,6));
		 sheet.addMergedRegion(new CellRangeAddress(number,number, 7,7));
		 getcell(getStyle(0,null , 1),rows,0,"该供应商在福田公司的供货比例");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,1,FotonCo.getfoton2(list6,1));
		 getcell(getStyle(1,null , 1),rows,2,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,3,FotonCo.getfoton2(list6,2));
		 getcell(getStyle(1,null, 1),rows,4,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,5,FotonCo.getfoton2(list6,3));
		 getcell(getStyle(1,null , 1),rows,6,"");
		 getcell(getStyle(1,getHSSFFont((short) 150,(short) 200) , 1),rows,7,"");

	    wb.write(response.getOutputStream());
	}
}
