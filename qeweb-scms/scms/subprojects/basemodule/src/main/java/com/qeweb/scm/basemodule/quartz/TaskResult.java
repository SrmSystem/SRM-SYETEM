package com.qeweb.scm.basemodule.quartz;

public class TaskResult {

	public TaskResult() {
		
	}
    /**
     * @param isCompleted
     */
    public TaskResult(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    /**
     * 任务是否执行成功
     */
    public boolean isCompleted = false;
    /**
     * 任务是否抛出exception
     */
    public boolean isThrowable = false;
    /**
     * 相关执行信息
     */
    public Object results = "";
}
