package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 定时任务
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qrtz_triggers")
public class JobTriggersEntity implements java.io.Serializable{
	
	private static final long serialVersionUID = -153626398654207114L;
	
	private JobTriggersEntityKey key;   //复合主键
    private String jobName;             //任务名称
    private String jobGroup;            //任务组
    private String description;         //描述
    private Long nextFireTime;       	//下次触发时间
    private Long prevFireTime;       	//上次触发时间 
    private Integer priority;           //优先级
    private String triggerState;        //触发器状态
    private String triggerType;         //触发器类型
    private Long startTime;          	//开始时间
    private Long endTime;            	//结束时间
    private String calendarName;        //日期名称
    private Integer misfireInstr;       //失效Instr
    private JobCronEntity jobCron;		//表达式
	
	//非持久化字段
	private String schedName;
	private String triggerName;         //触发器名称
	private String triggerGroup;        //触发器组名
	private String cronExpression;		//表达式
    	
	public JobTriggersEntity(){
		
	}
	
    public JobTriggersEntity(JobTriggersEntityKey key){
    	this.key = key;
	}
	
	public JobTriggersEntity(JobTriggersEntityKey key, String jobName, String jobGroup, String description, Long nextFireTime, Long prevFireTime, Integer priority, String triggerState, String triggerType, Long startTime, Long endTime, String calendarName, Integer misfireInstr, Integer jobData) {  
		this.key = key;  
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.nextFireTime = nextFireTime;
        this.prevFireTime = prevFireTime;
        this.priority = priority;
        this.triggerState = triggerState;
        this.triggerType = triggerType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarName = calendarName;
        this.misfireInstr = misfireInstr;
    }
	
	@EmbeddedId
	public JobTriggersEntityKey getKey() {
		return key;
	}

	public void setKey(JobTriggersEntityKey key) {
		this.key = key;
	}

	@Column(name="JOB_NAME")
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Column(name="JOB_GROUP")
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="NEXT_FIRE_TIME")
	public Long getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	
	@Column(name="PREV_FIRE_TIME")
	public Long getPrevFireTime() {
		return prevFireTime;
	}
	public void setPrevFireTime(Long prevFireTime) {
		this.prevFireTime = prevFireTime;
	}
	
	@Column(name="PRIORITY")
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Column(name="TRIGGER_STATE")
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}
	
	@Column(name="TRIGGER_TYPE")
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	
	@Column(name="START_TIME")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	@Column(name="END_TIME")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="CALENDAR_NAME")
	public String getCalendarName() {
		return calendarName;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	
	@Column(name="MISFIRE_INSTR")
	public Integer getMisfireInstr() {
		return misfireInstr;
	}
	public void setMisfireInstr(Integer misfireInstr) {
		this.misfireInstr = misfireInstr;
	}
	
	@OneToOne
	@PrimaryKeyJoinColumn(name="key")  
	public JobCronEntity getJobCron() {  
		return jobCron;
	}

	public void setJobCron(JobCronEntity jobCron) {
		this.jobCron = jobCron;
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

	@Transient
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
}
