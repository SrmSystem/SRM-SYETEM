package com.qeweb.scm.basemodule.entity;

public class JobDetailEntityKey implements java.io.Serializable {  
  
    private static final long serialVersionUID = 1L;  
    
    private String schedName;  
    private String jobName;   //任务组名
    private String jobGroup;  //任务组
  
    public JobDetailEntityKey() {  
    }  
  
    public JobDetailEntityKey(String schedName, String jobName, String jobGroup) {  
        
    	this.schedName = schedName;  
        this.jobName = jobName;  
        this.jobGroup = jobGroup;  
    }  
  
    public String getSchedName() {  
    	
        return this.schedName;  
    }  
  
    public void setSchedName(String schedName) {  
    	
        this.schedName = schedName;  
    }  
  
    public String getJobName() { 
    	
        return this.jobName;  
    }  
  
    public void setJobName(String jobName) { 
    	
        this.jobName = jobName;  
    }  
    
public String getJobGroup() { 
    	
        return this.jobGroup;  
    }  
  
    public void setJobGroup(String jobGroup) { 
    	
        this.jobGroup = jobGroup;  
    } 
  
    public boolean equals(Object other) {  
    	
        if ((this == other))  
            return true;  
        if ((other == null))  
            return false;  
        if (!(other instanceof JobDetailEntityKey))  
            return false;  
        
        JobDetailEntityKey castOther = (JobDetailEntityKey) other;  
  
        return ((this.getSchedName() == castOther.getSchedName()) || (this.getSchedName() != null && castOther.getSchedName() != null && this.getSchedName().equals(castOther.getSchedName())))  
            && ((this.getJobName() == castOther.getJobName()) || (this.getJobName() != null && castOther.getJobName() != null && this.getJobName().equals(  castOther.getJobName())))
            && ((this.getJobGroup() == castOther.getJobGroup()) || (this.getJobGroup() != null && castOther.getJobGroup() != null && this.getJobGroup().equals(  castOther.getJobGroup()))); 
    }  
  
    public int hashCode() {  
    	
        int result = 17;  
  
        result = 37 * result + (getSchedName() == null ? 0 : this.getSchedName().hashCode()); 
        result = 37 * result + (getJobName() == null ? 0 : this.getJobName().hashCode()); 
        result = 37 * result + (getJobGroup() == null ? 0 : this.getJobGroup().hashCode()); 
        
        return result;  
    }  
  
}  