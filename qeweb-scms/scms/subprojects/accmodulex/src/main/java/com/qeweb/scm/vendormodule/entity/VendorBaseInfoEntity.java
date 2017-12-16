package com.qeweb.scm.vendormodule.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.SectionOfficeEntity;

@Entity
@Table(name = "QEWEB_VENDOR_BASE_INFO")
public class VendorBaseInfoEntity extends BaseEntity{

	private Long orgId;
	private String code;
	private String name;
	private String shortName;
	private String duns;			//邓白氏编码
	//供应商性质  暂时处理原有数据的问题。新增如下字段
	//1：国有企业 2：国有控股企业 3：外资企业 4：合资企业 5:私营企业
	//暂时处理原有数据的问题。新增如下
	//10:有限公司 11：私有控股 12:外商独资 13:国有(集体)控股 14:私营企业
	//15:集体所有制企业 16:外资企业  17:私营股份 18:集体 19:股份制 20：国有控股企业 21：集体企业 22：私营控股企业 23：有限责任公司 24：中德合资 25：私营有限责任公司 26：股份制企业 27：中外合资
	
	private String property;		
	private String legalPerson;		//企业法人
	private Long country;  
	private String countryText;		
	private Long province;
	private String provinceText;
	private Long city;
	private String cityText;
	private Long district;			//区域
	private String districtText;
	private String address;
	private Timestamp regtime;
	private Long materialId;
	private Long materialTypeId;
	private String mainProduct;		//主要产品,根据选择的可供物料来获取
	private String productLine;		//产品线
	private String lastYearIncome;	//上年度销售收入（万元）
	private String employeeAmount;	//员工人数
	private String stockShare;		//股比构成
	private String productPower;	//产能
	private Integer ipo;			//是否上市
	private String webAddr;			//网址
	private String taxId;			//税务登记号
	private String ownership;		//企业所有权
	private String bankName;		//银行名称
	private String bankCard;		//银行帐号
    private String mainBU;			//主供事业部
    private String regCapital;		//注册资本
    private String totalCapital;	//总资本
    private String workingCapital;	//流动总金额
    private String floorArea;		//占地面积
    private Long vendorType;		//供应商类型
    private String vendorLevel;		//供应商等级
    private String vendorClassify;	//供应商分类1
    private String vendorClassify2;	//供应商分类2，目前ABC
    private String qsa;				//质量体系审核
    private String qsaResult;		//质量体系审核评优结果
    private String qsCertificate;   //质量体系证书
    private String qsReport;   //质量体系报告证书-文件路径
    //add by chao.gu 20161104 现场审核报告
    private String siteAudiReportName;//文件名
    private String siteAudiReportPath;//路径
    //add end
    
	private Long phaseId;//所在阶段,供应商阶段定义，非配置
	private Long templateId;//所采用的模版
	private Long phaseCfgId;//所在的配置阶段
	private Integer  phaseSn;//所处的配置阶段顺序
	
	private Integer versionNO;//版本号，记录版本号，编辑时，一定是最新版本
	private Integer currentVersion;//是否为当前版本。第一个版本和最新的通过审核的版本才是当前版本
	private Integer auditStatus;//审核状态
	private Integer approveStatus;//审批状态
	private Integer submitStatus;//提交状态
	
	private String auditReason;//审核原因
	private String auditUser;//审核人
	
	private Long soId;//科室ID
	private Integer soStatus;//是否已经有科室
	
	private SectionOfficeEntity office;
	
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
	private String col30;//体系外供应商标示   1为体系外
    
	//级联对象
	private OrganizationEntity org;
	private VendorPhaseEntity vendorPhase;
	
	private AreaEntity countrys;
	private AreaEntity provinces;
	private AreaEntity citys;
	private AreaEntity districts;
	
	private Integer isControl;//是否统管

	//非持久化字段
	private List<VendorBaseInfoExEntity> exList;//供应商基本信息-扩展信息集合
	private int surveyTotal;//当前阶段调查表总数
	private String surveySubmitInfo;//供应商调查表提交情况  ps:0/2
	private String surveyAuditInfo;//供应商调查表审核情况  ps:0/2
	private Integer statisticsCount;//供应商统计数量，供统计分析中计算供应商数量使用
	private List<VendorMaterialRelEntity> venMatRelList;//供应商供货关系集合
	private String propertyText;		//供应商性质文本
	private String mainCustomer;	
	private String mainBUss;
	private String ids;
	private Long buyerId;
	
	private String buyerName;	//采购组织名称
	private String buyerCode;	//采购组织编码
	
	
	
