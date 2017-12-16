package com.qeweb.scm.basemodule.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column; 

/**
 * 定时任务
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qrtz_job_details")
public class JobDetailEntity implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L; 
	private JobDetailEntityKey key;   //复合主键 
	private String description; 	// 描述
	private String jobClassName; 	// 任务类
	private Boolean isDurable; 		// 是否持久化
	private Boolean isNonconcurrent; // 是否非共点的
	private Boolean isUpdateData; 	// 是否修改数据
	private Boolean requestsRecovery; // 是否需要恢复
	
	//非持久化字段
	private String schedName;
	private String jobName;  
	private String jobGroup;
	
	private String triggerName;
	private String triggerGroup;
	private String cronExpression;
	

	public JobDetailEntity(){
		
	}
	
    public JobDetailEntity(JobDetailEntityKey key){
    	this.key = key;
	}
	
	public JobDetailEntity(JobDetailEntityKey key, String description, String jobClassName, Boolean isDurable, Boolean isNonconcurrent, Boolean isUpdateData, Boolean requestsRecovery) {  
		this.key = key;  
        this.description = description;  
        this.jobClassName = jobClassName;  
        this.isDurable = isDurable;
        this.isNonconcurrent = isNonconcurrent;
        this.isUpdateData = isUpdateData;
        this.requestsRecovery = requestsRecovery;
    }

	@EmbeddedId
	public JobDetailEntityKey getKey() {
		return key;
	}

	public void setKey(JobDetailEntityKey key) {
		this.key = key;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "JOB_CLASS_NAME")
	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	@Column(name = "IS_DURABLE")
	public Boolean getIsDurable() {
		return isDurable;
	}

	public void setIsDurable(Boolean isDurable) {
		this.isDurable = isDurable;
	}

	@Column(name = "IS_NONCONCURRENT")
	public Boolean getIsNonconcurrent() {
		return isNonconcurrent;
	}

	public void setIsNonconcurrent(Boolean isNonconcurrent) {
		this.isNonconcurrent = isNonconcurrent;
	}

	@Column(name = "IS_UPDATE_DATA")
	public Boolean getIsUpdateData() {
		return isUpdateData;
	}

	public void setIsUpdateData(Boolean isUpdateData) {
		this.isUpdateData = isUpdateData;
	}

	@Column(name = "REQUESTS_RECOVERY")
	public Boolean getRequestsRecovery() {
		return requestsRecovery;
	}

	public void setRequestsRecovery(Boolean requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}  

	@Transient
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	@Transient
	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	@Transient
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Transient
	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Transient
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Transient
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
}
