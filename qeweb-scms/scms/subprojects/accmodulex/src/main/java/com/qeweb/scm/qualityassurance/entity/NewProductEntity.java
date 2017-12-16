package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 新产品开发质量
 * @author lyy
 *
 */
@Entity
@Table(name = "qeweb_qm_newproduct")
public class NewProductEntity  extends BaseEntity{
	private String seq;			//序号
	private Timestamp qmTime;		//时间
	private String month; //月份
//	private Long vendorId;
//	private Long matId;
	private String matName;
	private int sampleSize;	//样件/小批量
	private int deliverTimes;	//第几次交付
	private int qualified;		//合格
	private int dataStatus;	//发布状态
	private MaterialEntity material;		//零件(零件名称、零件号、版本号、时间)
	private OrganizationEntity vendor;		//供应商(供应商编号、供应商名称、供应商联系人、供应商联系电话)
//	private UserEntity user;
	private long ppap;//PPAP一次通过率
	@JoinColumn(name="SEQ")
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	
	@JoinColumn(name="QM_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getQmTime() {
		return qmTime;
	}
	public void setQmTime(Timestamp qmTime) {
		this.qmTime = qmTime;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	@JoinColumn(name="SAMPLE_SIZE")
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	
	@JoinColumn(name="DELIVER_TIMES")
	public int getDeliverTimes() {
		return deliverTimes;
	}
	public void setDeliverTimes(int deliverTimes) {
		this.deliverTimes = deliverTimes;
	}
	
	@JoinColumn(name="QUALIFIED")
	public int getQualified() {
		return qualified;
	}
	public void setQualified(int qualified) {
		this.qualified = qualified;
	}
	
	@JoinColumn(name="DATA_STATUS")
	public int getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(int dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	@JoinColumn(name="MAT_NAME")
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	@ManyToOne
		@JoinColumn(name="MAT_ID")
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	@ManyToOne
		@JoinColumn(name="VENDOR_ID")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@Transient
	public long getPpap() {
		return ppap;
	}
	public void setPpap(long ppap) {
		this.ppap = ppap;
	}
		
}
