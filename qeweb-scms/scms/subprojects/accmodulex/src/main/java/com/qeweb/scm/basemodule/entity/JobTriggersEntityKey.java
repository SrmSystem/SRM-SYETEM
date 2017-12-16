package com.qeweb.scm.basemodule.entity;

public class JobTriggersEntityKey implements java.io.Serializable {  
  
	private static final long serialVersionUID = 3166593369813629754L;
	
	private String schedName;
	private String triggerName;         //触发器名称
	private String triggerGroup;        //触发器组名
  
    public JobTriggersEntityKey() {  
    }  
  
    public JobTriggersEntityKey(String schedName, String triggerName, String triggerGroup) {  
        
    	this.schedName = schedName;  
        this.triggerName = triggerName;  
        this.triggerGroup = triggerGroup;  
    }  
  
    public String getSchedName() {  
    	
        return this.schedName;  
    }  
  
    public void setSchedName(String schedName) {  
    	
        this.schedName = schedName;  
    }  
  
    public String getTriggerName() { 
    	
        return this.triggerName;  
    }  
  
    public void setTriggerName(String triggerName) { 
    	
        this.triggerName = triggerName;  
    }  
    
public String getTriggerGroup() { 
    	
        return this.triggerGroup;  
    }  
  
    public void setTriggerGroup(String triggerGroup) { 
    	
        this.triggerGroup = triggerGroup;  
    } 
  
    public boolean equals(Object other) {  
    	
        if ((this == other))  
            return true;  
        if ((other == null))  
            return false;  
        if (!(other instanceof JobTriggersEntityKey))  
            return false;  
        
        JobTriggersEntityKey castOther = (JobTriggersEntityKey) other;  
  
        return ((this.getSchedName() == castOther.getSchedName()) || (this.getSchedName() != null && castOther.getSchedName() != null && this.getSchedName().equals(castOther.getSchedName())))  
            && ((this.getTriggerName() == castOther.getTriggerName()) || (this.getTriggerName() != null && castOther.getTriggerName() != null && this.getTriggerName().equals(  castOther.getTriggerName())))
            && ((this.getTriggerGroup() == castOther.getTriggerGroup()) || (this.getTriggerGroup() != null && castOther.getTriggerGroup() != null && this.getTriggerGroup().equals(  castOther.getTriggerGroup()))); 
    }  
  
    public int hashCode() {  
    	
        int result = 17;  
  
        result = 37 * result + (getSchedName() == null ? 0 : this.getSchedName().hashCode()); 
        result = 37 * result + (getTriggerName() == null ? 0 : this.getTriggerName().hashCode()); 
        result = 37 * result + (getTriggerGroup() == null ? 0 : this.getTriggerGroup().hashCode()); 
        
        return result;  
    }  
  
}  