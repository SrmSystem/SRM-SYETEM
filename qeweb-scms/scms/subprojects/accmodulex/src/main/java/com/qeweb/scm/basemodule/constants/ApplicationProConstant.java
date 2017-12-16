package com.qeweb.scm.basemodule.constants;


/**
 * 系统的属性常量类，其他项目可继承该常量类，添加自定义系统属性
 * @author pjjxiajun
 * @date 2015年7月17日
 * @path com.qeweb.scm.basemodule.constans.ApplicationProConstant.java
 */
public interface ApplicationProConstant {
	/** 项目名称*/
	public final static String PROJECT_NAME = "project.name";
	/** 项目LOGO*/
	public final static String PROJECT_LOGO = "project.logo";
	/** 文件基本目录 */
	public final static String FILE_DIR = "file.dir";
	/** 日志文件目录 */
	public final static String FILE_LOG_DIR = "file.log.dir";
	/** 是否启用自动任务 是-true,否-false或空 */
	public final static String TASK_ENABLE = "task.enable";
	/** ftp用户名称 */
	public final static String FTPUSERNAME = "ftpusername";
	/** ftp用户密码 */
	public final static String FTPPASSWORD = "ftppassword";
	/** ftp地址 */
	public final static String FTPHOSTNAME = "ftphostname";
	/** ftp端口 */
	public final static String FTPPORT = "ftpport";
	/** ftp上传路径 */
	public final static String FTPUPLOADPATH = "ftpuploadpath";
	/** ftp下载路径 */
	public final static String FTPDOWNLOADPATH = "ftpdownloadpath";
	/** 物料分类叶子节点层级，如果有值（数值类型），本层级为叶子节点，层级从1开始 */
	public final static String MATERIALTYPE_LEAFLEVEL = "materialtype.leaflevel";
	/** 非叶子节点是否可以增加物料,可以-true,不可以-false或为空 */
	public final static String MATERIALTYPE_NOLEAFLEVEL_ALLOW = "materialtype.noleaflevel.allow";

	

}
