package com.qeweb.scm.baseline.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.entity.WarnItemEntity;
import com.qeweb.scm.baseline.common.entity.WarnMainEntity;
import com.qeweb.scm.baseline.common.entity.WarnMessageEntity;
import com.qeweb.scm.baseline.common.repository.WarnItemDao;
import com.qeweb.scm.baseline.common.repository.WarnMainDao;
import com.qeweb.scm.baseline.common.repository.WarnMessageDao;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.entity.UserWarnRelEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.RoleDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.UserWarnRelService;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseGoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanItemDao;


/**
 *预警设置
 */
@Service
@Transactional
public class WarnMainService extends BaseService{

	@Autowired
	private WarnMainDao warnMainDao;
	
	@Autowired
	private WarnMessageDao warnMessageDao;
	
	@Autowired
	private WarnItemDao warnItemDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PurchaseOrderItemDao orderItemDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private IClassSystemService classService;
	
	@Autowired
	private PurchasePlanItemDao purchasePlanItemDao;
	
	@Autowired
	private UserWarnRelService userWarnRelService;
	
	
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseGoodsRequestDao purchaseGoodsRequestDao;
	
	@Autowired
	private PurchasePlanDao purchasePlanDao;


	public WarnMainEntity findOne(Long id){
		WarnMainEntity warnMain =  warnMainDao.findOne(id);
		if(warnMain.getIsVendor() == 1){
			warnMain.setIsVendorName("供应方");
		}else{
			warnMain.setIsVendorName("采购方");
		}
		return warnMain;
	}
	
	public void save(WarnMainEntity po){
		warnMainDao.save(po);
	}

	/**
	 * 业务操作完成后删除提醒
	 * @param billId
	 * @param billType
	 */
	
	public void delMessageByBillIdAndBillType(Long billId,String code,Long userId){
		WarnMainEntity warnMain = warnMainDao.findByCode(code);
		Long warnMainId = warnMain.getId();
		List<WarnMessageEntity> warnMessages = warnMessageDao.findByMainIdAndBillIdAndUserId(warnMainId,billId,userId);
		for(WarnMessageEntity warnMessage:warnMessages){
		warnMessage.setAbolished(1);
		warnMessageDao.save(warnMessage);
		}
	}
	
	
	 
	
	public void updateWarnMainWithEnableStatus(int enableStatus,long id){
		warnMainDao.updateWarnMainWithEnableStatus(enableStatus,id);
	}
	
	public Page<WarnMainEntity> getWarnMainList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WarnMainEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WarnMainEntity.class);
		return warnMainDao.findAll(spec,pagin);
	}
	public Page<WarnItemEntity> getItemListByMainId(int pageNumber, int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WarnItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WarnItemEntity.class);
		return warnItemDao.findAll(spec,pagin);
	}

	public WarnItemEntity findItemList(Long itemId) {
		
		return warnItemDao.findOne(itemId);
		
	}
	public void deletePromotion(Long id){
		//warnItemDao.delete(id);
		warnItemDao.abolish(id);
	}
	public void saveWarnItem(WarnItemEntity warnItem){
		warnItemDao.save(warnItem);
	}
	public void updateWarnItem(Long roleId,String warnContent,Long id){
		warnItemDao.updateWarnItem(roleId,warnContent,id);
	}
	public List<RoleEntity> getRoleName(){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(map);
		Specification<RoleEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), RoleEntity.class);
		return  roleDao.findAll(spec);
	}
	
	public Page<WarnMessageEntity> getWarnMessageList(int pageNumber, int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WarnMessageEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WarnMessageEntity.class);
		return warnMessageDao.findAll(spec,pagin);
	}
	public WarnMessageEntity findMessage(Long id){
		return warnMessageDao.findOne(id);
	}
	public WarnMainEntity getWarnMain(Long id){
		return warnMainDao.findOne(id);
	}
	
	//获取消息
	public List<WarnMessageEntity> getWarnInfoList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<WarnMessageEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), WarnMessageEntity.class);
		return warnMessageDao.findAll(spec);
	}
	
	//获取预警和晋级预警
	public List<WarnMessageEntity> getViewWarnList(Long userId) {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		return warnMessageDao.getViewWarnList(userId,curr);
	}
	
	public void closeMessage(Long warmId) {
		WarnMessageEntity warn = findMessage(warmId);
		warn.setIsRead(StatusConstant.STATUS_YES);
		warnMessageDao.save(warn);
	}
	
	/**
	 * 添加 消息 - 预警信息 - 晋级提醒
	 * @param billId
	 * @param code
	 */
