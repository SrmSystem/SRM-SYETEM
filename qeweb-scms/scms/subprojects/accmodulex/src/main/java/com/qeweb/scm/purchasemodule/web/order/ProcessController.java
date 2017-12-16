package com.qeweb.scm.purchasemodule.web.order;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.convert.EasyuiComboBox;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.ProcessEntity;
import com.qeweb.scm.purchasemodule.entity.ProcessMaterialRelEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemMatEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderProcessEntity;
import com.qeweb.scm.purchasemodule.repository.ProcessMaterialRelDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemMatDao;
import com.qeweb.scm.purchasemodule.service.ProcessService;

/**
 * 工序（弃用）
 * @author hp-pc
 *
 */
@Controller
@RequestMapping(value = "/manager/order/process")
public class ProcessController extends BaseService implements ILog {
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private ProcessMaterialRelDao processMaterialRelDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private PurchaseOrderItemMatDao purchaseOrderItemMatDao;
	

	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/order/processList";  
	}
	
	@RequestMapping(value = "getProcessCombo/{materialId}",method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiComboBox> getOrgCombo(@PathVariable("materialId") long materialId,ServletRequest request){
		List<EasyuiComboBox> couTreeList = new LinkedList<EasyuiComboBox>();
		List<ProcessMaterialRelEntity> list=processMaterialRelDao.findByMaterialIdAndAbolished(materialId, 0);
		for(int i=0;i < list.size();i++){
			ProcessEntity bo=list.get(i).getProcess();
			if(bo!=null){
				EasyuiComboBox option = new EasyuiComboBox(bo.getId()+"",bo.getName());
				couTreeList.add(option);
			}
		}
		return couTreeList;
	}
	
	/**
	 * 订单下工序
	 * @param model
	 * @param orderItemId
	 * @param isVendor
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value = "displayProcess/{orderItemId}/{isVendor}")
	public String displayProcess(Model model ,@PathVariable("orderItemId") Long orderItemId,@PathVariable("isVendor") Boolean isVendor){
		model.addAttribute("orderItemId", orderItemId);
		model.addAttribute("isVendor", isVendor);
		
		PurchaseOrderItemMatEntity item=purchaseOrderItemMatDao.findOne(orderItemId);
		model.addAttribute("orderItem", item);
		processService.initOrderProcess(orderItemId);
		return "back/order/orderProcessList";  
	}
	
	@RequestMapping(value="getPurchaseOrderProcessList/{orderItemId}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("orderItemId") long orderItemId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItemId", orderItemId);
		Page<PurchaseOrderProcessEntity> userPage = processService.getPurchaseOrderProcessList(pageNumber, pageSize, searchParamMap);
		if(userPage!=null){
			for (int i = 0 ; i < userPage.getContent().size() ; i++) {
				PurchaseOrderItemMatEntity sys=purchaseOrderItemMatDao.findOne(userPage.getContent().get(i).getOrderItemId());
				if(sys!=null){
					userPage.getContent().get(i).setOrderCount(sys.getOrderNum());
				}
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value="getPurchaseOrderItemMatList/{orderItemId}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPurchaseOrderItemMatList(@PathVariable("orderItemId") long orderItemId,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.id", orderItemId);
		Page<PurchaseOrderItemMatEntity> userPage = processService.getOrderItemMatList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	
	/**
	 * 上传工序图片
	 * @param request
	 * @param files
	 * @return
	 */
	@RequestMapping(value="/saveFile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveFile(HttpServletRequest request, @RequestParam("fujian") MultipartFile files){
		Map<String,Object> map = new HashMap<String, Object>();
		File savefile = null;
		String logpath = null;
		try{ 
			logpath = getLogger().init(this);
			log("->开始准备保存上传文件...");
			//1、保存文件至服务器
			savefile = FileUtil.savefile(files, request.getSession().getServletContext().getRealPath("/") + "upload/");
			if(savefile == null || savefile.length() == 0) {
				log("->上传文件为空，上传失败");
				throw new Exception("上传文件为空，上传失败!");
			}
			
			
	        
	        
			//2、组装并保存数据	
			String fileName = files.getOriginalFilename();
			String filePath = savefile.getPath().replaceAll("\\\\", "/");
			map.put("fileName",fileName);
			map.put("filePath",filePath);
			
			map.put("message","操作成功！");
			map.put("success", true);
		} catch (Exception e) {
			map.put("message", e.getMessage());  
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

	
	/**
	 * 供应商填写订单下工序信息
	 * @param orderProcessx
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveOrderProcess",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveOrderProcess(@Valid PurchaseOrderProcessEntity orderProcessx) throws Exception{ 
	
		
		String items=orderProcessx.getTableDatas();
		Map<String,Object> map = new HashMap<String, Object>();
		//明细
		List<PurchaseOrderProcessEntity> itemList = new ArrayList<PurchaseOrderProcessEntity>();
		if(!StringUtils.isEmpty(items)){
			PurchaseOrderProcessEntity item = null;
			JSONObject object = JSONObject.fromObject(items);     
			JSONArray array = (JSONArray) object.get("rows");
			
			if(array.size()>0){
				for(int i= 0; i < array.size(); i ++) {
					object = array.getJSONObject(i);
					item = new PurchaseOrderProcessEntity(); 
					item.setId(StringUtils.convertToLong(StringUtils.convertToString(object.get("id"))==null?"0":StringUtils.convertToString(object.get("id"))));
					item.setOrderNum(StringUtils.convertToDouble(object.get("orderNum")+""));
					item.setFileName(StringUtils.convertToString(object.get("fileName")+""));
					item.setFilePath(StringUtils.convertToString(object.get("filePath")+""));
					itemList.add(item);
				}
			}
		}
		processService.saveOrderProcess(itemList);
		map.put("msg", "操作成功");
		map.put("success", true);
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
