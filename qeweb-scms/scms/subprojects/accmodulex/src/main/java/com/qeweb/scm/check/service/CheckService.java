package com.qeweb.scm.check.service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.repository.BaseDao;
import com.qeweb.scm.basemodule.context.MessageSourceUtil;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.TimetaskSettingEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.TimetaskSettingDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.ResultUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.check.entity.CheckEntity;
import com.qeweb.scm.check.entity.CheckExceptionEntity;
import com.qeweb.scm.check.entity.CheckInvoiceEntity;
import com.qeweb.scm.check.entity.CheckItemEntity;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.repository.CheckDao;
import com.qeweb.scm.check.repository.CheckExceptionDao;
import com.qeweb.scm.check.repository.CheckInvoiceDao;
import com.qeweb.scm.check.repository.CheckItemDao;
import com.qeweb.scm.purchasemodule.constants.CheckStatusConstant;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.InStorageItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.CheckItemInBuyerTransfer;
import com.qeweb.scm.purchasemodule.web.vo.CheckItemInVendorTransfer;
@Service
@Transactional(rollbackOn=Exception.class)
public class CheckService{
	
	@Autowired
	private CheckDao checkDao;
	
	@Autowired
	private CheckItemDao checkItemDao;
	
	@Autowired
	private CheckExceptionDao checkExDao;
	
	@Autowired
	private CheckInvoiceDao checkInvoiceDao;

	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TimetaskSettingDao timetaskSettingDao;
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private InStorageItemDao inStorageItemDao;
	
	@Autowired
	private ReceiveItemDao receiveItemDao;
	
	@Autowired
	private PurchaseOrderItemDao retItemDao;
	
	@Autowired
	NoCheckedItemsService noCheckedItemsService;
	
	public CheckEntity getCheckById(Long id) {
		return checkDao.findOne(id);
	}
	
	public CheckItemEntity getCheckItemById(Long id) {
		if(checkDao.findCheckItemById(id).size()>0){
			return checkDao.findCheckItemById(id).get(0);
		}
		return null;
	}
	
	public CheckExceptionEntity getCheckExById(Long id) {
		return checkExDao.findOne(id);
	}
	
