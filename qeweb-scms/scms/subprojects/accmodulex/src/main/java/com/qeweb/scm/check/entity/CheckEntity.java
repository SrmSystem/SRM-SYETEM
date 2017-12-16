package com.qeweb.scm.check.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
/**
 * 
 * @author VILON
 *
 */
@Entity
@Table(name = "qeweb_check")
public class CheckEntity extends BaseEntity {
	/**
	 * 编码
	 */
	private String code;		
	/**
	 * 供应商
	 */
	private OrganizationEntity vendor;
	/**
	 * 采购组织
	 */
	private OrganizationEntity buyer;
	/**
	 * 年
	 */
	private Integer year;
	/**
	 * 月
	 */
	private Integer month;
	/**
	 * 类型，1：国内，2：国外，3,：外协
	 */
	private Integer type;
	/**
	 * 供应商确认状态
	 */
	private Integer vConfirmStatus;
	/**
	 * 采购组织确认状态
	 */
	private Integer bConfirmStatus;
	/**
	 * 开票状态
	 */
	private Integer billStatus;
	/**
	 * 发票接收状态
	 */
	private Integer invoiceReceiveStatus;
	/**
	 * 异常状态
	 */
	private Integer exStatus;
	/**
	 * 异常处理状态
	 */
	private Integer exDealStatus;
	/**
	 * 异常确认状态
	 */
	private Integer exConfirmStatus;
	/**
	 * 付款状态
	 */
	private Integer payStatus;
	/**
	 * 关闭状态
	 */
	private Integer closeStatus;
	/**
	 * 发布状态
	 */
	private Integer publishStatus;
	/**
	 * 发布时间
	 */
	private Timestamp publishTime;
	/**
	 * 发布人
	 */
	private UserEntity publishUser;
	
	//add by zhangjiejun 2015.11.04 start
	/**
	 * 审核状态
	 */
	private Integer reviewStatus;
	
	/**
	 * 审核时间
	 */
	private Timestamp reviewTime;
	
	/**
	 * 审核人
	 */
	private UserEntity reviewUser;
	//add by zhangjiejun 2015.11.04 end
	
	/**
	 * 供应商确认时间
	 */
	private Timestamp vConfirmTime;
	/**
	 * 供应商确认人
	 */
	private UserEntity vConfirmUser;
	/**
	 * 采购组织确认时间
	 */
	private Timestamp bConfirmTime;
	/**
	 * 采购组织确认人
	 */
	private UserEntity bConfirmUser;
	/**
	 * 核对金额
	 */
	private Double checkAmount;
	/**
	 * 供应商核对金额
	 */
	private Double vCheckAmount;
	/**
	 * 采购组织核对金额
	 */
	private Double bCheckAmount;
	/**
	 * 开票金额
	 */
	private Double billAmount;
	/**
	 * 索赔金额
	 */
	private Double claimAmount;
	/**
	 * 索赔描述
	 */
	private String claimDescription;
	/**
	 * 开票描述
	 */
	private String billDescription;
	/**
	 * 索赔文件路径
	 */
	private String claimFilePath;
	/**
	 * 索赔文件名
	 */
	private String cliamFileName;
	
	
	/**
	 * 预计付款日期
	 */
	private Timestamp expPayTime;
	
	/**
	 * 付款金额
	 */
	private Double payAmount;
	
	/**
	 * 开票驳回原因
	 */
	private String billRejectReason;
	
	//add by zhangjiejun 2015.09.14 start
	/**
	 * 对账详细集合
	 */
	private Set<CheckItemEntity> checkItem;
	
	private String col1;						//差异价格
	private String col2;						//索赔金额开票状态(1为已开索赔金额发票)
	private String col3;						//对账单周期时间（每个账期内只生成一张对账单）
	private String col4;						//对账返回日志		
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	
	@OneToMany(mappedBy="check")
	@JsonIgnore
	public Set<CheckItemEntity> getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(Set<CheckItemEntity> checkItem) {
		this.checkItem = checkItem;
	}
	//add by zhangjiejun 2015.09.14 end
	
	//add by zhangjiejun 2015.09.16 start
	/**
	 * Batch
	 */
	private String batch;
	
	/**
	 * 队列号(系统生成的队列号，和QAD传过来的voucher不是同一个东西，要区分开来)
	 */
	private int voucher;
	
	/**
	 * 凭证号(QAD传过来的voucher)
	 */
	private int qadVoucher;
	
	/**
	 * 供应商付款截止时间
	 */
	private Timestamp dueDate;
	
	/**
	 * 反馈状态
	 */
	private String backStatus;
	
	//冗余字段
	private Integer feedbackCount;//反馈数量
	
	public String getBatch() {
		return batch;
	}
	
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public int getVoucher() {
		return voucher;
	}
	
	public void setVoucher(int voucher) {
		this.voucher = voucher;
	}
	
	@Column(name="qad_voucher")
	public int getQadVoucher() {
		return qadVoucher;
	}
	public void setQadVoucher(int qadVoucher) {
		this.qadVoucher = qadVoucher;
	}
	
//	
//	public int getQADVoucher() {
//		return qadVoucher;
//	}
//	
//	public void setQADVoucher(int qADVoucher) {
//		QADVoucher = qADVoucher;
//	}
	
