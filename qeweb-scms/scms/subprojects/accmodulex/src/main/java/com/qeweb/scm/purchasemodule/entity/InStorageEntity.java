package com.qeweb.scm.purchasemodule.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 入库主单
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_in_storage")
public class InStorageEntity extends BaseEntity {

	private String inStorageCode;		//入库单号
	private OrganizationEntity buyer;
	private OrganizationEntity vendor;
	private Set<InStorageItemEntity> inStorageItem;
	private Integer inStorageType;//类型1，国内，2，国外，3，外协
	private String orgId;
	private String consignedFlag;  //是否上线(入库为空，上线为Y)
	private String poNumber;

	@Column(name="in_storage_code")
	public String getInStorageCode() {
		return inStorageCode;
	}

	public void setInStorageCode(String inStorageCode) {
		this.inStorageCode = inStorageCode;
	}

	@ManyToOne
	@JoinColumn(name="buyer_id")
	public OrganizationEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@OneToMany(mappedBy="inStorage")
	@JsonIgnore
	public Set<InStorageItemEntity> getInStorageItem() {
		return inStorageItem;
	}

	public void setInStorageItem(Set<InStorageItemEntity> inStorageItem) {
		this.inStorageItem = inStorageItem;
	}

	@Column(name="in_storage_type")
	public Integer getInStorageType() {
		return inStorageType;
	}

	public void setInStorageType(Integer inStorageType) {
		this.inStorageType = inStorageType;
	}


	@Column(name="org_id")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getConsignedFlag() {
		return consignedFlag;
	}

	public void setConsignedFlag(String consignedFlag) {
		this.consignedFlag = consignedFlag;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
}