/*	public void warnMessageCreate(Long billId, String key, List<Long> userIdList) {
		int i = 100;
		//处理keys
		String [] warnConstant=key.split("-");
		String  type= warnConstant[0];
		String  code = warnConstant[1];
		if(type.equals("PLAN")){
			i = 0;
		}else if(type.equals("ORDER")){
			i=1;
		}else if(type.equals("GOODS")){
			i=2;
		}else if(type.equals("ASN")){
			i=3;
		}else if(type.equals("DN")){
			i=4;
		}
		
		switch (i){ 
			case 0 : 
				warnMessagePlanSet(billId ,code , userIdList); //用于预测计划
				break; 
			case 1: 
				warnMessageOrderSet(billId, code , userIdList); //用于采购订单
				break; 
			case 2: 
				warnMessageGoodsSet(billId, code , userIdList); //用于要货计划
				break; 
			case 3: 
				warnMessageAsnSet(billId, code , userIdList); //用于asn
				break; 
			case 4: 
				warnMessageDnSet(billId, code , userIdList); //用于DN收货
				break; 
			case 100: //不做任何处理                                               
				break;
		} 
		
	}*/
	
	
	/**
	 * 消息 - 预警信息 - 晋级提醒
	 * @param billId
	 * @param code
	 */
	public void warnMessageSet(Long billId, String code, List<Long> userIdList) {
		List<WarnMessageEntity> warnMessageList = new  ArrayList<WarnMessageEntity>();
		Timestamp curr=DateUtil.getCurrentTimestamp();
		//查询预警主设置信息
		WarnMainEntity warnMain = warnMainDao.findByCode(code);
		
		//是否启用
		if(warnMain.getEnableStatus() == 1){
			String warnInfoMessage ="";
			String warnInfoTitle ="";
			//获取显示的信息
			warnInfoTitle = warnMain.getName();
			
			//获取设置的消息
			warnInfoMessage =getContentInfo(billId,code,warnMain);
		
			
			//生成消息提醒
			for (Long userId : userIdList) {
				WarnMessageEntity warnInfo = warnMessageDao.findByMainIdAndBillIdAndBillTypeAndUserId(warnMain.getId(),billId,WarnConstant.INFO,userId);
				if(warnInfo != null){
					//修改是否查看就好了
					warnInfo.setIsRead(0);
				}else{
					    warnInfo = new WarnMessageEntity();
						warnInfo.setBillId(billId);
						warnInfo.setBillType(WarnConstant.INFO);
						warnInfo.setWarnMainId(warnMain.getId());
						warnInfo.setWarnTitle(warnInfoTitle);
						warnInfo.setWarnMessage(warnInfoMessage);
						warnInfo.setIsRead(0);
						warnInfo.setUserId(userId);
						warnInfo.setAbolished(0);
						warnInfo.setIsPromotion(0);
						warnInfo.setIsOutTime(0);
						warnInfo.setWarnTime(curr);
				}
				warnMessageList.add(warnInfo);
			}
			
			//生成预警
			if(warnMain.getIsWarning() ==1){
				//消息内容			
				warnInfoMessage =getContentWarn(billId,code,warnMain);
				
				for (Long userId : userIdList) {
					WarnMessageEntity warnWarn = warnMessageDao.findByMainIdAndBillIdAndBillTypeAndUserId(warnMain.getId(),billId,WarnConstant.WARN,userId);
					//获取预警的显示时间
					Timestamp warnTime = classService.getWarnTimeByUserId(userId, Double.parseDouble(warnMain.getWarnTime()));
					
					if(warnWarn != null){
						//修改是否查看就好了
						warnWarn.setIsRead(0);
						warnWarn.setWarnTime(warnTime);//重新定义时间
					}else{
						warnWarn = new WarnMessageEntity(); 
						warnWarn.setBillId(billId);
						warnWarn.setBillType(WarnConstant.WARN);
						warnWarn.setWarnMainId(warnMain.getId());
						warnWarn.setWarnTitle(warnInfoTitle);
						warnWarn.setWarnMessage(warnInfoMessage);
						warnWarn.setIsRead(0);
						warnWarn.setUserId(userId);
						warnWarn.setAbolished(0);
						warnWarn.setIsPromotion(0);
						warnWarn.setIsOutTime(0);
						warnWarn.setWarnTime(warnTime);
					}
					warnMessageList.add(warnWarn);
				}
				
				//生成晋级提醒
				List<WarnItemEntity>   warnItemList  =  warnItemDao.findByWarnMainId(warnMain.getId());
				
				if(warnItemList.size() > 0){
					for (Long userId : userIdList) {
						//获取当前人员设置晋级通知的的人员的信息
						List<UserWarnRelEntity> userWarnRelList= userWarnRelService.getRelByUserId(userIdList.get(0));
						for(WarnItemEntity warnItem  :  warnItemList){
							for(UserWarnRelEntity userWarnRel :  userWarnRelList){
									if(warnItem.getRoleId() == userWarnRel.getRoleId() ){
										//添加信息
										warnInfoMessage =getContentWarnPromotion(billId,code,warnItem);
										
										WarnMessageEntity warnPromotinWarn = warnMessageDao.findByMainIdAndBillIdAndBillTypeAndUserId(warnMain.getId(),billId,WarnConstant.PROMOTIN_WARN,userId);
										//获取预警的显示时间
										Timestamp warnPromotinTime = classService.getWarnTimeByUserId(userWarnRel.getRoleUserId(), Double.parseDouble(warnMain.getWarnTime()));
										
										if(warnPromotinWarn != null){
											//修改是否查看就好了
											warnPromotinWarn.setIsRead(0);
											warnPromotinWarn.setWarnTime(warnPromotinTime);//重新定义时间
										}else{
											warnPromotinWarn = new WarnMessageEntity(); 
											warnPromotinWarn.setBillId(billId);
											warnPromotinWarn.setBillType(WarnConstant.PROMOTIN_WARN);
											warnPromotinWarn.setWarnMainId(warnMain.getId());
											warnPromotinWarn.setWarnTitle(warnInfoTitle);
											warnPromotinWarn.setWarnMessage(warnInfoMessage);
											warnPromotinWarn.setIsRead(0);
											warnPromotinWarn.setUserId(userWarnRel.getRoleUserId());
											warnPromotinWarn.setIsPromotion(1);
											warnPromotinWarn.setAbolished(0);
											warnPromotinWarn.setIsOutTime(0);
											warnPromotinWarn.setWarnTime(warnPromotinTime);
										}
										warnMessageList.add(warnPromotinWarn);
									}	
									break;
								}
							}
						}	
				}	
			}	
		}
		warnMessageDao.save(warnMessageList);
	}
	
	
	

	
	/**
	 * 若改提醒功能开启发送邮件功能，则发送邮件提醒
	 * @param warnMessage
	 * @param warnMain
	 */
	public void sendMail(WarnMessageEntity warnMessage,  
			WarnMainEntity warnMain) {

		MailObject mo = new MailObject();
		UserEntity user = userDao.findOne(warnMessage.getUserId());
		
		mo.toMail = user.getEmail();
		mo.templateName = "defaultTemp";
		Map<String, String> params = new HashMap<String, String>();
		mo.title = warnMessage.getWarnTitle();
		params.put("tempMessage", warnMessage.getWarnMessage());
		mo.params = params;
		mailSendService.send(mo, 2);
	}
	
	
	/**
	 * 获取设置的消息的文本
	 * @param warnMessage
	 * @param warnMain
	 */
	public String getContentInfo(Long billId, String code,WarnMainEntity warnMain) {
       String infoText = "";
       
       //采购订单
       if(code.indexOf("ORDER")  >=1  ){
    	   //查询订单
    	   PurchaseOrderEntity order = purchaseOrderDao.findById(billId);
    	   infoText = warnMain.getContent().replace("{orderCode}", order.getOrderCode());
    	   return infoText;
       }
       if(code.indexOf("PLAN")  >=1  ){
    	   //查询预测计划
    	   PurchasePlanEntity plan = purchasePlanDao.findOne(billId);
    	   infoText = warnMain.getWarnContent().replace("{VersionNum}", plan.getMonth());
    	   return infoText;
       }
       if(code.indexOf("GOODS")  >=1  ){
    	   //查询要货计划
    	   PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findById(billId);
    	   infoText = warnMain.getWarnContent().replace("{FactoryCode}", goods.getFactory().getCode());
    	   infoText = warnMain.getWarnContent().replace("{GroupCode}", goods.getGroup().getCode());
    	   infoText = warnMain.getWarnContent().replace("{MaterialCode}", goods.getMaterial().getCode());
    	   infoText = warnMain.getWarnContent().replace("{Date}", goods.getRq().toString());
    	   return infoText;
       }
       
		return infoText;
	}
	
	
	/**
	 * 获取设置的预警的文本
	 * @param warnMessage
	 * @param warnMain
	 */
	public String getContentWarn(Long billId, String code,WarnMainEntity warnMain) {
       String infoText = "";
       //采购订单
       if(code.indexOf("ORDER")  != -1  ){
    	   //查询订单
    	   PurchaseOrderEntity order = purchaseOrderDao.findById(billId);
    	   infoText = warnMain.getWarnContent().replace("{orderCode}", order.getOrderCode());
    	   return infoText;
       }
       //预测计划
       if(code.indexOf("PLAN")  != -1  ){
    	   //查询预测计划
    	   PurchasePlanEntity plan = purchasePlanDao.findOne(billId);
    	   infoText = warnMain.getWarnContent().replace("{VersionNum}", plan.getMonth());
    	   return infoText;
       }
       //要货计划
       if(code.indexOf("GOODS")  != -1  ){
    	   //查询要货计划
    	   PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findById(billId);
    	   infoText = warnMain.getWarnContent().replace("{FactoryCode}", goods.getFactory().getCode());
    	   infoText = warnMain.getWarnContent().replace("{GroupCode}", goods.getGroup().getCode());
    	   infoText = warnMain.getWarnContent().replace("{MaterialCode}", goods.getMaterial().getCode());
    	   infoText = warnMain.getWarnContent().replace("{Date}", goods.getRq().toString());
    	   return infoText;
       }
       
		return infoText;
	}
	
	/**
	 * 获取设置的预警的文本
	 * @param warnMessage
	 * @param warnMain
	 */
	public String getContentWarnPromotion(Long billId, String code,WarnItemEntity WarnItem) {
       String infoText = "";
       //采购订单
       if(code.indexOf("ORDER")  != -1  ){
    	   //查询订单
    	   PurchaseOrderEntity order = purchaseOrderDao.findById(billId);
    	   infoText = WarnItem.getWarnContent().replace("{orderCode}", order.getOrderCode());
    	   return infoText;
       }
       //预测计划
       if(code.indexOf("PLAN")  != -1  ){
    	   //查询预测计划
    	   PurchasePlanEntity plan = purchasePlanDao.findOne(billId);
    	   infoText = WarnItem.getWarnContent().replace("{VersionNum}", plan.getMonth());
    	   return infoText;
       }
       //要货计划
       if(code.indexOf("GOODS")  != -1  ){
    	   //查询要货计划
    	   PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findById(billId);
    	   infoText = WarnItem.getWarnContent().replace("{FactoryCode}", goods.getFactory().getCode());
    	   infoText = WarnItem.getWarnContent().replace("{GroupCode}", goods.getGroup().getCode());
    	   infoText = WarnItem.getWarnContent().replace("{MaterialCode}", goods.getMaterial().getCode());
    	   infoText = WarnItem.getWarnContent().replace("{Date}", goods.getRq().toString());
    	   return infoText;
       }
		return infoText;
	}
	
	
}