	public Page<CheckEntity> getChecks(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CheckEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), CheckEntity.class);
		return checkDao.findAll(spec,pagin);
	}
	
	public Page<CheckExceptionEntity> getExceptions(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse_(searchParamMap);
		Specification<CheckExceptionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), CheckExceptionEntity.class);
		//return checkExDao.findAll(pagin);
		return checkExDao.findAll(spec,pagin);
	}
	
	public List<CheckItemEntity> getCheckItemsByCheckId(Long id){
		List<CheckItemEntity> list = checkDao.findItemsByCheckId(id);
		return list;
	}
	
	public CheckItemEntity getCheckItemsByCheckItemId(Long checkItemId){
		CheckItemEntity checkItem = checkItemDao.findById(checkItemId);
		return checkItem;
	}
	
	public List<CheckInvoiceEntity> getInvoicesByCheckId(Long id){
		List<CheckInvoiceEntity> list = checkDao.getInvoicesByCheckId(id);
		return list;
	}
	
	public void publish(Long id,String claimDescription,Double claimAmount, HttpServletRequest request){
		CheckEntity check = getCheckById(id);
		check.setPublishStatus(1);
		check.setPublishTime(DateUtil.getCurrentTimestamp());
		check.setClaimAmount(claimAmount);
		check.setClaimDescription(claimDescription);
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		check.setPublishUser(new UserEntity(user.id));
		baseDao.update(check);
		
		//add by zhangjiejun 2016.03.25 start
		//采方已发布，邮件提醒供方查看
		OrganizationEntity vendor = check.getVendor();
		if(vendor!=null){
			String toEmail = vendor.getEmail();
			String toPerson = vendor.getName();
			if(CommonUtil.isNotNullAndNotEmpty(toEmail) && CommonUtil.isNotNullAndNotEmpty(toPerson)){
				String title = MessageSourceUtil.getMessage("message.check.remind.title", request);
				String msg = MessageSourceUtil.getMessage("message.check.buyer.publish", request) + " " + 
				MessageFormat.format(MessageSourceUtil.getMessage("message.check.remind.msg", request), new Object[]{check.getCode()});
				CommonUtil.sendMail_LG_request(toEmail, toPerson, title, msg, request);
			}
		}
		//add by zhangjiejun 2016.03.25 end
	}

	public void editClaimDescription(Long id, String claimDescription) {
		CheckEntity check = getCheckById(id);
		check.setClaimDescription(claimDescription);
		baseDao.update(check);
	}
	
	public void editClaimAmount(Long id, Double claimAmount) {
		CheckEntity check = getCheckById(id);
		check.setClaimAmount(claimAmount);
		baseDao.update(check);
	}
	
	public void vConfirm(JSONArray array,Long id, HttpServletRequest request) {
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity user = baseDao.findOne(UserEntity.class,suser.id);
		CheckEntity check = getCheckById(id);
		boolean exStatus = true;
		for(int i= 0; i < array.size(); i ++) {
			JSONObject object = array.getJSONObject(i);
			Integer pk = (Integer)object.get("id");
			CheckItemEntity item = baseDao.findOne(CheckItemEntity.class,(long)pk);
			item.setLastUpdateTime(current);
			item.setUpdateUserId(suser.id);
			item.setUpdateUserName(user.getName());
			Double vendorCheckPrice = Double.valueOf(object.get("vendorCheckPrice").toString());
			
			NoCheckItemsEntity  entity  = noCheckedItemsService.getByItemId(item.getViewNoCheckItemId());
			double receiveQty = 0;
			if(vendorCheckPrice!=null){
				item.setVendorCheckPrice(vendorCheckPrice);
				if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==item.getSource()){//收货
					receiveQty = entity.getRecQty();
					//税金四舍五入保留2位小数
					BigDecimal count = new BigDecimal(BigDecimalUtil.mul(vendorCheckPrice, receiveQty, 0.17d));
					item.setCol1(StringUtils.convertToString(count.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
				}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==item.getSource()){//退货
					receiveQty = entity.getOrderQty();
					//税金四舍五入保留3位小数
					BigDecimal count = new BigDecimal(BigDecimalUtil.mul(vendorCheckPrice, receiveQty, 0.17d));
					item.setCol1(StringUtils.convertToString(count.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
				}
			}
			baseDao.save(item);
			
			double d_attr3 = null==item.getCheckPrice()?0:item.getCheckPrice();
			
			if(item.getVendorCheckPrice() != d_attr3){				//供应商填写单价不等于核价单价
				item.setExStatus(CheckStatusConstant.EX_YES);
				check.setExStatus(CheckStatusConstant.EX_YES);
				baseDao.update(item);
				CheckExceptionEntity cee = new CheckExceptionEntity();
				cee.setCheckItem(item);
				cee.setAbolished(0);
				cee.setCreateTime(current);
				cee.setCreateUserId(-1l);
				cee.setCreateUserName("系统");
				cee.setLastUpdateTime(current);
				cee.setUpdateUserId(-1l);
				cee.setUpdateUserName("系统");
				cee.setIsOutData(1);
				baseDao.save(cee);
				exStatus = false;
			}
		}
		check.setvConfirmStatus(CheckStatusConstant.CONFIRM_YES);
		check.setvConfirmUser(user);
		check.setvConfirmTime(current);
		if(!exStatus){				
			check.setExStatus(1);		
		}else{
			check.setExStatus(0);		//供应商填写的价格与核价价格一致时，【异常状态】应该显示为没有异常
		}
		baseDao.update(check);
		
		//add by zhangjiejun 2016.03.25 start
		//供方确认对账单，邮件发给采方
//		CheckItemEntity checkItem = check.getCheckItem().iterator().next();
//		if(CommonUtil.isNotNullAndNotEmpty(checkItem)){
//			ReceiveItemEntity recItem = checkItem.getRecItem();
//			if(CommonUtil.isNotNullAndNotEmpty(recItem)){
//				PurchaseOrderItemEntity orderItem = recItem.getOrderItem();
//				if(CommonUtil.isNotNullAndNotEmpty(orderItem)){
//					String col4 = orderItem.getCol4();							//获取登录用户名称
//					if(CommonUtil.isNotNullAndNotEmpty(col4)){
//						UserEntity user_buyer = userDao.findByLoginName(col4);	//获取登录用户对象
//						if(CommonUtil.isNotNullAndNotEmpty(user_buyer)){
//							String email = user_buyer.getEmail();
//							if(CommonUtil.isNotNullAndNotEmpty(email)){			//存在邮箱，则发送给用户
//								String toEmail = email;
//								String toPerson = col4;
//								if(CommonUtil.isNotNullAndNotEmpty(toEmail) && CommonUtil.isNotNullAndNotEmpty(toPerson)){
//									String title = MessageSourceUtil.getMessage("message.check.remind.title", request);
////									String msg = MessageSourceUtil.getMessage("message.check.remind.msg", request);
//									String msg = MessageSourceUtil.getMessage("message.check.vendor.confirm", request) + " " + 
//									MessageFormat.format(MessageSourceUtil.getMessage("message.check.remind.msg", request), new Object[]{check.getCode()});
//									CommonUtil.sendMail_LG_request(toEmail, toPerson, title, msg, request);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		//add by zhangjiejun 2016.03.25 start
		
	}

	public void batchModifyPrice(JSONArray array) {
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity user = baseDao.findOne(UserEntity.class,suser.id);
		for(int i= 0; i < array.size(); i ++) {
			JSONObject object = array.getJSONObject(i);
			Integer pk = (Integer)object.get("id");
			CheckExceptionEntity ex = checkExDao.findOne((long)pk);
			CheckItemEntity item = ex.getCheckItem();
			if(object.get("buyerCheckPrice").equals("null")||(item.getBuyerCheckPrice()!=null&&item.getBuyerCheckPrice()==Double.valueOf(object.get("buyerCheckPrice").toString())
					&&object.get("exDiscription")!=null&&object.get("exDiscription").toString().equals(item.getExDiscription()))){
				continue;
			}
			item.setBuyerCheckPrice(Double.valueOf(object.get("buyerCheckPrice").toString()));
			item.setExDiscription(object.get("exDiscription").equals("null")?"":object.get("exDiscription").toString());
			item.setLastUpdateTime(current);
			item.setUpdateUserId(suser.id);
			item.setUpdateUserName(user.getName());
			baseDao.update(item);
		}
	}

	public void confirmModifyPrice(List<CheckExceptionEntity> exList) throws Exception{
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity user = baseDao.findOne(UserEntity.class,suser.id);
		CheckEntity check = checkDao.findOne(exList.get(0).getCheckItem().getCheck().getId());
		for(int i= 0; i < exList.size(); i ++) {
			CheckExceptionEntity ex = exList.get(i);
			CheckItemEntity item = ex.getCheckItem();
			if(item.getBuyerCheckPrice()==null){
				throw new Exception("请先填写核对价格。");
				//continue;
			}
			if(item.getExDealStatus()==CheckStatusConstant.EX_DEAL_NO){
				item.setExDealStatus(CheckStatusConstant.EX_DEAL_YES);
				item.setLastUpdateTime(current);
				item.setUpdateUserId(suser.id);
				item.setUpdateUserName(user.getName());
				baseDao.update(item);
			}else{
				throw new RuntimeException("包含已确认的数据，不能重复确认。");
			}
		}
		int count = checkDao.findNoDealItemsByCheckId(check.getId());
		if(count==0){
			check.setExDealStatus(CheckStatusConstant.EX_DEAL_YES);
			baseDao.update(check);
		}
	}

	public void confirmEx(List<CheckExceptionEntity> exList) {
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		CheckEntity check = checkDao.findOne(exList.get(0).getCheckItem().getCheck().getId());
		UserEntity user = baseDao.findOne(UserEntity.class,suser.id);
		for(int i= 0; i < exList.size(); i ++) {
			CheckExceptionEntity ex = exList.get(i);
			CheckItemEntity item = checkItemDao.findById(ex.getCheckItem().getId());
			if(item.getExConfirmStatus()==CheckStatusConstant.CONFIRM_NO){
				item.setExConfirmStatus(CheckStatusConstant.CONFIRM_YES);
				item.setLastUpdateTime(current);
				item.setUpdateUserId(suser.id);
				item.setUpdateUserName(user.getName());
				baseDao.update(item);
			}
		}
		int count = checkDao.findNoConfirmItemsByCheckId(check.getId());
		if(count==0){
			check.setExConfirmStatus(CheckStatusConstant.CONFIRM_YES);
			baseDao.update(check);
		}
		
	}
	
	/**
	 * 开票
	 * @param checkInvoice		发票对象
	 * @param items				明细集合
	 * @param claimBillFlag		索赔勾选状态
	 * @param isAbroad			是否国外
	 * @throws Exception 
	 */
	public void bill(CheckInvoiceEntity checkInvoice, List<CheckItemEntity> items, boolean claimBillFlag, boolean isAbroad) throws Exception {
		if(CollectionUtils.isEmpty(items))
			return;
//		checkInvoice.setCheck(items.get(0).getCheck()); 
		CheckEntity check = checkInvoice.getCheck();
		check = checkDao.findOne(check.getId());
		checkInvoice.setTaxRate(0.17);		//税率
		double tax = BigDecimalUtil.mul(0.17d, checkInvoice.getNoTaxAmount());
		checkInvoice.setTax(tax);			//税金
//		checkInvoice.setTaxAmount(tax); 	//税后单价
		checkInvoice.setTaxAmount(BigDecimalUtil.add(tax, checkInvoice.getNoTaxAmount())); 	//税后单价改名含税金额
		if(!isAbroad && claimBillFlag){		//非国外，且勾选了索赔金额开票
			checkInvoice.setCol2("1");		//索赔金额开票状态(1为已开索赔金额发票)
			if(check!=null){
				check.setCol2("1");			//索赔金额开票状态(1为已开索赔金额发票)
				checkDao.save(check);
//				new CheckUtil(Lists.newArrayList(check), claimBillFlag).exec();
			}
		}
		baseDao.save(checkInvoice);
		
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity user = baseDao.findOne(UserEntity.class,suser.id);
//		Set<Long> checkIdSet = new HashSet<Long>();
		for(CheckItemEntity item : items) {
//			checkIdSet.add(item.getCheck().getId());
			item.setInvoice(checkInvoice);
			item.setLastUpdateTime(current);
			item.setUpdateUserId(suser.id);
			item.setUpdateUserName(user.getName());
			baseDao.update(item);
		}
//		updateCheckBillStatus(checkIdSet);
		
//		if(CommonUtil.isNotNullAndNotEmpty(check)){
//			Set<CheckItemEntity> itemSet = check.getCheckItem();
//			boolean flag = true;
//			for (CheckItemEntity item : itemSet) {
//				if(item.getInvoice() == null) {
//					flag = false;
//					break;
//				}
//			}
//			if(flag) {															//发票开完了，自动变成已开票状态
//				check.setBillStatus(CheckStatusConstant.BILL_YES);
//				check.setInvoiceReceiveStatus(CheckStatusConstant.BILL_NO);		//添加这个状态，采方即可确认或者驳回发票
//				checkDao.save(check);
//			} 
//		}
	}

	/**
	 * 更新主单开票状态
	 * @param checkIdSet
	 */
	private void updateCheckBillStatus(Set<Long> checkIdSet) {
		CheckEntity check = null;
		List<CheckItemEntity> itemSet = null;
		boolean flag = false;
		for(Long id : checkIdSet) {
			check = checkDao.findOne(id);
//			itemSet = check.getCheckItem(); 
			itemSet = checkDao.findItemsByCheckId(id);
			for(CheckItemEntity item : itemSet) {
				if(item.getInvoice() == null) {
					flag = true;
					break;
				}
			}
			if(!flag) {															//发票开完了，自动变成已开票状态
				check.setBillStatus(CheckStatusConstant.BILL_YES);
				check.setInvoiceReceiveStatus(CheckStatusConstant.BILL_NO);		//添加这个状态，采方即可确认或者驳回发票
				checkDao.save(check);
			} 
			flag = false;
		}
	}

	public void confirmInvoice(List<CheckInvoiceEntity> invoiceList) {
		for(int i= 0; i < invoiceList.size(); i ++) {
			CheckInvoiceEntity invoice = invoiceList.get(i);
			invoice.setReceiveStatus(1);
			baseDao.update(invoice);
		}
	}

	public void closeCheck(List<CheckEntity> checkList) {
		for(CheckEntity check:checkList){
			check.setCloseStatus(CheckStatusConstant.CLOSE_YES);
			baseDao.update(check);
		}
	}

	public void bConfirm(Long id,String claimDescription,Double claimAmount, HttpServletRequest request) {
		CheckEntity check = checkDao.findById(id);
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		check.setbConfirmStatus(CheckStatusConstant.CONFIRM_YES);
		check.setbConfirmTime(DateUtil.getCurrentTimestamp());
		check.setbConfirmUser(new UserEntity(suser.id));
		if(claimAmount != null)
			check.setClaimAmount(claimAmount);
		if(StringUtils.isNotEmpty(claimDescription))
			check.setClaimDescription(claimDescription);
		checkDao.save(check);
		
		//add by zhangjiejun 2016.03.28 start
		//采方已确认，邮件提醒供方查看
		OrganizationEntity vendor = check.getVendor();
//		if(CommonUtil.isNotNullAndNotEmpty(vendor)){
//			String toEmail = vendor.getEmail();
//			String toPerson = vendor.getName();
//			if(CommonUtil.isNotNullAndNotEmpty(toEmail) && CommonUtil.isNotNullAndNotEmpty(toPerson)){
//				String title = MessageSourceUtil.getMessage("message.check.remind.title", request);
////				String msg = MessageSourceUtil.getMessage("message.check.remind.msg", request);
//				String msg = MessageSourceUtil.getMessage("message.check.buyer.confirm", request) + " " + 
//				MessageFormat.format(MessageSourceUtil.getMessage("message.check.remind.msg", request), new Object[]{check.getCode()});
//				CommonUtil.sendMail_LG_request(toEmail, toPerson, title, msg, request);
//			}
//		}
		//add by zhangjiejun 2016.03.28 end
	}

	public void confirmInvoice(Long id, UserEntity userEntity) throws Exception {
		CheckEntity check = checkDao.findOne(id);
		check.setInvoiceReceiveStatus(CheckStatusConstant.CONFIRM_YES);
		
		//modify by zhangjiejun 2015.11.04 start
		check.setReviewStatus(CheckStatusConstant.REVIEW_YES);
		check.setReviewTime(DateUtil.getCurrentTimestamp());
		check.setReviewUser(userEntity);
		//modify by zhangjiejun 2015.11.04 end
		checkDao.save(check);
		//接口同步
//		CheckUtil.execute(Lists.newArrayList(check));
//		new CheckUtil(Lists.newArrayList(check)).exec();
//		new CheckUtil(Lists.newArrayList(check), true).exec();
	}
	
	public void rejectInvoice(Long id,String billRejectReason, UserEntity userEntity) {
		CheckEntity check = checkDao.findOne(id);
		check.setInvoiceReceiveStatus(CheckStatusConstant.REJECT_YES);
		check.setBillStatus(CheckStatusConstant.BILL_NO);
		check.setBillRejectReason(billRejectReason);
		
		//modify by zhangjiejun 2015.11.04 start
		check.setReviewStatus(CheckStatusConstant.REVIEW_NO);
		check.setReviewTime(DateUtil.getCurrentTimestamp());
		check.setReviewUser(userEntity);
		//modify by zhangjiejun 2015.11.04 end
		checkDao.save(check);
	}

	public void billOk(JSONArray array, Long id) {
		CheckEntity check = checkDao.findOne(id);
		check.setInvoiceReceiveStatus(CheckStatusConstant.CONFIRM_NO);
		check.setBillStatus(CheckStatusConstant.BILL_YES);
		checkDao.save(check);
		
		CheckItemEntity item = null;
		JSONObject object = null;
		for(int i= 0; i < array.size(); i ++) {
			object = array.getJSONObject(i);
			item = checkItemDao.findById(StringUtils.convertLong(StringUtils.convertToString(object.get("id"))));
			item.setCol1(StringUtils.convertToString(object.get("col1")));
			checkItemDao.save(item);
		}
	}

	public void delInvoice(Long id) {
		CheckInvoiceEntity cie = baseDao.findOne(CheckInvoiceEntity.class, id);
		List<CheckItemEntity> items = checkDao.getItemsByInvoiceId(id);
		for(CheckItemEntity item:items){
			item.setInvoice(null);
		}
		baseDao.save(items);
		String col2 = cie.getCol2();
		if(!StringUtils.isEmpty(col2) && col2.equals("1")){//存在索赔状态，则重置
			CheckEntity check = cie.getCheck();
			if(check!=null){
				check.setCol2("");
				baseDao.save(check);
			}
		}
		baseDao.delete(cie);
	}

	public CheckInvoiceEntity getInvoicesById(Long id) {
		return baseDao.findOne(CheckInvoiceEntity.class, id);
	}

	public void saveInvoice(CheckInvoiceEntity invoice, Long id) {
		CheckInvoiceEntity invoice_ = baseDao.findOne(CheckInvoiceEntity.class, id);
		invoice_.setBillTime(invoice.getBillTime());
		invoice_.setCode(invoice.getCode());
		invoice_.setNoTaxAmount(invoice.getNoTaxAmount());
		invoice_.setTax(BigDecimalUtil.mul(0.17d, invoice_.getNoTaxAmount()));
		invoice_.setTaxAmount(BigDecimalUtil.mul(1.17d, invoice_.getNoTaxAmount())); 
		invoice_.setCol1(invoice.getCol1());
		String fileName = invoice.getInvoiceFileName();
		if(fileName != null){
			invoice_.setInvoiceFileName(fileName);
			invoice_.setInvoiceFilePath(invoice.getInvoiceFilePath());
		}
		baseDao.save(invoice_);
	}

	public void dealEx(CheckEntity check) throws Exception {
		CheckEntity checkEntity = checkDao.findOne(check.getId());
		checkEntity.setCol1(check.getCol1()); 
		Set<CheckItemEntity> set = checkEntity.getCheckItem();
		double allCheckPrice = 0;														//核价总金额
		if(!CollectionUtils.isEmpty(set)) {
			for (CheckItemEntity checkItemEntity : set) {
				if(checkItemEntity != null){
					ReceiveItemEntity recItem =checkItemEntity.getRecItem();
					double receiveQty = recItem.getReceiveQty();						//国外获取的是收货明细的收货数量
					String attr3 = recItem.getAttr3();									//核价价格
					Double checkPrice = 0.0;
					if(attr3 != null && !attr3.equals("")){
						checkPrice = Double.parseDouble(recItem.getAttr3());			//核价价格
					}
					allCheckPrice = BigDecimalUtil.add(allCheckPrice, BigDecimalUtil.mul(checkPrice, receiveQty));		//核价价格 * 数量
				}
			}
		}
		if(StringUtils.convertToDouble(check.getCol1()).doubleValue() != BigDecimalUtil.sub(checkEntity.getBillAmount(), allCheckPrice)) {
			throw new Exception("差价必须等于发票总金额-核价总金额，请重新输入");
		}
		checkEntity.setBillStatus(0);
		checkEntity.setExDealStatus(CheckStatusConstant.EX_DEAL_YES);
		checkDao.save(checkEntity);
		//同步接口
//		CheckUtil.execute(Lists.newArrayList(checkEntity));
//		new CheckUtil(Lists.newArrayList(checkEntity)).exec();
	}

	//add by zhangjiejun 2015.10.16 start
	/**
	 * 修改对账单为发货状态
	 * @param checkList	对账单集合
	 */
	public void publishCheck(List<CheckEntity> checkList) {
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for(CheckEntity check : checkList){
			check.setPublishStatus(CheckStatusConstant.PUBLISH_YES);
			check.setPublishTime(DateUtil.getCurrentTimestamp());
			check.setPublishUser(new UserEntity(user.id));
			baseDao.update(check);
		}
	}
	//add by zhangjiejun 2015.10.16 end

	//add by zhangjiejun 2015.11.04 start
	/**
	 * 确认核对异常管理数据
	 * @param 	array	异常管理数据
	 */
	public void confirmCheckPrice(JSONArray array) {
		Timestamp current = DateUtil.getCurrentTimestamp();
		ShiroUser suser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		UserEntity user = baseDao.findOne(UserEntity.class, suser.id);
		List<CheckItemEntity> checkItemList = new ArrayList<CheckItemEntity>();
		for(int i= 0; i < array.size(); i ++) {
			JSONObject object = array.getJSONObject(i);
			Integer pk = (Integer)object.get("id");
			@SuppressWarnings("unchecked")
			Map<String,Object> map= (Map<String, Object>) object.get("checkItem");
			CheckExceptionEntity ex = checkExDao.findOne((long)pk);
			CheckItemEntity item = ex.getCheckItem();
			if(object.get("buyerCheckPrice").equals("null")||(item.getBuyerCheckPrice()!=null&&item.getBuyerCheckPrice()==Double.valueOf(object.get("buyerCheckPrice").toString())
					&&object.get("exDiscription")!=null&&object.get("exDiscription").toString().equals(item.getExDiscription()))){
				continue;
			}
			item.setBuyerCheckPrice(Double.valueOf(object.get("buyerCheckPrice").toString()));
			item.setExDiscription(object.get("exDiscription").equals("null")?"":object.get("exDiscription").toString());
			item.setLastUpdateTime(current);
			item.setUpdateUserId(suser.id);
			item.setUpdateUserName(user.getName());
			item.setExDealStatus(CheckStatusConstant.EX_DEAL_YES);
			
			
			NoCheckItemsEntity entity =noCheckedItemsService.getByItemId(Long.valueOf(map.get("viewNoCheckItemId").toString()));
			double receiveQty = 0;
			//重新计算税金
			if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==item.getSource()){
				receiveQty = entity.getRecQty();
				//税金四舍五入保留3位小数
				BigDecimal count = new BigDecimal(BigDecimalUtil.mul(item.getBuyerCheckPrice(), receiveQty, 0.17d));
				item.setCol1(StringUtils.convertToString(count.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==item.getSource()){
				receiveQty = entity.getOrderQty();
				//税金四舍五入保留3位小数
				BigDecimal count = new BigDecimal(BigDecimalUtil.mul(item.getBuyerCheckPrice(), receiveQty, 0.17d));
				item.setCol1(StringUtils.convertToString(count.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			item.setMaterialCode(entity.getMaterialCode());
			item.setMaterialName(entity.getMaterialName());
			item.setUnitName(entity.getUnitName());
			item.setCheckPrice(entity.getPrice());
			checkItemList.add(item);
		}
		if(checkItemList != null && checkItemList.size() != 0){
			baseDao.update(checkItemList);
			CheckEntity check = checkDao.findOne(checkItemList.get(0).getCheck().getId());
			int count = checkDao.findNoDealItemsByCheckId(check.getId());
			if(count==0){
				check.setExDealStatus(CheckStatusConstant.EX_DEAL_YES);
				baseDao.update(check);
			}
		}
	}
	//add by zhangjiejun 2015.11.04 end
	
	/**
	 * 根据发票号查找到对账单明细
	 * @param invoiceCode
	 * @return
	 */
	public List<CheckItemEntity> getCheckItemsByInvoiceCode(String invoiceCode){
		List<CheckItemEntity> list = checkDao.getItemsByInvoiceCode(invoiceCode);
		return list;
	}

	/**
	 * 索赔金额开票
	 * @param 	checkInvoice 发票信息
	 */
	public void addClaimBill(CheckInvoiceEntity checkInvoice) {
		checkInvoice.setTaxRate(0.17);
		checkInvoice.setTax(BigDecimalUtil.mul(0.17d, checkInvoice.getNoTaxAmount()));
		checkInvoice.setTaxAmount(BigDecimalUtil.mul(1.17d, checkInvoice.getNoTaxAmount()));
		checkInvoice.setCol2("1");
		baseDao.save(checkInvoice);
		CheckEntity check = checkInvoice.getCheck();
		if(check != null){
			long id = check.getId();
			check = checkDao.findOne(id);
			check.setCol2("1");
			checkDao.save(check);
		}
	}


	/**
	 * 国内、外协 供应商
	 * @param list	对账明细集合
	 * @return		国内、外协 供应商导出集合
	 */
	public List<CheckItemInVendorTransfer> initCheckItemInVendorTransfer(List<CheckItemEntity> list) {
		List<CheckItemInVendorTransfer> checkItemInVendorTransferList = new ArrayList<CheckItemInVendorTransfer>();
		CheckItemInVendorTransfer transfer = null;
		CheckInvoiceEntity invoice = null;
		String invoiceCode = "";
		for (CheckItemEntity checkItem : list) {
			transfer = new CheckItemInVendorTransfer();
			invoice = checkItem.getInvoice();
			
			transfer.setQadCode(checkItem.getQadCode());
			transfer.setReceiveTime(checkItem.getCreateTime() + "");
			transfer.setReceiveQty(checkItem.getReceiveQty() + "");
			if(CommonUtil.isNotNullAndNotEmpty(invoice)){
				invoiceCode = invoice.getCode();
			}
			else
			{
				invoiceCode="";
			}
			transfer.setInvoiceCode(invoiceCode);
			transfer.setMaterialCode(checkItem.getMaterialCode());
			transfer.setMaterialName(checkItem.getMaterialName());
			transfer.setUnitName(checkItem.getUnitName());
			Double vendorCheckPrice = checkItem.getVendorCheckPrice();
			if(CommonUtil.isNotNullAndNotEmpty(vendorCheckPrice)){
				transfer.setVendorCheckPrice(vendorCheckPrice + "");
			}
			Double buyerCheckPrice = checkItem.getBuyerCheckPrice();
			if(CommonUtil.isNotNullAndNotEmpty(buyerCheckPrice)){
				transfer.setBuyerCheckPrice(buyerCheckPrice + "");
			}
			transfer.setCol1(checkItem.getCol1());
			transfer.setExStatus(checkItem.getExStatus() == 1 ? "√" : "×");
			transfer.setExDiscription(checkItem.getExDiscription());
			transfer.setExDealStatus(checkItem.getExDealStatus() == 1 ? "√" : "×");
			String exConfirmStatus = CommonUtil.initExConfirmStatus(checkItem.getExConfirmStatus());
			transfer.setExConfirmStatus(exConfirmStatus);
			checkItemInVendorTransferList.add(transfer);
		}
		return checkItemInVendorTransferList;
	}

	/**
	 * 国内、外协 采购商
	 * @param list	对账明细集合
	 * @return		国内、外协 采购商导出集合
	 */
	public List<CheckItemInBuyerTransfer> initCheckItemInBuyerTransfer(List<CheckItemEntity> list) {
		List<CheckItemInBuyerTransfer> checkItemInBuyerTransferList = new ArrayList<CheckItemInBuyerTransfer>();
		CheckItemInBuyerTransfer transfer = null;
		CheckInvoiceEntity invoice = null;
		String invoiceCode = "";
		for (CheckItemEntity checkItem : list) {
			transfer = new CheckItemInBuyerTransfer();
			invoice = checkItem.getInvoice();
			
			transfer.setReceiveTime(checkItem.getCreateTime() + "");
			transfer.setQadCode(checkItem.getQadCode());
			transfer.setDeliveryCode(checkItem.getDeliveryCode());
			if(CommonUtil.isNotNullAndNotEmpty(invoice)){
				invoiceCode = invoice.getCode();
			}
			transfer.setInvoiceCode(invoiceCode);
			transfer.setMaterialCode(checkItem.getMaterialCode());
			transfer.setReceiveQty(checkItem.getReceiveQty() + "");
			transfer.setUnitName(checkItem.getUnitName());
			
			Double vendorCheckPrice = checkItem.getVendorCheckPrice();
			if(CommonUtil.isNotNullAndNotEmpty(vendorCheckPrice)){
				transfer.setVendorCheckPrice(vendorCheckPrice + "");
			}
			Double buyerCheckPrice = checkItem.getBuyerCheckPrice();
			if(CommonUtil.isNotNullAndNotEmpty(buyerCheckPrice)){
				transfer.setBuyerCheckPrice(buyerCheckPrice + "");
			}
			
			transfer.setCol1(checkItem.getCol1());
			transfer.setExStatus(checkItem.getExStatus() == 1 ? "√" : "×");
			transfer.setExDiscription(checkItem.getExDiscription());
			transfer.setExDealStatus(checkItem.getExDealStatus() == 1 ? "√" : "×");
			String exConfirmStatus = CommonUtil.initExConfirmStatus(checkItem.getExConfirmStatus());
			transfer.setExConfirmStatus(exConfirmStatus);
			checkItemInBuyerTransferList.add(transfer);
		}
		return checkItemInBuyerTransferList;
	}

	/**
	 * 更新对账单
	 * @param check	对账单对象
	 */
	public void saveCheck(CheckEntity check) {
		baseDao.update(check);
	}

	/**
	 * 通过code查询发票
	 * @param code
	 * @return
	 */
	public List<CheckInvoiceEntity> findInvoiceListByCode(String code) {
		return checkInvoiceDao.findByCode(code);
	}
	
	/**
	 * 获取默认对账日
	 * @return
	 */
	public TimetaskSettingEntity getDefauleDateSetting(){
		return timetaskSettingDao.findOne(-1L);
	}

	/**
	 * 获取所有对账日
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<TimetaskSettingEntity> getDateSettingList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<TimetaskSettingEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), TimetaskSettingEntity.class);
		Page<TimetaskSettingEntity> timetaskSettingEntity = timetaskSettingDao.findAll(spec,pagin);
		return timetaskSettingEntity;
	}
	
	public Map<String, Object> add(TimetaskSettingEntity dateSetting) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		map.put("msg", "设置成功");
		//id为0执行新增操作,id不为0  执行更新操作
		OrganizationEntity org = orgDao.findOne(dateSetting.getVendorId());
		if(dateSetting.getId()!=0){
			TimetaskSettingEntity timetaskSetting=timetaskSettingDao.findOne(dateSetting.getId());
			if(timetaskSetting.getVendorId().equals(dateSetting.getVendorId())){
				dateSetting.setVendor(org);
				dateSetting.setTaskName("createCheckTask");
				timetaskSettingDao.save(dateSetting);
				return map;
			}
		}
		List<TimetaskSettingEntity>  timetaskSettingLists=timetaskSettingDao.findByVendorId(dateSetting.getVendorId());
		if(!CollectionUtils.isEmpty(timetaskSettingLists)){
			map.put("success", false);
			map.put("msg", "该供应商已经设置，不需要重复设置");
			return map;
		}
		dateSetting.setVendor(org);
		dateSetting.setTaskName("createCheckTask");
		timetaskSettingDao.save(dateSetting);
		return map;
	}
	
	
	/**
	 * 保存默认设置
	 * @param dateSetting
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateDefaultDateSetting(TimetaskSettingEntity pageDateSetting) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		map.put("success", true);
		map.put("msg", "设置成功");
		if(StringUtils.isEmpty(pageDateSetting.getDay())){
			map.put("success", false);
			map.put("msg", "日期不能为空！");
		}else{
			try{
				if(Double.parseDouble(pageDateSetting.getDay())>28){
					map.put("success", false);
					map.put("msg", "日期不能大于28！");
				}
			}catch(Exception e){
				map.put("success", false);
				map.put("msg", "日期必须为数字！");
			}
		}
		TimetaskSettingEntity setting=timetaskSettingDao.findOne(-1L);
		setting.setDay(pageDateSetting.getDay());
		timetaskSettingDao.save(setting);
		return map;
	}
	
	public String releaseDateSetting( List<TimetaskSettingEntity> dateSettings) {
		for(TimetaskSettingEntity dateSetting:dateSettings)
		{
			timetaskSettingDao.updateTimetaskSettingabolished(0, DateUtil.getCurrentTimestamp(), dateSetting.getId());
		}
		return "1";
	}
	
	public String delsDateSetting( List<TimetaskSettingEntity> dateSettings) {
		for(TimetaskSettingEntity dateSetting:dateSettings)
		{
			timetaskSettingDao.updateTimetaskSettingabolished(1, DateUtil.getCurrentTimestamp(), dateSetting.getId());
		}
		return "1";
	}
	public String deleteDateSetting( List<TimetaskSettingEntity> dateSettings) {
		timetaskSettingDao.delete(dateSettings);
		return "1";
	}
	
	public String getDateSetting(Long id) {
		TimetaskSettingEntity timetaskSetting=timetaskSettingDao.findOne(id);
		String data="";
		data=timetaskSetting.getId()+",";
		data=data+timetaskSetting.getVendorId()+",";
		data=data+timetaskSetting.getVendor().getName()+",";
		data=data+timetaskSetting.getDay()+",";
		data=data+timetaskSetting.getAbolished()+",";
		return data;
	}
	
	
	//add by yao.jin 2017.02.10  根据对采购组织的id获得对账单明细的Id
	public List<Long> getCheckItemIds(List<Long> buyerIds){
		List<Long> itemIds = checkItemDao.findCheckItemIdsByBuyerId(buyerIds);
		return itemIds;
	}
	
	//end add
	
	
	
}
