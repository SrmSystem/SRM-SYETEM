package com.qeweb.scm.basemodule.log;


public interface ILogger {

	/**
	 * 初始化日志
	 * @param log
	 * @return 日志路径
	 * @throws Exception
	 */
    public String init(ILog log) throws Exception;


    public void log(Object object);


    public void destory();
}
