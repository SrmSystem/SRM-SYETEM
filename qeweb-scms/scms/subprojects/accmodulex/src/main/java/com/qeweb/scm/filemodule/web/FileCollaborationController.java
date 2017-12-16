package com.qeweb.scm.filemodule.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.web.Servlets;









import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.filemodule.entity.FileCollaborationEntity;
import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;
import com.qeweb.scm.filemodule.service.FileCollaborationService;
import com.qeweb.scm.filemodule.service.FileFeedbackService;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;

@Controller
@RequestMapping("/manager/file/fileCollaboation")
public class FileCollaborationController {
	
	
	@Autowired
	private FileCollaborationService fileCollaborationService;
	
	@Autowired
	private FileFeedbackService fileFeedbackService;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/file/fileCollaboationList";
	}
	
	 /**
     * 获取文件协同列表
     */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> companyList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		Page<FileCollaborationEntity> page = fileCollaborationService.getFileCollaboationList(pageNumber,pageSize,searchParamMap);
		for(FileCollaborationEntity s : page.getContent()){
			if(s != null){
				s.setFileContent(null);
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	
	
	
	/**
	 * 获取文件协同详细列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap FileCollaboration
	 * @return
	 */
	@RequestMapping(value="getFileItemList/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getFileItemList(@PathVariable(value="id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_fileCollaboration.id", StringUtils.convertToString(id));
		Page<FileFeedbackEntity> userPage = fileCollaborationService.getFeedbackList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	 /**
     * 新增文件协同
	 * @throws IOException 
     */
	@RequestMapping(value = "add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(@Valid FileCollaborationEntity fileCollaboration,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		FileCollaborationEntity temp =  fileCollaborationService.getFileCollaboationByTitle(fileCollaboration.getTitle());
		if( temp != null) {
			map.put("msg", "名称已存在！");
			map.put("success", false);
			return map;
		}
		String fileName =  fileCollaboration.getFileName();
		String filePath = fileCollaboration.getFilePath();
		Blob fileContent= fileCollaboration.getFileContent();
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {			
			File savefile = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			Session session = generialDao.getSession();
			LobHelper lh = session.getLobHelper();
			
			fileContent = lh.createBlob(file2bytes(savefile));
			fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
			filePath = savefile.getPath().replaceAll("\\\\", "/");
		}
		fileCollaboration.setFileContent(fileContent);
		fileCollaboration.setFileName(fileName);
		fileCollaboration.setFilePath(filePath);
		fileCollaboration.setPublishStatus(0);
		fileCollaborationService.add(fileCollaboration);
		map.put("success", true);
		return map;
	}
	
	 /**
     * 更新文件协同
     */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@Valid @ModelAttribute("file") FileCollaborationEntity fileCollaboration,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)throws IOException {
		String fileName =  fileCollaboration.getFileName();
		String filePath = fileCollaboration.getFilePath();
		Blob fileContent= fileCollaboration.getFileContent();
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {			
			File savefile = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			Session session = generialDao.getSession();
			LobHelper lh = session.getLobHelper();
			
			fileContent = lh.createBlob(file2bytes(savefile));
			fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
			filePath = savefile.getPath().replaceAll("\\\\", "/");
		}
		fileCollaboration.setFileContent(fileContent);
		fileCollaboration.setFileName(fileName);
		fileCollaboration.setFilePath(filePath);
		map = new HashMap<String, Object>();
		fileCollaborationService.update(fileCollaboration);
		map.put("success", true);
		return map;
		
	}
	

	
	 /**
     * 删除文件协同
     */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@RequestBody List<FileCollaborationEntity> fileCollaborationList){
		Map<String,Object> map = new HashMap<String, Object>();
		fileCollaborationService.delete(fileCollaborationList);
		map.put("success", true);
		return map;
	}
	
	
	/**
     * 获取单个文件协同
     */
	@RequestMapping("get/{id}")
	@ResponseBody
	public FileCollaborationEntity get(@PathVariable("id") Long id, Model model){
		FileCollaborationEntity  e = fileCollaborationService.get(id);
		e.setFileContent(null);
		String vendorNames = "";
		String [] vendorIds=e.getVendorIds().split(",");
		if(null!=vendorIds && vendorIds.length>0){
			for (String str : vendorIds) {
				if(!StringUtils.isEmpty(str)){
					OrganizationEntity org = 	organizationDao.findOne(Long.valueOf(str));
					vendorNames= vendorNames + org.getName()+",";
				}
			}	
		}
		e.setVendorNames(vendorNames);
		e.setCollaborationTypeCode(e.getCollaborationType().getCode());
		model.addAttribute("FileCollaborationEntity", e);
		return e;
	}
	
	
	 /**
     * 跳转文件协同详细页面
	 * @throws 
     */
	@RequestMapping(value="viewDetailed/{id}", method = RequestMethod.GET)
	public String viewDetailed(@PathVariable("id") Long id,Model model) {
		//通过id查询文件协同
		FileCollaborationEntity  e =  new  FileCollaborationEntity(); 
		e= fileCollaborationService.get(id);
		if(e != null){
			e.setFileContent(null);
			Set<FileFeedbackEntity> feedbackSet  = e.getFileFeedback();
			if(CommonUtil.isNotNullCollection(feedbackSet)){
				for (FileFeedbackEntity fileFeedbackEntity : feedbackSet) {
					fileFeedbackEntity.setFileContent(null);
				}
			}
		}
		model.addAttribute("FileCollaboration", e);
		return "back/file/fileCollaboationDetailed";
	}
	
	
	
	
	
	
	 /**
     * 发布文件协同
	 * @throws IOException 
     */
	@RequestMapping(value = "publish",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> publish(@RequestBody List<FileCollaborationEntity> fileCollaborationList) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		fileCollaborationService.publish(fileCollaborationList);
		map.put("success", true);
		return map;
	}
	
	
	 /**
     * 下载文件协同
     */
	@RequestMapping("downloadFile")
	public ModelAndView downloadFile(@RequestParam("billId") Long id,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileCollaborationEntity  f = fileCollaborationService.get(id);
		String fileName=f.getFileName();
		Blob content=f.getFileContent();
		String filep=f.getFilePath();
		
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

	
	
	 /**
     * 下载回传文件
     */
	@RequestMapping("downloadFeedbackFile")
	public ModelAndView downloadFeedbackFile(@RequestParam("billId") Long id,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileFeedbackEntity  f = fileFeedbackService.get(id);
		String fileName=f.getFileName();
		Blob content=f.getFileContent();
		String filep=f.getFilePath();
		
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
	
	
	
	
	
	@RequestMapping(value = "fileFeedbackUpload",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> fileFeedbackUpload(@RequestParam("fileCollaborationId") Long id,@RequestParam("planfiles") MultipartFile file) throws IOException{
		map = new HashMap<String, Object>();

		String fileName = "";
		String filePath = "";
		Blob fileContent = null;
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {			
			File savefile = FileUtil.savefile(file, request.getSession().getServletContext().getRealPath("/") + "upload/");
			Session session = generialDao.getSession();
			LobHelper lh = session.getLobHelper();
			fileContent = lh.createBlob(file2bytes(savefile));
			fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
			filePath = savefile.getPath().replaceAll("\\\\", "/");
		}
		
		//更改回传的状态
		fileCollaborationService.fileFeedbackUpload(id,fileName,filePath,fileContent);
		map.put("success", true);
		map.put("msg", "操作成功");
		return map;
	}
	
	
	
	/**
	 *  显示页面获取文件协同详细列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap FileCollaboration
	 * @return
	 */
	@RequestMapping(value="getViewFileCollaborationList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getViewFileCollaborationList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "query-");
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParamMap.put("EQ_abolished", "0");
		searchParamMap.put("EQ_publishStatus","1");
		searchParamMap.put("LIKE_vendorIds",user.orgId);
		Page<FileCollaborationEntity> page = fileCollaborationService.getFileCollaboationList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}

	
	
	
	
	
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bind(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			FileCollaborationEntity  e = fileCollaborationService.get(id);
			if(e != null){
				e.setFileContent(null);
			}
			model.addAttribute("file",  e);
		}
	}
	
	
	
	/**
	 * 将文件转换为字节编码
	 * @throws IOException 
	 */
	public byte[] file2bytes(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		byte[] data = new byte[in.available()];

		try {
			in.read(data);
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}

		return data;
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
