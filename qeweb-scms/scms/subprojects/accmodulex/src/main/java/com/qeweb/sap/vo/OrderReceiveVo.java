package com.qeweb.sap.vo;

/**
 * 收货接口vo
 *
 */
public class OrderReceiveVo {
	private String VBELN;	//CHAR	10		交货
	private String EBELN;	//CHAR	10		采购凭证号 
	private Integer EBELP;	//NUMC	5		采购凭证的项目编号 
	private String BOLNR;	//CHAR	35		提单
	private Double LFIMG;	//QUAN	13		实际已交货量（按销售单位）
	private String MEINS;	//UNIT	3		基本计量单位
	private Double ZDJSL;	//QUAN	13		待检数量
	private Double ZZJBL;	//QUAN	13		质检不良数量
	private Double ZSJHG;	//QUAN	13		送检合格
	private Double ZLLBL;	//QUAN	13		来料不良数量
	
	private String BUDAT;
	private String CPUTM;
	private String REPLCODE;//补货ASN单号
	
	public String getVBELN() {
		return VBELN;
	}
	public void setVBELN(String vBELN) {
		VBELN = vBELN;
	}
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
	public String getBOLNR() {
		return BOLNR;
	}
	public void setBOLNR(String bOLNR) {
		BOLNR = bOLNR;
	}
	public Double getLFIMG() {
		return LFIMG;
	}
	public void setLFIMG(Double lFIMG) {
		LFIMG = lFIMG;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public Double getZDJSL() {
		return ZDJSL;
	}
	public void setZDJSL(Double zDJSL) {
		ZDJSL = zDJSL;
	}
	public Double getZZJBL() {
		return ZZJBL;
	}
	public void setZZJBL(Double zZJBL) {
		ZZJBL = zZJBL;
	}
	public Double getZSJHG() {
		return ZSJHG;
	}
	public void setZSJHG(Double zSJHG) {
		ZSJHG = zSJHG;
	}
	public Double getZLLBL() {
		return ZLLBL;
	}
	public void setZLLBL(Double zLLBL) {
		ZLLBL = zLLBL;
	}
	public String getBUDAT() {
		return BUDAT;
	}
	public void setBUDAT(String bUDAT) {
		BUDAT = bUDAT;
	}
	public String getCPUTM() {
		return CPUTM;
	}
	public void setCPUTM(String cPUTM) {
		CPUTM = cPUTM;
	}
	public String getREPLCODE() {
		return REPLCODE;
	}
	public void setREPLCODE(String rEPLCODE) {
		REPLCODE = rEPLCODE;
	}


	
}
