package com.qeweb.sap.vo;

/**
 *采购订单价格VO
 */
public class OrderPriceVo {
	
	private String  EBELN;	//CHAR	10		采购凭证号 
	private Integer EBELP;	//NUMC	5		采购凭证的项目编号 
	
	private String MATNR;	//CHAR	18		物料号 
	private String MAKTX;	//CHAR	40		物料描述（短文本） 
	private String WAERS;	//CUKY	5		货币码 
	
	private Double ZZSDJ;	//CURR	16		净价 
	private Double ZHD;	//CURR	16		含税价 
	
	public String getEBELN() {
		return EBELN;
	}
	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}
	public Integer getEBELP() {
		return EBELP;
	}
	public void setEBELP(Integer eBELP) {
		EBELP = eBELP;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getMAKTX() {
		return MAKTX;
	}
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	public String getWAERS() {
		return WAERS;
	}
	public void setWAERS(String wAERS) {
		WAERS = wAERS;
	}
	public Double getZZSDJ() {
		return ZZSDJ;
	}
	public void setZZSDJ(Double zZSDJ) {
		ZZSDJ = zZSDJ;
	}
	public Double getZHD() {
		return ZHD;
	}
	public void setZHD(Double zHD) {
		ZHD = zHD;
	}
	
	


	
}