	public Long getVendorType() {
		return vendorType;
	}
	public void setVendorType(Long vendorType) {
		this.vendorType = vendorType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
	public Long getCountry() {
		return country;
	}
	public void setCountry(Long country) {
		this.country = country;
	}
	public String getCountryText() {
		return countryText;
	}
	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}
	public Long getProvince() {
		return province;
	}
	public void setProvince(Long province) {
		this.province = province;
	}
	public String getProvinceText() {
		return provinceText;
	}
	public void setProvinceText(String provinceText) {
		this.provinceText = provinceText;
	}
	public Long getCity() {
		return city;
	}
	public void setCity(Long city) {
		this.city = city;
	}
	public String getCityText() {
		return cityText;
	}
	public void setCityText(String cityText) {
		this.cityText = cityText;
	}
	public String getDistrictText() {
		return districtText;
	}
	public void setDistrictText(String districtText) {
		this.districtText = districtText;
	}
	public void setDistrict(Long district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Long getMaterialTypeId() {
		return materialTypeId;
	}
	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getLastYearIncome() {
		return lastYearIncome;
	}
	public void setLastYearIncome(String lastYearIncome) {
		this.lastYearIncome = lastYearIncome;
	}
	public String getEmployeeAmount() {
		return employeeAmount;
	}
	public void setEmployeeAmount(String employeeAmount) {
		this.employeeAmount = employeeAmount;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getDuns() {
		return duns;
	}
	public void setDuns(String duns) {
		this.duns = duns;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8:00")
	public Timestamp getRegtime() {
		return regtime;
	}
	public void setRegtime(Timestamp regtime) {
		this.regtime = regtime;
	}
	public String getMainProduct() {
		return mainProduct;
	}
	public void setMainProduct(String mainProduct) {
		this.mainProduct = mainProduct;
	}
	public Long getPhaseId() {
		return phaseId;
	}
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	
	public String getProductPower() {
		return productPower;
	}
	public void setProductPower(String productPower) {
		this.productPower = productPower;
	}
	@ManyToOne
	@JoinColumn(name = "orgId",insertable = false,updatable = false)
	public OrganizationEntity getOrg() {
		return org;
	}
	public void setOrg(OrganizationEntity org) {
		this.org = org;
	}
	
	@ManyToOne
	@JoinColumn(name = "phaseId",insertable = false,updatable = false)
	public VendorPhaseEntity getVendorPhase() {
		return vendorPhase;
	}
	public void setVendorPhase(VendorPhaseEntity vendorPhase) {
		this.vendorPhase = vendorPhase;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Integer getIpo() {
		return ipo;
	}
	public void setIpo(Integer ipo) {
		this.ipo = ipo;
	}
	public String getStockShare() {
		return stockShare;
	}
	public void setStockShare(String stockShare) {
		this.stockShare = stockShare;
	}
	public String getWebAddr() {
		return webAddr;
	}
	public void setWebAddr(String webAddr) {
		this.webAddr = webAddr;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getOwnership() {
		return ownership;
	}
	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getMainBU() {
		return mainBU;
	}
	public void setMainBU(String mainBU) {
		this.mainBU = mainBU;
	}
	public String getRegCapital() {
		return regCapital;
	}
	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}
	public String getTotalCapital() {
		return totalCapital;
	}
	public void setTotalCapital(String totalCapital) {
		this.totalCapital = totalCapital;
	}
	public String getWorkingCapital() {
		return workingCapital;
	}
	public void setWorkingCapital(String workingCapital) {
		this.workingCapital = workingCapital;
	}
	public String getFloorArea() {
		return floorArea;
	}
	public void setFloorArea(String floorArea) {
		this.floorArea = floorArea;
	}
	public Long getPhaseCfgId() {
		return phaseCfgId;
	}
	public void setPhaseCfgId(Long phaseCfgId) {
		this.phaseCfgId = phaseCfgId;
	}
	public Integer getPhaseSn() {
		return phaseSn;
	}
	public void setPhaseSn(Integer phaseSn) {
		this.phaseSn = phaseSn;
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
	
/* ********************查看使用*************************** */	
	@Transient
	public int getSurveyTotal() {
		return surveyTotal;
	}
	public void setSurveyTotal(int surveyTotal) {
		this.surveyTotal = surveyTotal;
	}
	@Transient
	public String getSurveySubmitInfo() {
		return surveySubmitInfo;
	}
	public void setSurveySubmitInfo(String surveySubmitInfo) {
		this.surveySubmitInfo = surveySubmitInfo;
	}
	
	public Long getDistrict() {
		return district;
	}
	@Transient
	public String getSurveyAuditInfo() {
		return surveyAuditInfo;
	}
	public void setSurveyAuditInfo(String surveyAuditInfo) {
		this.surveyAuditInfo = surveyAuditInfo;
	}
	@Transient
	public List<VendorBaseInfoExEntity> getExList() {
		return exList;
	}
	public void setExList(List<VendorBaseInfoExEntity> exList) {
		this.exList = exList;
	}
	public Integer getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(Integer versionNO) {
		this.versionNO = versionNO;
	}
	public Integer getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(Integer currentVersion) {
		this.currentVersion = currentVersion;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Integer getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}
	public Integer getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getVendorLevel() {
		return vendorLevel;
	}
	public void setVendorLevel(String vendorLevel) {
		this.vendorLevel = vendorLevel;
	}
	public String getVendorClassify() {
		return vendorClassify;
	}
	public void setVendorClassify(String vendorClassify) {
		this.vendorClassify = vendorClassify;
	}
	public String getVendorClassify2() {
		return vendorClassify2;
	}
	public void setVendorClassify2(String vendorClassify2) {
		this.vendorClassify2 = vendorClassify2;
	}
	public String getQsa() {
		return qsa;
	}
	public void setQsa(String qsa) {
		this.qsa = qsa;
	}
	public String getQsaResult() {
		return qsaResult;
	}
	public void setQsaResult(String qsaResult) {
		this.qsaResult = qsaResult;
	}
	@Transient
	public Integer getStatisticsCount() {
		return statisticsCount;
	}
	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}
	@Transient
	public List<VendorMaterialRelEntity> getVenMatRelList() {
		return venMatRelList;
	}
	public void setVenMatRelList(List<VendorMaterialRelEntity> venMatRelList) {
		this.venMatRelList = venMatRelList;
	}
	public String getQsCertificate() {
		return qsCertificate;
	}
	public void setQsCertificate(String qsCertificate) {
		this.qsCertificate = qsCertificate;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getQsReport() {
		return qsReport;
	}
	public void setQsReport(String qsReport) {
		this.qsReport = qsReport;
	}
	
	
	public String getSiteAudiReportName() {
		return siteAudiReportName;
	}
	public void setSiteAudiReportName(String siteAudiReportName) {
		this.siteAudiReportName = siteAudiReportName;
	}
	public String getSiteAudiReportPath() {
		return siteAudiReportPath;
	}
	public void setSiteAudiReportPath(String siteAudiReportPath) {
		this.siteAudiReportPath = siteAudiReportPath;
	}
	@Transient
	public String getPropertyText() {
		return propertyText;
	}
	public void setPropertyText(String propertyText) {
		this.propertyText = propertyText;
	}
	@Transient
	public String getMainCustomer() {
		return mainCustomer;
	}
	public void setMainCustomer(String mainCustomer) {
		this.mainCustomer = mainCustomer;
	}
	@Transient
	public String getMainBUss() {
		return mainBUss;
	}
	public void setMainBUss(String mainBUss) {
		this.mainBUss = mainBUss;
	}
	public Long getSoId() {
		return soId;
	}
	public void setSoId(Long soId) {
		this.soId = soId;
	}
	public Integer getSoStatus() {
		return soStatus;
	}
	public void setSoStatus(Integer soStatus) {
		this.soStatus = soStatus;
	}
	@ManyToOne
	@JoinColumn(name = "soId",insertable = false,updatable = false)
	public SectionOfficeEntity getOffice() {
		return office;
	}
	public void setOffice(SectionOfficeEntity office) {
		this.office = office;
	}
	@ManyToOne
	@JoinColumn(name = "country",insertable = false,updatable = false)
	public AreaEntity getCountrys() {
		return countrys;
	}
	public void setCountrys(AreaEntity countrys) {
		this.countrys = countrys;
	}
	@ManyToOne
	@JoinColumn(name = "province",insertable = false,updatable = false)
	public AreaEntity getProvinces() {
		return provinces;
	}
	public void setProvinces(AreaEntity provinces) {
		this.provinces = provinces;
	}
	@ManyToOne
	@JoinColumn(name = "city",insertable = false,updatable = false)
	public AreaEntity getCitys() {
		return citys;
	}
	public void setCitys(AreaEntity citys) {
		this.citys = citys;
	}
	@ManyToOne
	@JoinColumn(name = "district",insertable = false,updatable = false)
	public AreaEntity getDistricts() {
		return districts;
	}
	public void setDistricts(AreaEntity districts) {
		this.districts = districts;
	}
	public Integer getIsControl() {
		return isControl;
	}
	public void setIsControl(Integer isControl) {
		this.isControl = isControl;
	}
	@Transient
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Transient
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	@Transient
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@Transient
	public String getBuyerCode() {
		return buyerCode;
	}
	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}
	
	
//	@Transient
//	public Long getSoId() {
//		return soId;
//	}
//	public void setSoId(Long soId) {
//		this.soId = soId;
//	}
//	@Transient
//	public Integer getSoStatus() {
//		return soStatus;
//	}
//	public void setSoStatus(Integer soStatus) {
//		this.soStatus = soStatus;
//	}
}
