package com.qeweb.scm.purchasemodule.web.vo;

/**
 * 补货VO
 * 
 * @author chao.gu 20170913
 */
public class ReplishmentVO {
	private String BOLNR;// ASN单号
	private String EBELP; // 采购订单号行项目
	private String SOURCE_BOLNR;// 原ASN单号
	private String VBELN;// 原交货单号（DN）
	private String MATNR;// 物料号
	private String MAKTX;// 物料描述
	private String LIFNR;// 供应商账号
	private String EBELN;// 采购订单号
	private String LFIMG;// 补货数量
	private String MEINS;// 采购订单单位
	private String CHARG;// 批号
	private String HSDAT;// 生产日期
	private String VERSION;// 版本

	public String getBOLNR() {
		return BOLNR;
	}

	public void setBOLNR(String bOLNR) {
		BOLNR = bOLNR;
	}

	public String getEBELP() {
		return EBELP;
	}

	public void setEBELP(String eBELP) {
		EBELP = eBELP;
	}

	public String getSOURCE_BOLNR() {
		return SOURCE_BOLNR;
	}

	public void setSOURCE_BOLNR(String sOURCE_BOLNR) {
		SOURCE_BOLNR = sOURCE_BOLNR;
	}


	public String getVBELN() {
		return VBELN;
	}

	public void setVBELN(String vBELN) {
		VBELN = vBELN;
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

	public String getLIFNR() {
		return LIFNR;
	}

	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}

	public String getEBELN() {
		return EBELN;
	}

	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}

	public String getLFIMG() {
		return LFIMG;
	}

	public void setLFIMG(String lFIMG) {
		LFIMG = lFIMG;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getCHARG() {
		return CHARG;
	}

	public void setCHARG(String cHARG) {
		CHARG = cHARG;
	}

	public String getHSDAT() {
		return HSDAT;
	}

	public void setHSDAT(String hSDAT) {
		HSDAT = hSDAT;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

}
