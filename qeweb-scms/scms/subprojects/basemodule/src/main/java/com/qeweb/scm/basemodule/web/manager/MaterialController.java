package com.qeweb.scm.basemodule.web.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.vo.MaterialImpVO;
import com.qeweb.scm.basemodule.web.manager.impl.MaterialControllerImpl;

@Controller
@RequestMapping("/manager/basedata/material")
public class MaterialController implements ILog{
	/** 覆盖实现类 */
	@Autowired 
	private MaterialControllerImpl materialControllerImpl;
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MaterialTypeService materialTypeService;
	
	private Map<String,Object> map;
	
	/**
	 * 进入物料管理界面，获取两个系统属性，叶子节点层级，非叶子节点是否可以增加物料
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(Model model) {
		ModelAndView mv = materialControllerImpl.list(model);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> materialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		return materialControllerImpl.materialList(pageNumber,pageSize,model,request);
	}
	
	/**
	 * 查找所有的材料
	 * @param map
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAllMaterial")
	@ResponseBody
	public List<MaterialEntity> getAllMaterial(Map<String, Object> map,Model model,ServletRequest request){
		List<MaterialEntity> materialList=materialService.findAll();
		//map = new HashMap<String, Object>();
		//map.put("materialList",materialList);
		return materialList;
	}
	
	@RequestMapping(value = "addNew",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNew(MaterialEntity material,Model model,ServletRequest request){
		map = materialService.addNew(material);
		return map;
	}
	
	@RequestMapping(value = "update",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@ModelAttribute(value = "material")MaterialEntity material,Model model,ServletRequest request){
		map = materialService.update(material);
		return map;
	}
	
	@RequestMapping(value = "deleteMaterial",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteMaterialList(@RequestBody List<MaterialEntity> materialList){
		Map<String,Object> map = new HashMap<String, Object>();
		materialService.deleteMaterialList(materialList);
		map.put("success", true);
		return map;
	}
	@RequestMapping(value="/getPicStatus")
	@ResponseBody
	public String getPicStatus()
	{
		return materialService.getPicStatus();
	}
	
	/**
	 * 给物料分类
	 * @param materialIds 
	 * @param materialTypeId
	 * @return 分类结果
	 */
	@RequestMapping(value="category",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> category(String materialIds,String materialTypeId){
		Map<String,Object> map = materialService.category(materialIds,materialTypeId);
		return map;
	}
	
	@RequestMapping(value="getMaterial/{id}",method = RequestMethod.GET)
	@ResponseBody
	public MaterialEntity getMaterial(@PathVariable("id")Long id){
		return materialService.getMaterialById(id);
	}
	
	@RequestMapping(value="import",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> importExcel(MultipartFile importFile){
		File savefile = null;
		String logPath = null;
		try {
			logPath = getLogger().init(this);
			savefile = FileUtil.savefile(importFile, null);
			ExcelUtil<MaterialImpVO> excelutil = new ExcelUtil<MaterialImpVO>(savefile.getPath(), new ExcelAnnotationReader(MaterialImpVO.class),getLogger()); 
			List<MaterialImpVO> list =  excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			map = materialService.importFile(list,getLogger());
			map.put("logPath", logPath);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			getLogger().destory();  
			savefile.deleteOnExit();
		}
		return map;
	}

	/**
	 * 绑定物料，根据ID，默认为-1，如果没有则不会持久化
	 */
	public void bindMaterial(@RequestParam(value = "id",defaultValue = "-1") Long id,Model model){
		if(id!=-1l){
			model.addAttribute("material",materialService.getMaterialById(id));
		}
	}
	@RequestMapping(value="abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(@RequestBody List<MaterialEntity> materials){
		Map<String,Object> map = materialService.abolishBatch(materials);
		return map;
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
