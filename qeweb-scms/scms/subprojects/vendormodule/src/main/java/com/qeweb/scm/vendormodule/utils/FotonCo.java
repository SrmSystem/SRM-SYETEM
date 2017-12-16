package com.qeweb.scm.vendormodule.utils;

import java.text.DecimalFormat;
import java.util.List;

import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;

public class FotonCo {

	
	public static String getfoton1(List<VendorSurveyDataEntity> list6, List<VendorExcel2> list, double i)
	{
		DecimalFormat df = new DecimalFormat("0.00");   
		if(i==1)
		{
			double j=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol5());
				}catch(Exception e){
					j=j+0;
				}
			}
			
			try{
				k=j/Double.parseDouble(list.get(1).getCol1());
			}catch(Exception e){
				k=0;
			}
			return ""+df.format(k);
		}
		if(i==2)
		{
			double j=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol6());
				}catch(Exception e){
					j=j+0;
				}
			}
			
			try{
				k=j/Double.parseDouble(list.get(1).getCol2());
			}catch(Exception e){
				k=0;
			}
			return ""+df.format(k);
		}
		if(i==3)
		{
			double j=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol7());
				}catch(Exception e){
					j=j+0;
				}
			}
			
			try{
				k=j/Double.parseDouble(list.get(1).getCol3());
			}catch(Exception e){
				k=0;
			}
			return ""+df.format(k);
		}
		return "";
	}
	public static String getfoton2(List<VendorSurveyDataEntity> list6, double i)
	{
		DecimalFormat df = new DecimalFormat("0.00");  
		if(i==1)
		{
			double j=0;
			double l=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol5());
				}catch(Exception e){
					j=j+0;
				}
				try{
					l=l+(Double.parseDouble(v.getCol5())/Double.parseDouble(v.getCol4()));
				}catch(Exception e){
					l=l+0;
				}
			}
			if(j!=0)
			{
				k=j/l;
			}
			return ""+df.format(k);
		}
		if(i==2)
		{
			double j=0;
			double l=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol6());
				}catch(Exception e){
					j=j+0;
				}
				try{
					l=l+(Double.parseDouble(v.getCol6())/Double.parseDouble(v.getCol4()));
				}catch(Exception e){
					l=l+0;
				}
			}
			if(j!=0)
			{
				k=j/l;
			}
			return ""+df.format(k);
		}
		if(i==3)
		{
			double j=0;
			double l=0;
			double k=0;
			for(VendorSurveyDataEntity v:list6)
			{
				try{
					j=j+Double.parseDouble(v.getCol7());
				}catch(Exception e){
					j=j+0;
				}
				try{
					l=l+(Double.parseDouble(v.getCol7())/Double.parseDouble(v.getCol4()));
				}catch(Exception e){
					l=l+0;
				}
			}
			if(j!=0)
			{
				k=j/l;
			}
			return ""+df.format(k);
		}
		return "";
	}
}