	@Column(name="due_date")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name="back_status")
	public String getBackStatus() {
		return backStatus;
	}
	
	public void setBackStatus(String backStatus) {
		this.backStatus = backStatus;
	}

	//add by zhangjiejun 2015.09.16 end
	@Column(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	@ManyToOne
	@JoinColumn(name="buyer_id")
	public OrganizationEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}
	@Column(name="year")
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	@Column(name="month")
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name="v_confirm_status")
	public Integer getvConfirmStatus() {
		return vConfirmStatus;
	}
	public void setvConfirmStatus(Integer vConfirmStatus) {
		this.vConfirmStatus = vConfirmStatus;
	}
	@Column(name="b_confirm_status")
	public Integer getbConfirmStatus() {
		return bConfirmStatus;
	}
	public void setbConfirmStatus(Integer bConfirmStatus) {
		this.bConfirmStatus = bConfirmStatus;
	}
	@Column(name="invoice_receive_status")
	public Integer getInvoiceReceiveStatus() {
		return invoiceReceiveStatus;
	}
	public void setInvoiceReceiveStatus(Integer invoiceReceiveStatus) {
		this.invoiceReceiveStatus = invoiceReceiveStatus;
	}
	@Column(name="bill_status")
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	@Column(name="ex_status")
	public Integer getExStatus() {
		return exStatus;
	}
	public void setExStatus(Integer exStatus) {
		this.exStatus = exStatus;
	}
	@Column(name="ex_deal_status")
	public Integer getExDealStatus() {
		return exDealStatus;
	}
	public void setExDealStatus(Integer exDealStatus) {
		this.exDealStatus = exDealStatus;
	}
	@Column(name="ex_confirm_status")
	public Integer getExConfirmStatus() {
		return exConfirmStatus;
	}
	public void setExConfirmStatus(Integer exConfirmStatus) {
		this.exConfirmStatus = exConfirmStatus;
	}
	@Column(name="pay_status")
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	@Column(name="close_status")
	public Integer getCloseStatus() {
		return closeStatus;
	}
	public void setCloseStatus(Integer closeStatus) {
		this.closeStatus = closeStatus;
	}
	@Column(name="publish_status")
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	@Column(name="publish_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}
	@ManyToOne
	@JoinColumn(name="publish_user_id")
	public UserEntity getPublishUser() {
		return publishUser;
	}
	public void setPublishUser(UserEntity publishUser) {
		this.publishUser = publishUser;
	}
	
	//add by zhangjiejun 2015.11.04 start
	@Column(name="review_status")
	public Integer getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
	@Column(name="review_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}
	
	@ManyToOne
	@JoinColumn(name="review_user_id")
	public UserEntity getReviewUser() {
		return reviewUser;
	}
	public void setReviewUser(UserEntity reviewUser) {
		this.reviewUser = reviewUser;
	}
	//add by zhangjiejun 2015.11.04 end
	
	@Column(name="v_confirm_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getvConfirmTime() {
		return vConfirmTime;
	}
	public void setvConfirmTime(Timestamp vConfirmTime) {
		this.vConfirmTime = vConfirmTime;
	}
	@ManyToOne
	@JoinColumn(name="v_confirm_user_id")
	public UserEntity getvConfirmUser() {
		return vConfirmUser;
	}
	public void setvConfirmUser(UserEntity vConfirmUser) {
		this.vConfirmUser = vConfirmUser;
	}
	@Column(name="b_confirm_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getbConfirmTime() {
		return bConfirmTime;
	}
	public void setbConfirmTime(Timestamp bConfirmTime) {
		this.bConfirmTime = bConfirmTime;
	}
	@ManyToOne
	@JoinColumn(name="b_confirm_user_id")
	public UserEntity getbConfirmUser() {
		return bConfirmUser;
	}
	public void setbConfirmUser(UserEntity bConfirmUser) {
		this.bConfirmUser = bConfirmUser;
	}
	@Column(name="check_amount")
	public Double getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(Double checkAmount) {
		this.checkAmount = checkAmount;
	}
	@Column(name="v_check_amount")
	public Double getvCheckAmount() {
		return vCheckAmount;
	}
	public void setvCheckAmount(Double vCheckAmount) {
		this.vCheckAmount = vCheckAmount;
	}
	@Column(name="b_check_amount")
	public Double getbCheckAmount() {
		return bCheckAmount;
	}
	public void setbCheckAmount(Double bCheckAmount) {
		this.bCheckAmount = bCheckAmount;
	}
	@Column(name="bill_amount")
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	@Column(name="claim_amount")
	public Double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}
	@Column(name="claim_description")
	public String getClaimDescription() {
		return claimDescription;
	}
	public void setClaimDescription(String claimDescription) {
		this.claimDescription = claimDescription;
	}
	@Column(name="bill_description")
	public String getBillDescription() {
		return billDescription;
	}
	public void setBillDescription(String billDescription) {
		this.billDescription = billDescription;
	}
	@Column(name="claim_file_path")
	public String getClaimFilePath() {
		return claimFilePath;
	}
	public void setClaimFilePath(String claimFilePath) {
		this.claimFilePath = claimFilePath;
	}
	@Column(name="claim_file_name")
	public String getCliamFileName() {
		return cliamFileName;
	}
	public void setCliamFileName(String cliamFileName) {
		this.cliamFileName = cliamFileName;
	}
	
	
	@Column(name="exp_pay_time")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getExpPayTime() {
		return expPayTime;
	}
	public void setExpPayTime(Timestamp expPayTime) {
		this.expPayTime = expPayTime;
	}
	
	@Column(name="pay_amount")
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	
	@Column(name="bill_reject_reason")
	public String getBillRejectReason() {
		return billRejectReason;
	}
	public void setBillRejectReason(String billRejectReason) {
		this.billRejectReason = billRejectReason;
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
	
	@Transient
	public Integer getFeedbackCount() {
		return feedbackCount;
	}

	public void setFeedbackCount(Integer feedbackCount) {
		this.feedbackCount = feedbackCount;
	}
	
}