package com.qeweb.scm.basemodule.web.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.constants.ApplicationProConstant;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.MaterialService;
import com.qeweb.scm.basemodule.service.MaterialTypeService;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.TreeUtil;
import com.qeweb.scm.basemodule.vo.MaterialTypeImpVO;
import com.qeweb.scm.basemodule.web.manager.impl.MaterialTypeControllerImpl;

@Controller
@RequestMapping("/manager/basedata/materialType")
public class MaterialTypeController implements ILog{
	
	private ILogger logger = new FileLogger();
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MaterialTypeService materialTypeService;
	
	private Map<String,Object> map;
	
	@Autowired
	private MaterialTypeControllerImpl materialTypeControllerImpl;
	

	/** 外部调用tree */
	@RequestMapping(value="getMaterialTypeTree",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTree> getMaterialTypeTree(Long id,Integer orgType,ServletRequest request){
		return materialTypeControllerImpl.getMaterialTypeTree(id,orgType,request);
	}
	
	
	@RequestMapping(value="getMaterialTypeEasyuiTree",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTree> getEasyuiTree(Long id,Integer orgType,ServletRequest request){
		List<EasyuiTree> rootList = new ArrayList<EasyuiTree>();
		List<EasyuiTree> materialNodeList = null;
		if( id!=null&&id!=0){
			List<MaterialTypeEntity> materialTypeList = materialTypeService.getMaterialListByPIdAndBuyer(id);
			rootList = TreeUtil.toEasyuiTree_materialType(materialTypeList);
		}else{
			List<MaterialTypeEntity> materialTypeList = materialTypeService.getMaterialListByPIdAndBuyer(id);
			materialNodeList = TreeUtil.toEasyuiTree_materialType(materialTypeList);
			EasyuiTree root = new EasyuiTree("", "全部分类", "open", "", materialNodeList, null);
			rootList.add(root);
		}
		return rootList;
	}
	
	@RequestMapping("getMaterialTypeLeafList")
	@ResponseBody
	public Map<String,Object> getMaterialTypeLeafList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MaterialTypeEntity> page = materialTypeService.getMatTypeList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
		
	}
	
	
	@RequestMapping(value = "addNewMaterialType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNewMaterialType(@Valid MaterialTypeEntity materialType){
		Map<String,Object> map = materialTypeService.addNewMaterialType(materialType);
		return map;
	}
	
	@RequestMapping(value="getLeafMaterialTypeList")
	@ResponseBody
	public List<MaterialTypeEntity> getLeafMaterialTypeList(Integer leaf){
		return materialTypeService.findLeafMaterialTypeList(leaf);
	}
	
	
	@RequestMapping(value = "update",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(@ModelAttribute("materialType") MaterialTypeEntity materialType){
		Map<String,Object> map = materialTypeService.updateMaterialType(materialType);
		return map;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(Long id){
		Map<String,Object> map = materialTypeService.delete(id);
		return map;
	}
	
	@RequestMapping(value="abolishBatch",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> abolishBatch(Long id){
		Map<String,Object> map = materialTypeService.abolishBatch(id);
		return map;
	}
	
	@RequestMapping(value="import",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> importExcel(MultipartFile importFile){
		File savefile = null;
		String logPath = null;
		try {
			logPath = getLogger().init(this);
			savefile = FileUtil.savefile(importFile, null);
			ExcelUtil<MaterialTypeImpVO> excelutil = new ExcelUtil<MaterialTypeImpVO>(savefile.getPath(), new ExcelAnnotationReader(MaterialTypeImpVO.class),getLogger()); 
			List<MaterialTypeImpVO> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			map = materialTypeService.importFile(list,getLogger());
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
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void bindUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("materialType", materialTypeService.getMaterial(id));
		}
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
