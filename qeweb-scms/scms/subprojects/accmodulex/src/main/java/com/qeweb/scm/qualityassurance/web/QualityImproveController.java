package com.qeweb.scm.qualityassurance.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.QualityImproveEntity;
import com.qeweb.scm.qualityassurance.service.QualityImproveService;

@Controller
@RequestMapping("/manager/qualityassurance/qualityImprove")
public class QualityImproveController implements ILog {
	private Map<String, Object> map;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private QualityImproveService qualityImproveService;

	private ILogger logger = new FileLogger();

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("vendor", false);
		return "back/qualityImprove/qualityImproveList";
	}

	@RequestMapping(value = "vendor", method = RequestMethod.GET)
	public String vendorlist(Model model) {
		model.addAttribute("vendor", true);
		return "back/qualityImprove/qualityImproveVenList";
	}

	@RequestMapping(value = "/{vendor}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> qualityImproveList(@PathVariable(value = "vendor") boolean vendor,
			@RequestParam(value = "page") int pageNumber, @RequestParam(value = "rows") int pageSize, Model model,
			ServletRequest request) {
		Map<String, Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		if (vendor) {
			ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			searchParamMap.put("EQ_vendor.id", user.orgId + "");
			searchParamMap.put("EQ_dataStatus", 1);
		}
		Page<QualityImproveEntity> page = qualityImproveService.getQualityImproveList(pageNumber, pageSize,
				searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows", page.getContent());
		map.put("total", page.getTotalElements());
		return map;
	}

	// 质量改进通知 附件上传
	@RequestMapping(value = "informUpload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> informUpload(@Valid QualityImproveEntity qualityImproveEntity,
			@RequestParam("informfiles") MultipartFile files, @RequestParam("mid") String mid) {
		Map<String, Object> map = new HashMap<String, Object>();
		File savefile = null;
		String logpath = null;
		String fileName = null;
		try {
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			// 1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if (savefile == null || savefile.length() == 0) {
				if (mid.equals("")) {
					log("->上传文件为空，上传失败");
					throw new Exception();
				}
			}
			// 2、组装并保存数据
			String originalName = files.getOriginalFilename();
			fileName = originalName.substring(0, originalName.lastIndexOf("."));
			if (!mid.equals("")) {
				qualityImproveEntity.setId(StringUtils.convertLong(mid));
				map = qualityImproveService.modInform(savefile, fileName, qualityImproveEntity);
			} else {
				map = qualityImproveService.informUpLoad(savefile, fileName, qualityImproveEntity);
			}
		} catch (Exception e) {
			map.put("message", "上传附件失败!");
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			getLogger().destory();
			map.put("name", StringUtils.encode(new File(logpath).getName()));
			map.put("log", StringUtils.encode(logpath));
		}
		return map;
	}

	@RequestMapping("getQualityImprove/{id}")
	@ResponseBody
	public QualityImproveEntity getQualityImprove(@PathVariable("id") Long id) {
		QualityImproveEntity entity = qualityImproveService.getQualityImprove(id);
		return entity;
	}

	/**
	 * 批量发布通知
	 * 
	 * @param informList
	 * @return
	 */
	@RequestMapping(value = "publishInform", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publishInform(@RequestBody List<QualityImproveEntity> informList) {
		Map<String, Object> map = new HashMap<String, Object>();
		qualityImproveService.publishInforms(informList);
		;
		map.put("success", true);
		return map;
	}

	/**
	 * 批量关闭通知
	 * 
	 * @param informList
	 * @return
	 */
	@RequestMapping(value = "closeInform", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> closeInform(@RequestBody List<QualityImproveEntity> informList) {
		Map<String, Object> map = new HashMap<String, Object>();
		qualityImproveService.closeInforms(informList);
		;
		map.put("success", true);
		return map;
	}

	/**
	 * 批量废除通知
	 * 
	 * @param informList
	 * @return
	 */
	@RequestMapping(value = "abolishInform", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> abolishInform(@RequestBody List<QualityImproveEntity> informList) {
		Map<String, Object> map = new HashMap<String, Object>();
		qualityImproveService.abolishInforms(informList);
		;
		map.put("success", true);
		return map;
	}

	// 上传/修改质量改进方案
	@RequestMapping(value = "saveImprove/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveImprove(@RequestParam("improveFile") MultipartFile files,
			@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		File savefile = null;
		String logpath = null;
		String fileName = null;
		try {
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			// 1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if (savefile == null || savefile.length() == 0) {
				log("->上传文件为空，上传失败");
				throw new Exception();
			}
			// 2、组装并保存数据
			String originalName = files.getOriginalFilename();
			fileName = originalName.substring(0, originalName.lastIndexOf("."));
			QualityImproveEntity qualityImproveEntity = qualityImproveService.getQualityImprove(id);
			qualityImproveEntity.setImproveFileName(fileName);
			map = qualityImproveService.saveImprove(savefile, qualityImproveEntity);
		} catch (Exception e) {
			map.put("message", "上传附件失败!");
			map.put("success", false);
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			getLogger().destory();
			map.put("name", StringUtils.encode(new File(logpath).getName()));
			map.put("log", StringUtils.encode(logpath));
		}
		return map;

	}

	// 继续整改
	@RequestMapping("conImprove/{id}")
	@ResponseBody
	public Map<String, Object> conImprove(@PathVariable("id") Long id) {
		map = qualityImproveService.conImprove(id);
		return map;
	}

	// 完成整改
	@RequestMapping("finishImprove/{id}")
	@ResponseBody
	public Map<String, Object> finishImprove(@PathVariable("id") Long id) {
		map = qualityImproveService.finishImprove(id);
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
