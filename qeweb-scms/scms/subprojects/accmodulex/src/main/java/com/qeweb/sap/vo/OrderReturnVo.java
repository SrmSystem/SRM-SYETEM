package com.qeweb.sap.vo;

/**
 * 退货接口vo
 *
 */
public class OrderReturnVo {
	private String EBELN;	//CHAR	10		采购凭证号 
	private Integer EBELP;	//NUMC	5		采购凭证的项目编号 
	private Double MENGE;	//QUAN	13		数量
	private String MEINS;	//UNIT	3		基本计量单位
	private String BUDAT;
	private String CPUTM;
	
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
	public Double getMENGE() {
		return MENGE;
	}
	public void setMENGE(Double mENGE) {
		MENGE = mENGE;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
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
	

	
}
