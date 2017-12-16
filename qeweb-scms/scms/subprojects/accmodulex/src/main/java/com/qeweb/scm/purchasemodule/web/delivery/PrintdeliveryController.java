package com.qeweb.scm.purchasemodule.web.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.web.vo.DeliveryVO;
import com.qeweb.scm.purchasemodule.web.vo.PrintDlvVO;

/**
 * 发货单打印
 */
@Controller
@RequestMapping(value = "/manager/order/printdelivery")
public class PrintdeliveryController {
	
	private Map<String,Object> map;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private DictItemService dictItemService;
	
	/**
	 * 打印送货单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pending/{id}",method = RequestMethod.GET)
	public String pendingList(@PathVariable("id") Long id,Model model) {
		DeliveryEntity deliveryInfo=deliveryService.getDeliveryById(id);
		DictItemEntity dictItem = dictItemService.findDictItemByCode(deliveryInfo.getTransportType());
		deliveryInfo.setTransportName(dictItem.getName());
		//add　by chao.gu 20170817 二维码前加*A
		model.addAttribute("deliveryCode", "*A"+deliveryInfo.getDeliveryCode());
		//add end
		model.addAttribute("delivery_Info", deliveryInfo);
		List<DeliveryItemEntity> list = deliveryService.getDeliveryItems(deliveryInfo);
		
		if(null!=list &&list.size()>0){
			if(PurchaseConstans.SHIP_TYPE_REPL == deliveryInfo.getShipType()){
				for (DeliveryItemEntity deliveryItemEntity : list) {
					StringBuffer shipQrCode=new StringBuffer();
					shipQrCode.append("版本：*")
							.append(null==deliveryItemEntity.getVersion()?"":deliveryItemEntity.getVersion())
							.append("   批次：*")
							.append(null==deliveryItemEntity.getCharg()?"":deliveryItemEntity.getCharg())
							.append("  生产日期：*")
							.append(null==deliveryItemEntity.getManufactureDate()?"":DateUtil.dateToString(
									deliveryItemEntity.getManufactureDate(),
									"yyyyMMdd")).append("   交货单：*")
							.append(null==deliveryItemEntity.getDn()?"":deliveryItemEntity.getDn())
							.append("  ASN: *")
							.append(null==deliveryInfo.getDeliveryCode()?"":deliveryInfo.getDeliveryCode());
					deliveryItemEntity.setShipQrCode(shipQrCode.toString());
				}
				
			}
		}
		int pageCount =15;
		if(PurchaseConstans.SHIP_TYPE_REPL == deliveryInfo.getShipType()){
			pageCount =3;
		}
		int pageSize=list.size()/pageCount;
		if(list.size()%pageCount>0){
			pageSize++;//有余数
		}
		pageSize =0==pageSize?1:pageSize;
		List<DeliveryVO> volist = new ArrayList<DeliveryVO>();
		
		for(int i=1;i<=pageSize;i++){
			DeliveryVO vo=new DeliveryVO();
			BeanUtil.copyPropertyNotNull(deliveryInfo, vo);
			List<DeliveryItemEntity> subList=null;
			if(i<pageSize){
				subList=list.subList((i-1)*pageCount, i*pageCount);
			}else{
				subList=list.subList((i-1)*pageCount, list.size());
			}
			vo.setDeliveryItem(subList);
			volist.add(vo);
		}
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("volist", volist);
		return "back/delivery/printPage";

	}
	
	/**
	 * 打印大包装
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="maxPackage/{id}",method = RequestMethod.GET)
	public String getPCList(@PathVariable("id") Long id,Model model) {
		DeliveryItemEntity dlvItem=deliveryService.getDeliveryItemById(id);
		Double deliveryQty = dlvItem.getDeliveryQty();
		Double standardBoxNum =dlvItem.getStandardBoxNum();//大包装
		String maxboxNum=dlvItem.getBoxNum();//箱数（大包装）
		
		Double maxBox=Double.valueOf(maxboxNum)-1;
		Double lastMaxBoxNum=BigDecimalUtil.sub(deliveryQty, BigDecimalUtil.mul(standardBoxNum, maxBox));
		
		List<PrintDlvVO> pcList=new ArrayList<PrintDlvVO>();
		PrintDlvVO vo=new PrintDlvVO();
	
		
		Double tempBox = BigDecimalUtil.sub(deliveryQty,standardBoxNum);
		if(tempBox < 0 ){//判断是否小于标准箱
			
			vo.setVendorCode(dlvItem.getOrderItem().getOrder().getVendor().getCode());
			vo.setMaterialCode("*M"+dlvItem.getMaterial().getCode());
			vo.setMaterialName(dlvItem.getMaterial().getName());
			vo.setMaterialVersion("*V"+dlvItem.getVersion());
			vo.setBoxNum("*Q"+deliveryQty);
			vo.setCharg("*B"+dlvItem.getCharg());
			vo.setManufactureDate("*D"+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
			pcList.add(vo);
			
			
		}else{
			
			vo.setVendorCode(dlvItem.getOrderItem().getOrder().getVendor().getCode());
			vo.setMaterialCode("*M"+dlvItem.getMaterial().getCode());
			vo.setMaterialName(dlvItem.getMaterial().getName());
			vo.setMaterialVersion("*V"+dlvItem.getVersion());
			vo.setBoxNum("*Q"+standardBoxNum);
			vo.setCharg("*B"+dlvItem.getCharg());
			vo.setManufactureDate("*D"+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
			pcList.add(vo);
			
			
			for(double i=1;i<maxBox;i++){
				PrintDlvVO vo1=new PrintDlvVO();
				BeanUtil.copyPropertyNotNull(vo, vo1);
				pcList.add(vo1);
			}
			
			
			if(maxBox>0){//add by chao.gu 20170809
				PrintDlvVO lastVo=new PrintDlvVO();
				BeanUtil.copyPropertyNotNull(vo, lastVo);
				vo.setBoxNum("*Q"+lastMaxBoxNum);
				pcList.add(lastVo);
			}
		}
		
		
		
		List<List<PrintDlvVO>> aaa = new ArrayList<List<PrintDlvVO>>();
		int count = pcList.size()%1==0?pcList.size()/1:pcList.size()/1+1;
		for (int j = 1; j <= count; j++) {
			List<PrintDlvVO> temp = new ArrayList<PrintDlvVO>(); 
			for (int i = 0; i < pcList.size(); i++) {
				if((i+1) <= j*1 && (i+1) > (j-1)*1){
					temp.add(pcList.get(i));
				}
			}
			aaa.add(temp);
		}
		model.addAttribute("aaa", aaa);
		model.addAttribute("size", aaa.size());
		return "back/delivery/printPCPage";
		
	}
	
	/**
	 * 打印小包装
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="minPackage/{id}",method = RequestMethod.GET)
	public String getDTList(@PathVariable("id") Long id,Model model) {
		DeliveryItemEntity dlvItem=deliveryService.getDeliveryItemById(id);
		Double deliveryQty = dlvItem.getDeliveryQty();
		Double minPackageQty=dlvItem.getMinPackageQty();//小包装
		String minboxNum=dlvItem.getMinBoxNum();//箱数（小包装）
		
		Double minBox=Double.valueOf(minboxNum)-1;
		Double lastMinBoxNum=BigDecimalUtil.sub(deliveryQty, BigDecimalUtil.mul(minPackageQty, minBox));
		
		List<PrintDlvVO> pcList=new ArrayList<PrintDlvVO>();
		PrintDlvVO vo=new PrintDlvVO();
		
		Double tempBox = BigDecimalUtil.sub(deliveryQty,minPackageQty);
		if(tempBox < 0 ){//判断是否小于标准箱
		
			vo.setVendorCode(dlvItem.getOrderItem().getOrder().getVendor().getCode());
			vo.setMaterialCode("*M"+dlvItem.getMaterial().getCode());
			vo.setMaterialName(dlvItem.getMaterial().getName());
			vo.setMaterialVersion("*V"+dlvItem.getVersion());
			vo.setBoxNum("*Q"+deliveryQty);
			vo.setVendorCharg("*T"+dlvItem.getVendorCharg());
			vo.setCharg("*B"+dlvItem.getCharg());
			vo.setManufactureDate("*D"+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
			pcList.add(vo);
			
			
		}else{
			
			vo.setVendorCode(dlvItem.getOrderItem().getOrder().getVendor().getCode());
			vo.setMaterialCode("*M"+dlvItem.getMaterial().getCode());
			vo.setMaterialName(dlvItem.getMaterial().getName());
			vo.setMaterialVersion("*V"+dlvItem.getVersion());
			vo.setBoxNum("*Q"+minPackageQty);
			vo.setVendorCharg("*T"+dlvItem.getVendorCharg());
			vo.setCharg("*B"+dlvItem.getCharg());
			vo.setManufactureDate("*D"+DateUtil.dateToString(dlvItem.getManufactureDate(), "yyyyMMdd"));
			pcList.add(vo);
			for(double i=1;i<minBox;i++){
				PrintDlvVO vo1=new PrintDlvVO();
				BeanUtil.copyPropertyNotNull(vo, vo1);
				pcList.add(vo1);
			}
			
			if(minBox>0){//add by chao.gu 20170809
			PrintDlvVO lastVo=new PrintDlvVO();
			BeanUtil.copyPropertyNotNull(vo, lastVo);
			vo.setBoxNum("*Q"+lastMinBoxNum);
			pcList.add(lastVo);
			}
			
			
		}
		
		
		
		
		
		
		
		
		
		List<List<PrintDlvVO>> aaa = new ArrayList<List<PrintDlvVO>>();
		int count = pcList.size()%1==0?pcList.size()/1:pcList.size()/1+1;
		for (int j = 1; j <= count; j++) {
			List<PrintDlvVO> temp = new ArrayList<PrintDlvVO>(); 
			for (int i = 0; i < pcList.size(); i++) {
				if((i+1) <= j*1 && (i+1) > (j-1)*1){
					temp.add(pcList.get(i));
				}
			}
			aaa.add(temp);
		}
		model.addAttribute("aaa", aaa);
		model.addAttribute("size", aaa.size());
		return "back/delivery/printDTPage";
	}
	


}
