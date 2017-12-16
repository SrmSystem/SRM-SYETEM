package com.qeweb.scm.purchasemodule.task.service;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.qeweb.sap.CommonSapFw;
import com.qeweb.sap.PurchaseOrderSAP;
import com.qeweb.sap.vo.OrderPriceVo;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.TimetaskSettingEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.TimetaskSettingDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.check.entity.CheckEntity;
import com.qeweb.scm.check.entity.CheckItemEntity;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.repository.CheckDao;
import com.qeweb.scm.check.repository.CheckItemDao;
import com.qeweb.scm.check.repository.NoCheckItemsDao;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
@Service
@Transactional(rollbackOn=Exception.class)
public class CreateCheckService{
	
	@Autowired
	private OrganizationDao orgDAO;
	
	@Autowired
	private CheckDao checkDAO;
	
	@Autowired
	private CheckItemDao checkItemDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private ReceiveItemDao receiveItemDao;
	
	@Autowired
	private PurchaseOrderItemDao purItemDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TimetaskSettingDao timetaskSettingDao;
	
	@Autowired
	private NoCheckItemsDao noCheckItemsDao;
	
	public static final String TASK_NAME="createCheckTask";
	public static final Long TASK_COMMON_ID=-1L;
	
	/**
	 * 根据配置表获取需要对账的数据
	 * @return
	 */
	public List<?> findNeedCheckVendor(StringBuffer log){
		List<Long> notInVendorIds = null;
		List<Long> inVendorIds = null;
		boolean isCommonCreateDay=false;
		//1、获取配置表中今天是否是一般供应商的对账日
		TimetaskSettingEntity commonSetting=timetaskSettingDao.findByTaskNameAndAbolishedAndIdAndDay(TASK_NAME,0,TASK_COMMON_ID,DateUtil.getCurrentDate()+"");
		if(null!=commonSetting){//2、是一般对账日，则获取所有不是今天对账的特殊供应商ID
			isCommonCreateDay=true;
			notInVendorIds=timetaskSettingDao.findNotCurrCheckSpecialVendors(TASK_NAME,DateUtil.getCurrentDate()+"",0);
		}else{//2、不是一般对账日，则获取是今天对账的特殊供应商ID
			inVendorIds=timetaskSettingDao.findCurrCheckSpecialVendors(TASK_NAME,DateUtil.getCurrentDate()+"",0);
		}
		
		//3、组装sql
		Timestamp t0 = DateUtil.stringToTimestamp("2015-01-01 00:00:00", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		Timestamp t1 = DateUtil.stringToTimestamp(DateUtil.getCurrentYear() +"-"+ DateUtil.getCurrentMonth() + "-15 23:59:59", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		String sql = findCheckSql(t0,t1,isCommonCreateDay,notInVendorIds,inVendorIds);
		
		//4、执行sql
		if(null!=sql){
			log.append("\r\n").append(sql).append("\r\n");
		return generialDao.queryBySql_map(sql);
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public void create(ILogger logger) throws Exception { 
		FileWriter fw = CommonSapFw.initFw(CreateCheckService.class);
		StringBuffer log=new StringBuffer();
		try{
	    log.append("定时任务：创建对账单开始--"+DateUtil.getCurrentDate());
		Timestamp t0 = DateUtil.stringToTimestamp("2015-01-01 00:00:00", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		Timestamp t1 = DateUtil.stringToTimestamp(DateUtil.getCurrentYear() +"-"+ DateUtil.getCurrentMonth() + "-15 23:59:59", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		log.append("时间："+t0+"："+t1);
		List<?> list =findNeedCheckVendor(log);
		if(null!=list && list.size()>0){
							
							List<CheckItemEntity> checkItemList = new ArrayList<CheckItemEntity>();
							CheckEntity ce = null;
							//1、循环保存所有供应商的对账单
							for(int i = 0; i < list.size(); i++){  
								Map map = (Map) list.get(i);
								Long buyerId = ((BigDecimal) map.get("BUYER_ID")).longValue();
								Long vendorId = ((BigDecimal) map.get("VENDOR_ID")).longValue();
								log.append("BUYER_ID:"+buyerId+",VENDOR_ID:"+vendorId);
								
								List<ReceiveItemEntity> recItems = Lists.newArrayList();
								List<PurchaseOrderItemEntity> orderItems = Lists.newArrayList();
								ReceiveItemEntity  receiveItem = null;
								PurchaseOrderItemEntity purchaseOrderItem = null;
								
								List<NoCheckItemsEntity> items = noCheckItemsDao.findItems(t0, t1, buyerId, vendorId);
								
								
								if(items.size()>0){
									for(NoCheckItemsEntity noCheckItems: items){
										if(noCheckItems.getDataType()==PurchaseConstans.CHECK_ITEM_SOURCE_REV){
											receiveItem = new ReceiveItemEntity();
											receiveItem.setViewNoCheckItemId(noCheckItems.getBuyerId());
											receiveItem.setOrderCode(noCheckItems.getOrderCode());
											receiveItem.setItemNo(noCheckItems.getItemNo());
											recItems.add(receiveItem);
										}else{
											purchaseOrderItem = new PurchaseOrderItemEntity();
											purchaseOrderItem.setViewNoCheckItemId(noCheckItems.getBuyerId());
											purchaseOrderItem.setOrderCode(noCheckItems.getOrderCode());
											purchaseOrderItem.setItemNo(noCheckItems.getItemNo());
											orderItems.add(purchaseOrderItem);
										}
										noCheckItems.setState("1");
										noCheckItemsDao.save(noCheckItems);
									}
								}else{
									items = Lists.newArrayList();
								}
								//List<ReceiveItemEntity> recItems = receiveItemDao.findRecItems(t0, t1, buyerId, vendorId);
								//List<PurchaseOrderItemEntity> orderItems=purItemDao.findReturnOrderItems(t0, t1, buyerId, vendorId);
								if((null==recItems|| recItems.size()<1) && (null==orderItems || orderItems.size()<1) ){
									log.append("无收货和退货，忽略");
									continue;
								}
								
								ce = getCheckEntity(t1, vendorId,buyerId);
								//组装对账单数据
								compoCheckItem(ce,vendorId,buyerId,t1,checkItemList,recItems,orderItems,log);
								
								
								if(CollectionUtils.isEmpty(checkItemList))
									continue;  
								
								log.append(">>>>开始组装价格VO，准备调用sap接口");
								
								boolean flag=findPriceFromSap(checkItemList,ce,log,logger);
								if(flag){
									checkDAO.save(ce);
									checkItemDao.save(checkItemList);
									if(null!=recItems && recItems.size()>0){
										receiveItemDao.save(recItems);
									}
									
									if(null!=orderItems && orderItems.size()>0){
										purItemDao.save(orderItems);
									}
									//发送邮件
									sendMail(checkItemList);
								}else{
									continue;
								}
							
							}
							
		}
		}catch(Exception e) {  
            e.printStackTrace();  
        	log.append(e.getMessage());
        }finally{
        	log.append("生成对账单结束").append(DateUtil.getCurrentDate());
        	fw.write(log.toString());
			fw.flush();
			fw.close();
        }
	}
	
	/**
	 * 组装对账单数据
	 * @author chao.gu
	 * @param ce 对账主单
	 * @param vendorId
	 * @param buyerId
	 * @param t1 当前时间
	 * @param checkItemList 对账单明细
	 * @param recItems 收货
	 * @param orderItems 退货
	 */
	public void compoCheckItem(CheckEntity ce,Long vendorId,Long buyerId,Timestamp t1,List<CheckItemEntity> checkItemList,List<ReceiveItemEntity> recItems,List<PurchaseOrderItemEntity> orderItems,StringBuffer log){
		Timestamp createTime=DateUtil.getCurrentTimestamp();
		if(ce.getId() == 0l) {
			SerialNumberService serialNumberService = SpringContextUtils.getBean("serialNumberService");
			ce.setCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.CHECK));
			OrganizationEntity buyer = orgDAO.findOne(buyerId); 
			OrganizationEntity vendor = orgDAO.findOne(vendorId);
			ce.setBuyer(buyer);
			ce.setVendor(vendor);
			ce.setAbolished(0);
			ce.setCreateTime(createTime);
			ce.setCol3(DateUtil.dateToString(t1)); 
		}
		ce.setbConfirmStatus(0);
		ce.setBillStatus(0);
		ce.setCloseStatus(0);
		ce.setCreateUserId(-1l);
		ce.setCreateUserName("系统");
		ce.setExDealStatus(0);
		ce.setExConfirmStatus(0);
		ce.setExStatus(0);
		ce.setPayStatus(0);
		ce.setLastUpdateTime(createTime);
		ce.setMonth(DateUtil.getCurrentMonth());
		//modify by chao.gu
		ce.setYear(DateUtil.getDateYear(t1));
		//modify end
		ce.setPublishStatus(0);
		ce.setCheckAmount(0d);
		ce.setvConfirmStatus(0);
		ce.setReviewStatus(0);
		ce.setIsOutData(StatusConstant.STATUS_YES);
		
		//保存对账单明细
		checkItemList.clear();
		CheckItemEntity cie = null;
		//2、保存收货的对账数据
		if(null!=recItems && recItems.size()>0){
			log.append("收货数据一共："+recItems.size());
			for(ReceiveItemEntity recItem:recItems){
				//log.append("收货单号："+recItem.getReceive().getReceiveCode()+",收货数量"+recItem.getReceiveQty()+",收货明细ID:"+recItem.getId());
				cie = getCheckItemEntity(ce, recItem.getViewNoCheckItemId(),PurchaseConstans.CHECK_ITEM_SOURCE_REV);
				
				cie.setAbolished(0);
				cie.setCheck(ce);
				cie.setCreateTime(createTime);
				cie.setCreateUserId(-1l);
				cie.setCreateUserName("系统");
				cie.setExConfirmStatus(0);
				cie.setExDealStatus(0);
				cie.setExStatus(0);
				cie.setLastUpdateTime(createTime);
				cie.setRecItem(recItem);
				//cie.setMaterial(recItem.getOrderItem().getMaterial());
				recItem.setBalanceStatus(PurchaseConstans.BALANCE_STATUS_YES);	//已对账
				cie.setIsOutData(StatusConstant.STATUS_YES);
				cie.setSource(PurchaseConstans.CHECK_ITEM_SOURCE_REV);//来源于收货
				cie.setOrderCode(recItem.getOrderCode());
				cie.setItemNo(recItem.getItemNo());
				cie.setViewNoCheckItemId(recItem.getViewNoCheckItemId());
				cie.setBuyerCheckPrice(recItem.getBuyerCheckPrice());
				checkItemList.add(cie);
			}
		}
		//3、保存退货的对账数据
		if(null!=orderItems && orderItems.size()>0){
			log.append("退货数据一共："+orderItems.size());
			for(PurchaseOrderItemEntity retItem:orderItems){
				//log.append("退货单号："+retItem.getOrder().getOrderCode()+",退货数量"+retItem.getOrderQty()+",退货明细ID:"+retItem.getId());
				cie = getCheckItemEntity(ce, retItem.getId(),PurchaseConstans.CHECK_ITEM_SOURCE_RET);
				cie.setAbolished(0);
				cie.setCheck(ce);
				cie.setCreateTime(createTime);
				cie.setCreateUserId(-1l);
				cie.setCreateUserName("系统");
				cie.setExConfirmStatus(0);
				cie.setExDealStatus(0);
				cie.setExStatus(0);
				cie.setLastUpdateTime(createTime);
				cie.setRetItem(retItem);
				cie.setMaterial(retItem.getMaterial());
				retItem.setBalanceStatus(PurchaseConstans.BALANCE_STATUS_YES);	//已对账
				cie.setIsOutData(StatusConstant.STATUS_YES);
				cie.setSource(PurchaseConstans.CHECK_ITEM_SOURCE_RET);//来源于退货
				cie.setViewNoCheckItemId(retItem.getViewNoCheckItemId());
				cie.setOrderCode(retItem.getOrderCode());
				cie.setItemNo(retItem.getItemNo());
				cie.setCheckPrice(retItem.getDcol1());
				cie.setBuyerCheckPrice(retItem.getDcol1());
				checkItemList.add(cie);
			}
		}
	}
	
	/**
	 * 调用价格接口,返回总价
	 * @param checkItemList
	 * @param logger
	 * @return
	 */
	public boolean findPriceFromSap(List<CheckItemEntity>  checkItemList,CheckEntity ce,StringBuffer log,ILogger logger){
		boolean flag=true;
		Double totalPrice = 0D; 
		Map<String,OrderPriceVo> map;
		ReceiveItemEntity recItem;
		PurchaseOrderItemEntity retItem;
		if(null!=checkItemList && checkItemList.size()>0){
		List<OrderPriceVo> inputs=new ArrayList<OrderPriceVo>();
		OrderPriceVo vo;
		for (CheckItemEntity item : checkItemList) {
			vo=new OrderPriceVo();
		    vo.setEBELN(item.getOrderCode());//采购凭证号 
			vo.setEBELP(item.getItemNo());
			inputs.add(vo);
		} 
		
		try {
			log.append("调用价格接口开始："+inputs.size());
			List<OrderPriceVo> orderPriceVos =PurchaseOrderSAP.getOrderItemPrice(inputs,logger);
			log.append("调用价格接口结束："+orderPriceVos.size());
			if(null!=orderPriceVos && orderPriceVos.size()>0){
				map=new HashMap<String, OrderPriceVo>();
				for (OrderPriceVo orderPriceVo : orderPriceVos) {
					map.put(orderPriceVo.getEBELN()+"_"+orderPriceVo.getEBELP(), orderPriceVo);
				}
			
				//给checkItem赋值
				if(null!=map && map.size()>0){
					for (CheckItemEntity item : checkItemList) {
						vo=map.get(item.getOrderCode()+"_"+item.getItemNo());
						if(null==vo || null==vo.getZZSDJ()){
							flag=false;
							log.append("\r\n订单号：").append(item.getOrderCode()).append(",行号：").append(item.getItemNo()).append("没有在sap中查到价格");
							break;
						}
						item.setCheckPrice(vo.getZZSDJ());//净价
						item.setZhd(vo.getZHD());//含税
						if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==item.getSource()){//收货加
							recItem=item.getRecItem();
							totalPrice = BigDecimalUtil.add(totalPrice, BigDecimalUtil.mul(vo.getZZSDJ() == null ? 0d : vo.getZZSDJ(), 
									recItem.getReceiveQty() == null ? 0d :recItem.getReceiveQty()));
						}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==item.getSource()){//退货减
							retItem=item.getRetItem();
						    totalPrice= BigDecimalUtil.sub(totalPrice, BigDecimalUtil.mul(vo.getZZSDJ() == null ? 0d : vo.getZZSDJ(), 
							retItem.getOrderQty() == null ? 0d : retItem.getOrderQty()));
						}
					}
				}else{
					flag=false;
				}
			}else{
				flag=false;
			}
		} catch (Exception e) {
			flag=false;
			log.append("调用价格接口失败："+e.getMessage());
			e.printStackTrace();
		}
		}
		ce.setCheckAmount(totalPrice);
		return flag;
	}

	/**
	 * 组装邮件格式
	 * @param checkItemList
	 */
	public void sendMail(List<CheckItemEntity> checkItemList ){
		Map<String, String> userMap = new HashMap<String, String>();
		UserEntity user;
		if(CommonUtil.isNotNullCollection(checkItemList)){								//如果不为空，开始初始化邮件信息
			for (CheckItemEntity checkItem : checkItemList) {
				CheckEntity check = checkItem.getCheck();								//对账主单
				OrganizationEntity vendor = check.getVendor();							//获取供应商
				ReceiveItemEntity recItem = checkItem.getRecItem();
				PurchaseOrderItemEntity retItem=checkItem.getRetItem();//退货
				if(CommonUtil.isNotNullAndNotEmpty(recItem)){							//收货明细不为空
					PurchaseOrderItemEntity orderItem = recItem.getOrderItem();
					if(CommonUtil.isNotNullAndNotEmpty(orderItem)){						//订单明细不为空
						user=orderItem.getPublishUser();
						if(CommonUtil.isNotNullAndNotEmpty(user)){
								String email = user.getEmail();
								if(CommonUtil.isNotNullAndNotEmpty(email)){				//存在邮箱，开始初始化邮件内容信息
									String key = user.getLoginName() + ":" + email;
									String msg = "";
									if(userMap.containsKey(key)){						//存在就累加邮件内容信息
										msg = userMap.get(key);							//获取既存的邮件内容信息
										msg += "对账单号: " + check.getCode() + ",  供应商代码: " + vendor.getCode() + 
											   ",  供应商名称: " + vendor.getName() + ",  年: " + check.getYear() + 
											   ",  月: " + check.getMonth() + "\n";
									}else{												//不存在就初始化邮件内容信息
										msg = "对账单号: " + check.getCode() + ",  供应商代码: " + vendor.getCode() + 
												   ",  供应商名称: " + vendor.getName() + ",  年: " + check.getYear() + 
												   ",  月: " + check.getMonth() + "\n";
									}
									userMap.put(key, msg);
								}
						}
					}
				}
				
				if(CommonUtil.isNotNullAndNotEmpty(retItem)){
					user=retItem.getPublishUser();
					if(CommonUtil.isNotNullAndNotEmpty(user)){
							String email = user.getEmail();
							if(CommonUtil.isNotNullAndNotEmpty(email)){				//存在邮箱，开始初始化邮件内容信息
								String key = user.getLoginName() + ":" + email;
								String msg = "";
								if(userMap.containsKey(key)){						//存在就累加邮件内容信息
									msg = userMap.get(key);							//获取既存的邮件内容信息
									msg += "对账单号: " + check.getCode() + ",  供应商代码: " + vendor.getCode() + 
										   ",  供应商名称: " + vendor.getName() + ",  年: " + check.getYear() + 
										   ",  月: " + check.getMonth() + "\n";
								}else{												//不存在就初始化邮件内容信息
									msg = "对账单号: " + check.getCode() + ",  供应商代码: " + vendor.getCode() + 
											   ",  供应商名称: " + vendor.getName() + ",  年: " + check.getYear() + 
											   ",  月: " + check.getMonth() + "\n";
								}
								userMap.put(key, msg);
							}
					}
				}
			}
			
			senMailToBuyer(userMap);
		}
		
	
	}
	
	/**
	 * 调用邮件服务
	 * @param userMap
	 */
	public void senMailToBuyer(final Map<String, String> userMap){
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				String title = "对账单生成提醒";
				String msg = "对账单信息已生成，还请登录网站查看";
				for (Map.Entry<String, String> entry : userMap.entrySet()) {
					String key = entry.getKey();		//获取key = 采购商名称 + ":" + 采购商email
					String[] keys = key.split(":");
					if(CommonUtil.isNotNullAndNotEmpty(keys) && keys.length == 2){
						String toEmail = keys[1];
						String toPerson = keys[0];
						CommonUtil.sendMail(toEmail, toPerson, title, msg);//发送邮件
					}
				}
			}
		});
	}
	
	/**
	 * 获取对账期内对账单
	 * @param t1
	 * @param vendorId
	 * @return
	 */
	private CheckEntity getCheckEntity(Timestamp t1, Long vendorId,Long buyerId) {
		List<CheckEntity> list = checkDAO.findCheckEntity(vendorId, DateUtil.dateToString(t1),buyerId); 
		if(CollectionUtils.isEmpty(list))
			return new CheckEntity();
		
		return list.get(0);
	}
	
	private CheckItemEntity getCheckItemEntity(CheckEntity ck, Long sourceId,int source) {
		if(ck.getId() == 0l)
			return new CheckItemEntity();
		
		List<CheckItemEntity> list = null;
	
		if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==source){	//1、收货
			list = checkItemDao.findCheckItemByRec(ck.getId(), sourceId,source);
		}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==source){	//2、退货
			list = checkItemDao.findCheckItemByRet(ck.getId(), sourceId,source);
		}
		if(CollectionUtils.isEmpty(list))
			return new CheckItemEntity();
		
		return list.get(0); 
	}
	
	
	
	/**
	 * 获取要对账的采购组织和供应商
	 * @param t0
	 * @param t1
	 * @param isCommonCreateDay
	 * @param notInVendorIds
	 * @param inVendorIds
	 * @return
	 */
	public String findCheckSql(Timestamp t0,Timestamp t1,boolean isCommonCreateDay,List<Long> notInVendorIds,List<Long> inVendorIds){
		//1、没有任何对账单需生成
		if(!isCommonCreateDay && (null==inVendorIds || inVendorIds.size()==0)){
			return null;
		}
		//2、需生成对账单
		StringBuffer notInVendorIdsStr = null;
		StringBuffer inVendorIdsStr = null;
		if(isCommonCreateDay){
			if(null!=notInVendorIds && notInVendorIds.size()>0){
				notInVendorIdsStr=new StringBuffer();
				for (Long long1 : notInVendorIds) {
					notInVendorIdsStr.append(long1).append(",");
				}
			}
		}else if(null!=inVendorIds && inVendorIds.size()>0){
			inVendorIdsStr=new StringBuffer();
			for (Long long1 : inVendorIds) {
				inVendorIdsStr.append(long1).append(",");
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" BUYER_ID,");
		sql.append(" VENDOR_ID");
		sql.append(" FROM");
		sql.append(" VIEW_NO_CHECK_ITEM");
		sql.append(" WHERE");
		sql.append(" CREATE_TIME >= TO_TIMESTAMP (");
		sql.append(" '").append(t0).append("',");
		sql.append(" 'yyyy-MM-dd hh24:mi:ss.ff'");
		sql.append(" )");
		sql.append(" AND CREATE_TIME < TO_TIMESTAMP (");
		sql.append(" '").append(t1).append("',");
		sql.append(" 'yyyy-MM-dd hh24:mi:ss.ff'");
		sql.append(" )");
		sql.append(" AND type = 0 ");
		sql.append(" AND state = 0 ");
		if(null!=notInVendorIdsStr){
			sql.append(" AND VENDOR_ID NOT IN (").append(notInVendorIdsStr.substring(0, notInVendorIdsStr.length()-1)).append(")");	
		}else if(null!=inVendorIdsStr){
			sql.append(" AND VENDOR_ID  IN (").append(inVendorIdsStr.substring(0, inVendorIdsStr.length()-1)).append(")");	
		}
		sql.append(" GROUP BY");
		sql.append(" BUYER_ID,");
		sql.append(" VENDOR_ID");
		return sql.toString();
	
	}
	
	
	/**
	 * 将未对账管理中数据，整理
	 * @param datas
	 * @return
	 */
	public Map<String, List<NoCheckItemsEntity>> compoNoCheckDatas(List<NoCheckItemsEntity> datas,StringBuffer log){
		Map<String, List<NoCheckItemsEntity>> map=new HashMap<String,List<NoCheckItemsEntity>>();
		List<NoCheckItemsEntity> list;
		for (NoCheckItemsEntity noCheckItem : datas) {
			String key=noCheckItem.getBuyerId()+"_"+noCheckItem.getVendorId();
			log.append("map中的key(buyerId_vedorId):").append(key).append("\r\n");
			if(map.containsKey(key)){
				list=map.get(key);
			}else{
				list=new ArrayList<NoCheckItemsEntity>();
			}
			list.add(noCheckItem);
			map.put(key, list);
		}
		return map;
		
	}
	
	/**
	 * 未对账管理中生成对账单
	 * @param datas
	 * @param str 
	 * @return
	 * @throws Exception 
	 */
	public boolean createNoCheck(List<NoCheckItemsEntity> datas,StringBuffer str,ILogger logger) throws Exception{
		FileWriter fw = CommonSapFw.initFw(CreateCheckService.class);
		StringBuffer log=new StringBuffer();
		boolean flag=true;
		
		List<NoCheckItemsEntity> list;
		List<ReceiveItemEntity> recItemList;
		List<PurchaseOrderItemEntity> retItemList;
		try{
		Map<String, List<NoCheckItemsEntity>> map=compoNoCheckDatas(datas,log);
		Timestamp t1=DateUtil.getCurrentTimestamp();
		if(null!=map && map.size()>0){
			for (String key : map.keySet()) {
				list=map.get(key);
				log.append("key:").append(key).append(">>>>>>>>>>>>").append("\r\n");;
				recItemList=new ArrayList<ReceiveItemEntity>();
				retItemList=new ArrayList<PurchaseOrderItemEntity>();
				List<CheckItemEntity> checkItemList = new ArrayList<CheckItemEntity>();
				CheckEntity ce = null;
				//将收货和退货数据整理
				for (NoCheckItemsEntity noCheckItem : list) {
					
					if(PurchaseConstans.CHECK_ITEM_SOURCE_REV==noCheckItem.getDataType().intValue()){
						ReceiveItemEntity rec= new  ReceiveItemEntity();
						rec.setViewNoCheckItemId(noCheckItem.getId());
						rec.setOrderCode(noCheckItem.getOrderCode());
						rec.setItemNo(noCheckItem.getItemNo());
						rec.setBuyerCheckPrice(noCheckItem.getPrice());
						recItemList.add(rec);
						log.append("收货单：");
					}else if(PurchaseConstans.CHECK_ITEM_SOURCE_RET==noCheckItem.getDataType().intValue()){
						PurchaseOrderItemEntity ret = new PurchaseOrderItemEntity();
						ret.setViewNoCheckItemId(noCheckItem.getId());
						ret.setOrderCode(noCheckItem.getOrderCode());
						ret.setItemNo(noCheckItem.getItemNo());
						ret.setDcol1(noCheckItem.getPrice());
						retItemList.add(ret);
						log.append("退货单：");
					}
					noCheckItem.setState("1");
					noCheckItemsDao.save(noCheckItem);
				}
				log.append(">>>>>>>>>>").append("\r\n");
				if((null==retItemList|| retItemList.size()<1) && (null==recItemList || recItemList.size()<1) ){
					str.append("无收货和退货数据，忽略");
					log.append("无收货和退货数据，忽略\r\n");
					continue;
				}
				
				String []arr=key.split("_");
				ce = getCheckEntity(t1, Long.valueOf(arr[1]),Long.valueOf(arr[0]));
				//组装对账单数据
				compoCheckItem(ce,Long.valueOf(arr[1]),Long.valueOf(arr[0]),t1,checkItemList,recItemList,retItemList,log);
				
				
				if(CollectionUtils.isEmpty(checkItemList)){
					log.append("checkItemList").append("是空，忽略");
					continue; 
				}else{
					log.append("checkItemList条数：").append(checkItemList.size());
				}
					
				
				//boolean sapFlag=findPriceFromSap(checkItemList,ce,log,logger);
				/*if(sapFlag){
					if(ce.getId()>0){
						  BOHelper.setBOPublicFields_update(ce);
					}else{
						  BOHelper.setBOPublicFields_insert(ce);
					}*/
					checkDAO.save(ce);
					checkItemDao.save(checkItemList);
					if(null!=recItemList && recItemList.size()>0){
						receiveItemDao.save(recItemList);
					}
					
					if(null!=retItemList && retItemList.size()>0){
						purItemDao.save(retItemList);
					}
					//发送邮件
					sendMail(checkItemList);
					str.append("对账单：").append(ce.getCode()).append(",").append("采购商：").append(ce.getBuyer().getCode()).append(",").append(ce.getBuyer().getName()).append(",供应商：")
					.append(ce.getVendor().getCode()).append(",").append(ce.getVendor().getName()).append("生成对账单成功！\n");
			/*	}else{
					str.append("采购商：").append(ce.getBuyer().getCode()).append(",").append(ce.getBuyer().getName()).append(",供应商：")
					.append(ce.getVendor().getCode()).append(",").append(ce.getVendor().getName()).append("获取sap价格失败，忽略不生成对账单");
					continue;
				}*/
				
			}
		}else{
			flag=false;
			str.append("无数据，请重新查询数据生成对账单");
		}
		}catch(Exception e) { 
			str.append("SAP连接失败,");
			    flag=false;
	            e.printStackTrace();  
	        	log.append(e.getMessage());
	        }finally{
	        	log.append("生成对账单结束日期：").append(DateUtil.getCurrentDate()).append("\r\n");
	        	log.append("----------以下为页面显示数据-----------------\r\n");
	        	log.append(str);
	        	fw.write(log.toString());
				fw.flush();
				fw.close();
	        }
		if(flag){
			str.append("创建对账单成功");
		}else{
			str.append("创建对账单失败");
		}
		return flag;
	}
	
	
	
}
