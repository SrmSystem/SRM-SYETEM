package com.qeweb.scm.baseline.common.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.qeweb.scm.baseline.common.entity.BaseFileEntity;
import com.qeweb.scm.baseline.common.service.BaseFileService;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Controller
@RequestMapping("/manager/common/baseFile")
public class BaseFileController{

	private ILogger logger = new FileLogger();
	@Autowired
	private BaseFileService baseFileService;
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/common/baseFileList";
	}


	
	@RequestMapping(value = "addEntity",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addEntity(@RequestParam("planfiles") MultipartFile files) throws IOException{
		map = new HashMap<String, Object>();
		File savefile = null;
		if(files!=null&&!StringUtils.isEmpty(files.getOriginalFilename())){
			//1、保存文件至服务器
			try {
				savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
				if(savefile == null || savefile.length() == 0) {
					throw new Exception();
				}
			} catch (Exception e) {
				map.put("success", false);
			}
		}
		baseFileService.saveFile(savefile,savefile==null?"":files.getOriginalFilename().substring(0,files.getOriginalFilename().lastIndexOf(".")));
		map.put("success", true);
		map.put("msg", "操作成功");
		return map;
	}
	

	

	
	@RequestMapping("downloadFile")
	public ModelAndView downloadFile(@RequestParam("billId") Long billId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BaseFileEntity f=baseFileService.findFile(10L, "10");
		String fileName=f.getFileName();
		String filep=f.getFilePath();
		Blob content=f.getFileContent();
		System.out.println(request.getSession().getServletContext().getRealPath("/"));
		
		File fileroot = new File(request.getSession().getServletContext().getRealPath("/") + "download");
		if(!fileroot.isDirectory()){
			fileroot.mkdirs();
		}
		 File file = new File(request.getSession().getServletContext().getRealPath("/") + "download/" + fileName);
		 if (!file.exists()) {
			 file.createNewFile(); // 如果文件不存在，则创建
		 }
		   BufferedOutputStream bos1 = null;  
	       FileOutputStream fos = null;  
	       fos = new FileOutputStream(file);  
	       bos1 = new BufferedOutputStream(fos);  
	       bos1.write(blobToBytes(content));  
		
		//下载文件
		response.setContentType("application/octet-stream;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");    
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
	
		try {
			long fileLength = new File(file.getAbsolutePath()).length();
			response.setContentType("application/x-msdownload;");   
			response.setHeader("Content-disposition", "attachment;filename="  + (new String(fileName.getBytes("GBK"), "ISO8859-1") + "." + getExtensionName(filep))) ;  
			response.setHeader("Content-Length", String.valueOf(fileLength));  
			bis = new BufferedInputStream(new FileInputStream(file));
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
			if (bos1 != null) {
				bos1.close();
			}
		}
		return null;
	}
	
	public  byte[] blobToBytes(Blob blob) {  
        BufferedInputStream is = null;  
        try {  
            is = new BufferedInputStream(blob.getBinaryStream());  
            byte[] bytes = new byte[(int) blob.length()];  
            int len = bytes.length;  
            int offset = 0;  
            int read = 0;  
            while (offset < len  
                    && (read = is.read(bytes, offset, len - offset)) >= 0) {  
                offset += read;  
            }  
            return bytes;  
        } catch (Exception e) {  
            return null;  
        } finally {  
            try {  
                is.close();  
                is = null;  
            } catch (IOException e) {  
                return null;  
            }  
        }  
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
