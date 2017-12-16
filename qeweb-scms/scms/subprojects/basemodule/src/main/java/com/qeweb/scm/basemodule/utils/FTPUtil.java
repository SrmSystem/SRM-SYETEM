package com.qeweb.scm.basemodule.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.qeweb.modules.utils.PropertiesUtil;

/**
 * 采用的是apache commons-net架包中的ftp工具类实现的
 * 
 */
public class FTPUtil {
	private String username;
	private String password;
	private String ftpHostName;
	private int port = 21;
	private String uploadPath;
	private String downloadPath;
	private FTPClient ftpClient = new FTPClient();
	private FileOutputStream fos = null;
	private final Log logger = LogFactory.getLog(FTPUtil.class);
	
	public FTPUtil() {
		super();
		this.username = PropertiesUtil.getProperty("ftpusername");
		this.password = PropertiesUtil.getProperty("ftppassword");
		this.ftpHostName = PropertiesUtil.getProperty("ftphostname");
		this.port = StringUtils.convertToInt(PropertiesUtil.getProperty("ftpport", "21"));
		this.uploadPath = PropertiesUtil.getProperty("ftpuploadpath");
		this.downloadPath = PropertiesUtil.getProperty("ftpdownloadpath");
//		this.username = "sharp";
//		this.password = "2wsx#EDC";
//		this.ftpHostName = "192.168.0.238";
//		this.port = 21;
//		this.uploadPath = "business";
//		this.downloadPath = "qeweb";
	}

	public FTPUtil(String username, String password, String ftpHostName, int port) {
		super();
		this.username = username;
		this.password = password;
		this.ftpHostName = ftpHostName;
		this.port = port;
	}

