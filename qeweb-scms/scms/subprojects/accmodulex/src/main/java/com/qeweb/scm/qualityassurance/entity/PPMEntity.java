package com.qeweb.scm.qualityassurance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@Entity
@Table(name = "QEWEB_QM_PPM")
public class PPMEntity extends BaseEntity {

	private String month;
	private OrganizationEntity vendor;
	private Double ppm;
	private Double rate;
	private Integer ppmType;
	private MaterialEntity material;

	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@ManyToOne
	@JoinColumn(name = "material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@ManyToOne
	@JoinColumn(name = "vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	public Double getPpm() {
		return ppm;
	}

	public void setPpm(Double ppm) {
		this.ppm = ppm;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Column(name="PPM_TYPE")
	public Integer getPpmType() {
		return ppmType;
	}

	public void setPpmType(Integer ppmType) {
		this.ppmType = ppmType;
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

}
