package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="qeweb_material")
public class MaterialEntity extends BaseEntity {

	private String code;
	private String name;  
	private String specification;		//规格
	private String describe;			//描述
	private String unit;				//单位
	private String unitAmount;			//单位数量
	private String picStatus;			//图纸状态
	private Integer enableStatus;		//禁用，启用
	private Integer categoryStatus;		//分类状态，0-未分类，1-已分类
	private String technician;			//技术人员	
	private String partsCode;			// 零部件编码
	private String partsName;			// 零部件名称
	private String partsType;           // 零部件类别
	private Long materialTypeId;			
	private String remark;
	
	private String vesion;				//版本
	private String edition;				//版次
	private String referenceNum;		//参图号
	private String mappableUnit;		//图幅
	private String weight;				//重量
	private String grade;				//分级
	private String rawmaterialCode;     //材料牌号
	private String boxType;             //所用箱型
	
	private String col1;				//安全库存
	private String col2;				//最大数量
	private String col3;				//EOQ(订单批量)
	private String col4;				//再订货点
	private String col5;				//采购员
	private String col6;                //理论重量
	
	//扩展字段
	private String col7;
	private String col8;
	private String cdqwl;               //长短周期 X=长周期  空白短周期
	//透明字段
	private String c22;
	private Double vmiStockQty;        // 三方库存
	private Double wmsStockQty;        // wms库存
	private Double reqMonthQty;        // 当月需求量
	
	
	private MaterialTypeEntity materialType;
	


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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(String unitAmount) {
		this.unitAmount = unitAmount;
	}


	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getPartsCode() {
		return partsCode;
	}

	public void setPartsCode(String partsCode) {
		this.partsCode = partsCode;
	}

	public String getPartsName() {
		return partsName;
	}

	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}

	

	public String getPicStatus() {
		return picStatus;
	}

	public void setPicStatus(String picStatus) {
		this.picStatus = picStatus;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public Integer getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Integer categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@JoinColumn(name = "materialTypeId",insertable = false,updatable=false)
	public MaterialTypeEntity getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialTypeEntity materialType) {
		this.materialType = materialType;
	}

	public String getVesion() {
		return vesion;
	}

	public void setVesion(String vesion) {
		this.vesion = vesion;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(String referenceNum) {
		this.referenceNum = referenceNum;
	}

	public String getMappableUnit() {
		return mappableUnit;
	}

	public void setMappableUnit(String mappableUnit) {
		this.mappableUnit = mappableUnit;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPartsType() {
		return partsType;
	}

	public void setPartsType(String partsType) {
		this.partsType = partsType;
	}

	public String getRawmaterialCode() {
		return rawmaterialCode;
	}

	public void setRawmaterialCode(String rawmaterialCode) {
		this.rawmaterialCode = rawmaterialCode;
	}

	public String getBoxType() {
		return boxType;
	}

	public void setBoxType(String boxType) {
		this.boxType = boxType;
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

	@Transient
	public String getC22() {
		return c22;
	}

	public void setC22(String c22) {
		this.c22 = c22;
	}

	@Transient
	public Double getVmiStockQty() {
		return vmiStockQty;
	}

	public void setVmiStockQty(Double vmiStockQty) {
		this.vmiStockQty = vmiStockQty;
	}

	@Transient
	public Double getWmsStockQty() {
		return wmsStockQty;
	}

	public void setWmsStockQty(Double wmsStockQty) {
		this.wmsStockQty = wmsStockQty;
	}

	@Transient
	public Double getReqMonthQty() {
		return reqMonthQty;
	}

	public void setReqMonthQty(Double reqMonthQty) {
		this.reqMonthQty = reqMonthQty;
	}

	public String getCdqwl() {
		return cdqwl;
	}

	public void setCdqwl(String cdqwl) {
		this.cdqwl = cdqwl;
	}
	
	
}