	/**
	 * 建立连接b
	 */
	private void connect() {
		try {
			logger.info("开始连接");
			// 连接
			ftpClient.connect(ftpHostName, port);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}
			// 登录
			ftpClient.login(username, password);
			ftpClient.setBufferSize(256);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("utf8");
			ftpClient.enterLocalPassiveMode();
			logger.info("登录成功！");
			logger.info("开始登录！");
		} catch (SocketException e) {
			logger.error("登录失败1", e);
		} catch (IOException e) {
			logger.error("登录失败2", e);
		}
	}

	/**
	 * 关闭输入输出流
	 * 
	 * @param fos
	 */
	private void close(FileOutputStream fos) {
		try {
			if (fos != null) {
				fos.close();
			}
			ftpClient.logout();
			logger.info("退出登录");
			ftpClient.disconnect();
			logger.info("关闭连接");
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 关闭输入输出流
	 * @param inputStream
	 */
	private void close(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close(); 
			}
			ftpClient.logout();
			logger.info("退出登录");
			ftpClient.disconnect();
			logger.info("关闭连接");
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	public boolean upload(String filename, InputStream input) {
		connect();
		boolean flag = upload(filename, uploadPath, input);
		close(input);
		return flag;
	}
	
	public boolean upload(String filename, String path, InputStream input) {
		boolean success = true;
		try{
			ftpClient.changeWorkingDirectory(path);
			ftpClient.storeFile(filename, input);
			logger.info("上传成功！");
		}catch(Exception e) {
			success = false;
			logger.error("上传失败！", e);
		}
		return success;
	}
	
	//add by zhangjiejun 2015.09.14 start
	/**
	 * 创建路劲，并将文件上传
	 * @param 	filename	文件名
	 * @param 	path		上传路劲
	 * @param 	input		输入流
	 * @param 	reName 		重命名文件名
	 * @return	上传成功状态
	 */
	public boolean upload2(String filename, String path, InputStream input, String reName) {
		boolean success = true;
		try{
			connect();
			ftpClient.makeDirectory(path);							//不存在路劲，就创建
			ftpClient.changeWorkingDirectory(path);					//定位路劲
			ftpClient.dele(reName);									//已存在的直接删除
			boolean flag = ftpClient.storeFile(filename, input);	//上传文件
			logger.info("flag == " + flag);
			if(flag){
				logger.info("文件传输成功，修改文件名: " + reName);
				ftpClient.rename(filename, reName);					//修改文件名
			}
			close(input);
			logger.info("上传成功！");
		}catch(Exception e) {
			success = false;
			logger.error("上传失败！", e);
		}
		return success;
	}
	//add by zhangjiejun 2015.09.14 end
	
	/**
	 * 下载文件
	 * 
	 * @param ftpFileName
	 * @param localDir
	 */
	public boolean down(String ftpFileName, String localDir) {
		connect();
		boolean flag = downFileOrDir(downloadPath, ftpFileName, localDir);
		close(fos);
		return flag;
	}
	
	public boolean down(String remotePath, String ftpFileName, String localDir) {
		connect();
		boolean flag = downFileOrDir(remotePath, ftpFileName, localDir);
		close(fos);
		return flag;
	}

	private boolean downFileOrDir(String remotePath, String ftpFileName, String localDir) {
		boolean success = true;
		try {
			ftpClient.changeWorkingDirectory(remotePath);			// 转移到FTP服务器目录  
            FTPFile[] fs = ftpClient.listFiles();  
            FTPFile ff;  
            for (int i = 0; i < fs.length; i++) {  
                ff = fs[i];  
//                if (StringUtils.isEmpty(ftpFileName) || (null != ff && null != ff.getName() && ff.getName().equals(ftpFileName))) {  
                String fileName = ff.getName();						//FTP服务器上的文件名
                if (StringUtils.isEmpty(ftpFileName) || (null != ff && null != fileName && fileName.startsWith(ftpFileName))) {
                	if(StringUtils.isEmpty(ftpFileName)){
                		 logger.info("传入的文件名为null，下载全部文件: " + fileName);
                	}else{
                		logger.info("开始下载以" + ftpFileName + "开头的文件: " + fileName);
                	}
                	
                	long fileSize_first = ff.getSize();				//初始文件size
                	boolean continueFlag = matchFileIsFinished(fileSize_first, fileName);//匹配文件是否上传成功
                	logger.info("continueFlag == " + continueFlag);
                	if(continueFlag){								//continue状态为true，直接跳过
                		logger.info("continueFlag is " + continueFlag + ", so continue " + fileName);
                		continue;
                	}else{
                		logger.info("continueFlag is " + continueFlag + ", so download the file : " + fileName);
                	}
                	
                    File localFile = new File(localDir + "/" + fileName);  
                    OutputStream is = new FileOutputStream(localFile);  
                    ftpClient.retrieveFile(fileName, is);  
                    is.close();
                    try {  
//                    	ftpClient.sendCommand("dele " + fileName);  
                    	logger.info("下载成功, fileName: " + fileName);    
//                    	ftpClient.deleteFile(fileName);
                    	
                    	String back = "back/";
                    	String remotePath_back = back + DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD) + "/";
                    	logger.info("remotePath_back == " + remotePath_back); 
                    	
                    	boolean createFlag_back = ftpClient.makeDirectory(back);							//在root目录下创建文件夹
                    	boolean createFlag_time = ftpClient.makeDirectory(remotePath_back);					//在root目录下创建文件夹
                    	logger.info("创建back路劲状态 createFlag_back : " + createFlag_back);
                    	logger.info("创建back_time路劲状态 createFlag_time : " + createFlag_time);
//                    	boolean moveFlag = ftpClient.rename(fileName, "./" + remotePath_back + fileName);	//移动文件到back路劲
//                    	logger.info("移动文件到back路劲, fileName: " + fileName);    
//                    	logger.info("移动文件到back状态, moveFlag: " + moveFlag); 
                    	
                    	ByteArrayOutputStream is2 = new ByteArrayOutputStream();
                        ftpClient.retrieveFile(fileName, is2);
                    	ByteArrayInputStream in = new ByteArrayInputStream(is2.toByteArray());
                    	boolean uploadFlag = ftpClient.storeFile(remotePath_back + fileName, in);
                    	logger.info("从内存中上传到指定路劲状态 uploadFlag : " + uploadFlag);
                    	is2.close();
                    	in.close();
                    	
                    	boolean deleteFlag = ftpClient.deleteFile(fileName);
                    	logger.info("删除文件状态 deleteFlag : " + deleteFlag);
                    } catch (Exception e) {  
                        logger.info("移动文件到back路劲失败");
                    }
                    logger.info("---------------------------------------------------");
                }
            }
//			logger.info("下载成功！");
		} catch (Exception e) {
			success = false;
			logger.error("连接失败！", e);
		}
		return success;
	}

	/**
	 * 匹配文件是否上传成功
	 * @param 	fileSize_first			文件初始大小
	 * @param 	fileName				文件名
	 * @return							文件完成状态
	 * @throws 	Exception				异常
	 */
	private boolean matchFileIsFinished(long fileSize_first, String fileName) throws Exception {
		boolean continueFlag = false;
		int times = 2;									//定义执行次数
    	logger.info("begin to mtach file--------------------------------------------------------");
    	while (true) {									//定义while循环
    		if(times == 0){								//执行次数等于0，退出循环
    			break;
    		}
    		FTPFile[] fs2 = ftpClient.listFiles();
    		FTPFile ff2 = null;
    		for (FTPFile ftpFile : fs2) {
				if(ftpFile.getName().equals(fileName)){
					ff2 = ftpFile;
					logger.info("get file again...");
				}
			}
    		if(ff2 != null){
    			long fileSize = ff2.getSize();			//每次获取的文件size
    			logger.info(fileName + " : fileSize == " + fileSize + ", fileSize_first == " + fileSize_first);
    			if(fileSize_first != fileSize){
    				logger.info("size is not same...");
    				continueFlag = true;
    			}else{
    				logger.info("size is same...");
    				continueFlag = false;
    			}
    			fileSize_first = fileSize;
    			Thread.sleep(1000);						//每次执行沉睡1秒
    			logger.info("Thread sleep 1 seconds...\n\n");
    			times--;								//执行次数递减
    		}else{
    			logger.info("file dosen't exists, break!");
    			continueFlag = true;
    			break;
    		}
		}
    	logger.info("end to mtach file--------------------------------------------------------");
		return continueFlag;
	}

	// 判断是否是目录
	public boolean isDir(String fileName) {
		try {
			// 切换目录，若当前是目录则返回true,否则返回true。
			boolean falg = ftpClient.changeWorkingDirectory(fileName);
			return falg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpHostName() {
		return ftpHostName;
	}

	public void setFtpHostName(String ftpHostName) {
		this.ftpHostName = ftpHostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) {
//		FTPUtil ftpUtil = new FTPUtil();
//		boolean flag = ftpUtil.down("qeweb/qeweb","", "d:/qeweb"); 
//		logger.info(flag);  
	}

}