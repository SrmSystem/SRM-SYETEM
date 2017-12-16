package com.qeweb.scm.basemodule.web.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.utils.StringUtils;

/**
 * 文件下载
 * @author ALEX
 *
 */
@Controller
@RequestMapping(value="/common/download")
public class FileDownloadController {   

	/**
	 * 页面显示log
	 * @param filepath
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="log", method=RequestMethod.GET)  
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("common/log");      
	}  
	/**
	 * 页面显示log
	 * @param filepath
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="log", method=RequestMethod.POST)  
	public ModelAndView download(@RequestParam("filepath") String filepath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("filepath", StringUtils.decode(filepath));      
		return new ModelAndView("common/log");      
	}  
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView downloadFile(@RequestParam("filePath") String filePath, @RequestParam(value="fileName",required=false) String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 return commonDown(filePath,fileName,request,response);
	}
	
	@RequestMapping(value="{filePath}/{filexls}", method=RequestMethod.GET)
	public ModelAndView downloadFileGET(@PathVariable("filePath") String filePath,@PathVariable("filexls") String filexls, HttpServletRequest request, HttpServletResponse response) throws Exception {
		filePath=StringUtils.decode(filePath);
		
		return commonDown(filePath+"."+filexls,"",request,response);
	}
	
	private ModelAndView commonDown(String filePath, String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream;charset=UTF-8");
		request.setCharacterEncoding(Constant.ENCODING_UTF8);    
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		if(filePath == null) {
			return null;
		}
		filePath = filePath.replaceAll("\\\\", "/");
		if(filePath.startsWith("WEB-INF")) {
			filePath =  request.getSession().getServletContext().getRealPath("/") + "/" + filePath;  
		}  
		if(StringUtils.isEmpty(fileName)) {
			fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));
		}  
		File file = new File(filePath);
		try {
			long fileLength = new File(file.getAbsolutePath()).length();
			response.setContentType("application/x-msdownload;");   
			response.setHeader("Content-disposition", "attachment;filename="  + new String(fileName.getBytes("GBK"), "ISO8859-1") + "." + getExtensionName(filePath)) ;  
			response.setHeader("Content-Length", String.valueOf(fileLength));  
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];  
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}
	
	public static String getExtensionName(String filepath) {
		if (filepath != null && filepath.length() > 0 && filepath.lastIndexOf(".") > 0) {
			int dot = filepath.lastIndexOf('.');
			if (dot > -1 && dot < filepath.length() - 1) { 
				return filepath.substring(dot + 1);
			}
		}
		return filepath;
	}
}
