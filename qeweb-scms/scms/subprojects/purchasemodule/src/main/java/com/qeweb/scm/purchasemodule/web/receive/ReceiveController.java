package com.qeweb.scm.purchasemodule.web.receive;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILog;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.service.ReceiveService;
import com.qeweb.scm.purchasemodule.web.vo.ReceiveTransfer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 采购收货单管理
 * @author alex
 * @date 2015年5月4日
 * @path com.qeweb.scm.purchasemodule.web.delivery.ReceiveController.java
 */
@Controller
@RequestMapping(value = "/manager/order/receive")
public class ReceiveController implements ILog {
	
	private ILogger logger = new FileLogger();
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	/**
	 * 收货看板
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pending", method = RequestMethod.GET)
	public String pendingList(Model model) {
		return "back/receive/receivePendingList";
	}
	
	@RequestMapping(value="pending", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getPendingList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_deliveryStatus", PurchaseConstans.DELIVERY_STATUS_YES + "");
		Page<DeliveryEntity> userPage = deliveryService.getDeliverys(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 收货记录
	 * @param theme
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/receive/receiveList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(@RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<ReceiveEntity> userPage = receiveService.getReceives(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * 获取收货主单信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getReceive/{id}")
	@ResponseBody
	public ReceiveEntity getDelivery(@PathVariable("id") Long id){
		return receiveService.getReceiveById(id);
	}
	
	/**
	 * 查看收货单详情
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "receiveitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemList(@PathVariable("id") Long receiveid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_receive.id", receiveid + "");
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
//				item.setDeliveryItem(deliveryItem);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
				
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "byorderreceiveitem/{id}")
	@ResponseBody
	public Map<String,Object> getItemListByOrder(@PathVariable("id") Long orderid, @RequestParam(value="page") int pageNumber, @RequestParam(value="rows") int pageSize, Model model){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		searchParamMap.put("EQ_orderItem.order.id", orderid + "");   
		Page<ReceiveItemEntity> userPage = receiveService.getReceiveItems(pageNumber,pageSize,searchParamMap);
		List<ReceiveItemEntity> itemList = userPage.getContent();
		if(itemList != null && itemList.size() > 0){
			for (ReceiveItemEntity item: itemList){
				long deliveryItemId = receiveService.getDeliveryItemIdByReceiveItemId(item.getId());
				DeliveryItemEntity deliveryItem = deliveryService.getDeliveryItemById(deliveryItemId);
				item.setMaterialCode(deliveryItem.getMaterial().getCode());
				item.setMaterialName(deliveryItem.getMaterial().getName());
				item.setDeliveryQty(deliveryItem.getDeliveryQty());
			}
		}
		map = new HashMap<String, Object>();
		map.put("rows",itemList);
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	/**
	 * Single收货
	 * @param deliveryId 发货单ID
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "doreceivesingle",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveReceive(@RequestParam("deliveryId") long deliveryId, @RequestParam(value="datas") String datas) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		ReceiveEntity receive = new ReceiveEntity();
		receive.setReceiveCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.RECEIVE));
		List<ReceiveItemEntity> receiveItem = new ArrayList<ReceiveItemEntity>();
		ReceiveItemEntity item = null;
		JSONObject object = JSONObject.fromObject(datas);    
		JSONArray array = (JSONArray) object.get("rows");  
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = new ReceiveItemEntity();
//			item.setReceive(receive); 
			item.setReceiveQty(StringUtils.convertToDouble(object.get("toreceiveQty") + ""));
			item.setReturnQty(StringUtils.convertToDouble(object.get("toreturnQty") + ""));
			item.setInStoreQty(BigDecimalUtil.sub(item.getReceiveQty(), item.getReturnQty())); //入库=收货-验退
			item.setDeliveryItemId(StringUtils.convertToLong(object.get("id") + "")); 
			receiveItem.add(item);
		}
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		receiveService.saveReceive(deliveryId, receive, receiveItem, new UserEntity(user.id));
		map.put("message", "保存收货单成功");  
		map.put("success", true);
		return map;   
	}
	
	/**
	 * 批量收货
	 * @param deliveryList
	 * @return
	 */
	@RequestMapping(value = "doreceive",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doReceives(@RequestBody List<DeliveryEntity> deliveryList){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		receiveService.doReceives(deliveryList, new UserEntity(user.id));
		map.put("message", "批量收货成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 导入收货单
	 * @param files
	 * @return
	 */
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
			ExcelUtil excelutil = new ExcelUtil(savefile.getPath(), new ExcelAnnotationReader(ReceiveTransfer.class), getLogger()); 
			List<ReceiveTransfer> list = excelutil.readExcel(0);
			if(excelutil.getErrorNum() > 0 || list.size() == 0) {
				throw new Exception("上传文件为空，或无内容");
			}
			//3、组装并保存数据
			log("->数据转换完成共" + list.size() + " 条，开始构建持久化对象...");
			boolean flag = receiveService.saveReceives(list, getLogger());
			if(flag) {
				map.put("msg", "导入收货单成功!");
				map.put("success", true);
			} else {
				map.put("msg", "导入收货单失败!");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "导入采购计划失败!");  
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
