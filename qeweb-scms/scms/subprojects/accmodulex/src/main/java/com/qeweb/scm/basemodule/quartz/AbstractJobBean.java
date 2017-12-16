package com.qeweb.scm.basemodule.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.ClassUtil;


public abstract class AbstractJobBean extends QuartzJobBean implements ILog {

    private static final Log _logger = LogFactory.getLog(AbstractJobBean.class);   

    /**
    *
    */
    private boolean running = false;
   
    /**
     * logger
     */
    private ILogger taskLogger = new FileLogger();


    /**
     * You can do anything before task exec.
     */
    protected abstract void prepare();

    /**
     * Execute the task.
     *
     * @return TaskResult
     * @throws java.lang.Exception
     */
    protected abstract TaskResult exec() throws Exception;
    
    /**
     * You can do anything after task execed.
     */
    protected abstract void destory();

    /**
     * 
     * @return
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     *
     * @return
     */
    public String getName() {
        return ClassUtil.getSimpleClassName(this.getClass());
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException{
    	running = true;
    	TaskResult result = new TaskResult();
    	String _log;
        try {
            getTaskLogger().init(this);
            prepare();
            result = exec();
        } catch (Exception ex) {
            _logger.error(ex.getMessage(), ex);
            _log = "Execute faild, " + ex.getMessage();
            log(_log);
            result.isThrowable = true;
            result.results = _log;
        } finally {
            try {
                destory();
            } catch (Exception ex) {
                _logger.error(ex.getMessage(), ex);
                _log = "Destory faild, " + ex.getMessage();
                log(_log);
                result.isThrowable = true;
                result.results = _log;
            } finally {
                getTaskLogger().destory();
            }
            running = false;
        }
    }

    /**
     *
     * @param message
     */
    public void log(Object message) {
        getTaskLogger().log(message);
    }

    /**
     * @return the taskLogger
     */
    public ILogger getTaskLogger() {
        return taskLogger;
    }

    /**
     * @param taskLogger the taskLogger to set
     */
    public void setTaskLogger(ILogger taskLogger) {
        this.taskLogger = taskLogger;
    }
}
