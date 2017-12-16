package com.qeweb.scm.basemodule.web.manager;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.DataInService;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.vo.DataInVO;

@Controller
@RequestMapping("/manager/basedata/dataIn")
public class DataInController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private DataInService dataInService;
	
	@Autowired
	private HttpServletRequest request;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/dataInList";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("filesUpload")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files,Long cycleId) {
		File savefile = null;
		try{ 
			log("->开始准备保存上传文件...");
			logger.log("------------------->开始检查导入时间<---------------------");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(DataInVO.class), getLogger()); 
			List<DataInVO> list = (List<DataInVO>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = dataInService.combine(list);
			if(flag) {
				map.put("msg", "导入数据成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入数据失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入数据失败!");  
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		}
		return map;   
	}
 
	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	@Override
	public void log(Object message) {
		getLogger().log(message); 
	} 
}
