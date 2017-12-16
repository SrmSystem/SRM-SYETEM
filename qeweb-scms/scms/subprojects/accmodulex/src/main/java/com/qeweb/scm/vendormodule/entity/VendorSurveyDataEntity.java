package com.qeweb.scm.vendormodule.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * @author pjjxiajun
 * @date 2015年7月5日
 * @path com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity.java
 */
@Entity
@Table(name = "qeweb_vendor_survey_data")
public class VendorSurveyDataEntity extends BaseEntity{
	private Long vendorCfgId;//供应商调查表配置ID
	private long baseId;
	private String ctId;
	private String ctCode;
	private String ctName;
	private Long vendorId;
	private Long orgId;
	private Long templateId;
	private Integer dataType;
	private boolean fixed;
	
	//级联对象
	private OrganizationEntity organizationEntity;
	private VendorSurveyBaseEntity vendorSurveybase;
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	private String col11;
	private String col12;
	private String col13;
	private String col14;
	private String col15;
	private String col16;
	private String col17;
	private String col18;
	private String col19;
	private String col20;
	private String col21;
	private String col22;
	private String col23;
	private String col24;
	private String col25;
	private String col26;
	private String col27;
	private String col28;
	private String col29;
	private String col30;
	
	
	public Long getVendorCfgId() {
		return vendorCfgId;
	}
	public void setVendorCfgId(Long vendorCfgId) {
		this.vendorCfgId = vendorCfgId;
	}
	public boolean getFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	public long getBaseId() {
		return baseId;
	}
	public void setBaseId(long baseId) {
		this.baseId = baseId;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getCtId() {
		return ctId;
	}
	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	public String getCtCode() {
		return ctCode;
	}
	public void setCtCode(String ctCode) {
		this.ctCode = ctCode;
	}
	public String getCtName() {
		return ctName;
	}
	public void setCtName(String ctName) {
		this.ctName = ctName;
	}

	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	public String getCol4() {
		return col4;
	}
	public void setCol4(String col4) {
		this.col4 = col4;
	}
	public String getCol5() {
		return col5;
	}
	public void setCol5(String col5) {
		this.col5 = col5;
	}
	public String getCol6() {
		return col6;
	}
	public void setCol6(String col6) {
		this.col6 = col6;
	}
	public String getCol7() {
		return col7;
	}
	public void setCol7(String col7) {
		this.col7 = col7;
	}
	public String getCol8() {
		return col8;
	}
	public void setCol8(String col8) {
		this.col8 = col8;
	}
	public String getCol9() {
		return col9;
	}
	public void setCol9(String col9) {
		this.col9 = col9;
	}
	public String getCol10() {
		return col10;
	}
	public void setCol10(String col10) {
		this.col10 = col10;
	}
	public String getCol11() {
		return col11;
	}
	public void setCol11(String col11) {
		this.col11 = col11;
	}
	public String getCol12() {
		return col12;
	}
	public void setCol12(String col12) {
		this.col12 = col12;
	}
	public String getCol13() {
		return col13;
	}
	public void setCol13(String col13) {
		this.col13 = col13;
	}
	public String getCol14() {
		return col14;
	}
	public void setCol14(String col14) {
		this.col14 = col14;
	}
	public String getCol15() {
		return col15;
	}
	public void setCol15(String col15) {
		this.col15 = col15;
	}
	public String getCol16() {
		return col16;
	}
	public void setCol16(String col16) {
		this.col16 = col16;
	}
	public String getCol17() {
		return col17;
	}
	public void setCol17(String col17) {
		this.col17 = col17;
	}
	public String getCol18() {
		return col18;
	}
	public void setCol18(String col18) {
		this.col18 = col18;
	}
	public String getCol19() {
		return col19;
	}
	public void setCol19(String col19) {
		this.col19 = col19;
	}
	public String getCol20() {
		return col20;
	}
	public void setCol20(String col20) {
		this.col20 = col20;
	}
	public String getCol21() {
		return col21;
	}
	public void setCol21(String col21) {
		this.col21 = col21;
	}
	public String getCol22() {
		return col22;
	}
	public void setCol22(String col22) {
		this.col22 = col22;
	}
	public String getCol23() {
		return col23;
	}
	public void setCol23(String col23) {
		this.col23 = col23;
	}
	public String getCol24() {
		return col24;
	}
	public void setCol24(String col24) {
		this.col24 = col24;
	}
	public String getCol25() {
		return col25;
	}
	public void setCol25(String col25) {
		this.col25 = col25;
	}
	public String getCol26() {
		return col26;
	}
	public void setCol26(String col26) {
		this.col26 = col26;
	}
	public String getCol27() {
		return col27;
	}
	public void setCol27(String col27) {
		this.col27 = col27;
	}
	public String getCol28() {
		return col28;
	}
	public void setCol28(String col28) {
		this.col28 = col28;
	}
	public String getCol29() {
		return col29;
	}
	public void setCol29(String col29) {
		this.col29 = col29;
	}
	public String getCol30() {
		return col30;
	}
	public void setCol30(String col30) {
		this.col30 = col30;
	}
	@ManyToOne
	@JoinColumn(name = "orgId",insertable = false,updatable = false)
	public OrganizationEntity getOrganizationEntity() {
		return organizationEntity;
	}
	public void setOrganizationEntity(OrganizationEntity organizationEntity) {
		this.organizationEntity = organizationEntity;
	}
	@ManyToOne
	@JoinColumn(name = "baseId",insertable = false,updatable = false)
	public VendorSurveyBaseEntity getVendorSurveybase() {
		return vendorSurveybase;
	}
	public void setVendorSurveybase(VendorSurveyBaseEntity vendorSurveybase) {
		this.vendorSurveybase = vendorSurveybase;
	}
	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}
	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}
	@Transient
	public String getXMLVal(String colName) {
    	Map<String,String> colMap = new HashMap<String,String>();
    	colMap.put("col1", getCol1());
    	colMap.put("col2", getCol2());
    	colMap.put("col3", getCol3());
    	colMap.put("col4", getCol4());
    	colMap.put("col5", getCol5());
    	colMap.put("col6", getCol6());
    	colMap.put("col7", getCol7());
    	colMap.put("col8", getCol8());
    	colMap.put("col9", getCol9());
    	colMap.put("col10", getCol10());
    	colMap.put("col11", getCol11());
    	colMap.put("col12", getCol12());
    	colMap.put("col13", getCol13());
    	colMap.put("col14", getCol14());
    	colMap.put("col15", getCol15());
    	colMap.put("col16", getCol16());
    	colMap.put("col17", getCol17());
    	colMap.put("col18", getCol18());
    	colMap.put("col19", getCol19());
    	colMap.put("col20", getCol20());
    	colMap.put("col21", getCol21());
    	colMap.put("col22", getCol22());
    	colMap.put("col23", getCol23());
    	colMap.put("col24", getCol24());
    	colMap.put("col25", getCol25());
    	colMap.put("col26", getCol26());
    	colMap.put("col27", getCol27());
    	colMap.put("col28", getCol28());
    	colMap.put("col29", getCol29());
    	colMap.put("col30", getCol30());
    	return colMap.get(colName);
	}
	
	
	

	

}
