package com.qeweb.scm.purchasemodule.web.inventory;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.InventoryEntity;
import com.qeweb.scm.purchasemodule.service.InventoryService;
import com.qeweb.scm.purchasemodule.web.vo.InventoryTransfer;
import com.qeweb.scm.purchasemodule.web.vo.MinInventorySettingTransfer;

/**
 * VMI库存管理
 * @author alex
 * @date 2015年5月4日
 */
@Controller
@RequestMapping(value = "/manager/inventory/vmi")
public class InventoryController implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private InventoryService inventoryService;    
	
	/**
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/inventory/inventoryvmiList";                 
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<InventoryEntity> userPage = inventoryService.getVmiInventory(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping("getInventory/{id}")
	@ResponseBody
	public InventoryEntity getInventory(@PathVariable("id") Long id){
		InventoryEntity entity = inventoryService.getInventory(id);   
		entity.setVendorId(entity.getVendor().getId());
		entity.setVendorCode(entity.getVendor().getCode());
		entity.setVendorName(entity.getVendor().getName());
		entity.setMaterialId(entity.getMaterial().getId());  
		entity.setMaterialCode(entity.getMaterial().getCode());
		entity.setMaterialName(entity.getMaterial().getName());
		entity.setStockMinQty(entity.getMinInventory() != null ? entity.getMinInventory().getStockMinQty() : 0d);
		return entity;       
	}
	
	/**
	 * 更新最小库存
	 * @return
	 */
	@RequestMapping(value = "updateMin",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateMin(@Valid @ModelAttribute("inventory") InventoryEntity inventory) {
		inventoryService.updateMinInventory(inventory);
		map = new HashMap<String, Object>();
		map.put("msg", "编辑最小库存成功!");  
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("filesUpload")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Map<String,Object> filesUpload(@RequestParam("planfiles") MultipartFile files) {
		File savefile = null;
		String logpath = null; 
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，导入失败");
				throw new Exception();
			}
			//2、读取并解析文件
			log("->文件上传服务器成功，开始解析数据...");
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(MinInventorySettingTransfer.class), getLogger()); 
			List<MinInventorySettingTransfer> list = (List<MinInventorySettingTransfer>) excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象并保存...");
			boolean flag = inventoryService.saveMinInventorySetting(list, getLogger());
			if(flag) {
				map.put("msg", "批量设置最小库存成功!");
				map.put("success", true);
			} else {
				map.put("msg", "批量设置最小库存失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "批量设置最小库存失败!");  
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
	
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("VMI库存", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		List<InventoryTransfer> list = inventoryService.getVmiInventoryVo(searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(InventoryTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}

	@Override
	public void log(Object message) {
		getLogger().log(message); 
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
	
}
