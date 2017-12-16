package com.qeweb.scm.basemodule.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Column; 
import javax.persistence.Transient;

/**
 * 定时任务
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qrtz_cron_triggers")
public class JobCronEntity implements java.io.Serializable{
	
	private static final long serialVersionUID = 2223084423360977947L;
	
	private JobTriggersEntityKey key;   //复合主键
    private String cronExpression;      //Cron表达式
 
	
	//非持久化字段
	private String schedName;
	private String triggerName;         //触发器名称
	private String triggerGroup;        //触发器组名
    	
	public JobCronEntity(){
		
	}
	
    public JobCronEntity(JobTriggersEntityKey key){
    	this.key = key;
	}
	
	public JobCronEntity(JobTriggersEntityKey key, String cronExpression) {  
		this.key = key;  
        this.cronExpression = cronExpression;
    }  
	
	@EmbeddedId
	public JobTriggersEntityKey getKey() {
		return key;
	}

	public void setKey(JobTriggersEntityKey key) {
		this.key = key;
	}

	@Column(name="cron_expression")
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
}
